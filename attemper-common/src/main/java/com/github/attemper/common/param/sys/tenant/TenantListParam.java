package com.github.attemper.common.param.sys.tenant;

import com.github.attemper.common.param.PageSortParam;
import lombok.Data;

@Data
public class TenantListParam extends PageSortParam {

    protected String userName;

    protected String displayName;

}
