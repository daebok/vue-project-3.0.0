/**
 * 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.web.controller.bond;

import com.rd.ifaes.common.dict.BondEnum;
import com.rd.ifaes.common.dict.BondInvestEnum;
import com.rd.ifaes.common.dict.InvestEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.ip.IPUtil;
import com.rd.ifaes.common.util.resource.BondResource;
import com.rd.ifaes.core.bond.domain.Bond;
import com.rd.ifaes.core.bond.model.BondInvestModel;
import com.rd.ifaes.core.bond.model.BondModel;
import com.rd.ifaes.core.bond.service.BondInvestService;
import com.rd.ifaes.core.bond.service.BondService;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.URLConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.tpp.model.jx.JxCreditInvestModel;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.web.controller.WebController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 债权专区  和 债权详情
 *
 * @author QianPengZhan
 * @version 3.0
 * @date 2016年7月26日
 */
@Controller
public class BondController extends WebController {
    /**
     * 债权投资业务
     */
    @Resource
    private transient BondInvestService bondInvestService;

    /**
     * 债权业务
     */
    @Resource
    private transient BondService bondService;

    /**
     * 借款标业务
     */
    @Resource
    private transient ProjectService projectService;

    /**
     * 进入债权列表页面
     *
     * @return
     * @author QianPengZhan
     * @date 2016年8月2日
     */
    @RequestMapping("/bond/index")
    public String index() {
        return "/bond/index";
    }

    /**
     * 获取债权专区数据
     *
     * @param model
     * @return
     * @author QianPengZhan
     * @date 2016年8月2日
     */
    @RequestMapping("/bond/bondListData")
    @ResponseBody
    public Object bondListData(final BondModel bondModel, HttpServletRequest request) {
        final Map<String, Object> data = renderSuccessResult();
        if (bondModel.getPage() == null) {
            bondModel.setPage(new Page<BondModel>());
        }
        long time1 = System.currentTimeMillis();
        data.put(RESULT_PAGE, bondService.findPageData(bondModel));
        long time2 = System.currentTimeMillis();
        LOGGER.info("债权专区耗时：{},time2：{}", (time2 - time1) / 1000.0d, time2);

        return data;
    }

    /**
     * 债权专区筛选条件
     *
     * @return
     * @author QianPengZhan
     * @date 2016年8月24日
     */
    @RequestMapping(value = "/bond/queryBondCondition")
    @ResponseBody
    public Object queryBondCondition() {
        final Map<String, Object> data = renderSuccessResult();
        data.put("condition", bondService.queryBondCondition());
        return data;
    }

