/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.manage.risk;
import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.risk.domain.LevelConfig;
import com.rd.ifaes.core.risk.service.LevelConfigService;
import com.rd.ifaes.manage.sys.SystemController;

/**
 * 风险评估后台配置
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年7月26日
 */
@Controller
public class RiskConfigManageController extends SystemController{
	
	/**
	 * 等级业务类
	 */
	@Resource
	private transient LevelConfigService levelConfigService;
	
	
	
	/**
	 * 产品风险等级和用户风险承受能力对应关系界面
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @return
	 */
	@RequiresPermissions("set:riskTpl:riskLevel:query")
	@RequestMapping(value="/risk/riskConfigManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.RISK_LEVEL)
	public String riskConfigManage(){
		return "/risk/riskConfigManage";
	}
	
	/**
	 *  风险评估配置列表
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @param model
	 * @return
	 */
	@RequiresPermissions("set:riskTpl:riskLevel:query")
	@RequestMapping(value="/risk/riskConfigList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.RISK_LEVEL)
	public Object riskConfigList(final LevelConfig model){
		if(StringUtils.isNotBlank(model.getKeywords())){
			model.setRiskLevelName(model.getKeywords());
		}
		model.setDeleteFlag(Constant.INT_ZERO);
		return levelConfigService.findPage(model);
	}
	
	/**
	 * 进入新增试卷配置界面
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @return
	 */
	@RequestMapping(value = "/risk/riskConfigAddPage")
	@RequiresPermissions("set:riskTpl:riskLevel:add")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.RISK_LEVEL)
	public String addPage(final Model model) {
		LevelConfig config = levelConfigService.getMaxConfig();
		if(config == null){
			config = new LevelConfig();
			config.setRiskLevelVal(Constant.INT_ONE);
			config.setUserRiskLevelVal(Constant.INT_ZERO);
		}else{
			config.setRiskLevelVal(config.getRiskLevelVal()+Constant.INT_ONE);
			config.setUserRiskLevelVal(config.getRiskLevelVal()-Constant.INT_ONE);
		}
		 model.addAttribute("config", config);
		return "/risk/riskConfigAddPage";
	}
	
	/**
	 * 新增产品风险等级和用户风险承受能力对应关系配置
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/risk/riskConfigAdd", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("set:riskTpl:riskLevel:add")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.RISK_LEVEL)
	public Object add(final LevelConfig model){		
		levelConfigService.insert(model) ;
		return renderResult(true,MSG_SUCCESS);	  
	}
	
	/**
	 *  进入配置编辑界面
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/risk/riskConfigEditPage")
	@RequiresPermissions("set:riskTpl:riskLevel:edit")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.RISK_LEVEL)
	public String editPage(final String id,final Model model) {
		if(StringUtils.isNotBlank(id)){
			 model.addAttribute("riskConfig", levelConfigService.get(id));
		}
		return "/risk/riskConfigEditPage";
	}
	
	/**
	 *  编辑产品安全配置
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/risk/riskConfigEdit",method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("set:riskTpl:riskLevel:edit")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.RISK_LEVEL)
	public Object edit(final LevelConfig model){
		levelConfigService.update(model) ;
		return renderResult(true,MSG_SUCCESS);	 
	}
	
	
	/**
	 * 单体或者批量逻辑删除
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/risk/delLogic")
	@ResponseBody
	@RequiresPermissions("set:riskTpl:riskLevel:del")
	@SystemLog(method=SysLogConstant.DEL,content=SysLogConstant.RISK_LEVEL)
	public Object delLogic(final String[] ids) {
		levelConfigService.deleteBatch(ids);
		return renderResult(true,MSG_SUCCESS);	 
	}
}
