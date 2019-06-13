package com.github.attemper.common.result.dispatch.job;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author ldang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    protected String jobName;

    protected String displayName;

    protected String jobContent;

    protected int status;

    protected int timeout;

    protected boolean concurrent;

    protected Integer maxReversion;

    protected Integer maxVersion;

    protected Integer reversion;

    protected Integer version;

    protected Date createTime;

    protected Date updateTime;

    protected Date deploymentTime;

    protected List<Date> nextFireTimes;

    protected String remark;

    protected String tenantId;

}
