/**
 * 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.mobile.controller.project;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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

import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.resource.LoanResource;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ProjectConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.project.domain.BorrowUpload;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectRepayment;
import com.rd.ifaes.core.project.exception.ProjectException;
import com.rd.ifaes.core.project.model.ProjectInvestModel;
import com.rd.ifaes.core.project.service.BorrowUploadService;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.project.service.ProjectRepaymentService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.risk.domain.LevelConfig;
import com.rd.ifaes.core.risk.service.LevelConfigService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserBaseInfo;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.domain.UserCompanyInfo;
import com.rd.ifaes.core.user.domain.UserIdentify;
import com.rd.ifaes.core.user.model.UserFreezeModel;
import com.rd.ifaes.core.user.model.UserQualificationApplyModel;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserCompanyInfoService;
import com.rd.ifaes.core.user.service.UserFreezeService;
import com.rd.ifaes.core.user.service.UserIdentifyService;
import com.rd.ifaes.core.user.service.UserQualificationApplyService;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.mobile.common.AppDataUtil;
import com.rd.ifaes.mobile.common.AppException;
import com.rd.ifaes.mobile.common.AppResultCode;
import com.rd.ifaes.mobile.controller.WebController;
import com.rd.ifaes.mobile.model.project.projectUplodeitemModel;
import com.rd.ifaes.mobile.model.project.projectdetailModel;

/**
 * 项目相关信息（详情、借款资料等） // *
 * 
 * @author FangJun
 * @date 2016年7月25日
 */
@Controller
public class ProjectController extends WebController {
	@Resource
	private ProjectService projectService;
	@Resource
	private BorrowUploadService borrowUploadService;
	@Resource
	private ProjectRepaymentService projectRepaymentService;
	@Resource
	private UserCacheService userCacheService;
	@Resource
	private UserFreezeService userFreezeService;
	 /**
     * 借款资质申请类
     */
	@Resource
	private transient UserQualificationApplyService applyService;
	/**
	 * LevelConfigService业务
	 */
	@Resource
	private transient LevelConfigService levelConfigService;
	/**
	 * 认证信息类
	 */
	@Resource
	private transient UserIdentifyService identifyService;
	@Resource
	private ProjectInvestService projectInvestService;
	@Resource
	private UserService userService;
	@Resource
	private UserCompanyInfoService userCompanyInfoService;

