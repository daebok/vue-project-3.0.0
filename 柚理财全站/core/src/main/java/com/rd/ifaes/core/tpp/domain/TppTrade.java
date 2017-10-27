package com.rd.ifaes.core.tpp.domain;


import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.annotation.DictType;
import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.dict.TppEnum;
import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.OrderNoUtils;


/**
 * entity:TppTrade
 * 
 * @author FangJun
 * @version 3.0
 * @date 2016-7-18
 */
public class TppTrade extends BaseEntity<TppTrade> {

	private static final long serialVersionUID = 1L;

	@DictType(type="tppStatus")
	private String status; /* 状态：0-未处理，1-成功，2-失败 3-已查询*/
	private String serviceType; /* 平台操作类型 */
	@DictType(type="tppType")
	private String tppType; /* 第三方服务类型 */
	private String userId; /* 付款用户 */
	private String tppUserCustId; /* 付款用户第三方帐户 */
	private String toUserId; /* 收款用户 */
	private String toTppUserCustId; /* 收款用户第三方账户 */
	private BigDecimal money; /* 交易金额 */
	private BigDecimal servFee; /* 服务费 */
	private String orderNo; /* 平台订单号 */
	private Date orderDate; /* 平台订单日期 */
	private String tradeNo; /* 三方流水号 */
	private Date tradeDate; /* 三方交易日期 */
	private String projectId; /* 本地 projectNo，对应第三方的projectId */
	private String investId; /* 投资id */
	private String params; /* 扩展参数 */
	private String returnUrl; /* 前台回调url */
	private String noticeUrl; /* 后台回调url */
	private String respDesc; /* 回调信息 */
	private String investProjectId; /* 本地projectId */
	private Date createTime; /* 添加时间 */
    private  String preTradeId;/*重发原记录ID*/
	private Date updateTime; /* 发送时间 */
	private Date  notifyTime; /* 异步回调时间 */
    private String authCode;
    private String projectRepaymentId;//对应的还款记录ID
	// 其他自定义
	//还款类型  01：融资人还款       02：商户平台代偿 03：融资人还代偿给商户平台 04：担保方代偿       05：融资人还代偿给担保方, 06：债权还款 
	private String repayType;
	//资金流向 01：项目资金转入       02：项目资金转出 03：项目资金无密转入 
	private String fundFlow;
	/**
	 * 冻结类型
	 */
	private String freezeType;
	/**
	 * 解冻类型
	 */
	private String unFreezeType;
	
	private String realName;//付款方姓名
	private String toRealName;//收款方姓名
	private String userCustId;//用户(投资人或者借款人)第三方账户
	
	/**
	 * 获取repayType的值
	 * @return repayType值
	 */
	public String getRepayType() {
		return repayType;
	}
	/**
	 * 设置repayType的值
	 * @param  repayType值
	 */
	public void setRepayType(String repayType) {
		this.repayType = repayType;
	}
	/**
	 * 获取fundFlow的值
	 * @return fundFlow值
	 */
	public String getFundFlow() {
		return fundFlow;
	}
	/**
	 * 设置fundFlow的值
	 * @param  fundFlow值
	 */
	public void setFundFlow(String fundFlow) {
		this.fundFlow = fundFlow;
	}
	// Constructor
	public TppTrade() {
	}

