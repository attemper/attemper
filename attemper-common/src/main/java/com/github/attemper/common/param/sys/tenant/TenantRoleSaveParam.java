package com.github.attemper.common.param.sys.tenant;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class TenantRoleSaveParam extends TenantNameParam {

    protected List<String> roleNames;

}
