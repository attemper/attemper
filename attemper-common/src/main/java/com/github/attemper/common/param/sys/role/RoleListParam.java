package com.github.attemper.common.param.sys.role;

import com.github.attemper.common.param.PageSortParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoleListParam extends PageSortParam {

    protected String roleName;

    protected String displayName;

}
