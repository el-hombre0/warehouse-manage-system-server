package ru.evendot.warehouse.enums;

public enum StringConst {
    BASE_URL("http://localhost:8080/api/v1");

    private final String text;

    StringConst(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
