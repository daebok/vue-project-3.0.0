package com.rd.ifaes.core.operate.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.dict.OperateEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.ObjectUtils;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.operate.domain.RateCouponRule;
import com.rd.ifaes.core.operate.domain.UserActivityAwardLog;
import com.rd.ifaes.core.operate.domain.UserRateCoupon;
import com.rd.ifaes.core.operate.mapper.UserRateCouponMapper;
import com.rd.ifaes.core.operate.service.UserActivityAwardLogService;
import com.rd.ifaes.core.operate.service.UserRateCouponService;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectType;
import com.rd.ifaes.core.project.service.ProjectTypeService;
import com.rd.ifaes.core.statistic.constant.StatisticConstant;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.model.StatisticResultModel;
import com.rd.ifaes.core.statistic.service.StatisticService;
import com.rd.ifaes.core.sys.domain.DictData;
import com.rd.ifaes.core.sys.service.DictDataService;
import com.rd.ifaes.core.user.domain.User;

/**
 * ServiceImpl:UserRatecouponServiceImpl
 * @author 
 * @version 3.0
 * @date 2016-7-27
 */
@Service("userRateCouponService") 
public class UserRateCouponServiceImpl  extends BaseServiceImpl<UserRateCouponMapper, UserRateCoupon> implements UserRateCouponService{
	
	//活动获取红包加锁，防止并发情况获取多个红包
	private static final Lock LOCK= new ReentrantLock();
	
	@Resource
	private transient RateCouponRuleServiceImpl rateCouponRuleServiceImpl;
	@Resource
	private transient ProjectTypeService projectTypeService;
	/** 奖励日志业务处理 */
	@Resource
	private transient UserActivityAwardLogService userActivityAwardLogService;
    @Resource
    private StatisticService statisticService;
    @Resource
    private DictDataService dictDataService;
	
	/**
	 * 日志记录
	 */
	private static final Logger  LOGGER =LoggerFactory.getLogger(UserRateCouponServiceImpl.class);
	/**
	 * 修改加息券状态
	 */
	@Override
	public boolean updateStatus(final String uuid,final String investId, final String status, final String preStatus) {
		int result = Constant.INT_ZERO;
	    if(OperateEnum.STATUS_USE.eq(status)){
			result =dao.updateStatus(uuid, investId,DateUtils.getNow() ,status, preStatus);
		}else{
			result =dao.updateStatus(uuid, investId,null,status, preStatus);
		}
		
		if(result == Constant.INT_ZERO){
			final String msg = "加息劵状态更新失败 [uuid="+uuid+", status="+status+", preStatus="+preStatus+"]";
			LOGGER.error(msg);
		     throw new BussinessException(msg);
		}
		
		return true;
	}
	/**
	 * 查询指定投资使用的加息劵
	 */
	@Override
	public UserRateCoupon findByInvestId(final String investId) {
		return dao.findByInvestId(investId);
	}
	/**
	 * 修改加息卷使用tenderId
	 */
	@Override
	public void updateRateCouponTenderId(final String oldInvestId, final String newInvestId) {
		dao.updateRateCouponTenderId(oldInvestId, newInvestId);
	}
	/**
	 * 加息券过期处理
	 */
	@Override
	public int expiredHandle() {
		Date now = DateUtils.getNow();
		List<String> ids = dao.findExpireList(DateUtils.rollMinute(now, -10), now, OperateEnum.STATUS_NO_USE.getValue());
		List<UserRateCoupon> list = new ArrayList<>(ids.size());
		if(CollectionUtils.isNotEmpty(ids)){
			
			//封装成对象
			for(String id:ids){
				UserRateCoupon urc =new UserRateCoupon();
				urc.setUuid(id);
				urc.setStatus(OperateEnum.STATUS_EXPIRED.getValue());
				list.add(urc);				
			}
			//批量更新
			List<List<UserRateCoupon>> splitList = ObjectUtils.splitList(list, 200);
			for (List<UserRateCoupon> ls : splitList) {
				dao.updateBatchStatus(ls);
			}
		}
		return Constant.INT_ONE;
	}
	
	@Override
	public List<UserRateCoupon> findAvailableRateCoupon(UserRateCoupon userRateCoupon, Project project,
			BigDecimal tenderMoney) {
		userRateCoupon.setStatus(Constant.STATUS_NO_USE);  //未使用的加息券
		userRateCoupon.setTenderMoney(tenderMoney);  //设置当前投资金额，查询时候条件判断
		userRateCoupon.setProjectTypeId(project.getProjectTypeId()); //设置当前标的类型ID，查询时候条件判断
		List<UserRateCoupon> list=dao.viableUserRateCoupon(userRateCoupon);
		for (UserRateCoupon userRedenvelope : list) {
			final StringBuilder useProjectTypeName = new StringBuilder();
			if (userRedenvelope.getUseProjectType() != null) {// 有限制项目分类
				final String[] uuids = userRedenvelope.getUseProjectType().split(",");
				for (int i = 0; i < uuids.length; i++) {
					ProjectType projectType = projectTypeService.get(uuids[i]);
					if (i == uuids.length - 1) {
						useProjectTypeName.append(projectType.getTypeName());
					} else {
						useProjectTypeName.append(projectType.getTypeName()).append(',');
					}
				}
			}
			userRedenvelope.setUseProjectTypeName(useProjectTypeName.toString());
		}
		return list;
	}	
	
