package com.rd.ifaes.core.account.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.annotation.DictType;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.account.service.impl.CashServiceImpl;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.DictUtils;

/**
 * entity:Cash
 * 
 * @author xhf
 * @version 3.0
 * @date 2016-7-2
 */
public class Cash extends BaseEntity<Cash> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Cash.class);
	
	public static String KEY_ADVANCE_CASH_FEE_MONTH_COUNT = "advance:cash:fee:month:%s:userId:%s";
	
	private static final long serialVersionUID = 1L;
	/** 用户ID **/ 
	private String	userId;	
	/** 订单号（平台本地生成订单号） */ 
	private String	orderNo;
	/** 取现订单号（UFX流水号） */ 
	private String	cashNo;		
	/** 取现金额 */ 
	private BigDecimal	amount;		
	/** 实际到账金额 */ 
	private BigDecimal	realAmount;	
	/** 取现手续费 */ 
	private BigDecimal	cashFee;
	/** 手续费 */ 
	private BigDecimal	servFee;	
	/** 实际操作金额 */
	private BigDecimal	money;	
	/** 0：提现申请，1：提现处理中，2：提现待审核， 3：提现成功， 4：提现失败 */ 
	@DictType(type="cashStatus")
	private String	status;	
	 /** 取现银行卡号 */
	private String	cardId;		 
	 /** 所属银行 */
	@DictType(type="accountBank")
	private String	bankCode;	
	/** 是否需要审核（1：需要 0：不需要） */ 
	private String	needAudit;	
	/** 是否垫付手续费（1：需要 0：不需要） */ 
	@DictType(type="cashFeeType")
	private String	isAdvance;
	/*垫付的手续费*/
	private BigDecimal advanceFee;	
	/** 添加时间 */ 
	private Date	addTime;
	/** 添加IP */ 
	private String	addIp;	
	/** 审核时间 */ 
	private Date	verifyTime;		 
	/** 审核人 */ 
	private String	verifyUserName;	
	/** 审核备注 */ 
	private String	verifyRemark;		
	/** 提现备注 */ 
	private String	remark;		
	//其他自定义属性
	/** 用户名 */ 
	private String userName;   
	/** 真实姓名 */ 
	private String realName;  
	/** 用户类型 */ 
	@DictType(type="userNature")
	private String userNature; 
	/** 人工核查（0：无  1：待处理  2：已处理） */ 
	private String	manHandle;	
	/** 核查原因 */ 
	private String handleReason;
	/** 核查时间 */ 
	private Date	handleTime;		 
	/** 核查人 */ 
	private String	handleUser;	
	
	/** 订单原状态 */
	private String preStatus;
	/** 渠道 */
	private String channel;
	/**
	 * 对账结果
	 */
	private String chkResult;
	/**
	 * 获得用户Id
	 * @return
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置用户Id
	 * @param userId
	 */
	public void setUserId(final String userId) {
		this.userId = userId;
	}

	/**
	 * 获得订单编号
	 * @return
	 */
	public String getOrderNo() {
		return orderNo;
	}

	/**
	 * 设置订单编号
	 * @param orderNo
	 */
	public void setOrderNo(final String orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * 获得提现订单编号
	 * @return
	 */
	public String getCashNo() {
		return cashNo;
	}

	/**
	 * 设置提现订单编号
	 * @param cashNo
	 */
	public void setCashNo(final String cashNo) {
		this.cashNo = cashNo;
	}

	/**
	 * 获得金额
	 * @return
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * 设置金额
	 * @param amount
	 */
	public void setAmount(final BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * 获得实际到账金额
	 * @return
	 */
	public BigDecimal getRealAmount() {
		return realAmount;
	}

	/**
	 * 设置实际到账金额
	 * @param realAmount
	 */
	public void setRealAmount(final BigDecimal realAmount) {
		this.realAmount = realAmount;
	}

	/**
	 * 获得提现手续费
	 * @return
	 */
	public BigDecimal getCashFee() {
		return cashFee;
	}

	/**
	 * 设置提现手续费
	 * @param cashFee
	 */
	public void setCashFee(final BigDecimal cashFee) {
		this.cashFee = cashFee;
	}

	/**
	 * 获得服务费
	 * @return
	 */
	public BigDecimal getServFee() {
		return servFee;
	}

	/**
	 * 设置服务费
	 * @param servFee
	 */
	public void setServFee(final BigDecimal servFee) {
		this.servFee = servFee;
	}

	/**
	 * 获得状态
	 * @return
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * 设置状态
	 * @param status
	 */
	public void setStatus(final String status) {
		this.status = status;
	}

	/**
	 * 获得银行卡ID
	 * @return
	 */
	public String getCardId() {
		return cardId;
	}

	/**
	 * 设置银行卡ID
	 * @param cardId
	 */
	public void setCardId(final String cardId) {
		this.cardId = cardId;
	}

	/**
	 * 获得银行编号
	 * @return
	 */
	public String getBankCode() {
		return bankCode;
	}

	/**
	 * 设置银行编号
	 * @param bankCode
	 */
	public void setBankCode(final String bankCode) {
		this.bankCode = bankCode;
	}

	/**
	 * 获得是否需要审核（1：需要 0：不需要）
	 * @return
	 */
	public String getNeedAudit() {
		return needAudit;
	}

	/**
	 * 设置是否需要审核（1：需要 0：不需要）
	 * @param needAudit
	 */
	public void setNeedAudit(final String needAudit) {
		this.needAudit = needAudit;
	}

	/**
	 * 是否垫付手续费（1：需要 0：不需要） 
	 * @return
	 */
	public String getIsAdvance() {
		return isAdvance;
	}

	/**
	 * 设置是否垫付手续费（1：需要 0：不需要） 
	 * @param isAdvance
	 */
	public void setIsAdvance(final String isAdvance) {
		this.isAdvance = isAdvance;
	}

	/**
	 * 获得添加时间
	 * @return
	 */
	public Date getAddTime() {
		return addTime;
	}

	/**
	 * 设置添加时间
	 * @param addTime
	 */
	public void setAddTime(final Date addTime) {
		this.addTime = addTime;
	}

	/**
	 * 获得添加IP
	 * @return
	 */
	public String getAddIp() {
		return addIp;
	}

	/**
	 * 设置添加ip
	 * @param addIp
	 */
	public void setAddIp(final String addIp) {
		this.addIp = addIp;
	}

	/**
	 * 获得审核时间
	 * @return
	 */
	public Date getVerifyTime() {
		return verifyTime;
	}

	/**
	 * 设置审核时间
	 * @param verifyTime
	 */
	public void setVerifyTime(final Date verifyTime) {
		this.verifyTime = verifyTime;
	}

	/**
	 * 获得审核人 
	 * @return
	 */
	public String getVerifyUserName() {
		return verifyUserName;
	}

	/**
	 * 设置审核人 
	 */
	public void setVerifyUserName(final String verifyUserName) {
		this.verifyUserName = verifyUserName;
	}
	
	/**
	 * 获得审核备注 
	 * @return
	 */
	public String getVerifyRemark() {
		return verifyRemark;
	}

	/**
	 * 设置审核备注
	 * @param verifyRemark
	 */
	public void setVerifyRemark(final String verifyRemark) {
		this.verifyRemark = verifyRemark;
	}

	@Override
	public String toString() {
		return "Cash [userId=" + userId + ", orderNo=" + orderNo + ", cashNo="
				+ cashNo + ", amount=" + amount + ", realAmount=" + realAmount
				+ ", cashFee=" + cashFee + ", servFee=" + servFee + ", money="
				+ money + ", status=" + status + ", cardId=" + cardId
				+ ", bankCode=" + bankCode + ", needAudit=" + needAudit
				+ ", isAdvance=" + isAdvance + ", addTime=" + addTime
				+ ", addIp=" + addIp + ", verifyTime=" + verifyTime
				+ ", verifyUserName=" + verifyUserName + ", verifyRemark="
				+ verifyRemark + "]";
	}

	/**
	 * 获得实际操作金额 
	 * @return
	 */
	public BigDecimal getMoney() {
		return money;
	}

	/**
	 * 设置实际操作金额 
	 * @param money
	 */
	public void setMoney(final BigDecimal money) {
		this.money = money;
	}

	/**
	 * 获得状态显示名称
	 * @return
	 */
	public String getStatusStr() {
		return DictUtils.getItemName(DictTypeEnum.CASH_STATUS.getValue(), getStatus());
	}

	/**
	 * 获得用户名
	 * @return
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * 设置用户名
	 * @param userName
	 */
	public void setUserName(final String userName) {
		this.userName = userName;
	}

	/**
	 * 获得真实姓名
	 * @return
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * 设置真实姓名
	 * @param realName
	 */
	public void setRealName(final String realName) {
		this.realName = realName;
	}

	/**
	 * 获得用户类型
	 * @return
	 */
	public String getUserNature() {
		return userNature;
	}

	/**
	 * 设置用户类型
	 * @param userNature
	 */
	public void setUserNature(final String userNature) {
		this.userNature = userNature;
	}
	
	
	/**
	 * 获取属性remark的值
	 * @return remark属性值
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置属性remark的值
	 * @param  remark属性值
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 获得手续费承担方
	 * @return
	 */
	public String getFeeBearTip(){
		String feeBear = "个人支付";
    	if(CommonEnum.YES.eq(getIsAdvance())){
    		return "平台垫付";
    	}
    	return feeBear;
    }
	
	/**
	 * 获取总手续费，包括平台服务费和第三方收取的费用
	 * @return
	 */
	public BigDecimal getFee(){
		BigDecimal fee=null;
		if(getCashFee()!=null){ //说明异步回调信息已返回
			if(CommonEnum.YES.eq(getIsAdvance())){
	    		return getServFee();
	    	}else{
	    		return BigDecimalUtils.add(getCashFee(),getServFee());
	    	}
        }
        return fee;
	}
	
	/**
	 * 获取是否需要人工审核（1：需要0：不需要）
	 * @return manHandle
	 */
	public String getManHandle() {
		return manHandle;
	}

	/**
	 * 设置是否需要人工审核（1：需要0：不需要）
	 * @param  manHandle
	 */
	public void setManHandle(String manHandle) {
		this.manHandle = manHandle;
	}

	/**
	 * 获取人工审核原因
	 * @return handleReason
	 */
	public String getHandleReason() {
		return handleReason;
	}

	/**
	 * 设置人工审核原因
	 * @param  handleReason
	 */
	public void setHandleReason(String handleReason) {
		this.handleReason = handleReason;
	}

	/**
	 * 获取核查时间
	 * @return handleTime
	 */
	public Date getHandleTime() {
		return handleTime;
	}

	/**
	 * 设置核查时间
	 * @param  handleTime
	 */
	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}

	/**
	 * 获取核查人
	 * @return handleUser
	 */
	public String getHandleUser() {
		return handleUser;
	}

	/**
	 * 设置核查人
	 * @param  handleUser
	 */
	public void setHandleUser(String handleUser) {
		this.handleUser = handleUser;
	}
	
	/**
	 * 获取原状态
	 * @return preStatus
	 */
	public String getPreStatus() {
		return preStatus;
	}

	/**
	 * 设置原状态
	 * @param  preStatus
	 */
	public void setPreStatus(String preStatus) {
		this.preStatus = preStatus;
	}

	/**
	 * @return the 渠道
	 */
	public String getChannel() {
		return channel;
	}

	/**
	 * @param 渠道 the channel to set
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}
	/**
	 * 获取属性chkResult的值
	 * @return chkResult属性值
	 */
	public String getChkResult() {
		String result = StringUtils.EMPTY;
		if("W3".equals(this.chkResult)){
			result =  "系统受理中";
		}else if("W4".equals(this.chkResult)){
			result =  "银行受理中";
		}else if("S1".equals(this.chkResult)){
			result =  "银行交易成功";
		}else if("F1".equals(this.chkResult)){
			result =  "付款失败付款失败";
		}else if("F2".equals(this.chkResult)){
			result =  "付款核销";
		}else if("R9".equals(this.chkResult)){
			result =  "审批拒绝";
		}
		return result;
	}

	/**
	 * 设置属性chkResult的值
	 * @param  chkResult属性值
	 */
	public void setChkResult(String chkResult) {
		this.chkResult = chkResult;
	}

	public BigDecimal getAdvanceFee() {
		return advanceFee;
	}

	public void setAdvanceFee(BigDecimal advanceFee) {
		this.advanceFee = advanceFee;
	}

	public String getAdvanceCountCacheKey(){
		return String.format(Cash.KEY_ADVANCE_CASH_FEE_MONTH_COUNT, DateUtils.getMonth(), getUserId());
	}
	//平台垫付手续费+1次
	public void changeAdvanceCount(final int value) {
		int advanceCount = CacheUtils.getInt(getAdvanceCountCacheKey());
		int remainDays = DateUtils.getMonthRemainDays();
		CacheUtils.set(String.format(getAdvanceCountCacheKey()), 
				advanceCount + value, remainDays * 24 * 60 * 60);
		LOGGER.debug("用户userId:{},当前已垫付手续费次数为：{}", getUserId(), advanceCount + value);
	}
	
	
}
