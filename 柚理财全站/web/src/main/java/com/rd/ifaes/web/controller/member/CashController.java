package com.rd.ifaes.web.controller.member;

import java.math.BigDecimal;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.rd.account.domain.Account;
import com.rd.account.model.AccountQueryModel;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.dict.InvestEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.NumberUtils;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.ip.IPUtil;
import com.rd.ifaes.core.account.model.CashModel;
import com.rd.ifaes.core.account.service.CashService;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.tpp.model.jx.JxWithdrawModel;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.domain.UserIdentify;
import com.rd.ifaes.core.user.service.UserIdentifyService;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.web.controller.WebController;

/**
 * 我的账户-充值提现-提现
 *
 * @author xhf
 * @version 3.0
 * @date 2016年7月27日
 */
@Controller
public class CashController extends WebController {

    /**
     * 提现处理类
     */
    @Resource
    private transient CashService cashService;
    /**
     * 认证信息类
     */
    @Resource
    private transient UserIdentifyService identifyService;
    /**
     * 账户处理类
     */
    @Resource
    private transient AccountService accountService;
    @Resource
	private transient UserService userService;

    /**
     * 获得提现记录类
     *
     * @param model
     * @return
     * @author xhf
     * @date 2016年8月26日
     */
    @RequestMapping(value = "/member/cash/getLogList")
    @ResponseBody
    public Object getLogList(final CashModel model) {
        final Map<String, Object> data = Maps.newHashMap();
        data.put("result", true);
        data.put("data", cashService.findByDate(model));
        return data;
    }

    /**
     * 提现详情页面
     *
     * @return
     * @author xhf
     * @date 2016年7月27日
     */
    @RequestMapping(value = "/member/cash/detail")
    public String detail(final Model model) {
        handleDetail(model);
        return "/member/cash/detail";
    }

    /**
     * 企业提现详情页面
     *
     * @return
     * @author xhf
     * @date 2016年7月27日
     */
    @RequestMapping(value = "/member/cash/companyDetail")
    public String companyDetail(final Model model) {
        handleDetail(model);
        return "/member/cash/companyDetail";
    }

    /**
     * 重复区域代码处理
     *
     * @author QianPengZhan
     * @date 2016年9月13日
     */
    private void handleDetail(final Model model) {
        final User sessionUser = SessionUtils.getSessionUser(BussinessException.TYPE_CLOSE);
        final User user = userService.get(sessionUser.getUuid());
		// 判断是否是个人用户
		if (!UserCache.USER_NATURE_PERSON.equals(user.getUserNature())) {
			throw new BussinessException("非个人账户提现请在线下进行！", BussinessException.TYPE_JSON);
		}
        final Account account = accountService.getMoneyByUserId(new AccountQueryModel(user.getUuid(), ConfigUtils.getValue(Constant.ACCOUNT_CODE)));
        final UserIdentify userIdentify = identifyService.findByUserId(user.getUuid());

        //可用余额
        BigDecimal useMoney = BigDecimal.ZERO;
        if (null != account.getUseMoney()) {
            useMoney = account.getUseMoney();
        }
        model.addAttribute("userMoney", useMoney);
        //平台今日可提现金额
        BigDecimal webCashMoney = cashService.getSingleDayRemainCashAmount(user.getUuid());
        //第三方可提现余额
        BigDecimal tppCashMoney = BigDecimal.ZERO;
        if (useMoney.compareTo(BigDecimal.ZERO) > 0 && webCashMoney.compareTo(BigDecimal.ZERO) > 0) {
            tppCashMoney = cashService.getTppCashAmount(user.getUuid());
        }

        //可提现金额
        BigDecimal cashMoney = useMoney;
        if (cashMoney.compareTo(webCashMoney) > 0) {
            cashMoney = webCashMoney;
        }
        if (tppCashMoney != null && cashMoney.compareTo(tppCashMoney) > 0) {
            cashMoney = tppCashMoney;
        }
        model.addAttribute("cashMoney", cashMoney);

        //单日提现次数限制
        model.addAttribute("cashSingleDayTimeLimit", ConfigUtils.getInt(Constant.CASH_DAY_TIME_LIMIT));
        //今日已提现次数
        model.addAttribute("todayDoCashTimes", cashService.getSingleDaytime(user.getUuid()));
        //单笔提现额度限制
        model.addAttribute("cashSingleMaxAmountLimit", ConfigUtils.getBigDecimal(Constant.CASH_SINGE_MAX_AMNT));
        //单日提现额度限制
        model.addAttribute("cashSingleDayMaxAmountLimit", ConfigUtils.getBigDecimal(Constant.CASH_DAY_MAX_AMNT));
        //单笔最小提现金额
        model.addAttribute("cashMinAmount", ConfigUtils.getBigDecimal(Constant.CASH_MIN_AMNT));
        //实名认证
        model.addAttribute("realNameStatus", userIdentify.getRealNameStatus());
        model.addAttribute("payPwdStatus", user.getPayPwd());
    }

    /**
     * 提现详情页面 -担保用户
     *
     * @return
     * @author xhf
     * @date 2016年7月27日
     */
    @RequestMapping(value = "/member/cash/vouchDetail")
    public String vouchDetail(final Model model) {
        handleDetail(model);
        return "/member/cash/vouchDetail";
    }

    /**
     * 提现（跳转第三方）
     *
     * @return
     * @author xhf
     * @date 2016年7月27日
     */
    @RequestMapping(value = "/member/cash/doCash")
    public String doCash(final String amount, final String bankNo, final String bankCode, final String routeCode, final String cardBankCnaps,
                         final HttpServletRequest request, final Model model) {
        if (StringUtils.isBlank(routeCode) && NumberUtils.getBigDecimal(amount).compareTo(new BigDecimal(50000)) > 0) {
            throw new BussinessException("提现金额不能超过50000！", BussinessException.TYPE_CLOSE);
        }
        if(CashModel.ROUTE_CODE_WHOLESALE.equals(routeCode)){
			//大额通道必须输入12位的cardBankCnaps
			if(StringUtils.isBlank(cardBankCnaps) || cardBankCnaps.trim().length() != 12){
				throw new BussinessException("人行通道提现请输入12位绑定银行联行号", BussinessException.TYPE_CLOSE);
			}
		}
        final CashModel cash = new CashModel();
        cash.setAmount(NumberUtils.getBigDecimal(amount));
        cash.setAddIp(IPUtil.getRemortIP(request));
        cash.setCardId(bankNo);
        cash.setBankCode(bankCode);
        cash.setUser(getSessionUser());
        cash.setChannel(InvestEnum.INVEST_CHANNEL_PC.getValue());
        cash.setRouteCode(routeCode);
        cash.setCardBankCnaps(cardBankCnaps);

        JxWithdrawModel apiCash = (JxWithdrawModel) cashService.doCash(cash);
        model.addAttribute("apiCash", apiCash);
        model.addAttribute("sign", apiCash.getSign());
        return ConfigUtils.getTppName() + "/cash";
    }

    /**
     * 校验是否可充值
     *
     * @return
     * @author xhf
     * @date 2016年7月27日
     */
    @RequestMapping(value = "/member/cash/check")
    @ResponseBody
    public Object check() {
        identifyService.validIdentifyForRecharge();
        return renderSuccessResult();
    }

}
