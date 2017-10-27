/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.tpp.controller.cbhb;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.dict.CacheKeys;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.tpp.exception.CbhbException;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbFileReleaseModel;

/**
 * 放款异步回调  --  处理资金
 * @version 3.0
 * @author QianPengZhan
 * @date 2017年3月8日
 */
@Controller
public class CbhbFileReleaseController extends BaseController{
	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CbhbFileReleaseController.class);
	
	@Resource
	private transient ProjectService projectService;
	
	@RequestMapping(value="/cbhb/fileRelease/notify")
	@ResponseBody
	public void CbhbFileReleaseNotify(final CbhbFileReleaseModel cbhbModel,final HttpServletResponse response, final HttpServletRequest request){
		LOGGER.info("fileRelease异步回调业务"+this.getRequestMap(request).toString());
		LOGGER.info("放款({})进入异步回调",cbhbModel.getMerBillNo());
		cbhbModel.setResponseType(CbhbConstant.CBHB_RESPONSE_TYPE_NOTIFY);
		cbhbModel.response(this.getRequestMap(request));
		this.fileReleaseHandle(cbhbModel);
		cbhbModel.printSuccess500Return(response); 
	}
	
	/**
	 * 放款
	 */
	private void fileReleaseHandle(CbhbFileReleaseModel model) {
		boolean ret = model.validSign(model);
		if (ret) { // 验签成功
			// 重复处理判断(缓存标记)
			String handleKey = String.format(CacheKeys.PREFIX_LOAN_ORDER_NO_HANDLE_NUM.getKey(), model.getMerBillNo());
			if (CacheUtils.incr(handleKey, Constant.DOUBLE_ONE) > Constant.DOUBLE_ONE) {
				LOGGER.info("渤海银行----放款回调重复处理----merBillNo：{}", model.getMerBillNo());
				return;
			}
			LOGGER.info("渤海银行----放款回调进入本地处理，merBillNo：{}，返回状态：{}", 
					model.getMerBillNo(), model.getRespCode());
			CacheUtils.expire(handleKey, ExpireTime.ONE_MIN);
			projectService.cbhbLoanNotify(model);
		} else {
			LOGGER.error("渤海银行----放款回调验签失败，merBillNo：{}，investNo:{}", model.getMerBillNo());
			throw new CbhbException(ResourceUtils.get(ResourceConstant.TPP_NOTIFY_SIGN_FAIL), BussinessException.TYPE_CLOSE);
		}
	}
}
