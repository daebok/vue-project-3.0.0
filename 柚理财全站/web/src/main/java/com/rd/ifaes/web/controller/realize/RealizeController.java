package com.rd.ifaes.web.controller.realize;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.dict.InvestEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.TokenUtils;
import com.rd.ifaes.common.util.ip.IPUtil;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.project.domain.Realize;
import com.rd.ifaes.core.project.domain.RealizeRule;
import com.rd.ifaes.core.project.model.ProjectInvestModel;
import com.rd.ifaes.core.project.service.ProductService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.project.service.ProjectTypeService;
import com.rd.ifaes.core.project.service.RealizeInvestService;
import com.rd.ifaes.core.project.service.RealizeRuleService;
import com.rd.ifaes.core.project.service.RealizeService;
import com.rd.ifaes.core.tpp.model.ufx.UfxInvestModel;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.web.controller.WebController;


/**
 * 变现投资
 * @version 3.0
 * @author fxl
 * @date 2016年7月27日
 */
@Controller
public class RealizeController extends WebController {

	@Resource
	private transient RealizeService realizeService;
	@Resource
	private transient RealizeInvestService realizeInvestService;
	@Resource
	private transient RealizeRuleService realizeRuleService;
	@Resource
	private transient ProjectService projectService;
	@Resource
	private transient ProjectTypeService projectTypeService;
	@Resource
	private transient ProductService productService;

	
	/**
	 * 变现介绍
	 * @author fxl
	 * @date 2016年7月27日
	 * @return
	 */
	@RequestMapping(value = "/realize/introduce")
	public String introduce(final Model model) {
		final RealizeRule rule = realizeRuleService.getRule();
		 model.addAttribute("rule", rule);
		return "/realize/introduce";
	}
	
	/**
	 * 变现频道首页
	 * @author fxl
	 * @date 2016年7月27日
	 * @return
	 */
	@RequestMapping(value = "/realize/index")
	public String index() {
		return "/realize/index";
	}

	/**
	 * 变现项目列表
	 * @author fxl
	 * @date 2016年7月27日
	 * @param realize
	 * @return
	 */
	@RequestMapping(value = "/realize/projectList")
	@ResponseBody
	public Object projectList(final Realize realize) {
		final Map<String,Object> data =new HashMap<>();
		realize.setStatusSet(new String []{ LoanEnum.STATUS_RAISING.getValue(), 
				LoanEnum.STATUS_RAISE_FINISHED.getValue(),
				LoanEnum.STATUS_REPAY_START.getValue(),
				LoanEnum.STATUS_REPAYED_SUCCESS.getValue()});
		realize.setSaleChannel(InvestEnum.INVEST_CHANNEL_PC.getValue());
		data.put("result", true);
		data.put("List", realizeService.findAllPageByCache(realize));
		data.put("systemTime", System.currentTimeMillis());
		return data;
	}
	
	/**
	 * 变现列表筛选条件
	 * @author fxl
	 * @date 2016年8月19日
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "/realize/queryCondition")
	@ResponseBody
	public Object queryCondition() {
		final Map<String, Object> data = renderSuccessResult();
		data.put("condition", realizeService.queryCondition());
		return data;
	}
	
	
	/**
	 * 变现详情页面
	 * @author fxl
	 * @date 2016年7月27日
	 * @return
	 */
	@RequestMapping(value = "/realize/detail")
	public String detail(final String id,final Model model) {
		final Realize realize = realizeService.findRealizeFromCache(id);
		 model.addAttribute("id", id);
		 model.addAttribute("projectId", id);
		 model.addAttribute("investId", realize.getInvestId());
		 model.addAttribute("originalProjectId", realize.getOriginalProjectId());
		return "/realize/detail";
	}

	/**
	 * 变现详情数据(JSON)
	 * @author fxl
	 * @date 2016年7月27日
	 * @return
	 */
	@RequestMapping(value = "/realize/detailInfo")
	@ResponseBody
	public Object detailInfo(final String id) {
		final Map<String, Object> data = renderSuccessResult();
		final Realize realize = realizeService.findRealizeFromCache(id);
		data.put("realize", realize);
		data.putAll(realizeService.getDetailInfo(realize));
		// 比较时间使用
		data.put("systemTime", System.currentTimeMillis());
		if(realize!=null){
			data.put("protocolId",realize.getProtocolId());
		}
		return data;
	}

	/**
	 * 所持资产信息
	 * @author fxl
	 * @date 2016年7月27日
	 * @param id 变现投标ID
	 * @return
	 */
	@RequestMapping(value = "/realize/productInfo")
	@ResponseBody
	public Object productInfo(final String id) {
		return projectService.getProductInfo(id);
	}
	
	
	/**
	 * 产品简介
	 * @author fxl
	 * @date 2016年7月27日
	 * @param id 原始项目ID
	 * @return
	 */
	@RequestMapping(value = "/realize/productContent")
	@ResponseBody
	public Object productContent(final String id) {
		final Map<String,Object> data = renderSuccessResult();
		data.put("content", productService.getOrgProduct(id));
		return data;
	}
	
	/**
	 * 产品特点 
	 * @author fxl
	 * @date 2016年7月27日
	 * @param id 原始项目ID
	 * @return
	 */
	@RequestMapping(value = "/realize/productTrait")
	@ResponseBody
	public Object productTrait(final String id) {
		final Map<String,Object> data = renderSuccessResult();
		data.put("content", projectTypeService.getOrgProjectFeatures(id));
		return data;
	}
	
	/**
	 * 变现投资
	 * @author fxl
	 * @date 2016年7月27日
	 * @param invest
	 * @param model
	 * @return
	 */
	//http://localhost/realize/doRealizeInvest.html?amount=100&projectId=b005e5d3922940698925ebd9021193e7
	@RequestMapping("/realize/doRealizeInvest")
	public String doRealizeInvest(final ProjectInvestModel invest,final HttpServletRequest request,final Model model ) {
		// 用户登录状态
		User user = (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
		if (user == null) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_NOT_LOGIN), BussinessException.TYPE_CLOSE);
		} else {
			invest.setUserId(user.getUuid());
		}
		invest.setUserId(user.getUuid());
		invest.setAddIp( IPUtil.getRemortIP(request) );
		invest.setInvestType(InvestEnum.INVEST_TYPE_WEB.getValue());
		invest.setInvestChannel(InvestEnum.INVEST_CHANNEL_PC.getValue());
		if(TokenUtils.validToken(request,TokenConstants.TOKEN_INVEST)){
	    	TokenUtils.clearToken(request, TokenConstants.TOKEN_INVEST);
	    }else{
	    	throw new BussinessException(ResourceUtils.get(ResourceConstant.REPEAT_SUBMIT_ERROR), BussinessException.TYPE_CLOSE);
	    }
		final UfxInvestModel ufxInvestModel = realizeInvestService.doRealizeInvest(invest);
		model.addAttribute("invest", ufxInvestModel);
		return ConfigUtils.getTppName()+"/invest";
	}

}
