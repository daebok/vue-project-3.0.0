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
import com.rd.ifaes.core.tpp.model.cbhb.app.CbhbNetLoanRechargeModel;

@Controller
public class CbhbNetLoanWithDrawController extends BaseController{
	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CbhbNetLoanWithDrawController.class);
	/**
	 * app提现同步回调
	 * @return
	 */
	@RequestMapping(value = "/cbhbApp/appWithDraw/return")
	public String tppReturn(final CbhbNetLoanRechargeModel cbhbNetLoanRechargeModel, final HttpServletRequest request, final Model model) {
		 LOGGER.info("渤海银行APP端提现进入同步回调");
		 String msg = StringUtils.EMPTY;
		 String resultFlag = StringUtils.EMPTY;
		 cbhbNetLoanRechargeModel.response(this.getRequestMap(request));
		 if(cbhbNetLoanRechargeModel.verifyMac(cbhbNetLoanRechargeModel.getDecryMac())){//验签  
			 if (!CbhbConstant.CBHB_CODE_SUCCESS.equals(cbhbNetLoanRechargeModel.getRpCode())) {
				msg = ResourceUtils.get(ResourceConstant.USER_TPP_REGISTER_FAIL);
				if (StringUtils.isNotBlank(cbhbNetLoanRechargeModel.getRpDesc())) {
					msg = cbhbNetLoanRechargeModel.getRpDesc();
				}
			 }else{
					resultFlag = String.format(CacheKeys.PREFIX_TPP_ORDER_HANDLE_KEY.getKey(), cbhbNetLoanRechargeModel.getMerBillNo());
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
	 * app提现异步回调
	 * @throws IOException 
	 */
	@RequestMapping(value = "/cbhbApp/appWithDraw/notify")
	public void tppNotify(final CbhbNetLoanRechargeModel cbhbNetLoanRechargeModel,
			final HttpServletRequest request,final HttpServletResponse response) {
		LOGGER.info("渤海银行APP端提现进入异步回调，回调参数：{}" , getRequestMap(request).toString());
		cbhbNetLoanRechargeModel.response(this.getRequestMap(request));
		cbhbNetLoanRechargeModel.printSuccess500Return(response);
	}
}
