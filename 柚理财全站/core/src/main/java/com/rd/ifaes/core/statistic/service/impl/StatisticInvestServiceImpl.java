package com.rd.ifaes.core.statistic.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.CommonConstants;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.DictConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.operate.service.UserRateCouponService;
import com.rd.ifaes.core.operate.service.UserRedenvelopeService;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.domain.ProjectType;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.project.service.ProjectTypeService;
import com.rd.ifaes.core.statistic.constant.StatisticConstant;
import com.rd.ifaes.core.statistic.domain.StatisticInvest;
import com.rd.ifaes.core.statistic.domain.StatisticUser;
import com.rd.ifaes.core.statistic.mapper.StatisticInvestMapper;
import com.rd.ifaes.core.statistic.model.DataModel;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.model.StatisticResultModel;
import com.rd.ifaes.core.statistic.service.StatisticInvestService;
import com.rd.ifaes.core.statistic.service.StatisticService;
import com.rd.ifaes.core.statistic.service.StatisticUserService;
import com.rd.ifaes.core.sys.domain.DictData;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.service.UserService;


/**
 * 投资统计实现类
 * @version 3.0
 * @author xhf
 * @date 2017年2月21日
 */
@Service("statisticInvestService") 
public class StatisticInvestServiceImpl  extends BaseStatisticServiceImpl<StatisticInvestMapper, StatisticInvest> implements StatisticInvestService{

	@Resource
	private ProjectInvestService projectInvestService;
	
	@Resource
	private ProjectService projectService;
	
	@Resource
	private ProjectTypeService projectTypeService;
	
	@Resource
	private UserService userService;
	
	@Resource
	private StatisticService statisticService;
	
	@Resource
	private UserRedenvelopeService userRedenvelopeService;
	
	@Resource
	private UserRateCouponService userRateCouponService;
	
	@Resource
	private StatisticUserService statisticUserService;
	
	/**
	 * 新增统计分析记录
	 * @throws ParseException 
	 */
	@Override
	public void recordByDate(Date investDate){
		//判断是否已经统计过
		int count = dao.getCount(new StatisticInvest(investDate));
		if(count>0){
			return;
		}
		
		final int dayOfMonth = ConfigUtils.getInt(ConfigConstant.DAYS_OF_MONTH,Constant.INT_THIRTING);
		final String investDateStr = DateUtils.formatDate(investDate, DateUtils.DATEFORMAT_STR_012);
		final List<StatisticInvest> statisticInvestList = new ArrayList<StatisticInvest>();
		
		List<ProjectInvest> investList = projectInvestService.findByInvestDate(investDateStr);
		for (ProjectInvest projectInvest : investList) {
			StatisticInvest statisticInvest = new StatisticInvest();
			statisticInvest.setInvestDate(DateUtils.valueOf(projectInvest.getInvestDate(), DateUtils.DATEFORMAT_STR_012));
			statisticInvest.setInvestId(projectInvest.getUuid());
			statisticInvest.setInvestAmount(projectInvest.getAmount());
			statisticInvest.setInvestChannel(projectInvest.getInvestChannel());
			statisticInvest.setRealAmount(projectInvest.getRealAmount());
			
			//区域
			StatisticUser statUser = statisticUserService.get(projectInvest.getUserId());
			statisticInvest.setInvestArea(statUser==null?"":statUser.getArea());
			//project
			Project project = projectService.get(projectInvest.getProjectId());
			statisticInvest.setProjectApr(project.getApr());
			if(LoanEnum.TIME_TYPE_MONTH.getValue().equals(project.getTimeType())){ //月标
				statisticInvest.setProjectTimeLimit(project.getTimeLimit()*dayOfMonth);
			}else{
				statisticInvest.setProjectTimeLimit(project.getTimeLimit());
			}
			//产品是否加息
			if(project.getAddApr()!=null && project.getAddApr().compareTo(BigDecimal.ZERO)>0){
				statisticInvest.setIsAddApr(CommonConstants.YES);
			}else{
				statisticInvest.setIsAddApr(CommonConstants.NO);
			}
			//projectType
			if(CommonEnum.YES.getValue().equals(projectInvest.getRealizeFlag())){ //是否变现产品
				statisticInvest.setProjectType(StatisticConstant.PROJECT_TYPE_REALIZE);
			}else if(StringUtils.isNotBlank(projectInvest.getOriginInvestId())){ //转让订单有原始订单ID
				statisticInvest.setProjectType(StatisticConstant.PROJECT_TYPE_BOND);
			}else{ //普通订单类型以projectType的ID为准
				ProjectType projectType = projectTypeService.get(project.getProjectTypeId());
				statisticInvest.setProjectType(projectType.getNid());
			}
			//是否使用了红包
			int redCount = userRedenvelopeService.getCountByTenderId(projectInvest.getUuid());
			if(redCount>0){
				statisticInvest.setIsUseRed(CommonConstants.YES);
			}else{
				statisticInvest.setIsUseRed(CommonConstants.NO);
			}
			//是否使用了加息券
			int rateCount = userRateCouponService.getCountByTenderId(projectInvest.getUuid());
			if(rateCount>0){
				statisticInvest.setIsUseRate(CommonConstants.YES);
			}else{
				statisticInvest.setIsUseRate(CommonConstants.NO);
			}
			//user
			User user = userService.get(projectInvest.getUserId());
			if(user!=null){
				statisticInvest.setUserId(projectInvest.getUserId());
				statisticInvest.setUserNature(user.getUserNature());
				statisticInvestList.add(statisticInvest);	
			}

		}
		//save
		this.insertBatch(statisticInvestList);
	}
	
