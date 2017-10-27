package com.rd.ifaes.manage.operate;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.dict.DeleteFlagEnum;
import com.rd.ifaes.common.dict.OperateEnum;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.validator.BeanValidators;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.operate.domain.RateCouponRule;
import com.rd.ifaes.core.operate.domain.RedenvelopeRule;
import com.rd.ifaes.core.operate.domain.UserGift;
import com.rd.ifaes.core.operate.domain.UserVip;
import com.rd.ifaes.core.operate.domain.UserVipGrowthLog;
import com.rd.ifaes.core.operate.domain.UserVipLevel;
import com.rd.ifaes.core.operate.model.UserVipLevelModel;
import com.rd.ifaes.core.operate.service.RateCouponRuleService;
import com.rd.ifaes.core.operate.service.RedenvelopeRuleService;
import com.rd.ifaes.core.operate.service.UserGiftService;
import com.rd.ifaes.core.operate.service.UserVipGrowthLogService;
import com.rd.ifaes.core.operate.service.UserVipLevelService;
import com.rd.ifaes.core.operate.service.UserVipService;
import com.rd.ifaes.manage.sys.SystemController;

/**
 * 
 * VIP管理
 * 
 * @version 3.0
 * @author wyw
 * @date 2016-8-1
 */
@Controller
public class VipManageController extends SystemController {
	/** 红包规则业务处理*/
	@Resource
	private transient RedenvelopeRuleService redenvelopeRuleService;
	/** 加息券规则业务处理*/
	@Resource
	private transient  RateCouponRuleService rateCouponRuleService;
	/**用户Vip等级业务处理*/
	@Resource
	private transient UserVipLevelService userVipLevelService;
	/**用户Vip礼包业务处理*/
	@Resource
	private transient UserGiftService userGiftService;
	/**用户Vip业务处理*/
	@Resource
	private transient UserVipService userVipService;
	/**用户Vip成长日志业务处理*/
	@Resource
	private transient UserVipGrowthLogService userVipGrowthLogService;
	/**
	 * vip等级设置管理页面
	 * @author wyw
	 * @date 2016-8-1
	 * @return
	 */
	@RequestMapping(value = "/operate/vip/vipLevelManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.VIP_LEVEL)
	public String vipLevelManage() {
		return "/operate/vip/vipLevelManage";
	}
	/**
	 * vip等级设置数据接口
	 * @author wyw
	 * @date 2016-8-1
	 * @param activityPlan
	 * @return
	 */
	@RequestMapping(value = "/operate/vip/vipLevelList")
	@ResponseBody
	@RequiresPermissions("oper:vip:vipMan:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.VIP_LEVEL)
	public Object vipLevelList(final UserVipLevel userVipLevel) {
		userVipLevel.setDeleteFlag(DeleteFlagEnum.NO.getValue());
		return userVipLevelService.findList(userVipLevel);
	}

	/**
	 * 
	 * vip等级设置添加页面
	 * 
	 * @author wyw
	 * @date 2016-8-1
	 * @return
	 */
	@RequestMapping(value = "/operate/vip/vipLevelAddPage")
	@RequiresPermissions("oper:vip:vipMan:add")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.VIP_LEVEL)
	public String vipLevelAddPage(final Model model) {
		initVipLevelData(model);
		return "/operate/vip/vipLevelAddPage";
	}

	/**
	 * vip等级设置添加操作
	 * @author wyw
	 * @date 2016-8-1
	 * @param model
	 * @param request
	 * @return
	 */
	@RequiresPermissions("oper:vip:vipMan:add")
	@RequestMapping(value = "/operate/vip/vipLevelAdd", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.VIP_LEVEL)
	public Map<String, Object> vipLevelAdd(final UserVipLevelModel model) {
		userVipLevelService.saveVipLevel(model);
		return renderSuccessResult();
	}

	/**
	 * vip等级设置修改页面
	 * @author wyw
	 * @date 2016-8-1
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/operate/vip/vipLevelEditPage")
	@RequiresPermissions("oper:vip:vipMan:edit")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.VIP_LEVEL)
	public String vipLevelEditPage(final String id, final Model model) {
		if (StringUtils.isNotBlank(id)) {
			initVipLevelData(model);
			final UserVipLevel vipLevel = userVipLevelService.get(id);
			 model.addAttribute("vipLevel", vipLevel);
		}
		return "/operate/vip/vipLevelEditPage";
	}

	/**
	 * vip等级修改操作
	 * @author wyw
	 * @date 2016-8-1
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/operate/vip/vipLevelEdit", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("oper:vip:vipMan:edit")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.VIP_LEVEL)
	public Map<String, Object> vipLevelEdit(final UserVipLevel model) {
		userVipLevelService.updateVipLevel(model);
		return renderSuccessResult();
	}
	/**
	 * 
	 * 删除vip等级设置
	 * @author wyw
	 * @date 2016-8-1
	 * @param ids
	 * @return
	 */
	@RequiresPermissions("oper:vip:vipMan:del")
	@RequestMapping(value = "/operate/vip/vipLevelDel", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.DEL,content=SysLogConstant.VIP_LEVEL)
	public Map<String, Object> vipLevelDel(final String[] ids){
		if(ids ==null || ids.length==0){
			return renderResult( false, "请勾选需要删除的记录");
		}
		userVipLevelService.checkVipUser();
	    userVipLevelService.deleteBatch(ids);
		return renderSuccessResult();
	}
	
	/**
	 * 
	 * 用户礼包设置页面
	 * @author wyw
	 * @date 2016-8-2
	 * @return
	 */
	@RequestMapping(value = "/operate/vip/userGiftManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_GIFT)
	public String userGiftManage() {
		return "/operate/vip/userGiftManage";
	}
	/**
	 * 
	 * 用户礼包设置 数据接口
	 * @author wyw
	 * @date 2016-8-2
	 * @param userVipLevel
	 * @return
	 */
	@RequestMapping(value = "/operate/vip/userGiftList")
	@ResponseBody
	@RequiresPermissions("oper:vip:vipPack:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_GIFT)
	public Object userGiftList(final UserGift userGift) {
		userGift.setDeleteFlag(DeleteFlagEnum.NO.getValue());
		return userGiftService.findManageList(userGift);
	}
	/**
	 * 
	 * 用户礼包设置添加页面
	 * @author wyw
	 * @date 2016-8-2
	 * @return
	 */
	@RequiresPermissions("oper:vip:vipPack:add")
	@RequestMapping(value = "/operate/vip/userGiftAddPage")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.USER_GIFT)
	public String userGiftAddPage(final Model model) {
		initUserGiftData(model);
		return "/operate/vip/userGiftAddPage";
	}
	/**
	 * 
	 * 用户礼包 添加操作
	 * @author wyw
	 * @date 2016-8-2
	 * @param model
	 * @param request
	 * @return
	 */
	@RequiresPermissions("oper:vip:vipPack:add")
	@RequestMapping(value = "/operate/vip/userGiftAdd", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.USER_GIFT)
	public Map<String, Object> userGiftAdd(final UserGift model) {
		userGiftService.addUserGift(model);
		return renderSuccessResult();
	}
	/**
	 * 
	 * 用户礼包设置编辑页面
	 * @author wyw
	 * @date 2016-8-2
	 * @param id
	 * @param model
	 * @return
	 */
	@RequiresPermissions("oper:vip:vipPack:edit")
	@RequestMapping(value = "/operate/vip/userGiftEditPage")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.USER_GIFT)
	public String userGiftEditPage(final String id, final Model model) {
		if (StringUtils.isNotBlank(id)) {
			initUserGiftData(model);
			final UserGift userGift = userGiftService.get(id);
			 model.addAttribute("userGift", userGift);
		}
		return "/operate/vip/userGiftEditPage";
	}
	/**
	 * 
	 * vip礼包修改操作
	 * @author wyw
	 * @date 2016-8-2
	 * @param model
	 * @param request
	 * @return
	 */
	@RequiresPermissions("oper:vip:vipPack:edit")
	@RequestMapping(value = "/operate/vip/userGiftEdit", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.USER_GIFT)
	public Map<String, Object> userGiftEdit(final UserGift model,final  HttpServletRequest request) {
		BeanValidators.validate(model);
		model.validation();
		userGiftService.update(model);
		return renderSuccessResult();
	}
	/**
	 * 
	 * 礼包删除
	 * @author wyw
	 * @date 2016-8-28
	 * @param ids
	 * @return
	 */
	@RequiresPermissions("oper:vip:vipPack:del")
	@RequestMapping(value = "/operate/vip/userGiftDel", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.DEL,content=SysLogConstant.USER_GIFT)
	public Map<String, Object> userGiftDel(final String[] ids){
		if(ids ==null || ids.length==0){
			return renderResult( false, "请勾选需要删除的记录");
		}
		userGiftService.deleteBatch(ids);
		return renderSuccessResult();
	}
	/**
	 * 
	 * 加载规则下拉 与礼包下拉
	 * 
	 * @author wyw
	 * @date 2016-8-1
	 */
	private void initVipLevelData(final Model model) {
		final RedenvelopeRule redRuleQuery = new RedenvelopeRule();
		redRuleQuery.setActivityCode(OperateEnum.ACTIVITYPLAN_VIP_LEVEL.getValue());
		redRuleQuery.setDeleteFlag(DeleteFlagEnum.NO.getValue());
		redRuleQuery.setStatus(OperateEnum.STATUS_SERVICE.getValue());
		// VIP等级设置的红包规则
		final List<RedenvelopeRule> redRuleList = redenvelopeRuleService.findList(redRuleQuery);
		// vip等级设置的加息规则
		final RateCouponRule queryRate = new RateCouponRule();
		queryRate.setActivityCode(OperateEnum.ACTIVITYPLAN_VIP_LEVEL.getValue());
		queryRate.setDeleteFlag(DeleteFlagEnum.NO.getValue());
		queryRate.setStatus(OperateEnum.STATUS_SERVICE.getValue());
		final List<RateCouponRule> rateRuleList = rateCouponRuleService.findList(queryRate);
		// 生日礼包 
		final UserGift queryGift =  new UserGift(); 
		queryGift.setDeleteFlag(DeleteFlagEnum.NO.getValue());
		queryGift.setGiftType(OperateEnum.GIFT_TYPE_BIRTHDAY.getValue());
		 model.addAttribute("birthdayGiftList", userGiftService.findList(queryGift));
		// 专享礼包 
		queryGift.setDeleteFlag(DeleteFlagEnum.NO.getValue());
		queryGift.setGiftType(OperateEnum.GIFT_TYPE_EXCLUSIVE.getValue());
		 model.addAttribute("exclusiveGiftList", userGiftService.findList(queryGift));
		 model.addAttribute("redRuleList", redRuleList);
		 model.addAttribute("rateRuleList", rateRuleList);
		UserVipLevel level = new UserVipLevel();
		level.setDeleteFlag("0");
		int count = userVipLevelService.getCount(level);
		 model.addAttribute("count", count);
	}
	/**
	 * 
	 * 礼包设置页面 规则数据加载
	 * @author wyw
	 * @date 2016-8-3
	 */
	private void initUserGiftData(final Model model) {
		final RedenvelopeRule redRuleQuery = new RedenvelopeRule();
		redRuleQuery.setActivityCode(OperateEnum.ACTIVITYPLAN_VIP_LEVEL.getValue());
		redRuleQuery.setDeleteFlag(DeleteFlagEnum.NO.getValue());
		redRuleQuery.setStatus(OperateEnum.STATUS_SERVICE.getValue());
		// VIP等级设置的红包规则
		final List<RedenvelopeRule> redRuleList = redenvelopeRuleService.findList(redRuleQuery);
		// vip等级设置的加息规则
		final RateCouponRule queryRate = new RateCouponRule();
		queryRate.setActivityCode(OperateEnum.ACTIVITYPLAN_VIP_LEVEL.getValue());
		queryRate.setDeleteFlag(DeleteFlagEnum.NO.getValue());
		queryRate.setStatus(OperateEnum.STATUS_SERVICE.getValue());
		final List<RateCouponRule> rateRuleList = rateCouponRuleService.findList(queryRate);
		 model.addAttribute("redRuleList", redRuleList);
		 model.addAttribute("rateRuleList", rateRuleList);
	}
	/**
	 * 
	 * 用户vip管理页面
	 * @author wyw
	 * @date 2016-8-3
	 * @return
	 */
	@RequestMapping(value = "/operate/vip/userVipManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_VIP)
	public String userVipManage(final Model model) {
		List<String> vipList = userVipLevelService.getLevelByStatus(Constant.FLAG_NO);
		model.addAttribute("vipList", vipList);
		return "/operate/vip/userVipManage";
	}
	/**
	 * 
	 * 用户vip数据接口
	 * @author wyw
	 * @date 2016-8-3
	 * @param userVip
	 * @return
	 */
	@RequestMapping(value = "/operate/vip/userVipList")
	@ResponseBody
	@RequiresPermissions("oper:vip:vipUser:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_VIP)
	public Object userVipList(final UserVip userVip) {
		return userVipService.findPage(userVip);
	}
	/**
	 * 
	 * 用户vip成长日志管理页面
	 * @author wyw
	 * @date 2016-8-3
	 * @return
	 */
	@RequestMapping(value = "/operate/vip/vipGrowthLogManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_VIP)
	public String vipGrowthLogManage(String userId,final Model model) {
		 model.addAttribute("userId", userId);
		return "/operate/vip/vipGrowthLogManage";
	}
	
	
	/**
	 * 
	 * 用户vip成长日志管理页面(弹窗用)
	 * @author wyw
	 * @date 2016-8-3
	 * @return
	 */
	@RequestMapping(value = "/operate/vip/vipGrowthLogPage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_VIP)
	public String vipGrowthLogPage(String userId,final Model model) {
		 model.addAttribute("userId", userId);
		return "/operate/vip/vipGrowthLogPage";
	}
	
	
	/**
	 * 
	 * 用户vip成长日志数据
	 * @author wyw
	 * @date 2016-8-3
	 * @param userVipGrowthLog
	 * @return
	 */
	@RequestMapping(value = "/operate/vip/vipGrowthLogList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_VIP)
	public Object vipGrowthLogList(final UserVipGrowthLog userVipGrowthLog) {
		return userVipGrowthLogService.findPage(userVipGrowthLog);
	}
}
