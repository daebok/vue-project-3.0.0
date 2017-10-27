package com.rd.ifaes.core.statistic.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.NumberUtils;
import com.rd.ifaes.core.core.util.DictUtils;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.statistic.constant.StatisticConstant;
import com.rd.ifaes.core.statistic.domain.StatisticUser;
import com.rd.ifaes.core.statistic.mapper.StatisticInvestMapper;
import com.rd.ifaes.core.statistic.mapper.StatisticUserMapper;
import com.rd.ifaes.core.statistic.model.DataModel;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.model.StatisticResultModel;
import com.rd.ifaes.core.statistic.service.StatisticInvestService;
import com.rd.ifaes.core.statistic.service.StatisticUserService;
import com.rd.ifaes.core.sys.domain.DictData;

/**
 * ServiceImpl:StatisticUserServiceImpl
 * @author fxl
 * @version 3.0
 * @date 2017-3-6
 */
@Service("statisticUserService") 
public class StatisticUserServiceImpl  extends BaseStatisticServiceImpl<StatisticUserMapper, StatisticUser> implements StatisticUserService{

	@Resource
	private StatisticInvestMapper statisticInvestMapper;
	@Resource
	private StatisticInvestService statisticInvestService;
	@Resource
	private ProjectService projectService;
	
	
	/**
	 * 用户统计数据接口
	 * @author fxl
	 * @date 2017年3月6日
	 * @param model
	 * @return
	 */
	@Override
	public Map<String, Object> getUserStatistic(StatisticModel model) {
		Map<String, StatisticResultModel> countMap = new HashMap<String, StatisticResultModel>();
		model.fillDictList(DictTypeEnum.AGE_RANGE.getValue());
		List<StatisticResultModel> list =  dao.getStatisticUser(model);
		BigDecimal lastTotal = dao.getLastTotal(model);
		for (StatisticResultModel resultModel : list) {
			countMap.put(resultModel.getStatDate(), resultModel);
		}
		return fillStatisticCountList(model, lastTotal, countMap);
	}
	
	
	/**
	 * 封装统计用户列表
	 * @author fxl
	 * @date 2017年3月6日
	 * @param model
	 * @param lastTotal
	 * @param countMap
	 * @return
	 */
	private Map<String, Object> fillStatisticCountList(StatisticModel model,BigDecimal lastTotal,Map<String, StatisticResultModel> countMap){
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> dateList = new ArrayList<String>();// 时间列表
		List<BigDecimal> totalList = new ArrayList<BigDecimal>();// 总计列表
		List<String> ratioList = new ArrayList<String>();// 环比列表
		Date calDate = model.getStartDateTime();
		String dateStr = model.getStartDate();
		while(!NumberUtils.isBigger(dateStr, model.getEndDate())){
			// 封装日期
			dateList.add(fomateDateStr(calDate,model));
			// 获取数据
			StatisticResultModel resultModel = countMap.get(dateStr);
			// 总计值
			BigDecimal total = resultModel!=null?resultModel.getTotalCount():BigDecimal.ZERO;
			totalList.add(total);
			ratioList.add(getRatioValue(lastTotal, total));
			// 记录本次的总计
			lastTotal = total;
			// 日期处理
			calDate = nextCalDate(model, calDate);
			dateStr = nextDateStr(model, calDate);
		}
		map.put("date", dateList);
		map.put("total", totalList);
		map.put("ratio", ratioList);
		return map;
	}

	/**
	 * 统计用户地区
	 * @author fxl
	 * @date 2017年2月23日
	 * @param model
	 * @return
	 */
	@Override
	public Map<String, Object> getUserArea(StatisticModel model) {
		Map<String, Object> map = new HashMap<String,Object>();
		model.fillDictList(DictTypeEnum.AGE_RANGE.getValue());
		List<Map<String,Object>> list = dao.getUserArea(model);
		List<DataModel> totalList = new ArrayList<DataModel>();
		for (Map<String, Object> result : list) {
			String area = (String) result.get("area");
			totalList.add(new DataModel(area,result.get("totalCount")));
		}
		map.put("total", totalList);
		return map;
	}

