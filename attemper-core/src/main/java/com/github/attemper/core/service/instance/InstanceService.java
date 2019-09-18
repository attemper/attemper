package com.github.attemper.core.service.instance;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.enums.InstanceStatus;
import com.github.attemper.common.param.dispatch.instance.InstanceActParam;
import com.github.attemper.common.param.dispatch.instance.InstanceGetParam;
import com.github.attemper.common.param.dispatch.instance.InstanceListParam;
import com.github.attemper.common.param.sys.tenant.TenantGetParam;
import com.github.attemper.common.property.StatusProperty;
import com.github.attemper.common.result.dispatch.instance.Instance;
import com.github.attemper.common.result.dispatch.instance.InstanceAct;
import com.github.attemper.common.result.dispatch.instance.InstanceWithChildren;
import com.github.attemper.common.result.dispatch.job.Job;
import com.github.attemper.common.result.sys.tenant.Tenant;
import com.github.attemper.core.dao.instance.InstanceMapper;
import com.github.attemper.core.ext.notice.MessageBean;
import com.github.attemper.core.ext.notice.NoticeService;
import com.github.attemper.core.ext.notice.channel.Sender;
import com.github.attemper.core.service.dispatch.JobService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class InstanceService extends BaseServiceAdapter {

    @Autowired
    private InstanceMapper mapper;

    @Autowired
    private JobService jobService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private TenantService tenantService;

    public Instance get(String id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put(CommonConstants.id, id);
        return mapper.get(map);
    }

    public Instance getByInstId(String procInstId) {
        Map<String, Object> map = new HashMap<>(1);
        map.put(CommonConstants.procInstId, procInstId);
        return mapper.get(map);
    }

    public int count(InstanceListParam param, String tenantId) {
        return mapper.count(injectTenantIdToMap(param, tenantId));
    }

    public Map<String, Object> listMonitor(InstanceListParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        Page<InstanceWithChildren> list = (Page<InstanceWithChildren>) mapper.listInstance(paramMap);
        if (param.isListChildren()) {
            list.parallelStream().forEach(item -> {
                if (item.getProcInstId() != null) {
                    item.setHasChildren(mapper.countProcessChildren(item.getProcInstId()) > 0);
                }
            });
        }
        return PageUtil.toResultMap(list);
    }

    public List<InstanceWithChildren> listMonitorChildren(InstanceGetParam param) {
        List<InstanceWithChildren> list = mapper.listProcessChildren(param.getProcInstId());
        list.parallelStream().forEach(item -> {
            if (item.getProcInstId() != null) {
                item.setHasChildren(mapper.countProcessChildren(item.getProcInstId()) > 0);
            }
        });
        return list;
    }

    public Map<String, Object> listRetry(InstanceListParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        paramMap.put(CommonConstants.isRetry, true);
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        Page<InstanceWithChildren> list = (Page<InstanceWithChildren>) mapper.listInstance(paramMap);
        if (param.isListChildren()) {
            list.parallelStream().forEach(item -> {
                item.setHasChildren(mapper.countRetriedChildren(item.getId()) > 0);
            });
        }
        return PageUtil.toResultMap(list);
    }

    public List<InstanceWithChildren> listRetriedChildren(InstanceGetParam param) {
        List<InstanceWithChildren> list = mapper.listRetriedChildren(param.getId());
        list.parallelStream().forEach(item -> {
            item.setHasChildren(mapper.countRetriedChildren(item.getId()) > 0);
        });
        return list;
    }

    public List<InstanceAct> listAct(InstanceActParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        return mapper.listAct(paramMap);
    }

    public Instance add(Instance instance) {
        if (instance.getDisplayName() == null) {
            Job job = jobService.get(instance.getJobName(), instance.getTenantId());
            if (job != null) {
                instance.setDisplayName(job.getDisplayName());
            }
        }
        mapper.add(instance);
        noticeWithInstance(instance);
        return instance;
    }

    public Instance update(Instance instance) {
        mapper.update(instance);
        noticeWithInstance(instance);
        return instance;
    }

    public Instance updateDone(Instance instance) {
        mapper.updateDone(instance);
        noticeWithInstance(instance);
        return instance;
    }

    public InstanceAct addAct(InstanceAct instanceAct) {
        mapper.addAct(instanceAct);
        return instanceAct;
    }

    public InstanceAct updateAct(InstanceAct instanceAct) {
        mapper.updateAct(instanceAct);
        return instanceAct;
    }

    public List<Instance> listRunningOfExecutor(String executorAddress) {
        return mapper.listRunningOfExecutor(executorAddress);
    }

    @Async
    public void noticeWithInstance(Instance instance) {
        if (InstanceStatus.FAILURE.getStatus() == instance.getStatus()) {
            try {
                Tenant tenant = tenantService.get(new TenantGetParam().setUserName(instance.getTenantId()));
                MessageBean messageBean = new MessageBean()
                        .setTo(tenant)
                        .setSubject(MessageFormat.format(StatusProperty.getValue(900), instance.getJobName(), instance.getDisplayName()))
                        .setContent(instance.getMsg());
                String sendConfig = tenant.getSendConfig();
                if (StringUtils.isNotBlank(sendConfig)) {
                    for (int i = 0; i < sendConfig.length(); i++) {
                        if (sendConfig.charAt(i) != '0') {
                            Sender sender = noticeService.getSenderMap().get(i);
                            if (sender != null) {
                                sender.send(messageBean);
                            } else {
                                log.error("send is missing:{} of {}", i, sendConfig);
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
