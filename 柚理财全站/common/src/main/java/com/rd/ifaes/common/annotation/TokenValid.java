/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *令牌校验、用户提交传递到session
 * @version 3.0
 * @author FangJun
 * @date 2016年8月3日
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface TokenValid {
	/**
	 * TOKEN名称
	 * @author  FangJun
	 * @date 2016年8月3日
	 * @return  TOKEN名称
	 */
	String value() default "";
	/**
	 *  是否校验TOKEN
	 * @author  FangJun
	 * @date 2016年8月3日
	 * @return   是否校验，默认校验
	 */
	boolean valid() default true;
	/**
	 *  是否在校验后清除TOKEN
	 * @author  FangJun
	 * @date 2016年8月3日
	 * @return   是否清除，默认不清除
	 */
	boolean clear() default false;
	/**
	 * 是否转存用户输入Token,在service层数据持久前做校验
	 * @author  FangJun
	 * @date 2016年8月3日
	 * @return  是否转存用户输入Token,默认不转存
	 */
	boolean  dispatch() default false;
}
