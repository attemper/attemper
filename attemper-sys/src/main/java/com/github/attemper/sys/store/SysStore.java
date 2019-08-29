package com.github.attemper.sys.store;

import com.github.attemper.common.result.sys.tenant.Tenant;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.collections.map.MultiKeyMap;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SysStore {

    private static Tenant adminTenant;

    private static Map<String, String> tenantTokenMap = new ConcurrentHashMap<>();

    public static Map<String, String> getTenantTokenMap() {
        return tenantTokenMap;
    }

    public static Tenant getAdminTenant() {
        return adminTenant;
    }

    public static void setAdminTenant(Tenant adminTenant) {
        SysStore.adminTenant = adminTenant;
    }

    private static MultiKeyMap bizDataSourceMap = new MultiKeyMap();

    public static synchronized DataSource getBizDataSource(String dbName, String tenantId) {
        return (DataSource) bizDataSourceMap.get(dbName, tenantId);
    }

    public static synchronized void putBizDataSource(String dbName, String tenantId, DataSource dataSource) {
        bizDataSourceMap.put(dbName, tenantId, dataSource);
    }

    public static synchronized void removeBizDataSource(String dbName, String tenantId) {
        if (bizDataSourceMap.containsKey(dbName, tenantId)) {
            HikariDataSource dataSource = (HikariDataSource) bizDataSourceMap.remove(dbName, tenantId);
            if (dataSource.isRunning() || !dataSource.isClosed()) {
                dataSource.close();
            }
            dataSource = null;
        }
    }
}
