package com.rd.ifaes.mobile.controller.member;

import com.google.common.collect.Maps;
import com.rd.account.domain.AccountLog;
import com.rd.account.model.AccountQueryModel;
import com.rd.account.service.AccountLogService;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.dict.RepaymentEnum;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TokenUtils;
import com.rd.ifaes.core.account.model.CashModel;
import com.rd.ifaes.core.account.model.RechargeModel;
import com.rd.ifaes.core.account.service.CashService;
import com.rd.ifaes.core.account.service.RechargeService;
import com.rd.ifaes.core.core.constant.*;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.DictUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.project.domain.*;
import com.rd.ifaes.core.project.service.*;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCompanyInfo;
import com.rd.ifaes.core.user.domain.UserIdentify;
import com.rd.ifaes.core.user.service.UserCompanyInfoService;
import com.rd.ifaes.core.user.service.UserFreezeService;
import com.rd.ifaes.core.user.service.UserIdentifyService;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.mobile.controller.WebController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 担保用户账户中心
 *
 * @author wyw
 * @version 3.0
 * @date 2016-8-12
 */
@Controller
public class VouchController extends WebController {
    /**
     * 图片服务器
     */
    private static final String IMAGE_SERVE_URL = "image_server_url";


    /**
     * 用户公司信息
     */
    @Resource
    private transient UserCompanyInfoService userCompanyInfoService;
    /**
     * 用户取现
     */
    @Resource
    private transient CashService cashService;
    /**
     * 用户业务处理
     */
    @Resource
    private transient UserService userService;
    /**
     * 项目业务处理
     */
    @Resource
    private transient ProjectService projectService;
    /**
     * 资金业务处理
     */
    @Resource
    private transient AccountService accountService;
    /**
     * 认证业务处理
     */
    @Resource
    private transient UserIdentifyService userIdentifyService;
    /**
     * 资金日志业务处理
     */
    @Resource
    private transient AccountLogService accountLogService;
    /**
     * 充值业务处理
     */
    @Resource
    private transient RechargeService rechargeService;
    /**
     * 借款业务处理
     */
    @Resource
    private transient BorrowService borrowService;
    /**
     * 还款业务处理
     */
    @Resource
    private transient ProjectRepaymentService projectRepaymentService;
    /**
     * 详情图片处理
     */
    @Resource
    private transient BorrowUploadService borrowUploadService;
    /**
     * 审核记录处理
     */
    @Resource
    private transient ProjectVerifyLogService projectVerifyLogService;
    @Resource
    private ProjectTypeService projectTypeService;

    @Resource
    private transient UserFreezeService userFreezeService;

    /**
     * 返回数据处理
     */
    private transient Map<String, Object> data;


