package com.github.attemper.core.dao.mapper.instance;

import com.github.attemper.common.base.BaseMapper;
import com.github.attemper.common.result.dispatch.instance.JobInstance;
import com.github.attemper.common.result.dispatch.instance.JobInstanceAct;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface JobInstanceMapper extends BaseMapper<JobInstance> {

    JobInstance get(String id);

    JobInstanceAct getAct(String actInstId);

    void addAct(JobInstanceAct jobInstanceAct);

    void updateAct(JobInstanceAct jobInstanceAct);

    List<JobInstanceAct> listAct(Map<String, Object> paramMap);

}
