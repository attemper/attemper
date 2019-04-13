package com.sse.attemper.common.param.dispatch.project;

import com.sse.attemper.common.param.CommonParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

/**
 * @author ldang
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRemoveParam implements CommonParam {

    protected String[] projectNames;

    public String validate() {
        if(projectNames == null || projectNames.length == 0 || StringUtils.isBlank(projectNames[0])){
            return "6500";
        }
        return null;
    }

}
