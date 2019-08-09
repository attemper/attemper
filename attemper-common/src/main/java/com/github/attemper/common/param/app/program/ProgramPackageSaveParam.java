package com.github.attemper.common.param.app.program;

import com.github.attemper.common.param.CommonParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@Getter
@Setter
@ToString
public class ProgramPackageSaveParam implements CommonParam {

    protected String programName;


    public String validate() {
        if (StringUtils.isBlank(programName)) {
            return "6600";
        }
        return null;
    }

}
