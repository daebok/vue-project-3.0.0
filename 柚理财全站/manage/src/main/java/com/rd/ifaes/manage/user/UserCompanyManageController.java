package com.rd.ifaes.manage.user;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.excel.ExportModel;
import com.rd.ifaes.common.util.excel.ExportUtil;
import com.rd.ifaes.common.validator.BeanValidators;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.DictUtils;
import com.rd.ifaes.core.core.util.OrderNoUtils;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.domain.UserCompanyInfo;
import com.rd.ifaes.core.user.model.UserModel;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserCompanyInfoService;
import com.rd.ifaes.core.user.service.UserFreezeService;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.manage.sys.SystemController;

/**
 * 
 *  会员管理-企业用户
 * @version 3.0
 * @author xhf
 * @date 2016年7月30日
 */
@SuppressWarnings("unchecked")
@Controller
public class UserCompanyManageController extends SystemController {
	
	@Resource
	private UserService userService;
	@Resource
	private UserCacheService userCacheService;
	@Resource
	private UserFreezeService userFreezeService;
	@Resource
	private UserCompanyInfoService userCompanyInfoService;
	
	/**
	 * 列表页面
	 * @return
	 */
	@RequestMapping(value = "/user/userCompany/manage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_COMPANY)
	public String userCompanyManage(){
		return "/user/userCompany/manage";
	}
	/**
	 * 列表数据
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/user/userCompany/getLogList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_COMPANY)
	public Page<User> getLogList(UserModel model){
		model.setUserNature(UserCache.USER_NATURE_COMPANY);
		return userService.findCompanyUser(model);
	}
	
	@RequestMapping(value = "/user/userCompany/doQuery")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_COMPANY)
	public Object query(final String accountId) throws Exception{		
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> map = new HashMap<>();
		map.put("accountId", accountId);
		resultMap = (Map<String, Object>) userService.queryCorpration(map);
	    return resultMap;
	}
	
	
	/**
	 * 添加页面
	 * @return 
	 */
	@RequiresPermissions("vip:company:add")
	@RequestMapping(value = "/user/userCompany/add")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.USER_COMPANY)
	public String add(final Model model) {
		String userNo = OrderNoUtils.getSerialNumber();
		model.addAttribute("tppName", ConfigUtils.getTppName());
		model.addAttribute("userNo", userNo);
		model.addAttribute("userName", Constant.USER_NAME_PREFIX + userNo);
		model.addAttribute("defalutPwd", ConfigUtils.getValue("default_user_pwd"));
		return "/user/userCompany/add";
	}
	
	/**
	 * 用户账号类型修改
	 * @return
	 */
	@RequiresPermissions("vip:company:edit")
	@RequestMapping(value = "/user/userCompany/editAcountType")
	public String editAcountType(String id,final Model model) {
		if(StringUtils.isNotBlank(id)){
			User user = userService.get(id);
			model.addAttribute("user", user);
			model.addAttribute("userAccountTypeList", DictUtils.list(DictTypeEnum.USER_ACCOUNT_TYPE.getValue()));
		}else{
			throw new BussinessException("请选择一条记录进行修改！");
		}
		return "/user/userPerson/editAccountType";
	}
	
