package ru.evendot.rental_order_service.Broker.Events.Cart;

import lombok.*;
import ru.evendot.rental_order_service.Broker.Events.BaseEvent;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Событие, возникающее при удалении товара из корзины
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRemovedFromCartEvent extends BaseEvent {

    // ===== Идентификаторы =====

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

    // ===== Информация об удаленном товаре =====

    /**
     * Идентификатор товара
     */
    private String productId;

    /**
     * Название товара
     */
    private String productName;

    /**
     * Количество удаляемого товара
     */
    private Integer quantity;

    // ===== Причина удаления =====

    /**
     * Причина удаления товара из корзины
     */
    private RemovalReason removalReason;

    /**
     * Кто инициировал удаление (опционально: userId или system)
     */
    private String deletedBy;

    // ===== Финансовая информация =====

    /**
     * Общая сумма корзины до удаления товара
     */
    private Double previousTotalAmount;

    /**
     * Общая сумма корзины после удаления товара
     */
    private Double newTotalAmount;

    /**
     * Проверка, было ли удаление автоматическим
     */
    public boolean isAutoRemoval() {
        return removalReason != null && removalReason.isAutomatic();
    }

    /**
     * Проверка, было ли удаление инициировано пользователем
     */
    public boolean isUserAction() {
        return removalReason == RemovalReason.USER_ACTION;
    }

    // ===== Вложенный enum для причины удаления =====

    @Getter
    public enum RemovalReason {
        USER_ACTION("user_action", false, "Пользователь удалил товар самостоятельно"),
        AUTO_EXPIRED("auto_expired", true, "Автоматическое удаление по истечении времени хранения в корзине"),
        OUT_OF_STOCK("out_of_stock", true, "Товар закончился на складе"),
        PRICE_CHANGED("price_changed", true, "Изменилась цена товара"),
        SESSION_EXPIRED("session_expired", true, "Сессия пользователя истекла");

        private final String code;
        private final boolean automatic;
        private final String description;

        RemovalReason(String code, boolean automatic, String description) {
            this.code = code;
            this.automatic = automatic;
            this.description = description;
        }

        public static RemovalReason fromCode(String code) {
            for (RemovalReason reason : values()) {
                if (reason.code.equalsIgnoreCase(code)) {
                    return reason;
                }
            }
            throw new IllegalArgumentException("Unknown removal reason: " + code);
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
                && removalReason != null
                && previousTotalAmount != null
                && newTotalAmount != null
                && previousTotalAmount.compareTo(newTotalAmount) >= 0;
    }
}