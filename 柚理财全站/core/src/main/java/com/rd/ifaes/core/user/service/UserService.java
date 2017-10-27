package com.rd.ifaes.core.user.service;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.account.model.AccountBankModel;
import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbBindMobileNoModel;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbExistUserRegisterModel;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.model.UserModel;

/**
 * Service Interface:UserService
 * @author xhf
 * @version 3.0
 * @date 2016-6-8
 */
public interface UserService extends BaseService<User>{
	
	/**登录成功**/
	String LOGIN_SUCCESS = "0000";     
	/**密码错误**/
	String LOGIN_FAIL_PASS = "01";       
	/**用户名不存在**/
	String LOGIN_FAIL_NAME = "02";  
	/**用户名已被锁定**/
	String LOGIN_FAIL_LOCK = "03";      
	/**图形验证码错误**/
	String LOGIN_FAIL_CODE = "04"; 
	
	/**
	 * 查询第三方的账户余额
	 * @author QianPengZhan
	 * @date 2016年12月22日
	 * @param user
	 * @param userCache
	 * @return
	 */
	BigDecimal getValidMoneyByUser(final User user,final UserCache userCache);
	
	/**
	 * 根据用户编号查询对象
	 * @param userNo
	 * @return
	 */
	User findByUserNo(String userNo);
	
	/**
	 * 根据userCustId查找对象
	 * @param userCustId
	 * @return
	 */
	User findByUserCustId(String userCustId);

	/**
	 * 根据status查找对象
	 * @param status
	 * @return
	 */
	List<User> findByStatus(String status);
	
	/**
	 * 用户注册
	 * @param userModel
	 */
	User doRegister(String code);
	/**
	 * 添加担保用户
	 * @return
	 * @throws Exception
	 */
	void addVouchUser(UserModel userModel);
	 /** 
	 * 修改担保用户
	 * @author wyw
	 * @date 2016-8-26
	 * @param userModel
	 * @return
	 */
	void editVouchUser(UserModel userModel);
	
	/**
	 * 
	 * 导出担保机构用户
	 * @author xhf
	 * @date 2016年9月21日
	 * @param model
	 * @return
	 */
	List<User> exportVouchUser(UserModel model);

	/**
	 * 
	 * 查看是否重复
	 * @author xhf
	 * @date 2016年8月26日
	 * @param model
	 * @return
	 */
	int checkRepeat(String uuid, String userName, String mobile, String email);
	
	/**
	 * 渤海银行  --  修改手机号
	 * TODO 方法说明
	 * @author QianPengZhan
	 * @date 2017年3月20日
	 * @param cardModel
	 */
	void cbhbUpdatePhone(final CbhbBindMobileNoModel mobileModel);
	
	/**
	 * 修改手机
	 * @param userId
	 * @param mobile
	 * @param status
	 */
	void modifyPhone(String userId, String mobile, String status);

	/**
	 * 校验用户注册信息
	 * @param model
	 */
	void checkUserRegister(UserModel model);
	
	/**
	 * 通过登录名查询用户
	 * @param loginName
	 * @return
	 */
	User getUserByLoginName(String loginName);
	
	/**
	 * 用户登录
	 * @param model 用户登录信息
	 * @return  登录错误提示信息
	 */
	Map<String, Object> doLogin(UserModel model);
	
	/**
	 * 校验用户是否开通托管账户
	 * @return
	 */
	User checkUser();
	
	/**
	 * 查询分页数据
	 * @param entity
	 * @return
	 */
	Page<User> findBorrowerPage(User model);
	/**
	 * 查询
	 * @param entity
	 * @return
	 */
	List<User> findBorrowerList(User model);
	/**
	 * 借款人自动不全
	 * @param model
	 * @return
	 */
	List<User> autoCompleteBorrower(User model);
	
	/**
	 * 获得银行卡列表
	 * @return
	 */
	List<AccountBankModel> getBankList();
	
	/**
	 * 绑定银行卡
	 * @return
	 */
	Object addBank();
	
	
	Object addBank(Map<String, Object> map);
	
	/**
	 * 解绑银行卡
	 * @param cardId    银行卡号
	 * @param bankCode  银行标识
	 * @return
	 */
	Object deleteBank(String cardId, String bankCode);
	
