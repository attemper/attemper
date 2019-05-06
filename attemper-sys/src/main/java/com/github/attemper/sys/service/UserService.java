package com.github.attemper.sys.service;

import com.github.attemper.sys.dao.mapper.UserMapper;
import com.github.attemper.sys.util.PageUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.sys.user.*;
import com.github.attemper.common.result.sys.resource.Resource;
import com.github.attemper.common.result.sys.tag.Tag;
import com.github.attemper.common.result.sys.tenant.Tenant;
import com.github.attemper.common.result.sys.user.User;
import com.github.attemper.common.result.sys.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ldang
 */
@Service
@Transactional
public class UserService extends BaseServiceAdapter {

	@Autowired
	private UserMapper mapper;

	@Autowired
	private TenantService tenantService;

	public UserInfo getUserInfo() {
		User user = injectUser();
		UserGetParam getParam = new UserGetParam(user.getUserName());
		List<Tag> tags = getTags(getParam);
		List<Resource> resources = getResources(getParam);
		return UserInfo.builder()
				.user(user)
				.resources(resources)
				.tags(tags)
				.build();
	}

	public Tenant getAdminTenant() {
		User user = injectUser();
		String userName = user.getUserName();
		return tenantService.getByAdmin(userName);
	}

	public Map<String, Object> list(UserListParam param) {
		Map<String, Object> paramMap = injectTenantIdToMap(param);
		PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
		Page<User> list = (Page<User>) mapper.list(paramMap);
		return PageUtil.toResultMap(list);
	}

	public List<User> login(User user) {
		return mapper.login(user);
	}

	public User get(UserGetParam param) {
		Map<String, Object> paramMap = injectTenantIdToMap(param);
		return mapper.get(paramMap);
	}

	public User add(UserSaveParam param) {
		User user = get(new UserGetParam(param.getUserName()));
		if (user != null) {
			throw new DuplicateKeyException(user.getUserName());
		}
		user = toUser(param);
		Date now = new Date();
		user.setCreateTime(now);
		user.setUpdateTime(now);
		mapper.add(user);
		return user;
	}

	private User toUser(UserSaveParam param) {
		return User.builder()
				.userName(param.getUserName())
				.displayName(param.getDisplayName())
				.password(param.getPassword())
				.email(param.getEmail())
				.mobile(param.getMobile())
				.status(param.getStatus())
				.tenantId(injectTenantId())
				.build();
	}

	public User update(UserSaveParam param) {
		User user = get(new UserGetParam(param.getUserName()));
		if (user == null) {
			throw new RTException(5250);
		}
		User updatedUser = toUser(param);
		updatedUser.setCreateTime(user.getCreateTime());
		updatedUser.setUpdateTime(new Date());
		mapper.update(updatedUser);
		return updatedUser;
	}

	public Void remove(UserRemoveParam param) {
		Map<String, Object> paramMap = injectTenantIdToMap(param);
		mapper.delete(paramMap);
		return null;
	}

	public List<Resource> getResources(UserGetParam param) {
		return mapper.getResources(injectTenantIdToMap(param));
	}

	public List<Tag> getTags(UserGetParam param) {
		return mapper.getTags(injectTenantIdToMap(param));
	}

	public void updateUserTags(UserTagUpdateParam param) {
		Map<String, Object> paramMap = injectTenantIdToMap(param);
		mapper.deleteUserTags(paramMap);
		if (param.getTagNames() == null || param.getTagNames().isEmpty()) {
			return;
		}
		mapper.saveUserTags(paramMap);
	}

}
