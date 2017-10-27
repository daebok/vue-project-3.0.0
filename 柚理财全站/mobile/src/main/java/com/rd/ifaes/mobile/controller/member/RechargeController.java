package com.rd.ifaes.mobile.controller.member;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.account.domain.Account;
import com.rd.account.model.AccountQueryModel;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.dict.InvestEnum;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.TokenUtils;
import com.rd.ifaes.common.util.ip.IPUtil;
import com.rd.ifaes.core.account.domain.AccountBankCard;
import com.rd.ifaes.core.account.domain.Recharge;
import com.rd.ifaes.core.account.model.RechargeModel;
import com.rd.ifaes.core.account.service.AccountBankCardService;
import com.rd.ifaes.core.account.service.RechargeService;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserIdentify;
import com.rd.ifaes.core.user.service.UserIdentifyService;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.mobile.common.AppException;
import com.rd.ifaes.mobile.common.AppResultCode;
import com.rd.ifaes.mobile.controller.WebController;
import com.rd.ifaes.mobile.model.PagedData;
import com.rd.ifaes.mobile.model.account.log.RechargeLogItemModel;

/**
 * 
 *  我的账户-充值
 * @version 3.0
 * @author xhf
 * @date 2016年7月27日
 */
@Controller
public class RechargeController extends WebController {
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
	private transient AccountBankCardService accountBankCardService;

	/**
	 * 充值记录
	 */
	@RequestMapping(value = "/app/member/recharge/getLogList")
	@ResponseBody
	public Object getLogList(RechargeModel model,HttpServletRequest request){
		Object obj=null;
	try{
		@SuppressWarnings("unused")
		User user=getAppSessionUser(request);//查询用户，更新缓存
		Page<Recharge> pages=rechargeService.findByDate(model);
	    List<Recharge> rechargeList=pages.getRows();
	    PagedData<RechargeLogItemModel> pirlist=new PagedData<RechargeLogItemModel>();
	    if(rechargeList!=null){
	    	pages.setPageSize(pages.getRows().size());
			fillPageDatas(pirlist, pages);
	    for(Recharge recharge:rechargeList){
	    	RechargeLogItemModel logModel=new RechargeLogItemModel();
	    	logModel.setAmount(recharge.getAmount());//交易金额
	    	logModel.setCreateTime(recharge.getCreateTime());//交易时间
	    	logModel.setOrderNo(recharge.getOrderNo());//订单号
	    	logModel.setPayWay(recharge.getPayWay());//支付方式(0：网银充值  2：快捷支付 3：线下充值 )
	    	if(!"".equals(recharge.getRemark())&&recharge.getRemark()!=null){
	    	 if(recharge.getRemark().contains("</a>")){
				 String mark1=recharge.getRemark().replace("</a>", "");
				 recharge.setRemark(mark1.replace(mark1.substring(mark1.indexOf("<"),mark1.lastIndexOf(">")+1), ""));
				 }
	    	}
			 logModel.setRemark(recharge.getRemark());   //备注
	    	logModel.setStatusStr(recharge.getStatusStr());//交易状态说明
	    	logModel.setTradeNo(recharge.getTradeNo());//流水号
	    	pirlist.getList().add(logModel);
	    }
	    }
	    obj=createSuccessAppResponse(pirlist);
		}catch(Exception e){
			obj=dealException(e);
		}
		return obj;
	}
	
	/**
	 * 充值初始化
	 * @author xiaodingjiang
	 * @date 2016年11月4日
	 * @return
	 */
	@RequestMapping(value = "/app/member/recharge/toRecharge")
	@ResponseBody
	public Object toRecharge(HttpServletRequest request) {
		Object obj = null;
		try {
			Map<String, Object> data = new HashMap<String, Object>();
			User user = getAppSessionUser(request);
			final Account account = accountService.getByUserId(new AccountQueryModel(user.getUuid(), ConfigUtils
					.getValue(ConfigConstant.ACCOUNT_CODE)));
			final UserIdentify userIdentify = identifyService.findByUserId(user.getUuid());
			if (!"1".equals(userIdentify.getRealNameStatus())) {
				throw new AppException(AppResultCode.USER_PAYMENT, "请先开通托管账户！");
			}
			mobileCheckFreeze(user.getUuid(), "recharge");
			List<AccountBankCard> bankList = accountBankCardService.findList(user.getUuid());
			if (bankList != null && bankList.size() > 0) {
				AccountBankCard accountBankCard = bankList.get(0);
				data.put("bankNo", accountBankCard.getBankNo());//银行卡
			} else {
				data.put("bankNo", "");//银行卡
			}
			data.put("bankList", bankList);
			data.put("mobile", user.getMobile());//手机号
			data.put("money", account != null ? account.getUseMoney() : BigDecimal.ZERO);// 账户可用余额
			data.put("rechargeMinAmount", ConfigUtils.getBigDecimal(Constant.RECHARGE_MIN_AMNT));// 最小充值金额
			data.put("rechargeMaxAmount", ConfigUtils.getBigDecimal(Constant.RECHARGE_MAX_AMNT));// 最大充值金额
			data.put("warmTips", "1.充值/提现必须为开通网上银行的借记卡，不支持存折、信用卡充值。" + "\n" + " 2.充值期间，请勿关闭浏览器，待充值成功并返回后，可在网站中查看充值金额。" + "\n"
					+ " " + "3.严禁信用卡套现、虚假交易等行为。");
			data.put("rechargeToken", TokenUtils.setToken(request.getSession(), "rechargeToken"));// 防重复标识
			obj = createSuccessAppResponse(data);
		} catch (Exception e) {
			obj = dealException(e);
		}
		return obj;
	}
	/**
	 * 确认充值（跳转第三方）
	 * @author xhf
	 * @date 2016年7月27日
	 * @return
	 */
	@RequestMapping(value = "/app/member/recharge/doRecharge")
	@TokenValid(value = "rechargeToken", dispatch = true)
	@ResponseBody
	public Object doRecharge(final RechargeModel recharge,final HttpServletRequest request ,final Model model) {
		Object obj = null;
		try {
			// 用户登录状态
			User user = getAppSessionUser(request);
			recharge.setAddIp(IPUtil.getRemortIP(request));
			recharge.setUser(user);
			String userAgent = request.getHeader("User-Agent").toLowerCase();// 判断访问来源
									
			if ("android".equals(userAgent) || "iphone".equals(userAgent)) {// 安卓
				recharge.setChannel(InvestEnum.INVEST_CHANNEL_APP.getValue());// app
			} else if (userAgent.contains("android") || userAgent.contains("iphone") || userAgent.contains("micromessenger")) {// 手机浏览器
				recharge.setChannel(InvestEnum.INVEST_CHANNEL_WECHAT.getValue());// 微信
			}
			Recharge re = (Recharge) rechargeService.doRecharge(recharge);
			if (Constant.RECHARGE_STATUS_FAIL.equals(re.getStatus())) {
				obj = createFailureAppResponse(AppResultCode.ERROR, re.getRemark());
			} else {
				obj = createSuccessAppResponse("充值成功");
			}
			
		} catch (Exception e) {
			obj = dealException(e);
		}

		return obj;
	}
}
