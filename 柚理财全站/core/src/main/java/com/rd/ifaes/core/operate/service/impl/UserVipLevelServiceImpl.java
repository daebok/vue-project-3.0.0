package com.rd.ifaes.core.operate.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rd.ifaes.common.annotation.CacheEvict;
import com.rd.ifaes.common.annotation.Cacheable;
import com.rd.ifaes.common.dict.DeleteFlagEnum;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.NumberUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.operate.domain.UserVipLevel;
import com.rd.ifaes.core.operate.mapper.UserVipLevelMapper;
import com.rd.ifaes.core.operate.model.UserVipLevelModel;
import com.rd.ifaes.core.operate.service.RateCouponRuleDetailService;
import com.rd.ifaes.core.operate.service.RedenvelopeRuleDetailService;
import com.rd.ifaes.core.operate.service.UserGiftService;
import com.rd.ifaes.core.operate.service.UserVipLevelService;

/**
 * ServiceImpl:UserVipLevelServiceImpl
 * 
 * @author
 * @version 3.0
 * @date 2016-8-1
 */
@Service("userVipLevelService")
public class UserVipLevelServiceImpl extends BaseServiceImpl<UserVipLevelMapper, UserVipLevel> implements UserVipLevelService {
	
	@Resource
	private transient RedenvelopeRuleDetailService redenvelopeRuleDetailService;
	@Resource
	private transient RateCouponRuleDetailService rateCouponRuleDetailService;
	
	@Resource
	private transient UserGiftService userGiftService;
	
	/**
	 * vip等级保存
	 */
	@Override
	@CacheEvict(keys={CacheConstant.KEY_PREFIX_VIP_LEVEL_LIST})
	public boolean saveVipLevel(final UserVipLevelModel userVipLevelModel) {
		// 获取最高vip_level值 的 UserVipLevel
		final UserVipLevel maxVipLevel =  getPreviousVipLevel();
		// 数据有效性检查
		 userVipLevelModel.checkAdd(maxVipLevel);         
		// 设置 userVipLevelModel的 vip_level 为最大UserVipLevel vip_level+1
         if(maxVipLevel==null){
        	 userVipLevelModel.setVipLevel(String.valueOf(Constant.INT_ONE));
         }else{
        	 userVipLevelModel.setVipLevel(String.valueOf(NumberUtils.getInt(maxVipLevel.getVipLevel())+1));
         }
		// 保存
		this.insert(userVipLevelModel);
		return true;
	}
	
	@Override
	public void updateVipLevel(UserVipLevel model) {
		String vipLevel = model.getVipLevel();
		final UserVipLevel minVipLevel = dao.getByVipLevel(StringUtils.stringAdd(vipLevel, Constant.INT_ONE));
		final UserVipLevel maxVipLevel = dao.getByVipLevel(StringUtils.stringAdd(vipLevel, Constant.INT_ONE_NEGA));
		model.checkUpdate(minVipLevel,maxVipLevel);
		dao.update(model);
		CacheUtils.batchDel(CacheConstant.KEY_PREFIX_VIP_LEVEL_LIST,StringUtils.format(CacheConstant.KEY_VIP_LEVEL, model.getVipLevel()));
	}
	
	
	@Override
	public void deleteBatch(String[] ids) {
		super.deleteBatch(ids);
		List<UserVipLevel> list = dao.findTrueList();
		int i = 1;
		for(UserVipLevel userVipLevel : list){
			userVipLevel.setVipLevel(String.valueOf(i));
			i ++;
		}
		dao.updateBatch(list);
		CacheUtils.batchDel(CacheConstant.KEY_PREFIX_VIP_LEVEL_LIST);
	}

