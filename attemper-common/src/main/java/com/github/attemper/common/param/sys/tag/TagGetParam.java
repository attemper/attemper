package com.github.attemper.common.param.sys.tag;

import com.github.attemper.common.param.CommonParam;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@ToString
public class TagGetParam implements CommonParam {

    protected String tagName;

    public String validate() {
        if(StringUtils.isBlank(tagName)) {
            return "5300";
        }
        return null;
    }

    public String getTagName() {
        return tagName;
    }

    public TagGetParam setTagName(String tagName) {
        this.tagName = tagName;
        return this;
    }
}
