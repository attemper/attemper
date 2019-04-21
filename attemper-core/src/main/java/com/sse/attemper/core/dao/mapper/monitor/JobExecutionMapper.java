package com.sse.attemper.core.dao.mapper.monitor;

import com.sse.attemper.common.base.BaseMapper;
import com.sse.attemper.common.result.dispatch.monitor.JobExecution;
import com.sse.attemper.common.result.dispatch.monitor.JobExecutionAct;
import com.sse.attemper.common.result.dispatch.monitor.JobInst;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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

    List<JobInst> list(Map<String, Object> paramMap);
}