	@Override
	public List<UserVipLevel> findList(final UserVipLevel model) {
		model.setPage(new Page<UserVipLevel>());
		final List<UserVipLevel> vipLevelList = super.findList(model);
		List<UserVipLevel> vipLevelList2=new ArrayList<UserVipLevel>();
		// 设置红包金额  加息率
		if(vipLevelList != null && vipLevelList.size() > 0){
			for(int i = 0; i < vipLevelList.size(); i++){
				UserVipLevel uvl = vipLevelList.get(i);
				BigDecimal redTotal=accountRedenvelopeTotal(uvl);
				Map<String, BigDecimal> minRateAndMaxRate=getMinRateAndMaxRate(uvl);
				uvl.setRedTotal(redTotal);
				uvl.setMinRate(minRateAndMaxRate.get("minRate"));
				uvl.setMaxRate(minRateAndMaxRate.get("maxRate"));
				// 获取下级所需成长值
				if(i < vipLevelList.size()-1){
					UserVipLevel uvl_ = vipLevelList.get(i+1);
					uvl.setGrowthLimitValue(BigDecimalUtils.sub(uvl_.getGrowthValue(), BigDecimal.ONE));
				}else{
					uvl.setGrowthLimitValue(BigDecimal.ZERO);
				}
				vipLevelList2.add(uvl);
			}
		}
		return vipLevelList2;
	}
	/**
	 * 获取上一个vip等级设置
	 * @author wyw
	 * @date 2016-8-1
	 * @return
	 */
	private UserVipLevel getPreviousVipLevel() {
		final List<UserVipLevel> allUserVipLevel = this.findAll();
		final int listSize = allUserVipLevel.size();
		UserVipLevel userVipLevel;
		UserVipLevel maxUserVip=null;
		int tempLevel =1;
		for (int i = 0; i < listSize; i++) {
			userVipLevel = allUserVipLevel.get(i);
			if(i==(listSize-1)){
				maxUserVip = userVipLevel;
			}else{
				if (tempLevel != NumberUtils.getInt(userVipLevel.getVipLevel())) {
					maxUserVip = userVipLevel;
					break;
				}
			}
			tempLevel++;
		}
		return maxUserVip;
	}
	/**
	 * 获取等级更高的vip等级设置
	 */
	@Override
	@Cacheable(key=CacheConstant.KEY_VIP_LEVEL_LIST, expire = ExpireTime.HALF_AN_HOUR)
	public List<UserVipLevel> findHighLever(final UserVipLevel  vipLevel) {
		return dao.findHighLever(vipLevel);
	}
	/**
	 * 根据vip等级获取设置
	 */
	@Override
	@Cacheable(key=CacheConstant.KEY_VIP_LEVEL, expire = ExpireTime.HALF_AN_HOUR)
	public UserVipLevel getByVipLevel(final String vipLevel) {
		return dao.getByVipLevel(vipLevel);
	}
	
	/**
	 * 获取用户满足的vip等级
	 * @author wyw
	 * @date 2016-8-2
	 * @param userVip
	 * @param addGrowth
	 * @return
	 */
	@Override
    public UserVipLevel getUserVipLevel(final BigDecimal growthValue,final String vipLevel){
    	final UserVipLevel queryVipLevel = new UserVipLevel();
    	queryVipLevel.setDeleteFlag(DeleteFlagEnum.NO.getValue());
    	queryVipLevel.setVipLevel(vipLevel);
    	UserVipLevel ownUserVipLevel =null;//用户的vip等级
    	// 获取大于当前vip等级 所有设置  只升级 不降级
    	final List<UserVipLevel> highList = findHighLever(queryVipLevel);
    	UserVipLevel tempVipLevel;
    	for(int i=0;i<highList.size();i++){
    		tempVipLevel = highList.get(i);
    		if(growthValue.doubleValue()>=tempVipLevel.getGrowthValue().doubleValue()){
    			ownUserVipLevel = tempVipLevel;
    			break;
    		}
    	}
		return ownUserVipLevel;
    }
	@Override
	public List<String> getAllLevel() {
		return dao.getAllLevel();
	}
	
