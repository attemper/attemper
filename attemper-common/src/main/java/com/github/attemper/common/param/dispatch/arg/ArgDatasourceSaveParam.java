package com.github.attemper.common.param.dispatch.arg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArgDatasourceSaveParam {

    protected String argName;

    protected String dbName;
}
