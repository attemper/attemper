package com.sse.attemper.sys.service;

import com.sse.attemper.common.exception.RTException;
import com.sse.attemper.common.param.EmptyParam;
import com.sse.attemper.common.param.sys.resource.ResourceRemoveParam;
import com.sse.attemper.common.param.sys.resource.ResourceSaveParam;
import com.sse.attemper.common.result.sys.resource.Resource;
import com.sse.attemper.sys.dao.mapper.ResourceMapper;
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
     * get tree of resources
     * @param param
     * @return
     */
    public List<Resource> getAll(EmptyParam param) {
        Map<String, Object> paramMap = injectAdminTenantIdToMap(param);
		List<Resource> sourceList = mapper.getAll(paramMap);
	    List<Resource> targetList = new ArrayList<>(sourceList.size());
        Resource rootResource = findRootResource(sourceList);
        targetList.add(rootResource);
        computeTreeList(sourceList, targetList, rootResource);
		return targetList;
    }

    public Resource save(ResourceSaveParam param) {
        Resource resource = toResource(param);
        Date now = new Date();
        resource.setCreateTime(now); //mapper中修改是不更新创建时间的
        resource.setUpdateTime(now);
        mapper.save(injectAdminTenantIdToMap(resource));
        return resource;
    }

    public Void remove(ResourceRemoveParam param) {
        Map<String, Object> paramMap = injectAdminTenantIdToMap(param);
        mapper.delete(paramMap);
        return null;
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

    private Resource toResource(ResourceSaveParam param) {
        return Resource.builder()
                .resourceName(param.getResourceName())
                .parentResourceName(param.getParentResourceName())
                .displayName(param.getDisplayName())
                .resourceType(param.getResourceType())
                .uri(param.getUri())
                .icon(param.getIcon())
                .position(param.getPosition())
                .extAttr(param.getExtAttr())
                .build();
    }
}
