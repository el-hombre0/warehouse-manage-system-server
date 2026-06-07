# forecast_service.py
"""
Микросервис прогнозирования спроса на FastAPI + Kafka + SARIMA.
Поддерживает два источника данных и сохраняет прогнозы в AnalyticsDB.
"""

import asyncio
import json
import logging
from datetime import datetime, timedelta
from collections import defaultdict
from typing import Dict, List, Optional, Any, Tuple
from contextlib import asynccontextmanager
from uuid import uuid4

import pandas as pd
import numpy as np
from fastapi import FastAPI, HTTPException, BackgroundTasks, Query
from pydantic import BaseModel, Field
from aiokafka import AIOKafkaConsumer, AIOKafkaProducer
from sqlalchemy.ext.asyncio import create_async_engine, AsyncSession
from sqlalchemy.orm import sessionmaker, declarative_base, Mapped, mapped_column
from sqlalchemy import text, select, func, desc, and_, Index, String, Integer, Float, DateTime, Boolean, JSON
from statsmodels.tsa.statespace.sarimax import SARIMAX
import uvicorn

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# ==================== Pydantic модели для API ====================

class ForecastRequest(BaseModel):
    equipment_type: str
    horizon_days: int = Field(default=7, ge=1, le=30)
    use_database_history: bool = Field(default=False, description="Использовать БД для исторических данных")

class ForecastResponse(BaseModel):
    id: Optional[str] = None
    equipment_type: str
    forecast_horizon_days: int
    forecast: List[Dict[str, Any]]
    model_params: Dict[str, Any]
    data_source: str
    history_days_used: int
    timestamp: str
    confidence_intervals: Optional[Dict[str, List[Dict[str, float]]]] = None

class HealthResponse(BaseModel):
    status: str
    equipment_types: List[str]
    events_consumed_from_kafka: int
    records_loaded_from_db: Dict[str, int]
    forecasts_saved: int

class LoadHistoryRequest(BaseModel):
    equipment_type: str
    days_back: int = Field(default=90, ge=7, le=365)
    force: bool = False

class SaveForecastRequest(BaseModel):
    equipment_type: str
    source: str
    force: bool = False

class ForecastHistoryResponse(BaseModel):
    forecasts: List[ForecastResponse]
    total: int

# ==================== SQLAlchemy модели для AnalyticsDB ====================

Base = declarative_base()

class SavedForecast(Base):
    """Модель для сохранённых прогнозов в AnalyticsDB"""
    __tablename__ = "forecasts"

    id: Mapped[str] = mapped_column(String(36), primary_key=True, default=lambda: str(uuid4()))
    equipment_type: Mapped[str] = mapped_column(String(255), nullable=False)
    forecast_horizon_days: Mapped[int] = mapped_column(Integer, nullable=False)
    data_source: Mapped[str] = mapped_column(String(50), nullable=False)  # "kafka" или "db"
    history_days_used: Mapped[int] = mapped_column(Integer, nullable=False)

    # Прогноз в JSON формате: [{"date": "2026-06-03", "predicted_demand": 12.5}]
    forecast_data: Mapped[Dict] = mapped_column(JSON, nullable=False)

    # Параметры модели
    model_order: Mapped[str] = mapped_column(String(50))
    model_seasonal_order: Mapped[str] = mapped_column(String(50))
    model_aic: Mapped[float] = mapped_column(Float, nullable=True)
    model_bic: Mapped[float] = mapped_column(Float, nullable=True)

    # Доверительные интервалы
    confidence_intervals: Mapped[Optional[Dict]] = mapped_column(JSON, nullable=True)

    # Метаданные
    created_at: Mapped[datetime] = mapped_column(DateTime, default=datetime.now)
    valid_until: Mapped[Optional[datetime]] = mapped_column(DateTime, nullable=True)
    is_active: Mapped[bool] = mapped_column(Boolean, default=True)

    # Дополнительные метрики
    mae_backtest: Mapped[Optional[float]] = mapped_column(Float, nullable=True)
    smape_backtest: Mapped[Optional[float]] = mapped_column(Float, nullable=True)

    # Внешние ключи и связи
    trigger_event_id: Mapped[Optional[str]] = mapped_column(String(255), nullable=True)
    version: Mapped[int] = mapped_column(Integer, default=1)

    __table_args__ = (
        Index("idx_forecasts_equipment_type", "equipment_type"),
        Index("idx_forecasts_created_at", "created_at"),
        Index("idx_forecasts_data_source", "data_source"),
        Index("idx_forecasts_is_active", "is_active"),
        Index("idx_forecasts_equipment_active", "equipment_type", "is_active"),
    )

