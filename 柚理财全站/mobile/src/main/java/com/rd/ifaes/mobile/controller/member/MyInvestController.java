package com.rd.ifaes.mobile.controller.member;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rd.ifaes.core.tpp.model.jx.JxBidApplyModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.dict.InvestEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectCollection;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.model.BuybackModel;
import com.rd.ifaes.core.project.model.MyProjectInvestModel;
import com.rd.ifaes.core.project.model.ProjectInvestModel;
import com.rd.ifaes.core.project.service.ProjectCollectionService;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.protocol.service.ProtocolService;
import com.rd.ifaes.core.tpp.model.ufx.UfxInvestModel;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.mobile.common.AppException;
import com.rd.ifaes.mobile.common.AppResultCode;
import com.rd.ifaes.mobile.controller.WebController;
import com.rd.ifaes.mobile.model.PagedData;
import com.rd.ifaes.mobile.model.account.ProjectCollectionModel;
import com.rd.ifaes.mobile.model.account.log.InvestLogItemModel;

/**
 * 我的资产-我的投资
 *
 * @author xiaodingjiang
 * @version 3.0
 * @date 2016年10月25日
 */
@Controller
public class MyInvestController extends WebController {
    /**
     * ERROR_LOAD
     */
    private static final String ERROR_LOAD = "下载出错,地址：{}";
    @Resource
    private transient ProjectService projectService;
    @Resource
    private transient ProjectInvestService projectInvestService;
    @Resource
    private transient ProjectCollectionService projectCollectionService;
    @Resource
    private transient ProtocolService protocolService;


    /**
     * 持有中
     *
     * @param model
     * @return
     * @author xiaodingjiang
     * @date 2016年10月25日
     */
    @RequestMapping("/app/member/myInvest/getBorrowHolding")
    @ResponseBody
    public Object getBorrowHolding(MyProjectInvestModel model, HttpServletRequest request) {
        Object obj;
        try {
            User user = getAppSessionUser(request);
            model.setUserId(user.getUuid());
            final String[] projectStatus = {LoanEnum.STATUS_REPAY_START.getValue(), LoanEnum.STATUS_REPAY_OVERDUE.getValue(), LoanEnum.STATUS_BAD_DEBT.getValue()};
            model.setProjectStatusSet(projectStatus);
            model.setType(MyProjectInvestModel.TYPE_HOLDING);
            model.setStatusSet(new String[]{InvestEnum.STATUS_SUCCESS.getValue(), InvestEnum.STATUS_DELETE.getValue()});//已转让的也显示
            model.setInvestStyle(InvestEnum.INVEST_STYLE_BUY.getValue());//受让投资不显示
            final Page<MyProjectInvestModel> investList = projectInvestService.myProjectInvestList(model);
            List<MyProjectInvestModel> borrowHoldingList = investList.getRows();
            PagedData<InvestLogItemModel> pirlist = new PagedData<InvestLogItemModel>();
            fillPageData(pirlist, investList);
            for (MyProjectInvestModel projectInvestModel : borrowHoldingList) {
                InvestLogItemModel investModel = new InvestLogItemModel();
                investModel.setAmount(projectInvestModel.getAmount());//投资金额
                investModel.setCreateTime(projectInvestModel.getCreateTime());//投标添加时间
                investModel.setInterest(projectInvestModel.getInterest());// 预期利息
                investModel.setInvestType(projectInvestModel.getInvestType());//投资方式  0手动投资;1自动投资
                investModel.setNextRepayDate(projectInvestModel.getNextRepayDate());// 下一次还款日
                investModel.setPayment(projectInvestModel.getPayment());//预收本息
                investModel.setProjectName(projectInvestModel.getProjectName());//项目名称
                investModel.setProjectStatus(projectInvestModel.getProjectStatus());//项目状态
                investModel.setRemainDays(projectInvestModel.getRemainDays());// 剩余期限
                investModel.setRepayedAmount(projectInvestModel.getRepayedAmount());//已还金额
                investModel.setRepayedInterest(projectInvestModel.getRepayedInterest());//已还利息（实际收益）
                investModel.setStatus(projectInvestModel.getStatus());//投资状态 0:待支付 1:投资成功 2:投资失败 3：退款处理中 4：投资作废 5：超时取消 6：订单失效
                investModel.setWaitAmount(projectInvestModel.getWaitAmount());//待还总额
                investModel.setWaitInterest(projectInvestModel.getWaitInterest());//待还利息
                investModel.setInterestDate(projectInvestModel.getInterestDate());//起息日
                investModel.setEndDate(projectInvestModel.getEndDate());//结束日
                investModel.setUuid(projectInvestModel.getUuid());//uuid(对应回款计划中的investId 以及协议下载)
                //investModel.setProtocolUrl(downloadInvestProtocol(projectInvestModel.getUuid()));//投资人下载协议
                investModel.setProjectId(projectInvestModel.getProjectId());//项目id
                investModel.setRealizeFlag(projectInvestModel.getRealizeFlag());//变现借款标识( 1是产品变现后发布借款，0 普通借款，默认0)
                //投资状态说明
                if (projectInvestModel.getStatus().equals("0")) {
                    investModel.setStatusStr(InvestEnum.STATUS_NEW.getName());
                } else if (projectInvestModel.getStatus().equals("1")) {
                    //投资成功对应改为支付成功
                    investModel.setStatusStr("支付成功");
                } else if (projectInvestModel.getStatus().equals("2") || projectInvestModel.getStatus().equals("3") || projectInvestModel.getStatus().equals("4")) {
                    //投资失败,退款处理中,投资作废 对应改为申请失败
                    investModel.setStatusStr("申请失败");
                } else if (projectInvestModel.getStatus().equals("5")) {
                    investModel.setStatusStr(InvestEnum.STATUS_TIMEOUT.getName());
                } else {
                    investModel.setStatusStr(InvestEnum.STATUS_CANCEL.getName());
                }
                pirlist.getList().add(investModel);
            }
            obj = createSuccessAppResponse(pirlist);
        } catch (Exception e) {
            obj = dealException(e);
        }
        return obj;
    }

