/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.manage.bond;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.dict.BondInvestEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.resource.BondResource;
import com.rd.ifaes.core.bond.model.BondInvestModel;
import com.rd.ifaes.core.bond.service.BondInvestService;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.manage.sys.SystemController;

/**
 * 债权投资记录（转让记录）
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年8月5日
 */
@Controller
@RequestMapping(value="/bond/bond")
public class BondInvestManageController extends SystemController{
	
	/**
	 * 债权投资业务类
	 */
	@Resource
	private transient BondInvestService bondInvestService;
	
	/**
	 * 构造函数
	 */
	public BondInvestManageController(){
		super();
	}
	
	/**
	 * 转让管理
	 * @author QianPengZhan
	 * @date 2016年8月5日
	 * @return
	 */
	@RequestMapping(value="/bondInvestManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.BOND_INVEST_LIST)
	public String bondInvestManage(){
		return "/bond/bond/bondInvestManage";
	}
	
	/**
	 * 转让记录
	 * @author QianPengZhan
	 * @date 2016年8月17日
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/bondInvestList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.BOND_INVEST_LIST)
	public Object bondInvestList(final BondInvestModel model){
		if(StringUtils.isNotBlank(model.getKeywords())){
			model.setUserName(model.getKeywords());
		}
		model.setStatus(BondInvestEnum.STATUS_COMPLETE.getValue());
		return bondInvestService.findPageModel(model);
	}
	
	
	/**
	 * 转让记录
	 * @author QianPengZhan
	 * @date 2016年8月17日
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/bondInvestListData")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.BOND_INVEST_LIST)
	public Object bondInvestList(@RequestParam("bondId") final String bondId){
		if(StringUtils.isBlank(bondId)){
			throw new BussinessException(ResourceUtils.get(BondResource.BOND_ID_NOT_EXISTS),BussinessException.TYPE_JSON);
		}
		final BondInvestModel model = new BondInvestModel();
		model.setBondId(bondId);
		return bondInvestService.findPageModel(model);
	}
	
}
