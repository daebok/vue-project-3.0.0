package com.rd.ifaes.core.operate.model;

import java.math.BigDecimal;

import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;

import com.rd.ifaes.common.dict.OperateEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.operate.domain.RateCouponRule;

/**
 * 加息券规则model
 * @version 3.0
 * @author wyw
 */
public class RateCouponRuleModel extends RateCouponRule {
	/** 序列号*/
	private static final long serialVersionUID = 9200204096882211934L;
	/** 发放时间限制 */
	private String expiryStatus;
	/** 发放时间限制 */
	private String grantTimestatus;
	/** 使用限制 */
	private String useStatus;
	/** 发放方式 固定值 */
	public static final String GRANT_TYPE_FIXED_AMOUNT = "1";
	/** 发放方式 金额满返 */
	public static final String GRANT_TYPE_INTERVAL_AMOUNT = "2";
	/** 状态 ->启用 */
	public static final String STATUS_SERVICE = "1";
	/** 状态 ->禁用 */
	public static final String STATUS_FORBIDDEN = "0";

	/** 有效期限制状态 ->不限制 */
	public static final String EXPIRY_STATUS_UNLIMITED = "0";
	/** 有效期限制状态 ->固定天数 */
	public static final String EXPIRY_STATUS_VALIDDAY = "1";
	/** 有效期限制状态 ->固定有效期 */
	public static final String EXPIRY_STATUS_EXPIRETIME = "2";

	/** 发放时间状态 ->限制 */
	public static final String TIME_STATUS_YES = "1";
	/** 发放时间状态 ->不限制 */
	public static final String TIME_STATUS_UNLIMITED = "0";

	/** 发放时间状态 ->限制项目类别 */
	public static final String USE_STATUS_PROJECT_TYPE = "1";
	/** 发放时间状态 ->不限制 */
	public static final String USE_STATUS_UNLIMITED = "0";

	/** 最大加息利率 */
	public static final BigDecimal UPAPRMAX = BigDecimal.valueOf(24);
	/** 最小加息利率 */
	public static final BigDecimal UPAPRMIN = BigDecimal.valueOf(0.01);
	/**
	 * 
	 * 根据页面参数,设置数据有效性
	 * 
	 * @author wyw
	 * @date 2016-7-26
	 */
	public void packageRatecoupon() {
		// 处理 有效期限制
		if (RateCouponRuleModel.EXPIRY_STATUS_UNLIMITED.equalsIgnoreCase(this.getExpiryStatus())) {// 有效期
																									// 不限制
			this.setUseValidDay(null);
			this.setUseExpireTime(null);
		} else if (RateCouponRuleModel.EXPIRY_STATUS_VALIDDAY.equalsIgnoreCase(this.getExpiryStatus())) {// 固定天数
			this.setUseExpireTime(null);
		} else if (RateCouponRuleModel.EXPIRY_STATUS_EXPIRETIME.equalsIgnoreCase(this.getExpiryStatus())) {// 固定有效期
			this.setUseValidDay(null);
		}
		// 处理发放时间限制
		if (RateCouponRuleModel.TIME_STATUS_UNLIMITED.equalsIgnoreCase(this.getGrantTimestatus())) {// 不限制
			this.setGrantEndTime(null);
			this.setGrantStartTime(null);
		}
		// 处理使用条件状态
		if (RateCouponRuleModel.USE_STATUS_UNLIMITED.equalsIgnoreCase(this.getUseStatus())) {// 不限制
			this.setUseProjectType(null);
		}
		//设置自定义url
		if(OperateEnum.ACTIVITYPLAN_CUSTOM.eq(this.activityCode)){
			this.setGrantUrl(ConfigUtils.getValue(ConfigConstant.WEB_URL)+"/platformActivity/"+this.grantUrl);
		}
		//设置发放数量
		if (this.totalNum != null &&this.totalNum==0) {
			this.setTotalNum(null);
		}
	}

	/**
	 * 获取过期时间限制状态
	 */
	public String getExpiryStatus() {
		return expiryStatus;
	}

	/**
	 * 设置过期时间限制状态
	 */
	public void setExpiryStatus(final String expiryStatus) {
		this.expiryStatus = expiryStatus;
	}

	/**
	 * 获取发放时间限制状态
	 */
	public String getGrantTimestatus() {
		return grantTimestatus;
	}

	/**
	 * 设置发放时间限制状态
	 */
	public void setGrantTimestatus(final String grantTimestatus) {
		this.grantTimestatus = grantTimestatus;
	}

	/**
	 * 获取使用状态
	 */
	public String getUseStatus() {
		return useStatus;
	}

	/**
	 * 设置使用状态
	 */
	public void setUseStatus(final String useStatus) {
		this.useStatus = useStatus;
	}

	/**
	 * 根据属性设置状态
	 * 
	 * @author wyw
	 */
	public void reFreshStatus() {
		// 设置 有效期 状态
		this.setExpiryStatus(RateCouponRuleModel.EXPIRY_STATUS_UNLIMITED);

		if (!ObjectUtils.isEmpty(this.getUseValidDay())) {
			this.setExpiryStatus(RateCouponRuleModel.EXPIRY_STATUS_VALIDDAY);
		}
		if (!ObjectUtils.isEmpty(this.getUseExpireTime())) {
			this.setExpiryStatus(RateCouponRuleModel.EXPIRY_STATUS_EXPIRETIME);
		}
		// 设置发放时间限制
		this.setGrantTimestatus(RateCouponRuleModel.TIME_STATUS_UNLIMITED);
		if (!ObjectUtils.isEmpty(this.getGrantStartTime())||!ObjectUtils.isEmpty( this.getGrantEndTime())) {
			this.setGrantTimestatus(RateCouponRuleModel.TIME_STATUS_YES);
		}
		// 设置使用限制状态
		this.setUseStatus(RateCouponRuleModel.USE_STATUS_UNLIMITED);
		if (!ObjectUtils.isEmpty(this.getUseProjectType())) {
			this.setUseStatus(RateCouponRuleModel.USE_STATUS_PROJECT_TYPE);
		}
	}

	/**
	 * 实例化 RatecouponRuleModel 对象
	 * 
	 * @author wyw
	 * @date 2016-7-28
	 * @param ratecouponRule
	 * @return
	 */
	public static RateCouponRuleModel instance(final RateCouponRule rateCouponRule) {
		final RateCouponRuleModel model = new RateCouponRuleModel();
		BeanUtils.copyProperties(rateCouponRule, model);
		return model;
	}

	/**
	 * 根据model返回 RatecouponRule原型
	 * 
	 * @author wyw
	 * @date 2016-7-28
	 * @return
	 */
	public RateCouponRule prototype() {
		final RateCouponRule ratecouponRule = new RateCouponRule();
		BeanUtils.copyProperties(this, ratecouponRule);
		return ratecouponRule;
	}
	/**
	 * 校验
	 */
	public void checkModel(){
		/**
		 * 固定期加息劵最大有效期天数不能超对应的系统参数值 
		 */
		if (EXPIRY_STATUS_VALIDDAY.equals(this.getExpiryStatus()) && Integer.parseInt(this.useValidDay) > ConfigUtils.getInt(ConfigConstant.RATECOUPON_MAX_AVAIL_PERIOD)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.RATECOUPON_MAX_PERIOD_ERROR,ConfigUtils.getInt(ConfigConstant.RATECOUPON_MAX_AVAIL_PERIOD)));
		}
		/**
		 * 发放数量
		 */
		if (this.totalNum!=null && this.totalNum>Constant.ONE_MILLION) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GRANT_TOTAL_NUM_ERROR));
		}
	}
}