	/**
	 * 修改登录密码
	 * @param userModel
	 * @return
	 */
	Boolean modifyPwd(UserModel userModel);
	
	/**
	 * 
	 * 修改邮箱
	 * @author xhf
	 * @date 2016年7月27日
	 * @param model
	 */
	void modifyEmail(UserModel model);
	
	/**
	 * 
	 * 修改手机号
	 * @author xhf
	 * @date 2016年7月28日
	 * @param model
	 * @throws Exception
	 */
	void modifyMobile(UserModel model);
	
	/**
	 * 
	 * 查询个人用户
	 * @author xhf
	 * @date 2016年7月30日
	 * @param model
	 * @return
	 */
	Page<User> findPersonUser(UserModel model);
	
	/**
	 * 
	 * 查询企业用户
	 * @author xhf
	 * @date 2016年8月1日
	 * @param model
	 * @return
	 */
	Page<User> findCompanyUser(UserModel model);
	
	/**
	 * 
	 * 后台新增用户
	 * @author xhf
	 * @date 2016年7月30日
	 * @param model
	 * @return
	 */
	User addUserByManage(UserModel model);
	
	/**
	 * 
	 * 更新用户信息
	 * @author xhf
	 * @date 2016年7月30日
	 * @param model
	 * @throws Exception
	 */
	void editUserByManage(UserModel model);
	
	/**
	 * 
	 * 导出用户信息excel
	 * @author xhf
	 * @date 2016年7月30日
	 * @param model
	 * @return
	 */
	List<User> exportPersonUser(UserModel model);
	
	/**
	 * 
	 * 后台新增企业用户
	 * @author xhf
	 * @date 2016年8月1日
	 * @param model
	 * @throws Exception
	 */
	void addCompanyByManage(UserModel model);
	
	/**
	 * 
	 * 更改企业用户信息
	 * @author xhf
	 * @date 2016年8月1日
	 * @param model
	 * @throws Exception
	 */
	void editCompanyByManage(UserModel model);
	
	/**
	 * 
	 * 导出企业用户
	 * @author xhf
	 * @date 2016年8月1日
	 * @param model
	 * @return
	 */
	List<User> exportCompanyUser(UserModel model);
	
	/**
	 *  用户登录成功，后续处理
	 * @param  
	 * @return void
	 * @author xuxb
	 * @date 2016年8月8日
	 */
    void loginSucess(User model);
	
    
    /**
     * 用户登录失败，后续处理
	 * @param  
	 * @return void
	 * @author xuxb
	 * @date 2016年8月8日
	 */
	int loginFail(User model);
	
	/**
	 * 
	 * 获得错误登录次数
	 * @author xhf
	 * @date 2016年8月9日
	 * @param loginName
	 * @return
	 */
	int getLoginFailCount(String loginName);
	
	/**
	 * 
	 * 校验登录图形验证码
	 * @author xhf
	 * @date 2016年8月9日
	 * @param model
	 * @throws Exception
	 */
	void checkLoginCaptcha(UserModel model);
	
	/**
	 * 
	 * 查询用户账户信息
	 * @author xhf
	 * @date 2016年9月18日
	 * @param model
	 * @return
	 */
	Page<User> findUserAccount(UserModel model);
	
	
	/**
	 * 查询用户列表用于发放红包,加息卷
	 * @author fxl
	 * @date 2016年10月9日
	 * @param model
	 * @return
	 */
	Page<User> findUserForRed(UserModel model);
	
	/**
	 * 每天下午17点30扫描昨天17点30~今天17点30  所有有资金操作的用户的本地和第三方资金的对比  找出差异的用户 并发短信通知运维人员
	 * @author QianPengZhan
	 * @date 2016年10月11日
	 */
	void scannerAccountCompareError();
	
	/**
	 * 本地和第三方资金的对比  找出差异的用户 并发短信通知运维人员具体处理循环处理
	 * @author QianPengZhan
	 * @date 2016年11月3日
	 * @param logList
	 */
	 void  handleLogList(final List<String> logList);
	
	/**
	 * 找回密码
	 * @author xxb
	 * @date 2016年10月17日
	 * @param 
	 */
	void retrievePassword(final String pathWay, final String password);
	
	/**
	 * 根据被邀请id 获取邀请者
	 * @author ywt
	 * @date 2016-11-07
	 * @param id
	 * @return
	 */
	User getByInvitee(String userId);
	
