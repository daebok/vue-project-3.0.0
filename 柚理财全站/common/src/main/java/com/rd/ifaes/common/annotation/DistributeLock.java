package com.rd.ifaes.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 分布式事务锁
 * @author lh
 * @version 3.0
 * @since 2016-7-25
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DistributeLock {
	/**
	 * 锁定值
	 * @return
	 */
	String value();
}
