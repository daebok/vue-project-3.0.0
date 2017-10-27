package com.rd.ifaes.core.account.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.rd.account.domain.Account;
import com.rd.account.model.AccountQueryModel;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.constant.TppServiceEnum;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TppUtil;
import com.rd.ifaes.core.account.domain.AccountCheck;
import com.rd.ifaes.core.account.mapper.AccountCheckMapper;
import com.rd.ifaes.core.account.service.AccountCheckService;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.tpp.model.ufx.UfxQueryBalanceModel;
import com.rd.ifaes.core.tpp.service.TppService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.mapper.UserCacheMapper;
import com.rd.ifaes.core.user.mapper.UserMapper;

/**
 * 
 * @ClassName: AccountCheckServiceImpl 
 * @Description: 
 * @author jxx 
 * @date 2016年7月13日 上午10:43:24 
 *
 */
@Service("accountCheckService") 
public class AccountCheckServiceImpl extends BaseServiceImpl<AccountCheckMapper, AccountCheck> implements AccountCheckService{
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountCheckServiceImpl.class);
	
    @Resource
    private AccountService accountService;
    @Resource
    private UserCacheMapper userCacheMapper;
    @Resource
    private UserMapper userMapper;

    @Override
	public void truncateAccountCheck() {
    	dao.truncateAccountCheck();
	}


	@Override
	public List<User> getOpenAccountUserList(User model) {
		return userMapper.findList(model);
	}
	
    
	@Override
	public void accountCheck(List<User> userList) {
		TppService tppService = (TppService)TppUtil.tppService();
		LOGGER.info("进入资金对账扫描");
		if(userList==null || userList.size() <=0){
			LOGGER.error("资金对账扫描,用户列表为空");
			return;
		}
		String userId = null;
		String tppUserCustId = null;
		try {
			List<AccountCheck> checkList = new ArrayList<>();
			for (User user : userList) {
				userId = user.getUuid();
				tppUserCustId = user.getTppUserCustId();
				LOGGER.info("用户名第三方id：{}",tppUserCustId);
				// 查询用户第三方的资金
				if (StringUtils.isBlank(tppUserCustId)) {
					continue;
				}
				// 第三方可用余额
				String payUseMoney = StringUtils.EMPTY;
				String payNoUseMoney = StringUtils.EMPTY;
				final String tppName = ConfigUtils.getTppName();
				if(TppServiceEnum.CHINAPNR.getName().equals(tppName)){
					Map<String, Object> ufxMap = new HashMap<String, Object>();
					ufxMap.put("userCustId",tppUserCustId);
					UserCache userCache = userCacheMapper.findByUserId(userId);
					if(UserCache.USER_NATURE_PERSON.equals(userCache.getUserNature())){
						ufxMap.put("userType", UfxQueryBalanceModel.USER_TYPE_PERSONAL);
						ufxMap.put("idNo", userCache.getIdNo());
					}else{
						ufxMap.put("userType", UfxQueryBalanceModel.USER_TYPE_COMPANY);
					}
					UfxQueryBalanceModel queryBalanceModel = (UfxQueryBalanceModel)tppService.tppQueryBalance(ufxMap);
					// 第三方可用余额
					payUseMoney = (String) queryBalanceModel.getAvlBal().replaceAll(",", "");
					// 第三方冻结金额
					payNoUseMoney = (String) queryBalanceModel.getFrzBal().replaceAll(",", "");
				}else if(TppServiceEnum.CBHB.getName().equals(tppName)){
					final Map<String, Object> cbhbMap = new HashMap<>();
					cbhbMap.put("plaCustId",user.getTppUserCustId());
					@SuppressWarnings("unchecked")
					Map<String,Object> map = (HashMap<String,Object>)tppService.tppQueryBalance(cbhbMap);
					payUseMoney = StringUtils.isNull(map.get("acctBal"));
					payNoUseMoney = StringUtils.isNull(map.get("frzBal"));
				}else{
					
				}
	
				// 得到本地的资金
				Account account = accountService.getMoneyByUserId(new AccountQueryModel(userId, ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE)));
				
				BigDecimal useMoney = account.getUseMoney();
				BigDecimal noUseMoney = account.getNoUseMoney();
	
			
				AccountCheck accountCheck = new AccountCheck();
				
				accountCheck.setTppUserCustId(user.getTppUserCustId());
				accountCheck.setUserId(userId);
				if (StringUtils.isNotBlank(payUseMoney)) {
					accountCheck.setTppUseMoney(new BigDecimal(payUseMoney));
				}else{
					accountCheck.setTppUseMoney(BigDecimal.ZERO);
				}
				if (StringUtils.isNotBlank(payNoUseMoney)) {
					accountCheck.setTppNoUseMoney(new BigDecimal(payNoUseMoney));
				}else{
					accountCheck.setTppNoUseMoney(BigDecimal.ZERO);
				}
				accountCheck.setUseMoney(useMoney);
				accountCheck.setNoUseMoney(noUseMoney);
				//计算可用金额差额
				BigDecimal useMoneyDiff = BigDecimalUtils.sub(accountCheck.getUseMoney(), accountCheck.getTppUseMoney());
				accountCheck.setUseMoneyDiff(useMoneyDiff);
				//计算冻结金额差额
				BigDecimal noUseMoneyDiff = BigDecimalUtils.sub(accountCheck.getNoUseMoney(), accountCheck.getTppNoUseMoney());
				accountCheck.setNoUseMoneyDiff(noUseMoneyDiff);
				
				if (accountCheck.getNoUseMoney().compareTo(accountCheck.getTppNoUseMoney()) != 0
						|| accountCheck.getUseMoney().compareTo(accountCheck.getTppUseMoney()) != 0) {
					LOGGER.info("{}的资金不平衡！",user.getUserName());
					checkList.add(accountCheck);
					//dao.insert(accountCheck);
				}
	
			}
			if(checkList != null && checkList.size() > 0){
				dao.insertBatch(checkList);				
			}
			LOGGER.info("6-添加数据对账完成");

		}catch (Exception e) {
			LOGGER.error("7-添加数据对账错误:{}",e.toString());
		}
	}

	@Override
	public Page<AccountCheck> findManagePage(AccountCheck entity) {
		Page<AccountCheck> page = entity.getPage();
		page.setRows(dao.findManageList(entity));
		return page;
	}


	@Override
	public Object doAutoQueryErrorBalance() {
		//清除原所有不同步数据
    	this.truncateAccountCheck();

    	User user = new User();
//    	user.setTppStatus(User.TPP_STATUS_SUCCESS);//谁去掉了这个字段也不看看有没有错的啊。
    	int countOpenAccount =  userMapper.getCount(user);
    	int pages = (int) Math.ceil(BigDecimalUtils.div(new BigDecimal(countOpenAccount),new BigDecimal(AccountCheck.PAGE_NUM)).doubleValue());
    	
    	Page<User> page = null;
		page = new Page<User>();
    	LOGGER.info("进入资金不平衡对账,总共{}页，{}条数据",pages,countOpenAccount);    	
    	for (int i = 0; i < pages; i++) {
    		page.setPage(i);
    		user.setPage(page);
    		List<User> userList =  this.getOpenAccountUserList(user);
    		this.accountCheck(userList);
		}
 		LOGGER.info("资金对账OK..");
		return null;
	}


	@Override
	public void deleteAccountCheck() {
		dao.deleteAccountCheck();
	}
}