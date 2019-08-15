package com.github.attemper.common.param.app.gist;

import com.github.attemper.common.param.IdParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GistInfoContentParam extends IdParam {

    protected String content;

}
