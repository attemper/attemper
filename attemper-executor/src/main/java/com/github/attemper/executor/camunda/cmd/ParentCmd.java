package com.github.attemper.executor.camunda.cmd;

import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;

public abstract class ParentCmd implements Command<Void> {

   protected JdbcTemplate injectJdbcTemplate(CommandContext commandContext) {
       return new JdbcTemplate(commandContext.getProcessEngineConfiguration().getDataSource());
   }

   protected String getDbType(JdbcTemplate jdbcTemplate) throws SQLException {
       return jdbcTemplate.getDataSource().getConnection().getMetaData().getDatabaseProductName().toLowerCase();
   }
}
