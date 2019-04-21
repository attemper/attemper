package com.sse.attemper.core.service.monitor;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sse.attemper.common.param.dispatch.monitor.JobInstListParam;
import com.sse.attemper.common.result.dispatch.monitor.JobInst;
import com.sse.attemper.core.dao.mapper.monitor.JobInstanceMapper;
import com.sse.attemper.sys.service.BaseServiceAdapter;
import com.sse.attemper.sys.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class HistoryMonitorService extends BaseServiceAdapter {

    @Autowired
    private JobInstanceMapper mapper;

    public Map<String, Object> list(JobInstListParam param) {
        Map<String, Object> paramMap = injectAdminTenantIdToMap(param);
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        Page<JobInst> list = (Page<JobInst>) mapper.list(paramMap);
        return PageUtil.toResultMap(list);
    }
}
