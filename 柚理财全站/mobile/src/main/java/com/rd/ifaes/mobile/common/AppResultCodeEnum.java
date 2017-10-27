package com.rd.ifaes.mobile.common;

public enum AppResultCodeEnum {

	FAIL_CODE("0000"), SUCCESS_CODE("9999");

	private final String value;

	public String getValue() {
		return value;
	}

	private AppResultCodeEnum(String value) {
		this.value = value;
	}

}
