package com.rd.ifaes.core.project.service.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.rd.account.domain.AccountLog;
import com.rd.account.model.AccountBatchModel;
import com.rd.account.model.AccountModel;
import com.rd.account.service.AccountLogService;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.annotation.CacheEvict;
import com.rd.ifaes.common.annotation.Cacheable;
import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.constant.TppServiceEnum;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.dict.DeleteFlagEnum;
import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.dict.RepaymentEnum;
import com.rd.ifaes.common.dict.TppEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.rabbit.RabbitUtils;
import com.rd.ifaes.common.rabbit.model.MqActPlanModel;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.FileUtil;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TppUtil;
import com.rd.ifaes.common.util.resource.LoanResource;
import com.rd.ifaes.core.account.model.TppMerchantLogModel;
import com.rd.ifaes.core.account.service.TppMerchantLogService;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.cache.ProjectCache;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.MessageConstant;
import com.rd.ifaes.core.core.constant.MqConstant;
import com.rd.ifaes.core.core.constant.ProjectConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.constant.URLConstant;
import com.rd.ifaes.core.core.sms.msg.BaseMsg;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.DictUtils;
import com.rd.ifaes.core.core.util.LogTemplateUtils;
import com.rd.ifaes.core.core.util.OrderNoUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.credit.service.UserCreditLineService;
import com.rd.ifaes.core.interest.InterestCalculator;
import com.rd.ifaes.core.operate.domain.UserRateCoupon;
import com.rd.ifaes.core.operate.service.UserRateCouponService;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectCollection;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.domain.ProjectRepayment;
import com.rd.ifaes.core.project.domain.ProjectType;
import com.rd.ifaes.core.project.mapper.ProjectMapper;
import com.rd.ifaes.core.project.model.ProjectRecord;
import com.rd.ifaes.core.project.service.BorrowService;
import com.rd.ifaes.core.project.service.ProductService;
import com.rd.ifaes.core.project.service.ProjectCollectionService;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.project.service.ProjectRepaymentService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.project.service.ProjectTypeService;
import com.rd.ifaes.core.project.service.ProjectVerifyLogService;
import com.rd.ifaes.core.project.worker.ProjectWorker;
import com.rd.ifaes.core.protocol.service.ProtocolService;
import com.rd.ifaes.core.protocol.util.invest.InvestProtocol;
import com.rd.ifaes.core.risk.domain.LevelConfig;
import com.rd.ifaes.core.risk.service.LevelConfigService;
import com.rd.ifaes.core.score.service.UserScoreService;
import com.rd.ifaes.core.sys.service.LogTemplateService;
import com.rd.ifaes.core.tpp.domain.TppTrade;
import com.rd.ifaes.core.tpp.exception.CbhbException;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbBatInvestCancleModel;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbFileReleaseModel;
import com.rd.ifaes.core.tpp.model.cbhb.chk.CbhbChkBaseModel;
import com.rd.ifaes.core.tpp.model.cbhb.file.FileBatInvestCancleDetailModel;
import com.rd.ifaes.core.tpp.model.cbhb.file.FileBatInvestCancleModel;
import com.rd.ifaes.core.tpp.service.TppService;
import com.rd.ifaes.core.tpp.service.tpp.TppTradeService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.domain.UserCompanyInfo;
import com.rd.ifaes.core.user.domain.UserQualificationApply;
import com.rd.ifaes.core.user.model.UserFreezeModel;
import com.rd.ifaes.core.user.model.UserQualificationResultModel;
import com.rd.ifaes.core.user.service.UserBaseInfoService;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserCompanyInfoService;
import com.rd.ifaes.core.user.service.UserFreezeService;
import com.rd.ifaes.core.user.service.UserQualificationApplyService;
import com.rd.ifaes.core.user.service.UserService;

/**
 * ServiceImpl:ProjectServiceImpl
 *
 * @author FangJun
 * @version 3.0
 * @date 2016-7-13
 */
