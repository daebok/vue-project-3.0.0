package com.rd.ifaes.web.controller.member;

import java.math.BigDecimal;
import java.util.HashMap;
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
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.JsonMapper;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TokenUtils;
import com.rd.ifaes.common.util.ip.IPUtil;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.DictConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.interest.EachPlan;
import com.rd.ifaes.core.interest.InterestCalculator;
import com.rd.ifaes.core.project.worker.ProjectWorker;
import com.rd.ifaes.core.sys.domain.DictData;
import com.rd.ifaes.core.sys.service.DictDataService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.model.InterestCalculationModel;
import com.rd.ifaes.core.user.model.UserOpinionModel;
import com.rd.ifaes.core.user.service.UserOpinionService;
import com.rd.ifaes.web.controller.WebController;

/**
 * 我的助手(首页工具栏-客服、收益计算、意见反馈)
 * @version 3.0
 * @author xxb
 * @date 2016年10月10日
 */
@Controller
public class MyAssistantController extends WebController {
	
	@Resource
	private transient UserOpinionService userOpinionService;
	
	@Resource
	private transient DictDataService dictDataService;
	
	/**
	 * 意见反馈维护页面
	 * @author xxb
	 * @date 2016年10月10日
	 * @return
	 */
	@RequestMapping("/member/myAssistant/opinionAdd")
	public String opinionAdd(Model model,final HttpServletRequest request){
		model.addAttribute(ConfigConstant.WEB_IMAGE_SERVER_URL, ConfigUtils.getValue(ConfigConstant.IMAGE_SERVER_URL));
		setToken(TokenConstants.TOKEN_ASSISTANT_COMMIT, request);
		return "/member/myAssistant/opinionAdd";
	}

	/**
	 * 
	 * 意见反馈新增保存
	 * @param  
	 * @return String
	 * @author xxb
	 * @date 2016年10月10日
	 */
	@RequestMapping("/member/myAssistant/opinionAddSave")
	@ResponseBody
	@TokenValid(value=TokenConstants.TOKEN_ASSISTANT_COMMIT,dispatch=true)
	public Object opinionAddSave(final HttpServletRequest request,final UserOpinionModel userOpinion){
		//检验验证码
		userOpinion.checkImgValidCode();
		User user = getSessionUser();
		String[] attachmentPaths = request.getParameterValues("attachmentPaths[]");
		userOpinion.setAttachmentPath(StringUtils.contact(attachmentPaths));
		userOpinion.setAddIp(IPUtil.getRemortIP(request) );
		userOpinion.setCreateTime(DateUtils.getNow());
		userOpinion.setUserId(user.getUuid());
		userOpinion.setStatus("0");//未处理
		TokenUtils.validShiroToken(TokenConstants.TOKEN_ASSISTANT_COMMIT);//录入信息前进行token的校验
		userOpinionService.insert(userOpinion);
		return this.renderSuccessResult();
	}
	
	/**
	 * 进入收益计算页面
	 * @author xxb
	 * @date 2016年10月13日
	 * @return
	 */
	@RequestMapping("/member/myAssistant/incomeCalculation")
	public String incomeCalculation(Model model){
		List<DictData> repayStyleList = dictDataService.findListByDictType(DictConstant.REPAY_STYLE);
		model.addAttribute("repayStyle", JsonMapper.toJsonString(repayStyleList)); //还款方式
		return "/member/myAssistant/incomeCalculation";
	}
	
	/**
	 * 计算收益
	 * @author xxb
	 * @date 2016年10月13日
	 * @return
	 */
	@RequestMapping("/member/myAssistant/calculateIncome")
	@ResponseBody
	public Object calculateIncome(final InterestCalculationModel interestCalculation){
		//接收计算参数
		BigDecimal principal = interestCalculation.getPrincipal();
		String periodType = interestCalculation.getPeriodType();
		int period = interestCalculation.getPeriod();
		BigDecimal apr = BigDecimalUtils.div(interestCalculation.getApr(), BigDecimal.valueOf(100), 4);
		String repayStyle = interestCalculation.getRepayStyle();
		
		BigDecimal planCapital = BigDecimal.ZERO;//计划待收总本金
		BigDecimal planInterest = BigDecimal.ZERO;//计划待收总利息
		BigDecimal avg = BigDecimal.ZERO; //本金、利息均值
		
		//校验业务合法性
		interestCalculation.checkCalculation();
		//每季按3个月算
		if(LoanEnum.STYLE_QUARTER_INTEREST.getValue().equals(repayStyle)){
			period = period * Constant.INT_THREE;
		}
		//调用计算
		InterestCalculator ic = ProjectWorker.doInterestCalculator(repayStyle, principal, apr, BigDecimal.ZERO, periodType, period, BigDecimal.ZERO, null, null, null);

		//为避免整体上的误差，直接取第一期。
		List<EachPlan> planList = ic.getEachPlan();
		EachPlan plan = planList.get(Constant.INT_ZERO);
		planCapital = plan.getCapital();
		planInterest = plan.getInterest();
		//求均值
		if (LoanEnum.STYLE_MONTHLY_INTEREST.getValue().equals(repayStyle)) { // 每月还息到期还本
			avg = planInterest; // 月收利息取第一期的利息
		} else if (LoanEnum.STYLE_QUARTER_INTEREST.getValue().equals(repayStyle)) { // 每季还息到期还本 取第一期的利息
			avg = planInterest; // 季收利息
		} else if (LoanEnum.STYLE_INSTALLMENT_REPAYMENT.getValue().equals(repayStyle)) { // 月等额本息
			avg = BigDecimalUtils.add(planCapital, planInterest);// 月收本息 取第一期的本息
		} else if (LoanEnum.STYLE_AVERAGE_CAPITAL_REPAYMENT.getValue().equals(repayStyle)) { // 月等额本金
			avg = planCapital;// 月收本金取第一期的本金
		}
		
		//回传参数
		final Map<String, Object> data = new HashMap<>();

		//状态标识与普通属性平级传送
		data.put(RESULT_NAME, true);
		data.put("repayStyle", repayStyle);
		data.put("total", ic.collectTotal());
		data.put("principal", principal);
		data.put("interest", ic.repayInterest());
		data.put("avg", avg);
		return data;
	}
	
}
