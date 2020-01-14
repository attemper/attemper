package com.github.attemper.core.ext.condition;

import com.github.attemper.common.enums.ConditionType;
import com.github.attemper.common.result.dispatch.condition.Condition;
import com.github.attemper.common.result.dispatch.condition.sub.SqlConditionResult;
import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.config.base.util.BeanUtil;
import com.github.attemper.core.ext.el.ELUtil;
import com.github.attemper.core.service.dispatch.DataSourceService;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@ConditionStrategyType(ConditionType.SQL)
public class SqlConditionStrategy implements ConditionStrategy<SqlConditionResult> {

    @Override
    public boolean validate(Condition condition, Map<String, Object> variableMap) {
        SqlConditionResult sqlConditionResult = parse(condition);
        String dbName = sqlConditionResult.getDbName();
        DataSourceService dataSourceService = SpringContextAware.getBean(DataSourceService.class);
        DataSource dataSource = dataSourceService.getDataSource(dbName, condition.getTenantId());
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String parsedSql = ELUtil.parseExpression(sqlConditionResult.getSql(), variableMap);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(parsedSql);
        return list.size() > 0;
    }

    @Override
    public SqlConditionResult parse(Condition condition) {
        SqlConditionResult conditionResult =
                BeanUtil.jsonStr2Bean(condition.getContent(), SqlConditionResult.class);
        conditionResult
                .setConditionName(condition.getConditionName())
                .setTenantId(condition.getTenantId());
        return conditionResult;
    }

}
