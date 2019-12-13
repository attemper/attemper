package com.github.attemper.core.service.dispatch;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.enums.ArgType;
import com.github.attemper.common.enums.ConditionType;
import com.github.attemper.common.param.dispatch.arg.ext.SqlArgParam;
import com.github.attemper.common.param.dispatch.arg.ext.TradeDateArgParam;
import com.github.attemper.common.param.dispatch.condition.ConditionSaveParam;
import com.github.attemper.common.param.dispatch.condition.sub.ConditionCommonParam;
import com.github.attemper.common.param.dispatch.job.JobArgListParam;
import com.github.attemper.common.param.dispatch.job.JobNameParam;
import com.github.attemper.common.param.dispatch.job.JobProjectSaveParam;
import com.github.attemper.common.result.app.project.Project;
import com.github.attemper.common.result.dispatch.arg.Arg;
import com.github.attemper.common.result.dispatch.condition.Condition;
import com.github.attemper.common.result.dispatch.condition.ConditionResult;
import com.github.attemper.common.result.dispatch.condition.sub.FtpFileConditionResult;
import com.github.attemper.common.result.dispatch.condition.sub.LocalFileConditionResult;
import com.github.attemper.common.result.dispatch.condition.sub.SqlConditionResult;
import com.github.attemper.common.result.dispatch.job.ArgAllocatedResult;
import com.github.attemper.common.result.dispatch.job.Job;
import com.github.attemper.config.base.util.BeanUtil;
import com.github.attemper.core.dao.dispatch.JobMapper;
import com.github.attemper.core.ext.condition.ConditionStrategyService;
import com.github.attemper.sys.service.BaseServiceAdapter;
import com.github.attemper.sys.util.PageUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class JobService extends BaseServiceAdapter {

    @Autowired
    private JobMapper mapper;

    @Autowired
    private ConditionStrategyService conditionStrategyService;

    /**
     * get job by jobName
     * @param param
     * @return
     */
    public Job get(JobNameParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        return mapper.get(paramMap);
    }

    public Project getProject(JobNameParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        return mapper.getProject(paramMap);
    }

    public Void saveProject(JobProjectSaveParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        mapper.deleteProject(paramMap);
        if (StringUtils.isNotBlank(param.getProjectName())) {
            mapper.addProject(paramMap);
        }
        return null;
    }

    public Map<String, Object> listArg(JobArgListParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        Page<ArgAllocatedResult> list = (Page<ArgAllocatedResult>) mapper.listArg(paramMap);
        return PageUtil.toResultMap(list);
    }

    public Job get(String jobName, String tenantId) {
        return mapper.get(injectTenantIdToMap(new JobNameParam().setJobName(jobName), tenantId));
    }

    public List<Arg> getArg(String jobName, String tenantId) {
        return mapper.getArg(injectTenantIdToMap(new JobNameParam().setJobName(jobName), tenantId));
    }

    /**con
     * get project by jobName and tenantId
     *
     * @param jobName
     * @param tenantId
     * @return
     */
    public Project getProject(String jobName, String tenantId) {
        return mapper.getProject(injectTenantIdToMap(new JobNameParam().setJobName(jobName), tenantId));
    }

    public String getJsonArg(JobNameParam jobNameParam) {
        Map<String, Object> map = transArgToMap(jobNameParam.getJobName());
        return map.isEmpty() ? null : BeanUtil.bean2JsonStr(map);
    }

    @Autowired
    private ArgService argService;

    public Map<String, Object> transArgToMap(String jobName) {
        return transArgToMap(jobName, injectTenantId());
    }

    public Map<String, Object> transArgToMap(String jobName, String tenantId) {
        Map<String, Object> varMap = new HashMap<>();
        List<Arg> args = getArg(jobName, tenantId);
        for (Arg arg : args) {
            if (arg.getArgValue() != null) {
                if (ArgType.get(arg.getArgType()) == null) {
                    arg.setArgType(ArgType.STRING.getValue());
                }
                Object realArgValue;
                switch (ArgType.get(arg.getArgType())) {
                    case TRADE_DATE:
                        realArgValue = argService.getTradeDate(
                                new TradeDateArgParam()
                                        .setCalendarName(arg.getAttribute())
                                        .setExpression(arg.getArgValue()));
                        break;
                    case GIST:
                        realArgValue = argService.getGistCode(arg.getArgValue(), tenantId);
                        break;
                    case SQL:
                        List<Map<String, Object>> mapList = argService.getSqlResult(
                                new SqlArgParam()
                                        .setDbName(arg.getAttribute())
                                        .setSql(arg.getArgValue()));
                        if (mapList.size() == 1) {
                            realArgValue = mapList.get(0).values().toArray()[0];
                        } else {
                            realArgValue = mapList;
                        }
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
            argType = ArgType.STRING.getValue();
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

    public ConditionResult getCondition(JobNameParam param) {
        List<Condition> conditions = getConditions(param.getJobName());
        List<FtpFileConditionResult> ftpFileConditionResults = new ArrayList<>(4);
        List<LocalFileConditionResult> localFileConditionResults = new ArrayList<>(4);
        List<SqlConditionResult> sqlConditionResults = new ArrayList<>(4);
        for (Condition condition : conditions) {
            if (condition.getConditionType() == ConditionType.FTP_FILE.getValue()) {
                ftpFileConditionResults.add(
                        (FtpFileConditionResult) conditionStrategyService.get(
                                ConditionType.FTP_FILE.getValue()).parse(condition));
            } else if (condition.getConditionType() == ConditionType.LOCAL_FILE.getValue()) {
                localFileConditionResults.add(
                        (LocalFileConditionResult) conditionStrategyService.get(
                                ConditionType.LOCAL_FILE.getValue()).parse(condition));
            } else if (condition.getConditionType() == ConditionType.SQL.getValue()) {
                sqlConditionResults.add(
                        (SqlConditionResult) conditionStrategyService.get(
                                ConditionType.SQL.getValue()).parse(condition));
            }
        }
        return new ConditionResult()
                .setFtpFileConditions(ftpFileConditionResults)
                .setLocalFileConditions(localFileConditionResults)
                .setSqlConditions(sqlConditionResults);
    }

    public List<Condition> getConditions(String jobName) {
        return getConditions(jobName, injectTenantId());
    }

    public List<Condition> getConditions(String jobName, String tenantId) {
        return mapper.getConditions(injectTenantIdToMap(new JobNameParam().setJobName(jobName), tenantId));
    }

    public Void updateCondition(ConditionSaveParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        List<Condition> conditions = mapper.getConditions(paramMap);
        mapper.deleteJobCondition(paramMap);
        removeConditions(conditions);
        List<Map<String, Object>> mapList = new ArrayList<>(4);
        parseAndAddCondition(mapList, param.getFtpFileConditions(), param.getJobName());
        parseAndAddCondition(mapList, param.getLocalFileConditions(), param.getJobName());
        parseAndAddCondition(mapList, param.getSqlConditions(), param.getJobName());
        if (mapList.size() > 0) {
            mapper.addJobConditions(mapList);
            addConditions(mapList);
        }
        return null;
    }

    private void parseAndAddCondition(List<Map<String, Object>> mapList, List<? extends ConditionCommonParam> list,
                                      String jobName) {
        if (list != null && list.size() > 0) {
            for (ConditionCommonParam conditionParam : list) {
                Map<String, Object> map = BeanUtil.bean2Map(conditionParam);
                map.put(CommonConstants.jobName, jobName);
                map.put(CommonConstants.tenantId, injectTenantId());
                map.put(CommonConstants.content, BeanUtil.bean2JsonStr(conditionParam));
                mapList.add(map);
            }
        }
    }

    public void removeConditions(List<Condition> conditions) {
        Map<String, Object> paramMap = new HashMap<>(2);
        if (conditions.size() > 0) {
            List<String> conditionNames = conditions.stream().map(Condition::getConditionName).collect(Collectors.toList());
            paramMap.put("conditionNames", conditionNames);
            paramMap.put(CommonConstants.tenantId, injectTenantId());
            mapper.deleteCondition(paramMap);
        }
    }

    public void addConditions(List<Map<String, Object>> mapList) {
        mapper.addConditions(mapList);
    }
}
