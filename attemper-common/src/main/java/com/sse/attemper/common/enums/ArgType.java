package com.sse.attemper.common.enums;

public enum ArgType {
    // integer
    INTEGER(1),
    // String
    STRING(2),
    // boolean
    BOOLEAN(3),
    // double
    DOUBLE(4),
    // long
    LONG(5),
    // datetime
    DATE(6),
    // SQL
    SQL(7);

    ArgType(int type) {
        this.type = type;
    }

    private int type;

    public int getType() {
        return type;
    }

    public static ArgType get(Integer type) {
        if (type != null) {
            for (ArgType argType : ArgType.values()) {
                if (argType.getType() == type) {
                    return argType;
                }
            }
        }
        return null;
    }
}
