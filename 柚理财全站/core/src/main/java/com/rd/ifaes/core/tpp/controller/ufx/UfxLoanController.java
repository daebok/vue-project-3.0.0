/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.tpp.controller.ufx;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rd.ifaes.common.dict.CacheKeys;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.EncodeUtils;
import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.tpp.exception.UfxException;
import com.rd.ifaes.core.tpp.model.ufx.UfxLoansModel;
import com.rd.ifaes.core.tpp.service.tpp.UfxLoanService;

/**
 * 放款回调
 * 
 * @version 3.0
 * @author FangJun
 * @date 2016年10月12日
 */
@Controller
public class UfxLoanController extends BaseController {
	@Resource
	private UfxLoanService  ufxLoanService;
	/**
	 * 放款异步回调处理
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/ufx/loan/notify")
	public void ufxLoanNotify(final UfxLoansModel model,
			final HttpServletRequest request,final HttpServletResponse response) {
		LOGGER.info("放款进入异步回调，回调参数：{}", getRequestParams(request));
		 doLoan(model);
		printSuccessReturn(response);
	}

	/**
	 * 放款逻辑处理处理
	 */
	private void doLoan(UfxLoansModel model) {
		boolean ret = model.validSign(model);

		if (ret) { // 验签成功
			// 重复处理判断(缓存标记)
			String handleKey = String.format(CacheKeys.PREFIX_LOAN_ORDER_NO_HANDLE_NUM.getKey(), model.getOrderNo());
			if (CacheUtils.incr(handleKey, Constant.DOUBLE_ONE) > Constant.DOUBLE_ONE) {
				LOGGER.info("放款回调重复处理----orderNo：{}，freezeNo:{}", model.getOrderNo(),model.getFreezeNo());
				return;
			}
			LOGGER.info("放款回调进入本地处理，orderNo：{}，freezeNo:{}，返回状态：{}", 
					model.getOrderNo(), model.getFreezeNo(),EncodeUtils.urlDecode(model.getRespCode()));
			CacheUtils.expire(handleKey, ExpireTime.ONE_MIN);
			if (ConfigUtils.isOpenMq()) {
//				RabbitUtils.loan(model);
			} else {
				ufxLoanService.ufxLoanHandle(model);
			}
		} else {
			LOGGER.error("放款回调验签失败，orderNo：{}，freezeNo:{}", model.getOrderNo(), model.getFreezeNo());
			throw new UfxException(ResourceUtils.get(ResourceConstant.TPP_NOTIFY_SIGN_FAIL), BussinessException.TYPE_CLOSE);
		}
	}
}
