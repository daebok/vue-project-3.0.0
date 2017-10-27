package com.rd.ifaes.core.score.domain;

import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * entity:UserScore
 * 
 * @author 
 * @version 3.0
 * @date 2016-8-4
 */
public class UserScore extends BaseEntity<UserScore> {
	/** 序列号*/
	private static final long serialVersionUID = 5232768912775121014L;
	/**  用户id*/
	private String	userId;	
	/**  总积分*/
	private Integer	totalScore;		 
	/**  可用积分*/
	private Integer	useScore;		
	/**  冻结积分*/
	private Integer	noUseScore;		
	/**  消费积分*/
	private Integer	expenseScore;		
	/**  创建日期 */
	private Date	createTime;	
	/**  手动发放积分*/
	private Integer handScore ;
	
	//其他自定义属性
	/**  用户账号 */
	private String userName; 
	/**  手机号码*/
	private String mobile;
	/**  真实姓名 */
	private String realName;

	/**  Constructor */
	public UserScore() {
		super();
	}

	/**
	 * full Constructor
	 */
	public UserScore(final String uuid,final  String userId,final  Integer totalScore, final Integer useScore, final Integer noUseScore,final  Integer expenseScore, final Date createTime) {
		super();
		setUuid(uuid);
		this.userId = userId;
		this.totalScore = totalScore;
		this.useScore = useScore;
		this.noUseScore = noUseScore;
		this.expenseScore = expenseScore;
		this.createTime = createTime;
	}
	/**  获取用户id */
	public String getUserId() {
		return userId;
	}
	/**  设置用户id */
	public void setUserId(final String userId) {
		this.userId = userId;
	}
	/**  获取用户积分总额 */
	public Integer getTotalScore() {
		return totalScore;
	}
	/**  设置用户积分总额 */
	public void setTotalScore(final Integer totalScore) {
		this.totalScore = totalScore;
	}
	/**  获取用户可用积分 */
	public Integer getUseScore() {
		return useScore;
	}
	/**  设置用户可用积分 */
	public void setUseScore(final Integer useScore) {
		this.useScore = useScore;
	}
	/**  获取用户冻结积分 */
	public Integer getNoUseScore() {
		return noUseScore;
	}
	/**  设置用户冻结积分 */
	public void setNoUseScore(final Integer noUseScore) {
		this.noUseScore = noUseScore;
	}
	/**  获取用户消费积分 */
	public Integer getExpenseScore() {
		return expenseScore;
	}
	/**  设置用户消费积分 */
	public void setExpenseScore(final Integer expenseScore) {
		this.expenseScore = expenseScore;
	}
	/**  获取创建日期 */
	public Date getCreateTime() {
		return createTime;
	}
	/**  设置创建日期 */
	public void setCreateTime(final Date createTime) {
		this.createTime = createTime;
	}
	
	public Integer getHandScore() {
		return handScore;
	}

	public void setHandScore(Integer handScore) {
		this.handScore = handScore;
	}

	/**  获取用户名 */
	public String getUserName() {
		return userName;
	}
	/**  设置用户名 */
	public void setUserName(final String userName) {
		this.userName = userName;
	}
	/**  获取手机号 */
	public String getMobile() {
		return mobile;
	}
	/**  设置手机号 */
	public void setMobile(final String mobile) {
		this.mobile = mobile;
	}
	/**  获取真实姓名 */
	public String getRealName() {
		return realName;
	}
	/**  设置真实姓名 */
	public void setRealName(final String realName) {
		this.realName = realName;
	}
	/**  toString */
	@Override
	public String toString() {
		return "UserScore [" + "uuid=" + uuid + ", userId=" + userId + ", totalScore=" + totalScore + ", useScore=" + useScore + ", noUseScore=" + noUseScore + ", expenseScore=" + expenseScore + ", createTime=" + createTime +  "]";
	}
}
