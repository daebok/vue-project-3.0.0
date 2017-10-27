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

import com.rd.ifaes.core.account.domain.Recharge;
import com.rd.ifaes.core.account.service.RechargeService;
import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.core.constant.CallbackConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.constant.URLConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.tpp.exception.UfxException;
import com.rd.ifaes.core.tpp.model.ufx.UfxRechargeModel;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.service.UserCacheService;

/**
 * 充值
 * @author xhf
 * @version 3.0
 * @date 2016年6月14日 上午11:25:39
 */
@Controller
public class UfxRechargeController extends BaseController {
	/**
	 * 用户附属信息类
	 */
	@Resource
	private transient UserCacheService userCacheService;
	/**
	 * 用户充值类
	 */
	@Resource
	private transient RechargeService rechargeService;
	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(UfxRechargeController.class);
	
	/**
	 * 充值同回调
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/ufx/recharge/return")
	public String tppReturn(final UfxRechargeModel ufxModel, final HttpServletRequest request, final Model model) {
		LOGGER.info("进入充值同步回调，回调参数：{}",getRequestParams(request));
		final Recharge recharge = rechargeService.getRechargeByOrderNo(ufxModel.getOrderNo());
		 String userAgent = request.getHeader("User-Agent").toLowerCase();//判断访问来源 是否是移动端
		 if(userAgent.contains("micromessenger")||userAgent.contains("android")||userAgent.contains("iphone")){//移动端
			 if (null==recharge) {//失败
					   model.addAttribute("r_msg", ResourceUtils.get(ResourceConstant.ORDER_NOT_EXIST));
					   model.addAttribute("res_code", "register_fail");
					   return "/mobileRetresult";
					}else{//成功
						model.addAttribute("res_msg", "充值处理完成");
						model.addAttribute("res_code", "recharge_success");
						return "/mobileResult";
					}
			}else{
		if (null==recharge) {
			throw new UfxException(ResourceUtils.get(ResourceConstant.ORDER_NOT_EXIST));
		}
		// 担保机构单独跳转页面
		UserCache userCache = userCacheService.findByUserId(recharge.getUserId());
		if(userCache != null && UserCache.USER_NATURE_VOUCH.equals(userCache.getUserNature())){
			 model.addAttribute("resultUrl", CallbackConstant.RECHARGE_VOUCH_REQUEST_URL); 
		}else{
			 model.addAttribute("resultUrl", CallbackConstant.RECHARGE_REQUEST_URL); 
			 model.addAttribute("buttonName", CallbackConstant.RECHARGE_BUTTON_NAME);  
			 model.addAttribute("buttonUrl", URLConstant.INVEST_HOME_PAGE); 
		}	
		 model.addAttribute("picType", CallbackConstant.RECHARGE_PICTYPE);   
		 model.addAttribute("requestType", CallbackConstant.RECHARGE_REQUEST_TYPE);            
		 model.addAttribute("resultName",  CallbackConstant.RECHARGE_REQUEST_NAME); 
		return "/retResult";
			}
	}
	
	/**
	 * 充值异步回调
	 * @throws IOException 
	 */
	@RequestMapping(value = "/ufx/recharge/notify")
	public void tppNotify(final UfxRechargeModel model,final HttpServletRequest request,final HttpServletResponse response){
		LOGGER.info("进入充值异步回调，回调参数：{}" , getRequestParams(request));
		rechargeService.doTppRecharge(model);
		printSuccessReturn(response);
	}
	
}
