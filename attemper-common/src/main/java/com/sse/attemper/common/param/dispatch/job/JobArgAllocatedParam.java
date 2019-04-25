package com.sse.attemper.common.param.dispatch.job;

import com.sse.attemper.common.param.PageSortParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobArgAllocatedParam extends PageSortParam {

    protected String jobName;

    protected String argName;
}
