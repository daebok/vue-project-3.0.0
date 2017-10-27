/**
 * 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.project.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.domain.Realize;
import com.rd.ifaes.core.project.model.MyProjectInvestModel;
import com.rd.ifaes.core.project.model.ProjectInvestModel;
import com.rd.ifaes.core.project.model.ProjectInvestRecord;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbBackInvestModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxInvestModel;
import com.rd.ifaes.core.user.domain.User;

/**
 * Service Interface:ProjectInvestService
 * @author FangJun
 * @version 3.0
 * @date 2016-7-13
 */
public interface ProjectInvestService extends BaseService<ProjectInvest> {
    /**
     * 根据projectInvestId 查询 转让次数 等参数
     * getProjectInvestModel:(这里用一句话描述这个方法的作用). <br/>
     * @author QianPengZhan
     * @param projectInvestId
     * @return
     */
    ProjectInvestModel getProjectInvestModel(final String projectInvestId);

    /**
     * 查询当前债权标的所有的债权投资记录下对应的原始标下的投资记录
     * @author QianPengZhan
     * @date 2016年10月23日
     * @return
     */
    List<ProjectInvest> findListByBondId(final String bondId);

    /**
     * 手动生成所有的投资记录的协议
     * @author QianPengZhan
     * @date 2016年10月19日
     */
    void handleAllProtocol(final String projectId, final String projectInvestId);

    /**
     * 根据父ID和本身ID来查询债权拆分的另一个原始标投资记录
     * @author QianPengZhan
     * @date 2016年9月27日
     * @param oriId
     * @param uuid
     * @return
     */
    ProjectInvest getInvestByOriIdAndUuid(@Param("oriId") final String oriId, @Param("uuid") final String uuid);

    /**
     * 校验并获取项目投资
     * @author QianPengZhan
     * @date 2016年8月30日
     * @param id
     * @param backUrl校验错误返回地址
     * @return
     */
    ProjectInvest getProjectInvest(String id, final String backUrl);

    /**
     * 更新债权标识
     * @author QianPengZhan
     * @date 2016年8月10日
     * @param bondFlag
     * @param uuid
     * @return
     */
    int updateBondFlagAndInvestStyle(String investStyle, String bondFlag, String uuid);

    /**
     * 根据订单号查询投资记录
     * @param orderNo 订单号
     * @return 对应投资记录
     */
    ProjectInvest getInvestByOrderNo(String orderNo);

    /**
     * 根据订单号查询投资记录
     * @author QianPengZhan
     * @date 2016年8月16日
     * @param orderNo 订单号
     * @param bondFlag 债权标识
     * @return 对应投资记录
     */
    ProjectInvest getInvestByOrderNoAndBondFlag(String orderNo, String bondFlag);

    /**
     * 根据冻结流水号查询投资记录
     * @param freezeNo 冻结流水号
     * @return 对应投资记录
     */
    ProjectInvest getInvestByFreezeNo(String freezeNo);

    /**
     * 根据用户选择项目、投资金额，封装支付请求
     * @param invest 用户投资信息
     * @return
     */
    public Object doInvest(ProjectInvestModel invest);


    /**
     * 投资失败-用户资金解冻处理
     * @author FangJun
     * @date 2016年8月19日
     * @param amount 投资金额
     * @param realAmount 实投金额
     * @param userId  投资用户ID
     * @param orderNo 投资订单号
     */
    public void handleFailInvesterAccount(BigDecimal amount, BigDecimal realAmount, String userId, String orderNo);

    /**
     *  获取可转让列表
     * @author QianPengZhan
     * @date 2016年7月30日
     * @param model
     * @return
     */
    Page<ProjectInvestModel> findAbleBondList(ProjectInvestModel model);

    /**
     *  更新投资变现状态
     * @author fxl
     * @date 2016年7月29日
     * @param realize
     * @param flag
     */
    void updateOriginalInvest(Realize realize, String flag);

    /**
     * 投资记录
     * @param entity
     * @return
     */
    Page<ProjectInvest> findRecordPage(ProjectInvest entity);

    /**
     *  根据投资信息，计算预期利息
     * @author FangJun
     * @date 2016年7月29日
     * @param invest  投资信息（投资项目，投资金额）
     * @return 预期利息
     */
    BigDecimal calcInterest(ProjectInvestModel invest);

    /**
     *  检查项目定向销售信息
     * @author FangJun
     * @date 2016年7月29日
     * @param invest  投资信息（投资项目，投资金额）
     * @return
     */
    Project checkSpecificSale(ProjectInvestModel invest, User user);

    /**
     *  校验定向销售的邮箱校验 或VIP等级校验
     * @author FangJun
     * @date 2016年9月27日
     * @param user 当前用户
     * @param project 投资项目
     * @return 校验错误信息
     */
    public String validMailAndVip(User user, Project project);

