package com.thor.core.param.tag;

import com.thor.common.param.CommonParam;
import com.thor.core.enums.TagType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * @author ldang
 */
@Data
public class TagRemoveParam implements CommonParam {

    @ApiModelProperty(value = "标签名称数组", dataType = "String[]", position = 1)
    private String[] tagNames;

    @ApiModelProperty(value = "标签类型", dataType = "String", position = 10)
    private Integer tagType;

    @Override
    public String validate() {
        if(tagNames == null || tagNames.length == 0 || StringUtils.isBlank(tagNames[0])){
            return "5300";
        }
        if(TagType.get(tagType) == null){
            return "5321";
        }
        return null;
    }
}
