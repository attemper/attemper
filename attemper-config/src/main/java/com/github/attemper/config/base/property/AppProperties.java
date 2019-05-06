package com.github.attemper.config.base.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(prefix = "app")
public class AppProperties {

    @NestedConfigurationProperty
    private final WebConfig web = new WebConfig();

    @NestedConfigurationProperty
    private final SchedulerConfig scheduler = new SchedulerConfig();

    @NestedConfigurationProperty
    private final SnowFlake snowFlake = new SnowFlake();

    public WebConfig getWeb() {
        return web;
    }

    public SchedulerConfig getScheduler() {
        return scheduler;
    }

    public SnowFlake getSnowFlake() {
        return snowFlake;
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
            return contextPath;
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
