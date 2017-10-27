package com.rd.ifaes.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * session 验证
 * @author lh
 * @version 3.0
 * @since 2016-05-17
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Session {
	/**
	 * 无session时，默认返回地址	
	 * @return
	 */
	String value() default "redirect:/admin/login.html";
}
