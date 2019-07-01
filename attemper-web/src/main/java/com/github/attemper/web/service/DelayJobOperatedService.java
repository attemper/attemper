package com.github.attemper.web.service;

import com.github.attemper.common.param.dispatch.delay.DelayJobListParam;
import com.github.attemper.java.sdk.common.web.param.delay.DelayJobExtSaveParam;
import com.github.attemper.java.sdk.common.web.param.delay.DelayJobIdsParam;
import com.github.attemper.java.sdk.common.web.result.delay.DelayJobResult;
import com.github.attemper.sys.service.BaseServiceAdapter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class DelayJobOperatedService extends BaseServiceAdapter {

    public Map<String, Object> list(DelayJobListParam param) {
        return null;
    }

    public Void remove(DelayJobIdsParam param) {
        return null;
    }

    public DelayJobResult add(DelayJobExtSaveParam param) {
        return null;
    }
}
