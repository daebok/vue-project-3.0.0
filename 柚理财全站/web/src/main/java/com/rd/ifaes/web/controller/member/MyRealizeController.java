package com.rd.ifaes.web.controller.member;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.StringUtils;
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
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.web.controller.WebController;

/**
 * 我的资产-我的变现
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
	 * @author fxl
	 * @date 2016年8月1日
	 * @return
	 */
	@RequestMapping("/member/myRealize/list")
	public String list(){
		final UserCache userCache = (UserCache)SessionUtils.getSessionAttr(Constant.SESSION_USER_CACHE);
		if(userCache!=null && UserCache.USER_NATURE_VOUCH.equals(userCache.getUserNature())){//担保用户
			return "/member/account/vouchMain";
		}
		return "/member/myRealize/list";
	}
	
	/**
	 * 可变现页面
	 * @author fxl
	 * @date 2016年8月1日
	 * @param model
	 * @return
	 */
	@RequestMapping("/member/myRealize/getRealizeAbleList")
	@ResponseBody
	public Object getRealizeAbleList(final ProjectInvestModel model){
		final User user = getSessionUser();
		model.setUserId(user.getUuid());
		final Map<String, Object> data = new HashMap<>();
		data.put(RESULT_NAME, true);
		data.put("data", realizeInvestService.findRealizeAbleList(model));
		return data;
	}
	
	/**
	 * 变现中列表
	 * @author fxl
	 * @date 2016年8月1日
	 * @param model
	 * @return
	 */
	@RequestMapping("/member/myRealize/getRealizingList")
	@ResponseBody
	public Object getRealizingList(final Realize model){
		final User user = getSessionUser();
		model.setUserId(user.getUuid());
		model.setStatusSet(new String[]{LoanEnum.STATUS_RAISING.getValue(), 
			LoanEnum.STATUS_RAISE_FINISHED.getValue()});
		final Map<String, Object> data = new HashMap<>();
		data.put(RESULT_NAME, true);
		data.put("data", realizeService.findAllPage(model));
		return data;
	}	
	
	/**
	 * 已变现列表
	 * @author fxl
	 * @date 2016年8月1日
	 * @param model
	 * @return
	 */
	@RequestMapping("/member/myRealize/getRealizedList")
	@ResponseBody
	public Object getRealizedList(final Realize model){
		final User user = getSessionUser();
		model.setUserId(user.getUuid());
		model.setStatusSet(new String[]{LoanEnum.STATUS_REPAY_START.getValue(), 
				LoanEnum.STATUS_REPAYED_SUCCESS.getValue()});
		final Map<String, Object> data = new HashMap<>();
		data.put(RESULT_NAME, true);
		data.put("data", realizeService.findAllPage(model));
		return data;
	}
	
	/**
	 * 发布变现页面
	 * @author fxl
	 * @date 2016年8月1日
	 * @param projectInvestId
	 * @return
	 */
	@RequestMapping("/member/myRealize/setting")
	public String setting(final String projectInvestId ,final HttpServletRequest request ,final Model model) {
		final RealizeRule rule = realizeRuleService.getRule();
		final ProjectInvest invest = projectInvestService.get(projectInvestId);
		if(invest==null){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.REALIZE_INVEST_NOT_FOUND));
		}
		int timeLimit = projectCollectionService.getReypayDays(invest.getUuid());
		if(!DateUtils.before(DateUtils.getNow(), rule.getIssueTime())){// 变现时间在发布时间之后
			timeLimit = timeLimit-1;
		}
		timeLimit = timeLimit < 0 ? 0 : timeLimit;
		// 待收总额
		final BigDecimal waitAmount = BigDecimalUtils.add(invest.getWaitAmount(), invest.getRaiseInterest().negate(), invest.getRealizeAmount().negate(),invest.getFreezeCapital(), invest.getFreezeInterest());
		// 待收利息
		final BigDecimal waitInterest =  BigDecimalUtils.add(invest.getWaitInterest(), invest.getRaiseInterest().negate(), invest.getRealizeInterest().negate(), invest.getFreezeInterest());
		model.addAttribute("timeLimit", timeLimit);
		model.addAttribute("waitAmount", waitAmount.doubleValue());
		model.addAttribute("waitInterest", waitInterest.doubleValue());
		model.addAttribute("invest", invest);
		model.addAttribute("daysOfYear", ConfigUtils.getValue("days_of_year"));
		model.addAttribute("rule", rule);
		setToken(TokenConstants.TOKEN_REALIZE_COMMIT,request);
		return "/member/myRealize/setting";
	}
	
	/**
	 * 获取最大可变现金额
	 * @author fxl
	 * @date 2016年8月1日
	 * @param projectInvestId
	 * @param apr
	 * @param timeLimit
	 * @return
	 */
	//http://localhost/member/myRealize/getMostRealizeAmount.html?projectInvestId=58548fd5ed274e84956b64f3ae28c48e&apr=12&timeLimit=30
	@RequestMapping("/member/myRealize/getMostRealizeAmount")
	@ResponseBody
	public Object getMostRealizeAmount(final String projectInvestId,final BigDecimal apr,final BigDecimal timeLimit){
		final ProjectInvest invest = projectInvestService.get(projectInvestId);
		if(invest==null){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.REALIZE_INVEST_NOT_FOUND));
		}
		final BigDecimal mostRealizeAmount = realizeService.getMostRealizeAmount(invest.getWaitAmount(),apr,timeLimit);
		final Map<String, Object> data =renderSuccessResult();
		data.put("mostRealizeAmount", mostRealizeAmount);
		return data;
	}
	
	/**
	 * 发布变现
	 * @author fxl
	 * @date 2016年8月1日
	 * @param entity
	 * @param agree
	 * @return
	 */
	//localhost/member/myRealize/doSet.html?apr=1&account=1000&investId=XXXX&agree=1
	@RequestMapping(value = "/member/myRealize/doSet" )
	@ResponseBody
	@TokenValid(value=TokenConstants.TOKEN_REALIZE_COMMIT,dispatch=true)
	public Object doSet(final Realize entity,final String agree){
		final User user = getSessionUser();
		entity.setUserId(user.getUuid());
		if(StringUtils.isBlank(agree) || !agree.equals(IS_AGREE)){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.REALIZE_PROTOCOL_NOT_CHECK));
		}
		realizeService.publish(entity);
		
		return renderResult(true, MSG_SUCCESS, "/member/myRealize/list.html");
	}

	
	/**
	 * 放款调用(测试用)
	 * @author fxl
	 * @date 2016年8月16日
	 * @param uuid
	 * @return
	 */
	// localhost/member/myRealize/doFullSuccess.html?uuid=
	@RequestMapping(value = "/member/myRealize/doFullSuccess" )
	@ResponseBody
	public Object doFullSuccess(final String uuid){
		realizeService.fullSuccess(uuid);
		return renderSuccessResult();
	}
	
	/**
	 * 变现还款
	 * @author fxl
	 * @date 2016年8月1日
	 * @param entity
	 * @return
	 */
	//http://localhost/member/myRealize/doRepay.html?uuid=
	@RequestMapping(value = "/member/myRealize/doRepay" )
	@ResponseBody
	public Object doRepay(final Realize entity){
		final User user = getSessionUser();
		entity.setUserId(user.getUuid());
		if(CbhbConstant.CBHB_TPP_NAME.equals(ConfigUtils.getTppName())){
			realizeRepaymentService.repayCbhb(entity);
		}else{
			realizeRepaymentService.repay(entity);
		}
		return renderSuccessResult();
	}
	
	
	/**
	 * 撤销
	 * @author fxl
	 * @date 2016年8月13日
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = "/member/myRealize/doCancel" )
	@ResponseBody
	public Object doCancel(final String uuid){
		realizeService.cancel(uuid);
		return renderSuccessResult();
	}
	
	/**
	 * 变现协议
	 * @author xhf
	 * @date 2016年9月11日
	 * @param protocolId
	 * @return
	 */
	@RequestMapping(value = "/member/myRealize/realizeProtocol")
	@ResponseBody
	public Object realizeProtocol() {
		final User user = getSessionUser();
		return protocolService.getRealizeProtocol(user);
	}
	
}
