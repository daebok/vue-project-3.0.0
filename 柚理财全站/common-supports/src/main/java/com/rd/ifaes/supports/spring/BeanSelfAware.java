package com.rd.ifaes.supports.spring;
/**
 * 定义BeanPostProcessor 需要使用的 标识接口
 * @author lh
 * @version 3.0
 * @since 2016-12-21
 */
public interface BeanSelfAware {
	
	public void setSelf(Object proxyBean);

}
