package com.github.attemper.common.param.tool.server;

import com.github.attemper.common.param.CommonParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@Getter
@Setter
@ToString
public class ServerInfoGetParam implements CommonParam {

    protected String address;

    /** web/scheduler/executor */
    protected String source;

    @Override
    public String validate() {
        if (StringUtils.isBlank(address)) {
            return "1520";
        }
        return null;
    }
}
