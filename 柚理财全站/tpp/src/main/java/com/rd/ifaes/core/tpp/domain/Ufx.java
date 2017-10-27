package com.rd.ifaes.core.tpp.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * 第三方处理记录
 * @author xhf
 * @version 3.0
 * @date 2016年6月5日 下午8:51:18
 */
public class Ufx extends BaseEntity<Ufx>{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	private String uuid;
	
	/**1:成功，2:失败，0:未触发**/
	private int status;

	/** 接口类型 */
	private String serviceType;
	
	/**
	 * 请求接口类型
	 */
	private String tppType;

	/** 用户 */
	private String userId;
	
	/**用戶号*/
	private String userNo;
	
	/**真实姓名*/
	private String realName;

	/** 用户资金托管账户 */
	private String tppUserCustId;

	/** 收款用户id */
	private String toUserId;

	/** 收款用户资金托管账户Id */
	private String toTppUserCustId;

	/** 交易金额 */
	private BigDecimal money;

	/** 服务费 **/
	private BigDecimal servFee;

	/**
	 * 标的号
	 */
	private String bidNo;

	/** ifaes系统交易流水号 */
	private String orderId;

	/** ifaes交易时间 */
	private String orderDate;

	/** 第三方订单号 */
	private String subOrdId;

	/** 第三方交易时间*/
	private String subOrdDate;
	
	/** 第三方冻结流水号 */
	private String trxId;

	/** 银行卡号 */
	private String bankCardId;

	/** 证件号 */
	private String cardId;

	/** 证件类型 */
	private String idType;

	/** 手机号 */
	private String phone;

	/** 邮箱 */
	private String email;

	/** 前台回调url */
	private String retUrl;

	/** 后台回调url */
	private String bgRetUrl;
	/**
	 * 投标对象
	 */
	private String borrowId;
	
	/**
	 * 投标对象
	 */
	private String tenderId;
	/**
	 * 扩展参数
	 */
	private String extended;
	
	/**
	 * 取现复核标识 
	 */
	private String flag;
	
	/**
	 * 回调结果
	 */
	private String respDesc;
	
	/**触发时间**/
	private Date addTime;
	
	/**
	 * 投资项目id
	 */
	private String tppBorrowId;

	/**
	 * 构造函数
	 */
	public Ufx() {
		super();
	}

	/**
	 * 构造函数
	 */
	public Ufx(final String cmdid, final BigDecimal ordamt, final String userId, final String toUserId,
			final int status, final Date addtime, final String serviceType) {
		super();
		this.tppType = cmdid;
		this.money = ordamt;
		this.userId = userId;
		this.toUserId = toUserId;
		this.status = status;
		this.addTime = addtime;
		this.serviceType = serviceType;
	}

	/**
	 * 获取主键
	 * @return uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * 设置主键
	 * @param  uuid
	 */
	public void setUuid(final String uuid) {
		this.uuid = uuid;
	}

	/**
	 * 获取1:成功，2:失败，0:未触发
	 * @return status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * 设置1:成功，2:失败，0:未触发
	 * @param  status
	 */
	public void setStatus(final int status) {
		this.status = status;
	}

	/**
	 * 获取接口类型
	 * @return serviceType
	 */
	public String getServiceType() {
		return serviceType;
	}

	/**
	 * 设置接口类型
	 * @param  serviceType
	 */
	public void setServiceType(final String serviceType) {
		this.serviceType = serviceType;
	}

	/**
	 * 获取请求接口类型
	 * @return tppType
	 */
	public String getTppType() {
		return tppType;
	}

	/**
	 * 设置请求接口类型
	 * @param  tppType
	 */
	public void setTppType(final String tppType) {
		this.tppType = tppType;
	}

	/**
	 * 获取用户
	 * @return userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置用户
	 * @param  userId
	 */
	public void setUserId(final String userId) {
		this.userId = userId;
	}

