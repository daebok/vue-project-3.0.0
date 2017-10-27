package com.rd.ifaes.core.tpp.controller.ufx;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rd.ifaes.core.base.web.BaseController;


/**
 * 自动投资UFX回调
 * @author xx
 * @version 2.3.0.0
 * @date 2016年1月4日 下午3:41:09
 * Copyright 杭州融都科技股份有限公司 P2P  All Rights Reserved
 * 官方网站：www.erongdu.com
 * 研发中心：rdc@erongdu.com
 * 未经授权不得进行修改、复制、出售及商业使用
 */
@Controller
public class UfxAutoInvestController extends BaseController {
	
	/**
	 * 自动投资异步回调
	 * @return
	 */
	@RequestMapping(value = "/ufx/autoInvest/notify")
	public void investNotify(final HttpServletResponse response){
		printSuccessReturn(response);
	}
}
