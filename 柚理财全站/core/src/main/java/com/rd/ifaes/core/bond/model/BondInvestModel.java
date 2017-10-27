package com.rd.ifaes.core.bond.model;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.rd.ifaes.common.dict.BondInvestEnum;
import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.core.bond.domain.BondInvest;

/**
 * 债权投资展示类
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年8月30日
 */
public class BondInvestModel  extends BaseEntity<BondInvestModel>  {

	private static final long serialVersionUID = 1L;
	/**
	 * 投资渠道 1 PC  2 APP 3 微信
	 */
	private String investChannel;
	/**
	 * 待支付的剩余时间 
	 */
	private int remainTimes;
	/**
	 * 债权标对应剩余期限天数
	 */
	private String remainDays;
	/**
	 * 是否可以再次转让的标识   true 显示转让按钮；false 不显示转让按钮
	 */
	private boolean isShowBond;
	/**
	 * 原始标状态
	 */
	private String projectStatus;
	/**
	 * 债权ID  
	 */
	private String	bondId;		
	/**
	 * 项目ID
	 */
	private String	projectId;
	/**
	 * 债权投资订单号
	 */
	private String  investOrderNo;   
	/**
	 * 投标ID 
	 */
	private String	investId;	
	/**
	 * 投资流水号
	 */
	private String	investNo;
	/**
	 * 投资日期
	 */
	private String	investDate;
	/**
	 * 受让人 
	 */
	private String	userId;
	/**
	 * 投资金额 
	 */
	private BigDecimal	amount;
	/**
	 * 债权人支付手续费
	 */
	private BigDecimal  bondFee;  
	/**
	 * 提前付息
	 */
	private BigDecimal	payedInterest;
	/**
	 * 已收金额
	 */
	private BigDecimal	receivedAccount; 
	/**
	 * 状态   状态   受让状态：待支付 0  受让状态：受让成功  1 受让状态：受让失败  2   受让状态：超时取消 3  重新支付 投资作废  4
	 */
	private String	status;		 
	/**
	 * 添加时间 
	 */
	private Date	createTime;		 
	/**
	 * 添加IP
	 */
	private String	addIp;		 
	/**
	 * 再次转让时bond_invest对应的在原始标下的投资id
	 */
	private String projectInvestId;
	
	
	/**
	 * 状态集合
	 */
	private String[] statusSet;
	/**
	 * 债权标识
	 */
	private String bondFlag;
	
	/**
	 * 债权投资用户名
	 */
	private String userName;
	
	/**
	 * 债权投资真实姓名
	 */
	private String realName;
	
	/**
	 * 债权投资人手机号
	 */
	private String mobile;
	/**
	 * 原始标名称
	 */
	private String projectName;
	
	/**
	 * 债权名称
	 */
	private String bondName;
	
	/**
	 * 折溢价率
	 */
	private BigDecimal bondApr;
	
	/**
	 * 支付金额
	 */
	private BigDecimal paidMoney;
	
	/**
	 * 待收本息
	 */
	private BigDecimal waitMoney;
	
	/**
	 * 风险评估提示勾选，继续投资
	 */
	private int isSelectedTip;
	
	/**
	 * 受让成功时间  字符串
	 */
	private String createTimeStr;
	
	/**
	 * @return the isShowBond
	 */
	public boolean isShowBond() {
		return isShowBond;
	}

	/**
	 * @param isShowBond the isShowBond to set
	 */
	public void setShowBond(boolean isShowBond) {
		this.isShowBond = isShowBond;
	}

	/**
	 * 获取属性isSelectedTip的值
	 * @return isSelectedTip属性值
	 */
	public int getIsSelectedTip() {
		return isSelectedTip;
	}

	/**
	 * 设置属性isSelectedTip的值
	 * @param  isSelectedTip属性值
	 */
	public void setIsSelectedTip(int isSelectedTip) {
		this.isSelectedTip = isSelectedTip;
	}

	/**
	 * 获取项目名称
	 * @author QianPengZhan
	 * @date 2016年8月30日
	 * @return
	 */
	public String getProjectName() {
		return projectName;
	}
	
	/**
	 * 项目名称
	 * @author QianPengZhan
	 * @date 2016年8月30日
	 * @param projectName
	 */
	public void setProjectName(final String projectName) {
		this.projectName = projectName;
	}
	
