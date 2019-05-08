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
public interface UserMapper extends BaseMapper<User> {

	List<User> login(User user);

    List<Resource> getResources(Map<String, Object> paramMap);

    List<Tag> getTags(Map<String, Object> paramMap);

    void deleteUserTags(Map<String, Object> paramMap);

	void saveUserTags(Map<String, Object> paramMap);
}
