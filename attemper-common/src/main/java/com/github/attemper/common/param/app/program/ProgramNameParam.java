package com.github.attemper.common.param.app.program;

import com.github.attemper.common.param.CommonParam;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@ToString
public class ProgramNameParam implements CommonParam {

    protected String programName;

    @Override
    public String validate() {
        if (StringUtils.isBlank(programName)) {
            return "6600";
        }
        return null;
    }

    public String getProgramName() {
        return programName;
    }

    public ProgramNameParam setProgramName(String programName) {
        this.programName = programName;
        return this;
    }
}
