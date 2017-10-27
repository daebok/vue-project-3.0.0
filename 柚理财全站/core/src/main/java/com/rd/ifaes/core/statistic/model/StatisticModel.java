package com.rd.ifaes.core.statistic.model;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.DictUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.statistic.constant.StatisticConstant;
import com.rd.ifaes.core.sys.domain.DictData;

/**
 * 统计model
 * @version 3.0
 * @author fxl
 * @date 2017年2月21日
 */
@SuppressWarnings("rawtypes")
public class StatisticModel extends BaseEntity{

	private static final long serialVersionUID = 1L;

	/**
	 * 产品类型
	 */
	private String[] projectType;
	
	/**
	 * 项目金额条件：1万元以下 (1)1~5万(2) 5~10万(3)10万元以上(4);
	 */
	private String[] account;
	
	/**
	 * 借款性质
	 */
	private String borrowNature;

	/**
	 * 借款性质
	 */
	private String[] borrowNatureArr;
	
	/**
	 * 借贷类型 1-抵押 2-担保 3-其他
	 */
	private String borrowType;
	
	/**
	 * 借贷类型 1-抵押 2-担保 3-其他
	 */
	private String[] borrowTypeArr;
	
	/**
	 * 日期类型(1-天 2-周 3-月 )
	 */
	private String dateType;
	
	/**
	 * 开始日期
	 */
	private String startDate;
	
	/**
	 * 结束日期
	 */
	private String endDate;
	
	/**
	 * 对比日期
	 */
	private String contrastDate;
	
	/**
	 * 年利率
	 */
	private String[] aprArr;
	
	/**
	 * 投资渠道：PC、APP、微信端
	 */
	private String[] channelArr;
	
	/**
	 * 用户类型
	 */
	private String[] userNatureArr;
	
	/**
	 * 年龄
	 */
	private String[] ageArr;
	
	/**
	 * 通用查询类型
	 */
	private String[] typeArr;
	
	/**
	 * 统计类型：1-人数，2-金额，3-笔数
	 */
	private String statType;
	
	/**
	 * 报表类型：1-按金额  2-按笔数
	 */
	private String reportType;
	
	/**
	 * 还款状态 0未还 1已还
	 */
	private String repayStatus;
	
	/**
	 * 实名状态
	 */
	private String realNameStatus;
	
	/**
	 * 性别
	 */
	private String sex;
	/**
	 * 统计表：date-时间（横坐标），total-总计，ratio-环比值
	 * 状态，有些订单需要判断是否是成功状态
	 */
	private String status;
	
	/**
	 * 年份
	 */
	private Integer year;
	
	/**
	 * 活跃类型 1 登录 2投资
	 */
	private String activeType;
	/**
	 * 用户属性 1 投资人 2借款人 3逾期人
	 */
	private String userType;
	
	/**
	 * 字典列表
	 */
	private List<DictData> dictList;
	
	/**
	 * 如果有多个查询字典数据
	 */
	private List<DictData> dictListTwo;
	
	/**
	 * 金额范围：accountRange
	 */
	private String[] accountRange;
	
	/**
	 * 运营方式  1-红包  2-加息券  3-产品加息
	 */
	private String[] operateArr;
	
	public StatisticModel(){
		
	}
	
	public StatisticModel(String borrowType,String borrowNature,String repayStatus){
		this.borrowType = borrowType;
		this.repayStatus = repayStatus;
		this.borrowNature = borrowNature;
	}
	/**
	 * @return the 借款类型
	 */
	public String[] getProjectType() {
		return projectType;
	}

	/**
	 * @param 借款类型 the projectType to set
	 */
	public void setProjectType(String[] projectType) {
		this.projectType = projectType;
	}

	/**
	 * 获取属性account的值
	 * @return account属性值
	 */
	public String[] getAccount() {
		return account;
	}

	/**
	 * 设置属性account的值
	 * @param  account属性值
	 */
	public void setAccount(String[] account) {
		this.account = account;
	}

