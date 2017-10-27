package com.rd.ifaes.manage.user;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.domain.UserQualificationApply;
import com.rd.ifaes.core.user.domain.UserQualificationUpload;
import com.rd.ifaes.core.user.service.UserBaseInfoService;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserCompanyInfoService;
import com.rd.ifaes.core.user.service.UserQualificationApplyService;
import com.rd.ifaes.core.user.service.UserQualificationUploadService;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.manage.sys.SystemController;

/**
 * 
 *  会员管理-资质认证
 * @version 3.0
 * @author xhf
 * @date 2016年7月30日
 */
@Controller
public class UserQualificationManageController extends SystemController {
	
	@Resource
	private UserService userService;
	@Resource
	private UserCacheService userCacheService;
	@Resource
	private UserBaseInfoService userBaseInfoService;
	@Resource
	private UserCompanyInfoService userCompanyInfoService;
	@Resource
	private UserQualificationApplyService applyService;
	@Resource
	private UserQualificationUploadService uploadService;
	
	/**
	 * 列表页面
	 * @return
	 */
	@RequestMapping(value = "/user/userQualification/manage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_QUALIFICATION)
	public String userQualificationManage(String status,final Model model) {
		 model.addAttribute("status", status);
		return "/user/userQualification/manage";
	}
	
	/**
	 * 列表数据
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/user/userQualification/getLogList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_QUALIFICATION)
	public Page<UserQualificationApply> getLogList(UserQualificationApply model){
		return applyService.getLogList(model);
	}
	
	/**
	 * 查看页面
	 * @return 
	 */
	@RequestMapping(value = "/user/userQualification/view")
	@SystemLog(method=SysLogConstant.VIEW,content=SysLogConstant.USER_QUALIFICATION)
	public String view(String id,final Model model) {
		UserQualificationApply apply = applyService.get(id);
		String userId = apply.getUserId();
		 model.addAttribute("apply", apply);
		
		//用户信息
		User user = userService.get(userId);
		 model.addAttribute("user", user);
		
		//附属信息
		UserCache userCache = userCacheService.findByUserId(userId);
		 model.addAttribute("userCache", userCache);
		
		//资质文件列表
		List<UserQualificationUpload> uploadList = uploadService.findByQualificationApply(apply.getUuid());
		 model.addAttribute("picList", uploadList);
		
		//基本信息
		String goPage="";
		if(UserCache.USER_NATURE_PERSON.equals(userCache.getUserNature())){
			goPage="/user/userQualification/viewPerson";
			 model.addAttribute("userBaseInfo", userBaseInfoService.findByUserId(userId));
		}else{
			goPage="/user/userQualification/viewCompany";
			 model.addAttribute("userCompanyInfo", userCompanyInfoService.findByUserId(userId));
		}
		return goPage;
	}
	
	/**
	 * 审核页面
	 * @return
	 */
	@RequiresPermissions("vip:qualification:auditing")
	@RequestMapping(value = "/user/userQualification/audit")
	@SystemLog(method=SysLogConstant.AUDIT,content=SysLogConstant.USER_QUALIFICATION)
	public String audit(String id,final Model model) {
		UserQualificationApply apply = applyService.get(id);
		String userId = apply.getUserId();
		 model.addAttribute("apply", apply);
		
		//用户信息
		User user = userService.get(userId);
		 model.addAttribute("user", user);
		
		//附属信息
		UserCache userCache = userCacheService.findByUserId(userId);
		 model.addAttribute("userCache", userCache);
		
		//资质文件列表
		List<UserQualificationUpload> uploadList = uploadService.findByQualificationApply(apply.getUuid());
		 model.addAttribute("picList", uploadList);
		
		//基本信息
		String goPage="";
		if(UserCache.USER_NATURE_PERSON.equals(userCache.getUserNature())){
			goPage="/user/userQualification/auditPerson";
			 model.addAttribute("userBaseInfo", userBaseInfoService.findByUserId(userId));
		}else{
			goPage="/user/userQualification/auditCompany";
			 model.addAttribute("userCompanyInfo", userCompanyInfoService.findByUserId(userId));
		}
		return goPage;
	}
	
	/**
	 * 审核请求
	 * @return 
	 */
	@RequiresPermissions("vip:qualification:auditing")
	@RequestMapping(value = "/user/userQualification/doAudit")
	@ResponseBody
	@SystemLog(method=SysLogConstant.AUDIT,content=SysLogConstant.USER_QUALIFICATION)
	public Map<String, Object> doAudit(UserQualificationApply model){		
	    model.setVerifyUser(getUserId());
	    applyService.doAudit(model);
	    return renderResult(true, "操作成功");
	}
	
}
