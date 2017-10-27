package com.rd.ifaes.mobile.controller.member;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TokenUtils;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.domain.Realize;
import com.rd.ifaes.core.project.domain.RealizeRule;
import com.rd.ifaes.core.project.model.ProjectInvestModel;
import com.rd.ifaes.core.project.service.ProjectCollectionService;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.project.service.RealizeInvestService;
import com.rd.ifaes.core.project.service.RealizeRepaymentService;
import com.rd.ifaes.core.project.service.RealizeRuleService;
import com.rd.ifaes.core.project.service.RealizeService;
import com.rd.ifaes.core.protocol.service.ProtocolService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.mobile.controller.WebController;
import com.rd.ifaes.mobile.model.PagedData;
import com.rd.ifaes.mobile.model.account.ProjectToRealizeModel;
import com.rd.ifaes.mobile.model.account.log.RealizeLogItemModel;

/**
 * 我的资产-我的变现
 * 
 * @version 3.0
 * @author FangJun
 * @date 2016年7月27日
 */
@Controller
public class MyRealizeController extends WebController {

	/**
	 * 同意协议
	 */
	protected static final String IS_AGREE = "1";

	@Resource
	private transient RealizeService realizeService;
	@Resource
	private transient RealizeRuleService realizeRuleService;
	@Resource
	private transient RealizeInvestService realizeInvestService;
	@Resource
	private transient RealizeRepaymentService realizeRepaymentService;
	@Resource
	private transient ProjectInvestService projectInvestService;
	@Resource
	private transient ProjectCollectionService projectCollectionService;
	@Resource
	private transient ProtocolService protocolService;
	/**
	 * 列表页面
	 * 
	 * @author fxl
	 * @date 2016年8月1日
	 * @return
	 */
	@RequestMapping("/member/myRealize/list")
	public String list() {
		return "/member/myRealize/list";
	}