    /**
     * 投资申请
     *
     * @param model
     * @return
     * @author xiaodingjiang
     * @date 2016年10月25日
     */
    @RequestMapping("/app/member/myInvest/getInvestApply")
    @ResponseBody
    public Object getInvestApply(MyProjectInvestModel model, HttpServletRequest request) {
        Object obj = null;
        try {
            User user = getAppSessionUser(request);
            model.setUserId(user.getUuid());
            final String[] projectStatus = {LoanEnum.STATUS_RAISING.getValue(), LoanEnum.STATUS_RAISE_FINISHED.getValue(), LoanEnum.STATUS_RAISE_FINISHED.getValue(), LoanEnum.STATUS_NOT_ESTABLISH.getValue()};
            model.setProjectStatusSet(projectStatus);
            model.setType(MyProjectInvestModel.TYPE_APPLY);
            Page<MyProjectInvestModel> investList = projectInvestService.myProjectInvestList(model);
            List<MyProjectInvestModel> projectInvestList = investList.getRows();
            PagedData<InvestLogItemModel> pirlist = new PagedData<InvestLogItemModel>();
            fillPageData(pirlist, investList);
            for (MyProjectInvestModel projectInvestModel : projectInvestList) {
                InvestLogItemModel investModel = new InvestLogItemModel();
                investModel.setAmount(projectInvestModel.getAmount());//投资金额
                investModel.setCreateTime(projectInvestModel.getCreateTime());//投标添加时间
                investModel.setInterest(projectInvestModel.getInterest());// 预期利息
                investModel.setInvestType(projectInvestModel.getInvestType());//投资方式  0手动投资;1自动投资
                investModel.setNextRepayDate(projectInvestModel.getNextRepayDate());// 下一次还款日
                investModel.setPayment(projectInvestModel.getPayment());//预收本息
                investModel.setProjectName(projectInvestModel.getProjectName());//项目名称
                investModel.setProjectStatus(projectInvestModel.getProjectStatus());//项目状态
                investModel.setRemainDays(projectInvestModel.getRemainDays());// 剩余期限
                investModel.setRepayedAmount(projectInvestModel.getRepayedAmount());//已还金额
                investModel.setRepayedInterest(projectInvestModel.getRepayedInterest());//已还利息（实际收益）
                investModel.setStatus(projectInvestModel.getStatus());//投资状态 0:待支付 1:投资成功 2:投资失败 3：退款处理中 4：投资作废 5：超时取消 6：订单失效
                investModel.setWaitAmount(projectInvestModel.getWaitAmount());//待还总额
                investModel.setWaitInterest(projectInvestModel.getWaitInterest());//待还利息
                investModel.setInterestDate(projectInvestModel.getInterestDate());//起息日
                investModel.setEndDate(projectInvestModel.getEndDate());//结束日
                investModel.setUuid(projectInvestModel.getUuid());//uuid(对应回款计划中的investId)
                investModel.setProjectId(projectInvestModel.getProjectId());//项目id
                investModel.setRealizeFlag(projectInvestModel.getRealizeFlag());//变现借款标识( 1是产品变现后发布借款，0 普通借款，默认0)
                //投资状态说明
                if (projectInvestModel.getStatus().equals("0")) {
                    investModel.setStatusStr(InvestEnum.STATUS_NEW.getName());
                    investModel.setRemainTimes(projectInvestModel.getRemainTimes());//剩余支付时间
                } else if (projectInvestModel.getStatus().equals("1")) {
                    investModel.setStatusStr("支付成功");
                } else if (projectInvestModel.getStatus().equals("2") || projectInvestModel.getStatus().equals("3")) {
                    investModel.setStatusStr("申请失败");
                } else if (projectInvestModel.getStatus().equals("4")) {
                    investModel.setStatusStr(InvestEnum.STATUS_DELETE.getName());
                } else if (projectInvestModel.getStatus().equals("5")) {
                    investModel.setStatusStr("支付失败");
                } else {
                    investModel.setStatusStr(InvestEnum.STATUS_CANCEL.getName());
                }
                pirlist.getList().add(investModel);
            }
            obj = createSuccessAppResponse(pirlist);
        } catch (Exception e) {
            obj = dealException(e);
        }
        return obj;
    }

