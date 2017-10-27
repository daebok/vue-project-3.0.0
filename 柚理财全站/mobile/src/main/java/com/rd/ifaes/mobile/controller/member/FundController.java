package com.rd.ifaes.mobile.controller.member;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.account.domain.AccountLog;
import com.rd.account.service.AccountLogService;
import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.DictUtils;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.mobile.controller.WebController;
import com.rd.ifaes.mobile.model.PagedData;
import com.rd.ifaes.mobile.model.account.log.AccountLogItemModel;

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
	@Resource
	private ProjectInvestService projectInvestService;
	@Resource
	private ProjectService projectService;
	
	/**
	 * 资金明细列表页面--个人
	 * @author xhf
	 * @date 2016年7月27日
	 * @return
	 */
	@RequestMapping(value = "/member/fund/list")
	public String list(){
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
	 * @author xiaodingjiang
	 * @date 2016年11月4日
	 * @return
	 */
	@RequestMapping(value = "/app/member/fund/getLogList")
	@ResponseBody
	public Object getLogList(final AccountLog accountLog,String dateType,HttpServletRequest request){
		Object obj=null;
		try{
	    User user=getAppSessionUser(request);
		accountLog.setUserId(user.getUuid());
		accountLog.setAccountCode(ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE));
		accountLog.setDateType(Constant.DATE_TYPE_ALL.equals(dateType)? "":dateType);
		accountLog.setDateTypeTime(getDateTypeTime(dateType));
		if(!StringUtils.isBlank(accountLog.getStartTime())){
			accountLog.setStartTime(DateUtils.getDateStart(DateUtils.parseDate(accountLog.getStartTime())));
		}
		if(!StringUtils.isBlank(accountLog.getEndTime())){
			accountLog.setEndTime(DateUtils.getDateEnd(DateUtils.parseDate(accountLog.getEndTime())));
		}
		Page<AccountLog>pages = accountLogService.findByDate(accountLog);
		List<AccountLog> logList = pages.getRows();
		PagedData<AccountLogItemModel> pirlist=new PagedData<AccountLogItemModel>();
		if(logList!=null){
			pages.setPageSize(pages.getRows().size());
			fillPageDatas(pirlist, pages);
		for ( AccountLog log : logList) {
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
			}
			AccountLogItemModel logModel=new AccountLogItemModel();
			logModel.setAccountType(log.getAccountType());//账户类别 
			//logModel.setAccountTypeStr(log.getAccountTypeStr());//状态说明
			logModel.setCreateTime(log.getCreateTime());//添加时间
			logModel.setMoney(log.getMoney());//交易金额
			//获取资金交易名称
			//logModel.setFunName(getFunName(log.getAccountType(),log.getOrderNo()));
			logModel.setFunName(log.getAccountTypeStr());
			if(!"".equals(log.getRemark())&&log.getRemark()!=null){
			 if(log.getRemark().contains("</a>")){
				 String mark1=log.getRemark().replace("</a>", "");
				 log.setRemark(mark1.replace(mark1.substring(mark1.indexOf("<"),mark1.lastIndexOf(">")+1), ""));
				 }
			}
			 logModel.setRemark(log.getRemark());   //备注
			//资金说明(显示资金正负)
			//logModel.setMoneyStr(getMoneyStr(log.getMoney(),logModel.getFunName()));
			logModel.setMoneyStr(getPaymentsTypeStr(log.getPaymentsType()));
			pirlist.getList().add(logModel);
		}
		}
		obj=createSuccessAppResponse(pirlist);
		}catch(Exception e){
			obj=dealException(e);
		}
		return obj;
	}
/*	private String getFunName(String accountType, String orderNo) {
		String result="";
		if(accountType.contains("invest")){
				if(accountType.equals("invest_unfreeze")){
				result="投资失败";
				}else if(accountType.equals("invest_freeze")){
				if(!StringUtils.isBlank(orderNo)){
					    ProjectInvest projectInvest=projectInvestService.getInvestByOrderNo(orderNo);//根据订单号查询投资记录
						Project  project=projectService.getProjectByUuid(projectInvest.getProjectId());//根据项目id来获取项目信息
						result="投资_"+project.getProjectName();//投资项目名称
				}
				}else{
			    result="投资成功";
				}
		}else if(accountType.equals("collect_capital")){
			result="回款_本金";	
		}else if(accountType.equals("collect_interest")){
			result="回款_利息";
		}else if(accountType.contains("recharge")){
			result="充值";
		}else if(accountType.contains("cash")){
			result="提现";
		}else if(accountType.equals("loan")){
			result="借款";
		}else{
			result="其他";
		}
		return result;
	}*/
	
	//给操作金额进行正负判断
/*	private String getMoneyStr(BigDecimal money,String funName){
		String result="";
		if(funName.contains("投资")){
			if(funName.equals("投资成功")){
				result="-";
			}else{
				result="";
			}
		}else if(funName.equals("借款")){
			result="+";
		}else if(funName.equals("充值")||funName.contains("回款")){
			result="+";
		}else if(funName.equals("其他")){
			result="-";
		}else{
			result="-";
		}
		return result;
	}*/
	private String getPaymentsTypeStr(String paymentsType){
		String result="";
		if("1".endsWith(paymentsType)){
				result="+";
		}else if("2".endsWith(paymentsType)){
			result="-";
		}else{
			result="";
		}
		return result;
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
			//提现处理中：提现申请、提现申请成功、待审核
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

			//投资冻结：投资产品后待成立审核、待满额放款（变现产品），显示状态为投资冻结

			//投资成功：成立审核通过后放款、无需成立审核的产品支付成功、债权转让产品受让成功、变现产品满额后放款，显示状态为投资成功

			//投资失败：产品成立审核不通过、产品变现失败，显示状态为投资失败

			//加息利息收回：加息券加息、vip加息、产品加息，这部分利息收回后显示此状态

			//管理费：包括利息管理费、变现服务费、转让手续费、借款管理费、提现手续费，状态均显示为管理费
			
			accountTypeStr = DictUtils.getItemName(DictTypeEnum.ACCOUNT_TYPE.getValue(), accountType);
		}
		return accountTypeStr;
	}

}
