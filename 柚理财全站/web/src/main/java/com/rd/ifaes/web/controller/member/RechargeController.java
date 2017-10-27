package com.rd.ifaes.web.controller.member;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.rd.account.domain.Account;
import com.rd.account.domain.AccountLog;
import com.rd.account.model.AccountQueryModel;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.dict.InvestEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.rabbit.RabbitProducer;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TppUtil;
import com.rd.ifaes.common.util.ip.IPUtil;
import com.rd.ifaes.core.account.domain.Recharge;
import com.rd.ifaes.core.account.model.RechargeModel;
import com.rd.ifaes.core.account.service.RechargeService;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.MqConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.LogTemplateUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.tpp.service.TppService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.domain.UserIdentify;
import com.rd.ifaes.core.user.service.UserIdentifyService;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.web.controller.WebController;

/**
 * 
 * 我的账户-充值
 * 
 * @version 3.0
 * @author xhf
 * @date 2016年7月27日
 */
@Controller
public class RechargeController extends WebController {
	/**
	 * USER_MONEY
	 */
	private static final String USER_MONEY = "userMoney";

	/**
	 * 用户认证信息类
	 */
	@Resource
	private transient UserIdentifyService identifyService;
	/**
	 * 充值操作类
	 */
	@Resource
	private transient RechargeService rechargeService;
	/**
	 * 账户操作类
	 */
	@Resource
	private transient AccountService accountService;
	/**
	 * 用户基础类
	 */
	@Resource
	private transient UserService userService;

	@Resource
	private RabbitProducer rabbitProducer;

	/**
	 * 充值提现页面 -个人/企业
	 * 
	 * @author xhf
	 * @date 2016年7月27日
	 * @return
	 */
	@RequestMapping(value = "/member/recharge/index")
	public String index(final Model model) {
		final User user = SessionUtils.getSessionUser(BussinessException.TYPE_CLOSE);
		final Account account = accountService
				.getMoneyByUserId(new AccountQueryModel(user.getUuid(), ConfigUtils.getValue(Constant.ACCOUNT_CODE)));
		model.addAttribute(USER_MONEY, account != null ? account.getUseMoney() : BigDecimal.ZERO);
		UserCache userCache = (UserCache) SessionUtils.getSessionAttr(Constant.SESSION_USER_CACHE);
		if (userCache != null && UserCache.USER_NATURE_VOUCH.equals(userCache.getUserNature())) {// 担保机构
			return "/member/recharge/vouchIndex";
		}
		return "/member/recharge/index";
	}

	/**
	 * 充值提现页面 -担保用户
	 * 
	 * @author wyw
	 * @date 2016年7月27日
	 * @return
	 */
	@RequestMapping(value = "/member/recharge/vouchIndex")
	public String vouchIndex(final Model model) {
		final User user = SessionUtils.getSessionUser(BussinessException.TYPE_CLOSE);
		final Account account = accountService
				.getMoneyByUserId(new AccountQueryModel(user.getUuid(), ConfigUtils.getValue(Constant.ACCOUNT_CODE)));
		model.addAttribute(USER_MONEY, account != null ? account.getUseMoney() : BigDecimal.ZERO);

		return "/member/recharge/vouchIndex";
	}

	@RequestMapping(value = "/member/recharge/getLogList")
	@ResponseBody
	public Object getLogList(final RechargeModel model) {
		final Map<String, Object> data = Maps.newHashMap();
		data.put("result", true);
		data.put("data", rechargeService.findByDate(model));
		return data;
	}

