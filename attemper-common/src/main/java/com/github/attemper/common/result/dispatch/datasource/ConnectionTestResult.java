package com.github.attemper.common.result.dispatch.datasource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionTestResult {

    protected String dbName;

    protected String errorMsg;
}
