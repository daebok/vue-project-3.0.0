package com.rd.ifaes.mobile.controller.member;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.DictUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.risk.domain.LevelConfig;
import com.rd.ifaes.core.risk.service.LevelConfigService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserBaseInfo;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.domain.UserCompanyInfo;
import com.rd.ifaes.core.user.domain.UserIdentify;
import com.rd.ifaes.core.user.domain.UserSecurityAnswer;
import com.rd.ifaes.core.user.service.UserBaseInfoService;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserCompanyInfoService;
import com.rd.ifaes.core.user.service.UserIdentifyService;
import com.rd.ifaes.core.user.service.UserLoginLogService;
import com.rd.ifaes.core.user.service.UserSecurityAnswerService;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.mobile.controller.WebController;

/**
 * 
 *  账户管理-基本信息
 * @version 3.0
 * @author xhf
 * @date 2016年7月27日
 */
@Controller
public class BasicInfoController extends WebController {
	/**
	 * USERNAME
	 */
	private static final String USERNAME = "userName";
	/**
	 * 登录日志处理类
	 */
	@Resource
	private transient UserLoginLogService loginLogService;
	/**
	 * 用户附属信息类
	 */
	@Resource
	private transient UserCacheService userCacheService;
	/**
	 * 用户认证信息类
	 */
	@Resource
	private transient UserIdentifyService identifyService;
	/**
	 * 用户基础类
	 */
	@Resource
	private transient UserService userService;
	/**
	 * 用户基本信息类
	 */
	@Resource
	private transient UserBaseInfoService baseInfoService;
	/**
	 * 密保问题处理类
	 */
    @Resource
    private transient UserSecurityAnswerService secAnswerService;
    /**
     * 风险类型处理类
     */
	@Resource
	private transient LevelConfigService levelService;
	/**
	 * 公司信息类
	 */
	@Resource
	private transient UserCompanyInfoService companyService;
	@Resource
	private ProjectService projectService;

	/**
	 * 基本信息
	 * @author xhf
	 * @date 2016年7月27日
	 * @return
	 */
	@RequestMapping(value = "/member/baseInfo/index")
	public String view(final Model model) {
		 model.addAttribute("areaJson", CacheUtils.getStr(CacheConstant.KEY_AREA_JSON));
		final User user = SessionUtils.getSessionUser();		
		final UserCache userCache = userCacheService.findByUserId(user.getUuid());
		
		String goPage="/member/baseInfo/companyIndex";
		if(UserCache.USER_NATURE_PERSON.equals(userCache.getUserNature())){
			goPage = "/member/baseInfo/userIndex";
		}else if(UserCache.USER_NATURE_VOUCH.equals(userCache.getUserNature())){
			goPage = "/member/baseInfo/vouchIndex";
		}
		return goPage;
	}
	/**
	 * 基本信息 -担保用户
	 * @author xhf
	 * @date 2016年7月27日
	 * @return
	 */
	@RequestMapping(value = "/member/baseInfo/vouchIndex")
	public String vouchIndex(){
		return "/member/baseInfo/vouchIndex";
	}
	
	/**
	 * 安全设置信息
	 * @author xhf
	 * @date 2016年7月27日
	 * @return
	 */
	@RequestMapping(value = "/member/baseInfo/getIdentifyInfo")
	@ResponseBody
	public Object getIdentifyInfo(){
		final User user = userService.get(SessionUtils.getSessionUser().getUuid());
		final Map<String, Object> data = Maps.newHashMap();
		data.put(RESULT_NAME, true);
		//头像路径
		final UserCache userCache = userCacheService.findByUserId(user.getUuid());
		data.put("avatar_photo", ConfigUtils.getValue(ConfigConstant.IMAGE_SERVER_URL)+userCache.getAvatarPhoto());
		//安全等级
		data.put("securityLevel", identifyService.getSecurityLevel(user));
		data.put("securityValue", identifyService.getSecurityValue(user));
		//上次登录时间
		data.put("lastLoginTime", loginLogService.getLastLoginTime(user.getUuid()));
		//托管账户
		data.put("tppUserAccId", user.getTppUserAccId());
		//手机号码
		data.put("mobile", user.getHideMobile());
		//邮箱
		data.put("email", user.getHideEmail());
		//风险等级
		if(!StringUtils.isBlank(userCache.getRiskLevel())){
			LevelConfig levelConfig = levelService.findListByUserRiskLevelVal(Integer.valueOf(userCache.getRiskLevel()));
			data.put("riskLevel", levelConfig.getUserRiskLevelName());
		}else{
			data.put("riskLevel", null);
		}

		//安全设置
		final UserIdentify userIdentify = identifyService.findByUserId(user.getUuid());
		SessionUtils.setSessionAttr(Constant.SESSION_USER_IDENTIFY, userIdentify);
		data.put("userIdentify", userIdentify);
		//是否设置了密保问题
		final List<UserSecurityAnswer> answerList = secAnswerService.findByUserId(user.getUuid());
		data.put("hasPwdQuest", answerList.isEmpty()? Constant.FLAG_NO:Constant.FLAG_YES);
		//图片文件地址
		data.put("image_server_url", ConfigUtils.getValue(ConfigConstant.IMAGE_SERVER_URL));
		//托管账户栏显示
		data.put("userNature", userCache.getUserNature());
		if(!UserCache.USER_NATURE_PERSON.equals(userCache.getUserNature())){
			UserCompanyInfo userCompanyInfo = companyService.findByUserId(user.getUuid());
			data.put("auditStatus", userCompanyInfo.getAuditStatus());
			data.put("auditMessage", userCompanyInfo.getAuditMessage());
		}
		
		return data;
	}
	
