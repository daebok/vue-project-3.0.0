package com.rd.ifaes.core.tpp.controller.ufx;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rd.ifaes.core.account.service.TppMerchantLogService;
import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.tpp.model.ufx.UfxRechargeModel;

/**
 * 
 *  商户充值回调
 * @version 3.0
 * @author jxx
 * @date 2016年7月26日
 */
@Controller
public class UfxMerchantRechargeController  extends BaseController{

	private static final Logger LOGGER = LoggerFactory.getLogger(UfxMerchantRechargeController.class);
	@Resource
	private TppMerchantLogService tppMerchantLogService;

	/**
	 * 
	 * 商户充值同回调
	 * @author jxx
	 * @date 2016年7月26日
	 * @return
	 */
	@RequestMapping("/ufx/merchantRecharge/return")
	public String tppReturn(UfxRechargeModel ufxModel, final HttpServletRequest request, final Model model) {
		LOGGER.info("进入商户充值同步回调处理，返回参数:{}",getRequestParams(request));
		String msg = ResourceUtils.get(ResourceConstant.RECHARGE_SUCCESS);
		if (!tppMerchantLogService.doMerchantRecharge(ufxModel)) {
			msg = ResourceUtils.get(ResourceConstant.RECHARGE_FAIL);
		}
		 model.addAttribute("r_msg", msg);
		return "/retResult";
	}
	
	/**
	 * 
	 * 商户充值异步回调
	 * @author jxx
	 * @date 2016年7月26日
	 * @throws IOException
	 */
	@RequestMapping("/ufx/merchantRecharge/notify")
	public void tppNotify(UfxRechargeModel model,final HttpServletRequest request,final HttpServletResponse response){
		LOGGER.info("进入商户充值异步回调处理，返回参数：{}",getRequestParams(request));
		tppMerchantLogService.doMerchantRecharge(model);
		printSuccessReturn(response);
	} 
	
}