	/**
	 * 设置折溢价率
	 * @author QianPengZhan
	 * @date 2016年8月30日
	 * @return
	 */
	public BigDecimal getBondApr() {
		return bondApr;
	}
	
	/**
	 * 设置折溢价率
	 * @author QianPengZhan
	 * @date 2016年8月30日
	 * @param bondApr
	 */
	public void setBondApr(final BigDecimal bondApr) {
		this.bondApr = bondApr;
	}
	
	/**
	 * 获取支付金额
	 * @author QianPengZhan
	 * @date 2016年8月30日
	 * @return
	 */
	public BigDecimal getPaidMoney() {
		return paidMoney;
	}
	
	/**
	 * 设置支付金额
	 * @author QianPengZhan
	 * @date 2016年8月30日
	 * @param paidMoney
	 */
	public void setPaidMoney(final BigDecimal paidMoney) {
		this.paidMoney = paidMoney;
	}
	
	/**
	 * @return waitMoney
	 */
	public BigDecimal getWaitMoney() {
		return waitMoney;
	}
	
	/**
	 * 设置待收金额
	 * @author QianPengZhan
	 * @date 2016年8月30日
	 * @param waitMoney
	 */
	public void setWaitMoney(final BigDecimal waitMoney) {
		this.waitMoney = waitMoney;
	}
	
	
	/**
	 * 复制属性方法
	 * @author QianPengZhan
	 * @date 2016年8月30日
	 * @return
	 */
	public BondInvest prototype(){
		final BondInvest bondInvest = new BondInvest();
		BeanUtils.copyProperties(this, bondInvest);
		return bondInvest;
	}

