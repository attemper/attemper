package com.sse.attemper.core.service.monitor;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sse.attemper.common.param.dispatch.monitor.JobInstanceActParam;
import com.sse.attemper.common.param.dispatch.monitor.JobInstanceListParam;
import com.sse.attemper.common.result.dispatch.monitor.JobInstance;
import com.sse.attemper.common.result.dispatch.monitor.JobInstanceAct;
import com.sse.attemper.core.dao.mapper.monitor.JobInstanceMapper;
import com.sse.attemper.sys.service.BaseServiceAdapter;
import com.sse.attemper.sys.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MonitorService extends BaseServiceAdapter {

    @Autowired
    private JobInstanceMapper mapper;

    public Map<String, Object> list(JobInstanceListParam param) {
        Map<String, Object> paramMap = injectAdminTenantIdToMap(param);
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        Page<JobInstance> list = (Page<JobInstance>) mapper.list(paramMap);
        return PageUtil.toResultMap(list);
    }

    public List<JobInstanceAct> listAct(JobInstanceActParam param) {
        Map<String, Object> paramMap = injectAdminTenantIdToMap(param);
        return mapper.listAct(paramMap);
    }
}
