package com.github.attemper.config.base.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * operate log
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiLog {

    private String id;

    private String tenantId;

    private String className;

    private String method;

    /** Api */
    private String tag;

    /** ApiOperation */
    private String operation;

    /** Request url */
    private String path;

    private String param;

    private String result;

    protected int code;

    protected String msg;

    protected Long handleTime;

    protected String duration;

    private String ip;
}
