package com.rd.ifaes.core.operate.model;

import java.math.BigDecimal;

import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;

import com.rd.ifaes.common.dict.OperateEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.operate.domain.RedenvelopeRule;

/**
 * 红包规则model类
 * 
 * @author wyw
 */
public class RedenvelopeRuleModel extends RedenvelopeRule {
	/** 序列号 */
	private static final long serialVersionUID = 9200204096882211934L;

	/** 有效期限制 */
	private String expiryStatus;
	/** 发放时间限制 */
	private String grantTimeStatus;
	/** 使用限制 */
	private String useStatus;
	
	/** 发放最大金额（比例满返的时候，设置发放红包最大值） */
	private BigDecimal grantMaxTemp;
	/** 发放最小金额（比例满返的时候，设置发放红包最小值） */
	private BigDecimal grantMinTemp;
	
	/**红包适用投资期限（以月份为单位）*/
	private int redInvestExpireTime;
	
	/** 发放方式 :固定金额 */
	public static final String GRANT_TYPE_FIXED_AMOUNT = "1";
	/** 发放方式 :固定比例 */
	public static final String GRANT_TYPE_FIXED_RATE = "2";
	/** 发放方式 :金额满返 */
	public static final String GRANT_TYPE_INTERVAL_AMOUNT = "3";
	/** 发放方式 :比例满返 */
	public static final String GRANT_TYPE_INTERVAL_RATE = "4";

	/** 状态 :启用 */
	public static final String STATUS_SERVICE = "1";
	/** 状态 :禁用 */
	public static final String STATUS_FORBIDDEN = "0";

	/** 有效期限制状态 :不限制 */
	public static final String EXPIRY_STATUS_UNLIMITED = "0";
	/** 有效期限制状态 :固定天数 */
	public static final String EXPIRY_STATUS_VALIDDAY = "1";
	/** 有效期限制状态 :固定有效期 */
	public static final String EXPIRY_STATUS_EXPIRETIME = "2";

	/** 发放时间状态 :限制 */
	public static final String TIME_STATUS_YES = "1";
	/** 发放时间状态 :不限制 */
	public static final String TIME_STATUS_UNLIMITED = "0";

	/** 使用条件状态 :限制项目类别 */
	public static final String USE_STATUS_PROJECT_TYPE = "1";
	/** 使用条件状态 :不限制 */
	public static final String USE_STATUS_UNLIMITED = "0";

	/**
	 * 根据页面参数,设置数据有效性
	 * 
	 * @author wyw
	 */
	public void packageRedEnvelope() {
		// 处理 有效期限制
		if (RedenvelopeRuleModel.EXPIRY_STATUS_UNLIMITED.equalsIgnoreCase(this.getExpiryStatus())) {// 有效期
																									// 不限制
			this.setUseValidDay(null);
			this.setUseExpireTime(null);
		} else if (RedenvelopeRuleModel.EXPIRY_STATUS_VALIDDAY.equalsIgnoreCase(this.getExpiryStatus())) {// 固定天数
			this.setUseExpireTime(null);
		} else if (RedenvelopeRuleModel.EXPIRY_STATUS_EXPIRETIME.equalsIgnoreCase(this.getExpiryStatus())) {// 固定有效期
			this.setUseValidDay(null);
		}
		// 处理发放时间限制
		if (RedenvelopeRuleModel.TIME_STATUS_UNLIMITED.equalsIgnoreCase(this.getGrantTimeStatus())) {// 不限制
			this.setGrantEndTime(null);
			this.setGrantStartTime(null);
		}
		// 处理使用条件状态
		if (RedenvelopeRuleModel.USE_STATUS_UNLIMITED.equalsIgnoreCase(this.getUseStatus())) {// 不限制
			this.setUseProjectType(null);
		}
		//设置自定义url
		if(OperateEnum.ACTIVITYPLAN_CUSTOM.eq(this.activityCode)){
			this.setGrantUrl(ConfigUtils.getValue(ConfigConstant.WEB_URL)+"/platformActivity/"+this.grantUrl);
		}
		//设置发放数量
		if (this.totalNum != null && this.totalNum==0) {
			this.setTotalNum(null);
		}
	}

