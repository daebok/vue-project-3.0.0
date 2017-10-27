package com.rd.ifaes.manage.tpp;

import javax.annotation.Resource;

import com.rd.ifaes.common.dict.TppEnum;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.tpp.service.tpp.JxbankService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.tpp.domain.TppTrade;
import com.rd.ifaes.core.tpp.service.tpp.TppTradeService;
import com.rd.ifaes.manage.sys.SystemController;


/**
 * 第三方交易调度任务
 *
 * @author lh
 * @version 3.0
 * @since 2016-9-27
 */
@Controller
public class UfxManageController extends SystemController {

    private static final String PLATFORM_NAME = "平台";

    @Resource
    private TppTradeService tppTradeService;
    @Resource
    private JxbankService jxbankService;

    /**
     * 管理界面
     *
     * @return
     */
    @RequestMapping(value = "/tpp/ufx/tppManage")
    @SystemLog(method = SysLogConstant.QUERY, content = SysLogConstant.TPP_TRADE)
    public String manage(Model model) {
        return "/tpp/ufx/tppManage";
    }


    /**
     * 列表
     *
     * @param entity
     * @return
     */
    @RequestMapping(value = "/tpp/ufx/list")
    @ResponseBody
    @SystemLog(method = SysLogConstant.QUERY, content = SysLogConstant.TPP_TRADE)
    public Page<TppTrade> list(TppTrade tppTrade) {
        if (PLATFORM_NAME.equals(tppTrade.getRealName())) {
            tppTrade.setRealName(null);
            tppTrade.setTppUserCustId(ConfigUtils.getValue(ConfigConstant.UFX_CUSTOMERID));
        }
        if (PLATFORM_NAME.equals(tppTrade.getToRealName())) {
            tppTrade.setToRealName(null);
            tppTrade.setToTppUserCustId(ConfigUtils.getValue(ConfigConstant.UFX_CUSTOMERID));
        }
        if (PLATFORM_NAME.equals(tppTrade.getKeywords())) {
            tppTrade.setKeywords(null);
            tppTrade.setUserCustId(ConfigUtils.getValue(ConfigConstant.UFX_CUSTOMERID));
        }
        return tppTradeService.findRecordPage(tppTrade);
    }


    /**
     * 调度任务
     *
     * @param entity
     * @return
     */
    @RequiresPermissions("set:control:edit")
    @RequestMapping(value = "/tpp/ufx/dispatchTask")
    @ResponseBody
    @SystemLog(method = SysLogConstant.DO, content = SysLogConstant.TPP_TRADE)
    public Object dispatchTask(String[] ids) {
        tppTradeService.doTppTask(ids);
        return renderSuccessResult();
    }

    /**
     * 调度任务
     *
     * @param entity
     * @return
     */
    @RequiresPermissions("set:control:edit")
    @RequestMapping(value = "/tpp/jxbank/doJxTppTask")
    @ResponseBody
    @SystemLog(method = SysLogConstant.DO, content = SysLogConstant.TPP_TRADE)
    public Object doJxTppTask(String id) {
        String rst = tppTradeService.doJxTppTask(id);
        if (StringUtils.isBlank(rst)) {
            return renderSuccessResult();
        } else {
            return renderResult(false, rst);
        }
    }

    /**
     * 查询批次交易明细状态
     *
     * @param id
     * @return
     */
    @RequiresPermissions("set:control:edit")
    @RequestMapping(value = "/tpp/jxbank/batchDetailsQuery")
    @ResponseBody
    @SystemLog(method = SysLogConstant.DO, content = SysLogConstant.TPP_TRADE)
    public Object batchDetailsQuery(String id) {
        TppTrade tppTrade = tppTradeService.get(id);
        if (tppTrade == null) {
            return renderResult(false, "找不到该交易！");
        }
        String rst = "";
        if (TppEnum.SERVICE_TYPE_BATCH_VOUCHER.getValue().equals(tppTrade.getTppType())) {
            rst = jxbankService.jxBatchVoucherDetailsHandle(tppTrade);
        } else {
            rst = jxbankService.jxBatchDetailsHandle(tppTrade);
        }
        if (StringUtils.isBlank(rst)) {
            return renderSuccessResult();
        } else {
            return renderResult(false, rst);
        }
    }

}
