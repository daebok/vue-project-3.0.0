package com.rd.ifaes.mobile.controller.realize;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.dict.InvestEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.TokenUtils;
import com.rd.ifaes.common.util.ip.IPUtil;
import com.rd.ifaes.core.cache.ProjectCache;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.Realize;
import com.rd.ifaes.core.project.model.ProjectInvestModel;
import com.rd.ifaes.core.project.service.ProductService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.project.service.ProjectTypeService;
import com.rd.ifaes.core.project.service.RealizeInvestService;
import com.rd.ifaes.core.project.service.RealizeRuleService;
import com.rd.ifaes.core.project.service.RealizeService;
import com.rd.ifaes.core.risk.domain.LevelConfig;
import com.rd.ifaes.core.risk.service.LevelConfigService;
import com.rd.ifaes.core.tpp.model.ufx.UfxInvestModel;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.domain.UserIdentify;
import com.rd.ifaes.core.user.model.UserFreezeModel;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserFreezeService;
import com.rd.ifaes.core.user.service.UserIdentifyService;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.mobile.controller.WebController;
import com.rd.ifaes.mobile.model.PagedData;
import com.rd.ifaes.mobile.model.realize.realizeItemModel;
import com.rd.ifaes.mobile.model.realize.realizedetailModel;

/**
 * 变现投资
 * 
 * @version 3.0
 * @author fxl
 * @date 2016年7月27日
 */
@Controller
public class RealizeController extends WebController {

	@Resource
	private transient RealizeService realizeService;
	@Resource
	private transient RealizeInvestService realizeInvestService;
	@Resource
	private transient RealizeRuleService realizeRuleService;
	@Resource
	private transient ProjectService projectService;
	@Resource
	private transient ProjectTypeService projectTypeService;
	@Resource
	private transient ProductService productService;
	@Resource
	private UserCacheService userCacheService;
	@Resource
	private UserFreezeService userFreezeService;
	@Resource
	private UserService userService;
	/**
	 * 认证信息类
	 */
	@Resource
	private transient UserIdentifyService identifyService;
	/**
	 * LevelConfigService业务
	 */
	@Resource
	private transient LevelConfigService levelConfigService;

	/**
	 * 变现项目列表
	 * 
	 * @author lgx
	 * @date 2016年11月24日
	 * @param realize
	 * @return
	 */
	@RequestMapping(value = "/app/open/index/realize/projectList")
	@ResponseBody
	public Object projectList(final Realize realize) {
		Object obj = null;
		try {
			PagedData<realizeItemModel> realizeitemlist = new PagedData<realizeItemModel>();
			realize.setUserId(null);
			realize.setUuid(null);
			realize.setStatusSet(
					new String[] { LoanEnum.STATUS_RAISING.getValue(), LoanEnum.STATUS_RAISE_FINISHED.getValue(),
							LoanEnum.STATUS_REPAY_START.getValue(), LoanEnum.STATUS_REPAYED_SUCCESS.getValue() });
			Page<Realize> realizepage = realizeService.findAllPageByCache(realize);
			List<Realize> realizelist = realizepage.getRows();
			fillPageData(realizeitemlist, realizepage);
			for (Realize realizemodel : realizelist) {
				realizeItemModel model = new realizeItemModel();
				model.setProjectId(realizemodel.getUuid());/* 项目ID */
				model.setProjectName(realizemodel.getProjectName()); /* 项目名称 */
				model.setApr(realizemodel.getApr());/* 年利率 */
				model.setRemainAccount(realizemodel.getRemainAccount());/* 剩余可投金额 */
				model.setTimeLimit(realizemodel.getTimeLimit());/* 借款期限 */
				model.setTimeType(
						realizemodel.getTimeType());/* 借款期限类型： 0月标 1天标 */
				model.setStatus(realizemodel
						.getStatus());/*
										 * 状态: 0 新增 1发布待审 2 发布审核成功(待售) 3发布审核失败4
										 * 募集中 5 满额待审（已售） 6 成立审核成功（还款中）8 还款中87
										 * 逾期中（还款中）88坏账（还款中） 90部分还款（还款中）
										 * 9还款成功（已完成）91逾期还款（已完成）
										 */
				model.setLowestAccount(realizemodel.getLowestAccount());// 最低起投金额
				model.setAccount(realizemodel.getAccount());// 项目总额

				model.setInvestId(realizemodel.getInvestId()); /* 投标ID */
				realizeitemlist.getList().add(model);
			}
			obj = createSuccessAppResponse(realizeitemlist);
		} catch (Exception e) {
			dealException(e);
		}
		return obj;
	}

