package com.sse.attemper.common.param.sys.tenant;

import com.sse.attemper.common.param.PageSortParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenantListParam extends PageSortParam {

    protected String id;

    protected String name;

}
