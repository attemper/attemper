package com.thor.common.result;

import com.thor.common.constant.GlobalConstants;
import com.thor.common.enums.ResultStatus;
import com.thor.common.exception.RTException;
import com.thor.common.property.CommonProperty;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

/**
 * 返回数据封装类
 * @auth ldang
 */
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
		this.responseTime = Date.from(Instant.now());
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
     * 使用ResStatus构造Res
     * @param resultStatus
     * @return
     */
	public static CommonResult put(ResultStatus resultStatus) {
		return put(resultStatus.getCode(),
				CommonProperty.getValue(GlobalConstants.PREFIX_STATUS_MSG + resultStatus.getCode()));
	}

    /**
     * 替换掉ResStatus中的msg
     * @param resultStatus
     * @param replaceMsg
     * @return
     */
	public static CommonResult putWith(ResultStatus resultStatus, String replaceMsg){
	    return put(resultStatus.getCode(), replaceMsg);
    }

    /**
     * 将额外信息附加在msg中
     * @param resultStatus
     * @param extraMsg
     * @return
     */
	public static CommonResult putAdd(ResultStatus resultStatus, String extraMsg){
	    return put(resultStatus.getCode(),
				CommonProperty.getValue(GlobalConstants.PREFIX_STATUS_MSG + resultStatus.getCode())
						+ ":" +
						CommonProperty.getValue(extraMsg));
    }

    /**
     * 最简单的调用成功Res
     * @return
     */
	public static CommonResult ok() {
		return put(ResultStatus.OK);
	}

    /**
     * 替换"调用成功"
     * @param msg
     * @return
     */
	public static CommonResult ok(String msg) {
	    return putWith(ResultStatus.OK, msg);
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
        return CommonResult.putAdd(ResultStatus.INTERNAL_SERVER_ERROR, msg);
    }

    /**
     * RTException -> CommonResult
     * @param rte
     * @return
     */
	public static CommonResult error(RTException rte){
	    return CommonResult.put(rte.getCode(), rte.getMsg());
    }

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

/*	public String getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}*/

	public Date getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "CommonResult{" +
				"code=" + code +
				", msg='" + msg + '\'' +
				", responseTime='" + responseTime + '\'' +
				", duration='" + duration + '\'' +
				", result=" + result +
				'}';
	}
}
