package com.rd.ifaes.mobile.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.dict.InvestEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.resource.ArticleResource;
import com.rd.ifaes.core.column.domain.Article;
import com.rd.ifaes.core.column.domain.Section;
import com.rd.ifaes.core.column.service.ArticleService;
import com.rd.ifaes.core.column.service.SectionService;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.home.service.HomeService;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.service.ProjectInvestBespeakService;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.project.service.ProjectTypeService;
import com.rd.ifaes.core.sys.domain.DictData;
import com.rd.ifaes.core.sys.service.DictDataService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.service.UserEarnLogService;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.mobile.model.index.AvailableServerModel;
import com.rd.ifaes.mobile.model.index.bannerModel;
import com.rd.ifaes.mobile.model.index.indexModel;
import com.rd.ifaes.mobile.model.project.projectIndexItemRecordModel;

/**
 * 首页
 * 
 * @version 3.0
 * @since 2016-6-16
 */
@Controller
public class IndexController extends WebController {
	/**
	 * 文章业务
	 */
	@Resource
	private transient ArticleService articleService;
	/**
	 * sectionService 业务
	 */
	@Resource
	private transient SectionService sectionService;
	/**
	 * 用户业务
	 */
	@Resource
	private transient UserService userService;
	/**
	 * 项目业务
	 */
	@Resource
	private transient ProjectService projectService;
	/**
	 * userEarnLogService 业务
	 */
	@Resource
	private transient UserEarnLogService userEarnLogService;
	/**
	 * 项目投资业务
	 */
	@Resource
	private transient ProjectInvestService projectInvestService;
	/**
	 * 项目预约投资业务
	 */
	@Resource
	private transient ProjectInvestBespeakService projectInvestBespeakService;
	@Resource
	private transient HomeService homeService;
	/**
	 * projectTypeService 业务
	 */
	@Resource
	private transient ProjectTypeService projectTypeService;
	 @Resource
	 private transient DictDataService dictDataService;

