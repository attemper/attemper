package com.github.attemper.common.param.dispatch.delay;

import lombok.ToString;

@ToString
public class DelayJobIdParam {

    protected String id;

    public String getId() {
        return id;
    }

    public DelayJobIdParam setId(String id) {
        this.id = id;
        return this;
    }
}
