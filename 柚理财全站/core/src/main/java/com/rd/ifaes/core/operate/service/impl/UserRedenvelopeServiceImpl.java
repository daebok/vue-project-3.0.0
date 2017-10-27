package com.rd.ifaes.core.operate.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.dict.OperateEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.ObjectUtils;
import com.rd.ifaes.common.util.redisson.RedissonDistributeLock;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.operate.domain.RedenvelopeRule;
import com.rd.ifaes.core.operate.domain.UserActivityAwardLog;
import com.rd.ifaes.core.operate.domain.UserRedenvelope;
import com.rd.ifaes.core.operate.mapper.UserRedenvelopeMapper;
import com.rd.ifaes.core.operate.service.RedenvelopeRuleService;
import com.rd.ifaes.core.operate.service.UserActivityAwardLogService;
import com.rd.ifaes.core.operate.service.UserRedenvelopeService;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.project.service.ProjectTypeService;
import com.rd.ifaes.core.statistic.constant.StatisticConstant;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.model.StatisticResultModel;
import com.rd.ifaes.core.statistic.service.StatisticService;
import com.rd.ifaes.core.sys.domain.DictData;
import com.rd.ifaes.core.sys.service.DictDataService;
import com.rd.ifaes.core.user.domain.User;

/**
 * ServiceImpl:UserRedenvelopeServiceImpl
 * @author 
 * @version 3.0
 * @date 2016-7-22
 */
@Service("userRedenvelopeService") 
public class UserRedenvelopeServiceImpl  extends BaseServiceImpl<UserRedenvelopeMapper, UserRedenvelope> implements UserRedenvelopeService{
	
	@Resource
	private transient RedenvelopeRuleService redenvelopeRuleService;
	@Resource
	private transient ProjectTypeService projectTypeService;
	/** 奖励日志业务处理 */
	@Resource
	private transient UserActivityAwardLogService userActivityAwardLogService;
    @Resource
    private StatisticService statisticService;
    @Resource
    private DictDataService dictDataService;
    @Resource
	private ProjectService projectService;
	
	/**
	 * 获取用户可用的红包 projectInvest属性money必须，peoject_id 必须
	 */
	@Override
	public List<UserRedenvelope> availableRedenvelope(final User user,final Project project,final BigDecimal tenderMoney) {
		// 获取用户未使用，并且未过期红包
		final UserRedenvelope queryRed = new UserRedenvelope();
		final Page<UserRedenvelope> page = new Page<>();
		page.setSort("useExpireTime");
		page.setOrder(Constant.DESC);
		queryRed.setUserId(user.getUuid());
		queryRed.setStatus(OperateEnum.STATUS_NO_USE.getValue());
		final BigDecimal redRate=BigDecimalUtils.validAmount(project.getRedEnvelopeRate())? 
				project.getRedEnvelopeRate(): ConfigUtils.getBigDecimal(ConfigConstant.INVEST_REDENVELOPE_RATE);
		if(BigDecimalUtils.validAmount(redRate)){
			queryRed.setAmount(BigDecimalUtils.div(BigDecimalUtils.mul(tenderMoney,redRate), BigDecimal.valueOf(Constant.DOUBLE_ONE_HUNDRED)));
		}
		queryRed.setPage(page);
		final List<UserRedenvelope> userRedList = this.findList(queryRed);
		final List<UserRedenvelope> userRedListNew = new ArrayList<UserRedenvelope>();
		final int listSize = userRedList.size();
		// 循环获取 并验证是否可用 isRedenvelopeAvailable
		for(int i=0;i<listSize;i++){
			if(isRedenvelopeAvailable(project, userRedList.get(i), tenderMoney)){
				userRedListNew.add(userRedList.get(i));
			}	
		}
		return userRedListNew;
	}
	/**
	 * 用户的红包是否可用
	 */
	@Override
	public Boolean isRedenvelopeAvailable(final Project project,final UserRedenvelope userRedenvelope, final BigDecimal tenderMoney) {
		return userRedenvelope.isAvailable(project, tenderMoney);
	}

