package com.rd.ifaes.core.tpp.controller.ufx;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.constant.UfxConstant;
import com.rd.ifaes.core.account.domain.Cash;
import com.rd.ifaes.core.account.service.CashService;
import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.core.constant.CallbackConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.tpp.exception.UfxException;
import com.rd.ifaes.core.tpp.model.ufx.UfxCashModel;
import com.rd.ifaes.core.tpp.service.tpp.UfxCashService;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.service.UserCacheService;

/**
 * 用户取现回调
 * @author xhf
 * @version 3.0
 * @date 2016年7月4日 下午4:46:04
 */
@Controller
public class UfxCashController extends BaseController {

	/**
	 * 日志跟踪器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(UfxCashController.class);
	
	/**
	 * 用户异步提现处理类
	 */
	@Resource
	private transient UfxCashService ufxCashService;
	
	/**
	 * 提现处理类
	 */
	@Resource
	private transient CashService cashService;
	
	/**
	 * 用户附属信息类
	 */
	@Resource
	private transient UserCacheService userCacheService;
	
    /**
     * 提现同步回调
     * @return
     * @throws Exception
     */
	@RequestMapping(value = "/ufx/cash/return")
	public String tppReturn(final UfxCashModel ufxModel, final HttpServletRequest request, final Model model) {
	    LOGGER.info("取现({})进入同步回调",ufxModel.getOrderNo());
	    final Cash cash = cashService.findByOrderNo(ufxModel.getOrderNo());
	    String userAgent = request.getHeader("User-Agent").toLowerCase();//判断访问来源 是否是移动端
		 if(userAgent.contains("micromessenger")||userAgent.contains("android")||userAgent.contains("iphone")){//移动端
			 if (null==cash) {//失败
					   model.addAttribute("r_msg", ResourceUtils.get(ResourceConstant.ORDER_NOT_EXIST));
					   model.addAttribute("res_code", "register_fail");
					   return "/mobileRetresult";
					}else{//成功
						model.addAttribute("res_msg", "提现处理完成");
						model.addAttribute("res_code", "cash_success");
						return "/mobileResult";
					}
			}else{
		if (null==cash) {
			throw new UfxException(ResourceUtils.get(ResourceConstant.ORDER_NOT_EXIST));
		}
		// 担保机构单独跳转页面
		UserCache userCache = userCacheService.findByUserId(cash.getUserId());
		if(userCache != null && UserCache.USER_NATURE_VOUCH.equals(userCache.getUserNature())){
			 model.addAttribute("resultUrl", CallbackConstant.CASH_VOUCH_REQUEST_URL); 
		}else{
			 model.addAttribute("resultUrl", CallbackConstant.CASH_REQUEST_URL); 
		}	
		 model.addAttribute("picType", CallbackConstant.CASH_PICTYPE);   
		 model.addAttribute("requestType", CallbackConstant.CASH_REQUEST_TYPE);            
		 model.addAttribute("resultName",  CallbackConstant.CASH_REQUEST_NAME); 
		return "/retResult";
			}
	}

	/**
	 * 提现异步回调
	 * @throws Exception
	 */
	@RequestMapping(value = "/ufx/cash/notify")
	@ResponseBody
	public Object tppNotify(final UfxCashModel model){
		 LOGGER.info("取现({})进入异步回调",model.getOrderNo());
		ufxCashService.doCash(model);
		return renderResult(true, UfxConstant.UFX_NOTIFY_RECEIVED_SUCCESS);
	}
	

}
