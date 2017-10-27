package com.rd.ifaes.core.tpp.service.tpp;

import com.rd.ifaes.core.tpp.model.ufx.TppRechargeModel;

/**
 * 
 * UFX充值业务处理
 * @version 3.0
 * @author xhf
 * @date 2016年8月26日
 */
public interface UfxRechargeService {
	
	/**
	 * 第三方充值回调处理
	 * @param rechargeModel
	 */
	void tppRechargeSucc(TppRechargeModel rechargeModel);
	
	/**
	 * 充值失败回调处理
	 * @param rechargeModel
	 */
	void tppRechargeFail(TppRechargeModel rechargeModel);

}
