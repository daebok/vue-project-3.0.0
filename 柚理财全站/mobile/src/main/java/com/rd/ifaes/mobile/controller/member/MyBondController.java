package com.rd.ifaes.mobile.controller.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.dict.BondEnum;
import com.rd.ifaes.common.dict.BondInvestEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TokenUtils;
import com.rd.ifaes.common.util.ip.IPUtil;
import com.rd.ifaes.common.util.resource.BondResource;
import com.rd.ifaes.core.bond.domain.Bond;
import com.rd.ifaes.core.bond.domain.BondInvest;
import com.rd.ifaes.core.bond.model.BondInvestModel;
import com.rd.ifaes.core.bond.model.BondModel;
import com.rd.ifaes.core.bond.model.BondRuleModel;
import com.rd.ifaes.core.bond.service.BondInvestService;
import com.rd.ifaes.core.bond.service.BondRuleService;
import com.rd.ifaes.core.bond.service.BondService;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.URLConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.project.domain.ProjectCollection;
import com.rd.ifaes.core.project.model.ProjectInvestModel;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.protocol.service.ProtocolService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.mobile.controller.WebController;
import com.rd.ifaes.mobile.model.PagedData;
import com.rd.ifaes.mobile.model.account.BondItemModel;
import com.rd.ifaes.mobile.model.account.ProjectToBondModel;
import com.rd.ifaes.mobile.model.account.log.BondInvestLogModel;

