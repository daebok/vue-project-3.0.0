package com.rd.ifaes.core.tpp.controller.jxbank;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.rd.ifaes.core.account.service.CashService;
import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.core.constant.CallbackConstant;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.tpp.model.jx.JxWithdrawModel;
import com.rd.ifaes.core.tpp.service.tpp.JxbankService;
import com.rd.ifaes.core.user.service.UserCacheService;

@Controller
public class JxCashController extends BaseController {
    /**
     * 日志跟踪器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JxCashController.class);

    /**
     * 提现处理类
     */
    @Resource
    private transient CashService cashService;

    /**
     * 用户附属信息类
     */
    @Resource
    private transient UserCacheService userCacheService;
    
    @Resource
    private JxbankService jxbankService;


    /**
     * 提现同步回调
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jxbank/cash/return")
    public String tppReturn(final String bgData, final HttpServletRequest request, final Model model) {
        LOGGER.info("取现({})进入同步回调", getRequestParams(request));
//        final JxWithdrawModel jxModel = JSON.parseObject(StringUtils.isNull(bgData), JxWithdrawModel.class);
//        final Cash cash = cashService.findByOrderNo(jxModel.getOrderNo());
        String userAgent = request.getHeader("User-Agent") == null ? "" : request.getHeader("User-Agent").toLowerCase();//判断访问来源 是否是移动端
        if (userAgent.contains("micromessenger") || userAgent.contains("android") || userAgent.contains("iphone")) {//移动端
//            if (null == cash) {//失败
//                model.addAttribute("r_msg", ResourceUtils.get(ResourceConstant.ORDER_NOT_EXIST));
//                model.addAttribute("res_code", "register_fail");
//                return "/mobileRetresult";
//            } else {//成功
                model.addAttribute("res_msg", "提现处理完成");
                model.addAttribute("res_code", "cash_success");
                model.addAttribute("mobile_url", ConfigUtils.getValue(ConfigConstant.MOBILE_URL));
                return "/mobileResult";
//            }
        } else {
//            if (null == cash) {
//                throw new UfxException(ResourceUtils.get(ResourceConstant.ORDER_NOT_EXIST));
//            }
            // 担保机构单独跳转页面
//            UserCache userCache = userCacheService.findByUserId(cash.getUserId());
//            if (userCache != null && UserCache.USER_NATURE_VOUCH.equals(userCache.getUserNature())) {
//                model.addAttribute("resultUrl", CallbackConstant.CASH_VOUCH_REQUEST_URL);
//            } else {
                model.addAttribute("resultUrl", CallbackConstant.CASH_REQUEST_URL);
//            }
            model.addAttribute("picType", CallbackConstant.CASH_PICTYPE);
            model.addAttribute("requestType", CallbackConstant.CASH_REQUEST_TYPE);
            model.addAttribute("resultName", CallbackConstant.CASH_REQUEST_NAME);
            return "/retResult";
        }
    }

    /**
     * 提现异步回调
     *
     * @throws Exception
     */
    @RequestMapping(value = "/jxbank/cash/notify")
    public void tppNotify(final String bgData, final HttpServletRequest request, final HttpServletResponse response) {
        LOGGER.info("取现({})进入异步回调", getRequestParams(request));
        final JxWithdrawModel model = JSON.parseObject(bgData, JxWithdrawModel.class);
        if (model.responseVerify()) {
        	jxbankService.doCash(model);
        } else {
            LOGGER.error("验签失败：{}", model.getOrderNo());
        }
        printSuccessReturn(response);
    }
}
