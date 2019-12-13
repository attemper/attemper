package com.github.attemper.web.service.statistics;

import com.github.attemper.common.param.statistics.CountInstanceParam;
import com.github.attemper.common.result.MapResult;
import com.github.attemper.web.dao.statistics.CountMapper;
import com.github.attemper.sys.service.BaseServiceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CountService extends BaseServiceAdapter {

    @Autowired
    private CountMapper mapper;

    public List<MapResult<String, Integer>> getTenantCount() {
        return mapper.getTenantCount();
    }

    public List<MapResult<String, Integer>> getJobCount() {
        Map<String, Object> paramMap = injectTenantIdExceptAdminToMap(null);
        return mapper.getJobCount(paramMap);
    }

    public List<MapResult<String, Integer>> getInstanceCount(CountInstanceParam param) {
        Map<String, Object> paramMap = injectTenantIdExceptAdminToMap(param);
        return mapper.getInstanceCount(paramMap);
    }

}
