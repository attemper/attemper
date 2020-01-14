package com.github.attemper.common.param.dispatch.job;

import lombok.ToString;

@ToString
public class JobContentSaveParam extends JobNameParam {

    protected String content;

    public String getContent() {
        return content;
    }

    public JobContentSaveParam setContent(String content) {
        this.content = content;
        return this;
    }

}
