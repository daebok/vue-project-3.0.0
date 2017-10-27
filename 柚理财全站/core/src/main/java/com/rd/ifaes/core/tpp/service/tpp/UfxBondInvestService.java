/**
 * 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.tpp.service.tpp;

import com.rd.ifaes.core.bond.model.BondInvestOtherModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxCreditTransferModel;

/**
 * 债权投资业务逻辑处理类
 * 
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年8月21日
 */
public interface UfxBondInvestService {

	/**
	 * 回调验签处理业务
	 * 
	 * @author QianPengZhan
	 * @date 2016年8月23日
	 * @param model
	 */
	String  doBondInvestValid(UfxCreditTransferModel model);

	/**
	 * 债权投资失败处理
	 * 
	 * @author QianPengZhan
	 * @date 2016年8月21日
	 */
	void doBondInvestFailHanlde(UfxCreditTransferModel model);

	/**
	 * 债权投资
	 * 
	 * @author QianPengZhan
	 * @date 2016年7月29日
	 * @param model
	 */
	void doBondInvestSuccessHandle(UfxCreditTransferModel model);
	
	
	/**
	 * 债权投资成功 处理其他业务
	 * @author QianPengZhan
	 * @date 2016年12月7日
	 * @param model
	 */
	void doBondInvestOtherService(final BondInvestOtherModel model);

}
