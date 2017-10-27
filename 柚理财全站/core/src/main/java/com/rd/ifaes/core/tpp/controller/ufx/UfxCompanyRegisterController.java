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
import com.rd.ifaes.core.tpp.model.ufx.UfxCompanyRegisterModel;
import com.rd.ifaes.core.user.service.UserCacheService;

/**
 * 企业开户
 * @author xhf
 * @version 3.0
 * @date 2016年6月5日 下午4:46:04
 */
@Controller
public class UfxCompanyRegisterController extends BaseController {

	/**
	 * 日志记录器
	 */
	private final static Logger LOGGER = LoggerFactory.getLogger(UfxCompanyRegisterController.class);
	
	/**
	 * 用户附属信息类
	 */
	@Resource
	private transient UserCacheService userCacheService;
	
	/**
	 * 企业开户同步回调
	 * 企业开户没有同步回调，只有在第三方校验不通过的情况下才需要用到同步地址
	 * @return
	 */
	@RequestMapping(value = "/ufx/companyRegister/return")
	public String tppReturn(final UfxCompanyRegisterModel ufxModel, final HttpServletRequest request, final Model model) {
		 LOGGER.info("企业开户进入同步回调，回调参数：{}" , getRequestParams(request));
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
		 model.addAttribute("msg", msg);
		 model.addAttribute("resultFlag", resultFlag);
		
		 model.addAttribute("picType", CallbackConstant.REGISTER_PICTYPE);   
		 model.addAttribute("requestType", CallbackConstant.REGISTER_REQUEST_TYPE);            
		 model.addAttribute("resultName",  CallbackConstant.REGISTER_REQUEST_NAME); 
		 model.addAttribute("resultUrl", CallbackConstant.REGISTER_REQUEST_URL); 
		return "/retRegResult";
	}
	
	/**
	 * 企业开户异步回调
	 * @throws IOException 
	 */
	@RequestMapping(value = "/ufx/companyRegister/notify")
	public void tppNotify(final UfxCompanyRegisterModel ufxModel,
			final HttpServletRequest request,final HttpServletResponse response) {
		LOGGER.info("企业开户进入异步回调，回调参数：{}" , getRequestParams(request));
		
		userCacheService.doCompanyRegisger(ufxModel);
		printSuccessReturn(response);
	}	
	
}