class ForecastAccuracyMetric(Base):
    """Модель для метрик точности прогнозов (для бэктестинга)"""
    __tablename__ = "forecast_accuracy_metrics"

    id: Mapped[str] = mapped_column(String(36), primary_key=True, default=lambda: str(uuid4()))
    forecast_id: Mapped[str] = mapped_column(String(36), nullable=False)
    equipment_type: Mapped[str] = mapped_column(String(255), nullable=False)
    evaluation_date: Mapped[datetime] = mapped_column(DateTime, default=datetime.now)
    actual_demand: Mapped[Dict] = mapped_column(JSON)  # фактические значения за период прогноза
    mae: Mapped[float] = mapped_column(Float)
    rmse: Mapped[float] = mapped_column(Float)
    smape: Mapped[float] = mapped_column(Float)
    horizon_day: Mapped[int] = mapped_column(Integer)  # день горизонта, для которого считана метрика

    __table_args__ = (
        Index("idx_metrics_forecast_id", "forecast_id"),
        Index("idx_metrics_equipment_type", "equipment_type"),
    )

# ==================== Конфигурация ====================

class Config:
    KAFKA_BOOTSTRAP_SERVERS = "localhost:9092"
    POSTGRES_DSN = "postgresql+asyncpg://user:password@localhost:5432/logging_db"
    ANALYTICS_DSN = "postgresql+asyncpg://user:password@localhost:5432/analytics_db"

    # Топики Kafka
    KAFKA_TOPICS = ["orders.created", "orders.cancelled", "items.returned"]

    # Параметры SARIMA
    MIN_HISTORY_DAYS = 60
    DEFAULT_FORECAST_HORIZON = 7
    SARIMA_ORDER = (1, 1, 1)
    SARIMA_SEASONAL_ORDER = (1, 1, 1, 7)

    # Настройки сохранения
    FORECAST_VALIDITY_HOURS = 24  # прогноз считается актуальным 24 часа
    AUTO_SAVE_FORECASTS = True    # автоматически сохранять прогнозы в БД

# ==================== Сервис прогнозирования ====================

