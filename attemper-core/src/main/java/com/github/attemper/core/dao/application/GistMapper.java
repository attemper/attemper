package com.github.attemper.core.dao.application;

import com.github.attemper.common.base.BaseMapper;
import com.github.attemper.common.result.app.gist.Gist;
import com.github.attemper.common.result.app.gist.GistInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface GistMapper extends BaseMapper<Gist> {

    List<GistInfo> listInfo(Map<String, Object> paramMap);

    void addInfo(GistInfo gistInfo);

    void removeInfo(String id);

    String getContent(String id);

    String getLatestContent(Map<String, Object> paramMap);

    void updateContent(Map<String, Object> paramMap);
}