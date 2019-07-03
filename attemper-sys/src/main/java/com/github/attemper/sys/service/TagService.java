package com.github.attemper.sys.service;

import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.sys.tag.*;
import com.github.attemper.common.result.sys.tag.Tag;
import com.github.attemper.common.result.sys.tenant.Tenant;
import com.github.attemper.config.base.util.BeanUtil;
import com.github.attemper.sys.dao.mapper.TagMapper;
import com.github.attemper.sys.util.PageUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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
		Map<String, Object> paramMap = BeanUtil.bean2Map(param);
		PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
		Page<Tag> list = (Page<Tag>) mapper.list(paramMap);
		return PageUtil.toResultMap(list);
	}

	public Tag get(TagGetParam getParam){
		Map<String, Object> paramMap = BeanUtil.bean2Map(getParam);
	    return mapper.get(paramMap);
    }

	public Tag add(TagSaveParam param) {
		Tag user = get(new TagGetParam().setTagName(param.getTagName()));
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
		Tag tag = get(new TagGetParam().setTagName(param.getTagName()));
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
		Map<String, Object> paramMap = BeanUtil.bean2Map(param);
		mapper.delete(paramMap);
		return null;
	}

	public List<Tenant> getTenants(TagGetParam param) {
		Map<String, Object> paramMap = BeanUtil.bean2Map(param);
		return mapper.getTenants(paramMap);
    }

	public Void saveTenants(TagTenantSaveParam param) {
		Map<String, Object> paramMap = BeanUtil.bean2Map(param);
		mapper.deleteTenants(paramMap);
		if (param.getUserNames() != null && !param.getUserNames().isEmpty()) {
			mapper.addTenants(paramMap);
        }
		return null;
    }

	public List<String> getResources(TagGetParam param) {
		Map<String, Object> paramMap = BeanUtil.bean2Map(param);
		return mapper.getResources(paramMap);
	}

	public Void saveResources(TagResourceSaveParam param) {
		Map<String, Object> paramMap = BeanUtil.bean2Map(param);
		mapper.deleteResources(paramMap);
		if (param.getResourceNames() != null && !param.getResourceNames().isEmpty()) {
			mapper.addResources(paramMap);
		}
		return null;
	}

	private Tag toTag(TagSaveParam param) {
		return Tag.builder()
				.tagName(param.getTagName())
				.displayName(param.getDisplayName())
				.remark(param.getRemark())
				.build();
	}
}
