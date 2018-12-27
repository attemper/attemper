package com.thor.common.exception;

import com.thor.common.constant.GlobalConstants;
import com.thor.common.enums.ResultStatus;
import com.thor.common.property.CommonProperty;

/**
 * 运行时异常
 * @auth ldang
 *
 */
public class RTException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private int code;
    private String msg;
    
    public RTException() {
    	super();
    }
    
    /**
	 * @param code
	 * @param msg
	 */
	public RTException(int code, String msg){
		super(msg);
		this.code = code;
		this.msg = msg;
	}
	
    /**
     * code=500
     * @param msg
     */
	public RTException(String msg) {
		this(ResultStatus.INTERNAL_SERVER_ERROR.getCode(), msg);
	}
	
	/**
	 * <code>code = resultStatus.getCode();</code> <br>
	 * <code>msg = resultStatus.getMsg();</code>
	 * @param resultStatus
	 */
	public RTException(ResultStatus resultStatus){
		this(resultStatus.getCode(), CommonProperty.getValue(GlobalConstants.PREFIX_STATUS_MSG + resultStatus.getCode()));
	}

    /**
     * <code>code = resultStatus.getCode();</code> <br>
     * <code>msg = resultStatus.getMsg() + e.getMessage();</code>
     * @param resultStatus
     * @param e
     */
    public RTException(ResultStatus resultStatus, Throwable e) {
        super(e.getMessage(), e);
        this.code = resultStatus.getCode();
        this.msg = CommonProperty.getValue(GlobalConstants.PREFIX_STATUS_MSG + resultStatus.getCode()) + ":" + e.getMessage();
    }

    /**
     * <code>code = resultStatus.getCode();</code> <br>
     * <code>msg = resultStatus.getMsg() + msg;</code>
     * @param resultStatus
     * @param msg
     */
    public RTException(ResultStatus resultStatus, String msg) {
        super(msg);
        this.code = resultStatus.getCode();
        this.msg = CommonProperty.getValue(GlobalConstants.PREFIX_STATUS_MSG + resultStatus.getCode()) + ":" + msg;
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

	@Override
	public String toString() {
		return "RTException{" +
				"code=" + code +
				", msg='" + msg + '\'' +
				'}';
	}
}
