package com.github.attemper.core.service.statistics;

import com.github.attemper.core.dao.statistics.CountMapper;
import com.github.attemper.sys.service.BaseServiceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CountService extends BaseServiceAdapter {

    @Autowired
    private CountMapper mapper;

    public Integer getTenantCount() {
        return mapper.getTenantCount();
    }

    public List<Map<String, Object>> getJobCount() {
        Map<String, Object> paramMap = injectTenantIdExceptAdminToMap(null);
        return mapper.getJobCount(paramMap);
    }

    public List<Map<String, Object>> getJobInstanceCount() {
        Map<String, Object> paramMap = injectTenantIdExceptAdminToMap(null);
        return mapper.getJobInstanceCount(paramMap);
    }

}
