package com.github.attemper.common.enums;

public enum ArgType {
    // generic type
    STRING(0),
    BOOLEAN(1),
    INTEGER(2),
    DOUBLE(3),
    LONG(4),
    DATE(10),
    TIME(11),
    DATETIME(12),

    // raw type
    LIST(20),
    MAP(21),

    // sql type
    SQL(30),

    //trade date
    TRADE_DATE(40),;

    ArgType(int type) {
        this.type = type;
    }

    private int type;

    public int getType() {
        return type;
    }

    public static ArgType get(Integer type) {
        if (type != null) {
            for (ArgType item : ArgType.values()) {
                if (item.getType() == type) {
                    return item;
                }
            }
        }
        return null;
    }
}
