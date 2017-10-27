package com.rd.ifaes.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作日志注解
 * @version 3.0
 * @author fxl
 * @date 2016年10月22日
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SystemLog {

	/**
	 * 操作方式
	 * @author fxl
	 * @date 2016年10月22日
	 * @return
	 */
	public String method() default ""; 
	
	/**
	 * 操作内容
	 * @author fxl
	 * @date 2016年10月22日
	 * @return
	 */
	public String content() default ""; 

}
