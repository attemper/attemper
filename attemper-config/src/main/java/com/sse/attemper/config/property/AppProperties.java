package com.sse.attemper.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(prefix = "app")
public class AppProperties {

    @NestedConfigurationProperty
    private SchedulerConfig scheduler;

    public SchedulerConfig getScheduler() {
        return scheduler;
    }

    public void setScheduler(SchedulerConfig scheduler) {
        this.scheduler = scheduler;
    }

    public static class SchedulerConfig {
        private String name;

        private String contextPath;

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
    }

}
