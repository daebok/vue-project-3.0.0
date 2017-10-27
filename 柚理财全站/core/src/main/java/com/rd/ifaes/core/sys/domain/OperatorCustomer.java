package com.rd.ifaes.core.sys.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Size;

import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.core.core.constant.ResourceConstant;

/**
 * 
 * 
 * @author wyw
 * @version 3.0
 * @date 2016-8-19
 */
public class OperatorCustomer extends BaseEntity<OperatorCustomer> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7150930730292724870L;
	private String	userId;		     /* 客户id */ 
	private String	operatorId;		 /* 经纪人id */ 
	private Date	createTime;		 /* 添加时间 */ 
	private String	addType;		 /* 添加类型 1 注册添加、2 后台添加 */
	@Size(max=256,message="{"+ResourceConstant.CUSTOMER_REMARK_OVER_LENGTH+"}")
	private String	remark;		     /* 备注 */ 

	//其他自定义属性
	private String	mobile;	             /* 手机号 */
	private String  userName;            /* 用户名 */
	private String  realName;            /* 真实姓名 */
	private String  userNature;          /* 用户类型:  1个人用户、2 企业用户、3 担保机构 */
	private Date	registerTime;		 /* 用户注册时间 */ 
	private String	tenderMoney;	     /* 投资金额 */
	
	private String	operatorRealName;	             /* 经纪人真实名称 */
	private BigDecimal sumAmount;   /* 投资总额 */
	private String status;  /* 投资状态*/
	
	public static final String ADD_TYPE_MANAGER = "2";     //后台添加
	public static final String ADD_TYPE_REGISTER = "1";     //注册添加
	// Constructor
	public OperatorCustomer() {
	}

	/**
	 * full Constructor
	 */
	public OperatorCustomer(String uuid, String userId, String operatorId, Date createTime, String addType, String remark) {
		setUuid(uuid);
		this.userId = userId;
		this.operatorId = operatorId;
		this.createTime = createTime;
		this.addType = addType;
		this.remark = remark;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	public BigDecimal getSumAmount() {
		return sumAmount;
	}
	public void setSumAmount(BigDecimal sumAmount) {
		this.sumAmount = sumAmount;
	} 
	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getAddType() {
		return addType;
	}

	public void setAddType(String addType) {
		this.addType = addType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getUserNature() {
		return userNature;
	}

	public void setUserNature(String userNature) {
		this.userNature = userNature;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public String getTenderMoney() {
		return tenderMoney;
	}

	public void setTenderMoney(String tenderMoney) {
		this.tenderMoney = tenderMoney;
	}

	public String getOperatorRealName() {
		return operatorRealName;
	}

	public void setOperatorRealName(String operatorRealName) {
		this.operatorRealName = operatorRealName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "OperatorCustomer [" + "uuid=" + uuid + ", userId=" + userId + ", operatorId=" + operatorId + ", createTime=" + createTime + ", addType=" + addType + ", remark=" + remark +  "]";
	}
}
