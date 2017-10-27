package com.rd.ifaes.core.score.model;

import com.rd.ifaes.core.score.domain.ScoreGoodsStore;

public class ScoreGoodsStoreModel extends ScoreGoodsStore{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7319586159264188794L;
	public ScoreGoodsStoreModel(){
		
	}
	public ScoreGoodsStoreModel(String goodsId, int freezeStore, int soldAmount) {
		this.setGoodsId(goodsId);
		this.setFreezeStore(freezeStore);
		this.setSoldAmount(soldAmount);
	}
}
