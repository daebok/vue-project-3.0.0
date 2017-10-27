package com.rd.ifaes.core.tpp.exception;

public class JxbankException extends TppException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JxbankException() {
		super();
	}

	public JxbankException(String message) {
		super(message);
	}
	
	public JxbankException(String message, String url) {
		super(message, url);
	}
	
	public JxbankException(String message, String url,String button) {
		super(message, url , button);
	}
	
	public JxbankException(String message, int type) {
		super(message, type);
	}
}
