package com.github.attemper.common.result.dispatch.instance;

import lombok.Data;

import java.util.List;

@Data
public class JobInstanceWithChildren extends JobInstance{

    protected String rowKey;

    protected List<JobInstanceWithChildren> children;

}
