package com.rd.ifaes.core.tpp.service.tpp;

import com.rd.ifaes.core.tpp.model.ufx.UfxCashAuditModel;

/**
 * 取现复核
 * @author xhf
 *
 */
public interface UfxCashAuditService  {
	/**
	 * 取现复核业务处理
	 * @param cashModel
	 * @param cash
	 */
	void doTppCashAuditService(UfxCashAuditModel model);
	
	/**
	 * 取现复核回调业务
	 * @param resultFlag
	 */
	void doCashAudit(UfxCashAuditModel model); 
}
