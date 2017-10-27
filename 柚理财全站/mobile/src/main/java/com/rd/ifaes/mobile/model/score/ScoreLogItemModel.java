package com.rd.ifaes.mobile.model.score;

import java.util.Date;

import com.rd.ifaes.core.core.constant.Constant;

public class ScoreLogItemModel {

	/** 积分类型名称 */
	private String typeName;
	
	/** 兑换日期 */
	private Date convertTime;
	
	/** 操作类型（0减少，1增加）*/
	private String	optType;	
	
	/** 操作积分*/
	private Integer optValue;

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	public Date getConvertTime() {
		return convertTime;
	}

	public void setConvertTime(Date convertTime) {
		this.convertTime = convertTime;
	}

	public String getOptType() {
		return optType;
	}

	public void setOptType(String optType) {
		this.optType = optType;
	}

	public Integer getOptValue() {
		return optValue;
	}
	
	public String getOptValueStr() {
		if(Constant.STRING_ZERO.equals(getOptType())){
			return "-" + optValue;
		}
		return "+" + optValue;
	}

	public void setOptValue(Integer optValue) {
		this.optValue = optValue;
	}
	
	
}
