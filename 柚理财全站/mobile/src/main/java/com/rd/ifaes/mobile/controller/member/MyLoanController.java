package com.rd.ifaes.mobile.controller.member;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectRepayment;
import com.rd.ifaes.core.project.service.ProjectRepaymentService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.protocol.service.ProtocolService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.mobile.common.AppException;
import com.rd.ifaes.mobile.common.AppResultCode;
import com.rd.ifaes.mobile.controller.WebController;
import com.rd.ifaes.mobile.model.PagedData;
import com.rd.ifaes.mobile.model.account.log.BorrowLogItemModel;
import com.rd.ifaes.mobile.model.account.log.RepaymentLogItemModel;

/**
 * 
 * 我的借款-借款列表
 * 
 * @version 3.0
 * @author xiaodingjiang
 * @date 2016年10月26日
 */
@Controller
public class MyLoanController extends WebController {

	private static final String ERROR_LOAD = "下载出错,地址：{}";
	@Resource
	private ProjectService projectService;
	@Resource
	private ProjectRepaymentService projectRepaymentService;
	@Resource
	private transient ProtocolService protocolService;

	/**
	 * 获得借款列表记录
	 * 
	 * @author xiaodingjiang
	 * @date 2016年10月26日
	 * @return
	 */
	@RequestMapping(value = "/app/member/myLoan/getLogList")
	@ResponseBody
	public Object getLogList(Project model, HttpServletRequest request) {
		Object obj=null;
		try {
			@SuppressWarnings("unused")
			User user = getAppSessionUser(request);// 查询用户，更新缓存
			Page<Project> pages = projectService.findByDate(model);
			List<Project> logList = pages.getRows();
			PagedData<BorrowLogItemModel> pirlist = new PagedData<BorrowLogItemModel>();
			if(logList!=null){
				pages.setPageSize(pages.getRows().size());
				fillPageDatas(pirlist, pages);
			for (Project project : logList) {
				BorrowLogItemModel logModel = new BorrowLogItemModel();
				logModel.setAccount(project.getAccount());// 借款金额
				logModel.setAccountYes(project.getAccountYes());// 募集金额
				logModel.setApr(project.getApr());// 借款年利率
				logModel.setBorrowFlag(project.getBorrowFlag());// 借款标标识(1是，借款标，0否，即是理财产品）
				logModel.setCreateTime(project.getCreateTime());// 借款申请时间
				logModel.setProjectName(project.getProjectName());// 项目名称
				logModel.setScales(new BigDecimal(project.getScales()));// 借款进度
				logModel.setStatusStr(project.getStatusStr());// 借款状态
				logModel.setTimeLimitStr(project.getTimeLimitStr());// 借款期限
				logModel.setUuid(project.getUuid());// uuid(对应还款计划的projectId)
				String resultStr = "";
				logModel.setProtocolUrl(resultStr);// 借款人下载协议url
				/*  状态: 0 新增 1发布待审 2 发布审核成功(待售) 3发布审核失败4 募集中 5 满额待审（已售） 
                6 成立审核成功（还款中）8 还款中87 逾期中（还款中）88坏账（还款中） 90部分还款（还款中） 9还款成功（已完成）91逾期还款（已完成）    */
				logModel.setStatus(project.getStatus());
				pirlist.getList().add(logModel);
			}
			}
			obj=createSuccessAppResponse(pirlist);
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;

	}

	/**
	 * 获得还款计划列表记录
	 * 
	 * @author xiaodingjiang
	 * @date 2016年10月26日
	 * @return
	 */
	@RequestMapping(value = "/app/member/myLoan/getProjectRepaymentList")
	@ResponseBody
	public Object getProjectRepaymentList(ProjectRepayment model,HttpServletRequest request) {
		Object obj=null;
		try {
			/* String projectId=paramString("projectId"); */
			@SuppressWarnings("unused")
			User user=getAppSessionUser(request);
			Page<ProjectRepayment> pages = projectRepaymentService
					.findByProjectId(model);
			List<ProjectRepayment> projectRepaymentList = pages.getRows();
			PagedData<RepaymentLogItemModel> pirlist = new PagedData<RepaymentLogItemModel>();
			if(projectRepaymentList!=null){
				pages.setPageSize(pages.getRows().size());
				fillPageDatas(pirlist, pages);
			for (ProjectRepayment projectRepayment : projectRepaymentList) {
				RepaymentLogItemModel logModel = new RepaymentLogItemModel();
				logModel.setCapital(projectRepayment.getCapital());// 应还本金
				logModel.setPeriodsStr(projectRepayment.getPeriodsStr());// 期数
				logModel.setPeriod(projectRepayment.getPeriod());// 第几期
				logModel.setPeriods(projectRepayment.getPeriods());// 总期数
				logModel.setRepayTime(projectRepayment.getRepayTime());// 预计还款时间
				logModel.setStatusStr(projectRepayment.getStatusStr());// 还款状态
				logModel.setInterest(projectRepayment.getInterest());// 应还利息
				logModel.setLateInterest(projectRepayment.getLateInterest());// 逾期罚息
				logModel.setStatus(projectRepayment.getStatus());// 还款状态 0未还；1已还 -1（前面还有期数没有还款，不能进行还款操作）
				logModel.setProjectName(projectRepayment.getProjectName());// 项目名称
				logModel.setRealRepayTime(projectRepayment.getRealRepayTime());// 实际还款时间
				logModel.setRepaymentTypeStr(projectRepayment.getRepaymentTypeStr());// 还款计划-类型	
				logModel.setPayment(projectRepayment.getPayment());// 应还款金额
				logModel.setUuid(projectRepayment.getUuid());// uuid
				logModel.setProjectId(projectRepayment.getProjectId());// 项目id
				//还款计划详情说明-- 状态备注
			if(projectRepayment.getStatus().equals("1")){
					logModel.setStatusTypeStr("已还 "+projectRepayment.getRepaymentTypeStr());
			  }else{
			      if(projectRepayment.getStatusTypeStr().equals("逾期待还")){	
				logModel.setStatusTypeStr("已逾期  "+projectRepayment.getRepaymentTypeStr());
				   }else{
			    logModel.setStatusTypeStr(projectRepayment.getStatusTypeStr()+" "+projectRepayment.getRepaymentTypeStr());
				    }
				 }
				pirlist.getList().add(logModel);
			}
			}
			obj=createSuccessAppResponse(pirlist);
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
	}

	/**
	 * 借款人下载协议
	 * 
	 * @author QianPengZhan
	 * @date 2016年10月19日
	 * @param projectId
	 * @return
	 * @throws IOException
	 *//*
	@RequestMapping("/app/member/myLoan/downloadInvestProtocol")
	@ResponseBody
	public Object downloadProjectProtocol(final String projectId)
			throws IOException {
		LOGGER.info("进入借款协议下载  .zip接口...start");
		Map<String, Object> data = new HashMap<String, Object>();
		Object obj = null;
		try {
		Project project = projectService.get(projectId);
		String fileName = project.getProjectName() + "_"
				+ project.getProjectNo() + ".zip";
		String resultStr = protocolService.downloadProtocol(projectId,
				projectId, fileName);
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
}