	/**
	 * 添加请求
	 * @return 
	 * @throws Exception 
	 */
	@RequiresPermissions("vip:company:add")
	@RequestMapping(value = "/user/userCompany/doAdd")
	@ResponseBody
	@TokenValid(value=TokenConstants.TOKEN_ADD_USER_COMPANY,dispatch=true)
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.USER_COMPANY)
	public Object doAdd( UserModel model, BindingResult bindingResult) throws Exception{		
		BeanValidators.validate(model);
		Map<String, Object> resultMap = renderBindingResult(bindingResult);
	    if (resultMap == null) {  	
	    	userService.addCompanyByManage(model);
	    	resultMap = renderSuccessResult();
	    }
	    return resultMap;
	}
	
	/**
	 * 修改页面
	 * @return
	 */
	@RequiresPermissions("vip:company:edit")
	@RequestMapping(value = "/user/userCompany/edit")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.USER_COMPANY)
	public String edit(String id, final Model model) {
		if(StringUtils.isNotBlank(id)){
			User user = userService.get(id);
			 model.addAttribute("user", user);
			UserCache userCache = userCacheService.findByUserId(user.getUuid());
			if(!Constant.FLAG_YES.equals(userCache.getRegMode())){
				throw new BussinessException("该用户非后台录入，不可修改");
			}
			//企业信息
			model.addAttribute("userCompanyInfo", userCompanyInfoService.findByUserId(user.getUuid()));
		}
		return "/user/userCompany/edit";
	}
	
	/**
	 * 修改请求
	 * @return 
	 */
	@RequiresPermissions("vip:company:edit")
	@RequestMapping(value = "/user/userCompany/doEdit")
	@ResponseBody
	@TokenValid(value=TokenConstants.TOKEN_EDIT_USER_COMPANY,dispatch=true)
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.USER_COMPANY)
	public Object doEdit( UserModel model, BindingResult bindingResult)throws Exception{		
		BeanValidators.validate(model);
		Map<String, Object> resultMap = renderBindingResult(bindingResult);
	    if (resultMap == null) {  	
	    	userService.editCompanyByManage(model);
	    	resultMap = renderSuccessResult();
	    }
	    return resultMap;
	}
	
	/**
	 * 冻结页面
	 * @return
	 */
	@RequiresPermissions("vip:company:freeze")
	@RequestMapping(value = "/user/userCompany/freeze")
	@SystemLog(method=SysLogConstant.FREEZE,content=SysLogConstant.USER_COMPANY)
	public String freeze(String id, final Model model) {
		if(StringUtils.isBlank(id)){
			throw new BussinessException("请先选择一条记录",BussinessException.TYPE_CLOSE);
		}
		User user = userService.get(id);
		UserCache userCache = userCacheService.findByUserId(user.getUuid());
		UserCompanyInfo userCompanyInfo = userCompanyInfoService.findByUserId(user.getUuid());
		
		//参数
		 model.addAttribute("uuid", user.getUuid());
		 model.addAttribute("status", user.getStatus());
		 model.addAttribute("companyName", userCompanyInfo.getCompanyName());
		 model.addAttribute("lockRemark", userCache.getLockRemark());
		//操作功能列表
		if(UserCache.USER_NATURE_VOUCH.equals(userCache.getUserNature())){
			 model.addAttribute("freezeOperationList", DictUtils.list(DictTypeEnum.VOUCH_FREEZE.getValue()));
		}else{
			 model.addAttribute("freezeOperationList", DictUtils.list(DictTypeEnum.USER_FREEZE.getValue()));
		}
		 model.addAttribute("userFreezeList", userFreezeService.findOperationByUserId(user.getUuid()));
		return "/user/userCompany/freeze";
	}
	
	/**
	 * 冻结解冻请求
	 * @return 
	 */
	@RequiresPermissions("vip:company:freeze")
	@RequestMapping(value = "/user/userCompany/doFreeze")
	@ResponseBody
	@SystemLog(method=SysLogConstant.FREEZE,content=SysLogConstant.USER_COMPANY)
	public Object doFreeze(UserModel model)throws Exception{	
        userFreezeService.freeze(model);
	    return renderResult(true, "操作成功");
	}
	
	/**
	 * 导出认证记录列表
	 * 
	 * @throws Exception
	 */
	@RequiresPermissions("vip:company:export")
	@RequestMapping(value = "/user/userCompany/export")
	@SystemLog(method=SysLogConstant.EXPORT,content=SysLogConstant.USER_COMPANY)
	public void export(final UserModel model,final HttpServletRequest request,final HttpServletResponse response) throws IOException{
		// 获取记录
		model.setUserNature(UserCache.USER_NATURE_COMPANY);
		
		ExportUtil.exportExcel(new ExportModel<UserModel>() {
			@Override
			public String getCacheKey() {
				return  UserCompanyManageController.this.getUserId();
			}
			@Override
			public UserModel getModel() {
				return model;
			}
			@Override
			public int getTotal(UserModel entity) {
				return userService.getCompanyUserCount(entity);
			}
			@Override
			public List<User> getPageRecords(UserModel entity) {
				return userService.findCompanyUser(entity).getRows();
			}
		}, request, response);
	}
	
}