	/**
	 * 充值详情页面
	 * 
	 * @author xhf
	 * @date 2016年7月27日
	 * @return
	 */
	@RequestMapping(value = "/member/recharge/detail")
	public String detail(final Model model) {
		final User sessionUser = SessionUtils.getSessionUser(BussinessException.TYPE_CLOSE);
		final User user = userService.get(sessionUser.getUuid());
		final Account account = accountService
				.getMoneyByUserId(new AccountQueryModel(user.getUuid(), ConfigUtils.getValue(Constant.ACCOUNT_CODE)));
		final UserIdentify userIdentify = identifyService.findByUserId(user.getUuid());

		// 更新缓存
		SessionUtils.setSessionAttr(Constant.SESSION_USER, user);
		SessionUtils.setSessionAttr(Constant.SESSION_USER_IDENTIFY, userIdentify);

		model.addAttribute("money", account != null ? account.getUseMoney() : BigDecimal.ZERO);
		model.addAttribute("rechargeMinAmount", ConfigUtils.getBigDecimal(Constant.RECHARGE_MIN_AMNT));
		model.addAttribute("rechargeMaxAmount", ConfigUtils.getBigDecimal(Constant.RECHARGE_MAX_AMNT));
		model.addAttribute("realNameStatus", userIdentify.getRealNameStatus());
		model.addAttribute("showMobile", ConfigUtils.getValue(ConfigConstant.CONFIG_ONLINE));
		model.addAttribute("mobile", user.getMobile());
		model.addAttribute("tppId", user.getTppUserCustId());

		// 判断是否是个人用户
		if (!UserCache.USER_NATURE_PERSON.equals(user.getUserNature())) {
			// 不是个人用户
			model.addAttribute("tppUserCustId", user.getTppUserCustId());
			return "/member/recharge/companyDetail";
		}
		return "/member/recharge/detail";
	}

	/**
	 * 充值详情页面
	 * 
	 * @author xhf
	 * @date 2016年7月27日
	 * @return
	 */
	@RequestMapping(value = "/member/recharge/companyDetail")
	public String companyDetail(final Model model) {
		final User sessionUser = SessionUtils.getSessionUser(BussinessException.TYPE_CLOSE);
		final User user = userService.get(sessionUser.getUuid());
		final Account account = accountService
				.getMoneyByUserId(new AccountQueryModel(user.getUuid(), ConfigUtils.getValue(Constant.ACCOUNT_CODE)));
		final UserIdentify userIdentify = identifyService.findByUserId(user.getUuid());

		// 更新缓存
		SessionUtils.setSessionAttr(Constant.SESSION_USER, user);
		SessionUtils.setSessionAttr(Constant.SESSION_USER_IDENTIFY, userIdentify);

		model.addAttribute("money", account != null ? account.getUseMoney() : BigDecimal.ZERO);
		model.addAttribute("rechargeMinAmount", ConfigUtils.getBigDecimal(Constant.RECHARGE_MIN_AMNT));
		model.addAttribute("rechargeMaxAmount", ConfigUtils.getBigDecimal(Constant.RECHARGE_MAX_AMNT));
		model.addAttribute("realNameStatus", userIdentify.getRealNameStatus());
		model.addAttribute("showMobile", ConfigUtils.getValue(ConfigConstant.CONFIG_ONLINE));
		model.addAttribute("mobile", user.getMobile());
		model.addAttribute("tppId", user.getTppUserCustId());
		return "/member/recharge/companyDetail";
	}

	/**
	 * 充值详情页面 - 担保用户
	 * 
	 * @author wyw
	 * @date 2016年7月27日
	 * @return
	 */
	@RequestMapping(value = "/member/recharge/vouchDetail")
	public String vouchDetail(final Model model) {
		final User sessionUser = SessionUtils.getSessionUser(BussinessException.TYPE_CLOSE);
		final User user = userService.get(sessionUser.getUuid());
		final Account account = accountService
				.getMoneyByUserId(new AccountQueryModel(user.getUuid(), ConfigUtils.getValue(Constant.ACCOUNT_CODE)));
		final UserIdentify userIdentify = identifyService.findByUserId(user.getUuid());

		// 更新缓存
		SessionUtils.setSessionAttr(Constant.SESSION_USER, user);
		SessionUtils.setSessionAttr(Constant.SESSION_USER_IDENTIFY, userIdentify);

		model.addAttribute(USER_MONEY, account != null ? account.getUseMoney() : BigDecimal.ZERO);
		model.addAttribute("money", account != null ? account.getUseMoney() : BigDecimal.ZERO);
		model.addAttribute("rechargeMinAmount", ConfigUtils.getBigDecimal(Constant.RECHARGE_MIN_AMNT));
		model.addAttribute("rechargeMaxAmount", ConfigUtils.getBigDecimal(Constant.RECHARGE_MAX_AMNT));
		model.addAttribute("realNameStatus", userIdentify.getRealNameStatus());
		model.addAttribute("showMobile", ConfigUtils.getValue(ConfigConstant.CONFIG_ONLINE));
		model.addAttribute("mobile", user.getMobile());
		return "/member/recharge/vouchDetail";
	}

