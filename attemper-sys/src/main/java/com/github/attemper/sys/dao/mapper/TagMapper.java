package com.github.attemper.sys.dao.mapper;

import com.github.attemper.common.base.BaseMapper;
import com.github.attemper.common.result.sys.tag.Tag;
import com.github.attemper.common.result.sys.tenant.Tenant;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 *
 * @author ldang
 */
@Mapper
@Repository
public interface TagMapper extends BaseMapper<Tag> {

    List<Tenant> getTenants(Map<String, Object> paramMap);

    void deleteTagTenants(Map<String, Object> paramMap);

    void saveTagTenants(Map<String, Object> paramMap);

    List<String> getResources(Map<String, Object> paramMap);

	void deleteTagResources(Map<String, Object> paramMap);

	void saveTagResources(Map<String, Object> paramMap);
}
