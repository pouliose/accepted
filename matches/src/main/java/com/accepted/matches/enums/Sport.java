package com.accepted.matches.enums;

public enum Sport {
    FOOTBALL(1),
    BASKETBALL(2);

    private final Integer value;

    Sport(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
