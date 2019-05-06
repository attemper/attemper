package com.github.attemper.config.base.entity;

import com.github.attemper.config.base.constant.ConfigDBConstants;
import com.github.attemper.config.base.constant.PrimaryKeyGeneratorConstants;
import com.github.attemper.common.constant.GlobalConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * 操作日志
 * @auth: ldang
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = GlobalConstants.LOG_DB_PREFIX + ConfigDBConstants.TB_API_LOG)
public class ApiLog implements PrimaryKeyGeneratorConstants {

    /** 主键编号 */
    @Id
    @GeneratedValue(generator = ConfigDBConstants.TB_API_LOG, strategy = GenerationType.TABLE)
    @TableGenerator(
            name = ConfigDBConstants.TB_API_LOG,
            table = GENERATOR_TABLE,
            pkColumnName = PK_COLUMN_NAME,
            pkColumnValue = ConfigDBConstants.TB_API_LOG,
            valueColumnName = PK_COLUMN_VALUE,
            allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;

    /** 租户编号 */
    @Column(name = "TENANT_ID")
    private String tenantId;

    /** 用户名 */
    @Column(name = "USER_NAME")
    private String userName;

    /** java类名 */
    @Column(name = "CLASS_NAME")
    private String className;

    /** java方法名 */
    @Column(name = "METHOD")
    private String method;

    /** 分类(Controller) */
    @Column(name = "TAG")
    private String tag;

    /** 用户操作(method) */
    @Column(name = "OPERATION")
    private String operation;

    /** 请求的url */
    @Column(name = "PATH")
    private String path;

    /** 请求参数 */
    @Column(name = "PARAM")
    private String param;

    /** 返回值 */
    @Column(name = "RESULT")
    private String result;

    /** 返回码 */
    @Column(name = "CODE")
    private int code;

    /** 返回信息 */
    @Column(name = "MSG")
    private String msg;

    /** 创建时间 */
    @Column(name = "RESPONSE_TIME")
    private Date responseTime;

    /** 执行时长(秒) */
    @Column(name = "DURATION")
    private String duration;

    /** IP地址 */
    @Column(name = "IP")
    private String ip;
}
