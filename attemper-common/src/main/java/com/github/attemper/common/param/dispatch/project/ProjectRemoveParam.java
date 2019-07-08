package com.github.attemper.common.param.dispatch.project;

import com.github.attemper.common.param.CommonParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

import java.util.List;

@Getter
@Setter
@ToString
public class ProjectRemoveParam implements CommonParam {

    protected List<String> projectNames;

    public String validate() {
        if (projectNames == null || projectNames.isEmpty() || StringUtils.isBlank(projectNames.get(0))) {
            return "6500";
        }
        return null;
    }

}
