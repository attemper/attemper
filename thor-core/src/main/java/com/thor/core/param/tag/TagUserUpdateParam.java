package com.thor.core.param.tag;

import com.thor.common.param.CommonParam;
import com.thor.core.enums.TagType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author ldang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagUserUpdateParam implements CommonParam {

    @NotBlank(message = "5300")
    @ApiModelProperty(value = "标签名称", dataType = "String", position = 5)
    private String tagName;

    @ApiModelProperty(value = "标签类型", dataType = "String", position = 10)
    private Integer tagType;

    @ApiModelProperty(value = "用户名数组", dataType = "String[]", position = 15)
    private String[] userNames;

    @Override
    public String validate() {
        if(TagType.get(tagType) == null){
            return "5321";
        }
        return null;
    }
}
