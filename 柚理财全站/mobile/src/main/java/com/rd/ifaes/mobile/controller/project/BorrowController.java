/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.mobile.controller.project;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.TokenUtils;
import com.rd.ifaes.common.util.ip.IPUtil;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.project.domain.Borrow;
import com.rd.ifaes.core.project.service.BorrowService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.service.UserQualificationApplyService;
import com.rd.ifaes.mobile.controller.WebController;

/**
 *  前台借款
 * @version 3.0
 * @author ZhangBiao
 * @date 2016年8月2日
 */
@Controller
public class BorrowController extends WebController {
	/**
	 * WEB_BORROW
	 */
	private static final String WEB_BORROW = "webBorrow";
	/**
	 * BORROW_QUALIFICATION
	 */
	private static final String BORROW_QUALIFICATION ="/borrow/qualification";
	/**
	 * IMAGE_SERVE_URL
	 */
	private static final String IMAGE_SERVE_URL = "image_server_url";
	/**
	 * WEB_IMAGE_SERVE_URL
	 */
	private static final String WEB_IMAGE_SERVE_URL = "web_image_server_url";
	
	/**
	 * 证明资料service
	 */
	@Resource
	private transient UserQualificationApplyService applyService;
	/**
	 * 借款service
	 */
	@Resource
	private transient BorrowService borrowService;
	
	/**
	 * 
	 * 资质证明页面
	 * @author ZhangBiao
	 * @date 2016年7月30日
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/borrow/qualification")
	public String qualification(final HttpServletRequest request ,final Model model) {
		return checkUser(TokenConstants.TOKEN_WEB_BORROW_ADD,request,model)?BORROW_QUALIFICATION:"redirect:/borrow/addBorrowPage.html";
	}
	
	
	/**
	 * 
	 * 增加证明材料
	 * @author ZhangBiao
	 * @date 2016年8月2日
	 * @param qualificationTypes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/borrow/addFiles")
	@ResponseBody
	@TokenValid(value = TokenConstants.TOKEN_WEB_BORROW_QUALIFICATION_ADD, dispatch = true)
	public Object addFiles(@RequestParam(value = "qualificationTypes[]") final String[] qualificationTypes,final HttpServletRequest request) {
		applyService.addFiles(qualificationTypes, IPUtil.getRemortIP(request) , request);
		return renderResult(true, ResourceUtils.get(ResourceConstant.GLOBAL_MSG_SUCCESS));
	}

	/**
	 * 
	 * 借款页面
	 * @author ZhangBiao
	 * @date 2016年7月30日
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/borrow/addBorrowPage")
	public String addBorrowPage(final HttpServletRequest request ,final Model model) {
		return checkUser(TokenConstants.TOKEN_WEB_BORROW_ADD,request,model)?BORROW_QUALIFICATION:"/borrow/addBorrowPage";
	}
	
	/**
	 * 
	 * 上传数据
	 * @author ZhangBiao
	 * @date 2016年7月30日
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/borrow/addBorrowDetail")
	@ResponseBody
	@TokenValid(value = TokenConstants.TOKEN_WEB_BORROW_ADD, dispatch = true)
	public Object addBorrowDetail(final Borrow borrow,final HttpServletRequest request){
		// 获取借款资料及抵押物资料
		borrow.setPicPath(request.getParameterValues("picPaths[]"));
		borrow.setMtgPath(request.getParameterValues("mtgPaths[]"));
		final User user = SessionUtils.getSessionUser();
		final Borrow webBorrow = borrowService.addBorrow(borrow,user);
		SessionUtils.setSessionAttr(WEB_BORROW, webBorrow);
		return renderResult(true, ResourceUtils.get(ResourceConstant.BORROW_WEB_CONFIRM));
	}
	
	/**
	 * 
	 * 跳转借款确认页面
	 * @author ZhangBiao
	 * @date 2016年7月30日
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/borrow/addBorrow")
	public String addBorrow(final HttpServletRequest request ,final Model model) {
		return checkUser(TokenConstants.TOKEN_WEB_BORROW_DO_ADD,request,model)?BORROW_QUALIFICATION:"/borrow/addBorrow";
	}
	
	/**
	 * 
	 * 添加借款
	 * @author ZhangBiao
	 * @date 2016年7月30日
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/borrow/doAddBorrow")
	@ResponseBody
	@TokenValid(value = TokenConstants.TOKEN_WEB_BORROW_DO_ADD, dispatch = true)
	public Object doAddBorrow(final String validCode){
		final Borrow entity = (Borrow)SessionUtils.getSessionAttr(WEB_BORROW);
		entity.setValidCode(validCode);
		borrowService.doAddBorrow(entity);
		return renderResult(true, ResourceUtils.get(ResourceConstant.BORROW_WEB_SUCCESS));
	}
	
	/**
	 * 
	 * 获取已保存借款资料
	 * @author ZhangBiao
	 * @date 2016年8月12日
	 * @param qualificationType
	 * @return
	 */
	@RequestMapping(value = "/borrow/getBorrowData")
	@ResponseBody
	public Object getBorrowData(){
		final Borrow borrow = (Borrow) SessionUtils.getSessionAttr(WEB_BORROW);
		return borrowService.getBorrowDetail(borrow);
	}
	
	/**
	 * 校验跳转页面
	 * @author ZhangBiao
	 * @date 2016年10月24日
	 * @return
	 */
	private boolean checkUser(String token ,final HttpServletRequest request ,final Model model) {
		final String url = ConfigUtils.getValue(IMAGE_SERVE_URL);
		request.setAttribute(WEB_IMAGE_SERVE_URL, url);
		final User user = SessionUtils.getSessionUser();
		Map<String, Object> data = Maps.newHashMap();
		data = borrowService.checkUserBorrow(user);
		 model.addAttribute("data", JSON.toJSONString(data));
		final boolean bl = applyService.needAddFiles(user);
		if(bl){
			TokenUtils.setToken(request.getSession(), TokenConstants.TOKEN_WEB_BORROW_QUALIFICATION_ADD);
		}else{
			TokenUtils.setToken(request.getSession(), token);
		}
		return bl;
	}
}
