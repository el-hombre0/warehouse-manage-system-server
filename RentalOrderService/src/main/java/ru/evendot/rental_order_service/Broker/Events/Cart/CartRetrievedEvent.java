package ru.evendot.rental_order_service.Broker.Events.Cart;

import lombok.*;
import ru.evendot.rental_order_service.Broker.Events.BaseEvent;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Событие, возникающее при получении корзины
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartRetrievedEvent extends BaseEvent {

    // ===== Идентификаторы события =====

    /**
     * Идентификатор сессии пользователя
     */
    private String sessionId;

    // ===== Информация о корзине =====

    /**
     * ID корзины
     */
    private Long cartId;

    /**
     * ID пользователя, которому принадлежит корзина
     */
    private Long userId;

    /**
     * Общая сумма корзины
     */
    private Double totalAmount;

    /**
     * Общее количество всех товаров в корзине (сумма quantity)
     */
    private Integer totalQuantity;

    /**
     * Статус корзины (пустая/не пустая)
     */
    private CartStatus status;

    // ===== Метаданные запроса =====

    /**
     * Источник запроса: web, mobile, api, admin
     */
    private SourceChannel sourceChannel;


    // ===== Вложенные DTO =====



    // ===== Вложенные Enum'ы =====

    @Getter
    public enum CartStatus {
        EMPTY("empty", "Корзина пуста"),
        NON_EMPTY("non_empty", "В корзине есть товары");

        private final String code;
        private final String description;

        CartStatus(String code, String description) {
            this.code = code;
            this.description = description;
        }

    }

    @Getter
    public enum SourceChannel {
        WEB("web"), MOBILE("mobile"), API("api"), ADMIN("admin");

        private final String code;

        SourceChannel(String code) { this.code = code; }
    }

    /**
     * Проверка, является ли корзина просроченной (например, старше 7 дней без изменений)
     */
    public boolean isStale(LocalDateTime lastActivityTime, int staleDays) {
        return lastActivityTime != null &&
                lastActivityTime.plusDays(staleDays).isBefore(LocalDateTime.now());
    }

    // ===== Валидация события =====

    /**
     * Проверка корректности события
     */
    public boolean isValid() {
        return cartId != null && cartId > 0
                && userId != null && userId > 0
                && totalAmount != null
                && totalAmount >= 0
                && sourceChannel != null;
    }
}
