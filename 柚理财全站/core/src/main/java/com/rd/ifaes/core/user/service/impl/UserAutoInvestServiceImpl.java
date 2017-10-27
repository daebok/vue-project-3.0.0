package com.rd.ifaes.core.user.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rd.account.domain.Account;
import com.rd.account.model.AccountQueryModel;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.constant.URLConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.risk.service.RiskUserLogService;
import com.rd.ifaes.core.sys.domain.DictData;
import com.rd.ifaes.core.sys.service.DictDataService;
import com.rd.ifaes.core.user.domain.AutoInvestRuleLog;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserAutoInvest;
import com.rd.ifaes.core.user.domain.UserIdentify;
import com.rd.ifaes.core.user.mapper.UserAutoInvestMapper;
import com.rd.ifaes.core.user.model.AutoInvestRuleLogModel;
import com.rd.ifaes.core.user.service.AutoInvestRuleLogService;
import com.rd.ifaes.core.user.service.UserAutoInvestService;
import com.rd.ifaes.core.user.service.UserIdentifyService;

/**
 * ServiceImpl:UserAutoInvestServiceImpl
 * @author zb
 * @version 3.0
 * @date 2016-7-25
 */
@Service("userAutoInvestService") 
public class UserAutoInvestServiceImpl  extends BaseServiceImpl<UserAutoInvestMapper, UserAutoInvest> implements UserAutoInvestService{

    @Resource
    private transient AutoInvestRuleLogService autoInvestRuleLogService;
    @Resource
    private transient AccountService accountService;
    @Resource
    private transient DictDataService dictDataService;
    @Resource
    private transient UserIdentifyService userIdentifyService;
    @Resource
	private RiskUserLogService riskUserLogService;
    
