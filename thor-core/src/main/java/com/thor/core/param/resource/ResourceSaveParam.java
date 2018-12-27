package com.thor.core.param.resource;

import com.thor.common.param.CommonParam;
import com.thor.core.entity.Resource;
import com.thor.core.enums.ResourceType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author ldang
 */
@Data
public class ResourceSaveParam implements CommonParam {

    @Length(max = 255, message = "1503")
    @NotBlank(message = "5400")
    @ApiModelProperty(value = "资源名称", dataType = "String", position = 1)
    private String resourceName;

    @Length(max = 255, message = "1503")
    @NotBlank(message = "5401")
    @ApiModelProperty(value = "父资源名称", dataType = "String", position = 5)
    private String parentResourceName;

    @Length(max = 255, message = "1503")
    @NotBlank(message = "5403")
    @ApiModelProperty(value = "中文名称", dataType = "String", position = 10)
    private String displayName;

    @ApiModelProperty(value = "资源类型", dataType = "Integer", position = 15)
    private Integer resourceType;

    @Length(max = 255, message = "1503")
    @ApiModelProperty(value = "资源路径", dataType = "String", position = 20)
    private String uri;

    @Length(max = 255, message = "1503")
    @ApiModelProperty(value = "图标代码", dataType = "String", position = 25)
    private String icon;

    @ApiModelProperty(value = "位置/排序", dataType = "Integer", position = 30)
    private Integer position;

    @Length(max = 1000, message = "1503")
    @ApiModelProperty(value = "额外属性(json字符串存储)", dataType = "String", position = 35)
    private String extAttr;

    public Resource toResource() {
        return Resource.builder()
                .resourceName(resourceName)
                .parentResourceName(parentResourceName)
                .displayName(displayName)
                .resourceType(resourceType)
                .uri(uri)
                .icon(icon)
                .position(position)
                .extAttr(extAttr)
                .build();
    }

    @Override
    public String validate() {
        if(ResourceType.get(resourceType) == null) {

        }
        return null;
    }
}
