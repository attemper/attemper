package com.sse.attemper.sys.dao.mapper;

import com.sse.attemper.common.base.BaseMapper;
import com.sse.attemper.sdk.common.result.sys.resource.Resource;
import com.sse.attemper.sdk.common.result.sys.tag.Tag;
import com.sse.attemper.sdk.common.result.sys.user.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 *
 * @auth ldang
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

	List<User> list(Map<String, Object> paramMap);

	List<User> login(User user);

	User get(Map<String, Object> paramMap);

	void add(User user);

	void update(User user);

    void delete(Map<String, Object> paramMap);

    List<Resource> getResources(Map<String, Object> paramMap);

    List<Tag> getTags(Map<String, Object> paramMap);

    void deleteUserTags(Map<String, Object> paramMap);

	void saveUserTags(Map<String, Object> paramMap);
}
