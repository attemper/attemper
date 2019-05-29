package com.github.attemper.sys.store;

import com.github.attemper.common.result.sys.tenant.Tenant;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Store {

    private static Tenant adminTenant;

    private static Map<String, String> tenantTokenMap = new ConcurrentHashMap<>();

    public static Map<String, String> getTenantTokenMap() {
        return tenantTokenMap;
    }

    public static void setTenantTokenMap(Map<String, String> tenantTokenMap) {
        Store.tenantTokenMap = tenantTokenMap;
    }

    public static Tenant getAdminTenant() {
        return adminTenant;
    }

    public static void setAdminTenant(Tenant adminTenant) {
        Store.adminTenant = adminTenant;
    }
}