	/**
	 * 项目详情(JSON）
	 * 
	 * @param projectId
	 *            项目UUID
	 * @return
	 */
	@RequestMapping(value = "/app/member/project/detail")
	@ResponseBody
	public Object detail(final String projectId, HttpServletRequest request) {
		Object obj = null;
		projectdetailModel model = new projectdetailModel();
		String tendercondition = "";
		try {
			if (StringUtils.isNotBlank(projectId)) {
				User user = getAppSessionUser(request);
				UserIdentify userIdentify = identifyService.findByUserId(user.getUuid());
				UserCache userCache = userCacheService.findByUserId(user.getUuid());
				Map<String, Object> projectmap = projectService.getDetail(projectId, user.getUuid());
				Iterator<String> it = projectmap.keySet().iterator();
				model.setRealNameStatus(userIdentify.getRealNameStatus());/**
												 * 实名认证 0:未认证 1：实名认证通过 -1：实名认证未通过
												 */
				long timeToStart = 0;// 剩余开标时间
				String puserid = "";// 当前标的用户id
				String status = "";// 标的状态
				Map<String, Object> remainmap = null;
				while (it.hasNext()) {
					Object key = it.next();
					if ("risk".equals(key))
						model.setRiskLevel("安全等级为" + (String) projectmap.get(key));// 安全等级
					if ("project".equals(key)) {
						Project project = (Project) projectmap.get(key);
						model.setProjectName(project.getProjectTypeName() + "-" + project.getProjectNo());/* 项目名称 */
						model.setApr(project.getApr()); // 年利率
						model.setTimeType(project.getTimeType());// 借款期限类型： 0月标
																	// 1天标
						model.setVipLevel(project.getVipLevel());// 等级限制
						model.setTimeLimit(project.getTimeLimit());// 借款期限
						model.setLowestAccount(project.getLowestAccount());// 最低起投金额
						model.setLowestCopies(project.getLowestCopies());// 最低起投份数
						model.setTotalCopies(project.getTotalCopies()); // 总份数
						model.setCopyAccount(project.getCopyAccount()); // 每份金额
						model.setBorrowFlag(project.getBorrowFlag());// 借款标标识(1是，借款标，
																		// 0否，即是理财产品）
						model.setMostAccount(project.getMostAccount()); // 最高投资总额
						model.setMostCopies(project.getMostCopies()); // 最高投资份数
						model.setStopTime(project.getStopTime());// 下架时间
						model.setSaleEndTime(project.getSaleEndTime());// 销售结束时间
						model.setProtocolId(project.getProtocolId());//协议id
						status = project.getStatus();
						model.setIsVouch(project.getIsVouch() == null || "0".equals(project.getIsVouch()) ? "0" : "1");// 是否有担保机构
																														// 是否担保(
																														// 1
																														// 担保，0
																														// 不担保）
						if ("1".equals(model.getIsVouch())) {
							User uservouch = userService.get(project.getVouchId());
							model.setVouchName(uservouch.getRealName());// 担保机构名称
						} else {
							model.setVouchName("");
						}
						model.setRepayStyle(project.getRepayStyle() == null ? "0" : project.getRepayStyle(),
								project.getFixedRepayDay());// 还款方式
						if (null != project.getSaleTime() && project.getSaleTime().after(new Date())) {
							model.setIfSale("0");
						} else {
							model.setIfSale("1");// 已经开售
						}

						model.setAddApr(project.getAddApr());// 加息利率
						model.setBondUseful(project.getBondUseful() == null ? "0" : project.getBondUseful());// 是否可以债权转让
																												// 1
																												// 可以，0不可以
						model.setNovice(project.getNovice());// 新手标标识： 1新手专享 0
																// 普通 ，默认：0
						model.setRealizeUseful(project.getRealizeUseful());// 是否可变现:
																			// 1
																			// 可变现
																			// 0
																			// 不可变现，默认
																			// 0

						remainmap = projectService.getRemainAccount(projectId);
						Iterator<String> itr = remainmap.keySet().iterator();
						while (itr.hasNext()) {
							Object keyr = itr.next();
							if ("remainAccount".equals(keyr))
								model.setRemainAccount((Double) remainmap.get(keyr));// 剩余可投金额
						}
						model.setSaleTime(project.getSaleTime());// 预售时间
						model.setSpecificSale(project.getSpecificSale());// 定向销售方式(0
																			// 不定向
																			// 1
																			// 定向密码
																			// 2
																			// 定向等级
																			// 3
																			// 定向邮箱域名，默认0)
						model.setSpecificSaleRule(project
								.getSpecificSaleRule());/*
														 * 当specificSale为2或3，
														 * 后台会根据当前登录用户再校验，
														 * 不匹配，该项内容为错误信息；匹配，该项为空
														 */
						model.setAccount(project.getAccount());// 项目总额
						model.setAccounts("产品金额" + AppDataUtil.doubleFormat(project.getAccount()) + "元");// 项目总额说明
						if (!"".equals(project.getInterestStyle())) {
							if ("1".equals(project.getInterestStyle())) {
								model.setInterestStyle("成立计息");// 计息方式/* 计息方式:
																// 1、成立计息
																// 2、T+N计息 */
							} else if ("2".equals(project.getInterestStyle())) {
								model.setInterestStyle("T+" + project.getInterestStartDays() + "计息");
							}
						}
						model.setSaleStyle(project.getSaleStyle());// 销售规则 ：1
																	// 按金额
																	// 2按份数，默认1
						model.setRaiseTimeLimit(project.getRaiseTimeLimit());// 募集期:
																				// 募集期时长，单位：
																				// 天
						if ("0".equals(model.getIfSale())) {// 如果是预售 应该是
															// 开售时间减当前时间+募集时长
							int daynum = DateUtils.daysBetween(new Date(), project.getSaleTime()) + 1;
							model.setRaiseTimeLimits("剩余" + daynum + "天");
						} else if (project.getRaiseTimeLimit()!=null&&project.getRaiseTimeLimit() > 0) {
							model.setRaiseTimeLimits("剩余" + project.getRaiseTimeLimit() + "天");// 募集期:募集期时长，
																								// 单位：天
						} else {
							model.setRaiseTimeLimits("");// 0天为空
						}
						if (project.getSaleTime() != null) {
							Date start_time = project.getSaleTime();
							timeToStart = (start_time.getTime() - System.currentTimeMillis()) / 1000;
						}
						model.setTimeToStart(Math.max(timeToStart, 0));// 倒计时
						puserid = project.getUserId();
					}
					if ("investCondition".equals(key)) {
						model.setTenderCondition("投资条件为" + (String) projectmap.get(key) + "及以上");
						tendercondition = (String) projectmap.get(key);
					}
					if ("bespeak".equals(key))
						model.setBespeak((BigDecimal) projectmap.get(key));
					if ("bespeakNum".equals(key))
						model.setBespeaNum((Long) projectmap.get(key));
					if ("systemTime".equals(key))
						model.setTimeNow((long) projectmap.get(key));// 当前时间
				}

				ProjectInvestModel invest = new ProjectInvestModel();
				invest.setProjectId(projectId);
				if (user != null) {
					invest.setUserId(user.getUuid());
					model.setUserInvestAmount(projectInvestService.countUserInvestProject(invest));
				}
				Project projectmodel = new Project();
				projectmodel.setUuid(projectId);
				projectmodel.setStatus(LoanEnum.STATUS_RAISING.getValue());
				if (projectService.getCount(projectmodel) == Constant.INT_ZERO) {// 不在募集期
					model.setIfSaleStatus("0");
				} else {
					model.setIfSaleStatus("1");
				}
				int notice = 0;// 默认不是新手
				if (user != null) {
					notice = userCache.getInvestNum() == null ? Constant.INT_ONE
							: (userCache.getInvestNum() > Constant.INT_ZERO ? Constant.INT_ZERO : Constant.INT_ONE);// novice
																													// 为0
																													// 当前用户新手标识
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
				} else if (!"1".equals(model.getRealNameStatus())) {// 是否实名
																	// 如果没有实名成功
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
					model.setClickCode("8");
					model.setRemainAccount((double) 0);
				}  else if (model.getRemainAccount() == 0) {
					model.setIsClick("0");
					model.setClickTitle("已抢光");
					model.setClickCode("5");
				} else if (model.getMostAccount() == model.getUserInvestAmount()) {
					model.setIsClick("0");
					model.setClickTitle("您已达到最高可投金额");
					model.setClickCode("6");
				} else if ("1".equals(model.getNovice()) && notice == 0) {// 是否可投新手项目
																			// 如果是新手标
					model.setIsClick("0");
					model.setClickTitle("该产品仅新手可投资");
					model.setClickCode("9");
				} else if ("2".equals(model.getSpecificSale()) && model.getSpecificSaleRule() != null) {
						model.setIsClick("0");
						model.setClickTitle(model.getSpecificSaleRule());
						model.setClickCode("11");
				} else if ("3".equals(model.getSpecificSale()) && model.getSpecificSaleRule() != null) {
					if (!"1".equals(userIdentify.getEmailStatus())) {// 是否绑定邮箱
						model.setIsClick("1");
						model.setClickTitle("去绑定邮箱");
						model.setClickCode("3");// 去绑定邮箱！
					} else if (model.getSpecificSaleRule() != null) {
						model.setIsClick("0");
						model.setClickTitle(model.getSpecificSaleRule());
						model.setClickCode("11");
					}
				} else if ("".equals(userCache.getRiskLevel()) || userCache.getRiskLevel() == null) {// 是否评测
					model.setIsClick("1");
					model.setClickTitle("去评测风险承受能力");
					model.setClickCode("2");// 去开评测风险承受能力！
				} else if (model.getSpecificSaleRule() != null
						&& ("2".equals(model.getSpecificSale()) || "3".equals(model.getSpecificSale()))) {// 是否定向等级
					model.setIsClick("0");
					model.setClickTitle(model.getSpecificSaleRule());
					model.setClickCode("12");
				} else if (userFreezeService.isFreezed(user.getUuid(), UserFreezeModel.STATUS_USER_FREEZE_INVEST)) {
					model.setIsClick("1");
					model.setClickTitle("立即投资");
					model.setClickCode("4");// 用户被冻结
				} else if ("1".equals(model.getSpecificSale())) {// 需要定向密码
					model.setIsClick("1");
					model.setClickTitle("立即投资");
					model.setClickCode("7");
				} else if (!"1".equals(user.getPayPwd())) {// 需要设置密码
					model.setIsClick("1");
					model.setClickTitle("去设置支付密码");
					model.setClickCode("16");
				} else {
					model.setIsClick("1");
					model.setClickTitle("立即投资");
					model.setClickCode("10");
				}

				final Project project = projectService.getProjectByUuid(projectId);
				 LevelConfig levelConfigUser = null;
					if(userCache.getRiskLevel()!=null&&!"".equals(userCache.getRiskLevel())){
						levelConfigUser=levelConfigService.findListByUserRiskLevelVal(NumberUtils.toInt(userCache.getRiskLevel()));
					}
				if ((NumberUtils.toInt(project.getRiskLevel()) - 1 > NumberUtils.toInt(userCache.getRiskLevel()))) {
					if ("1".equals(model.getIsClick())) {
						model.setIsTipe("1");
					} else {
						model.setIsTipe("0");
					}
				} else {
					if (userCache.getRiskLevel() == null) {
						model.setIsTipe("1");
					} else {
						model.setIsTipe("0");
					}
				}
				String userLevelName = levelConfigUser == null ? "" : levelConfigUser.getUserRiskLevelName();
				/*
				 * if(userCache.getRiskLevel()==null&&"1".equals(model.getIsTipe
				 * ())){//犹豫可能有 缓存老数据迁徙的原因 特意加个判断 一般 是不会出现这种情况的
				 * model.setUserLevelTitle("首次投资请您完成风险承受能力评估测试");等级弹窗文本说明 }else{
				 */
				model.setUserLevelTitle("投资本项目所需风险承受能力为" + tendercondition + "及以上，超出您的风险承受能力" + userLevelName+"。");/* 等级弹窗文本说明 */
				model.setUserLevelName(userLevelName);//当前用户风险承受能力
				model.setTenderConditionstr(tendercondition+"及以上");//投资条件 不加投资为
				// }
				if("7".equals(status)){//如果不在募集期 剩余金额设为0
					model.setRemainAccount(model.getAccount().doubleValue());
				}
			} else {
				throw new AppException(AppResultCode.ERROR, "项目id不能为空");
			}
			obj = createSuccessAppResponse(model);
		} catch (Exception e) {
			obj = dealException(e);
		}
		return obj;
	}

