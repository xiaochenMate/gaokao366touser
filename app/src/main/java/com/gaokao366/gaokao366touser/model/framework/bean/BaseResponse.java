package com.gaokao366.gaokao366touser.model.framework.bean;

import java.io.Serializable;

public abstract class BaseResponse implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5375804597574885028L;
	public int errCode = -1;
	public String msg;
}
