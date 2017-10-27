package com.rd.ifaes.core.user.service;

import java.util.List;
import java.util.Map;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbRealNameWebModel;
import com.rd.ifaes.core.tpp.model.cbhb.app.CbhbNetLoanRegisterModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxCompanyRegisterModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxRegisterModel;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserBaseInfo;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.domain.UserCompanyInfo;
import com.rd.ifaes.core.user.model.UserCacheModel;

/**
 * Service Interface:UserCacheService
 * @author xhf
 * @version 3.0
 * @date 2016-6-15
 */
public interface UserCacheService extends BaseService<UserCache>{

	/**
	 * 根据userId查找对下对象
	 * @param userId
	 * @return
	 */
	UserCache findByUserId(String userId);
	
	/**
	 * 个人开户-前期处理
	 * @param user
	 * @return
	 */
	Object tppRealName(UserCacheModel model);

	/**
	 * 企业开户-前期处理
	 * @param user
	 * @return
	 */
	UfxCompanyRegisterModel tppCompanyRealName(UserCacheModel model);
	
	/**
	 * 个人开户-回调处理
	 */
	void tppUfxUserRegis(UfxRegisterModel model);

	/**
	 * 企业开户-回调处理
	 */
	void tppUfxCompanyRegis(UfxCompanyRegisterModel model);
	
	/**
	 * 更新个人信息
	 * @param userName
	 * @param userCache
	 * @param userBaseInfo
	 */
	void modifyUserInfo(String userName, UserCache userCache, UserBaseInfo userBaseInfo);
	
	/**
	 *  更新企业用户信息
	 * @author xhf
	 * @date 2016年7月26日
	 * @param userName
	 * @param userCache
	 * @param userCompanyInfo
	 */
	void modifyCompanyInfo(String userName, UserCache userCache, UserCompanyInfo userCompanyInfo);
	
	/**
	 * 更新头像
	 * @param u
	 * @param path
	 */
	void updateAvaPic(User user,String path);
	
	/**
	 * 
	 * 更新备注
	 * @author xhf
	 * @date 2016年7月30日
	 * @param userCache
	 * @return
	 */
	int updateLockRemark(UserCache userCache);
	
	/**
	 * 
	 * 个人开户回调处理
	 * @author xhf
	 * @date 2016年9月22日
	 * @param model
	 */
	void doRegisger(UfxRegisterModel model);
	
	/**
	 * 企业开户异步回调处理
	 * @param flag
	 * @return
	 */
	void doCompanyRegisger(UfxCompanyRegisterModel model);
	
	/**
	 * 校验身份证
	 * @author fxl
	 * @date 2016年9月26日
	 * @param cardId
	 */
	boolean checkCardId(final String cardId);
	
	/**
	 * 
	 * 创建企业用户签章
	 * @author xhf
	 * @date 2016年11月3日
	 * @param userCompanyInfo
	 * @param user
	 */
	boolean createCompanySign(UserCompanyInfo userCompanyInfo, User user);
	/**
	 * 用户首投处理（firstAwardFlag 0->1)
	 * @author FangJun
	 * @date 2016年10月24日
	 * @param userId 用户ID
	 * @return 更新记录条数
	 */
	int userFirstInvest(String userId);

	/**
	 * 用户成功投资记录数+1
	 * @author FangJun
	 * @date 2016年10月24日
	 * @param userId 用户ID
	 * @return 更新记录条数
	 */
	int addUserInvestNum(String userId);

	/**
	 * 用户投资记录数-1
	 * @author FangJun
	 * @date 2016年10月24日
	 * @param userId 用户ID
	 * @return 更新记录条数
	 */
	int subUserInvestNum(String userId);
	
	/**
	 * 根据用户列表得到UserCache
	 * @author fxl
	 * @date 2017年1月10日
	 * @param list
	 * @return
	 */
	List<Map<String, String>> getUserCacheByUserList(List<User> list);

	/**
	 * 渤海银行开户回调
	 * @author ZhangBiao
	 * @date 2017年2月28日
	 * @param model
	 */
	void doCbhbRegisger(CbhbRealNameWebModel model);
	
	/**
	 * 渤海银行APP开户回调
	 * @author ZhangBiao
	 * @date 2017年2月28日
	 * @param model
	 */
	void doCbhbAppRegisger(CbhbNetLoanRegisterModel model);

	/**
	 * 渤海银行开户
	 * @author ZhangBiao
	 * @date 2017年2月28日
	 * @param model
	 */
	void tppCbhbUserRegis(CbhbRealNameWebModel model);
	
	/**
	 * APP渤海银行开户
	 * @author ZhangBiao
	 * @date 2017年2月28日
	 * @param model
	 */
	void tppCbhbAppUserRegis(CbhbNetLoanRegisterModel model);
	
	/**
	 * 
	 * 按证件号查询电子账号
	 * @author jxx
	 * @date 2017年8月2日
	 * @param idNo
	 */
	void accountIdQuery(String idNo, String realName);
}