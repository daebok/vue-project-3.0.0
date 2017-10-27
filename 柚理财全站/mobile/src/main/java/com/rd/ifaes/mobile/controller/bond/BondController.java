/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.mobile.controller.bond;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.account.domain.Account;
import com.rd.account.model.AccountQueryModel;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.dict.BondInvestEnum;
import com.rd.ifaes.common.dict.InvestEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TokenUtils;
import com.rd.ifaes.common.util.ip.IPUtil;
import com.rd.ifaes.common.util.resource.BondResource;
import com.rd.ifaes.common.util.resource.LoanResource;
import com.rd.ifaes.core.bond.domain.Bond;
import com.rd.ifaes.core.bond.model.BondInvestModel;
import com.rd.ifaes.core.bond.model.BondModel;
import com.rd.ifaes.core.bond.service.BondInvestService;
import com.rd.ifaes.core.bond.service.BondRuleService;
import com.rd.ifaes.core.bond.service.BondService;
import com.rd.ifaes.core.cache.InvestCache;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.URLConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.risk.domain.LevelConfig;
import com.rd.ifaes.core.risk.service.LevelConfigService;
import com.rd.ifaes.core.tpp.model.jx.JxCreditInvestModel;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.domain.UserIdentify;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserIdentifyService;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.mobile.common.AppDataUtil;
import com.rd.ifaes.mobile.common.AppException;
import com.rd.ifaes.mobile.common.AppResultCode;
import com.rd.ifaes.mobile.controller.WebController;
import com.rd.ifaes.mobile.model.PagedData;
import com.rd.ifaes.mobile.model.bond.BondToBondModel;
import com.rd.ifaes.mobile.model.bond.bondItemModel;
import com.rd.ifaes.mobile.model.bond.bondItemdetailModel;
import com.rd.ifaes.mobile.model.bond.bondListItemModel;

/**
 * 债权专区 和 债权详情
 * 
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年7月26日
 */
@Controller
public class BondController extends WebController {
	/**
	 * LevelConfigService业务
	 */
	@Resource
	private transient LevelConfigService levelConfigService;
	/**
	 * 债权规则业务
	 */
	@Resource
	private transient BondRuleService bondRuleService;

	/**
	 * 债权投资业务
	 */
	@Resource
	private transient BondInvestService bondInvestService;

	/**
	 * 债权业务
	 */
	@Resource
	private transient BondService bondService;
	@Resource
	private AccountService accountService;
	/**
	 * 认证信息类
	 */
	@Resource
	private transient UserIdentifyService identifyService;
	@Resource
	private transient UserCacheService userCacheService;
	/**
	 * 借款标业务
	 */
	@Resource
	private transient ProjectService projectService;
	@Resource
	private UserService userService;
	/**
	 * 移动端 获取债权专区数据
	 * 
	 * @author LGX
	 * @date 2016年10月25日
	 * @param model
	 * @return
	 */
	@RequestMapping("/app/open/bond/bondListData")
	@ResponseBody
	public Object bondListData(BondModel bondModel) {
		PagedData<bondItemModel> pirlist = new PagedData<bondItemModel>();
		Object obj = null;
		try {
			bondModel.setUserId(null);
			bondModel.setUuid(null);
			Page<BondModel> bondmode = bondService.findPageData(bondModel);
			List<BondModel> bondmoellist = bondmode.getRows();
			fillPageData(pirlist, bondmode);
			for (BondModel bondModels : bondmoellist) {
				bondItemModel model = new bondItemModel();
				model.setProjectId(bondModels.getUuid());// 项目id
				model.setApr(bondModels.getApr());// 利率
				model.setBondApr(bondModels.getBondApr());// 折溢价率
				model.setBondMoney(bondModels.getBondMoney());// 债权金额
				model.setBondName(bondModels.getBondName());// 债权名称
				model.setRemainDays(bondModels.getRemainDays());// 剩余期限（天）
				model.setStartPeriod(bondModels.getStartPeriod());// 开始期数
				model.setStatus(bondModels.getStatus());// 债权状态:发布0，
														// 转让完成3，自动撤回4，后台撤回5
				model.setLimitHours(bondModels.getLimitHours());// 剩余时长
				model.setSuccessTime(bondModels.getSuccessTime());// 成功时间
				model.setRemainMoney(bondModels.getRemainMoney());// 已售债权本金
				pirlist.getList().add(model);
			}
			obj = createSuccessAppResponse(pirlist);
		} catch (Exception e) {
			obj = dealException(e);
		}
		return obj;
	}

