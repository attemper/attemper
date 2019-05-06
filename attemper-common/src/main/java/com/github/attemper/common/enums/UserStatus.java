package com.github.attemper.common.enums;

/**
 * @author ldang
 */
public enum UserStatus {

    NORMAL(0),

    /**
     * frozen
     */
    FROZEN(1),

    /**
     * deleted in logic
     */
    DELETED(2);

    private int status;

    UserStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public static UserStatus get(int status) {
        for (UserStatus _userStatus : UserStatus.values()) {
            if (_userStatus.getStatus() == status) {
                return _userStatus;
            }
        }
        return null;
    }
}
