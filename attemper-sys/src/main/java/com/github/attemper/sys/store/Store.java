package com.github.attemper.sys.store;

import com.github.attemper.common.result.sys.tenant.Tenant;

public class Store {

    private static Tenant adminTenant;

    public static Tenant getAdminTenant() {
        return adminTenant;
    }

    public static void setAdminTenant(Tenant adminTenant) {
        Store.adminTenant = adminTenant;
    }
}
