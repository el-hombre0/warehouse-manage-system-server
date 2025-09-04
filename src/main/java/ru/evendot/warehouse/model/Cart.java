package ru.evendot.warehouse.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Корзина товаров
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double totalAmount = 0.00;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> cartItems = new HashSet<>();

    /**
     * Добавление товара в корзину
     *
     * @param item Добавляемый товар
     */
    public void addItem(CartItem item) {
        this.cartItems.add(item);
        item.setCart(this);
        updateTotalAmount();
    }

    /**
     * Удаление товара из корзины
     *
     * @param item Удаляемый товар
     */
    public void removeItem(CartItem item) {
        this.cartItems.remove(item);
        item.setCart(null);
        updateTotalAmount();
    }

    /**
     * Обновление стоимости товаров в корзине
     */
    private void updateTotalAmount() {
        this.totalAmount = cartItems.stream().map(cartItem -> {
            Double unitPrice = cartItem.getUnitPrice();
            if (unitPrice == null) {
                return 0.00;
            }
            return unitPrice * cartItem.getQuantity();
        }).reduce(0.00, Double::sum);
    }
}
