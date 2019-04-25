package com.sse.attemper.core.service.arg;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sse.attemper.common.param.dispatch.arg.ArgGetParam;
import com.sse.attemper.common.param.dispatch.arg.ArgListParam;
import com.sse.attemper.common.param.dispatch.arg.ArgRemoveParam;
import com.sse.attemper.common.param.dispatch.arg.ArgSaveParam;
import com.sse.attemper.common.result.dispatch.arg.Arg;
import com.sse.attemper.core.dao.mapper.arg.ArgMapper;
import com.sse.attemper.sys.service.BaseServiceAdapter;
import com.sse.attemper.sys.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Transactional
@Service
public class ArgService extends BaseServiceAdapter {

    @Autowired
    private ArgMapper mapper;

    public Arg get(ArgGetParam param) {
        Map<String, Object> paramMap = injectAdminTenantIdToMap(param);
        return mapper.get(paramMap);
    }

    public Arg add(ArgSaveParam param) {
        Arg arg = get(ArgGetParam.builder().argName(param.getArgName()).build());
        if (arg != null) {
            throw new DuplicateKeyException(param.getArgName());
        }
        arg = toArg(param);
        mapper.add(arg);
        return arg;
    }

    public Arg update(ArgSaveParam param) {
        Arg oldArg = get(ArgGetParam.builder().argName(param.getArgName()).build());
        if (oldArg == null) {
            return add(param);
        }
        Arg updatedArg = toArg(param);
        mapper.update(updatedArg);
        return updatedArg;
    }

    public Map<String, Object> list(ArgListParam param) {
        Map<String, Object> paramMap = injectAdminTenantIdToMap(param);
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        Page<Arg> list = (Page<Arg>) mapper.list(paramMap);
        return PageUtil.toResultMap(list);
    }

    public Void remove(ArgRemoveParam param) {
        Map<String, Object> paramMap = injectAdminTenantIdToMap(param);
        mapper.delete(paramMap);
        return null;
    }

    private Arg toArg(ArgSaveParam param) {
        return Arg.builder()
                .argName(param.getArgName())
                .argType(param.getArgType())
                .argValue(param.getArgValue())
                .remark(param.getRemark())
                .tenantId(injectAdminTenant().getId())
                .build();
    }
}
