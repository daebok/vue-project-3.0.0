package com.rd.ifaes.core.bond.service;


import java.math.BigDecimal;
import java.util.List;

import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.bond.domain.Bond;
import com.rd.ifaes.core.bond.domain.BondInvest;
import com.rd.ifaes.core.bond.model.BondInvestModel;
import com.rd.ifaes.core.bond.model.BondModel;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.tpp.model.jx.JxCreditInvestModel;
import com.rd.ifaes.core.user.domain.User;

/**
 * 债权投资业务处理类
 *
 * @author QianPengZhan
 * @version 3.0
 * @date 2016年8月1日
 */
public interface BondInvestService extends BaseService<BondInvest> {

    /**
     * 根据债权标识查询此标的最后一笔投资
     *
     * @param bondId 债权标识
     * @param status 投资状态
     * @return
     * @author QianPengZhan
     * @date 2016年9月21日
     */
    BondInvest getLastBondInvestByBondId(final String bondId, final String status);

    /**
     * 去支付业务处理
     *
     * @param model
     * @param user
     * @param backUrl
     * @return UfxCreditTransferModel
     * @author QianPengZhan
     * @date 2016年8月24日
     */
    Object doBondPay(BondInvestModel model, User user, final String backUrl);

    /**
     * 债权投资订单超时处理
     *
     * @author QianPengZhan
     * @date 2016年8月21日
     */
    void bondInvestOverTimeHandle();

    /**
     * 查询扩展属性的分页数据
     *
     * @param model
     * @param location
     * @return
     * @author QianPengZhan
     * @date 2016年8月1日
     */
    Page<BondInvestModel> findPageModel(final BondInvestModel model);

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
     * 债权投资模拟方法
     *
     * @param model 投资记录
     * @param user  投资人信息
     * @return
     * @author QianPengZhan
     * @date 2016年7月28日
     */
    Object doBondInvest(final BondInvestModel model, final User user);

    /**
     * 处理债权投资之后的本地资金
     *
     * @param project
     * @param bondUser
     * @param investUser
     * @param invest
     * @param info
     * @author QianPengZhan
     * @date 2016年8月1日
     */
    void handleBondInvestAccount(Project project, User bondUser, User investUser, BondInvest invest,
                                 BigDecimal bondColMoney, boolean fullFlag, ProjectInvest pInvest, final String info);

    /**
     * 受让人生成新的原始标投资记录
     *
     * @param invest
     * @param flag   满标标识
     * @author QianPengZhan
     * @date 2016年8月1日
     */
    BigDecimal createProjectInvestAndProjectCollection(BondInvest invest, JxCreditInvestModel model, boolean flag);

    /**
     * 获取债权标扩展信息
     *
     * @param bond
     * @param project
     * @return
     * @author QianPengZhan
     * @date 2016年8月19日
     */
    BondModel getBondModel(Bond bond, Project project);

    /**
     * 转让人原始标投资记录和原始标待收   作废
     *
     * @param pi       投资记录
     * @param bondUser 转让人
     * @param flag     是否是拆分的时候作废
     * @author QianPengZhan
     * @date 2016年8月9日
     */
    void cancleOriProjectInvestAndProjectCollection(ProjectInvest projectInvest, User bondUser, boolean flag);

    /**
     * 债权投资成功 更新状态
     *
     * @param investId
     * @param newStatus
     * @param oldStatus
     * @author QianPengZhan
     * @date 2016年8月19日
     */
    void updateBondInvstStatus(String investId, String newStatus, String oldStatus);

    /**
     * 风险评估校验
     *
     * @param invest
     * @param user
     * @param project
     * @param backUrl 校验返回地址
     * @author QianPengZhan
     * @date 2016年9月19日
     */
    void validUserRiskTip(final BondInvestModel invest, final User user, final Project project, final String backUrl);

    /**
     * 根据投资日期统计投资订单
     *
     * @param investDate
     * @return
     */
    List<BondInvest> findByInvestDate(String investDate);

    /**
     * 根据uuid更新investId
     *
     * @param investId
     * @param uuid
     */
    int updateInvestIdByUuid(String investId, String uuid);
}