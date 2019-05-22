package com.github.attemper.web.service;

import com.github.attemper.common.result.dispatch.job.Job;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
public class CamundaHandler {

    @Autowired
    private RepositoryService repositoryService;

    public Deployment createDefault(Job job) {
        return repositoryService.createDeployment()
                .addModelInstance(job.getJobName() + ".bpmn20.xml",
                        Bpmn.readModelFromStream(new ByteArrayInputStream(job.getJobContent().getBytes())))
                .name(job.getDisplayName())
                .tenantId(job.getTenantId())
                .deploy();
    }

    @Async
    public void removeDefinitionAndDeployment(List<String> jobNames, String tenantId) {
        jobNames.forEach(jobName -> {
            List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionKey(jobName)
                    .tenantIdIn(tenantId)
                    .list();
            for (ProcessDefinition processDefinition : processDefinitions) {
                repositoryService.deleteProcessDefinition(processDefinition.getId());
                repositoryService.deleteDeployment(processDefinition.getDeploymentId());
            }
        });
    }
}
