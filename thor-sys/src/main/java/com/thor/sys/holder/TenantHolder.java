package com.thor.sys.holder;

import com.thor.sdk.common.result.sys.tenant.Tenant;

/**
 * the tenant which user admin it
 */
public class TenantHolder {
    
    private static final ThreadLocal<Tenant> contextHolder = new ThreadLocal<Tenant>();

    public static void set(Tenant Tenant) {
        contextHolder.set(Tenant);
    }

    public static Tenant get() {
        return contextHolder.get();
    }

    public static void clear() {
        contextHolder.remove();
    }
}
