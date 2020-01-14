package com.github.attemper.executor.ext.enums;

public enum ResultDataType {

    OBJECT(0),

    MAP(1),

    LIST(2);

    private int value;

    ResultDataType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ResultDataType get(int value) {
        for (ResultDataType item : ResultDataType.values()) {
            if (item.getValue() == value) {
                return item;
            }
        }
        return null;
    }
}