	/**
	 * 获取属性borrowNature的值
	 * @return borrowNature属性值
	 */
	public String getBorrowNature() {
		return borrowNature;
	}

	/**
	 * 设置属性borrowNature的值
	 * @param  borrowNature属性值
	 */
	public void setBorrowNature(String borrowNature) {
		this.borrowNature = borrowNature;
	}

	/**
	 * 获取属性borrowNatureArr的值
	 * @return borrowNatureArr属性值
	 */
	public String[] getBorrowNatureArr() {
		return borrowNatureArr;
	}

	/**
	 * 设置属性borrowNatureArr的值
	 * @param  borrowNatureArr属性值
	 */
	public void setBorrowNatureArr(String[] borrowNatureArr) {
		this.borrowNatureArr = borrowNatureArr;
	}

	/**
	 * 获取属性dateType的值
	 * @return dateType属性值
	 */
	public String getDateType() {
		return dateType;
	}

	/**
	 * 设置属性dateType的值
	 * @param  dateType属性值
	 */
	public void setDateType(String dateType) {
		this.dateType = dateType;
	}

	/**
	 * 获取属性startDate的值
	 * @return startDate属性值
	 * @throws ParseException 
	 */
	public String getStartDate() {
		String startDateStr = null;
		if(StringUtils.isNoneBlank(startDate)){
			Date startDateTime = DateUtils.valueOf(startDate, DateUtils.DATEFORMAT_STR_002);
			if(StatisticConstant.DATE_TYPE_MONTH.equals(dateType)){
				startDateStr = DateUtils.dateStr(startDateTime, DateUtils.DATEFORMAT_STR_013);
			}else if(StatisticConstant.DATE_TYPE_WEEK.equals(dateType)){
				startDateStr = DateUtils.getYearWeek(startDateTime);
			}else{
				startDateStr = DateUtils.formatDate(startDateTime, DateUtils.DATEFORMAT_STR_012);
			}
		}
		return startDateStr;
	}
	
	/**
	 * 获取属性borrowType的值
	 * @return borrowType属性值
	 */
	public String getBorrowType() {
		return borrowType;
	}

	/**
	 * 设置属性borrowType的值
	 * @param  borrowType属性值
	 */
	public void setBorrowType(String borrowType) {
		this.borrowType = borrowType;
	}

	/**
	 * 获取属性borrowTypeArr的值
	 * @return borrowTypeArr属性值
	 */
	public String[] getBorrowTypeArr() {
		return borrowTypeArr;
	}

	/**
	 * 设置属性borrowTypeArr的值
	 * @param  borrowTypeArr属性值
	 */
	public void setBorrowTypeArr(String[] borrowTypeArr) {
		this.borrowTypeArr = borrowTypeArr;
	}

	public Date getStartDateTime(){
		Date startDateTime = null;
		if(StringUtils.isNoneBlank(startDate)){
			startDateTime = DateUtils.valueOf(startDate, DateUtils.DATEFORMAT_STR_002);
		}
		return startDateTime;
	}

	/**
	 * 设置属性startDate的值
	 * @param  startDate属性值
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * 获取属性endDate的值
	 * @return endDate属性值
	 * @throws ParseException 
	 */
	public String getEndDate(){
		String endDateStr = null;
		if(StringUtils.isNoneBlank(endDate)){
			Date startDateTime = DateUtils.valueOf(endDate, DateUtils.DATEFORMAT_STR_002);
			if(StatisticConstant.DATE_TYPE_MONTH.equals(dateType)){
				endDateStr = DateUtils.dateStr(startDateTime, DateUtils.DATEFORMAT_STR_013);
			}else if(StatisticConstant.DATE_TYPE_WEEK.equals(dateType)){
				endDateStr = DateUtils.getYearWeek(startDateTime);
			}else{
				endDateStr = DateUtils.formatDate(startDateTime, DateUtils.DATEFORMAT_STR_012);
			}
		}
		return endDateStr;
	}
	
