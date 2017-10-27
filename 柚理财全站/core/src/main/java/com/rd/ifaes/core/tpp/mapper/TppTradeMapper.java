package com.rd.ifaes.core.tpp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.tpp.domain.TppTrade;
import com.rd.ifaes.core.tpp.model.TppTradeModel;

/**
 * Dao Interface:TppTradeMapper
 *
 * @author FangJun
 * @version 3.0
 * @date 2016-7-18
 */
public interface TppTradeMapper extends BaseMapper<TppTrade> {

    /**
     * 查询待处理的第三方交易记录
     *
     * @param ids
     * @return
     */
    List<TppTrade> findTodoList(String[] ids);

    /**
     * 调度任务查询
     *
     * @param model
     * @return
     */
    List<TppTrade> findRecord(TppTrade model);

    /**
     * 批量更新订单号
     *
     * @param list
     */
    void updateRedoFlagBatch(List<String> ids);

    /**
     * 批量更新三方回调信息
     *
     * @param list
     */
    void updateCallback(List<TppTradeModel> list);

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
    int updateStatus(@Param("uuid") String uuid, @Param("status") String status, @Param("preStatus") String preStatus);

    /**
     * 异步回调处理（状态、异步处理时间更新）
     *
     * @param uuid     记录ID
     * @param status   状态 ( 1成功 2失败)
     * @param respDesc 三方返回信息
     * @return 更新条数  1 成功 0 未成功
     */
    int callbackHandle(@Param("uuid") String uuid, @Param("status") String status, @Param("respDesc") String respDesc);

    /**
     * 根据流水号更新状态
     *
     * @param tradeNo   流水号
     * @param status    状态
     * @param preStatus 更新前的状态
     * @param remark    备注
     */
    int updateStatusByTradeNo(@Param("tradeNo") String tradeNo, @Param("status") String status, @Param("preStatus") String preStatus, @Param("remark") String remark);

    TppTrade findByTradeNoAndType(@Param("tradeNo") String tradeNo, @Param("type") String type, @Param("status") String status);

    List<TppTrade> getListByTradeNoAndType(@Param("tradeNo") String tradeNo, @Param("type") String type, @Param("status") String status);

    int countByTradeNoAndType(@Param("tradeNo") String tradeNo, @Param("type") String type, @Param("status") String status);

    int updateStatusByTradeNoAndType(@Param("tradeNo") String tradeNo, @Param("type") String type, @Param("status") String status);

    int countUnSuccessByTradeNoAndType(@Param("tradeNo") String tradeNo, @Param("type") String type);

    int countByTypeAndProjectId(@Param("type")String type,  @Param("projectId")String projectId);

    int bailRepaySuccessHandle(@Param("uuid")String uuid, @Param("authCode")String authCode);

    List<TppTrade> findListByTypeAndRepaymentId(@Param("type")String type, @Param("repaymentId")String repaymentId);

    int countByTypeAndProjectIdAndStatus(@Param("type")String type,  @Param("projectId")String projectId);
}