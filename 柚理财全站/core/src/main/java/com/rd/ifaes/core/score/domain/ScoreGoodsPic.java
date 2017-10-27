package com.rd.ifaes.core.score.domain;

import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * entity:ScoreGoodsPic  商品图片详情表
 * 
 * @author ywt
 * @version 3.0
 * @date 2017-2-20
 */
public class ScoreGoodsPic extends BaseEntity<ScoreGoodsPic>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8117600624256102178L;
	/**商品id*/
	private String goodsId;
	/**图片类型 1 缩略图 2  实体照片*/
	private String picType;
	/**图片路径*/
	private String picPath;
	/**创建日期*/
	private Date createTime;
	
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getPicType() {
		return picType;
	}
	public void setPicType(String picType) {
		this.picType = picType;
	}
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
}
