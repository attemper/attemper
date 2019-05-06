package com.github.attemper.common.enums;

/**
 * @author ldang
 */
public enum TagType {

    ROLE(0),

    GROUP(1),

    POST(2);

    private int type;

    TagType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static TagType get(int type) {
        for (TagType tagType : TagType.values()) {
            if (tagType.getType() == type) {
                return tagType;
            }
        }
        return null;
    }
}
