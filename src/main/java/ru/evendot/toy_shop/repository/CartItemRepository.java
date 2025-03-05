package ru.evendot.toy_shop.repository;

public interface CartItemRepository {
    void deleteAllByCartId(Long id);
}