	/**
	 * 批量修改指定用户红包状态
	 */
	@Override
	public int updateStatusBatch(final String uuids, final String investId,final  String status, final String preStatus) {
		 if(OperateEnum.STATUS_USE.eq(status)){
			return dao.updateStatusBatch(uuids, investId,DateUtils.getNow() ,status, preStatus);
		} 
		 return dao.updateStatusBatch(uuids, investId,null, status, preStatus);
	}
	/**
	 * 修改使用红包的投资ID
	 */
	@Override
	public void updateRedenvelopeTenderId(final String oldInvestId, final String newInvestId) {
		dao.updateRedenvelopeTenderId(oldInvestId,newInvestId);
	}
	/**
	 * 红包过期处理
	 */
	@Override
	public int expiredHandle() {
		Date now = DateUtils.getNow();		
		List<String> ids = dao.findExpireList(DateUtils.rollMinute(now, -10), now, OperateEnum.STATUS_NO_USE.getValue());
		List<UserRedenvelope> list = new ArrayList<>(ids.size());
		if(CollectionUtils.isNotEmpty(ids)){
			for(String id:ids){
				UserRedenvelope url = new UserRedenvelope();
				url.setUuid(id);
				url.setStatus(OperateEnum.STATUS_EXPIRED.getValue());
				list.add(url);
			}
			//批量更新
			List<List<UserRedenvelope>> splitList = ObjectUtils.splitList(list, 200);
			for (List<UserRedenvelope> ls : splitList) {
				dao.updateBatchStatus(ls);
			}
		}
		return Constant.INT_ONE;
	}
	
	@Override
	public List<UserRedenvelope> findAvailableRedenvelope(UserRedenvelope userRedenvelope, Project project,
			BigDecimal tenderMoney) {
		userRedenvelope.setStatus(Constant.STATUS_NO_USE);  //未使用的红包
		userRedenvelope.setTenderMoney(tenderMoney); 
		// 大于 当前投资金额*红包使用比例 的红包不显示
		userRedenvelope.setAmount(BigDecimalUtils.div(BigDecimalUtils.mul(project.getRedEnvelopeRate(),tenderMoney),BigDecimal.valueOf(100)));
		userRedenvelope.setProjectTypeId(project.getProjectTypeId()); //设置当前标的类型ID，查询时候条件判断
		userRedenvelope.setInvestExpireTime(project.getTimeType());//设置当前标的类型是天标还是月标，查询时候条件判断
		userRedenvelope.setRedInvestExpireTime(project.getTimeLimit());//设置当前标的期限，利用标的期限去查询符合条件的红包
		List<UserRedenvelope> list=dao.viableUserRedenvelope(userRedenvelope);
		//项目类型名称
		Map<String, String> typeNames = projectTypeService.findProjectTypeNames();
		for (UserRedenvelope redenvelope : list) {
			final StringBuilder useProjectTypeName = new StringBuilder();
			if (redenvelope.getUseProjectType() != null) {// 有限制项目分类
				final String[] uuids = redenvelope.getUseProjectType().split(",");
				for (int i = 0; i < uuids.length; i++) {
					useProjectTypeName.append(typeNames.get(uuids[i])).append(",");
				}
			}
			redenvelope.setUseProjectTypeName(useProjectTypeName.length()> 0?useProjectTypeName.substring(0, useProjectTypeName.length() -1):"");
		}
		return list;
	}
	
