package ru.evendot.warehouse.repository;

public enum SQL_Queries {
    SELECT_ALL("SELECT * FROM orders"),
    SELECT_FROM_TABLE_WHERE_ID("SELECT * FROM orders WHERE uuid = ?"),
    SELECT_ORDERS_WHERE_USERID("SELECT * FROM orders WHERE user_id = ?"),
    SAVE_ORDER("INSERT INTO orders (total_amount, uuid, order_items, payment_method, payment_status, user_id, comment, time_creation, order_status, address_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"),
    FIND_ORDER_BY_ID("SELECT * FROM orders WHERE id = ?");
    private final String text;

    SQL_Queries(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
