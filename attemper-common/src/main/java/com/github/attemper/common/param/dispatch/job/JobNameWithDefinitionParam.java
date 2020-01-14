package com.github.attemper.common.param.dispatch.job;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JobNameWithDefinitionParam extends JobNameParam {

    protected String procDefId;
}
