package ru.evendot.warehouse.repository;

public enum SQL_Queries {
    SELECT_ALL("SELECT * FROM orders"),
    SELECT_FROM_TABLE_WHERE_ID("SELECT * FROM orders WHERE uuid = ?"),
    INSERT_INTO_TABLE("INSERT INTO orders (cost, uuid, pay_method, products, user, comment, time_creation, status, address) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
    private final String text;
    SQL_Queries(final String text){
        this.text = text;
    }

    @Override
    public String toString(){
        return text;
    }
}
