package com.github.attemper.executor.service.instance;

import com.github.attemper.executor.service.BaseOfExeServiceAdapter;
import com.github.attemper.common.result.dispatch.monitor.JobInstance;
import com.github.attemper.common.result.dispatch.monitor.JobInstanceAct;
import com.github.attemper.core.dao.mapper.monitor.JobExecutionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class JobExecutionOfExeService extends BaseOfExeServiceAdapter {

    @Autowired
    private JobExecutionMapper mapper;

    @Async
    public void delete(JobInstance jobInstance) {
        mapper.delete(jobInstance);
    }

    @Async
    public void deleteAct(JobInstanceAct jobInstanceAct) {
        mapper.deleteAct(jobInstanceAct);
    }
}