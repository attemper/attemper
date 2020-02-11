package com.github.attemper.executor.task.db;

import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.util.ReflectUtil;
import com.github.attemper.executor.ext.enums.ResultDataType;
import com.github.attemper.executor.ext.param.ResultTypeSqlParam;
import com.github.attemper.executor.task.Databasing;
import com.github.attemper.executor.task.ParentTask;
import org.apache.commons.lang.StringUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Task to handle sql
 */
@Component
public class SqlTask extends ParentTask implements Databasing {

    @Override
    public void executeIntern(DelegateExecution execution) {
        ResultTypeSqlParam param = ReflectUtil.reflectObj(new ResultTypeSqlParam(), execution.getVariables());
        appendLogText(execution, 10002, param);
        String sql = param.getSql();
        if (StringUtils.isBlank(sql)) {
            throw new RTException(1202);
        }
        String startStr = sql.substring(0, 6).toLowerCase();
        //param must be from input parameter, otherwise it should be constant
        Object resultValue;
        switch (startStr) {
            case SELECT:
                resultValue = select(execution, param);
                execution.setVariable(KEY_RESULT_DATA, resultValue);
                break;
            case UPDATE:
            case DELETE:
            case INSERT:
                resultValue = execute(execution, param, startStr);
                break;
            default:
                throw new RTException(1203, sql);
        }
        execution.setVariable(KEY_RESULT_DATA, resultValue);
    }

    /**
     * select
     * @param execution
     * @param param
     * @return
     */
    private Object select(DelegateExecution execution, ResultTypeSqlParam param) {
        Object resultValue = null;
        JdbcTemplate jdbcTemplate = injectJdbcTemplate(param.getDbName(), execution.getTenantId());
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(param.getSql());
        switch (ResultDataType.get(param.getResultType())) {
            case OBJECT:
                if (mapList.size() > 0) {
                    if (mapList.size() != 1) {
                        throw new RTException(1205, mapList);
                    } else if (mapList.get(0).size() != 1) {
                        throw new RTException(1204, mapList.get(0));
                    } else {
                        for (Map.Entry<String, Object> entry : mapList.get(0).entrySet()) {
                            resultValue = entry.getValue();
                        }
                    }
                }
                appendLogText(execution, 10301, resultValue);
                break;
            case MAP:
                if (mapList.size() > 0) {
                    if (mapList.size() != 1) {
                        throw new RTException(1205, mapList);
                    }
                    resultValue = mapList.get(0);
                    execution.setVariables(mapList.get(0));  // It may be useful
                }
                appendLogText(execution, 10302, resultValue);
                break;
            default:
                resultValue = mapList;
                appendLogText(execution, 10303, mapList.size(), mapList.size() > 0 ? mapList.get(0).size() : 0);
                break;
        }
        return resultValue;
    }

    /**
     * insert/update/delete
     * @param execution
     * @param param
     * @param startStr
     * @return
     */
    private int execute(DelegateExecution execution, ResultTypeSqlParam param, String startStr) {
        if (StringUtils.isBlank(param.getDbName())) {
            throw new RTException(8000, param.getSql());
        }
        JdbcTemplate jdbcTemplate = injectJdbcTemplate(param.getDbName(), execution.getTenantId());
        int resultValue = jdbcTemplate.update(param.getSql());
        appendLogText(execution, 10300, startStr, resultValue);
        return resultValue;
    }

    private static final String KEY_RESULT_DATA = "resultData";
}
