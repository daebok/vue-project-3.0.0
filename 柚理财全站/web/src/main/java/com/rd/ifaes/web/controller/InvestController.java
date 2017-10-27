/**
 * 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.web.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.rd.ifaes.core.tpp.model.jx.JxBidApplyModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxInvestModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxProjectApplyModel;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.account.domain.Account;
import com.rd.account.model.AccountQueryModel;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.constant.TppServiceEnum;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.dict.InvestEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TokenUtils;
import com.rd.ifaes.common.util.TppUtil;
import com.rd.ifaes.common.util.ip.IPUtil;
import com.rd.ifaes.common.util.resource.LoanResource;
import com.rd.ifaes.common.util.resource.UserResource;
import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.core.constant.CallbackConstant;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ProjectConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.constant.URLConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.DictUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.model.ProjectInvestModel;
import com.rd.ifaes.core.project.model.ProjectInvestRecord;
import com.rd.ifaes.core.project.model.ProjectRecord;
import com.rd.ifaes.core.project.service.ProjectInvestBespeakService;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.project.service.ProjectTypeService;
import com.rd.ifaes.core.risk.domain.LevelConfig;
import com.rd.ifaes.core.risk.service.LevelConfigService;
import com.rd.ifaes.core.risk.service.RiskUserLogService;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbBackInvestModel;
import com.rd.ifaes.core.tpp.service.TppService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.model.UserFreezeModel;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserFreezeService;
import com.rd.ifaes.core.user.service.UserService;

/**
 * 理财频道
 *
 * @author FangJun
 * @date 2016年7月11日
 */
@Controller
public class InvestController extends BaseController {
    @Resource
    private ProjectInvestService projectInvestService;
    @Resource
    private ProjectService projectService;
    @Resource
    private ProjectTypeService projectTypeService;
    @Resource
    private AccountService accountService;
    @Resource
    private UserFreezeService userFreezeService;
    @Resource
    private RiskUserLogService riskUserLogService;
    @Resource
    private ProjectInvestBespeakService bespeakService;
    @Resource
    private UserService userService;

    /**
     * LevelConfigService业务
     */
    @Resource
    private transient LevelConfigService levelConfigService;
    /**
     * UserCacheService业务
     */
    @Resource
    private transient UserCacheService userCacheService;

    /**
     * 理财频道首页
     *
     * @return
     */
    @RequestMapping(value = "/invest/index")
    public String index(String projectTypeId, Model model) {
        if (StringUtils.isNotBlank(projectTypeId)) {
            model.addAttribute("projectTypeId", projectTypeId);
        }
        return "/invest/index";
    }

    /**
     * 项目分类列表
     *
     * @return
     */
    @RequestMapping(value = "/invest/projectTypeList")
    @ResponseBody
    public Object projectTypeList(String parentId) {
        final Map<String, Object> data = renderSuccessResult();
        data.put(RESULT_LIST, projectTypeService.findChilds(parentId));
        return data;
    }


