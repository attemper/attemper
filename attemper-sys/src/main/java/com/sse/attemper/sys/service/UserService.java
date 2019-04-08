package com.sse.attemper.sys.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sse.attemper.common.exception.RTException;
import com.sse.attemper.common.param.sys.user.*;
import com.sse.attemper.common.result.sys.resource.Resource;
import com.sse.attemper.common.result.sys.tag.Tag;
import com.sse.attemper.common.result.sys.tenant.Tenant;
import com.sse.attemper.common.result.sys.user.User;
import com.sse.attemper.common.result.sys.user.UserInfo;
import com.sse.attemper.sys.dao.mapper.UserMapper;
import com.sse.attemper.sys.util.PageUtil;
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

	/**
	 * 根据token获取用户信息
	 *
	 * @return
	 */
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

	/**
	 * 根据token获取用户和其管理的租户信息
	 *
	 * @return
	 */
	public Tenant getAdminedTenant() {
		User user = injectUser();
		String userName = user.getUserName();
		return tenantService.getByAdmin(userName);
	}

	public Map<String, Object> list(UserListParam listParam) {
		Map<String, Object> paramMap = injectTenantIdToMap(listParam);
		PageHelper.startPage(listParam.getCurrentPage(), listParam.getPageSize());
		Page<User> list = (Page<User>) mapper.list(paramMap);
		return PageUtil.toResultMap(list);
	}

	/**
	 * 供登录接口使用
	 * 支持用户名/手机号/邮箱号来登录
	 *
	 * @param user
	 * @return
	 */
	public List<User> login(User user) {
		return mapper.login(user);
	}

	public User get(UserGetParam getParam) {
		Map<String, Object> paramMap = injectTenantIdToMap(getParam);
		return mapper.get(paramMap);
	}

	/**
	 * 新增
	 *
	 * @param saveParam
	 * @return
	 */
	public User add(UserSaveParam saveParam) {
		//主键应在数据库中   不存在
		User user = get(new UserGetParam(saveParam.getUserName()));
		if (user != null) {
			throw new DuplicateKeyException(user.getUserName());
		}
		user = toUser(saveParam);
		Date now = new Date();
		user.setCreateTime(now);
		user.setUpdateTime(now);
		user.setTenantId(injectTenantId());
		mapper.add(user);
		return user;
	}

	private User toUser(UserSaveParam saveParam) {
		return User.builder()
				.userName(saveParam.getUserName())
				.displayName(saveParam.getDisplayName())
				.password(saveParam.getPassword())
				.email(saveParam.getEmail())
				.mobile(saveParam.getMobile())
				.status(saveParam.getStatus())
				.build();
	}

	/**
	 * 更新
	 *
	 * @param saveParam
	 * @return
	 */
	public User update(UserSaveParam saveParam) {
		//主键应在数据库中  存在
		User user = get(new UserGetParam(saveParam.getUserName()));
		if (user == null) {
			throw new RTException(5250);  //5250
		}
		User updatedUser = toUser(saveParam);
		updatedUser.setCreateTime(user.getCreateTime());
		updatedUser.setUpdateTime(new Date());
		updatedUser.setTenantId(injectTenantId());
		mapper.update(updatedUser);
		return updatedUser;
	}

	/**
	 * 删除
	 *
	 * @param removeParam
	 * @return
	 */
	public void remove(UserRemoveParam removeParam) {
		Map<String, Object> paramMap = injectTenantIdToMap(removeParam);
		mapper.delete(paramMap);
	}

	/**
	 * 获取用户拥有的资源集合
	 *
	 * @param getParam
	 * @return
	 */
	public List<Resource> getResources(UserGetParam getParam) {
		return mapper.getResources(injectTenantIdToMap(getParam));
	}

	/**
	 * 获取用户隶属的标签集合
	 *
	 * @param getParam
	 * @return
	 */
	public List<Tag> getTags(UserGetParam getParam) {
		return mapper.getTags(injectTenantIdToMap(getParam));
	}

	/**
	 * 更新用户关联的标签数据
	 *
	 * @param updateParam
	 */
	public void updateUserTags(UserTagUpdateParam updateParam) {
		Map<String, Object> paramMap = injectTenantIdToMap(updateParam);
		mapper.deleteUserTags(paramMap);
		if (updateParam.getTagNames() == null || updateParam.getTagNames().length == 0) {
			return;
		}
		mapper.saveUserTags(paramMap);
	}

}