class DemandForecastService:
    """
    Сервис прогнозирования с сохранением прогнозов в AnalyticsDB.
    """

    def __init__(self):
        # Kafka компоненты
        self.consumer: Optional[AIOKafkaConsumer] = None
        self.producer: Optional[AIOKafkaProducer] = None

        # PostgreSQL компоненты (логи)
        self.async_engine = None
        self.async_session_maker = None

        # AnalyticsDB компоненты (прогнозы)
        self.analytics_engine = None
        self.analytics_session_maker = None

        # Хранилище данных
        self.daily_demand: Dict[str, Dict[str, pd.Series]] = defaultdict(lambda: {"kafka": None, "db": None})

        # Обученные модели и кэш прогнозов
        self.models: Dict[str, Any] = {}
        self.last_forecasts: Dict[str, Dict[str, ForecastResponse]] = defaultdict(dict)

        # Метаданные
        self.kafka_events_consumed = 0
        self.db_load_stats: Dict[str, int] = {}
        self.forecasts_saved_count = 0

    # ==================== Инициализация ====================

    async def start(self):
        """Запуск подключений"""
        # Kafka
        self.consumer = AIOKafkaConsumer(
            *Config.KAFKA_TOPICS,
            bootstrap_servers=Config.KAFKA_BOOTSTRAP_SERVERS,
            group_id="forecast_service_group_v3",
            auto_offset_reset="earliest",
            enable_auto_commit=True,
            value_deserializer=lambda m: json.loads(m.decode("utf-8"))
        )
        self.producer = AIOKafkaProducer(
            bootstrap_servers=Config.KAFKA_BOOTSTRAP_SERVERS,
            value_serializer=lambda v: json.dumps(v, default=str).encode("utf-8")
        )
        await self.consumer.start()
        await self.producer.start()

        # PostgreSQL (логи)
        self.async_engine = create_async_engine(Config.POSTGRES_DSN, echo=False)
        self.async_session_maker = sessionmaker(
            self.async_engine, class_=AsyncSession, expire_on_commit=False
        )

        # AnalyticsDB (прогнозы)
        self.analytics_engine = create_async_engine(Config.ANALYTICS_DSN, echo=False)
        self.analytics_session_maker = sessionmaker(
            self.analytics_engine, class_=AsyncSession, expire_on_commit=False
        )

        # Создаём таблицы в AnalyticsDB
        async with self.analytics_engine.begin() as conn:
            await conn.run_sync(Base.metadata.create_all)

        logger.info("Connections established: Kafka + PostgreSQL(logging_db) + AnalyticsDB")

    async def stop(self):
        """Корректное завершение"""
        if self.consumer:
            await self.consumer.stop()
        if self.producer:
            await self.producer.stop()
        if self.async_engine:
            await self.async_engine.dispose()
        if self.analytics_engine:
            await self.analytics_engine.dispose()

    # ==================== Сохранение прогнозов в AnalyticsDB ====================

    async def save_forecast_to_db(self, forecast: ForecastResponse, trigger_event_id: str = None) -> str:
        """
        Сохраняет прогноз в аналитическую базу данных.
        Возвращает ID сохранённой записи.
        """
        async with self.analytics_session_maker() as session:
            # Деактивируем старые прогнозы для того же типа оборудования и источника
            await session.execute(
                text("""
                     UPDATE forecasts
                     SET is_active = False
                     WHERE equipment_type = :equipment_type
                       AND data_source = :data_source
                       AND is_active = True
                     """),
                {"equipment_type": forecast.equipment_type, "data_source": forecast.data_source}
            )

            saved_forecast = SavedForecast(
                equipment_type=forecast.equipment_type,
                forecast_horizon_days=forecast.forecast_horizon_days,
                data_source=forecast.data_source,
                history_days_used=forecast.history_days_used,
                forecast_data=forecast.forecast,
                model_order=forecast.model_params.get("order"),
                model_seasonal_order=forecast.model_params.get("seasonal_order"),
                model_aic=forecast.model_params.get("aic"),
                model_bic=forecast.model_params.get("bic"),
                confidence_intervals=forecast.confidence_intervals,
                valid_until=datetime.now() + timedelta(hours=Config.FORECAST_VALIDITY_HOURS),
                trigger_event_id=trigger_event_id,
                version=1
            )

            session.add(saved_forecast)
            await session.commit()
            await session.refresh(saved_forecast)

            self.forecasts_saved_count += 1
            logger.info(f"Forecast saved to AnalyticsDB with id {saved_forecast.id} for {forecast.equipment_type}")

            return saved_forecast.id

    async def get_saved_forecasts(
            self,
            equipment_type: str = None,
            limit: int = 10,
            offset: int = 0,
            active_only: bool = True
    ) -> Tuple[List[SavedForecast], int]:
        """Получение сохранённых прогнозов из AnalyticsDB"""
        async with self.analytics_session_maker() as session:
            query = select(SavedForecast)
            count_query = select(func.count()).select_from(SavedForecast)

            if equipment_type:
                query = query.where(SavedForecast.equipment_type == equipment_type)
                count_query = count_query.where(SavedForecast.equipment_type == equipment_type)

            if active_only:
                query = query.where(SavedForecast.is_active == True)
                count_query = count_query.where(SavedForecast.is_active == True)

            query = query.order_by(desc(SavedForecast.created_at)).offset(offset).limit(limit)

            result = await session.execute(query)
            count_result = await session.execute(count_query)

            forecasts = result.scalars().all()
            total = count_result.scalar()

            return forecasts, total

    async def get_forecast_by_id(self, forecast_id: str) -> Optional[SavedForecast]:
        """Получение прогноза по ID"""
        async with self.analytics_session_maker() as session:
            result = await session.execute(
                select(SavedForecast).where(SavedForecast.id == forecast_id)
            )
            return result.scalar_one_or_none()

    async def save_accuracy_metric(
            self,
            forecast_id: str,
            equipment_type: str,
            actual_demand: Dict,
            mae: float,
            rmse: float,
            smape: float,
            horizon_day: int
    ):
        """Сохранение метрики точности прогноза после наступления фактических данных"""
        async with self.analytics_session_maker() as session:
            metric = ForecastAccuracyMetric(
                forecast_id=forecast_id,
                equipment_type=equipment_type,
                actual_demand=actual_demand,
                mae=mae,
                rmse=rmse,
                smape=smape,
                horizon_day=horizon_day
            )
            session.add(metric)
            await session.commit()
            logger.info(f"Accuracy metric saved for forecast {forecast_id}")

    # ==================== Загрузка из PostgreSQL (system_event_logs) ====================

    async def load_history_from_db(
            self,
            equipment_type: str,
            days_back: int = 90,
            force: bool = False
    ) -> Tuple[pd.Series, int]:
        """Загрузка исторических данных из таблицы system_event_logs"""
        async with self.async_session_maker() as session:
            query = text("""
                         WITH demand_events AS (
                             SELECT
                             DATE(business_timestamp) as demand_date,
                             COALESCE(SUM(quantity), 0) as total_demand
                         FROM system_event_logs
                         WHERE
                             entity_type = :equipment_type
                           AND event_type IN ('ORDER_CREATED', 'ORDER_CONFIRMED', 'RENTAL_STARTED')
                           AND business_timestamp >= NOW() - INTERVAL ':days_back days'
                           AND status = 'SUCCESS'
                           AND quantity > 0
                         GROUP BY DATE(business_timestamp)

                         UNION ALL

                         SELECT
                             DATE(business_timestamp) as demand_date,
                             COALESCE(-SUM(quantity), 0) as total_demand
                         FROM system_event_logs
                         WHERE
                             entity_type = :equipment_type
                           AND event_type IN ('ORDER_CANCELLED', 'RENTAL_CANCELLED')
                           AND business_timestamp >= NOW() - INTERVAL ':days_back days'
                           AND status = 'SUCCESS'
                           AND quantity > 0
                         GROUP BY DATE(business_timestamp)
                             )
                         SELECT
                             demand_date,
                             SUM(total_demand) as net_demand
                         FROM demand_events
                         GROUP BY demand_date
                         ORDER BY demand_date
                         """)

            result = await session.execute(
                query,
                {"equipment_type": equipment_type, "days_back": days_back}
            )
            rows = result.fetchall()

            if not rows:
                logger.warning(f"No data in DB for {equipment_type}")
                return pd.Series(dtype=float), 0

            dates = [row[0] for row in rows]
            demands = [float(row[1]) for row in rows]

            date_range = pd.date_range(start=min(dates), end=max(dates), freq="D")
            demand_series = pd.Series(0, index=date_range.date)
            for d, val in zip(dates, demands):
                if d in demand_series.index:
                    demand_series[d] = val

            self.daily_demand[equipment_type]["db"] = demand_series
            self.db_load_stats[equipment_type] = len(rows)

            logger.info(f"Loaded {len(rows)} demand records for {equipment_type} from DB")
            return demand_series, len(rows)

    # ==================== Обработка потоковых событий из Kafka ====================

    async def consume_loop(self):
        """Основной цикл потребления событий из Kafka"""
        async for msg in self.consumer:
            try:
                event = msg.value
                topic = msg.topic

                equipment_type = event.get("entity_type") or event.get("equipment_type")
                if not equipment_type:
                    equipment_type = event.get("product_category", "unknown")

                date_str = event.get("business_timestamp") or event.get("timestamp")
                if date_str:
                    date = datetime.fromisoformat(date_str).date()
                else:
                    date = datetime.now().date()

                quantity = event.get("quantity", 1)

                if topic in ["orders.created", "RENTAL_STARTED"]:
                    delta = +quantity
                elif topic in ["orders.cancelled", "ORDER_CANCELLED"]:
                    delta = -quantity
                else:
                    delta = 0

                if delta != 0:
                    self._add_kafka_demand_point(equipment_type, date, delta)
                    self.kafka_events_consumed += 1

                    # Обновляем прогноз и сохраняем в БД
                    await self._update_model_and_forecast(equipment_type, source="kafka", trigger_event_id=event.get("event_id"))

            except Exception as e:
                logger.error(f"Error processing Kafka event: {e}")

    def _add_kafka_demand_point(self, equipment_type: str, date, delta: int):
        """Обновление временного ряда из Kafka"""
        current_series = self.daily_demand[equipment_type].get("kafka")

        if current_series is None:
            new_series = pd.Series(0, index=pd.DatetimeIndex([date]).date)
            new_series[date] = delta
            self.daily_demand[equipment_type]["kafka"] = new_series
        else:
            if date in current_series.index:
                current_series[date] += delta
            else:
                new_index = sorted(list(current_series.index) + [date])
                new_series = current_series.reindex(new_index, fill_value=0)
                new_series[date] = delta
                self.daily_demand[equipment_type]["kafka"] = new_series

    # ==================== SARIMA модель и прогноз ====================

    def _prepare_series_for_model(self, series: pd.Series) -> pd.Series:
        """Подготовка временного ряда для SARIMA"""
        if series is None or len(series) < Config.MIN_HISTORY_DAYS:
            return None

        full_index = pd.date_range(start=series.index.min(), end=datetime.now().date(), freq="D")
        aligned_series = series.reindex(full_index.date, fill_value=0)
        aligned_series = aligned_series.clip(lower=0)

        return aligned_series

    async def _update_model_and_forecast(
            self,
            equipment_type: str,
            source: str = "kafka",
            horizon: int = None,
            trigger_event_id: str = None
    ):
        """Обучение SARIMA модели, генерация прогноза и сохранение в БД"""
        if horizon is None:
            horizon = Config.DEFAULT_FORECAST_HORIZON

        series = self.daily_demand[equipment_type].get(source)
        if series is None:
            logger.warning(f"No {source} data for {equipment_type}")
            return

        prepared_series = self._prepare_series_for_model(series)
        if prepared_series is None or len(prepared_series) < Config.MIN_HISTORY_DAYS:
            logger.warning(f"Not enough {source} data for {equipment_type}")
            return

        try:
            sarima_model = SARIMAX(
                prepared_series,
                order=Config.SARIMA_ORDER,
                seasonal_order=Config.SARIMA_SEASONAL_ORDER,
                enforce_stationarity=False,
                enforce_invertibility=False
            )
            fitted_model = sarima_model.fit(disp=False)

            forecast_result = fitted_model.forecast(steps=horizon)
            forecast_values = np.maximum(forecast_result, 0)

            forecast_obj = fitted_model.get_forecast(steps=horizon)
            confidence_int = forecast_obj.conf_int()

            start_date = datetime.now().date()
            forecast_dates = [start_date + timedelta(days=i) for i in range(horizon)]

            forecast_list = [
                {"date": d.isoformat(), "predicted_demand": float(round(val, 1))}
                for d, val in zip(forecast_dates, forecast_values)
            ]

            ci_data = {
                "lower": [
                    {"date": d.isoformat(), "value": float(max(0, round(lower, 1)))}
                    for d, lower in zip(forecast_dates, confidence_int.iloc[:, 0])
                ],
                "upper": [
                    {"date": d.isoformat(), "value": float(round(upper, 1))}
                    for d, upper in zip(forecast_dates, confidence_int.iloc[:, 1])
                ]
            } if confidence_int is not None else None

            response = ForecastResponse(
                equipment_type=equipment_type,
                forecast_horizon_days=horizon,
                forecast=forecast_list,
                model_params={
                    "order": str(Config.SARIMA_ORDER),
                    "seasonal_order": str(Config.SARIMA_SEASONAL_ORDER),
                    "aic": float(fitted_model.aic),
                    "bic": float(fitted_model.bic)
                },
                data_source=source,
                history_days_used=len(prepared_series),
                timestamp=datetime.now().isoformat(),
                confidence_intervals=ci_data
            )

            self.last_forecasts[equipment_type][source] = response
            self.models[f"{equipment_type}_{source}"] = fitted_model

            # Сохраняем прогноз в AnalyticsDB
            if Config.AUTO_SAVE_FORECASTS:
                saved_id = await self.save_forecast_to_db(response, trigger_event_id)
                response.id = saved_id

            # Публикуем прогноз в Kafka
            await self.producer.send(
                "forecast.updated",
                value={
                    "equipment_type": equipment_type,
                    "source": source,
                    "forecast_id": response.id,
                    "forecast": forecast_list,
                    "timestamp": datetime.now().isoformat()
                }
            )
            logger.info(f"Forecast saved and published for {equipment_type} from {source}")

        except Exception as e:
            logger.error(f"Error fitting SARIMA for {equipment_type} ({source}): {e}")

    async def get_forecast(
            self,
            equipment_type: str,
            horizon: int = 7,
            use_database_history: bool = False,
            force_refresh: bool = False
    ) -> ForecastResponse:
        """Получить прогноз из выбранного источника"""
        source = "db" if use_database_history else "kafka"

        if self.daily_demand[equipment_type].get(source) is None:
            if source == "db":
                await self.load_history_from_db(equipment_type)
            else:
                raise HTTPException(
                    status_code=404,
                    detail=f"No {source} data available for {equipment_type}"
                )

        if force_refresh or f"{equipment_type}_{source}" not in self.models:
            await self._update_model_and_forecast(equipment_type, source, horizon)

        if source not in self.last_forecasts.get(equipment_type, {}):
            raise HTTPException(status_code=404, detail=f"No forecast available for {equipment_type} from {source}")

        return self.last_forecasts[equipment_type][source]

    async def get_available_types(self, source: str = None) -> List[str]:
        """Список типов оборудования с прогнозами"""
        if source:
            return [k for k, v in self.last_forecasts.items() if source in v]
        return list(self.last_forecasts.keys())

    async def compare_forecasts(self, equipment_type: str, horizon: int = 7) -> Dict:
        """Сравнение прогнозов из двух источников"""
        kafka_forecast = await self.get_forecast(equipment_type, horizon, use_database_history=False, force_refresh=False)
        db_forecast = await self.get_forecast(equipment_type, horizon, use_database_history=True, force_refresh=False)

        values1 = [f["predicted_demand"] for f in kafka_forecast.forecast]
        values2 = [f["predicted_demand"] for f in db_forecast.forecast]
        mae = float(np.mean(np.abs(np.array(values1) - np.array(values2))))

        return {
            "equipment_type": equipment_type,
            "kafka_source": kafka_forecast.dict(),
            "postgres_source": db_forecast.dict(),
            "comparison": {"mae_between_sources": mae}
        }


