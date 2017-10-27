package com.rd.ifaes.core.sys.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.rd.ifaes.common.dict.InvestEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.resource.OperatorCustomerResource;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.sys.domain.OperatorCustomer;
import com.rd.ifaes.core.sys.mapper.OperatorCustomerMapper;
import com.rd.ifaes.core.sys.model.SaleStatisticsModel;
import com.rd.ifaes.core.sys.service.OperatorCustomerService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserService;

/**
 * ServiceImpl:OperatorCustomerServiceImpl
 * @author wyw
 * @version 3.0
 * @date 2016-8-19
 */
@Service("operatorCustomerService") 
public class OperatorCustomerServiceImpl  extends BaseServiceImpl<OperatorCustomerMapper, OperatorCustomer> implements OperatorCustomerService{
	@Resource
	private UserService userService;
	
	@Resource
	private UserCacheService userCacheService;
	
	/**
	 * 校验手机号格式 长度
	 * @author QianPengZhan
	 * @date 2016年9月23日
	 * @param mobile
	 * @return
	 */
	private boolean validMobileStyle(final String mobile){
		Pattern p = null;  
        Matcher m = null;  
        boolean b = false;   
        p = Pattern.compile("^[1][0-9][0-9]{9}$"); // 验证手机号  
        m = p.matcher(mobile);  
        b = m.matches();   
        return b;
	}
	
	@Override
	@Transactional(readOnly=false)
	public OperatorCustomer addCustomerManager(OperatorCustomer operatorCustomer) {
		//2、检查手机号      是否已经添加过    手机号长度 格式校验
		String mobile = operatorCustomer.getMobile();
		//a) 手机号长度 格式校验
		if(validMobileStyle(mobile) == false){
        	throw new BussinessException(ResourceUtils.get(OperatorCustomerResource.CUSTOMER_MOBILE_STYLE_ERROR), BussinessException.TYPE_JSON);
        }
		//b)   是否已经添加过校验
		User queryUser = new User();
		queryUser.setMobile(mobile);
		List<User> userList = userService.findList(queryUser);
		if(CollectionUtils.isEmpty(userList)){//手机号码不存在
			throw new BussinessException(ResourceUtils.get(OperatorCustomerResource.CUSTOMER_MOBILE_NOT_EXISTS), BussinessException.TYPE_JSON);
		}
		queryUser = userList.get(0);
		//检查用户是个人用户不是企业用户或者担保用户
		if(!UserCache.USER_NATURE_PERSON.equals(queryUser.getUserNature())){
			throw new BussinessException(ResourceUtils.get(OperatorCustomerResource.CUSTOMER_USER_NOT_PERSONAL), BussinessException.TYPE_JSON);	
		}
		//检查用户是否已经被添加过经纪人
		OperatorCustomer qCustomer = new OperatorCustomer();
		qCustomer.setUserId(queryUser.getUuid());
		List<OperatorCustomer> customer =  this.findList(qCustomer);
		if(!CollectionUtils.isEmpty(customer)){//客户已经有被添加经纪人
			throw new BussinessException(ResourceUtils.get(OperatorCustomerResource.CUSTOMER_OPERATOR_EXISTS), BussinessException.TYPE_JSON);	
		}
		operatorCustomer.setAddType(OperatorCustomer.ADD_TYPE_MANAGER);
		operatorCustomer.setUserId(queryUser.getUuid());
		operatorCustomer.setCreateTime(DateUtils.getNow());
		this.insert(operatorCustomer);
		return operatorCustomer;
	}
	
	/**
	 * 更新时只需更新备注信息
	 */
	@Override
	@Transactional(readOnly=false)
	public OperatorCustomer editCustomerManager(OperatorCustomer operatorCustomer) {
		//1、备注长度的校验
		/*if(operatorCustomer.getRemark().length() >= Constant.INT_ONE_HUNDRED_TWENY_EIGHT){
			throw new BussinessException(ResourceUtils.get(OperatorCustomerResource.CUSTOMER_REMARK_TOO_LONG), BussinessException.TYPE_JSON);
		}*/
		this.update(operatorCustomer);
		return operatorCustomer;
	}
	@Override
	public OperatorCustomer get(String id) {
		OperatorCustomer operatorCustomer = super.get(id);
		return operatorCustomer;
	}
	@Override
	public List<SaleStatisticsModel> findSaleStatistics(OperatorCustomer operatorCustomer) {
		List<SaleStatisticsModel> list = new ArrayList<>();
		for (int i = 1; i < 13; i++) {
			list.add(new SaleStatisticsModel(i, BigDecimal.ZERO));
		}
		operatorCustomer.setStatus(InvestEnum.STATUS_SUCCESS.getValue());
		List<SaleStatisticsModel> result = dao.findSaleStatistics(operatorCustomer);
		for (SaleStatisticsModel ssm : result) {
			list.set(ssm.getSaleMonth()-1, ssm);
		}
		return list;
	}
}