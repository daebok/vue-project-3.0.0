package com.rd.ifaes.core.operate.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.dict.DeleteFlagEnum;
import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;

/**
 * entity:UserVipLevel
 * @author 
 * @version 3.0
 * @date 2016-8-1
 */
public class UserVipLevel extends BaseEntity<UserVipLevel> {
		
	/** 序列号*/
	private static final long serialVersionUID = -8334986094236973768L;
	/** 所需成长值*/
	private BigDecimal	growthValue;		
	/** 成长封顶值*/
	private BigDecimal	growthLimitValue;		
	/** vip等级 每次新增+1*/
	private String	vipLevel;		
	/** 年扣除成长值*/
	private BigDecimal	yearDeduction;		
	/** 额外收益率*/
	private BigDecimal	upApr;		
	/** 红包发放规则id*/
	private String	redenvelopeRuleId;		 
	/** 加息券发放规则id*/
	private String	rateCouponRuleId;		 
	/** 生日礼包id*/
	private String	birthdayGiftId;		
	/** 专享礼包id*/
	private String	exclusiveGiftId;		
	/**创建时间*/
	private Date	createTime;		
	/**更新日期*/
	private Date	updateTime;		
	/**逻辑删除标识 0正常  1删除*/
	private String	deleteFlag;		 

	//其他自定义属性
	/** 加息券规则名称*/
	private String rateName;
	/** 红包规则名称*/
	private String redName;
	/** 生日礼包名称*/
	private String birthdayGiftName; 
	/** 专享礼包名称*/
	private String exclusiveGiftName; 
	/** 红包金额总数*/
	private BigDecimal	redTotal;  
	/** 红包规则*/
	private RedenvelopeRule redRule;
	/** 加息券规则*/
	private RateCouponRule rateRule;
	/** 生日礼包*/
	private UserGift   birthdayGig;
	/** 专享礼包*/
	private UserGift   exclusiveGiftGig;
	/** 最低加息率*/
	private BigDecimal minRate;
	/** 最高加息率*/
	private BigDecimal maxRate;

	/** Constructor*/
	public UserVipLevel() {
		super();
	}

	/** full Constructor */
	public UserVipLevel(final String uuid, final BigDecimal growthValue,final  BigDecimal growthLimitValue,final  String vipLevel,
			final BigDecimal yearDeduction,final  BigDecimal upApr,final  String redenvelopeRuleId,final  String rateCouponRuleId,
			final String birthdayGiftId, final String exclusiveGiftId,final  Date createTime, final Date updateTime, final String deleteFlag) {
		super();
		setUuid(uuid);
		this.growthValue = growthValue;
		this.growthLimitValue = growthLimitValue;
		this.vipLevel = vipLevel;
		this.yearDeduction = yearDeduction;
		this.upApr = upApr;
		this.redenvelopeRuleId = redenvelopeRuleId;
		this.rateCouponRuleId = rateCouponRuleId;
		this.birthdayGiftId = birthdayGiftId;
		this.exclusiveGiftId = exclusiveGiftId;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.deleteFlag = deleteFlag;
	}
	
