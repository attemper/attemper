package com.github.attemper.common.param.app.project;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ProjectExecutorSaveParam extends ProjectNameParam {

    protected List<String> executorUris;
}
