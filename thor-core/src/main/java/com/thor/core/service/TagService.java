package com.thor.core.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.thor.common.enums.ResultStatus;
import com.thor.common.exception.RTException;
import com.thor.core.dao.mapper.TagMapper;
import com.thor.core.entity.Resource;
import com.thor.core.entity.Tag;
import com.thor.core.entity.User;
import com.thor.core.param.tag.*;
import com.thor.core.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ldang
 */
@Service
public class TagService extends BaseServiceAdapter {

	private @Autowired TagMapper mapper;

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
		user = saveParam.toTag();
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
			throw new RTException(ResultStatus.INTERFACE_TAG_UPDATE_NOTEXIST_ERROR);  //5350
		}
        Tag updatedTag = saveParam.toTag();
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
    public List<User> getTagUsers(TagGetParam getParam) {
        Map<String, Object> paramMap = injectTenantIdToMap(getParam);
        return mapper.getUsers(paramMap);
    }

    /**
     * 为标签分配用户
     * @param tagUserUpdateParam
     */
    public void updateTagUsers(TagUserUpdateParam tagUserUpdateParam) {
        Map<String, Object> paramMap = injectTenantIdToMap(tagUserUpdateParam);
        mapper.deleteTagUser(paramMap);
        if(tagUserUpdateParam.getUserNames() == null || tagUserUpdateParam.getUserNames().length == 0){
            return;
        }
        mapper.saveTagUser(paramMap);
    }

	/**
	 * 获取某标签下的资源
	 * @param getParam
	 * @return
	 */
	public List<Resource> getTagResources(TagGetParam getParam) {
		Map<String, Object> paramMap = injectTenantIdToMap(getParam);
		return mapper.getResources(paramMap);
	}

	/**
	 * 为标签分配资源
	 * @param updateParam
	 */
	public void updateTagResources(TagResourceUpdateParam updateParam) {
		Map<String, Object> paramMap = injectTenantIdToMap(updateParam);
		mapper.deleteTagResource(paramMap);
		if(updateParam.getResourceNames() == null || updateParam.getResourceNames().length == 0){
			return;
		}
		mapper.saveTagResource(paramMap);
	}
}