	/**
	 * 移动端 项目详情、借款详情
	 * 
	 * @param projectId
	 *            项目UUID
	 * @return
	 */
	@RequestMapping(value = "/app/member/project/content")
	@ResponseBody
	public Object content(final String projectId, HttpServletRequest request) {
		Object obj = null;
		try {
			Map<String, Object> data = new HashMap<String, Object>();
			if (StringUtils.isNotBlank(projectId)) {
				@SuppressWarnings("unused")
				User user = getAppSessionUser(request);
				Map<String, Object> projectmap = projectService.getDetail(projectId, user.getUuid());
				Iterator<String> itprojectmap = projectmap.keySet().iterator();
				while (itprojectmap.hasNext()) {
					Object key = itprojectmap.next();
					if ("project".equals(key)) {
						Project project = (Project) projectmap.get(key);
						data.put("projectName",project.getProjectName());// 借款标题
					}
				}
				
				
				Map<String, Object> projectContent = projectService.getContent(projectId);
				Iterator<String> it = projectContent.keySet().iterator();
				while (it.hasNext()) {
					Object key = it.next();
					if ("content".equals(key))
						data.put("content", projectContent.get(key));// 借款描述
					if ("borrowUse".equals(key))
						data.put("borrowUse", projectContent.get(key));// 借款用途
				}
			} else {
				throw new AppException(AppResultCode.ERROR, "项目id不能为空");
			}
			obj = createSuccessAppResponse(data);
		} catch (Exception e) {
			obj = dealException(e);
		}
		return obj;
	}

