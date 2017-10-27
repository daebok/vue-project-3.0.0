package com.rd.account.exception;
/**
 * 账户中心异常类
 * @author lh
 * @version 3.0
 * @since 2016-8-5
 */
public class AccountException extends RuntimeException {
	private static final long serialVersionUID = 538922474277376456L;
	/**
	 * JOSN形式返回到页面
	 */
	public static final int TYPE_JSON = 1;
	/**
	 * 关闭窗
	 */
	public static final int TYPE_CLOSE = 2;
	/**
	 * 跳转url
	 */
	protected String url;
	/**
	 * 返回类型
	 */
	protected int type;
	/**
	 * 页面按钮名字
	 */
	protected String buttonName;
	
	/**
	 * 任意对象
	 */
	protected Object object;

	public AccountException(String msg, RuntimeException ex) {
		super(msg, ex);
	}

	public AccountException() {
		super();
	}

	public AccountException(String message) {
		super(message);
	}

	public AccountException(String message, String url) {
		super(message);
		this.url = url;
	}

	public AccountException(String message, String url,String buttonName) {
		super(message);
		this.url = url;
		this.buttonName = buttonName;
	}
	
	public AccountException(String message, int type) {
		super(message);
		this.type = type;
	}
	/**
	 * 
	 * @param message 异常信息
	 * @param object 需要处理的对象
	 * @author zlh@erongdu.com
	 * @date 2015年7月25日 上午10:40:57
	 */
	public AccountException(String message, Object object){
		super(message);
		this.object = object;
	}
	/**
	 * 
	 * @param message 提示信息
	 * @param url 错误跳转url 
	 * @param type 错误提示类型 
	 */
	public AccountException(String message, String url, int type) {
		super(message);
		this.url = url;
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public String getButtonName() {
		return buttonName;
	}

	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 获取 the object
	 * @return 
	 */
	public Object getObject() {
		return object;
	}

	/**
	 * 设置 the object
	 * @param 
	 */
	public void setObject(Object object) {
		this.object = object;
	}

	
}
