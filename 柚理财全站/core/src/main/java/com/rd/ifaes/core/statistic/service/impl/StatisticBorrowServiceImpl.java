package com.rd.ifaes.core.statistic.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.dict.UserEnum;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.NumberUtils;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.project.domain.Borrow;
import com.rd.ifaes.core.project.service.BorrowService;
import com.rd.ifaes.core.statistic.constant.StatisticConstant;
import com.rd.ifaes.core.statistic.domain.StatisticBorrow;
import com.rd.ifaes.core.statistic.domain.StatisticUser;
import com.rd.ifaes.core.statistic.mapper.StatisticBorrowMapper;
import com.rd.ifaes.core.statistic.model.DataModel;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.model.StatisticResultModel;
import com.rd.ifaes.core.statistic.service.StatisticBorrowService;
import com.rd.ifaes.core.statistic.service.StatisticService;
import com.rd.ifaes.core.statistic.service.StatisticUserService;


/**
 * 借款统计实现类
 * @version 3.0
 * @author fxl
 * @date 2017年2月21日
 */
@Service("statisticBorrowService") 
public class StatisticBorrowServiceImpl extends BaseStatisticServiceImpl<StatisticBorrowMapper, StatisticBorrow> implements StatisticBorrowService{

	@Resource
	private BorrowService borrowService;
	@Resource
	private StatisticService statisticService;
	@Resource
	private StatisticUserService statisticUserService;
	
	/**
	 * 记录所传日期的借款信息
	 * @author fxl
	 * @date 2017年2月22日
	 * @param date
	 */
	@Override
	@Transactional(readOnly = false)
	public void recordBorrowByDate(Date lastDate) {
		final int dayOfMonth = ConfigUtils.getInt(ConfigConstant.DAYS_OF_MONTH,Constant.INT_THIRTING);
		List<Borrow> borrowList = borrowService.findBorrowListByDate(lastDate);
		List<StatisticBorrow> statisticList = new ArrayList<>();
		for (Borrow borrow : borrowList) {
			StatisticBorrow statisticBorrow = new StatisticBorrow(borrow.getUuid(), lastDate, borrow.getUserId(), borrow.getIsVouch(),
					borrow.getHasPawn(), borrow.getAccount(), borrow.getBorrowNature());
			statisticBorrow.setTimeLimit(calTimeLimit(borrow,dayOfMonth));
			// 借款人地区
			StatisticUser stauser = statisticUserService.get(borrow.getUserId());
			if(stauser!=null){
				statisticBorrow.setArea(stauser.getArea());
				// 标记为借款人
				statisticUserService.updateUserType(stauser.getUserId(), UserEnum.USER_TYPE_BORROWER.getValue());
			}
			statisticBorrow.preInsert();
			statisticList.add(statisticBorrow);
		}
		// 存入数据库
		if(CollectionUtils.isNotEmpty(statisticList)){
			dao.insertBatch(statisticList);
		}
	}

	/**
	 * 获取统计结果集
	 * @author fxl
	 * @date 2017年2月23日
	 * @param msodel
	 * @return
	 */
	public Map<String, StatisticResultModel> getBorrowStatisticMap(StatisticModel model){
		// 获取统计结果集
		Map<String, StatisticResultModel> countMap = new HashMap<String, StatisticResultModel>();
		List<StatisticResultModel> list =  dao.getBorrowStatistic(model);
		for (StatisticResultModel resultModel : list) {
			countMap.put(resultModel.getStatDate(), resultModel);
		}
		return countMap;
	}
	