    /**
     * 赎回申请
     *
     * @return
     * @author fxl
     * @date 2016年8月17日
     */
    @RequestMapping("/member/myInvest/getRedemptionApply")
    @ResponseBody
    public Object getRedemptionApply() {
        final List<BuybackModel> investList = new ArrayList<>();
        final BuybackModel model = new BuybackModel();
        model.setProjectName("融地盈-20160917");  //产品名称
        model.setMoney(1000.00);                  //赎回金额
        model.setApplyTime(DateUtils.getNow());   //赎回申请时间
        model.setStatus("01");                    //赎回状态
        investList.add(model);
        final Map<String, Object> data = new HashMap<>();
        data.put(RESULT_NAME, true);
        data.put("data", investList);
        return data;
    }

    /**
     * 已结束
     *
     * @param model
     * @return
     * @author xiaodingjiang
     * @date 2016年10月25日
     */
    @RequestMapping("/app/member/myInvest/getInvestClosed")
    @ResponseBody
    public Object getInvestClosed(MyProjectInvestModel model, HttpServletRequest request) {
        Object obj = null;

        try {
            User user = getAppSessionUser(request);
            model.setUserId(user.getUuid());
            final String[] projectStatus = {LoanEnum.STATUS_REPAYED_SUCCESS.getValue(), LoanEnum.STATUS_REPAYED_LATE.getValue()};
            final String[] statusSet = {InvestEnum.STATUS_DELETE.getValue(), InvestEnum.STATUS_SUCCESS.getValue()};
            model.setProjectStatusSet(projectStatus);
            model.setStatusSet(statusSet);
            model.setType(MyProjectInvestModel.TYPE_CLOSED);
            model.setInvestStyle(InvestEnum.INVEST_STYLE_BUY.getValue());//债权受让人的原始标投资记录不显示

            final Page<MyProjectInvestModel> investList = projectInvestService.myProjectInvestList(model);
            List<MyProjectInvestModel> investClosedList = investList.getRows();
            PagedData<InvestLogItemModel> pirlist = new PagedData<InvestLogItemModel>();
            fillPageData(pirlist, investList);
            for (MyProjectInvestModel projectInvestModel : investClosedList) {
                InvestLogItemModel investModel = new InvestLogItemModel();
                investModel.setAmount(projectInvestModel.getAmount());//投资金额
                investModel.setCreateTime(projectInvestModel.getCreateTime());//投标添加时间
                investModel.setInterest(projectInvestModel.getInterest());// 预期利息
                investModel.setInvestType(projectInvestModel.getInvestType());//投资方式  0手动投资;1自动投资
                investModel.setNextRepayDate(projectInvestModel.getNextRepayDate());// 下一次还款日
                investModel.setPayment(projectInvestModel.getPayment());//预收本息
                investModel.setProjectName(projectInvestModel.getProjectName());//项目名称
                investModel.setProjectStatus(projectInvestModel.getProjectStatus());//项目状态
                investModel.setRemainDays(projectInvestModel.getRemainDays());// 剩余期限
                investModel.setRepayedAmount(projectInvestModel.getRepayedAmount());//已还金额
                investModel.setRepayedInterest(projectInvestModel.getRepayedInterest());//已还利息（实际收益）
                investModel.setStatus(projectInvestModel.getStatus());//投资状态
                investModel.setWaitAmount(projectInvestModel.getWaitAmount());//待还总额
                investModel.setWaitInterest(projectInvestModel.getWaitInterest());//待还利息
                investModel.setInterestDate(projectInvestModel.getInterestDate());//起息日
                investModel.setEndDate(projectInvestModel.getEndDate());//结束日
                investModel.setUuid(projectInvestModel.getUuid());//uuid(对应回款计划中的investId)
                investModel.setProjectId(projectInvestModel.getProjectId());//项目id
                investModel.setRealizeFlag(projectInvestModel.getRealizeFlag());//变现借款标识( 1是产品变现后发布借款，0 普通借款，默认0)
                pirlist.getList().add(investModel);
            }
            obj = createSuccessAppResponse(pirlist);
        } catch (Exception e) {
            obj = dealException(e);
        }
        return obj;
    }

