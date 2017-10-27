package com.rd.ifaes.core.operate.model;

import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.operate.domain.UserVipLevel;

/**
 * 
 * UserVipLevel vip等级设置model
 * 
 * @version 3.0
 * @author wyw
 * @date 2016-8-1
 */
public class UserVipLevelModel extends UserVipLevel {

	/** 序列号 */
	private static final long serialVersionUID = -8230523620690431022L;

	/**
	 * 添加数据有效性检查
	 * 
	 * @author wyw
	 */
	public void checkAdd(final UserVipLevel maxVipLevel) {
		// 检查 userVipLevelModel设置的成长所需值是否 大于 最大等级的 所需成长值
		if (maxVipLevel != null && this.getGrowthValue().doubleValue() <= maxVipLevel.getGrowthValue().doubleValue()) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GROWTHVALUE_MUST_GT_UPLEVEL,String.valueOf(maxVipLevel.getGrowthValue().longValue())));  // 该值为整数值，无需小数点位数
		}
	}
	
}
