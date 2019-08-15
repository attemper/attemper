package com.github.attemper.core.service.application;

import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.IdParam;
import com.github.attemper.common.param.app.gist.*;
import com.github.attemper.common.result.app.gist.Gist;
import com.github.attemper.common.result.app.gist.GistInfo;
import com.github.attemper.config.base.util.BeanUtil;
import com.github.attemper.core.dao.application.GistMapper;
import com.github.attemper.sys.service.BaseServiceAdapter;
import com.github.attemper.sys.util.PageUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.impl.cfg.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class GistService extends BaseServiceAdapter {

    @Autowired
    private GistMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    public Gist get(GistNameParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        return mapper.get(paramMap);
    }

    public Map<String, Object> list(GistListParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        Page<Gist> list = (Page<Gist>) mapper.list(paramMap);
        return PageUtil.toResultMap(list);
    }

    public Gist add(GistSaveParam param) {
        Gist entity = get(new GistNameParam().setGistName(param.getGistName()));
        if (entity != null) {
            throw new DuplicateKeyException(param.getGistName());
        }
        entity = paramToResult(param);
        entity.setCreateTime(new Date());
        mapper.add(entity);
        return entity;
    }

    public Void remove(GistNamesParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        mapper.delete(paramMap);
        return null;
    }

    private Gist paramToResult(GistSaveParam param) {
        return new Gist()
                .setGistName(param.getGistName())
                .setTenantId(injectTenantId());
    }

    public List<GistInfo> listInfo(GistNameParam param) {
        return mapper.listInfo(injectTenantIdToMap(param));
    }

    public GistInfo addInfo(GistInfoSaveParam param) {
        List<GistInfo> gistInfos = listInfo(new GistNameParam().setGistName(param.getGistName()));
        String lastVersionContent = null;
        if (gistInfos.size() > 0) {
            boolean sameVersion = gistInfos.parallelStream().anyMatch(item -> item.getVersion().equals(param.getVersion()));
            if (sameVersion) {
                throw new RTException(6801, param.getVersion());
            }
            lastVersionContent = getContent(new IdParam().setId(gistInfos.get(0).getId()));
        }
        GistInfo gistInfo = new GistInfo()
                .setId(idGenerator.getNextId())
                .setGistName(param.getGistName())
                .setVersion(param.getVersion())
                .setUpdateTime(new Date())
                .setContent(lastVersionContent)
                .setTenantId(injectTenantId());
        mapper.addInfo(gistInfo);
        return gistInfo;
    }

    public Void removeInfo(IdParam param) {
        mapper.removeInfo(param.getId());
        return null;
    }

    public String getContent(IdParam param) {
        return mapper.getContent(param.getId());
    }

    public Void updateContent(GistInfoContentParam param) {
        mapper.updateContent(BeanUtil.bean2Map(param));
        return null;
    }
}
