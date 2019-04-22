package com.sse.attemper.common.enums;

/**
 * @author ldang
 */
public enum ResourceType {

    FOLDER(0),

    MENU(1),

    BUTTON(2),

    BLOCK(3);

    private int type;

    ResourceType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static ResourceType get(int type) {
        for (ResourceType resourceType : ResourceType.values()) {
            if (resourceType.getType() == type) {
                return resourceType;
            }
        }
        return null;
    }
}
