package com.github.attemper.common.enums;

public enum PredefinedDateArgType {
    TODAY("T"),
    WEEK("W"),
    MONTH("M"),
    SEASON("S"),
    HALF_YEAR("H"),
    YEAR("Y"),
    ;

    PredefinedDateArgType(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public static PredefinedDateArgType get(String type) {
        if (type != null) {
            for (PredefinedDateArgType item : PredefinedDateArgType.values()) {
                if (item.getValue().equals(type)) {
                    return item;
                }
            }
        }
        return null;
    }
}