	/**
	 * 根据属性刷新状态
	 */
	public void reFreshStatus() {
		// 设置 有效期 状态
		this.setExpiryStatus(RedenvelopeRuleModel.EXPIRY_STATUS_UNLIMITED);
		if (!ObjectUtils.isEmpty(this.getUseValidDay())) {
			this.setExpiryStatus(RedenvelopeRuleModel.EXPIRY_STATUS_VALIDDAY);
		}
		if (!ObjectUtils.isEmpty(this.getUseExpireTime())) {
			this.setExpiryStatus(RedenvelopeRuleModel.EXPIRY_STATUS_EXPIRETIME);
		}
		// 设置发放时间限制
		this.setGrantTimeStatus(RedenvelopeRuleModel.TIME_STATUS_UNLIMITED);
		if (!ObjectUtils.isEmpty(this.getGrantStartTime()) || !ObjectUtils.isEmpty(this.getGrantEndTime())) {
			this.setGrantTimeStatus(RedenvelopeRuleModel.TIME_STATUS_YES);
		}
		// 设置使用限制
		this.setUseStatus(RedenvelopeRuleModel.USE_STATUS_UNLIMITED);
		if (!ObjectUtils.isEmpty(this.getUseProjectType())) {
			this.setUseStatus(RedenvelopeRuleModel.USE_STATUS_PROJECT_TYPE);
		}
	}

