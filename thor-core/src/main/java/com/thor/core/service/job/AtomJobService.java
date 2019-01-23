package com.thor.core.service.job;

import com.thor.core.dao.mapper.job.BaseJobMapper;
import com.thor.sdk.common.param.job.atom.AtomJobConfigUpdateParam;
import com.thor.sdk.common.result.job.BaseJob;
import com.thor.sys.service.BaseServiceAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ldang
 */
@Service
@Transactional
@Slf4j
public class AtomJobService extends BaseServiceAdapter {

    @Autowired
    private BaseJobMapper mapper;


    /**
     * 更新
     * @param saveParam
     * @return
     */
    public BaseJob updateAtomJobConfig(AtomJobConfigUpdateParam saveParam) {
        BaseJob atomJob = mapper.get(injectAdminedTenantIdToMap(saveParam));
        atomJob.setJobContent(saveParam.getJobContent());
        mapper.update(atomJob);
        return atomJob;
    }
}
