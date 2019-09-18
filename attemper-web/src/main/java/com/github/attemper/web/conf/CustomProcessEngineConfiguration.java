package com.github.attemper.web.conf;

import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.spring.boot.starter.configuration.impl.AbstractCamundaConfiguration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(-1)
@Component
public class CustomProcessEngineConfiguration extends AbstractCamundaConfiguration {

    @Override
    public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        processEngineConfiguration.setCmmnEnabled(false);
        processEngineConfiguration.setDmnEnabled(false);
        processEngineConfiguration.setDbIdentityUsed(false);
    }
}
