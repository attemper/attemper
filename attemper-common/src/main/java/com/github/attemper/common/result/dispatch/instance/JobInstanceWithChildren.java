package com.github.attemper.common.result.dispatch.instance;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class JobInstanceWithChildren extends JobInstance{

    protected boolean hasChildren;

    protected List<JobInstanceWithChildren> children;

}
