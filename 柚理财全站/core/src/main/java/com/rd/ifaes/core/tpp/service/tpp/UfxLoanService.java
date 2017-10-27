/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.tpp.service.tpp;

import com.rd.ifaes.core.tpp.domain.TppTrade;
import com.rd.ifaes.core.tpp.model.ufx.UfxLoansModel;

/**
 * 放款回调处理
 * @version 3.0
 * @author FangJun
 * @date 2016年10月14日
 */
public interface UfxLoanService {

	/**
	 * 放款回调处理(controller)
	 * @param model
	 */
	void ufxLoanHandle(UfxLoansModel model);

	/**
	 * 放款成功
	 * @author FangJun
	 * @date 2016年11月3日
	 * @param trade 放款报文记录
	 */
	void successLoanHandle(TppTrade trade);

}
