package com.github.attemper.interior.param;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.CommonParam;
import lombok.Data;

@Data
public class CleanLogParam implements CommonParam {

    protected String tenantId;

    /** null:200, false: except 200, true:all codes */
    protected Boolean cleanAll;

    protected Integer offsetDays;

    @Override
    public String validate() {
        if (offsetDays < 0) {
            throw new RTException(CommonConstants.INTERNAL_SERVER_ERROR, String.valueOf(offsetDays));
        }
        return null;
    }

    @Override
    public void preHandle() {
        if (offsetDays == null) {
            offsetDays = 30;
        }
    }
}