	//统计红包金额
	public BigDecimal accountRedenvelopeTotal(UserVipLevel uvl){
		BigDecimal redTotal=BigDecimal.ZERO;
		//红包规则
		if (StringUtils.isNotBlank(uvl.getRedenvelopeRuleId())) {
			BigDecimal collectAmount = redenvelopeRuleDetailService.collectByRuleId(uvl.getRedenvelopeRuleId());
			redTotal = BigDecimalUtils.add(redTotal, collectAmount);
		}
		/*if (null!=uvl.getRedenvelopeRuleId() && !uvl.getRedenvelopeRuleId().equals("")) {
			List<RedenvelopeRuleDetail> detailList = redenvelopeRuleDetailService.getListByRuleId(uvl.getRedenvelopeRuleId());
			if(detailList != null && detailList.size() > 0){
				RedenvelopeRuleDetail redenvelopeRuleDetail = detailList.get(0);//因为都是固定金额，所有只有一条数据
				redTotal=BigDecimalUtils.add(redTotal,redenvelopeRuleDetail.getAmount());
			}
		}*/
		//生日礼包
		/*if (null!=uvl.getBirthdayGiftId() && !uvl.getBirthdayGiftId().equals("")) {
			UserGift birthdayGift=userGiftService.get(uvl.getBirthdayGiftId());
			if (birthdayGift!=null) {
				List<RedenvelopeRuleDetail> detailList = redenvelopeRuleDetailService.getListByRuleId(birthdayGift.getRedenvelopeRuleId());
				if (detailList != null && detailList.size() > 0) {
					RedenvelopeRuleDetail birthdayGiftDetail = detailList.get(0);    
					redTotal=BigDecimalUtils.add(redTotal,birthdayGiftDetail.getAmount());
				}
			}
		}
		//专属礼包
		if (null!=uvl.getExclusiveGiftId() && !uvl.getExclusiveGiftId().equals("")) {
			UserGift exclusiveGift=userGiftService.get(uvl.getExclusiveGiftId());
			if (exclusiveGift!=null) {
				List<RedenvelopeRuleDetail> detailList = redenvelopeRuleDetailService.getListByRuleId(exclusiveGift.getRedenvelopeRuleId());
				if (detailList != null && detailList.size() > 0) {
					RedenvelopeRuleDetail exclusiveGiftDetail = detailList.get(0);
					redTotal=BigDecimalUtils.add(redTotal,exclusiveGiftDetail.getAmount());
				}
			}
		}*/
		return redTotal;
	}
	
	//统计加息率
	public Map<String, BigDecimal> getMinRateAndMaxRate(UserVipLevel uvl){
		Map<String, BigDecimal> map = new HashMap<>();
		//加息卷规则
		if(StringUtils.isNotBlank(uvl.getRateCouponRuleId())){
			return rateCouponRuleDetailService.collectByRuleId(uvl.getRateCouponRuleId());
		}
		//生日礼包
		/*if (null!=uvl.getBirthdayGiftId() && !uvl.getBirthdayGiftId().equals("")) {
			UserGift birthdayGift=userGiftService.get(uvl.getBirthdayGiftId());
			if (birthdayGift!=null) {
				List<RateCouponRuleDetail> detailList = rateCouponRuleDetailService.getListByRuleId(birthdayGift.getRateCouponRuleId());
				if (detailList != null && detailList.size() > 0) {
					RateCouponRuleDetail birthdayGiftDetail = detailList.get(0);   
					if (birthdayGiftDetail.getUpApr().compareTo(minRate)<0) {
						minRate=birthdayGiftDetail.getUpApr();
					}
					if (birthdayGiftDetail.getUpApr().compareTo(maxRate)>0) {
						maxRate=birthdayGiftDetail.getUpApr();
					}
				}
			}
		}
		//专属礼包
		if (null!=uvl.getExclusiveGiftId() && !uvl.getExclusiveGiftId().equals("")) {
			UserGift exclusiveGift=userGiftService.get(uvl.getExclusiveGiftId());
			if (exclusiveGift!=null && StringUtils.isNotBlank(exclusiveGift.getRateCouponRuleId())) {
				List<RateCouponRuleDetail> detailList = rateCouponRuleDetailService.getListByRuleId(exclusiveGift.getRateCouponRuleId());
				if (detailList != null && detailList.size() > 0) {
					RateCouponRuleDetail exclusiveGiftDetail = detailList.get(0);    
					if (exclusiveGiftDetail.getUpApr().compareTo(minRate)<0) {
						minRate=exclusiveGiftDetail.getUpApr();
					}
					if (exclusiveGiftDetail.getUpApr().compareTo(maxRate)>0) {
						maxRate=exclusiveGiftDetail.getUpApr();
					}
				}
			}
		}*/
		return map;
	}

	@Override
	public List<String> getLevelByStatus(String deleteFlag) {
		return dao.getLevelByStatus(deleteFlag);
	}

	@Override
	public void checkVipUser() {
		if(dao.countVipUser()> 0){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_VIP_COUNT_GT_ZERO));
		}
	}

}