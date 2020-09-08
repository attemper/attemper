package com.github.attemper.common.param.sys.role;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Getter
@Setter
@ToString
public class RoleTenantSaveParam extends RoleNameParam {

    protected List<String> userNames;

}