    /**
     * 账户预览中心数据
     *
     * @return
     * @author wyw
     * @date 2016-8-12
     */
    @RequestMapping(value = "/member/vouch/getAccountInfo")
    @ResponseBody
    public Object getUserInfo() {
        //用户信息
        final User user = SessionUtils.getSessionUser();
        //认证信息
        final UserIdentify userIdentify = (UserIdentify) SessionUtils.getSessionAttr(Constant.SESSION_USER_IDENTIFY);
        //返回参数
        data = new HashMap<>();
        final UserCompanyInfo userCompanyInfo = userCompanyInfoService.findByUserId(user.getUuid());
        //用户认证信息
        data.put(Constant.SESSION_USER_IDENTIFY, userIdentify);
        if (!ObjectUtils.isEmpty(userCompanyInfo) && StringUtils.isNotBlank(userCompanyInfo.getLogo())) {
            data.put("logo", ConfigUtils.getValue(IMAGE_SERVE_URL) + userCompanyInfo.getLogo());
        } else {
            data.put("logo", "");
        }
        data.put("image_server_url", IMAGE_SERVE_URL);
        //用户账户信息
        data.put("account", accountService.getByUserId(new AccountQueryModel(user.getUuid(), ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE))));
        //安全等级
        data.put("securityLevel", userIdentifyService.getSecurityLevel(user));
        //用户信息
        data.put(Constant.SESSION_USER, user);
        final Map<String, String> countMap = projectService.countVouchProject(user.getUuid());
        //担保中金额
        data.put("vouchAccount", countMap.get("vouchAccount"));
        //待审核项目
        data.put("toAuditCount", countMap.get("toAuditCount"));
        //担保中项目
        data.put("auditingCount", countMap.get("auditingCount"));
        //待垫付项目
        data.put("toAdvanceCount", countMap.get("toAdvanceCount"));
        return data;
    }

    /**
     * 资金日志数据
     *
     * @param accountLog
     * @return
     * @author wyw
     * @date 2016-8-12
     */
    @RequestMapping(value = "/member/vouch/getLogList")
    @ResponseBody
    public Object getLogList(final AccountLog accountLog, final String dateType) {
        final User user = SessionUtils.getSessionUser();
        accountLog.setUserId(user.getUuid());
        accountLog.setAccountCode(ConfigUtils.getValue("account_code"));
        accountLog.setDateType(Constant.DATE_TYPE_ALL.equals(dateType) ? "" : dateType);
        accountLog.setDateTypeTime(getDateTypeTime(dateType));
        if (!StringUtils.isBlank(accountLog.getStartTime())) {
            accountLog.setStartTime(DateUtils.getDateStart(DateUtils.parseDate(accountLog.getStartTime())));
        }
        if (!StringUtils.isBlank(accountLog.getEndTime())) {
            accountLog.setEndTime(DateUtils.getDateEnd(DateUtils.parseDate(accountLog.getEndTime())));
        }
        final Page<AccountLog> page = accountLogService.findByDate(accountLog);
        final List<AccountLog> logList = page.getRows();
        for (final AccountLog log : logList) {
            if (Constant.REPAID_CAPITAL.equals(log.getAccountType()) || Constant.REPAID_INTEREST.equals(log.getAccountType()) ||
                    Constant.REPAID_LATE_INTEREST.equals(log.getAccountType()) || Constant.REPAID_MERCHANT_LATE_INTEREST.equals(log.getAccountType())) {
                log.setAccountType(Constant.PAY_FOR_SUCCESS);
            }
            log.setAccountTypeStr(DictUtils.getItemName(DictTypeEnum.ACCOUNT_TYPE.getValue(), log.getAccountType()));
            if (StringUtils.isNotBlank(log.getToUser())) {
                if (ConfigUtils.getValue(Constant.ADMIN_ID).equals(log.getToUser())) {
                    log.setToUser(ConfigUtils.getValue("web_name"));
                } else {
                    User toUser = userService.get(log.getToUser());
                    if (ObjectUtils.isEmpty(toUser)) {
                        log.setToUser(Constant.TO_USER_NULL);
                    } else {
                        log.setToUser(toUser.getHideUserName());
                    }
                }
            } else {
                log.setToUser(Constant.TO_USER_NULL);
            }
        }
        final Map<String, Object> data = Maps.newHashMap();
        data.put("result", true);
        data.put("data", page);
        return data;
    }

    /**
     * 充值记录
     *
     * @param model
     * @return
     * @author wyw
     * @date 2016-8-12
     */
    @RequestMapping(value = "/member/vouch/rechargeList")
    @ResponseBody
    public Object rechargeList(final RechargeModel model) {
        data = new HashMap<>();
        data.put(RESULT_NAME, true);
        data.put("data", rechargeService.findByDate(model));
        return data;
    }

    /**
     * 提现记录
     *
     * @param model
     * @return
     * @author wyw
     * @date 2016-8-12
     */
    @RequestMapping(value = "/member/vouch/cashList")
    @ResponseBody
    public Object cashList(final CashModel model) {
        data.put(RESULT_NAME, true);
        data.put("data", cashService.findByDate(model));
        return data;
    }

    /**
     * 审核项目页面
     *
     * @return
     * @author wyw
     * @date 2016-8-12
     */
    @RequestMapping(value = "/member/vouch/verify")
    public String verify() {
        return "/member/vouch/verify";
    }

    /**
     * 审核项目列表数据
     *
     * @param model
     * @return
     * @author wyw
     * @date 2016-8-12
     */
    @RequestMapping(value = "/member/vouch/verifyList")
    @ResponseBody
    public Object verifyList(final Borrow model) {
        final User user = SessionUtils.getSessionUser();
        //担保用户id
        model.setVouchId(user.getUuid());
        //是担保项目
        model.setIsVouch(LoanEnum.VOUCH_FLAG_YES.getValue());
        //审核通过的项目
        Page<Borrow> borrowList = model.getPage();
        if (LoanEnum.VOUCH_STATUS_WAIT_VERIFY.getValue().equalsIgnoreCase(model.getVouchStatus())) {
            model.setStatus(LoanEnum.STATUS_WAIT_VOUCH_VERIFY.getValue());
            borrowList.setSort("update_time");
        } else {
            borrowList.setSort("vouch_verify_time");
        }
        borrowList.setOrder(Constant.DESC);
        model.setPage(borrowList);
        borrowList = borrowService.findPage(model);
        //处理期限真实姓名
        for (int i = 0; i < borrowList.getRows().size(); i++) {
            Borrow borrow = borrowList.getRows().get(i);
            borrow.setRealName(borrow.getBorrower());
            if (borrow.getTimeType().equalsIgnoreCase(LoanEnum.TIME_TYPE_MONTH.getValue())) {
                borrow.setTimeLimit(borrow.getTimeLimit() * 30);
            }
            if (StringUtils.isNotBlank(borrow.getProjectTypeId())) {
                ProjectType projectType = projectTypeService.get(borrow.getProjectTypeId());
                borrow.setProjectTypeName(projectType.getTypeName());
            }
        }
        data = new HashMap<>();
        data.put(RESULT_NAME, true);
        data.put("data", borrowList);
        return data;

    }

    /**
     * 审核项目页面操作--担保用户
     *
     * @param uuid
     * @return
     * @author wyw
     * @date 2016-8-12
     */
    @RequestMapping(value = "/member/vouch/verifyPage")
    public String verifyPage(final String id, final Model model) {
        model.addAttribute("uuid", id);
        return "/member/vouch/verifyPage";
    }

    /**
     * 获得审核项目列表--担保用户
     *
     * @param model
     * @return
     * @author wyw
     * @date 2016-8-12
     */
    @RequestMapping(value = "/member/vouch/doVerify")
    @ResponseBody
    public Object doVerify(final Project model, final String remark) {
        final User user = SessionUtils.getSessionUser();
        //记录审核用户UUID
        model.setVouchId(user.getUuid());

        if (projectService.vouchProject(model, remark) > Constant.INT_ZERO) {
            return renderSuccessResult();
        }
        return renderResult(false, ResourceUtils.get(ResourceConstant.RECORD_IS_VERIFIED));
    }

    /**
     * 项目详情页
     *
     * @param uuid
     * @return
     * @author wyw
     * @date 2016-8-12
     */
    @RequestMapping(value = "/member/vouch/projectInfo")
    public String projectInfo(final String uuid, final Model model) {
        model.addAttribute("uuid", uuid);
        return "/member/vouch/projectInfo";
    }

    /**
     * 获得项目详情页信息
     *
     * @param model
     * @return
     * @author wyw
     * @date 2016-8-12
     */
    @RequestMapping(value = "/member/vouch/getProjectInfo")
    @ResponseBody
    public Object getProjectInfo(final Borrow model) {
        final Borrow borrow = borrowService.get(model.getUuid());
        data = Maps.newHashMap();
        // 借款资料
        final BorrowUpload uploadModel = new BorrowUpload();
        uploadModel.setProjectId(borrow.getUuid());

        uploadModel.setFileType(BorrowUpload.FILE_TYPE_BORROW);
        final List<BorrowUpload> picList = borrowUploadService.findList(uploadModel);

        ProjectVerifyLog projectVerifyLog = projectVerifyLogService.getRemarkCreateTime(borrow.getUuid(), ProjectConstant.PROCESS_NODE_VOUCH_VERIFY);

        uploadModel.setFileType(BorrowUpload.FILE_TYPE_MORTGAGE);
        final List<BorrowUpload> mtgList = borrowUploadService.findList(uploadModel);
        data.put(IMAGE_SERVE_URL, ConfigUtils.getValue(IMAGE_SERVE_URL));
        data.put("borrow", borrow);
        data.put("projectVerifyLog", projectVerifyLog);
        data.put("picList", picList);
        data.put("mtgList", mtgList);
        data.put(RESULT_NAME, true);
        return data;
    }

    /**
     * 担保项目页
     *
     * @return
     * @author wyw
     * @date 2016-8-12
     */
    @RequestMapping(value = "/member/vouch/vouchProject")
    public String vouchProject() {
        return "/member/vouch/vouchProject";
    }

    /**
     * 获得担保项目列表
     *
     * @param model
     * @return
     * @author wyw
     * @date 2016-8-12
     */
    @RequestMapping(value = "/member/vouch/vouchProjectList")
    @ResponseBody
    public Object vouchProjectList(final ProjectRepayment model) {
        final User user = SessionUtils.getSessionUser();
        //担保用户id
        model.setVouchId(user.getUuid());
        //审核通过
        model.setVouchStatus(LoanEnum.VOUCH_STATUS_VERIFY.getValue());
        //是担保项目
        model.setIsVouch(LoanEnum.VOUCH_FLAG_YES.getValue());
        data = new HashMap<>();
        data.put(RESULT_NAME, true);
        data.put("data", projectRepaymentService.findPage(model));
        return data;
    }

    /**
     * 逾期项目页
     *
     * @return
     * @author wyw
     * @date 2016-8-12
     */
    @RequestMapping(value = "/member/vouch/overdue")
    public String overdue(final HttpServletRequest request) {
        TokenUtils.setToken(request.getSession(), TokenConstants.TOKEN_REPAY);
        return "/member/vouch/overdue";
    }

    /**
     * 获得逾期项目列表
     *
     * @param model
     * @return
     * @author wyw
     * @date 2016-8-12
     */
    @RequestMapping(value = "/member/vouch/overdueList")
    @ResponseBody
    public Object overdueList(final ProjectRepayment model, final String overdueStatus) {
        final User user = SessionUtils.getSessionUser();
        //担保用户id
        model.setVouchId(user.getUuid());
        //审核通过
        model.setVouchStatus(LoanEnum.VOUCH_STATUS_VERIFY.getValue());
        //是担保项目
        model.setIsVouch(LoanEnum.VOUCH_FLAG_YES.getValue());
        //逾期了
        model.setIsOverdue(String.valueOf(Constant.INT_ONE));
        if (RepaymentEnum.STATUS_REPAID.eq(overdueStatus)) {
            model.setStatus(RepaymentEnum.STATUS_REPAID.getValue());//已经还了
            model.setRepayType(RepaymentEnum.REPAY_TYPE_REPAYED_GUARANTEE.getValue());//已经垫付
        } else {
            model.setStatus(RepaymentEnum.STATUS_NOT_REPAY.getValue());
        }
        data = new HashMap<>();
        data.put(RESULT_NAME, true);
        data.put("data", projectRepaymentService.findOverduePage(model));
        return data;
    }

    /**
     * 逾期垫付页面
     *
     * @param projectRepayment
     * @return
     * @author wyw
     * @date 2016-8-12
     */
    @RequestMapping(value = "/member/vouch/overduePay")
    public String overduePay(final ProjectRepayment projectRepayment, final Model model) {
        //TokenUtils.setToken(request.getSession(), TokenConstants.TOKEN_REPAY);
        model.addAttribute("projectRepayment", projectRepayment);
        return "/member/vouch/overduePay";
    }

    /**
     * 垫付线下已经回款
     *
     * @param projectRepayment
     * @return
     * @author wyw
     * @date 2016-8-12
     */
    @RequestMapping(value = "/member/vouch/doLineRepay")
    @ResponseBody
    public Object doLineRepay(final ProjectRepayment projectRepayment) {
        projectRepayment.setRepayType(RepaymentEnum.REPAY_TYPE_REPAYED_GUARANTEE.getValue());
        projectRepaymentService.update(projectRepayment);
        return renderSuccessResult();
    }

    /**
     * 逾期垫付项目详情页面
     *
     * @param uuid
     * @return
     * @author wyw
     * @date 2016-8-12
     */
    @RequestMapping(value = "/member/vouch/overdueDetail")
    public String overdueDetail(final String uuid, final Model model) {
        model.addAttribute("id", uuid);
        return "/member/vouch/overdueDetail";
    }

    /**
     * 逾期垫付项目详情
     *
     * @param uuid
     * @return
     * @author ZhangBiao
     * @date 2016年9月27日
     */
    @RequestMapping(value = "/member/vouch/overdueDetailData")
    @ResponseBody
    public Object overdueDetailData(final String uuid) {
        return projectRepaymentService.get(uuid);
    }

    /**
     * 担保用户垫付还款--获取手机验证码
     *
     * @return 发送结果
     * @author FangJun
     * @date 2016年8月9日
     */
    @RequestMapping(value = "/member/vouch/getAdvanceRepayCode")
    @ResponseBody
    public Object getAdvanceRepayCode() {
        final User currentUser = getSessionUser();
        sendMessage(currentUser, currentUser.getMobile(), MessageConstant.MESSAGE_USER_ADVANCE_PHONECODE);
        return renderSuccessResult();
    }

    /**
     * 逾期垫付操作
     *
     * @param projectRepayment
     * @param phoneCode
     * @return
     * @author wyw
     * @date 2016-8-12
     */
    @RequestMapping(value = "/member/vouch/doOverdue")
    @ResponseBody
    @TokenValid(value = TokenConstants.TOKEN_REPAY, dispatch = true)
    public Object doOverdue(final ProjectRepayment projectRepayment, final String code) {
        final User currentUser = getSessionUser();
        projectRepaymentService.overdueRepay(projectRepayment.getUuid(), currentUser.getMobile(), code, currentUser.getUuid());
        return renderSuccessResult();
    }

    /**
     * 银行卡列表
     *
     * @return
     * @throws Exception
     * @author wyw
     * @date 2016-8-12
     */
    @RequestMapping("/member/vouch/getBank")
    @ResponseBody
    public Object getBank() {
        data = Maps.newHashMap();
        data.put("bankNum", ConfigUtils.getInt("bank_num"));
        data.put("bankList", userService.getBankList());
        return data;
    }
}