	/**
	 * 可变现页面
	 * 
	 * @author xiaodingjiang
	 * @date 2016年1月1日
	 * @param model
	 * @return
	 */
	@RequestMapping("/app/member/myRealize/getRealizeAbleList")
	@ResponseBody
	public Object getRealizeAbleList(ProjectInvestModel model,
			HttpServletRequest request) {
		Object obj=null;
		try {
			int page=model.getPage().getPage();
			Page<ProjectInvestModel> pages=new Page<ProjectInvestModel>();
			User user = getAppSessionUser(request);
			model.setUserId(user.getUuid());
			pages = realizeInvestService
					.findRealizeAbleList(model);
			List<ProjectInvestModel> realizeAbleList = pages.getRows();
			PagedData<ProjectToRealizeModel> pirlist = new PagedData<ProjectToRealizeModel>();
			if(realizeAbleList!=null){
			pages.setPageSize(pages.getRows().size());
			pages.setPage(page);
			fillPageDatas(pirlist, pages);
			for (ProjectInvestModel projectInvestModel : realizeAbleList) {
				ProjectToRealizeModel logModel = new ProjectToRealizeModel();
				logModel.setAmount(projectInvestModel.getAmount());// 投资金额
				logModel.setInterest(projectInvestModel.getWaitInterest());// 预期收益
				logModel.setNextRepayTime(projectInvestModel.getNextRepayTime());// 本期回款日
				logModel.setProjectName(projectInvestModel.getProjectName());// 项目名称
				logModel.setRemainDays(projectInvestModel.getRemainDays());// 剩余期限
				logModel.setUuid(projectInvestModel.getUuid());// 投资项目uuid（对应变现projectInvestId）
				logModel.setProjectId(projectInvestModel.getProjectId());//项目id
				logModel.setRealizeFlag(projectInvestModel.getRealizeFlag());//变现标识
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
	 * 变现中列表
	 * 
	 * @author xiaodingjiang
	 * @date 2016年11月1日
	 * @param model
	 * @return
	 */
	@RequestMapping("/app/member/myRealize/getRealizingList")
	@ResponseBody
	public Object getRealizingList(Realize model,
			HttpServletRequest request) {
		Object obj=null;
		try {
			int page=model.getPage().getPage();
			Page<Realize> pages=new Page<Realize>();
			User user = getAppSessionUser(request);
			model.setUserId(user.getUuid());
			model.setStatusSet(new String[] {
					LoanEnum.STATUS_RAISING.getValue(),
					LoanEnum.STATUS_RAISE_FINISHED.getValue() });
			pages = realizeService.findAllPage(model);
			List<Realize> realizingList = pages.getRows();
			PagedData<RealizeLogItemModel> pirlist = new PagedData<RealizeLogItemModel>();
			if(realizingList!=null){
				pages.setPageSize(pages.getRows().size());
				pages.setPage(page);
				fillPageDatas(pirlist, pages);
			for (Realize realize : realizingList) {
				RealizeLogItemModel logModel = new RealizeLogItemModel();
				logModel.setAccount(realize.getAccount());// 变现金额
				logModel.setApr(realize.getApr());// 变现利率
				logModel.setCreateTime(realize.getCreateTime());// 变现申请时间
				logModel.setInvestId(realize.getInvestId());// 投标id
				logModel.setReviewTime(realize.getReviewTime());// 变现完成时间
				logModel.setScales(new BigDecimal(realize.getScales()));// 进度
				logModel.setProjectName(realize.getProjectName());// 产品名称
				logModel.setUuid(realize.getUuid());// uuid
				logModel.setProjectId(realize.getProjectId());//项目id
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
	 * 已变现列表
	 * 
	 * @author xiaodingjiang
	 * @date 2016年11月1日
	 * @param model
	 * @return
	 */
	@RequestMapping("/app/member/myRealize/getRealizedList")
	@ResponseBody
	public Object getRealizedList(Realize model, HttpServletRequest request) {
		Object obj=null;
		try {
			int page=model.getPage().getPage();
			Page<Realize> pages=new Page<Realize>();
			User user = getAppSessionUser(request);
			model.setUserId(user.getUuid());
			model.setStatusSet(new String[] {
					LoanEnum.STATUS_REPAY_START.getValue(),
					LoanEnum.STATUS_REPAYED_SUCCESS.getValue() });
			pages = realizeService.findAllPage(model);
			List<Realize> realizedList = pages.getRows();
			PagedData<RealizeLogItemModel> pirlist = new PagedData<RealizeLogItemModel>();
			if(realizedList!=null){
				pages.setPage(page);
				pages.setPageSize(pages.getRows().size());
				fillPageDatas(pirlist, pages);
			for (Realize realize : realizedList) {
				RealizeLogItemModel logModel = new RealizeLogItemModel();
				logModel.setAccount(realize.getAccount());// 变现金额
				logModel.setApr(realize.getApr());// 变现利率
				logModel.setCreateTime(realize.getCreateTime());// 变现申请时间
				logModel.setInvestId(realize.getInvestId());// 投标id
				logModel.setReviewTime(realize.getReviewTime());// 变现完成时间
				logModel.setScales(new BigDecimal(realize.getScales()));// 进度
				logModel.setProjectName(realize.getProjectName());// 产品名称
				logModel.setUuid(realize.getUuid());// uuid
				logModel.setProjectId(realize.getProjectId());//项目id
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
	 * 发布变现页面
	 * 
	 * @author xiaodingjiang
	 * @date 2016年11月1日
	 * @param projectInvestId
	 * @return
	 */
	@RequestMapping("/app/member/myRealize/toSet")
	@ResponseBody
	public Object setting(String projectInvestId,HttpServletRequest request) {
		Object obj=null;
		Map<String,Object>data=new HashMap<String,Object>();
		try {
			@SuppressWarnings("unused")
			User user = getAppSessionUser(request);
			data.put("commitRealizeToken", TokenUtils.setToken(request.getSession(), TokenConstants.TOKEN_REALIZE_COMMIT));
			final RealizeRule rule = realizeRuleService.getRule();
			final ProjectInvest invest = projectInvestService
					.get(projectInvestId);
			if (invest == null) {
				throw new BussinessException(
						ResourceUtils
								.get(ResourceConstant.REALIZE_INVEST_NOT_FOUND));
			}
			int timeLimit = projectCollectionService.getReypayDays(invest
					.getUuid());
			if(!DateUtils.before(DateUtils.getNow(), rule.getIssueTime())){// 变现时间在发布时间之后
				timeLimit = timeLimit-1;
			}
			timeLimit = timeLimit < 0 ? 0 : timeLimit;
			final BigDecimal waitAmount = BigDecimalUtils.add(invest.getWaitAmount(), invest.getRaiseInterest().negate(), invest.getRealizeAmount().negate(),invest.getFreezeCapital(), invest.getFreezeInterest());
			final BigDecimal waitInterest =  BigDecimalUtils.add(invest.getWaitInterest(), invest.getRaiseInterest().negate(), invest.getRealizeInterest().negate(), invest.getFreezeInterest());
			data.put("daysOfYear", ConfigUtils.getValue("days_of_year"));// 每年的天数，用于还款时计息的计算
			data.put("timeLimit", timeLimit);// 变现期限
			data.put("waitAmount", waitAmount);// 所持资产
			data.put("waitInterest", waitInterest);//预期收益
			data.put("realizeRateMax", rule.getRealizeRateMax());//变现利率上限值
			data.put("realizeRateMin", rule.getRealizeRateMin());// 变现利率下限值
			data.put("uuid", invest.getUuid());// uuid
			data.put("sellStyle",rule.getSellStyle()); /* 变现金额方式:0 部分变现;1全额变现,默认0 */ 
			//温馨提示
			data.put("warmTips", "1.发起变现申请的时间早于"+rule.getIssueTime()+"，则在当天"+rule.getRaiseEndTime()+"之前未变现成功则自动撤销变现申请，若变现成功则放款；若发起变现申请的时间晚于"+rule.getIssueTime()+"，则在第二天"+rule.getRaiseEndTime()+"之前未变现成功则自动撤销变现申请，若变现成功则放款 "+"\n"+" 2.按变现金额的"+rule.getFeeRate().stripTrailingZeros()+"%收取服务费，单次变现收取服务费总额不大于"+BigDecimalUtils.round(rule.getFeeSingleMax())+"元"+"\n"+" "+"3.申请变现期间及变现成功后，原始项目的还款金额都需要冻结");
			obj=createSuccessAppResponse(data);
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
	}

	/**
	 * 发布变现页面
	 * 
	 * @author fxl
	 * @date 2016年8月1日
	 * @param projectInvestId
	 * @return
	 */
	@RequestMapping("/member/myRealize/setting")
	public String setting(final String projectInvestId,
			final HttpServletRequest request, final Model model) {
		final RealizeRule rule = realizeRuleService.getRule();
		final ProjectInvest invest = projectInvestService.get(projectInvestId);
		if (invest == null) {
			throw new BussinessException(
					ResourceUtils
							.get(ResourceConstant.REALIZE_INVEST_NOT_FOUND));
		}
		int timeLimit = projectCollectionService
				.getReypayDays(invest.getUuid());
		if (!DateUtils.before(DateUtils.getNow(), rule.getIssueTime())) {// 变现时间在发布时间之后
			timeLimit = timeLimit - 1;
		}
		timeLimit = timeLimit < 0 ? 0 : timeLimit;
		final BigDecimal waitAmount = BigDecimalUtils.add(
				invest.getWaitAmount(), invest.getRealizeAmount().negate(),
				invest.getFreezeInterest());
		final BigDecimal waitInterest = BigDecimalUtils.add(
				invest.getWaitInterest(), invest.getRealizeInterest().negate(),
				invest.getFreezeInterest());
		model.addAttribute("timeLimit", timeLimit);
		model.addAttribute("waitAmount", waitAmount.doubleValue());
		model.addAttribute("waitInterest", waitInterest.doubleValue());
		model.addAttribute("invest", invest);
		model.addAttribute("daysOfYear", ConfigUtils.getValue("days_of_year"));
		model.addAttribute("rule", rule);
		setToken(TokenConstants.TOKEN_REALIZE_COMMIT, request);
		return "/member/myRealize/setting";
	}

	/**
	 * 获取最大可变现金额
	 * 
	 * @author fxl
	 * @date 2016年8月1日
	 * @param projectInvestId
	 * @param apr
	 * @param timeLimit
	 * @return
	 */
	// http://localhost/member/myRealize/getMostRealizeAmount.html?projectInvestId=58548fd5ed274e84956b64f3ae28c48e&apr=12&timeLimit=30
	@RequestMapping("/member/myRealize/getMostRealizeAmount")
	@ResponseBody
	public Object getMostRealizeAmount(final String projectInvestId,
			final BigDecimal apr, final BigDecimal timeLimit) {
		final ProjectInvest invest = projectInvestService.get(projectInvestId);
		if (invest == null) {
			throw new BussinessException(
					ResourceUtils
							.get(ResourceConstant.REALIZE_INVEST_NOT_FOUND));
		}
		final BigDecimal mostRealizeAmount = realizeService
				.getMostRealizeAmount(invest.getWaitAmount(), apr, timeLimit);
		final Map<String, Object> data = renderSuccessResult();
		data.put("mostRealizeAmount", mostRealizeAmount);
		return data;
	}

	/**
	 * 发布变现
	 * 
	 * @author fxl
	 * @date 2016年8月1日
	 * @param entity
	 * @param agree
	 * @return
	 */
	// localhost/member/myRealize/doSet.html?apr=1&account=1000&investId=XXXX&agree=1
	@RequestMapping(value = "/app/member/myRealize/doSet")
	@ResponseBody
	@TokenValid(value=TokenConstants.TOKEN_REALIZE_COMMIT,dispatch=true)
	public Object doSet(final Realize entity, final String agree) {
		Object obj=null;
		try{
		final User user = getSessionUser();
		entity.setUserId(user.getUuid());
		if (StringUtils.isBlank(agree) || !agree.equals(IS_AGREE)) {
			throw new BussinessException(
					ResourceUtils
							.get(ResourceConstant.REALIZE_PROTOCOL_NOT_CHECK));
		}
		realizeService.publish(entity);
		 obj=createSuccessAppResponse("变现成功");
		}catch(Exception e){
			obj=dealException(e);
		}
		return obj;
	}

	/**
	 * 放款调用(测试用)
	 * 
	 * @author fxl
	 * @date 2016年8月16日
	 * @param uuid
	 * @return
	 */
	// localhost/member/myRealize/doFullSuccess.html?uuid=
	@RequestMapping(value = "/member/myRealize/doFullSuccess")
	@ResponseBody
	public Object doFullSuccess(final String uuid) {
		realizeService.fullSuccess(uuid);
		return renderSuccessResult();
	}

	/**
	 * 变现还款
	 * 
	 * @author fxl
	 * @date 2016年8月1日
	 * @param entity
	 * @return
	 */
	// http://localhost/member/myRealize/doRepay.html?uuid=c3de20f478f043f58f4cad7a7e4a8326
	@RequestMapping(value = "/member/myRealize/doRepay")
	@ResponseBody
	public Object doRepay(final Realize entity) {
		final User user = getSessionUser();
		entity.setUserId(user.getUuid());
		realizeRepaymentService.repay(entity);
		return renderSuccessResult();
	}
	/**
	 * 移动端--变现协议
	 * @author lgx
	 * @date 2016年02月15日
	 * @return
	 */
	@RequestMapping(value = "/app/member/myRealize/realizeProtocol")
	public Object realizeProtocol(HttpServletRequest request,final Model model) {
		try{
		final User user = getSessionUser();
		Map<String,Object> prosermap=protocolService.getRealizeProtocol(user);
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
			return ConfigUtils.getTppName()+"/retresult";
		}
	}
	
	/**
	 * 移动端--变现协议--微信使用
	 * @author lgx
	 * @date 2016年02月15日
	 * @return
	 */
	@RequestMapping(value = "/app/member/myRealize/wxRealizeProtocol")
	@ResponseBody
	public Object wxRealizeProtocol(HttpServletRequest request,final Model model) {
		Object obj=null;
		try{
			Map<String, Object> data = new HashMap<String, Object>();
		final User user = getSessionUser();
		Map<String,Object> prosermap=protocolService.getRealizeProtocol(user);
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
}
