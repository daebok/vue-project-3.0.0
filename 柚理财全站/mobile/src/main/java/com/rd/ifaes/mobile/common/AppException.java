package com.rd.ifaes.mobile.common;

import java.util.HashMap;
import java.util.Map;

import com.rd.ifaes.common.exception.BussinessException;

/**
 * app异常类，app所有RuntimeException封装成AppException
 * 
 * @author Kunkka
 *
 */
public class AppException extends BussinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int res_code;
	private String res_msg;
	private Map<String,Object> res_data = null;

	public int getRes_code() {
		return res_code;
	}

	public void setRes_code(int res_code) {
		this.res_code = res_code;
	}

	public String getRes_msg() {
		return res_msg;
	}

	public void setRes_msg(String res_msg) {
		this.res_msg = res_msg;
	}

	public Map<String, Object> getRes_data() {
		return res_data;
	}

	public void setRes_data(Map<String, Object> res_data) {
		this.res_data = res_data;
	}

	public AppException(int res_code, String res_msg) {
		super();
		this.res_code = res_code;
		this.res_msg = res_msg;
	}

	public AppException(int res_code, String res_msg, String newSessionId) {
		super();
		this.res_code = res_code;
		this.res_msg = res_msg;
		this.res_data = new HashMap<String,Object>();
		this.res_data.put("session_id", newSessionId);
	}
}
