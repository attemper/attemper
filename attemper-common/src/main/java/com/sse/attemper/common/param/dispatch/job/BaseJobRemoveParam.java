package com.sse.attemper.common.param.dispatch.job;

import com.sse.attemper.common.param.CommonParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ldang
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseJobRemoveParam implements CommonParam {

    protected List<String> jobNames;

    public String validate() {
        if(jobNames == null || jobNames.isEmpty()){
            return "6000";
        }
        return null;
    }
}
