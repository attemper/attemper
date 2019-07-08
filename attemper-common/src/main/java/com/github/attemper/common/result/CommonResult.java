package com.github.attemper.common.result;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.property.StatusProperty;
import com.github.attemper.java.sdk.common.result.BaseResult;

public class CommonResult<T> extends BaseResult<T> {
	
	public CommonResult() {
		super();
	}

    /**
     * @param code
     * @param msg
     * @return
     */
    public static CommonResult put(int code, String msg) {
        CommonResult r = new CommonResult();
        r.code = code;
        r.msg = msg;
        return r;
    }

    /**
     * @param code
     * @return
     */
	public static CommonResult put(int code) {
		return put(code,
				StatusProperty.getValue(code));
	}

    /**
     * @param code
     * @param replaceMsg
     * @return
     */
	public static CommonResult putWith(int code, String replaceMsg){
	    return put(code, replaceMsg);
    }

    /**
     * @param code
     * @param extraMsg
     * @return
     */
	public static CommonResult putAdd(int code, String extraMsg){
	    return put(code,
				StatusProperty.getValue(code)
						+ ":" +
						StatusProperty.getValue(extraMsg));
    }

    /**
     * 200
     * @return
     */
	public static CommonResult ok() {
		return put(CommonConstants.OK);
	}

    /**
     * replace default ok msg
     * @param msg
     * @return
     */
	public static CommonResult ok(String msg) {
	    return putWith(CommonConstants.OK, msg);
	}

    /**
     * @param t
     * @param <T>
     * @return
     */
	public static <T> CommonResult<T> putResult(T t) {
		CommonResult r = ok();
		r.result = t;
		return r;
	}

    /**
     * code=500
     * @param msg
     * @return
     */
	public static CommonResult error(String msg){
        return CommonResult.putAdd(CommonConstants.INTERNAL_SERVER_ERROR, msg);
    }

    /**
     * RTException -> CommonResult
     * @param rte
     * @return
     */
	public static CommonResult error(RTException rte){
	    return CommonResult.put(rte.getCode(), rte.getMsg());
    }
}
