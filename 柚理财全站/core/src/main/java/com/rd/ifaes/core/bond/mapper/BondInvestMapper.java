package com.rd.ifaes.core.bond.mapper;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.bond.domain.BondInvest;
import com.rd.ifaes.core.bond.model.BondInvestModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 债权投资接口类
 *
 * @author QianPengZhan
 * @version 3.0
 * @date 2016年8月12日
 */
public interface BondInvestMapper extends BaseMapper<BondInvest> {
    /**
     * 更新债权投资记录对应的原始标的投资记录ID
     *
     * @param projectInvestId
     * @param uuid
     * @author QianPengZhan
     * @date 2016年11月3日
     */
    void updateBondInvestProjectInvestId(@Param("projectInvestId") final String projectInvestId, @Param("uuid") final String uuid);

    /**
     * 根据债权标识查询此标的最后一笔投资
     *
     * @param bondId 债权标识
     * @param status 状态
     * @return
     * @author QianPengZhan
     * @date 2016年9月21日
     */
    BondInvest getLastBondInvestByBondId(@Param("bondId") final String bondId, @Param("status") final String status);

    /**
     * 根据ufx订单号查询债权投资
     *
     * @param orderNo
     * @return
     * @author QianPengZhan
     * @date 2016年8月1日
     */
    BondInvest getBondInvestByOrderNo(String orderNo);

    /**
     * 查询modelList
     *
     * @param model
     * @return
     * @author QianPengZhan
     * @date 2016年8月4日
     */
    List<BondInvestModel> findModelList(BondInvestModel model);

    /**
     * 债权投资成功 更新状态
     *
     * @param investId
     * @param newStatus
     * @param oldStatus
     * @author QianPengZhan
     * @date 2016年8月19日
     */
    void updateBondInvstStatus(@Param("investId") String investId,
                               @Param("newStatus") String newStatus, @Param("oldStatus") String oldStatus);

    /**
     * 根据投资日期统计投资订单
     *
     * @param investDate
     * @return
     */
    List<BondInvest> findByInvestDate(@Param("investDate") String investDate, @Param("statusSet") String[] statusSet);

    /**
     * 根据uuid更新investId
     *
     * @param investId
     * @param uuid
     */
    int updateInvestIdByUuid(@Param("investId")String investId, @Param("uuid")String uuid);
}