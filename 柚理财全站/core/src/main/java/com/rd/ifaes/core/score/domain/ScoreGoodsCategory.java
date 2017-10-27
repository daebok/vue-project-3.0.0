package com.rd.ifaes.core.score.domain;

import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * entity:ScoreGoodsCategory 积分商城商品分类
 * 
 * @author ywt
 * @version 3.0
 * @date 2017-2-17
 */
public class ScoreGoodsCategory extends BaseEntity<ScoreGoodsCategory>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 623687842778984998L;
	
	/**商品分类名称*/
	private String goodsCategoryName;
	/**商品分类图片展示*/
	private String introPic;
	/**商品分类排序值*/
	private int sort;
	/**商品分类备注*/
	private String remark;
	/**操作员Id*/
	private String operator;
	/**商品创建时间*/
	private Date createTime;
	/**是否删除 1 是 0否，默认0*/
	private String deleteFlag;
	
	public String getGoodsCategoryName() {
		return goodsCategoryName;
	}
	public void setGoodsCategoryName(String goodsCategoryName) {
		this.goodsCategoryName = goodsCategoryName;
	}
	public String getIntroPic() {
		return introPic;
	}
	public void setIntroPic(String introPic) {
		this.introPic = introPic;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	
	
}
