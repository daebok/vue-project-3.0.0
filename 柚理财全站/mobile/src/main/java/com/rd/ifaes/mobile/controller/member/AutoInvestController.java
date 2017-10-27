package com.rd.ifaes.mobile.controller.member;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.account.domain.Account;
import com.rd.account.model.AccountQueryModel;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.user.domain.AutoInvestRuleLog;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserAutoInvest;
import com.rd.ifaes.core.user.mapper.UserAutoInvestMapper;
import com.rd.ifaes.core.user.model.AutoInvestRuleLogModel;
import com.rd.ifaes.core.user.service.AutoInvestRuleLogService;
import com.rd.ifaes.core.user.service.UserAutoInvestService;
import com.rd.ifaes.mobile.controller.WebController;

/**
 * 自动投标设置
 * 
 * @author zb
 * @version 3.0
 * @date 2016-7-25
 */
@Controller
public class AutoInvestController extends WebController{
	
	/**
	 * 自动投资service
	 */
	@Resource
	private transient UserAutoInvestService userAutoInvestService;
	@Resource
	private transient UserAutoInvestMapper dao;
	@Resource
    private transient AutoInvestRuleLogService autoInvestRuleLogService;
	@Resource
	private transient AccountService accountService;
	/**
	 * 
	 * 自动投标设置页面
	 * @author zb
	 * @date 2016年7月28日
	 * @return
	 */
	@RequestMapping(value = "/member/auto/setting")
	public String autoInvest(){
		return "/member/auto/setting";
	}
	
	
	@RequestMapping(value = "/app/member/auto/toSet")
	@ResponseBody
	public Object autoInvestTo(HttpServletRequest request){
		Object obj=null;
		Map<String,Object>  data = new HashMap<String,Object>();
		try{
			User user = getAppSessionUser(request);
			final Account account = accountService.getByUserId(new AccountQueryModel(user.getUuid(), ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE)));
			data.put("userMoney", account.getUseMoney());
			//自动投标温馨提示
			data.put("warmTips","1.开启自动投资后，自动投资功能将在5分钟内生效。"+"\n"+
		            "2.自动投资时不可使用红包、加息券等优惠券进行投资。"+"\n"+
		            "3.规则生成后，将根据单日剩余可投金额、产品剩余金额和账户可用余额执行自动投资。"+"\n"+
		            "4.修改投资规则后会进入队尾重新排队。");
			final BigDecimal minAmount = ConfigUtils.getBigDecimal("auto_invest_min_usemoney");
			data.put("minAmount", minAmount);
			final UserAutoInvest autoInvest = dao.getAutoInvestByUserId(user.getUuid());
			//判断是否有开启自动投标
			if(autoInvest!=null){
				final AutoInvestRuleLog log = autoInvestRuleLogService.get(autoInvest.getRuleId());
				data.put("isAuto", log.getStatus());
			}
			
			obj=createSuccessAppResponse(data);
		}catch(Exception e){
			obj=dealException(e);
		}
		return obj;
	}
	
	
	/**
	 * 移动端--开启自动投资功能
	 * 自动投标设置数据
	 * @author xdj
	 * @date 2016年11月21日
	 * @return
	 */
	@RequestMapping(value = "/app/member/autoInvestRule")
	@ResponseBody
	public Object autoInvestRule(HttpServletRequest request){
		Object obj = null; 
		try{
		@SuppressWarnings("unused")
		User user = getAppSessionUser(request);
		Object data= userAutoInvestService.getData();
		//删除收益方式，把它移动到公共的类中去
		@SuppressWarnings("unchecked")
		Map<String,Object> data1=(Map<String,Object>)data;
		if(data1.containsKey("buttonName")){
			if(data1.get("buttonName").equals("去开通")){
				data1.put("url","app/member/security/realNameIdentify.html");
			}else if(data1.get("buttonName").equals("去授权")){
				data1.put("url","app/member/security/authSign.html");
			}else if(data1.get("buttonName").equals("立即评估")){
				data1.put("url","app/member/risk/userRiskPapers.html");
			}else if(data1.get("buttonName").equals("立即充值")){
				data1.put("url","app/member/recharge/detail.html");
			}
			
		}else{
			data1.remove("repayStyles");
		}
		obj=createSuccessAutoOpenAppResponse(data1);
		}catch(Exception e){
			obj=dealException(e);
		}
		return obj;
	}
	
	
	/**
	 * 移动端--自动投标详情
	 * 自动投标设置数据
	 * @author xdj
	 * @date 2016年11月30日
	 * @return
	 */
	@RequestMapping(value = "/app/member/autoInvestDetails")
	@ResponseBody
	public Object autoInvestDetails(HttpServletRequest request,AutoInvestRuleLog rule){
		Object obj=null;
		try{
		User user = getAppSessionUser(request);
		final UserAutoInvest autoInvest = dao.getAutoInvestByUserId(user.getUuid());
		if(autoInvest != null){
			 rule = autoInvestRuleLogService.get(autoInvest.getRuleId());
			if(!Constant.FLAG_YES.equals(rule.getStatus())){
				rule=null;	
			}
			}
		String repayStylesstr="";//解决rule.getRepayStyles() 排序问题  
		if(rule!=null){
			if(rule.getRepayStyles()!=null){
		if(rule.getRepayStyles().contains("1")){
			repayStylesstr=",1";
		}
		if(rule.getRepayStyles().contains("2")){
			repayStylesstr=repayStylesstr+",2";
		}
		if(rule.getRepayStyles().contains("3")){
			repayStylesstr=repayStylesstr+",3";
		}
		if(rule.getRepayStyles().contains("4")){
			repayStylesstr=repayStylesstr+",4";
		}
		if(rule.getRepayStyles().contains("5")){
			repayStylesstr=repayStylesstr+",5";
		}
		if(!"".equals(repayStylesstr)){
			repayStylesstr=repayStylesstr.replaceFirst(",", "");
		}
			}
		rule.setRepayStyles(repayStylesstr);
		}
		obj=createSuccessAppResponse(rule);
		}catch(Exception e){
			obj=dealException(e);
		}
		return obj;
	}
	
	/**
	 * 
	 * 添加用户自动投标规则
	 * @author xdj
	 * @date 2016年12月16日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/app/member/addAutoInvest")
	@ResponseBody
	public Object addAutoInvest(final AutoInvestRuleLogModel model,final HttpServletRequest request){
		Object obj=null;
		try{
		@SuppressWarnings("unused")
		User user = getAppSessionUser(request);
		final User sessionUser = SessionUtils.getSessionUser(BussinessException.TYPE_CLOSE);
		// 获取收益方式
		final String[] repayStyle = request.getParameterValues("repayStyle[]");
		if(repayStyle != null && repayStyle.length > 0){
			final StringBuilder sb = new StringBuilder();
			for(int i = 0; i < repayStyle.length; i++ ){
				sb.append(repayStyle[i]);
				if(i < repayStyle.length-1){
					sb.append(',');
				}
			}
			model.setRepayStyles(URLDecoder.decode(sb.toString(), "UTF-8"));
		}else{
			throw new BussinessException(ResourceUtils.get(ResourceConstant.AUTO_INVEST_RULE_LOG_REPAY_STYLES_NOT_EMPTY));
		}
		userAutoInvestService.add(model,sessionUser);
		final Map<String,Object> data = new HashMap<>();
		data.put(RESULT_NAME, true);
		data.put(MSG_NAME, ResourceUtils.get(ResourceConstant.AUTO_INVEST_RULE_LOG_SUCCESS));
		//data.put(URL, "/member/auto/setting.html");
		obj=createSuccessAutoOpenAppResponse(data);
		}catch(Exception e){
			obj=dealException(e);
		}
		return obj;
	}
	
	/**
	 * 
	 * 关闭用户自动投标规则
	 * @author zb
	 * @date 2016年7月28日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/app/member/closeAutoInvest")
	@ResponseBody
	public Object closeAutoInvest(final HttpServletRequest request){
		Object obj=null;
		try{
			User user = getAppSessionUser(request);
		obj=createSuccessAutoCloseAppResponse(userAutoInvestService.close(user));
		}catch(Exception e){
			obj=dealException(e);
		}
		return obj;
	}
}
