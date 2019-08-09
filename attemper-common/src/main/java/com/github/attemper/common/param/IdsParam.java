package com.github.attemper.common.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class IdsParam implements CommonParam {

    protected List<String> ids;

    public String validate() {
        if (ids == null || ids.isEmpty()) {
            return "1504";
        }
        return null;
    }
}
