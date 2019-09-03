package com.github.attemper.executor.service;

import com.github.attemper.common.param.IdParam;
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

    public Void terminate(IdParam param) {
        Instance instance = getInstance(param.getId());
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

    private Instance getInstance(String id) {
        return instanceService.get(id);
    }
}
