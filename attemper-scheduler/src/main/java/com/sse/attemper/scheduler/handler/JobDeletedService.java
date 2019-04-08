package com.sse.attemper.scheduler.handler;

import com.sse.attemper.common.param.dispatch.job.BaseJobRemoveParam;
import com.sse.attemper.config.util.QuartzUtil;
import com.sse.attemper.sys.service.BaseServiceAdapter;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * update job or trigger in scheduler
 */
@Service
public class JobDeletedService extends BaseServiceAdapter {

    @Autowired
    private Scheduler scheduler;

    public Void deleteJob(BaseJobRemoveParam param) {
        QuartzUtil.deleteJobs(scheduler, param.getJobNames(), injectAdminedTenant().getId());
        return null;
    }
}
