package com.rd.ifaes.core.tpp.service.tpp;

import com.rd.ifaes.core.account.model.CashModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxCashModel;

/**
 * 取现
 * @author xhf
 *
 */
public interface UfxCashService  {
	/**
	 * 汇付取现业务处理
	 * @param cashModel
	 * @param cash
	 */
	void doTppCashService(CashModel cashModel);
	
	/**
	 * 取现异步银行处理结果回调
	 * @param cashModel
	 */
	void doCashBankReturn(CashModel cashModel);
	
	/**
	 * 处理取现回调业务
	 * @param resultFlag
	 */
	void doCash(UfxCashModel model); 
	
}
