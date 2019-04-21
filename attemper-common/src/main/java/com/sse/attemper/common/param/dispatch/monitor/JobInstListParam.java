package com.sse.attemper.common.param.dispatch.monitor;

import com.sse.attemper.common.param.PageSortParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author ldang
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobInstListParam extends PageSortParam {

    protected String jobName;

    protected String displayName;

    protected List<Integer> status;

    protected Date lowerStartTime;

    protected Date upperStartTime;

    protected Date lowerEndTime;

    protected Date upperEndTime;

    protected Long lowerDuration;

    protected Long upperDuration;
}
