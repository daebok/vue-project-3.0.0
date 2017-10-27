package com.rd.ifaes.core.score.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.constant.CommonConstant;
import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.Constant;

/**
 * entity:ScoreGoods 积分商城商品
 * 
 * @author ywt
 * @version 3.0
 * @date 2017-2-17
 */
public class ScoreGoods extends BaseEntity<ScoreGoods>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4646969738583876719L;
	/**商品名称*/
	private String goodsName;
	/**商品分类Id*/
	private String goodsCategoryId;
	/**商品单人限制兑换数量*/
	private int exchangeLimit;
	/**商品状态  0 待审核 1 审核成功   2 审核失败  3 已上架   4 已下架*/
	private String status;
	/**商品上架时间*/
	private Date saleTime;
	/**商品创建时间*/
	private Date createTime;
	/**商品创建者ID*/
	private String createOperatorId;
	/**商品修改时间*/
	private Date updateTime;
	/**商品修改者ID*/
	private String updateOperatorId;
	/**实际采购价格*/
	private BigDecimal costPrice;
	/**商品市场价*/
	private BigDecimal marketPrice;
	/**商品兑换所需的积分*/
	private int score;
	/**商品详情*/
	private String content;
	/**商品属性信息*/
	private String goodsAttribute;
	/**是否虚拟商品 1 是 0 否 默认0*/
	private String isVirtual;
	/**备注*/
	private String remark;
	/**审核人ID*/
	private String verifyOperatorId;
	/**审核备注*/
	private String verifyRemark;
	/**审核时间*/
	private Date verifyTime;
	/**是否推荐 1-推荐 0-不推荐*/
	private String recommend;
	/**虚拟物品所对应的规则Id*/
	private String ruleId;
	/**虚拟类型：0 红包 1加息卷*/
	private String virtualType;
	/** 供货商 */
	private String supplier ;
	/** 文字备注 */
	private String notes ;
	
	
	/**其他自定义属性*/
	/**商品分类名称*/
	private String goodsCategoryName;
	/**图片路径*/
	private String picPaths;
	/** 缩略图**/
	private String picSmall;
	/** 商品剩余数量**/
	private int lessNum;
	/** 积分范围查询  01-1000以下  02-1000~4999 03-5000~9999 04-10000以上**/
	private String queryScoreType;
	/** 积分最小值 **/
	private int scoreMin;
	/** 积分最大值 **/
	private int scoreMax;
	/** 商品库存 **/
	private int store;
	
	public int getStore() {
		return store;
	}
	public void setStore(int store) {
		this.store = store;
	}
	public String getQueryScoreType() {
		return queryScoreType;
	}
	public void setQueryScoreType(String queryScoreType) {
		this.queryScoreType = queryScoreType;
	}
	public int getLessNum() {
		return lessNum;
	}
	public void setLessNum(int lessNum) {
		this.lessNum = lessNum;
	}
	public int getScoreMin() {
		return scoreMin;
	}
	public void setScoreMin(int scoreMin) {
		this.scoreMin = scoreMin;
	}
	public int getScoreMax() {
		return scoreMax;
	}
	public void setScoreMax(int scoreMax) {
		this.scoreMax = scoreMax;
	}
	public String getPicSmall() {
		return picSmall;
	}
	public void setPicSmall(String picSmall) {
		this.picSmall = picSmall;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsCategoryId() {
		return goodsCategoryId;
	}
	public void setGoodsCategoryId(String goodsCategoryId) {
		this.goodsCategoryId = goodsCategoryId;
	}
	public int getExchangeLimit() {
		return exchangeLimit;
	}
	public void setExchangeLimit(int exchangeLimit) {
		this.exchangeLimit = exchangeLimit;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getSaleTime() {
		return saleTime;
	}
	public void setSaleTime(Date saleTime) {
		this.saleTime = saleTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateOperatorId() {
		return createOperatorId;
	}
	public void setCreateOperatorId(String createOperatorId) {
		this.createOperatorId = createOperatorId;
	}
	public BigDecimal getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}
	public BigDecimal getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getGoodsAttribute() {
		return goodsAttribute;
	}
	public void setGoodsAttribute(String goodsAttribute) {
		this.goodsAttribute = goodsAttribute;
	}
	public String getIsVirtual() {
		return isVirtual;
	}
	public void setIsVirtual(String isVirtual) {
		this.isVirtual = isVirtual;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getVerifyOperatorId() {
		return verifyOperatorId;
	}
	public void setVerifyOperatorId(String verifyOperatorId) {
		this.verifyOperatorId = verifyOperatorId;
	}
	public String getVerifyRemark() {
		return verifyRemark;
	}
	public void setVerifyRemark(String verifyRemark) {
		this.verifyRemark = verifyRemark;
	}
	public Date getVerifyTime() {
		return verifyTime;
	}
	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
	}
	public String getRecommend() {
		return recommend;
	}
	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getUpdateOperatorId() {
		return updateOperatorId;
	}
	public void setUpdateOperatorId(String updateOperatorId) {
		this.updateOperatorId = updateOperatorId;
	}
	public String getGoodsCategoryName() {
		return goodsCategoryName;
	}
	public void setGoodsCategoryName(String goodsCategoryName) {
		this.goodsCategoryName = goodsCategoryName;
	}
	public String getPicPaths() {
		return picPaths;
	}
	public void setPicPaths(String picPaths) {
		this.picPaths = picPaths;
	}
	
	public String getRuleId() {
		return ruleId;
	}
	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}
	public String getVirtualType() {
		return virtualType;
	}
	public void setVirtualType(String virtualType) {
		this.virtualType = virtualType;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	/**
	 * 根据类型设置积分查询区间
	 * @author ywt
	 * @date 2017-03-10
	 */
	public void putQueryScoreSection(){
		if (StringUtils.isNotBlank(this.getQueryScoreType()) && this.getScoreMax() == Constant.INT_ZERO && this.getScoreMin() == Constant.INT_ZERO) {
			int min=Constant.INT_ZERO;
			int max=Constant.INT_ZERO;
			if (CommonConstant.SCORE_TYPE_ONE.equals(this.getQueryScoreType())) {
				max=Constant.INT_ONE_THOUSAND;
			}else if (CommonConstant.SCORE_TYPE_TWO.equals(this.getQueryScoreType())) {
				min=Constant.INT_ONE_THOUSAND;
				max=Constant.INT_FIVE_THOUSAND;
			}else if (CommonConstant.SCORE_TYPE_THREE.equals(this.getQueryScoreType())) {
				min=Constant.INT_FIVE_THOUSAND;
				max=Constant.INT_TEN_THOUSAND;
			}else if (CommonConstant.SCORE_TYPE_FOUR.equals(this.getQueryScoreType())) {
				min=Constant.INT_TEN_THOUSAND;
			}
			this.setScoreMin(min);
			this.setScoreMax(max);
		}
	}
}
