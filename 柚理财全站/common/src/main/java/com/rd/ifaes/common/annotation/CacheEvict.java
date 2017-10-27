package com.rd.ifaes.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.rd.ifaes.common.dict.ExpireTime;
/**
 * 缓存清除
 * @author lh
 * @version 3.0
 * @since 2016-8-28
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CacheEvict {
	
	/**
	 * 缓存key数组
	 * @return
	 */
	String[] keys() default "";
	/**
	 * 操作之间的缓存时间（秒）
	 * @author  FangJun
	 * @date 2016年9月9日
	 * @return 默认0，不做限制
	 */
	ExpireTime interval() default ExpireTime.NONE;
	
}