	/**
	 * 
	 * 移动端 首页统计信息
	 * 
	 * @author wyw
	 * @date 2016-8-14
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "app/open/index/countInfo")
	@ResponseBody
	public Object countInfo() throws Exception {
		Object obj = null;
		try {
			indexModel model = new indexModel();
			ProjectInvest projectInvest = new ProjectInvest();
			projectInvest.setStatus(InvestEnum.STATUS_SUCCESS.getValue());
			Double investTotal = projectInvestService.sumInvest(projectInvest)
					.doubleValue();
			model.setInvestTotal(investTotal);
			obj = createSuccessAppResponse(model);
		} catch (Exception e) {
			obj = dealException(e);
		}
		return obj;
	}

	/**
	 * 
	 * 移动端 首页--推荐投资列表
	 * 
	 * @author lgx
	 * @date 2016-10-19
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "app/open/index/getProjectList")
	@ResponseBody
	public Object getProjectList(HttpServletRequest request) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		Object obj = null;
		try {
			final Project queryProject = new Project();
			String userAgent = request.getHeader("User-Agent").toLowerCase();//判断访问来源 是否是微信浏览器
			@SuppressWarnings("unused")
			final User user = (User) SessionUtils
					.getSessionAttr(Constant.SESSION_USER);
			// 募集中、 成立待审核、成立审核成功
			String[] projectStatus = { LoanEnum.STATUS_RAISING.getValue(),
					LoanEnum.STATUS_RAISE_FINISHED.getValue(),
					LoanEnum.STATUS_ESTABLISH.getValue() };
			// ,LoanEnum.STATUS_REPAY_START.getValue(),LoanEnum.STATUS_REPAYED_SUCCESS.getValue()};
			queryProject.setNovice(LoanEnum.NOVICE_FLAG_YES.getValue());
			// queryProject.setChoice(LoanEnum.CHOICE_FLAG_NO.getValue());
			// 排序
			queryProject.setStatusSet(projectStatus);
			if("android".equals(userAgent)||"iphone".equals(userAgent)){//安卓
				queryProject.setSaleChannel(InvestEnum.INVEST_CHANNEL_APP.getValue());
			}else{
				queryProject.setSaleChannel(InvestEnum.INVEST_CHANNEL_WECHAT.getValue());
			}
			final Page<Project> page = new Page<>();
			page.setPageSize(2);
			page.setSort("status, show_time");
			page.setOrder("desc");
			queryProject.setPage(page);
			Map<String ,Object> remainmap=null;
			final List<Project> projectlist = projectService
					.findIndexProjectList(queryProject);
			List<projectIndexItemRecordModel> pirlist = new ArrayList<projectIndexItemRecordModel>();
			for (Project project : projectlist) {
				projectIndexItemRecordModel model = new projectIndexItemRecordModel();
				model.setProjectId(project.getUuid());/* 项目id */
				model.setAccount(project.getAccount());/* 项目总额 */
				model.setMostAccount(project.getAccount());/* 最高投资总额 */
				model.setSaleTime(project.getSaleTime());// 开售时间
				if (null != project.getSaleTime()
						&& project.getSaleTime().after(new Date())) {
					model.setIfSale("0");
				} else {
					model.setIfSale("1");// 已经开售
				}
				model.setProjectName(project.getProjectTypeName() + "-"
						+ project.getProjectNo()); // 项目名称
				model.setApr(project.getApr()); // 年利率
				model.setAddApr(project.getAddApr());// 加息
				model.setTimeType(project.getTimeType());// 借款期限类型： 0月标 1天标
				model.setTimeLimit(project.getTimeLimit());// 借款期限
				model.setLowestAccount(project.getLowestAccount());// 最低起投金额
				model.setRealizeUseful(project.getRealizeUseful());/*
																	 * 是否可变现: 1
																	 * 可变现 0
																	 * 不可变现，默认 0
																	 */
				model.setBondUseful(project.getBondUseful());/*
															 * 是否可以债权转让 1
															 * 可以，0不可以
															 */
				model.setSpecificSale(project.getSpecificSale());/*
																 * 定向销售方式(0 不定向
																 * 1 定向密码 2 定向等级
																 * 3 定向邮箱域名，默认0)
																 */
				model.setSpecificSaleRule(project.getSpecificSaleRule()==null?"0":project.getSpecificSaleRule());/*定向销售规则（对应规则内容,如：具体定向密码、定向邮箱域名）*/
				model.setNovice(project.getNovice());/* 新手标标识： 1新手专享 0 普通 ，默认：0 */
				model.setTimeNow(DateUtils.getNow());//当前时间
				remainmap=projectService.getRemainAccount(project.getUuid());
				Iterator<String> it = remainmap.keySet().iterator();
				while (it.hasNext()) {
					Object key = it.next();
					if ("remainAccount".equals(key))
						model.setRemainAccount((Double) remainmap.get(key));//剩余可投金额					
				}
				 Project projectmodel = new Project();
				 projectmodel.setUuid(model.getProjectId());
				 projectmodel.setStatus(LoanEnum.STATUS_RAISING.getValue());
				if(projectService.getCount(projectmodel) == Constant.INT_ZERO){
					model.setRemainAccount((double) 0);//剩余可投金额					
				}
				pirlist.add(model);
			}
			data.put("novicelist",pirlist);

			Project queryProjectj = new Project();
			// 募集中 成立待审核
			String[] projectStatusj ={LoanEnum.STATUS_RAISING.getValue(), LoanEnum.STATUS_RAISE_FINISHED.getValue(), LoanEnum.STATUS_ESTABLISH.getValue()};
			queryProjectj.setChoice(LoanEnum.CHOICE_FLAG_YES.getValue());
			// 排序
			queryProjectj.setStatusSet(projectStatusj);
			if("android".equals(userAgent)||"iphone".equals(userAgent)){//安卓
				queryProjectj.setSaleChannel(InvestEnum.INVEST_CHANNEL_APP.getValue());
			}else{
				queryProjectj.setSaleChannel(InvestEnum.INVEST_CHANNEL_WECHAT.getValue());
			}
			Page<Project> pagej = new Page<>();
			pagej.setPageSize(6);
			pagej.setSort("status, show_time");
			pagej.setOrder("desc");
			queryProjectj.setPage(pagej);
			List<Project> projectlistj = projectService
					.findIndexProjectList(queryProjectj);
			List<projectIndexItemRecordModel> pirlistj = new ArrayList<projectIndexItemRecordModel>();
			for (Project project : projectlistj) {
				projectIndexItemRecordModel model = new projectIndexItemRecordModel();
				model.setProjectId(project.getUuid());/* 项目id */
				model.setAccount(project.getAccount());/* 项目总额 */
				model.setMostAccount(project.getAccount());/* 最高投资总额 */
				model.setSaleTime(project.getSaleTime());// 开售时间
				if (null != project.getSaleTime()
						&& project.getSaleTime().after(new Date())) {
					model.setIfSale("0");
				} else {
					model.setIfSale("1");// 已经开售
				}
				model.setProjectName(project.getProjectTypeName() + "-"
						+ project.getProjectNo()); // 项目名称
				model.setApr(project.getApr()); // 年利率
				model.setAddApr(project.getAddApr());// 加息
				model.setTimeType(project.getTimeType());// 借款期限类型： 0月标 1天标
				model.setTimeLimit(project.getTimeLimit());// 借款期限
				model.setLowestAccount(project.getLowestAccount());// 最低起投金额
				model.setRealizeUseful(project.getRealizeUseful());/*
																	 * 是否可变现: 1
																	 * 可变现 0
																	 * 不可变现，默认 0
																	 */
				model.setBondUseful(project.getBondUseful());/*
															 * 是否可以债权转让 1
															 * 可以，0不可以
															 */
				model.setSpecificSale(project.getSpecificSale());/*
																 * 定向销售方式(0 不定向
																 * 1 定向密码 2 定向等级
																 * 3 定向邮箱域名，默认0)
																 */
				model.setSpecificSaleRule(project.getSpecificSaleRule()==null?"0":project.getSpecificSaleRule());/*定向销售规则（对应规则内容,如：具体定向密码、定向邮箱域名）*/
				model.setNovice(project.getNovice());/* 新手标标识： 1新手专享 0 普通 ，默认：0 */
				remainmap=projectService.getRemainAccount(project.getUuid());
				Iterator<String> it = remainmap.keySet().iterator();
				while (it.hasNext()) {
					Object key = it.next();
					if ("remainAccount".equals(key))
						model.setRemainAccount((Double) remainmap.get(key));//剩余可投金额					
				}
				model.setTimeNow(DateUtils.getNow());//当前时间
				 Project projectmodelj = new Project();
				 projectmodelj.setUuid(model.getProjectId());
				 projectmodelj.setStatus(LoanEnum.STATUS_RAISING.getValue());
				if(projectService.getCount(projectmodelj) == Constant.INT_ZERO){
					model.setRemainAccount((double) 0);//剩余可投金额					
				}
				pirlistj.add(model);
			}
			data.put("choicelist",pirlistj);
			obj = createSuccessAppResponse(data);
		} catch (Exception e) {
			obj = dealException(e);
		}
		return obj;
	}

	// 移动端-投资预约
	@RequestMapping(value = "/app/member/index/investBespeak")
	@ResponseBody
	public Object investBespeak(String projectId, HttpServletRequest request)
			throws Exception {
		Object obj=null;
		try {
			User user = getAppSessionUser(request);
			Project project = projectService.get(projectId);
			projectInvestBespeakService.investBespeak(user.getUuid(), project);
			obj=createSuccessAppResponse("您已经设置开售提醒\n产品开售前5分钟提醒您前来投资");
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
	}

	/**
	 * 移动端首页banner
	 */
	@RequestMapping(value = "/app/open/index/banner")
	@ResponseBody
	public Object getBanner(String code) {
		Map<String, Object> data = new HashMap<String, Object>();
		Object obj=null;
		String bannerCode = "appBanner";
		try {
			List<bannerModel> bannerList = new ArrayList<bannerModel>();
			if(code!=null){
				bannerCode = code;
			}
			Section section = sectionService.getByCode(bannerCode);
			if (section == null) {
				throw new BussinessException(
						ResourceUtils.get(ArticleResource.ARTICLE_NOT_EXISTS),
						BussinessException.TYPE_JSON);
			}
			List<Article> list = articleService.findArticleBySection(section,4);
			if (list != null && list.size() > 0) {
				for (Article article : list) {
					bannerModel banner = new bannerModel();
					banner.setPicPath(article.getPicPath());// 图片路径
					banner.setTitle(article.getTitle());// 标题
					banner.setUrl(article.getUrl());// 链接
					bannerList.add(banner);
				}
			}
			data.put("list", bannerList);
			obj=createSuccessAppResponse(data);
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
	}

	/**
	 * 移动端 获取服务器地址
	 */
	@RequestMapping(value="/app/open/index/servers")
	@ResponseBody
	public Object servers() {
		Object obj=null;
		try{             
			AvailableServerModel availableServers = new AvailableServerModel();
		availableServers.setMobileServer(ConfigUtils.getValue("mobile_url"));
		availableServers.setImageServer(ConfigUtils.getValue(ConfigConstant.IMAGE_SERVER_URL));
		availableServers.setDaysOfYear(ConfigUtils.getValue("days_of_year"));
		final List<DictData> repayStyles = dictDataService.findListByDictType(DictTypeEnum.REPAY_STYLE.getValue());
		availableServers.setRepayStyles(repayStyles);
		obj=createSuccessAppResponse(availableServers);
	} catch (Exception e) {
		obj=dealException(e);
	}
	return obj;
	}
	/**
	 * 微信注册接口
	 * @author lgx
	 * @date 2017年1月13日
	 * @return
	 */
	@RequestMapping(value = "/app/open/wechat/register")
	public Object register(final Model model,String ui){
		try {
			model.addAttribute("mobileUrl", ConfigUtils.getValue("mobile_url"));
			model.addAttribute("ui", ui);
		return "/app/invite/app_register";
		} catch (Exception e) {
			model.addAttribute("r_msg", e.getMessage());
			return ConfigUtils.getTppName()+"/retresult";
		}
	}
	/**
	 * 微信注册接口--成功
	 * @author lgx
	 * @date 2017年1月13日
	 * @return
	 */
	@RequestMapping(value = "/app/open/wechat/registerSuccess")
	public Object registerSuccess(final Model model){
		try {
			model.addAttribute("mobileUrl", ConfigUtils.getValue("mobile_url"));
		return "/app/invite/app_register_success";
		} catch (Exception e) {
			model.addAttribute("r_msg", e.getMessage());
			return ConfigUtils.getTppName()+"/retresult";
		}
	}
}
