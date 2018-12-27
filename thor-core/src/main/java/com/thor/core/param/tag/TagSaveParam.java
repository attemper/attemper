package com.thor.core.param.tag;

import com.thor.common.param.CommonParam;
import com.thor.core.entity.Tag;
import com.thor.core.enums.TagType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author ldang
 */
@Data
public class TagSaveParam implements CommonParam {

    @Length(max = 64, message = "1503")
    @NotBlank(message = "5300")
    @ApiModelProperty(value = "标签名称", dataType = "String", position = 1)
    private String tagName;

    @Length(max = 255, message = "1503")
    @NotBlank(message = "5303")
    @ApiModelProperty(value = "标签中文名", dataType = "String", position = 5)
    private String displayName;

    @ApiModelProperty(value = "标签类型", dataType = "Integer", position = 15)
    private Integer tagType;

    @Length(max = 500, message = "1503")
    @ApiModelProperty(value = "备注", dataType = "String", position = 25)
    private String remark;

    @Override
    public String validate() {
        if(TagType.get(tagType) == null){
            return "5321";
        }
        return null;
    }

    public Tag toTag() {
        return Tag.builder()
                .tagName(tagName)
                .displayName(displayName)
                .tagType(tagType)
                .remark(remark)
                .build();
    }
}