	/**
	 * 获取用户资金托管账户
	 * @return tppUserCustId
	 */
	public String getTppUserCustId() {
		return tppUserCustId;
	}

	/**
	 * 设置用户资金托管账户
	 * @param  tppUserCustId
	 */
	public void setTppUserCustId(final String tppUserCustId) {
		this.tppUserCustId = tppUserCustId;
	}

	/**
	 * 获取收款用户id
	 * @return toUserId
	 */
	public String getToUserId() {
		return toUserId;
	}

	/**
	 * 设置收款用户id
	 * @param  toUserId
	 */
	public void setToUserId(final String toUserId) {
		this.toUserId = toUserId;
	}

	/**
	 * 获取收款用户资金托管账户Id
	 * @return toTppUserCustId
	 */
	public String getToTppUserCustId() {
		return toTppUserCustId;
	}

	/**
	 * 设置收款用户资金托管账户Id
	 * @param  toTppUserCustId
	 */
	public void setToTppUserCustId(final String toTppUserCustId) {
		this.toTppUserCustId = toTppUserCustId;
	}

	/**
	 * 获取交易金额
	 * @return money
	 */
	public BigDecimal getMoney() {
		return money;
	}

	/**
	 * 设置交易金额
	 * @param  money
	 */
	public void setMoney(final BigDecimal money) {
		this.money = money;
	}

	/**
	 * 获取服务费
	 * @return servFee
	 */
	public BigDecimal getServFee() {
		return servFee;
	}

	/**
	 * 设置服务费
	 * @param  servFee
	 */
	public void setServFee(final BigDecimal servFee) {
		this.servFee = servFee;
	}

	/**
	 * 获取标的号
	 * @return bidNo
	 */
	public String getBidNo() {
		return bidNo;
	}

	/**
	 * 设置标的号
	 * @param  bidNo
	 */
	public void setBidNo(final String bidNo) {
		this.bidNo = bidNo;
	}

	/**
	 * 获取ifaes系统交易流水号
	 * @return orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * 设置ifaes系统交易流水号
	 * @param  orderId
	 */
	public void setOrderId(final String orderId) {
		this.orderId = orderId;
	}

	/**
	 * 获取ifaes交易时间
	 * @return orderDate
	 */
	public String getOrderDate() {
		return orderDate;
	}

	/**
	 * 设置ifaes交易时间
	 * @param  orderDate
	 */
	public void setOrderDate(final String orderDate) {
		this.orderDate = orderDate;
	}

	/**
	 * 获取第三方订单号
	 * @return subOrdId
	 */
	public String getSubOrdId() {
		return subOrdId;
	}

	/**
	 * 设置第三方订单号
	 * @param  subOrdId
	 */
	public void setSubOrdId(final String subOrdId) {
		this.subOrdId = subOrdId;
	}

	/**
	 * 获取第三方交易时间
	 * @return subOrdDate
	 */
	public String getSubOrdDate() {
		return subOrdDate;
	}

	/**
	 * 设置第三方交易时间
	 * @param  subOrdDate
	 */
	public void setSubOrdDate(final String subOrdDate) {
		this.subOrdDate = subOrdDate;
	}

	/**
	 * 获取第三方冻结流水号
	 * @return trxId
	 */
	public String getTrxId() {
		return trxId;
	}

	/**
	 * 设置第三方冻结流水号
	 * @param  trxId
	 */
	public void setTrxId(final String trxId) {
		this.trxId = trxId;
	}

	/**
	 * 获取银行卡号
	 * @return bankCardId
	 */
	public String getBankCardId() {
		return bankCardId;
	}

	/**
	 * 设置银行卡号
	 * @param  bankCardId
	 */
	public void setBankCardId(final String bankCardId) {
		this.bankCardId = bankCardId;
	}

	/**
	 * 获取证件号
	 * @return cardId
	 */
	public String getCardId() {
		return cardId;
	}

