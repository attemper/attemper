package com.sse.attemper.common.enums;

/**
 * @author pczhao
 * @email
 * @date 2019-01-14 14:50
 */

public enum ArgType {
    // integer 类型
    INTEGER(1),
    // String 类型
    STRING(2),
    // boolean 类型
    BOOLEAN(3),
    // double 类型
    DOUBLE(4),
    // long 类型
    LONG(5),
    // datetime 类型
    DATE(6),
    // SQL 语句
    SQL(7)
    ;

    ArgType(int type) {
        this.type = type;
    }

    private int type;

    public int getType() {
        return type;
    }

    /**
     * 获取枚举对象
     * @param type
     * @return
     */
    public static ArgType get(Integer type){
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