	/**
	 * 移动端 债权详情页面上的债权和项目的详情
	 * 
	 * @author LGX
	 * @date 2016年10月25日
	 * @return
	 */
	@RequestMapping(value = "/app/member/bond/getBondProjectDetail")
	@ResponseBody
	public Object getBondProjectDetail(final String projectId, HttpServletRequest request) {
		Object obj = null;
		String tendercondition = "";
		try {
			if (StringUtils.isNotBlank(projectId)) {
				User user = getAppSessionUser(request);
				UserCache userCache = userCacheService.findByUserId(user.getUuid());
				 LevelConfig levelConfigUser = null;
				if(userCache.getRiskLevel()!=null&&!"".equals(userCache.getRiskLevel())){
					levelConfigUser=levelConfigService.findListByUserRiskLevelVal(NumberUtils.toInt(userCache.getRiskLevel()));
				}
				String userLevelName = levelConfigUser == null ? "" : levelConfigUser.getUserRiskLevelName();
				String backUrl = URLConstant.BOND_DETAIL_PAGE_PREFIX + projectId;
				UserIdentify userIdentify = identifyService.findByUserId(user.getUuid());
				Map<String, Object> bondmap = bondService.getBondProjectDetail(projectId, backUrl);
				Iterator<String> it = bondmap.keySet().iterator();
				bondItemdetailModel model = new bondItemdetailModel();
				model.setRealNameStatus(userIdentify
						.getRealNameStatus());/**
												 * 实名认证 0:未认证 1：实名认证通过
												 * -1：实名认证未通过
												 */
				String puserid = "";// 当前标的用户id
				String projectUserId="";//当前标的借款用户
				while (it.hasNext()) {
					Object key = it.next();
					if ("bondName".equals(key))
						model.setBondName((String) bondmap.get(key));// 债权名称
					if ("bondApr".equals(key))
						model.setBondApr((BigDecimal) bondmap.get(key));// 折溢价率
					if ("bondMoney".equals(key)) {
						model.setBondMostMoney((BigDecimal) bondmap.get(key));// 债权总额
						model.setBondMostMoneys(
								"债权金额" + AppDataUtil.doubleFormat((BigDecimal) model.getBondLowestMoney()) + "元");// 项目总额说明
					}
					if ("bondLowestMoney".equals(key))
						model.setBondLowestMoney((BigDecimal) bondmap.get(key));// 起投金额
					if ("remainDay".equals(key)) {
						model.setRemainDay((int) bondmap.get(key));// 剩余期限天数
						model.setRemainDays("剩余" + model.getRemainDay() + "天");// 剩余期限天数
																				// 说明
					}
					if ("interestStyle".equals(key)) {
						model.setInterestStyle((String) bondmap.get(key));// 计息方式
					}
					if ("repayStyle".equals(key))
						model.setRepayStyle((String) bondmap.get(key));// 还款方式
					if ("riskLevel".equals(key)) {
						model.setRiskLevel("安全等级为" + (String) bondmap.get(key));// 安全等级
						// model.setRiskLevels("安全等级为"+(String)
						// bondmap.get(key));//安全等级
					}
					if ("tenderCondition".equals(key)) {
						model.setTenderCondition("投资条件为" + (String) bondmap.get(key) + "及以上");// 投资条件
						tendercondition = (String) bondmap.get(key);
					}
					if ("deadTime".equals(key))
						// model.setDataTime((Date) bondmap.get(key));//剩余时间 小时
						model.setSaleEndTime((Date) bondmap.get(key));
					if ("currentTime".equals(key))
						model.setSaleEndTime((Date) bondmap.get(key));
					if ("status".equals(key))
						model.setStatus((String) bondmap.get(key));// 债权状态:发布0，
																	// 转让完成3，自动撤回4，后台撤回5
					if ("apr".equals(key))
						model.setApr((BigDecimal) bondmap.get(key));// 利率
					if ("remainMoney".equals(key))
						model.setRemainMoney((Double) bondmap.get(key));// 剩余金额
					if ("sellStyle".equals(key))
						model.setSellStyle((String) bondmap.get(key));// 转让金额方式:0
																		// 部分转让;1全额转让,默认0
					if ("userId".equals(key))
						model.setSellStyle((String) bondmap.get(key));// 转让金额方式:0
																		// 部分转让;1全额转让,默认0
					if ("projectId".equals(key))
						model.setOldProjectId((String) bondmap.get(key));
					if ("bondUserId".equals(key))
						puserid = (String) bondmap.get(key);
					if ("projectUserId".equals(key))
						projectUserId = (String) bondmap.get(key);
					if ("protocolId".equals(key))
						model.setProtocolId((String) bondmap.get(key));//协议id
				}

				Project proj = projectService.get(model.getOldProjectId());
				model.setBorrowFlag(proj.getBorrowFlag());

				if (user != null) {
					@SuppressWarnings("unused")
					int	notice = userCache.getInvestNum() == null ? Constant.INT_ONE
							: (userCache.getInvestNum() > Constant.INT_ZERO ? Constant.INT_ZERO : Constant.INT_ONE);// novice
				}

				/*
				 * 返回详情页 是否可点击 点击文本 优先级判断依次为 是否借款发布方 是否实名 是否抢光 当前用户是否投满 是否可投新手项目
				 * 是否可投定向项目 是否评测 是否绑定邮箱 是否定向邮箱域名符合
				 */
				if(user.getUuid().equals(projectUserId)){
					model.setIsClick("0");
					model.setClickTitle("借款用户无法进行投资");
					model.setClickCode("0");
				}else if (user.getUuid().equals(puserid)) {// 是否借款发布方
					model.setIsClick("0");
					model.setClickTitle("当前是您的转让项目，无法投资");
					model.setClickCode("0");
				} else if (!"1".equals(model.getRealNameStatus())) {// 是否实名
																	// 如果没有实名成功
					model.setIsClick("1");// 是否可点击 0 否1是
					model.setClickTitle("去开通托管账户");// 按钮文案
					model.setClickCode("1");// 去开通托管账户！
				} else if (model.getRemainMoney() == 0) {
					model.setIsClick("0");
					if ("4".equals(model.getStatus()) || "5".equals(model.getStatus())) {
						model.setClickTitle("债权不存在");
					} else {
						model.setClickTitle("已抢光");
					}
					model.setClickCode("5");
				} else if ("".equals(userCache.getRiskLevel()) || userCache.getRiskLevel() == null) {// 是否评测 如果没有评测
					model.setIsClick("1");
					model.setClickTitle("去评测风险承受能力！");
					model.setClickCode("2");// 去评测风险承受能力！
				} else if (!"1".equals(user.getPayPwd())) {// 需要设置密码
					model.setIsClick("1");
					model.setClickTitle("去设置支付密码");
					model.setClickCode("16");
				} else {
					model.setIsClick("1");
					model.setClickTitle("立即转入");
					model.setClickCode("10");
				}

				final Project project = projectService.getProjectByUuid(model.getOldProjectId());
				if (project.getRiskLevel() != null || !"".equals(project.getRiskLevel())) {
					if ((NumberUtils.toInt(project.getRiskLevel()) - 1 > NumberUtils.toInt(userCache.getRiskLevel()))) {
						model.setIsTipe("1");
					} else {
						model.setIsTipe("0");
					}
				} else {
					model.setIsTipe("0");
				}
				model.setUserLevelTitle("投资本项目所需风险承受能力为" + tendercondition + "及以上，超出您的风险承受能力" + userLevelName+"。");/* 等级弹窗文本说明 */
				model.setTimeNow(System.currentTimeMillis());// 当前时间
				model.setUserLevelName(userLevelName);//当前用户风险承受能力
				model.setTenderConditionstr(tendercondition+"及以上");//投资条件 不加投资为
				obj = createSuccessAppResponse(model);
			} else {
				throw new AppException(AppResultCode.ERROR, "项目id不能为空");
			}

		} catch (Exception e) {
			obj = dealException(e);
		}
		return obj;
	}

