package com.github.attemper.common.param.sys.role;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class RoleResourceSaveParam extends RoleNameParam {

    protected List<String> resourceNames;

}
