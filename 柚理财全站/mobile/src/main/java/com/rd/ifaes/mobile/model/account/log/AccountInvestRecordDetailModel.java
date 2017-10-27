package com.rd.ifaes.mobile.model.account.log;
import java.util.Date;
import java.util.List;
/**
 * @author shenliang
 * 用户投资记录详情（普通产品）
 */
public class AccountInvestRecordDetailModel {

	/**
	 * 标的ID
	 */
	private String id;
	
	private String uuid;
	
	/**
	 * 标的名称
	 */
	private String name;
	
	/**
	 * 标的金额
	 */
	private double money;
	
	/**
	 * 投资时间
	 */
	private Date starTime;
	
	/**
	 * 计息起始日
	 */
	private Date interestTime;
	
	/**
	 * 到期日
	 */
	private Date dueDateTime;
	
	/**
	 * 投资状态: 0投标待处理，1成功，2失败
	 */
	private int status;
	
	/**
	 * 产品利率
	 */
	private double apr;
	
	/**
	 * 还款方式: 1按月分期还款; 2一次性还款;3每月还息到期还本
	 */
	private int repaymentType;
	
	/**
	 * 优惠金额
	 */
	private double discount;
	
	/**
	 * 总收益
	 */
	private double totalProfit;
	
	/**
	 * 已收收益
	 */
	private double profitYet;
	
	/**
	 * 待收收益
	 */
	private double collectProfit;
	
	/**
	 * 合同链接
	 */
	private String protrcolUrl;
	
	/**
	 * 投资状态（文本）
	 */
	private String statusStr;
	
	private List<AccountInvestRecordDetailCollectionItemModel> collections;
	
	//--------------------------华丽分割线----------------------------------
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getUuid(){
		return uuid;
	}
	
	public void setUuid (String uuid){
		this.uuid = uuid;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}
	
	public Date getStarTime() {
		return starTime;
	}

	public void setStarTime(Date starTime) {
		this.starTime = starTime;
	}
	
	public Date getInterestTime() {
		return interestTime;
	}

	public void setInterestTime(Date interestTime) {
		this.interestTime = interestTime;
	}
	
	public Date getDueDateTime() {
		return dueDateTime;
	}

	public void setDueDateTime(Date dueDateTime) {
		this.dueDateTime = dueDateTime;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public double getApr(){
			return apr;
	}
	
	public void setApr(double apr){
		this.apr = apr;
	}
	
	public int getRepaymentType(){
		return repaymentType;
	}

	public void setRepaymentType(int repaymentType){
		this.repaymentType = repaymentType;
	}
	
	public double getTotalProfit(){
		return totalProfit;
	}

	public void setTotalProfit(double totalProfit){
		this.totalProfit = totalProfit;
	}
	
	public double getProfitYet(){
		return profitYet;
	}

	public void setProfitYet(double profitYet){
		this.profitYet = profitYet;
	}
	
	public double getDiscount(){
		return discount;
	}

	public void setDiscount(double discount){
		this.discount = discount;
	}
	
	public double getCollectProfit(){
		return collectProfit;
	}
	
	public void setCollectProfit(double collectProfit){
		this.collectProfit = collectProfit;
	}

	public String getProtrcolUrl(){
		return protrcolUrl;
	}
	
	public void setProtrcolUrl(String protrcolUrl){
		this.protrcolUrl = protrcolUrl;
	}

	public List<AccountInvestRecordDetailCollectionItemModel> getCollections() {
		return collections;
	}

	public void setCollections(List<AccountInvestRecordDetailCollectionItemModel> collections) {
		this.collections = collections;
	}
	
	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}
}
