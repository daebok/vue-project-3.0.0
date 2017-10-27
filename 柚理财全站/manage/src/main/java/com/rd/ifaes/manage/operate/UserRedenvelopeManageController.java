package com.rd.ifaes.manage.operate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.dict.DeleteFlagEnum;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.dict.OperateEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.rabbit.RabbitUtils;
import com.rd.ifaes.common.rabbit.model.MqActPlanModel;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.MqConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.operate.domain.ActivityPlan;
import com.rd.ifaes.core.operate.domain.RedenvelopeRule;
import com.rd.ifaes.core.operate.domain.UserRedenvelope;
import com.rd.ifaes.core.operate.model.BatchUserRedModel;
import com.rd.ifaes.core.operate.service.ActivityPlanService;
import com.rd.ifaes.core.operate.service.RedenvelopeRuleService;
import com.rd.ifaes.core.operate.service.UserRedenvelopeService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.model.UserModel;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.manage.sys.SystemController;

/**
 * 
 * 用户红包 Controller
 * 
 * @version 3.0
 * @author wyw
 * @date 2016-8-31
 */
@Controller
public class UserRedenvelopeManageController extends SystemController {
	/** 用户红包业务处理 */
	@Resource
	private transient UserRedenvelopeService userRedenvelopeService;
	/** 用户业务处理 */
	@Resource
	private transient UserService userService;
	/** 红包规则业务处理 */
	@Resource
	private transient RedenvelopeRuleService redenvelopeRuleService;
	@Resource
	private transient ActivityPlanService activityPlanService;
	
	//路径拼接符 "/"
	private static String separator = File.separator;
	
