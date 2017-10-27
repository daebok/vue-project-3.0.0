package com.rd.ifaes.mobile.controller.member;

import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.dict.RepaymentEnum;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.TokenUtils;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.MessageConstant;
import com.rd.ifaes.core.project.domain.ProjectRepayment;
import com.rd.ifaes.core.project.service.ProjectRepaymentService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.mobile.common.AppException;
import com.rd.ifaes.mobile.common.AppResultCode;
import com.rd.ifaes.mobile.controller.WebController;
import com.rd.ifaes.mobile.model.PagedData;
import com.rd.ifaes.mobile.model.account.log.RepaymentLogItemModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的借款-还款列表
 *
 * @author xhf
 * @version 3.0
 * @date 2016年7月27日
 */
@Controller
public class MyRepaymentController extends WebController {

    @Resource
    private ProjectService projectService;
    @Resource
    private ProjectRepaymentService projectRepaymentService;

    /**
     * 还款列表页面
     *
     * @return
     * @author xhf
     * @date 2016年7月27日
     */
    @RequestMapping(value = "/member/myRepayment/list")
    public String list() {
        return "/member/myRepayment/list";
    }

    /**
     * 获得还款列表记录
     *
     * @return
     * @author xiaodingjiang
     * @date 2016年10月26日
     */
    @RequestMapping(value = "/app/member/myRepayment/getLogList")
    @ResponseBody
    public Object getLogList(ProjectRepayment model, HttpServletRequest request) {
        Object obj = null;
        try {
            Page<ProjectRepayment> pages = new Page<ProjectRepayment>();
            int pageNo = model.getPage().getPage();
            User user = getAppSessionUser(request);
            //model.setPage(pages);
            model.setUserId(user.getUuid());
            model.setRepayTypeAdvance(LoanEnum.REPAY_TYPE_ADVANCE.getValue());
            if (RepaymentEnum.STATUS_NOT_REPAY.eq(model.getStatus())) {
                model.getPage().setSort("repay_time");
                model.getPage().setOrder(Constant.ASC);
            }
            if (RepaymentEnum.STATUS_REPAID.eq(model.getStatus())) {
                model.getPage().setSort("real_repay_time");
                model.getPage().setOrder(Constant.DESC);
            }

            pages = projectRepaymentService
                    .findByDate(model);
            List<ProjectRepayment> repaymentLogList = pages.getRows();
            PagedData<RepaymentLogItemModel> pirlist = new PagedData<RepaymentLogItemModel>();
            pages.setPage(pageNo);
            if (repaymentLogList != null) {
                pages.setPageSize(pages.getRows().size());
                fillPageDatas(pirlist, pages);
                for (ProjectRepayment projectRepayment : repaymentLogList) {
                    RepaymentLogItemModel logModel = new RepaymentLogItemModel();
                    projectRepayment.getUuid();
                    logModel.setCapital(projectRepayment.getCapital());//应还本金
                    logModel.setPeriodsStr(projectRepayment.getPeriodsStr());//期数
                    logModel.setPeriod(projectRepayment.getPeriod());//第几期
                    logModel.setPeriods(projectRepayment.getPeriods());//总期数
                    logModel.setRepayTime(projectRepayment.getRepayTime());//预计还款时间
                    logModel.setStatusStr(projectRepayment.getStatusStr());//还款状态
                    logModel.setInterest(projectRepayment.getInterest());//应还利息
                    logModel.setLateInterest(projectRepayment.getLateInterestSum());//逾期罚息
                    logModel.setStatus(projectRepayment.getStatus());//还款状态  0未还；1已还  -1（前面还有期数没有还款，不能进行还款操作）
                    logModel.setProjectName(projectRepayment.getProjectName());//项目名称
                    logModel.setRealRepayTime(projectRepayment.getRealRepayTime());//实际还款时间
                    logModel.setRepaymentTypeStr(projectRepayment.getRepaymentTypeStr());//还款计划-类型
                    logModel.setPayment(projectRepayment.getPayment());//应还款金额
                    logModel.setUuid(projectRepayment.getUuid());//uuid
                    logModel.setProjectId(projectRepayment.getProjectId());//项目id
                    logModel.setStatusTypeStr(projectRepayment.getStatusTypeStr());//状态备注
                    pirlist.getList().add(logModel);
                }
            }
            obj = createSuccessAppResponse(pirlist);
        } catch (Exception e) {
            obj = dealException(e);
        }
        return obj;
    }

    /**
     * 还款初始化
     *
     * @return
     * @author xiaodingjiang
     * @date 2016年11月4日
     */
    @RequestMapping(value = "/app/member/myRepayment/toRepay")
    @ResponseBody
    public Object toRepay(HttpServletRequest request, String projectId, String uuid, Integer period) {
        Object obj = null;
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            @SuppressWarnings("unused")
            User user = getAppSessionUser(request);
            //根据项目ID和期数查找未还待还记录
            ProjectRepayment projectRepay = projectRepaymentService.findByProjectIdAndPeriod(projectId, period);
            if (projectRepay == null) {
                throw new AppException(AppResultCode.ERROR, "该期借款已经还款,请不要重复操作");
            }
            ProjectRepayment prepayment = projectRepaymentService.get(uuid);
            data.put("capital", prepayment.getCapital());//应还本金
            data.put("interest", prepayment.getInterest());//应还利息
            data.put("lateInterest", prepayment.getLateInterest().add(prepayment.getMerchantLateInterest()));//逾期罚息(给平台逾期利息+逾期利息)
            BigDecimal amount = prepayment.getCapital().add(prepayment.getInterest()).add(prepayment.getLateInterest()).add(prepayment.getMerchantLateInterest());
            data.put("amount", amount);//应还金额
            data.put("uuid", prepayment.getUuid());//主键
            data.put("repayToken", TokenUtils.setToken(request.getSession(), TokenConstants.TOKEN_REPAY));//还款token
            obj = createSuccessAppResponse(data);
        } catch (Exception e) {
            obj = dealException(e);
        }
        return obj;
    }


    /**
     * 用户还款--获取手机验证码
     *
     * @return 发送结果
     * @author xiaodingjiang
     * @date 2016年10月26日
     */
    @RequestMapping(value = "/app/member/myRepayment/getRepayCode")
    @ResponseBody
    public Object getRepayCode(String repaymentId) {
        Object obj = null;
        try {
            User currentUser = getSessionUser();
            sendRepayMessage(currentUser, currentUser.getMobile(), MessageConstant.MESSAGE_USER_REPAY_PHONECODE, repaymentId);
            obj = createSuccessAppResponse("验证码已发送");
        } catch (Exception e) {
            obj = dealException(e);
        }
        return obj;
    }


    /**
     * 用户还款
     *
     * @return 处理结果
     * @author xiaodingjiang
     * @date 2016年10月26日
     */
    @RequestMapping(value = "/app/member/myRepayment/doRepay")
    @ResponseBody
    @TokenValid(value = TokenConstants.TOKEN_REPAY, dispatch = true)
    public Object doRepay(String uuid, String code) {
        Object obj = null;
        try {
    /*	String uuid=paramString("uuid");//借款还款记录表 中 uuid
		String code=paramString("code");*/
            User currentUser = getSessionUser();
            projectRepaymentService.repay(uuid, currentUser.getMobile(), code, currentUser.getUuid());
            obj = createSuccessAppResponse("还款成功");
        } catch (Exception e) {
            obj = dealException(e);
        }
        return obj;
    }
}
