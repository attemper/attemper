package com.github.attemper.common.exception;

import com.github.attemper.common.property.StatusProperty;
import lombok.extern.slf4j.Slf4j;

/**
 * Customize RuntimeException
 * @author ldang
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
	}

	public RTException(String message) {
		super(message);
	}

    public RTException(int code, Throwable e) {
        super(StatusProperty.getValue(code) + ":" + e.getMessage(), e);
        this.code = code;
        this.msg = StatusProperty.getValue(code) + ":" + e.getMessage();
    }

    public RTException(int code, String msg) {
        super(StatusProperty.getValue(code) + ":" + msg);
        this.code = code;
        this.msg = StatusProperty.getValue(code) + ":" + msg;
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
