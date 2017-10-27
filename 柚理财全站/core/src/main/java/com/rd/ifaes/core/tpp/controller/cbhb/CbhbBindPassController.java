/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.tpp.controller.cbhb;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.core.constant.CallbackConstant;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbBindPassModel;

/**
 * 修改找回支付密码
 * @version 3.0
 * @author QianPengZhan
 * @date 2017年2月24日
 */
@Controller
public class CbhbBindPassController extends BaseController{
	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CbhbBindPassController.class);
	
	@RequestMapping(value="/cbhb/bindPass/return")
	public String CbhbBindPassReturn(final CbhbBindPassModel cbhbModel, final HttpServletRequest request, final Model model){
		LOGGER.info("BindPassReturn同步回调业务"+this.getRequestMap(request).toString());
		cbhbModel.setResponseType(CbhbConstant.CBHB_RESPONSE_TYPE_RETURN);
		cbhbModel.response(this.getRequestMap(request));
		if(cbhbModel.validSign(cbhbModel)){
			LOGGER.info("验签通过");
		}
		model.addAttribute("resultUrl",CallbackConstant.BINDPASS_REQUEST_URL);
		model.addAttribute("picType", CallbackConstant.BINDPASS_PICTYPE);
		model.addAttribute("requestType",CallbackConstant.BINDPASS_REQUEST_TYPE);
		model.addAttribute("resultName", CallbackConstant.BINDPASS_REQUEST_NAME);
		return "/retResult";
	}
	
	@RequestMapping(value="/cbhb/bindPass/notify")
	@ResponseBody
	public void CbhbBindPassNotify(final CbhbBindPassModel cbhbModel,final HttpServletResponse response,final HttpServletRequest request){
		LOGGER.info("BindPassNotity异步回调业务"+this.getRequestMap(request).toString());
		cbhbModel.setResponseType(CbhbConstant.CBHB_RESPONSE_TYPE_NOTIFY);
		cbhbModel.response(this.getRequestMap(request));
		if(cbhbModel.validSign(cbhbModel)){
			LOGGER.info("验签通过");
		}
		cbhbModel.printSuccess500Return(response); 
	}
}
