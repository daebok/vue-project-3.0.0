package com.rd.ifaes.statics.util;

import org.junit.Test;

public class MD5Test {
	@Test
	public void testAll() throws Exception {
		MD5 md5 = new MD5();
		
		md5.getMD5ofStr("");
		md5.getMD5ofStr(null);
		md5.getMD5ofStr("lt");
	}
}