# ==================== FastAPI приложение ====================

service = DemandForecastService()

@asynccontextmanager
async def lifespan(app: FastAPI):
    await service.start()
    asyncio.create_task(service.consume_loop())
    logger.info("Demand Forecast Service started with AnalyticsDB")
    yield
    await service.stop()

app = FastAPI(
    title="Demand Forecast Microservice (Kafka + PostgreSQL + AnalyticsDB)",
    description="SARIMA-based forecasting with forecast persistence in AnalyticsDB",
    version="3.0.0",
    lifespan=lifespan
)

# ==================== REST Endpoints ====================

@app.get("/health", response_model=HealthResponse)
async def health():
    return HealthResponse(
        status="ok",
        equipment_types=await service.get_available_types(),
        events_consumed_from_kafka=service.kafka_events_consumed,
        records_loaded_from_db=service.db_load_stats,
        forecasts_saved=service.forecasts_saved_count
    )

@app.get("/forecast/{equipment_type}", response_model=ForecastResponse)
async def get_forecast(
        equipment_type: str,
        horizon_days: int = Query(default=7, ge=1, le=30),
        use_database_history: bool = Query(default=False),
        force_refresh: bool = Query(default=False),
        save_to_db: bool = Query(default=True, description="Сохранить прогноз в AnalyticsDB")
):
    """Получить прогноз спроса"""
    Config.AUTO_SAVE_FORECASTS = save_to_db
    return await service.get_forecast(equipment_type, horizon_days, use_database_history, force_refresh)

