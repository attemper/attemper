package com.github.attemper.common.param.sys.tag;

import com.github.attemper.common.enums.TagType;
import com.github.attemper.common.param.CommonParam;
import com.github.attemper.common.enums.TagType;
import com.github.attemper.common.param.CommonParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * @author ldang
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagRemoveParam implements CommonParam {

    protected List<String> tagNames;

    protected Integer tagType;

    public String validate() {
        if (tagNames == null || tagNames.isEmpty() || StringUtils.isBlank(tagNames.get(0))) {
            return "5300";
        }
        if(TagType.get(tagType) == null){
            return "5321";
        }
        return null;
    }

}
