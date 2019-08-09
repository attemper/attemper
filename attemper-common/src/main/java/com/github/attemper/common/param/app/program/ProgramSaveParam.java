package com.github.attemper.common.param.app.program;

import com.github.attemper.common.param.CommonParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@Getter
@Setter
@ToString
public class ProgramSaveParam implements CommonParam {

    protected String programName;

    protected int injectOrder;

    public String validate() {
        if (StringUtils.isBlank(programName)) {
            return "6600";
        }
        return null;
    }

}
