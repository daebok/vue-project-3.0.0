package com.rd.ifaes.mobile.controller.member;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.account.domain.Account;
import com.rd.account.model.AccountQueryModel;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.dict.DeleteFlagEnum;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.core.account.service.AccountBankCardService;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.operate.domain.UserVip;
import com.rd.ifaes.core.operate.domain.UserVipLevel;
import com.rd.ifaes.core.operate.service.UserVipLevelService;
import com.rd.ifaes.core.operate.service.UserVipService;
import com.rd.ifaes.core.project.service.ProjectCollectionService;
import com.rd.ifaes.core.project.service.ProjectRepaymentService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.risk.domain.LevelConfig;
import com.rd.ifaes.core.risk.service.LevelConfigService;
import com.rd.ifaes.core.sys.service.LetterService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.domain.UserIdentify;
import com.rd.ifaes.core.user.service.UserAutoInvestService;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserEarnLogService;
import com.rd.ifaes.core.user.service.UserIdentifyService;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.mobile.controller.WebController;
import com.rd.ifaes.mobile.model.account.AccountBasicModel;
import com.rd.ifaes.mobile.model.account.AccountSecurityInfoModel;

/**
 * 
 *  我的账户-账户概览
 * @version 3.0
 * @author xhf
 * @date 2016年7月27日
 */
@Controller
public class AccountController extends WebController {
	
	/**
	 * 用户基础类
	 */
	@Resource
	private transient UserService userService;
	/**
	 * 账户信息处理类
	 */
	@Resource
	private transient AccountService accountService;
	/**
	 * 用户附属信息处理类
	 */
	@Resource
	private transient UserCacheService userCacheService;
	/**
	 * 用户认证信息处理类
	 */
	@Resource
	private transient UserIdentifyService identifyService;
	/**
	 * 风险类型处理类
	 */
	@Resource
	private transient LevelConfigService levelService;
	/**
	 * 邮件处理类
	 */
	@Resource
	private transient LetterService letterService;
	/**
	 * 还款操作类
	 */
	@Resource
	private transient ProjectRepaymentService projectRepaymentService;
	/**
	 * 项目处理类
	 */
	@Resource
	private transient ProjectService projectService;
	/**
	 * 代收处理类
	 */
	@Resource
	private transient ProjectCollectionService collectionService;
	/**
	 * 收益处理类
	 */
	@Resource
	private transient UserEarnLogService earnLogService;
	/**
	 * 用户VIP处理类
	 */
	@Resource
	private transient UserVipService userVipService;
	/**
	 * 自动投资处理类
	 */
	@Resource
	private transient UserAutoInvestService autoInvestService;
	/**
	 * vip等级服务
	 */
	@Resource
	private transient UserVipLevelService userVipLevelService;
	/**
	 * levelConfigService业务
	 */
	@Resource
	private transient LevelConfigService levelConfigService;
	
	@Resource
	private transient AccountBankCardService accountBankCardService;
	/**
	 * 
	 * 账户概览
	 * @author xhf
	 * @date 2016年7月27日
	 * @return
	 */
	@RequestMapping(value = "/member/account/main")
	public String main(){
		String goPage = "/member/account/main";
		final UserCache userCache = (UserCache)SessionUtils.getSessionAttr(Constant.SESSION_USER_CACHE);
		if(userCache!=null && UserCache.USER_NATURE_VOUCH.equals(userCache.getUserNature())){//担保用户
			goPage = "/member/account/vouchMain";
		}
		return goPage;
	}
	


/**
	 * 移动端获得账户概览信息
	 * @author xdj
	 * @date 2016年10月20日
	 * @return
	 */
	@RequestMapping(value = "/app/member/account/getAccountInfo")
	@ResponseBody
	public Object getUserInfo(HttpServletRequest request) {
		Object obj=null;
		AccountBasicModel model = new AccountBasicModel();
		try {
			User user = getAppSessionUser(request);
			// 获得资金详情
			Account account = accountService.getByUserId(new AccountQueryModel(user.getUuid(), ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE)));
			model.setRealName(user.getRealName());
			model.setHideMobilePhone(user.getHideMobile());
			model.setUseMoney(account.getUseMoney());
			model.setTotalMoney(account.getTotal());
			model.setNoUseMoney(account.getNoUseMoney());
			//未读信息
			model.setUnreadMessageCount(letterService.unreadCount(user.getUuid()));
			//收益概览
			//累计净收益，只算到昨天
			model.setEarnAmount(earnLogService.allEarnAmount(user.getUuid()));
			obj=createSuccessAppResponse(model);
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
	}

	
	