	/**
	 * 根据输入金额获取预期收益和实际支付金额
	 * 
	 * @author QianPengZhan
	 * @date 2016年8月2日
	 * @param amount
	 *            输入的受让本金
	 * @param id
	 *            债权标ID
	 * @return
	 */
	@RequestMapping(value = "/app/member/bond/getInvestData")
	@ResponseBody
	public Object getInvestData(@RequestParam("amount") final BigDecimal amount, @RequestParam("id") final String id) {
		Object obj = null;
		try {
			obj = createSuccessAppResponse(bondService.getInvestData(amount, id));
		} catch (Exception e) {
			obj = dealException(e);
		}
		return obj;
	}

	/**
	 * 移动端-获取债权投资记录的接口
	 * 
	 * @author liugx
	 * @date 2017年1月10日
	 * @param id
	 *            债权标ID
	 * @return
	 */
	@RequestMapping(value = "/app/member/bond/getBondInvestPage")
	@ResponseBody
	public Object getBondInvestPage(final String projectId,HttpServletRequest request) {
		Object obj = null;
		try {
			@SuppressWarnings("unused")
			User user = getAppSessionUser(request);
			PagedData<bondListItemModel> bondListItemModellist=new PagedData<bondListItemModel>();
			if (StringUtils.isNotBlank(projectId)) {
				final BondInvestModel entity = new BondInvestModel();
				entity.setBondId(projectId);
				entity.setStatus(BondInvestEnum.STATUS_COMPLETE.getValue());
				Page<BondInvestModel> bondeinvestmodelpage = bondInvestService.findPageModel(entity);
				List<BondInvestModel> bondInvestModellist = bondeinvestmodelpage.getRows();
				fillPageData(bondListItemModellist, bondeinvestmodelpage);
				bondListItemModel model = new bondListItemModel();
				PagedData<bondListItemModel> pirlist = new PagedData<bondListItemModel>();
				for (BondInvestModel bondInvestModel : bondInvestModellist) {
					model.setCreateTime(bondInvestModel.getCreateTime());// 投资时间
					model.setMoney(bondInvestModel.getAmount());// 投资金额
					model.setUserName(bondInvestModel.getUserName());// 马沙克受让人
					pirlist.getList().add(model);
				}
				obj = createSuccessAppResponse(pirlist);
			} else {
				throw new BussinessException(ResourceUtils.get(BondResource.BOND_ID_NOT_EXISTS),
						BussinessException.TYPE_JSON);
			}
		} catch (Exception e) {
			obj = dealException(e);
		}
		return obj;
	}