    /**
     * 回款计划页面
     *
     * @param investId
     * @return
     * @author fxl
     * @date 2016年8月17日
     */
    @RequestMapping(value = "/member/myInvest/projectCollection")
    public String projectCollection(final String investId, final Model model) {
        model.addAttribute("investId", investId);
        return "/member/myInvest/projectCollection";
    }

    /**
     * 回款计划数据
     *
     * @param model
     * @return
     * @author xiaodingjiang
     * @date 2016年10月21日
     */
    @RequestMapping(value = "/app/member/myInvest/getProjectCollectionList")
    @ResponseBody
    public Object getProjectCollectionList(ProjectCollection model, HttpServletRequest request) {
        Object obj = null;
        try {
            @SuppressWarnings("unused")
            User user = getAppSessionUser(request);
            Page<ProjectCollection> pages = projectCollectionService.findForPage(model);
            List<ProjectCollection> projectCollectionList = pages.getRows();
            PagedData<ProjectCollectionModel> pirlist = new PagedData<ProjectCollectionModel>();
            if (projectCollectionList != null) {
                pages.setPageSize(pages.getRows().size());
                fillPageDatas(pirlist, pages);
                for (ProjectCollection projectCollection : projectCollectionList) {
                    ProjectCollectionModel collectionModel = new ProjectCollectionModel();
                    collectionModel.setAddIp(projectCollection.getAddIp());//ip
                    collectionModel.setCapital(projectCollection.getCapital());//本金
                    collectionModel.setCollectionType(projectCollection.getCollectionType());///待收类型  0普通待收;1债权转让
                    collectionModel.setInterest(projectCollection.getInterest());//利息
                    collectionModel.setPeriodsStr(projectCollection.getPeriodsStr());//期数
                    if ("1".equals(projectCollection.getStatus())) {
                        collectionModel.setRepayedAmount(projectCollection.getRepayedAmount());//实际回款金额
                        collectionModel.setRepayTime(projectCollection.getLastRepayTime());//实际还款时间
                    } else {
                        collectionModel.setRepayedAmount(projectCollection.getPayment());//预计回款金额
                        collectionModel.setRepayTime(projectCollection.getRepayTime());//预计还款时间
                    }
                    //添加还款标识符为前端添加图标做依据
                    if (projectCollection.getRepayedAmountStr().equals("未到期")) {
                        collectionModel.setRepayedStatus("1");
                        collectionModel.setRepayedAmountStr("待收  " + projectCollection.getRepayTypeStr());//回款状态
                    } else if (projectCollection.getRepayedAmountStr().equals("已逾期")) {
                        collectionModel.setRepayedStatus("2");
                        collectionModel.setRepayedAmountStr("已逾期  " + projectCollection.getRepayTypeStr());//回款状态
                    } else {
                        collectionModel.setRepayedStatus("3");
                        collectionModel.setRepayedAmountStr("已收  " + projectCollection.getRepayTypeStr());//回款状态
                    }
                    pirlist.getList().add(collectionModel);
                }
            }
            obj = createSuccessAppResponse(pirlist);
        } catch (Exception e) {
            obj = dealException(e);
        }
        return obj;


    }

