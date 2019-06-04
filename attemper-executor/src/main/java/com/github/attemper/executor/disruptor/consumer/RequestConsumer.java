package com.github.attemper.executor.disruptor.consumer;

import com.github.attemper.common.enums.JobInstanceStatus;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.executor.JobInvokingParam;
import com.github.attemper.common.property.StatusProperty;
import com.github.attemper.common.result.dispatch.instance.JobInstance;
import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.core.service.instance.JobInstanceService;
import com.github.attemper.executor.disruptor.container.RequestContainer;
import com.lmax.disruptor.WorkHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.camunda.bpm.engine.RuntimeService;

import java.util.Date;

/**
 * consumer of request
 */
@Slf4j
public class RequestConsumer implements WorkHandler<RequestContainer> {

    @Override
    public void onEvent(RequestContainer container) throws Exception {
        RuntimeService runtimeService = SpringContextAware.getBean(RuntimeService.class);
        JobInvokingParam param = container.getParam();
        try {
            runtimeService.startProcessInstanceByKey(param.getJobName(), param.getId());
        } catch (Exception e) {
            int code;
            if (StringUtils.isNotBlank(param.getTriggerName())) {
                code = 2000;
            } else {
                code = 2001;
            }
            updateInstance(param, code, StatusProperty.getValue(code) + ":" + e.getMessage());
            throw new RTException(code, e);
        }
    }

    private void updateInstance(JobInvokingParam param, int code, String msg) {
        JobInstanceService jobInstanceService = SpringContextAware.getBean(JobInstanceService.class);
        JobInstance jobInstance = jobInstanceService.get(param.getId());
        Date now = new Date();
        jobInstance.setEndTime(now);
        jobInstance.setDuration(now.getTime() - jobInstance.getStartTime().getTime());
        jobInstance.setStatus(JobInstanceStatus.FAILURE.getStatus());
        jobInstance.setCode(code);
        jobInstance.setMsg(msg);
        jobInstanceService.update(jobInstance);
    }
}
