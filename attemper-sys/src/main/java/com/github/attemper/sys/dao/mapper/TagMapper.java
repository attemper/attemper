package com.github.attemper.sys.dao.mapper;

import com.github.attemper.common.base.BaseMapper;
import com.github.attemper.common.result.sys.resource.Resource;
import com.github.attemper.common.result.sys.tag.Tag;
import com.github.attemper.common.result.sys.user.User;
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
