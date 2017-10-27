package com.rd.ifaes.core.project.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.project.domain.RealizeFreeze;
import com.rd.ifaes.core.project.mapper.RealizeFreezeMapper;
import com.rd.ifaes.core.project.service.RealizeFreezeService;

/**
 * 变现冻结信息
 * @version 3.0
 * @author fxl
 * @date 2016年8月1日
 */
@Service("realizeFreezeService") 
public class RealizeFreezeServiceImpl  extends BaseServiceImpl<RealizeFreezeMapper, RealizeFreeze> implements RealizeFreezeService{

	@Override
	public BigDecimal getSumFreezeMoney(String userId, String collectionId, String freezeType) {
		return dao.getSumFreezeMoney(userId,collectionId,freezeType) ;
	}

	@Override
	public List<RealizeFreeze> getFreezeListByCollection(String collectionId) {
		return dao.getFreezeListByCollection(collectionId) ;
	}
	
	@Override
	public void updateFreeze(String collectionId,String realizeId,String freezeType,String freezeNo,String orderNo) {
		dao.updateFreeze(collectionId,realizeId,freezeType,freezeNo,orderNo);
	}
	
	@Override
	public void cancelFreeze(String realizeId) {
		dao.cancelFreeze(realizeId);
	}
	
	@Override
	public List<RealizeFreeze> getFreezedListByRealizeId(String realizeId,String status){
		return dao.getFreezedListByRealizeId(realizeId,status);
	}
	
}