	/**
	 * 移动端 账户与设置首页
	 * @author lgx
	 * @date 2016年11月16日
	 * @return
	 */
	@RequestMapping(value = "/app/member/account/basicInfo")
	@ResponseBody
	public Object basicInfo(HttpServletRequest request) {
		Object obj=null;
		AccountSecurityInfoModel model = new AccountSecurityInfoModel();
		try {
			User user = getAppSessionUser(request);
			
			final User users = userService.get(user.getUuid());//更新user
			final UserCache userCache = userCacheService.findByUserId(user.getUuid());
			final UserIdentify userIdentify = identifyService.findByUserId(user.getUuid());
			
			//更新缓存
			SessionUtils.setSessionAttr(Constant.SESSION_USER, users);
			SessionUtils.setSessionAttr(Constant.SESSION_USER_IDENTIFY, userIdentify);
			model.setAuthorizated(userIdentify.getAuthSignStatus()==null?"0":userIdentify.getAuthSignStatus());/** 业务授权状态（0.未授权 1.已授权，默认0） */
			if(userCache.getAvatarPhoto()==null||"".equals(userCache.getAvatarPhoto())){
				model.setAvatarPhoto("");
			}else{
				model.setAvatarPhoto(ConfigUtils.getValue(ConfigConstant.IMAGE_SERVER_URL)+userCache.getAvatarPhoto());
			}
			model.setRealnameStatus(userIdentify.getRealNameStatus());//实名认证 0:未认证 1：实名认证通过 -1：实名认证未通过
			model.setTppUserAccId(users.getTppUserCustId()==null?"":users.getTppUserCustId());//用户资金存管平台账户号
			model.setRealname(users.getRealName()==null?"":users.getRealName());//真实姓名
			if(!"1".equals(userIdentify.getRealNameStatus())||users.getRealName()==null||"".equals(users.getRealName())){
			/*model.setHideUserName(user.getUserName().charAt(0) + "******"
					+ user.getUserName().charAt(user.getUserName().length() - 1));*/
				model.setHideUserName(users.getHideMobile());
			}else{
				model.setHideUserName(getHideRealName(users.getRealName()));
			}
			model.setUsername(users.getUserName());
			//VIP等级
			final UserVip userVip = userVipService.getUserVip(users.getUuid());
			String vipGift = UserVip.GIFT_CAN_NOT_RECEIVE;
			if(userVip!=null){
				model.setVipLevel(userVip.getVipLevel());
				//VIP礼包
			/*	if(UserVip.GIFT_STATUS_NO_RECEIVE.equalsIgnoreCase(userVip.getBirthdayGiftStatus()) || 
						UserVip.GIFT_STATUS_NO_RECEIVE.equalsIgnoreCase(userVip.getExclusiveGiftStatus())){
					vipGift = UserVip.GIFT_CAN_RECEIVE;
				}*/
				// VIP礼包
				if (userVipService.checkShowVipGift(user)) {
					vipGift = UserVip.GIFT_CAN_RECEIVE;
				}
				
			}else{
				//没有VIP等级
				model.setVipLevel(BigDecimal.ZERO.toString());
			}
			model.setEmailStatus(userIdentify.getEmailStatus());//0:未认证1：邮箱认证通过
			model.setEmail(users.getHideEmail()==null?"":users.getHideEmail());//邮箱
			model.setRiskLevel(userCache.getRiskLevel()==null?"":userCache.getRiskLevel());//风险评估等级(数字等级)
			model.setVipGift(vipGift);
			if(userCache.getRiskLevel()!=null){
				 LevelConfig levelConfig = levelConfigService.findListByUserRiskLevelVal(NumberUtils.toInt(userCache.getRiskLevel()));
				model.setRiskLevelStr(levelConfig.getUserRiskLevelName());//数字等级名称
			}else{
				model.setRiskLevelStr("去评测");
			}
			
			model.setHasSetPayPwd(users.getPayPwd()==null?0:1);//是否设置交易密码
			model.setMobilePhoneStatus(userIdentify.getMobilePhoneStatus()==null?"":userIdentify.getMobilePhoneStatus());//手机认证 -1:未通过,0:未认证,1:已认证
			if(!"1".equals(userIdentify.getRealNameStatus())){
				model.setBankNum(0);//银行卡张数
			}else{
				model.setBankNum(accountBankCardService.findList(user.getUuid()).size());//银行卡张数
			}
			model.setMobile(users.getHideMobile()==null?"":users.getHideMobile());//手机号
			obj=createSuccessAppResponse(model);
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
	}
	
	public String getHideRealName(String userName) {
		if(userName.length()<3){
			return "*"+userName.substring(userName.length()-1, userName.length());
		}else{
		return userName.substring(0, 1)+"*"+userName.substring(userName.length()-1, userName.length());
		}
}
	/**
	 * VIP 等级说明接口--移动端
	 * @author lgx
	 * @date 2017年1月13日
	 * @return
	 */
	@RequestMapping(value = "/app/member/account/vip")
	@ResponseBody
	public Object accountVIP(HttpServletRequest request){
		Map<String, Object> data = new HashMap<String, Object>();
		Object obj=null;
		try {
			User user = getAppSessionUser(request);
		//当前用户VIP等级信息
		UserVip userVip = userVipService.getUserVip(user.getUuid());
		if(userVip == null){
			userVip = new UserVip(Constant.FLAG_NO,BigDecimal.ZERO);
		}
		//用户附属信息
		@SuppressWarnings("unused")
		final UserCache userCache = userCacheService.findByUserId(user.getUuid());
		//头像路径
		//data.put("avatar_photo", StringUtils.isNull(userCache.getAvatarPhoto()));
		//VIP等级相关信息
		UserVipLevel userVipLevel = new UserVipLevel();
		userVipLevel.setDeleteFlag(DeleteFlagEnum.NO.getValue());
		List<UserVipLevel> vipLevelList = userVipLevelService.findList(userVipLevel);
		
		data.put("userVipLevel", userVip.getVipLevel()==null?0:userVip.getVipLevel());//用户vip等级
		data.put("growthValue", userVip.getGrowthValue());//当前成长值
		data.put("vipLevelList", vipLevelList);//等级说明集合
		data.put("telephone", user.getHideMobile());//马沙克手机号
		obj=createSuccessAppResponse(data);
	} catch (Exception e) {
		obj=dealException(e);
	}
	return obj;
}
	
	/**
	 * VIP 等级说明接口--app端调用
	 * @author lgx
	 * @date 2017年1月13日
	 * @return
	 */
	@RequestMapping(value = "/app/member/account/appvip")
	public Object accountAppVIP(HttpServletRequest request,final Model model){
		try {
			User user = getAppSessionUser(request);
		//当前用户VIP等级信息
		UserVip userVip = userVipService.getUserVip(user.getUuid());
		if(userVip == null){
			userVip = new UserVip(Constant.FLAG_NO,BigDecimal.ZERO);
		}
		//用户附属信息
		@SuppressWarnings("unused")
		final UserCache userCache = userCacheService.findByUserId(user.getUuid());
		//VIP等级相关信息
		UserVipLevel userVipLevel = new UserVipLevel();
		userVipLevel.setDeleteFlag(DeleteFlagEnum.NO.getValue());
		List<UserVipLevel> vipLevelList = userVipLevelService.findList(userVipLevel);
		model.addAttribute("userVipLevel", userVip.getVipLevel()==null?0:userVip.getVipLevel());//用户vip等级
		model.addAttribute("growthValue", userVip.getGrowthValue());//当前成长值
		model.addAttribute("vipLevelList", vipLevelList);//等级说明集合
		model.addAttribute("mobileUrl", ConfigUtils.getValue("mobile_url"));//等级说明集合
		return "/app/vip/vipLevel";
		} catch (Exception e) {
			model.addAttribute("r_msg", e.getMessage());
			return ConfigUtils.getTppName()+"/retresult";
		}
}
}
