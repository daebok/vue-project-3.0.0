/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.tpp.controller.cbhb;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.dict.CacheKeys;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.account.service.AccountBankCardService;
import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.core.constant.CallbackConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbBindCardWebModel;

/**
 * 渤海银行修改绑定银行卡的控制层
 * @version 3.0
 * @author QianPengZhan
 * @date 2017年2月23日
 */
@Controller
public class CbhbBindCardWebController extends BaseController{
	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CbhbRealNameWebController.class);
	
	@Resource
	private transient AccountBankCardService accountBankCardService;
	
	@RequestMapping(value="/cbhb/bindCardWeb/return")
	public String CbhbBindCardWebReturn(final CbhbBindCardWebModel cardModel,final HttpServletRequest request,final Model model){
		LOGGER.info("渤海银行---bindCardWeb进入同步回调，回调参数：{}" , this.getRequestMap(request).toString());
		String msg = "", resultFlag = "";
		cardModel.setResponseType(CbhbConstant.CBHB_RESPONSE_TYPE_RETURN);//录入同步通知类型
		cardModel.response(this.getRequestMap(request));
		if (cardModel.validSign(cardModel)) { // 验签
			if (!CbhbConstant.CBHB_CODE_SUCCESS.equals(cardModel.getRespCode())) {
				msg = "修改银行卡失败。";
				if (StringUtils.isNotBlank(cardModel.getRespDesc())) {
					msg = cardModel.getRespDesc();
				}
			} else {
				resultFlag = String.format(CacheKeys.PREFIX_TPP_ORDER_HANDLE_KEY.getKey(),cardModel.getMerBillNo());
			}
		} else {
			msg = ResourceUtils.get(ResourceConstant.TPP_NOTIFY_SIGN_FAIL);
		}
		//accountBankCardService.bindCardWebHandle(cardModel);  同步只处理状态
		
		model.addAttribute("msg", msg);
		model.addAttribute("resultFlag", resultFlag);
		model.addAttribute("resultUrl", CallbackConstant.BIND_CARD_REQUEST_URL);
		model.addAttribute("picType", CallbackConstant.BIND_CARD_PICTYPE);
		model.addAttribute("requestType", CallbackConstant.BIND_CARD_REQUEST_TYPE);
		model.addAttribute("resultName", CallbackConstant.BIND_CARD_REQUEST_NAME);
		return "/retResult";
	}
	
	@RequestMapping(value="/cbhb/bindCardWeb/notify")
	@ResponseBody
	public void CbhbBindCardWebNotify(final CbhbBindCardWebModel cardModel,
			final HttpServletRequest request,final HttpServletResponse response){
		LOGGER.info("渤海银行---bindCardWeb进入异步回调，回调参数：{}" , getRequestMap(request).toString());
		cardModel.setResponseType(CbhbConstant.CBHB_RESPONSE_TYPE_NOTIFY);//录入同步通知类型
		cardModel.response(this.getRequestMap(request));
		accountBankCardService.bindCardWebHandle(cardModel);//异步处理业务
		cardModel.printSuccess500Return(response);
	}
}
