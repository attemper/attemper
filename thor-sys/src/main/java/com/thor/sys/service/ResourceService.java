package com.thor.sys.service;

import com.thor.sdk.common.exception.RTException;
import com.thor.sdk.common.param.CommonParam;
import com.thor.sdk.common.param.resource.ResourceRemoveParam;
import com.thor.sdk.common.param.resource.ResourceSaveParam;
import com.thor.sdk.common.result.resource.Resource;
import com.thor.sys.dao.mapper.ResourceMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author ldang
 */
@Service
@Transactional
public class ResourceService extends BaseServiceAdapter {

    @Autowired
	private ResourceMapper mapper;

    /**
     * 获取资源树
     * @param commonParam
     * @return
     */
	public List<Resource> getAll(CommonParam commonParam){
        Map<String, Object> paramMap = injectTenantIdToMap(commonParam);
		List<Resource> sourceList = mapper.getAll(paramMap);
	    List<Resource> targetList = new ArrayList<>(sourceList.size());
        Resource rootResource = findRootResource(sourceList);
        targetList.add(rootResource);
        computeTreeList(sourceList, targetList, rootResource);
		return targetList;
    }

    /**
     * 保存
     * @param saveParam
     * @return
     */
    public Resource save(ResourceSaveParam saveParam) {
        Resource resource = toResource(saveParam);
        Date now = new Date();
        resource.setCreateTime(now); //mapper中修改是不更新创建时间的
        resource.setUpdateTime(now);
        mapper.save(injectTenantIdToMap(resource));
        return resource;
    }

    /**
     * 删除
     * @param removeParam
     * @return
     */
    public void remove(ResourceRemoveParam removeParam) {
        Map<String, Object> paramMap = injectTenantIdToMap(removeParam);
        mapper.delete(paramMap);
    }

    private void computeTreeList(List<Resource> sourceList, List<Resource> targetList, Resource cellResource) {
	    Iterator<Resource> it = sourceList.iterator();
	    boolean find = false;
	    while(it.hasNext()){
	        Resource current = it.next();
	        if(StringUtils.equals(current.getParentResourceName(), cellResource.getResourceName())){
                targetList.add(current);
                find = true;
                computeTreeList(sourceList, targetList, current);
            }
        }
        if(!find){
	        return;
        }
    }

    private Resource findRootResource(List<Resource> sourceList) {
	    List<Resource> resources =
                sourceList.stream().filter(resource -> resource.getParentResourceName() == null).collect(Collectors.toList());
	    if(resources.size() != 1){
            throw new RTException(5470); //5470
        }
        return resources.get(0);
    }

    private Resource toResource(ResourceSaveParam saveParam) {
        return Resource.builder()
                .resourceName(saveParam.getResourceName())
                .parentResourceName(saveParam.getParentResourceName())
                .displayName(saveParam.getDisplayName())
                .resourceType(saveParam.getResourceType())
                .uri(saveParam.getUri())
                .icon(saveParam.getIcon())
                .position(saveParam.getPosition())
                .extAttr(saveParam.getExtAttr())
                .build();
    }
}
