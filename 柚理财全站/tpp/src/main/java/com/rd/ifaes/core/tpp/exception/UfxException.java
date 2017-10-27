package com.rd.ifaes.core.tpp.exception;

import com.rd.ifaes.core.tpp.exception.TppException;


public class UfxException extends TppException {

	private static final long serialVersionUID = 1L;

	public UfxException() {
		super();
	}

	public UfxException(String message) {
		super(message);
	}
	
	public UfxException(String message, String url) {
		super(message, url);
	}
	
	public UfxException(String message, String url,String button) {
		super(message, url , button);
	}
	

	public UfxException(String message, int type) {
		super(message, type);
	}
}
