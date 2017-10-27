/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.common.constant;

import com.rd.ifaes.common.dict.BaseEnum;

/**
 * 放款方式 枚举类
 * @version 3.0
 * @author QianPengZhan
 * @date 2017年3月7日
 */
public enum ReleaseEnum implements BaseEnum<String, String>{
	/**
	 *  放款方式
	 */
	RELEASE_TYPE_NORMAL("普通放款","0");

	// 字典项的中文名称
		private String name;
		// 字典项的值
		private String value;

		private ReleaseEnum(String name, String value) {
			this.name = name;
			this.value = value;
		}

		/**
		 * 获取label
		 * 
		 * @return
		 */
		@Override
		public String getName() {
			return this.name;
		}

		/**
		 * 获取值
		 * 
		 * @return
		 */
		@Override
		public String getValue() {
			return this.value;
		}

		 /**
		  * 根据VALUE比较
		  * @param obj 目标值
		  * @return
		  */
		public boolean eq(Object obj) {
			return this.value.equals(obj);
		}

	
}
