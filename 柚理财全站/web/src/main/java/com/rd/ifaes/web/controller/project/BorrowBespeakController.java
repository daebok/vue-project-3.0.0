/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.web.controller.project;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.ip.IPUtil;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.project.model.BorrowBespeakModel;
import com.rd.ifaes.core.project.service.BorrowBespeakService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.sys.domain.DictData;
import com.rd.ifaes.core.sys.service.DictDataService;
import com.rd.ifaes.web.controller.WebController;

/**
 * 预约借款
 * @version 3.0
 * @author zb
 * @date 2016年7月30日
 */
@Controller
public class BorrowBespeakController extends WebController {
	
	/**
	 * 预约借款service
	 */
	@Resource
	private transient BorrowBespeakService borrowBespeakService;
	/**
	 * 数据字典service
	 */
	@Resource
	private transient DictDataService dictDataService;
	/**
	 * 产品service
	 */
	@Resource
	private transient ProjectService projectService;
	
	/**
	 * 
	 * 预约借款页面
	 * @author ZhangBiao
	 * @date 2016年7月30日
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/borrow/bespeak")
	public String bespeak(final Model model,final HttpServletRequest request) {
		// 获取地区
		 model.addAttribute("areaJson", CacheUtils.getStr(CacheConstant.KEY_AREA_JSON));
		// 获取期限
		final List<DictData> bespeakTimeList = dictDataService.findListByDictType(DictTypeEnum.BESPEAK_LIMIT_TIME.getValue());
		model.addAttribute("bespeakTimeList", JSONArray.toJSONString(bespeakTimeList));
		// 获取已借款成功金额 //LoanEnum.STATUS_REPAYED_PART.getValue()
		final String[] projectStatus={LoanEnum.STATUS_ESTABLISH.getValue(),
				LoanEnum.STATUS_REPAY_START.getValue(),
				LoanEnum.STATUS_REPAY_OVERDUE.getValue(),
				LoanEnum.STATUS_BAD_DEBT.getValue(),
				LoanEnum.STATUS_REPAYED_SUCCESS.getValue()
				};
		 model.addAttribute("account", projectService.sumProjectAccount(projectStatus).intValue());
		 model.addAttribute("borrowMinAccount", ConfigUtils.getDouble(ConfigConstant.BORROW_LOWEST_ACCOUNT));
		 model.addAttribute("borrowMaxAccount", ConfigUtils.getDouble(ConfigConstant.BORROW_MOST_ACCOUNT));
		 setToken(TokenConstants.TOKEN_ADD_BORROW_BESPEAK,request);//token 设置
		return  "/borrow/bespeak";
	}
	
	/**
	 * 
	 * 预约借款添加
	 * @author ZhangBiao
	 * @date 2016年7月30日
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/borrow/bespeakAdd")
	@ResponseBody
	@TokenValid(value=TokenConstants.TOKEN_ADD_BORROW_BESPEAK,dispatch=true)
	public Object bespeakAdd(@RequestParam(value = "zone[]") final String[] zone,
			final BorrowBespeakModel model ,final HttpServletRequest request){
		// 校验验证码
		model.validRegRule();
		model.setAddIp(IPUtil.getRemortIP(request) );
		if(zone == null || zone.length < Constant.INT_TWO || StringUtils.isBlank(zone[Constant.INT_ZERO]) 
				|| StringUtils.isBlank(zone[Constant.INT_ONE])){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.BORROW_BESPEAK_ZONE_NOT_EMPTY));
		}
		borrowBespeakService.add(model,zone);
		return renderResult(true, ResourceUtils.get(ResourceConstant.BORROW_BESPEAK_SUCCESS));
	}
}
