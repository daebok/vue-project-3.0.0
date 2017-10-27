package com.rd.ifaes.core.user.domain;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.core.core.util.DictUtils;

/**
 * entity:UserBaseInfo
 * 
 * @author xhf
 * @version 3.0
 * @date 2016-6-15
 */
public class UserBaseInfo extends BaseEntity<UserBaseInfo> {
	
	private static final long serialVersionUID = 1L;
	/** 用户ID */
	private String	userId;		
	/** 出生年月日 */
	private Date	birthday;	
	/** 学历 1：小学；2：初中；3：高中；4：中专；5：大专；6：本科；7：硕士；8：博士；9其他 */
	private String	education;		 
	/** 婚姻状况 0:未婚；1：已婚；2：离异；3：丧偶 */ 
	private String	maritalStatus;	
	/** 工作年限 1：0-3年；2：3-5年；3:5-8年；4:8年以上； */ 
	private String	workExperience;		
	/** 月收入范围 */ 
	private String	monthIncomeRange;	
	/** 车产有无 */ 
	private String	carStatus;	
	/** 房产有无 */
	private String	houseStatus;
	/* 职业 */
	private String profession;
	
	/* 其他自定义属性 */
	private String professionStr;

	/**
	 *  构造函数
	 */
	public UserBaseInfo() {
		super();
	}

	/**
	 *  构造函数
	 */
	public UserBaseInfo(final String userId, final Date birthday, final String education,
			final String maritalStatus, final String workExperience,final String monthIncomeRange,
			final String carStatus, final String houseStatus) {
		super();
		this.userId = userId;
		this.birthday = birthday;
		this.education = education;
		this.maritalStatus = maritalStatus;
		this.workExperience = workExperience;
		this.monthIncomeRange = monthIncomeRange;
		this.carStatus = carStatus;
		this.houseStatus = houseStatus;
	}
	
	/**
	 * 获取用户ID
	 * @return userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置用户ID
	 * @param  userId
	 */
	public void setUserId(final String userId) {
		this.userId = userId;
	}

	/**
	 * 获取出生年月日
	 * @return birthday
	 */
	public Date getBirthday() {
		return birthday;
	}

	/**
	 * 设置出生年月日
	 * @param  birthday
	 */
	public void setBirthday(final Date birthday) {
		this.birthday = birthday;
	}

	/**
	 * 获取学历1：小学；2：初中；3：高中；4：中专；5：大专；6：本科；7：硕士；8：博士；9其他
	 * @return education
	 */
	public String getEducation() {
		return education;
	}

	/**
	 * 设置学历1：小学；2：初中；3：高中；4：中专；5：大专；6：本科；7：硕士；8：博士；9其他
	 * @param  education
	 */
	public void setEducation(final String education) {
		this.education = education;
	}

	/**
	 * 获取婚姻状况0:未婚；1：已婚；2：离异；3：丧偶
	 * @return maritalStatus
	 */
	public String getMaritalStatus() {
		return maritalStatus;
	}

	/**
	 * 设置婚姻状况0:未婚；1：已婚；2：离异；3：丧偶
	 * @param  maritalStatus
	 */
	public void setMaritalStatus(final String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	/**
	 * 获取工作年限1：0-3年；2：3-5年；3:5-8年；4:8年以上；
	 * @return workExperience
	 */
	public String getWorkExperience() {
		return workExperience;
	}

	/**
	 * 设置工作年限1：0-3年；2：3-5年；3:5-8年；4:8年以上；
	 * @param  workExperience
	 */
	public void setWorkExperience(final String workExperience) {
		this.workExperience = workExperience;
	}

	/**
	 * 获取月收入范围
	 * @return monthIncomeRange
	 */
	public String getMonthIncomeRange() {
		return monthIncomeRange;
	}

	/**
	 * 设置月收入范围
	 * @param  monthIncomeRange
	 */
	public void setMonthIncomeRange(final String monthIncomeRange) {
		this.monthIncomeRange = monthIncomeRange;
	}

	/**
	 * 获取车产有无
	 * @return carStatus
	 */
	public String getCarStatus() {
		return carStatus;
	}

	/**
	 * 设置车产有无
	 * @param  carStatus
	 */
	public void setCarStatus(final String carStatus) {
		this.carStatus = carStatus;
	}

	/**
	 * 获取房产有无
	 * @return houseStatus
	 */
	public String getHouseStatus() {
		return houseStatus;
	}

	/**
	 * 设置房产有无
	 * @param  houseStatus
	 */
	public void setHouseStatus(final String houseStatus) {
		this.houseStatus = houseStatus;
	}

	/**
	 * 获得"学历"显示值
	 * @return
	 */
	public String getEducationStr() {
		String educationStr = "";
		if(StringUtils.isNotBlank(getEducation())){
			educationStr = DictUtils.getItemName(DictTypeEnum.EDUCATION_LEVEL.getValue(), getEducation());
		}
		return educationStr;
	}
	
	/**
	 * 获得"婚姻"显示值
	 * @return
	 */
	public String getMaritalStatusStr() {
		String maritalStatusStr = "";
		if(StringUtils.isNotBlank(getMaritalStatus())){
			maritalStatusStr = DictUtils.getItemName(DictTypeEnum.MARITAL_STATUS.getValue(), getMaritalStatus());
		}
		return maritalStatusStr;
	}

	/**
	 * 获得"工作经历"显示值
	 * @return
	 */
	public String getWorkExperienceStr() {
		String workExperienceStr = "";
		if(StringUtils.isNotBlank(getWorkExperience())){
			workExperienceStr = DictUtils.getItemName(DictTypeEnum.WORK_EXPERIENCE.getValue(), getWorkExperience());
		}
		return workExperienceStr;
	}
	
	/**
	 * 获得"月收入"显示值
	 * @return
	 */
	public String getMonthIncomeRangeStr() {
		String monthIncomeRangeStr = "";
		if(StringUtils.isNotBlank(getMonthIncomeRange())){
			monthIncomeRangeStr = DictUtils.getItemName(DictTypeEnum.SALARY_RANGE.getValue(), getMonthIncomeRange());
		}
		return monthIncomeRangeStr;
	}
	
	/**
	 * 获得"是否有车"显示值
	 * @return
	 */
	public String getCarStatusStr() {
		String carStatusStr = "";
		if(StringUtils.isNotBlank(getCarStatus())){
			carStatusStr = DictUtils.getItemName(DictTypeEnum.CAR_STATUS.getValue(), getCarStatus());
		}
		return carStatusStr;
	}
	
	/**
	 * 获得"是否有房"显示值
	 * @return
	 */
	public String getHouseStatusStr() {
		String houseStatusStr = "";
		if(StringUtils.isNotBlank(getHouseStatus())){
			houseStatusStr = DictUtils.getItemName(DictTypeEnum.HOUSES_STATUS.getValue(), getHouseStatus());
		}
		return houseStatusStr;
	}

	/**
	 * 重写toString()方法
	 */
	@Override
	public String toString() {
		return "UserBaseInfo [" + "uuid=" + uuid + ", userId=" + userId + ", birthday=" + birthday + ", education=" + education + ", maritalStatus=" + maritalStatus + ", workExperience=" + workExperience + ", monthIncomeRange=" + monthIncomeRange + ", carStatus=" + carStatus + ", houseStatus=" + houseStatus +  "]";
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getProfessionStr() {
		return profession;
	}

	public void setProfessionStr(String professionStr) {
		this.professionStr = professionStr;
	}
	
	
}
