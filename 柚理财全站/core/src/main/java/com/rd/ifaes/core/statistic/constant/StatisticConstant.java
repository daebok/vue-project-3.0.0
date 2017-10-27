package com.rd.ifaes.core.statistic.constant;

/**
 * 统计常量类
 * @version 3.0
 * @author fxl
 * @date 2017年3月23日
 */
public final class StatisticConstant {
	
	/** 二维统计图表：date-时间（横坐标）*/
	public static final String STAT_CHART_DATE = "chart_date";
	/** 二维统计图表：total-总计 */
	public static final String STAT_CHART_TOTAL = "chart_total";
	/** 二维统计图表：ratio-环比值 */
	public static final String STAT_CHART_RATIO = "chart_ratio";
	/** 二维统计图表：amnt-金额 */
	public static final String STAT_CHART_AMNT = "chart_amnt";
	/** 二维统计图表：num-数量 */
	public static final String STAT_CHART_NUM = "chart_num";
	
	/** 时间  */
	public static final String DATE = "date";
	/** 总计  */
	public static final String TOTAL = "total";
	/** 抵押借款  */
	public static final String MORTGAGE = "mortgage";
	/** 担保借款  */
	public static final String VOUCH = "vouch";
	/** 其他  */
	public static final String OTHER = "other";
	/** 环比值  */
	public static final String RATIO = "ratio";
	/** 本金  */
	public static final String CAPITAL = "capital";
	/** 利息  */
	public static final String INTEREST = "interest";
	/** 原列表  */
	public static final String SELECT = "select";
	/** 对比列表  */
	public static final String CONTRAST = "contrast";
	/** 数据列表  */
	public static final String ROWS = "rows";
	/** 结果  */
	public static final String RESULT = "result";
	/** 借款人数  */
	public static final String BORROWER_NUM = "borrowerNum";
	/** 借款金额  */
	public static final String ACCOUNT = "account";
	/** 借款笔数  */
	public static final String BORROW_NUM = "borrowNum";
	
	/** 90天以下 */
	public static final String UNDER_NINETY = "90天以下";
	/** 90天-180天  */
	public static final String UNDER_HUNDRED_EIGHTY = "90天-180天";
	/** 180天以上  */
	public static final String ABOVE_HUNDRED_EIGHTY = "180天以上";
	
	
	public static final String COMPARE_DATE = "compare_date";
	/** 对比时段-选择日期 */
	public static final String COMPARE_DATE_SELECT = "compare_date_select";
	/** 对比时段-数据 */
	public static final String COMPARE_DATA_SELECT = "compare_data_select";
	public static final String COMPARE_KEY_SELECT = "compare_key_select";
	/** 对比时段-对比日期  */	
	public static final String COMPARE_DATE_CONTRAST = "compare_date_contrast";
	/** 对比时段-数据 */
	public static final String COMPARE_DATA_CONTRAST = "compare_data_contrast";
	public static final String COMPARE_KEY_CONTRAST = "compare_key_contrast";
	
	/** 饼状图-单个饼状图 */
	public static final String STAT_PIE_CHART = "pieChart";
	/** 饼状图-金额 */
	public static final String STAT_PIE_AMNT = "pieChart_amnt";
	/** 饼状图-数量 */
	public static final String STAT_PIE_NUM = "pieChart_num";
	/** 饼状图-类型 */
	public static final String STAT_PIE_TYPE = "pieChart_type";
	/** 饼状图-状态 */
	public static final String STAT_PIE_STATUS = "pieChart_status";	
	/** 饼状图-利率 */
	public static final String STAT_PIE_APR = "pieChart_apr";
	/** 饼状图-人数 */
	public static final String STAT_PIE_USER = "pieChart_user";
	
	/** 日期类型：1-日 */
	public static final String DATE_TYPE_DAY = "1";
	/** 日期类型：2-周 */
	public static final String DATE_TYPE_WEEK = "2";
	/** 日期类型：3-月 */
	public static final String DATE_TYPE_MONTH = "3";
	
	/** 统计类型 人数 1 */
	public static final String STAT_TYPE_CUSTOMERS = "1";
	/** 统计类型 金额 2 */
	public static final String STAT_TYPE_AMOUNT = "2";
	/** 统计类型 笔数 3 */
	public static final String STAT_TYPE_NUMBERS = "3";
	
	/** 报表类型：1-按金额 */
	public static final String REPORT_TYPE_AMOUNT = "1";
	/** 报表类型：2-按笔数 */
	public static final String REPORT_TYPE_COUNT = "2";
	
	/** 报表数据类型：productType(产品类型)*/
	public static final String REPORT_DATA_PRODUCT_TYPE = "product";
	/** 报表数据类型：apr(年利率)*/
	public static final String REPORT_DATA_APR = "apr";
	/** 报表数据类型：time(投资期限)*/
	public static final String REPORT_DATA_TIME = "time";
	/** 报表数据类型：amount(投资金额) */
	public static final String REPORT_DATA_AMOUNT = "amount";

	/** 统计条件：状态  */
	public static final String STAT_BY_STATUS = "status";
	/** 统计条件：类型  */
	public static final String STAT_BY_TYPE = "type";
	/** 活跃类型 1 登录  */
	public static final String ACTIVE_TYPE_LOGIN = "1";
	/** 活跃类型 2 投资  */
	public static final String ACTIVE_TYPE_INVEST = "2";
	
	/** 用户属性 1 投资人  */
	public static final String USER_TYPE_INVEST = "1";
	/** 用户属性  2借款人   */
	public static final String USER_TYPE_BORROWER = "2";
	/** 用户属性  3逾期人  */
	public static final String USER_TYPE_OVERDUE = "3";
	
	/**1-变现理财**/
	public static final String PROJECT_TYPE_REALIZE = "realizeFinance";
	public static final String PROJECT_TYPE_REALIZE_NAME = "变现理财";
	/**2-债权转让**/
	public static final String PROJECT_TYPE_BOND = "bondTransfer";
	public static final String PROJECT_TYPE_BOND_NAME = "债权转让";
	
	//统计列表字段 start
	public static final String STAT_LIEST_DATE = "date";
	public static final String STAT_LIEST_DATE_NAME = "日期";
	public static final String STAT_LIEST_TOTAL = "total";
	public static final String STAT_LIEST_TOTAL_NAME = "总计";
	
	/** 报表导出 **/
	public static final String BORROW_EXCEL_HEADER = "日期,抵押借款,担保借款,其他,总计";
	public static final String BORROW_EXCEL_FIELDS = "statDate,mortgageCount,vouchCount,otherCount,totalCount";
	public static final String AREA_EXCEL_HEADER = "地域(省份),借款人数(人),借款金额(元),借款笔数(笔)";
	public static final String AREA_EXCEL_FIELDS = "area,borrowerNum,account,borrowNum";
	/**
	 * 项目期限临最小分组值
	 */
	public static final String PROJECT_TIME_LIMIT_MIN = "1";
 
	private StatisticConstant() {
	}
}
