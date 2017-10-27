package com.rd.ifaes.common.dict;
/**
 * 数据删除标记
 * @author  FangJun
 * @date 2016年7月1日
 */
public enum DeleteFlagEnum implements BaseEnum<String, String> {
		 /**
	     * 已删除,1
	     */
	     YES("已删除","1"),
	     /**
	      * 未删除,0
	      */
	     NO("未删除","0");
	
	  //字典项的中文名称
		private String name;
		//字典项的值
		private String value;

		private DeleteFlagEnum(String name,String value){
			this.name = name;
			this.value = value;
		}
		/**
		 * 获取label
		 * @return
		 */
		@Override
		public String getName(){
			return this.name;
		}

		/**
		 * 获取值
		 * @return
		 */
		@Override
		public String getValue(){
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
