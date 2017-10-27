package com.rd.ifaes.web.controller.member;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.dict.RepaymentEnum;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.TokenUtils;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.MessageConstant;
import com.rd.ifaes.core.project.domain.ProjectRepayment;
import com.rd.ifaes.core.project.service.ProjectRepaymentService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.web.controller.WebController;

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
    public String list(final HttpServletRequest request) {
        TokenUtils.setToken(request.getSession(), TokenConstants.TOKEN_REPAY);
        return "/member/myRepayment/list";
    }

    /**
     * 获得还款列表记录
     *
     * @return
     * @author xhf
     * @date 2016年7月27日
     */
    @RequestMapping(value = "/member/myRepayment/getLogList")
    @ResponseBody
    public Object getLogList(ProjectRepayment model) {
        User user = SessionUtils.getSessionUser();
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
        Map<String, Object> data = new HashMap<>();
        data.put("result", true);
        data.put("data", projectRepaymentService.findByDate(model));
        return data;
    }

    /**
     * 用户还款--获取手机验证码
     *
     * @return 发送结果
     * @author FangJun
     * @date 2016年8月9日
     */
    @RequestMapping(value = "/member/myRepayment/getRepayCode")
    @ResponseBody
    public Object getRepayCode(String repaymentId) {
        User currentUser = getSessionUser();
        sendRepayMessage(currentUser, currentUser.getMobile(), MessageConstant.MESSAGE_USER_REPAY_PHONECODE, repaymentId);
        return renderSuccessResult();
    }


    /**
     * 用户还款
     *
     * @return 处理结果
     * @author FangJun
     * @date 2016年8月9日
     */
    @RequestMapping(value = "/member/myRepayment/doRepay")
    @ResponseBody
    @TokenValid(value = TokenConstants.TOKEN_REPAY, dispatch = true)
    public Object doRepay(String uuid, String code) {
        User currentUser = getSessionUser();
        projectRepaymentService.repay(uuid, currentUser.getMobile(), code, currentUser.getUuid());
        return renderSuccessResult();
    }

    /**
     * 用户还担保垫付款
     *
     * @return 处理结果
     * @author wt
     * @date 2017年9月18日
     */
    @RequestMapping(value = "/member/myRepayment/doRepayBail")
    @ResponseBody
    @TokenValid(value = TokenConstants.TOKEN_REPAY, dispatch = true)
    public Object doRepayBail(String uuid, String code) {
        User currentUser = getSessionUser();
        projectRepaymentService.repayBail(uuid, currentUser.getMobile(), code, currentUser.getUuid());
        return renderSuccessResult();
    }
}