@Service("projectService")
public class ProjectServiceImpl extends BaseServiceImpl<ProjectMapper, Project> implements ProjectService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectServiceImpl.class);

    @Resource
    private TppMerchantLogService tppMerchantLogService;
    @Resource
    private ProtocolService protocolService;
    @Resource
    private ProjectInvestService projectInvestService;
    @Resource
    private UserScoreService userScoreService;
    @Resource
    private UserService userService;
    @Resource
    private UserCacheService userCacheService;
    @Resource
    private TppTradeService tppTradeService;
    @Resource
    private UserBaseInfoService userBaseInfoService;
    @Resource
    private UserQualificationApplyService userQualificationApplyService;
    @Resource
    private AccountService accountService;
    @Resource
    private AccountLogService accountLogService;
    @Resource
    private ProjectCollectionService projectCollectionService;
    @Resource
    private ProjectRepaymentService projectRepaymentService;
    @Resource
    private UserCompanyInfoService userCompanyInfoService;
    @Resource
    private ProjectTypeService projectTypeService;
    @Resource
    private LogTemplateService logTemplateService;
    @Resource
    private UserRateCouponService userRateCouponService;
    @Resource
    private LevelConfigService levelConfigService;
    @Resource
    private ProjectVerifyLogService projectVerifyLogService;
    @Resource
    private BorrowService borrowService;
    @Resource
    private ProductService productService;
    @Resource
    private PlatformTransactionManager transactionManager;
    @Resource
    private transient UserFreezeService userFreezeService;
    @Resource
    private UserCreditLineService userCreditLineService;

    @Override
    //@Cacheable(key = CacheConstant.KEY_PROJECT_UUID, expire = ExpireTime.FIVE_MIN)
    public Project get(String uuid) {
        return super.get(uuid);
    }

    @Override
    public Map<String, Object> getDetail(String projectId, String userId) {
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) CacheUtils.get(CacheConstant.KEY_PREFIX_PROJECT_DETAIL + projectId, Map.class);
        if (data == null) {
            data = doGetDetail(projectId);
            try {
                CacheUtils.set(CacheConstant.KEY_PREFIX_PROJECT_DETAIL + projectId, data, ExpireTime.FIVE_MIN);
            } catch (Exception e) {
                LOGGER.error("project detail cache  error! ");
            }
        }
        //比较时间使用
        data.put("systemTime", System.currentTimeMillis());
        final Object projectObj = data.get("project");
        if (projectObj != null && projectObj instanceof Project) {
            Project project = (Project) projectObj;
            if (userId != null && (LoanEnum.SPECIFIC_SALE_MAIL.eq(project.getSpecificSale())
                    || LoanEnum.SPECIFIC_SALE_VIP_LEVEL.eq(project.getSpecificSale()))) {
                if (LoanEnum.SPECIFIC_SALE_VIP_LEVEL.eq(project.getSpecificSale())) {
                    project.setVipLevel(project.getSpecificSaleRule());
                }
                //登录用户的定向销售邮箱、VIP校验
                final String errorMsg = projectInvestService.validMailAndVip(userService.get(userId), project);
                project.setSpecificSaleRule(errorMsg);
            } else if (LoanEnum.SPECIFIC_SALE_MAIL.eq(project.getSpecificSale())) {
                //未登录，定向邮箱销售提示
                project.setSpecificSaleRule(ResourceUtils.get(ResourceConstant.PROJECT_SALE_MAIL_MSG));
            } else if (LoanEnum.SPECIFIC_SALE_VIP_LEVEL.eq(project.getSpecificSale())) {
                //未登录，定向VIP提示
                project.setSpecificSaleRule(ResourceUtils.get(ResourceConstant.PROJECT_SALE_VIP_LEVEL_MSG, project.getSpecificSaleRule()));
            }

            final Map<String, Object> bespeakInfo = this.getBespeakInfo(projectId, userId);
            data.put("bespeakNum", bespeakInfo.get("bespeak_num"));//本项目预约人数
            data.put("bespeak", bespeakInfo.get("bespeak"));//当前登录用户是否预约本项目
        }
        return data;
    }

    /**
     * 查询项目详情
     *
     * @param projectId 项目UUID
     * @return
     */
    private Map<String, Object> doGetDetail(String projectId) {
        final Project project = dao.getDetail(projectId);
        if (project == null) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_NO_RECORD), BussinessException.TYPE_JSON);
        }
        final Map<String, Object> data = new HashMap<String, Object>();
        if (LoanEnum.SPECIFIC_SALE_PASSWORD.eq(project.getSpecificSale())) {
            project.setSpecificSaleRule(null);
        }
        // 获取可转让状态
        if (LoanEnum.BORROW_FLAG_BORROW.eq(project.getBorrowFlag())) {
            final String bondUseful = borrowService.getBondUsefulByProjectId(project.getUuid());
            project.setBondUseful(bondUseful);
        } else {
            final String realizeUseful = productService.getRealizeUsefulByProjectId(project.getUuid());
            project.setRealizeUseful(realizeUseful);
        }
        data.put("project", project);
        LevelConfig lc = levelConfigService.findByRiskLevel(project.getRiskLevel());
        data.put("risk", lc.getRiskLevelName());//安全等级
        final ProjectType projectType = projectTypeService.get(project.getProjectTypeId());
        if (projectType == null) {
            project.setProjectTypeName(ProjectConstant.DEFAULT_PROJECT_TYPE_NAME);
        } else if (ProjectType.TYPE_LEVEL_MAX == projectType.getTypeLevel()) {
            //详情页返回理财频道标签页，使用的二级项目类别 #19261
            data.put("tabProjectTypeId", projectType.getParentId());
        } else {
            data.put("tabProjectTypeId", project.getProjectTypeId());
        }
        data.put("investCondition", lc.getUserRiskLevelName());//投资条件
        if (project.getTotalPeriod() != null && project.getRepayedPeriod() != null) {
            data.put("remainPeriod", project.getTotalPeriod() - project.getRepayedPeriod());
        } else {
            data.put("remainPeriod", project.getTotalPeriod());
        }
        data.put("nextRepayDay", project.getNextRepayTime());
        data.put("lastRepayTime", project.getLastRepayTime());
        return data;
    }

    @Override
    public String nextProjectNo(String createDate) {
        return ProjectCache.getNextProjectNo(createDate);
    }

    @Override
    public void updateStatus(String uuid, String status, String preStatus) {
        int result = dao.updateStatus(uuid, status, preStatus);
        if (result == Constant.INT_ZERO) {
            String msg = "项目状态更新失败 [uuid=" + uuid + ", status=" + status + ", preStatus=" + preStatus + "]";
            LOGGER.error(msg);
            throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_STATUS_ERROR));
        }
    }

    @Override
    public void deleteBatch(String[] ids) {
        int notEditableCount = getNotEditableCount(ids);
        if (notEditableCount > Constant.INT_ZERO) {
            String msg = ResourceUtils.get(ResourceConstant.GLOBAL_STATUS_ERROR);
            LOGGER.error(msg);
            throw new BussinessException(msg);
        }
        super.deleteBatch(ids);
    }

    @Override
    public int getNotEditableCount(String[] ids) {
        // 0 3 12 的不能编辑
        String[] status = {LoanEnum.STATUS_NEW.getValue(),
                LoanEnum.STATUS_PUBLISH_FAIL.getValue(),
                LoanEnum.STATUS_VOUCH_VERIFY_FAIL.getValue()};
        return dao.getNotEditableCount(ids, status);
    }

    /**
     * 根据项目编号
     *
     * @param projectNo 项目编号
     * @return 指定编号的项目
     */
    public Project getProjectByNo(String projectNo) {
        return dao.getProjectByNo(projectNo);
    }

    @Override
    @CacheEvict(keys = {CacheConstant.KEY_PREFIX_PROJECT_LIST, CacheConstant.KEY_PROJECT_UUID})
    public void updateAccountYes(double addAmount, String uuid) {
        int result = dao.updateAccountYes(addAmount, uuid);
        if (result == Constant.INT_ZERO) {
            String msg = "项目已投金额更新失败 [uuid=" + uuid + ", amount=" + addAmount + "]";
            LOGGER.error(msg);
            throw new BussinessException(msg);
        }
    }

    @Override
    @CacheEvict(keys = CacheConstant.KEY_PROJECT_UUID)
    public void cancel(final String uuid) {
        final List<TppTrade> tppList = new ArrayList<TppTrade>();
        LOGGER.info("项目(({}))审核不成立  BEGIN..........", uuid);

        //渤海银行 -- 参数需要
        String tppName = ConfigUtils.getTppName();
        Map<String, Object> cbhbMap = new HashMap<String, Object>();
        Project project = dao.get(uuid);

        // 手动控制提交事务，避免关闭队列，同步发送报文太久，TPP未提交，异步回调先到，查询不到TPP
        TransactionDefinition definition = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(definition);
        try {
            // 保存成立审核时间
            Project projectTemp = new Project();
            projectTemp.setUuid(uuid);
            projectTemp.setReviewTime(DateUtils.getNow());
            projectTemp.setStage(LoanEnum.STAGE_REPAY.getInt());
            this.update(projectTemp);

            // 1 更新项目状态
            updateStatus(uuid, LoanEnum.STATUS_CANCELLING.getValue(), LoanEnum.STATUS_RAISE_FINISHED.getValue());
            // 2 成功投资的发送投资失败报文到三方，回调处理（退回投资用户资金、添加资金日志、退回优惠劵）
            final List<ProjectInvest> investList = projectInvestService.findSuccessInvest(uuid);
            if (CollectionUtils.isEmpty(investList)) {
                LOGGER.info("项目({})审核不成立, 无成功投资记录", uuid);
            } else {
                String[][] detailList = new String[investList.size()][];
                // 生成对应投资退款申请（investFail）
                for (int j = 0; j < investList.size(); j++) {
                    ProjectInvest invest = investList.get(j);
                    if (StringUtils.isBlank(invest.getFreezeNo())) {
                        throw new BussinessException(
                                ResourceUtils.get(LoanResource.INVEST_FREEZE_NO_EMPTY_ERROR, invest.getUuid()));
                    }
                    User investUser = userService.get(invest.getUserId());
                    if (TppServiceEnum.CBHB.getName().equals(tppName)) {
                        //撤销的明细
                        String[] detail = new String[5];
                        detail[0] = invest.getInvestOrderNo().substring(invest.getInvestOrderNo().length() - 8, invest.getInvestOrderNo().length());
                        detail[1] = invest.getInvestNo();
                        detail[2] = investUser.getTppUserCustId();
                        detail[3] = invest.getAmount().toString();
                        detail[4] = invest.getFreezeNo();
                        detailList[j] = detail;
                    } else if (TppServiceEnum.CHINAPNR.getName().equals(tppName)||TppServiceEnum.JXBANK.getName().equals(tppName)) {
                        TppTrade tpp = new TppTrade(TppEnum.SERVICE_TYPE_PROJECT_CANCEL.getValue(),
                                TppEnum.TPP_TYPE_INVEST_FAIL.getValue(), null, null, invest.getUserId(),
                                investUser.getTppUserCustId(), invest.getAmount(), invest.getUuid());
                        tpp.setProjectId(invest.getProjectId());
                        tpp.setPreTradeId(invest.getFreezeNo());
                        tppList.add(tpp);
                    }
                } // for loop end
                if (TppServiceEnum.CBHB.getName().equals(tppName)) {
                    cbhbMap.put("detailList", detailList);
                    TppTrade tpp = new TppTrade(TppEnum.SERVICE_TYPE_PROJECT_CANCEL.getValue(),
                            TppEnum.TPP_TYPE_INVEST_FAIL.getValue(), null, null, null,
                            null, null, null);
                    tpp.setProjectId(project.getProjectNo());
                    tpp.setInvestProjectId(project.getUuid());
                    tpp.setOrderNo(project.getProjectNo());
                    tppList.add(tpp);
                }
                // 修改投资记录状态为：退款处理中
                projectInvestService.refundByProject(uuid);
                // TPP入库
                tppTradeService.insertBatch(tppList);
            }
            if (TppServiceEnum.CHINAPNR.getName().equals(tppName) || TppServiceEnum.JXBANK.getName().equals(tppName)) {
                // 未支付成功用户(退回投资用户资金、添加资金日志、退回优惠劵)
                projectInvestService.cancelProjectHandleUnpay(uuid);
            } else if (TppServiceEnum.CBHB.getName().equals(tppName)) {
                LOGGER.info("渤海银行--批量投资撤销申请中,异步回调处理本地资金");
            }

            // 更新项目状态
            updateStatus(uuid, LoanEnum.STATUS_NOT_ESTABLISH.getValue(), LoanEnum.STATUS_CANCELLING.getValue());
			if(LoanEnum.PROJECT_TYPE_CREDIT.getValue().equals(project.getProjectType())){
				//信用标，返还用户的信用额度
				userCreditLineService.addCreditByAccount(project.getUserId(), project.getAccount(), project.getProjectName() + "-" + project.getProjectNo());
			}
            
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }

        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("user", userService.get(project.getUserId()));
            params.put("projectName", project.getProjectName().length() > 10 ? project.getProjectName().substring(0, 10) + "..." : project.getProjectName());
            BaseMsg baseMsg = new BaseMsg(MessageConstant.PROJECT_FULL_FAIL, params);
            baseMsg.doEvent();
        } catch (Exception e) {
            LOGGER.error("给借款人发送项目成立通知处理失败", e);
        }
        if (TppServiceEnum.CHINAPNR.getName().equals(tppName)||TppServiceEnum.JXBANK.getName().equals(tppName)) {
            tppTradeService.sendTppTrade(tppList, MqConstant.ROUTING_KEY_TRADE_INVESTFAIL);
        } else if (TppServiceEnum.CBHB.getName().equals(tppName)) {
            TppService tppService = (TppService) TppUtil.tppService();
            CbhbBatInvestCancleModel responseModel = (CbhbBatInvestCancleModel) tppService.tppBatInvestCancle(cbhbMap);
            if (CbhbConstant.CBHB_CODE_SUCCESS.equals(responseModel.getRespCode())) {//实时回调保存三方流水号，异步回调用
                TppTrade trade = tppTradeService.findByOrderNo(project.getProjectNo());//根据临时订单号来查
                trade.setOrderNo(responseModel.getMerBillNo());
                cbhbMap.remove("detailList");
                trade.setParams(cbhbMap.toString());
                tppTradeService.update(trade);
            }
        }
        LOGGER.info("项目({})审核不成立,退款处理完毕............", uuid);
    }


    @Override
    @CacheEvict(keys = CacheConstant.KEY_PROJECT_UUID)
    public void verifySuccess(final String uuid) {
        //1--本地业务处理   获取相关的信息（项目信息、借款人信息）  简单校验
        Project project = dao.get(uuid);//获取项目信息
        Date now = DateUtils.getNow();//获取当前时间
        project.setReviewTime(now);
        ProjectWorker projectWorker = new ProjectWorker(project);
        User projectUser = userService.get(project.getUserId());//获取借款人用户信息
        if (LoanEnum.STATUS_ESTABLISH.eq(project.getStatus())) {//检查项目状态
            LOGGER.error("项目({})在成立处理中，请勿重复处理!", uuid);
            return;
        }
        //2--初始化事务  手动控制提交事务，避免关闭队列，同步发送报文太久，TPP未提交，异步回调先到，查询不到TPP
        TransactionDefinition definition = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(definition);

        //3--根据不同的第三方处理不同的业务
        final String tppName = ConfigUtils.getTppName();
        //三方还款任务
        final List<TppTrade> tppList = new ArrayList<TppTrade>();
        List<ProjectCollection> pclist = null;

        //渤海放款接口所需参数
        Map<String, Object> tppMap = new HashMap<String, Object>();
        if (TppServiceEnum.CBHB.getName().equals(tppName)) {
            TppTrade fileReleaseTrade = new TppTrade(TppEnum.SERVICE_TYPE_LOAN.getValue(), TppEnum.TPP_TYPE_LOAN.getValue(), null, null,
                    project.getUserId(), projectUser.getTppUserCustId(), project.getAccountYes(), null);
            fileReleaseTrade.setProjectId(project.getProjectNo());// 项目
            fileReleaseTrade.setInvestProjectId(project.getUuid());
            fileReleaseTrade.setOrderNo(project.getProjectNo());
            tppList.add(fileReleaseTrade);
            tppMap.put("borrowId", project.getProjectNo());// 录入标的ID
            tppMap.put("borrCustId", projectUser.getTppUserCustId());//借款人的存管账号
        }
        try {
            // 截标处理
            truncProject(project);
            // 更新项目投资阶段
            LOGGER.info("updateStage >>:成立审核");
            updateStage(LoanEnum.STAGE_REPAY.getInt(), project.getProjectNo());
            // 项目成立状态
            this.updateStatus(uuid, LoanEnum.STATUS_ESTABLISH.getValue(), LoanEnum.STATUS_RAISE_FINISHED.getValue());
            // 2 根据投资列表， 计算加息（平台加息、加息券、VIP加息）， 生成待收记录，更新投资记录
            pclist = createProjectCollection(project, projectWorker, projectUser, tppList, tppMap);
            // 3 生成 待还记录
            createProjectRepayment(project, projectWorker, pclist);
            // 4 修改状态为还款中，记录审核时间
            setRepayStartState(uuid);
            // 5 TPP入库
            tppTradeService.insertBatch(tppList);

            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("user", projectUser);
            params.put("projectName", project.getProjectName().length() > 10 ? project.getProjectName().substring(0, 10) + "..." : project.getProjectName());
            BaseMsg baseMsg = new BaseMsg(MessageConstant.PROJECT_FULL_SUCC, params);
            baseMsg.doEvent();
        } catch (Exception e) {
            LOGGER.error("给借款人发送项目成立通知处理失败，projectId:{}", uuid, e);
        }

        // 5 发送第三方 --放款
        if (TppServiceEnum.CHINAPNR.getName().equals(tppName)) {
            tppTradeService.sendTppTrade(tppList, MqConstant.ROUTING_KEY_TRADE_LOAN);
        } else if (TppServiceEnum.CBHB.getName().equals(tppName)) {
            TppService tppService = (TppService) TppUtil.tppService();
            CbhbFileReleaseModel responseModel = (CbhbFileReleaseModel) tppService.tppLoan(tppMap);
            TppTrade trade = tppTradeService.findByOrderNo(project.getProjectNo());//根据临时订单号来查
            trade.setServFee(BigDecimalUtils.valueOf(StringUtils.isNull(tppMap.get("feeAmt")))); // 借款管理费
            if (responseModel != null) {
                trade.setOrderNo(responseModel.getMerBillNo());
            }
            tppMap.remove("detailList");
            trade.setParams(tppMap.toString());
            tppTradeService.update(trade);
        } else if (TppServiceEnum.JXBANK.getName().equals(tppName)) {
            TppService tppService = (TppService) TppUtil.tppService();
            //noinspection unchecked
            Map<String, Object> map = (Map<String, Object>) tppService.tppLoan(tppMap);
            String received = StringUtils.isNull(map.get("received"));
            if (!"success".equals(received)) {
                String tradeNo = (String) tppMap.get("tradeNo");
                tppTradeService.updateStatusByTradeNo(tradeNo, TppEnum.STATUS_FAIL.getValue(), TppEnum.STATUS_UNDO.getValue(), "存管方接收失败！");
            }
        }
        LOGGER.info("项目({})审核成立,处理完毕............", uuid);
    }

    /**
     * 即息理财-单笔投资放款
     *
     * @param invest
     */
    public void handleInvestLoan(ProjectInvest invest) {
        LOGGER.debug("即息理财投资放款----begin---- invest UUID:{},orderId:{}", invest.getUuid(), invest.getInvestOrderNo());
        final Project project = dao.get(invest.getProjectId());
        final ProjectWorker projectWorker = new ProjectWorker(project);
        // 三方还款任务
        final List<TppTrade> tppList = new ArrayList<TppTrade>();
        // 手动控制提交事务，避免关闭队列，同步发送报文太久，TPP未提交，异步回调先到，查询不到TPP
        TransactionDefinition definition = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(definition);
        try {
            final List<ProjectCollection> pclist = new ArrayList<ProjectCollection>();
            //生成待收记录
            this.handleInvestCollection(project, projectWorker, pclist, invest);
            projectCollectionService.insertBatch(pclist);
            // 借款人
            final User projectUser = userService.get(project.getUserId());
            final User investUser = userService.get(invest.getUserId());
            // 封装第三方放款参数(产品-即息 不收借款管理费)
            TppTrade tpp = this.fillTppTrade(investUser, project, projectUser, invest.getAmount(), invest, invest.getRealAmount(), BigDecimal.ZERO);
            // 生成 待还记录
            this.createProjectRepayment(project, projectWorker, pclist);
            tppList.add(tpp);
            // TPP入库
            tppTradeService.insert(tpp);

            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
        //  发送第三方 --放款
        tppTradeService.sendTppTrade(tppList, MqConstant.ROUTING_KEY_TRADE_LOAN);

        LOGGER.info("即息理财投资放款----end---- invest UUID:{},orderId:{}", invest.getUuid(), invest.getInvestOrderNo());
    }

    /**
     * 修改状态为还款中，记录审核时间
     *
     * @param uuid 指定项目ID
     * @author FangJun
     * @date 2016年7月27日
     */
    private void setRepayStartState(String uuid) {
        this.updateStatus(uuid, LoanEnum.STATUS_REPAY_START.getValue(), LoanEnum.STATUS_ESTABLISH.getValue());
        Project projectTemp = new Project();
        projectTemp.setUuid(uuid);
        projectTemp.setReviewTime(DateUtils.getNow());
        dao.update(projectTemp);
        CacheUtils.del(CacheConstant.KEY_PROJECT_UUID_PREFIX + uuid);
    }

    /**
     * 更新已还金额
     *
     * @param uuid
     * @author fxl
     * @date 2016年8月23日
     */
    @Override
    public void updateRepayAmount(Project project, BigDecimal repaymentAmount, BigDecimal repaymentInterest) {
        Project projectTemp = new Project();
        projectTemp.setUuid(project.getUuid());
        projectTemp.setRepayYesAccount(BigDecimalUtils.add(project.getRepayYesAccount(), repaymentAmount));
        projectTemp.setRepayYesInterest(BigDecimalUtils.add(project.getRepayYesInterest(), repaymentInterest));
        dao.update(projectTemp);
    }

    /**
     * 借款人入账本地资金处理、生成 待还记录
     *
     * @param project       指定项目
     * @param projectWorker 项目工具类
     * @author FangJun
     * @date 2016年7月27日
     */
    private void createProjectRepayment(final Project project, final ProjectWorker projectWorker, final List<ProjectCollection> pcList) {
        LOGGER.info("项目({})生成待还记录--BEGIN--", project.getUuid());
        List<ProjectRepayment> repayList = projectWorker.createRepaymentList(pcList);
        if (CollectionUtils.isEmpty(repayList)) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_REPAYMENT_CREATE_FAIL, project.getProjectName()));
        } else {
            //设置项目的总待还金额，初始化已还本金、已还利息
            BigDecimal repayTotal = BigDecimal.ZERO;
            for (ProjectRepayment repayment : repayList) {
                repayTotal = BigDecimalUtils.add(repayTotal, repayment.getCapital(), repayment.getInterest());
            }
            final Project projectTemp = new Project();
            projectTemp.setUuid(project.getUuid());
            projectTemp.setTotalPeriod(repayList.size());
            projectTemp.setRepayAmount(repayTotal);
            projectTemp.setNextRepayTime(repayList.get(Constant.INT_ZERO).getRepayTime());
            projectTemp.setLastRepayTime(repayList.get(repayList.size() - Constant.INT_ONE).getRepayTime());
            this.update(projectTemp);
        }
        //待还期数一般不超过100，一次批量足够
        projectRepaymentService.insertBatch(repayList);

        // TODO 借款管理费处理（汇付 借款管理费 不需要额外调用接口）

        LOGGER.info("项目({})生成待还记录--END--", project.getUuid());
    }

    /**
     * 生成待收记录
     *
     * @param project       指定项目
     * @param projectWorker 项目工具类
     * @param projectUser   借款人
     * @param taskList      放款任务列表
     * @author FangJun
     * @date 2016年7月27日
     */
    private List<ProjectCollection> createProjectCollection(final Project project, final ProjectWorker projectWorker, final User projectUser,
                                                            final List<TppTrade> tppList, final Map<String, Object> params) {
        final List<ProjectInvest> investList = projectInvestService.findSuccessInvest(project.getUuid());
        final String tppName = ConfigUtils.getTppName();
        if (CollectionUtils.isEmpty(investList)) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_HAS_NOTSUCCESSINVEST, project.getProjectName()));
        }
        // 产品没有有借款管理费
        BigDecimal borrowManageFee = BigDecimal.ZERO;
        if (LoanEnum.BORROW_FLAG_BORROW.eq(project.getBorrowFlag())) {
            borrowManageFee = BigDecimalUtils.round(project.getAccount().multiply(project.getBorrowManageRate()).divide(BigDecimal.valueOf(100.00)));
        }
        // 2.2 生成对应放款任务数据TPP
        BigDecimal transAmt = BigDecimal.ZERO; // 实际投资金额
        BigDecimal manageFeeTotal = BigDecimal.ZERO; // 存放累计借款管理费
        final List<ProjectCollection> allCollectionList = new ArrayList<ProjectCollection>();
        String[][] detailList = new String[investList.size()][];//初始化渤海银行放款所需数组
        //江西银行
        BigDecimal txAmount = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
        int txCounts = 0;
        List<Map> subPacks = new ArrayList<>();
        Date now = new Date();
        String batchNo = OrderNoUtils.getRandomStr(6);
        String tradeNo = DateUtils.dateStr7(now).concat(batchNo);
        for (int i = 0; i < investList.size(); i++) {
            final List<ProjectCollection> collectionList = new ArrayList<ProjectCollection>();
            ProjectInvest invest = investList.get(i);//投资记录
            final User investUser = userService.get(invest.getUserId());//投资人
            final BigDecimal amount = invest.getAmount();//投资金额
            final BigDecimal realAmount = invest.getRealAmount();//实际投资金额

            //1-- 渤海银行所需数组录入
            if (TppServiceEnum.CBHB.getName().equals(tppName)) {
                String[] detail = new String[4];
                detail[0] = invest.getInvestOrderNo().substring(invest.getInvestOrderNo().length() - 8, invest.getInvestOrderNo().length());
                detail[1] = investUser.getTppUserCustId();
                detail[2] = amount.toString();
                LOGGER.info("放款的realAmount:{}", detail[2]);
                detail[3] = invest.getFreezeNo();
                detailList[i] = detail;
                transAmt = BigDecimalUtils.add(transAmt, amount);
            }
            //2--处理单个投资的待收
            this.handleInvestCollection(project, projectWorker, collectionList, invest);

            //3--计算借款管理费
            BigDecimal manageFee = BigDecimal.ZERO;
            if (LoanEnum.BORROW_FLAG_BORROW.eq(project.getBorrowFlag())
                    && manageFeeTotal.doubleValue() < borrowManageFee.doubleValue()) {
                //每笔投资的借款管理费
                manageFee = BigDecimalUtils.round(amount.multiply(project.getBorrowManageRate()).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP));
                LOGGER.info("计算借款管理费，第{}笔{}元对应管理费金额{}，累计金额：{} ", i + 1, amount, manageFee, manageFeeTotal);
                // 如果超过预计总额，做减法运算
                if (manageFeeTotal.add(manageFee).doubleValue() > borrowManageFee.doubleValue()) {
                    manageFee = BigDecimalUtils.round(borrowManageFee.subtract(manageFeeTotal));
                    LOGGER.info("计算借款管理费，中间第{}笔核对，累计金额：{} ", i + 1, manageFeeTotal);
                } else if (i == investList.size() - 1 && manageFeeTotal.add(manageFee).doubleValue() < borrowManageFee.doubleValue()) {
                    //最后一笔做核对
                    manageFee = BigDecimalUtils.round(borrowManageFee.subtract(manageFeeTotal));
                    LOGGER.info("计算借款管理费，最后笔核对，差额：{} ", manageFee);
                }
            }
            
            /************************************projectcollection借款管理费计算start**************************************/
            LOGGER.info("-----projectcollection借款管理费计算start-------invest：{} ", invest.getUuid());
            BigDecimal collectionManageFee = BigDecimal.ZERO;//每个投资人每期对应借款人的管理费
            BigDecimal collectionManageFeeTotal = BigDecimal.ZERO;//每个投资人每期对应借款人的管理费累加
            int j = 0;
            for (ProjectCollection projectCollection : collectionList) {
            	//每期计算
            	collectionManageFee = BigDecimalUtils.round(manageFee.divide(BigDecimal.valueOf(collectionList.size()), 2, BigDecimal.ROUND_HALF_UP));
				LOGGER.info("-----借款管理费计算-------collectionManageFee:{},manageFee:{},capital:{},amount:{} ", collectionManageFee,
						manageFee, projectCollection.getCapital(), invest.getAmount());
				if (collectionManageFeeTotal.add(collectionManageFee).doubleValue() > manageFee.doubleValue()) {
            		collectionManageFee = BigDecimalUtils.round(manageFee.subtract(collectionManageFeeTotal));
                    LOGGER.info("计算每个投资人每期借款管理费，中间第{}笔核对，累计金额：{} ", i + 1, collectionManageFeeTotal);
                } 
            	if (j == collectionList.size() - 1 && collectionManageFeeTotal.add(collectionManageFee).doubleValue() < manageFee.doubleValue()) {
            		collectionManageFee = BigDecimalUtils.round(manageFee.subtract(collectionManageFeeTotal));
            		LOGGER.info("计算每个投资人每期借款管理费，最后笔核对，差额：{} ", collectionManageFee);
            	}
            	projectCollection.setBorrowManageFee(collectionManageFee);
            	collectionManageFeeTotal = BigDecimalUtils.round(collectionManageFeeTotal.add(collectionManageFee));
            	j++;
            }
            LOGGER.info("-----projectcollection借款管理费计算end-------invest：{} ", invest.getUuid());
            /************************************projectcollection借款管理费计算end***********************************************/
            
            
            manageFeeTotal = manageFeeTotal.add(manageFee);

            //4-- 封装第三方放款参数
            if (TppServiceEnum.CHINAPNR.getName().equals(tppName)) {
                tppList.add(fillTppTrade(investUser, project, projectUser, amount, invest, realAmount, manageFee));
            }
            //TODO 5 --代收金放款（红包、汇付收取代金劵不需要额外调用接口）

            //江西银行
            if (TppServiceEnum.JXBANK.getName().equals(tppName)) {
                TppTrade tpp = fillTppTrade(investUser, project, projectUser, amount, invest, realAmount, manageFee);
                tpp.setTradeNo(tradeNo);
                tppList.add(tpp);
                Map<String, Object> map = new HashMap<>();
                map.put("accountId", investUser.getTppUserAccId());
                map.put("orderId", tpp.getOrderNo());
                BigDecimal money = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
                map.put("txAmount", money);
//                if (manageFee.compareTo(BigDecimal.ZERO) > 0) {
//                    map.put("debtFee", manageFee.setScale(2, BigDecimal.ROUND_HALF_UP));
//                }
                map.put("forAccountId", projectUser.getTppUserAccId());
                map.put("productId", project.getUuid());
                map.put("authCode", invest.getAuthCode());
                subPacks.add(map);
                txAmount = txAmount.add(money);
                txCounts++;
            }
            allCollectionList.addAll(collectionList);
        }// loop end!

        //6-- 批量提交
        projectCollectionService.insertBatch(allCollectionList);

        //7-- 录入cbhb所需信息
        if (TppServiceEnum.CBHB.getName().equals(tppName)) {
            params.put("feeAmt", manageFeeTotal);//借款管理费
            params.put("detailList", detailList);
            params.put("transAmt", transAmt);// 实际到账金额
        }
        //江西银行
        if (TppServiceEnum.JXBANK.getName().equals(tppName)) {
            params.put("txAmount", txAmount);
            params.put("txCounts", txCounts);
            params.put("subPacks", subPacks);
            params.put("batchNo", batchNo);
            params.put("tradeNo", tradeNo);
            //保存批量放款记录
            TppTrade tpp = new TppTrade();
            tpp.setServiceType(TppEnum.SERVICE_TYPE_BATCH_LOAN.getValue());
            tpp.setTppType(TppEnum.TPP_TYPE_BATCH_LOAN.getValue());
            tpp.setToUserId(projectUser.getUuid());
            tpp.setToTppUserCustId(projectUser.getTppUserCustId());
            tpp.setMoney(txAmount);
            tpp.setCreateTime(now);
            tpp.setStatus(TppEnum.STATUS_UNDO.getValue());
            tpp.setTradeNo(tradeNo);
            tpp.setTradeDate(now);
            tpp.setProjectId(project.getUuid());
            tpp.setInvestProjectId(project.getUuid());
            tpp.setServFee(manageFeeTotal);
            tppList.add(tpp);
        }
        LOGGER.info("项目({})生成待收记录--END", project.getUuid());
        return allCollectionList;
    }

    /**
     * 处理单个投资的待收
     *
     * @param project           项目
     * @param projectWorker     项目工具类
     * @param allCollectionList 待收收集列表
     * @param invest            投资记录
     */
    @Override
    public void handleInvestCollection(final Project project, final ProjectWorker projectWorker,
                                       final List<ProjectCollection> allCollectionList, ProjectInvest invest) {
        final BigDecimal amount = invest.getAmount();
        if (StringUtils.isBlank(invest.getFreezeNo())) {
            throw new BussinessException("投资记录(" + invest.getUuid() + ")冻结流水号为空!");
        }
        // 根据投资记录生成待收记录
        BigDecimal addApr = BigDecimal.ZERO;
        if (BigDecimalUtils.validAmount(project.getAddApr())) {
            addApr = project.getAddApr();
        } else {
            UserRateCoupon userRateCoupon = userRateCouponService.findByInvestId(invest.getUuid());
            if (userRateCoupon != null) {
                addApr = userRateCoupon.getUpApr();
            }
        }
        final InterestCalculator ic = projectWorker.interestCalculator(amount, project.getApr(), addApr, invest.getCreateTime());

        // 判断是否执行发放首投奖励
        if (ConfigUtils.firstInvestRewardInVerify()) {
            // 用户首次投资逻辑处理
            UserCache userCache = userCacheService.findByUserId(invest.getUserId());
            if (!CommonEnum.YES.eq(StringUtils.isNull(userCache.getFirstAwardFlag()))
                    && userCacheService.userFirstInvest(invest.getUserId()) > 0) {
                invest.setUserFirstInvest(CommonEnum.YES.getValue());
                LOGGER.info("进入首投,userId = {}", invest.getUserId());
                //首投活动入队列
                MqActPlanModel actModel = new MqActPlanModel(MqConstant.OPERATE_ACTPLAN_INVEST_FIRST, userService.get(invest.getUserId()), invest, null);
                RabbitUtils.actPlan(actModel);
            }
        }
        //投资活动入队列
        MqActPlanModel actModel = new MqActPlanModel(MqConstant.OPERATE_ACTPLAN_INVEST, userService.get(invest.getUserId()), invest, null);
        RabbitUtils.actPlan(actModel);
        invest.setUserFirstInvest(CommonEnum.NO.getValue());
        // 设置投资记录预还本息、利息
        invest.setPayment(BigDecimalUtils.add(amount, ic.collectInterest()));
        invest.setInterest(ic.repayInterest());
        invest.setRaiseInterest(ic.raiseInterest());//加息利息
        invest.setWaitInterest(ic.collectInterest());
        invest.setWaitAmount(invest.getPayment());
        invest.setWaitRaiseInterest(ic.raiseInterest());
        invest.setInterestDate(projectWorker.getInterestTime(invest.getCreateTime()));// 起息日
        projectInvestService.update(invest);

        //清理缓存
        CacheUtils.del(CacheConstant.KEY_PREFIX_USER_CACHE_USER_ID + invest.getUserId());

        // 单个投资记录的待收
        List<ProjectCollection> singleInvestCollectionList = projectWorker.createCollectionList(invest, ic);
        allCollectionList.addAll(singleInvestCollectionList);
    }

    @Override
    public TppTrade fillTppTrade(User investUser, Project project, User projectUser, BigDecimal amount, ProjectInvest invest,
                                 BigDecimal realAmount, BigDecimal manageFee) {
        // 三方放款报文记录
        TppTrade tpp = new TppTrade(TppEnum.SERVICE_TYPE_LOAN.getValue(), TppEnum.TPP_TYPE_LOAN.getValue(), invest.getUserId(),
                investUser.getTppUserCustId(), project.getUserId(), projectUser.getTppUserCustId(), amount, invest.getUuid());
        tpp.setProjectId(project.getUuid());// 项目
        tpp.setInvestProjectId(invest.getProjectId());
        final StringBuilder buffer = new StringBuilder();
        buffer.append("{projectAmount:").append(BigDecimalUtils.round(project.getAccount()))
                .append(",investNo:").append(invest.getInvestNo())  // 投资流水号
                .append(",investDate:").append(invest.getInvestDate())
                .append(",freezeNo:").append(invest.getFreezeNo())
                .append(",userNo:").append(investUser.getUserNo())
                .append(",realName:").append(investUser.getRealName())
                .append(",realAmount:").append(realAmount) //对账使用
                .append(",voucher:").append(BigDecimalUtils.round(amount.subtract(realAmount))).append("}");
        tpp.setParams(buffer.toString());
//        tpp.setServFee(manageFee); // 借款管理费
        tpp.setAuthCode(invest.getAuthCode());//投资授权码
        return tpp;
    }

    /**
     * 截标处理
     *
     * @param project 指定项目
     * @author FangJun
     * @date 2016年7月27日
     */
    private void truncProject(final Project project) {
        if (BigDecimalUtils.validAmount(project.getAccountYes()) && project.getAccountYes().compareTo(project.getAccount()) < 0) {
            //剩余投资金额
        	BigDecimal remainInvestAccount = BigDecimalUtils.sub(project.getAccount(), project.getAccountYes());
        	// 设置项目总额和投资进度
            project.setAccount(project.getAccountYes());
            // 更新数据库
            Project projectTemp = new Project();
            projectTemp.setUuid(project.getUuid());
            projectTemp.setAccount(project.getAccountYes());
            dao.update(projectTemp);
            if(BigDecimal.ZERO.compareTo(remainInvestAccount) < 0 && LoanEnum.PROJECT_TYPE_CREDIT.getValue().equals(project.getProjectType())){
				//信用标截标返还用户剩余额度
				userCreditLineService.addCreditByAccount(project.getUserId(), remainInvestAccount, project.getProjectName() + "-" + project.getProjectNo());
			}
            LOGGER.info("项目({})截标处理--END", project.getUuid());
        }
    }

    @Override
    public Map<String, String> countVouchProject(String vouchId) {
        final Map<String, String> data = new HashMap<String, String>();
        if (StringUtils.isBlank(vouchId)) {
            throw new BussinessException("vouchId 参数错误");
        }
        // 担保标记
        String isVouch = LoanEnum.VOUCH_FLAG_YES.getValue();
        // 担保中金额 查询 审核通过  并且未还款标的总额
        BigDecimal vouchAccount = dao.vouchAccount(isVouch, RepaymentEnum.REPAY_TYPE_GUARANTEE.getValue(),
                LoanEnum.VOUCH_STATUS_VERIFY.getValue(), vouchId);
        data.put("vouchAccount", vouchAccount != null ? vouchAccount.toString() : "0");
        // 待审核项目
        String[] toAuditStatus = {LoanEnum.STATUS_WAIT_VOUCH_VERIFY.getValue()};
        Integer toAuditcount = dao.countVouchProject(isVouch, vouchId, LoanEnum.VOUCH_STATUS_WAIT_VERIFY.getValue(), toAuditStatus);
        data.put("toAuditCount", toAuditcount.toString());
        // 担保中项目
        String[] auditingStatus = {LoanEnum.STATUS_ESTABLISH.getValue(), LoanEnum.STATUS_REPAY_START.getValue(),
                LoanEnum.STATUS_REPAY_OVERDUE.getValue(), LoanEnum.STATUS_BAD_DEBT.getValue(),
                LoanEnum.STATUS_REPAYED_SUCCESS.getValue(), LoanEnum.STATUS_REPAYED_LATE.getValue()};
        Integer auditingCount = dao.countVouchProject(isVouch, vouchId, LoanEnum.VOUCH_STATUS_VERIFY.getValue(), auditingStatus);
        data.put("auditingCount", auditingCount.toString());
        // 待垫付项目
        data.put("toAdvanceCount", String.valueOf(dao.countOverdueProjectNum(vouchId)));
        return data;
    }

    @Override
    public Page<Project> findProject(Project project) {
        Page<Project> page = project.getPage();
        page.setRows(dao.findProject(project));
        return page;
    }

    @Override
    public int updateVouchStatus(Project project) {
        return dao.updateVouchStatus(project);
    }

    @Override
    public int vouchProject(Project model, String remark) {
        Project project = this.get(model.getUuid());
        if (!CommonEnum.YES.eq(project.getIsVouch()) || StringUtils.isBlank(project.getVouchId())) {
            //审核项目不是担保项目
            throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_VERIFY_NOT_VOUCH), BussinessException.TYPE_CLOSE);
        }
        if (StringUtils.isBlank(project.getVouchId()) || !project.getVouchId().equals(model.getVouchId())) {
            //当前用户不是该项目担保人
            throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_VERIFY_NOT_VOUCH_USER), BussinessException.TYPE_CLOSE);
        }

        //校验用户是否审核功能被冻结
        final boolean isFreezed = userFreezeService.isFreezed(model.getVouchId(), UserFreezeModel.STATUS_USER_FREEZE_VOUCH);
        if (isFreezed) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.VOUCH_IS_FREEZE), BussinessException.TYPE_CLOSE);
        }

        //审核
        if (LoanEnum.VOUCH_STATUS_VERIFY.getValue().equalsIgnoreCase(model.getVouchStatus())) {
            model.setStatus(LoanEnum.STATUS_WAIT_PUBLISH.getValue());
        } else {
            model.setVouchStatus(LoanEnum.VOUCH_STATUS_WAIT_FAIL.getValue());
            model.setStatus(LoanEnum.STATUS_VOUCH_VERIFY_FAIL.getValue());
        }
        //检查备注长度
        if (remark != null && remark.length() > Constant.INT_ONE_HUNDRED_TWENY_EIGHT) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_VERIFY_REMARK_LENGTH_ERROR), BussinessException.TYPE_CLOSE);
        }
        model.setVouchVerifyTime(DateUtils.getNow());
        // 添加担保审核记录
        User user = userService.get(model.getVouchId());
        this.vouchVerify(model, remark, user);

        return dao.updateVouchStatus(model);
    }

    /**
     * 计算借款总额，条件：募集中、成立待审、成立审核成功、还款中、逾期中、坏账、还款处理中、还款成功、逾期还款
     */
    @Override
    public BigDecimal getAccountTotalByUserId(String userId) {
        //借款总额
        String[] projectStatus = {
                LoanEnum.STATUS_RAISING.getValue(),
                LoanEnum.STATUS_RAISE_FINISHED.getValue(),
                LoanEnum.STATUS_ESTABLISH.getValue(),
                LoanEnum.STATUS_REPAY_START.getValue(),
                LoanEnum.STATUS_REPAY_OVERDUE.getValue(),
                LoanEnum.STATUS_BAD_DEBT.getValue(),
                LoanEnum.STATUS_REPAYING.getValue(),
                LoanEnum.STATUS_REPAYED_SUCCESS.getValue(),
                LoanEnum.STATUS_REPAYED_LATE.getValue()
        };
        return dao.getAccountTotalByUserId(userId, projectStatus);
    }

    @Override
    @Cacheable(key = CacheConstant.KEY_PREFIX_PROJECT_CONTENT + "{projectId}", expire = ExpireTime.FIVE_MIN)
    public Map<String, Object> getContent(String projectId) {
        Project project = this.get(projectId);
        if (project == null) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_NO_RECORD), BussinessException.TYPE_JSON);
        }
        return dao.getContent(projectId, project.getBorrowFlag());
    }

    @Override
    @Cacheable(key = CacheConstant.KEY_PREFIX_BORROWER_INFO + "{projectId}", expire = ExpireTime.FIVE_SEC)
    public Map<String, Object> getBorrowerInfo(String projectId) {
        Project project = this.get(projectId);
        if (project == null) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_NO_RECORD), BussinessException.TYPE_JSON);
        }
        final Map<String, Object> data = new HashMap<String, Object>();
        //系统时间
        data.put("systemTime", System.currentTimeMillis());
        // 是否有担保
        data.put("isVouch", project.getIsVouch());
        User user = userService.get(project.getUserId());
        UserCache userCache = userCacheService.findByUserId(project.getUserId());
        data.put("userName", StringUtils.hideName(user.getRealName()));
        //身份证
        if (StringUtils.isNotBlank(userCache.getIdNo()) && userCache.getIdNo().length() > 10) {
            data.put("idNo", StringUtils.hideStr(userCache.getIdNo(), 10, userCache.getIdNo().length() - 2));
        }
        data.put("image_server_url", ConfigUtils.getValue(ConfigConstant.IMAGE_SERVER_URL));
        // 借款人用户类型
        data.put("userNature", userCache.getUserNature());
        if (UserCache.USER_NATURE_PERSON.equals(userCache.getUserNature())) {
            // 借款人基本信息
            data.put("baseInfo", userBaseInfoService.findByUserId(project.getUserId()));
        } else {
            //企业
            final UserCompanyInfo companyInfo = userCompanyInfoService.findByUserId(project.getUserId());
            final Map<String, Object> infoData = new HashMap<String, Object>();
            infoData.put("userId", project.getUserId());
            infoData.put("companyName", StringUtils.hideCompanyName(companyInfo.getCompanyName(), Constant.INT_TWO, Constant.INT_SIX));
            infoData.put("simpleName", StringUtils.hideName(companyInfo.getSimpleName()));
            infoData.put("registeredCapital", companyInfo.getRegisteredCapital());
            infoData.put("address", StringUtils.isNull(userCache.getProvinceStr()) + StringUtils.isNull(userCache.getCityStr()) + StringUtils.isNull(userCache.getAreaStr()));
            infoData.put("officeAddress", StringUtils.isNull(companyInfo.getOfficeProvinceStr()) + StringUtils.isNull(companyInfo.getOfficeCityStr()) + StringUtils.isNull(companyInfo.getOfficeAreaStr()) + StringUtils.isNull(companyInfo.getOfficeAddress()));
            infoData.put("establishDate", companyInfo.getEstablishDate());
            infoData.put("legalDelegate", StringUtils.hideName(companyInfo.getLegalDelegate()));
            infoData.put("naturalPerson", StringUtils.hideListName(StringUtils.isNull(companyInfo.getNaturalPerson()).split("[,，]")));
            infoData.put("legalPerson", StringUtils.hideListName(StringUtils.isNull(companyInfo.getLegalPerson()).split("[,，]")));

            data.put("baseInfo", infoData);
        }
        // 省市
        data.put("province", userCache.getProvinceStr());
        data.put("city", userCache.getCityStr());
        // 借款资质信息
        UserQualificationApply model = new UserQualificationApply();
        model.setUserId(project.getUserId());
        List<UserQualificationApply> qualificationApplyList = userQualificationApplyService.findList(model);
        List<UserQualificationResultModel> qualificationResultList = new ArrayList<UserQualificationResultModel>();
        for (final UserQualificationApply apply : qualificationApplyList) {
            qualificationResultList.add(new UserQualificationResultModel(apply.getQualificationType(), apply.getStatus()));
        }
        data.put("qualificationResultList", qualificationResultList);
        // 担保机构信息
        if (Constant.FLAG_YES.equals(project.getIsVouch())) {
            User vouchCompany = userService.get(project.getVouchId());
            if (vouchCompany != null) {
                data.put("vouchName", vouchCompany.getRealName());
                //LOGO
                UserCompanyInfo companyInfo = userCompanyInfoService.findByUserId(vouchCompany.getUuid());
                if (companyInfo != null) {
                    final String vouchLogoPath = StringUtils.trim(companyInfo.getLogo());
                    data.put("vouchLogo", StringUtils.isBlank(vouchLogoPath) ? null : ConfigUtils.getValue("image_server_url") + vouchLogoPath);
                }
            }
        }

        return data;
    }

    @Override
    public Page<Project> findByDate(Project model) {
        Page<Project> page = model.getPage();
        if (page == null) {
            page = new Page<Project>();
        }
        List<Project> projectList = dao.findByDate(model);
        for (Project project : projectList) {
            project.setStatusStr(DictUtils.getItemName(DictTypeEnum.MY_LOAN_STATUS.getValue(), project.getStatus()));
        }
        page.setRows(projectList);
        return page;
    }

    @Override
    public BigDecimal sumProjectAccount(String[] projectStatus) {
        BigDecimal total = dao.sumProjectAccount(projectStatus);
        if (total == null) {
            total = new BigDecimal(Constant.DOUBLE_ZERO);
        }
        return total;
    }

    /**
     * 产品列表查询
     *
     * @param model 产品查询条件
     * @return ProjectRecord分页列表数据
     * @author FangJun
     * @date 2016年8月20日
     */
    @Cacheable(key = CacheConstant.KEY_PROJECT_LIST, expire = ExpireTime.FIVE_SEC)
    public Page<ProjectRecord> findProjectPage(ProjectRecord model) {
        final ProjectType typeModel = new ProjectType();
        typeModel.setParentId(model.getProjectTypeId());
        typeModel.setDeleteFlag(DeleteFlagEnum.NO.getValue());
        final int childTypeNum = projectTypeService.getCount(typeModel);
        if (childTypeNum > 0) {
            List<ProjectType> typeChilds = projectTypeService.findChilds(model.getProjectTypeId());
            StringBuilder buffer = new StringBuilder();
            buffer.append("'").append(model.getProjectTypeId()).append("',");
            if (CollectionUtils.isNotEmpty(typeChilds)) {
                for (ProjectType type : typeChilds) {
                    buffer.append("'").append(type.getUuid()).append("',");
                }
                model.setProjectTypeIdStr(buffer.substring(0, buffer.length() - 1));
                model.setProjectTypeId(null);
            }
        }
        // 判断是否去除产品列表
        if (Constant.STRING_ZERO.equals(ConfigUtils.getValue(ConfigConstant.OPEN_PRODUCT))) {
            model.setBorrowFlag(LoanEnum.BORROW_FLAG_BORROW.getValue());
        }
        //投资列表展示设置定向状态
        setSpecificSaleStatus(model);
        
        List<ProjectRecord> recordList = dao.findProjectPage(model);

        fillProjectPage(recordList);//设置列表页面其他属性值

        model.getPage().setRows(recordList);
        return model.getPage();
    }

    //定向销售状态，默认展示除定向用户之外的所有状态，定向用户只有登录的京账户或者柚账户看到自己对应账户的标
    private void setSpecificSaleStatus(ProjectRecord model) {
    	 List<String> specificSaleStatus = new ArrayList<String>();
    	 specificSaleStatus.add(LoanEnum.SPECIFIC_SALE_NONE.getValue());//不定向
    	 specificSaleStatus.add(LoanEnum.SPECIFIC_SALE_PASSWORD.getValue());//定向密码
		 specificSaleStatus.add(LoanEnum.SPECIFIC_SALE_VIP_LEVEL.getValue());//定向等级
		 specificSaleStatus.add(LoanEnum.SPECIFIC_SALE_MAIL.getValue());//定向邮箱
		 if(User.ACCOUNT_TYPE_YOU.equals(model.getUserAccountType()) || User.ACCOUNT_TYPE_JING.equals(model.getUserAccountType())){
			 //已登录
			 specificSaleStatus.add(LoanEnum.SPECIFIC_SALE_USER.getValue());//定向用户
		 }
    	 model.setSpecificSaleStatus(specificSaleStatus);
		
	}

	/**
     * 设置列表页面其他属性值
     *
     * @param recordList
     */
    private void fillProjectPage(List<ProjectRecord> recordList) {
        StringBuilder borrowIds = new StringBuilder();    //借贷Id
        StringBuilder productIds = new StringBuilder();    //产品id
        for (ProjectRecord record : recordList) {
            if (LoanEnum.BORROW_FLAG_BORROW.eq(record.getBorrowFlag())) {
                borrowIds.append("'").append(record.getUuid()).append("',");
            } else {
                productIds.append("'").append(record.getUuid()).append("',");
            }
            // 查询缓存中剩余可投
            record.setRemainAccount(ProjectCache.getRemainAccount(record.getUuid()));
        }

        String buids = getUsefulIds(borrowIds.toString(), LoanEnum.BORROW_FLAG_BORROW.getValue());
        String ruids = getUsefulIds(productIds.toString(), LoanEnum.BORROW_FLAG_PRODUCT.getValue());
        for (ProjectRecord record : recordList) {
            record.setBondUseful(buids.indexOf(record.getUuid()) >= 0 ? CommonEnum.YES.getValue() : CommonEnum.NO.getValue());
            record.setRealizeUseful(ruids.indexOf(record.getUuid()) >= 0 ? CommonEnum.YES.getValue() : CommonEnum.NO.getValue());
        }
    }

    @Override
    public List<Project> findListByStatusTime(String status, Date startDate,
                                              Date endDate) {
        return dao.findListByStatusTime(status, startDate, endDate);
    }

    @Override
    public String getProjectNameByInvestId(String investId) {
        String projectName = "";
        ProjectInvest invest = projectInvestService.get(investId);
        if (invest != null) {
            Project project = dao.get(invest.getProjectId());
            if (project != null) {
                if (LoanEnum.REALIZE_FLAG_NORMAL.eq(project.getRealizeFlag())) {
                    projectName = projectTypeService.getProjectTypeName(project.getProjectTypeId()) + "-" + project.getProjectNo();
                } else {
                    projectName = project.getProjectName();
                }
            }
        }
        return projectName;
    }

    @Override
    public List<Project> findExpireRealize(final Date endTime) {
        return dao.findExpireRealize(endTime);
    }

    @Override
    @Cacheable(key = CacheConstant.KEY_PREFIX_PRODUCT_INFO, expire = ExpireTime.FIVE_MIN)
    public Map<String, Object> getProductInfo(final String investId) {
        final Map<String, Object> data = new HashMap<String, Object>();
        data.put("result", true);
        final ProjectInvest invest = projectInvestService.get(investId);
        if (invest == null) {
            throw new BussinessException("所持资产未找到");
        }
        data.put("investAmount", invest.getAmount());
        final Project oldProject = dao.get(invest.getProjectId());
        data.put("project", oldProject);
        final String repayTime = DateUtils.formatDate(projectCollectionService.getlastRepayTime(investId), DateUtils.DATEFORMAT_STR_002);
        data.put("repayTime", repayTime);
        return data;
    }


    //@Transactional(readOnly=false)
    public int autoStopSale() {
        List<String> list = dao.findAutoStopSaleUuids();
        if (list != null && !list.isEmpty()) {
            return dao.autoStopSale(list.toArray(new String[list.size()]));
        }
        return 0;
    }

    /**
     * 获取首页项目列表
     */
    @Override
    public List<Project> findWebProjectList(Project project) {
        return dao.findWebProjectList(project);
    }

    @Override
    public Project getProjectByUuid(String uuid) {
        return dao.get(uuid);
    }

    @Override
    @Cacheable(key = CacheConstant.KEY_PROJECT_LIST_INDEX, expire = ExpireTime.FIVE_SEC)
    public List<Project> findIndexProjectList(Project model) {
        List<Project> projectList = super.findList(model);

        StringBuilder borrowIds = new StringBuilder();//借贷Id
        StringBuilder productIds = new StringBuilder();//产品id
        for (Project project : projectList) {
            if (LoanEnum.BORROW_FLAG_BORROW.eq(project.getBorrowFlag())) {
                borrowIds.append("'").append(project.getUuid()).append("',");
            } else {
                productIds.append("'").append(project.getUuid()).append("',");
            }
        }
        String buids = getUsefulIds(borrowIds.toString(), LoanEnum.BORROW_FLAG_BORROW.getValue());
        String ruids = getUsefulIds(productIds.toString(), LoanEnum.BORROW_FLAG_PRODUCT.getValue());
        for (Project project : projectList) {
            project.setBondUseful(buids.indexOf(project.getUuid()) >= 0 ? CommonEnum.YES.getValue() : CommonEnum.NO.getValue());
            project.setRealizeUseful(ruids.indexOf(project.getUuid()) >= 0 ? CommonEnum.YES.getValue() : CommonEnum.NO.getValue());
            if (StringUtils.isNotBlank(project.getProjectTypeId())) {
                ProjectType projectType = projectTypeService.get(project.getProjectTypeId());
                project.setProjectTypeName(projectType.getTypeName());
            }
            //首页列表只显示定向等级的定向规则内容
            if (!LoanEnum.SPECIFIC_SALE_VIP_LEVEL.eq(project.getSpecificSale())) {
                project.setSpecificSaleRule(null);
            }
        }
        return projectList;
    }

    /**
     * 查询可转让或者可变现产品id集合
     *
     * @param ids
     * @param borrowFlag
     * @return
     * @author ZhangBiao
     * @date 2016年10月19日
     */
    private String getUsefulIds(String ids, String borrowFlag) {
        List<String> idlist = new ArrayList<>();
        if (StringUtils.isBlank(ids)) {
            return "";
        }
        if (LoanEnum.BORROW_FLAG_BORROW.eq(borrowFlag)) {
            idlist = dao.findBondUsefulByProjectIds(ids.substring(0, ids.length() - 1));
        } else {
            idlist = dao.findRealizeUsefulByProjectIds(ids.substring(0, ids.length() - 1));
        }
        return StringUtils.join(idlist, ',');
    }

    @Override
    public void autoRemoveProjectCache(int delaySeconds) {

        if (dao.getShowingCount(delaySeconds) > 0) {
            LOGGER.info("RemoveProjectCache...");
            CacheUtils.batchDel(CacheConstant.KEY_PREFIX_PROJECT_LIST, CacheConstant.KEY_PROJECT_UUID_PREFIX);
        }

    }

    @Override
    public BigDecimal sumProjectAccountByDate(String[] projectStatus,
                                              String startDate, String endDate) {
        return dao.sumProjectAccountByDate(projectStatus, startDate, endDate);
    }

    @Override
    public List<Project> findListByStatus(String status) {
        return dao.findListByStatus(status);
    }

    @Override
    public Map<String, Object> getRemainAccount(String projectId) {
        final Project project = this.get(projectId);
        if (project == null) {
            throw new BussinessException(ResourceUtils.get(LoanResource.PROJECT_NOT_EXISTS), BussinessException.TYPE_JSON);
        }
        final double remainAccount = ProjectCache.getRemainAccount(project.getUuid());
        final Map<String, Object> data = new HashMap<String, Object>();
        data.put("remainAccount", remainAccount);
        return data;
    }

    @Override
    public Map<String, Object> getBespeakInfo(String projectId, String userId) {
        return dao.getBespeakInfo(projectId, userId);
    }

    @Override
    public void vouchVerify(Project model, String remark, User user) {
        projectVerifyLogService.save(model.getUuid(), user.getUserName(),
                ProjectConstant.PROCESS_NODE_VOUCH_VERIFY,
                DictUtils.getItemName(ProjectConstant.DICT_TYPE_PROJECTSTATUS, model.getStatus()),
                remark);
    }

    @Override
    public int getProjectCount(String riskLevel) {
        return dao.getProjectCount(riskLevel);
    }

    @Override
    public void updateStage(int stage, String projectNo) {
        LOGGER.info("projectNo={},stage={}", projectNo, stage);
        dao.updateStage(stage, projectNo);
    }

    @Override
    public void removeInvestBespeak(String projectNo) {
        dao.removeInvestBespeak(projectNo);
    }

    @Override
    public int getProjectNumByUserId(String userId) {
        return dao.getProjectNumByUserId(userId);
    }

    /**
     * 判断协议是否存在   存在true
     *
     * @param projectId
     * @return
     * @author QianPengZhan
     * @date 2016年11月5日
     */
    private boolean judgeFile(final String projectId) {
        boolean flag = true; //默认存在
        String imageHost = ConfigUtils.getValue("image_server_url");
        StringBuilder urlSb = new StringBuilder(imageHost);
        urlSb.append("/protocol/judgeProjectProtocolFileIsExists.html?&type=").append("zip")
                .append("&projectId=").append(projectId).append("&investId=").append(projectId);
        ;
        PostMethod post = new PostMethod(urlSb.toString());
        post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
        try {
            HttpClient client = new HttpClient();
            client.getHttpConnectionManager().getParams()
                    .setConnectionTimeout(5000);
            int status = client.executeMethod(post);
            if (status == HttpStatus.SC_OK) {//不存在 返回OK
                flag = false;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            post.releaseConnection();
        }
        return flag;
    }

    @Override
    //@Transactional(readOnly=false)
    public void projectCreateProtocol() {
        final Date now = DateUtils.getNow();//当前时间
        final Date beforeTime = DateUtils.rollMinute(now, Constant.INT_THIRTING_NEGE);//当前时间前30分钟的时间
        LOGGER.info("当前时间：{},往前推31分钟：{}", now, beforeTime);
        final List<Project> projectList = dao.findListByReviewTime(beforeTime, now);
        if (CollectionUtils.isNotEmpty(projectList)) {
            for (final Project project : projectList) {
                itemProjectCreateProtocol(project);
            }
        }
    }

    private void itemProjectCreateProtocol(final Project project) {
        LOGGER.info("判断协议是否存在,projectId:{}-----start", project.getUuid());
        final boolean flag = judgeFile(project.getUuid());
        LOGGER.info("判断协议是否存在:{}-----end", flag);
        if (!flag) {
            LOGGER.info("协议处理,projectId:{}-----start", project.getUuid());
            this.createProjectProtocol(project);
            LOGGER.info("协议处理,projectId:{}-----end", project.getUuid());
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void createProjectProtocol(final Project project) {
        /**
         * 定时器处理协议不走异步队列请求
         */
        final List<ProjectInvest> investList = projectInvestService.findSuccessInvest(project.getUuid());
        if (CollectionUtils.isNotEmpty(investList)) {
            for (final ProjectInvest invest : investList) {
                //循环生成投资人协议
                LOGGER.info("项目ID：{}成立审核---循环生成每个投资人的协议investUserId{}-------start", project.getUuid(), invest.getUserId());
                final InvestProtocol investProtocol = new InvestProtocol();
                investProtocol.setInvestId(invest.getUuid());
                investProtocol.setUserId(invest.getUserId());
                final InvestProtocol projectProtocol = new InvestProtocol();
                projectProtocol.setInvestId(invest.getUuid());
                projectProtocol.setUserId(project.getUserId());
                protocolService.buildProtocol(invest.getUuid(), invest.getUserId());
                protocolService.buildProtocol(invest.getUuid(), project.getUserId());
                LOGGER.info("成立审核---循环生成每个投资人的协议-------end");
            }
        }
    }

    @Override
    public Project getProjectValid(final String projectId, final String backUrl) {
        final Project project = dao.get(projectId);
        if (project == null) {
            throw new BussinessException(ResourceUtils.get(LoanResource.PROJECT_NOT_EXISTS), backUrl, BussinessException.TYPE_JSON);
        }
        return project;
    }

    @Override
    public String getProjectName(final Project project) {
        String infoName = StringUtils.EMPTY;
        if (LoanEnum.REALIZE_FLAG_REALIZE.eq(project.getRealizeFlag())) {
            infoName = project.getProjectName();
        } else {
            final String projectTypeName = projectTypeService.getProjectTypeName(project.getProjectTypeId());
            if (StringUtils.isNotBlank(projectTypeName)) {
                infoName = infoName.concat(projectTypeName).concat(StringUtils.HYPHEN).concat(project.getProjectNo());
            } else {
                infoName = project.getProjectName();
            }
        }
        return infoName;
    }

    @Override
    public String getProjectInfo(String uuid, String projectName, String realizeFlag) {
        final StringBuffer infoBuffer = new StringBuffer();
        String url = URLConstant.INVEST_DETAIL_PAGE_PREFIX;
        if (LoanEnum.REALIZE_FLAG_REALIZE.eq(realizeFlag)) {
            url = URLConstant.REALIZE_DETAIL_PAGE_PREFIX;
        }
        infoBuffer.append("<a href=\"")/*.append(StringUtils.stripEnd(ConfigUtils.getValue(ConfigConstant.WEB_URL), "/"))*/
                .append(url).append(uuid).append("\"  target=\"_blank\">").append(projectName).append("</a>");
        return infoBuffer.toString();
    }

    @Override
    public List<Project> getCountByStatus(String borrowFlag, String[] projectStatus) {
        return dao.getCountByStatus(borrowFlag, projectStatus);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void cbhbBatInvestCancleNotify(CbhbBatInvestCancleModel cbhbModel) {
        boolean ret = cbhbModel.validSign(cbhbModel);
        if (ret) {
            String respDesc = StringUtils.EMPTY;
            try {
                respDesc = URLDecoder.decode(cbhbModel.getRespDesc(), "GBK");
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("解码失败");
            }
            LOGGER.info("批量投资撤销回调业务处理 orderNo: {},respCode:{},respDesc:{}", cbhbModel.getMerBillNo(),
                    cbhbModel.getRespCode(), respDesc);
            final TppTrade trade = tppTradeService.findByOrderNo(cbhbModel.getMerBillNo());
            if (trade == null) {
                throw new CbhbException(ResourceUtils.get(ResourceConstant.GLOBAL_RECORD_NO_EXISTS), BussinessException.TYPE_JSON);
            }
            int updateNum = 0;
            if (CbhbConstant.CBHB_CODE_SUCCESS.equals(cbhbModel.getRespCode())) {
                updateNum = tppTradeService.callbackHandle(trade.getUuid(), TppEnum.STATUS_SUCEESS.getValue(), respDesc);
                if (updateNum == Constant.INT_ONE) {
                    //处理本地业务   FTP获取reslut文件  然后解析放入对象  根据对象的处理结果来处理本地业务
                    this.successBatInvestCancleHandle(trade, cbhbModel);
                }
            } else {
                // 失败处理
                updateNum = tppTradeService.callbackHandle(trade.getUuid(), TppEnum.STATUS_FAIL.getValue(), respDesc);
            }
            if (updateNum != Constant.INT_ONE) {
                LOGGER.warn("批量投资撤销回调业务处理--更新状态失败,orderNo：{}", cbhbModel.getMerBillNo());
            }
            LOGGER.info("批量投资撤销回调业务处理结束 orderNo: {},respCode:{},respDesc:{}", cbhbModel.getMerBillNo(),
                    cbhbModel.getRespCode(), respDesc);
        } else {
            LOGGER.error("验签失败");
            throw new CbhbException("验签失败", BussinessException.TYPE_JSON);
        }
    }

    /**
     * 批量撤销的业务处理
     *
     * @param trade
     * @param cbhbModel
     * @author QianPengZhan
     * @date 2017年3月13日
     */
    @Transactional(readOnly = false)
    void successBatInvestCancleHandle(final TppTrade trade, final CbhbBatInvestCancleModel cbhbModel) {
        //1、获取下载的result文件然后处理信息放入对象
        String fileName = cbhbModel.getResultFileName();
        String localFilePath = cbhbModel.getResultFileNamePath();//相对路径
        String remotePath = new CbhbChkBaseModel().getRemotePath(DateUtils.getNow());
        Map<String, Object> map = FileUtil.getResultFileContent(localFilePath, remotePath, fileName);
        String firstContent = StringUtils.isNull(map.get("firstContent"));
        String[] otherContent = (String[]) map.get("otherContent");
        FileBatInvestCancleModel cancleModel = new FileBatInvestCancleModel();
        cancleModel = cancleModel.getModelByContent(firstContent);
        FileBatInvestCancleDetailModel detailMolde = new FileBatInvestCancleDetailModel();
        List<FileBatInvestCancleDetailModel> list = detailMolde.getListByArray(otherContent);

        //2、根据结果批量处理投资撤销业务
        if (Integer.parseInt(cancleModel.getTotalNum()) == list.size()) {
            boolean bl = true;
            for (int i = 0; i < list.size(); i++) {
                FileBatInvestCancleDetailModel detail = list.get(i);
                if (!CbhbConstant.CBHB_CODE_SUCCESS.equals(detail.getRespCode())) {//撤销一笔不成功，则全部不成功
                    bl = false;
                }
            }
            if (bl) {
                // 未支付成功用户(退回投资用户资金、添加资金日志、退回优惠劵)
                projectInvestService.cancelProjectHandleUnpay(trade.getInvestProjectId());
            } else {
                LOGGER.info("撤销失败");
            }
        } else {
            LOGGER.info("笔数不对或者批次号不对");
        }
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void cbhbLoanNotify(CbhbFileReleaseModel cbhbModel) {
        String respDesc = StringUtils.EMPTY;
        try {
            respDesc = URLDecoder.decode(cbhbModel.getRespDesc(), "GBK");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("解码失败");
        }
        LOGGER.info("放款回调业务处理 orderNo: {},respCode:{},respDesc:{}", cbhbModel.getMerBillNo(),
                cbhbModel.getRespCode(), respDesc);
        final TppTrade trade = tppTradeService.findByOrderNo(cbhbModel.getMerBillNo());
        if (trade == null) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_RECORD_NO_EXISTS));
        }
        int updateNum = 0;
        if (CbhbConstant.CBHB_CODE_SUCCESS.equals(cbhbModel.getRespCode())) {
            updateNum = tppTradeService.callbackHandle(trade.getUuid(), TppEnum.STATUS_SUCEESS.getValue(), respDesc);
            if (updateNum == Constant.INT_ONE) {
                this.successLoanHandle(trade);
            }
        } else {
            // 失败处理
            updateNum = tppTradeService.callbackHandle(trade.getUuid(), TppEnum.STATUS_FAIL.getValue(), respDesc);
        }
        if (updateNum != Constant.INT_ONE) {
            LOGGER.warn("放款异步回调处理--更新状态失败,orderNo：{}", cbhbModel.getMerBillNo());
        }
        LOGGER.info("放款回调业务处理结束 orderNo: {},respCode:{},respDesc:{}", cbhbModel.getMerBillNo(),
                cbhbModel.getRespCode(), respDesc);
    }

    /**
     * 放款成功处理本地业务
     *
     * @param trade
     * @author QianPengZhan
     * @date 2017年3月8日
     */
    private void successLoanHandle(final TppTrade trade) {
        // 根据项目ID查询放款项目
        final Project project = dao.get(trade.getInvestProjectId());
        // 初始化需要调用Account的dubbo服务执行： 借款人、投资人的账户金额处理
        final List<AccountModel> accountList = new ArrayList<AccountModel>();
        // 初始化借款人、投资人的资金日志
        final List<AccountLog> accountLogList = new ArrayList<AccountLog>();
        // 项目详情页链接
        final String projectInfo = this.getProjectInfo(project.getUuid(), this.getProjectName(project), project.getRealizeFlag());
        // 处理投资人资金
        handleInvesterAccount(trade, project, projectInfo, accountList, accountLogList);
        // 处理借款人资金
        if (LoanEnum.REALIZE_FLAG_NORMAL.eq(project.getRealizeFlag())) {// 普通标放款
            loanHandleForProject(trade, project, projectInfo, accountList, accountLogList);
        } else {
            loanHandleForRealize(trade, project, projectInfo, accountList, accountLogList);
        }
        // 6 Dubbo服务处理账户信息
        accountService.saveBatch(new AccountBatchModel(accountList, accountLogList));
    }


    /**
     * 处理投资人资金
     *
     * @param trade 报文记录
     * @author FangJun
     * @date 2016年8月9日
     */
    private void handleInvesterAccount(TppTrade trade, Project project, String projectInfo, List<AccountModel> accountList,
                                       List<AccountLog> accountLogList) {
        List<ProjectInvest> list = projectInvestService.findSuccessInvest(trade.getInvestProjectId());
        if (CollectionUtils.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                ProjectInvest invest = list.get(i);
                final User investUser = userService.get(invest.getUserId());
                // 投资人资金修改
                accountList.add(new AccountModel(invest.getUserId(), ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE), BigDecimal.ZERO,
                        invest.getRealAmount().negate()));
                final Map<String, Object> remarkData = new HashMap<String, Object>(1);
                remarkData.put("amount", invest.getAmount().doubleValue());
                remarkData.put("realAmount", invest.getRealAmount().doubleValue());
                remarkData.put("projectInfo", projectInfo);
                final String remark = LogTemplateUtils.getAccountTemplate(Constant.INVEST_SUCCESS, remarkData);
                AccountLog accountLog = new AccountLog(ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE), Constant.INVEST_SUCCESS,
                        investUser.getUuid(), invest.getRealAmount(), remark);
                accountLog.setToUser(project.getUserId());
                accountLog.setPaymentsType(AccountLog.PAYMENTS_TYPE_EXPEND);
                accountLogList.add(accountLog);

                //平台资金日志- 红包抵扣
                BigDecimal voucher = BigDecimalUtils.round(invest.getAmount().subtract(invest.getRealAmount()));
                if (BigDecimalUtils.validAmount(voucher)) {
                    tppMerchantLogService.saveMerchantLog(TppMerchantLogModel.TYPE_REDENVELOPE, invest.getUserId(), voucher, trade.getOrderNo());
                }
                // 给投资人发送项目成立通知  invest_success_msg
                try {
                    HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put("user", userService.get(invest.getUserId()));
                    params.put("amount", invest.getAmount().doubleValue());
                    params.put("realAmount", invest.getRealAmount().doubleValue());
                    params.put("projectName", project.getProjectName().length() > 10 ? project.getProjectName().substring(0, 10) + "..." : project.getProjectName());
                    BaseMsg baseMsg = new BaseMsg(MessageConstant.INVEST_SUCC, params);
                    baseMsg.doEvent();
                } catch (Exception e) {
                    LOGGER.error("给投资人发送项目成立通知处理失败，orderNo:{}", trade.getOrderNo(), e);
                }
            }
        }
    }

    /**
     * 普通标放款处理借款人
     *
     * @author fxl
     * @date 2016年10月27日
     */
    private void loanHandleForProject(TppTrade trade, Project project, String projectInfo, List<AccountModel> accountList,
                                      List<AccountLog> accountLogList) {
        accountList.add(new AccountModel(project.getUserId(), ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE), trade.getMoney(),
                BigDecimal.ZERO));
        Map<String, Object> borrowerRemarkData = new HashMap<String, Object>(1);
        borrowerRemarkData.put("amount", trade.getMoney().doubleValue());
        borrowerRemarkData.put("projectInfo", projectInfo);
        String borrowerRemark = LogTemplateUtils.getAccountTemplate(Constant.BORROW_SUCCESS, borrowerRemarkData);
        final AccountLog borrowerAccountLog = new AccountLog(ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE),
                Constant.BORROW_SUCCESS, project.getUserId(), trade.getMoney(), borrowerRemark);
        borrowerAccountLog.setToUser(Constant.TO_USER_INVEST);
        borrowerAccountLog.setOrderNo(trade.getOrderNo());
        borrowerAccountLog.setPaymentsType(AccountLog.PAYMENTS_TYPE_INCOME);
        accountLogList.add(borrowerAccountLog);

        // 借贷产品收取管理费
        if (LoanEnum.BORROW_FLAG_BORROW.eq(project.getBorrowFlag()) && trade.getServFee().doubleValue() > 0) {
            accountList.add(new AccountModel(project.getUserId(), ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE),
                    trade.getServFee().negate(), BigDecimal.ZERO));
            Map<String, Object> feeRemarkData = new HashMap<String, Object>(1);
            feeRemarkData.put("fee", trade.getServFee().doubleValue());
            String feeRemark = LogTemplateUtils.getAccountTemplate(Constant.BORROW_FEE, feeRemarkData);
            final AccountLog feeAccountLog = new AccountLog(ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE),
                    Constant.BORROW_FEE, project.getUserId(), trade.getServFee(), feeRemark);
            feeAccountLog.setToUser(ConfigUtils.getValue(Constant.ADMIN_ID));
            feeAccountLog.setOrderNo(trade.getOrderNo());
            feeAccountLog.setPaymentsType(AccountLog.PAYMENTS_TYPE_EXPEND);
            accountLogList.add(feeAccountLog);

            //平台资金日志- 借款管理费
            tppMerchantLogService.saveMerchantLog(TppMerchantLogModel.TYPE_BORROW_FEE, project.getUserId(), trade.getServFee(), trade.getOrderNo());
        }
    }


    /**
     * 变现放款处理借款人资金
     *
     * @author fxl
     * @date 2016年10月27日
     */
    private void loanHandleForRealize(TppTrade trade, Project project, String projectInfo, List<AccountModel> accountList,
                                      List<AccountLog> accountLogList) {
        // 借款人变现入账
        accountList.add(new AccountModel(project.getUserId(), ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE), trade.getMoney(),
                BigDecimal.ZERO));
        Map<String, Object> borrowerRemarkData = new HashMap<String, Object>(1);
        borrowerRemarkData.put("amount", trade.getMoney().doubleValue());
        borrowerRemarkData.put("projectInfo", projectInfo);
        String borrowerRemark = LogTemplateUtils.getAccountTemplate(Constant.REALIZE_INCOME, borrowerRemarkData);
        final AccountLog borrowerAccountLog = new AccountLog(ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE),
                Constant.REALIZE_INCOME, project.getUserId(), trade.getMoney(), borrowerRemark);
        borrowerAccountLog.setToUser(ConfigUtils.getValue(Constant.ADMIN_ID));
        borrowerAccountLog.setOrderNo(trade.getOrderNo());
        borrowerAccountLog.setPaymentsType(AccountLog.PAYMENTS_TYPE_INCOME);
        accountLogList.add(borrowerAccountLog);
        // 变现管理费
        BigDecimal realizeManageFee = trade.getServFee();
        if (realizeManageFee.doubleValue() > 0) {
            accountList.add(new AccountModel(project.getUserId(), ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE),
                    realizeManageFee.negate(), BigDecimal.ZERO));
            Map<String, Object> feeRemarkData = new HashMap<String, Object>(1);
            feeRemarkData.put("amount", realizeManageFee.doubleValue());
            String feeRemark = LogTemplateUtils.getAccountTemplate(Constant.REALIZE_MANAGEFEE, feeRemarkData);
            final AccountLog feeAccountLog = new AccountLog(ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE),
                    Constant.REALIZE_MANAGEFEE, project.getUserId(), realizeManageFee, feeRemark);
            feeAccountLog.setToUser(ConfigUtils.getValue(Constant.ADMIN_ID));
            feeAccountLog.setOrderNo(trade.getOrderNo());
            feeAccountLog.setPaymentsType(AccountLog.PAYMENTS_TYPE_EXPEND);
            accountLogList.add(feeAccountLog);

            //平台资金日志- 借款管理费(变现)
            tppMerchantLogService.saveMerchantLog(TppMerchantLogModel.TYPE_BORROW_FEE, project.getUserId(), realizeManageFee, trade.getOrderNo());
        }
    }
}