package com.rd.ifaes.core.operate.model;

import java.io.Serializable;
/**
 * 用户红包批量发放Model 
 * @author lh
 * @since 2017-03-15
 * @version 3.0
 *
 */
public class BatchUserRedModel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5023195025906482426L;
	private String[] userIds;//用户集合
	private String redenvelopeRuleId;//红包规则id
	private int type;//类型
	
	public BatchUserRedModel() {
	}
	
	public BatchUserRedModel(String[] userIds, String redenvelopeRuleId, int type) {
		super();
		this.userIds = userIds;
		this.redenvelopeRuleId = redenvelopeRuleId;
		this.type = type;
	}

	public String[] getUserIds() {
		return userIds;
	}
	public void setUserIds(String[] userIds) {
		this.userIds = userIds;
	}
	public String getRedenvelopeRuleId() {
		return redenvelopeRuleId;
	}
	public void setRedenvelopeRuleId(String redenvelopeRuleId) {
		this.redenvelopeRuleId = redenvelopeRuleId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	
	
	
	

}