	/**
	 * 封装统计列表
	 * @author fxl
	 * @date 2017年2月27日
	 * @param model
	 * @param countMap
	 * @return
	 */
	@Override
	public Map<String, Object> getBorrowStatistic(StatisticModel model){
		model.checkQueryDate();
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> dateList = new ArrayList<String>();// 时间列表
		List<BigDecimal> totalList = new ArrayList<BigDecimal>();// 总计列表
		List<BigDecimal> mortgageList = new ArrayList<BigDecimal>();// 抵押列表
		List<BigDecimal> vouchList = new ArrayList<BigDecimal>();// 担保列表
		List<BigDecimal> otherList = new ArrayList<BigDecimal>();// 其他列表
		List<String> ratioList = new ArrayList<String>();// 环比列表
		Date calDate = model.getStartDateTime();
		String dateStr = model.getStartDate();
		// 转化金额区间
		model.fillDictList(DictTypeEnum.ACCOUNT_RANGE.getValue());
		Map<String, StatisticResultModel> countMap = getBorrowStatisticMap(model);// 获取数据
		BigDecimal lastTotal = dao.getLastTotal(model);//前一天的值
		while(!NumberUtils.isBigger(dateStr, model.getEndDate())){
			// 封装日期
			dateList.add(fomateDateStr(calDate,model));
			StatisticResultModel resultModel = countMap.get(dateStr);
			// 总计值
			BigDecimal total = resultModel!=null?resultModel.getTotalCount():BigDecimal.ZERO;
			totalList.add(total);
			mortgageList.add(resultModel!=null?resultModel.getMortgageCount():BigDecimal.ZERO);
			vouchList.add(resultModel!=null?resultModel.getVouchCount():BigDecimal.ZERO);
			otherList.add(resultModel!=null?resultModel.getOtherCount():BigDecimal.ZERO);
			ratioList.add(getRatioValue(lastTotal, total));
			// 记录本次的总计
			lastTotal = total;
			// 日期处理
			calDate = nextCalDate(model, calDate);
			dateStr = nextDateStr(model, calDate);
		} 
 		map.put(StatisticConstant.DATE, dateList);
		map.put(StatisticConstant.TOTAL, totalList);
		map.put(StatisticConstant.MORTGAGE, mortgageList);
		map.put(StatisticConstant.VOUCH, vouchList);
		map.put(StatisticConstant.OTHER, otherList);
		map.put(StatisticConstant.RATIO, ratioList);
		return map;
	}
	
	/**
	 * 获取期限
	 * @author fxl
	 * @date 2017年2月27日
	 * @return
	 */
	public Integer calTimeLimit(Borrow borrow,int dayOfMonth){
		if(LoanEnum.TIME_TYPE_MONTH.eq(borrow.getTimeType())){
			return borrow.getTimeLimit()*dayOfMonth;
		}else{
			return borrow.getTimeLimit();
		}
	}
	
	/**
	 * 对比时段
	 * @author fxl
	 * @date 2017年3月15日
	 * @param model
	 * @return
	 */
	@Override
	public Map<String,Object> compareDataByDate(StatisticModel model){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("result", true);
		//校验时间
		model.checkQueryDate();
		//原查询数据
		model.fillDictList(DictTypeEnum.ACCOUNT_RANGE.getValue());
		Map<String, StatisticResultModel> countMap = getBorrowStatisticMap(model);
		List<String> selectDate = statisticService.getStatChartDate(model);
		map.put(StatisticConstant.SELECT, fillcompareData(model,countMap));
		//对比数据
		model.setStartDate(model.getContrastStartDate());
		model.setEndDate(model.getContrastEndDate());
		Map<String, StatisticResultModel> newcountMap = getBorrowStatisticMap(model);
		List<String> contrastDate = statisticService.getStatChartDate(model);
		map.put(StatisticConstant.CONTRAST, fillcompareData(model,newcountMap));
		//对比日期
		map.put(StatisticConstant.COMPARE_DATE_SELECT, statisticService.getCompareDateList(selectDate, contrastDate));
		//返回结果
		return map;
	}
	
	/**
	 * 填充对比时段
	 * @author fxl
	 * @date 2017年3月15日
	 * @param model
	 * @return
	 */
	private List<BigDecimal> fillcompareData(StatisticModel model,Map<String, StatisticResultModel> countMap){
		List<BigDecimal> totalList = new ArrayList<BigDecimal>();// 总计列表
		Date calDate = model.getStartDateTime();
		String dateStr = model.getStartDate();
		while(!NumberUtils.isBigger(dateStr, model.getEndDate())){
			// 获取数据
			StatisticResultModel resultModel = countMap.get(dateStr);
			// 总计值
			totalList.add(resultModel!=null?resultModel.getTotalCount():BigDecimal.ZERO);
			// 日期处理
			calDate = nextCalDate(model, calDate);
			dateStr = nextDateStr(model, calDate);
		} 
		return totalList;
	}
	
