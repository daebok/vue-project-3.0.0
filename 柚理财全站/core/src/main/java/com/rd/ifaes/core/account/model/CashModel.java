package com.rd.ifaes.core.account.model;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.rd.account.domain.Account;
import com.rd.account.model.AccountQueryModel;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.dict.CacheKeys;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.NumberUtils;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.account.domain.Cash;
import com.rd.ifaes.core.account.service.CashService;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserIdentify;
import com.rd.ifaes.core.user.model.UserFreezeModel;
import com.rd.ifaes.core.user.model.UserIdentifyModel;
import com.rd.ifaes.core.user.service.UserFreezeService;
import com.rd.ifaes.core.user.service.UserIdentifyService;

/**
 * 提现model
 * 
 * @author xhf
 * @version 3.0
 * @since 2016年6月29日
 */
public class CashModel extends Cash{
	
	private static final long serialVersionUID = 1L;
	
	public static final String  ROUTE_CODE_WHOLESALE = "2";//大额通道
	
    /** 取现返回码 **/
    private String returnCode;
    /** 取现返回值 **/
    private String returnDesc;
    
    /** 取现对象 **/
    private User user;
	
	/**状态集合**/
	private List<String> statusList;
	
	/**
	 * 审核状态
	 */
	private String verifyStatus;
	
	private String[] statusSet;
	
	/** 审核对象名称 **/
	private String operatorName;
	
	//路由代码	A	1	C	0-本行通道1-银联通道2-大额通道空-自动选择
	private String routeCode;
	//绑定银行联行号	A	20	C	人民银行分配的12位联行号routeCode=2，必输
	private String cardBankCnaps;
	
	

	/**
	 * 获得取现返回码 
	 * @return
	 */
	public String getReturnCode() {
		return returnCode;
	}

	/**
	 * 设置取现返回码 
	 * @param returnCode
	 */
	public void setReturnCode(final String returnCode) {
		this.returnCode = returnCode;
	}

	/**
	 * 获取取现对象
	 * @return
	 */
	public User getUser() {
		return user;
	}

	/**
	 * 设置取现对象
	 * @param user
	 */
	public void setUser(final User user) {
		this.user = user;
	}

	/**
	 * @return the 审核对象名称
	 */
	public String getOperatorName() {
		return operatorName;
	}

	/**
	 * @param 审核对象名称 the operatorName to set
	 */
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	/**
	 * 获取审核状态
	 * @return
	 */
	public String getVerifyStatus() {
		return verifyStatus;
	}

	/**
	 * 设置审核对象
	 * @param verifyStatus
	 */
	public void setVerifyStatus(final String verifyStatus) {
		this.verifyStatus = verifyStatus;
	}

	/**
	 * 设置状态集合
	 * @return
	 */
	public List<String> getStatusList() {
		return statusList;
	}

	/**
	 * 设置状态集合
	 * @param statusList
	 */
	public void setStatusList(final List<String> statusList) {
		this.statusList = statusList;
	}

	/**
	 * 获取cash对象
	 * @return
	 */
	public Cash prototype() {
		final Cash cash = new Cash();
		BeanUtils.copyProperties(this, cash);
		return cash;
	}
	
	/**
	 * 获取属性returnDesc的值
	 * @return returnDesc属性值
	 */
	public String getReturnDesc() {
		return returnDesc;
	}

	/**
	 * 设置属性returnDesc的值
	 * @param  returnDesc属性值
	 */
	public void setReturnDesc(String returnDesc) {
		this.returnDesc = returnDesc;
	}

	/**
	 * 获取属性statusSet的值
	 * @return statusSet属性值
	 */
	public String[] getStatusSet() {
		return statusSet;
	}

	/**
	 * 设置属性statusSet的值
	 * @param  statusSet属性值
	 */
	public void setStatusSet(String[] statusSet) {
		this.statusSet = statusSet;
	}

