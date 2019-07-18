package com.github.attemper.executor.camunda.cmd;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

@Slf4j
public class UpdateHistoricInstanceCmd extends ParentCmd {

    private static final String sql =
            "UPDATE act_hi_procinst\n" +
                    "SET END_TIME_ = ?, DURATION_ = ?, STATE_ = ?\n" +
                    "WHERE PROC_INST_ID_ = ?";

    protected String procInstId;

    protected Date endTime;

    protected long duration;

    protected String state;

    public UpdateHistoricInstanceCmd(String procInstId, Date endTime, long duration, String state) {
        this.procInstId = procInstId;
        this.endTime = endTime;
        this.duration = duration;
        this.state = state;
    }

    @Override
    public Void execute(CommandContext commandContext) {
        JdbcTemplate jdbcTemplate = super.injectJdbcTemplate(commandContext);
        try {
            jdbcTemplate.update(sql, endTime, duration, state, procInstId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