	/**
	 * 移动端--app使用 项目详情、借款详情
	 * 
	 * @param projectId
	 *            项目UUID
	 * @return
	 */
	@RequestMapping(value = "/app/member/project/appContent")
	public Object appContent(String projectId, HttpServletRequest request, final Model model, String projectName,
			String borrowFlag) {
		try {
			if (StringUtils.isNotBlank(projectId)) {
				User user = getAppSessionUser(request);
				Map<String, Object> projectContent = projectService.getContent(projectId);
				Iterator<String> it = projectContent.keySet().iterator();
				while (it.hasNext()) {
					Object key = it.next();
					if ("content".equals(key))
						model.addAttribute("content", projectContent.get(key));// 借款描述
					if ("borrowUse".equals(key))
						model.addAttribute("borrowUse", projectContent.get(key));// 借款用途
				}
				model.addAttribute("mobileUrl", ConfigUtils.getValue("mobile_url"));
		
				model.addAttribute("borrowFlag", borrowFlag);/// * 借款标标识(1是，借款标，
																/// 0否，即是理财产品）
																/// */

				if ("1".equals(borrowFlag)) {
					Map<String, Object> projectmap = projectService.getDetail(projectId, user.getUuid());
					Iterator<String> itprojectmap = projectmap.keySet().iterator();
					while (itprojectmap.hasNext()) {
						Object key = itprojectmap.next();
						if ("project".equals(key)) {
							Project project = (Project) projectmap.get(key);
							model.addAttribute("projectName",project.getProjectName());// 借款标题
						}
					}
					
					// 借款人信息
					Map<String, Object> projectBorrowerInfo = projectService.getBorrowerInfo(projectId);
					Iterator<String> itb = projectBorrowerInfo.keySet().iterator();
					String province = "";
					String city = "";
					String userNature = "";//// 用户类型 1 个人用户、2 企业用户、3 担保机构
					while (itb.hasNext()) {
						Object key = itb.next();
						if ("idNo".equals(key))
							model.addAttribute("idNo", projectBorrowerInfo.get(key));// 证件号码
						if ("userNature".equals(key)) {
							userNature = (String) projectBorrowerInfo.get(key);
							model.addAttribute("userNature", userNature);// 1,个人用户
																			// 2,企业用户
						}
						if ("1".equals(userNature)) {// 个人用户
							if ("userName".equals(key))
								model.addAttribute("userName", projectBorrowerInfo.get(key));// 姓名
							if ("province".equals(key))
								province = (String) projectBorrowerInfo.get(key);// 户籍所在省
							if ("city".equals(key))
								city = (String) projectBorrowerInfo.get(key);// 户籍所在市
							if (!"".equals(city))
								model.addAttribute("provinceCity", province + "、" + city);// 户籍城市
							if ("baseInfo".equals(key)) {
								UserBaseInfo userBaseInfo = (UserBaseInfo) projectBorrowerInfo.get(key);
								model.addAttribute("education", getEducation(userBaseInfo.getEducation()));// 学历1：小学；2：初中；3：高中；4：中专；5：大专；6：本科；7：硕士；8：博士；9其他
								model.addAttribute("maritalStatus", getMaritalStatus(userBaseInfo.getMaritalStatus()));// 婚姻状况
																														// 0:未婚；1：已婚；2：离异；3：丧偶
								model.addAttribute("monthIncomeRange", getMonthIncomeRange(userBaseInfo.getMonthIncomeRange()));// 月收入范围
								model.addAttribute("workExperience",
										getWorkExperience(userBaseInfo.getWorkExperience()));// 工作年限
																								// 1：0-3年；2：3-5年；3:5-8年；4:8年以上
								String carStatus="";
								if(userBaseInfo.getCarStatus()==null){
									carStatus="";
								}else if("0".equals(userBaseInfo.getCarStatus())){
									carStatus="无";
								}else{
									carStatus="有";
								}
								String houseStatus="";
								if(userBaseInfo.getHouseStatus()==null){
									houseStatus="";
								}else if("0".equals(userBaseInfo.getHouseStatus())){
									houseStatus="无";
								}else{
									houseStatus="有";
								}
								model.addAttribute("carStatus", carStatus);// 车产有无
								model.addAttribute("houseStatus", houseStatus);// 房产有无
								final User users = userService.get(userBaseInfo.getUserId());
								String qualificationType="";
								String flag="0";
								List<UserQualificationApplyModel> qualificationList=applyService.getApplyFile(qualificationType,users);
								for(UserQualificationApplyModel amodel:qualificationList){
									if("1".equals(amodel.getStatus())){
										flag="1";
									}
								}
								model.addAttribute("qualificationList", qualificationList);
								model.addAttribute("flag", flag);
							}
						} else if ("2".equals(userNature)) {// 企业用户
							if ("baseInfo".equals(key)) {
								// UserCompanyInfo userCompanyInfo =
								// (UserCompanyInfo)
								// projectBorrowerInfo.get(key);
								@SuppressWarnings("unchecked")
								Map<String, Object> cominfo = ((Map<String, Object>) projectBorrowerInfo.get(key));
								Iterator<String> itc= cominfo.keySet().iterator();
								while (itc.hasNext()) {
									Object keyc = itc.next();
									if ("companyName".equals(keyc))
										model.addAttribute("companyName", (String) cominfo.get(keyc));// 企业名称
									if ("simpleName".equals(keyc))
									model.addAttribute("simpleName", (String) cominfo.get(keyc));// 企业简称
									if ("registeredCapital".equals(keyc))
									model.addAttribute("registeredCapital", (String) cominfo.get(keyc));// 注册资本
									if ("address".equals(keyc))
										model.addAttribute("address", (String) cominfo.get(keyc));// 注册地区
									if ("establishDate".equals(keyc))
										model.addAttribute("establishDate", (Date) cominfo.get(keyc));// 成立 时间
									if ("legalDelegate".equals(keyc))
										model.addAttribute("legalDelegate", (String) cominfo.get(keyc));// 法人代表
									if ("officeAddress".equals(keyc))
										model.addAttribute("officeAddress", (String) cominfo.get(keyc));// 办公地点
									if ("naturalPerson".equals(keyc))
										model.addAttribute("naturalPerson", (String) cominfo.get(keyc));// 自然人
									if ("legalPerson".equals(keyc))
										model.addAttribute("lgalPerson", (String) cominfo.get(keyc));// 法人代表
								}
								UserCompanyInfo userCompanyInfo = userCompanyInfoService
										.findByUserId((String) cominfo.get("userId"));
								
								final User users = userService.get(userCompanyInfo.getUserId());
								String qualificationType="";
								String flag="0";
								List<UserQualificationApplyModel> qualificationList=applyService.getApplyFile(qualificationType,users);
								for(UserQualificationApplyModel amodel:qualificationList){
									if("1".equals(amodel.getStatus())){
										flag="1";
									}
								}
								model.addAttribute("qualificationList", qualificationList);
								model.addAttribute("flag", flag);
							}
						}

					}

					List<BorrowUpload> borrowUploadlist = borrowUploadService.findBorrowPic(projectId);
					List<projectUplodeitemModel> proilist = new ArrayList<projectUplodeitemModel>();
					for (int i = 0; i < borrowUploadlist.size(); i++) {
						BorrowUpload borrowUpload = borrowUploadlist.get(i);
						projectUplodeitemModel imgmodel = new projectUplodeitemModel();
						imgmodel.setFileType(borrowUpload.getFileType());// 上传文件类型:1、借款资料；2、抵押物资料；3、其他
						if("1".equals(borrowUpload.getFileType())||"2".equals(borrowUpload.getFileType())){
						imgmodel.setFilePath(borrowUpload.getFilePath().replace(".jpg", "mobile.jpg"));// 上传文件路径
						}
						proilist.add(imgmodel);
					}
					model.addAttribute("imglist", proilist);
				}
			} else {
				throw new AppException(AppResultCode.ERROR, "项目id不能为空");
			}
			return "/app/invest/invest_info";
		} catch (Exception e) {
			if (e.getMessage() != null || !"".equals(e.getMessage())) {
				model.addAttribute("r_msg", e.getMessage());
			} else {
				model.addAttribute("r_msg", "系统异常");
			}
			return ConfigUtils.getTppName() + "/retresult";
		}
	}

