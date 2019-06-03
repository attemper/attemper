package com.github.attemper.common.param.sys.tag;

import com.github.attemper.common.param.PageSortParam;
import lombok.Data;

@Data
public class TagListParam extends PageSortParam {

    protected String tagName;

    protected String displayName;

}
