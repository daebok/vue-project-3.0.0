package com.rd.ifaes.common.dict;

/**
 * 工作流枚举类
 * @version 3.0
 * @author lh
 * @date 2016年8月3日
 */
public enum WorkflowEnum implements BaseEnum<String, String> {

	/**
	 * 借贷流程KEY
	 */
	PROCESS_KEY_BORROW("借贷流程主键","processBorrow"),
	
	/**
	 * 参数名称：审核意见
	 */
	PARAMS_NAME_AUDIT_OPINION("审核意见","auditOpinion")
	;

	// 字典项的中文名称
	private String name;
	// 字典项的值
	private String value;

	private WorkflowEnum(String name, String value) {
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
	 * 
	 * @param obj
	 *            目标值
	 * @return
	 */
	public boolean eq(Object obj) {
		return this.value.equals(obj);
	}

}