	/**
	 * 进入债权订单确认页面
	 * 
	 * @author QianPengZhan
	 * @date 2016年8月3日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/bond/order")
	public String order(final BondInvestModel invest, final Model model) {
		final String backUrl = URLConstant.BOND_DETAIL_PAGE_PREFIX + invest.getBondId();
		// 1、 用户登录状态
		final User user = (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
		if (user == null) {
			return "/user/login";
		}
		invest.setUserId(user.getUuid());

		// 2、校验信息
		final Bond bond = bondService.checkBondInfo(invest, backUrl);
		// 查询其他信息 缓存获取
		final Project project = projectService.getProjectValid(bond.getProjectId(), backUrl);

		// 3、风险评估校验
		bondInvestService.validUserRiskTip(invest, user, project, backUrl);
		final BondModel bondModel = bondInvestService.getBondModel(bond, project);

		// 4、债权规则 是否全部受让
		final boolean isBuyAll = bondModel.judgeIfBuyAll();

		// 5、freemarker渲染页面
		model.addAttribute("investTimeOut",
				ConfigUtils.getInt(ConfigConstant.INVEST_TIMEOUT, ConfigConstant.INVEST_DEFAULT_ORDER_TIME_OUT));// 订单的时效
																													// 默认20分钟
																													// 不能低于10分钟
		model.addAttribute("project", project);
		model.addAttribute("bondModel", bondModel);
		model.addAttribute("amount", invest.getAmount());
		model.addAttribute("receivedAccount", invest.getReceivedAccount());
		model.addAttribute("isBuyAll", isBuyAll);// 是否全部受让
		return "/bond/order";
	}

	/**
	 * 移动端 受让人进行债权投资初始化
	 * 
	 * @author lgx
	 * @date 2016年11月3日
	 * @return
	 */
	@RequestMapping(value = "/app/member/bond/toBondInvest", method = RequestMethod.POST)
	@ResponseBody
	public Object toBondInvest(final String bondId, HttpServletRequest request) {
		BondToBondModel model = new BondToBondModel();
		Object obj = null;
		try {
			User user = getAppSessionUser(request);
			user = userService.get(user.getUuid());//更新user
			//更新缓存
			SessionUtils.setSessionAttr(Constant.SESSION_USER, user);
			if (StringUtils.isNotBlank(bondId)) {
				mobileCheckFreeze(user.getUuid(), "bond");
				String backUrl = URLConstant.BOND_DETAIL_PAGE_PREFIX + bondId;
				Map<String, Object> bondmap = bondService.getBondProjectDetail(bondId, backUrl);
				Iterator<String> it = bondmap.keySet().iterator();
				String RepayStyle = "";
				// 获得资金详情
				Account account = accountService.getByUserId(
						new AccountQueryModel(user.getUuid(), ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE)));
				model.setUserMoney(account.getUseMoney());
				model.setDaysOfYear(ConfigUtils.getValue("days_of_year"));//一年？天
				while (it.hasNext()) {
					Object key = it.next();
					if ("bondApr".equals(key))
						model.setBondApr((BigDecimal) bondmap.get(key));// 折溢价率
					if ("bondMostMoney".equals(key))
						model.setBondMostMoney((BigDecimal) bondmap.get(key));// 债权总额
					if ("bondLowestMoney".equals(key))
						model.setBondLowestMoney((BigDecimal) bondmap.get(key));// 起投金额
					if ("repayStyle".equals(key))
						RepayStyle = (String) bondmap.get(key);// 还款方式
					if ("apr".equals(key))
						model.setApr((BigDecimal) bondmap.get(key));// 利率
					if ("remainMoney".equals(key))
						model.setRemainMoney((Double) bondmap.get(key));// 剩余金额
					if ("sellStyle".equals(key))
						model.setSellStyle((String) bondmap.get(key));// 转让金额方式:0
																		// 部分转让;1全额转让,默认0
					if ("projectId".equals(key))
						model.setProjectId((String) bondmap.get(key));// 项目id
					if ("investId".equals(key))
						model.setInvestId((String) bondmap.get(key));// 投标
					if ("bondName".equals(key))
						model.setProjectName((String) bondmap.get(key));// 标名称
					if ("protocolId".equals(key))
						model.setProtocolId((String) bondmap.get(key));//协议id
				}
				if ("月等额本息".equals(RepayStyle) || "月等额本金".equals(RepayStyle)) {// 还款方式为月等额本息、月等额本金则必须一次性全额受让
					model.setSellStyle("1");
				}
				model.setBondToken(TokenUtils.setToken(request.getSession(), "bondToken"));// 防重复标识
				model.setOrderEnableTime(ConfigUtils.getInt(ConfigConstant.INVEST_TIMEOUT,
						ConfigConstant.INVEST_DEFAULT_BOND_ORDER_TIME_OUT) - 1 + "分59秒");// 订单有效时间

				model.setInterestManageRate(ConfigUtils.getValue("interest_manage_rate"));// 利息管理费率
				Map<String, Object> investdatamap = bondService.getInvestData(model.getBondMostMoney(), bondId);
				BigDecimal totalInterest = null;
				BigDecimal realPayAmount = null;
				Iterator<String> investdata = investdatamap.keySet().iterator();
				while (investdata.hasNext()) {
					Object key = investdata.next();
					if ("preInterest".equals(key))
						totalInterest = (BigDecimal) investdatamap.get(key);
					if ("realPayAmount".equals(key))
						realPayAmount = (BigDecimal) investdatamap.get(key);
				}
				model.setTotalInterest(totalInterest);// 总额预期收益
				model.setTotalrealPayAmount(realPayAmount);// 总额实际支付
			}
			// 未支付订单数校验
			if (!InvestCache.checkUserInvestUnpay(user.getTppUserCustId())) {
				throw new AppException(AppResultCode.ERROR_BACK, StringUtils.format(ResourceUtils.get(LoanResource.INVEST_UNPAY_TOO_MUCH), ConfigUtils.getValue(ConfigConstant.INVEST_UNPAY_MAX)));
			} 
			obj = createSuccessAppResponse(model);
		} catch (Exception e) {
			obj = dealException(e);
		}
		return obj;
	}

	/**
	 * 移动端 受让人进行债权投资
	 * 
	 * @author lgx
	 * @return
	 */
	@RequestMapping(value = "/app/member/bond/doBondInvest", method = RequestMethod.POST)
	@TokenValid(value = "bondToken", dispatch = true)
	public String doBondInvest(final BondInvestModel invest, final HttpServletRequest request, final Model model) {
		try {
			User user = getAppSessionUser(request);
			final String requestIp = IPUtil.getRemortIP(request);// 投资获取IP
			invest.setAddIp(requestIp);
			invest.setInvestChannel(InvestEnum.INVEST_CHANNEL_PC.getValue());//投资渠道为PC
            JxCreditInvestModel jxModel = (JxCreditInvestModel) bondInvestService.doBondInvest(invest, user);
            model.addAttribute("model", jxModel);
            model.addAttribute("sign", jxModel.getSign());
            return ConfigUtils.getTppName() + "/bondInvest";
		} catch (Exception e) {
			model.addAttribute("r_msg", e.getMessage());
			return ConfigUtils.getTppName() + "/retresult";
		}
	}

}
