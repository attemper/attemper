package com.thor.core.service.job;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.stark.sdk.common.constant.StarkSdkCommonConstants;
import com.stark.sdk.common.exception.RTException;
import com.stark.sdk.common.result.PageResult;
import com.thor.core.constant.CoreConstants;
import com.thor.core.dao.mapper.job.BaseJobMapper;
import com.thor.sdk.common.param.job.group.GroupSubJobRemoveParam;
import com.thor.sdk.common.param.job.group.GroupSubJobUpdateParam;
import com.thor.sdk.common.param.job.group.GroupWithAtomAndSubListParam;
import com.thor.sdk.common.result.job.BaseJob;
import com.thor.sdk.common.result.job.config.support.PriorityJobEntity;
import com.thor.sys.service.BaseServiceAdapter;
import com.xiaoleilu.hutool.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

/**
 * @author ldang
 */
@Service
@Transactional
@Slf4j
public class GroupJobService extends BaseServiceAdapter {

    @Autowired
    private BaseJobMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;


    /**
     * 更新
     * @param saveParam
     * @return
     */
    public BaseJob updateSubJob(GroupSubJobUpdateParam saveParam) {
        Map<String, Object> groupParamMap = toGroupGetParam(saveParam.getGroupName());
        BaseJob groupJob = mapper.get(groupParamMap);
        List<PriorityJobEntity> subJobList = getSubJobList(groupJob);
        Iterator<String> jobNameIt = saveParam.getJobNames().iterator();
        while (jobNameIt.hasNext()) {
            String subJobName = jobNameIt.next();
            subJobList.forEach(entity -> {
                if(StringUtils.equals(subJobName, entity.getJobName())) {
                    entity.setPriority(saveParam.getPriority());
                    jobNameIt.remove();
                }
            });
        }
        saveParam.getJobNames().forEach(newSubJobName -> {
            subJobList.add(PriorityJobEntity.builder().jobName(newSubJobName).priority(saveParam.getPriority()).build());
        });
        String jobContentJsonStr = groupJob.getJobContent();
        replaceJobContent(groupJob, subJobList, jobContentJsonStr);
        return groupJob;
    }

    private void replaceJobContent(BaseJob groupJob, List<PriorityJobEntity> subJobList, String jobContentJsonStr) {
        if (StringUtils.isNotBlank(jobContentJsonStr)) {
            try {
                ObjectNode jobContentNode = (ObjectNode) objectMapper.readTree(jobContentJsonStr);
                ArrayNode subJobsNode = objectMapper.createArrayNode();
                subJobList.forEach(subJob -> {
                    ObjectNode objectNode = objectMapper.createObjectNode();
                    objectNode.put(CoreConstants.jobName, subJob.getJobName());
                    objectNode.put(CoreConstants.priority, subJob.getPriority());
                    subJobsNode.add(objectNode);
                });
                jobContentNode.replace(CoreConstants.subJobs, subJobsNode);
                groupJob.setJobContent(objectMapper.writeValueAsString(jobContentNode));
                mapper.update(groupJob);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new RTException(6140);
            }
        }
    }

    /**
     * 删除
     * @param removeParam
     * @return
     */
    public void removeSubJob(GroupSubJobRemoveParam removeParam) {
        Map<String, Object> groupParamMap = toGroupGetParam(removeParam.getGroupName());
        BaseJob groupJob = mapper.get(groupParamMap);
        List<PriorityJobEntity> subJobList = getSubJobList(groupJob);
        Iterator<PriorityJobEntity> subJobIt = subJobList.iterator();
        while (subJobIt.hasNext()) {
            PriorityJobEntity entity = subJobIt.next();
            removeParam.getJobNames().forEach(subJobName -> {
                if(StringUtils.equals(subJobName, entity.getJobName())) {
                    subJobIt.remove();
                }
            });
        }
        String jobContentJsonStr = groupJob.getJobContent();
        replaceJobContent(groupJob, subJobList, jobContentJsonStr);
    }

    /**
     * 查询列表
     * @param listParam
     * @return
     */
    public Map<String, Object> listAtomAndSubJob(GroupWithAtomAndSubListParam listParam) {
        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, Object>> allJobMapList = new ArrayList<>();
        Map<String, Object> paramMap = injectAdminedTenantIdToMap(listParam);
        List<BaseJob> baseJobs = mapper.list(paramMap); // 因为需要已分配的任务在前,所以逻辑分页
        if(StringUtils.isNotBlank(listParam.getGroupName())) {
            Map<String, Object> groupParamMap = toGroupGetParam(listParam.getGroupName());
            BaseJob groupJob = mapper.get(groupParamMap);
            List<PriorityJobEntity> subJobList = getSubJobList(groupJob);
            if(subJobList.size() > 0) {
                subJobList.sort((o1, o2) -> o1 != null ? o1.getPriority().compareTo(o2.getPriority()) : -1);  //按优先级排序
                subJobList.forEach(priorityJob -> {
                    Iterator<BaseJob> it = baseJobs.iterator();
                    while(it.hasNext()) {
                        BaseJob baseJob = it.next();
                        if(StringUtils.equals(priorityJob.getJobName(), baseJob.getJobName())) {
                            Map<String, Object> map = BeanUtil.beanToMap(baseJob);
                            map.put(CoreConstants.priority, priorityJob.getPriority());
                            allJobMapList.add(map);
                            it.remove();
                            break;
                        }
                    }
                });
            }
        }
        baseJobs.forEach(baseJob -> allJobMapList.add(BeanUtil.beanToMap(baseJob)));
        int currentPage = listParam.getCurrentPage();
        int pageSize = listParam.getPageSize();
        PageResult pageResult = new PageResult();
        List<Map<String, Object>> pagedMapList;
        if(allJobMapList.size() >= currentPage * pageSize) {
            pagedMapList = allJobMapList.subList((currentPage-1) * pageSize, currentPage * pageSize);
        } else {
            pagedMapList = allJobMapList.subList((currentPage-1) * pageSize, allJobMapList.size());
        }
        pageResult.setCurrentPage(listParam.getCurrentPage());
        pageResult.setPageSize(listParam.getPageSize());
        pageResult.setTotal(allJobMapList.size());
        resultMap.put(StarkSdkCommonConstants.list, pagedMapList);
        resultMap.put(StarkSdkCommonConstants.page, pageResult);
        return resultMap;
    }

    private List<PriorityJobEntity> getSubJobList(BaseJob groupJob) {
        String jobContentJsonStr = groupJob.getJobContent();
        if (StringUtils.isNotBlank(jobContentJsonStr)){
            try{
                JsonNode jobContentNode = objectMapper.readTree(jobContentJsonStr);
                JsonNode subJobsNode = jobContentNode.get(CoreConstants.subJobs);
                List<Map<String, Object>> mapList= objectMapper.readValue(objectMapper.writeValueAsString(subJobsNode), List.class);
                List<PriorityJobEntity> priorityJobEntities = new ArrayList<>(mapList.size());
                for (Map<String, Object> map : mapList) {
                    priorityJobEntities.add(BeanUtil.mapToBean(map, PriorityJobEntity.class, false));
                }
                return priorityJobEntities;
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new RTException(6140);
            }
        }
        return new ArrayList<>(0);
    }

    private Map<String, Object> toGroupGetParam(String groupName) {
        Map<String, Object> groupParamMap = new HashMap<>(2);
        groupParamMap.put(CoreConstants.jobName, groupName);
        groupParamMap.put(StarkSdkCommonConstants.tenantId, injectAdminedTenant().getId());
        return groupParamMap;
    }
}
