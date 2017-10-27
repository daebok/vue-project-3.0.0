/**
 * 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.tpp.service.tpp;

import com.rd.ifaes.core.tpp.domain.TppTrade;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbFileRepaymentModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxRepaymentModel;

/**
 *  还款异步回调处理
 * @version 3.0
 * @author FangJun
 * @date 2016年10月24日
 */
public interface UfxRepayService {
    /**
     * 还款异步回调处理
     * @param model
     */
    void ufxRepayHandle(UfxRepaymentModel model);

    /**
     * 渤海银行还款回调
     * @author ZhangBiao
     * @date 2017年3月9日
     * @param model
     */
    void cbhbRepayHandle(CbhbFileRepaymentModel model);

    /**
     * 还款成功处理
     * @param trade
     */
    void successRepayHandle(TppTrade trade);

    /**
     * 还垫付款成功
     * @param trade
     */
    void successRepayBailHandle(TppTrade trade);
}
