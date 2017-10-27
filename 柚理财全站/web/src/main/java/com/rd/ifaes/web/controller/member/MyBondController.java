/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.web.controller.member;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.dict.BondEnum;
import com.rd.ifaes.common.dict.BondInvestEnum;
import com.rd.ifaes.common.dict.InvestEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.ip.IPUtil;
import com.rd.ifaes.core.bond.domain.Bond;
import com.rd.ifaes.core.bond.domain.BondInvest;
import com.rd.ifaes.core.bond.model.BondInvestModel;
import com.rd.ifaes.core.bond.model.BondModel;
import com.rd.ifaes.core.bond.service.BondInvestService;
import com.rd.ifaes.core.bond.service.BondService;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.URLConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.project.model.ProjectInvestModel;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.protocol.service.ProtocolService;
import com.rd.ifaes.core.tpp.model.jx.JxCreditInvestModel;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.web.controller.WebController;

/**
 * 我的资产  --- 债权转让
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年7月26日
 */
@Controller
public class MyBondController extends WebController { 
	/**
	 * 排序列
	 */
	private static final String BOND_SORT = "p1.success_time";
	/**
	 * 排序方式
	 */
	private static final String BOND_ORDER = "desc";
	
	/**
	 * 转让成功
	 */
	private static final String STATUS_COMPLETE = BondEnum.STATUS_COMPLETE.getValue();
	/**
	 * 转让中
	 */
	private static final String STATUS_PUBLISH = BondEnum.STATUS_PUBLISH.getValue();
	/**
	 * 债权投资成功
	 */
	private static final String INVEST_SUCCESS = BondInvestEnum.STATUS_COMPLETE.getValue();
	
	/**
	 * 债权投资待支付
	 */
	private static final String INVEST_INIT = BondInvestEnum.STATUS_INIT.getValue();
	/**
	 * 债权发布成功返回的URL
	 */
	private static final String SUCCESS_URL = "/member/myBond/list.html";
	/**
	 * 债权取消成功返回的URL
	 */
	private static final String CANCLE_URL = "/member/myBond/list.html";
	
	/**
	 * 协议业务
	 */
	@Resource
	private transient ProtocolService protocolService;
	/**
	 * 项目投资业务
	 */
	@Resource
	private transient ProjectInvestService projectInvestService;
	/**
	 * 债权业务
	 */
	@Resource
	private transient BondService bondService;
	/**
	 * 债权投资业务
	 */
	@Resource
	private transient BondInvestService bondInvestService;
	
