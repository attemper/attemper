package com.thor.sys.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.thor.sdk.common.exception.RTException;
import com.thor.sdk.common.param.sys.tag.*;
import com.thor.sdk.common.result.sys.resource.Resource;
import com.thor.sdk.common.result.sys.tag.Tag;
import com.thor.sdk.common.result.sys.user.User;
import com.thor.sys.dao.mapper.TagMapper;
import com.thor.sys.util.PageUtil;
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

	private @Autowired
	TagMapper mapper;

	public Map<String, Object> list(TagListParam listParam) {
        Map<String, Object> paramMap = injectTenantIdToMap(listParam);
		PageHelper.startPage(listParam.getCurrentPage(), listParam.getPageSize());
		Page<Tag> list = (Page<Tag>) mapper.list(paramMap);
		return PageUtil.toResultMap(list);
	}

	public Tag get(TagGetParam getParam){
        Map<String, Object> paramMap = injectTenantIdToMap(getParam);
	    return mapper.get(paramMap);
    }

	/**
	 * 新增
	 * @param saveParam
	 * @return
	 */
	public Tag add(TagSaveParam saveParam) {
		//主键应不在数据库中
		Tag user = get(new TagGetParam(saveParam.getTagName(), saveParam.getTagType()));
		if(user != null){
			throw new DuplicateKeyException(user.getTagName());
		}
		user = toTag(saveParam);
		Date now = new Date();
		user.setCreateTime(now);
		user.setUpdateTime(now);
		user.setTenantId(injectTenantId());
		mapper.add(user);
		return user;
	}

	/**
	 * 更新
	 * @param saveParam
	 * @return
	 */
	public Tag update(TagSaveParam saveParam) {
		//主键应在数据库中
		Tag tag = get(new TagGetParam(saveParam.getTagName(), saveParam.getTagType()));
		if(tag == null){
			throw new RTException(5350);  //5350
		}
		Tag updatedTag = toTag(saveParam);
        updatedTag.setCreateTime(tag.getCreateTime());
        updatedTag.setUpdateTime(new Date());
        updatedTag.setTenantId(injectTenantId());
        mapper.update(updatedTag);
        return updatedTag;
	}

	/**
	 * 删除
	 * @param removeParam
	 * @return
	 */
	public void remove(TagRemoveParam removeParam) {
        Map<String, Object> paramMap = injectTenantIdToMap(removeParam);
		mapper.delete(paramMap);
	}

    /**
     * 获取某标签下的用户
     * @param getParam
     * @return
     */
    public List<User> getUsers(TagGetParam getParam) {
        Map<String, Object> paramMap = injectTenantIdToMap(getParam);
        return mapper.getUsers(paramMap);
    }

    /**
     * 为标签分配用户
     * @param tagUserUpdateParam
     */
    public void updateTagUsers(TagUserUpdateParam tagUserUpdateParam) {
        Map<String, Object> paramMap = injectTenantIdToMap(tagUserUpdateParam);
        mapper.deleteTagUsers(paramMap);
        if(tagUserUpdateParam.getUserNames() == null || tagUserUpdateParam.getUserNames().length == 0){
            return;
        }
        mapper.saveTagUsers(paramMap);
    }

	/**
	 * 获取某标签下的资源
	 * @param getParam
	 * @return
	 */
	public List<Resource> getResources(TagGetParam getParam) {
		Map<String, Object> paramMap = injectTenantIdToMap(getParam);
		return mapper.getResources(paramMap);
	}

	/**
	 * 为标签分配资源
	 * @param updateParam
	 */
	public void updateTagResources(TagResourceUpdateParam updateParam) {
		Map<String, Object> paramMap = injectTenantIdToMap(updateParam);
		mapper.deleteTagResources(paramMap);
		if(updateParam.getResourceNames() == null || updateParam.getResourceNames().length == 0){
			return;
		}
		mapper.saveTagResources(paramMap);
	}

	private Tag toTag(TagSaveParam saveParam) {
		return Tag.builder()
				.tagName(saveParam.getTagName())
				.displayName(saveParam.getDisplayName())
				.tagType(saveParam.getTagType())
				.remark(saveParam.getRemark())
				.build();
	}
}
