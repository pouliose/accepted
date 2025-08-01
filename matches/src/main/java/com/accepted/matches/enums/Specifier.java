package com.accepted.matches.enums;

public enum Specifier {
    ONE("1"),
    X("X"),
    TWO("2");

    private final String value;

    Specifier(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
