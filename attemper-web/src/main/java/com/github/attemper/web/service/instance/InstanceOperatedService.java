package com.github.attemper.web.service.instance;

import com.github.attemper.common.enums.InstanceStatus;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.dispatch.instance.InstanceActArgParam;
import com.github.attemper.common.param.dispatch.instance.InstanceIdParam;
import com.github.attemper.common.param.dispatch.instance.InstanceJsonArgParam;
import com.github.attemper.common.property.StatusProperty;
import com.github.attemper.common.result.dispatch.instance.Instance;
import com.github.attemper.config.base.util.BeanUtil;
import com.github.attemper.core.service.instance.InstanceService;
import com.github.attemper.invoker.service.JobCallingService;
import com.github.attemper.web.ext.app.ExecutorHandler;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.history.HistoricVariableInstanceQuery;
import org.camunda.bpm.engine.impl.cfg.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class InstanceOperatedService {

    @Autowired
    private InstanceService instanceService;

    @Autowired
    private JobCallingService jobCallingService;

    @Autowired
    private ExecutorHandler executorHandler;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private HistoryService historyService;

    public String getInstanceArgs(InstanceActArgParam param) {
        HistoricVariableInstanceQuery varQuery = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(param.getProcInstId())
                .activityInstanceIdIn(param.getActInstId());
        List<HistoricVariableInstance> vars = varQuery.list();
        if (vars.size() == 0) {
            return null;
        }
        Map<String, Object> instArgMap = new HashMap<>(vars.size());
        for (HistoricVariableInstance var : vars) {
            instArgMap.put(var.getName(), var.getValue());
        }
        return BeanUtil.bean2JsonStr(instArgMap);
    }

    public Void retry(InstanceJsonArgParam param) {
        Instance instance = getInstance(param.getProcInstId());
        if (!isDone(instance.getStatus())) {
            throw new RTException(6202, instance.getStatus());
        }
        String parentId = instance.getId();
        if (instance.getParentId() == null) {
            instance.setParentId(instance.getId());
            instanceService.updateDone(instance);
        }
        jobCallingService.retry(idGenerator.getNextId(), instance.getJobName(), instance.getTenantId(), parentId, param.getBeforeActIds(), param.getAfterActIds(), BeanUtil.bean2Map(param.getJsonData()));
        return null;
    }

    public Void terminate(InstanceIdParam param) {
        Instance instance = getInstance(param.getProcInstId());
        if (!isDoing(instance.getStatus())) {
            throw new RTException(6202, instance.getStatus());
        }
        executorHandler.terminate(instance.getExecutorUri(), instance);
        int code = 901;
        instance.setCode(code);
        instance.setMsg(StatusProperty.getValue(code));
        updateInstanceStatus(instance, InstanceStatus.TERMINATED.getStatus());
        return null;
    }

    private void updateInstanceStatus(Instance instance, int status) {
        instance.setStatus(status);
        instanceService.updateDone(instance);
    }

    private boolean isDoing(int status) {
        return status == InstanceStatus.RUNNING.getStatus();
    }

    private boolean isDone(int status) {
        return status == InstanceStatus.SUCCESS.getStatus() ||
                status == InstanceStatus.FAILURE.getStatus() ||
                status == InstanceStatus.TERMINATED.getStatus();
    }

    private Instance getInstance(String procInstId) {
        Instance instance = instanceService.getByInstId(procInstId);
        if (instance == null) {
            throw new RTException(6201);
        }
        return instance;
    }
}