    /**
     * 进入债权详情页面
     *
     * @param id
     * @return
     * @author QianPengZhan
     * @date 2016年8月2日
     */
    @RequestMapping("/bond/detail")
    public String index(final String id, final Model model) {
        //进入页面 freemarker渲染参数
        final String backUrl = URLConstant.BOND_HOME_PAGE;
        final Bond bond = bondService.getBond(id, backUrl);//缓存中取出债权标
        if (BondEnum.STATUS_CANCLE_AUTO.eq(bond.getStatus()) || BondEnum.STATUS_CANCLE_MANGE.eq(bond.getStatus())
                || BondEnum.STATUS_CANCLE_WEB.eq(bond.getStatus())) {
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_NOT_EXISTS), backUrl, BussinessException.TYPE_JSON);
        }
        model.addAttribute("id", id);
        if (StringUtils.isBlank(bond.getProjectId())) {
            throw new BussinessException(ResourceUtils.get(BondResource.PROJECT_ID_NOT_EXISTS), backUrl, BussinessException.TYPE_JSON);
        }
        model.addAttribute("projectId", bond.getProjectId());
        return "/bond/detail";
    }

    /**
     * 债权详情页面上的债权和项目的详情
     *
     * @return
     * @author QianPengZhan
     * @date 2016年8月2日
     */
    @RequestMapping(value = "/bond/getBondProjectDetail")
    @ResponseBody
    public Object getBondProjectDetail(@RequestParam final String id) {
        final String backUrl = URLConstant.BOND_DETAIL_PAGE_PREFIX + id;
        final User user = (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put(RESULT_NAME, true);
        data.put(MSG_NAME, MSG_SUCCESS);
        data.put("currentTime", DateUtils.getNow());//当前时间
        if (user == null) {
            data.putAll(bondService.getBondProjectDetail(id, backUrl));//追加参数
        } else {
            data.putAll(bondService.getBondProjectDetail(id, user, backUrl));//追加参数
        }
        return data;
    }

    /**
     * 根据输入金额获取预期收益和实际支付金额
     *
     * @param amount 输入的受让本金
     * @param id     债权标ID
     * @return
     * @author QianPengZhan
     * @date 2016年8月2日
     */
    @RequestMapping(value = "/bond/getInvestData")
    @ResponseBody
    public Object getInvestData(@RequestParam("amount") final BigDecimal amount, @RequestParam("id") final String id) {
        final Map<String, Object> data = renderSuccessResult();
        data.putAll(bondService.getInvestData(amount, id));
        return data;
    }

    /**
     * 获取债权投资记录的接口
     *
     * @param id 债权标ID
     * @return
     * @author QianPengZhan
     * @date 2016年8月2日
     */
    @RequestMapping(value = "/bond/getBondInvestPage")
    @ResponseBody
    public Object getBondInvestPage(@RequestParam("id") final String id) {
        final Map<String, Object> data = renderSuccessResult();
        if (StringUtils.isNotBlank(id)) {
            final BondInvestModel entity = new BondInvestModel();
            entity.setBondId(id);
            entity.setStatus(BondInvestEnum.STATUS_COMPLETE.getValue());
            data.put(RESULT_PAGE, bondInvestService.findPageModel(entity));
            return data;
        }
        throw new BussinessException(ResourceUtils.get(BondResource.BOND_ID_NOT_EXISTS), BussinessException.TYPE_JSON);
    }


    /**
     * 进入债权订单确认页面
     *
     * @param model
     * @return
     * @author QianPengZhan
     * @date 2016年8月3日
     */
    @RequestMapping(value = "/bond/order")
    public String order(final BondInvestModel invest, final Model model) {
        final String backUrl = URLConstant.BOND_DETAIL_PAGE_PREFIX + invest.getBondId();
        //1、 用户登录状态
        final User user = (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
        if (user == null) {
            return "/user/login";
        }
        invest.setUserId(user.getUuid());

        //2、校验信息
        final Bond bond = bondService.checkBondInfo(invest, backUrl);
        //查询其他信息  缓存获取
        final Project project = projectService.getProjectValid(bond.getProjectId(), backUrl);

        //3、风险评估校验
        bondInvestService.validUserRiskTip(invest, user, project, backUrl);
        final BondModel bondModel = bondInvestService.getBondModel(bond, project);

        //4、债权规则 是否全部受让
        final boolean isBuyAll = bondModel.judgeIfBuyAll();

        //5、freemarker渲染页面
        model.addAttribute("investTimeOut",
                ConfigUtils.getInt(ConfigConstant.INVEST_TIMEOUT, ConfigConstant.INVEST_DEFAULT_BOND_ORDER_TIME_OUT));//订单的时效  默认10分钟   不能低于10分钟
        model.addAttribute("project", project);
        model.addAttribute("bondModel", bondModel);
        model.addAttribute("amount", invest.getAmount());
        model.addAttribute("receivedAccount", invest.getReceivedAccount());
        model.addAttribute("isBuyAll", isBuyAll);//是否全部受让
        return "/bond/order";
    }

    /**
     * 受让人进行债权投资
     *
     * @return
     * @author QianPengZhan
     * @date 2016年7月28日
     */
    @RequestMapping(value = "/bond/doBondInvest", method = RequestMethod.POST)
    public String doBondInvest(final BondInvestModel invest, final HttpServletRequest request, final Model model) {
        String url = StringUtils.EMPTY;
        final User user = (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
        if (user == null) {
            url = "/user/login";
        } else {
            final String requestIp = IPUtil.getRemortIP(request);//投资获取IP
            invest.setAddIp(requestIp);
            invest.setInvestChannel(InvestEnum.INVEST_CHANNEL_PC.getValue());//投资渠道为PC
            JxCreditInvestModel jxModel = (JxCreditInvestModel) bondInvestService.doBondInvest(invest, user);
            model.addAttribute("model", jxModel);
            model.addAttribute("sign", jxModel.getSign());
            url = ConfigUtils.getTppName() + "/bondInvest";
        }
        return url;
    }


}
