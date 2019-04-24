package com.sse.attemper.common.param.dispatch.monitor;

import com.sse.attemper.common.param.PageSortParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

/**
 * @author ldang
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobInstanceListParam extends PageSortParam {

    protected String rootProcInstId;

    protected String jobName;

    protected String displayName;

    protected Integer[] status;

    protected String lowerStartTime;

    protected String upperStartTime;

    protected String lowerEndTime;

    protected String upperEndTime;

    protected Long lowerDuration;

    protected Long upperDuration;

    @Override
    public void preHandle() {
        lowerStartTime = StringUtils.trimToNull(lowerStartTime);
        upperStartTime = StringUtils.trimToNull(upperStartTime);
        lowerEndTime = StringUtils.trimToNull(lowerEndTime);
        upperEndTime = StringUtils.trimToNull(upperEndTime);
    }
}
