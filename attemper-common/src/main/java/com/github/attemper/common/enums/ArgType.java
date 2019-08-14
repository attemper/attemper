package com.github.attemper.common.enums;

public enum ArgType {
    // generic type
    STRING("String", 0),
    BOOLEAN("Boolean", 1),
    INTEGER("Integer", 2),
    DOUBLE("Double", 3),
    LONG("Long", 4),
    DATE("Date", 10),
    TIME("Time", 11),
    DATETIME("DateTime", 12),

    // raw type
    LIST("List", 30),
    MAP("Map", 31),

    // sql type
    SQL("Sql", 40),

    //trade date
    TRADE_DATE("TradeDate", 50),;

    ArgType(String name, int value) {
        this.name = name;
        this.value = value;
    }

    private String name;

    private int value;

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public static ArgType get(Integer type) {
        if (type != null) {
            for (ArgType item : ArgType.values()) {
                if (item.getValue() == type) {
                    return item;
                }
            }
        }
        return null;
    }
}
