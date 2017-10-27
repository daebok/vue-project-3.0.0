package com.rd.ifaes.core.score.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.dict.OperateEnum;
import com.rd.ifaes.common.dict.ScoreEnum;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.bond.domain.Bond;
import com.rd.ifaes.core.bond.domain.BondInvest;
import com.rd.ifaes.core.bond.service.BondInvestService;
import com.rd.ifaes.core.bond.service.BondService;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.score.domain.ScoreType;
import com.rd.ifaes.core.score.domain.UserScore;
import com.rd.ifaes.core.score.domain.UserScoreLog;
import com.rd.ifaes.core.score.mapper.UserScoreMapper;
import com.rd.ifaes.core.score.service.ScoreTypeService;
import com.rd.ifaes.core.score.service.UserScoreLogService;
import com.rd.ifaes.core.score.service.UserScoreService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.service.UserService;

/**
 * ServiceImpl:UserScoreServiceImpl
 * @author wyw
 * @version 3.0
 * @date 2016-8-4
 */
@Service("userScoreService") 
public class UserScoreServiceImpl  extends BaseServiceImpl<UserScoreMapper, UserScore> implements UserScoreService{
    /**
     * 日志类
     */
	private static final Logger LOGGER = LoggerFactory.getLogger(UserScoreServiceImpl.class);
	@Resource
    private transient BondService bondService;
	@Resource
    private transient BondInvestService bondInvestService;
	/** 积分类型业务处理 */
	@Resource
    private transient ScoreTypeService scoreTypeService;
	/** 积分日志业务处理 */
    @Resource
    private transient UserScoreLogService userScoreLogService;
    @Resource
    private transient ProjectInvestService projectInvestService;
    @Resource
    private transient ProjectService projectService;
    @Resource
    private transient UserService userService; 
    
