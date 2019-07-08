package com.github.attemper.common.result.dispatch.project;

import lombok.*;

@Getter
@Setter
@ToString
public class ProjectInfo {

    protected String projectName;

    protected String parentProjectName;

    protected String displayName;

    protected String uri;

    protected String contextPath;

    protected Integer type;

}
