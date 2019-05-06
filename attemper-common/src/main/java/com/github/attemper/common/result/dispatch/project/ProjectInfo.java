package com.github.attemper.common.result.dispatch.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectInfo {

    protected String projectName;

    protected String parentProjectName;

    protected String displayName;

    protected String uri;

    protected String contextPath;

    protected Integer type;

}
