package com.rd.ifaes.core.tpp.service.tpp;

import com.rd.ifaes.common.rabbit.model.MqAutoInvestModel;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbBackInvestModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxInvestFailModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxInvestModel;
/**
 * UFX投资业务处理
 * @author  FangJun
 * @date 2016年7月13日
 */
public interface UfxInvestService {
	
	/**
	 * 投资回调处理(controller)
	 * 
	 * @param model
	 */
	void ufxInvestHandle(UfxInvestModel model);

	/**
	 * 投资成功处理（队列）
	 * 
	 * @param model
	 */
	void ufxInvestSuccess(UfxInvestModel model);

	/**
	 * 投资支付失败处理（队列）
	 * 
	 * @param model
	 */
	void investPayFail(UfxInvestModel model);

	/**
	 * 投资超时后支付成功处理（队列）
	 * 
	 * @param model
	 */
	void ufxInvestTimeout(UfxInvestModel model);

	/**
	 * 投资失败退回资金，回调处理
	 * 
	 * @param model
	 */
	void ufxInvestFailHandle(UfxInvestFailModel model);

	/**
	 * 自动投资
	 * 
	 * @author zb
	 * @date 2016年8月6日
	 * @param model
	 */
	boolean doAutoInvest(MqAutoInvestModel model);

	/**
	 * 渤海银行投资回调成功处理
	 * @param cbhbModel
	 * @param invest 
	 */
	void cbhbInvestSuccess(CbhbBackInvestModel cbhbModel, ProjectInvest invest);
}
