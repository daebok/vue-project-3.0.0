package com.rd.ifaes.core.operate.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.operate.domain.UserRedenvelope;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.user.domain.User;

/**
 * 
 * 用户红包业务接口
 * 
 * @version 3.0
 * @author wyw
 * @date 2016-7-29
 */
public interface UserRedenvelopeService extends BaseService<UserRedenvelope> {
	/**
	 * 用户的红包是否可用
	 * @author wyw
	 * @param projectInvest
	 * @param userRedenvelope
	 * @return
	 */
	Boolean isRedenvelopeAvailable(Project project, UserRedenvelope userRedenvelope, BigDecimal tenderMoney);

	/**
	 * 获取用户可用的红包 projectInvest属性money必须，peoject_id 必须
	 * @author wyw
	 * @param user
	 * @param projectInvest
	 * @return
	 */
	List<UserRedenvelope> availableRedenvelope(User user, Project project, BigDecimal tenderMoney);

	/**
	 * 批量修改指定用户红包状态
	 * @author FangJun
	 * @param uuids 红包ID字符串拼接
	 * @param investId 投资记录D
	 * @param status 红包当前状态
	 * @param preStatus 红包前一状态
	 * @return 修改记录条数
	 */
	int updateStatusBatch(String uuids, String investId, String status, String preStatus);

	/**
	 * 修改使用红包的投资ID
	 * 
	 * @author fxl
	 * @date 2016年8月24日
	 */
	void updateRedenvelopeTenderId(String oldInvestId, String newInvestId);

	/**
	 * 
	 * 红包过期处理
	 * 
	 * @author wyw
	 * @date 2016-8-25
	 */
	int expiredHandle();
	
	/**
	 * 获取用户可用的红包 
	 * @author wyw
	 * @param user
	 * @param projectInvest
	 * @return
	 */
	List<UserRedenvelope> findAvailableRedenvelope(UserRedenvelope userRedenvelope,Project project, BigDecimal tenderMoney);

	/**
	 * 获取自定义活动的红包（每个用户只能获取一次）
	 * 
	 * @param user
	 * @param RedenvelopeRuleId
	 * @return
	 */
	void gainActivityRedenvelope(User user,String RedenvelopeRuleId);

	/**
	 * 前台获取红包列表
	 * @author fxl
	 * @date 2016年11月8日
	 * @param entity
	 * @return
	 */
	Page<UserRedenvelope> findWebPage(UserRedenvelope entity);
	
	/**
	 * 作废红包
	 * @author ywt
	 * @date 2016年11月16日
	 * @param RedenvelopeId
	 * @return
	 */
	void cancellationRedenvelope(String RedenvelopeId);
	
	/**
	 * 根据日期统计红包
	 * @param model
	 * @return
	 */
	Map<String,Object> findByStatDate(StatisticModel model);
	
	/**
	 * 根据状态统计红包
	 * @param model
	 * @return
	 */
	Map<String,Object> findByStatStatus(StatisticModel model);

	/**
	 * 根据tenderId计数
	 * @param tenderId
	 * @return
	 */
	int getCountByTenderId(String tenderId);

	/**
	 * 
	 * 根据投资订单号查找红包
	 * @author jxx
	 * @date 2017年9月20日
	 * @param tenderId
	 * @return
	 */
	UserRedenvelope getByTenderId(String tenderId);
}