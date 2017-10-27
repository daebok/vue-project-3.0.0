/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.manage.bond;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.constant.UfxConstant;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.bond.domain.BondRule;
import com.rd.ifaes.core.bond.model.BondRuleModel;
import com.rd.ifaes.core.bond.service.BondRuleService;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.manage.sys.SystemController;

/**
 *   债权规则类
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年7月26日
 */
@Controller
public class BondRuleManageController extends SystemController{
	/**
	 * 请选择一条记录
	 */
	private static final String IDMSG =  "请选择一条记录";
	/**
	 * 修改完成
	 */
	private static final String SUMESG = "操作成功";
	/**
	 * 债权规则业务类
	 */
	@Resource
	private transient BondRuleService bondRuleService;
	
	/**
	 *  债权规则界面
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @return
	 */
	@RequestMapping(value="/bond/rule/bondRuleManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.BOND_RULE)
	@Deprecated
	public  String bondRuleManage(){
		return "/bond/rule/bondRuleManage";
	}
	
	/**
	 *  进入债权规则配置添加修改界面
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @return
	 */
	@RequiresPermissions("project:bond:bondRule:add")
	@RequestMapping(value="/bond/rule/bondRuleAddPage")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.BOND_RULE)
	@Deprecated
	public String bondRuleAddPage(final Model model) {
		final BondRuleModel rule = bondRuleService.getRecentBondRuleModel();
		if(model != null){
			 model.addAttribute("model", rule);
		}
		return "/bond/rule/bondRuleAddPage";
	}
	
	/**
	 * 查看规则
	 * @author QianPengZhan
	 * @date 2016年9月20日
	 * @param id
	 * @param request
	 * @return
	 */
	@Deprecated
	@RequiresPermissions("project:bond:bondRule:query")
	@RequestMapping(value="/bond/rule/bondRuleQuery")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.BOND_RULE)
	public String bondRuleQuery(final String id ,final Model model) {
		if(StringUtils.isBlank(id)){
			renderResult(false, IDMSG);
		}
		final BondRule rule = bondRuleService.get(id);
		 model.addAttribute("model", rule);
		return "/bond/rule/bondRuleQuery";
	}
	
	/**
	 * 直接进入债权规则页面查看  可编辑 查看历史记录
	 * @author QianPengZhan
	 * @date 2016年12月5日
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/bond/rule/bondRuleAddEditQuery")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.BOND_RULE)
	public String bondRuleAddEditQuery(final Model model){
		final BondRuleModel rule = bondRuleService.getRecentBondRuleModel();
		if(model != null){
			 model.addAttribute("model", rule);
			 model.addAttribute("flag",!ConfigUtils.getTppName().equals(UfxConstant.UFX_TPP_NAME_CHINAPNR));
		}
		return "/bond/rule/bondRuleAddEditQuery";
	}
	
	/**
	 *  债权规则配置添加修改操作执行
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @param model
	 * @return
	 */
	@RequiresPermissions("project:bond:bondRule:add")
	@RequestMapping(value="/bond/rule/bondRuleAdd",method = RequestMethod.POST)
	@ResponseBody
	@TokenValid(value = TokenConstants.TOKEN_BONDRULE_ADD, dispatch = true)
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.BOND_RULE)
	public Object bondRuleAdd(final BondRuleModel model){
		model.setIsRemainDays(CommonEnum.YES.getValue());
		final String errorMsg = bondRuleService.insertBondRuleModel(model);
		boolean flag = true;
		//转让规则提示信息展示
		StringBuilder message = new StringBuilder();
		if(StringUtils.isBlank(errorMsg)){
			message.append(SUMESG);
			if(UfxConstant.UFX_TPP_NAME_CHINAPNR.equals(ConfigUtils.getTppName())){
				//第三方是汇付
				message.append("<br><font style='font-size:14px;color:red;'>注意:</font>您现在所用的第三方托管账户为汇付天下,每笔转让手续费不得超过受让金额的")
				.append("<font style='font-size:14px;color:red;'>").append(ConfigUtils.getValue(ConfigConstant.UFX_BOND_FEE_LIMIT)).append("%</font>").append("");
			}
		}else{
			flag = false;
			message.append("<br><font style='font-size:14px;color:red;'>注意:</font>").append(errorMsg);
		}
		return renderResult(flag, message.toString());
	}
	
	/**
	 * 进入债权规则历史记录界面
	 * @author QianPengZhan
	 * @date 2016年12月5日
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/bond/rule/bondRuleViewHistory")
	@SystemLog(method=SysLogConstant.VIEW,content=SysLogConstant.BOND_RULE)
	public String bondRuleViewHistory(final BondRule model){
		return "/bond/rule/bondRuleViewHistory";
	}
	
	/**
	 *  债权规则列表  债权规则历史记录
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/bond/rule/bondRuleList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.BOND_RULE)
	public  Object bondRuleList(final BondRule model){
		return bondRuleService.findPage(model);
	}
	
	
}
