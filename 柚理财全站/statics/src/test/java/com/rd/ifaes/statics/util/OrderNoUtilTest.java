package com.rd.ifaes.statics.util;

import org.junit.Test;

public class OrderNoUtilTest {
	@Test
	public void testAll() throws Exception {
		new OrderNoUtil();
		
		for (int i = 0; i < 20; i++) {
			System.out.println(OrderNoUtil.getSerialNumber());
		}
	}
}