	/**
	 * 借款人信息（基本信息、借款资质、担保机构）
	 * 
	 * @param projectId
	 *            项目UUID
	 * @return
	 */
	@RequestMapping(value = "/app/member/project/borrower")
	@ResponseBody
	public Object borrower(String projectId, HttpServletRequest request) {
		Object obj = null;
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			if (StringUtils.isNotBlank(projectId)) {
				@SuppressWarnings("unused")
				User user = getAppSessionUser(request);
				Map<String, Object> projectBorrowerInfo = projectService.getBorrowerInfo(projectId);
				Iterator<String> it = projectBorrowerInfo.keySet().iterator();
				String province = "";
				String city = "";
				String userNature = "";//// 用户类型 1 个人用户、2 企业用户、3 担保机构
				while (it.hasNext()) {
					Object key = it.next();
					if ("idNo".equals(key))
						data.put("idNo", projectBorrowerInfo.get(key));// 证件号码
					if ("userNature".equals(key)) {
						userNature = (String) projectBorrowerInfo.get(key);
						data.put("userNature", userNature);// 1,个人用户
																		// 2,企业用户
					}
					if ("1".equals(userNature)) {// 个人用户
						if ("userName".equals(key))
							data.put("userName", projectBorrowerInfo.get(key));// 姓名
						if ("province".equals(key))
							province = (String) projectBorrowerInfo.get(key);// 户籍所在省
						if ("city".equals(key))
							city = (String) projectBorrowerInfo.get(key);// 户籍所在市
						if (!"".equals(city))
							data.put("provinceCity", province + "、" + city);// 户籍城市
						if ("baseInfo".equals(key)) {
							UserBaseInfo userBaseInfo = (UserBaseInfo) projectBorrowerInfo.get(key);
							data.put("education", getEducation(userBaseInfo.getEducation()));// 学历1：小学；2：初中；3：高中；4：中专；5：大专；6：本科；7：硕士；8：博士；9其他
							data.put("maritalStatus", getMaritalStatus(userBaseInfo.getMaritalStatus()));// 婚姻状况
																													// 0:未婚；1：已婚；2：离异；3：丧偶
							data.put("monthIncomeRange", getMonthIncomeRange(userBaseInfo.getMonthIncomeRange()));// 月收入范围
							data.put("workExperience",
									getWorkExperience(userBaseInfo.getWorkExperience()));// 工作年限
																							// 1：0-3年；2：3-5年；3:5-8年；4:8年以上
							data.put("carStatus", userBaseInfo.getCarStatus());// 车产有无
							data.put("houseStatus", userBaseInfo.getHouseStatus());// 房产有无
							final User users = userService.get(userBaseInfo.getUserId());
							String qualificationType="";
							String flag="0";
							List<UserQualificationApplyModel> qualificationList=applyService.getApplyFile(qualificationType,users);
							for(UserQualificationApplyModel amodel:qualificationList){
								if("1".equals(amodel.getStatus())){
									flag="1";
								}
							}
							data.put("qualificationList", qualificationList);
							data.put("flag", flag);
						}
					} else if ("2".equals(userNature)) {// 企业用户
						if ("baseInfo".equals(key)) {
							// UserCompanyInfo userCompanyInfo =
							// (UserCompanyInfo)
							// projectBorrowerInfo.get(key);
							@SuppressWarnings("unchecked")
							Map<String, Object> cominfo = (Map<String, Object>) projectBorrowerInfo.get(key);
							Iterator<String> itc= cominfo.keySet().iterator();
							while (itc.hasNext()) {
								Object keyc = itc.next();
								if ("companyName".equals(keyc))
									data.put("companyName", (String) cominfo.get(keyc));// 企业名称
								if ("simpleName".equals(keyc))
									data.put("simpleName", (String) cominfo.get(keyc));// 企业简称
								if ("registeredCapital".equals(keyc))
									data.put("registeredCapital", (String) cominfo.get(keyc));// 注册资本
								if ("address".equals(keyc))
									data.put("address", (String) cominfo.get(keyc));// 注册地区
								if ("establishDate".equals(keyc))
									data.put("establishDate", (Date) cominfo.get(keyc));// 成立 时间
								if ("legalDelegate".equals(keyc))
									data.put("legalDelegate", (String) cominfo.get(keyc));// 法人代表
								if ("officeAddress".equals(keyc))
									data.put("officeAddress", (String) cominfo.get(keyc));// 办公地点
								if ("naturalPerson".equals(keyc))
									data.put("naturalPerson", (String) cominfo.get(keyc));// 自然人
								if ("legalPerson".equals(keyc))
									data.put("lgalPerson", (String) cominfo.get(keyc));// 法人代表
							}
							UserCompanyInfo userCompanyInfo = userCompanyInfoService
									.findByUserId((String) cominfo.get("userId"));
							
							final User users = userService.get(userCompanyInfo.getUserId());
							String qualificationType="";
							String flag="0";
							List<UserQualificationApplyModel> qualificationList=applyService.getApplyFile(qualificationType,users);
							for(UserQualificationApplyModel amodel:qualificationList){
								if("1".equals(amodel.getStatus())){
									flag="1";
								}
							}
							data.put("qualificationList", qualificationList);
							data.put("flag", flag);
						}
					}

					obj = createSuccessAppResponse(data);
				}
			} else {
				throw new AppException(AppResultCode.ERROR, "项目id不能为空");
			}
		} catch (Exception e) {
			obj = dealException(e);
		}
		return obj;
	}

	// 学历1：小学；2：初中；3：高中；4：中专；5：大专；6：本科；7：硕士；8：博士；9其他
	public String getEducation(String education) {
		String educations = "";
		if ("11".equals(education)){
			educations = "幼稚园";
	   }
		if ("1".equals(education)){
			educations = "小学";}
	    if ("2".equals(education)){
			educations = "初中";}
	    if ("4".equals(education)){
			educations = "中专";}
	    if ("3".equals(education)){
			educations = "高中";}
	    if ("5".equals(education)){
			educations = "大专";}
	    if ("6".equals(education)){
			educations = "本科";}
	    if ("7".equals(education)){
			educations = "硕士";}
	    if ("8".equals(education)){
			educations = "博士";}
	    if ("9".equals(education)){
			educations = "其他";}
	    if ("10".equals(education)){
			educations = "博士后";}
		return educations;
	}

	// 婚姻状况 0:未婚；1：已婚；2：离异；3：丧偶
	public String getMaritalStatus(String maritalStatus) {
		String maritalStatusstr = "";
		if ("0".equals(maritalStatus))
			maritalStatusstr = "未婚";
		if ("1".equals(maritalStatus))
			maritalStatusstr = "已婚";
		if ("2".equals(maritalStatus))
			maritalStatusstr = "离异";
		if ("3".equals(maritalStatus))
			maritalStatusstr = "丧偶";
		return maritalStatusstr;
	}

	// 2万以下、2-4万、4-8万、8-10万、10-15万、15-20万、20-30万、30-50万、50-80万、80-100万、100万以上
	public String getMonthIncomeRange(String monthIncomeRange) {
		String workExperiencestr = "";
		if ("1".equals(monthIncomeRange))
			workExperiencestr = "2万以下/年";
		if ("2".equals(monthIncomeRange))
			workExperiencestr = "2-4万/年";
		if ("3".equals(monthIncomeRange))
			workExperiencestr = "4-8万/年";
		if ("4".equals(monthIncomeRange))
			workExperiencestr = "8-10万/年";
		if ("5".equals(monthIncomeRange))
			workExperiencestr = "10-15万/年";
		if ("6".equals(monthIncomeRange))
			workExperiencestr = "15-20万/年";
		if ("7".equals(monthIncomeRange))
			workExperiencestr = "20-30万/年";
		if ("8".equals(monthIncomeRange))
			workExperiencestr = "30-50万/年";
		if ("9".equals(monthIncomeRange))
			workExperiencestr = "50-80万/年";
		if ("10".equals(monthIncomeRange))
			workExperiencestr = "80-100万/年";
		if ("11".equals(monthIncomeRange))
			workExperiencestr = "100万以上/年";
		return workExperiencestr;
	}
	// 工作年限 1：0-3年；2：3-5年；3:5-8年；4:8年以上
	public String getWorkExperience(String workExperience) {
		String workExperiencestr = "";
		if ("1".equals(workExperience))
			workExperiencestr = "0-3年";
		if ("2".equals(workExperience))
			workExperiencestr = "3-5年";
		if ("3".equals(workExperience))
			workExperiencestr = "5-8年";
		if ("4".equals(workExperience))
			workExperiencestr = "8年";
		if ("5".equals(workExperience))
			workExperiencestr = "10年以上";
		if ("6".equals(workExperience))
			workExperiencestr = "10年以下";
		return workExperiencestr;
	}

	/**
	 * 借款资料
	 * 
	 * @param projectId
	 *            借款UUID
	 * @return
	 */
	@RequestMapping(value = "/app/member/project/borrowPic")
	@ResponseBody
	public Object borrowPic(String projectId, HttpServletRequest request) {
		Map<String, Object> data = new HashMap<String, Object>();
		Object obj = null;
		try {
			if (StringUtils.isNotBlank(projectId)) {
				@SuppressWarnings("unused")
				User user = getAppSessionUser(request);
				List<BorrowUpload> borrowUploadlist = borrowUploadService.findBorrowPic(projectId);
				List<projectUplodeitemModel> proilist = new ArrayList<projectUplodeitemModel>();
				for (int i = 0; i < borrowUploadlist.size(); i++) {
					BorrowUpload borrowUpload = borrowUploadlist.get(i);
					projectUplodeitemModel model = new projectUplodeitemModel();
					model.setFileType(borrowUpload.getFileType());// 上传文件类型:1、借款资料；2、抵押物资料；3、其他
					if("1".equals(borrowUpload.getFileType())||"2".equals(borrowUpload.getFileType())){
					model.setFilePath(borrowUpload.getFilePath().replace(".jpg", "mobile.jpg"));// 上传文件路径
					}
					proilist.add(model);
				}
				data.put("list", proilist);
			} else {
				throw new AppException(AppResultCode.ERROR, "项目id不能为空");
			}
			obj = createSuccessAppResponse(data);
		} catch (Exception e) {
			obj = dealException(e);
		}
		return obj;
	}

	/**
	 * 指定项目的还款计划
	 * 
	 * @author FangJun
	 * @date 2016年8月29日
	 * @param projectId
	 *            项目UUID
	 * @return
	 */
	@RequestMapping(value = "/project/repaymentList")
	@ResponseBody
	public Object getProjectRepaymentList(ProjectRepayment model) {
		Map<String, Object> data = new HashMap<>();
		data.put("result", true);
		data.put(RESULT_PAGE, projectRepaymentService.findByProjectId(model));
		return data;
	}

	/**
	 * 移动端 检查输入的定向密码正确性
	 * 
	 * @author lgx
	 * @param projectId
	 *            项目UUID
	 * @param pwd
	 *            输入的定向密码
	 * @return 校验结果
	 */
	@RequestMapping(value = "/app/open/project/checkPwd")
	@ResponseBody
	public Object checkPwd(String projectId, String pwd, HttpServletRequest request) {
		Object obj = null;
		try {
			if (StringUtils.isBlank(pwd)) {
				throw new BussinessException(ResourceUtils.get(LoanResource.SALE_PASS_ERROR),
						BussinessException.TYPE_JSON);
			}
			if (StringUtils.isNotBlank(projectId)) {
				final Project project = projectService.get(projectId);
				if (project == null) {
					throw new BussinessException(ResourceUtils.get(LoanResource.PROJECT_NOT_EXISTS),
							BussinessException.TYPE_JSON);
				}
				if (LoanEnum.SPECIFIC_SALE_PASSWORD.eq(project.getSpecificSale())) {
					//校验定向密码
					  if(!project.getSpecificSaleRule().equals(pwd) ){
						 request.getSession().removeAttribute(String.format(ProjectConstant.PROJECT_PASS_RIGHT_PROJECT_ID, projectId));
						 throw new ProjectException(ResourceUtils.get(LoanResource.SALE_PASS_ERROR), BussinessException.TYPE_JSON);
					}
					
					request.getSession().setAttribute(String.format(ProjectConstant.PROJECT_PASS_RIGHT_PROJECT_ID, projectId), true);
				}
			} else {
				throw new BussinessException(ResourceUtils.get(LoanResource.PROJECT_ID_IS_NULL),
						BussinessException.TYPE_JSON);
			}
			obj = createSuccessAppResponse("校验成功！");
		} catch (Exception e) {
			obj = dealException(e);
		}
		return obj;
	}
}