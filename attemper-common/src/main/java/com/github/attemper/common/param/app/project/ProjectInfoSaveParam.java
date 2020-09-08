package com.github.attemper.common.param.app.project;

import com.github.attemper.common.enums.UriType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@ToString
public class ProjectInfoSaveParam extends ProjectNameParam {

    protected String uri;

    protected Integer uriType;

    @Override
    public String validate() {
        if(StringUtils.isBlank(uri)) {
            return "6506";
        }
        if(UriType.get(uriType) == null){
            return "6509";
        }
        return super.validate();
    }
}
