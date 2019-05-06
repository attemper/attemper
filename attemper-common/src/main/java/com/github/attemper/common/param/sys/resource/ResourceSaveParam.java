package com.github.attemper.common.param.sys.resource;

import com.github.attemper.common.enums.ResourceType;
import com.github.attemper.common.param.CommonParam;
import com.github.attemper.common.enums.ResourceType;
import com.github.attemper.common.param.CommonParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceSaveParam implements CommonParam {

    protected String resourceName;

    protected String parentResourceName;

    protected String displayName;

    protected Integer resourceType;

    protected String uri;

    protected String icon;

    protected Integer position;

    protected String extAttr;

    public String validate() {
        if (StringUtils.isBlank(resourceName)) {
            return "5400";
        } else if (resourceName.length() >= 255) {
            return "1503";
        }
        if (StringUtils.isBlank(parentResourceName)) {
            return "5401";
        }
        if (StringUtils.isBlank(displayName)) {
            return "5403";
        }
        if (ResourceType.get(resourceType) == null) {
            return "5421";
        }
        return null;
    }

}
