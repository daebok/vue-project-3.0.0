package com.rd.ifaes.core.operate.domain;

import java.math.BigDecimal;
import java.util.Date;



import org.hibernate.validator.constraints.Length;

import com.rd.ifaes.common.dict.DeleteFlagEnum;
import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;

/**
 * 
 *  useGift POJO
 * @version 3.0
 * @author wyw
 * @date 2016-8-2
 */
public class UserGift extends BaseEntity<UserGift> {
	/**序列号 */
	private static final long serialVersionUID = -882830078290712823L;
	/**礼包名称 */
	@Length(min=2,max=15,message="{"+ResourceConstant.GIFT_NAME_ERROR+"}")
	private String	giftName;	
	/**礼包类型 （0生日礼包 1专享礼包） */
	private String	giftType;		 
	/** 红包发放规则id */
	private String	redenvelopeRuleId;		  
	/** 加息券发放规则id */
	private String	rateCouponRuleId;		
	/** 礼包详情 */
	private String	remark;		 
	/** 添加日期 */
	private Date	createTime;		
	/** 最后修改日期  */
	private Date	updateTime;		 
	/** 逻辑删除标识 0正常  1删除  */
	private String	deleteFlag;		

	//其他自定义属性
	/**  加息券规则名称  */
	private String rateName;  
	/**  红包规则名称  */
	private String redName; 
	/**  红包规则  */
	private RedenvelopeRule redRule;
	/**  加息券规则*/
	private RateCouponRule rateRule;
	/**  红包金额  */
	private BigDecimal redAmount;
	/**  加息券范围  */
	private String rateRange;
	
	
	/**  Constructor*/
	public UserGift() {
		super();
	}
	/** full Constructor*/
	public UserGift(final String uuid,final  String giftName, final String giftType, 
			final String redenvelopeRuleId, final String rateCouponRuleId,final  String remark,final  Date createTime,
			final Date updateTime, final String deleteFlag) {
		super();
		setUuid(uuid);
		this.giftName = giftName;
		this.giftType = giftType;
		this.redenvelopeRuleId = redenvelopeRuleId;
		this.rateCouponRuleId = rateCouponRuleId;
		this.remark = remark;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.deleteFlag = deleteFlag;
	}
	@Override
	/** 持久化前执行的操作*/
	public void preInsert() {
		super.preInsert();
		this.deleteFlag = DeleteFlagEnum.NO.getValue();
		this.createTime = DateUtils.getNow();
		this.updateTime = DateUtils.getNow();
	}
	public void validation(){
		if (StringUtils.isBlank(this.redenvelopeRuleId) && StringUtils.isBlank(this.rateCouponRuleId)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_GIFT_AWARD_NOT_NULL));
		}
	}
	
	/** 获取 礼包的名称 */
	public String getGiftName() {
		return giftName;
	}
	/** 设置 礼包的名称 */
	public void setGiftName(final String giftName) {
		this.giftName = giftName;
	}
	/** 获取 礼包的类型 */
	public String getGiftType() {
		return giftType;
	}
	/** 设置 礼包的类型 */
	public void setGiftType(final String giftType) {
		this.giftType = giftType;
	}
	/** 获取 红包规则id */
	public String getRedenvelopeRuleId() {
		return redenvelopeRuleId;
	}
	/** 设置 红包规则id */
	public void setRedenvelopeRuleId(final String redenvelopeRuleId) {
		this.redenvelopeRuleId = redenvelopeRuleId;
	}
	/** 获取 加息券规则id */
	public String getRateCouponRuleId() {
		return rateCouponRuleId;
	}
	/** 设置 加息券规则id */
	public void setRateCouponRuleId(final String rateCouponRuleId) {
		this.rateCouponRuleId = rateCouponRuleId;
	}
	/** 获取 礼包描述 */
	public String getRemark() {
		return remark;
	}
	/** 设置 礼包描述 */
	public void setRemark(final String remark) {
		this.remark = remark;
	}
	/** 获取 礼包创建日期 */
	public Date getCreateTime() {
		return createTime;
	}
	/** 设置 礼包创建日期 */
	public void setCreateTime(final Date createTime) {
		this.createTime = createTime;
	}
	/** 获取 礼包更新日期 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/** 设置 礼包更新日期 */
	public void setUpdateTime(final Date updateTime) {
		this.updateTime = updateTime;
	}
	/** 获取删除标示 */
	public String getDeleteFlag() {
		return deleteFlag;
	}
	/** 设置删除标示 */
	public void setDeleteFlag(final String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	/** 获取 加息券规则名称 */
	public String getRateName() {
		return rateName;
	}
	/** 设置  加息券规则名称 */
	public void setRateName(final String rateName) {
		this.rateName = rateName;
	}
	/** 获取 红包规则名称 */
	public String getRedName() {
		return redName;
	}
	/** 设置 红包规则名称 */
	public void setRedName(final String redName) {
		this.redName = redName;
	}
	/** 获取 红包规则*/
	public RedenvelopeRule getRedRule() {
		return redRule;
	}
	/** 获取设置规则*/
	public void setRedRule(final RedenvelopeRule redRule) {
		this.redRule = redRule;
	}
	/** 获取 加息券规则*/
	public RateCouponRule getRateRule() {
		return rateRule;
	}
	/** 设置 加息券规则*/
	public void setRateRule(final RateCouponRule rateRule) {
		this.rateRule = rateRule;
	}
	public BigDecimal getRedAmount() {
		return redAmount;
	}
	public void setRedAmount(BigDecimal redAmount) {
		this.redAmount = redAmount;
	}
	public String getRateRange() {
		return rateRange;
	}
	public void setRateRange(String rateRange) {
		this.rateRange = rateRange;
	}
	/** toString*/
	@Override
	public String toString() {
		return "UserGift [" + "uuid=" + uuid + ", giftName=" + giftName + ", giftType=" + giftType + ", redenvelopeRuleId=" + redenvelopeRuleId + ", rateCouponRuleId=" + rateCouponRuleId + ", remark=" + remark + ", createTime=" + createTime + ", updateTime=" + updateTime + ", deleteFlag=" + deleteFlag +  "]";
	}
}
