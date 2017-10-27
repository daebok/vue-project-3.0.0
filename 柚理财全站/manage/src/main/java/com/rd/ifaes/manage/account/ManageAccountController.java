package com.rd.ifaes.manage.account;

import com.rd.account.domain.Account;
import com.rd.account.domain.AccountLog;
import com.rd.account.model.AccountQueryModel;
import com.rd.account.service.AccountLogService;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.constant.TppServiceEnum;
import com.rd.ifaes.common.constant.UfxConstant;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TppUtil;
import com.rd.ifaes.common.util.excel.ExportModel;
import com.rd.ifaes.common.util.excel.ExportUtil;
import com.rd.ifaes.common.util.ip.IPUtil;
import com.rd.ifaes.core.account.domain.AccountCheck;
import com.rd.ifaes.core.account.domain.TppMerchantLog;
import com.rd.ifaes.core.account.model.TppMerchantLogModel;
import com.rd.ifaes.core.account.service.AccountCheckService;
import com.rd.ifaes.core.account.service.RechargeService;
import com.rd.ifaes.core.account.service.TppMerchantLogService;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.tpp.model.cbhb.xml.XmlQueryMerchantAccts;
import com.rd.ifaes.core.tpp.model.jx.json.JxAccountDetailsQuerySubPacksModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxCashModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxQueryBalanceModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxRechargeModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxSubAccountModel;
import com.rd.ifaes.core.tpp.service.TppService;
import com.rd.ifaes.core.tpp.service.tpp.JxbankService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.model.UserModel;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.manage.sys.SystemController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ManageAccountController extends SystemController {

    @Resource
    private AccountService accountService;
    @Resource
    private AccountLogService accountLogService;
    @Resource
    private UserCacheService userCacheService;
    @Resource
    private UserService userService;
    @Resource
    private AccountCheckService accountCheckService;
    @Resource
    private TppMerchantLogService tppMerchantLogService;
    @Resource
    private RechargeService rechargeService;
    @Resource
    private JxbankService jxbankService;


    /**
     * 列表页面
     */
    @RequestMapping(value = "/account/account/accountList")
    @SystemLog(method = SysLogConstant.QUERY, content = SysLogConstant.ACCOUNT_LIST)
    public String accountList() {
        return "/account/account/accountList";
    }

    /**
     * 列表
     *
     * @return
     */
    @RequestMapping(value = "/account/account/list")
    @ResponseBody
    @RequiresPermissions("account:userAccount:query")
    @SystemLog(method = SysLogConstant.QUERY, content = SysLogConstant.ACCOUNT_LIST)
    public Object list(final UserModel model) {
        return userService.findUserAccount(model);
    }

    /**
     * 用户资金
     *
     * @param id
     * @return
     * @throws Exception
     * @author jxx
     * @date 2016年8月15日
     */
    @RequestMapping(value = "/account/account/userAccount")
    @RequiresPermissions("account:userAccount:query")
    @SystemLog(method = SysLogConstant.QUERY, content = SysLogConstant.USER_ACCOUNT)
    public String userAccount(final String id, final Model model) {
        final User user = userService.get(id);
        final String tppUserCustId = user.getTppUserCustId();
        final Account account = accountService.getByUserId(new AccountQueryModel(id, ConfigUtils.getValue(Constant.ACCOUNT_CODE)));
        model.addAttribute("account", account);
        if (StringUtils.isNotBlank(tppUserCustId)) {
            final String tppName = ConfigUtils.getTppName();
            TppService tppCbhbService = (TppService) TppUtil.tppService();
            if (TppServiceEnum.CHINAPNR.getName().equals(tppName)) {
                final Map<String, Object> ufxMap = new HashMap<>();
                ufxMap.put("userCustId", tppUserCustId);
                final UserCache userCache = userCacheService.findByUserId(id);
                ufxMap.put("userType", userCache.getUserNature());
                final UfxQueryBalanceModel queryBalanceModel = (UfxQueryBalanceModel) tppCbhbService.tppQueryBalance(ufxMap);
                model.addAttribute("data", queryBalanceModel);
            } else if (TppServiceEnum.CBHB.getName().equals(tppName)) {
                //第三方可提现余额  暂时无   默认可用余额
                final Map<String, Object> cbhbMap = new HashMap<>();
                cbhbMap.put("plaCustId", user.getTppUserCustId());
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (HashMap<String, Object>) tppCbhbService.tppQueryBalance(cbhbMap);
                model.addAttribute("data", map);
            } else if (TppServiceEnum.JXBANK.getName().equals(tppName)) {
                //第三方可提现余额  暂时无   默认可用余额
                final Map<String, Object> jxMap = new HashMap<>();
                jxMap.put("accountId", user.getTppUserCustId());
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (HashMap<String, Object>) tppCbhbService.tppQueryBalance(jxMap);
                model.addAttribute("data", map);
            } else {

            }
        }
        return "/account/account/userAccount";
    }


    /**
     * 列表页面
     */
    @RequestMapping(value = "/account/account/accountLogList")
    @SystemLog(method = SysLogConstant.QUERY, content = SysLogConstant.ACCOUNTLOG_LIST)
    public String accountLogList() {
        return "/account/account/accountLogList";
    }

    /**
     * 列表
     *
     * @return
     */
    @RequestMapping(value = "/account/account/logList")
    @ResponseBody
    @RequiresPermissions("account:accountList:query")
    @SystemLog(method = SysLogConstant.QUERY, content = SysLogConstant.ACCOUNTLOG_LIST)
    public Object logList(final AccountLog accountLog) {

        Page<AccountLog> result = accountLogService.findManagePage(accountLog);
        List<AccountLog> list = result.getRows();
        for (AccountLog log : list) {
            log.setRemark(log.getRemark().replaceAll("href=\"/", "href=\"" + ConfigUtils.getValue(ConfigConstant.WEB_URL) + "/"));
        }
        return result;
    }


    /**
     * 导出记录
     *
     * @param model
     * @param request
     * @param response
     * @throws Exception
     * @author jxx
     * @date 2016年8月4日
     */
    @RequestMapping(value = "/account/account/logExportExcel")
    @RequiresPermissions("account:accountList:export")
    @SystemLog(method = SysLogConstant.EXPORT, content = SysLogConstant.ACCOUNTLOG_LIST)
    public void logExportExcel(final AccountLog model, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        ExportUtil.exportExcel(new ExportModel<AccountLog>() {
            @Override
            public String getCacheKey() {
                return ManageAccountController.this.getUserId();
            }

            @Override
            public AccountLog getModel() {
                return model;
            }

            @Override
            public int getTotal(AccountLog entity) {
                return accountLogService.getManageCount(entity);
            }

            @Override
            public List<AccountLog> getPageRecords(AccountLog entity) {
                return accountLogService.findManagePage(entity).getRows();
            }
        }, request, response);
    }

    /**
     * 列表页面
     */
    @RequestMapping(value = "/account/merchant/tppMerchantLogList")
    @SystemLog(method = SysLogConstant.QUERY, content = SysLogConstant.MERCHANT_LOG)
    public String tppMerchantLogList(final Model model) {
        model.addAttribute("tppName", ConfigUtils.getTppName());
        return "/account/merchant/tppMerchantLogList";
    }

    /**
     * 列表
     *
     * @return
     */
    @RequestMapping(value = "/account/merchant/merchantLogList")
    @ResponseBody
    @RequiresPermissions("account:platAccount:query")
    @SystemLog(method = SysLogConstant.QUERY, content = SysLogConstant.MERCHANT_LOG)
    public Object merchantLogList(final TppMerchantLogModel tppMerchantLog) {
        return tppMerchantLogService.findManagePage(tppMerchantLog);
    }

    @SuppressWarnings("unchecked")
    @RequiresPermissions("account:platAccount:query")
    @RequestMapping(value = "/account/merchant/merchantBalanceePage")
    @SystemLog(method = SysLogConstant.QUERY, content = SysLogConstant.MERCHANT_LOG)
    public String merchantBalanceePage(final Model model) {
        String tppName = ConfigUtils.getTppName();
        if (TppServiceEnum.CHINAPNR.getName().equals(tppName)) {
            if (StringUtils.isNotBlank(ConfigUtils.getValue(Constant.UFX_CUSTOMERID))) {
                final Map<String, Object> ufxMap = new HashMap<>();
                ufxMap.put("userCustId", ConfigUtils.getValue(Constant.UFX_CUSTOMERID));
                ufxMap.put("userType", UfxQueryBalanceModel.USER_TYPE_COMPANY);
                TppService tppService = (TppService) TppUtil.tppService();
                final UfxQueryBalanceModel queryBalanceModel = (UfxQueryBalanceModel) tppService.tppQueryBalance(ufxMap);
                model.addAttribute("data", queryBalanceModel);
            }
        } else if (TppServiceEnum.CBHB.getName().equals(tppName)) {
            final Map<String, Object> cbhbMap = new HashMap<>();
            TppService tppCbhbService = (TppService) TppUtil.tppService();
            List<XmlQueryMerchantAccts> list = (List<XmlQueryMerchantAccts>) tppCbhbService.queryMerchantAccts(cbhbMap);
            model.addAttribute("list", list);
        }
        return "/account/merchant/merchantBalanceePage";
    }

    /**
     * 第三方商户充值页面
     *
     * @return
     * @throws Exception
     * @author jxx
     * @date 2016年7月26日
     */
    @RequiresPermissions("account:platAccount:recharge")
    @RequestMapping("/account/merchant/merchantRechargePage")
    @SystemLog(method = SysLogConstant.VIEW, content = SysLogConstant.MERCHANT_RECHARGE)
    public String merchantRechargePage() throws Exception {
        if (UfxConstant.UFX_TPP_NAME_CHINAPNR.equals(ConfigUtils.getTppName())) {
            return "/account/merchant/merchantRechargePage";
        } else if (CbhbConstant.CBHB_TPP_NAME.endsWith(ConfigUtils.getTppName())) {
            return "/account/merchant/merchantCbhbRechargePage";
        }
        return "/account/merchant/merchantRechargePage";
    }


    /**
     * 第三方商户充值
     *
     * @return
     * @throws Exception
     * @author jxx
     * @date 2016年7月26日
     */
    @RequestMapping(value = "/account/merchant/merchantRecharge")
    @RequiresPermissions("account:platAccount:recharge")
    @SystemLog(method = SysLogConstant.DO, content = SysLogConstant.MERCHANT_RECHARGE)
    public String merchantRecharge(final TppMerchantLog tppMerchantLog,
                                   final HttpServletRequest request, final Model model) {
        LOGGER.info("商户充值...");

        tppMerchantLog.setAddIp(IPUtil.getRemortIP(request));
        tppMerchantLog.setOperatorId(getUser().getUuid());
        // 保存平台操作记录
        final UfxRechargeModel ufxRechargeModel = tppMerchantLogService.merchantRecharge(tppMerchantLog);
        model.addAttribute("recharge", ufxRechargeModel);
        return "/account/merchant/recharge";
    }

    /**
     * 渤海银行商户充值
     *
     * @return
     * @throws Exception
     * @author jxx
     * @date 2016年7月26日
     */
    @RequestMapping(value = "/account/merchant/cbhbMerchantRecharge")
    @ResponseBody
    @RequiresPermissions("account:platAccount:recharge")
    @SystemLog(method = SysLogConstant.DO, content = SysLogConstant.MERCHANT_RECHARGE)
    public Object cbhbMerchantRecharge(final TppMerchantLog tppMerchantLog,
                                       final HttpServletRequest request, final Model model) {
        LOGGER.info("商户充值...");

        tppMerchantLog.setAddIp(IPUtil.getRemortIP(request));
        tppMerchantLog.setOperatorId(getUser().getUuid());
        tppMerchantLogService.cbhbMerchantRecharge(tppMerchantLog);
        return this.renderSuccessResult();
    }


    /**
     * 第三方商户平台提现页面
     *
     * @return
     * @throws Exception
     * @author jxx
     * @date 2016年7月26日
     */
    @RequiresPermissions("account:platAccount:cash")
    @RequestMapping("/account/merchant/merchantCashPage")
    @SystemLog(method = SysLogConstant.VIEW, content = SysLogConstant.MERCHANT_CASH)
    public String merchantCashPage() throws Exception {
        if (UfxConstant.UFX_TPP_NAME_CHINAPNR.equals(ConfigUtils.getTppName())) {
            return "/account/merchant/merchantCashPage";
        } else if (CbhbConstant.CBHB_TPP_NAME.endsWith(ConfigUtils.getTppName())) {
            return "/account/merchant/merchantCbhbCashPage";
        }
        return "/account/merchant/merchantCashPage";
    }

    /**
     * 第三方商户平台提现
     *
     * @return
     * @throws Exception
     * @author jxx
     * @date 2016年7月26日
     */
    @RequestMapping(value = "/account/merchant/merchantCbhbCash")
    @RequiresPermissions("account:platAccount:cash")
    @ResponseBody
    @SystemLog(method = SysLogConstant.DO, content = SysLogConstant.MERCHANT_CASH)
    public Object merchantCbhbCash(TppMerchantLog tppMerchantLog, HttpServletRequest request, final Model model) {
        tppMerchantLog.setAddIp(IPUtil.getRemortIP(request));
        tppMerchantLog.setOperatorId(getUser().getUuid());
        // 保存平台操作记录
        tppMerchantLogService.merchantCbhbCash(tppMerchantLog);

        return this.renderSuccessResult();
    }

    /**
     * 渤海银行---商户平台提现
     *
     * @return
     * @throws Exception
     * @author jxx
     * @date 2016年7月26日
     */
    @RequestMapping(value = "/account/merchant/merchantCash")
    @RequiresPermissions("account:platAccount:cash")
    @SystemLog(method = SysLogConstant.DO, content = SysLogConstant.MERCHANT_CASH)
    public String merchantCash(TppMerchantLog tppMerchantLog, HttpServletRequest request, final Model model) {
        tppMerchantLog.setAddIp(IPUtil.getRemortIP(request));
        tppMerchantLog.setOperatorId(getUser().getUuid());
        // 保存平台操作记录
        UfxCashModel ufxCashModel = tppMerchantLogService.merchantCash(tppMerchantLog);

        model.addAttribute("apiCash", ufxCashModel);
        return "/account/merchant/cash";
    }

    /**
     * 第三方商户给用户转账页面
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/account/merchant/merchantTransferPage")
    @RequiresPermissions("account:platAccount:transfer")
    @SystemLog(method = SysLogConstant.VIEW, content = SysLogConstant.MERCHANT_TRANSFER)
    public String merchantTransferPage(final Model model) {

        List<UfxSubAccountModel> ufxSubAccounts = tppMerchantLogService.getSubAccountList();
        model.addAttribute("accts", ufxSubAccounts);
        return "/account/merchant/merchantTransferPage";
    }

    /**
     * 第三方商户给用户转账
     *
     * @throws Exception
     */
    @RequestMapping("/account/merchant/merchantTransfer")
    @ResponseBody
    @RequiresPermissions("account:platAccount:transfer")
    @SystemLog(method = SysLogConstant.DO, content = SysLogConstant.MERCHANT_TRANSFER)
    public Object merchantTransfer(TppMerchantLog tppMerchantLog, final String userName, final HttpServletRequest request) throws Exception {
        tppMerchantLog.setAddIp(IPUtil.getRemortIP(request));
        tppMerchantLog.setOperatorId(getUser().getUuid());

        String resultStr = tppMerchantLogService.merchantTransfer(tppMerchantLog, userName);
        if (StringUtils.isNotBlank(resultStr)) {
            return renderResult(false, resultStr);
        }
        return this.renderSuccessResult();
    }


    /**
     * 第三方商户平台账户间转账页面
     *
     * @return
     * @throws Exception
     * @author jxx
     * @date 2016年7月26日
     */
    @RequestMapping("/account/merchant/subMerchantTransferPage")
    @RequiresPermissions("account:platAccount:assign")
    @SystemLog(method = SysLogConstant.VIEW, content = SysLogConstant.SUB_MERCHANT_TRANSFER)
    public String subMerchantTransferPage(final Model model) {

        List<UfxSubAccountModel> ufxSubAccounts = tppMerchantLogService.getSubAccountList();
        model.addAttribute("accts", ufxSubAccounts);
        return "/account/merchant/subMerchantTransferPage";
    }


    /**
     * 第三方商户账户间转账
     *
     * @throws Exception
     * @author jxx
     * @date 2016年7月26日
     */
    @RequestMapping("/account/merchant/subMerchantTransfer")
    @ResponseBody
    @RequiresPermissions("account:platAccount:assign")
    @SystemLog(method = SysLogConstant.DO, content = SysLogConstant.SUB_MERCHANT_TRANSFER)
    public Object subMerchantTransfer(TppMerchantLog tppMerchantLog, final HttpServletRequest request) {

        tppMerchantLog.setAddIp(IPUtil.getRemortIP(request));
        tppMerchantLog.setOperatorId(getUser().getUuid());

        String resultStr = tppMerchantLogService.subMerchantTransfer(tppMerchantLog);
        if (StringUtils.isNotBlank(resultStr)) {
            return renderResult(false, resultStr);
        }
        return this.renderSuccessResult();
    }


    /**
     * 导出平台资金记录
     *
     * @param model
     * @param request
     * @param response
     * @throws Exception
     * @author jxx
     * @date 2016年8月4日
     */
    @RequestMapping(value = "/account/merchant/exportExcel")
    //@RequiresPermissions("account:platAccount:export")
    @SystemLog(method = SysLogConstant.EXPORT, content = SysLogConstant.MERCHANT_LOG)
    public void merchantExportExcel(final TppMerchantLogModel model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ExportUtil.exportExcel(new ExportModel<TppMerchantLogModel>() {
            @Override
            public String getCacheKey() {
                return ManageAccountController.this.getUserId();
            }

            @Override
            public TppMerchantLogModel getModel() {
                return model;
            }

            @Override
            public int getTotal(TppMerchantLogModel entity) {
                return tppMerchantLogService.getManageCount(entity);
            }

            @Override
            public List<TppMerchantLog> getPageRecords(TppMerchantLogModel entity) {
                return tppMerchantLogService.findManagePage(entity).getRows();
            }
        }, request, response);
    }


    /**
     * 资金对账记录列表页面
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/account/account/accountCheckList")
    @SystemLog(method = SysLogConstant.QUERY, content = SysLogConstant.ACCOUNT_CHECK)
    public String accountCheckList() throws Exception {
        return "/account/account/accountCheckList";
    }

    /**
     * 资金对账记录列表
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/account/account/checkList")
    @ResponseBody
    @SystemLog(method = SysLogConstant.QUERY, content = SysLogConstant.ACCOUNT_CHECK)
    public Object checkList(AccountCheck accountCheck) throws Exception {
        return accountCheckService.findManagePage(accountCheck);
    }

    @RequestMapping(value = "/account/account/doAutoQueryErrorBalance")
    @SystemLog(method = SysLogConstant.DO, content = SysLogConstant.ACCOUNT_CHECK)
    public Object doAutoQueryErrorBalance() {
        //资金对账
        accountCheckService.doAutoQueryErrorBalance();
        LOGGER.info("资金对账OK..");
        return null;
    }

    /**
     * 账户导出记录
     *
     * @param model
     * @param request
     * @param response
     * @throws Exception
     * @author xxb
     */
    @RequestMapping(value = "/account/account/export")
    @SystemLog(method = SysLogConstant.EXPORT, content = SysLogConstant.ACCOUNT_LIST)
    public void accoutExportExcel(final UserModel model, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        ExportUtil.exportExcel(new ExportModel<UserModel>() {
            @Override
            public String getCacheKey() {
                return ManageAccountController.this.getUserId();
            }

            @Override
            public UserModel getModel() {
                return model;
            }

            @Override
            public int getTotal(UserModel entity) {
                return userService.getAccountUserCount(entity);
            }

            @Override
            public List<User> getPageRecords(UserModel entity) {
                return userService.findUserAccount(entity).getRows();
            }
        }, request, response);
    }

    /**
     * 列表页面
     */
    @RequestMapping(value = "/account/account/accountDetailsQuery")
    @SystemLog(method = SysLogConstant.QUERY, content = SysLogConstant.ACCOUNTLOG_LIST)
    public String accountDetailsQuery() {
        return "/account/account/accountDetailsQuery";
    }

    /**
     * 列表
     *
     * @return
     */
    @RequestMapping(value = "/account/account/accountDetailsQueryList")
    @ResponseBody
    @RequiresPermissions("account:accountDetails:query")
    @SystemLog(method = SysLogConstant.QUERY, content = SysLogConstant.ACCOUNTLOG_LIST)
    public Object accountDetailsQueryList(final HttpServletRequest request, final Page<JxAccountDetailsQuerySubPacksModel> page) {
        Map<String, Object> map = new HashMap<>();
        User u = userService.getUserByMobile(request.getParameter("mobile"));
        if (u == null || StringUtils.isBlank(u.getTppUserAccId())) {
            return page;
        }
        map.put("accountId", u.getTppUserAccId());
        map.put("startDate", request.getParameter("startDate").replaceAll("-",""));
        map.put("endDate", request.getParameter("endDate").replaceAll("-",""));
        map.put("type", request.getParameter("type"));
        map.put("tranType", request.getParameter("tranType"));
        map.put("pageNum", page.getPage());
        map.put("pageSize", page.getPageSize());
        return jxbankService.accountDetailsQuery(map);
    }

}
