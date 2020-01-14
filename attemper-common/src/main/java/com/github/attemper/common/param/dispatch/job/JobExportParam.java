package com.github.attemper.common.param.dispatch.job;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JobExportParam extends JobNamesParam {

    protected String fileName;

}