	/**
	 * 设置证件号
	 * @param  cardId
	 */
	public void setCardId(final String cardId) {
		this.cardId = cardId;
	}

	/**
	 * 获取证件类型
	 * @return idType
	 */
	public String getIdType() {
		return idType;
	}

	/**
	 * 设置证件类型
	 * @param  idType
	 */
	public void setIdType(final String idType) {
		this.idType = idType;
	}

	/**
	 * 获取手机号
	 * @return phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * 设置手机号
	 * @param  phone
	 */
	public void setPhone(final String phone) {
		this.phone = phone;
	}

	/**
	 * 获取邮箱
	 * @return email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 设置邮箱
	 * @param  email
	 */
	public void setEmail(final String email) {
		this.email = email;
	}

	/**
	 * 获取前台回调url
	 * @return retUrl
	 */
	public String getRetUrl() {
		return retUrl;
	}

	/**
	 * 设置前台回调url
	 * @param  retUrl
	 */
	public void setRetUrl(final String retUrl) {
		this.retUrl = retUrl;
	}

	/**
	 * 获取后台回调url
	 * @return bgRetUrl
	 */
	public String getBgRetUrl() {
		return bgRetUrl;
	}

	/**
	 * 设置后台回调url
	 * @param  bgRetUrl
	 */
	public void setBgRetUrl(final String bgRetUrl) {
		this.bgRetUrl = bgRetUrl;
	}

	/**
	 * 获取投标对象
	 * @return borrowId
	 */
	public String getBorrowId() {
		return borrowId;
	}

	/**
	 * 设置投标对象
	 * @param  borrowId
	 */
	public void setBorrowId(final String borrowId) {
		this.borrowId = borrowId;
	}

	/**
	 * 获取投标对象
	 * @return tenderId
	 */
	public String getTenderId() {
		return tenderId;
	}

	/**
	 * 设置投标对象
	 * @param  tenderId
	 */
	public void setTenderId(final String tenderId) {
		this.tenderId = tenderId;
	}

	/**
	 * 获取扩展参数
	 * @return extended
	 */
	public String getExtended() {
		return extended;
	}

	/**
	 * 设置扩展参数
	 * @param  extended
	 */
	public void setExtended(final String extended) {
		this.extended = extended;
	}

	/**
	 * 获取取现复核标识
	 * @return flag
	 */
	public String getFlag() {
		return flag;
	}

	/**
	 * 设置取现复核标识
	 * @param  flag
	 */
	public void setFlag(final String flag) {
		this.flag = flag;
	}

	/**
	 * 获取回调结果
	 * @return respDesc
	 */
	public String getRespDesc() {
		return respDesc;
	}

	/**
	 * 设置回调结果
	 * @param  respDesc
	 */
	public void setRespDesc(final String respDesc) {
		this.respDesc = respDesc;
	}

	/**
	 * 获取触发时间
	 * @return addTime
	 */
	public Date getAddTime() {
		return addTime;
	}

	/**
	 * 设置触发时间
	 * @param  addTime
	 */
	public void setAddTime(final Date addTime) {
		this.addTime = addTime;
	}

	/**
	 * 获取投资项目id
	 * @return tppBorrowId
	 */
	public String getTppBorrowId() {
		return tppBorrowId;
	}

	/**
	 * 设置投资项目id
	 * @param  tppBorrowId
	 */
	public void setTppBorrowId(final String tppBorrowId) {
		this.tppBorrowId = tppBorrowId;
	}

	/**
	 * 获取属性userNo的值
	 * @return userNo属性值
	 */
	public String getUserNo() {
		return userNo;
	}

	/**
	 * 设置属性userNo的值
	 * @param  userNo属性值
	 */
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	/**
	 * 获取属性realName的值
	 * @return realName属性值
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * 设置属性realName的值
	 * @param  realName属性值
	 */
	public void setRealName(String realName) {
		this.realName = realName;
	}
	
}
