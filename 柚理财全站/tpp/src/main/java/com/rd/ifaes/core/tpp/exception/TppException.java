package com.rd.ifaes.core.tpp.exception;

import com.rd.ifaes.common.exception.BussinessException;
/**
 * TPP异常
 * @author lh
 * @version 3.0
 * @since 2016-12-5
 *
 */
public class TppException extends BussinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5919643665596845168L;
	
	public TppException() {
		super();
	}

	public TppException(String message) {
		super(message);
	}
	
	public TppException(String message, String url) {
		super(message, url);
	}
	
	public TppException(String message, String url,String button) {
		super(message, url , button);
	}
	

	public TppException(String message, int type) {
		super(message, type);
	}
}
