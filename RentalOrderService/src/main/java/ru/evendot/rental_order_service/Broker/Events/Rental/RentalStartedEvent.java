package ru.evendot.rental_order_service.Broker.Events.Rental;

import lombok.*;
import ru.evendot.rental_order_service.Broker.Events.BaseEvent;
import ru.evendot.rental_order_service.Broker.Events.Cart.CartRetrievedEvent;
import ru.evendot.rental_order_service.DTOs.OrderItemDTO;
import ru.evendot.rental_order_service.DTOs.Product.ProductDTO;

import java.time.LocalDateTime;
import java.util.*;

import java.time.LocalDateTime;

/**
 * Событие, возникающее при начале аренды (выдаче оборудования клиенту)
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
public class RentalStartedEvent extends BaseEvent {

    public RentalStartedEvent() {
        setEventType("RENTAL_STARTED");
    }

    // ===== Идентификаторы события =====

        /**
         * Идентификатор аренды
         */
        private String rentalId;

        /**
         * Идентификатор клиента
         */
        private String customerId;

        // ===== Даты аренды =====

        /**
         * Фактическая дата и время начала аренды (момент выдачи)
         */
        private LocalDateTime actualStartDate;

        /**
         * Плановая дата и время окончания аренды
         */
        private LocalDateTime plannedEndDate;

        // ===== Выданное оборудование =====

        /**
         * Список выданного оборудования
         */
//        @Builder.Default
//        private List<IssuedEquipment> issuedEquipment = new ArrayList<>();
        private Set<OrderItemDTO> issuedEquipment;


    // ===== Информация о выдаче =====

        /**
         * Кто выдал оборудование (ID сотрудника или имя)
         */
        private String issuedBy;

        /**
         * Место выдачи (адрес пункта проката или ID локации)
         */
        private String issueLocation;

        // ===== Подпись клиента =====

        /**
         * Требуется ли подпись клиента
         */
        @Builder.Default
        private Boolean customerSignatureRequired = true;

        /**
         * Дата и время подписания клиентом (если подпись была)
         */
        private LocalDateTime customerSignedAt;

        /**
         * Статус подписи клиента
         */
        private SignatureStatus signatureStatus;

        // ===== Чек-лист =====

        /**
         * Чек-лист выдачи оборудования (пройден или нет)
         */
        @Builder.Default
        private Boolean checklistCompleted = false;

        // ===== Дополнительные метаданные =====

        /**
         * Комментарий к выдаче
         */
        private String issueComment;

        /**
         * Источник события (web, mobile, admin, system)
         */
        private CartRetrievedEvent.SourceChannel sourceChannel;

        // ===== Вложенные классы =====

        /**
         * Состояние оборудования на момент выдачи
         */
        @Getter
        public enum ConditionOnIssue {
            EXCELLENT("excellent", "Отличное состояние, как новое"),
            GOOD("good", "Хорошее состояние, мелкие потертости"),
            FAIR("fair", "Удовлетворительное, следы использования"),
            NEEDS_MAINTENANCE("needs_maintenance", "Требует обслуживания"),
            WITH_DAMAGE("with_damage", "Имеет повреждения (отмечены в заметках)");

            private final String code;
            private final String description;

            ConditionOnIssue(String code, String description) {
                this.code = code;
                this.description = description;
            }

        }

        /**
         * Статус подписи клиента
         */
        @Getter
        public enum SignatureStatus {
            NOT_REQUIRED("not_required", "Подпись не требуется"),
            PENDING("pending", "Ожидает подписи"),
            SIGNED("signed", "Подписано"),
            DECLINED("declined", "Клиент отказался подписывать");

            private final String code;
            private final String description;

            SignatureStatus(String code, String description) {
                this.code = code;
                this.description = description;
            }

        }


        // ===== Вспомогательные методы =====

        /**
         * Валидация события
         */
        public boolean isValid() {
            return rentalId != null && !rentalId.isBlank()
                    && customerId != null && !customerId.isBlank()
                    && actualStartDate != null
                    && plannedEndDate != null
                    && plannedEndDate.isAfter(actualStartDate)
                    && issuedEquipment != null && !issuedEquipment.isEmpty()
                    && issuedBy != null && !issuedBy.isBlank()
                    && issueLocation != null && !issueLocation.isBlank()
                    && checklistCompleted != null;
        }
}
