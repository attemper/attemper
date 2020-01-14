package com.github.attemper.common.enums;

public enum TenantStatus {

    NORMAL(0),

    /**
     * frozen
     */
    FROZEN(1),

    /**
     * disabled
     */
    DISABLED(2);

    private int status;

    TenantStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public static TenantStatus get(int status) {
        for (TenantStatus item : TenantStatus.values()) {
            if (item.getStatus() == status) {
                return item;
            }
        }
        return null;
    }
}
