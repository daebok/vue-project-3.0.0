/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.manage.tpp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.TppUtil;
import com.rd.ifaes.core.account.domain.Cash;
import com.rd.ifaes.core.account.domain.Recharge;
import com.rd.ifaes.core.account.service.CashService;
import com.rd.ifaes.core.account.service.RechargeService;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.model.ProjectInvestModel;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.tpp.model.cbhb.chk.CbhbExpChkModel;
import com.rd.ifaes.core.tpp.model.cbhb.chk.CbhbInvestChkModel;
import com.rd.ifaes.core.tpp.model.cbhb.chk.CbhbPpdChkModel;
import com.rd.ifaes.core.tpp.model.cbhb.chk.CbhbWdcChkModel;
import com.rd.ifaes.core.tpp.service.TppService;
import com.rd.ifaes.manage.sys.SystemController;

/**
 * 渤海银行对账类
 * @version 3.0
 * @author QianPengZhan
 * @date 2017年4月5日
 */
@Controller
public class CbhbManageController extends SystemController {
	@Resource
	private transient RechargeService rechargeService;
	
	@Resource
	private transient ProjectInvestService projectInvestService;
	@Resource
	private transient CashService cashService;
	
	/**
	 * 投资对账界面
	 * @author QianPengZhan
	 * @date 2017年4月5日
	 */
	@RequestMapping(value = "/tpp/cbhb/investChkManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.CBHB_INVEST_CHECK)
	public String investChkManage(Model model){
		return "/tpp/cbhb/investChkManage";
	}
	
	/**
	 * 投资对账列表
	 * @author QianPengZhan
	 * @date 2017年4月5日
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/tpp/cbhb/investChkList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.CBHB_INVEST_CHECK)
	public Object getInvestChkList(ProjectInvestModel  investModel){
		TppService tppService = (TppService)TppUtil.tppService();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("searchTime",DateUtils.getNow());
		List<ProjectInvestModel> list = new ArrayList<ProjectInvestModel>();
		List<CbhbInvestChkModel> chkList = (List<CbhbInvestChkModel>)tppService.tppInvestChk(params);
		if(CollectionUtils.isNotEmpty(chkList)){
			for (int i = 0; i < chkList.size(); i++) {
				CbhbInvestChkModel chk = chkList.get(i);
				ProjectInvest invest = projectInvestService.getInvestByOrderNo(chk.getOrdNo());
				ProjectInvestModel model = invest.prototype();
				model.setChkResult(chk.getOrdSts());
				list.add(model);
			}
		}
		return list;
	}
	
	/**
	 * 充值对账界面
	 * @author QianPengZhan
	 * @date 2017年4月5日
	 */
	@RequestMapping(value = "/tpp/cbhb/pddChkManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.CBHB_PDD_CHECK)
	public String pddChkManage(Model model){
		return "/tpp/cbhb/pddChkManage";
	}
	
	/**
	 * 充值对账列表
	 * @author QianPengZhan
	 * @date 2017年4月5日
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/tpp/cbhb/pddChkList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.CBHB_PDD_CHECK)
	public Object getPddChkList(Recharge  Recharge){
		TppService tppService = (TppService)TppUtil.tppService();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("searchTime",DateUtils.getNow());
		List<Recharge> list = new ArrayList<Recharge>();
		List<CbhbPpdChkModel> chkList = (List<CbhbPpdChkModel>)tppService.tppPpdChk(params);
		if(CollectionUtils.isNotEmpty(chkList)){
			for (int i = 0; i < chkList.size(); i++) {
				CbhbPpdChkModel chk = chkList.get(i);
				Recharge recharge = rechargeService.getRechargeByOrderNo(chk.getOrdNo());
				list.add(recharge);
			}
		}
		return list;
	}
	
	/**
	 * 提现对账界面
	 * @author QianPengZhan
	 * @date 2017年4月5日
	 */
	@RequestMapping(value = "/tpp/cbhb/wdcChkManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.CBHB_WDC_CHECK)
	public String wdcChkManage(Model model){
		return "/tpp/cbhb/wdcChkManage";
	}
	
	/**
	 * 提现对账列表
	 * @author QianPengZhan
	 * @date 2017年4月5日
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/tpp/cbhb/wdcChkList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.CBHB_WDC_CHECK)
	public Object getWdcChkList(Cash model){
		TppService tppService = (TppService)TppUtil.tppService();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("searchTime",DateUtils.getNow());
		List<Cash> list = new ArrayList<Cash>();
		List<CbhbWdcChkModel> chkList = (List<CbhbWdcChkModel>)tppService.tppWdcChk(params);
		if(CollectionUtils.isNotEmpty(chkList)){
			for (int i = 0; i < chkList.size(); i++) {
				CbhbWdcChkModel chk = chkList.get(i);
				Cash cash = cashService.findByOrderNo(chk.getOrdNo());
				list.add(cash);
			}
		}
		return list;
	}
	
	
	/**
	 * 实时红包对账界面
	 * @author QianPengZhan
	 * @date 2017年4月5日
	 */
	@RequestMapping(value = "/tpp/cbhb/expChkManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.CBHB_EXP_CHECK)
	public String expChkManage(Model model){
		return "/tpp/cbhb/expChkManage";
	}
	
	/**
	 * 实时红包对账列表
	 * @author QianPengZhan
	 * @date 2017年4月5日
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/tpp/cbhb/expChkList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.CBHB_EXP_CHECK)
	public Object getExpChkList(Cash model){
		TppService tppService = (TppService)TppUtil.tppService();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("searchTime",DateUtils.getNow());
		List<CbhbExpChkModel> chkList = (List<CbhbExpChkModel>)tppService.tppExpChk(params);
		return chkList;
	}
	
 }