	/**
	 * 用户总数列表
	 * @author fxl
	 * @date 2017年3月7日
	 * @param model
	 * @return
	 */
	@Override
	public Map<String, Object> getUserCountList(StatisticModel model) {
		Date nowDate = DateUtils.getToday();
		Date lastMonth = DateUtils.rollMon(nowDate, -1);//一个月前
		Date lastThreeMonth = DateUtils.rollMon(nowDate, -3);// 三个月前
		Date lastSixMonth = DateUtils.rollMon(nowDate, -6);// 六个月前
		Map<String, Object> map = dao.getUserCountList(model.getRealNameStatus(),lastMonth,lastThreeMonth,lastSixMonth);
		return map;
	}

	/**
	 * 用户活跃度列表接口
	 * @author fxl
	 * @date 2017年3月6日
	 * @param model
	 * @return
	 */
	@Override
	public Map<String, Object> getUserActiveList(StatisticModel model) {
		Map<String, StatisticResultModel> countUserMap = new HashMap<String, StatisticResultModel>();
		Map<String, StatisticResultModel> activeLMap = new HashMap<String, StatisticResultModel>();
		List<StatisticResultModel> activeList =  new ArrayList<StatisticResultModel>();
		if(StatisticConstant.ACTIVE_TYPE_INVEST.equals(model.getActiveType())){// 投资
			activeList = statisticInvestMapper.getUserActiveByInvest(model);
		}else{// 登录
			activeList = dao.getUserActiveByLogin(model);
		}
		model.setChannelArr(null);// 取用户总额
		List<StatisticResultModel> userList =  dao.getStatisticUser(model);// 每日新增用户
		BigDecimal lastTotal =  dao.sumLastUserNum(model);// 开始日期前用户总数
		for (StatisticResultModel resultModel : userList) {
			countUserMap.put(resultModel.getStatDate(), resultModel);
		}
		for (StatisticResultModel active : activeList) {
			activeLMap.put(active.getStatDate(), active);
		}
		return fillUserActiveList(model, activeLMap, countUserMap,lastTotal);
	}

	/**
	 * 填充活跃度列表
	 * @author fxl
	 * @date 2017年3月9日
	 * @param model
	 * @param activeMap
	 * @param countUserMap
	 * @return
	 */
	private Map<String, Object> fillUserActiveList(StatisticModel model,Map<String, StatisticResultModel> activeMap,Map<String, StatisticResultModel> countUserMap,BigDecimal lastTotal){
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> dateList = new ArrayList<String>();// 时间列表
		List<BigDecimal> totalList = new ArrayList<BigDecimal>();// 总计列表
		List<BigDecimal> activeList = new ArrayList<BigDecimal>();// 活跃率列表
		Date calDate = model.getStartDateTime();
		String dateStr = model.getStartDate();
		while(!NumberUtils.isBigger(dateStr, model.getEndDate())){
			// 封装日期
			dateList.add(fomateDateStr(calDate,model));
			// 获取数据
			StatisticResultModel resultModel = countUserMap.get(dateStr);
			StatisticResultModel activeModel = activeMap.get(dateStr);
			// 总计
			BigDecimal total = resultModel!=null?resultModel.getTotalCount():BigDecimal.ZERO;
			lastTotal = BigDecimalUtils.add(total,lastTotal);
			BigDecimal active = activeModel!=null?activeModel.getTotalCount():BigDecimal.ZERO;
			// 活跃用户
			totalList.add(active);
			// 活跃率 = 活跃用户 / 当日用户总数
			activeList.add(getDivValue(active, lastTotal));
			// 日期处理
			calDate = nextCalDate(model, calDate);
			dateStr = nextDateStr(model, calDate);
		}
		map.put("date", dateList);
		map.put("total", totalList);
		map.put("active", activeList);
		return map;
	}
	
