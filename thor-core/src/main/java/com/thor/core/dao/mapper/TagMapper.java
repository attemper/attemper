package com.thor.core.dao.mapper;

import com.thor.common.base.BaseMapper;
import com.thor.core.entity.Resource;
import com.thor.core.entity.Tag;
import com.thor.core.entity.User;
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

    List<User> getUsers(Map<String,Object> paramMap);

    void deleteTagUser(Map<String,Object> paramMap);

	void saveTagUser(Map<String,Object> paramMap);

    List<Resource> getResources(Map<String,Object> paramMap);

	void deleteTagResource(Map<String,Object> paramMap);

	void saveTagResource(Map<String,Object> paramMap);
}