	/**
	 * 修改校验
	 * @author ZhangBiao
	 * @date 2016年10月27日
	 * @param minVipLevel
	 * @param maxVipLevel
	 */
	public void checkUpdate(UserVipLevel minVipLevel, UserVipLevel maxVipLevel) {
		// 检查 userVipLevelModel设置的成长所需值是否 大于 最大等级的 成长封顶值
		if (maxVipLevel != null && this.getGrowthValue().doubleValue() <= maxVipLevel.getGrowthValue().doubleValue()) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GROWTHVALUE_MUST_GT_UPLEVEL,String.valueOf(maxVipLevel.getGrowthValue().longValue())));
		}
		// 检查 userVipLevelModel设置的所需成长封顶值需小于下级成长值
		if (minVipLevel != null && this.getGrowthValue().doubleValue() >= minVipLevel.getGrowthValue().doubleValue()) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GROWTHVALUE_MUST_LT_DOWNLEVEL,String.valueOf(minVipLevel.getGrowthValue().longValue())));
		}
	}
	
	@Override
	/** 持久化前执行的操作 */
	public void preInsert() {
		super.preInsert();
		this.deleteFlag = DeleteFlagEnum.NO.getValue();
		this.createTime = DateUtils.getNow();
		this.updateTime = DateUtils.getNow();
	}
	/** 获取成长值  */
	public BigDecimal getGrowthValue() {
		return growthValue;
	}
	/** 设置成长值  */
	public void setGrowthValue(final BigDecimal growthValue) {
		this.growthValue = growthValue;
	}
	/** 获取最大成长值  */
	public BigDecimal getGrowthLimitValue() {
		return growthLimitValue;
	}
	/** 设置最大成长值  */
	public void setGrowthLimitValue(final BigDecimal growthLimitValue) {
		this.growthLimitValue = growthLimitValue;
	}
	/** 获取vip等级  */
	public String getVipLevel() {
		return vipLevel;
	}
	/** 设置vip等级  */
	public void setVipLevel(final String vipLevel) {
		this.vipLevel = vipLevel;
	}
	/** 获取年扣值  */
	public BigDecimal getYearDeduction() {
		return yearDeduction;
	}
	/** 设置年扣值  */
	public void setYearDeduction(final BigDecimal yearDeduction) {
		this.yearDeduction = yearDeduction;
	}
	/** 获取加息值  */
	public BigDecimal getUpApr() {
		return upApr;
	}
	/** 设置加息值  */
	public void setUpApr(final BigDecimal upApr) {
		this.upApr = upApr;
	}
	/** 获取红包规则id  */
	public String getRedenvelopeRuleId() {
		return redenvelopeRuleId;
	}
	/** 设置红包规则id  */
	public void setRedenvelopeRuleId(final String redenvelopeRuleId) {
		this.redenvelopeRuleId = redenvelopeRuleId;
	}
	/** 获取加息券规则id  */
	public String getRateCouponRuleId() {
		return rateCouponRuleId;
	}
	/** 设置加息券规则id  */
	public void setRateCouponRuleId(final String rateCouponRuleId) {
		this.rateCouponRuleId = rateCouponRuleId;
	}
	/** 获取生日礼包id  */
	public String getBirthdayGiftId() {
		return birthdayGiftId;
	}
	/** 设置生日礼包id  */
	public void setBirthdayGiftId(final String birthdayGiftId) {
		this.birthdayGiftId = birthdayGiftId;
	}
	/** 获取专享礼包id  */
	public String getExclusiveGiftId() {
		return exclusiveGiftId;
	}
	/** 设置专享礼包id  */
	public void setExclusiveGiftId(final String exclusiveGiftId) {
		this.exclusiveGiftId = exclusiveGiftId;
	}
	/** 获取创建日期  */
	public Date getCreateTime() {
		return createTime;
	}
	/** 设置创建日期  */
	public void setCreateTime(final Date createTime) {
		this.createTime = createTime;
	}
	/** 获取更新日期  */
	public Date getUpdateTime() {
		return updateTime;
	}
	/** 设置更新日期  */
	public void setUpdateTime(final Date updateTime) {
		this.updateTime = updateTime;
	}
	/** 获取删除标识  */
	public String getDeleteFlag() {
		return deleteFlag;
	}
	/** 设置删除标识  */
	public void setDeleteFlag(final String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	/** 获取加息券规则名称  */
	public String getRateName() {
		return rateName;
	}
	/** 设置加息券规则名称  */
	public void setRateName(final String rateName) {
		this.rateName = rateName;
	}
	/** 获取红包规则名称  */
	public String getRedName() {
		return redName;
	}
	/** 设置红包规则名称  */
	public void setRedName(final String redName) {
		this.redName = redName;
	}
	/** 获取生日礼包名称  */
	public String getBirthdayGiftName() {
		return birthdayGiftName;
	}
	/** 设置生日礼包名称  */
	public void setBirthdayGiftName(final String birthdayGiftName) {
		this.birthdayGiftName = birthdayGiftName;
	}
	/** 获取专享礼包名称  */
	public String getExclusiveGiftName() {
		return exclusiveGiftName;
	}
	/** 设置专享礼包名称  */
	public void setExclusiveGiftName(final String exclusiveGiftName) {
		this.exclusiveGiftName = exclusiveGiftName;
	}
	/** 获取红包总额  */
	public BigDecimal getRedTotal() {
		return redTotal;
	}
	/** 设置红包总额  */
	public void setRedTotal(final BigDecimal redTotal) {
		this.redTotal = redTotal;
	}
	/** 获取红包规则 */
	public RedenvelopeRule getRedRule() {
		return redRule;
	}
	/** 设置红包规则 */
	public void setRedRule(final RedenvelopeRule redRule) {
		this.redRule = redRule;
	}
	/** 获取加息券规则 */
	public RateCouponRule getRateRule() {
		return rateRule;
	}
	/** 设置加息券规则 */
	public void setRateRule(final RateCouponRule rateRule) {
		this.rateRule = rateRule;
	}
	/** 获取生日礼包 */
	public UserGift getBirthdayGig() {
		return birthdayGig;
	}
	/** 设置生日礼包 */
	public void setBirthdayGig(final UserGift birthdayGig) {
		this.birthdayGig = birthdayGig;
	}
	/** 获取专享礼包 */
	public UserGift getExclusiveGiftGig() {
		return exclusiveGiftGig;
	}
	/** 设置专享礼包 */
	public void setExclusiveGiftGig(final UserGift exclusiveGiftGig) {
		this.exclusiveGiftGig = exclusiveGiftGig;
	}
	public String getGrowthValueStr(){
		return this.growthValue.toString();
	}
	public String getGrowthLimitValueStr(){
		if(StringUtils.isBlank(this.growthLimitValue)){
			return StringUtils.EMPTY;
		}else{
			return this.growthLimitValue.toString();
		}
	}
	public String getYearDeductionStr(){
		return this.yearDeduction.toString();
	}
	
	public BigDecimal getMinRate() {
		return minRate;
	}

	public void setMinRate(BigDecimal minRate) {
		this.minRate = minRate;
	}

	public BigDecimal getMaxRate() {
		return maxRate;
	}

	public void setMaxRate(BigDecimal maxRate) {
		this.maxRate = maxRate;
	}

	/** toString */
	@Override
	public String toString() {
		return "UserVipLevel [" + "uuid=" + uuid + ", growthValue=" + growthValue + ", growthLimitValue=" + growthLimitValue + ", vipLevel=" + vipLevel + ", yearDeduction=" + yearDeduction + ", upApr=" + upApr + ", redenvelopeRuleId=" + redenvelopeRuleId + ", rateCouponRuleId=" + rateCouponRuleId + ", birthdayGiftId=" + birthdayGiftId + ", exclusiveGiftId=" + exclusiveGiftId + ", createTime=" + createTime + ", updateTime=" + updateTime + ", deleteFlag=" + deleteFlag +  "]";
	}

}
