package com.rd.ifaes.core.tpp.controller.ufx;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.tpp.model.ufx.UfxCashAuditModel;
import com.rd.ifaes.core.tpp.service.tpp.UfxCashAuditService;

/**
 * 用户取现复核回调
 * @author xhf
 * @version 3.0
 * @date 2016年7月4日 下午4:46:04
 */
@Controller
public class UfxCashAuditController extends BaseController {

	/**
	 * 日志跟踪器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(UfxCashAuditController.class);
	
	/**
	 * 用户基础类
	 */
	@Resource
	private transient UfxCashAuditService ufxCashAuditService;

	/**
	 * 提现复核异步回调
	 * @throws Exception
	 */
	@RequestMapping(value = "/ufx/cashAudit/notify")
	@ResponseBody
	public Object tppNotify(final UfxCashAuditModel model,final HttpServletRequest request){
		LOGGER.info("取现复核进入异步回调，回调参数：{}" , getRequestParams(request));
		ufxCashAuditService.doCashAudit(model);
		return renderResult(true, "SUCCESS");
	}

}
