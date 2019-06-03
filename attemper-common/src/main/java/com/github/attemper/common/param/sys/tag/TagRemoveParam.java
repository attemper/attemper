package com.github.attemper.common.param.sys.tag;

import com.github.attemper.common.param.CommonParam;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * @author ldang
 */
@Data
public class TagRemoveParam implements CommonParam {

    protected List<String> tagNames;

    public String validate() {
        if (tagNames == null || tagNames.isEmpty() || StringUtils.isBlank(tagNames.get(0))) {
            return "5300";
        }
        return null;
    }

}
