package com.github.attemper.common.result.app.project;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
