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
import com.rd.ifaes.core.account.domain.Cash;
import com.rd.ifaes.core.account.service.CashService;
import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.core.constant.CallbackConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.tpp.exception.CbhbException;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbDrawingsModel;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.service.UserCacheService;

/**
 * 渤海银行  提现回调类
 * @version 3.0
 * @author QianPengZhan
 * @date 2017年2月28日
 */
@Controller
public class CbhbDrawingsController extends BaseController{
	
	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CbhbDrawingsController.class);
	
	/**
	 * 提现业务
	 */
	@Resource
	private transient CashService cashService;
	
	/**
	 * 用户附属信息类
	 */
	@Resource
	private transient UserCacheService userCacheService;
	
	@RequestMapping(value="/cbhb/drawings/return")
	public String CbhbDrawingsReturn(final CbhbDrawingsModel cbhbModel, final HttpServletRequest request, final Model model){
		LOGGER.info("bindMobileWeb同步回调业务"+this.getRequestMap(request).toString());
		LOGGER.info("取现({})进入同步回调",cbhbModel.getMerBillNo());
		cbhbModel.setResponseType(CbhbConstant.CBHB_RESPONSE_TYPE_RETURN);
		cbhbModel.response(this.getRequestMap(request));
		final Cash cash = cashService.findByOrderNo(cbhbModel.getMerBillNo());
	    String userAgent = request.getHeader("User-Agent").toLowerCase();//判断访问来源 是否是移动端
		if (userAgent.contains("micromessenger")
				|| userAgent.contains("android")
				|| userAgent.contains("iphone")) {// 移动端
			if (null == cash) {// 失败
				model.addAttribute("r_msg",ResourceUtils.get(ResourceConstant.ORDER_NOT_EXIST));
				model.addAttribute("res_code", "register_fail");
				return "/mobileRetresult";
			} else {// 成功
				model.addAttribute("res_msg", "提现处理完成");
				model.addAttribute("res_code", "cash_success");
				return "/mobileResult";
			}
		} else {
			if (null == cash) {
				throw new CbhbException(ResourceUtils.get(ResourceConstant.ORDER_NOT_EXIST));
			}
			cashService.doCbhbCashNotify(cbhbModel);
			// 担保机构单独跳转页面
			UserCache userCache = userCacheService.findByUserId(cash.getUserId());
			if (userCache != null && UserCache.USER_NATURE_VOUCH.equals(userCache.getUserNature())) {
				model.addAttribute("resultUrl",CallbackConstant.CASH_VOUCH_REQUEST_URL);
			} else {
				model.addAttribute("resultUrl",CallbackConstant.CASH_REQUEST_URL);
			}
			model.addAttribute("picType", CallbackConstant.CASH_PICTYPE);
			model.addAttribute("requestType",CallbackConstant.CASH_REQUEST_TYPE);
			model.addAttribute("resultName", CallbackConstant.CASH_REQUEST_NAME);
			return "/retResult";
		}
	}
	
	@RequestMapping(value="/cbhb/drawings/notify")
	@ResponseBody
	public void CbhbDrawingsNotify(final CbhbDrawingsModel cbhbModel,final HttpServletResponse response, final HttpServletRequest request){
		LOGGER.info("bindMobileWeb异步回调业务"+this.getRequestMap(request).toString());
		LOGGER.info("取现({})进入异步回调",cbhbModel.getMerBillNo());
		cbhbModel.setResponseType(CbhbConstant.CBHB_RESPONSE_TYPE_NOTIFY);
		cbhbModel.response(this.getRequestMap(request));
		cashService.doCbhbCashNotify(cbhbModel);
		cbhbModel.printSuccess500Return(response); 
	}
}
