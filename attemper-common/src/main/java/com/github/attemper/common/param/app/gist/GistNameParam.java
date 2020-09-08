package com.github.attemper.common.param.app.gist;

import com.github.attemper.common.param.CommonParam;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@ToString
public class GistNameParam implements CommonParam {

    protected String gistName;

    @Override
    public String validate() {
        if (StringUtils.isBlank(gistName)) {
            return "6800";
        }
        return null;
    }

    public String getGistName() {
        return gistName;
    }

    public GistNameParam setGistName(String gistName) {
        this.gistName = gistName;
        return this;
    }
}
