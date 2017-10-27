package com.rd.ifaes.core.project.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.DateUtils;

/**
 * 变现冻结信息
 * @version 3.0
 * @author fxl
 * @date 2016年8月1日
 */
public class RealizeFreeze extends BaseEntity<RealizeFreeze> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户ID
	 */
	private String	userId;	
	/**
	 * 变现产品ID
	 */
	private String	realizeId;
	
	/**
	 * 被变现待收ID
	 */
	private String	collectionId;
	/**
	 * 冻结金额
	 */
	private BigDecimal	money;
	/**
	 * 冻结类型
	 */
	private String freezeType;
	/**
	 * 状态 0未冻结  1已冻结 2已解冻
	 */
	private String	status;
	/**
	 * 冻结订单号
	 */
	private String orderNo;
	/**
	 * 冻结流水号
	 */
	private String	freezeNo;
	/**
	 * 添加时间
	 */
	private Date	createTime;
	

	// Constructor
	public RealizeFreeze() {
	}

	
	public RealizeFreeze(String userId,String realizeId,String collectionId,String freezeType,BigDecimal money) {
		this.userId = userId;
		this.realizeId = realizeId;
		this.collectionId = collectionId;
		this.money = money;
		this.freezeType = freezeType;
		this.createTime = DateUtils.getNow();
		this.status = "0";
	}

	/**
	 * 获取属性userId的值
	 * @return userId属性值
	 */
	public String getUserId() {
		return userId;
	}


	/**
	 * 设置属性userId的值
	 * @param  userId属性值
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}


	/**
	 * 获取属性realizeId的值
	 * @return realizeId属性值
	 */
	public String getRealizeId() {
		return realizeId;
	}


	/**
	 * 设置属性realizeId的值
	 * @param  realizeId属性值
	 */
	public void setRealizeId(String realizeId) {
		this.realizeId = realizeId;
	}


	/**
	 * 获取属性collectionId的值
	 * @return collectionId属性值
	 */
	public String getCollectionId() {
		return collectionId;
	}


	/**
	 * 设置属性collectionId的值
	 * @param  collectionId属性值
	 */
	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}

	/**
	 * 获取属性money的值
	 * @return money属性值
	 */
	public BigDecimal getMoney() {
		return money;
	}


	/**
	 * 设置属性money的值
	 * @param  money属性值
	 */
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	

	/**
	 * 获取属性freezeType的值
	 * @return freezeType属性值
	 */
	public String getFreezeType() {
		return freezeType;
	}


	/**
	 * 设置属性freezeType的值
	 * @param  freezeType属性值
	 */
	public void setFreezeType(String freezeType) {
		this.freezeType = freezeType;
	}


	/**
	 * 获取属性status的值
	 * @return status属性值
	 */
	public String getStatus() {
		return status;
	}


	/**
	 * 设置属性status的值
	 * @param  status属性值
	 */
	public void setStatus(String status) {
		this.status = status;
	}


	/**
	 * 获取属性freezeNo的值
	 * @return freezeNo属性值
	 */
	public String getFreezeNo() {
		return freezeNo;
	}


	/**
	 * 设置属性freezeNo的值
	 * @param  freezeNo属性值
	 */
	public void setFreezeNo(String freezeNo) {
		this.freezeNo = freezeNo;
	}


	/**
	 * 获取属性orderNo的值
	 * @return orderNo属性值
	 */
	public String getOrderNo() {
		return orderNo;
	}


	/**
	 * 设置属性orderNo的值
	 * @param  orderNo属性值
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}


	/**
	 * 获取属性createTime的值
	 * @return createTime属性值
	 */
	public Date getCreateTime() {
		return createTime;
	}


	/**
	 * 设置属性createTime的值
	 * @param  createTime属性值
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
