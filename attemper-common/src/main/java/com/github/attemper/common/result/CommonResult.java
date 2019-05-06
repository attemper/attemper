package com.github.attemper.common.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.property.StatusProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 返回数据封装类
 * @auth ldang
 */
@Data
public class CommonResult<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 状态码
	 */
	protected int code;

	/**
	 * 状态信息
	 */
	protected String msg;

	/**
	 * 响应时刻，字符串格式 yyyy-MM-dd HH:mm:ss SSS
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	protected Date responseTime;

	/**
	 * 处理耗时，单位秒
	 */
	protected String duration;

	/**
	 * 数据实体
	 */
	protected T result;
	
	public CommonResult() {
		this.responseTime = new Date();
	}

    /**
     * 使用code和msg构造Res，此方法私有，通过定义ResStatus来处理
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
     * 将额外信息附加在msg中
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
     * 最简单的调用成功Res
     * @return
     */
	public static CommonResult ok() {
		return put(CommonConstants.OK);
	}

    /**
     * 替换"调用成功"
     * @param msg
     * @return
     */
	public static CommonResult ok(String msg) {
	    return putWith(CommonConstants.OK, msg);
	}

    /**
     * 调用成功且附带返回数据
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
