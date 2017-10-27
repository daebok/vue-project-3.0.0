package com.rd.ifaes.core.tpp.controller.cbhb;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.rd.ifaes.core.tpp.model.cbhb.CbhbFileRepaymentModel;
import com.rd.ifaes.core.tpp.service.tpp.UfxRepayService;

/**
 * 渤海银行还款回调
 * @version 3.0
 * @author ZhangBiao
 * @date 2017年3月9日
 */
@Controller
public class CbhbFileRepaymentController extends BaseController {
	@Resource
	private UfxRepayService  ufxRepayService;
	/**
	 * 还款异步回调处理
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cbhb/fileRepayment/notify")
	public void ufxLoanNotify(final CbhbFileRepaymentModel model,final HttpServletRequest request,final HttpServletResponse response) {
		LOGGER.info("渤海银行----还款进入异步回调，回调参数：{}", getRequestMap(request).toString());
		model.setResponseType(CbhbConstant.CBHB_RESPONSE_TYPE_NOTIFY);
		model.response(getRequestMap(request));
		this.repayHandle(model);
		model.printSuccess500Return(response); 
	}

	/**
	 * 还款逻辑处理处理
	 */
	private void repayHandle(CbhbFileRepaymentModel model) {
		boolean ret = model.validSign(model);

		if (ret) { // 验签成功
			// 重复处理判断(缓存标记)
			String handleKey = String.format(CacheKeys.PREFIX_REPAY_ORDER_NO_HANDLE_NUM.getKey(), model.getMerBillNo());
			if (CacheUtils.incr(handleKey, Constant.DOUBLE_ONE) > Constant.DOUBLE_ONE) {
				LOGGER.info("渤海银行----还款回调重复处理----merBillNo：{}", model.getMerBillNo());
				return;
			}
			LOGGER.info("渤海银行----还款回调进入本地处理，merBillNo：{}，返回状态：{}", 
					model.getMerBillNo(), model.getRespCode());
			CacheUtils.expire(handleKey, ExpireTime.ONE_MIN);
			ufxRepayService.cbhbRepayHandle(model);
		} else {
			LOGGER.error("渤海银行----还款回调验签失败，merBillNo：{}，investNo:{}", model.getMerBillNo());
			throw new CbhbException(ResourceUtils.get(ResourceConstant.TPP_NOTIFY_SIGN_FAIL), BussinessException.TYPE_CLOSE);
		}
	}
}
