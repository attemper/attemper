package com.thor.core.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.thor.core.dao.mapper.AtomJobMapper;
import com.thor.core.util.PageUtil;
import com.thor.sdk.common.param.job.atom.AtomJobListParam;
import com.thor.sdk.common.result.job.atom.AtomJob;
import com.thor.sys.service.BaseServiceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

/**
 * @author ldang
 */
@Service
@Transactional
public class AtomJobService extends BaseServiceAdapter {

    private @Autowired
    AtomJobMapper mapper;

    /**
     * 根据id查询租户
     * @param getParam
     * @return
     */
/*    public AtomJob get(AtomJobGetParam getParam) {
        return mapper.get(getParam.getId());
    }*/

    /**
     * 查询列表
     * @param listParam
     * @return
     */
    public Map<String, Object> list(AtomJobListParam listParam) {
        Map<String, Object> paramMap = injectAdminedTenantIdToMap(listParam);
        PageHelper.startPage(listParam.getCurrentPage(), listParam.getPageSize());
        Page<AtomJob> list = (Page<AtomJob>) mapper.list(paramMap);
        return PageUtil.toResultMap(list);
    }

    /**
     * 新增
     * @param saveParam
     * @return
     */
    /*public AtomJob add(AtomJobSaveParam saveParam) {
        //主键应在数据库中   不存在
        AtomJob AtomJob = get(new AtomJobGetParam(saveParam.getId()));
        if(AtomJob != null){
            throw new DuplicateKeyException(saveParam.getId());
        }
        AtomJob = toAtomJob(saveParam);
        AtomJob.setCreateTime(new Date());
        return save(AtomJob);
    }*/

    /**
     * 更新
     * @param saveParam
     * @return
     */
    /*public AtomJob update(AtomJobSaveParam saveParam) {
        //主键应在数据库中  存在
        AtomJob AtomJob = get(new AtomJobGetParam(saveParam.getId()));
        if(AtomJob == null){
            throw new RTException(5150);  //5150
        }
        AtomJob updatedAtomJob = toAtomJob(saveParam);
        updatedAtomJob.setCreateTime(AtomJob.getCreateTime());
        return save(updatedAtomJob);
    }*/

    /**
     * 保存
     * @param AtomJob
     */
    /*public AtomJob save(AtomJob AtomJob) {
        String sign = secretService.encode(AtomJob.getId());
        AtomJob.setSign(sign);
        AtomJob.setUpdateTime(new Date());
        mapper.save(BeanUtil.beanToMap(AtomJob));
        return AtomJob;
    }*/

    /**
     * 删除
     * @param removeParam
     * @return
     */
    /*public void remove(AtomJobRemoveParam removeParam) {
        for (String id : removeParam.getIds()) {
            if (StringUtils.equals(id, coreProperties.getSuperAtomJob().getId())) {
                throw new RTException(5115, id);
            }
        }
        mapper.delete(removeParam.getIds());
    }*/

    /*private AtomJob toAtomJob(AtomJobSaveParam saveParam) {
        return AtomJob.builder()
                .id(saveParam.getId())
                .name(saveParam.getName())
                .admin(saveParam.getAdmin())
                .build();
    }*/
}