    /**
     * 去支付--移动端
     *
     * @param invest
     * @param model
     * @return
     * @author fxl
     * @date 2016年8月24日
     */
    @RequestMapping("/app/member/myInvest/doPay")
    public String doPay(final ProjectInvestModel invest, final Model model, HttpServletRequest request) {
        try {
            // 用户登录状态
            User user = getAppSessionUser(request);
            if (user == null) {
                throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_NOT_LOGIN), BussinessException.TYPE_CLOSE);
            } else {
                invest.setUserId(user.getUuid());
            }
            JxBidApplyModel jxmodel = (JxBidApplyModel) projectInvestService.doPay(invest);
            model.addAttribute("model", jxmodel);
            model.addAttribute("sign", jxmodel.getSign());
            return ConfigUtils.getTppName() + "/invest";
        } catch (Exception e) {
            model.addAttribute("r_msg", e.getMessage());
            return ConfigUtils.getTppName() + "/retresult";
        }
    }
    /**
     * 投资人下载协议
     * @author QianPengZhan
     * @date 2016年10月19日
     * @return
     * @throws IOException
     */
/*	@RequestMapping("/app/member/myInvest/downloadInvestProtocol")
    @ResponseBody
	public Object downloadInvestProtocol(final String investId,HttpServletRequest request) {
		LOGGER.info("进入投资款协议下载  .pdf接口...start");
		Map<String, Object> data = new HashMap<String, Object>();
		Object obj = null;
		try {
		User user = getAppSessionUser(request);
		ProjectInvest projectInvest = projectInvestService.get(investId);
		String projectId = projectInvest.getProjectId();
		Project project = projectService.get(projectId);
		
		String fileName = project.getProjectName() +"_"+project.getProjectNo()+ ".pdf";
		
		String resultStr = protocolService.downloadProtocol(projectId, investId, fileName);
		if(StringUtils.isNotBlank(resultStr)){
			LOGGER.info("进入投资协议下载  .pdf接口...end");
			data.put("protocolUrl", resultStr);
		}else{
			throw new AppException(AppResultCode.ERROR, "投资协议下载失败");
		}
		obj = createSuccessAppResponse(data);
		} catch (Exception e) {
			obj = dealException(e);
		}
		return obj;
	}*/

    /**
     * 移动端--普通投资借款协议预览
     *
     * @param protocolId
     * @param projectId
     * @return
     * @author QianPengZhan
     * @date 2017年02月10日
     */
    @RequestMapping("/app/member/myInvest/protocolSearch")
    public Object protocolSearch(final String protocolId, final String projectId, final Model model, HttpServletRequest request) {
        try {
            LOGGER.info("移动端进入借款协议预览接口...start");
            // 用户登录状态
            User user = getAppSessionUser(request);
            Map<String, Object> prosermap = protocolService.getInvestProtocol(user, projectId, protocolId);
            Iterator<String> it = prosermap.keySet().iterator();
            while (it.hasNext()) {
                Object key = it.next();
                if ("content".equals(key))
                    model.addAttribute("content", (String) prosermap.get(key));
            }
            model.addAttribute("titleName", "投资协议");
            return "/app/protocol/registerProtocol";
        } catch (Exception e) {
            model.addAttribute("r_msg", e.getMessage());
            return ConfigUtils.getTppName() + "/retresult";
        }
    }

    /**
     * 移动端--普通投资借款协议预览--微信
     *
     * @param protocolId
     * @param projectId
     * @return
     * @author QianPengZhan
     * @date 2017年02月10日
     */
    @RequestMapping("/app/member/myInvest/wxProtocolSearch")
    @ResponseBody
    public Object wxProtocolSearch(final String protocolId, final String projectId, final Model model, HttpServletRequest request) {
        Object obj = null;
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            LOGGER.info("移动端进入借款协议预览接口...start");
            // 用户登录状态
            User user = getAppSessionUser(request);
            Map<String, Object> prosermap = protocolService.getInvestProtocol(user, projectId, protocolId);
            Iterator<String> it = prosermap.keySet().iterator();
            while (it.hasNext()) {
                Object key = it.next();
                if ("content".equals(key))
                    data.put("content", (String) prosermap.get(key));
            }
            data.put("titleName", "投资协议");
            obj = createSuccessAppResponse(data);
        } catch (Exception e) {
            obj = dealException(e);
        }
        return obj;
    }

    /**
     * 生成普通投资协议
     *
     * @return
     * @throws IOException
     * @author QianPengZhan
     * @date 2016年10月19日
     */
    @RequestMapping("/app/open/myInvest/buildProtocolAndDownload")
    @ResponseBody
    public Object buildProtocolAndDownload(final String projectId, String projectInvestId,
                                           final String type, final HttpServletResponse response, Model model, HttpServletRequest request) throws IOException {
        LOGGER.info("进入生成普通投资协议接口...start");
        if (StringUtils.isBlank(projectInvestId)) {
            projectInvestId = projectId;
        }
        projectInvestService.handleAllProtocol(projectId, projectInvestId);
        if ("zip".equals(type)) {
            downloadProjectProtocol(projectId, response);
        } else {
            downloadInvestProtocol(projectInvestId, response);
        }
        return renderResult(true, "");
    }

    /**
     * 借款人下载协议
     *
     * @param projectId
     * @return
     * @throws IOException
     * @author QianPengZhan
     * @date 2016年10月19日
     */
    @RequestMapping("/app/member/myInvest/downloadProjectProtocol")
    public String downloadProjectProtocol(final String projectId, final HttpServletResponse response) throws IOException {
        LOGGER.info("进入借款协议下载  .zip接口...start");
        Project project = projectService.get(projectId);
        String fileName = project.getProjectName() + "_" + project.getProjectNo() + ".zip";
        String url = "/upload/downloadContractMobile.html";
        String webUrl = ConfigUtils.getValue("mobile_url");
        String resultStr = protocolService.downloadProtocol(projectId, projectId, fileName, url, webUrl);
        if (StringUtils.isNotBlank(resultStr)) {
            response.sendRedirect(resultStr);
            LOGGER.info("进入借款协议下载  .zip接口...end");
        } else {
            LOGGER.error(ERROR_LOAD, resultStr);
        }
        return null;
    }

    /**
     * 投资人下载协议
     *
     * @return
     * @throws IOException
     * @author QianPengZhan
     * @date 2016年10月19日
     */
    @RequestMapping("/app/member/myInvest/downloadInvestProtocol")
    public String downloadInvestProtocol(final String investId, final HttpServletResponse response) throws IOException {
        LOGGER.info("进入借款协议下载  .pdf接口...start");
        ProjectInvest projectInvest = projectInvestService.get(investId);
        String projectId = projectInvest.getProjectId();
        Project project = projectService.get(projectId);

        String fileName = project.getProjectName() + "_" + project.getProjectNo() + ".pdf";
        String url = "/upload/downloadContractMobile.html";
        String webUrl = ConfigUtils.getValue("mobile_url");
        String resultStr = protocolService.downloadProtocol(projectId, investId, fileName, url, webUrl);
        if (StringUtils.isNotBlank(resultStr)) {
            response.sendRedirect(resultStr);
            LOGGER.info("进入借款协议下载  .pdf接口...end");
        } else {
            LOGGER.error(ERROR_LOAD, resultStr);
        }
        return null;
    }
}
