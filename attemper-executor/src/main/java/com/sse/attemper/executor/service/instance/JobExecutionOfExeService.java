package com.sse.attemper.executor.service.instance;

import com.sse.attemper.common.result.dispatch.monitor.JobExecution;
import com.sse.attemper.common.result.dispatch.monitor.JobExecutionAct;
import com.sse.attemper.core.dao.mapper.monitor.JobExecutionMapper;
import com.sse.attemper.executor.service.BaseOfExeServiceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class JobExecutionOfExeService extends BaseOfExeServiceAdapter {

    @Autowired
    private JobExecutionMapper mapper;

    public void add(JobExecution jobExecution) {
        mapper.add(jobExecution);
    }

    public void update(JobExecution jobExecution) {
        mapper.update(jobExecution);
    }

    public void delete(JobExecution jobExecution) {
        mapper.delete(jobExecution);
    }

    public void addAct(JobExecutionAct jobExecutionAct) {
        mapper.addAct(jobExecutionAct);
    }

    public void updateAct(JobExecutionAct jobExecutionAct) {
        mapper.updateAct(jobExecutionAct);
    }

    public void deleteAct(JobExecutionAct jobExecutionAct) {
        mapper.deleteAct(jobExecutionAct);
    }
}
