package com.github.attemper.executor.disruptor.consumer;

import com.github.attemper.common.enums.ArgType;
import com.github.attemper.common.enums.JobInstanceStatus;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.dispatch.arg.ext.SqlArgParam;
import com.github.attemper.common.param.dispatch.arg.ext.TradeDateArgParam;
import com.github.attemper.common.param.executor.JobInvokingParam;
import com.github.attemper.common.property.StatusProperty;
import com.github.attemper.common.result.dispatch.arg.Arg;
import com.github.attemper.common.result.dispatch.instance.JobInstance;
import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.core.service.arg.ArgService;
import com.github.attemper.core.service.instance.JobInstanceService;
import com.github.attemper.core.service.job.JobService;
import com.github.attemper.executor.disruptor.event.JobEvent;
import com.lmax.disruptor.WorkHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.repository.ProcessDefinition;

import java.util.*;

/**
 * consumer of request
 */
@Slf4j
public class RequestConsumer implements WorkHandler<JobEvent> {

    @Override
    public void onEvent(JobEvent event) throws Exception {
        RepositoryService repositoryService = SpringContextAware.getBean(RepositoryService.class);
        RuntimeService runtimeService = SpringContextAware.getBean(RuntimeService.class);
        JobInvokingParam param = event.getParam();
        try {
            List<ProcessDefinition> processDefinitions = repositoryService
                    .createProcessDefinitionQuery()
                    .processDefinitionKey(param.getJobName())
                    .tenantIdIn(param.getTenantId())
                    .latestVersion()
                    .list();
            if (processDefinitions.size() == 1) {
                ProcessDefinition processDefinition = processDefinitions.get(0);
                runtimeService.startProcessInstanceById(processDefinition.getId(), param.getId(), buildVariableMap(param));
            } else if (processDefinitions.isEmpty()) {
                log.error("process not exists:{}|{}|{}", param.getId(), param.getJobName(), param.getTenantId());
                throw new RTException(2002);
            } else {
                log.error("process more than 1:{}|{}|{}", param.getId(), param.getJobName(), param.getTenantId());
                throw new RTException(2003);
            }
        } catch (Exception e) {
            if (e instanceof RTException) {
                RTException rte = (RTException) e;
                updateInstance(param, rte.getCode(), rte.getMsg());
                throw rte;
            } else {
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
    }

    private Map<String, Object> buildVariableMap(JobInvokingParam param) {
        Map<String, Object> varMap = new HashMap<>();
        JobService jobService = SpringContextAware.getBean(JobService.class);
        ArgService argService = SpringContextAware.getBean(ArgService.class);
        List<Arg> args = jobService.getAllArg(param.getJobName(), param.getTenantId());
        for (Arg arg : args) {
            if (arg.getArgValue() != null) {
                if (ArgType.get(arg.getArgType()) == null) {
                    arg.setArgType(ArgType.STRING.getType());
                }
                Object realArgValue;
                switch (ArgType.get(arg.getArgType())) {
                    case TRADE_DATE:
                        realArgValue = argService.getTradeDate(
                                new TradeDateArgParam()
                                        .setCalendarName(arg.getAttribute())
                                        .setExpression(arg.getArgValue()));
                        break;
                    case SQL:
                        realArgValue = argService.getSqlResult(
                                new SqlArgParam()
                                        .setDbName(arg.getAttribute())
                                        .setSql(arg.getArgValue()));
                        break;
                    case LIST:
                        realArgValue = toListValue(arg);
                        break;
                    case MAP:
                        realArgValue = toMapValue(arg);
                        break;
                    default: //string date datetime time
                        realArgValue = toGenericValue(arg.getArgType(), arg.getArgValue());
                        break;
                }
                varMap.put(arg.getArgName(), realArgValue);
            }
        }
        return varMap;
    }

    private Object toGenericValue(int argType, String argValue) {
        if (ArgType.get(argType) == null) {
            argType = ArgType.STRING.getType();
        }
        try {
            switch (ArgType.get(argType)) {
                case BOOLEAN:
                    return Boolean.parseBoolean(argValue);
                case INTEGER:
                    return Integer.parseInt(argValue);
                case DOUBLE:
                    return Double.parseDouble(argValue);
                case LONG:
                    return Long.parseLong(argValue);
                default: //string date datetime time
                    return argValue;
            }
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
            return argValue;
        }
    }

    private List<Object> toListValue(Arg arg) {
        List<Object> list = new ArrayList<>();
        String[] values = arg.getArgValue().split(",,");
        for (String value : values) {
            list.add(toGenericValue(arg.getGenericType(), value));
        }
        return list;
    }

    private Map<String, Object> toMapValue(Arg arg) {
        Map<String, Object> map = new HashMap<>();
        String[] entries = arg.getArgValue().split(",,");
        for (String entry : entries) {
            String[] keyValueArray = entry.split("::");
            if (keyValueArray.length > 1) {
                map.put(keyValueArray[0], toGenericValue(arg.getGenericType(), keyValueArray[1]));
            }
        }
        return map;
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