	@Override
	public void gainActivityRedenvelope(User user, String RedenvelopeRuleId) {
		if (null==user) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_NOT_LOGIN));
		}
		try {
			RedissonDistributeLock.lock(user.getUuid());
			UserRedenvelope rp=new UserRedenvelope();
			rp.setUserId(user.getUuid());
			rp.setRuleId(RedenvelopeRuleId);
			//List<UserRedenvelope> list=dao.findList(rp);
			List<UserRedenvelope> urvs = new ArrayList<>();
			List<UserActivityAwardLog> logs = new ArrayList<>();
			//if (list.isEmpty()) {   //自定义活动的红包每个用户只能获取一次
				RedenvelopeRule rule=redenvelopeRuleService.get(RedenvelopeRuleId);
				if (OperateEnum.ACTIVITYPLAN_CUSTOM.eq(rule.getActivityCode())) { //如果查询到的规则不是自定义发放的类型，说明是非法提交
					redenvelopeRuleService.grantRedenvelope(user, rule, null, null,urvs,logs);
				}else{
					throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_MSG_ILLEGAL));
				}
			//}
			//else{
			//	throw new BussinessException(ResourceUtils.get(ResourceConstant.AWARD_ONLY_ONCE));
			//}
			if(!CollectionUtils.isEmpty(urvs)){
				dao.insertBatch(urvs);
			}
			if(!CollectionUtils.isEmpty(logs)){
				userActivityAwardLogService.insertBatch(logs);				
			}
		} finally {
			RedissonDistributeLock.unlock(user.getUuid());
		}
	}
	
	@Override
	public Page<UserRedenvelope> findWebPage(UserRedenvelope entity) {
		Page<UserRedenvelope> page = entity.getPage();
		if(page==null){
			page=new Page<UserRedenvelope>();
		}
		List<UserRedenvelope> list = dao.findList(entity);
		//项目类型名称
		Map<String, String> typeNames = projectTypeService.findProjectTypeNames();		
		for (UserRedenvelope userRedenvelope : list) {
			final StringBuilder useProjectTypeName = new StringBuilder();
			if (userRedenvelope.getUseProjectType() != null) {// 有限制项目分类
				final String[] uuids = userRedenvelope.getUseProjectType().split(",");
				for (int i = 0; i < uuids.length; i++) {
					useProjectTypeName.append(typeNames.get(uuids[i])).append(",");
				}
			}
			userRedenvelope.setUseProjectTypeName(useProjectTypeName.length()> 0?useProjectTypeName.substring(0, useProjectTypeName.length() -1):"");
		}
		page.setRows(list);
		return page;
	}
	@Override
	public void cancellationRedenvelope(String RedenvelopeId) {
		UserRedenvelope userRedenvelope = get(RedenvelopeId);
		if (null!=userRedenvelope && !Constant.STATUS_NO_USE.equals(userRedenvelope.getStatus())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.REDENVELOPE_CANCEL_STATUS_ERROR));
		}
		userRedenvelope.setStatus(Constant.STATUS_CANCEL);
		update(userRedenvelope);
	}
	
	@Override
	public Map<String,Object> findByStatDate(StatisticModel model){
		//校验时间
		model.checkQueryDate();
        //根据日期查询符合记录
		final Map<String, Object> amntMap = Maps.newHashMap();
		final Map<String, Object> numMap = Maps.newHashMap();
		List<StatisticResultModel> redList = dao.findByStatDate(model);
		for (StatisticResultModel resultModel : redList) {
			amntMap.put(resultModel.getStatDate(), resultModel.getTotalAmnt());
			numMap.put(resultModel.getStatDate(), resultModel.getTotalCount());
		}
		//返回
		final Map<String,Object> map = new HashMap<String, Object>();
		map.put("result", true);
		//日期横坐标
		map.put(StatisticConstant.STAT_CHART_DATE, statisticService.getStatChartDate(model));
		//红包数量
		model.setStatType(StatisticConstant.STAT_TYPE_NUMBERS);
		map.put(StatisticConstant.STAT_CHART_NUM, statisticService.statByDate(numMap, model));
		//红包金额
		model.setStatType(StatisticConstant.STAT_TYPE_AMOUNT);
		map.put(StatisticConstant.STAT_CHART_AMNT, statisticService.statByDate(amntMap, model));
		return map;
	}
	
	@Override
	public Map<String,Object> findByStatStatus(StatisticModel model){
		//校验时间
		model.checkQueryDate();
        //根据日期查询符合记录
		final Map<String, BigDecimal> amntStatusMap = Maps.newHashMap();
		final Map<String, BigDecimal> numStatusMap = Maps.newHashMap();
		List<StatisticResultModel> redList = dao.findByStatStatus(model);
		for (StatisticResultModel resultModel : redList) {
			amntStatusMap.put(resultModel.getStatus(), resultModel.getTotalAmnt());
			numStatusMap.put(resultModel.getStatus(), resultModel.getTotalCount());
		}
		
		final Map<String, Object> amntMap = Maps.newHashMap();
		final Map<String, Object> numMap = Maps.newHashMap();
		final List<DictData> dictDataList = dictDataService.findListByDictType(DictTypeEnum.ACTIVITY_LOG_STATUS.getValue());
		for (DictData dictData : dictDataList) {
			BigDecimal amnt = amntStatusMap.get(dictData.getItemValue());
			BigDecimal num = numStatusMap.get(dictData.getItemValue());
			amntMap.put(dictData.getItemName(), amnt==null?BigDecimal.ZERO:amnt);
			numMap.put(dictData.getItemName(), num==null?BigDecimal.ZERO:num);
		}
		
		//返回结果
		final Map<String,Object> map = new HashMap<String, Object>();
		map.put("result", true);
		//红包金额
		map.put(StatisticConstant.STAT_PIE_AMNT, amntMap);
		//红包数量
		map.put(StatisticConstant.STAT_PIE_NUM, numMap);
		return map;
	}
	
	/**
	 * 根据tenderId计数
	 * @param tenderId
	 * @return
	 */
	@Override
	public int getCountByTenderId(String tenderId){
		return dao.getCountByTenderId(tenderId);
	}
	@Override
	public UserRedenvelope getByTenderId(String tenderId) {
		return dao.getByTenderId(tenderId);
	}
}