	/**
	 * 构造函数
	 * 
	 * @param serviceType
	 *            平台操作类型
	 * @param tppType
	 *            第三方服务类型
	 * @param userId
	 *            付款方ID
	 * @param toUserId
	 *            收款方ID
	 * @param money
	 *            交易金额
	 * @param investId
	 *            投资ID
	 */
	public TppTrade(String serviceType, String tppType, String userId, String tppUserCustId, String toUserId, String toTppUserCustId,
			BigDecimal money, String investId) {
		this.serviceType = serviceType;
		this.tppType = tppType;
		this.userId = userId;
		this.tppUserCustId = tppUserCustId;
		this.toUserId = toUserId;
		this.toTppUserCustId = toTppUserCustId;
		this.money = money;
		this.investId = investId;
		this.orderNo = OrderNoUtils.getSerialNumber();
		this.orderDate = DateUtils.getNow();
		this.createTime = DateUtils.getNow();
		this.status=TppEnum.STATUS_UNDO.getValue();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getTppType() {
		return tppType;
	}

	public void setTppType(String tppType) {
		this.tppType = tppType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTppUserCustId() {
		return tppUserCustId;
	}

	public void setTppUserCustId(String tppUserCustId) {
		this.tppUserCustId = tppUserCustId;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public String getToTppUserCustId() {
		return toTppUserCustId;
	}

	public void setToTppUserCustId(String toTppUserCustId) {
		this.toTppUserCustId = toTppUserCustId;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public BigDecimal getServFee() {
		return servFee;
	}

	public void setServFee(BigDecimal servFee) {
		this.servFee = servFee;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public Date getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getInvestId() {
		return investId;
	}

	public void setInvestId(String investId) {
		this.investId = investId;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getNoticeUrl() {
		return noticeUrl;
	}

	public void setNoticeUrl(String noticeUrl) {
		this.noticeUrl = noticeUrl;
	}

	public String getRespDesc() {
		return respDesc;
	}

	public void setRespDesc(String respDesc) {
		this.respDesc = respDesc;
	}

	public String getInvestProjectId() {
		return investProjectId;
	}

	public void setInvestProjectId(String investProjectId) {
		this.investProjectId = investProjectId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取freezeType的值
	 * @return freezeType值
	 */
	public String getFreezeType() {
		return freezeType;
	}
	/**
	 * 设置freezeType的值
	 * @param  freezeType值
	 */
	public void setFreezeType(String freezeType) {
		this.freezeType = freezeType;
	}
	/**
	 * 获取unFreezeType的值
	 * @return unFreezeType值
	 */
	public String getUnFreezeType() {
		return unFreezeType;
	}
	/**
	 * 设置unFreezeType的值
	 * @param  unFreezeType值
	 */
	public void setUnFreezeType(String unFreezeType) {
		this.unFreezeType = unFreezeType;
	}
	
	public String getRealName() {
		return realName;
	}
	public String getRealNameStr() {
		if(ConfigUtils.getValue(ConfigConstant.UFX_CUSTOMERID).equals(this.tppUserCustId)){
			return "平台";
		}
		if(CbhbConstant.CBHB_TPP_NAME.equals(ConfigUtils.getTppName())){
			if(TppEnum.TPP_TYPE_LOAN.getValue().equals(this.tppType)){
				return "投资人";
			}
			if(TppEnum.TPP_TYPE_EXPERCAP.getValue().equals(this.tppType)){
				return "平台";
			}
		}
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getToRealName() {
		return toRealName;
	}
	public String getToRealNameStr() {
		if(ConfigUtils.getValue(ConfigConstant.UFX_CUSTOMERID).equals(this.toTppUserCustId)){
			return "平台";
		}
		if(CbhbConstant.CBHB_TPP_NAME.equals(ConfigUtils.getTppName())){
			if(TppEnum.TPP_TYPE_REPAY.getValue().equals(this.tppType) 
					|| TppEnum.TPP_TYPE_EXPERCAP.getValue().equals(this.tppType)){
				return "投资人";
			}
		}
		return toRealName;
	}
	public void setToRealName(String toRealName) {
		this.toRealName = toRealName;
	}
		
	public String getUserCustId() {
		return userCustId;
	}
	public void setUserCustId(String userCustId) {
		this.userCustId = userCustId;
	}
	
	/**
	 * 获取重发原记录ID
	 * @return 重发原记录ID
	 */
	public String getPreTradeId() {
		return preTradeId;
	}
	/**
	 * 设置重发原记录ID
	 * @param  重发原记录ID
	 */
	public void setPreTradeId(String preTradeId) {
		this.preTradeId = preTradeId;
	}
 
	/**
	 * 获取发送时间
	 * @return 发送时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置发送时间
	 * @param  发送时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取异步回调时间
	 * @return 异步回调时间
	 */
	public Date getNotifyTime() {
		return notifyTime;
	}
	/**
	 * 设置异步回调时间
	 * @param  异步回调时间
	 */
	public void setNotifyTime(Date notifyTime) {
		this.notifyTime = notifyTime;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getProjectRepaymentId() {
		return projectRepaymentId;
	}

	public void setProjectRepaymentId(String projectRepaymentId) {
		this.projectRepaymentId = projectRepaymentId;
	}

	@Override
	public String toString() {
		return "TppTrade [status=" + status + ", serviceType=" + serviceType + ", tppType=" + tppType + ", userId=" + userId
				+ ", tppUserCustId=" + tppUserCustId + ", toUserId=" + toUserId + ", toTppUserCustId=" + toTppUserCustId
				+ ", money=" + money + ", servFee=" + servFee + ", orderNo=" + orderNo + ", orderDate=" + orderDate + ", tradeNo="
				+ tradeNo + ", tradeDate=" + tradeDate + ", projectId=" + projectId + ", investId=" + investId + ", params="
				+ params + ", returnUrl=" + returnUrl + ", noticeUrl=" + noticeUrl + ", respDesc=" + respDesc
				+ ", investProjectId=" + investProjectId + ", createTime=" + createTime + ", preTradeId=" + preTradeId
				+ ", updateTime=" + updateTime + ", notifyTime=" + notifyTime + ", repayType=" + repayType + ", fundFlow="
				+ fundFlow + ", freezeType=" + freezeType + ", unFreezeType=" + unFreezeType + ", realName=" + realName
				+ ", toRealName=" + toRealName + ", userCustId=" + userCustId + "]";
	}
 
}
