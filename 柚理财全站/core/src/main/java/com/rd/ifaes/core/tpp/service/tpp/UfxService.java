package com.rd.ifaes.core.tpp.service.tpp;

import java.util.List;

import com.rd.ifaes.core.account.model.AccountBankModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxFreezeModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxUnFreezeModel;

/**
 * 
 * UFX同步业务处理类
 * @version 3.0
 * @author xhf
 * @date 2016年8月26日
 */
public interface UfxService {
	
	/**
	 * 查询银行卡列表
	 * @param tppModel
	 * @return
	 */
	List<AccountBankModel> tppQueryBank(UfxModel tppModel);

	/**
	 * 冻结资金
	 * @param model
	 * @return
	 */
	UfxFreezeModel freeze(UfxModel model);

	/**
	 * 解冻资金
	 * @param model
	 * @return
	 */
	UfxUnFreezeModel unFreeze(UfxModel model);
}
