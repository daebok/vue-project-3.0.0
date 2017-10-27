package com.rd.ifaes.core.project.mapper;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.model.MyProjectInvestModel;
import com.rd.ifaes.core.project.model.OutTimeProjectInvest;
import com.rd.ifaes.core.project.model.ProjectInvestModel;
import com.rd.ifaes.core.project.model.ProjectInvestRecord;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dao Interface:ProjectInvestMapper
 *
 * @author FangJun
 * @version 3.0
 * @date 2016-7-13
 */
public interface ProjectInvestMapper extends BaseMapper<ProjectInvest> {
    /**
     * 根据projectInvestId 查询 转让次数 等参数
     * getProjectInvestModel:(这里用一句话描述这个方法的作用). <br/>
     *
     * @param projectInvestId
     * @return
     * @author QianPengZhan
     */
    ProjectInvestModel getProjectInvestModel(@Param("projectInvestId") final String projectInvestId);

    /**
     * 供协议的查询语句
     *
     * @param projectId
     * @return
     * @author QianPengZhan
     * @date 2016年11月4日
     */
    List<ProjectInvest> findSuccessInvestForProtocol(@Param("projectId") final String projectId);

    /**
     * 查询当前债权标的所有的债权投资记录下对应的原始标下的投资记录
     *
     * @return
     * @author QianPengZhan
     * @date 2016年10月23日
     */
    List<ProjectInvest> findListByBondId(@Param("bondId") final String bondId);

    /**
     * 根据父ID和本身ID来查询债权拆分的另一个原始标投资记录
     *
     * @param oriId
     * @param uuid
     * @return
     * @author QianPengZhan
     * @date 2016年9月27日
     */
    ProjectInvest getInvestByOriIdAndUuid(@Param("oriId") final String oriId, @Param("uuid") final String uuid);

    /**
     * 更新债权标识
     *
     * @param bondFlag
     * @param uuid
     * @return
     * @author QianPengZhan
     * @date 2016年8月10日
     */
    int updateBondFlagAndInvestStyle(@Param("investStyle") String investStyle
            , @Param("bondFlag") String bondFlag, @Param("uuid") String uuid);

    /**
     * 根据订单号查询投资记录
     *
     * @param orderNo 订单号
     * @return 对应投资记录
     */
    ProjectInvest getInvestByOrderNo(String orderNo);

    /**
     * 根据订单号查询投资记录
     *
     * @param orderNo  订单号
     * @param bondFlag 债权标识
     * @return 对应投资记录
     * @author QianPengZhan
     * @date 2016年8月16日
     */
    ProjectInvest getInvestByOrderNoAndBondFlag(@Param("invest_order_no") final String invest_order_no, @Param("bondFlag") String bondFlag);


    /**
     * 根据冻结流水号查询投资记录
     *
     * @param freezeNo 冻结流水号
     * @return 对应投资记录
     */
    ProjectInvest getInvestByFreezeNo(String freezeNo);

    /**
     * 根据用户id获取用户当日自动投资总额度
     *
     * @param userId
     * @return
     * @author zb
     * @date 2016年7月27日
     */
    BigDecimal getDayInvestMoney(String userId);

    /**
     * 获取可变现列表
     *
     * @param model
     * @return
     */
    List<ProjectInvestModel> findRealizeAbleList(ProjectInvestModel model);

    /**
     * 获取可转让列表
     *
     * @param model
     * @return
     * @author QianPengZhan
     * @date 2016年7月30日
     */
    List<ProjectInvestModel> findAbleBondList(ProjectInvestModel model);

    /**
     * 查询用户在指定项目上累计投资金额
     *
     * @param model 用户投资信息（投资项目ID，用户ID）
     * @return 累计投资金额
     * @author FangJun
     * @date 2016年8月1日
     */
    BigDecimal countUserInvestProject(ProjectInvestModel model);

    BigDecimal sumInvest(ProjectInvest model);

    /**
     * 前台投资详情页-投资记录 -分页查询
     *
     * @param model 投资记录查询条件
     * @return
     */
    List<ProjectInvestRecord> findListForDetail(ProjectInvestRecord model);

    /**
     * 更新投资记录状态
     *
     * @param uuid      投资记录ID
     * @param status    投资记录当前状态
     * @param preStatus 投资记录前一状态
     * @return 成功 1 ，失败 0
     * @author FangJun
     * @date 2016年8月14日
     */
    int updateStatus(@Param("uuid") String uuid, @Param("status") String status, @Param("preStatus") String preStatus);

    /**
     * 投资订单超时处理
     *
     * @param investUuids 超时订单UUID拼接,如： '882fa2fdb88','ab23d432df2432'
     * @return 更新记录数
     * @author FangJun
     * @date 2016年8月16日
     */
    int investTimeoutHandle(@Param("investUuids") String investUuids, @Param("status") String status, @Param("preStatus") String preStatus);

    /**
     * 查询当前超时投资订单列表
     *
     * @param minutes 超时时间 （分）
     * @return 超时投资订单列表
     * @author FangJun
     * @date 2016年8月16日
     */
    List<OutTimeProjectInvest> getTimeOutInvest(int minutes);

    /**
     * 我的投资
     *
     * @param model
     * @return
     * @author fxl
     * @date 2016年8月17日
     */
    List<MyProjectInvestModel> getMyProjectInvestList(MyProjectInvestModel model);

    /**
     * 更新原始投资记录(变现)
     *
     * @param uuid
     * @author fxl
     * @date 2016年8月24日
     */
    void updateOriginalInvest(@Param("uuid") String uuid, @Param("realizeAmount") BigDecimal realizeAmount,
                              @Param("realizeInterest") BigDecimal realizeInterest, @Param("flag") String flag);

    /**
     * 审核不成立，投资记录状态改为退款中
     *
     * @param projectId
     * @return 修改投资记录条数
     * @author FangJun
     * @date 2016年9月20日
     * @see com.rd.ifaes.common.dict.InvestEnum#STATUS_REFUND
     */
    int refundByProject(@Param("projectId") String projectId);

    /**
     * 查询该项目所有成功的投资记录
     *
     * @param projectId 项目ID
     * @return 成功的投资记录列表
     * @author FangJun
     * @date 2016年8月19日
     */
    List<ProjectInvest> findSuccessInvest(@Param("projectId") String projectId);

    /**
     * 更新变现冻结金额
     *
     * @author fxl
     * @date 2016年12月6日
     */
    void updateRealizeFreeze(@Param("investId") String investId, @Param("freezeCapital") BigDecimal freezeCapital, @Param("freezeInterest") BigDecimal freezeInterest);

    /**
     * 根据投资日期统计投资订单
     *
     * @param investDate
     * @return
     */
    List<ProjectInvest> findByInvestDate(String investDate);

    /**
     * 获取需要结束债权的投资记录
     *
     * @param projectId
     * @return
     */
    List<Map> getNeedCreditEndList(String projectId);

    /**
     * 根据projectId和investStyle查询
     * @param projectId
     * @param investStyle
     * @return
     */
    List<ProjectInvest> findListAfterBond(@Param("projectId")String projectId);
}