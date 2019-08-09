package com.github.attemper.common.param.app.program;

import com.github.attemper.common.param.PageSortParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProgramListParam extends PageSortParam {

    protected String programName;
}
