package com.rd.ifaes.manage.account;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.util.excel.ExportModel;
import com.rd.ifaes.common.util.excel.ExportUtil;
import com.rd.ifaes.core.account.domain.Cash;
import com.rd.ifaes.core.account.model.CashModel;
import com.rd.ifaes.core.account.service.CashService;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.manage.sys.SystemController;

@Controller
public class ManageCashController extends SystemController {

	@Resource
	private CashService cashService;
	
	
	/**
	 * 列表页面
	 */
	@RequestMapping(value = "/account/cash/cashList")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.CASH_LIST)
	public String cashList(final Model model) {
		 model.addAttribute("cashNeedAudit", ConfigUtils.getValue(Constant.CASH_NEED_AUDIT));
		return "/account/cash/cashList";
	}
	
	/**
	 * 列表
	 * @return
	 */
	@RequestMapping(value = "/account/cash/list")
	@ResponseBody
	@RequiresPermissions("account:cashList:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.CASH_LIST)
	public Object list(CashModel cash){
		final String cashNeedAudit = ConfigUtils.getValue(Constant.CASH_NEED_AUDIT);
		if(Constant.FLAG_YES.equals(cashNeedAudit)){ //启用
			//如果首次查询，则默认显示待审核记录
			if(null == cash.getKeywords() && null == cash.getUserName() && null == cash.getRealName()
					&& null == cash.getCashNo() && null == cash.getStatus() && null == cash.getIsAdvance() 
					&& null == cash.getStartTime() && null == cash.getEndTime()){
				cash.setStatus(Constant.CASH_STATUS_AUDIT);
			}
		}
		return cashService.findManagePage(cash);
	}
	
	/**
	 * 待审核页面
	 * @return
	 */
	@RequestMapping(value = "/account/cash/auditPage")
	@RequiresPermissions("account:cashList:auditing")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.CASH_AUDIT)
	public String auditPage(final String id,final Model model) {
		 model.addAttribute("cash", cashService.getCashModel(id));
		return "/account/cash/auditPage";
	}
	
	/**
	 * 提交审核
	 * @return
	 */
	@RequestMapping(value = "/account/cash/doAudit")
	@ResponseBody
	@RequiresPermissions("account:cashList:auditing")
	@TokenValid(value=TokenConstants.TOKEN_CASH_AUDIT,dispatch=true)
	@SystemLog(method=SysLogConstant.AUDIT,content=SysLogConstant.CASH_AUDIT)
	public Object doAudit(CashModel model){
		model.setOperatorName(getCurrUserName());
		cashService.checkAndunFreeze(model);
		cashService.doAudit(model);
		return renderResult(true, "操作成功");
	}
	
	/**
	 * 人工核查页面
	 * @return
	 */
	@RequestMapping(value = "/account/cash/checkPage")
	@RequiresPermissions("account:cashList:check")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.CASH_CHECK)
	public String checkPage(final String id,final Model model) {
		 model.addAttribute("cash", cashService.getCashModel(id));
		return "/account/cash/checkPage";
	}
	
	/**
	 * 更新核查状态
	 * @return
	 */
	@RequestMapping(value = "/account/cash/doCheck")
	@ResponseBody
	@RequiresPermissions("account:cashList:check")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.CASH_CHECK)
	public Object doCheck(CashModel model){
		model.setOperatorName(getCurrUserName());
		cashService.doCheck(model);
		return renderResult(true, "操作成功");
	}
	
	/**
	 * 
	 * 导出提现记录
	 * @author jxx
	 * @date 2016年8月4日
	 * @param model
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws Exception
	 */
	@RequiresPermissions("account:cashList:export")
	@RequestMapping(value = "/account/cash/exportExcel")
	@SystemLog(method=SysLogConstant.EXPORT,content=SysLogConstant.CASH_LIST)
	public void exportExcel(final CashModel model,final HttpServletRequest request,final HttpServletResponse response) throws IOException {
		ExportUtil.exportExcel(new ExportModel<CashModel>() {
			@Override
			public String getCacheKey() {
				return  ManageCashController.this.getUserId();
			}
			@Override
			public CashModel getModel() {
				return model;
			}
			@Override
			public int getTotal(CashModel entity) {
				return cashService.getManageCount(entity);
			}
			@Override
			public List<Cash> getPageRecords(CashModel entity) {
				return cashService.findManagePage(entity).getRows();
			}
		}, request, response);
	}
}
