package ru.evendot.rental_order_service.Broker.Events.Cart;

import lombok.*;
import ru.evendot.rental_order_service.Broker.Events.BaseEvent;


/**
 * Событие, возникающее при обновлении количества товара в корзине
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemQuantityUpdatedEvent extends BaseEvent {

    /**
     * Идентификатор сессии пользователя
     */
    private String sessionId;

    /**
     * Идентификатор клиента (пользователя)
     */
    private String customerId;

    /**
     * Идентификатор корзины
     */
    private String cartId;

    // ===== Информация о товаре =====

    /**
     * Идентификатор товара
     */
    private String productId;

    /**
     * Название товара
     */
    private String productName;

    // ===== Количественные показатели =====

    /**
     * Предыдущее количество товара
     */
//    private Integer previousQuantity;

    /**
     * Новое количество товара
     */
    private Integer newQuantity;

    /**
     * Изменение количества (дельта)
     */
//    private Integer quantityDelta;

    // ===== Финансовые показатели =====

    /**
     * Цена за единицу товара (актуальная на момент обновления)
     */
    private Double unitPrice;

    /**
     * Дневная ставка аренды
     */
    private Double hourlyRate;


    /**
     * Предыдущая общая сумма корзины
     */
    private Double previousCartTotalAmount;

    /**
     * Новая общая сумма корзины
     */
    private Double newCartTotalAmount;

    /**
     * Изменение общей суммы корзины (дельта)
     */
    private Double cartTotalDelta;

    // ===== Дополнительные агрегаты
    /**
     * Источник изменения: web, mobile, api, admin, system
     */
    private SourceChannel sourceChannel;

    /**
     * Причина изменения (опционально)
     */
    private UpdateReason updateReason;

    /**
     * Идентификатор примененного промокода (если влияет на цену)
     */
    private String appliedPromoCode;

    /**
     * Скидка на единицу товара (если есть)
     */
    private Double discountPerUnit;

    /**
     * Категория товара (для аналитики)
     */
    private String productCategory;


    @Getter
    public enum SourceChannel {
        WEB("web"), MOBILE("mobile"), API("api"), ADMIN("admin"), SYSTEM("system");

        private final String code;

        SourceChannel(String code) { this.code = code; }
    }

    @Getter
    public enum UpdateReason {
        USER_MANUAL("user_manual", "Пользователь вручную изменил количество"),
        AUTO_CORRECTION("auto_correction", "Автоматическая корректировка (например, по остаткам)"),
        PROMO_CODE_APPLIED("promo_code_applied", "Применен промокод, повлиявший на минимальное количество"),
        STOCK_LIMIT("stock_limit", "Ограничение по наличию на складе"),
        BUNDLE_DISCOUNT("bundle_discount", "Изменение из-за акции 'купи больше - плати меньше'"),
        PRICE_CHANGE("price_change", "Изменение цены товара повлекло изменение количества");

        private final String code;
        private final String description;

        UpdateReason(String code, String description) {
            this.code = code;
            this.description = description;
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
                && newQuantity != null && newQuantity >= 0
                && previousCartTotalAmount != null
                && newCartTotalAmount != null;
    }
}
