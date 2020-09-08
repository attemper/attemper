package com.github.attemper.common.param.statistics;

import com.github.attemper.common.param.PageSortParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;


@Getter
@Setter
@ToString
public class InstanceDurationParam extends PageSortParam {

    protected String jobName;

    protected Long lowerStartTime;

    protected Long upperStartTime;

    protected String firedSource;

    @Override
    public void preHandle() {
        firedSource = StringUtils.trimToNull(firedSource);
    }

    @Override
    public String validate() {
        if (StringUtils.isBlank(jobName)) {
            return "6000";
        }
        return null;
    }
}
