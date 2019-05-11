package com.github.attemper.sys.service;

import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.sys.resource.ResourceRemoveParam;
import com.github.attemper.common.param.sys.resource.ResourceSaveParam;
import com.github.attemper.common.result.sys.resource.Resource;
import com.github.attemper.config.base.util.BeanUtil;
import com.github.attemper.sys.dao.mapper.ResourceMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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
     * @return
     */
    public List<Resource> getAll() {
        List<Resource> sourceList = mapper.getAll();
	    List<Resource> targetList = new ArrayList<>(sourceList.size());
        Resource rootResource = findRootResource(sourceList);
        targetList.add(rootResource);
        computeTreeList(sourceList, targetList, rootResource);
		return targetList;
    }

    public Resource save(ResourceSaveParam param) {
        Resource resource = toResource(param);
        Date now = new Date();
        resource.setCreateTime(now);
        resource.setUpdateTime(now);
        mapper.save(BeanUtil.bean2Map(resource));
        return resource;
    }

    public Void remove(ResourceRemoveParam param) {
        mapper.delete(BeanUtil.bean2Map(param));
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
            throw new RTException(5470);
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
