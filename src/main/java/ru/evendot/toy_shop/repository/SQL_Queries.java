package ru.evendot.toy_shop.repository;

public enum SQL_Queries {
    SELECT_ALL("SELECT * FROM orders");

    private final String text;
    SQL_Queries(final String text){
        this.text = text;
    }

    @Override
    public String toString(){
        return text;
    }
}
