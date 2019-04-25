package com.sse.attemper.sys.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sse.attemper.common.exception.RTException;
import com.sse.attemper.common.param.sys.tag.*;
import com.sse.attemper.common.result.sys.resource.Resource;
import com.sse.attemper.common.result.sys.tag.Tag;
import com.sse.attemper.common.result.sys.user.User;
import com.sse.attemper.sys.dao.mapper.TagMapper;
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
public class TagService extends BaseServiceAdapter {

	@Autowired
	private TagMapper mapper;

	public Map<String, Object> list(TagListParam param) {
		Map<String, Object> paramMap = injectTenantIdToMap(param);
		PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
		Page<Tag> list = (Page<Tag>) mapper.list(paramMap);
		return PageUtil.toResultMap(list);
	}

	public Tag get(TagGetParam getParam){
        Map<String, Object> paramMap = injectTenantIdToMap(getParam);
	    return mapper.get(paramMap);
    }

	public Tag add(TagSaveParam param) {
		Tag user = get(new TagGetParam(param.getTagName(), param.getTagType()));
		if(user != null){
			throw new DuplicateKeyException(user.getTagName());
		}
		user = toTag(param);
		Date now = new Date();
		user.setCreateTime(now);
		user.setUpdateTime(now);
		mapper.add(user);
		return user;
	}

	public Tag update(TagSaveParam param) {
		Tag tag = get(new TagGetParam(param.getTagName(), param.getTagType()));
		if(tag == null){
			throw new RTException(5350);
		}
		Tag updatedTag = toTag(param);
        updatedTag.setCreateTime(tag.getCreateTime());
        updatedTag.setUpdateTime(new Date());
        mapper.update(updatedTag);
        return updatedTag;
	}

	public Void remove(TagRemoveParam param) {
		Map<String, Object> paramMap = injectTenantIdToMap(param);
		mapper.delete(paramMap);
		return null;
	}

	public List<User> getUsers(TagGetParam param) {
		Map<String, Object> paramMap = injectTenantIdToMap(param);
        return mapper.getUsers(paramMap);
    }

	public Void updateTagUsers(TagUserUpdateParam param) {
		Map<String, Object> paramMap = injectTenantIdToMap(param);
        mapper.deleteTagUsers(paramMap);
		if (param.getUserNames() != null && !param.getUserNames().isEmpty()) {
			mapper.saveTagUsers(paramMap);
        }
		return null;
    }

	public List<Resource> getResources(TagGetParam param) {
		Map<String, Object> paramMap = injectTenantIdToMap(param);
		return mapper.getResources(paramMap);
	}

	public Void updateTagResources(TagResourceUpdateParam param) {
		Map<String, Object> paramMap = injectTenantIdToMap(param);
		mapper.deleteTagResources(paramMap);
		if (param.getResourceNames() != null && !param.getResourceNames().isEmpty()) {
			mapper.saveTagResources(paramMap);
		}
		return null;
	}

	private Tag toTag(TagSaveParam param) {
		return Tag.builder()
				.tagName(param.getTagName())
				.displayName(param.getDisplayName())
				.tagType(param.getTagType())
				.remark(param.getRemark())
				.tenantId(injectTenantId())
				.build();
	}
}
