package com.rd.ifaes.core.project.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.rd.account.domain.AccountLog;
import com.rd.account.model.AccountModel;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.domain.Realize;
import com.rd.ifaes.core.project.domain.RealizeFreeze;
import com.rd.ifaes.core.project.model.RealizeModel;
import com.rd.ifaes.core.tpp.domain.TppTrade;
import com.rd.ifaes.core.user.domain.User;

/**
 * 变现业务类
 * 
 * @version 3.0
 * @author fxl
 * @date 2016年8月1日
 */
public interface RealizeService extends BaseService<Realize> {

	/**
	 * 查询变现列表
	 * 
	 * @author fxl
	 * @date 2016年7月26日
	 * @param entity
	 * @return
	 */
	Page<Realize> findAllPage(Realize entity);

	/**
	 * 从缓存中取出变现列表
	 * 
	 * @author fxl
	 * @date 2016年8月23日
	 * @param entity
	 * @return
	 */
	Page<Realize> findAllPageByCache(Realize entity);

	/**
	 * 后台变现列表
	 * 
	 * @author fxl
	 * @date 2016年8月10日
	 * @param entity
	 * @return
	 */
	Page<RealizeModel> findPageModel(Realize entity);

	/**
	 * 查询变现详情
	 * 
	 * @author fxl
	 * @date 2016年8月29日
	 * @param projectId
	 * @return
	 */
	Map<String, Object> getDetailInfo(Realize realize);

	/**
	 * 查询变现对象
	 * 
	 * @author fxl
	 * @date 2016年7月26日
	 * @param entity
	 * @return
	 */
	Realize findRealize(String uuid);

	/**
	 * 查询缓存对象
	 * 
	 * @author fxl
	 * @date 2016年7月26日
	 * @param entity
	 * @return
	 */
	Realize findRealizeFromCache(String uuid);

	/**
	 * 变现到期处理
	 * 
	 * @author fxl
	 * @date 2016年8月24日
	 */
	void autoDealRealize();

	/**
	 * 变现发布
	 * 
	 * @author fxl
	 * @date 2016年7月26日
	 * @return
	 */
	void publish(Realize entity);

	/**
	 * 变现撤回
	 * 
	 * @author fxl
	 * @date 2016年7月29日
	 * @param entity
	 * @return
	 */
	void cancel(String uuid);

	/**
	 * 变现放款
	 * 
	 * @author fxl
	 * @date 2016年7月30日
	 * @param uuid
	 */
	void fullSuccess(String uuid);

	/**
	 * 最高可变现金额
	 * 
	 * @author fxl
	 * @date 2016年8月4日
	 * @param waitAmount
	 * @param apr
	 * @param timeLimit
	 * @return
	 */
	BigDecimal getMostRealizeAmount(BigDecimal waitAmount, BigDecimal apr, BigDecimal timeLimit);

	/**
	 * 查询变现列表筛选条件
	 * 
	 * @author fxl
	 * @date 2016年8月19日
	 * @return
	 */
	Map<String, Object> queryCondition();

	/**
	 * 生成账户记录
	 * 
	 * @author fxl
	 * @date 2016年8月1日
	 */
	void addAccountLog(User user, String toUserId, BigDecimal amount, String accountType, String paymentsType,
			List<AccountLog> accountLogList, String remark);

	/**
	 * 获取当日还款的变现产品
	 * @author fxl
	 * @date 2016年8月31日
	 * @param model
	 * @return
	 */
	List<Realize> findRealizeRepay(Date nowTime);
	
	/**
	 * 封装第三方冻结列表
	 * @author fxl
	 * @date 2016年8月1日
	 * @param taskList
	 * @param invest
	 * @param investUser
	 * @param realizeFreezeList
	 * @param realize
	 */
	BigDecimal fillFreezeTaskList(final List<TppTrade> taskList, final ProjectInvest invest, final String tppUserCustId,
			final List<RealizeFreeze> realizeFreezeList, final String projectNo,final String collectionId);
	
	/**
	 * 解冻已变现冻结金额
	 * @author fxl
	 * @date 2016年10月18日
	 * @param taskList
	 * @param user
	 * @param realize
	 * @param accountList
	 * @param accountLogList
	 * @return
	 */
	void fillUnFreezeTaskList(final List<TppTrade> taskList, final User user, final Realize realize,
			final List<AccountModel> accountList,final List<AccountLog> accountLogList);	
	
}