	/**
	 * 校验冻结功能
	 * @param userId
	 */
	public void checkFreeze(final String userId){
		final UserFreezeService userFreezeService = (UserFreezeService)SpringContextHolder.getBean("userFreezeService");
		final boolean isFreezed = userFreezeService.isFreezed(userId, UserFreezeModel.STATUS_USER_FREEZE_CASH);
		if(isFreezed){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.CASH_IS_FREEZE), BussinessException.TYPE_CLOSE);
		}
	}
	
	/**
	 * 校验用户提现认证信息
	 * @param userId
	 */
	public void checkIdentify(final String userId){
		final UserIdentifyService userIdentifyService = (UserIdentifyService)SpringContextHolder.getBean("userIdentifyService");
		final UserIdentify userIdentify = userIdentifyService.findByUserId(userId);
		if (!UserIdentifyModel.STATUS_REALNAME_SUCC.equals(userIdentify.getRealNameStatus())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_TPP_STATUS_NOT_OPEN), BussinessException.TYPE_CLOSE);
		}
	}
	
	/**
	 * 校验银行卡
	 * @param userId
	 */
	public void checkBankCard(){
		if(StringUtils.isBlank(getBankCode())||StringUtils.isBlank(getCardId())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.CASH_NEED_BANK_CARD), BussinessException.TYPE_CLOSE);
		}
	}
	
	/**
	 * 校验提现金额
	 */
	public void checkCashMoney(final String userId){
		//基础校验
		final BigDecimal amount = getAmount();
		if(amount==null||amount.compareTo(BigDecimal.ZERO)<=0){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.CASH_MONEY_POSITIVE), BussinessException.TYPE_CLOSE); 
		}
		//单笔最小提现金额
		final BigDecimal minCashAmount = ConfigUtils.getBigDecimal(Constant.CASH_MIN_AMNT);
		if(minCashAmount.compareTo(BigDecimal.ZERO)>0 && minCashAmount.compareTo(amount)>0){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.CASH_SINGLE_MIN_MONEY, minCashAmount), BussinessException.TYPE_CLOSE);
		}
		//可用余额
		final AccountService accountService = (AccountService)SpringContextHolder.getBean("accountService");
		final Account account = accountService.getMoneyByUserId(new AccountQueryModel(userId,ConfigUtils.getValue(Constant.ACCOUNT_CODE)));
		if(account.getUseMoney().compareTo(amount)<0){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.CASH_MAX_USE_MONEY), BussinessException.TYPE_CLOSE); 
		}
		
		//提现次数并发业务处理完后再进行校验
		// 校验单日提现次数
		final CashService cashService = (CashService) SpringContextHolder.getBean("cashService");
		final int dayTimeLimit = ConfigUtils.getInt(Constant.CASH_DAY_TIME_LIMIT);
		if(dayTimeLimit>0){
			final int singleDayTime = cashService.getSingleDaytime(userId);
			if(singleDayTime >= dayTimeLimit){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.CASH_DAY_TIME_LIMIT, dayTimeLimit), BussinessException.TYPE_CLOSE);
			}
		}
		// 校验单笔提现额度
		final BigDecimal singleMaxAmnt = ConfigUtils.getBigDecimal(Constant.CASH_SINGE_MAX_AMNT);
		if(singleMaxAmnt.compareTo(BigDecimal.ZERO)>0){
			if(amount.compareTo(singleMaxAmnt)>0){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.CASH_SINGLE_MIN_MONEY, NumberUtils.format2Str(singleMaxAmnt)));
			}
		}
		// 校验单日提现额度
		final BigDecimal dayMaxAmount = ConfigUtils.getBigDecimal(Constant.CASH_DAY_MAX_AMNT);
		if(dayMaxAmount.compareTo(BigDecimal.ZERO)>0){
			final BigDecimal remainAmount = cashService.getSingleDayRemainCashAmount(userId);
			if(remainAmount.compareTo(BigDecimal.ZERO)<=0){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.CASH_SINGLE_MIN_MONEY, NumberUtils.format2Str(dayMaxAmount)));
			}else if(remainAmount.compareTo(BigDecimal.ZERO)>0 && remainAmount.compareTo(amount)<0 ){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.CASH_SINGLE_MIN_MONEY, NumberUtils.format2Str(remainAmount)));
			}
		}
		
		//校验第三方可用提现额度
		BigDecimal tppCashMoney = cashService.getTppCashAmount(user.getUuid());
		if(tppCashMoney!=null && amount.compareTo(tppCashMoney)>0){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.CASH_MONEY_OVER_TPP_MOENY), BussinessException.TYPE_CLOSE);
		}
	}
	
	/**
	 * 
	 * 用户1s内只能提现一次，防止并发操作
	 * @author xhf
	 * @date 2016年9月23日
	 * @param userId
	 */
	public void checkCashFrequency(final String userId){
		// 频繁处理判断(缓存标记)
		String handleKey = String.format(CacheKeys.PREFIX_CASH_APPLY_HANDLE_NUM.getKey(), userId);
		if (CacheUtils.incr(handleKey, Constant.DOUBLE_ONE) > Constant.DOUBLE_ONE) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.CASH_IS_FREQUENCY));
		}
		CacheUtils.expire(handleKey, ExpireTime.ONE_SEC);
	}
	
	/**
	 * 
	 * 用户1s内只能提现审核一次，防止并发操作
	 * @author xhf
	 * @date 2016年9月23日
	 * @param userId
	 */
	public void checkCashAudit(final String cashNo){
		// 频繁处理判断(缓存标记)
		String handleKey = String.format(CacheKeys.PREFIX_CASH_AUDIT_HANDLE_NUM.getKey(), cashNo);
		if (CacheUtils.incr(handleKey, Constant.DOUBLE_ONE) > Constant.DOUBLE_ONE) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.CASH_IS_DOING));
		}
		CacheUtils.expire(handleKey, ExpireTime.ONE_SEC);
	}
	
	/**
	 * 
	 * 校验提现
	 * @author xhf
	 * @date 2016年9月23日
	 * @param userId
	 */
	public void checkCash(){
		//用户在固定时间内不能频繁操作
		checkCashFrequency(user.getUuid());
		//校验用户提现功能是否冻结
		checkFreeze(user.getUuid());
	    //校验认证信息
        checkIdentify(user.getUuid());
		//校验金额
		checkCashMoney(user.getUuid());
		//校验银行卡
//		if(!TppServiceEnum.CBHB.getName().equals(ConfigUtils.getTppName())){
//			checkBankCard();
//		}
	}

	public String getRouteCode() {
		return routeCode;
	}

	public void setRouteCode(String routeCode) {
		this.routeCode = routeCode;
	}

	public String getCardBankCnaps() {
		return cardBankCnaps;
	}

	public void setCardBankCnaps(String cardBankCnaps) {
		this.cardBankCnaps = cardBankCnaps;
	}
}
