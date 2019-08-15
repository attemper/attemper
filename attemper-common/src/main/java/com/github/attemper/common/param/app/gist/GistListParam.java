package com.github.attemper.common.param.app.gist;

import com.github.attemper.common.param.PageSortParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GistListParam extends PageSortParam {

    protected String gistName;
}
