package com.rd.ifaes.core.score.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * entity:ScoreGoodsOrder 商品订单
 * 
 * @author ywt
 * @version 3.0
 * @date 2017-2-17
 */
public class ScoreGoodsOrder extends BaseEntity<ScoreGoodsOrder>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1061306172152766345L;
	
	/**用户id*/
	private String userId;
	/**用户名*/
	private String userName;
	/**商品id*/
	private String goodsId;
	/**商品名称*/
	private String goodsName;
	/**兑换数量*/
	private int num;
	/**使用积分值*/
	private int score;
	/**兑换时间*/
	private Date createTime;
	/**发货时间*/
	private Date deliverTime;
	/**发送人*/
	private String deliverUser;
	/**物流名称*/
	private String expressName;
	/**物流单号*/
	private String expressNo;
	/**费用*/
	private BigDecimal money;
	/**收货地址*/
	private String receiveAddress;
	/**收货人电话*/
	private String receivePhone;
	/**收货人姓名*/
	private String receiveUserName;
	/**兑换留言*/
	private String receiveRemark;
	/**状态 0 新建 1审批通过  2 审核失败 3 已发货 4 已收货 5结束*/
	private String status;
	/**审批时间*/
	private Date verifyTime;
	/**审批人ID*/
	private String verifyUserId;
	/**审批人用户名*/
	private String verifyUser;
	/**审批备注*/
	private String verifyRemark;
	/**用户头像*/
	private String avatarPhoto;
	public String getAvatarPhoto() {
		return avatarPhoto;
	}
	public void setAvatarPhoto(String avatarPhoto) {
		this.avatarPhoto = avatarPhoto;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getDeliverTime() {
		return deliverTime;
	}
	public void setDeliverTime(Date deliverTime) {
		this.deliverTime = deliverTime;
	}
	public String getDeliverUser() {
		return deliverUser;
	}
	public void setDeliverUser(String deliverUser) {
		this.deliverUser = deliverUser;
	}
	public String getExpressName() {
		return expressName;
	}
	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}
	public String getExpressNo() {
		return expressNo;
	}
	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public String getReceiveAddress() {
		return receiveAddress;
	}
	public void setReceiveAddress(String receiveAddress) {
		this.receiveAddress = receiveAddress;
	}
	public String getReceivePhone() {
		return receivePhone;
	}
	public void setReceivePhone(String receivePhone) {
		this.receivePhone = receivePhone;
	}
	public String getReceiveUserName() {
		return receiveUserName;
	}
	public void setReceiveUserName(String receiveUserName) {
		this.receiveUserName = receiveUserName;
	}
	public String getReceiveRemark() {
		return receiveRemark;
	}
	public void setReceiveRemark(String receiveRemark) {
		this.receiveRemark = receiveRemark;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getVerifyTime() {
		return verifyTime;
	}
	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
	}
	public String getVerifyUserId() {
		return verifyUserId;
	}
	public void setVerifyUserId(String verifyUserId) {
		this.verifyUserId = verifyUserId;
	}
	public String getVerifyUser() {
		return verifyUser;
	}
	public void setVerifyUser(String verifyUser) {
		this.verifyUser = verifyUser;
	}
	public String getVerifyRemark() {
		return verifyRemark;
	}
	public void setVerifyRemark(String verifyRemark) {
		this.verifyRemark = verifyRemark;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	
	
	
	
}
