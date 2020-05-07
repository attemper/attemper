package com.github.attemper.core.service.instance;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.enums.InstanceStatus;
import com.github.attemper.common.param.dispatch.instance.InstanceActParam;
import com.github.attemper.common.param.dispatch.instance.InstanceGetParam;
import com.github.attemper.common.param.dispatch.instance.InstanceListParam;
import com.github.attemper.common.param.sys.tenant.TenantNameParam;
import com.github.attemper.common.property.TipProperty;
import com.github.attemper.common.result.dispatch.instance.Instance;
import com.github.attemper.common.result.dispatch.instance.InstanceAct;
import com.github.attemper.common.result.dispatch.instance.InstanceWithChildren;
import com.github.attemper.common.result.sys.tenant.Tenant;
import com.github.attemper.core.dao.instance.InstanceMapper;
import com.github.attemper.core.ext.notice.MessageBean;
import com.github.attemper.core.ext.notice.NoticeService;
import com.github.attemper.core.ext.notice.channel.Sender;
import com.github.attemper.core.service.dispatch.ArgService;
import com.github.attemper.java.sdk.common.util.DateUtil;
import com.github.attemper.sys.service.BaseServiceAdapter;
import com.github.attemper.sys.service.TenantService;
import com.github.attemper.sys.util.PageUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class InstanceService extends BaseServiceAdapter {

    @Autowired
    private InstanceMapper mapper;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private ArgService argService;

    @Autowired
    private ScheduledExecutorService scheduledExecutorService;

    public Instance get(String id) {
        return mapper.getById(id);
    }

    public List<Instance> getByIds(List<String> ids) {
        return mapper.getByIds(ids);
    }

    public Instance getByInstId(String procInstId) {
        return mapper.getByInstId(procInstId);
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
        mapper.addExecution(instance);
        mapper.addInstance(instance);
        noticeWithInstance(instance);
        return instance;
    }

    public void update(Instance instance) {
        mapper.updateExecution(instance);
        mapper.updateInstance(instance);
        noticeWithInstance(instance);
    }

    public void updateDone(Instance instance) {
        mapper.updateDone(instance);
        mapper.deleteExecution(instance);
        noticeWithInstance(instance);
    }

    public void addAct(InstanceAct instanceAct) {
        mapper.addAct(instanceAct);
    }

    public void updateAct(InstanceAct instanceAct) {
        mapper.updateAct(instanceAct);
    }

    public List<Instance> listRunningOfExecutor(String executorAddress) {
        return mapper.listRunningOfExecutor(executorAddress);
    }

    public int countInstance(Map<String, Object> paramMap) {
        return mapper.countInstance(paramMap);
    }

    public void noticeWithInstance(Instance instance) {
        if (InstanceStatus.FAILURE.getStatus() == instance.getStatus()) {
            String noticeTime = DateUtil.format(new Date(), DateUtil.DATE_FORMAT_YYYYMMDDHHMMSS);
            scheduledExecutorService.schedule(() -> {
                try {
                    sendNotice(instance, noticeTime);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }, 5 + (int) (Math.random() * 3), TimeUnit.SECONDS);
        }
    }

    private void sendNotice(Instance instance, String noticeTime) {
        Tenant tenant = tenantService.get(new TenantNameParam().setUserName(instance.getTenantId()));
        MessageBean messageBean = buildMessageBean(
                instance,
                tenant,
                noticeTime);
        Map<String, Object> alarmArgMap = argService.listStartsWith(CommonConstants.KEY_ALARM_ARG, instance.getTenantId());
        messageBean.setExtraMap(alarmArgMap);
        String sendConfig = tenant.getSendConfig();
        if (StringUtils.isNotBlank(sendConfig)) {
            for (int i = 0; i < sendConfig.length(); i++) {
                if (sendConfig.charAt(i) != '0') {
                    Sender sender = noticeService.getSenderMap().get(i);
                    if (sender != null) {
                        try {
                            sender.send(messageBean);
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                    } else {
                        log.error("send is missing:{} of {}", i, sendConfig);
                    }
                }
            }
        }
    }

    private MessageBean buildMessageBean(Instance instance, Tenant tenant, String now) {
        StringBuilder actIdSB = new StringBuilder();
        StringBuilder actNameSB = new StringBuilder();
        StringBuilder bizUriSB = new StringBuilder();
        StringBuilder logKeySB = new StringBuilder();
        StringBuilder errorMsgSB = new StringBuilder();
        String alarmPosition = CommonConstants.EMPTY;
        if (StringUtils.isNotBlank(instance.getProcInstId())) {
            List<InstanceAct> instanceActs = listAct(new InstanceActParam().setProcInstId(instance.getProcInstId()));
            for (InstanceAct instanceAct : instanceActs) {
                if (instanceAct.getStatus() == InstanceStatus.FAILURE.getStatus()
                        && (StringUtils.isNotBlank(instanceAct.getLogKey())
                        || StringUtils.isNotBlank(instanceAct.getLogText()))) {
                    actIdSB.append(StringUtils.trimToEmpty(instanceAct.getActId())).append(CommonConstants.COMMA);
                    if (instanceAct.getActName() != null) {
                        actNameSB.append(instanceAct.getActName()).append(CommonConstants.COMMA);
                    }
                    if (instanceAct.getBizUri() != null) {
                        bizUriSB.append(instanceAct.getBizUri()).append(CommonConstants.COMMA);
                    }
                    if (instanceAct.getLogKey() != null) {
                        logKeySB.append(instanceAct.getLogKey()).append(CommonConstants.COMMA);
                    }
                    if (StringUtils.isBlank(instanceAct.getLogText())) {
                        errorMsgSB.append(instanceAct.getIncidentMsg()).append(CommonConstants.BR);
                    } else {
                        errorMsgSB
                                .append(instanceAct.getLogText()).append(CommonConstants.BR)
                                .append(instanceAct.getIncidentMsg()).append(CommonConstants.BR);
                    }
                }
            }
            if (actIdSB.length() >= 1) {
                actIdSB.deleteCharAt(actIdSB.length() - 1);
            }
            if (actNameSB.length() >= 1) {
                actNameSB.deleteCharAt(actNameSB.length() - 1);
            }
            if (bizUriSB.length() >= 1) {
                bizUriSB.deleteCharAt(bizUriSB.length() - 1);
                if (bizUriSB.length() >= 1) {
                    alarmPosition = TipProperty.getValue(10053);
                } else {
                    alarmPosition = TipProperty.getValue(10054);
                }
            }
            if (logKeySB.length() >= 1) {
                logKeySB.deleteCharAt(logKeySB.length() - 1);
            }
        } else {
            alarmPosition = TipProperty.getValue(10052);
            logKeySB.append(instance.getCode());
            errorMsgSB.append(instance.getMsg());
        }
        String subject = MessageFormat.format(TipProperty.getValue(10050),
                instance.getJobName(),
                instance.getDisplayName());
        String content = MessageFormat.format(TipProperty.getValue(10051),
                now,
                tenant.getUserName(),
                tenant.getDisplayName(),
                instance.getJobName(),
                instance.getDisplayName(),
                instance.getStartTime() == null ? CommonConstants.EMPTY : DateUtil.format(new Date(instance.getStartTime()), DateUtil.DATE_FORMAT_YYYYMMDDHHMMSS),
                actIdSB,
                actNameSB,
                instance.getSchedulerUri(),
                StringUtils.trimToEmpty(instance.getExecutorUri()),
                bizUriSB,
                alarmPosition,
                logKeySB,
                errorMsgSB
                );
        return new MessageBean()
                .setTo(tenant)
                .setSubject(subject)
                .setContent(content);
    }
}
