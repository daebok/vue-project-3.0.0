package com.rd.ifaes.core.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.model.UserModel;

/**
 * Dao Interface:UserMapper
 * @author lh
 * @version 3.0
 * @date 2016-6-8
 */
public interface UserMapper extends BaseMapper<User> {

	/**
	 * 根据userNo查找对象
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
	 * @param userCustId
	 * @return
	 */
	List<User> findByStatus(String status);
	
	/**
	 * 检查重复
	 * @param user
	 * @return
	 */
	int checkRepeat(User user);
	
	/**
	 * 查询借款人
	 * @param model
	 * @return
	 */
	List<User> findBorrower(User model);

	/**
	 * 
	 * @Title: countOpenAccountUser 
	 * @Description: 已开通第三方的数量
	 * @param @return 
	 * @return int
	 * @throws
	 */
	int countOpenAccountUser();
	
	/**
	 * 更新密码
	 * @author xhf
	 * @date 2016年8月31日
	 * @param userModel
	 * @return
	 */
	int updatePwd(UserModel userModel);
	/**
	 * 根据登录名称，查询合适用户
	 * @author  FangJun
	 * @date 2016年7月26日
	 * @param loginName
	 * @return
	 */
	User getUserByLoginName(User model);
	
	/**
	 * 
	 * 查询个人用户
	 * @author xhf
	 * @date 2016年7月30日
	 * @return
	 */
	List<User> findPersonUser(UserModel model);
	
	/**
	 * 
	 * 查询企业用户
	 * @author xhf
	 * @date 2016年8月1日
	 * @return
	 */
	List<User> findCompanyUser(UserModel model);
	
	/**
	 * 
	 * 后台查询用户
	 * @author xhf
	 * @date 2016年9月18日
	 * @param model
	 * @return
	 */
	List<User> findAccountUser(UserModel model);
	
	/**
	 * 
	 * 更新用户状态
	 * @author xhf
	 * @date 2016年8月9日
	 * @param userModel
	 * @return
	 */
	int updateStatus(User user);
	
	/**
	 * 查询用户列表用于发放红包,加息卷
	 * @author fxl
	 * @date 2016年10月9日
	 * @param model
	 * @return
	 */
	List<User> findUserForRed(UserModel model);
	
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
	 * 检查用户是否存在
	 * @param mobile
	 * @return
	 */
	int checkUser(String mobile);

	/**
	 * 根据手机号获取用户
	 * @param mobile
	 * @return
	 */
	User getUserByMobile(String mobile);

	/**
	 * 根据uuid获取用户信息
	 * @param uuids
	 * @return
	 */
	List<User> findUserByIds(@Param("uuids") String[] uuids);
	
	/**
	 * 根据userName查询用户
	 * @param userName
	 * @return
	 */
	List<User> findByUserName(User user);
	
	/**
	 * 取得个人用户数
	 * @param model
	 * @return
	 */
	int getCountByUserName(@Param("keywords") String keywords);
	/**
	 * 个人用户投资情况查询（是否投过资，投资次数）
	 */
	User queryInvestDetails(String id);
	/**
	 * 个人用户借款情况查询
	 */
	User queryBorrowDetails(String id);
	/**
	 * 修改用户账号类型
	 */
	void editUserAccountTypeByManage(UserModel model);
}