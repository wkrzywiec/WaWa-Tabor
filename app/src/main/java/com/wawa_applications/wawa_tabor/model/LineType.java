package com.wawa_applications.wawa_tabor.model;

public enum LineType {

    BUS(1),
    TRAM(2);

    private final int value;

    LineType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
