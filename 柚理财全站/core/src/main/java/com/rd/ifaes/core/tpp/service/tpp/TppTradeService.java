package com.rd.ifaes.core.tpp.service.tpp;

import java.util.List;

import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.core.constant.MqConstant;
import com.rd.ifaes.core.tpp.domain.TppTrade;
import com.rd.ifaes.core.tpp.model.TppTradeModel;

/**
 * Service Interface:TppTradeService
 *
 * @author FangJun
 * @version 3.0
 * @date 2016-7-18
 */
public interface TppTradeService extends BaseService<TppTrade> {

    /**
     * 第三方业务处理
     *
     * @param taskList
     */
    void doTppTask(List<TppTradeModel> taskList);

    /**
     * 发送三方任务
     *
     * @param tppList TPP任务列表
     * @param key     队列KEY ,如下
     * @author FangJun
     * @date 2016年8月9日
     * @see MqConstant#ROUTING_KEY_TRADE
     * @see MqConstant#ROUTING_KEY_TRADE_LOAN
     * @see MqConstant#ROUTING_KEY_TRADE_REPAY
     * @see MqConstant#ROUTING_KEY_TRADE_INVESTFAIL
     */
    void sendTppTrade(final List<TppTrade> tppList, String key);

    /**
     * 第三方业务处理
     *
     * @param ids
     */
    void doTppTask(String[] ids);

    /**
     * 调度任务分页查询
     *
     * @param model
     * @return
     */
    Page<TppTrade> findRecordPage(TppTrade model);

    /**
     * 根据订单号查找记录
     *
     * @param orderNo 订单号
     * @return 任务记录
     * @author FangJun
     * @date 2016年10月25日
     */
    TppTrade findByOrderNo(String orderNo);

    /**
     * 更新状态
     *
     * @param uuid      记录ID
     * @param status    状态
     * @param preStatus 上一状态
     * @return 更新条数  1 成功 0 未成功
     */
    void updateStatus(String uuid, String status, String preStatus);

    /**
     * 异步回调处理（状态、异步处理时间更新）
     *
     * @param uuid     记录ID
     * @param status   状态 ( 1成功 2失败)
     * @param respDesc 三方返回信息
     * @return 更新条数  1 成功 0 未成功
     */
    int callbackHandle(String uuid, String status, String respDesc);

    /**
     * 批量更新三方回调信息
     *
     * @param list
     */
    void updateCallback(List<TppTradeModel> list);

    /**
     * 根据流水号更新状态
     *
     * @param tradeNo   流水号
     * @param status    状态
     * @param preStatus 更新前的状态
     * @param remark    备注
     */
    int updateStatusByTradeNo(String tradeNo, String status, String preStatus, String remark);

    /**
     * 根据流水号和交易类型和状态查找单个
     * @param tradeNo
     * @param type
     * @param status
     * @return
     */
    TppTrade findByTradeNoAndType(String tradeNo, String type, String status);

    /**
     * 根据流水号和交易类型和状态查找列表
     * @param tradeNo
     * @param type
     * @param status
     * @return
     */
    List<TppTrade> getListByTradeNoAndType(String tradeNo, String type, String status);

    /**
     * 根据流水号和交易类型和状态统计个数
     * @param tradeNo
     * @param value
     * @param status
     * @return
     */
    int countByTradeNoAndType(String tradeNo, String type, String status);

    String doJxTppTask(String id);

    int updateStatusByTradeNoAndType(String tradeNo, String type, String status);

    int countUnSuccessByTradeNoAndType(String tradeNo, String type);

    int countByTypeAndProjectId(String type, String projectId);

    int bailRepaySuccessHandle(String uuid, String authCode);

    List<TppTrade> findListByTypeAndRepaymentId(String type, String repaymentId);
    
    int countByTypeAndProjectIdAndStatus(String type, String projectId);
}