	/**
	 * 
	 * 用户红包页面
	 * 
	 * @author wyw
	 * @date 2016-8-28
	 * @return
	 */
	@RequestMapping(value = "/operate/redEnvelope/userRedenvelopeManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_RED)
	public String userRedenvelopeManage() {
		return "/operate/redEnvelope/userRedenvelopeManage";
	}

	/**
	 * 
	 * 用户红包列表数据接口
	 * 
	 * @author wyw
	 * @date 2016-8-28
	 * @param userRedenvelope
	 * @return
	 */
	@RequestMapping(value = "/operate/redEnvelope/userRedenvelopeList")
	@ResponseBody
	@RequiresPermissions("oper:redpacket:redpacketList:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_RED)
	public Object userRedenvelopeList(final UserRedenvelope userRedenvelope) {
		return userRedenvelopeService.findPage(userRedenvelope);
	}

	/**
	 * 
	 * 作废红包
	 * 
	 * @author ywt
	 * @date 2016-11-16
	 * @param RedenvelopeId
	 * @return
	 */
	@RequiresPermissions("oper:redpacket:redpacketList:cancel")
	@RequestMapping(value = "/operate/redEnvelope/userRedenvelopeStatus")
	@ResponseBody
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.USER_RED)
	public Object userRedenvelopeStatus(final String id) {
		userRedenvelopeService.cancellationRedenvelope(id);
		return renderSuccessResult();
	}

	/**
	 * 
	 * 发放用户红包页面
	 * 
	 * @author wyw
	 * @date 2016-8-28
	 * @return
	 */
	@RequestMapping(value = "/operate/redEnvelope/grantUserRed")
	@SystemLog(method=SysLogConstant.GRANT,content=SysLogConstant.USER_RED)
	public String grantUserRed(final Model model) {
		ActivityPlan actp=activityPlanService.findActivityPlanByCode(OperateEnum.ACTIVITYPLAN_SELECT_USERS.getValue());
		if (actp==null || actp.getStatus().equals(OperateEnum.STATUS_FORBIDDEN.getValue())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.ACTIVITY_GRANT_BAN));
		}
		// 加载自定义红包规则
		final RedenvelopeRule redRuleQuery = new RedenvelopeRule();
		redRuleQuery.setActivityCode(OperateEnum.ACTIVITYPLAN_SELECT_USERS.getValue());
		redRuleQuery.setDeleteFlag(DeleteFlagEnum.NO.getValue());
		redRuleQuery.setStatus(OperateEnum.STATUS_SERVICE.getValue());
		final List<RedenvelopeRule> redRuleList = redenvelopeRuleService.findListForGrant(redRuleQuery);
		 model.addAttribute("redRuleList", redRuleList);
		return "/operate/redEnvelope/grantUserRed";
	}

	/**
	 * 红包发放页面 已选择用户列表数据
	 * 
	 * @author wyw
	 * @date 2016-8-28
	 * @param userModel
	 * @return
	 */
	@RequestMapping(value = "/operate/redEnvelope/selectedGrantUser")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_LIST)
	public Object selectedGrantUser(final UserModel userModel, final String userIds) {
		Page<User> userPage = null;
		if(StringUtils.isBlank(userIds)){
			userPage = new Page<User>();
			List<User> userList = new ArrayList<User>(); 
			userPage.setRows(userList);
		}else{
			userModel.setUuids(userIds.split(","));
			userModel.setIsVouch(String.valueOf(Constant.INT_ZERO));
			userPage = userService.findUserForRed(userModel);
		}
		return userPage;
	}
	
	/**
	 * 红包发放页面 可选择用户列表数据
	 * 
	 * @author wyw
	 * @date 2016-8-28
	 * @param userModel
	 * @return
	 */
	@RequestMapping(value = "/operate/redEnvelope/selectableGrantUser")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_LIST)
	public Object selectableGrantUser(final UserModel userModel, final String userIds) {
		// 非担保机构
		userModel.setIsVouch(String.valueOf(Constant.INT_ZERO));
		if (StringUtils.isNotBlank(userIds)) {
			userModel.setUuids(userIds.split(","));
		}
		return userService.findUserForRed(userModel);
	}

	/**
	 * 红包发放操作
	 * 
	 * @author wyw
	 * @date 2016-8-28
	 * @param userIds
	 * @param redenvelopeRuleId
	 * @return
	 */
	@RequiresPermissions("oper:redpacket:redpacketList:grant")
	@RequestMapping(value = "/operate/redEnvelope/doRedenvelope")
	@ResponseBody
	@SystemLog(method=SysLogConstant.GRANT,content=SysLogConstant.USER_RED)
	@TokenValid(value = TokenConstants.TOKEN_GRANT_REDENVELOPE, dispatch = true)
	public Object doRedenvelope(final String[] userIds, final String redenvelopeRuleId) {
		if(userIds!=null && userIds.length > Constant.INT_TWO_HUNDRED){//发放用户数量太多，直接入队列
			//队列红包发放限制发放频率，防止队列消息大量堆积
			final String intervalKey = CacheConstant.KEY_GRANT_USER_RED;
			if (CacheUtils.incr(intervalKey, Constant.DOUBLE_ONE) > Constant.DOUBLE_ONE) {
				return renderResult(false, ResourceUtils.get(ResourceConstant.USER_RED_GRANT_TOO_OFTEN));
			}
			CacheUtils.expire(intervalKey, ExpireTime.TWO_MIN);
			//发放操作入队列
			BatchUserRedModel batchUserRed = new BatchUserRedModel(userIds, redenvelopeRuleId, Constant.INT_ONE);
			RabbitUtils.actPlan(new MqActPlanModel(MqConstant.OPERATE_ACTPLAN_BATCH_USER_RED_GRANT, batchUserRed));			
		}else{
			// 调用红包发放规则 发放红包
			redenvelopeRuleService.grantSelectUserRed(userIds, redenvelopeRuleId,Constant.INT_ONE);			
		}
		return renderSuccessResult();
	}
	
	
	/**
	 * 红包发放页面 导入excel
	 * 
	 * @author ywt
	 * @date 2016-12-13
	 * @param file excel
	 * @return
	 */
	@RequestMapping(value = "/operate/redEnvelope/userExcel")
	@ResponseBody
	@SystemLog(method=SysLogConstant.GRANT_RATE_IMPORT_DATA,content=SysLogConstant.USER_LIST)
	public String importUserExcel(@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request) {
		
		String path = request.getSession().getServletContext().getRealPath("/");
		path = path + separator + "data" + separator + "userRedEnvelope" + separator + DateUtils.dateStr7(new Date());
        String fileName = file.getOriginalFilename()+DateUtils.dateStr3(new Date());  
        File targetFile = new File(path, fileName);
        if(!targetFile.exists()){  
            targetFile.mkdirs();  
        }  
        try {
			file.transferTo(targetFile);
		} catch (IllegalStateException|IOException e) {
			LOGGER.error(e.getMessage(), e);
		} 
        return reBuildJson(userService.checkExcelUser(targetFile));
	}
	
}
