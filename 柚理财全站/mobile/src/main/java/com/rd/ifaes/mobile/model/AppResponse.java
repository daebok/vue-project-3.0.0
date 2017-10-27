/**
 * App请求响应
 */
package com.rd.ifaes.mobile.model;

import com.rd.ifaes.mobile.common.AppException;
import com.rd.ifaes.mobile.util.HtmlRegexpUtil;

/**
 * @author yoseflin
 *         App请求响应
 */
public class AppResponse {

	/**
	 * 返回代码
	 */
	private int resCode;

	/**
	 * 返回说明
	 */
	private String resMsg;

	/**
	 * 返回数据
	 */
	private Object resData;

	//--------------------------华丽分割线----------------------------------
	public AppResponse( int code , String message , Object data ) {
		this.resCode = code;
		this.resMsg = HtmlRegexpUtil.filterHtml(message);
		this.resData = data;
	}

	public AppResponse(int code, String message) {
		this.resCode = code;
		this.resMsg = HtmlRegexpUtil.filterHtml(message);
		this.resData = "";
	}

	public AppResponse(AppException exception) {
		this.resCode = exception.getRes_code();
		this.resMsg = HtmlRegexpUtil.filterHtml(exception.getRes_msg());
		this.resData = "";
	}

	public int getResCode() {
		return resCode;
	}

	public void setResCode(int code) {
		this.resCode = code;
	}

	public String getResMsg() {
		return resMsg;
	}

	public void setResMsg(String message) {
		this.resMsg = HtmlRegexpUtil.filterHtml(message);
	}

	public Object getResData() {
		return resData;
	}

	public void setResData(Object data) {
		this.resData = data;
	}

}
