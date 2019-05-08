package com.github.attemper.interior.service;

import com.github.attemper.config.base.annotation.MultiDataSource;
import com.github.attemper.config.base.util.BeanUtil;
import com.github.attemper.interior.dao.mapper.CleanLogMapper;
import com.github.attemper.interior.param.CleanLogParam;
import com.github.attemper.java.sdk.common.executor2biz.param.TaskExecutionParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("cleanLog")
public class CleanLogService {

    @Autowired
    private CleanLogMapper mapper;

    @MultiDataSource
    public void delete(TaskExecutionParam<CleanLogParam> taskCommonParam) {
        CleanLogParam bizParam = taskCommonParam.getBizParam();
        Map<String, Object> cleanLogMap = new HashMap<>();
        cleanLogMap.putAll(BeanUtil.bean2Map(bizParam));
        cleanLogMap.put("deadLineDate",
                new Date(new Date().getTime() - bizParam.getOffsetDays() * 24 * 60 * 60 * 1000));
        mapper.delete(cleanLogMap);
    }
}
