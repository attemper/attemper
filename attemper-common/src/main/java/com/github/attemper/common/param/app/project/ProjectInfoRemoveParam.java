package com.github.attemper.common.param.app.project;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@Getter
@Setter
@ToString
public class ProjectInfoRemoveParam extends ProjectNameParam {

    protected String uri;

    @Override
    public String validate() {
        if(StringUtils.isBlank(uri)) {
            return "6506";
        }
        return super.validate();
    }
}
