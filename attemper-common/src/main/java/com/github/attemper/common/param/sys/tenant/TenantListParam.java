package com.github.attemper.common.param.sys.tenant;

import com.github.attemper.common.param.PageSortParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TenantListParam extends PageSortParam {

    protected String userName;

    protected String displayName;

}
