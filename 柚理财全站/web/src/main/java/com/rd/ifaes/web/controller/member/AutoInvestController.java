package com.rd.ifaes.web.controller.member;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.model.AutoInvestRuleLogModel;
import com.rd.ifaes.core.user.service.UserAutoInvestService;
import com.rd.ifaes.web.controller.WebController;

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
	
	/**
	 * 
	 * 自动投标设置数据
	 * @author zb
	 * @date 2016年7月28日
	 * @return
	 */
	@RequestMapping(value = "/member/autoInvestRule")
	@ResponseBody
	public Object autoInvestRule(){
		return userAutoInvestService.getData();
	}
	
	/**
	 * 
	 * 添加用户自动投标规则
	 * @author zb
	 * @date 2016年7月28日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/member/addAutoInvest")
	@ResponseBody
	public Object addAutoInvest(final AutoInvestRuleLogModel model,final HttpServletRequest request){
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
			model.setRepayStyles(sb.toString());
		}else{
			throw new BussinessException(ResourceUtils.get(ResourceConstant.AUTO_INVEST_RULE_LOG_REPAY_STYLES_NOT_EMPTY));
		}
		userAutoInvestService.add(model,sessionUser);
		final Map<String,Object> data = new HashMap<>();
		data.put(RESULT_NAME, true);
		data.put(MSG_NAME, ResourceUtils.get(ResourceConstant.AUTO_INVEST_RULE_LOG_SUCCESS));
		data.put(URL, "/member/auto/setting.html");
		return data;
	}
	
	/**
	 * 
	 * 关闭用户自动投标规则
	 * @author zb
	 * @date 2016年7月28日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/member/closeAutoInvest")
	@ResponseBody
	public Object closeAutoInvest(){
		final User sessionUser = SessionUtils.getSessionUser(BussinessException.TYPE_CLOSE);
		return userAutoInvestService.close(sessionUser);
	}
}
