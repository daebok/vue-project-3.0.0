package com.rd.ifaes.core.tpp.controller.cbhb;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.dict.CacheKeys;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.tpp.exception.CbhbException;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbExistUserRegisterModel;
import com.rd.ifaes.core.user.service.UserService;

/**
 * 
 * 渤海银行批量注册
 * @version 3.0
 * @author ZhangBiao
 * @date 2017年2月28日
 */
@Controller
public class CbhbExistUserRegisterController extends BaseController {
	@Resource
	private transient UserService userService;
	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CbhbExistUserRegisterController.class);
	
	/**
	 * 批量注册回调
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cbhb/existUserRegister/notify")
	public void tppReturn(final CbhbExistUserRegisterModel cbhbModel,final HttpServletResponse response, final HttpServletRequest request) {
		LOGGER.info("批量注册异步回调业务参数{}",this.getRequestMap(request).toString());
		cbhbModel.setResponseType(CbhbConstant.CBHB_RESPONSE_TYPE_NOTIFY);
		cbhbModel.response(this.getRequestMap(request));
		this.realNameHandle(cbhbModel);
		cbhbModel.printSuccess500Return(response); 
	}
	
	/**
	 * 批量注册回调
	 */
	private void realNameHandle(CbhbExistUserRegisterModel model) {
		boolean ret = model.validSign(model);
		if (ret) { // 验签成功
			// 重复处理判断(缓存标记)
			String handleKey = String.format(CacheKeys.PREFIX_EXIST_USER_REGISTER_ORDER_NO_HANDLE_NUM.getKey(), model.getMerBillNo());
			if (CacheUtils.incr(handleKey, Constant.DOUBLE_ONE) > Constant.DOUBLE_ONE) {
				LOGGER.info("渤海银行----批量注册回调重复处理----merBillNo：{}", model.getMerBillNo());
				return;
			}
			LOGGER.info("渤海银行----批量注册回调进入本地处理，merBillNo：{}，返回状态：{}", 
					model.getMerBillNo(), model.getRespCode());
			CacheUtils.expire(handleKey, ExpireTime.ONE_MIN);
			userService.CbhbExistUserRegister(model);
		} else {
			LOGGER.error("渤海银行----批量注册回调验签失败，merBillNo：{}，investNo:{}", model.getMerBillNo());
			throw new CbhbException(ResourceUtils.get(ResourceConstant.TPP_NOTIFY_SIGN_FAIL), BussinessException.TYPE_CLOSE);
		}
	}
}
