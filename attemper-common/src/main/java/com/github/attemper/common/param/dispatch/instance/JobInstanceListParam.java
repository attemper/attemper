package com.github.attemper.common.param.dispatch.instance;

import com.github.attemper.common.param.PageSortParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobInstanceListParam extends PageSortParam {

    protected String rootProcInstId;

    protected String jobName;

    protected String displayName;

    protected List<Integer> status;

    protected String lowerStartTime;

    protected String upperStartTime;

    protected String lowerEndTime;

    protected String upperEndTime;

    protected Long lowerDuration;

    protected Long upperDuration;

    protected boolean listChildren;

    @Override
    public void preHandle() {
        lowerStartTime = StringUtils.trimToNull(lowerStartTime);
        upperStartTime = StringUtils.trimToNull(upperStartTime);
        lowerEndTime = StringUtils.trimToNull(lowerEndTime);
        upperEndTime = StringUtils.trimToNull(upperEndTime);
    }
}
