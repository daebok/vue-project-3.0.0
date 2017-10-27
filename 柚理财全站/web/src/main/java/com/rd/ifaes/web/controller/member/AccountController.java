package com.rd.ifaes.web.controller.member;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.rd.account.model.AccountQueryModel;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.dict.DeleteFlagEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.dict.ProjectCollectionEnum;
import com.rd.ifaes.common.dict.RepaymentEnum;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.operate.domain.UserVip;
import com.rd.ifaes.core.operate.domain.UserVipLevel;
import com.rd.ifaes.core.operate.service.UserVipLevelService;
import com.rd.ifaes.core.operate.service.UserVipService;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectRepayment;
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
import com.rd.ifaes.web.controller.WebController;

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
	 *  用户账户信息（可用余额、冻结金额、总额)
	 * @author  FangJun
	 * @date 2016年10月19日
	 * @return
	 */
	@RequestMapping(value = "/member/account/userAccount")
	@ResponseBody
	public Object userAccount(){
		User user = SessionUtils.getSessionUser();
		final Map<String,Object> result=this.renderSuccessResult();
		result.put("account", accountService.getByUserId(new AccountQueryModel(user.getUuid(),ConfigUtils.getValue(Constant.ACCOUNT_CODE))));
		return result;
	}
	/**
	 * 获得账户概览信息
	 * @author xhf
	 * @date 2016年7月27日
	 * @return
	 */
	@RequestMapping(value = "/member/account/getAccountInfo")
	@ResponseBody
	public Object getUserInfo(){
		//用户信息
		final User user = userService.get(SessionUtils.getSessionUser().getUuid());
		SessionUtils.setSessionAttr(Constant.SESSION_USER, user);
		//用户附属信息
		final UserCache userCache = userCacheService.findByUserId(user.getUuid());
		//认证信息
		final UserIdentify userIdentify = identifyService.findByUserId(user.getUuid());
		SessionUtils.setSessionAttr(Constant.SESSION_USER_IDENTIFY, userIdentify);
		
		//返回参数
		final Map<String, Object> data = Maps.newHashMap();
		data.put("result", true);
		//VIP等级
		final UserVip userVip = userVipService.getUserVip(user.getUuid());
		String vipGift = UserVip.GIFT_CAN_NOT_RECEIVE;
		if (userVip != null) {
			data.put("VIPLevel", userVip.getVipLevel());
			// VIP礼包
			if (userVipService.checkShowVipGift(user)) {
				vipGift = UserVip.GIFT_CAN_RECEIVE;
			}
		} else {
			data.put("VIPLevel", BigDecimal.ZERO);
		}
		data.put("vipGift",vipGift);
		//风险等级
		if(!StringUtils.isBlank(userCache.getRiskLevel())){
			final LevelConfig levelConfig = levelService.findListByUserRiskLevelVal(Integer.valueOf(userCache.getRiskLevel()));
			data.put("riskLevel", levelConfig.getUserRiskLevelName());
		}else{
			data.put("riskLevel", null);
		}
		//安全等级
		data.put("securityLevel", identifyService.getSecurityLevel(user));
		//未读信息
		data.put("unreadCount", letterService.unreadCount(user.getUuid()));
		//是否自动投资
		data.put("autoTender", autoInvestService.getAutoStatusByUserId(user.getUuid()));
		//头像路径
		data.put("avatar_photo", StringUtils.isNull(userCache.getAvatarPhoto()));
		//用户信息
		data.put(Constant.SESSION_USER, user);
		//用户认证信息
		data.put(Constant.SESSION_USER_IDENTIFY, userIdentify);
		//资金概览
		data.put("account", accountService.getByUserId(new AccountQueryModel(user.getUuid(),ConfigUtils.getValue(Constant.ACCOUNT_CODE))));
		
		//收益概览
		//昨日净收益
		data.put("yesterdayEarnAmount", earnLogService.yesterdayEarnAmount(user.getUuid()));
		//累计净收益，只算到昨天
		data.put("earnAmount", earnLogService.allEarnAmount(user.getUuid()));
		//回款列表
		data.put("collectMonth", collectionService.getYearCollectMonths());
		data.put("collectData", collectionService.getYearCollectDataByUserId(user.getUuid(),ProjectCollectionEnum.STATUS_NOT_REPAY.getValue()));
		data.put("repaidData", collectionService.getYearCollectDataByUserId(user.getUuid(),ProjectCollectionEnum.STATUS_PAID.getValue()));
		//我的借款
        //借款总额		
		data.put("totalBorrowAmount", projectService.getAccountTotalByUserId(user.getUuid()));
		//待还总额
		data.put("totalRepayAmount", projectRepaymentService.getWaitRepayAccountTotal(user.getUuid()));
		//下一笔待还
		final ProjectRepayment repayment = projectRepaymentService.getNextRepayByUserId(user.getUuid());
		data.put("nextRepayDate", repayment==null? BigDecimal.ZERO:repayment.getRepayTime());
		data.put("nextRepayAmount",repayment==null? BigDecimal.ZERO:repayment.getPayment());
		return data;
	}
	
	/**
	 * 企业用户的我的借款
	 * @param project
	 * @return
	 */
	@RequestMapping(value = "/member/account/getProjectList")
	@ResponseBody
	public Object getProjectList(final Project model){
		User user = SessionUtils.getSessionUser();
		model.setUserId(user.getUuid());
		Map<String,Object> data = new HashMap<>();
		data.put("result", true);
		data.put("data", projectService.findByDate(model));
		return data;
	}
	
	
	/**
	 * 企业用户的我的待还借款
	 * @param project
	 * @return
	 */
	@RequestMapping(value = "/member/account/getProjectRepayList")
	@ResponseBody
	public Object getProjectRepayList(ProjectRepayment model){
		User user = SessionUtils.getSessionUser();
		model.setUserId(user.getUuid());
		model.setRepayTypeAdvance(LoanEnum.REPAY_TYPE_ADVANCE.getValue());
		model.setStatus(RepaymentEnum.STATUS_NOT_REPAY.getValue());
		model.getPage().setSort("repay_time");
		model.getPage().setOrder(Constant.ASC);
		Map<String,Object> data = new HashMap<>();
		data.put("result", true);
		data.put("data", projectRepaymentService.findByDate(model));
		return data;
	}
	
	/**
	 * 领取VIP礼包
	 * @author fxl
	 * @date 2016年10月8日
	 * @return
	 */
	@RequestMapping(value = "/member/account/doVipGift")
	@ResponseBody
	public Object doVipGift(){
		User user = SessionUtils.getSessionUser();
		final Map<String,Object> data = userVipService.grantVipGift(user);
		data.put(RESULT_NAME, true);
		return data;
		
	}
	
	/**
	 * 领取VIP礼包
	 * @author xxb
	 * @date 2016年10月10日
	 * @return
	 */
	@RequestMapping(value = "/member/account/vip")
	public String accountVIP(Model model){
		
		//当前用户VIP等级信息
		User user = SessionUtils.getSessionUser();
		UserVip userVip = userVipService.getUserVip(user.getUuid());
		if(userVip == null){
			userVip = new UserVip(Constant.FLAG_NO,BigDecimal.ZERO);
		}
		//用户附属信息
		final UserCache userCache = userCacheService.findByUserId(user.getUuid());
		//头像路径
		model.addAttribute("avatar_photo", StringUtils.isNull(userCache.getAvatarPhoto()));
		//VIP等级相关信息
		UserVipLevel userVipLevel = new UserVipLevel();
		userVipLevel.setDeleteFlag(DeleteFlagEnum.NO.getValue());
		List<UserVipLevel> vipLevelList = userVipLevelService.findList(userVipLevel);
		
		model.addAttribute("currentUserVip", userVip);
		model.addAttribute("vipLevelList", vipLevelList);
		model.addAttribute("telephone", user.getHideMobile());
		return "member/account/vip";
	}
	
}
