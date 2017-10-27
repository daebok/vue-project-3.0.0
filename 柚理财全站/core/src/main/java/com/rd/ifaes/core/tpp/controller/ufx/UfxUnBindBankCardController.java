package com.rd.ifaes.core.tpp.controller.ufx;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.core.constant.CallbackConstant;
import com.rd.ifaes.core.tpp.model.ufx.UfxUnBindBankCardModel;

/**
 * 解绑银行卡
 * @author xhf
 * @version 3.0
 * @date 2016年1月6日 下午4:42:54
 * Copyright 杭州融都科技股份有限公司   All Rights Reserved
 * 官方网站：www.erongdu.com
 * 研发中心：rdc@erongdu.com
 * 未经授权不得进行修改、复制、出售及商业使用
 */
@Controller
public class UfxUnBindBankCardController extends BaseController {

	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(UfxUnBindBankCardController.class);
	
	/**
	 * 解绑银行卡同步通知
	 * @return
	 */
	@RequestMapping("/ufx/unBindBankCard/return")
	public String tppReturn(final UfxUnBindBankCardModel ufxModel, final HttpServletRequest request, final Model model) {
		LOGGER.info("解绑银行卡同步回调通知:{}" , getRequestParams(request));
		 
		 String userAgent = request.getHeader("User-Agent").toLowerCase();//判断访问来源 是否是移动端
		 if(userAgent.contains("micromessenger")||userAgent.contains("android")||userAgent.contains("iphone")){//移动端
						model.addAttribute("res_msg", "解绑成功");
						model.addAttribute("res_code", "delBindBank_success");
						return "/mobileResult";
			}else{
		 model.addAttribute("picType", CallbackConstant.BANK_CARD_PICTYPE);   
		 model.addAttribute("requestType", CallbackConstant.BANK_CARD_REQUEST_TYPE_UN_BIND);            
		 model.addAttribute("resultName",  CallbackConstant.BANK_CARD_REQUEST_NAME); 
		 model.addAttribute("resultUrl", CallbackConstant.BANK_CARD_REQUEST_URL); 
		return "/retResult";
			}
	}
	
	/**
	 * 解绑银行卡异步通知
	 * @return
	 */
	@RequestMapping("/ufx/unBindBankCard/notify")
	public void tppNotify(final UfxUnBindBankCardModel model,final HttpServletRequest request,final HttpServletResponse response){
		LOGGER.info("解绑银行卡异步回调通知:{}" , getRequestParams(request));
		printSuccessReturn(response);
	}
}