	/**
	 * 获取属性userName的值
	 * @return userName属性值
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * 设置属性userName的值
	 * @param  userName属性值
	 */
	public void setUserName(final String userName) {
		this.userName = userName;
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
	public void setRealName(final String realName) {
		this.realName = realName;
	}

	/**
	 * 获取属性mobile的值
	 * @return mobile属性值
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * 设置属性mobile的值
	 * @param  mobile属性值
	 */
	public void setMobile(final String mobile) {
		this.mobile = mobile;
	}


	/**
	 * 获取属性bondName的值
	 * @return bondName属性值
	 */
	public String getBondName() {
		return bondName;
	}

	/**
	 * 设置属性bondName的值
	 * @param  bondName属性值
	 */
	public void setBondName(final String bondName) {
		this.bondName = bondName;
	}

	
	/**
	 * 获取属性bondFlag的值
	 * @return bondFlag属性值
	 */
	public String getBondFlag() {
		return bondFlag;
	}

	/**
	 * 设置属性bondFlag的值
	 * @param  bondFlag属性值
	 */
	public void setBondFlag(final String bondFlag) {
		this.bondFlag = bondFlag;
	}

	/**
	 * 获取属性projectInvestId的值
	 * @return projectInvestId属性值
	 */
	public String getProjectInvestId() {
		return projectInvestId;
	}

	/**
	 * 设置属性projectInvestId的值
	 * @param  projectInvestId属性值
	 */
	public void setProjectInvestId(final String projectInvestId) {
		this.projectInvestId = projectInvestId;
	}

	/**
	 * 获取属性statusSet的值
	 * @return statusSet属性值
	 */
	public String[] getStatusSet() {
		return statusSet;
	}

	/**
	 * 设置属性statusSet的值
	 * @param  statusSet属性值
	 */
	public void setStatusSet(final String[] statusSet) {
		this.statusSet = statusSet;
	}
	
	/**
	 * 获取属性bondId的值
	 * @return bondId属性值
	 */
	public String getBondId() {
		return bondId;
	}

	/**
	 * 设置属性bondId的值
	 * @param  bondId属性值
	 */
	public void setBondId(final String bondId) {
		this.bondId = bondId;
	}

	/**
	 * 获取属性projectId的值
	 * @return projectId属性值
	 */
	public String getProjectId() {
		return projectId;
	}

	/**
	 * 设置属性projectId的值
	 * @param  projectId属性值
	 */
	public void setProjectId(final String projectId) {
		this.projectId = projectId;
	}

	/**
	 * 获取属性investOrderNo的值
	 * @return investOrderNo属性值
	 */
	public String getInvestOrderNo() {
		return investOrderNo;
	}

	/**
	 * 设置属性investOrderNo的值
	 * @param  investOrderNo属性值
	 */
	public void setInvestOrderNo(final String investOrderNo) {
		this.investOrderNo = investOrderNo;
	}

	/**
	 * 获取属性investId的值
	 * @return investId属性值
	 */
	public String getInvestId() {
		return investId;
	}

	/**
	 * 设置属性investId的值
	 * @param  investId属性值
	 */
	public void setInvestId(final String investId) {
		this.investId = investId;
	}

	/**
	 * 获取属性investNo的值
	 * @return investNo属性值
	 */
	public String getInvestNo() {
		return investNo;
	}

	/**
	 * 设置属性investNo的值
	 * @param  investNo属性值
	 */
	public void setInvestNo(final String investNo) {
		this.investNo = investNo;
	}

	/**
	 * 获取属性investDate的值
	 * @return investDate属性值
	 */
	public String getInvestDate() {
		return investDate;
	}

	/**
	 * 设置属性investDate的值
	 * @param  investDate属性值
	 */
	public void setInvestDate(final String investDate) {
		this.investDate = investDate;
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
	public void setUserId(final String userId) {
		this.userId = userId;
	}

	/**
	 * 获取属性amount的值
	 * @return amount属性值
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * 设置属性amount的值
	 * @param  amount属性值
	 */
	public void setAmount(final BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * 获取属性bondFee的值
	 * @return bondFee属性值
	 */
	public BigDecimal getBondFee() {
		return bondFee;
	}

	/**
	 * 设置属性bondFee的值
	 * @param  bondFee属性值
	 */
	public void setBondFee(final BigDecimal bondFee) {
		this.bondFee = bondFee;
	}

	/**
	 * 获取属性payedInterest的值
	 * @return payedInterest属性值
	 */
	public BigDecimal getPayedInterest() {
		return payedInterest;
	}

	/**
	 * 设置属性payedInterest的值
	 * @param  payedInterest属性值
	 */
	public void setPayedInterest(final BigDecimal payedInterest) {
		this.payedInterest = payedInterest;
	}

	/**
	 * 获取属性receivedAccount的值
	 * @return receivedAccount属性值
	 */
	public BigDecimal getReceivedAccount() {
		return receivedAccount;
	}

	/**
	 * 设置属性receivedAccount的值
	 * @param  receivedAccount属性值
	 */
	public void setReceivedAccount(final BigDecimal receivedAccount) {
		this.receivedAccount = receivedAccount;
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
	public void setStatus(final String status) {
		this.status = status;
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
	public void setCreateTime(final Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取属性addIp的值
	 * @return addIp属性值
	 */
	public String getAddIp() {
		return addIp;
	}

	/**
	 * 设置属性addIp的值
	 * @param  addIp属性值
	 */
	public void setAddIp(final String addIp) {
		this.addIp = addIp;
	}

	/**
	 * 获取属性projectStatus的值
	 * @return projectStatus属性值
	 */
	public String getProjectStatus() {
		return projectStatus;
	}

	/**
	 * 设置属性projectStatus的值
	 * @param  projectStatus属性值
	 */
	public void setProjectStatus(final String projectStatus) {
		this.projectStatus = projectStatus;
	}

	/**
	 * 获取属性remainDays的值
	 * @return remainDays属性值
	 */
	public String getRemainDays() {
		return remainDays;
	}

	/**
	 * 设置属性remainDays的值
	 * @param  remainDays属性值
	 */
	public void setRemainDays(String remainDays) {
		this.remainDays = remainDays;
	}

	public int getRemainTimes() {
		return remainTimes;
	}

	public void setRemainTimes(int remainTimes) {
		this.remainTimes = remainTimes;
	}

	/**
	 * 获取属性investChannel的值
	 * @return investChannel属性值
	 */
	public String getInvestChannel() {
		return investChannel;
	}

	/**
	 * 设置属性investChannel的值
	 * @param  investChannel属性值
	 */
	public void setInvestChannel(String investChannel) {
		this.investChannel = investChannel;
	}

	/**
	 * 获取属性createTimeStr的值
	 * @return createTimeStr属性值
	 */
	public String getCreateTimeStr() {
		 createTimeStr = this.createTime.toString();
		 if(BondInvestEnum.STATUS_INIT.eq(this.status)){
			 createTimeStr = BondInvestEnum.STATUS_INIT.getName();
		 }
		 return createTimeStr;
	}

	/**
	 * 设置属性createTimeStr的值
	 * @param  createTimeStr属性值
	 */
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	
	
}
