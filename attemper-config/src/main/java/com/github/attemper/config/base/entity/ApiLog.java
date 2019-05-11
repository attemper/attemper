package com.github.attemper.config.base.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 操作日志
 * @auth: ldang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiLog {

    /** 主键编号 */
    private String id;

    /** 租户编号 */
    private String tenantId;

    /** java类名 */
    private String className;

    /** java方法名 */
    private String method;

    /** 分类(Controller) */
    private String tag;

    /** 用户操作(method) */
    private String operation;

    /** 请求的url */
    private String path;

    /** 请求参数 */
    private String param;

    /** 返回值 */
    private String result;

    /** 返回码 */
    private int code;

    /** 返回信息 */
    private String msg;

    /** 创建时间 */
    private Date responseTime;

    /** 执行时长(秒) */
    private String duration;

    /** IP地址 */
    private String ip;
}
