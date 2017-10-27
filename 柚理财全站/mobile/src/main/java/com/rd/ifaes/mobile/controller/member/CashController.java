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
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.NumberUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TokenUtils;
import com.rd.ifaes.common.util.ip.IPUtil;
import com.rd.ifaes.core.account.domain.Cash;
import com.rd.ifaes.core.account.model.CashModel;
import com.rd.ifaes.core.account.service.AccountBankCardService;
import com.rd.ifaes.core.account.service.CashService;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.tpp.model.jx.JxWithdrawModel;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserIdentify;
import com.rd.ifaes.core.user.service.UserIdentifyService;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.mobile.common.AppException;
import com.rd.ifaes.mobile.common.AppResultCode;
import com.rd.ifaes.mobile.controller.WebController;
import com.rd.ifaes.mobile.model.PagedData;
import com.rd.ifaes.mobile.model.account.log.CashLogItemModel;

/**
 * 
 *  我的账户-充值提现-提现
 * @version 3.0
 * @author xhf
 * @date 2016年7月27日
 */
@Controller
public class CashController extends WebController {

	/**
	 * 提现处理类
	 */
	@Resource
	private transient CashService cashService;
	/**
	 * 认证信息类
	 */
	@Resource
	private transient UserIdentifyService identifyService;
	/**
	 * 账户处理类
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
	 * 
	 * 获得提现记录类
	 * @author xiaodingjiang
	 * @date 2016年10月28日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/app/member/cash/getLogList")
	@ResponseBody
	public Object getLogList(final CashModel model){
		 Object obj=null;
		 try{
			Page<Cash> pages=cashService.findByDate(model);
		    List<Cash> cashList=pages.getRows();
		    PagedData<CashLogItemModel> pirlist=new PagedData<CashLogItemModel>();
		    if(pirlist!=null){
		    	pages.setPageSize(pages.getRows().size());
				fillPageDatas(pirlist, pages);
		    for(Cash cash:cashList){
		    	CashLogItemModel logModel=new CashLogItemModel();
		    	logModel.setAddIp(cash.getAddIp());//ip地址
		    	logModel.setAddTime(cash.getAddTime());//添加时间
		    	logModel.setAmount(cash.getAmount());//提现金额
		    	logModel.setBankCode(cash.getBankCode());//所属银行
		    	logModel.setCardId(cash.getCardId());//取现银行卡号
		    	logModel.setCashFee(cash.getCashFee());//取现手续费
		    	logModel.setCashNo(cash.getCashNo());//取现订单号（UFX流水号）
		    	logModel.setFeeBearTip(cash.getFeeBearTip());//手续费承担方
		    	logModel.setHandleReason(cash.getHandleReason());//核查原因
		    	logModel.setIsAdvance(cash.getIsAdvance());//是否垫付手续费（1：需要 0：不需要）
		    	logModel.setMoney(cash.getMoney());//实际操作金额
		    	logModel.setNeedAudit(cash.getNeedAudit());//是否需要审核（1：需要 0：不需要）
		    	logModel.setOrderNo(cash.getOrderNo());//订单号（平台本地生成订单号）
		    	logModel.setStatus(cash.getStatus());//状态：0：提现申请，1：提现处理中，2：提现待审核， 3：提现成功， 4：提现失败 
		    	logModel.setStatusStr(cash.getStatusStr());//状态说明
		    	logModel.setUserId(cash.getUserId());//用户id
		    	logModel.setUuid(cash.getUuid());//uuid
		    	if(!"".equals(cash.getRemark())&&cash.getRemark()!=null){
		    	 if(cash.getRemark().contains("</a>")){
					 String mark1=cash.getRemark().replace("</a>", "");
					 cash.setRemark(mark1.replace(mark1.substring(mark1.indexOf("<"),mark1.lastIndexOf(">")+1), ""));
					 }
		    	}
				 logModel.setRemark(cash.getRemark());   //备注
		    	pirlist.getList().add(logModel);
		    }
		    }
		    obj=createSuccessAppResponse(pirlist);
			}catch(Exception e){
				obj=dealException(e);
			}
			return obj;
	}
	
	/**移动端
	 * 提现初始化
	 * @author lgx
	 * @date 2016年12月16日
	 * @return
	 */
	@RequestMapping(value = "/app/member/cash/toCash")
	@ResponseBody
	public Object toCash(HttpServletRequest request){
		Object obj=null;
        try{
        	 Map<String, Object> data = new HashMap<String, Object>();
 			User user=getAppSessionUser(request);
		final Account account = accountService.getByUserId(new AccountQueryModel(user.getUuid(), ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE)));
		final UserIdentify userIdentify = identifyService.findByUserId(user.getUuid());
		  if(!"1".equals(userIdentify.getRealNameStatus())){
			  throw new AppException(AppResultCode.USER_PAYMENT, "请先开通托管账户！");
		  }
		  mobileCheckFreeze(user.getUuid(),"cash");
		//可用余额
		BigDecimal useMoney = BigDecimal.ZERO;
		if(null != account.getUseMoney()){
			useMoney = account.getUseMoney();
		}
		data.put("userMoney", useMoney);
		//今日可提现金额
		BigDecimal webCashMoney = cashService.getSingleDayRemainCashAmount(user.getUuid());
		//第三方可提现余额
		BigDecimal tppCashMoney = BigDecimal.ZERO;
		if(useMoney.compareTo(BigDecimal.ZERO)>0 && webCashMoney.compareTo(BigDecimal.ZERO)>0){
			tppCashMoney = cashService.getTppCashAmount(user.getUuid());
		}
		//可提现金额
		BigDecimal cashMoney = useMoney;
		if(cashMoney.compareTo(webCashMoney)>0){
			cashMoney = webCashMoney;
		}
		if(tppCashMoney!=null && cashMoney.compareTo(tppCashMoney)>0){
			cashMoney = tppCashMoney;
		}
			data.put("cashMoney", cashMoney);
		//单日提现次数限制
		data.put("cashSingleDayTimeLimit", ConfigUtils.getInt(Constant.CASH_DAY_TIME_LIMIT));
		//今日已提现次数
		data.put("todayDoCashTimes", cashService.getSingleDaytime(user.getUuid()));
		//单笔提现额度限制
		data.put("cashSingleMaxAmountLimit", ConfigUtils.getBigDecimal(Constant.CASH_SINGE_MAX_AMNT));
		//单日提现额度限制
		data.put("cashSingleDayMaxAmountLimit", ConfigUtils.getBigDecimal(Constant.CASH_DAY_MAX_AMNT));
		//单笔最小提现金额
		data.put("cashMinAmount", ConfigUtils.getBigDecimal(Constant.CASH_MIN_AMNT));
		//银行卡列表
//		List<AccountBankModel> banklists=new ArrayList<AccountBankModel>();
//			List<AccountBankModel> banklist=userService.getBankList();
//			for(AccountBankModel model:banklist){
//				model.setPicPath(ConfigUtils.getValue("mobile_url")+model.getPicPath());
//				model.setBankCardType("储蓄卡");
//				banklists.add(model);
//			}
		data.put("bankList", accountBankCardService.findList(user.getUuid()));
		data.put("warmTips","1.提现操作将在资金托管账户网站进行。"+"\n"+"2.当日网银充值和快捷充值资金当日无法取现。"+"\n"+"3.单日最多可提现"+ConfigUtils.getInt(Constant.CASH_DAY_TIME_LIMIT)+"次，单笔最多可提现"+ConfigUtils.getBigDecimal(Constant.CASH_SINGE_MAX_AMNT).intValue()+"元，单日最多可提现"+ConfigUtils.getBigDecimal(Constant.CASH_DAY_MAX_AMNT).intValue()+"元。"+"\n"+"4.预计到账时间：T日10:00AM之前申请提现，预计当日到账；T日10:00AM以后申请提现，预计最晚T+1个工作日到账；遇双休日或法定节假日顺延，实际到账时间可能会受各银行工作时间影响。");
		data.put("cashToken",TokenUtils.setToken(request.getSession(), "cashToken"));//防重复标识
		data.put("bankNum", ConfigUtils.getInt("bank_num"));//绑定银行卡数量
		 obj=createSuccessAppResponse(data);
  	  }catch(Exception e){
  			obj=dealException(e);
  		}
  		return obj;
	}
	
	/**移动端
	 * 确认提现（跳转第三方）
	 * @author lgx
	 * @date 2016年12月23日
	 * @return
	 */
	@RequestMapping(value = "/app/member/cash/doCash")
	@TokenValid(value = "cashToken", dispatch = true)
	public String doCash(final String amount,final String bankNo,final String bankCode,final String routeCode,
			final String cardBankCnaps, final HttpServletRequest request ,final Model model) {
		try {
			User user = getAppSessionUser(request);
			if (StringUtils.isBlank(routeCode) && NumberUtils.getBigDecimal(amount).compareTo(new BigDecimal(50000)) > 0) {
	            throw new BussinessException("提现金额不能超过50000！", BussinessException.TYPE_JSON);
	        }
	        if(CashModel.ROUTE_CODE_WHOLESALE.equals(routeCode)){
				//大额通道必须输入12位的cardBankCnaps
				if(StringUtils.isBlank(cardBankCnaps) || cardBankCnaps.trim().length() != 12){
					model.addAttribute("r_msg", "人行通道提现请输入12位绑定银行联行号");
					return ConfigUtils.getTppName() + "/retresult";
				}
			}
			
			final CashModel cash = new CashModel();
			cash.setAmount(NumberUtils.getBigDecimal(amount));
			cash.setAddIp(IPUtil.getRemortIP(request));
			cash.setCardId(bankNo);
			cash.setBankCode(bankCode);
			cash.setUser(user);
			cash.setRouteCode(routeCode);
	        cash.setCardBankCnaps(cardBankCnaps);
			
			String userAgent = request.getHeader("User-Agent").toLowerCase();// 判断访问来源
									
			if ("android".equals(userAgent) || "iphone".equals(userAgent)) {// 安卓
				cash.setChannel(InvestEnum.INVEST_CHANNEL_APP.getValue());// app
			} else if (userAgent.contains("android") || userAgent.contains("iphone") || userAgent.contains("micromessenger")) {// 手机浏览器
				cash.setChannel(InvestEnum.INVEST_CHANNEL_WECHAT.getValue());// 微信
			}

			JxWithdrawModel apiCash = (JxWithdrawModel) cashService.doCash(cash);
			model.addAttribute("apiCash", apiCash);
			model.addAttribute("sign", apiCash.getSign());
		} catch (Exception e) {
			model.addAttribute("r_msg", e.getMessage());
			return ConfigUtils.getTppName() + "/retresult";
		}
		return ConfigUtils.getTppName() + "/cash";
	}
}
