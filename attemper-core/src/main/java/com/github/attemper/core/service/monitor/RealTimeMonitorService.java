package com.github.attemper.core.service.monitor;

import com.github.attemper.common.param.dispatch.monitor.JobInstanceListParam;
import com.github.attemper.common.result.dispatch.monitor.JobInstance;
import com.github.attemper.core.dao.mapper.monitor.JobExecutionMapper;
import com.github.attemper.sys.service.BaseServiceAdapter;
import com.github.attemper.sys.util.PageUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RealTimeMonitorService extends BaseServiceAdapter {

    @Autowired
    private JobExecutionMapper mapper;

    public Map<String, Object> list(JobInstanceListParam param) {
        Map<String, Object> paramMap = injectAdminTenantIdToMap(param);
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        Page<JobInstance> list = (Page<JobInstance>) mapper.list(paramMap);
        return PageUtil.toResultMap(list);
    }
}
