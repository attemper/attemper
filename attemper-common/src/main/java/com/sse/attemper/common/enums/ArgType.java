package com.sse.attemper.common.enums;

public enum ArgType {
    STRING(0),
    INTEGER(1),
    BOOLEAN(2),
    DOUBLE(3),
    LONG(4),
    DATE(5),
    DATETIME(6),
    LIST(7),
    MAP(8),
    SQL(9);

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
