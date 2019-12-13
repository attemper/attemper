package com.github.attemper.common.result.dispatch.condition.sub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FtpFileConditionResult extends LocalFileConditionResult {

    protected String prefix;

}
