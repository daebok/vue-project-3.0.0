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
import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.core.constant.CallbackConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbBindMobileNoModel;
import com.rd.ifaes.core.user.service.UserService;

/**
 * 修改手机号  回调控制层
 * @version 3.0
 * @author QianPengZhan
 * @date 2017年2月23日
 */
@Controller
public class CbhbBindMobileWebController extends BaseController {
	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CbhbBindMobileWebController.class);
	
	/**
	 * 用户业务
	 */
	@Resource
	private transient UserService userService;
	
	@RequestMapping(value="/cbhb/bindMobileWeb/return")
	public String CbhbBindCardWebReturn(final CbhbBindMobileNoModel mobileModel,final HttpServletRequest request,final Model model){
		LOGGER.info("bindMobileWeb同步回调业务");
		String msg = "", resultFlag = "";
		mobileModel.setResponseType(CbhbConstant.CBHB_RESPONSE_TYPE_RETURN);
		mobileModel.response(this.getRequestMap(request));
		if (mobileModel.validSign(mobileModel)) { // 验签
			if (!CbhbConstant.CBHB_CODE_SUCCESS.equals(mobileModel.getRespCode())) {
				msg = "修改手机号失败。";
				if (StringUtils.isNotBlank(mobileModel.getRespDesc())) {
					msg = mobileModel.getRespDesc();
				}
			} else {
				resultFlag = String.format(CacheKeys.PREFIX_TPP_ORDER_HANDLE_KEY.getKey(),mobileModel.getMerBillNo());
			}
		} else {
			msg = ResourceUtils.get(ResourceConstant.TPP_NOTIFY_SIGN_FAIL);
		}
		model.addAttribute("msg", msg);
		model.addAttribute("resultFlag", resultFlag);
		model.addAttribute("resultUrl", CallbackConstant.BIND_MOBILE_REQUEST_URL);
		model.addAttribute("picType", CallbackConstant.BIND_MOBILE_PICTYPE);
		model.addAttribute("requestType", CallbackConstant.BIND_MOBILE_REQUEST_TYPE);
		model.addAttribute("resultName", CallbackConstant.BIND_MOBILE_REQUEST_NAME);
		return "/retResult";
	}
	
	@RequestMapping(value="/cbhb/bindMobileWeb/notify")
	@ResponseBody
	public void CbhbBindCardWebNotify(final CbhbBindMobileNoModel mobileModel,
			final HttpServletRequest request,final HttpServletResponse response){
		LOGGER.info("bindMobileWeb异步回调业务");
		mobileModel.setResponseType(CbhbConstant.CBHB_RESPONSE_TYPE_NOTIFY);
		mobileModel.response(this.getRequestMap(request));
		userService.cbhbUpdatePhone(mobileModel);
		mobileModel.printSuccess500Return(response);
	}
}
