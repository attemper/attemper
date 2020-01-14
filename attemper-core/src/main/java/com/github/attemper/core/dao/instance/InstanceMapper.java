package com.github.attemper.core.dao.instance;

import com.github.attemper.common.result.dispatch.instance.Instance;
import com.github.attemper.common.result.dispatch.instance.InstanceAct;
import com.github.attemper.common.result.dispatch.instance.InstanceWithChildren;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface InstanceMapper {

    Instance getById(String id);

    Instance getByInstId(String procInstId);

    int count(Map<String, Object> paramMap);

    void addExecution(Instance instance);

    void addInstance(Instance instance);

    void updateExecution(Instance instance);

    void updateInstance(Instance instance);

    List<InstanceWithChildren> listInstance(Map<String, Object> paramMap);

    int countProcessChildren(String superProcInstId);

    int countRetriedChildren(String parentId);

    int countInstance(Map<String, Object> paramMap);

    List<InstanceWithChildren> listProcessChildren(String superProcInstId);

    List<InstanceWithChildren> listRetriedChildren(String parentId);

    void addAct(InstanceAct instanceAct);

    void updateAct(InstanceAct instanceAct);

    List<InstanceAct> listAct(Map<String, Object> paramMap);

    void updateDone(Instance instance);

    void deleteExecution(Instance instance);

    List<Instance> listRunningOfExecutor(String executorAddress);

    List<Instance> getByIds(List<String> ids);
}
