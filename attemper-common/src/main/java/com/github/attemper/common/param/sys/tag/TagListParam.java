package com.github.attemper.common.param.sys.tag;

import com.github.attemper.common.param.PageSortParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TagListParam extends PageSortParam {

    protected String tagName;

    protected String displayName;

}
