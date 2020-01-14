package com.github.attemper.common.enums;

public enum ConditionType {

    SQL(0),

    FTP_FILE(10),

    LOCAL_FILE(11),
    ;

    private int value;

    ConditionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
