package com.rd.ifaes.core.tpp.controller.ufx;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rd.ifaes.common.constant.UfxConstant;
import com.rd.ifaes.common.dict.CacheKeys;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.rabbit.RabbitUtils;
import com.rd.ifaes.common.util.EncodeUtils;
import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.core.constant.CallbackConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.constant.URLConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.tpp.exception.UfxException;
import com.rd.ifaes.core.tpp.model.ufx.UfxInvestFailModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxInvestModel;
import com.rd.ifaes.core.tpp.service.tpp.UfxInvestService;

/**
 * 投资相关回调处理
 * 
 * @author FangJun
 * @date 2016年7月14日
 */
@Controller
public class UfxInvestController extends BaseController {
	@Resource
	private UfxInvestService ufxInvestService;

	@Resource
	private ProjectInvestService projectInvestService;

	/**
	 * 投资同步回调处理
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/ufx/invest/return")
	public String ufxInvestReturn(UfxInvestModel ufxModel, final HttpServletRequest request, final Model model) {
		LOGGER.info("投资进入同步回调，回调参数：{}", getRequestParams(request));
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
			ProjectInvest invest = projectInvestService.getInvestByOrderNo(ufxModel.getOrderNo());
			if (invest != null) {
				model.addAttribute("buttonUrl", URLConstant.INVEST_DETAIL_PAGE_PREFIX + invest.getProjectId()); // 点击按钮跳转页面地址
			}
			model.addAttribute("resultName", CallbackConstant.PAY_REQUEST_NAME);
			model.addAttribute("resultUrl", URLConstant.MEMBER_MYINVEST_PAGE); // 点击“我的资产>我的投资”跳转页面地址
			model.addAttribute("buttonName", CallbackConstant.PAY_BUTTON_NAME);
		}
		// doInvest(ufxModel,request);
		String userAgent = request.getHeader("User-Agent").toLowerCase();// 判断访问来源
																			// 是否是移动端
		if (userAgent.contains("micromessenger") || userAgent.contains("android") || userAgent.contains("iphone")) {// 移动端
			if (errorMsg != null) {// 失败
				model.addAttribute("r_msg", errorMsg);
				model.addAttribute("res_code", "register_fail");
				return "/mobileRetresult";
			} else {// 成功
				model.addAttribute("res_msg", "投资处理完成");
				model.addAttribute("res_code", "invest_success");
				return "/mobileResult";
			}
		} else {
			model.addAttribute("picType", picType);
			model.addAttribute("requestType", CallbackConstant.PAY_REQUEST_TYPE);

			model.addAttribute("otherUrl", URLConstant.INVEST_HOME_PAGE); // 点击"其它产品"跳转页面地址
			model.addAttribute("errorMsg", errorMsg);
			return "/retResult";
		}
	}

	/**
	 * 投资异步回调处理
	 * 
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping(value = "/ufx/invest/notify")
	public void ufxInvestNotify(UfxInvestModel ufxModel, final HttpServletRequest request,final HttpServletResponse response) {
		LOGGER.info("投资进入异步回调，回调参数：{}", getRequestParams(request));
		doInvest(ufxModel,request);
		printSuccessReturn(response);
	}

	/**
	 * 变现投资回调
	 * 
	 * @author fxl
	 * @date 2016年8月01日
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ufx/realize/return")
	public String ufxRealizeReturn(UfxInvestModel ufxModel, final HttpServletRequest request, final Model model) {
		LOGGER.info("变现投资进入同步回调，回调参数：{}", getRequestParams(request));
		String userAgent = request.getHeader("User-Agent").toLowerCase();//判断访问来源 是否是移动端
		if(userAgent.contains("micromessenger")||userAgent.contains("android")||userAgent.contains("iphone")){//移动端
					model.addAttribute("res_msg", "变现投资处理完成");
					model.addAttribute("res_code", "realize_success");
					return "/mobileResult";
		}else{
		 model.addAttribute("picType", CallbackConstant.PAY_PICTYPE);
		 model.addAttribute("requestType", CallbackConstant.PAY_REQUEST_TYPE);
		 model.addAttribute("resultName", CallbackConstant.PAY_REQUEST_NAME);
		 model.addAttribute("resultUrl", URLConstant.MEMBER_MYINVEST_PAGE); // 点击“我的资产>我的投资”跳转页面地址
		 model.addAttribute("buttonName", CallbackConstant.PAY_BUTTON_NAME);
		ProjectInvest invest = projectInvestService.getInvestByOrderNo(ufxModel.getOrderNo());
		if (invest != null) {
			 model.addAttribute("buttonUrl", URLConstant.REALIZE_DETAIL_PAGE_PREFIX + invest.getProjectId()); // 点击按钮跳转页面地址
		} else {
			 model.addAttribute("buttonUrl", URLConstant.REALIZE_HOME_PAGE);
		}
		 model.addAttribute("otherUrl", URLConstant.REALIZE_HOME_PAGE); // 点击"其它产品"跳转页面地址
		return "/retResult";
		}
	}

	/**
	 * 变现投资进入异步回调
	 * 
	 * @param model
	 */
	@RequestMapping(value = "/ufx/realize/notify")
	public void ufxRealizeNotify(UfxInvestModel model, final HttpServletRequest request,final HttpServletResponse response) {
		LOGGER.info("变现投资进入异步回调，回调参数：{}", getRequestParams(request));
		doInvest(model,request);
		printSuccessReturn(response);
	}

