package com.rd.ifaes.core.cache;

import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.ProjectConstant;
import com.rd.ifaes.core.core.util.CacheUtils;

/**
 * 变现缓存处理
 * 
 * @version 3.0
 * @author fxl
 * @date 2016年8月21日
 */
public class RealizeCache {

	private RealizeCache() {
		throw new IllegalAccessError("Utility class");
	}

	/**
	 * 清除指定变现缓存
	 * 
	 * @author fxl
	 * @param uuid
	 * @date 2016年8月29日
	 */
	public static void delRealize(String uuid) {
		CacheUtils.del(CacheConstant.KEY_REALIZE_UUID.concat(uuid));
	}

	/**
	 * 清除指定变现以及列表缓存
	 * 
	 * @author fxl
	 * @param uuid
	 * @date 2016年8月29日
	 */
	public static void delRealizeAndList(String uuid) {
		CacheUtils.del(CacheConstant.KEY_REALIZE_UUID.concat(uuid));
		CacheUtils.batchDel(CacheConstant.KEY_PREFIX_REALIZE_LIST);
		CacheUtils.del(CacheConstant.KEY_REALIZE_INFO.concat(uuid));
	}

	/**
	 * 剩余可投加入缓存
	 * 
	 * @param projectNo
	 * @param amount
	 */
	public static void setRemainAccount(String projectNo, double amount) {
		String projectRemainAccountKey = String.format(ProjectConstant.KEY_PROJECT_REMAIN_ACCOUNT, projectNo);
		CacheUtils.setDouble(projectRemainAccountKey, amount, ExpireTime.NONE);
	}

}
