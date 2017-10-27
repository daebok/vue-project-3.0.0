package com.rd.ifaes.core.operate.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.dict.OperateEnum;
import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;

/**
 * entity:UserVipGrowthLog
 * 
 * @author 
 * @version 3.0
 * @date 2016-8-2
 */
public class UserVipGrowthLog extends BaseEntity<UserVipGrowthLog> {
	/**序列号*/
	private static final long serialVersionUID = 62535354136752526L;
	/**用户id*/
	private String	userId;		
	/**原来成长值*/
	private BigDecimal	growthValue;		
	/**操作类型（0减少，1增加）*/
	private String	optType;		
	/**操作后的成长值*/
	private BigDecimal	growthValueNew;		
	/**本次操作成长值 */
	private BigDecimal	optValue;		
	/**操作备注 */
	private String	remark;		
	/**操作时间 */
	private Date	createTime;		

	//其他自定义属性
	/**用户账号 */
	private String userName;
	/**手机号码 */
	private String mobile; 
	/**真实姓名 */
	private String realName;

	// Constructor
	/**Constructor */
	public UserVipGrowthLog() {
		super();
	}
	/**构造成长日志*/
	public UserVipGrowthLog(final BigDecimal oldGrowthValue,final BigDecimal optValue,final String remark,
			final String optType,final String userId){
		super();
		this.createTime = DateUtils.getNow();
		this.userId = userId;
		this.growthValue = oldGrowthValue;
		if(OperateEnum.VIP_GROWTH_OPT_ADD.getValue().equalsIgnoreCase(optType)){
			this.growthValueNew = BigDecimalUtils.add(oldGrowthValue, optValue);
		}else{
			this.growthValueNew = BigDecimalUtils.sub(oldGrowthValue, optValue);
		} 
		this.remark =remark;
		this.optValue =optValue;
		this.optType = optType;
	}
	/** full Constructor*/
	public UserVipGrowthLog(final String uuid,final  String userId,final BigDecimal growthValue, final String optType,
			final BigDecimal growthValueNew,final  BigDecimal optValue, final String remark,final  Date createTime) {
		super();
		setUuid(uuid);
		this.userId = userId;
		this.growthValue = growthValue;
		this.optType = optType;
		this.growthValueNew = growthValueNew;
		this.optValue = optValue;
		this.remark = remark;
		this.createTime = createTime;
	}
	/** 获取用户id*/
	public String getUserId() {
		return userId;
	}
	/** 设置用户id*/
	public void setUserId(final String userId) {
		this.userId = userId;
	}
	/** 获取用户成长值*/
	public BigDecimal getGrowthValue() {
		return growthValue;
	}
	/** 设置用户成长值*/
	public void setGrowthValue(final BigDecimal growthValue) {
		this.growthValue = growthValue;
	}
	/** 获取操作类型*/
	public String getOptType() {
		return optType;
	}
	/** 设置操作类型*/
	public void setOptType(final String optType) {
		this.optType = optType;
	}
	/** 获取新的成长值*/
	public BigDecimal getGrowthValueNew() {
		return growthValueNew;
	}
	/** 设置新的成长值*/
	public void setGrowthValueNew(final BigDecimal growthValueNew) {
		this.growthValueNew = growthValueNew;
	}
	/** 获取操作值*/
	public BigDecimal getOptValue() {
		return optValue;
	}
	/** 设置操作值*/
	public void setOptValue(final BigDecimal optValue) {
		this.optValue = optValue;
	}
	/** 获取备注*/
	public String getRemark() {
		return remark;
	}
	/** 设置备注*/
	public void setRemark(final String remark) {
		this.remark = remark;
	}
	/** 获取创建日期*/
	public Date getCreateTime() {
		return createTime;
	}
	/** 设置创建日期*/
	public void setCreateTime(final Date createTime) {
		this.createTime = createTime;
	}
	/** 获取用户名*/
	public String getUserName() {
		return userName;
	}
	/** 设置用户名*/
	public void setUserName(final String userName) {
		this.userName = userName;
	}
	/** 获取手机号*/
	public String getMobile() {
		return mobile;
	}
	/** 设置手机号*/
	public void setMobile(final String mobile) {
		this.mobile = mobile;
	}
	/** 获取真实姓名*/
	public String getRealName() {
		return realName;
	}
	/** 设置真实姓名*/
	public void setRealName(final String realName) {
		this.realName = realName;
	}
	/** toString*/
	@Override
	public String toString() {
		return "UserVipGrowthLog [" + "uuid=" + uuid + ", userId=" + userId + ", growthValue=" + growthValue + ", optType=" + optType + ", growthValueNew=" + growthValueNew + ", optValue=" + optValue + ", remark=" + remark + ", createTime=" + createTime +  "]";
	}
}
