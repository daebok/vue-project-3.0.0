package com.rd.ifaes.manage.account;


import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.util.excel.ExportModel;
import com.rd.ifaes.common.util.excel.ExportUtil;
import com.rd.ifaes.core.account.domain.Recharge;
import com.rd.ifaes.core.account.model.RechargeModel;
import com.rd.ifaes.core.account.service.RechargeService;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.manage.sys.SystemController;

@Controller
public class ManageRechargeController extends SystemController {

	@Resource
	private RechargeService rechargeService;
	
	
	/**
	 * 列表页面
	 */
	@RequestMapping(value = "/account/recharge/rechargeList")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.RECHARGE_LIST)
	public String rechargeList(){
		return "/account/recharge/rechargeList";
	}
	/**
	 * 列表
	 * @return
	 */
	@RequiresPermissions("account:rechargeList:query")
	@RequestMapping(value = "/account/recharge/list")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.RECHARGE_LIST)
	public Object list(RechargeModel recharge){
		return rechargeService.findManagePage(recharge);
	}
	
	/**
	 * 
	 * 导出充值记录
	 * @author jxx
	 * @date 2016年8月4日
	 * @param model
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/account/recharge/exportExcel")
	@RequiresPermissions("account:rechargeList:export")
	@SystemLog(method=SysLogConstant.EXPORT,content=SysLogConstant.RECHARGE_LIST)
	public void exportExcel(final RechargeModel model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ExportUtil.exportExcel(new ExportModel<RechargeModel>() {
			@Override
			public String getCacheKey() {
				return   ManageRechargeController.this.getUserId();
			}
			@Override
			public RechargeModel getModel() {
				return model;
			}
			@Override
			public int getTotal(RechargeModel entity) {
				return rechargeService.getManageCount(entity);
			}
			@Override
			public List<Recharge> getPageRecords(RechargeModel entity) {
				return rechargeService.findManagePage(entity).getRows();
			}
		}, request, response);
	}
}
