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

import java.util.Date;
import java.util.Map;

@Transactional
@Service
public class ArgService extends BaseServiceAdapter {

    @Autowired
    private ArgMapper mapper;

    public Arg add(ArgSaveParam param) {
        Arg arg = get(ArgGetParam.builder().argName(param.getArgName()).build());
        if (arg != null) {
            throw new DuplicateKeyException(param.getArgName());
        }
        arg = toArgument(param);
        arg.setTenantId(injectAdminTenant().getId());
        arg.setCreateTime(new Date());
        arg.setUpdateTime(new Date());
        mapper.add(arg);
        return arg;
    }

    public Arg get(ArgGetParam param) {
        Map<String, Object> paramMap = injectAdminTenantIdToMap(param);
        return mapper.get(paramMap);
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

    public Arg update(ArgSaveParam updateParam) {
        Arg argOld = get(ArgGetParam.builder().argName(updateParam.getArgName()).build());
        if (get(ArgGetParam.builder().argName(updateParam.getArgName()).build()) == null) {
            return add(updateParam);
        }
        Arg argNew = toArgument(updateParam);
        argNew.setTenantId(injectAdminTenant().getId());
        argNew.setUpdateTime(new Date());
        argNew.setCreateTime(argOld.getCreateTime());
        mapper.update(argNew);
        return argNew;
    }

    private Arg toArgument(ArgSaveParam param) {
        Arg arg = new Arg();
        arg.setArgName(param.getArgName());
        arg.setArgType(param.getArgType());
        arg.setDefVal(param.getDefVal());
        arg.setRemark(param.getRemark());
        return arg;
    }
}
