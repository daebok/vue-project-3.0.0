package com.rd.ifaes.core.user.mapper;

import java.util.List;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.user.domain.UserCompanyInfo;
import com.rd.ifaes.core.user.model.UserModel;

/**
 * Dao Interface:UserCompanyInfoMapper
 * @author xhf
 * @version 3.0
 * @date 2016-6-15
 */
public interface UserCompanyInfoMapper extends BaseMapper<UserCompanyInfo> {

	/**
	 * 
	 * 根据用户状态和用户类型获取公司信息
	 * @author ZhangBiao
	 * @param userModel 
	 * @date 2016年8月15日
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
	 * 计算组织机构代码或者社会统一信用代码记录数
	 * @author xhf
	 * @date 2016年10月31日
	 * @param model
	 * @return
	 */
	int countRegisterCode(UserCompanyInfo model);
}