	/**
	 * 用户活跃度地区接口
	 * @author fxl
	 * @date 2017年3月6日
	 * @param model
	 * @return
	 */
	@Override
	public Map<String, Object> getUserActiveArea(StatisticModel model) {
		Map<String, Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		if(StatisticConstant.ACTIVE_TYPE_INVEST.equals(model.getActiveType())){// 投资
			list = statisticInvestMapper.getUserActiveAreaByInvest(model);
		}else{// 登录
			list = dao.getUserActiveAreaByLogin(model);
		}		
		List<DataModel> totalList = new ArrayList<DataModel>();
		for (Map<String, Object> result : list) {
			String area = (String) result.get("area");
			totalList.add(new DataModel(area,result.get("totalCount")));
		}
		map.put("total", totalList);
		return map;
	}
	
	/**
	 * 用户活跃度总计接口
	 * @author fxl
	 * @date 2017年3月6日
	 * @param model
	 * @return
	 */
	@Override
	public Map<String, Object> getUserActiveCountList(StatisticModel model) {
		Date nowDate = DateUtils.getToday();
		Date lastSevenDay = DateUtils.rollDay(nowDate, -7);// 过去7天
		Date lastThirtyDay = DateUtils.rollDay(nowDate, -30);// 过去30天
		Date lastNinetyDay = DateUtils.rollDay(nowDate, -90);// 过去90天
		Date lastHalfYear = DateUtils.rollDay(nowDate, -180);// 过去180天
		if(StatisticConstant.ACTIVE_TYPE_INVEST.equals(model.getActiveType())){// 投资
			return statisticInvestMapper.getUserActiveCountByInvest(model.getChannelArr(),lastSevenDay,lastThirtyDay,lastNinetyDay,lastHalfYear);
		}else{// 登录lastSevenDay
			return dao.getUserActiveCountByLogin(model.getChannelArr(),lastSevenDay,lastThirtyDay,lastNinetyDay,lastHalfYear);
		}
	}
	
	/**
	 * 获取用户画像
	 * @author fxl
	 * @date 2017年3月7日
	 * @param model
	 * @return
	 */
	@Override
	public List<Map<String, BigDecimal>> getUserPortrait(StatisticModel model) {
		List<Map<String,BigDecimal>> list = new ArrayList<Map<String,BigDecimal>>();
		// 性别
		List<DictData> sexDictDataList = new ArrayList<DictData>();
		sexDictDataList.add(new DictData("男", "M"));
		sexDictDataList.add(new DictData("女", "F"));
		model.setDictList(sexDictDataList);
		list.add(dao.countUserSex(model));
		// 年龄
		model.fillDictList(DictTypeEnum.AGE_RANGE.getValue());
		list.add(dao.countUserAge(model));
		// 学历
		model.setDictList(DictUtils.list(DictTypeEnum.EDUCATION_LEVEL.getValue()));
		list.add(dao.countUsereducation(model));
		// 婚姻
		model.setDictList(DictUtils.list(DictTypeEnum.MARITAL_STATUS.getValue()));
		list.add(dao.countUserMarital(model));
		// 年收入
		if(!StatisticConstant.USER_TYPE_OVERDUE.equals(model.getUserType())){
			model.setDictList(DictUtils.list(DictTypeEnum.SALARY_RANGE.getValue()));
			list.add(dao.countUserIncome(model));
		}
		// 用户类型
		model.setDictList(DictUtils.list(DictTypeEnum.USER_NATURE.getValue()));
		list.add(dao.countUserNatrue(model));
		return list;
	}

	/**
	 * 更新用户类型
	 * @author fxl
	 * @date 2017年3月7日
	 * @param userId
	 * @param userType
	 */
	@Override
	public void updateUserType(String userId, String userType) {
		dao.updateUserType(userId,userType);
	}

}