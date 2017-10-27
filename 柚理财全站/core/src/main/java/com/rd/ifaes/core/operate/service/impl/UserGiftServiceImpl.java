package com.rd.ifaes.core.operate.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.validator.BeanValidators;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.operate.domain.UserActivityAwardLog;
import com.rd.ifaes.core.operate.domain.UserGift;
import com.rd.ifaes.core.operate.domain.UserRateCoupon;
import com.rd.ifaes.core.operate.domain.UserRedenvelope;
import com.rd.ifaes.core.operate.mapper.UserGiftMapper;
import com.rd.ifaes.core.operate.service.RateCouponRuleDetailService;
import com.rd.ifaes.core.operate.service.RateCouponRuleService;
import com.rd.ifaes.core.operate.service.RedenvelopeRuleDetailService;
import com.rd.ifaes.core.operate.service.RedenvelopeRuleService;
import com.rd.ifaes.core.operate.service.UserActivityAwardLogService;
import com.rd.ifaes.core.operate.service.UserGiftService;
import com.rd.ifaes.core.operate.service.UserRateCouponService;
import com.rd.ifaes.core.operate.service.UserRedenvelopeService;
import com.rd.ifaes.core.user.domain.User;

/**
 * ServiceImpl:UserGiftServiceImpl
 * @author 
 * @version 3.0
 * @date 2016-8-1
 */
@Service("userGiftService") 
public class UserGiftServiceImpl  extends BaseServiceImpl<UserGiftMapper, UserGift> implements UserGiftService{
	/**红包规则业务处理*/
	@Resource
	private transient  RedenvelopeRuleService   redenvelopeRuleService;
	/**加息券规则业务处理*/
	@Resource
	private transient  RateCouponRuleService   rateCouponRuleService;
	@Resource
	private transient RateCouponRuleDetailService rateCouponRuleDetailService;
	@Resource
	private transient RedenvelopeRuleDetailService redenvelopeRuleDetailService;
	/** 用户红包业务处理 */
	@Resource
	private transient UserRedenvelopeService userRedenvelopeService;
	/** 奖励日志业务处理 */
	@Resource
	private transient UserActivityAwardLogService userActivityAwardLogService;
	/** 用户加息券业务处理 */
	@Resource
	private transient UserRateCouponService userRateCouponService;
	/**
	 * 发放生日礼包
	 */
	@Override
	public Map<String, Object> grantBirthGift(final UserGift userGift,final User user) {
		// 生日礼包验证
		List<UserRedenvelope> urvs = new ArrayList<>();
		List<UserRateCoupon> urcs = new ArrayList<>();
		List<UserActivityAwardLog> logs = new ArrayList<>();
		redenvelopeRuleService.grantRedenvelope(user, userGift.getRedRule(), null,null,urvs,logs);
		rateCouponRuleService.grantRateCoupon(user, userGift.getRateRule(), null,null,urcs,logs);
		if(!CollectionUtils.isEmpty(urvs)){
			userRedenvelopeService.insertBatch(urvs);
		}
		if(!CollectionUtils.isEmpty(urcs)){
			userRateCouponService.insertBatch(urcs);
		}
		if(!CollectionUtils.isEmpty(logs)){
			userActivityAwardLogService.insertBatch(logs);				
		}
		return null;
	}
	
	@Override
	public void addUserGift(UserGift userGift) {
		BeanValidators.validate(userGift);
		userGift.validation();
		userGift.preInsert();
		dao.insert(userGift);
	}

	@Override
	public Object findManageList(final UserGift model) {
		List<UserGift> list = this.findList(model);
		for(UserGift userGift : list){
			// 获取红包金额
			BigDecimal redAmount = null;
			if(StringUtils.isNotBlank(userGift.getRedenvelopeRuleId())){
				redAmount = redenvelopeRuleDetailService.getAmountByRuleId(userGift.getRedenvelopeRuleId());
			}
			userGift.setRedAmount(redAmount==null?BigDecimal.ZERO:redAmount);
			// 获取加息券加息范围
			BigDecimal minApr = BigDecimal.ZERO; // 最小利率
			BigDecimal maxApr = BigDecimal.ZERO; // 最大利率
			if(StringUtils.isNotBlank(userGift.getRateCouponRuleId())){
				final Map<String, BigDecimal> rateDetailMap = rateCouponRuleDetailService.getIntervalAprByRuleId(userGift.getRateCouponRuleId());
				if(rateDetailMap != null && rateDetailMap.size() > Constant.INT_ZERO){
					minApr = rateDetailMap.get("minApr");
					maxApr = rateDetailMap.get("maxApr");
				}
			}
			StringBuilder rateRange = new StringBuilder(String.valueOf(BigDecimalUtils.round(minApr)));
			// 如果存在最大利率 拼接
			if(maxApr.doubleValue() > Constant.INT_ZERO && maxApr.compareTo(minApr) > Constant.INT_ZERO){
				rateRange.append("~");
				rateRange.append(String.valueOf(BigDecimalUtils.round(maxApr)));
			}
			userGift.setRateRange(rateRange.toString());
		}
		return list;
	}

}