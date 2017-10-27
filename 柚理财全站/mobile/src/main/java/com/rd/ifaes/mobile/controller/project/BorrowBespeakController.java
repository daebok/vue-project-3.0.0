/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.mobile.controller.project;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TokenUtils;
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
import com.rd.ifaes.mobile.controller.WebController;


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
	 * 预约借款页面--移动端
	 * @author lgx
	 * @return
	 */
	@RequestMapping(value="/app/open/borrow/bespeak")
	@ResponseBody
	public Object bespeak(final HttpServletRequest request){
		Object obj=null;
		try{
			Map<String, Object> data = new HashMap<String, Object>();
			// 获取地区
			data.put("areaJson", CacheUtils.getStr(CacheConstant.KEY_AREA_JSON));
			// 获取期限
			final List<DictData> bespeakTimeList = dictDataService.findListByDictType(DictTypeEnum.BESPEAK_LIMIT_TIME.getValue());
			final JSONArray jsonarray = JSONArray.fromObject(bespeakTimeList);
			data.put("bespeakTimeList", jsonarray.toString());
			// 获取已借款成功金额 //LoanEnum.STATUS_REPAYED_PART.getValue()
			final String[] projectStatus={LoanEnum.STATUS_ESTABLISH.getValue(),
					LoanEnum.STATUS_REPAY_START.getValue(),
					LoanEnum.STATUS_REPAY_OVERDUE.getValue(),
					LoanEnum.STATUS_BAD_DEBT.getValue(),
					LoanEnum.STATUS_REPAYED_SUCCESS.getValue()
					};
			data.put("account", projectService.sumProjectAccount(projectStatus).intValue());//已借款成功金额
			data.put("borrowMinAccount", ConfigUtils.getDouble(ConfigConstant.BORROW_LOWEST_ACCOUNT));//借款最大金额
			data.put("borrowMaxAccount", ConfigUtils.getDouble(ConfigConstant.BORROW_MOST_ACCOUNT));//借款最小金额
			data.put("borrowBespeakAddToken",TokenUtils.setToken(request.getSession(), TokenConstants.TOKEN_ADD_BORROW_BESPEAK));//token 设置
			data.put("__sid",request.getSession().getId());//token 设置
			
			obj=createSuccessAppResponse(data);
		}catch(Exception e){
			obj=dealException(e);
		}
		return obj;
	}
	
	
	/**
	 * 
	 * 预约借款页面--app专用
	 * @author lgx
	 */
	@RequestMapping(value="/app/open/borrow/appbespeak")
	public Object appbespeak(final Model model,final HttpServletRequest request){
			// 获取地区
		model.addAttribute("areaJson", CacheUtils.getStr(CacheConstant.KEY_AREA_JSON));
			// 获取期限
			final List<DictData> bespeakTimeList = dictDataService.findListByDictType(DictTypeEnum.BESPEAK_LIMIT_TIME.getValue());
			final JSONArray jsonarray = JSONArray.fromObject(bespeakTimeList);
			model.addAttribute("bespeakTimeList", jsonarray.toString());
			// 获取已借款成功金额 //LoanEnum.STATUS_REPAYED_PART.getValue()
			final String[] projectStatus={LoanEnum.STATUS_ESTABLISH.getValue(),
					LoanEnum.STATUS_REPAY_START.getValue(),
					LoanEnum.STATUS_REPAY_OVERDUE.getValue(),
					LoanEnum.STATUS_BAD_DEBT.getValue(),
					LoanEnum.STATUS_REPAYED_SUCCESS.getValue()
					};
			model.addAttribute("account", projectService.sumProjectAccount(projectStatus).intValue());//已借款成功金额
			model.addAttribute("borrowMinAccount", ConfigUtils.getDouble(ConfigConstant.BORROW_LOWEST_ACCOUNT));//借款最大金额
			model.addAttribute("borrowMaxAccount", ConfigUtils.getDouble(ConfigConstant.BORROW_MOST_ACCOUNT));//借款最小金额
			model.addAttribute("mobileUrl", ConfigUtils.getValue("mobile_url"));
			//model.addAttribute("mobileUrl", "http://10.10.1.85:8089");
			setToken(TokenConstants.TOKEN_ADD_BORROW_BESPEAK,request);//token 设置
			return  "/app/bespeak/bespeak_borrow";
	}
	
	/**
	 * 
	 * 预约借款添加--移动端
	 * @author lgx
	 * @date 2016年11月9日
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/app/open/borrow/bespeakAdd")
	@ResponseBody
	@TokenValid(value=TokenConstants.TOKEN_ADD_BORROW_BESPEAK,dispatch=true)
	public Object bespeakAdd(String zone,
			final BorrowBespeakModel model ,final HttpServletRequest request){
		Object obj=null;
		try{
			zone=URLDecoder.decode(zone);
			model.setContactName(URLDecoder.decode(model.getContactName()));
			String[] zones=zone.split(","); 
		// 校验验证码
		//model.validRegRule();
		model.setAddIp(IPUtil.getRemortIP(request) );
		if(zone == null || zones.length < Constant.INT_TWO || StringUtils.isBlank(zones[Constant.INT_ZERO]) 
				|| StringUtils.isBlank(zones[Constant.INT_ONE])){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.BORROW_BESPEAK_ZONE_NOT_EMPTY));
		}
		borrowBespeakService.add(model,zones);
	    obj=createSuccessAppResponse("您已提交借款申请，我们的工作人员会在24小时内审核您的请求，并尽快与您取得联系。");
				} catch (Exception e) {
					obj=dealException(e);
				}
				return obj;
	}
}
