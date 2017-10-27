package com.rd.ifaes.statics.util;

import java.lang.reflect.Constructor;

import org.junit.Test;

public class PropertiesTest {
	@Test
	public void testAll() throws Exception {
		PropertiesUtil.getMessage("avatar", null);
		
		PropertiesUtil.getMessage("avatar");
		PropertiesUtil.getMessage("test");
		
		System.out.println("通过Constructor.newInstance()调用私有构造函数:");
		Class c = Class.forName("com.rd.ifaes.statics.util.PropertiesUtil");
		Constructor c0=c.getDeclaredConstructor();
		c0.setAccessible(true);
		PropertiesUtil po=(PropertiesUtil)c0.newInstance();
	}
}
