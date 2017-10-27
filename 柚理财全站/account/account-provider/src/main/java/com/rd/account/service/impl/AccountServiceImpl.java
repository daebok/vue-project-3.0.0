package com.rd.account.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.rd.account.domain.Account;
import com.rd.account.domain.AccountLog;
import com.rd.account.exception.AccountException;
import com.rd.account.log.AccountLogback;
import com.rd.account.mapper.AccountMapper;
import com.rd.account.model.AccountBatchModel;
import com.rd.account.model.AccountModel;
import com.rd.account.model.AccountQueryModel;
import com.rd.account.model.AccountRegisterModel;
import com.rd.account.service.AccountLogService;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.rabbit.RabbitProducer;
import com.rd.ifaes.common.security.shiro.IdGen;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.MqConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
/**
 * ServiceImpl:AccountServiceImpl
 * 
 * @author lh
 * @version 3.0
 * @date 2016-6-14
 */
@Service("accountService")
public class AccountServiceImpl extends BaseServiceImpl<AccountMapper, Account> implements AccountService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);
	
	/**
	 * 资金处理类
	 */
	@Resource
	private AccountLogService accountLogService;
	@Resource
	private RabbitProducer rabbitProducer ;
	
	/**
	 * 账户注册
	 */
	@Transactional(readOnly = false)
	@Override
	public void register(AccountRegisterModel model) {
		String userId = model.getUserId();
		String accountCode = model.getAccountCode();
		String accountType = model.getAccountType();
		// 获取客户端隐式传入的参数，用于框架集成，不建议常规业务使用
		// 数据校验
         this.accountInfoValid(userId, accountCode);

        final Account _account = findAccount(new AccountQueryModel(userId, accountCode, accountType));
		if (_account != null && StringUtils.isNotBlank(_account.getUuid())) {
			throw new AccountException(ResourceUtils.get(ResourceConstant.USER_IS_EXIST));
		}

		final Account account = new Account(IdGen.uuid(), userId, accountCode, accountType, model.getParentCode(), BigDecimal.ZERO,
				BigDecimal.ZERO, BigDecimal.ZERO, new Date(), Account.DEFAILT_STATUS);
		account.setCashFeeMark(BigDecimal.ZERO);
		final int records = dao.insert(account);
		if(records<=0){
			throw new AccountException(ResourceUtils.get(ResourceConstant.USER_ADD_FAILD));
		}
	}


	/**
	 * 取得账户信息
	 */
	@Override
	public Account findAccount(AccountQueryModel aqmodel) {
		final Account model = new Account(aqmodel.getUserId(), aqmodel.getAccountCode(), aqmodel.getAccountType());
		Account account = null;
		final List<Account> list = dao.findList(model);
		if (list != null && list.size() > 0) {
			account = list.get(0);
		}
		return account;
	}

	/**
	 * 根据userId和accountCode查询账户对象
	 */
	@Override
	public Account getByUserId(AccountQueryModel aqmodel) {
		return findAccount(aqmodel);
	}

	/**
	 * 账户信息校验
	 * @param userId 用户ID
	 * @param accountCode 账户编号
	 */
	private void accountInfoValid(final String userId, final String accountCode ) {
		if (StringUtils.isBlank(userId)) {
			throw new AccountException(ResourceUtils.get(ResourceConstant.USER_ID_IS_NOT_NULL));
		}
		if (StringUtils.isBlank(accountCode)) {
			throw new AccountException(ResourceUtils.get(ResourceConstant.USER_ACCOUNTCODE_IS_NOT_NULL));
		}
	}

	/**
	 * 批量修改资金账户、保存资金记录
	 * 处理过程：
	 *  1、检测账户和日志对象个数保持一致；
	 *  2、检测账户和日志中对应的账户实体标识要一致；
	 *  3、采用ABAB..方式保存数据；
	 * @author xxb
	 * @date 2016年10月18日
	 * @param accountList 资金账户修改列表
	 * @param logList 资金记录列表
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void saveBatch(AccountBatchModel batchModel) {
		final List<AccountModel> accountList = batchModel.getAccountList();
		final List<AccountLog> logList = batchModel.getLogList();
		//参数是否为空
		if (CollectionUtils.isEmpty(accountList)   ||  CollectionUtils.isEmpty(logList)) {
			 throw new AccountException(ResourceUtils.get(ResourceConstant.ACCOUNT_AND_LOG_NOT_NULL));
		}
		//传入参数对象个数是否一致
		if(accountList.size() != logList.size()){
			throw new AccountException(ResourceUtils.get(ResourceConstant.ACCOUNT_AND_LOG_NOT_EQUAL));
		}
		//账户修改、日志用户是否一致
		int index = 0;
		for (AccountModel account : accountList) {
			AccountLog log = logList.get(index++);
			if (!(account.getUserId().equals(log.getUserId()) && account.getAccountCode().equals(log.getAccountCode()))) {
				throw new AccountException(ResourceUtils.get(ResourceConstant.ACCOUNT_AND_LOG_NOT_MATCH));
			}
		}
		
		//业务处理  
		index = 0;
		for (AccountModel model : accountList) {
			this.saveAccountChange(model, logList.get(index++));
		}
	}
	
	@Override
	//@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void saveAccountChange(final AccountModel model, final AccountLog log) {
		//日志
		LOGGER.debug("AccountServiceImpl.saveBatch()修改账户({})资金, 账户:{},total:{},useMoney:{},noUseMoney:{}",
				model.getUserId(),model.getAccountCode(),model.getTotal(),model.getUseMoney(),model.getNoUseMoney());
		
		String userId = model.getUserId();
		String accountCode = model.getAccountCode();
		BigDecimal useVar = model.getUseMoney();
		BigDecimal nouseVar = model.getNoUseMoney();
		
		this.accountInfoValid(userId, accountCode);
		//可用金额、不可用金额修改不可同为0 或正数、负数
		if (BigDecimal.ZERO.compareTo(useVar) == 0 && BigDecimal.ZERO.compareTo(nouseVar)==0) {
			throw new AccountException(ResourceUtils.get(ResourceConstant.CHANGE_MONEY_NOT_NULL));
		}
		if (BigDecimal.ZERO.compareTo(useVar) > 0 && BigDecimal.ZERO.compareTo(nouseVar) >0 ) {
			throw new AccountException(ResourceUtils.get(ResourceConstant.USE_MONEY_NO_USE_MONEY_CANT_INCREASE_TOGETHER));
		}
		if (BigDecimal.ZERO.compareTo(useVar) < 0 && BigDecimal.ZERO.compareTo(nouseVar) < 0 ) {
			throw new AccountException(ResourceUtils.get(ResourceConstant.USE_MONEY_NO_USE_MONEY_CANT_DECREASE_TOGETHER));
		}
		
		//修改账户资金		 
		if (dao.updateAccount(model) !=1) {
		     AccountLogback.LOGGER.error("更新用户[{}]账户[{}]数据失败: userVar: {},nouseVar: {}",userId,accountCode,useVar,nouseVar);
			 throw new AccountException(userId+","+accountCode+ResourceUtils.get(ResourceConstant.USER_UPDATE_FAILD));
		} 			
		
		//保存资金日志
		Account account = dao.getMoneyByUserId(model);
		log.setUseMoney(account.getUseMoney());
		log.setNoUseMoney(account.getNoUseMoney());		
		rabbitProducer.send(MqConstant.ROUTING_KEY_ACCOUNT_LOG, log);
	}
	
	/**
	 * 获取起始日期前一天数值
	 * @author xhf
	 * @date 2017年2月27日
	 * @param model
	 * @return
	 */
	@Override
	public BigDecimal getTotalUseMoney(){
		BigDecimal total = dao.getTotalUseMoney() ;
		if(total == null) {
			total = BigDecimal.ZERO ;
		}
		return total;
	}
	
	@Override
	public Account getMoneyByUserId(AccountQueryModel aqmodel) {
		final AccountModel model = new AccountModel(aqmodel.getUserId(), aqmodel.getAccountCode(), null, null);
		return dao.getMoneyByUserId(model);
	}


	@Override
	public void saveLogs(AccountBatchModel model) {
		for(AccountLog log : model.getLogList()){
			LOGGER.info("AccountLogService.save()保存账户资金变动记录, 账户:{}",log.getUserId());
			log.setUseMoney(BigDecimal.ZERO);
			log.setNoUseMoney(BigDecimal.ZERO);		
			accountLogService.save(log);
		}
	}


	@Override
	public BigDecimal sumUseByDate(String dateStart, String dateEnd) {
		BigDecimal total =  dao.sumUseByDate(dateStart, dateEnd);
		if(total == null) {
			total = BigDecimal.ZERO ;
		}
		return total ;
	}


	@Override
	public BigDecimal getNoUseTotalMoney() {
		BigDecimal total = dao.getNoUseTotalMoney();
		if(total == null) {
			total = BigDecimal.ZERO ;
		}
		return total;
	}


	@Override
	public BigDecimal sumNoUseMoneyByDate(String startDate, String endDate) {
		BigDecimal total = dao.sumNoUseMoneyByDate(startDate, endDate) ;
		if(total == null) {
			total = BigDecimal.ZERO ;
		}
		return total;
	}


	@Override
	public void updateOneByModel(Account account) {
		dao.updateOneByModel(account);
	}


	@Override
	public Map<String, Object> updateCompanyUserMoney(final Account account) {
		Map<String, Object> remarkData = new HashMap<String, Object>();
		final Account oldAccount = getByUserId(new AccountQueryModel(account.getUserId(), ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE)));
		if(oldAccount == null){
			remarkData.put("errmsg", "资金账户异常，没有找到所属的资金账户信息！");
			return remarkData;
		}
		if(account.getTotal() == null || account.getUseMoney() == null || account.getNoUseMoney() == null){
			remarkData.put("errmsg", "存管账户资金异常！");
			return remarkData;
		}
		if(account.getTotal().compareTo(oldAccount.getTotal()) == 0 
				&& account.getUseMoney().compareTo(oldAccount.getUseMoney()) == 0){
			remarkData.put("errmsg", "当前账户已是最新的资金状态！");
			return remarkData;
		}
		account.setUuid(oldAccount.getUuid());
		dao.updateCompanyUserMoney(account);
		remarkData.put("total", account.getTotal());
		remarkData.put("useMoney", account.getUseMoney());
		remarkData.put("oldTotal", oldAccount.getTotal());
		remarkData.put("oldUseMoney", oldAccount.getUseMoney());
		remarkData.put("money", BigDecimalUtils.sub(account.getUseMoney(), oldAccount.getUseMoney()).negate());
		return remarkData;
	}
}