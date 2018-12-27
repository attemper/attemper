package com.thor.core.service;

import com.thor.common.enums.ResultStatus;
import com.thor.common.exception.RTException;
import com.thor.common.param.CommonParam;
import com.thor.core.dao.mapper.ResourceMapper;
import com.thor.core.entity.Resource;
import com.thor.core.param.resource.ResourceRemoveParam;
import com.thor.core.param.resource.ResourceSaveParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author ldang
 */
@Service
public class ResourceService extends BaseServiceAdapter {

	private @Autowired ResourceMapper mapper;

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
        Resource resource = saveParam.toResource();
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
	        throw new RTException(ResultStatus.INTERFACE_RESOURCE_NOT_ONE_ROOT_ERROR); //5470
        }
        return resources.get(0);
    }

}
