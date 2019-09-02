package com.github.attemper.core.dao.instance;

import com.github.attemper.common.base.BaseMapper;
import com.github.attemper.common.result.dispatch.instance.Instance;
import com.github.attemper.common.result.dispatch.instance.InstanceAct;
import com.github.attemper.common.result.dispatch.instance.InstanceWithChildren;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface InstanceMapper extends BaseMapper<Instance> {

    List<InstanceWithChildren> listInstance(Map<String, Object> paramMap);

    int countProcessChildren(String superProcInstId);

    int countRetriedChildren(String parentId);

    List<InstanceWithChildren> listProcessChildren(String superProcInstId);

    List<InstanceWithChildren> listRetriedChildren(String parentId);

    InstanceAct getAct(String id);

    void addAct(InstanceAct instanceAct);

    void updateAct(InstanceAct instanceAct);

    List<InstanceAct> listAct(Map<String, Object> paramMap);

    void updateDone(Instance instance);

    List<Instance> listRunningOfExecutor(String executorAddress);
}