	/**
	 * 进入账户中心 -- 我的理财--债权转让 四个列表空白页
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @return
	 */
	@RequestMapping("/member/myBond/list")
	public String list(){
		return "/member/myBond/list";
	}
	
	
	/**
	 * 进入账户中心 -- 我的理财--债权转让  中   ----可转让列表
	 * @author QianPengZhan
	 * @date 2016年8月4日
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/member/myBond/ableBondList")
	@ResponseBody
	public Object ableBondList(final ProjectInvestModel model){
		LOGGER.info("进入可转让列表...start");
		//1、获取当前用户
		final User user = (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
		model.setUserId(user.getUuid());//涉及到当前用户的尽量去缓存中的用户  
		LOGGER.info("userId:{}",user.getUuid());
		final Page<ProjectInvestModel> page = projectInvestService.findAbleBondList(model);
		
		//2、 将数据返回前台
		final Map<String, Object> data = new HashMap<>();
		data.put(RESULT_NAME, true);
		data.put(DATA_NAME, page);
		LOGGER.info("进入可转让列表...end:"+data.toString());
		return data;
	}
	
	
	/**
	 * 进入账户中心 -- 我的理财--债权转让  中   ---- 转让中列表
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @param model
	 * @return
	 */
	@RequestMapping("/member/myBond/sellingBondList")
	@ResponseBody
	public Object sellingBondList(final BondModel model){
		LOGGER.info("进入转让中列表...start");
		//1、获取当前用户 
		final User user = (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
		model.setUserId(user.getUuid());//涉及到当前用户的尽量去缓存中的用户  
		model.setStatus(STATUS_PUBLISH);//转让中
		model.getPage().setSort(BOND_SORT);//按照转让成功时间逆序排列
		model.getPage().setOrder(BOND_ORDER);
		final Page<BondModel> page = bondService.findModelPage(model);
		
		//3、 将数据返回前台
		final Map<String, Object> data =  this.renderSuccessResult();
		data.put(DATA_NAME, page);
		LOGGER.info("进入转让中列表...end:{}",data.toString());
		return data;
	}
	
	/**
	 * 进入账户中心 -- 我的理财--债权转让  中   ---- 已转让列表
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @param bond
	 * @return
	 */
	@RequestMapping("/member/myBond/soldBondList")
	@ResponseBody
	public Object soldBondList(final BondModel bondModel){
		LOGGER.info("进入 已转让列表...start");
		//1、用户是否登录校验 
		final User user = (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
		bondModel.setUserId(user.getUuid());
		bondModel.setStatus(STATUS_COMPLETE);//完成转让
		bondModel.getPage().setSort(BOND_SORT);//按照转让成功时间逆序排列
		bondModel.getPage().setOrder(BOND_ORDER);
		final Page<BondModel> page = bondService.findModelPage(bondModel);
		
		//2、 将数据返回前台
		final Map<String, Object> data = new HashMap<>();
		data.put(RESULT_NAME, true);
		data.put(DATA_NAME, page);
		LOGGER.info("进入 已转让列表...end:{}",data.toString());
		return data;
	}
	
	
	/**
	 * 进入账户中心 -- 我的理财--债权转让  中   ----  已受让列表
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @param model
	 * @return
	 */
	@RequestMapping("/member/myBond/boughtBondList")
	@ResponseBody
	public Object boughtBondList(final BondInvestModel model){
		LOGGER.info("进入已受让列表...start");
		//1、用户是否登录校验 
		final User user = (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
		model.setUserId(user.getUuid());
		model.setStatusSet(new String[]{INVEST_SUCCESS,INVEST_INIT});
		final Page<BondInvestModel> page = bondInvestService.findPageModel(model);
		
		//2、 将数据返回前台
		final Map<String, Object> data = Maps.newHashMap();
		data.put(RESULT_NAME, true);
		data.put(DATA_NAME, page);
		LOGGER.info("进入已受让列表...end:{}",data.toString());
		return data;
	}
	
	
	/**
	 * 进入账户中心 -- 我的理财--债权转让  中   ----  转让设置界面
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping("/member/myBond/bondSetList")
	public String setting(final String id,final HttpServletRequest request ,final Model model) {
		LOGGER.info("进入转让设置界面...start");
		final String backUrl = URLConstant.BOND_LIST_ABLE ;//返回链接地址
		Map<String,Object> data =  bondService.handleBondSet(id,backUrl);
		setToken(TokenConstants.TOKEN_BOND_COMMIT,request);
		 model.addAttribute("data", data);
		LOGGER.info("进入转让设置界面...end:"+data.toString());
		return "/member/myBond/setting";
	}
	
	/**
	 * 进入账户中心 -- 我的理财--债权转让  中   ---- 转让设置 提交  发布债权
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @param bond
	 * @return
	 */
	@RequestMapping("/member/myBond/bondSetCommit")
	@ResponseBody	
	@TokenValid(value=TokenConstants.TOKEN_BOND_COMMIT,dispatch=true)
	public  Object bondSetCommit(final Bond bond){
		LOGGER.info("进入转让设置 提交  发布债权...start,invest_id={}",bond.getInvestId());
		//报错时候跳转的链接地址
		final String backUrl = URLConstant.BOND_SET_PAGE + bond.getUuid();
		bondService.insert(bond,backUrl);
		
		LOGGER.info("进入转让设置 提交  发布债权...end,invest_id={}",bond.getInvestId());
		return renderResult(true, MSG_SUCCESS,SUCCESS_URL);	 
	}
	
	
	/**
	 * 受让列表 点击去支付
	 * @author QianPengZhan
	 * @date 2016年8月24日
	 * @param investOrderNo
	 * @return
	 */
	@RequestMapping("/member/myBond/doBondPay")
	public Object doBondPay(@RequestParam("investOrderNo") final String investOrderNo,
			final HttpServletRequest request ,final Model model) {
		LOGGER.info("进入受让列表 点击去支付...start");
		//1、用户是否登录校验 
		final String backUrl = URLConstant.BOND_LIST_BOUGHT;
		String url = StringUtils.EMPTY;
		final User user = (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
		if(user == null){
			url =  URLConstant.LOGIN_HTML;
		}else{
			//2、根据旧的订单号 处理生成新的订单号  返回 新的订单号的 债权投资记录
			final BondInvest invest = bondInvestService.getBondInvestByOrderNo(investOrderNo);
			final BondInvestModel bondInvestModel = invest.instance();
			bondInvestModel.setIsSelectedTip(Constant.INT_ONE);
			bondInvestModel.setAddIp(IPUtil.getRemortIP(request));
			bondInvestModel.setInvestChannel(InvestEnum.INVEST_CHANNEL_PC.getValue());
			
			
            JxCreditInvestModel jxModel = (JxCreditInvestModel) bondInvestService.doBondPay(bondInvestModel, user ,backUrl);
            model.addAttribute("model", jxModel);
            model.addAttribute("sign", jxModel.getSign());
            url = ConfigUtils.getTppName() + "/bondInvest";
            LOGGER.info("进入受让列表 点击去支付...end");
		}
		return url;
	}
	
	/**
	 * 受让列表中获取某一笔受让的回款计划
	 * @author QianPengZhan
	 * @date 2016年8月28日
	 * @return
	 */
	@RequestMapping("/member/myBond/getRepayPlan")
	@ResponseBody
	public Object getRepayPlan(@RequestParam("bondInvestId") final String bondInvestId){
		final String backUrl = URLConstant.BOND_LIST_BOUGHT;
		return bondService.getRepayPlan(bondInvestId,backUrl);
	}
	
	/**
	 * 进入账户中心 -- 我的理财--债权转让  中   ---- 前台手动撤销债权标
	 * @author QianPengZhan
	 * @date 2016年8月4日
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping("/member/myBond/cancleBond")
	@ResponseBody
	public Object cancleBond(@RequestParam("id") final String id){
		LOGGER.info("进入前台手动撤销债权标...start");
		final String backUrl = URLConstant.BOND_LIST_SELLING;
		bondService.cancleBond(id,false,backUrl);
		LOGGER.info("进入前台手动撤销债权标...end");
		return  renderResult(true, MSG_SUCCESS,CANCLE_URL);	 	 
	}
	
	/**
	 * 债权协议预览接口
	 * @author QianPengZhan
	 * @date 2016年9月29日
	 * @param protocolId
	 * @return
	 */
	@RequestMapping("/member/myBond/getBondProtocolContent")
	@ResponseBody
	public Object getBondProtocolContent(final String protocolId,final String bondId){
		LOGGER.info("进入债权协议预览接口...start");
		final User user =  (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
		return protocolService.getBondProtocol(user,  StringUtils.isNull(bondId), protocolId);
	}
	
	/**
	 * 生成债权协议
	 * @author QianPengZhan
	 * @date 2016年9月29日
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/member/myBond/buildBondProtocol")
	@ResponseBody
	public Object buildBondProtocol(final String bondId,String bondInvestId,
			final String type,final HttpServletResponse response) throws Exception{
		LOGGER.info("进入生成普通投资协议接口...start");
		if(StringUtils.isBlank(bondInvestId)){
			bondInvestId = bondId;
		}
		bondService.handleAllBondProtocol(bondId,bondInvestId);
		if("zip".equals(type)){
			downloadAllBondProtocol(bondId,response);
		}else{
			downloadBondInvestProtocol(bondInvestId,response);
		}
		return renderResult(true, "");
	}
	
	/**
	 * 投资人债权协议下载
	 * @author QianPengZhan
	 * @date 2016年9月29日
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/member/myBond/downloadBondInvestProtocol")
	public String downloadBondInvestProtocol(final String bondInvestId,final HttpServletResponse response) throws Exception {
		LOGGER.info("进入投资人债权协议下载接口...start");
		final String resultStr = bondService.downloadBondProtocol(bondInvestId);
		if(StringUtils.isNotBlank(resultStr)){
			response.sendRedirect(resultStr);
			LOGGER.info("进入投资人债权协议下载接口...end");
		}else{
			LOGGER.error("下载出错,地址：{}",resultStr);
			throw new BussinessException("下载出错");
		}
		return null;
	}
	
	
	/**
	 * 转让人债权协议下载  .zip
	 * @author QianPengZhan
	 * @date 2016年9月29日
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/member/myBond/downloadAllBondProtocol")
	public String downloadAllBondProtocol(final String bondId,final HttpServletResponse response) throws Exception {
		LOGGER.info("进入转让人债权协议下载  .zip接口...start");
		final String resultStr = bondService.downloadAllBondProtocol(bondId);
		if(StringUtils.isNotBlank(resultStr)){
			response.sendRedirect(resultStr);
			LOGGER.info("进入转让人债权协议下载  .zip接口...end");
		}else{
			LOGGER.error("下载出错,地址：{}",resultStr);
			throw new BussinessException("下载出错");
		}
		return null;
	}
}
