package com.rd.ifaes.core.project.service;

import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectCollection;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.domain.ProjectRepayment;
import com.rd.ifaes.core.tpp.domain.TppTrade;
import com.rd.ifaes.core.user.domain.User;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Service Interface:ProjectRepaymentService
 *
 * @author FangJun
 * @version 3.0
 * @date 2016-7-13
 */
public interface ProjectRepaymentService extends BaseService<ProjectRepayment> {

    /**
     * 批量更新待收逾期利息
     *
     * @param list
     * @author QianPengZhan
     * @date 2016年11月25日
     */
    void updateBatchLateInterest(final List<ProjectRepayment> list);

    /**
     * 根据项目ID和状态查询还款列表的总期数 未还期数 已还期数
     *
     * @param projectId
     * @param status
     * @return
     * @author QianPengZhan
     * @date 2016年8月31日
     */
    int getRepayPeriodsByProjectIdAndStatus(final String projectId, final String status);

    /**
     * 查询待还金额
     *
     * @param userId
     * @return
     */
    BigDecimal getWaitRepayAccountTotal(String userId);

    /**
     * 根据用户ID查询下一条还款记录
     *
     * @param userId
     * @return
     */
    ProjectRepayment getNextRepayByUserId(String userId);

    /**
     * 下一次还款金额
     *
     * @return
     */
    BigDecimal getNextRepayAccountByTime(String userId, Date repayTime);

    /**
     * 根据项目ID和期数查找未还待还记录
     *
     * @param projectId 项目ID
     * @param period    还款期数
     * @return 还款记录
     * @author fxl
     * @date 2016年8月1日
     */
    ProjectRepayment findByProjectIdAndPeriod(String projectId, Integer period);

    /**
     * 根据项目Id找到项目待还
     *
     * @param projectId
     * @return
     * @author QianPengZhan
     * @date 2016年8月1日
     */
    ProjectRepayment getRepayByProject(String projectId);

    /**
     * 正常还款
     *
     * @param reaymentId   待还记录ID
     * @param repayerPhone 还款人手机号码
     * @param phoneCode    手机验证码
     * @author FangJun
     * @date 2016年8月2日
     */
    void repay(String reapaymentId, String repayerPhone, String phoneCode, String repayUserId);

    /**
     * 逾期垫付
     *
     * @param reaymentId   待还记录ID
     * @param repayerPhone 还款人手机号码
     * @param phoneCode    手机验证码
     * @author FangJun
     * @date 2016年8月2日
     */
    void overdueRepay(String reapaymentId, String repayerPhone, String phoneCode, String repayUserId);

    /**
     * 根据projectId查询还款计划列表
     *
     * @param projectId
     * @return
     * @author xhf
     * @date 2016年8月2日
     */
    Page<ProjectRepayment> findByProjectId(ProjectRepayment model);

    /**
     * 根据ProjectRepayment查询记录列表
     *
     * @param model
     * @return
     * @author xhf
     * @date 2016年8月2日
     */
    Page<ProjectRepayment> findByDate(ProjectRepayment model);

    /**
     * 更新指定还款记录状态
     *
     * @param uuid      记录ID
     * @param status    状态
     * @param preStatus 上一状态
     * @return 更新条数  1 成功 0 未成功
     * @author FangJun
     * @date 2016年8月2日
     */
    void updateStatus(String uuid, String status, String preStatus);

    /**
     * 确认线下还款
     *
     * @param uuid
     * @author fxl
     * @date 2016年12月20日
     */
    int offlineRepay(String uuid);

    /**
     * 逾期利息处理  定时任务
     *
     * @author wyw
     * @date 2016-8-25
     */
    public void overdueInterestHandle();

    /**
     * 更新逾期信息
     *
     * @param model
     * @return
     * @author wyw
     * @date 2016-8-25
     */
    public int updateOverdueInfo(ProjectRepayment model);

    /**
     * 最后还款日（项目详情页使用）
     *
     * @param projectId
     * @return
     * @author FangJun
     * @date 2016年9月12日
     */
    @Deprecated
    Date getLastRepayDay(final String projectId);

    /**
     * 逾期列表
     *
     * @param model
     * @return
     */
    Page<ProjectRepayment> findOverduePage(ProjectRepayment model);

    /**
     * 还款操作
     *
     * @param reapayment 待还记录
     * @param isOverdue  是否垫付
     * @author FangJun
     * @date 2016年8月9日
     */
    void doRepay(final ProjectRepayment repayment, final boolean isOverdue, final boolean isAuto);

    /**
     * 产品自动还款
     *
     * @author FangJun
     * @date 2016年10月24日
     */
    void autoRepay();

    /**
     * 是否有逾期中的借款
     *
     * @param userId
     * @return
     * @author fxl
     * @date 2016年10月25日
     */
    boolean checkOverDueByUser(String userId);

    /**
     * 填充三方交易记录
     *
     * @param project       借款项目
     * @param invest        投资记录
     * @param repayUser     实际还款人
     * @param money         金额
     * @param accountType   资金类型 collect_capital_interest 本金+利息 +逾期利息  collect_add_interest 加息
     * @param fee           手续费
     * @param isOverdue     是否垫付
     * @param isAddInterest 是否加息
     * @return 三方交易记录
     * @author FangJun
     * @date 2016年8月9日
     */
    TppTrade fillTppTrade(final Project project, final ProjectInvest invest, final User repayUser,
                          final BigDecimal money, final String accountType, final BigDecimal fee, boolean bondFlag,
                          final boolean isOverdue, final ProjectCollection collection);

    /**
     * 后台垫付数据
     *
     * @param ProjectRepayment model
     * @author ywt
     * @date 2016-12-16
     */
    Page<ProjectRepayment> findAdvance(ProjectRepayment model);

    /**
     * 短信催收
     */
    public void repaymentPhoneCode(String id);

    /**
     * 定时提醒3天内的还款记录
     *
     * @param model
     */
    public void repaymentTimeoutHandle();

    /**
     * 查询待还总额
     *
     * @return
     */
    BigDecimal getTotalCollection();

    /**
     * 根据日期查询待还总额
     *
     * @param startDate
     * @param endDate
     * @return
     */
    BigDecimal sumTotalCollectionByDate(String startDate, String endDate);

    /**
     * 借款人还担保垫付款
     * @param uuid
     * @param mobile
     * @param code
     * @param repayUserId
     */
    void repayBail(String uuid, String mobile, String code, String repayUserId);
    
    /**
     * 根据不同的状态查询总额
     *
     */
    Page<ProjectRepayment> findDetail(ProjectRepayment model);
}