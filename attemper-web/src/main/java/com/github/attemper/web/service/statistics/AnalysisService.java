package com.github.attemper.web.service.statistics;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.param.statistics.InstanceDurationParam;
import com.github.attemper.common.result.dispatch.instance.Instance;
import com.github.attemper.common.result.statistics.analysis.AppPlan;
import com.github.attemper.web.dao.statistics.AnalysisMapper;
import com.github.attemper.java.sdk.common.util.DateUtil;
import com.github.attemper.sys.service.BaseServiceAdapter;
import com.github.attemper.sys.util.PageUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AnalysisService extends BaseServiceAdapter {

    @Autowired
    private AnalysisMapper mapper;

    public List<AppPlan> getAppPlan() {
        return mapper.getNextFireTimeWithJobName(injectTenantIdExceptAdminToMap(null));
    }

    public Map<String, Object> getInstanceDuration(InstanceDurationParam param) {
        Map<String, Object> resultMap = new HashMap<>(2);
        Map<String, Object> paramMap = injectTenantIdExceptAdminToMap(param);
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        Page<Instance> instances = (Page<Instance>) mapper.getInstanceDuration(paramMap);
        resultMap.put(CommonConstants.page, PageUtil.toPageResult(instances));
        List<String> startTimes = new ArrayList<>(instances.size());
        List<Long> durations = new ArrayList<>(instances.size());
        for (Instance instance : instances) {
            startTimes.add(DateUtil.format(new Date(instance.getStartTime()), "yyyy-MM-dd\nHH:mm:ss"));
            if (instance.getDuration() <= 0) {
                durations.add(0L);
            } else if (instance.getDuration() > 0 && instance.getDuration() < 1000) {
                durations.add(1L);
            } else {
                durations.add(instance.getDuration()/1000);
            }
        }
        resultMap.put("startTimes", startTimes);
        resultMap.put("durations", durations);
        return resultMap;
    }
}
