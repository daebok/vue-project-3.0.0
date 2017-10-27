/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.home.service;

import java.util.List;
import java.util.Map;

import com.rd.ifaes.core.home.model.SureProfitModel;

/**
 *  首页数据查询--服务接口
 * @version 3.0
 * @author FangJun
 * @date 2016年11月8日
 */
public interface HomeService {
    /**
     * 前台首页统计信息
     * @author  FangJun
     * @date 2016年11月8日
     * @return 统计信息
     */
	Map<String, Object> webCountInfo();
	/**
	 * 首页稳赚系列查询数据
	 * @author  FangJun
	 * @date 2016年11月9日
	 * @return 项目分类、年利率信息
	 */
	 List<SureProfitModel> getSureProfit();
}
