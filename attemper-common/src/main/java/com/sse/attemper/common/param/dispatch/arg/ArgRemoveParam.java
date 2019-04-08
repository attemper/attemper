package com.sse.attemper.common.param.dispatch.arg;

import com.sse.attemper.common.param.CommonParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArgRemoveParam implements CommonParam {

    protected List<String> argNames;

    public String validate() {
        if (argNames == null || argNames.isEmpty()) {
            return "7000";
        }
        return null;
    }
}
