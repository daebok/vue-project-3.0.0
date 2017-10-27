package com.rd.ifaes.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * DictType
 * @author lh
 * @version 3.0
 * @since 2016-5-23
 * 注意：本注解专为POJO字典项属性配置，在执行导出等操作时将字典属性值（Dict.value）改为显示值(Dict.label) 
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Inherited
public @interface DictType {
	
	/**
	 * 类型
	 * @return
	 */
	String type();

}
