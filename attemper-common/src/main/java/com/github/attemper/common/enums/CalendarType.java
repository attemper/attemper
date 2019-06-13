package com.github.attemper.common.enums;

public enum CalendarType {

    Day(0),

    Time(1),

    ;

    private int type;

    CalendarType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static CalendarType get(int type) {
        for (CalendarType item : CalendarType.values()) {
            if (item.getType() == type) {
                return item;
            }
        }
        return null;
    }
}