@app.get("/forecast/saved/{forecast_id}")
async def get_saved_forecast(forecast_id: str):
    """Получить сохранённый прогноз из AnalyticsDB по ID"""
    forecast = await service.get_forecast_by_id(forecast_id)
    if not forecast:
        raise HTTPException(status_code=404, detail="Forecast not found")

    return {
        "id": forecast.id,
        "equipment_type": forecast.equipment_type,
        "forecast_horizon_days": forecast.forecast_horizon_days,
        "forecast": forecast.forecast_data,
        "model_params": {
            "order": forecast.model_order,
            "seasonal_order": forecast.model_seasonal_order,
            "aic": forecast.model_aic,
            "bic": forecast.model_bic
        },
        "data_source": forecast.data_source,
        "history_days_used": forecast.history_days_used,
        "created_at": forecast.created_at.isoformat(),
        "valid_until": forecast.valid_until.isoformat() if forecast.valid_until else None,
        "is_active": forecast.is_active
    }

@app.get("/forecast/history")
async def get_forecast_history(
        equipment_type: str = None,
        limit: int = Query(default=10, le=100),
        offset: int = Query(default=0),
        active_only: bool = Query(default=True)
):
    """История сохранённых прогнозов из AnalyticsDB"""
    forecasts, total = await service.get_saved_forecasts(equipment_type, limit, offset, active_only)

    return {
        "total": total,
        "limit": limit,
        "offset": offset,
        "forecasts": [
            {
                "id": f.id,
                "equipment_type": f.equipment_type,
                "data_source": f.data_source,
                "created_at": f.created_at.isoformat(),
                "forecast_horizon_days": f.forecast_horizon_days,
                "is_active": f.is_active,
                "forecast_summary": f.forecast_data[:3] if f.forecast_data else []
            }
            for f in forecasts
        ]
    }

