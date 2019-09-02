package com.github.attemper.common.result.dispatch.instance;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class InstanceWithChildren extends Instance {

    protected boolean hasChildren;

    protected List<InstanceWithChildren> children;

}
