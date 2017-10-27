package com.rd.ifaes.common.constant;

/**
 * 第三方支付服务类型
 * @author lh
 * @version 3.0
 * @since 2016-12-5
 *
 */
public enum TppServiceEnum {
	
	/**
	 *  浙商银行 czbank
		徽商银行 hsbank
		联动优势   ump
		乾多多	   mmm
		汇付天下 chinapnr
	 */
	CHINAPNR("chinapnr","汇付天下"),
	CZBANK("czbank","浙商银行"),
	HSBANK("hsbank","徽商银行"),
	CIB("cib","兴业银行"),
	UMP("ump","联动优势"),
	MMM("mmm","乾多多"),	
	JXBANK("jxbank","江西银行"),	
	
	CBHB("cbhb","渤海银行");
	
	private String name;//服务名称
	private String desc;//服务描述信息
	
	private TppServiceEnum(String name, String desc) {
		this.name = name;
		this.desc = desc;
	}
	
	/**
	 * 根据name 取得枚举
	 * @param name
	 * @return
	 */
	public static TppServiceEnum nameOf(String name){
		for (TppServiceEnum ts : TppServiceEnum.values()) {
			if(ts.name.equals(name)){
				return ts;
			}
		}
		return CHINAPNR;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	

}