@app.post("/forecast/save")
async def save_current_forecast(request: SaveForecastRequest):
    """Принудительное сохранение текущего прогноза в AnalyticsDB"""
    forecast = await service.get_forecast(
        request.equipment_type,
        use_database_history=(request.source == "db"),
        force_refresh=request.force
    )
    forecast_id = await service.save_forecast_to_db(forecast)
    return {"status": "saved", "forecast_id": forecast_id, "equipment_type": request.equipment_type}

@app.post("/forecast/compare/{equipment_type}")
async def compare_forecasts(equipment_type: str, horizon_days: int = 7):
    """Сравнить прогнозы из двух источников"""
    return await service.compare_forecasts(equipment_type, horizon_days)

@app.post("/history/load")
async def load_history_from_db(request: LoadHistoryRequest):
    """Загрузка исторических данных из PostgreSQL"""
    series, count = await service.load_history_from_db(request.equipment_type, request.days_back, request.force)
    return {
        "status": "loaded",
        "equipment_type": request.equipment_type,
        "days_loaded": len(series) if series is not None else 0,
        "records_processed": count
    }

@app.post("/history/load-all")
async def load_all_history(days_back: int = 90, background_tasks: BackgroundTasks = None):
    """Загрузка истории для всех типов оборудования"""
    if background_tasks:
        background_tasks.add_task(service.load_all_history_from_db, days_back)
        return {"status": "loading_started", "days_back": days_back}
    else:
        await service.load_all_history_from_db(days_back)
        return {"status": "loaded", "days_back": days_back}

