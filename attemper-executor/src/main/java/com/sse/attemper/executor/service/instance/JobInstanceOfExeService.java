package com.sse.attemper.executor.service.instance;

import com.sse.attemper.common.result.dispatch.monitor.JobInstance;
import com.sse.attemper.common.result.dispatch.monitor.JobInstanceAct;
import com.sse.attemper.core.dao.mapper.monitor.JobInstanceMapper;
import com.sse.attemper.executor.service.BaseOfExeServiceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class JobInstanceOfExeService extends BaseOfExeServiceAdapter {

    @Autowired
    private JobInstanceMapper mapper;

    @Async
    public void add(JobInstance jobInstance) {
        mapper.add(jobInstance);
    }

    @Async
    public void update(JobInstance jobInstance) {
        mapper.update(jobInstance);
    }

    @Async
    public void addAct(JobInstanceAct jobInstanceAct) {
        mapper.addAct(jobInstanceAct);
    }

    @Async
    public void updateAct(JobInstanceAct jobInstanceAct) {
        mapper.updateAct(jobInstanceAct);
    }
}
