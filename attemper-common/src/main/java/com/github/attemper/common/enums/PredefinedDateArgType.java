package com.github.attemper.common.enums;

public enum PredefinedDateArgType {
    TODAY("T"),
    WEEK("W"),
    MONTH("M"),
    SEASON("S"),
    HALF_YEAR("H"),
    YEAR("Y"),
    ;

    PredefinedDateArgType(String type) {
        this.type = type;
    }

    private String type;

    public String getType() {
        return type;
    }

    public static PredefinedDateArgType get(String type) {
        if (type != null) {
            for (PredefinedDateArgType item : PredefinedDateArgType.values()) {
                if (item.getType().equals(type)) {
                    return item;
                }
            }
        }
        return null;
    }
}