	public Date getEndDateTime(){
		Date endDateTime = null;
		if(StringUtils.isNoneBlank(endDate)){
			endDateTime = DateUtils.valueOf(endDate, DateUtils.DATEFORMAT_STR_002);
		}
		return endDateTime;
	}

	/**
	 * 设置属性endDate的值
	 * @param  endDate属性值
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * 获取属性contrastStartDate的值
	 * @return contrastStartDate属性值
	 */
	public String getContrastStartDate() {
		Date conStartdate = null;
		Date contrastDateTime = DateUtils.valueOf(contrastDate, DateUtils.DATEFORMAT_STR_002);
		Date startDateTime = DateUtils.valueOf(startDate, DateUtils.DATEFORMAT_STR_002);
		Date endDateTime = DateUtils.valueOf(endDate, DateUtils.DATEFORMAT_STR_002);
		//月
		if(StatisticConstant.DATE_TYPE_MONTH.equals(dateType)){
			int month = DateUtils.getMonthDiff(startDateTime, endDateTime);
			conStartdate = DateUtils.rollMon(contrastDateTime, month);
		} else if (StatisticConstant.DATE_TYPE_WEEK.equals(dateType)){
			Date startWeekLastDay = DateUtils.getWeekLastDay(Integer.valueOf(getStartDate()));
			Date endWeekLastDay = DateUtils.getWeekLastDay(Integer.valueOf(getEndDate()));
			int betweenDay = DateUtils.daysBetween(startWeekLastDay, endWeekLastDay);
			
			Date cEndWeekFirstDay = DateUtils.getWeekLastDay(Integer.valueOf(DateUtils.getYearWeek(contrastDateTime)));
			conStartdate = DateUtils.rollDay(cEndWeekFirstDay, -betweenDay);
		} else { //日
			int betweenDay = DateUtils.daysBetween(startDateTime, endDateTime);
			conStartdate = DateUtils.rollDay(contrastDateTime, -betweenDay);
		}
		
		return DateUtils.dateStr2(conStartdate);
		
		
	}

	/**
	 * 获取属性contrastEndDate的值
	 * @return contrastEndDate属性值
	 */
	public String getContrastEndDate() {
		return contrastDate;
	}

	/**
	 * @return the 对比日期
	 */
	public String getContrastDate() {
		return contrastDate;
	}

	/**
	 * @param 对比日期 the contrastDate to set
	 */
	public void setContrastDate(String contrastDate) {
		this.contrastDate = contrastDate;
	}

	/**
	 * @return the 开始日期前一天
	 * @throws ParseException 
	 */
	public String getBeforeStartDate(){
		String startDateStr = null;
		if(StringUtils.isNoneBlank(startDate)){
			Date startDateTime = DateUtils.valueOf(startDate, DateUtils.DATEFORMAT_STR_002);
			if(StatisticConstant.DATE_TYPE_MONTH.equals(dateType)){
				Date beforeDate = DateUtils.rollMon(startDateTime, -1);
				startDateStr = DateUtils.dateStr(beforeDate, DateUtils.DATEFORMAT_STR_013);
			}else if(StatisticConstant.DATE_TYPE_WEEK.equals(dateType)){
				Date beforeDate = DateUtils.rollDay(startDateTime, -7);
				startDateStr = DateUtils.getYearWeek(beforeDate);
			}else{
				Date beforeDate = DateUtils.rollDay(startDateTime, -1);
				startDateStr = DateUtils.formatDate(beforeDate, DateUtils.DATEFORMAT_STR_012);
			}
		}
		return startDateStr;
	}

	/**
	 * @return the 年利率
	 */
	public String[] getAprArr() {
		return aprArr;
	}

	/**
	 * @param 年利率 the aprArr to set
	 */
	public void setAprArr(String[] aprArr) {
		this.aprArr = aprArr;
	}

