package com.sse.attemper.core.dao.mapper.monitor;

import com.sse.attemper.common.base.BaseMapper;
import com.sse.attemper.common.result.dispatch.monitor.JobExecution;
import com.sse.attemper.common.result.dispatch.monitor.JobExecutionAct;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface JobExecutionMapper extends BaseMapper<JobExecution> {

    @Override
    void add(JobExecution jobExecution);

    void addAct(JobExecutionAct jobExecutionAct);

    void delete(JobExecution jobExecution);

    @Override
    void update(JobExecution jobExecution);

    void updateAct(JobExecutionAct jobExecutionAct);

    void deleteAct(JobExecutionAct jobExecutionAct);
}
