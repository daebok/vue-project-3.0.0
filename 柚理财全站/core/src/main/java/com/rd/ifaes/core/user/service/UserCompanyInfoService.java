package com.rd.ifaes.core.user.service;

import java.util.List;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.user.domain.UserCompanyInfo;
import com.rd.ifaes.core.user.model.UserModel;

/**
 * Service Interface:UserCompanyInfoService
 * @author xhf
 * @version 3.0
 * @date 2016-6-15
 */
public interface UserCompanyInfoService extends BaseService<UserCompanyInfo>{

	/**
	 * 根据userId查询用户
	 * @param userId
	 * @return
	 */
	UserCompanyInfo findByUserId(String userId);

	/**
	 * 
	 * 根据用户状态和用户类型获取公司信息
	 * @author ZhangBiao
	 * @date 2016年8月15日
	 * @param userModel
	 * @return
	 */
	List<UserCompanyInfo> findConmpanyListByNature(UserModel userModel);
	
	/**
	 * 
	 * 更新法定代表信息
	 * @author xhf
	 * @date 2016年9月29日
	 * @param model
	 */
	void updateLegalDelegate(UserCompanyInfo model);
	
	/**
	 * 
	 * 校验组织机构代码、营业执照号或者社会信用统一代码是否重复，排除开户失败情况
	 * @author xhf
	 * @date 2016年10月31日
	 */
	int countRegisterCode(UserCompanyInfo model);
}