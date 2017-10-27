package com.rd.ifaes.core.tpp.controller.ufx;

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
import com.rd.ifaes.core.tpp.model.ufx.UfxCashModel;

/**
 * 
 *  商户取现回调
 * @version 3.0
 * @author jxx
 * @date 2016年7月26日
 */
@Controller
public class UfxMerchantCashController  extends BaseController{

	private static final Logger LOGGER = LoggerFactory.getLogger(UfxMerchantCashController.class);
	@Resource
	private TppMerchantLogService tppMerchantLogService;
	
	/**
	 * 
	 * 商户取现同步回调
	 * @author jxx
	 * @date 2016年7月26日
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/ufx/merchantCash/return")
	public String tppReturn(UfxCashModel ufxModel, final HttpServletRequest request, final Model model) {
		LOGGER.info("进入商户取现同步回调处理，返回参数:{}",getRequestParams(request));
		
		String msg = ResourceUtils.get(ResourceConstant.CASH_SUCCESS);
		if (!tppMerchantLogService.doMerchantCash(ufxModel)) {
			msg = ResourceUtils.get(ResourceConstant.CASH_FAIL);//这里没有定义
		}
		 model.addAttribute("r_msg", msg);
		return "/retResult";
	}
	
	@RequestMapping("/ufx/merchantCash/notify")
	public void tppNotify(UfxCashModel model,final HttpServletRequest request,final HttpServletResponse response){
		LOGGER.debug("取现进入异步回调，回调参数：{}" , getRequestParams(request));
		tppMerchantLogService.doMerchantCash(model);
		printSuccessReturn(response);
	}
	
}
