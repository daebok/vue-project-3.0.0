/**  
 * All rights Reserved, Designed By QianPengZhan   
 * @Title:  RiskEnum.java   
 * @Package com.rd.ifaes.common.dict   
 * @date:   2016年7月12日 下午4:45:20   
 * @version V3.0     
 */
package com.rd.ifaes.common.dict;

/**   
 * @ClassName:  RiskEnum   
 * @Description:风险枚举类
 * @author: QianPengZhan 
 * @date:   2016年7月12日 下午4:45:20   
 *      
 */
public enum RiskEnum implements BaseEnum<String, String>{
	PAPERS_PUBLISH("已经发布","1"),
	
	PAPERS_NO_PUBLISH("未发布","2"),
	
	QUESTION_IS_SINGLE("单选","1"),
	
	QUESTION_IS_NOT_SINGLE("多选","2"),
	
	PAPERS_TYPE_ONE("其他","1"),
	
	PAPERS_TYPE_TWO("答题","2"),
	
	USER_LOG_STATUS_INIT("初始状态","1"),
	
	USER_LOG_STATUS_DO("有效状态","2"),
	
	CONFIG_IS_DELETE_FLAG("逻辑删除","1"),
	
	CONFIG_IS_NOT_DELETE_FLAG("逻辑未删除","0"),
	
	/**
	 * 按自定义序号排序
	 */
	CUSTOM_PAGE_SORT_SORT("按自定义序号排序","sort");

	// 字典项的中文名称
	private String name;
	// 字典项的值
	private String value;

	private RiskEnum(String name, String value) {
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
