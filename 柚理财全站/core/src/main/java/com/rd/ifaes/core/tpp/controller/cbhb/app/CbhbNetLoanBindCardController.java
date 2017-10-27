package com.rd.ifaes.core.tpp.controller.cbhb.app;

import java.io.IOException;

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
import com.rd.ifaes.core.tpp.model.cbhb.app.CbhbNetLoanRegisterModel;

/**
 * 渤海 APP 修改银行卡回调
 * @author qpz
 *
 */
@Controller
public class CbhbNetLoanBindCardController extends BaseController{
	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CbhbNetLoanBindCardController.class);
	/**
	 * 修改银行卡同步回调
	 * @return
	 */
	@RequestMapping(value = "/cbhbApp/bindCardApp/return")
	public String tppReturn(final CbhbNetLoanRegisterModel cbhbNetLoanRegisterModel, final HttpServletRequest request, final Model model) {
		 LOGGER.info("渤海银行APP端修改银行卡进入同步回调");
		 String msg = StringUtils.EMPTY;
		 String resultFlag = StringUtils.EMPTY;
		 cbhbNetLoanRegisterModel.response(this.getRequestMap(request));
		 if(cbhbNetLoanRegisterModel.verifyMac(cbhbNetLoanRegisterModel.getDecryMac())){//验签  默认是OK
			 if (!CbhbConstant.CBHB_CODE_SUCCESS.equals(cbhbNetLoanRegisterModel.getRpCode())) {
				msg = ResourceUtils.get(ResourceConstant.USER_TPP_REGISTER_FAIL);
				if (StringUtils.isNotBlank(cbhbNetLoanRegisterModel.getRpDesc())) {
					msg = cbhbNetLoanRegisterModel.getRpDesc();
				}
			 }else{
				// userCacheService.doCbhbAppRegisger(cbhbNetLoanRegisterModel);
				 resultFlag = String.format(CacheKeys.PREFIX_TPP_ORDER_HANDLE_KEY.getKey(), cbhbNetLoanRegisterModel.getMerBillNo());
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
	 * 修改银行卡异步回调
	 * @throws IOException 
	 */
	@RequestMapping(value = "/cbhbApp/bindCardApp/notify")
	public void tppNotify(final CbhbNetLoanRegisterModel cbhbNetLoanRegisterModel,
			final HttpServletRequest request,final HttpServletResponse response) {
		LOGGER.info("渤海银行APP端修改银行卡进入异步回调，回调参数：{}" , getRequestMap(request).toString());
		cbhbNetLoanRegisterModel.response(this.getRequestMap(request));
		//userCacheService.doCbhbAppRegisger(cbhbNetLoanRegisterModel);
		cbhbNetLoanRegisterModel.printSuccess500Return(response);
	}
}
