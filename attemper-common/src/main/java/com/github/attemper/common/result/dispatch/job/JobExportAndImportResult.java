package com.github.attemper.common.result.dispatch.job;

import com.github.attemper.common.param.dispatch.trigger.TriggerWrapper;

import java.util.List;

public class JobExportAndImportResult {

    protected Job job;

    protected List<String> args;

    protected List<String> conditions;

    protected TriggerWrapper trigger;

    public Job getJob() {
        return job;
    }

    public JobExportAndImportResult setJob(Job job) {
        this.job = job;
        return this;
    }

    public List<String> getArgs() {
        return args;
    }

    public JobExportAndImportResult setArgs(List<String> args) {
        this.args = args;
        return this;
    }

    public List<String> getConditions() {
        return conditions;
    }

    public JobExportAndImportResult setConditions(List<String> conditions) {
        this.conditions = conditions;
        return this;
    }

    public TriggerWrapper getTrigger() {
        return trigger;
    }

    public JobExportAndImportResult setTrigger(TriggerWrapper trigger) {
        this.trigger = trigger;
        return this;
    }
}
