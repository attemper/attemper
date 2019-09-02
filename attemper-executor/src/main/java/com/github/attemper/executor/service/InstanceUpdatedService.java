package com.github.attemper.executor.service;

import com.github.attemper.common.param.dispatch.instance.InstanceGetParam;
import com.github.attemper.common.param.dispatch.instance.InstanceIdParam;
import com.github.attemper.common.property.StatusProperty;
import com.github.attemper.common.result.dispatch.instance.Instance;
import com.github.attemper.core.service.instance.InstanceService;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InstanceUpdatedService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private InstanceService instanceService;

    public Void terminate(InstanceIdParam param) {
        Instance instance = getInstance(param);
        if (validateState(instance)) {
            runtimeService.deleteProcessInstanceIfExists(
                    instance.getProcInstId(),
                    StatusProperty.getValue(901),
                    true,
                    true,
                    false,
                    false);
        }
        return null;
    }

    private boolean validateState(Instance instance) {
        return instance.getProcInstId() != null;
    }

    private Instance getInstance(InstanceIdParam param) {
        return instanceService.get(new InstanceGetParam().setId(param.getId()));
    }
}