	/**
	 * 变现详情数据(JSON)
	 * 
	 * @author lgx
	 * @date 2016年11月24日
	 * @return
	 */
	@RequestMapping(value = "/app/member/realize/detailInfo")
	@ResponseBody
	public Object detailInfo(final String projectId, final String investId, HttpServletRequest request) {
		Object obj = null;
		String tendercondition = "";
		String status = "";// 标的状态
		realizedetailModel model = new realizedetailModel();
		try {
			User user = getAppSessionUser(request);
			UserCache userCache = userCacheService.findByUserId(user.getUuid());
			 LevelConfig levelConfigUser = null;
				if(userCache.getRiskLevel()!=null&&!"".equals(userCache.getRiskLevel())){
					levelConfigUser=levelConfigService.findListByUserRiskLevelVal(NumberUtils.toInt(userCache.getRiskLevel()));
				}
			String userLevelName = levelConfigUser == null ? "" : levelConfigUser.getUserRiskLevelName();
			UserIdentify userIdentify = identifyService.findByUserId(user.getUuid());
			model.setRealNameStatus(userIdentify
					.getRealNameStatus());/** 实名认证 0:未认证 1：实名认证通过 -1：实名认证未通过 */
			String puserid = "";
			final Realize realize = realizeService.findRealizeFromCache(projectId);
			model.setProjectName(realize.getProjectName()); // 项目名称
			model.setApr(realize.getApr()); // 年利率
			model.setTimeType(realize.getTimeType());// 借款期限类型： 0月标 1天标
			model.setBorrowFlag(realize.getBorrowFlag()); // 借款标标识(1是，借款标，
															// 0否，即是理财产品）
			model.setTimeLimit(realize.getTimeLimit());// 借款期限
			model.setLowestAccount(realize.getLowestAccount());// 最低起投金额
			model.setMostAccount(realize.getMostAccount()); // 最高投资总额
			model.setTimeNow(System.currentTimeMillis());// 当前时间
			System.out.println(">>>>>>>>>>>>>时间："+model.getTimeNow());
			model.setInvestId(realize.getInvestId());/// * 投标ID */
			model.setRepayStyle(realize.getRepayStyle());// 还款方式
			model.setSaleEndTime(realize.getStopTime());/* 下架时间 */
			puserid = realize.getUserId();
			model.setRemainAccount(ProjectCache.getRemainAccount(realize.getUuid()));// 剩余可投金额
			model.setSpecificSale(realize.getSpecificSale());// 定向销售方式(0 不定向 1
																// 定向密码 2 定向等级 3
																// 定向邮箱域名，默认0)
			model.setSpecificSaleRule(realize
					.getSpecificSaleRule());/*
											 * 当specificSale为2或3，后台会根据当前登录用户再校验，
											 * 不匹配，该项内容为错误信息；匹配，该项为空
											 */
			model.setAccount(realize.getAccount());// 项目总额
			status = realize.getStatus();
			model.setOldProjectId(realize.getOriginalProjectId());// 变现原项目id
			model.setIsVouch(realize.getIsVouch() == null ? "0" : "1");// 是否有担保机构
																		// 是否担保(
																		// 1
																		// 担保，0
																		// 不担保）
			model.setProjectId(realize.getProjectId());//项目id
			model.setProtocolId(realize.getProtocolId());//协议id
			if ("1".equals(model.getIsVouch())) {
				User uservouch = userService.get(realize.getVouchId());
				model.setVouchName(uservouch.getRealName());// 担保机构名称
			} else {
				model.setVouchName("");
			}

			model.setAccounts("产品金额" + realize.getAccount().setScale(2, BigDecimal.ROUND_DOWN) + "元");// 项目总额说明
			if (!"".equals(realize.getInterestStyle())) {
				if ("1".equals(realize.getInterestStyle())) {
					model.setInterestStyle("成立计息");// 计息方式/* 计息方式: 1、成立计息
													// 2、T+N计息 */
				} else if ("2".equals(realize.getInterestStyle())) {
					model.setInterestStyle("T+" + realize.getInterestStartDays() + "计息");
				}
			}
			model.setRaiseTimeLimit(realize.getRaiseTimeLimit());// 募集期:
																	// 募集期时长，单位：
																	// 天
			model.setRaiseTimeLimits("剩余" + realize.getRaiseTimeLimit() + "天");// 募集期:募集期时长，
																				// 单位：天

			// 查询变现详情
			Map<String, Object> realizemap = realizeService.getDetailInfo(realize);
			Iterator<String> itr = realizemap.keySet().iterator();
			while (itr.hasNext()) {
				Object key = itr.next();
				if ("risk".equals(key)) {
					model.setRiskLevel("安全等级为" + (String) realizemap.get(key));// 安全等级
				}
				if ("buyStyle".equals(key)) {
					model.setBuyStyle((String) realizemap
							.get(key));/* 投资金额方式:0 部分投资;1全额投资,默认0 */
				}
				if ("investCondition".equals(key)) {
					model.setTenderCondition("投资条件为" + (String) realizemap.get(key) + "及以上");
					tendercondition = (String) realizemap.get(key);
				}
			}

			if (user != null) {
				@SuppressWarnings("unused")
				int notice = userCache.getInvestNum() == null ? Constant.INT_ONE
						: (userCache.getInvestNum() > Constant.INT_ZERO ? Constant.INT_ZERO : Constant.INT_ONE);// novice
																												// 为0
																												// 当前用户新手标识
			}

			Project projectmodel = new Project();
			projectmodel.setUuid(projectId);
			projectmodel.setStatus(LoanEnum.STATUS_RAISING.getValue());
			if (projectService.getCount(projectmodel) == Constant.INT_ZERO) {// 不在募集期
				model.setIfSaleStatus("0");
			} else {
				model.setIfSaleStatus("1");
			}
			if(projectService.getCount(projectmodel) == Constant.INT_ZERO){//如果不在募集期 剩余金额设为0
				model.setRemainAccount((double) 0);
			}
			/*
			 * 返回详情页 是否可点击 点击文本 优先级判断依次为 是否借款发布方 是否实名 是否抢光 当前用户是否投满 是否可投新手项目
			 * 是否可投定向项目 是否评测 是否绑定邮箱 是否定向邮箱域名符合
			 */
			if (user.getUuid().equals(puserid)) {// 是否借款发布方
				model.setIsClick("0");
				model.setClickTitle("当前是您的借款项目，无法投资");
				model.setClickCode("0");
			} else if (!"1".equals(model.getRealNameStatus())) {// 是否实名 如果没有实名成功
				model.setIsClick("1");// 是否可点击 0 否1是
				model.setClickTitle("去开通托管账户");// 按钮文案
				model.setClickCode("1");// 去开通托管账户！
			}else if (projectService.getCount(projectmodel) == Constant.INT_ZERO) {// 项目状态校验
				model.setIsClick("0");
				if ("7".equals(status)) {
					model.setClickTitle("产品已撤销");
				} else {
					model.setClickTitle("已抢光");
				}
				model.setClickCode("5");
				model.setRemainAccount((double) 0);
			}  else if (model.getRemainAccount() == 0) {
				model.setIsClick("0");
				model.setClickTitle("已抢光");
				model.setClickCode("5");
			} else if ("".equals(userCache.getRiskLevel()) || userCache.getRiskLevel() == null) {// 是否评测 如果没有评测
				model.setIsClick("1");
				model.setClickTitle("去评测风险承受能力");
				model.setClickCode("2");// 去开评测风险承受能力！
			} else if (userFreezeService.isFreezed(user.getUuid(), UserFreezeModel.STATUS_USER_FREEZE_INVEST)) {
				model.setIsClick("1");
				model.setClickTitle("立即投资");
				model.setClickCode("4");// 用户被冻结
			} else {
				model.setIsClick("1");
				model.setClickTitle("立即投资");
				model.setClickCode("10");
			}
			final Project project = projectService.getProjectByUuid(model.getOldProjectId());
			if ((NumberUtils.toInt(project.getRiskLevel()) - 1 > NumberUtils.toInt(userCache.getRiskLevel()))) {
				model.setIsTipe("1");
			} else {
				model.setIsTipe("0");
			}
			model.setUserLevelTitle("投资本项目所需风险承受能力为" + tendercondition + "及以"+"上，超出您的风险承受能力" + userLevelName+"。");/* 等级弹窗文本说明 */
			model.setUserLevelName(userLevelName);//当前用户风险承受能力
			model.setTenderConditionstr(tendercondition+"及以上");//投资条件 不加投资为
			if("7".equals(status)){//如果不在募集期 剩余金额设为0
				model.setRemainAccount(model.getAccount().doubleValue());
			}
			obj = createSuccessAppResponse(model);
		} catch (Exception e) {
			obj = dealException(e);
		}
		return obj;
	}
		/*
		 * 移动端 了解项目  APP
		 */
	@RequestMapping("/app/member/realize/appContent")
	public String appContent(String investId, final HttpServletRequest request, final Model model) {
		try {
			// 用户登录状态
			@SuppressWarnings("unused")
			User user = getAppSessionUser(request);
			// 查询原产品详情
			Map<String, Object> projectmap = projectService.getProductInfo(investId);
			Iterator<String> it = projectmap.keySet().iterator();
			while (it.hasNext()) {
				Object key = it.next();
				if ("project".equals(key)) {
					Project project = (Project) projectmap.get(key);
					model.addAttribute("projectName", project.getProjectName());// 借款标题
					model.addAttribute("Apr", project.getApr());// 预计年化利率
					model.addAttribute("repayStyle",
							getRepayStyle(project.getRepayStyle(), project.getFixedRepayDay()));// 还款方式:
																								// 1月等额本息
																								// 2一次性还款
																								// 3
																								// 每月还息到期还本
																								// 4
																								// 等额本金
					/*
					 * model.addAttribute("timeType",project.getTimeType());//
					 * 借款期限类型： 0月标 1天标
					 * model.addAttribute("timeLimit",project.getTimeLimit());//
					 * 借款期限
					 */
					if ("0".equals(project.getTimeType())) {// 月标
						model.addAttribute("timeLimitType", project.getTimeLimit() + "天");// 借款期限
					} else {
						model.addAttribute("timeLimitType", project.getTimeLimit() + "个月");// 借款期限
					}
				}
				if ("investAmount".equals(key)) {
					model.addAttribute("investAmount", (BigDecimal) projectmap.get(key));// 投资金额
				}
				if ("repayTime".equals(key)) {
					model.addAttribute("oldRepayTime", (String) projectmap.get(key));// 收款日
				}
			}
			return "/app/realize/realize_info";
		} catch (Exception e) {
			model.addAttribute("r_msg", e.getMessage());
			return ConfigUtils.getTppName() + "/retresult";
		}
	}
	/*
	 * 移动端 了解项目  微信
	 */
	@RequestMapping("/app/member/realize/wxContent")
	@ResponseBody
	public Object wxContent(String investId, final HttpServletRequest request) {
		Object obj=null;
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			// 用户登录状态
			@SuppressWarnings("unused")
			User user = getAppSessionUser(request);
			// 查询原产品详情
			Map<String, Object> projectmap = projectService.getProductInfo(investId);
			Iterator<String> it = projectmap.keySet().iterator();
			while (it.hasNext()) {
				Object key = it.next();
				if ("project".equals(key)) {
					Project project = (Project) projectmap.get(key);
					data.put("projectName", project.getProjectName());// 借款标题
					data.put("Apr", project.getApr());// 预计年化利率
					data.put("repayStyle",
							getRepayStyle(project.getRepayStyle(), project.getFixedRepayDay()));// 还款方式:
																								// 1月等额本息
																								// 2一次性还款
																								// 3
																								// 每月还息到期还本
																								// 4
																								// 等额本金
					/*
					 * model.addAttribute("timeType",project.getTimeType());//
					 * 借款期限类型： 0月标 1天标
					 * model.addAttribute("timeLimit",project.getTimeLimit());//
					 * 借款期限
					 */
					if ("0".equals(project.getTimeType())) {// 月标
						data.put("timeLimitType", project.getTimeLimit() + "天");// 借款期限
					} else {
						data.put("timeLimitType", project.getTimeLimit() + "个月");// 借款期限
					}
				}
				if ("investAmount".equals(key)) {
					data.put("investAmount", (BigDecimal) projectmap.get(key));// 投资金额
				}
				if ("repayTime".equals(key)) {
					data.put("oldRepayTime", (String) projectmap.get(key));// 收款日
				}
			}
			obj = createSuccessAppResponse(data);
		} catch (Exception e) {
			obj = dealException(e);
		}
		return obj;
	}

	public String getRepayStyle(String repayStyle, Integer fixedRepayDay) {
		String repayStylestr = "";
		if (!"".equals(repayStyle)) {
			if ("1".equals(
					repayStyle)) {/* 还款方式: 1月等额本息 2一次性还款 3 每月还息到期还本 4 等额本金 */
				repayStylestr = "等额本息";
			} else if ("2".equals(repayStyle)) {
				repayStylestr = "一次性还款";
			} else if ("3".equals(repayStyle)) {
				if (fixedRepayDay > 0) {
					repayStylestr = "每月还息到期还本(固定还款日" + fixedRepayDay + "日)";
				} else {
					repayStylestr = "每月还息到期还本";
				}
			} else if ("4".equals(repayStyle)) {
				repayStylestr = "等额本金";
			} else if ("5".equals(repayStyle)) {
				if (fixedRepayDay > 0) {
					repayStylestr = "每季还息到期还本(固定还款日" + fixedRepayDay + "日)";
				} else {
					repayStylestr = "每季还息到期还本";
				}
			}
		}
		return repayStylestr;
	}

	/**
	 * 变现投资
	 * 
	 * @author lgx
	 * @date 2016年11月25日
	 * @param invest
	 * @param model
	 * @return
	 */
	// http://localhost/realize/doRealizeInvest.html?amount=100&projectId=b005e5d3922940698925ebd9021193e7
	@RequestMapping("/app/member/realize/doRealizeInvest")
	@TokenValid(value = TokenConstants.TOKEN_INVEST, dispatch = true)
	public String doRealizeInvest(final ProjectInvestModel invest, final HttpServletRequest request,
			final Model model) {
		try {
			// 用户登录状态
			User user = getAppSessionUser(request);
			String userAgent = request.getHeader("User-Agent").toLowerCase();// 判断访问来源
																				// 是否是微信浏览器
			if("android".equals(userAgent)||"iphone".equals(userAgent)){//app端
				invest.setInvestChannel(InvestEnum.INVEST_CHANNEL_APP.getValue());
			}else {//微信端
				invest.setInvestChannel(InvestEnum.INVEST_CHANNEL_WECHAT.getValue());
			}
			invest.setUserId(user.getUuid());
			invest.setAddIp(IPUtil.getRemortIP(request));
			invest.setInvestType(InvestEnum.INVEST_TYPE_WEB.getValue());
			final UfxInvestModel ufxInvestModel = realizeInvestService.doRealizeInvest(invest);
			if(TokenUtils.validToken(request,TokenConstants.TOKEN_INVEST)){
		    	TokenUtils.clearToken(request, TokenConstants.TOKEN_INVEST);
		    }else{
		    	throw new BussinessException(ResourceUtils.get(ResourceConstant.REPEAT_SUBMIT_ERROR), BussinessException.TYPE_CLOSE);
		    }
			model.addAttribute("invest", ufxInvestModel);
			return "/ufx/invest";
		} catch (Exception e) {
			model.addAttribute("r_msg", e.getMessage());
			return ConfigUtils.getTppName() + "/retresult";
		}
	}

}
