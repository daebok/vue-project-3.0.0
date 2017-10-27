package com.rd.ifaes.manage.loan;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.credit.domain.UserCredit;
import com.rd.ifaes.core.credit.domain.UserCreditLine;
import com.rd.ifaes.core.credit.service.UserCreditLineService;
import com.rd.ifaes.core.credit.service.UserCreditService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.manage.sys.SystemController;

@Controller
public class CreditManageController extends SystemController{
	@Resource
	private UserCreditService userCreditService ;
	@Resource
	private UserService userService ;
	@Resource
	private UserCreditLineService userCreditLineService ;
	
	
	/**
	 * 额度审核列表页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/loan/credit/creditRecord")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.VERIFY_LOG)
	public String record() {
		return "/loan/credit/creditRecord";
	}
	
	/**
	 * 额度审核列表记录
	 */
	@RequiresPermissions("project:borrow:credit:query")
	@RequestMapping(value = "/loan/credit/creditRecordList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.VERIFY_LOG)
	public Object recordList(UserCredit model) {
		return userCreditService.findPage(model) ;
	}
	
	/**
	 * 额度审核界面
	 * @param uuid
	 * @return
	 */
	@RequiresPermissions("project:borrow:credit:auditing")
	@RequestMapping(value = "/loan/credit/creditVerifyPage", method = RequestMethod.POST)
	@SystemLog(method=SysLogConstant.ESTABLISH,content=SysLogConstant.CREDIT_ESTABLISH)
	public String vefifyCredit(String uuid, final Model model) {
		LOGGER.info("uuid:{}", uuid);
		if (StringUtils.isBlank(uuid)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_NO_RECORD));
		}
		UserCredit userCredit = userCreditService.get(uuid) ;
		if(userCredit == null) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_NO_RECORD));
		}
		String userId = userCredit.getUserId() ;
		User user = userService.get(userId) ;
		userCredit.setUserName(user.getUserName());
		userCredit.setRealName(user.getRealName());
		userCredit.setMobile(user.getMobile());
		userCredit.setEmail(user.getEmail());
		model.addAttribute("userCredit", userCredit) ;
		return "/loan/credit/creditVerifyPage" ;
	}
	
	/**
	 * 成立审核
	 */
	@RequiresPermissions("project:borrow:credit:auditing")
	@RequestMapping(value = "/loan/credit/creditEstablishVerify", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.ESTABLISH,content=SysLogConstant.CREDIT_ESTABLISH)
	public Object establishVerify(UserCredit userCredit) {
		if(userCredit.getStatus().equals("3")) {
			userCredit.setStatus("2") ;
			userCreditService.update(userCredit);
			return renderResult(true, "操作成功!") ;
		}
		UserCredit model = userCreditService.get(userCredit.getUuid()) ;
		model.setRemark(userCredit.getRemark());
		LOGGER.info("deduct:{}", model.getDeduct());
		UserCreditLine userCreditLine = userCreditLineService.getByUserId(model.getUserId()) ;
		if(userCreditLine != null) {
			if(model.getDeduct() > userCreditLine.getUseCredit()) {
				userCredit.setStatus("4") ;
				userCreditService.update(userCredit);
				return renderResult(false, "用户可用额度不够扣除!") ;
			}
		}
		userCreditService.establishVerify(model);
		return renderResult(true, "审核通过!") ;
	}
	
	/**
	 * 用户额度界面
	 * @return
	 */
	@RequestMapping(value = "/loan/credit/creditList")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.CREDIT)
	public String list() {
		return "/loan/credit/creditList";
	}
	
	/**
	 * 用户额度列表
	 * @param model
	 * @return
	 */
	@RequiresPermissions("oper:credit:userCredit:query")
	@RequestMapping(value = "/loan/credit/doCreditList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.CREDIT)
	public Object doList(final UserCreditLine model) {
		return userCreditLineService.findPage(model) ;
	}
	
	/**
	 * 扣除额度界面
	 * @return
	 */
	@RequestMapping(value = "/loan/credit/deductCredit")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.CREDIT)
	public String deduct(String userId, final Model model) {
		LOGGER.info("用户id:{}", userId);
		model.addAttribute("userId", userId);
		return "/loan/credit/deductCredit";
	}
	
	/**
	 * 扣除额度
	 * @param userId
	 * @param deduct
	 * @return
	 */
	@RequestMapping(value = "/loan/credit/doDeductCredit", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.CREDIT)
	public Object doDeduct(String userId, String deduct, String content) {
		LOGGER.info("content:{}", content);
		UserCredit userCredit = new UserCredit() ;
		userCredit.setUserId(userId);
		userCredit.setCreateTime(DateUtils.getNow());
		userCredit.setDeduct(Integer.parseInt(deduct));
		userCredit.setContent(content);
		userCredit.setStatus("0");
		userCreditService.save(userCredit);
		return renderResult(true, "操作完成") ;
	}
}
