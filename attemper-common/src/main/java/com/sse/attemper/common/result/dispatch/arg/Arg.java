package com.sse.attemper.common.result.dispatch.arg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author pczhao
 * @email
 * @date 2019-01-14 16:12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Arg {

    private String argName;

    private Integer argType;

    private String defVal;

    private Date createTime;

    private Date updateTime;

    private String remark;

    private String tenantId;

}
