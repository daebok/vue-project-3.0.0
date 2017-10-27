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

/**
 * 绑定银行卡
 * @author xhf
 * @version 2.3.0.0
 * @date 2018年8月18日 下午3:10:35
 */
@Controller
public class UfxBindBankCardController extends BaseController {
	
	/**
	 * 日志跟踪器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(UfxBindBankCardController.class);
	
	/**
	 * 绑定银行卡同步通知
	 * @return
	 */
	@RequestMapping("/ufx/bindBankCard/return")
	public String tppReturn(final Model model,final HttpServletRequest request,final HttpServletResponse response){
		LOGGER.info("绑定银行卡同步回调通知:{}" , getRequestParams(request));
		String userAgent = request.getHeader("User-Agent").toLowerCase();//判断访问来源 是否是移动端
		if(userAgent.contains("micromessenger")||userAgent.contains("android")||userAgent.contains("iphone")){//移动端
					model.addAttribute("res_msg", "bindBankSuccess");
					model.addAttribute("res_code", "bindBank_Success");
					return "/mobileResult";
		}else{
		 model.addAttribute("picType", CallbackConstant.BANK_CARD_PICTYPE);   
		 model.addAttribute("requestType", CallbackConstant.BANK_CARD_REQUEST_TYPE);            
		 model.addAttribute("resultName",  CallbackConstant.BANK_CARD_REQUEST_NAME); 
		 model.addAttribute("resultUrl", CallbackConstant.BANK_CARD_REQUEST_URL); 
		return "/retResult";
		}
	}
}
