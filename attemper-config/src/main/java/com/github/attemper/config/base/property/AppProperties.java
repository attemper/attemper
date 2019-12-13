package com.github.attemper.config.base.property;

import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String schema = "http://";

    private int jucScheduledPoolSize = 10;

    @NestedConfigurationProperty
    private final LocaleInfo localeInfo = new LocaleInfo();

    @NestedConfigurationProperty
    private final WebConfig web = new WebConfig();

    @NestedConfigurationProperty
    private final SchedulerConfig scheduler = new SchedulerConfig();

    @NestedConfigurationProperty
    private final ExecutorConfig executor = new ExecutorConfig();

    @NestedConfigurationProperty
    private final SnowFlake snowFlake = new SnowFlake();

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public int getJucScheduledPoolSize() {
        return jucScheduledPoolSize;
    }

    public void setJucScheduledPoolSize(int jucScheduledPoolSize) {
        this.jucScheduledPoolSize = jucScheduledPoolSize;
    }

    public LocaleInfo getLocaleInfo() {
        return localeInfo;
    }

    public WebConfig getWeb() {
        return web;
    }

    public SchedulerConfig getScheduler() {
        return scheduler;
    }

    public ExecutorConfig getExecutor() {
        return executor;
    }

    public SnowFlake getSnowFlake() {
        return snowFlake;
    }

    public static class LocaleInfo {

        private String language = "zh";

        // nation or area
        private String country = "CN";

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }

    public static class WebConfig {

        private boolean enableScheduling;

        public boolean isEnableScheduling() {
            return enableScheduling;
        }

        public void setEnableScheduling(boolean enableScheduling) {
            this.enableScheduling = enableScheduling;
        }
    }

    public static class SchedulerConfig {
        private String name;

        private String contextPath;

        private int delayedInSecond;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContextPath() {
            return StringUtils.trimToEmpty(contextPath);
        }

        public void setContextPath(String contextPath) {
            this.contextPath = contextPath;
        }

        public int getDelayedInSecond() {
            return delayedInSecond;
        }

        public void setDelayedInSecond(int delayedInSecond) {
            this.delayedInSecond = delayedInSecond;
        }
    }

    public static class ExecutorConfig {
        private String name;

        private String contextPath;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        
        public String getContextPath() {
            return StringUtils.trimToEmpty(contextPath);
        }

        public void setContextPath(String contextPath) {
            this.contextPath = contextPath;
        }
    }

    public static class SnowFlake {

        private Long dataCenterId;

        private Long machineId;

        public Long getDataCenterId() {
            return dataCenterId;
        }

        public void setDataCenterId(Long dataCenterId) {
            this.dataCenterId = dataCenterId;
        }

        public Long getMachineId() {
            return machineId;
        }

        public void setMachineId(Long machineId) {
            this.machineId = machineId;
        }
    }

}