    /** 发放积分*/
	@Override
	public int grantScore(final User user, final String typeCode) {
		return grantScore(user, typeCode, null);
	}
	/**
	 * 发放积分
	 */
	@Override
	public int grantScore(final User user, final String typeCode, final String uuid) {
		// 获取 scoreType
	    final ScoreType scoreType = scoreTypeService.getScoreTypeByCode(typeCode);
	    
		// 根据 scoreType 的发放类型 发放积分
	    LOGGER.info("发放积分的类型：{},用户Id:{},投资ID：{}",typeCode,user==null?StringUtils.EMPTY:user.getUuid(),uuid);
	    int result = grantScoreByType(scoreType, user, uuid);
	    return result;
	}
	/**
	 * 发放积分：其他发放积分渠道，需要传递操作积分值，以及操作备注
	 */
	@Override
	public int grantScore(final User user, final String scoreType, final String scoreTypeName, final int optValue, final String remark) {
		if(optValue>0){
			//更新 userScore的值
			final UserScore userScore = updateUserScore(optValue, 0, 0, user.getUuid());
			//记录积分日志
			final UserScoreLog userScoreLog = new UserScoreLog();
			userScoreLog.setUserId(user.getUuid());
			userScoreLog.setTotalScore(userScore.getTotalScore());
			userScoreLog.setUseScore(userScore.getUseScore());
			userScoreLog.setOptValue(optValue);
			userScoreLog.setOptType(OperateEnum.SCORE_OPT_ADD.getValue());
			userScoreLog.setNoUseScore(userScore.getNoUseScore());
			userScoreLog.setTypeName(scoreType);
			userScoreLog.setTypeCode(scoreTypeName);
			if(remark == null || StringUtils.isBlank(remark)) {
				userScoreLog.setRemark("发放"+optValue+"积分");
			}else {
				userScoreLog.setRemark(remark+",发放"+optValue+"积分");
			}
			userScoreLog.setCreateTime(DateUtils.getNow());
			userScoreLogService.insert(userScoreLog);
		}	
		return optValue;
	}
	/**
	 * 根据用户id获取用户积分
	 */
	@Override
	public UserScore getUserScore(final String userId) {
		return dao.getUserScore(userId);
	}
	/**
	 * 更新用户积分值,并返回最新的值
	 */
	@Override
	public UserScore updateUserScore(int useScore, int noUseScore, int expenseScore,String userId) {
		LOGGER.info("实际发放start----更新用户积分值,并返回最新的值");
		dao.updateUserScore(useScore, noUseScore, expenseScore, userId);
		return this.getUserScore(userId);
	}
	/**
	 * 
	 * 初始化userScore 
	 * @author wyw
	 * @date 2016-8-4
	 * @param userId
	 * @return
	 */
	public   void  initUserScore(final String userId){
		UserScore userScore = new UserScore();
		userScore.setUserId(userId);
		userScore.setTotalScore(0);
		userScore.setUseScore(0);
		userScore.setNoUseScore(0);
		userScore.setExpenseScore(0);
		userScore.setHandScore(0);
		userScore.setCreateTime(DateUtils.getNow());
		this.insert(userScore);
	}
	/**
	 * 更新用户积分值,并返回最新的值
	 */
	private  int grantScoreByType(final ScoreType scoreType,final  User user, final String uuid) {
		final int  grantScore = getGrantScore(scoreType, uuid,user);//发放的积分值
		LOGGER.info("发放积分的值：{}",grantScore);
		if(grantScore>0){
			LOGGER.info("实际发放start");
			//更新 userScore的值
			final UserScore userScore = updateUserScore(grantScore, 0, 0, user.getUuid());
			if(userScore!=null){
				LOGGER.info("实际发放start,score:{}",userScore.getTotalScore());
				//记录积分日志
				final UserScoreLog userScoreLog = new UserScoreLog();
				userScoreLog.setUuid(uuid);
				userScoreLog.setUserId(user.getUuid());
				userScoreLog.setTotalScore(userScore.getTotalScore());
				userScoreLog.setUseScore(userScore.getUseScore());
				userScoreLog.setOptValue(grantScore);
				userScoreLog.setOptType(OperateEnum.SCORE_OPT_ADD.getValue());
				userScoreLog.setNoUseScore(userScore.getNoUseScore());
				userScoreLog.setTypeName(scoreType.getTypeName());
				userScoreLog.setTypeCode(scoreType.getTypeCode());
				userScoreLog.setRemark(scoreType.getTypeName()+",获得"+grantScore+"积分");
				userScoreLog.setCreateTime(DateUtils.getNow());
				userScoreLogService.insert(userScoreLog);				
			}
			LOGGER.info("实际发放end");
		}	
		return grantScore;
	}
	/**
	 * 
	 * 获取发放的积分值
	 * @author wyw
	 * @date 2016-8-6
	 * @param scoreType
	 * @param projectInvest
	 * @return
	 */
	private int getGrantScore(final ScoreType scoreType, final String uuid,final User user){
		BondInvest bondInvest = null;
		ProjectInvest projectInvest = null;
		LOGGER.info("获取积分的值--类型：{}",scoreType.getTypeCode());
		if(ScoreEnum.SCORE_BOND_INVEST_SUCCESS.eq(scoreType.getTypeCode())){
			bondInvest = bondInvestService.get(uuid);
		}else{
			projectInvest = projectInvestService.get(uuid);
		}
		//检查是否已经发放	
		if(checkGrant(scoreType, uuid, user)){
			return 0;
		}
		LOGGER.info("获取积分的值2--类型：{},发放形式(1:固定金额,2:固定比例,5:固定值增长)：{}",scoreType.getTypeCode(),scoreType.getGrantType());
		int  grantScore =0;
		if(OperateEnum.GRANT_TYPE_FIXED_AMOUNT.getValue().equalsIgnoreCase(scoreType.getGrantType())){//固定值发放
			grantScore = scoreType.getGrantValue();	
		}else if(OperateEnum.GRANT_TYPE_FIXEDL_UP.getValue().equalsIgnoreCase(scoreType.getGrantType())){//固定值增长
			final Date dataNow = new Date();
			List<UserScoreLog> list = null;
			//今天是否已经发放积分
			final UserScoreLog queryScore = new UserScoreLog();
			queryScore.setStartTime(DateUtils.getDateStart(dataNow));
			queryScore.setEndTime(DateUtils.getDateEnd(dataNow));
			queryScore.setTypeCode(scoreType.getTypeCode());
			final Page<UserScoreLog> logList = userScoreLogService.findByDate(queryScore, user);
			if(logList!=null){
				list=logList.getRows();				
			}
			if(CollectionUtils.isEmpty(list)){//当天没有发放积分
				//昨天是否获取过积分
				final Date dataYesDay = DateUtils.rollDay(dataNow, -1);
				queryScore.setStartTime(DateUtils.getDateStart(dataYesDay));
				queryScore.setEndTime(DateUtils.getDateEnd(dataYesDay));
				list=userScoreLogService.findByDate(queryScore, user).getRows();
				if(CollectionUtils.isEmpty(list)){//昨天没有获取积分值，取规则中的最小值
					grantScore = scoreType.getGrantMin();
				}else{//取出昨天获取的积分值，加上规则中的递增值
					grantScore = scoreType.getGrantUp()+list.get(0).getOptValue();
					if(grantScore>scoreType.getGrantMax()){	//判断最大值  不能超过配置的最大值
						grantScore = scoreType.getGrantMax();
					}
				}	
			}
		}else if(projectInvest!=null&& OperateEnum.GRANT_TYPE_FIXED_RATE.getValue().equalsIgnoreCase(scoreType.getGrantType())){//固定比例值
			//年化金额 = (投资金额*投资期限/365) 
			//积分 = 年化金额 * 比例（0.01）
			Project project = projectService.get(projectInvest.getProjectId());
			int period = Constant.INT_ONE;
			if(LoanEnum.TIME_TYPE_DAY.getValue().equals(project.getTimeType())) {  // 天标
				period = project.getTimeLimit();
			}else if(LoanEnum.TIME_TYPE_MONTH.getValue().equals(project.getTimeType())){// 月标
				period = project.getTimeLimit() * ConfigUtils.getInt(ConfigConstant.DAYS_OF_MONTH);
			}
			BigDecimal yearAprMoney = BigDecimalUtils.div(BigDecimalUtils.mul(true, projectInvest.getMoney(), BigDecimal.valueOf(period)), new BigDecimal(365), 20);//年化金额
			grantScore = BigDecimalUtils.mul(yearAprMoney, scoreType.getGrantRate()).intValue();
			LOGGER.info("投资成功发放投资积分，年化金额：{}，发放积分：{}", yearAprMoney, grantScore);
		}else if(bondInvest != null && OperateEnum.GRANT_TYPE_FIXED_RATE.getValue().equalsIgnoreCase(scoreType.getGrantType())){
			//年化金额 = (投资金额*投资期限/365) 
			//积分 = 年化金额 * 比例（0.01）
			final Bond bond = bondService.get(bondInvest.getBondId());
			int period = bond.getRemainDays();
			BigDecimal yearAprMoney = BigDecimalUtils.div(BigDecimalUtils.mul(true, bondInvest.getAmount(), BigDecimal.valueOf(period)), new BigDecimal(365), 20);//年化金额
			grantScore = BigDecimalUtils.mul(yearAprMoney, scoreType.getGrantRate()).intValue();
			LOGGER.info("债权投资--固定比例发放--债权投资ID：{},债权ID：{},投资金额：{},比例值：{},期限：{},年化金额：{},发放积分：{}",
					uuid,bond.getUuid(),bondInvest.getAmount(),scoreType.getGrantRate(),period, yearAprMoney,grantScore);
		}
		return grantScore;
	}
	/**
	 * 检查是否发放
	 */
	private boolean checkGrant(final ScoreType scoreType,final String uuid ,final User user){
		boolean grant =false;
		if(OperateEnum.GRANT_TYPE_FIXEDL_UP.getValue().equalsIgnoreCase(scoreType.getGrantType())){
			grant = false;
		}else{
			final UserScoreLog query = new UserScoreLog();
			query.setUserId(user.getUuid());
			query.setTypeCode(scoreType.getTypeCode());
			query.setUuid(uuid);
			grant = userScoreLogService.getCount(query)>0;
			
		}
		return grant;
	}
	
	@Override
	public int getListCount(String keywords) {
		return userService.getCountByUserName(keywords);
	}
	
}