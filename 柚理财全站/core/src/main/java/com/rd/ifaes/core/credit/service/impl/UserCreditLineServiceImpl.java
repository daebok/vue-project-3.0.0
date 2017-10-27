package com.rd.ifaes.core.credit.service.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rd.ifaes.common.security.shiro.IdGen;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.credit.domain.UserCredit;
import com.rd.ifaes.core.credit.domain.UserCreditLine;
import com.rd.ifaes.core.credit.mapper.UserCreditLineMapper;
import com.rd.ifaes.core.credit.service.UserCreditLineService;
import com.rd.ifaes.core.credit.service.UserCreditService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.service.UserService;

@Service("userCreditLineService")
@Transactional
public class UserCreditLineServiceImpl extends BaseServiceImpl<UserCreditLineMapper, UserCreditLine> 
	implements UserCreditLineService{

	@Resource
	private UserCreditService userCreditService;
	@Resource
	private UserService userService;
	
	public UserCreditLine getByUserId(String userId) {
		return dao.getByUserId(userId);
	}

	@Override
	public boolean subCreditByAccount(String userId, BigDecimal account, String projectName) {
		UserCreditLine userCreditLine = getByUserId(userId);
		if(userCreditLine != null && userCreditLine.getUseCredit() != null){
			BigDecimal useCredit = new BigDecimal(userCreditLine.getUseCredit());
			if(account != null){
				if(useCredit.compareTo(account) >= 0){
					userCreditLine.setUseCredit(BigDecimalUtils.sub(useCredit, account).intValue());
					update(userCreditLine);
					//保存额度记录
					UserCredit userCredit = new UserCredit();
					userCredit.setUuid(IdGen.uuid());
					userCredit.setAccount(Constant.INT_ZERO);
					userCredit.setUserId(userId);
					userCredit.setContent(ResourceUtils.get(ResourceConstant.USER_CREDIT_BORROW_DEDUCT, projectName, String.valueOf(account.intValue())));
					userCredit.setStatus(Constant.FLAG_YES);//自动审核通过
					userCredit.setCreateTime(DateUtils.getNow());
					userCredit.setDeduct(account.intValue());//扣除的额度值
					userCredit.setRemark("后台系统自动扣除用户可用信用额度！");
					userCreditService.insert(userCredit);
					return true;//积分足够
				}
			}
		}
		return false;
	}

	@Override
	public void addCreditByAccount(final String userId, final BigDecimal account, final String projectName) {
		UserCreditLine userCreditLine = getByUserId(userId);
		if(userCreditLine != null){
			if(userCreditLine.getUseCredit() == null){
				userCreditLine.setUseCredit(0);
			}
			userCreditLine.setUseCredit(BigDecimalUtils.add(new BigDecimal(userCreditLine.getUseCredit()), account).intValue());//添加额度
			update(userCreditLine);
			//保存额度记录
			UserCredit userCredit = new UserCredit();
			userCredit.setUuid(IdGen.uuid());
			userCredit.setAccount(account.intValue());//退还的信用额度
			userCredit.setUserId(userId);
			User user = userService.get(userId);
			userCredit.setContent(ResourceUtils.get(ResourceConstant.USER_CREDIT_BORROW_RETURN, projectName, user==null?"":user.getUserName(), String.valueOf(account.intValue())));
			userCredit.setStatus(Constant.FLAG_YES);//自动审核通过
			userCredit.setCreateTime(DateUtils.getNow());
			userCredit.setDeduct(Constant.INT_ZERO);
			userCredit.setRemark("系统自动退还剩余信用贷额度（初审不通过、成立审核不通过、下架）！");
			userCreditService.insert(userCredit);
		}
	}

}
