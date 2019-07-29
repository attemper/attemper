package com.github.attemper.core.service.instance;

import com.github.attemper.common.enums.JobInstanceStatus;
import com.github.attemper.common.param.dispatch.instance.JobInstanceActParam;
import com.github.attemper.common.param.dispatch.instance.JobInstanceGetParam;
import com.github.attemper.common.param.dispatch.instance.JobInstanceListParam;
import com.github.attemper.common.param.sys.tenant.TenantGetParam;
import com.github.attemper.common.property.StatusProperty;
import com.github.attemper.common.result.dispatch.instance.JobInstance;
import com.github.attemper.common.result.dispatch.instance.JobInstanceAct;
import com.github.attemper.common.result.dispatch.instance.JobInstanceWithChildren;
import com.github.attemper.common.result.dispatch.job.Job;
import com.github.attemper.common.result.sys.tenant.Tenant;
import com.github.attemper.core.dao.mapper.instance.JobInstanceMapper;
import com.github.attemper.core.ext.notice.MessageBean;
import com.github.attemper.core.ext.notice.NoticeService;
import com.github.attemper.core.ext.notice.channel.Sender;
import com.github.attemper.core.service.job.JobService;
import com.github.attemper.sys.service.BaseServiceAdapter;
import com.github.attemper.sys.service.TenantService;
import com.github.attemper.sys.util.PageUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class JobInstanceService extends BaseServiceAdapter {

    @Autowired
    private JobInstanceMapper mapper;

    @Autowired
    private JobService jobService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private TenantService tenantService;

    public JobInstance get(JobInstanceGetParam param) {
        return mapper.get(injectTenantIdToMap(param));
    }

    public int count(JobInstanceListParam param, String tenantId) {
        return mapper.count(injectTenantIdToMap(param, tenantId));
    }

    public JobInstanceAct getAct(String actInstId) {
        return mapper.getAct(actInstId);
    }

    public Map<String, Object> list(JobInstanceListParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        Page<JobInstanceWithChildren> list = (Page<JobInstanceWithChildren>) mapper.listInstance(paramMap);
        if (param.isListChildren()) {
            list.forEach(item -> {
                item.setRowKey(item.getId());
                if (item.isRetried()) {
                    List<JobInstanceWithChildren> children = mapper.listChildren(item.getId());
                    if (children.isEmpty()) {
                        log.error("children empty:{}", item.getId());
                        item.setRetried(false);
                    } else {
                        children.forEach(child -> child.setRowKey(child.getParentId() + ":" + child.getId()));
                        item.setChildren(children);
                    }
                }
            });
        }
        return PageUtil.toResultMap(list);
    }

    public List<JobInstanceAct> listAct(JobInstanceActParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        return mapper.listAct(paramMap);
    }

    public void add(JobInstance jobInstance) {
        // upgrade form instance to history
        mapper.upgradeToHis(jobInstance);
        if (jobInstance.getDisplayName() == null) {
            Job job = jobService.get(jobInstance.getJobName(), jobInstance.getTenantId());
            if (job != null) {
                jobInstance.setDisplayName(job.getDisplayName());
            }
        }
        mapper.add(jobInstance);
        noticeWithInstance(jobInstance);
    }

    public void update(JobInstance jobInstance) {
        mapper.update(jobInstance);
        noticeWithInstance(jobInstance);
    }

    public void addAct(JobInstanceAct jobInstanceAct) {
        mapper.addAct(jobInstanceAct);
    }

    public void updateAct(JobInstanceAct jobInstanceAct) {
        mapper.updateAct(jobInstanceAct);
    }

    @Async
    public void noticeWithInstance(JobInstance jobInstance) {
        if (JobInstanceStatus.FAILURE.getStatus() == jobInstance.getStatus()) {
            try {
                Tenant tenant = tenantService.get(new TenantGetParam().setUserName(jobInstance.getTenantId()));
                MessageBean messageBean = new MessageBean()
                        .setTo(tenant.getEmail())
                        .setSubject(MessageFormat.format(StatusProperty.getValue(900), jobInstance.getJobName(), jobInstance.getDisplayName()))
                        .setContent(jobInstance.getMsg());
                String sendConfig = tenant.getSendConfig();
                if (StringUtils.isNotBlank(sendConfig)) {
                    for (int i = 0; i < sendConfig.length(); i++) {
                        if (sendConfig.charAt(i) != '0') {
                            Sender sender = noticeService.getSenderMap().get(i);
                            if (sender != null) {
                                sender.send(messageBean);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
