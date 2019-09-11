package com.github.attemper.web.service.statistics;

import com.github.attemper.common.result.statistics.analysis.AppPlan;
import com.github.attemper.common.result.statistics.analysis.AppPlanWrapper;
import com.github.attemper.core.dao.statistics.AnalysisMapper;
import com.github.attemper.sys.service.BaseServiceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AnalysisOperatedService extends BaseServiceAdapter {

    @Autowired
    private AnalysisMapper mapper;

    public List<AppPlan> getAppPlan() {
        List<AppPlanWrapper> wrappers = mapper.getNextFireTimeWithJobName(injectTenantIdExceptAdminToMap(null));
        List<AppPlan> appPlans = new ArrayList<>(wrappers.size());
        for (AppPlanWrapper wrapper : wrappers) {
            AppPlan appPlan = new AppPlan()
                    .setJobName(wrapper.getJobName())
                    .setNextFireTime(new Date(wrapper.getNextFireTime()));
            if (wrapper.getPrevFireTime() != null && wrapper.getPrevFireTime() > 0) {
                appPlan.setPrevFireTime(new Date(wrapper.getPrevFireTime()));
            }
            appPlans.add(appPlan);
        }
        return appPlans;
    }

}
