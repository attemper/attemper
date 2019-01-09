package com.thor.sys.holder;

import com.stark.sdk.common.result.user.AdminInfo;

/**
 * @author ldang
 */
public class AdminInfoHolder {

    private static final ThreadLocal<AdminInfo> contextHolder = new ThreadLocal<>();

    public static void set(AdminInfo adminInfo) {
        contextHolder.set(adminInfo);
    }

    public static AdminInfo get() {
        return contextHolder.get();
    }

    public static void clear() {
        contextHolder.remove();
    }
}
