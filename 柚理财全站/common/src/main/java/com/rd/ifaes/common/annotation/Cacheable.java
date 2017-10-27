package com.rd.ifaes.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.rd.ifaes.common.dict.ExpireTime;
/**
 * 添加缓存
 * @author lh
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Cacheable {

	/**
	 * 缓存key
	 * @return
	 */
	public String key() default ""; 
	
	/**
	 * 缓存时效,默认无限期
	 * @return
	 */
	public ExpireTime expire() default ExpireTime.NONE; 

}
