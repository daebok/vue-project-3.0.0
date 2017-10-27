/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.manage.bond;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.core.bond.model.BondModel;
import com.rd.ifaes.core.bond.service.BondService;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.manage.sys.SystemController;

/**
 * 债权标管理类
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年8月4日
 */
@Controller
public class BondManageController extends SystemController{
	
	/**
	 * 债权业务
	 */
	@Resource
	private transient BondService bondService;
	
	/**
	 * 进入后台的债权标管理界面
	 * @author QianPengZhan
	 * @date 2016年8月4日
	 * @return
	 */
	@RequestMapping(value="/bond/bond/bondManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.BOND_LIST)
	public  String bondManage(){
		return "/bond/bond/bondManage";
	}
	
	
	/**
	 * 获取债权列表数据
	 * @author QianPengZhan
	 * @date 2016年8月5日
	 * @return
	 */
	@RequestMapping(value="/bond/bond/bondList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.BOND_LIST)
	public Object bondList(final BondModel bondModel){
		if(StringUtils.isNotBlank(bondModel.getKeywords())){
			bondModel.setKeywords(bondModel.getKeywords());
		}
		return bondService.findManagePage(bondModel);
	}
	
	/**
	 * 转让投资记录
	 * @author QianPengZhan
	 * @date 2016年8月17日
	 * @param id
	 * @return
	 */
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.BOND_INVEST_LIST)
	@RequestMapping(value = "/bond/bond/bondInvestPage")
	public String tenderPage(final String id,final Model model) {
		 model.addAttribute("bondId", id);
		return "/bond/bond/bondInvestPage";
	}
}
