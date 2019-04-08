package com.sse.attemper.common.result.sys.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ldang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResult {

    /**
     * 刷新后的token
     */
    protected String token;

}
