package com.github.attemper.common.param.sys.tag;

import com.github.attemper.common.param.CommonParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@Getter
@Setter
@ToString
public class TagSaveParam implements CommonParam {

    protected String tagName;

    protected String displayName;

    protected String remark;

    public String validate() {
        if(StringUtils.isBlank(tagName)) {
            return "5300";
        }
        if(StringUtils.isBlank(displayName)) {
            return "5303";
        }
        return null;
    }

}