	/**
	 * 统计投资人数/金额/笔数
	 * @return
	 * @throws ParseException 
	 */
	@Override
	public Map<String, Object> findInvestByStatType(StatisticModel model){
		final Map<String, Object> map = Maps.newHashMap();
		map.put("result", true);
		//校验时间
		model.checkQueryDate();
		//利率
		if(model.getAprArr()!=null && model.getAprArr().length>0){
			model.fillDictList(DictConstant.STAT_APR_TYPE);
		}
		
		//统计所有状态
		String[] typeArra = null;
		if(model.getProjectType()!=null && model.getProjectType().length>0){
			typeArra = model.getProjectType().clone();
		}else{
			final List<ProjectType> typeList = projectTypeService.findUseProjectType();
			typeArra = new String[typeList.size()+1];
			for (int i = 0; i < typeList.size(); i++) {
				typeArra[i] = typeList.get(i).getNid();
			}
			//变现理财
			//typeArra[typeList.size()] = StatisticConstant.PROJECT_TYPE_REALIZE;
	        //债权转让
			typeArra[typeList.size()] = StatisticConstant.PROJECT_TYPE_BOND;
		}
		
		//日期横坐标
		map.put(StatisticConstant.STAT_CHART_DATE, statisticService.getStatChartDate(model));
		
		//总计
		if(model.getProjectType()==null || model.getProjectType().length==0){
			model.setProjectType(typeArra);
		}
		List<String> totalNumList = statisticService.statByDate(dao.findInvestByStatType(model), model); 
		map.put(StatisticConstant.STAT_CHART_TOTAL, totalNumList);
		//环比值
		//计算查询日期前一天的记录数
		BigDecimal beforeCount = dao.findInvestDayBefore(model);
		map.put(StatisticConstant.STAT_CHART_RATIO, statisticService.statisticRatio(totalNumList, beforeCount));
		
		//单个状态查询
		for (String type : typeArra) {
			model.setProjectType(new String[]{type});
			map.put(type, statisticService.statByDate(dao.findInvestByStatType(model), model));
		}
		return map;
	}
	
	/**
	 * 统计投资情况
	 * @author xhf
	 * @date 2017年2月23日
	 * @param model
	 * @return
	 */
	@Override
	public List<Map<String, BigDecimal>> getInvestCondition(StatisticModel model) {
		List<Map<String, BigDecimal>> list = new ArrayList<Map<String,BigDecimal>>();
		if(StatisticConstant.REPORT_TYPE_COUNT.equals(model.getReportType())){
			//产品类型
			model.setDictList(getProjectTypeDict());
			list.add(dao.countByProjectType(model));
			//投资金额
			model.fillDictList(DictTypeEnum.INVEST_AMOUNT_RANGE.getValue());
			list.add(dao.countByProjectAmnt(model)); 
			//年利率
			model.fillDictList(DictConstant.STAT_APR_TYPE);
			list.add(dao.countByProjectApr(model));
			//借款期限
			model.fillDictList(DictTypeEnum.TIME_LIMIT_RANGE.getValue());
			list.add(dao.countByProjectTimeLimit(model));
		}else{
			//产品类型
			model.setDictList(getProjectTypeDict());
			list.add(dao.sumByProjectType(model));
			//年利率
			model.fillDictList(DictConstant.STAT_APR_TYPE);
			list.add(dao.sumByProjectApr(model)); 
			//借款期限
			model.fillDictList(DictTypeEnum.TIME_LIMIT_RANGE.getValue());
			list.add(dao.sumByProjectTimeLimit(model));
		}
		return list;
	}
	
