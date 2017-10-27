package com.rd.ifaes.manage.user;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.excel.ExportModel;
import com.rd.ifaes.common.util.excel.ExportUtil;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.OrderNoUtils;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.model.UserModel;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserCompanyInfoService;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.manage.sys.SystemController;

/**
 * 会员管理-担保用户
 * @author wyw
 *
 */
@Controller
public class UserVouchManageController extends SystemController {
	
	@Resource
	private UserService userService;
	@Resource
	private UserCacheService userCacheService;
	@Resource
	private UserCompanyInfoService userCompanyInfoService;
	/**
	 * 列表页面
	 * @return
	 */
	@RequestMapping(value = "/user/userVouch/manage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.VOUCH)
	public String userVouchManage(){
		return "/user/userVouch/manage";
	}
	
	/**
	 * 列表数据
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/user/userVouch/getLogList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.VOUCH)
	public Page<User> getLogList(UserModel userModel){
		userModel.setUserNature(UserCache.USER_NATURE_VOUCH);
		return userService.findCompanyUser(userModel);
	}
	
	/**
	 * 添加页面
	 * @return 
	 */
	@RequiresPermissions("vip:vouch:add")
	@RequestMapping(value = "/user/userVouch/add")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.VOUCH)
	public String userVouchAddPage(final Model model) {
		String userNo = OrderNoUtils.getSerialNumber();
		 model.addAttribute("userNo", userNo);
		 model.addAttribute("userName", Constant.USER_NAME_PREFIX + userNo);
		 model.addAttribute("defalutPwd", ConfigUtils.getValue("default_user_pwd"));
		return "/user/userVouch/add";
	}
	
	/**
	 * 添加请求
	 * @return 
	 */
	@RequiresPermissions("vip:vouch:add")
	@RequestMapping(value = "/user/userVouch/doAdd", method = RequestMethod.POST)
	@ResponseBody
	@TokenValid(value=TokenConstants.TOKEN_ADD_USER_VOUCH,dispatch=true)
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.VOUCH)
	public Map<String, Object> doAdd( UserModel model , HttpServletRequest request)throws Exception{		
		//验证数据的有效性 
		userService.addVouchUser(model);
		return renderSuccessResult();			
	}
	
	/**
	 * 修改页面
	 * @return
	 */
	@RequestMapping(value = "/user/userVouch/edit")
	@RequiresPermissions("vip:vouch:edit")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.VOUCH)
	public String userVouchEditPage(String id,final Model model) {
		if(StringUtils.isNotBlank(id)){
			User user = userService.get(id);
			 model.addAttribute("user", user);
			//企业信息
			 model.addAttribute("userCompanyInfo", userCompanyInfoService.findByUserId(user.getUuid()));
			//清理缓存 
			CacheUtils.del(CacheConstant.KEY_PREFIX_USER_CACHE_USER_ID + user.getUuid());
			 model.addAttribute("userCache", userCacheService.findByUserId(user.getUuid()));
		}
		return "/user/userVouch/edit";
	}
	
	/**
	 * 修改请求
	 * @return 
	 */
	@RequestMapping(value = "/user/userVouch/doEdit", method = RequestMethod.POST)
	@TokenValid(value=TokenConstants.TOKEN_EDIT_USER_VOUCH,dispatch=true) 
	@RequiresPermissions("vip:vouch:edit")
	@ResponseBody
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.VOUCH)
	public Map<String, Object> doEdit( UserModel model , HttpServletRequest request)throws Exception{		
		userService.editVouchUser(model);
		return renderSuccessResult();			
	}
	
	/**
	 * 导出担保机构列表
	 * 
	 * @throws Exception
	 */
	@RequiresPermissions("vip:vouch:export")
	@RequestMapping(value = "/user/userVouch/export")
	@SystemLog(method=SysLogConstant.EXPORT,content=SysLogConstant.VOUCH)
	public void export(final UserModel model,final HttpServletRequest request,final HttpServletResponse response) throws IOException{
		
		model.setUserNature(UserCache.USER_NATURE_VOUCH);
		
		ExportUtil.exportExcel(new ExportModel<UserModel>() {
			@Override
			public String getCacheKey() {
				return  UserVouchManageController.this.getUserId();
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
