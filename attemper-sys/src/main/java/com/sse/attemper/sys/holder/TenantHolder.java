package com.sse.attemper.sys.holder;

import com.sse.attemper.common.result.sys.tenant.Tenant;

/**
 * the tenant which user admin it
 */
public class TenantHolder {
    
    private static final ThreadLocal<Tenant> contextHolder = new ThreadLocal<Tenant>();

    public static void set(Tenant tenant) {
        contextHolder.set(tenant);
    }

    public static Tenant get() {
        return contextHolder.get();
    }

    public static void clear() {
        contextHolder.remove();
    }
}