	/**
	 * 统计投资情况-产品类型
	 * @param projectTypeList
	 * @return
	 */
	private List<DictData> getProjectTypeDict(){
		List<DictData> dataList = new ArrayList<DictData>();
		//普通理财
		List<ProjectType> typeList = projectTypeService.findUseProjectType();
		for (ProjectType projectType : typeList) {
			dataList.add(new DictData(projectType.getTypeName(),projectType.getNid()));
		}
		//变现理财
		//dataList.add(new DictData(StatisticConstant.PROJECT_TYPE_REALIZE_NAME,StatisticConstant.PROJECT_TYPE_REALIZE));
        //债权转让
		dataList.add(new DictData(StatisticConstant.PROJECT_TYPE_BOND_NAME,StatisticConstant.PROJECT_TYPE_BOND));
        return dataList;
	}
	
	/**
	 * 投资地区统计
	 * @author xhf
	 * @date 2017年2月23日
	 * @param model
	 * @return
	 */
	@Override
	public Map<String, Object> getInvestArea(StatisticModel model) {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("result", true);
		
		List<StatisticResultModel> dataList = dao.getInvestArea(model);
		List<DataModel> investUserList = new ArrayList<DataModel>();
		List<DataModel> investAmntList = new ArrayList<DataModel>();
		List<DataModel> investNumList = new ArrayList<DataModel>();
		int totalUsers = 0;
		BigDecimal totalAmnt = BigDecimal.ZERO;
		BigDecimal totalNums = BigDecimal.ZERO;
		for (StatisticResultModel resultModel : dataList) {
			investUserList.add(new DataModel(resultModel.getArea(),resultModel.getUserNum()));
			investAmntList.add(new DataModel(resultModel.getArea(),resultModel.getTotalAmnt()));
			investNumList.add(new DataModel(resultModel.getArea(),resultModel.getTotalCount()));
			totalUsers = totalUsers+resultModel.getUserNum();
			totalAmnt = totalAmnt.add(resultModel.getTotalAmnt());
			totalNums = totalNums.add(resultModel.getTotalCount());
		}
		//图形
		map.put("investUser", investUserList);
		map.put("investAmnt", investAmntList);
		map.put("investNum", investNumList);
		//列表
		map.put("dataList", dataList);
		map.put("totalUsers", totalUsers);
		map.put("totalAmnt", totalAmnt);
		map.put("totalNums", totalNums);
		return map;
	}
	
	/**
	 * 投资地区列表统计
	 * @author xhf
	 * @date 2017年2月23日
	 * @param model
	 * @return
	 */
	@Override
	public Map<String, Object> getInvestAreaList(StatisticModel model) {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("result", true);
		
		int totalUsers = 0;
		BigDecimal totalAmnt = BigDecimal.ZERO;
		BigDecimal totalNums = BigDecimal.ZERO;
		
		List<StatisticResultModel> dataList = dao.getInvestArea(model);
		for (StatisticResultModel resultModel : dataList) {
			totalUsers = totalUsers+resultModel.getUserNum();
			totalAmnt = totalAmnt.add(resultModel.getTotalAmnt());
			totalNums = totalNums.add(resultModel.getTotalCount());
		}
		//列表
		map.put("dataList", dataList);
		map.put("totalUsers", totalUsers);
		map.put("totalAmnt", totalAmnt);
		map.put("totalNums", totalNums);
		return map;
	}
	
	@Override
	public Map<String,Object> getInvestList(StatisticModel model){
		//校验
		model.checkQueryDate();
		//年利率
		if(model.getAprArr()!=null && model.getAprArr().length>0){
			model.fillDictList(DictConstant.STAT_APR_TYPE);
		}

		//状态
		final List<ProjectType> typeList = projectTypeService.findUseProjectType();
		String[] typeArra = new String[typeList.size()+1];
		for (int i = 0; i < typeList.size(); i++) {
			typeArra[i] = typeList.get(i).getNid();
		}
		//变现理财
		//typeArra[typeList.size()] = StatisticConstant.PROJECT_TYPE_REALIZE;
        //债权转让
		typeArra[typeList.size()] = StatisticConstant.PROJECT_TYPE_BOND;
		
		//状态
		if(model.getProjectType()==null || model.getProjectType().length==0){
			//列表
			model.setProjectType(typeArra);
		}
		//总数
		List<String> dateCountList = statisticService.statByDate(dao.findInvestByStatType(model), model);
		
		//设置导出列名和字段
		//单个状态查询
		Map<String, List<String>> typeListMap = new HashMap<String,List<String>>();
		for (String type : model.getProjectType()) {
			model.setProjectType(new String[]{type});
			typeListMap.put(type, statisticService.statByDate(dao.findInvestByStatType(model), model));
		}
		
		//日期
		List<String> dateList = statisticService.getStatChartDate(model);
		
		List<Object> dataList = new ArrayList<Object>();
		for (int i = 0; i < dateList.size(); i++) {
			Map<String,String> columnDataMap = Maps.newHashMap();
			//日期
			columnDataMap.put(StatisticConstant.STAT_LIEST_DATE, dateList.get(i));
			//类型
			for (String type : typeArra) {
				String typeCount = CollectionUtils.isEmpty(typeListMap.get(type))?"0":typeListMap.get(type).get(i);
				columnDataMap.put(type,StringUtils.isBlank(typeCount)?"0":typeCount);
			}
			//总计
			String totalCount = CollectionUtils.isEmpty(dateCountList)?"0":dateCountList.get(i);
			columnDataMap.put(StatisticConstant.STAT_LIEST_TOTAL, StringUtils.isBlank(totalCount)?"0":totalCount);
			dataList.add(columnDataMap);
		}
		
		//总数
		model.setProjectType(typeArra);
		BigDecimal totalCount = dao.getTotalCountByDate(model);
		if(totalCount != null){
			if(StatisticConstant.STAT_TYPE_AMOUNT.equals(model.getStatType())){
				totalCount = totalCount.divide(new BigDecimal(10000),2,BigDecimal.ROUND_HALF_UP);
			}
		}else{
			totalCount = BigDecimal.ZERO;
		}
		
		Map<String, Object> dataMap = new HashMap<String,Object>();
		dataMap.put("result", true);
		dataMap.put("dataList", dataList);
		dataMap.put("totalCount", totalCount);
		return dataMap;
	}
	