	/**
	 * 投资逻辑处理处理
	 */
	private void doInvest(UfxInvestModel model, final HttpServletRequest request) {
		LOGGER.info("投资回调参数：{}", getRequestParams(request));
		boolean ret = model.validSign(model);

		if (ret) { // 验签成功
			// 重复处理判断(缓存标记)
			String handleKey = String.format(CacheKeys.PREFIX_INVEST_ORDER_NO_HANDLE_NUM.getKey(), model.getOrderNo());
			if (CacheUtils.incr(handleKey, Constant.DOUBLE_ONE) > Constant.DOUBLE_ONE) {
				LOGGER.info("订单号：{} ----投资订单回调, 已处理", model.getOrderNo());
				return;
			}
			LOGGER.info("投资回调进入本地处理，订单号：{}，返回状态：{}", model.getOrderNo(), EncodeUtils.urlDecode(model.getRespCode()));
			CacheUtils.expire(handleKey, ExpireTime.ONE_MIN);

			ufxInvestService.ufxInvestHandle(model);
		} else {
			LOGGER.error("投资回调验签失败，订单号：{}", model.getOrderNo());
			throw new UfxException(ResourceUtils.get(ResourceConstant.TPP_NOTIFY_SIGN_FAIL), BussinessException.TYPE_CLOSE);
		}
	}

	/**
	 * 投资失败资金退回（investFail）-异步回调处理
	 * 
	 * @param model
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/ufx/investFail/notify")
	public void ufxInvestFailNotify(UfxInvestFailModel model, final HttpServletRequest request,final HttpServletResponse response) {
		LOGGER.info("投资失败资金退回（investFail）进入异步回调，回调参数：{}", getRequestParams(request));
		boolean ret = model.validSign(model);
		if (ret) {
			if (UfxConstant.UFX_CODE_SUCCESS.equals(model.getRespCode())) {
				// 重复处理判断(缓存标记)
				String handleKey = String.format(CacheKeys.PREFIX_INVEST_FAIL_FREEZE_NO_HANDLE_NUM.getKey(), model.getFreezeNo());
				if (CacheUtils.incr(handleKey, Constant.DOUBLE_ONE) > Constant.DOUBLE_ONE) {
				    LOGGER.info( "订单号：{} ----投资失败资金退回,已处理",model.getFreezeNo());
					return;
				}
				// 验签成功
				LOGGER.info("investFail 回调进入本地处理，订单号：{} ,冻结流水号: {}，返回状态：{}", model.getOrderNo(),
						model.getFreezeNo(),EncodeUtils.urlDecode(model.getRespCode()));
				CacheUtils.expire(handleKey, ExpireTime.ONE_MIN);
				if(ConfigUtils.isOpenMq()){
				     RabbitUtils.investFail(model);
				}else{
					ufxInvestService.ufxInvestFailHandle(model);
				}
			} else {
				LOGGER.error("investFail  处理失败，订单号：{} ,冻结流水号: {},原因：{}", model.getOrderNo(),
						model.getFreezeNo(), EncodeUtils.urlDecode(model.getRespDesc()));
				throw new BussinessException(ResourceUtils.get(ResourceConstant.TPP_INVESTFAIL_FAIL), BussinessException.TYPE_CLOSE);
			}
		} else {
			LOGGER.error("investFail验签失败，订单号：{}", model.getOrderNo());
			throw new UfxException(ResourceUtils.get(ResourceConstant.TPP_NOTIFY_SIGN_FAIL), BussinessException.TYPE_CLOSE);
		}

		printSuccessReturn(response);
	}
}