    /**
     *
     * 统计字段
     * @author wyw
     * @date 2016-8-9
     * @param model
     * @param column
     * @return
     */
    BigDecimal sumInvest(ProjectInvest model);

    /**
     * 前台投资详情页-投资记录查询
     *
     * @author FangJun
     * @date 2016年8月10日
     * @param entity 投资记录查询条件
     * @return
     */
    Page<ProjectInvestRecord> findPageForDetail(ProjectInvestRecord entity);

    /**
     * 更新投资记录状态
     * @author FangJun
     * @date 2016年8月14日
     * @param uuid 投资记录ID
     * @param status 投资记录当前状态
     * @param preStatus 投资记录前一状态
     * @return 成功 1 ，失败 0
     */
    boolean updateStatus(String uuid, String status, String preStatus);

    /**
     * 投资订单超时处理
     *
     * @author FangJun
     * @date 2016年8月16日
     * @return 更新记录数
     */
    void investTimeoutHandle();

    /**
     * 账户中心-我的投资
     * @author fxl
     * @date 2016年8月17日
     * @param model
     * @return
     */
    Page<MyProjectInvestModel> myProjectInvestList(MyProjectInvestModel model);

    /**
     * 退还指定投资的优惠劵（红包、加息券）
     *
     * @author FangJun
     * @date 2016年8月19日
     * @param investId 投资ID
     */
    public void backVoucher(String investId);

    /**
     * 检查红包、加息券
     *
     * @author FangJun
     * @date 2016年8月17日
     * @param model 投资信息
     * @param project 投资项目
     * @param user 投资用户
     */
    public void checkVoucher(final ProjectInvestModel model, final Project project, final User user);

    /**
     * 投资金额校验
     *
     * @author FangJun
     * @date 2016年7月30日
     * @param project 投资项目
     * @param model 投资信息
     * @param remainAccount 项目可投金额
     */
    public void checkInvestAmount(final Project project, final ProjectInvestModel model, final BigDecimal remainAccount);

    /**
     *  查询用户在指定项目上累计投资金额
     * @author FangJun
     * @date 2016年8月1日
     * @param model 用户投资信息（投资项目ID，用户ID）
     * @return 累计投资金额
     */
    public BigDecimal countUserInvestProject(ProjectInvestModel model);

    /**
     * 去支付
     * @author fxl
     * @date 2016年8月24日
     * @param invest
     * @return
     */
    Object doPay(ProjectInvestModel invest);

    /**
     * 审核不成立，投资记录状态改为退款中
     * @see com.rd.ifaes.common.dict.InvestEnum#STATUS_REFUND
     * @author FangJun
     * @date 2016年9月20日
     * @param projectId 项目ID
     * @return 修改投资记录条数
     */
    int refundByProject(final String projectId);

    /**
     * 校验用户投资再支付信息，返回投资记录
     * @author fxl
     * @date 2016年9月28日
     * @param investModel 投资记录查询信息
     * @return 投资记录
     */
    public ProjectInvest checkPayInvest(final ProjectInvestModel investModel);

    /**
     * 校验：项目、用户可投情况
     *
     * @param project 投资项目
     * @param user 当前投资用户
     * @param amount 投资金额
     */
    void checkOrder(final Project project, final User user, final BigDecimal amount);


    /**
     * 查询该项目所有成功的投资记录
     * @author FangJun
     * @date 2016年8月19日
     * @param projectId 项目ID
     * @return 成功的投资记录列表
     */
    List<ProjectInvest> findSuccessInvest(String projectId);

    /**
     * 撤销项目，解冻未支付订单的本地资金
     * @param projectId 项目UUID
     */
    void cancelProjectHandleUnpay(String projectId);

    /**
     * 更新变现冻结金额
     * @author fxl
     * @date 2016年12月6日
     */
    void updateRealizeFreeze(String investId, BigDecimal freezeCapital, BigDecimal freezeInterest);

    /**
     * 根据投资日期统计投资订单
     * @param investDate
     * @return
     */
    List<ProjectInvest> findByInvestDate(String investDate);

    /**
     * 渤海银行投资处理
     * @author ZhangBiao
     * @date 2017年3月1日
     * @param invest
     */
    CbhbBackInvestModel doCbhbInvest(ProjectInvestModel invest);

    /**
     * 渤海银行 -- 撤销投资
     * TODO 方法说明
     * @author QianPengZhan
     * @date 2017年3月6日
     * @param invest
     * @return
     */
    boolean cbhbInvestCancle(final ProjectInvestModel invest);

    /**
     * 获取需要结束债权的投资记录
     * @param projectId
     * @return userId accountId authCode investId
     */
    List<Map> getNeedCreditEndList(String projectId);

    /**
     * 查询转让之后生成新的记录
     * @param projectId
     * @param investStyle
     * @return
     */
    List<ProjectInvest> findListAfterBond(String projectId);
}