    /**
     * 投资项目列表
     *
     * @return
     */
    @RequestMapping(value = "/invest/projectList")
    @ResponseBody
    public Object projectList(final ProjectRecord project) {
        if (LOGGER.isDebugEnabled()) {
            final StringBuilder buffer = new StringBuilder();
            buffer.append("产品列表查询: projectTypeId:").append(project.getProjectTypeId())
                    .append(",amountCondition:").append(project.getAmountCondition())
                    .append(",timeCondition:").append(project.getTimeCondition())
                    .append(",repayStyle:").append(project.getRepayStyle());
            if (project.getPage() != null) {
                buffer.append(",sort:").append(project.getPage().getSort()).append(",order:").append(project.getPage().getOrder());
            }
            LOGGER.debug(buffer.toString());
        }
        final Map<String, Object> data = renderSuccessResult();
        project.setRealizeFlag(CommonEnum.NO.getValue());
        project.setSaleChannel(InvestEnum.INVEST_CHANNEL_PC.getValue());
        //开售倒计时使用
        data.put("systemTime", System.currentTimeMillis());
        if (project.getPage() == null) {
            project.setPage(new Page<ProjectRecord>());
        }
        
        //定向销售类型是定向用户的标的登录与未登录的需要分开查看，解决用户看到其他用户的缓存数据（将列表缓存数据根据用户类型分为三类）
        User user = (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
		if (user != null) {
			user = userService.get(user.getUuid());//那最新的账号值，避免session中存储的值不是最新的
			//用户已经登录
			if(StringUtils.isBlank(user.getAccountType())){
				project.setUserAccountType(User.ACCOUNT_TYPE_NULL);
			}
			project.setUserAccountType(user.getAccountType());
		}else{
			project.setUserAccountType(User.ACCOUNT_TYPE_NULL);
		}
        Page<ProjectRecord> page = projectService.findProjectPage(project);
        //预约信息处理
        if (user != null) {
            List<String> bespeakProjectIds = bespeakService.getUserBespeakProjectIds(user.getUuid());
            if (CollectionUtils.isNotEmpty(bespeakProjectIds)) {
                for (ProjectRecord record : page.getRows()) {
                    record.setBespeak(bespeakProjectIds.contains(record.getUuid()) ?
                            Integer.valueOf(Constant.INT_ONE) : Integer.valueOf(Constant.INT_ZERO));
                }
            }
        }
        data.put(RESULT_PAGE, page);
        return data;
    }

    /**
     * 产品的详情页
     *
     * @param uuid 产品UUID
     * @return
     */
    @RequestMapping(value = URLConstant.INVEST_DETAIL_PAGE_MVC)
    public String detail(String id, Model model, String preview) {
        if (StringUtils.isBlank(id)) {
            throw new BussinessException(ResourceUtils.get(LoanResource.PROJECT_ID_IS_NULL), BussinessException.TYPE_JSON);
        }
        final Project project = projectService.get(id);
        if(LoanEnum.SPECIFIC_SALE_USER.getValue().equals(project.getSpecificSale())){
        	User user = (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
			if (user != null) {
				user = userService.get(user.getUuid());//那最新的账号值，避免session中存储的值不是最新的
				if(user.getUuid().equals(project.getUserId())){
					//当前借款人的标，不做处理，可以查看
				}else if(StringUtils.isBlank(user.getAccountType()) || !user.getAccountType().equals(project.getSpecificSaleRule())){
					throw new BussinessException("抱歉，当前标的只开放给" + DictUtils.getItemName(DictTypeEnum.USER_ACCOUNT_TYPE.getValue(), project.getSpecificSaleRule()) + "！");
				}
			}else{
				throw new BussinessException("当前标的为定向用户标，请登录后查看！");
			}
        }
        model.addAttribute("projectId", id);
        model.addAttribute("preview", preview);
        return URLConstant.INVEST_DETAIL_PAGE_MVC;
    }

    /**
     * 风险评估提示接口
     *
     * @param projectId
     * @param isSelectedTip 过滤风险继续投资
     * @return
     * @author QianPengZhan
     * @date 2016年9月19日
     */
    @RequestMapping(value = "/invest/userRiskLevelTip")
    @ResponseBody
    public Object userRiskLevelTip(final String projectId, final int isSelectedTip) {
        final User user = (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
        String msg = ResourceUtils.get(UserResource.USER_NOT_LOGIN);
        String userLevelName = StringUtils.EMPTY;
        String projectLevelName = StringUtils.EMPTY;
        boolean isTip = false;
        boolean isNeedDoRisk = false;
        if (user != null) {
            final UserCache userCache = userCacheService.findByUserId(user.getUuid());
            if (riskUserLogService.getCountByUserId(user.getUuid()) <= Constant.INT_ZERO) {//若评测记录为空 则说明没有做过任何评估
                isNeedDoRisk = true;
            }
            final Project project = projectService.getProjectByUuid(projectId);
            final LevelConfig levelConfigUser = levelConfigService.findListByUserRiskLevelVal(NumberUtils.toInt(userCache.getRiskLevel()));
            final LevelConfig levelConfigProject = levelConfigService.findByRiskLevel(project.getRiskLevel());
            if (isSelectedTip == Constant.INT_ZERO &&
                    (NumberUtils.toInt(project.getRiskLevel()) - 1 > NumberUtils.toInt(userCache.getRiskLevel()))) {
                isTip = true;
            }
            msg = StringUtils.EMPTY;
            if (levelConfigUser != null && levelConfigProject != null) {
                userLevelName = levelConfigUser.getUserRiskLevelName();
                projectLevelName = levelConfigProject.getUserRiskLevelName().concat("及以上");
            }
        }
        final Map<String, Object> map = this.renderResult(true, msg);
        map.put("isNeedDoRisk", isNeedDoRisk);//是否需要重新评估
        map.put("riskLevel", userLevelName);//您的承受风险类型
        map.put("userRiskLevel", projectLevelName);//项目所需风险承受能力   xxx及以上
        map.put("isTip", isTip);//是否需要提示

        return map;
    }

    /**
     * 投资订单页面
     *
     * @return 订单页面URL
     */
    @RequestMapping(value = "/invest/order") // ,method=RequestMethod.POST
    public String order(ProjectInvestModel invest, Model model, final HttpServletRequest request) {
        // 用户登录状态
        User user = (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
        if (user == null) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_NOT_LOGIN), BussinessException.TYPE_CLOSE);
        }
        if (userFreezeService.isFreezed(user.getUuid(), UserFreezeModel.STATUS_USER_FREEZE_INVEST)) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_FREEZE_INVEST_FREEZED), BussinessException.TYPE_CLOSE);
        }
        Object pwdVerifyFlag = request.getSession().getAttribute(String.format(ProjectConstant.PROJECT_PASS_RIGHT_PROJECT_ID, invest.getProjectId()));
        if (pwdVerifyFlag != null) {
            invest.setPwdHasVerify(true);
        }
        final Project project = projectInvestService.checkSpecificSale(invest, user);
        //校验投资准备情况
        projectInvestService.checkOrder(project, user, invest.getAmount());

        model.addAttribute("project", project);
        model.addAttribute("amount", invest.getAmount());
        model.addAttribute("investTimeout", ConfigUtils.getInt(ConfigConstant.INVEST_TIMEOUT, ConfigConstant.INVEST_DEFAULT_ORDER_TIME_OUT));
        BigDecimal redEnvelopeRateMax = project.getRedEnvelopeRate();
        if (CommonEnum.YES.eq(project.getRedEnvelopeUseful()) && !BigDecimalUtils.validAmount(redEnvelopeRateMax)) {
            redEnvelopeRateMax = ConfigUtils.getBigDecimal(ConfigConstant.INVEST_REDENVELOPE_RATE);
        }
        model.addAttribute("redEnvelopeRateMax", redEnvelopeRateMax);

        Account account = accountService.getMoneyByUserId(new AccountQueryModel(user.getUuid(), ConfigUtils.getValue(Constant.ACCOUNT_CODE)));
        model.addAttribute("useMoney", account.getUseMoney() == null ? BigDecimal.ZERO : account.getUseMoney());
        TokenUtils.setToken(request.getSession(), TokenConstants.TOKEN_INVEST);
        return "/invest/order";
    }

    /**
     * 指定产品的投资记录
     *
     * @param model 投资记录查询信息
     * @return 投资记录列表
     */
    @RequestMapping("/invest/recordList")
    @ResponseBody
    public Object recordList(ProjectInvestRecord model) {
        final Map<String, Object> data = renderSuccessResult();
        data.put(RESULT_PAGE, projectInvestService.findPageForDetail(model));
        return data;
    }

    /**
     * 投资订单确认，新建投资记录，跳转三方支付页面
     *
     * @param invest 投资信息
     * @param model
     * @return UFX投资报文Form页面
     * @author FangJun
     * @date 2016年7月23日
     */
    @RequestMapping("/invest/doInvest")
    public String doInvest(ProjectInvestModel invest, Model model, HttpServletRequest request) {
        invest.setAddIp(IPUtil.getRemortIP(request));
        //投资方式和投资渠道，需要在WEB层设置
        invest.setInvestType(InvestEnum.INVEST_TYPE_WEB.getValue());
        invest.setInvestChannel(InvestEnum.INVEST_CHANNEL_PC.getValue());
        String tppName = ConfigUtils.getTppName();
        // 用户登录状态
        User user = (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
        if (user == null) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_NOT_LOGIN), BussinessException.TYPE_CLOSE);
        } else {
            invest.setUserId(user.getUuid());
        }
        //是否校验过定向密码
        if (request.getSession().getAttribute(
                String.format(ProjectConstant.PROJECT_PASS_RIGHT_PROJECT_ID, invest.getProjectId())) != null) {
            invest.setPwdHasVerify(true);
        }
        if (TppServiceEnum.CHINAPNR.getName().equals(tppName)) {
            if (TokenUtils.validToken(request, TokenConstants.TOKEN_INVEST)) {
                TokenUtils.clearToken(request, TokenConstants.TOKEN_INVEST);
            } else {
                throw new BussinessException(ResourceUtils.get(ResourceConstant.REPEAT_SUBMIT_ERROR), BussinessException.TYPE_CLOSE);
            }
            model.addAttribute("invest", projectInvestService.doInvest(invest));
            return tppName + "/invest";
        } else if (TppServiceEnum.CBHB.getName().equals(tppName)) {
            String errorMsg = null;//错误信息
            String picType = CallbackConstant.PAY_PICTYPE;
            CbhbBackInvestModel cbhbModel = projectInvestService.doCbhbInvest(invest);
            if (CbhbConstant.CBHB_CODE_SUCCESS.equals(cbhbModel.getRespCode())) {
                model.addAttribute("buttonUrl", URLConstant.INVEST_DETAIL_PAGE_PREFIX + invest.getProjectId()); // 点击按钮跳转页面地址
                model.addAttribute("resultName", CallbackConstant.PAY_REQUEST_NAME);
                model.addAttribute("resultUrl", URLConstant.MEMBER_MYINVEST_PAGE);
                model.addAttribute("buttonName", CallbackConstant.PAY_BUTTON_NAME);
            } else {
                picType = CallbackConstant.ERROR_PICTYPE;
                errorMsg = cbhbModel.getRespDesc();
            }
            model.addAttribute("picType", picType);
            model.addAttribute("requestType", CallbackConstant.PAY_REQUEST_TYPE);
            model.addAttribute("otherUrl", URLConstant.INVEST_HOME_PAGE); // 点击"其它产品"跳转页面地址
            model.addAttribute("errorMsg", errorMsg);
            return "/retResult";
        } else if (TppServiceEnum.JXBANK.getName().equals(tppName)) {
            if (TokenUtils.validToken(request, TokenConstants.TOKEN_INVEST)) {
                TokenUtils.clearToken(request, TokenConstants.TOKEN_INVEST);
            } else {
                throw new BussinessException(ResourceUtils.get(ResourceConstant.REPEAT_SUBMIT_ERROR), BussinessException.TYPE_CLOSE);
            }
            JxBidApplyModel jxmodel = (JxBidApplyModel) projectInvestService.doInvest(invest);
            model.addAttribute("model", jxmodel);
            model.addAttribute("sign", jxmodel.getSign());
            return tppName + "/invest";
        }
        return "";
    }

    /**
     * 获取验证码
     *
     * @return
     * @author ZhangBiao
     * @date 2017年3月1日
     */
    @RequestMapping("/invest/getCode")
    @ResponseBody
    public Object getCode() {
        final Map<String, Object> data = renderSuccessResult();
        TppService tppService = (TppService) TppUtil.tppService();
        User user = (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
        if (user == null) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_NOT_LOGIN), BussinessException.TYPE_CLOSE);
        } else {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("mobileNo", user.getMobile());
            data.put("code", tppService.tppSendUapMsg(params).toString());
        }
        return data;
    }

    /**
     * 渤海银行投资接口
     *
     * @param model
     * @return
     * @author ZhangBiao
     * @date 2017年3月1日
     */
    @RequestMapping("/invest/cbhbDoInvest")
    @ResponseBody
    public Object cbhbDoInvest(ProjectInvestModel invest, Model model, HttpServletRequest request) {
        final Map<String, Object> data = renderSuccessResult();
        invest.setAddIp(IPUtil.getRemortIP(request));
        //投资方式和投资渠道，需要在WEB层设置
        invest.setInvestType(InvestEnum.INVEST_TYPE_WEB.getValue());
        invest.setInvestChannel(InvestEnum.INVEST_CHANNEL_PC.getValue());

        // 用户登录状态
        User user = (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
        if (user == null) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_NOT_LOGIN), BussinessException.TYPE_CLOSE);
        } else {
            invest.setUserId(user.getUuid());
        }
        //是否校验过定向密码
        if (request.getSession().getAttribute(
                String.format(ProjectConstant.PROJECT_PASS_RIGHT_PROJECT_ID, invest.getProjectId())) != null) {
            invest.setPwdHasVerify(true);
        }
        CbhbBackInvestModel result = projectInvestService.doCbhbInvest(invest);
        if (CbhbConstant.CBHB_CODE_SUCCESS.equals(result.getRespCode())) {
            if (TokenUtils.validToken(request, TokenConstants.TOKEN_INVEST)) {
                TokenUtils.clearToken(request, TokenConstants.TOKEN_INVEST);
            } else {
                throw new BussinessException(ResourceUtils.get(ResourceConstant.REPEAT_SUBMIT_ERROR), BussinessException.TYPE_CLOSE);
            }
        }
        return data;
    }

    /**
     * 计算投资预计收益
     *
     * @param invest 投资信息：投资项目ID、投资金额
     * @return 预计收益
     * @author FangJun
     * @date 2016年7月29日
     */
    @RequestMapping(value = "/invest/interest")
    @ResponseBody
    public Object interest(ProjectInvestModel invest) {
        final Map<String, Object> data = renderSuccessResult();
        data.put("interest", projectInvestService.calcInterest(invest));
        // 用户登录
        User user = (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
        if (user != null) {
            invest.setUserId(user.getUuid());
            data.put("userInvestAmount", projectInvestService.countUserInvestProject(invest));
        }
        return data;
    }

    /**
     * 指定类别对应的产品列表过滤条件(产品类别、产品金额、产品期限、收益方式）
     *
     * @return
     * @author FangJun
     * @date 2016年8月11日
     */
    @RequestMapping(value = "/invest/queryCondition")
    @ResponseBody
    public Object queryCondition(String projectTypeId) {
        final Map<String, Object> data = renderSuccessResult();
        data.put("condition", projectTypeService.queryCondition(projectTypeId));
        return data;
    }

    /**
     * 渤海银行  ---  投资用户前台在满标之前手动撤销投资   暂时不接入
     *
     * @return
     * @author QianPengZhan
     * @date 2017年3月6日
     */
    @Deprecated
    @RequestMapping("/invest/investCancle")
    public Object investCancle(final Model model, final HttpServletRequest request, final ProjectInvestModel invest) {
        final Map<String, Object> data = renderSuccessResult();
        boolean flag = projectInvestService.cbhbInvestCancle(invest);
        if (!flag) {
            data.put(RESULT_NAME, false);
            data.put(MSG_NAME, MSG_FAIL);
        }
        return data;
    }


}
