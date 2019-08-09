package com.github.attemper.common.param.app.program;

import com.github.attemper.common.param.PageSortParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@Getter
@Setter
@ToString
public class ProgramPackageListParam extends PageSortParam {

    protected String programName;

    protected String packageName;

    @Override
    public String validate() {
        if (StringUtils.isBlank(programName)) {
            return "6600";
        }
        return null;
    }
}
