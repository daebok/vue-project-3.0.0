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

import com.rd.ifaes.common.constant.UfxConstant;
import com.rd.ifaes.common.dict.CacheKeys;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.core.constant.CallbackConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.tpp.model.ufx.UfxRegisterModel;
import com.rd.ifaes.core.user.service.UserCacheService;

/**
 * 
 * 个人开户回调
 * @version 3.0
 * @author xhf
 * @date 2016年8月10日
 */
@Controller
public class UfxRegisterController extends BaseController{

	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(UfxRegisterController.class);
	
	/**
	 * 用户附属信息类
	 */
	@Resource
	private transient UserCacheService userCacheService;
	
	/**
	 * 开户同步回调
	 * @return
	 */
	@RequestMapping(value = "/ufx/register/return")
	public String tppReturn(final UfxRegisterModel ufxModel, final HttpServletRequest request, final Model model) {
		 LOGGER.info("个人开户进入同步回调，回调参数：{}" , getRequestParams(request));
		 String msg = "", resultFlag = "";
		 if(ufxModel.validSign(ufxModel)){  //验签
			 if (!UfxConstant.UFX_CODE_SUCCESS.equals(ufxModel.getRespCode())) {
				msg = ResourceUtils.get(ResourceConstant.USER_TPP_REGISTER_FAIL);
				if (StringUtils.isNotBlank(ufxModel.getRespDesc())) {
					msg = ufxModel.getRespDesc();
				}
			 }else{
				 resultFlag = String.format(CacheKeys.PREFIX_TPP_ORDER_HANDLE_KEY.getKey(), ufxModel.getOrderNo());
			 }
		 }else{
			msg = ResourceUtils.get(ResourceConstant.TPP_NOTIFY_SIGN_FAIL);
		 }
		 
		 String userAgent = request.getHeader("User-Agent").toLowerCase();//判断访问来源 是否是移动端
		 if(userAgent.contains("micromessenger")||userAgent.contains("android")||userAgent.contains("iphone")){//移动端
				if(!"".equals(msg)){//失败
					   model.addAttribute("r_msg", msg);
					   model.addAttribute("res_code", "register_fail");
					   return "/mobileRetresult";
					}else if(!"".equals(resultFlag)){//成功
						model.addAttribute("res_msg", "开户处理完成");
						model.addAttribute("res_code", "register_success");
						return "/mobileResult";
					}else{
						model.addAttribute("r_msg","开户处理失败");
						model.addAttribute("res_code", "register_fail");
						return "/mobileRetresult";
					}
			}else{
		 model.addAttribute("msg", msg);
		 model.addAttribute("resultFlag", resultFlag);
		
		 model.addAttribute("picType", CallbackConstant.REGISTER_PICTYPE);   
		 model.addAttribute("requestType", CallbackConstant.REGISTER_REQUEST_TYPE);            
		 model.addAttribute("resultName",  CallbackConstant.REGISTER_REQUEST_NAME); 
		 model.addAttribute("resultUrl", CallbackConstant.REGISTER_REQUEST_URL); 
		return "/retRegResult";
			}
	}
	
	/**
	 * 开户异步回调
	 * @throws IOException 
	 */
	@RequestMapping(value = "/ufx/register/notify")
	public void tppNotify(final UfxRegisterModel model,
			final HttpServletRequest request,final HttpServletResponse response) {
		LOGGER.info("个人开户进入异步回调，回调参数：{}" , getRequestParams(request));
		
		userCacheService.doRegisger(model);
		printSuccessReturn(response);
	}

}