	/**
	 * 充值（跳转第三方）
	 * 
	 * @author xhf
	 * @date 2016年7月27日
	 * @return
	 */
	@RequestMapping(value = "/member/recharge/doRecharge")
	@ResponseBody
	public Object doRecharge(final RechargeModel recharge, final HttpServletRequest request) {
		final User sessionUser = (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
		final User user = userService.get(sessionUser.getUuid());
		// 判断是否是个人用户
		if (!UserCache.USER_NATURE_PERSON.equals(user.getUserNature())) {
			// 从第三方同步账户金额
			if (StringUtils.isNotBlank(user.getTppUserCustId())) {
				TppService tppCbhbService = (TppService) TppUtil.tppService();
				final Map<String, Object> jxMap = new HashMap<>();
				jxMap.put("accountId", user.getTppUserCustId());
				@SuppressWarnings("unchecked")
				Map<String, Object> map = (HashMap<String, Object>) tppCbhbService.tppQueryBalance(jxMap);
				Account account = new Account();
				account.setUserId(user.getUuid());
				if (map.containsKey("avlBal")) {
					account.setUseMoney((BigDecimal) map.get("avlBal"));// 可用余额
				}
				if (map.containsKey("acctBal")) {
					account.setTotal((BigDecimal) map.get("acctBal"));// 账户总额
				}
				if (map.containsKey("frzBal")) {
					account.setNoUseMoney((BigDecimal) map.get("frzBal"));// 冻结金额
				}
				Map<String, Object> remarkData = accountService.updateCompanyUserMoney(account);//更新账户资金
				if(remarkData.containsKey("errmsg")){
					throw new BussinessException((String)remarkData.get("errmsg"));
				}
				// 保存资金日志
				String remark = LogTemplateUtils.getAccountTemplate(Constant.MONEY_SYNCHRONIZATION_SUCCESS, remarkData);
				AccountLog accountLog = new AccountLog(ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE),
						Constant.MONEY_SYNCHRONIZATION_SUCCESS, account.getUserId(), BigDecimal.ZERO, remark);
				accountLog.setMoney((BigDecimal) remarkData.get("money"));
				accountLog.setToUser(user.getUuid());
				accountLog.setOrderNo(StringUtils.EMPTY);
				accountLog.setPaymentsType(AccountLog.PAYMENTS_TYPE_REMAIN);
				accountLog.setUseMoney(account.getUseMoney());
				accountLog.setNoUseMoney(account.getNoUseMoney());
				rabbitProducer.send(MqConstant.ROUTING_KEY_ACCOUNT_LOG, accountLog);
				return renderResult(true, "同步存管资金账户成功！");
			} else {
				return renderResult(false, "同步数据失败，请在实名之后再同步！");
			}
		}
		recharge.setAddIp(IPUtil.getRemortIP(request));
		recharge.setUser(user);
		recharge.setChannel(InvestEnum.INVEST_CHANNEL_PC.getValue());
		Recharge re = (Recharge) rechargeService.doRecharge(recharge);
		if (Constant.RECHARGE_STATUS_FAIL.equals(re.getStatus())) {
			return renderResult(false, re.getRemark());
		}
		return renderResult(true, ResourceUtils.get(ResourceConstant.RECHARGE_SUCCESS));
	}

	/**
	 * 校验是否可充值
	 * 
	 * @author xhf
	 * @date 2016年7月27日
	 * @return
	 */
	@RequestMapping(value = "/member/recharge/check")
	@ResponseBody
	public Object check() {
		identifyService.validIdentifyForRecharge();
		return renderSuccessResult();
	}

}