	/**
	 * 检查添加红包规则 数据
	 */
	public void checkAddModel() {
		/**
		 * 固定期红包最大有效期天数不能超对应的系统参数值 
		 */
		if (RedenvelopeRuleModel.EXPIRY_STATUS_VALIDDAY.equals(this.getExpiryStatus()) && Integer.parseInt(this.useValidDay) > ConfigUtils.getInt(ConfigConstant.REDENVELPE_MAX_AVAIL_PERIOD)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.REDENVELOPE_MAX_PERIOD_ERROR,ConfigUtils.getInt(ConfigConstant.REDENVELPE_MAX_AVAIL_PERIOD)));
		}
		/**
		 * 发放数量
		 */
		if (this.totalNum!=null && this.totalNum>Constant.ONE_MILLION) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GRANT_TOTAL_NUM_ERROR));
		}
		
		/**
		 * 固定比例 校验最大最小金额
		 */
		if (GRANT_TYPE_FIXED_RATE.equals(this.getGrantType())) {
			if (this.getGrantMin().compareTo(BigDecimal.ZERO) == Constant.INT_ZERO) {
				throw new BussinessException(ResourceUtils.get(ResourceConstant.REDENVELOPE_GRANTMIN_NOTEMPTY));
			}
			if (this.getGrantMax().compareTo(BigDecimal.ZERO) == Constant.INT_ZERO) {
				throw new BussinessException(ResourceUtils.get(ResourceConstant.REDENVELOPE_GRANTMAX_NOTEMPTY));
			}
			if (this.getGrantMin().compareTo(this.getGrantMax()) == Constant.INT_ONE) {
				throw new BussinessException(ResourceUtils.get(ResourceConstant.REDENVELOPE_GRANT_AMOUNT_ERROR));
			}
		}
		
		/**
		 *  比例满返 校验最大最小金额
		 */
		if(GRANT_TYPE_INTERVAL_RATE.equals(this.getGrantType())){
			if (this.getGrantMinTemp().compareTo(BigDecimal.ZERO) == Constant.INT_ZERO) {
				throw new BussinessException(ResourceUtils.get(ResourceConstant.REDENVELOPE_GRANTMIN_NOTEMPTY));
			}
			if (this.getGrantMaxTemp().compareTo(BigDecimal.ZERO) == Constant.INT_ZERO) {
				throw new BussinessException(ResourceUtils.get(ResourceConstant.REDENVELOPE_GRANTMAX_NOTEMPTY));
			}
			if (this.getGrantMinTemp().compareTo(this.getGrantMaxTemp()) == Constant.INT_ONE) {
				throw new BussinessException(ResourceUtils.get(ResourceConstant.REDENVELOPE_GRANT_AMOUNT_ERROR));
			}
			this.setGrantMax(this.getGrantMaxTemp());
			this.setGrantMin(this.getGrantMinTemp());
		}
		
		/**
		 * grantTimeStatus 发放时间
		 */
		if(TIME_STATUS_YES.equals(this.getGrantTimeStatus())){
			if(this.getGrantStartTime() == null){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.REDENVELOPE_GRANT_BEGINTIME_NOTEMPTY));
			}
			if(this.getGrantEndTime() == null){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.REDENVELOPE_GRANT_ENDTIME_NOTEMPTY));
			}
			if(this.getGrantEndTime().before(this.getGrantStartTime())){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.REDENVELOPE_GRANT_TIME_ERROR));
			}
		}
		
		//使用条件
		if (USE_STATUS_PROJECT_TYPE.equals(this.getUseStatus()) && StringUtils.isBlank(this.useProjectType)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USE_PROJECT_TYPE_NOTEMPTY));
		}
		//有效期
		if((EXPIRY_STATUS_VALIDDAY.equals(this.getExpiryStatus()) && StringUtils.isBlank(this.getUseValidDay())) 
				|| (EXPIRY_STATUS_EXPIRETIME.equals(this.getExpiryStatus()) && StringUtils.isBlank(this.getUseExpireTime()))){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.USE_VALIDDAY_NOTEMPTY));
		}
		
	}

	
	/**
	 * 获取过期状态
	 */
	public String getExpiryStatus() {
		return expiryStatus;
	}

	/**
	 * 设置过期状态限制
	 */
	public void setExpiryStatus(final String expiryStatus) {
		this.expiryStatus = expiryStatus;
	}

	

	/**
	 * 获取属性grantTimeStatus的值
	 * @return grantTimeStatus属性值
	 */
	public String getGrantTimeStatus() {
		return grantTimeStatus;
	}

	/**
	 * 设置属性grantTimeStatus的值
	 * @param  grantTimeStatus属性值
	 */
	public void setGrantTimeStatus(String grantTimeStatus) {
		this.grantTimeStatus = grantTimeStatus;
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
	 * 实例化UserRedEnvelopeModel
	 * 
	 * @param userRedEnvelope
	 * @return UserRedEnvelopeModel
	 */
	public static RedenvelopeRuleModel instance(final RedenvelopeRule userRedEnvelope) {
		final RedenvelopeRuleModel model = new RedenvelopeRuleModel();
		BeanUtils.copyProperties(userRedEnvelope, model);
		return model;
	}

	/**
	 * 根据Model返回UserRedEnvelope原型
	 * 
	 * @return UserRedEnvelope
	 */
	public RedenvelopeRule prototype() {
		final RedenvelopeRule redenvelopeRule = new RedenvelopeRule();
		BeanUtils.copyProperties(this, redenvelopeRule);
		return redenvelopeRule;
	}

	public BigDecimal getGrantMaxTemp() {
		return grantMaxTemp;
	}

	public void setGrantMaxTemp(BigDecimal grantMaxTemp) {
		this.grantMaxTemp = grantMaxTemp;
	}

	public BigDecimal getGrantMinTemp() {
		return grantMinTemp;
	}

	public void setGrantMinTemp(BigDecimal grantMinTemp) {
		this.grantMinTemp = grantMinTemp;
	}
	/**
	 * 获得红包适用期限
	 * @return
	 */
	public int getRedInvestExpireTime() {
		return redInvestExpireTime;
	}
	/**
	 * 设置红包适用期限
	 * @param redInvestExpireTime
	 */
	public void setRedInvestExpireTime(int redInvestExpireTime) {
		this.redInvestExpireTime = redInvestExpireTime;
	}
}
