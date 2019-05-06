package com.github.attemper.executor.task.sample1;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

@Slf4j
public class Step1 implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.debug("{} execute begin {}", execution.getBpmnModelInstance().getModel().getModelName(), execution.getCurrentActivityId());
        Thread.sleep(20000L);
        log.debug("{} execute end {}", execution.getBpmnModelInstance().getModel().getModelName(), execution.getCurrentActivityId());
    }
}
