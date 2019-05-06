package com.github.attemper.common.param.sys.user;

import com.github.attemper.common.param.CommonParam;
import com.github.attemper.common.param.CommonParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * @author ldang
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRemoveParam implements CommonParam {

    protected List<String> userNames;

    public String validate() {
        if (userNames == null || userNames.isEmpty() || StringUtils.isBlank(userNames.get(0))) {
            return "5200";
        }
        return null;
    }

}
