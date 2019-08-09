package com.github.attemper.common.param.app.program;

import com.github.attemper.common.param.CommonParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ProgramNamesParam implements CommonParam {

    protected List<String> programNames;

    public String validate() {
        if (programNames == null || programNames.isEmpty()) {
            return "6600";
        }
        return null;
    }
}
