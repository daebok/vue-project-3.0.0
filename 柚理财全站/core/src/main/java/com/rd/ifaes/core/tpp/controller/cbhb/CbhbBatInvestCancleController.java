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
import com.rd.ifaes.common.util.EncodeUtils;
import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.tpp.exception.CbhbException;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbBatInvestCancleModel;

/**
 * 批量投资撤销  --  异步回调
 * @version 3.0
 * @author QianPengZhan
 * @date 2017年3月13日
 */
@Controller
public class CbhbBatInvestCancleController extends BaseController{
	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CbhbBatInvestCancleController.class);
	
	@Resource
	private transient ProjectService projectService;
	
	@RequestMapping(value="/cbhb/batInvestCancle/notify")
	@ResponseBody
	public void CbhbBatInvestCancleNotify(final CbhbBatInvestCancleModel cbhbModel,final HttpServletResponse response, final HttpServletRequest request){
		LOGGER.info("batInvestCancle异步回调业务"+this.getRequestMap(request).toString());
		LOGGER.info("(批量投资撤销{})进入异步回调",cbhbModel.getMerBillNo());
		cbhbModel.setResponseType(CbhbConstant.CBHB_RESPONSE_TYPE_NOTIFY);//录入异步通知类型
		cbhbModel.response(this.getRequestMap(request));
		this.batInvestCancleHandle(cbhbModel);
		cbhbModel.printSuccess500Return(response); 
	}
	
	/**
	 * 批量投资撤销 逻辑处理
	 */
	private void batInvestCancleHandle(CbhbBatInvestCancleModel model) {
		boolean ret = model.validSign(model);
		if (ret) {
			if (CbhbConstant.CBHB_CODE_SUCCESS.equals(model.getRespCode())) {
				// 重复处理判断(缓存标记)
				String handleKey = String.format(CacheKeys.PREFIX_INVEST_FAIL_FREEZE_NO_HANDLE_NUM.getKey(), model.getMerBillNo());
				if (CacheUtils.incr(handleKey, Constant.DOUBLE_ONE) > Constant.DOUBLE_ONE) {
				    LOGGER.info( "订单号：{} ----投资失败资金退回,已处理",model.getMerBillNo());
					return;
				}
				// 验签成功
				LOGGER.info("investFail 回调进入本地处理，订单号：{} ，返回状态：{}", model.getMerBillNo(),EncodeUtils.urlDecode(model.getRespCode()));
				CacheUtils.expire(handleKey, ExpireTime.ONE_MIN);
				projectService.cbhbBatInvestCancleNotify(model);
			} else {
				LOGGER.error("investFail  处理失败，订单号：{},原因：{}", model.getMerBillNo(), EncodeUtils.urlDecode(model.getRespDesc()));
				throw new BussinessException(ResourceUtils.get(ResourceConstant.TPP_INVESTFAIL_FAIL), BussinessException.TYPE_CLOSE);
			}
		} else {
			LOGGER.error("investFail验签失败，订单号：{}", model.getMerBillNo());
			throw new CbhbException(ResourceUtils.get(ResourceConstant.TPP_NOTIFY_SIGN_FAIL), BussinessException.TYPE_CLOSE);
		}
	}
}
