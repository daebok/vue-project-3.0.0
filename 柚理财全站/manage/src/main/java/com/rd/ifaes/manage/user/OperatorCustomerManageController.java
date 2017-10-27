package com.rd.ifaes.manage.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.PrincipalUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.validator.BeanValidators;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.sys.domain.Operator;
import com.rd.ifaes.core.sys.domain.OperatorCustomer;
import com.rd.ifaes.core.sys.service.OperatorCustomerService;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.manage.sys.SystemController;

/**
 * 经纪人 -客户管理
 * @author wyw
 *
 */
@Controller
public class OperatorCustomerManageController extends SystemController {
	
	@Resource
	private UserService userService;
	@Resource
	private OperatorCustomerService operatorCustomerService;
	/**
	 * 
	 * 经纪人客户页面
	 * @author wyw
	 * @date 2016-8-19
	 * @return
	 */
	@RequestMapping(value = "/user/customer/customerManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.OPERATOR_CUSTOMER)
	public String customerManage(){
		return "/user/customer/customerManage";
	}
	
	/**
	 * 
	 * 经纪人客户列表数据
	 * @author wyw
	 * @date 2016-8-19
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/user/customer/getCustomerList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.OPERATOR_CUSTOMER)
	public Page<OperatorCustomer> getCustomerList(OperatorCustomer model){
		Operator operator = (Operator) PrincipalUtils.getPrincipal();
		model.setOperatorId(operator.getUuid());
		model.setUserNature(UserCache.USER_NATURE_VOUCH);//不包括担保机构
		return operatorCustomerService.findPage(model);
	}
	/**
	 * 
	 * 客户添加页面
	 * @author wyw
	 * @date 2016-8-19
	 * @return
	 */
	@RequestMapping(value = "/user/customer/customerAdd")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.OPERATOR_CUSTOMER)
	public String customerAddPage(){
		return "/user/customer/customerAdd";
	}
	
	/**
	 * 
	 * 添加客户请求
	 * @author wyw
	 * @date 2016-8-19
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/user/customer/doAdd", method = RequestMethod.POST)
	@ResponseBody
	@TokenValid(value=TokenConstants.TOKEN_ADD_OPERATOR_CUSTOMER,dispatch=true) 
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.OPERATOR_CUSTOMER)
	public Map<String, Object> doAdd( OperatorCustomer model , HttpServletRequest request)throws Exception{
		BeanValidators.validate(model);
		if(StringUtils.isBlank(model.getOperatorId())){
			Operator operator = (Operator) PrincipalUtils.getPrincipal();
			model.setOperatorId(operator.getUuid());
		}
		operatorCustomerService.addCustomerManager(model);
		return renderSuccessResult();						
	}	
	/**
	 * 
	 * 修改客户
	 * @author wyw
	 * @date 2016-8-19
	 * @return
	 */
	@RequestMapping(value = "/user/customer/customerEdit")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.OPERATOR_CUSTOMER)
	public String customerEditPage(String id ,final Model model) {
		if(StringUtils.isNotBlank(id)){
			 model.addAttribute("customer", operatorCustomerService.get(id));
		}
		return "/user/customer/customerEdit";
	}
	/**
	 * 修改请求
	 * @return 
	 */
	@RequestMapping(value = "/user/customer/doEdit", method = RequestMethod.POST)
	@ResponseBody
	@TokenValid(value=TokenConstants.TOKEN_EDIT_OPERATOR_CUSTOMER,dispatch=true) 
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.OPERATOR_CUSTOMER)
	public Map<String, Object> doEdit( OperatorCustomer model , HttpServletRequest request)throws Exception{		
		if(StringUtils.isNotBlank(model.getRemark())){
		    operatorCustomerService.editCustomerManager(model);
		}
			return renderSuccessResult();			
	}
	/**
	 * 
	 * 业务统计页面
	 * @author wyw
	 * @date 2016-8-20
	 * @return
	 */
	@RequestMapping(value = "/user/customer/saleStatistics")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.SALE_STATISTICS)
	public String saleStatistics(final Model model){
		int currentYear = Integer.valueOf(DateUtils.getYear());
		model.addAttribute("currentYear", currentYear);
		model.addAttribute("years", getStatisticsYears(currentYear-2, currentYear+2));
		return "/user/customer/saleStatistics";
	}
	
	private List<Integer> getStatisticsYears(int start, int end){
		List<Integer> years = new ArrayList<>();
		for (int i = start; i <= end; i++) {
			years.add(i);
		}
		return years;
	}

	/**
	 * 
	 * 业绩统计数据
	 * @author wyw
	 * @date 2016-8-20
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/user/customer/getSaleStatistics")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.SALE_STATISTICS)
	public Object getimple(final OperatorCustomer model,final String year){
		Operator operator = (Operator) PrincipalUtils.getPrincipal();
		String queryYear = year;
		model.setOperatorId(operator.getUuid());
		if(StringUtils.isBlank(year)){
			queryYear  = DateUtils.getYear();
		}
		model.setPage(null);
		String startTime = DateUtils.monthStartTime(queryYear, 1);
		String endTime = DateUtils.monthEndTime(queryYear, 12);
		model.setStartTime(startTime);
		model.setEndTime(endTime);
		return operatorCustomerService.findSaleStatistics(model);
	}
}