	/**
	 * 借款列表
	 * @author fxl
	 * @date 2017年3月15日
	 * @param model
	 * @return
	 */
	@Override
	public Map<String, Object> getBorrowList(StatisticModel model){
		model.checkQueryDate();
		Map<String, Object> map = new HashMap<String,Object>();
		BigDecimal totalCount = BigDecimal.ZERO;
		model.fillDictList(DictTypeEnum.ACCOUNT_RANGE.getValue());
		Map<String, StatisticResultModel> countMap = getBorrowStatisticMap(model);
		List<StatisticResultModel> newList =  new ArrayList<StatisticResultModel>();
		Date calDate = model.getStartDateTime();
		String dateStr = model.getStartDate();
		while(!NumberUtils.isBigger(dateStr, model.getEndDate())){
			// 获取数据
			StatisticResultModel resultModel = countMap.get(dateStr);
			newList.add(resultModel!=null?resultModel:new StatisticResultModel(dateStr,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO));
			BigDecimal total = resultModel!=null?resultModel.getTotalCount():BigDecimal.ZERO;
			totalCount = totalCount.add(total);
			// 日期处理
			calDate = DateUtils.rollDay(calDate, 1);
			dateStr = DateUtils.formatDate(calDate, DateUtils.DATEFORMAT_STR_012);
		} 
		map.put(StatisticConstant.RESULT, true);
		map.put(StatisticConstant.ROWS, newList);
		map.put(StatisticConstant.TOTAL, totalCount);
		return map;
	}
	
	
	/**
	 * 统计借款地区
	 * @author fxl
	 * @date 2017年2月23日
	 * @param model
	 * @return
	 */
	@Override
	public Map<String, Object> getBorrowArea(StatisticModel model) {
		model.checkQueryDate();
		Map<String, Object> map = new HashMap<String,Object>();
		List<StatisticResultModel> list = dao.getBorrowArea(model);
		List<DataModel> borrowerNumList = new ArrayList<DataModel>();
		List<DataModel> accountNumList = new ArrayList<DataModel>();
		List<DataModel> borrowNumList = new ArrayList<DataModel>();
		for (StatisticResultModel result : list) {
			String area = result.getArea();
			borrowerNumList.add(new DataModel(area,result.getBorrowerNum()));
			accountNumList.add(new DataModel(area,result.getAccount()));
			borrowNumList.add(new DataModel(area,result.getBorrowNum()));
		}
		map.put(StatisticConstant.BORROWER_NUM, borrowerNumList);
		map.put(StatisticConstant.ACCOUNT, accountNumList);
		map.put(StatisticConstant.BORROW_NUM, borrowNumList);
		return map;
	}
	
	/**
	 * 借款地区列表
	 * @author fxl
	 * @date 2017年2月23日
	 * @param model
	 * @return
	 */
	@Override
	public Map<String, Object> getBorrowAreaList(StatisticModel model) {
		model.checkQueryDate();
		Map<String, Object> map = new HashMap<String,Object>();
		List<StatisticResultModel> list = dao.getBorrowArea(model);
		StatisticResultModel result = dao.getBorrowTotal(model);
		result.setArea("总计");
		int count = list!=null?list.size():0;
		list.add(result);
		map.put(StatisticConstant.RESULT, true);
		map.put(StatisticConstant.ROWS, list);
		map.put(StatisticConstant.TOTAL, count);
		return map;
	}
	
	/**
	 * 统计借款情况
	 * @author fxl
	 * @date 2017年2月23日
	 * @param model
	 * @return
	 */
	@Override
	public List<Map<String,BigDecimal>> getBorrowInfo(StatisticModel model) {
		model.checkQueryDate();
		List<Map<String,BigDecimal>> list = new ArrayList<Map<String,BigDecimal>>();
		if(StatisticConstant.STAT_TYPE_AMOUNT.equals(model.getStatType())){
			// 借款期限
			model.fillDictList(DictTypeEnum.TIME_LIMIT_RANGE.getValue());
			list.add(dao.sumByBorrowTimeLimit(model));
			// 借款类型
			model.fillDictList(DictTypeEnum.BORROW_TYPE_RANGE.getValue());
			list.add(dao.sumByBorrowType(model));
		}else{
			// 借款金额
			model.fillDictList(DictTypeEnum.ACCOUNT_RANGE.getValue());
			list.add(dao.countByBorrowAccount(model));
			// 借款期限
			model.fillDictList(DictTypeEnum.TIME_LIMIT_RANGE.getValue());
			list.add(dao.countByBorrowTimeLimit(model));
			// 借款类型
			model.fillDictList(DictTypeEnum.BORROW_TYPE_RANGE.getValue());
			list.add(dao.countByBorrowType(model));
		}
		return list;
	}
	
}