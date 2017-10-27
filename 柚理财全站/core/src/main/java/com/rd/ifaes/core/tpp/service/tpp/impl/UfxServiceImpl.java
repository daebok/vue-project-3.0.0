package com.rd.ifaes.core.tpp.service.tpp.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TppUtil;
import com.rd.ifaes.core.account.model.AccountBankModel;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.tpp.model.ufx.UfxFreezeModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxQueryBankCardModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxUnFreezeModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxUserBankCardJsonModel;
import com.rd.ifaes.core.tpp.service.TppService;
import com.rd.ifaes.core.tpp.service.tpp.UfxService;

/**
 * 
 *  UFX业务处理类
 * @version 3.0
 * @author xhf
 * @date 2016年8月26日
 */
@Service("ufxService") 
@Transactional
public class UfxServiceImpl implements UfxService {
	
	@Override
	public List<AccountBankModel> tppQueryBank(final UfxModel tppModel) {
		final List<AccountBankModel> list = new ArrayList<AccountBankModel>();
		//参数封装
		final Map<String,Object> map = new HashMap<String,Object>();
		map.put("userCustId", tppModel.getTppUserCustId());
		map.put("userId", tppModel.getUserNo());
		//调用查询接口
		TppService tppService = (TppService)TppUtil.tppService();
		final UfxQueryBankCardModel cardModel = (UfxQueryBankCardModel)tppService.tppQueryUserBankCard(map);
		if (StringUtils.isNotBlank(cardModel.getUserCardList())) {
			final List<UfxUserBankCardJsonModel> cardJsonList = JSONArray.parseArray(cardModel.getUserCardList(), UfxUserBankCardJsonModel.class);
			if (!cardJsonList.isEmpty()) {
				for (UfxUserBankCardJsonModel model : cardJsonList) {
					AccountBankModel bankModel = new AccountBankModel();
					bankModel.setBankCode(model.getBankCode());
					bankModel.setBankNo(model.getCardId());
					bankModel.setPicPath(Constant.PIC_PATH_BANK + model.getBankCode() + ".png");
					bankModel.setBankName(model.getBankName());
					bankModel.setCanDisable(UfxUserBankCardJsonModel.CAN_DISABLE.equals(model.getCanDisable()));
					bankModel.setFastPayFlag(model.getFastPayFlag());
					list.add(bankModel);
				}
			}
		}
		return list;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public UfxFreezeModel freeze(UfxModel model) {
		TppService tppService = (TppService)TppUtil.tppService();
		//参数封装
		final Map<String,Object> map = new HashMap<String,Object>();
		map.put("userCustId", model.getTppUserCustId());
		map.put("amount", model.getMoney());
		map.put("userId", model.getUserId());
		map.put("freezeType", model.getFreezeType());
		map.put("projectId", model.getBidNo());
		final UfxFreezeModel freezeModel = (UfxFreezeModel)tppService.tppFreeze(map);
		return freezeModel;
	}

	@Override
	public UfxUnFreezeModel unFreeze(UfxModel model) {
		TppService tppService = (TppService)TppUtil.tppService();
		//参数封装
		final Map<String,Object> map = new HashMap<String,Object>();
		map.put("freezeNo", model.getTrxId());
		map.put("userCustId", model.getTppUserCustId());
		map.put("amount", model.getMoney());
		map.put("userId", model.getUserId());
		map.put("projectId", model.getBidNo());
		map.put("unFreezeType", model.getFreezeType());
		final UfxUnFreezeModel unFreezeModel = (UfxUnFreezeModel)tppService.tppUnFreeze(map);;
		return unFreezeModel;
	}

}