	/**
	 * @return the 投资渠道：PC、APP、微信端
	 */
	public String[] getChannelArr() {
		return channelArr;
	}

	/**
	 * @param 投资渠道：PC、APP、微信端 the channelArr to set
	 */
	public void setChannelArr(String[] channelArr) {
		this.channelArr = channelArr;
	}

	/**
	 * @return the 用户类型
	 */
	public String[] getUserNatureArr() {
		return userNatureArr;
	}

	/**
	 * @param 用户类型 the userNatureArr to set
	 */
	public void setUserNatureArr(String[] userNatureArr) {
		this.userNatureArr = userNatureArr;
	}

	/**
	 * @return the 统计类型：1-人数，2-金额，3-笔数
	 */
	public String getStatType() {
		return statType;
	}

	/**
	 * @param 统计类型：1-人数，2-金额，3-笔数 the statType to set
	 */
	public void setStatType(String statType) {
		this.statType = statType;
	}

	/**
	 * @return the 报表类型：1-按投资金额2-投资笔数
	 */
	public String getReportType() {
		return reportType;
	}

	/**
	 * @param 报表类型：1-按投资金额2-投资笔数 the reportType to set
	 */
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	/**
	 * 获取属性repayStatus的值
	 * @return repayStatus属性值
	 */
	public String getRepayStatus() {
		return repayStatus;
	}

	/**
	 * 设置属性repayStatus的值
	 * @param  repayStatus属性值
	 */
	public void setRepayStatus(String repayStatus) {
		this.repayStatus = repayStatus;
	}

	/**
	 * 获取属性realNameStatus的值
	 * @return realNameStatus属性值
	 */
	public String getRealNameStatus() {
		return realNameStatus;
	}

	/**
	 * 设置属性realNameStatus的值
	 * @param  realNameStatus属性值
	 */
	public void setRealNameStatus(String realNameStatus) {
		this.realNameStatus = realNameStatus;
	}

	/**
	 * 获取属性ageArr的值
	 * @return ageArr属性值
	 */
	public String[] getAgeArr() {
		return ageArr;
	}

	/**
	 * 设置属性ageArr的值
	 * @param  ageArr属性值
	 */
	public void setAgeArr(String[] ageArr) {
		this.ageArr = ageArr;
	}

