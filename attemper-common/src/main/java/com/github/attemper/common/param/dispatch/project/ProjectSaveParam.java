package com.github.attemper.common.param.dispatch.project;

import com.github.attemper.common.param.CommonParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectSaveParam implements CommonParam {

    protected String projectName;

    protected String parentProjectName;

    protected String displayName;

    protected String contextPath;

    protected Boolean bindExecutor;

    protected Integer position;

    public String validate() {
        if (StringUtils.isBlank(projectName)) {
            return "6500";
        } else if (projectName.length() >= 255) {
            return "1503";
        }
        if (StringUtils.isBlank(displayName)) {
            return "6503";
        }
        return null;
    }

}
