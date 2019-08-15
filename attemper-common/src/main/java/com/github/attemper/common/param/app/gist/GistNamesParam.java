package com.github.attemper.common.param.app.gist;

import com.github.attemper.common.param.CommonParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class GistNamesParam implements CommonParam {

    protected List<String> gistNames;

    public String validate() {
        if (gistNames == null || gistNames.isEmpty()) {
            return "6800";
        }
        return null;
    }
}