	@Override
	public void gainActivityRateCoupon(User user, String RateCouponRuleId) {
		if (null==user) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_NOT_LOGIN));
		}
		try {
			LOCK.lock();
			UserRateCoupon rp=new UserRateCoupon();
			rp.setUserId(user.getUuid());
			rp.setRuleId(RateCouponRuleId);
//			List<UserRateCoupon> list=dao.findList(rp);
			List<UserRateCoupon> urcs = new ArrayList<>();
			List<UserActivityAwardLog> logs = new ArrayList<>();
//			if (list.isEmpty()) {   //如果获取到发放记录，则报错
				RateCouponRule rule=rateCouponRuleServiceImpl.get(RateCouponRuleId);
				if (OperateEnum.ACTIVITYPLAN_CUSTOM.eq(rule.getActivityCode())) { //如果查询到的规则不是自定义发放的类型，说明是非法提交
					rateCouponRuleServiceImpl.grantRateCoupon(user, rule, null, null,urcs,logs);
				}
				else{
					throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_MSG_ILLEGAL));
				}
//			}
//			else{
//				throw new BussinessException(ResourceUtils.get(ResourceConstant.AWARD_ONLY_ONCE));
//			}
			if(!CollectionUtils.isEmpty(urcs)){
				dao.insertBatch(urcs);
			}
			if(!CollectionUtils.isEmpty(logs)){
				userActivityAwardLogService.insertBatch(logs);				
			}
		} finally {
			LOCK.unlock();
		}
	}

	@Override
	public Page<UserRateCoupon> findWebPage(UserRateCoupon entity) {
		Page<UserRateCoupon> page = entity.getPage();
		if(page==null){
			page = new Page<UserRateCoupon>();
		}
		List<UserRateCoupon> list = dao.findList(entity);
		for (UserRateCoupon userRedenvelope : list) {
			final StringBuilder useProjectTypeName = new StringBuilder();
			if (userRedenvelope.getUseProjectType() != null) {// 有限制项目分类
				final String[] uuids = userRedenvelope.getUseProjectType().split(",");
				for (int i = 0; i < uuids.length; i++) {
					ProjectType projectType = projectTypeService.get(uuids[i]);
					if (i == uuids.length - 1) {
						useProjectTypeName.append(projectType.getTypeName());
					} else {
						useProjectTypeName.append(projectType.getTypeName()).append(',');
					}
				}
			}
			userRedenvelope.setUseProjectTypeName(useProjectTypeName.toString());
		}
		page.setRows(list);
		return page;
	}
	
	@Override
	public void cancellationRateConpon(String RateCouponId) {
		UserRateCoupon userRateCoupon = get(RateCouponId);
		if (null == userRateCoupon || !Constant.STATUS_NO_USE.equals(userRateCoupon.getStatus())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.RATECOUPON_CANCEL_STATUS_ERROR));
		}
		userRateCoupon.setStatus(Constant.STATUS_CANCEL);
		update(userRateCoupon);
	}
	
	@Override
	public Map<String,Object> findByStatDate(StatisticModel model){
		//校验时间
		model.checkQueryDate();
        //根据日期查询符合记录
		final Map<String, Object> numMap = Maps.newHashMap();
		List<StatisticResultModel> redList = dao.findByStatDate(model);
		for (StatisticResultModel resultModel : redList) {
			numMap.put(resultModel.getStatDate(), resultModel.getTotalCount());
		}
		//返回
		final Map<String,Object> map = new HashMap<String, Object>();
		map.put("result", true);
		//日期横坐标
		map.put(StatisticConstant.STAT_CHART_DATE, statisticService.getStatChartDate(model));
		//红包数量
		map.put(StatisticConstant.STAT_CHART_NUM, statisticService.statByDate(numMap, model));
		return map;
	}
	
	@Override
	public Map<String,Object> findByStatType(StatisticModel model){
		//校验时间
		model.checkQueryDate();
        //根据状态查询符合记录
		final Map<String, Object> numMap = Maps.newHashMap();
		final Map<String, BigDecimal> numStatusMap = Maps.newHashMap();
		List<StatisticResultModel> redList = dao.findByStatType(model);
		for (StatisticResultModel resultModel : redList) {
			numStatusMap.put(resultModel.getType(), resultModel.getTotalCount());
		}
		Map<String,String> typeMap =  Maps.newHashMap();
		typeMap.put(OperateEnum.GRANT_TYPE_INTERVAL_AMOUNT_RATE.getName(), OperateEnum.GRANT_TYPE_INTERVAL_AMOUNT_RATE.getValue());
		typeMap.put(OperateEnum.GRANT_TYPE_FIXED_UPARP_RATE.getName(), OperateEnum.GRANT_TYPE_FIXED_UPARP_RATE.getValue());
		Iterator<String> it = typeMap.keySet().iterator();  
        while(it.hasNext()) {  
            String key = (String)it.next(); 
            String value = typeMap.get(key);
            numMap.put(key, numStatusMap.get(value)==null?"0":numStatusMap.get(value));
        }
		return numMap;
	}
	
	@Override
	public Map<String,Object> findByStatStatus(StatisticModel model){
		//校验时间
		model.checkQueryDate();
        //根据状态查询符合记录
		final Map<String, Object> numMap = Maps.newHashMap();
		final Map<String, BigDecimal> numStatusMap = Maps.newHashMap();
		List<StatisticResultModel> redList = dao.findByStatStatus(model);
		for (StatisticResultModel resultModel : redList) {
			numStatusMap.put(resultModel.getStatus(), resultModel.getTotalCount());
		}
		final List<DictData> dictDataList = dictDataService.findListByDictType(DictTypeEnum.ACTIVITY_LOG_STATUS.getValue());
		for (DictData dictData : dictDataList) {
			BigDecimal num = numStatusMap.get(dictData.getItemValue());
			numMap.put(dictData.getItemName(), num==null?BigDecimal.ZERO:num);
		}
		return numMap;
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
}