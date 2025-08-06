package com.accepted.matches.enums;

import java.util.HashMap;
import java.util.Map;

public enum Specifier {
    ONE("1"),
    X("X"),
    TWO("2");

    private final String value;
    private static final Map<String, Specifier> VALUE_TO_ENUM_MAP = new HashMap<>();

    static {
        for (Specifier specifier : Specifier.values()) {
            VALUE_TO_ENUM_MAP.put(specifier.value, specifier);
        }
    }

    Specifier(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Specifier fromValue(String value) {
        Specifier specifier = VALUE_TO_ENUM_MAP.get(value);
        if (specifier == null) {
            throw new IllegalArgumentException("Invalid Specifier value: " + value);
        }
        return specifier;
    }
}
