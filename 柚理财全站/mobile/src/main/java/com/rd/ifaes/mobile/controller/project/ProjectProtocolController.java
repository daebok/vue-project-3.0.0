package com.rd.ifaes.mobile.controller.project;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.protocol.service.ProtocolService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserService;

@Controller
public class ProjectProtocolController extends BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectProtocolController.class);
	/**
	 * ERROR_LOAD
	 */
	private static final String ERROR_LOAD = "下载出错,地址：{}";
	 
	/**
	 * REDIRECT_URL
	 */
	private static final String REDIRECT_URL = "/user/login.html?redirectURL=/member/invest/myInvest.html";
	 
	@Resource
	private ProjectInvestService projectInvestService;
	@Resource
	private ProjectService projectService;
	@Resource
	private ProtocolService protocolService;
	@Resource
	private UserService userService;
	@Resource
	private UserCacheService userCacheService;
	
	/**
	 * 
	 * 根据协议类型查询到多个协议
	 * http://localhost:8080/invest/getProtocolListByType.html?protocolType=register_protocol
	 * @author jxx
	 * @date 2016年8月2日
	 * @param protocolType
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/invest/getProtocolListByType")
	@ResponseBody
	public Object getProtocolListByType(String protocolType) throws Exception {
		LOGGER.debug("getProtocolList,protocolType:{}",protocolType);
		return protocolService.getProtocolListByType(protocolType);
	}
	
	/**
	 * 
	 * 下载页面
	 * 例子页面
	 * @author jxx
	 * @date 2016年7月28日
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/invest/testBuildProtocolPage")
	public String testBuildProtocolPage() throws Exception {
		
		return "/invest/testBuildProtocolPage";
	}
}
