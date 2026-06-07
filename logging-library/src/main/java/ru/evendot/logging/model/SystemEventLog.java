package ru.evendot.logging.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "system_event_logs",
        indexes = {
                @Index(name = "idx_event_type", columnList = "event_type"),
                @Index(name = "idx_service_name", columnList = "service_name"),
                @Index(name = "idx_entity_id", columnList = "entity_id"),
                @Index(name = "idx_user_id", columnList = "user_id"),
                @Index(name = "idx_timestamp", columnList = "event_timestamp"),
                @Index(name = "idx_correlation_id", columnList = "correlation_id"),
                @Index(name = "idx_transaction_id", columnList = "transaction_id")
        })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemEventLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== Идентификаторы события =====

    @Column(nullable = false, unique = true)
    @Builder.Default
    private String eventId = UUID.randomUUID().toString();

    @Column(nullable = false)
    private String eventType;          // PRODUCT_CREATED, CART_UPDATED, ORDER_PLACED, etc.

    @Column(nullable = false)
    private String serviceName;         // products-service, rental-order-service, users-service

    @Column(nullable = false)
    private String operation;            // CREATE, UPDATE, DELETE, GET, PLACE, etc.

    // ===== Временные метки =====

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime eventTimestamp;

    @Column
    private LocalDateTime businessTimestamp;  // бизнес-время события (если отличается от системного)

    // ===== Идентификаторы сущностей =====

    @Column
    private String entityType;          // Product, Cart, CartItem, Order, User

    @Column
    private Long entityId;

    @Column
    private String entityPublicId;       // UUID or article (для Product - article)

    @Column
    private String parentEntityType;     // Cart (для CartItem), User (для Cart/Order)

    @Column
    private Long parentEntityId;

    // ===== Пользовательский контекст =====

    @Column
    private Long userId;                 // ID пользователя, совершившего действие

    @Column
    private String userEmail;            // Email пользователя (денормализация)

    @Column
    private String userRole;             // Роль пользователя на момент действия

    // ===== Транзакционный контекст =====

    @Column
    private String correlationId;        // ID для связывания событий в одном бизнес-процессе

    @Column
    private String transactionId;        // ID транзакции БД

    @Column
    private String sessionId;            // ID сессии пользователя

    @Column
    private String requestId;            // ID HTTP запроса

    @Column
    private String sourceIp;             // IP источника запроса

    // ===== Содержание изменений =====

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> beforeState;     // состояние "до" изменения

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> afterState;      // состояние "после" изменения

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> delta;           // что именно изменилось (разница)

    @Column(columnDefinition = "text")
    private String details;                      // дополнительное описание в текстовом виде

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> metadata;        // дополнительные метаданные

    // ===== Агрегаты для модуля предсказания спроса

    // Детали продукта (дублируем на момент события, т.к. product может измениться)
    @Column
    private Long productId;

    @Column
    private Long productArticle;  // артикул для группировки

    @Column
    private String productTitle;

    @Column
    private Long categoryId;

    @Column
    private String categoryName;

    // Детали аренды/заказа
    @Column
    private Long orderId;

    @Column
    private Integer quantity;  // КОЛИЧЕСТВО товаров - ключевое для спроса!

    @Column
    private Double unitPrice;   // цена за единицу

    @Column
    private Double totalAmount; // общая сумма

    @Column
    private LocalDateTime rentalStartDateTime;

    @Column
    private LocalDateTime rentalPlannedEndDateTime;

    @Column
    private Integer rentalDurationHours; // вычисляемое поле

    // Поля для прогнозирования
    @Column
    private Integer stockLevelBefore;    // остаток до операции

    @Column
    private Integer stockLevelAfter;     // остаток после операции

    @Column
    private Boolean isPromotionApplied;   // действовала ли акция

    @Column
    private Double discountPercent;       // процент скидки

    // Временные метки для анализа
    @Column
    private String season;                // HIGH_SEASON, LOW_SEASON, HOLIDAY

    @Column
    private Boolean isWeekend;            // выходной день

    @Column
    private Integer hourOfDay;            // час дня (0-23)

    // ===== Статусы и результаты =====

    @Column(nullable = false)
    @Builder.Default
    private EventStatus status = EventStatus.SUCCESS;

    @Column
    private Integer httpStatus;                  // HTTP статус ответа

    @Column
    private String errorCode;                    // Код ошибки

    @Column(columnDefinition = "text")
    private String errorMessage;                 // Сообщение об ошибке

    @Column
    private Long executionTimeMs;                // Время выполнения операции в мс

    // ===== Версионирование =====

    @Column(nullable = false)
    @Builder.Default
    private Integer version = 1;

    @Column
    private Boolean isReplay = false;             // Воспроизведено ли событие из лога

    @Column
    private String replayedFromEventId;           // ID исходного события при воспроизведении

    // ===== Методы-помощники =====

    public enum EventStatus {
        SUCCESS, FAILURE, PARTIAL, PENDING, REJECTED
    }
}
