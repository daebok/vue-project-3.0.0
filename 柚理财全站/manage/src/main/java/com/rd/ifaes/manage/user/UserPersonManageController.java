package com.rd.ifaes.manage.user;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;



import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.excel.ExportModel;
import com.rd.ifaes.common.util.excel.ExportUtil;
import com.rd.ifaes.common.validator.BeanValidators;
import com.rd.ifaes.core.account.service.AccountBankCardService;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.DictUtils;
import com.rd.ifaes.core.core.util.OrderNoUtils;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.model.UserModel;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserFreezeService;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.manage.sys.SystemController;

/**
 * 
 *  会员管理-个人用户
 * @version 3.0
 * @author xhf
 * @date 2016年7月30日
 */
@Controller
public class UserPersonManageController extends SystemController {
	
	@Resource
	private UserService userService;
	@Resource
	private UserCacheService userCacheService;
	@Resource
	private UserFreezeService userFreezeService;
	@Resource
	private transient AccountBankCardService accountBankCardService;
	//路径拼接符 "/"
	private static String separator = File.separator;
	
	/**
	 * 列表页面
	 * @return
	 */
	@RequestMapping(value = "/user/userPerson/manage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER)
	public String userPersonManage(final Model model){
		model.addAttribute("tppName", ConfigUtils.getTppName());
		return "/user/userPerson/manage";
	}
	/**
	 * 列表数据
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/user/userPerson/getLogList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER)
	public Page<User> getLogList(UserModel model){
		model.setUserNature(UserCache.USER_NATURE_PERSON);
		return userService.findPersonUser(model);
	}
	/**
	 * 添加页面
	 * @return 
	 */
	@RequiresPermissions("vip:member:add")
	@RequestMapping(value = "/user/userPerson/add")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.USER)
	public String add(final Model model) {
		String userNo = OrderNoUtils.getSerialNumber();
		 model.addAttribute("userNo", userNo);
		 model.addAttribute("userName", Constant.USER_NAME_PREFIX + userNo);
		 model.addAttribute("defalutPwd", ConfigUtils.getValue("default_user_pwd"));
		return "/user/userPerson/add";
	}
	
