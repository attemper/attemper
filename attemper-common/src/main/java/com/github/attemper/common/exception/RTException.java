package com.github.attemper.common.exception;

import com.github.attemper.common.property.StatusProperty;
import com.github.attemper.java.sdk.common.util.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * Customize RuntimeException
 *
 */
@Slf4j
public class RTException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private int code;
    private String msg;
    
    public RTException() {
    	super();
    }

	public RTException(int code){
		super(StatusProperty.getValue(code));
		this.code = code;
		this.msg = StatusProperty.getValue(code);
		log.error("{} \n {}", this.code, this.msg);
	}

	public RTException(String message) {
		this(500, message);
	}

	public RTException(Throwable e) {
		this(500, e);
	}

    public RTException(int code, Throwable e) {
		super(StatusProperty.getValue(code) + ":" + e.getMessage(), e);
        this.code = code;
		this.msg = super.getMessage();
		log.error("{} \n {}", this.code, StatusProperty.getValue(code) + ":" + ExceptionUtil.getStackTrace(e));
    }

	public RTException(int code, Object msg) {
		super(StatusProperty.getValue(code) + ":" + String.valueOf(msg));
        this.code = code;
		this.msg = StatusProperty.getValue(code) + ":" + String.valueOf(msg);
		log.error("{} \n {}", this.code, this.msg);
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
