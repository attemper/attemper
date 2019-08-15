package com.github.attemper.common.param.app.gist;

import com.github.attemper.common.param.CommonParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@Getter
@Setter
@ToString
public class GistSaveParam implements CommonParam {

    protected String gistName;

    public String validate() {
        if (StringUtils.isBlank(gistName)) {
            return "6800";
        }
        return null;
    }

}
