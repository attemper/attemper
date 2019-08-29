package com.github.attemper.executor.task.db;

import com.github.attemper.common.exception.RTException;
import com.github.attemper.executor.task.Databasing;
import com.github.attemper.executor.task.ParentTask;
import org.apache.commons.lang.StringUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Task to handle sql
 */
@Component
public class SqlTask extends ParentTask implements JavaDelegate, Databasing {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        JdbcTemplate jdbcTemplate = injectJdbcTemplate(execution);
        String sql = injectSql(execution);
        int code;
        if (StringUtils.isBlank(sql)) {
            code = 1202;
            saveLogKey(execution, code);
            throw new RTException(code);
        } else {
            String startStr = sql.substring(0, 6).toLowerCase();
            //param must be from input parameter, otherwise it should be constant
            switch (startStr) {
                case SELECT:
                    List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);
                    break;
                case UPDATE:
                case DELETE:
                    int count = jdbcTemplate.update(sql);
                    appendLogText(execution, startStr + " " + count + ' ');
                    break;
                case INSERT:
                    //int[] counts = insert(execution, sql);
                    break;
                default:
                    code = 1203;
                    saveLogKey(execution, code);
                    throw new RTException(code, sql);
            }
        }
    }
}