	@Override
	public void add(final AutoInvestRuleLogModel model, final User sessionUser) {
		final Date date = DateUtils.getNow();
		// 校验参数
		model.checkParams();
		// 校验金额是否足够
		final Account account = accountService.getMoneyByUserId(new AccountQueryModel(sessionUser.getUuid(),ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE)));
		// 校验金额是否达到平台设置标准
		final BigDecimal minAmount = ConfigUtils.getBigDecimal(ConfigConstant.AUTO_INVEST_MIN_USEMONEY);
		if(minAmount.doubleValue() > Constant.INT_ZERO && model.getAmountDayMax().compareTo(minAmount) < Constant.INT_ZERO){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.AUTO_INVEST_RULE_LOG_INVEST_MIN_RANGE_ERROR,minAmount));
		}
		if(model.getAmountDayMax().compareTo(account.getUseMoney()) > Constant.INT_ZERO){
			final double useMoney = BigDecimalUtils.round(account.getUseMoney()).doubleValue();
			throw new BussinessException(ResourceUtils.get(ResourceConstant.AUTO_INVEST_RULE_LOG_AMOUNT_DAY_MAX_RANGE_ERROR,useMoney));
		}
		// 保存记录
		final AutoInvestRuleLog log = model.prototype();
		log.setCreateTime(date);
		log.setStatus(Constant.FLAG_YES);
		if(StringUtils.isBlank(log.getBondUseful())){
			log.setBondUseful(Constant.FLAG_NO);
		}
		if(StringUtils.isBlank(log.getRealizeUseful())){
			log.setRealizeUseful(Constant.FLAG_NO);
		}
		autoInvestRuleLogService.save(log);
		// 保存用户配置
		UserAutoInvest autoInvest = dao.getAutoInvestByUserId(sessionUser.getUuid());
		if(autoInvest == null){
			autoInvest = new UserAutoInvest();
			autoInvest.setUserId(sessionUser.getUuid());
		}
		autoInvest.setRuleId(log.getUuid());
		autoInvest.setSortTime(date.getTime());
		save(autoInvest);
	}
	
	@Override
	public Object close(final User sessionUser) {
		final Date date = DateUtils.getNow();
		// 获取对应规则
		final UserAutoInvest autoInvest = dao.getAutoInvestByUserId(sessionUser.getUuid());
		if(autoInvest != null){
			final AutoInvestRuleLog log = autoInvestRuleLogService.get(autoInvest.getRuleId());
			log.setStatus(CommonEnum.NO.getValue());
			log.setUuid(null);
			log.setCreateTime(date);
			autoInvestRuleLogService.save(log);
			// 保存用户配置
			autoInvest.setRuleId(log.getUuid());
			autoInvest.setSortTime(date.getTime());
			save(autoInvest);
		}
		final Map<String,Object> data = new HashMap<String,Object>();
		data.put("result", true);
		return data;
	}
	
	@Override
	public Object getData() {
		final User user = SessionUtils.getSessionUser(BussinessException.TYPE_CLOSE);
		// 自动投资用户资格校验
		Map<String,Object> data = checkUser(user.getUuid());
		if(data == null){
			data = new HashMap<String,Object>();
		}else{
			return data;
		}
		// 获取用户资金
		final Account account = accountService.getByUserId(new AccountQueryModel(user.getUuid(),ConfigUtils.getValue(Constant.ACCOUNT_CODE)));
		data.put("account", account);
		// 获取详细规则
		final UserAutoInvest autoInvest = dao.getAutoInvestByUserId(user.getUuid());
		if(autoInvest != null){
			final AutoInvestRuleLog rule = autoInvestRuleLogService.get(autoInvest.getRuleId());
			if(Constant.FLAG_YES.equals(rule.getStatus())){
				data.put("rule", rule);
				// 获取总人数
				final int sum = dao.getSumByStatus(CommonEnum.YES.getValue());
				data.put("sum", sum);
				// 获取排名
				final int num = dao.getNumByStatus(new UserAutoInvest(CommonEnum.YES.getValue(),autoInvest.getSortTime()));
				data.put("num", num);
			}else{
				// 校验金额是否达到平台设置标准
				final BigDecimal minAmount = ConfigUtils.getBigDecimal(ConfigConstant.AUTO_INVEST_MIN_USEMONEY);
				if(minAmount.doubleValue() > Constant.INT_ZERO && account.getUseMoney().compareTo(minAmount) < Constant.INT_ZERO){
					data.put("result", false);
					data.put("msg", ResourceUtils.get(ResourceConstant.AUTO_INVEST_RULE_LOG_USE_MONEY_ERROR,minAmount));
					data.put("url", URLConstant.RECHARGE_DETAIL);
					data.put("buttonName", ResourceUtils.get(ResourceConstant.AUTO_INVEST_RULE_LOG_GO_RECHARGE));
					return data;
				}
			}
		}else{
			// 校验金额是否达到平台设置标准
			final BigDecimal minAmount = ConfigUtils.getBigDecimal(ConfigConstant.AUTO_INVEST_MIN_USEMONEY);
			if(minAmount.doubleValue() > Constant.INT_ZERO && account.getUseMoney().compareTo(minAmount) < Constant.INT_ZERO){
				data.put("result", false);
				data.put("msg", ResourceUtils.get(ResourceConstant.AUTO_INVEST_RULE_LOG_USE_MONEY_ERROR,minAmount));
				data.put("url", URLConstant.RECHARGE_DETAIL);
				data.put("buttonName", ResourceUtils.get(ResourceConstant.AUTO_INVEST_RULE_LOG_GO_RECHARGE));
				return data;
			}
		}
		// 获取所有收益方式 
		final List<DictData> repayStyles = dictDataService.findListByDictType(DictTypeEnum.REPAY_STYLE.getValue());
		data.put("repayStyles", repayStyles);
		data.put("result", true);
		return data;
	}

	private Map<String,Object> checkUser(String uuid) {
		final Map<String,Object> data = new HashMap<String,Object>();
		// 判断用户是否实名和授权
		final UserIdentify userIdentify = userIdentifyService.findByUserId(uuid);
		if(!Constant.FLAG_YES.equals(userIdentify.getRealNameStatus())){
			data.put("result", false);
			data.put("msg", ResourceUtils.get(ResourceConstant.AUTO_INVEST_RULE_LOG_NEED_REAL_NAME));
			data.put("url", URLConstant.SECURITY_REALNAMEIDENTIFY);
			data.put("buttonName", ResourceUtils.get(ResourceConstant.AUTO_INVEST_RULE_LOG_OPEN_REAL_NAME));
			return data;
		}
		if(!Constant.FLAG_YES.equals(userIdentify.getAuthSignStatus())){
			data.put("result", false);
			data.put("msg", ResourceUtils.get(ResourceConstant.AUTO_INVEST_RULE_LOG_NEED_AUTH_SIGN));
			data.put("url", URLConstant.SECURITY_AUTHSIGN);
			data.put("buttonName", ResourceUtils.get(ResourceConstant.AUTO_INVEST_RULE_LOG_OPEN_AUTH_SIGN));
			return data;
		}
		// 判断用户是否进行过风险等级评估
		if(riskUserLogService.getCountByUserId(uuid) <= Constant.INT_ZERO){//若评测记录为空 则说明没有做过任何评估
			data.put("result", false);
			data.put("msg", ResourceUtils.get(ResourceConstant.AUTO_INVEST_RULE_LOG_NEED_RISK));
			data.put("url", URLConstant.RISK_USERRISKPAPERS);
			data.put("buttonName", ResourceUtils.get(ResourceConstant.AUTO_INVEST_RULE_LOG_GO_RISK));
			return data;
		}
		return null;
	}

	@Override
	public String getAutoStatusByUserId(final String uuid) {
		final UserAutoInvest autoInvest = dao.getAutoInvestByUserId(uuid);
		if(autoInvest != null){
			AutoInvestRuleLog rule = autoInvestRuleLogService.get(autoInvest.getRuleId());
			return rule.getStatus();
		}
		return Constant.FLAG_NO;
	}

}