package com.rd.ifaes.core.score.domain;

import org.apache.commons.lang3.StringUtils;

import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.core.core.util.DictUtils;

/**
 * entity:ScoreGoodsStore 积分商城商品库存表
 * 
 * @author ywt
 * @version 3.0
 * @date 2017-2-20
 */
public class ScoreGoodsStore extends BaseEntity<ScoreGoodsStore>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 481320855100398423L;
	/**商品id*/
	private String goodsId;
	/**商品库存*/
	private int store;
	/**冻结库存*/
	private int freezeStore;
	/**销售数量*/
	private int soldAmount;
	
	/**其他自定义属性*/
	private String goodsName; //产品名称
	private String status;    //产品状态
	
	/**
	 * 默认构造方法
	 */
	public ScoreGoodsStore(){
		
	}
	
	/**
	 * 
	 * @param goodsId 商品id
	 * @param freezeStore 冻结库存
	 * @param soldAmount  已售数量
	 */
	public ScoreGoodsStore(String goodsId,int freezeStore,int soldAmount){
		this.goodsId = goodsId;
		this.freezeStore = freezeStore;
		this.soldAmount = soldAmount;
	}
	
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public int getStore() {
		return store;
	}
	public void setStore(int store) {
		this.store = store;
	}
	public int getFreezeStore() {
		return freezeStore;
	}
	public void setFreezeStore(int freezeStore) {
		this.freezeStore = freezeStore;
	}
	public int getSoldAmount() {
		return soldAmount;
	}
	public void setSoldAmount(int soldAmount) {
		this.soldAmount = soldAmount;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 获得状态显示说明值
	 * @return
	 */
	public String getStatusStr() {
		String statusStr = "";
		if(StringUtils.isNotBlank(getStatus())){
			statusStr = DictUtils.getItemName(DictTypeEnum.GOODS_STATUS.getValue(), getStatus());
		}
		return statusStr;
	}

}
