package com.github.attemper.executor.conf;

import com.github.attemper.executor.camunda.incident.CustomJobIncidentHandler;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.runtime.Incident;
import org.camunda.bpm.spring.boot.starter.configuration.Ordering;
import org.camunda.bpm.spring.boot.starter.configuration.impl.AbstractCamundaConfiguration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Order(Ordering.DEFAULT_ORDER - 1)
public class CustomProcessEngineConfiguration extends AbstractCamundaConfiguration {

    @Override
    public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        processEngineConfiguration.setCmmnEnabled(false);
        processEngineConfiguration.setDmnEnabled(false);
        processEngineConfiguration.setDbIdentityUsed(false);
        processEngineConfiguration.setCustomIncidentHandlers(Arrays.asList(new CustomJobIncidentHandler(Incident.FAILED_JOB_HANDLER_TYPE)));
    }
}
