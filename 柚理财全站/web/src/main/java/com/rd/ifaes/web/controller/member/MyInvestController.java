package com.rd.ifaes.web.controller.member;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

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
import com.rd.ifaes.core.project.model.MyProjectInvestModel;
import com.rd.ifaes.core.project.model.ProjectInvestModel;
import com.rd.ifaes.core.project.service.ProjectCollectionService;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.protocol.service.ProtocolService;
import com.rd.ifaes.core.tpp.model.jx.JxBidApplyModel;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.web.controller.WebController;

/**
 * 
 *  我的资产-我的投资
 * @version 3.0
 * @author FangJun
 * @date 2016年7月27日
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
	@Resource
	private transient UserCacheService userCacheService;
	/**
	 * 我的投资列表页面
	 * @author fxl
	 * @date 2016年8月17日
	 * @return
	 */
	@RequestMapping("/member/myInvest/list")
	public String list(){
		return "/member/myInvest/list";
	}
	
	/**
	 * 持有中
	 * @author fxl
	 * @date 2016年8月17日
	 * @param model
	 * @return
	 */
	@RequestMapping("/member/myInvest/getBorrowHolding")
	@ResponseBody
	public Object getBorrowHolding(final MyProjectInvestModel model){
		final User user = getSessionUser();
		model.setUserId(user.getUuid());
		final String[] projectStatus = {LoanEnum.STATUS_REPAY_START.getValue(),LoanEnum.STATUS_REPAY_OVERDUE.getValue(),LoanEnum.STATUS_BAD_DEBT.getValue()};
		model.setProjectStatusSet(projectStatus);
		model.setType(MyProjectInvestModel.TYPE_HOLDING);
		model.setStatusSet(new String[]{InvestEnum.STATUS_SUCCESS.getValue(),InvestEnum.STATUS_DELETE.getValue()});//已转让的也显示
		model.setInvestStyle(InvestEnum.INVEST_STYLE_BUY.getValue());//受让投资不显示
		final Page<MyProjectInvestModel> investList = projectInvestService.myProjectInvestList(model);
		final Map<String, Object> data = new HashMap<>();
		data.put(RESULT_NAME, true);
		data.put("data", investList);
		return data;
	}
	
	/**
	 * 投资申请
	 * @author fxl
	 * @date 2016年8月17日
	 * @param model
	 * @return
	 */
	@RequestMapping("/member/myInvest/getInvestApply")
	@ResponseBody
	public Object getInvestApply(final MyProjectInvestModel model){
		final User user = getSessionUser();
		model.setUserId(user.getUuid());
		final String[] projectStatus = {LoanEnum.STATUS_RAISING.getValue(),LoanEnum.STATUS_RAISE_FINISHED.getValue(),LoanEnum.STATUS_NOT_ESTABLISH.getValue()};
		model.setProjectStatusSet(projectStatus);
		model.setType(MyProjectInvestModel.TYPE_APPLY);
		final Page<MyProjectInvestModel> investList = projectInvestService.myProjectInvestList(model);
		final Map<String, Object> data = new HashMap<>();
		data.put(RESULT_NAME, true);
		data.put("data", investList);
		return data;
	}	
	
	
	/**
	 * 已结束
	 * @author fxl
	 * @date 2016年8月17日
	 * @param model
	 * @return
	 */
	@RequestMapping("/member/myInvest/getInvestClosed")
	@ResponseBody
	public Object getInvestClosed(final MyProjectInvestModel model){
		final User user = getSessionUser();
		model.setUserId(user.getUuid());
		final String[] projectStatus = {LoanEnum.STATUS_REPAYED_SUCCESS.getValue(),LoanEnum.STATUS_REPAYED_LATE.getValue()};
		final String[] statusSet = {InvestEnum.STATUS_SUCCESS.getValue()};
		model.setProjectStatusSet(projectStatus);
		model.setStatusSet(statusSet);
		model.setType(MyProjectInvestModel.TYPE_CLOSED);
		model.setInvestStyle(InvestEnum.INVEST_STYLE_BUY.getValue());//债权受让人的原始标投资记录不显示 
		final Page<MyProjectInvestModel> investList = projectInvestService.myProjectInvestList(model);
		final Map<String, Object> data = new HashMap<>();
		data.put(RESULT_NAME, true);
		data.put("data", investList);
		return data;
	}
	
	/**
	 * 回款计划页面
	 * @author fxl
	 * @date 2016年8月17日
	 * @param investId
	 * @return
	 */
	@RequestMapping(value = "/member/myInvest/projectCollection")
	public String projectCollection(final String investId,final Model model) {
		 model.addAttribute("investId", investId);
		return "/member/myInvest/projectCollection";
	}
	
	/**
	 * 回款计划数据
	 * @author fxl
	 * @date 2016年8月17日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/member/myInvest/getProjectCollectionList")
	@ResponseBody
	public Object getProjectCollectionList(final ProjectCollection model){
		final Map<String,Object> data =new HashMap<>();
		data.put(RESULT_NAME, true);
		data.put("data", projectCollectionService.findForPage(model));
		data.put("projectName", projectService.getProjectNameByInvestId(model.getInvestId()));
		return data;
	}
	
	/**
	 * 去支付
	 * @author fxl
	 * @date 2016年8月24日
	 * @param invest
	 * @param model
	 * @return
	 */
	@RequestMapping("/member/myInvest/doPay")
	public String doPay(final ProjectInvestModel invest,final Model model) {
		// 用户登录状态
		final User user = (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
		if (user == null) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_NOT_LOGIN), BussinessException.TYPE_CLOSE);
		} else {
			invest.setUserId(user.getUuid());
		}
		JxBidApplyModel jxmodel = (JxBidApplyModel) projectInvestService.doPay(invest);
		model.addAttribute("model", jxmodel);
		model.addAttribute("sign", jxmodel.getSign());
		return ConfigUtils.getTppName() + "/invest";
	}
	
	 /**
	  * 
	  * @author  FangJun
	  * @date 2016年9月28日
	  * @param invest
	  * @param model
	  * @return
	  */
	@RequestMapping("/member/myInvest/repay")
	@ResponseBody
	public Object repay(final ProjectInvestModel model ) {
		// 用户登录状态
		final User user = (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
		if (user == null) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_NOT_LOGIN), BussinessException.TYPE_CLOSE);
		}else {
			model.setUserId(user.getUuid());
		}  
		final  ProjectInvest invest= projectInvestService.checkPayInvest(model);
		final int investExpireSeconds = ConfigUtils.getInt(ConfigConstant.INVEST_TIMEOUT) * 60;
		final long pastSeconds=DateUtils.pastSeconds(invest.getCreateTime()) ;
		if(investExpireSeconds > 0 &&  pastSeconds>= investExpireSeconds){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.ORDER_IS_TIMEOUT), BussinessException.TYPE_CLOSE);
		}
		Map<String, Object> data = renderSuccessResult();
		//剩余有效时间
		data.put("remainSeconds",  (investExpireSeconds - pastSeconds));
		return data;
	}
	
	/**
	 * 借款人下载协议
	 * @author QianPengZhan
	 * @date 2016年10月19日
	 * @param projectId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/member/myInvest/downloadProjectProtocol")
	public String downloadProjectProtocol(final String projectId,final HttpServletResponse response) throws IOException{
		LOGGER.info("进入借款协议下载  .zip接口...start");
		Project project = projectService.get(projectId);
		String fileName = project.getProjectName() +"_"+project.getProjectNo()+ ".zip";
		String url = "/upload/downloadContract.html";
		String webUrl =ConfigUtils.getValue(ConfigConstant.WEB_URL);
		String resultStr = protocolService.downloadProtocol(projectId, projectId, fileName, url, webUrl);
		if(StringUtils.isNotBlank(resultStr)){
			response.sendRedirect(resultStr);
			LOGGER.info("进入借款协议下载  .zip接口...end");
		}else{
			LOGGER.error(ERROR_LOAD,resultStr);
		}
		return null;
	}
	
	/**
	 * 投资人下载协议
	 * @author QianPengZhan
	 * @date 2016年10月19日
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/member/myInvest/downloadInvestProtocol")
	public String downloadInvestProtocol(final String investId,final HttpServletResponse response) throws IOException{
		LOGGER.info("进入借款协议下载  .pdf接口...start");
		ProjectInvest projectInvest = projectInvestService.get(investId);
		String projectId = projectInvest.getProjectId();
		Project project = projectService.get(projectId);
		
		String fileName = project.getProjectName() +"_"+project.getProjectNo()+ ".pdf";
		String url = "/upload/downloadContract.html";
		String webUrl =ConfigUtils.getValue(ConfigConstant.WEB_URL);
		String resultStr = protocolService.downloadProtocol(projectId, investId, fileName, url, webUrl);
		if(StringUtils.isNotBlank(resultStr)){
			response.sendRedirect(resultStr);
			LOGGER.info("进入借款协议下载  .pdf接口...end");
		}else{
			LOGGER.error(ERROR_LOAD,resultStr);
		}
		return null;
	}
	
	/**
	 * 借款协议预览
	 * @author QianPengZhan
	 * @date 2016年10月19日
	 * @param protocolId
	 * @param projectId
	 * @return
	 */
	@RequestMapping("/member/myInvest/protocolSearch")
	@ResponseBody
	public Object protocolSearch(final String protocolId,final String projectId){
		LOGGER.info("进入借款协议预览接口...start");
		final User user = getSessionUser();
		return protocolService.getInvestProtocol(user,projectId,protocolId);
	}
	
	/**
	 * 生成普通投资协议
	 * @author QianPengZhan
	 * @date 2016年10月19日
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/member/myInvest/buildProtocolAndDownload")
	@ResponseBody
	public Object buildProtocolAndDownload(final String projectId,String projectInvestId,
			final String type,final HttpServletResponse response) throws IOException{
		LOGGER.info("进入生成普通投资协议接口...start");
		if(StringUtils.isBlank(projectInvestId)){
			projectInvestId = projectId;
		}
		projectInvestService.handleAllProtocol(projectId,projectInvestId);
		if("zip".equals(type)){
			downloadProjectProtocol(projectId,response);
		}else{
			downloadInvestProtocol(projectInvestId,response);
		}
		return renderResult(true, "");
	}
}
