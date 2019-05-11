package com.github.attemper.sys.dao.mapper;

import com.github.attemper.common.base.BaseMapper;
import com.github.attemper.common.result.sys.resource.Resource;
import com.github.attemper.common.result.sys.tag.Tag;
import com.github.attemper.common.result.sys.tenant.Tenant;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author ldang
 */
@Mapper
@Repository
public interface TenantMapper extends BaseMapper<Tenant> {

    Tenant get(String userName);

    void delete(List<String> userNames);

    Tenant getAdmin();

    List<Resource> getResources(Map<String, Object> paramMap);

    List<Tag> getTags(Map<String, Object> paramMap);

    void deleteTags(Map<String, Object> paramMap);

    void saveTags(Map<String, Object> paramMap);
}