/**
 * 我的资产 --- 债权转让
 * 
 * @version 3.0
 * @author xiaodingjiang
 * @date 2016年10月21日
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
	private static final String STATUS_COMPLETE = BondEnum.STATUS_COMPLETE
			.getValue();
	/**
	 * 转让中
	 */
	private static final String STATUS_PUBLISH = BondEnum.STATUS_PUBLISH
			.getValue();
	/**
	 * 债权投资成功
	 */
	private static final String INVEST_SUCCESS = BondInvestEnum.STATUS_COMPLETE
			.getValue();

	/**
	 * 债权投资待支付
	 */
	private static final String INVEST_INIT = BondInvestEnum.STATUS_INIT
			.getValue();
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
	 * 债权规则业务类
	 */
	@Resource
	private transient BondRuleService bondRuleService;
	/**
	 * 进入账户中心 -- 我的理财--债权转让 四个列表空白页
	 * 
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @return
	 */
	@RequestMapping("/member/myBond/list")
	public String list() {
		return "/member/myBond/list";
	}

	/**
	 * 移动端----可转让列表
	 * 
	 * @author xiaodingjiang
	 * @date 2016年10月21日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/app/member/myBond/ableBondList")
	@ResponseBody
	public Object ableBondList(final ProjectInvestModel model,HttpServletRequest request) {
		Object obj=null;
		try {
			// 获取当前用户
			User user = getAppSessionUser(request);
			int page=model.getPage().getPage();
			Page<ProjectInvestModel> pages=new Page<ProjectInvestModel>();
			model.setUserId(user.getUuid());
			pages = projectInvestService
					.findAbleBondList(model);
			List<ProjectInvestModel> ableBondList = pages.getRows();
			PagedData<ProjectToBondModel> pirlist=new PagedData<ProjectToBondModel>();
			if(ableBondList!=null){
				pages.setPageSize(pages.getRows().size());
				pages.setPage(page);
				fillPageDatas(pirlist, pages);
			for(ProjectInvestModel projectInvest:ableBondList){
				ProjectToBondModel projectToBond=new ProjectToBondModel();
				projectToBond.setAmount(projectInvest.getAmount());// 有效投资金额(可转让金额)
				projectToBond.setApr(projectInvest.getApr());//原借款标预期年化
				projectToBond.setInterestTime(projectInvest.getInterestTime());//计息时间 
				projectToBond.setLastRepayTime(projectInvest.getLastRepayTime());// 最后还款时间
				projectToBond.setProjectName(projectInvest.getProjectName());// 项目名称
				projectToBond.setProjectRealRemainDays(projectInvest.getRemainDays());//剩余期限（当前时间到最后的还款日中间的时间天数）
				projectToBond.setProjectTotalRemainDays(projectInvest.getProjectTotalRemainDays());// 总期限（计息日到最后的还款日中间的时间天数）
				projectToBond.setPwdHasVerify(projectInvest.getPwdHasVerify());// 否校验过定向密码
				projectToBond.setRepayedAmount(projectInvest.getRepayedAmount());// 已还金额
				projectToBond.setRepayedInterest(projectInvest.getRepayedInterest());//已还利息
				projectToBond.setUuid(projectInvest.getUuid());//投资项目uuid（用于转让操作id）
				projectToBond.setNextRepayDate(projectInvest.getNextRepayTime());//本期回款时间
				projectToBond.setProjectId(projectInvest.getProjectId());//项目id
				projectToBond.setRaiseInterest(projectInvest.getRaiseInterest());//加息利息
				pirlist.getList().add(projectToBond);
			}}	
			obj=createSuccessAppResponse(pirlist);
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
	}

	/**
	 * 移动端 ---- 转让中列表
	 * 
	 * @author xiaodingjiang
	 * @date 2016年10月21日
	 * @param model
	 * @return
	 */
	@RequestMapping("/app/member/myBond/sellingBondList")
	@ResponseBody
	public Object sellingBondList(final BondModel model,HttpServletRequest request) {
		Object obj=null;
		try {
			// 获取当前用户
			User user = getAppSessionUser(request);
			int page=model.getPage().getPage();
			Page<BondModel> pages=new Page<BondModel>();
			model.setUserId(user.getUuid());//涉及到当前用户的尽量去缓存中的用户  
			model.setStatus(STATUS_PUBLISH);//转让中
			model.getPage().setSort(BOND_SORT);//按照转让成功时间逆序排列
			model.getPage().setOrder(BOND_ORDER);
			pages = bondService.findModelPage(model);
			List<BondModel> sellingBondList = pages.getRows();
			PagedData<BondItemModel> pirlist=new PagedData<BondItemModel>();
			if(sellingBondList!=null){
				pages.setPage(page);
				pages.setPageSize(pages.getRows().size());
				fillPageDatas(pirlist, pages);
			for(BondModel bondModel:sellingBondList){
				BondItemModel bondItemModel=new BondItemModel();
				bondItemModel.setApr(bondModel.getApr());//原资产预期年化(预期年化收益率)
				bondItemModel.setBondApr(bondModel.getBondApr());//折溢价率  
				bondItemModel.setBondEndTime(bondModel.getBondEndTime());// 债权截止日期
				bondItemModel.setBondMoney(bondModel.getBondMoney());//债权金额
				bondItemModel.setBondName(bondModel.getBondName());//债权名称
				bondItemModel.setBondNo(bondModel.getBondNo());//债权编号
				bondItemModel.setBondPrice(bondModel.getBondPrice());// 转让价格
				bondItemModel.setSuccessTime(bondModel.getBondSuccessTime());//转让成功时间
				bondItemModel.setCreateTime(bondModel.getCreateTime());//债权添加时间
				bondItemModel.setInvestId(bondModel.getInvestId());//投标id
				bondItemModel.setManageFee(bondModel.getManageFee());//手续费
				bondItemModel.setProjectId(bondModel.getProjectId());//项目ID
				bondItemModel.setProjectName(bondModel.getProjectName());//项目名称
				bondItemModel.setReceivedMoney(bondModel.getReceivedMoney());//实收金额
				bondItemModel.setUserId(bondModel.getUserId());//债权人
				bondItemModel.setUuid(bondModel.getUuid());//债权uuid（用于撤回操作id）
				pirlist.getList().add(bondItemModel);
			}
			}
			obj=createSuccessAppResponse(pirlist);
	
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
	}

	/**
	 * 移动端 ---- 已转让列表
	 * 
	 * @author xiaodingjiang
	 * @date 2016年10月21日
	 * @param bond
	 * @return
	 */
	@RequestMapping("/app/member/myBond/soldBondList")
	@ResponseBody
	public Object soldBondList(final BondModel model,HttpServletRequest request) {
		Object obj=null;
		try {
			// 获取当前用户
			User user = getAppSessionUser(request);
			int page=model.getPage().getPage();
			Page<BondModel> pages=new Page<BondModel>();
			model.setUserId(user.getUuid());
			model.setStatus(STATUS_COMPLETE);// 完成转让
			model.getPage().setSort(BOND_SORT);// 按照转让成功时间逆序排列
			model.getPage().setOrder(BOND_ORDER);
			pages = bondService.findModelPage(model);
			List<BondModel> soldBondList = pages.getRows();
			PagedData<BondItemModel> pirlist=new PagedData<BondItemModel>();
			if(soldBondList!=null){
				pages.setPage(page);
				pages.setPageSize(pages.getRows().size());
				fillPageDatas(pirlist, pages);
			for(BondModel bondModel:soldBondList){
				BondItemModel bondItemModel=new BondItemModel();
				bondItemModel.setApr(bondModel.getApr());//原资产预期年化(预期年化收益率)
				bondItemModel.setBondApr(bondModel.getBondApr());//折溢价率  
				bondItemModel.setBondEndTime(bondModel.getBondEndTime());// 债权截止日期
				bondItemModel.setBondMoney(bondModel.getBondMoney());//债权金额
				bondItemModel.setBondName(bondModel.getBondName());//债权名称
				bondItemModel.setBondNo(bondModel.getBondNo());//债权编号
				bondItemModel.setBondPrice(bondModel.getBondPrice());// 转让价格
				bondItemModel.setSuccessTime(bondModel.getSuccessTime());//转让成功时间
				bondItemModel.setCreateTime(bondModel.getCreateTime());//债权添加时间
				bondItemModel.setInvestId(bondModel.getInvestId());//投标id
				bondItemModel.setManageFee(bondModel.getManageFee());//手续费
				bondItemModel.setProjectId(bondModel.getProjectId());//项目ID
				bondItemModel.setProjectName(bondModel.getProjectName());//项目名称
				bondItemModel.setReceivedMoney(bondModel.getReceivedMoney());//实收金额
				bondItemModel.setUserId(bondModel.getUserId());//债权人
				bondItemModel.setUuid(bondModel.getUuid());//债权uuid（用于投资人债权协议操作）
				String resultStr = bondService.downloadAllBondProtocol(bondModel.getUuid());
				bondItemModel.setBondProtocolUrl(resultStr);//转让人债权协议下载url
				pirlist.getList().add(bondItemModel);
			}
			}
			obj=createSuccessAppResponse(pirlist);
		} catch (Exception e) {
			dealException(e);
		}
		return obj;
	}

	/**
	 * 移动端 ---- 已受让列表
	 * 
	 * @author xiaodingjiang
	 * @date 2016年10月21日
	 * @param model
	 * @return
	 */
	@RequestMapping("/app/member/myBond/boughtBondList")
	@ResponseBody
	public Object boughtBondList(BondInvestModel model,HttpServletRequest request) {
		Object obj=null;
		try {
			// 获取当前用户
			User user = getAppSessionUser(request);
			int page=model.getPage().getPage();
			model.setUserId(user.getUuid());
			model.setStatusSet(new String[] { INVEST_SUCCESS, INVEST_INIT });
			Page<BondInvestModel> pages = bondInvestService
					.findPageModel(model);//债权投资记录对应的原始标投资记录 
			List<BondInvestModel> boughtBondList = pages.getRows();
			PagedData<BondInvestLogModel> pirlist=new PagedData<BondInvestLogModel>();
			if(boughtBondList!=null){
				pages.setPage(page);
				pages.setPageSize(pages.getRows().size());
				fillPageDatas(pirlist, pages);
			for(BondInvestModel bondInvestModel:boughtBondList){
				BondInvestLogModel logModel=new BondInvestLogModel();
				logModel.setAddIp(bondInvestModel.getAddIp());//ip地址
				logModel.setAmount(bondInvestModel.getAmount());//债权本金
				logModel.setBondApr(bondInvestModel.getBondApr());//折溢价率
				logModel.setStatus(bondInvestModel.getStatus());//0 待支付  1 成功   3  超时
				logModel.setRemainTimes(bondInvestModel.getRemainTimes());//待支付的剩余时间 
				logModel.setBondName(bondInvestModel.getBondName());//债权名称
				logModel.setCreateTime(bondInvestModel.getCreateTime());//添加时间 
				logModel.setPaidMoney(bondInvestModel.getPaidMoney());//实付金额
			    logModel.setWaitMoney(bondInvestModel.getWaitMoney());//待收本息
			    logModel.setUuid(bondInvestModel.getUuid());//uuid
			    //String resultStr = bondService.downloadBondProtocol(bondInvestModel.getUuid());//（用于投资人债权协议操作）
			   // logModel.setBondProtocolUrl(resultStr);//投资人债权协议下载url
			    logModel.setProjectId(bondInvestModel.getProjectId());//项目id
			    logModel.setInvestId(bondInvestModel.getProjectInvestId());//InvestId（用于债权已受让列表中查询回款计划和转让操作）
			    logModel.setShowBond(bondInvestModel.isShowBond()==true?1:0);//是否可以转让
			    logModel.setInvestOrderNo(bondInvestModel.getInvestOrderNo());//债券投资订单号
			    logModel.setBondId(bondInvestModel.getBondId());//债券id
				pirlist.getList().add(logModel);
			}
			}
			obj=createSuccessAppResponse(pirlist);
			
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
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
	 * 移动端 债权转让 ---- 初始化
	 * 
	 * @author xiaodignjiang
	 * @date 2016年10月21日
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping("/app/member/myBond/toBondSet")
	@ResponseBody
	public Object setting(String id,HttpServletRequest request) {//可转让标的uuid
		final String backUrl = "";//返回链接地址
		Object obj=null;
		try {
		@SuppressWarnings("unused")
		User user=getAppSessionUser(request);
		Map<String,Object> data =  bondService.handleBondSet(id,backUrl);
		final BondRuleModel rule = bondRuleService.getRecentBondRuleModel();
		String tipTwo="";
			if(rule!=null){
				if("1".equals(rule.getFeeStyle())){/** 收费方式 0不收费 1固定金额 2 固定比例; 默认1 */
					 tipTwo="2.手续费将从转让后投资人支付的每笔成交额中扣除"+rule.getFeeFixed()+"元";	
				}else if("2".equals(rule.getFeeStyle())){
					tipTwo="2.手续费将从转让后投资人支付的每笔成交额中扣除"+rule.getFeeRate()+"%";	
				}
			}
			data.put("commitBondToken", TokenUtils.setToken(request.getSession(), TokenConstants.TOKEN_BOND_COMMIT));
			//温馨提示
			data.put("warmTips","1.转让成功后可赎回金额=转出债权本金x（1+折溢价率）+持有期限应收利息-应收利息的利息管理费-转让手续费"+"\n"+
            tipTwo+"\n"+
            "3.持有期限应收利息将由投资人投资时支付"+"\n"+
            "4.债权一旦发生转让，转让人将无法享受产品加息及加息券加息"+"\n"+
            "5.最高溢价率会随着转让人持有期限的增加而降低。转让过程中，当转让人设置的溢价率高于当日债权可设置的最高溢价率时，系统会自动把转让中债权的折溢价率改为当日债权可设置的最高溢价率的值");
			obj=createSuccessAppResponse(data);
		} catch (Exception e) {
			obj=dealExceptiontobond(e);
		}
		return obj;

	}

	/**
	 * 移动端 ---- 转让设置 提交 发布债权
	 * 
	 * @author xiaodingjiang
	 * @date 2016年10月21日
	 * @param bond
	 * @return
	 */
	@RequestMapping("/app/member/myBond/bondSetCommit")
	@TokenValid(value=TokenConstants.TOKEN_BOND_COMMIT,dispatch=true)
	@ResponseBody
	public Object bondSetCommit(final Bond bond,HttpServletRequest request) {
		Object obj=null;
		try {
			// 获取当前用户
			User user = getAppSessionUser(request);
			bond.setUserId(user.getUuid());
			//final String backUrl = URLConstant.BOND_SET_PAGE + bond.getUuid();
			String backUrl="";
			bondService.insert(bond,backUrl);
			obj=createSuccessAppResponse("债权发布成功");
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
	}

	/**
	 * 受让列表 点击去支付
	 * 
	 * @author QianPengZhan
	 * @date 2016年8月24日
	 * @param investOrderNo
	 * @return
	 */
	@RequestMapping("/app/member/myBond/doBondPay")
	public Object doBondPay(@RequestParam("investOrderNo") final String investOrderNo,
			final HttpServletRequest request ,final Model model) {
		try{
			// 用户登录状态
			User user = getAppSessionUser(request);
		LOGGER.info("移动端进入受让列表 点击去支付...start");
		//1、用户是否登录校验 
		final String backUrl = URLConstant.BOND_LIST_BOUGHT;
		String url = StringUtils.EMPTY;
		if(user == null){
			url =  URLConstant.LOGIN_HTML;
		}else{
			//2、根据旧的订单号 处理生成新的订单号  返回 新的订单号的 债权投资记录
			final BondInvest invest = bondInvestService.getBondInvestByOrderNo(investOrderNo);
			final String requestIp = IPUtil.getRemortIP(request) ;//投资获取IP
			invest.setAddIp(requestIp);//每次投资IP 都需要重置
			final BondInvestModel bondInvestModel = invest.instance();
			bondInvestModel.setIsSelectedTip(Constant.INT_ONE);
			bondInvestModel.setAddIp(IPUtil.getRemortIP(request));
			 model.addAttribute("ufxCreditModel", bondInvestService.doBondPay(bondInvestModel, user,backUrl));
			LOGGER.info("进入受让列表 点击去支付...end");
			url = ConfigUtils.getTppName()+"/doBondInvest";
		}
		return url;
		} catch (Exception e) {
			model.addAttribute("r_msg", e.getMessage());
			return ConfigUtils.getTppName()+"/retresult";
		}
	}

		/**
		 * 受让列表中获取某一笔受让的回款计划
		 * 
		 * @author QianPengZhan
		 * @date 2016年8月28日
		 * @return
		 */
		@SuppressWarnings("unchecked")
		@RequestMapping("/app/member/myBond/getRepayPlan")
		@ResponseBody
		public Object getRepayPlan(@RequestParam("bondInvestId") final String bondInvestId,HttpServletRequest request) {
			Object obj=null;
			List<ProjectCollection> showList = new ArrayList<ProjectCollection>();
			Map<String,Object> map;
			final String backUrl = "";
			try{
				@SuppressWarnings("unused")
				User user = getAppSessionUser(request);
			map=bondService.getRepayPlan(bondInvestId,backUrl);
			showList=(List<ProjectCollection>) map.get("list");
			obj=createSuccessAppResponse(showList);
			}catch(Exception e){
				obj=dealException(e);
			}
			return obj;
		}

	/**
	 * 移动端 债权转让 ---- 前台手动撤销债权标
	 * 
	 * @author xiaodingjiang
	 * @date 2016年10月21日
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping("/app/member/myBond/cancleBond")
	@ResponseBody
	public Object cancleBond(String id,HttpServletRequest request) {// 转让债权中标的uuid
	  Object obj=null;
	  final String backUrl = "";
	   try {
		   @SuppressWarnings("unused")
		User user = getAppSessionUser(request);
		  bondService.cancleBond(id,false,backUrl);
		  obj=createSuccessAppResponse("债权撤回成功");
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
		
	}

	/**
	 * 移动端--债权协议预览接口
	 * @author QianPengZhan
	 * @date 2016年9月29日
	 * @param protocolId
	 * @return
	 */
	@RequestMapping("/app/member/myBond/getBondProtocolContent")
	public Object getBondProtocolContent(final String protocolId,final String bondId,HttpServletRequest request,final Model model){
		try{
		LOGGER.info("移动端进入债权协议预览接口...start");
		User user = getAppSessionUser(request);
		Map<String,Object> prosermap=protocolService.getBondProtocol(user,  StringUtils.isNull(bondId), protocolId);
		Iterator<String> it = prosermap.keySet().iterator();
		while (it.hasNext()) {
			Object key = it.next();
			if ("content".equals(key))
				model.addAttribute("content", (String) prosermap.get(key));
		}
		model.addAttribute("titleName", "投资协议");
		return "/app/protocol/registerProtocol";
	} catch (Exception e) {
		model.addAttribute("r_msg", e.getMessage());
		return ConfigUtils.getTppName()+"/retresult";
	}
	}
	/**
	 * 移动端--债权协议预览接口
	 * @author QianPengZhan
	 * @date 2016年9月29日
	 * @param protocolId
	 * @return
	 */
	@RequestMapping("/app/member/myBond/wxGetBondProtocolContent")
	@ResponseBody
	public Object wxGetBondProtocolContent(final String protocolId,final String bondId,HttpServletRequest request,final Model model){
		Object obj=null;
		try{
			Map<String, Object> data = new HashMap<String, Object>();
		LOGGER.info("移动端进入债权协议预览接口...start");
		User user = getAppSessionUser(request);
		Map<String,Object> prosermap=protocolService.getBondProtocol(user,  StringUtils.isNull(bondId), protocolId);
		Iterator<String> it = prosermap.keySet().iterator();
		while (it.hasNext()) {
			Object key = it.next();
			if ("content".equals(key))
				data.put("content", (String) prosermap.get(key));
		}
		data.put("titleName", "转让协议");
		obj = createSuccessAppResponse(data);
		} catch (Exception e) {
			obj = dealException(e);
		}
		return obj;
	}
	
	/**
	 * 生成债权协议
	 * @author QianPengZhan
	 * @date 2016年9月29日
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/app/open/myBond/buildBondProtocol")
	@ResponseBody
	public Object buildBondProtocol(final String bondId,final String bondInvestId,
			final String type,final HttpServletResponse response,HttpServletRequest request, Model model) throws Exception{
		LOGGER.info("进入生成普通投资协议接口...start");
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
	 * 
	 * @author QianPengZhan
	 * @date 2016年9月29日
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/app/member/myBond/downloadBondInvestProtocol")
	public String downloadBondInvestProtocol(final String bondInvestId,final HttpServletResponse response)
			throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("进入投资人债权协议下载接口...start");
		}
		
		final BondInvest bondInvest = bondInvestService.get(bondInvestId);
		if(bondInvest == null){
			throw new BussinessException(ResourceUtils.get(BondResource.BOND_INVEST_NOT_EXISTS),BussinessException.TYPE_JSON);
		}
		final Bond bond = bondService.get(bondInvest.getBondId());
		if(bond == null){
			throw new BussinessException(ResourceUtils.get(BondResource.BOND_NOT_EXISTS),BussinessException.TYPE_JSON);
		}
		String fileName = bond.getBondName() +"_"+bondInvest.getInvestOrderNo() +".pdf";
		String url = "/upload/downloadBondContractMobile.html";
		String webUrl = ConfigUtils.getValue("mobile_url");
		final String resultStr = protocolService.downloadBondProtocol(bond.getUuid(),bondInvest.getUuid(), fileName,url,webUrl);
		
		if (StringUtils.isNotBlank(resultStr)) {
			response.sendRedirect(resultStr);
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("进入投资人债权协议下载接口...end");
			}
		} else {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("下载出错,地址：{}", resultStr);
			}
			throw new BussinessException("下载出错");
		}
		return null;
	}

	/**
	 * 转让人债权协议下载 .zip
	 * 
	 * @author QianPengZhan
	 * @date 2016年9月29日
	 * @return
	 * @throws Exception
	 */
	public String downloadBondProtocol(final String bondId,final HttpServletResponse response) throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("进入转让人债权协议下载  .zip接口...start");
		}
		final String resultStr = bondService.downloadAllBondProtocol(bondId);
		if (StringUtils.isNotBlank(resultStr)) {
			response.sendRedirect(resultStr);
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("进入转让人债权协议下载  .zip接口...end");
			}
		} else {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("下载出错,地址：{}", resultStr);
			}
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
	@RequestMapping("/app/member/myBond/downloadAllBondProtocol")
	public String downloadAllBondProtocol(final String bondId,final HttpServletResponse response) throws Exception {
		LOGGER.info("进入转让人债权协议下载  .zip接口...start");
		
		final Bond bond = bondService.get(bondId);
		if(bond == null){
			throw new BussinessException(ResourceUtils.get(BondResource.BOND_NOT_EXISTS),BussinessException.TYPE_JSON);
		}
		String fileName = bond.getBondName() +"_"+bond.getBondNo() +".zip";
		String url = "/upload/downloadBondContractMobile.html";
		String webUrl = ConfigUtils.getValue("mobile_url");
		final String resultStr= protocolService.downloadBondProtocol(bond.getUuid(),bond.getUuid(), fileName,url,webUrl);
		
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
