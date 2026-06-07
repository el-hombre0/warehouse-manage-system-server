package ru.evendot.logging.service;

import ru.evendot.logging.model.SystemEventLog;
import ru.evendot.logging.repository.SystemEventLogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SystemEventLogService {

    private final SystemEventLogRepository logRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void logEvent(LogEventBuilder builder) {
        try {
            SystemEventLog logEntry = builder.build();
            logRepository.save(logEntry);

            log.debug("Event logged: {} - {} - {}",
                    logEntry.getServiceName(),
                    logEntry.getEventType(),
                    logEntry.getEventId());
        } catch (Exception e) {
            log.error("Failed to save log event", e);
        }
    }

    @Transactional
    public void logProductEvent(String operation, Product product, Product oldProduct, Long userId) {
        LogEventBuilder builder = LogEventBuilder.forService("products-service")
                .eventType("PRODUCT_" + operation.toUpperCase())
                .operation(operation)
                .entityType("Product")
                .entityId(product.getId())
                .entityPublicId(String.valueOf(product.getArticle()))
                .userId(userId)
                .correlationId(UUID.randomUUID().toString());

        if (oldProduct != null) {
            builder.beforeState(entityToMap(oldProduct));
        }
        builder.afterState(entityToMap(product));

        // Расчет дельты
        if (oldProduct != null) {
            builder.delta(calculateDelta(oldProduct, product));
        }

        logEvent(builder);
    }

    @Transactional
    public void logCartEvent(String operation, Cart cart, Cart oldCart, Long userId, String details) {
        LogEventBuilder builder = LogEventBuilder.forService("rental-order-service")
                .eventType("CART_" + operation.toUpperCase())
                .operation(operation)
                .entityType("Cart")
                .entityId(cart.getId())
                .userId(userId)
                .details(details);

        if (oldCart != null) {
            builder.beforeState(entityToMap(oldCart));
        }
        builder.afterState(entityToMap(cart));

        logEvent(builder);
    }

    @Transactional
    public void logCartItemEvent(String operation, CartItem cartItem, Cart parentCart, Long userId) {
        LogEventBuilder builder = LogEventBuilder.forService("rental-order-service")
                .eventType("CART_ITEM_" + operation.toUpperCase())
                .operation(operation)
                .entityType("CartItem")
                .entityId(cartItem.getId())
                .parentEntityType("Cart")
                .parentEntityId(parentCart.getId())
                .userId(userId)
                .details(String.format("ProductId: %d, Quantity: %d, TotalPrice: %.2f",
                        cartItem.getProductId(), cartItem.getQuantity(), cartItem.getTotalPrice()))
                .afterState(Map.of(
                        "quantity", cartItem.getQuantity(),
                        "unitPrice", cartItem.getUnitPrice(),
                        "totalPrice", cartItem.getTotalPrice(),
                        "selectedStartDate", cartItem.getSelectedStartDate(),
                        "selectedEndDate", cartItem.getSelectedEndDate()
                ));

        logEvent(builder);
    }

    @Transactional
    public void logOrderEvent(String operation, Order order, Order oldOrder, Long userId) {
        LogEventBuilder builder = LogEventBuilder.forService("rental-order-service")
                .eventType("ORDER_" + operation.toUpperCase())
                .operation(operation)
                .entityType("Order")
                .entityId(order.getId())
                .entityPublicId(order.getUuid().toString())
                .userId(userId)
                .details(String.format("TotalAmount: %.2f, Status: %s, PaymentStatus: %s",
                        order.getTotalAmount(), order.getOrderStatus(), order.getPaymentStatus()))
                .afterState(Map.of(
                        "totalAmount", order.getTotalAmount(),
                        "orderStatus", order.getOrderStatus().name(),
                        "paymentStatus", order.getPaymentStatus().name(),
                        "paymentMethod", order.getPaymentMethod() != null ? order.getPaymentMethod().name() : null
                ));

        if (oldOrder != null) {
            builder.beforeState(entityToMap(oldOrder));
            builder.delta(calculateDelta(oldOrder, order));
        }

        logEvent(builder);
    }

    @Transactional
    public void logUserEvent(String operation, User user, User oldUser, Long userId, String details) {
        LogEventBuilder builder = LogEventBuilder.forService("users-service")
                .eventType("USER_" + operation.toUpperCase())
                .operation(operation)
                .entityType("User")
                .entityId(user.getId())
                .userId(userId != null ? userId : user.getId())
                .userEmail(user.getEmail())
                .details(details);

        if (oldUser != null) {
            builder.beforeState(Map.of(
                    "email", oldUser.getEmail(),
                    "role", oldUser.getRole()
            ));
        }
        builder.afterState(Map.of(
                "email", user.getEmail(),
                "role", user.getRole()
        ));

        logEvent(builder);
    }

    @Transactional
    public void logError(String serviceName, String operation, String entityType,
                         Long entityId, Long userId, String errorCode, String errorMessage) {
        logEvent(LogEventBuilder.forService(serviceName)
                .eventType("ERROR")
                .operation(operation)
                .entityType(entityType)
                .entityId(entityId)
                .userId(userId)
                .status(SystemEventLog.EventStatus.FAILURE)
                .errorCode(errorCode)
                .errorMessage(errorMessage));
    }

    // Вспомогательные методы
    private Map<String, Object> entityToMap(Object entity) {
        try {
            String json = objectMapper.writeValueAsString(entity);
            return objectMapper.readValue(json, Map.class);
        } catch (JsonProcessingException e) {
            log.warn("Failed to convert entity to map", e);
            return new HashMap<>();
        }
    }

    private Map<String, Object> calculateDelta(Object oldEntity, Object newEntity) {
        Map<String, Object> oldMap = entityToMap(oldEntity);
        Map<String, Object> newMap = entityToMap(newEntity);
        Map<String, Object> delta = new HashMap<>();

        for (String key : newMap.keySet()) {
            Object oldValue = oldMap.get(key);
            Object newValue = newMap.get(key);

            if (oldValue != null && !oldValue.equals(newValue)) {
                delta.put(key, Map.of("from", oldValue, "to", newValue));
            } else if (oldValue == null && newValue != null) {
                delta.put(key, Map.of("from", null, "to", newValue));
            }
        }

        return delta;
    }
}
