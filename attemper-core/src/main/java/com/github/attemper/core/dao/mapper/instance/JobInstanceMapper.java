package com.github.attemper.core.dao.mapper.instance;

import com.github.attemper.common.base.BaseMapper;
import com.github.attemper.common.result.dispatch.instance.JobInstance;
import com.github.attemper.common.result.dispatch.instance.JobInstanceAct;
import com.github.attemper.common.result.dispatch.instance.JobInstanceWithChildren;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface JobInstanceMapper extends BaseMapper<JobInstance> {

    List<JobInstanceWithChildren> listInstance(Map<String, Object> paramMap);

    int countProcessChildren(String superProcInstId);

    int countRetriedChildren(String parentId);

    int count(Map<String, Object> paramMap);

    List<JobInstanceWithChildren> listProcessChildren(String superProcInstId);

    List<JobInstanceWithChildren> listRetriedChildren(String parentId);

    JobInstanceAct getAct(String id);

    void updateHis(JobInstance jobInstance);

    void addHis(List<JobInstance> jobInstance);

    List<JobInstance> listUpgradedInstance(JobInstance jobInstance);

    void addAct(JobInstanceAct jobInstanceAct);

    void updateAct(JobInstanceAct jobInstanceAct);

    List<JobInstanceAct> listAct(Map<String, Object> paramMap);

}