@app.get("/history/{equipment_type}")
async def get_history(
        equipment_type: str,
        source: str = Query(default="kafka", regex="^(kafka|db)$")
):
    """Получить исторические данные спроса"""
    series = service.daily_demand[equipment_type].get(source)
    if series is None:
        raise HTTPException(status_code=404, detail=f"No history for {equipment_type} from {source}")

    return {
        "equipment_type": equipment_type,
        "source": source,
        "data": [{"date": str(date), "demand": float(val)} for date, val in series.items()]
    }

@app.post("/train/{equipment_type}")
async def train_model(
        equipment_type: str,
        use_database_history: bool = Query(default=False),
        background_tasks: BackgroundTasks = None
):
    """Принудительное переобучение модели"""
    source = "db" if use_database_history else "kafka"

    async def train():
        await service._update_model_and_forecast(equipment_type, source)

    if background_tasks:
        background_tasks.add_task(train)
        return {"status": "training_started", "equipment_type": equipment_type, "source": source}
    else:
        await train()
        return {"status": "training_completed", "equipment_type": equipment_type, "source": source}

@app.delete("/cache/{equipment_type}")
async def clear_cache(equipment_type: str, source: str = Query(default="both", regex="^(kafka|db|both)$")):
    """Очистить кэш прогноза"""
    if source in ["kafka", "both"]:
        service.last_forecasts[equipment_type].pop("kafka", None)
    if source in ["db", "both"]:
        service.last_forecasts[equipment_type].pop("db", None)

    keys_to_delete = [k for k in service.models.keys() if k.startswith(f"{equipment_type}_")]
    for key in keys_to_delete:
        if source == "both" or key.endswith(source):
            del service.models[key]

    return {"status": "cache_cleared", "equipment_type": equipment_type, "source": source}


if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)