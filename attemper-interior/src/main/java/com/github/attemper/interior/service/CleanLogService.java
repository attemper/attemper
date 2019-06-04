package com.github.attemper.interior.service;

import com.github.attemper.config.base.annotation.MultiDataSource;
import com.github.attemper.config.base.util.BeanUtil;
import com.github.attemper.interior.dao.mapper.CleanLogMapper;
import com.github.attemper.interior.param.CleanLogParam;
import com.github.attemper.java.sdk.common.executor.param.execution.TaskParam;
import com.github.attemper.java.sdk.common.executor.param.log.LogParam;
import com.github.attemper.java.sdk.common.result.execution.LogResult;
import com.github.attemper.java.sdk.micro.executor.client.ExecutorMicroClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("cleanLog")
public class CleanLogService {

    @Autowired
    private CleanLogMapper mapper;

    @Autowired
    private ExecutorMicroClient executorMicroClient;

    @MultiDataSource
    public void delete(TaskParam<CleanLogParam> taskCommonParam) {
        CleanLogParam bizParam = taskCommonParam.getBizParam();
        Map<String, Object> cleanLogMap = new HashMap<>();
        cleanLogMap.putAll(BeanUtil.bean2Map(bizParam));
        cleanLogMap.put("deadLineDate",
                new Date(new Date().getTime() - bizParam.getOffsetDays() * 24 * 60 * 60 * 1000));
        mapper.delete(cleanLogMap);
        LogParam logParam = new LogParam();
        logParam.setBaseExecutionParam(taskCommonParam.getMetaParam());
        LogResult logResult = new LogResult().setLogKey("10000").setLogText("删除api log成功");
        logParam.setLogResult(logResult);
        executorMicroClient.appendLog(logParam);
    }
}
