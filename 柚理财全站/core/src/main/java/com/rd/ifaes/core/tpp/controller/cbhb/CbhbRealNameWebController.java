package com.rd.ifaes.core.tpp.controller.cbhb;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.dict.CacheKeys;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.core.constant.CallbackConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbRealNameWebModel;
import com.rd.ifaes.core.user.service.UserCacheService;

/**
 * 
 *  新用户注册
 * @version 3.0
 * @author ZhangBiao
 * @date 2017年2月22日
 */
@Controller
public class CbhbRealNameWebController extends BaseController{

	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CbhbRealNameWebController.class);
	
	/**
	 * 用户附属信息类
	 */
	@Resource
	private transient UserCacheService userCacheService;
	
	/**
	 * 开户同步回调
	 * @return
	 */
	@RequestMapping(value = "/cbhb/register/return")
	public String tppReturn(final CbhbRealNameWebModel cbhbModel, final HttpServletRequest request, final Model model) {
		 LOGGER.info("个人开户进入同步回调，回调参数：{}" , getRequestMap(request).toString());
		 String msg = StringUtils.EMPTY;
		 String resultFlag = StringUtils.EMPTY;
		 cbhbModel.setResponseType(CbhbConstant.CBHB_RESPONSE_TYPE_RETURN);
		 cbhbModel.response(this.getRequestMap(request));
		 if(cbhbModel.validSign(cbhbModel)){  //验签
			 if (!CbhbConstant.CBHB_CODE_SUCCESS.equals(cbhbModel.getRespCode())) {
				msg = ResourceUtils.get(ResourceConstant.USER_TPP_REGISTER_FAIL);
				if (StringUtils.isNotBlank(cbhbModel.getRespDesc())) {
					msg = cbhbModel.getRespDesc();
				}
			 }else{
				 resultFlag = String.format(CacheKeys.PREFIX_TPP_ORDER_HANDLE_KEY.getKey(), cbhbModel.getMerBillNo());
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
	 * 开户异步回调
	 * @throws IOException 
	 */
	@RequestMapping(value = "/cbhb/register/notify")
	public void tppNotify(final CbhbRealNameWebModel model,
			final HttpServletRequest request,final HttpServletResponse response) {
		LOGGER.info("渤海银行---个人开户进入异步回调，回调参数：{}" , getRequestMap(request).toString());
		// 验签
		model.setResponseType(CbhbConstant.CBHB_RESPONSE_TYPE_NOTIFY);
		model.response(this.getRequestMap(request));
		userCacheService.doCbhbRegisger(model);
		model.printSuccess500Return(response);
	}

}
