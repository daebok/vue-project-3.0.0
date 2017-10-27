package com.rd.ifaes.core.user.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.rd.ifaes.core.base.service.*;
import com.rd.ifaes.core.user.domain.UserCompanyInfo;
import com.rd.ifaes.core.user.mapper.UserCompanyInfoMapper;
import com.rd.ifaes.core.user.model.UserModel;
import com.rd.ifaes.core.user.service.UserCompanyInfoService;

/**
 * 企业信息处理类
 * @author xhf
 * @version 3.0
 * @date 2016-6-15
 */
@Service("userCompanyInfoService") 
public class UserCompanyInfoServiceImpl  extends BaseServiceImpl<UserCompanyInfoMapper, UserCompanyInfo> implements UserCompanyInfoService{

   /**
    * 根据userId查询用户信息
    */
	@Override
	public UserCompanyInfo findByUserId(final String userId) {
		UserCompanyInfo userCompanyInfo = null;
		if(!StringUtils.isBlank(userId)){
			final List<UserCompanyInfo> list = dao.findList(new UserCompanyInfo(userId));
			if(!list.isEmpty()){
				userCompanyInfo = list.get(0);
			}
		}
		return userCompanyInfo;
	}

	/**
	 * 获取企业信息记录列表
	 */
	@Override
	public List<UserCompanyInfo> findConmpanyListByNature(final UserModel userModel) {
		return dao.findConmpanyListByNature(userModel);
	}
	
	/**
	 * 
	 * 更新法定代表信息
	 * @author xhf
	 * @date 2016年9月29日
	 * @param model
	 */
	@Override
	public void updateLegalDelegate(UserCompanyInfo model){
		dao.updateLegalDelegate(model);
	}
	
	/**
	 * 
	 * 校验组织机构代码、营业执照号或者社会信用统一代码是否重复，排除开户失败情况
	 * @author xhf
	 * @date 2016年10月31日
	 */
	@Override
	public int countRegisterCode(UserCompanyInfo model){
		model.setAuditStatus(UserCompanyInfo.AUDIT_STATUS_FAIL);
		return dao.countRegisterCode(model);
	}
}