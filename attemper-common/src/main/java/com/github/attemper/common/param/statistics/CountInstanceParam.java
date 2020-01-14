package com.github.attemper.common.param.statistics;

import com.github.attemper.common.param.CommonParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@Getter
@Setter
@ToString
public class CountInstanceParam implements CommonParam  {

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
        return null;
    }
}