	@Override
	public Map<String,Object> findRealAmntByDate(StatisticModel model){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("result", true);
		//校验时间
		model.checkQueryDate();
		//金额保持两位小数
		model.setStatType(StatisticConstant.STAT_TYPE_AMOUNT);
		//日期
		List<String> dateList = statisticService.getStatChartDate(model);
		map.put(StatisticConstant.STAT_CHART_DATE, dateList);
		//投资金额
		List<String> amntList = statisticService.statByDate(dao.findRealAmntByDate(model), model);
		map.put(StatisticConstant.STAT_CHART_AMNT, amntList);
		//金额占比
		List<String> proportionList = new ArrayList<String>();
		BigDecimal totalRealAmnt = dao.getTotalRealAmnt(model);
		for (String amntStr : amntList) {
			if(StringUtils.isBlank(amntStr)){
				proportionList.add("0");
			}else{
				proportionList.add(calProportion(new BigDecimal(amntStr),totalRealAmnt));
			}
		}
		map.put(StatisticConstant.STAT_CHART_RATIO, proportionList);
		return map;
	}
	
	private String calProportion(BigDecimal before,BigDecimal after){
		String result = "0";
		if(before!=null && after!=null && BigDecimal.ZERO.compareTo(before)<0){
			result = BigDecimalUtils.mul(before, new BigDecimal(100)).divide(after, 2, BigDecimal.ROUND_HALF_UP).toString();
		}
		return result;
	}
	
	@Override
	public Map<String,BigDecimal> getRealAmntMap(StatisticModel model){
		return dao.getRealAmntMap(model);
	}
	
	@Override
	public Map<String,Object> compareDataByDate(StatisticModel model){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("result", true);
		//校验时间
		model.checkQueryDate();
		model.checkCompareDate();
		//利率
		if(model.getAprArr()!=null && model.getAprArr().length>0){
			model.fillDictList(DictConstant.STAT_APR_TYPE);
		}
		//原查询数据
		List<String> dateSelect = statisticService.getStatChartDate(model);
		map.put(StatisticConstant.COMPARE_KEY_SELECT, model.getCompareDateKey());
		map.put(StatisticConstant.COMPARE_DATA_SELECT, statisticService.statByDate(dao.findInvestByStatType(model), model));
		//对比数据
		model.setStartDate(model.getContrastStartDate());
		model.setEndDate(model.getContrastEndDate());
		List<String> dateContrast = statisticService.getStatChartDate(model);
		map.put(StatisticConstant.COMPARE_KEY_CONTRAST, model.getCompareDateKey());
		map.put(StatisticConstant.COMPARE_DATA_CONTRAST, statisticService.statByDate(dao.findInvestByStatType(model), model));
		//对比日期
		map.put(StatisticConstant.COMPARE_DATE, statisticService.getCompareDateList(dateSelect, dateContrast));
		//返回结果
		return map;
	}
	
	@Override
	public Map<String,String> getProjectTypeMap(){
		final Map<String,String> typeMap = Maps.newLinkedHashMap();
		final List<ProjectType> typeList = projectTypeService.findUseProjectType();
		for (int i = 0; i < typeList.size(); i++) {
			typeMap.put(typeList.get(i).getNid(),typeList.get(i).getTypeName());
		}
		//列名-变现理财
		//ypeMap.put(StatisticConstant.PROJECT_TYPE_REALIZE,StatisticConstant.PROJECT_TYPE_REALIZE_NAME);
        //列名-债权转让
		typeMap.put(StatisticConstant.PROJECT_TYPE_BOND,StatisticConstant.PROJECT_TYPE_BOND_NAME);
		return typeMap;
	}
}