	/**
	 * 获取属性sex的值
	 * @return sex属性值
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * 设置属性sex的值
	 * @param  sex属性值
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	/**
	 * 校验查询时间
	 */
	public void checkQueryDate() {
		if(StringUtils.isBlank(this.getStartDate())||StringUtils.isBlank(this.getEndDate())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.STAT_QUERY_DATE_IS_EMPTY));
		}
		if(DateUtils.compareDate(this.getStartDateTime(), this.getEndDateTime())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.STAT_QUERY_DATE_IS_ERROR));
		}
	}
	
	public void checkCompareDate(){
		if(StringUtils.isBlank(this.getContrastDate())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.STAT_COMPARE_DATE_IS_EMPTY));
		}
	}

	/**
	 * @return the 状态，有些订单需要判断是否是成功状态
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param 状态，有些订单需要判断是否是成功状态 the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	public String[] getTypeArr() {
		return typeArr;
	}

	public void setTypeArr(String[] typeArr) {
		this.typeArr = typeArr;
	}

	/**
	 * 获取属性year的值
	 * @return year属性值
	 */
	public Integer getYear() {
		return year;
	}

	/**
	 * 设置属性year的值
	 * @param  year属性值
	 */
	public void setYear(Integer year) {
		this.year = year;
	}

	/**
	 * 获取属性activeType的值
	 * @return activeType属性值
	 */
	public String getActiveType() {
		return activeType;
	}

	/**
	 * 设置属性activeType的值
	 * @param  activeType属性值
	 */
	public void setActiveType(String activeType) {
		this.activeType = activeType;
	}
	
	/**
	 * @return the 运营方式1-红包2-加息券3-产品加息
	 */
	public String[] getOperateArr() {
		return operateArr;
	}

	/**
	 * @param 运营方式1-红包2-加息券3-产品加息 the operateArr to set
	 */
	public void setOperateArr(String[] operateArr) {
		this.operateArr = operateArr;
	}

	public String getCompareDateKey(){
		String firstDay="", endDay="";
		if(StatisticConstant.DATE_TYPE_MONTH.equals(dateType)){
			firstDay = DateUtils.getFirstDateOfMonth(
					DateUtils.valueOf(startDate, DateUtils.DATEFORMAT_STR_002), DateUtils.DATEFORMAT_STR_022);
			endDay = DateUtils.getLastDateOfMonth(
					DateUtils.valueOf(endDate, DateUtils.DATEFORMAT_STR_002), DateUtils.DATEFORMAT_STR_022);
		}else if(StatisticConstant.DATE_TYPE_WEEK.equals(dateType)){
			firstDay = DateUtils.dateStr6(DateUtils.getWeekFirstDay(Integer.valueOf(getStartDate())));
			endDay = DateUtils.dateStr6(DateUtils.getWeekLastDay(Integer.valueOf(getEndDate())));
		}else{
			firstDay = DateUtils.dateStr6(DateUtils.valueOf(getStartDate(), DateUtils.DATEFORMAT_STR_012));
			endDay = DateUtils.dateStr6(DateUtils.valueOf(getEndDate(), DateUtils.DATEFORMAT_STR_012));
		}
		return firstDay+"-"+endDay;
	}

	/**
	 * 获取属性userType的值
	 * @return userType属性值
	 */
	public String getUserType() {
		return userType;
	}

	/**
	 * 设置属性userType的值
	 * @param  userType属性值
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}

	/**
	 * 获取属性dictList的值
	 * @return dictList属性值
	 */
	public List<DictData> getDictList() {
		return dictList;
	}

	/**
	 * 设置属性dictList的值
	 * @param  dictList属性值
	 */
	public void setDictList(List<DictData> dictList) {
		this.dictList = dictList;
	}
	
	/**
	 * @return the 金额范围：accountRange
	 */
	public String[] getAccountRange() {
		return accountRange;
	}

	/**
	 * @param 金额范围：accountRange the 金额范围：accountRange to set
	 */
	public void setAccountRange(String[] accountRange) {
		this.accountRange = accountRange;
	}

	/**
	 * 字典表达式转化
	 * @author fxl
	 * @date 2017年3月20日
	 * @param dictType
	 */
	public void fillDictList(String dictType){
		List<DictData> list = DictUtils.list(dictType);
		for (DictData dictData : list) {
			if(dictData.getExpression() != null){
				String exp[] = dictData.getExpression().split(",");
				dictData.setMinValue(exp[0]);
				if(exp.length > 1){
					dictData.setMaxValue(exp[1]);
				}
			}
		}
		this.setDictList(list);
	}

	/**
	 * 设置属性dictList的值
	 * @return the 如果有多个查询字典数据
	 */
	public List<DictData> getDictListTwo() {
		return dictListTwo;
	}

	/**
	 * 字典表达式转化
	 */
	public void setDictListTwo(List<DictData> dictListTwo) {
		this.dictListTwo = dictListTwo;
	}
	
	/**
	 * 字典表达式转化
	 * @author xhf
	 * @date 2017年3月20日
	 * @param dictType
	 */
	public void fillDictListTwo(String dictType){
		List<DictData> list = DictUtils.list(dictType);
		for (DictData dictData : list) {
			if(dictData.getExpression() != null){
				String exp[] = dictData.getExpression().split(",");
				dictData.setMinValue(exp[0]);
				if(exp.length > 1){
					dictData.setMaxValue(exp[1]);
				}
			}
		}
		this.setDictListTwo(list);
	}
	
}