	/**
	 * 获取用户数
	 * @param model
	 * @return
	 */
	int getAccountUserCount(UserModel model);
	
	/**
	 * 取得企业用户数
	 * @param model
	 * @return
	 */
	int getCompanyUserCount(UserModel model);
	
	/**
	 * 取得个人用户数
	 * @param model
	 * @return
	 */
	int getPersonUserCount(UserModel model);

	/**
	 * 获取邀请人手机号
	 * @author ZhangBiao
	 * @date 2016年11月22日
	 * @param ui
	 * @return
	 */
	String getInviteUserMobile(String ui);

	/**
	 * 定时器自动解冻用户账户
	 */
	void unFreezeAccountByTimer();
	
	/**
	 * 检查excel中的用户
	 */
	Map<String, Object> checkExcelUser(File excel);

	/**
	 * 根据ids获取用户
	 * @param userIds
	 * @return
	 */
	List<User> findUserByIds(String[] userIds);
	
	/**
	 * 根据userName查询用户
	 * @param userName
	 * @return
	 */
	List<User> findByUserName(User model);

	/**
	 * 
	 * @param model
	 * @return
	 */
	int getCountByUserName(String keywords);

	/**
	 * 根据手机号获取用户
	 * @author ZhangBiao
	 * @date 2017年2月28日
	 * @param mobileNo
	 * @return
	 */
	User getUserByMobile(String mobileNo);
	/**
	 * 修改支付密码
	 * @author QianPengZhan
	 * @date 2017年3月4日
	 * @param user
	 * @return
	 */
	Object modifyPayPass(final User user);
	
	/**
	 * 
	 * 江西重置密码
	 * @author jxx
	 * @date 2017年8月17日
	 * @param user
	 * @return
	 */
	Object resetPwd(final User user, final String smsCode);

	/**
	 * 渤海后台批量注册用户
	 * @author ZhangBiao
	 * @date 2017年3月14日
	 * @param targetFile
	 */
	Map<String, Object> cbhbRealName(File targetFile);

	/**
	 * 渤海后台批量注册用户回调
	 * @author ZhangBiao
	 * @param cbhbModel 
	 * @date 2017年3月15日
	 */
	void CbhbExistUserRegister(CbhbExistUserRegisterModel cbhbModel);
	
	/**
	 * 
	 * 发送短信
	 * @author jxx
	 * @date 2017年8月1日
	 * @param mobile
	 * @param reqType
	 * @param srvTxCode
	 */
	void smsCodeApply(String mobile, String reqType, String srvTxCode, String cardId);
	
	/**
	 * 
	 * 企业账户查询
	 * @author jxx
	 * @date 2017年8月31日
	 * @param map
	 */
	Object queryCorpration(Map<String, Object> map);
	
	/**
	 * 
	 * 单笔资金类业务交易查询
	 * @author jxx
	 * @date 2017年8月31日
	 * @param map
	 */
	Object fundTransQuery(Map<String, Object> map);
	
	/**
	 * 
	 * 单笔资金类业务交易查询
	 * @author jxx
	 * @date 2017年9月8日
	 * @param map
	 * @return
	 */
	Object bidApplyQuery(Map<String, Object> map);
	
	/**
	 * 
	 * 投资人债权明细查询
	 * @author jxx
	 * @date 2017年9月8日
	 * @param map
	 * @return
	 */
	Object creditDetailsQuery(Map<String, Object> map);
	
	/**
	 * 个人用户投资情况查询(投资情况）
	 */
	User queryInvestDetails(String id);
	/**
	 * 个人用户借款情况查询（借款情况)
	 */
	User queryBorrowDetails(String id);

	/**
	 * 修改用户的账号类型
	 * @param model
	 */
	void editUserAccountTypeByManage(UserModel model);
	/*删除个人用户缓存中的数据*/
	void delUserCacheDataByType(final User user, final String type);
	
	/**
	 * 去第三方查询是否设置过交易密码
	 * @param accountId 存管平台分配的账号
	 * @return true表示设置过，false表示未设置过
	 */
	boolean passwordSetQuery(final String accountId);
	
	/**
	 * 上传用户积分excel
	 * @param excel
	 * @return
	 */
	Map<String, Object> checkExcelUserScore(File excel) ;
}