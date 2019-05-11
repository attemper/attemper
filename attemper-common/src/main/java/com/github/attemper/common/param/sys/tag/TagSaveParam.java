package com.github.attemper.common.param.sys.tag;

import com.github.attemper.common.param.CommonParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

/**
 * @author ldang
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
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