	/**
	 * 添加请求
	 * @return 
	 * @throws Exception 
	 */
	@RequiresPermissions("vip:member:add")
	@RequestMapping(value = "/user/userPerson/doAdd")
	@ResponseBody
	@TokenValid(value=TokenConstants.TOKEN_ADD_USER_PERSON,dispatch=true)
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.USER)
	public Object doAdd(UserModel model, BindingResult bindingResult) throws Exception{		
		BeanValidators.validate(model);
		Map<String, Object> resultMap = renderBindingResult(bindingResult);
	    if (resultMap == null) {  	
	    	userService.addUserByManage(model);
	    	resultMap = renderSuccessResult();
	    }
	    return resultMap;
	}
	
	/**
	 * 渤海银行 --- 批量添加页面
	 * @return 
	 */
	@RequiresPermissions("vip:member:add")
	@RequestMapping(value = "/user/userPerson/addBatch")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.USER)
	public String addBatch(final Model model) {
		return "/user/userPerson/addBatch";
	}
	
	/**
	 * 渤海银行 --- 批量添加请求
	 * @return 
	 * @throws Exception 
	 */
	@RequiresPermissions("vip:member:add")
	@ResponseBody
	@RequestMapping(value = "/user/userPerson/doAddBatch")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.USER)
	public String doAddBatch(@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request) throws Exception{		
		String path = request.getSession().getServletContext().getRealPath("/");
		path = path + separator + "data" + separator + "userPerson" + separator + DateUtils.dateStr7(new Date());
        String fileName = file.getOriginalFilename()+DateUtils.dateStr3(new Date());  
        File targetFile = new File(path, fileName);
        if(!targetFile.exists()){
            targetFile.mkdirs();  
        }  
        try {
			file.transferTo(targetFile);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
       return reBuildJson(userService.cbhbRealName(targetFile));
	}
	
	/**
	 * 修改页面
	 * @return
	 */
	@RequiresPermissions("vip:member:edit")
	@RequestMapping(value = "/user/userPerson/edit")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.USER)
	public String edit(String id,final Model model) {
		if(StringUtils.isNotBlank(id)){
			User user = userService.get(id);
			 model.addAttribute("user", user);
			
			UserCache userCache = userCacheService.findByUserId(user.getUuid());
			if(!Constant.FLAG_YES.equals(userCache.getRegMode())){
				throw new BussinessException("该用户非后台录入，不可修改");
			}
		}
		return "/user/userPerson/edit";
	}
	/**
	 * 用户账号类型修改
	 * @return
	 */
	@RequiresPermissions("vip:member:edit")
	@RequestMapping(value = "/user/userPerson/editAcountType")
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
	 * 修改请求
	 * @return 
	 */
	@RequestMapping(value = "/user/userPerson/doEditAcountType")
	@ResponseBody
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.USER)
	public Object doEditAcountType(UserModel model)throws Exception{	
		if(model == null || StringUtils.isBlank(model.getUuid())){
			throw new BussinessException("没有找到用户！");
		}
		if(!User.ACCOUNT_TYPE_JING.equals(model.getAccountType()) 
				&& !User.ACCOUNT_TYPE_YOU.equals(model.getAccountType())){
			throw new BussinessException("用户账号类型非法！");
		}
    	userService.editUserAccountTypeByManage(model);
    	//删除缓存
    	CacheUtils.del(CacheConstant.KEY_PREFIX_USER_USER_ID.concat(model.getUuid()));
	    return renderSuccessResult();
	}
	
	/**
	 * 修改请求
	 * @return 
	 */
	@RequiresPermissions("vip:member:edit")
	@RequestMapping(value = "/user/userPerson/doEdit")
	@ResponseBody
	@TokenValid(value=TokenConstants.TOKEN_EDIT_USER_PERSON,dispatch=true)
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.USER)
	public Object doEdit(UserModel model, BindingResult bindingResult)throws Exception{		
		BeanValidators.validate(model);
		Map<String, Object> resultMap = renderBindingResult(bindingResult);
	    if (resultMap == null) {  	
	    	userService.editUserByManage(model);
	    	resultMap = renderSuccessResult();
	    }
	    return resultMap;
	}
	
	/**
	 * 冻结页面
	 * @return
	 */
	@RequiresPermissions("vip:member:freeze")
	@RequestMapping(value = "/user/userPerson/freeze")
	@SystemLog(method=SysLogConstant.FREEZE,content=SysLogConstant.USER)
	public String freeze(String id, final Model model) {
		if(StringUtils.isBlank(id)){
			throw new BussinessException("请先选择一条记录",BussinessException.TYPE_CLOSE);
		}
		User user = userService.get(id);
		UserCache userCache = userCacheService.findByUserId(user.getUuid());
		//参数
		 model.addAttribute("uuid", user.getUuid());
		 model.addAttribute("status", user.getStatus());
		 model.addAttribute("userName", user.getUserName());
		 model.addAttribute("lockRemark", userCache.getLockRemark());
		//操作功能列表
		 model.addAttribute("freezeOperationList", DictUtils.list(DictTypeEnum.USER_FREEZE.getValue()));
		 model.addAttribute("userFreezeList", userFreezeService.findOperationByUserId(user.getUuid()));
		return "/user/userPerson/freeze";
	}
	
	/**
	 * 冻结解冻请求
	 * @return 
	 */
	@RequiresPermissions("vip:member:freeze")
	@RequestMapping(value = "/user/userPerson/doFreeze")
	@ResponseBody
	@SystemLog(method=SysLogConstant.FREEZE,content=SysLogConstant.USER)
	public Object doFreeze(UserModel model)throws Exception{	
        userFreezeService.freeze(model);
	    return renderResult(true, "操作成功");
	}
	
	/**
	 * 导出认证记录列表
	 * 
	 * @throws Exception
	 */
	@RequiresPermissions("vip:member:export")
	@RequestMapping(value = "/user/userPerson/export")
	@SystemLog(method=SysLogConstant.EXPORT,content=SysLogConstant.USER)
	public void export(final UserModel model,final HttpServletRequest request,final HttpServletResponse response) throws IOException{
		model.setUserNature(UserCache.USER_NATURE_PERSON);
		ExportUtil.exportExcel(new ExportModel<UserModel>() {
			@Override
			public String getCacheKey() {
				return  UserPersonManageController.this.getUserId();
			}
			@Override
			public UserModel getModel() {
				return model;
			}
			@Override
			public int getTotal(UserModel entity) {
				return userService.getPersonUserCount(entity);
			}
			@Override
			public List<User> getPageRecords(UserModel entity) {
				return userService.findPersonUser(entity).getRows();
			}
		}, request, response);
	}
	
	
	/**
	 * 银行卡页面
	 * @return
	 */
	@RequiresPermissions("vip:member:query")
	@RequestMapping(value = "/user/userPerson/bankListManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER)
	public String bankListManage(String id, final Model model) {
		if(StringUtils.isBlank(id)){
			throw new BussinessException("请先选择一条记录",BussinessException.TYPE_CLOSE);
		}
		User user = userService.get(id);
		//参数
		 model.addAttribute("uuid", user.getUuid());
		return "/user/userPerson/bankListManage";
	}
	
	/**
	 * 银行卡列表
	 * @return 
	 */
	@RequestMapping(value = "/user/userPerson/bankList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER)
	public Object bankList(UserModel model)throws Exception{	
		String userId = model.getUuid();
	    return  accountBankCardService.findList(userId);
	}
	/**
	 * 投资情况页面
	 */
	@RequestMapping(value = "/user/userPerson/userInvestPage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER)
	public String userInvestPage(String id,final Model model){
		if(StringUtils.isBlank(id)){
			throw new BussinessException("请先选择一条记录",BussinessException.TYPE_CLOSE);
		}
		User user = userService.get(id);
		model.addAttribute("uuid", user.getUuid());
		return "/user/userPerson/userInvestPage";
	}
	/**
	 * 处理投资情况请求
	 * @return 
	 */
	@RequestMapping(value = "/user/userPerson/userInvest")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER)
	public Object userInvest(UserModel model)throws Exception{	
		String id = model.getUuid();
		String username="no";//因为必须要username传到页面
		User userInvest=userService.queryInvestDetails(id);
		User userBorrow=userService.queryBorrowDetails(id);
		if(userInvest==null){//没有投资记录
			userInvest=new User();
			userInvest.setUserName(username);
			userInvest.setInvestNum(0);
			userInvest.setInvestStatus("未投过资");
		}
		else{
			userInvest.setInvestStatus("已投过资");
		}
		if(userBorrow==null){//没有借款记录
			userBorrow=new User();
			userBorrow.setUserName(username);
			userBorrow.setBorrowNum(0);
			userBorrow.setBorrowStatus("未过借款");
		}
		else{
			userBorrow.setBorrowStatus("已借过款");
		}
		if(userInvest!=null && userBorrow!=null){
			userInvest.setBorrowNum(userBorrow.getBorrowNum());
			if(userBorrow.getBorrowNum()>0){
				userInvest.setBorrowStatus("已借过款");
			}
			else{
				userInvest.setBorrowStatus("未借过款");
			}
		}
		List<User> list = new ArrayList<User>();
		list.add(userInvest);
	    return list;
	}
}
