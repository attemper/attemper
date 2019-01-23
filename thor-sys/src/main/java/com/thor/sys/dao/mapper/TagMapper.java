package com.thor.sys.dao.mapper;

import com.thor.common.base.BaseMapper;
import com.thor.sdk.common.result.resource.Resource;
import com.thor.sdk.common.result.tag.Tag;
import com.thor.sdk.common.result.user.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 *
 * @auth ldang
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

	List<Tag> list(Map<String, Object> paramMap);

	Tag get(Map<String, Object> paramMap);

	void add(Tag user);

	void update(Tag user);

    void delete(Map<String, Object> paramMap);

    List<User> getUsers(Map<String, Object> paramMap);

    void deleteTagUsers(Map<String, Object> paramMap);

	void saveTagUsers(Map<String, Object> paramMap);

    List<Resource> getResources(Map<String, Object> paramMap);

	void deleteTagResources(Map<String, Object> paramMap);

	void saveTagResources(Map<String, Object> paramMap);
}
