package com.rd.ifaes.web.controller.member;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.account.domain.AccountLog;
import com.rd.account.service.AccountLogService;
import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.DictUtils;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.web.controller.WebController;

/**
 * 
 *  我的账户-资金明细
 * @version 3.0
 * @author xhf
 * @date 2016年7月27日
 */
@Controller
public class FundController extends WebController {
	/**
	 * 账户资金日志类
	 */
	@Resource
	private transient AccountLogService accountLogService;
	/**
	 * 用户类
	 */
	@Resource
	private transient UserService userService;
	
	/**
	 * 资金明细列表页面--个人
	 * @author xhf
	 * @date 2016年7月27日
	 * @return
	 */
	@RequestMapping(value = "/member/fund/list")
	public String list(){
		UserCache userCache = (UserCache)SessionUtils.getSessionAttr(Constant.SESSION_USER_CACHE);
		if(userCache!=null && UserCache.USER_NATURE_VOUCH.equals(userCache.getUserNature())){//担保机构
			return "/member/fund/vouchList";
		}	
		return "/member/fund/list";
	}
	/**
	 * 资金明细列表页面--担保
	 * @author xhf
	 * @date 2016年7月27日
	 * @return
	 */
	@RequestMapping(value = "/member/fund/vouchList")
	public String vouchList(){
		return "/member/fund/vouchList";
	}
	
	/**
	 * 获得资金明细信息
	 * @author xhf
	 * @date 2016年7月27日
	 * @return
	 */
	@RequestMapping(value = "/member/fund/getLogList")
	@ResponseBody
	public Object getLogList(final AccountLog accountLog,final String dateType){
		final User user = SessionUtils.getSessionUser();
		accountLog.setUserId(user.getUuid());
		accountLog.setAccountCode(ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE));
		accountLog.setDateType(Constant.DATE_TYPE_ALL.equals(dateType)? "":dateType);
		accountLog.setDateTypeTime(getDateTypeTime(dateType));
		if(!StringUtils.isBlank(accountLog.getStartTime())){
			accountLog.setDateType("");
			accountLog.setStartTime(DateUtils.getDateStart(DateUtils.parseDate(accountLog.getStartTime())));
		}
		if(!StringUtils.isBlank(accountLog.getEndTime())){
			accountLog.setDateType("");
			accountLog.setEndTime(DateUtils.getDateEnd(DateUtils.parseDate(accountLog.getEndTime())));
		}
		final Page<AccountLog> page = accountLogService.findByDate(accountLog);
		final List<AccountLog> logList = page.getRows();
		for (final AccountLog log : logList) {
			log.setAccountTypeStr(getAccountTypeStr(log.getAccountType()));
			if(StringUtils.isNotBlank(log.getToUser())){
				if(ConfigUtils.getValue(Constant.ADMIN_ID).equals(log.getToUser())){
					log.setToUser(ConfigUtils.getValue(ConfigConstant.WEB_NAME));
				}else{
					User toUser = userService.get(log.getToUser());
					if(ObjectUtils.isEmpty(toUser)){
						log.setToUser(Constant.TO_USER_NULL);
					}else{
						log.setToUser(toUser.getHideUserName());
					}
				}
			}else{
				log.setToUser(Constant.TO_USER_NULL);
			}
			if(log.getRemark() != null && log.getRemark().contains("href")){
				log.setHtmlAUrl(StringUtils.getAHTMLUrl(log.getRemark()));
			}else{
				log.setHtmlAUrl(StringUtils.EMPTY);
			}
		}
		final Map<String, Object> data = this.renderSuccessResult();
		data.put("data", page);
		return data;
	}
	
	/**
	 * 
	 * 设置前台资金明细状态显示
	 * @author xhf
	 * @date 2016年9月30日
	 * @param accountType
	 * @return
	 */
	private String getAccountTypeStr(String accountType){
		String accountTypeStr = "";
		if(StringUtils.isNotBlank(accountType)){
			//充值处理中：充值申请、充值待审均显示充值处理中
			//提现成功：提现成功，提现回调成功
			if(Constant.CASH_AUDIT_SUCCESS.equals(accountType)||Constant.CASH_SUCCESS.equals(accountType)){
				accountType = Constant.CASH_SUCCESS;
				
			//提现失败：处理失败、银行对账失败均显示提现失败
			}else if(Constant.CASH_FAIL.equals(accountType)||Constant.CASH_BANK_FAIL.equals(accountType)){
				accountType = Constant.CASH_FAIL;
			
			//管理费：包括利息管理费、变现服务费、转让手续费、借款管理费、提现手续费，状态均显示为管理费	
			} else if(Constant.INTEREST_MANAGE_FEE.equals(accountType)||Constant.REALIZE_MANAGEFEE.equals(accountType)
					||Constant.BOND_BUY_FEE.equals(accountType)  ||Constant.BORROW_FEE.equals(accountType)
					||Constant.CASH_SERV_FEE.equals(accountType) ){
			    	accountType = Constant.MANAGE_FEE;
			}
			
			accountTypeStr = DictUtils.getItemName(DictTypeEnum.ACCOUNT_TYPE.getValue(), accountType);
		}
		return accountTypeStr;
	}

}
