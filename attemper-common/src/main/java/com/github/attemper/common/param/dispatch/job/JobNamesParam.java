package com.github.attemper.common.param.dispatch.job;

import com.github.attemper.common.param.CommonParam;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JobNamesParam implements CommonParam {

    protected List<String> jobNames;

    public String validate() {
        if(jobNames == null || jobNames.isEmpty()){
            return "6000";
        }
        return null;
    }
}
