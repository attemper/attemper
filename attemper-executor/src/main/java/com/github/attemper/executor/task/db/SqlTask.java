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

import java.util.*;

/**
 * Task to handle sql
 */
@Component
public class SqlTask extends ParentTask implements Databasing {

    @Override
    public void executeIntern(DelegateExecution execution) {
        ResultTypeSqlParam param = ReflectUtil.reflectObj(ResultTypeSqlParam.class, execution.getVariables());
        appendLogText(execution, 10002, param);
        String sql = param.getSql();
        if (StringUtils.isBlank(sql)) {
            throw new RTException(1202);
        }
        int emptyLoc = sql.indexOf(" ");
        if (emptyLoc == -1) {
            throw new RTException(1203, sql);
        }
        String startStr = sql.substring(0, emptyLoc).toLowerCase();
        //param must be from input parameter, otherwise it should be constant
        switch (startStr) {
            case SELECT:
                select(execution, param);
                break;
            case UPDATE:
            case DELETE:
            case INSERT:
                update(execution, param, startStr);
                break;
            default:
                execute(execution, param);
        }
    }

    /**
     * select
     * @param execution
     * @param param
     */
    private void select(DelegateExecution execution, ResultTypeSqlParam param) {
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
                            execution.setVariable(entry.getKey(), resultValue);  // Can use all key-value and resultData-value
                            execution.setVariable(KEY_RESULT_DATA, resultValue);
                            break;
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
                    execution.setVariables(mapList.get(0));  // Can use all key-value in the map
                    execution.setVariable(KEY_RESULT_MAP, resultValue);
                }
                appendLogText(execution, 10302, resultValue);
                break;
            default:
                resultValue = mapList;
                appendLogText(execution, 10303, mapList.size(), mapList.size() > 0 ? mapList.get(0).size() : 0);
                execution.setVariable(KEY_RESULT_LIST, resultValue);
                break;
        }
    }

    /**
     * insert/update/delete
     * @param execution
     * @param param
     * @param startStr
     */
    private void update(DelegateExecution execution, ResultTypeSqlParam param, String startStr) {
        if (StringUtils.isBlank(param.getDbName())) {
            throw new RTException(8000, param.getSql());
        }
        JdbcTemplate jdbcTemplate = injectJdbcTemplate(param.getDbName(), execution.getTenantId());
        int resultValue;
        if (INSERT.equals(startStr) && param.getSql().contains("?")) { // use batchUpdate
            Object obj = execution.getVariable(KEY_RESULT_LIST);
            if (obj == null) {
                appendLogText(execution, 10310);
            } else {
                List<Object[]> paramList = new LinkedList<>();
                List<Map<String, Object>> resultDataList = (List<Map<String, Object>>) obj;
                if (resultDataList.isEmpty()) {
                    appendLogText(execution, 10311);
                } else {
                    for (Map<String, Object> map : resultDataList) {
                        paramList.add(transMapToArray(map));
                    }
                    int[] addedRows = jdbcTemplate.batchUpdate(param.getSql(), paramList);
                    resultValue = Arrays.stream(addedRows).sum();
                    appendLogText(execution, 10300, startStr, resultValue);
                }
            }
        } else {
            resultValue = jdbcTemplate.update(param.getSql());
            appendLogText(execution, 10300, startStr, resultValue);
        }
    }

    /**
     * create/drop/alter/truncate
     * @param execution
     * @param param
     */
    private void execute(DelegateExecution execution, ResultTypeSqlParam param) {
        if (StringUtils.isBlank(param.getDbName())) {
            throw new RTException(8000, param.getSql());
        }
        JdbcTemplate jdbcTemplate = injectJdbcTemplate(param.getDbName(), execution.getTenantId());
        jdbcTemplate.execute(param.getSql());
    }

    private Object[] transMapToArray(Map<String, Object> resultDataObj) {
        Collection collection = (resultDataObj).values();
        Object[] os = new Object[collection.size()];
        int i = 0;
        for (Object o : collection) {
            os[i] = o;
            i++;
        }
        return os;
    }

    private static final String KEY_RESULT_DATA = "resultData";

    private static final String KEY_RESULT_MAP = "resultMap";

    private static final String KEY_RESULT_LIST = "resultList";
}
