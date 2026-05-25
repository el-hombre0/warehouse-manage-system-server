package ru.evendot.rental_order_service.Broker.Events.Cart;

import lombok.*;
import ru.evendot.rental_order_service.Broker.Events.BaseEvent;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Событие, возникающее при добавлении товара в корзину
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemAddedToCartEvent extends BaseEvent {

    // Идентификатор сессии пользователя
    private String sessionId;

    // Идентификатор клиента (пользователя)
    private String customerId;

    // Идентификатор корзины
    private String cartId;

    // Идентификатор товара
    private String productId;

    // Название товара
    private String productName;

    // Категория товара
    private String productCategory;

    // Количество товара
    private Integer quantity;

    // Цена за единицу товара
    private Double unitPrice;

    // Почасовая ставка аренды
    private Double hourlyRate;

    // ===== Период аренды =====

    // Выбранная дата начала аренды
    private LocalDateTime selectedStartDate;

    // Выбранная дата окончания аренды
    private LocalDateTime selectedEndDate;

    // ===== Финансовая информация =====

    // Общая сумма за позицию (количество * цена за период)
    private Double totalItemAmount;

    // ===== Каналы и промо =====

    // Источник добавления: web, mobile, api, admin
    private SourceChannel sourceChannel;

    // Сумма скидки (опционально)
    private Double discountAmount;

    // ===== Вспомогательные методы =====

    /**
     * Расчет продолжительности аренды в днях
     */
    public long getRentalDurationDays() {
        if (selectedStartDate == null || selectedEndDate == null) {
            return 0;
        }
        return java.time.Duration.between(selectedStartDate, selectedEndDate).toDays();
    }

    // ===== Вложенный enum для источника =====

    @Getter
    public enum SourceChannel {
        WEB("web"),
        MOBILE("mobile"),
        API("api"),
        ADMIN("admin");

        private final String code;

        SourceChannel(String code) {
            this.code = code;
        }

        public static SourceChannel fromCode(String code) {
            for (SourceChannel channel : values()) {
                if (channel.code.equalsIgnoreCase(code)) {
                    return channel;
                }
            }
            throw new IllegalArgumentException("Unknown source channel: " + code);
        }
    }

    // ===== Валидация события =====

    /**
     * Проверка корректности события
     */
    public boolean isValid() {
        return customerId != null && !customerId.isBlank()
                && cartId != null && !cartId.isBlank()
                && productId != null && !productId.isBlank()
                && quantity != null && quantity > 0
                && selectedStartDate != null
                && selectedEndDate != null
                && selectedEndDate.isAfter(selectedStartDate)
                && totalItemAmount != null
                && totalItemAmount.compareTo(0.0) >= 0;
    }
}