	/**
	 * 用户基本信息
	 * @author xhf
	 * @date 2016年7月27日
	 * @return
	 */
	@RequestMapping(value = "/member/baseInfo/getUserInfo")
	@ResponseBody
	public Object getUserInfo(){
		final String userId = SessionUtils.getSessionUser().getUuid();
		final User user = userService.get(userId);
		
		final Map<String, Object> data = Maps.newHashMap();
		data.put(RESULT_NAME, true);
		data.put(USERNAME,user.getUserName());
		final UserCache cache = userCacheService.findByUserId(userId);
		cache.setSignSealData(null);
		data.put("userCache", cache);
		data.put("userBaseInfo",baseInfoService.findByUserId(userId));
		data.put("educationList", DictUtils.list(DictTypeEnum.EDUCATION_LEVEL.getValue()));
		data.put("workExperienceList", DictUtils.list(DictTypeEnum.WORK_EXPERIENCE.getValue()));
		data.put("monthIncomeRangeList", DictUtils.list(DictTypeEnum.SALARY_RANGE.getValue()));
		return data;
	}
	
	/**
	 * 更新用户基本信息
	 * @author xhf
	 * @date 2016年7月27日
	 * @return
	 */
	@RequestMapping(value = "/member/baseInfo/modifyUserInfo")
	@ResponseBody
	public Object modifyUserInfo(@RequestParam(value = "zone[]") final String[] zone,final UserBaseInfo userBaseInfo,
			final String address,final String userName) {
		//附属信息
		final UserCache userCache = new UserCache();
		if(zone!=null && zone.length>0){
			userCache.setProvince(zone[0]);
			userCache.setCity(zone[1]);
			if(zone.length==3){ //类似北京和天津，只有二级
				userCache.setArea(zone[2]);
			}
		}
		userCache.setAddress(address);
		//更新数据库
		userCacheService.modifyUserInfo(userName, userCache, userBaseInfo);
		
		return this.renderSuccessResult();
	}
	
	/**
	 * 企业基本信息
	 * @author xhf
	 * @date 2016年7月27日
	 * @return
	 */
	@RequestMapping(value = "/member/baseInfo/getCompanyInfo")
	@ResponseBody
	public Object getCompanyInfo(){
		final String userId = SessionUtils.getSessionUser().getUuid();
		final User user = userService.get(userId);
		
		final Map<String, Object> data = Maps.newHashMap();
		data.put(RESULT_NAME, true);
		data.put(USERNAME,user.getUserName());
		data.put("userCache", userCacheService.findByUserId(userId));
		data.put("userCompanyInfo",companyService.findByUserId(userId));
		return data;
	}
	
	/**
	 * 更新企业基本信息
	 * @author xhf
	 * @date 2016年7月27日
	 * @return
	 */
	@RequestMapping(value = "/member/baseInfo/modifyCompanyInfo")
	@ResponseBody
	public Object modifyCompanyInfo(final HttpServletRequest request, final UserCompanyInfo userCompanyInfo, final String address,
			final String userName) {
		//企业成立时间
		if(!StringUtils.isBlank(userCompanyInfo.getEstablishDateStr())){
			userCompanyInfo.setEstablishDate(DateUtils.parseDate(userCompanyInfo.getEstablishDateStr()));
		}
		final String[] zone=request.getParameterValues("zone[]");
		//附属信息
		final UserCache userCache = new UserCache();
		if(zone !=null && zone.length>0){
			userCache.setProvince(zone[0]);
			userCache.setCity(zone[1]);
			if(zone.length==3){ //类似北京和天津，只有二级
				userCache.setArea(zone[2]);
			}
		}else{
			throw new BussinessException(ResourceUtils.get(ResourceConstant.COMPANY_ZONE_EMPTY));
		}
		final String[] officeZone=request.getParameterValues("officeZone[]");
		if(officeZone != null && officeZone.length>0){
			userCompanyInfo.setOfficeProvince(officeZone[0]);
			userCompanyInfo.setOfficeCity(officeZone[1]);
			if(officeZone.length==3){ //类似北京和天津，只有二级
				userCompanyInfo.setOfficeArea(officeZone[2]);
			}
		}else{
			throw new BussinessException(ResourceUtils.get(ResourceConstant.COMPANY_OFFICE_ZONE_EMPTY));
		}
		userCache.setAddress(address);
		//更新数据库
		userCacheService.modifyCompanyInfo(userName, userCache, userCompanyInfo);
		
		return this.renderSuccessResult();
	}
	
	/**
	 * 获取用户类型
	 * @author ZhangBiao
	 * @date 2016年10月18日
	 * @return
	 */
	@RequestMapping(value = "/member/baseInfo/getUserNature")
	@ResponseBody
	public Object getUserNature(){
		final Map<String, Object> data = Maps.newHashMap();
		UserCache userCache = (UserCache) SessionUtils.getSessionAttr(Constant.SESSION_USER_CACHE);
		// 获取借款数量
		final String userId = SessionUtils.getSessionUser().getUuid();
		int num = projectService.getProjectNumByUserId(userId);
		if(userCache == null){
			data.put(RESULT_NAME, false);
		}else{
			data.put(RESULT_NAME, true);
			data.put("userNature", userCache.getUserNature());
		}
		data.put("projectNum", num);
		return data;
	}
	
}
