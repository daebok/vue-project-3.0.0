/**
 * 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.tpp.controller.ufx;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rd.ifaes.common.constant.UfxConstant;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.bond.domain.BondInvest;
import com.rd.ifaes.core.bond.service.BondInvestService;
import com.rd.ifaes.core.core.constant.CallbackConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.constant.URLConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.tpp.model.ufx.UfxCreditTransferModel;
import com.rd.ifaes.core.tpp.service.tpp.UfxBondInvestService;

/**
 * 债权接口回调
 * 
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年7月29日
 */
@Controller
public class UfxBondController extends BaseController {
	/**
	 * 日志类
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(UfxBondController.class);

	/**
	 * 业务类
	 */
	@Resource
	private transient UfxBondInvestService ufxBondInvestService;
	/**
	 * BondInvestService业务类
	 */
	@Resource
	private transient BondInvestService bondInvestService;
	/**
	 * 债权投资同步回调
	 * 
	 * @author QianPengZhan
	 * @date 2016年7月29日
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ufx/bond/creditTransferReturn")
	public String creditTransferReturn(final UfxCreditTransferModel ufxModel, final HttpServletRequest request, final Model model) {
		LOGGER.info(DateUtils.getNow() + ":债权投资同步回调---回调状态码：{}----{}",ufxModel.getRespCode(),this.getRequestParams(request));
		final boolean ret = ufxModel.validSign(ufxModel);
		String errorMsg = null;// 错误信息
		String picType = CallbackConstant.PAY_PICTYPE;
		if (!ret) {
			// 验签失败
			errorMsg = ResourceUtils.get(ResourceConstant.TPP_NOTIFY_SIGN_FAIL);
			picType = CallbackConstant.ERROR_PICTYPE;
		} else if (!UfxConstant.UFX_CODE_SUCCESS.equals(ufxModel.getRespCode())) {
			// 三方处理出错
			errorMsg = ufxModel.getRespDesc();
			picType = CallbackConstant.ERROR_PICTYPE;
		} else {
			model.addAttribute("resultName", CallbackConstant.PAY_REQUEST_NAME);
			model.addAttribute("resultUrl", URLConstant.BOND_LIST_BOUGHT); // 点击“我的资产>我的投资”跳转页面地址
			String url = StringUtils.EMPTY;
			final BondInvest bondInvest = bondInvestService
					.getBondInvestByOrderNo(ufxModel.getOrderNo());
			if (bondInvest == null) {
				url = URLConstant.BOND_HOME_PAGE;
			} else {
				url = URLConstant.BOND_DETAIL_PAGE_PREFIX + bondInvest.getBondId();
			}
			model.addAttribute("buttonUrl", url); // 点击按钮跳转页面地址
			model.addAttribute("buttonName", CallbackConstant.PAY_BUTTON_NAME);
			model.addAttribute("otherUrl", URLConstant.BOND_HOME_PAGE); // 点击"其它产品"跳转页面地址
		}
		String userAgent = request.getHeader("User-Agent").toLowerCase();//判断访问来源 是否是移动端
		if(userAgent.contains("micromessenger")||userAgent.contains("android")||userAgent.contains("iphone")){//移动端
			 if (errorMsg!=null) {//失败
				   model.addAttribute("r_msg", errorMsg);
				   model.addAttribute("res_code", "register_fail");
				   return "/mobileRetresult";
				}else{//成功
					model.addAttribute("res_msg", "债券投资处理完成");
					model.addAttribute("res_code", "bond_success");
					return "/mobileResult";
				}
		}else{
			model.addAttribute("picType", picType);
			model.addAttribute("requestType", CallbackConstant.PAY_REQUEST_TYPE);
	
			model.addAttribute("otherUrl", URLConstant.BOND_HOME_PAGE); // 点击"其它产品"跳转页面地址
			model.addAttribute("errorMsg", errorMsg);
			return "/retResult";
		}
	}

	/**
	 * 债权投资异步回调
	 * 
	 * @author QianPengZhan
	 * @date 2016年7月29日
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ufx/bond/creditTransferNotfy")
	public void creditTransferNotfy(final UfxCreditTransferModel ufxModel ,
			final HttpServletRequest request,final HttpServletResponse response) {
		LOGGER.info("债权投资异步回调,回调参数：{}" , getRequestParams(request));
		// 1、实际业务处理
		ufxBondInvestService.doBondInvestValid(ufxModel);
		// 2 告知第三方汇付我们已经处理完毕
		printSuccessReturn(response);
	}

}
