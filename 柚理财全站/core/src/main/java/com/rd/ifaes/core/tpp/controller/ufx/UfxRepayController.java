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
import com.rd.ifaes.common.rabbit.RabbitUtils;
import com.rd.ifaes.common.util.EncodeUtils;
import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.tpp.exception.UfxException;
import com.rd.ifaes.core.tpp.model.ufx.UfxRepaymentModel;
import com.rd.ifaes.core.tpp.service.tpp.UfxRepayService;

/**
 * 还款异步回调处理
 * @version 3.0
 * @author FangJun
 * @date 2016年10月12日
 */
@Controller
public class UfxRepayController extends BaseController {
	@Resource
	private UfxRepayService  ufxRepayService;
	/**
	 * 还款异步回调处理
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/ufx/repay/notify")
	public void ufxLoanNotify(final UfxRepaymentModel model,final HttpServletRequest request,final HttpServletResponse response) {
		LOGGER.info("还款进入异步回调，回调参数：{}", getRequestParams(request));
		this.repayHandle(model);
		printSuccessReturn(response);
	}

	/**
	 * 还款逻辑处理处理
	 */
	private void repayHandle(UfxRepaymentModel model) {
		boolean ret = model.validSign(model);

		if (ret) { // 验签成功
			// 重复处理判断(缓存标记)
			String handleKey = String.format(CacheKeys.PREFIX_REPAY_ORDER_NO_HANDLE_NUM.getKey(), model.getOrderNo());
			if (CacheUtils.incr(handleKey, Constant.DOUBLE_ONE) > Constant.DOUBLE_ONE) {
				LOGGER.info("还款回调重复处理----orderNo：{}，investNo:{}", model.getOrderNo(),model.getInvestNo());
				return;
			}
			LOGGER.info("还款回调进入本地处理，orderNo：{}，investNo:{}，返回状态：{}", 
					model.getOrderNo(), model.getInvestNo(),EncodeUtils.urlDecode(model.getRespCode()));
			CacheUtils.expire(handleKey, ExpireTime.ONE_MIN);
			if(ConfigUtils.isOpenMq()){
			    RabbitUtils.repay(model);
		   }else{
			    ufxRepayService.ufxRepayHandle(model);
	 	    }
		} else {
			LOGGER.error("还款回调验签失败，orderNo：{}，investNo:{}", model.getOrderNo(),model.getInvestNo());
			throw new UfxException(ResourceUtils.get(ResourceConstant.TPP_NOTIFY_SIGN_FAIL), BussinessException.TYPE_CLOSE);
		}
	}
}
