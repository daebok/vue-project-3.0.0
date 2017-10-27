/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.mobile.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.account.domain.Account;
import com.rd.account.model.AccountQueryModel;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.dict.InvestEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TokenUtils;
import com.rd.ifaes.common.util.ip.IPUtil;
import com.rd.ifaes.common.util.resource.LoanResource;
import com.rd.ifaes.core.cache.InvestCache;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ProjectConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.operate.domain.UserRateCoupon;
import com.rd.ifaes.core.operate.domain.UserRedenvelope;
import com.rd.ifaes.core.operate.service.UserRateCouponService;
import com.rd.ifaes.core.operate.service.UserRedenvelopeService;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectType;
import com.rd.ifaes.core.project.domain.RealizeRule;
import com.rd.ifaes.core.project.model.ProjectInvestModel;
import com.rd.ifaes.core.project.model.ProjectInvestRecord;
import com.rd.ifaes.core.project.model.ProjectRecord;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.project.service.ProjectTypeService;
import com.rd.ifaes.core.project.service.RealizeRuleService;
import com.rd.ifaes.core.risk.service.LevelConfigService;
import com.rd.ifaes.core.tpp.model.jx.JxBidApplyModel;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.domain.UserIdentify;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserFreezeService;
import com.rd.ifaes.core.user.service.UserIdentifyService;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.mobile.common.AppException;
import com.rd.ifaes.mobile.common.AppResultCode;
import com.rd.ifaes.mobile.model.PagedData;
import com.rd.ifaes.mobile.model.project.projectIndexItemRecordModel;
import com.rd.ifaes.mobile.model.project.projectListItemModel;
import com.rd.ifaes.mobile.model.project.projectToInvestModel;
import com.rd.ifaes.mobile.model.project.projectitemTypeModel;

/**
 * 理财频道
 * 
 * @author FangJun
 * @date 2016年7月11日
 */
@Controller
public class InvestController extends WebController {
	@Resource
	private ProjectInvestService projectInvestService;
	@Resource
	private ProjectService projectService;
	@Resource
	private  ProjectTypeService projectTypeService;
	@Resource
	private AccountService accountService;
	@Resource
	private UserFreezeService userFreezeService;
	@Resource
	private transient RealizeRuleService realizeRuleService;
	/**用户红包业务处理*/
	@Resource
	private transient UserRedenvelopeService userRedenvelopeService;
	/**用户加息券业务处理*/
	@Resource
	private transient UserRateCouponService userRateCouponService;
	/**
	 * LevelConfigService业务
	 */
	@Resource
	private transient LevelConfigService levelConfigService;
	/**
	 * UserCacheService业务
	 */
	@Resource
	private transient UserCacheService userCacheService;
	/**
	 * 认证信息类
	 */
	@Resource
	private transient UserIdentifyService identifyService;
	@Resource
	private UserService userService;
	/**
	 * 移动端项目分类列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "app/open/invest/projectTypeList")
	@ResponseBody
	public Object projectTypeList(String parentId) {
		Map<String, Object> data = new HashMap<String, Object>();
		Object obj=null;
		try{
		parentId="7ca31c421ce34e3fb8d57208e42f409f";//固定写 理财频道的 uuid
		List<ProjectType> prtypelist=projectTypeService.findChilds(parentId);
		
		List<projectitemTypeModel> plist=new ArrayList<projectitemTypeModel>();
		for(ProjectType prjectType:prtypelist){
			projectitemTypeModel borrowModel = new projectitemTypeModel();
			borrowModel.setUuid(prjectType.getUuid());//类别的UUID
			borrowModel.setTypeName(prjectType.getTypeName());//分类名称
			borrowModel.setParentId(prjectType.getParentId());//父类id
			borrowModel.setProtocolId(prjectType.getProtocolId());//协议模板标识
			borrowModel.setFeatures(prjectType.getFeatures());//特点
			borrowModel.setSort(prjectType.getSort());//排序
			borrowModel.setDeleteFlag(prjectType.getDeleteFlag());//0 未删除，1 已删除
			borrowModel.setTypeLevel(prjectType.getTypeLevel());//层级(根目录为0层，最大2）
			plist.add(borrowModel);
		}
		/*projectitemTypeModel borrowModelr = new projectitemTypeModel();
		borrowModelr.setUuid("090d5d939784fe33aceff143ba1c198c");//类别的UUID  变现 固定 为 变现 的md5加密
		borrowModelr.setTypeName("变现");//分类名称
		plist.add(borrowModelr);*/
		projectitemTypeModel borrowModel = new projectitemTypeModel();
		borrowModel.setUuid("a31bd335e12ac0dced8849a16fd4a894");//类别的UUID  转让专区 固定 为 转让专区 的md5加密
		borrowModel.setTypeName("转让专区");//分类名称
		plist.add(borrowModel);
      data.put("projectItemTypeList", plist);		
      obj=createSuccessAppResponse(data);
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
	}
	

	/**
	 * 移动端 投资项目列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "app/open/invest/projectList")
	@ResponseBody
	public Object projectList(final ProjectRecord project, HttpServletRequest request) {
		Map<String, Object> data = new HashMap<String, Object>();
		Object obj=null;
		try{
			project.setUserId(null);
			PagedData<projectIndexItemRecordModel> pirlist=new PagedData<projectIndexItemRecordModel>();
		if (LOGGER.isDebugEnabled()) {
			final StringBuilder buffer = new StringBuilder();
			buffer.append("产品列表查询: projectTypeId:").append(project.getProjectTypeId())
			.append(",amountCondition:").append(project.getAmountCondition())
			.append(",timeCondition:").append(project.getTimeCondition())
			.append(",repayStyle:").append(project.getRepayStyle());
			if (project.getPage() != null) {
				buffer.append(",sort:").append(project.getPage().getSort()).append(",order:").append(project.getPage().getOrder());
			}
			LOGGER.debug(buffer.toString());
		}
		project.setRealizeFlag(CommonEnum.NO.getValue());
		
		String userAgent = request.getHeader("User-Agent").toLowerCase();//判断访问来源 是否是微信浏览器
		if("android".equals(userAgent)||"iphone".equals(userAgent)){//安卓
		project.setSaleChannel(InvestEnum.INVEST_CHANNEL_APP.getValue());
		}else{
		project.setSaleChannel(InvestEnum.INVEST_CHANNEL_WECHAT.getValue());
		}
		//开售倒计时使用
		data.put("systemTime", System.currentTimeMillis());
		if(project.getPage()==null){
			project.setPage(new Page<ProjectRecord>());
		}
		int page=project.getPage().getPage();
		
		//定向销售类型是定向用户的标的登录与未登录的需要分开查看，解决用户看到其他用户的缓存数据（将列表缓存数据根据用户类型分为三类）
		User user = (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
		if (user != null) {
			user = userService.get(user.getUuid());//那最新的账号值，避免session中存储的值不是最新的
			//用户已经登录
			if(StringUtils.isBlank(user.getAccountType())){
				project.setUserAccountType(User.ACCOUNT_TYPE_NULL);
			}
			project.setUserAccountType(user.getAccountType());
		}else{
			project.setUserAccountType(User.ACCOUNT_TYPE_NULL);
		}
		Page<ProjectRecord> projectRecore=projectService.findProjectPage(project);
		projectRecore.setPage(page);
		List<ProjectRecord>  precordlist=projectRecore.getRows();
		fillPageData(pirlist, projectRecore);
		Map<String ,Object> remainmap=null;
		projectIndexItemRecordModel model=null;
		for(ProjectRecord projectRecord:precordlist){
			 model=new projectIndexItemRecordModel();
			model.setProjectId(projectRecord.getUuid());/* 项目id */
			model.setAccount(projectRecord.getAccount());/* 项目总额 */
			model.setMostAccount(projectRecord.getAccount());/* 最高投资总额 */
			model.setSaleTime(projectRecord.getSaleTime());// 开售时间 
			if (null != projectRecord.getSaleTime() && projectRecord.getSaleTime().after(new Date())) {
				model.setIfSale("0");
			}else{
				model.setIfSale("1");//已经开售
			}
			model.setProjectName(projectRecord.getProjectTypeName()+"-"+projectRecord.getProjectNo());/* 项目名称 */
			model.setApr(projectRecord.getApr());/* 年利率 */
			model.setAddApr(projectRecord.getAddApr());//加息
			model.setTimeType(projectRecord.getTimeType());/* 借款期限类型： 0月标 1天标 */
			model.setTimeLimit(projectRecord.getTimeLimit());/* 借款期限 */
			model.setLowestAccount(projectRecord.getLowestAccount());/* 最低起投金额 */
			
			remainmap=projectService.getRemainAccount(projectRecord.getUuid());
			Iterator<String> it = remainmap.keySet().iterator();
			while (it.hasNext()) {
				Object key = it.next();
				if ("remainAccount".equals(key))
					model.setRemainAccount((Double) remainmap.get(key));//剩余可投金额					
			}
			if(Integer.parseInt(projectRecord.getStatus())>4){
				model.setRemainAccount(0.0);
			}
			model.setNovice(projectRecord.getNovice());/*  新手标标识： 1新手专享 0 普通 ，默认：0 */
			model.setSpecificSale(projectRecord.getSpecificSale());/* 定向销售方式(0 不定向 1 定向密码 2 定向等级 3 定向邮箱域名，默认0) */
			model.setSpecificSaleRule(projectRecord.getSpecificSaleRule()==null?"0":projectRecord.getSpecificSaleRule());/*定向销售规则（对应规则内容,如：具体定向密码、定向邮箱域名）*/
			model.setRealizeUseful(projectRecord.getRealizeUseful());/* 是否可变现: 1 可变现 0 不可变现，默认 0 */
			model.setBondUseful(projectRecord.getBondUseful());/* 是否可以债权转让 1 可以，0不可以 */
			model.setTimeNow(DateUtils.getNow());//当前时间
			
			pirlist.getList().add(model);
		}
		obj=createSuccessAppResponse(pirlist);
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
	}
	/**移动端
	 * 指定产品的投资记录
	 * 
	 * @return 投资记录列表
	 */
	@RequestMapping("/app/member/invest/recordList")
	@ResponseBody
	public Object recordList(ProjectInvestRecord projectInvestmodel,HttpServletRequest request) {
		Object obj=null;
			try{
				@SuppressWarnings("unused")
				User user = getAppSessionUser(request);
				PagedData<projectListItemModel> projectinvestlist=new PagedData<projectListItemModel>();
				 Page<ProjectInvestRecord> projectInvestRecordPage =projectInvestService.findPageForDetail(projectInvestmodel);
				 projectListItemModel model=null;
				 List<ProjectInvestRecord>  projectInvestRecordList=projectInvestRecordPage.getRows();
				 fillPageData(projectinvestlist, projectInvestRecordPage);
				for(ProjectInvestRecord projectInvestRecord:projectInvestRecordList){
					 model=new projectListItemModel();
					model.setUserName(projectInvestRecord.getUserName());//投资人马沙克用户名
					model.setMoney(projectInvestRecord.getMoney());//投资金额
					model.setCreateTime(projectInvestRecord.getCreateTime());//时间
					model.setInvestChannel(projectInvestRecord.getInvestChannel());///* 投资渠道 1 PC 2 APP 3 微信 */
					model.setRedenvelopeAmount(projectInvestRecord.getRedenvelopeAmount());//使用红包总金额（有效投资金额-用户实付资金）
					model.setRateCouponVal(projectInvestRecord.getRateCouponVal());//加息劵数值
					projectinvestlist.getList().add(model);
				}
				obj=createSuccessAppResponse(projectinvestlist);
			} catch (Exception e) {
				obj=dealException(e);
			}
			return obj;
	}

	/**
	 * 投资订单确认初始化
	 * @author FangJun
	 */
	@RequestMapping("/app/member/invest/toInvest")
	@ResponseBody
	public Object toInvest(final String projectId,HttpServletRequest request,String investType) throws Exception {
		Object obj=null;
		try {
			User user = getAppSessionUser(request);
			user = userService.get(user.getUuid());//更新user
			//更新缓存
			SessionUtils.setSessionAttr(Constant.SESSION_USER, user);
				if("invest".equals(investType)){
					mobileCheckFreeze(user.getUuid(),"invest");
				}else if("realize".equals(investType)){
					mobileCheckFreeze(user.getUuid(),"realize");
				}else{
				throw new AppException(AppResultCode.ERROR, "投资类型参数不能为空");
			   }
			
			projectToInvestModel model=new projectToInvestModel();
			 UserIdentify userIdentify = identifyService.findByUserId(user.getUuid());
			 UserCache userCache=userCacheService.findByUserId(user.getUuid());
			 int notice=0;
			 String pnotice="0";
			 String specificSale="";//定向标识
			 String specificSaleRule="";//定向规则
			 String puserid="";//定向规则
			 Map<String ,Object> remainmap=null;
				if (StringUtils.isNotBlank(projectId)) {
					if (user != null) {
					 notice=userCache.getInvestNum()==null ? Constant.INT_ONE : (userCache.getInvestNum()>Constant.INT_ZERO ?Constant.INT_ZERO:Constant.INT_ONE);//novice 为0 当前用户新手标识
						}
					if(!"1".equals(userIdentify.getRealNameStatus())){throw new AppException(AppResultCode.USER_PAYMENT, "请先开通托管账户！");}
					if("".equals(userCache.getRiskLevel())||userCache.getRiskLevel()==null){throw new AppException(AppResultCode.USER_RISK, "请先进行风险评测！"); }
				   // if(!"1".equals(userIdentify.getEmailStatus())){ throw new AppException(AppResultCode.USER_EMAIL, "请先绑定邮箱！");}
					Map<String ,Object> prejectmap=projectService.getDetail(projectId,user.getUuid());
					Iterator<String> it=prejectmap.keySet().iterator();
					ProjectInvestModel invest=new ProjectInvestModel();
					invest.setProjectId(projectId);
					invest.setUserId(user.getUuid());
					// 获得资金详情                                                    
					Account account = accountService.getByUserId(new AccountQueryModel(user.getUuid(), ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE)));
					model.setUserMoney(account.getUseMoney());
					model.setDaysOfYear(ConfigUtils.getValue("days_of_year"));//一年？天
					while(it.hasNext()){
						Object key=it.next();
						if("project".equals(key)){
							Project project=(Project)prejectmap.get(key);
							model.setRedEnvelopeRate(project.getRedEnvelopeRate());// 红包可用最大比例 
							model.setApr(project.getApr()); //年利率 
							model.setTimeLimit(project.getTimeLimit());//借款期限
							model.setLowestAccount(project.getLowestAccount());// 最低起投金额 
							model.setLowestCopies(project.getLowestCopies());// 最低起投份数 
							model.setTotalCopies(project.getTotalCopies()); // 总份数 
							model.setCopyAccount(project.getCopyAccount()); //每份金额 
							model.setMostAccount(project.getMostAccount()); //最高投资总额 
							model.setProtocolId(project.getProtocolId());//协议id
						if("realize".equals(investType)){
							model.setProjectName("变现-"+project.getProjectTypeName()+"-"+project.getProjectNo());/* 项目名称 */
						}else{
							model.setProjectName(project.getProjectTypeName()+"-"+project.getProjectNo());/* 项目名称 */
						}
							model.setMostCopies(project.getMostCopies()); //最高投资份数 
							model.setAddapr(project.getAddApr());//加息利率
							model.setAdditionalRateUseful("".equals(project.getAdditionalRateUseful())||project.getAdditionalRateUseful()==null?"0":project.getAdditionalRateUseful());/* 加息劵可用标识:1可用 0不可用 */
							model.setRedEnvelopeUseful("".equals(project.getRedEnvelopeUseful())||project.getRedEnvelopeUseful()==null?"0":project.getRedEnvelopeUseful());/* 红包可用标识: 1 红包可用 0 红包不可用 */
						    remainmap=projectService.getRemainAccount(projectId);
							Iterator<String> itr = remainmap.keySet().iterator();
							while (itr.hasNext()) {
								Object keyr = itr.next();
								if ("remainAccount".equals(keyr))
									model.setRemainAccount((Double) remainmap.get(keyr));//剩余可投金额					
							}
							model.setAccount(project.getAccount());//项目总额
							model.setSaleStyle(project.getSaleStyle());/* 销售规则 ：1 按金额 2 按份数，默认1 */
							model.setStepAccount(project.getStepAccount());//递增金额：整数、以元为单位
							invest.setAmount(project.getAccount());
							model.setTotalInterest(projectInvestService.calcInterest(invest));//项目总额总收益
						
							model.setUserCanInvestAmount(project.getMostAccount().subtract(projectInvestService.countUserInvestProject(invest)));//当前用户可投 
							if(model.getUserCanInvestAmount().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()>model.getRemainAccount()){
								model.setUserCanInvestAmount(new BigDecimal(model.getRemainAccount()));
							}
							model.setOrderEnableTime(ConfigUtils.getInt(ConfigConstant.INVEST_TIMEOUT,ConfigConstant.INVEST_DEFAULT_ORDER_TIME_OUT)-1+"分59秒");//订单有效时间
							model.setRepayStyle(project.getRepayStyle());//1 等额本息2一次性还款3每月还息到期还本4等额本金5每季还息到期还本
							pnotice=project.getNovice();
							specificSale=project.getSpecificSale();
							specificSaleRule=project.getSpecificSaleRule();
							puserid=project.getUserId();
						}
					}
					 if(user.getUuid().equals(puserid)){//是否借款发布方
							throw new AppException(AppResultCode.ERROR, "当前标是您借款项目，无法投资！！");
						}
					 if("1".equals(pnotice)){//是否可投新手项目  如果是新手标
							if(notice==0)
								throw new AppException(AppResultCode.ERROR, "该产品仅新手可投资！");
						}
					   if(specificSaleRule!=null&&("2".equals(specificSale)||"3".equals(specificSale))){//是否定向等级 ,邮箱 
								throw new AppException(AppResultCode.ERROR, specificSaleRule);
						}
					model.setInvestToken(TokenUtils.setToken(request.getSession(), TokenConstants.TOKEN_INVEST));//防重复标识
					 if("realize".equals(investType)){
							 RealizeRule rule = realizeRuleService.getRule();
							 if(rule!=null) model.setBuyStyle((rule.getBuyStyle()==null?"0":rule.getBuyStyle()));
						}else{
							model.setBuyStyle("0");
						}
					 
					 /*查是否有可用优惠券 start*/
					 UserRedenvelope userRedenvelope=new UserRedenvelope();
						userRedenvelope.setUserId(user.getUuid());
						  Page<UserRedenvelope> pageu=new Page<UserRedenvelope>();
							pageu.setPage(1);
							userRedenvelope.setPage(pageu);  
					 Project project=projectService.get(projectId);
					 final List<UserRedenvelope>  availableRedList=userRedenvelopeService.findAvailableRedenvelope(userRedenvelope, project, project.getAccount());
					 if(availableRedList.size()>0){
						 model.setIfRedEnvelopestatus("1");//有可用红包
					 }else{
						 model.setIfAdditionalRate("0");//无可用红包
					 }
					 
					 UserRateCoupon userRateCoupon=new UserRateCoupon();
					 userRateCoupon.setUserId(user.getUuid());
						final Page<UserRateCoupon> page=new Page<UserRateCoupon>();
						page.setPage(1);
						userRateCoupon.setPage(page);
						final List<UserRateCoupon>  list=userRateCouponService.findAvailableRateCoupon(userRateCoupon, project, project.getAccount());
						 if(list.size()>0){
							 model.setIfAdditionalRate("1");//有可用加息券
						 }else{
							 model.setIfAdditionalRate("0");//无可用加息券
						 }
					 /*查是否有可用优惠券 end*/
					 Project projectmodel = new Project();
					 projectmodel.setUuid(projectId);
					 projectmodel.setStatus(LoanEnum.STATUS_RAISING.getValue());
						//项目状态校验
						if(projectService.getCount(projectmodel) == Constant.INT_ZERO){
							throw new BussinessException(ResourceUtils.get(LoanResource.PROJECT_STATUS_NEQ_RAISING), BussinessException.TYPE_CLOSE);
						}
						
						// 未支付订单数校验
						if (!InvestCache.checkUserInvestUnpay(user.getTppUserCustId())) {
							throw new AppException(AppResultCode.ERROR_BACK, StringUtils.format(ResourceUtils.get(LoanResource.INVEST_UNPAY_TOO_MUCH), ConfigUtils.getValue(ConfigConstant.INVEST_UNPAY_MAX)));
						} 
				}else{
					throw new AppException(AppResultCode.ERROR, "项目id不能为空");
				}
				obj=createSuccessAppResponse(model);
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
	}
	/**
	 * 投资订单确认，新建投资记录，跳转三方支付页面
	 * @author FangJun
	 * @date 2016年7月23日
	 * @param invest 投资信息
	 * @param model
	 * @return UFX投资报文Form页面
	 */
	@RequestMapping("/app/member/invest/doInvest")
	@TokenValid(value = TokenConstants.TOKEN_INVEST, dispatch = true)
	public String doInvest(ProjectInvestModel invest, Model model,HttpServletRequest request) {
		try {
			// 用户登录状态
			User user = getAppSessionUser(request);
			invest.setAddIp(IPUtil.getRemortIP(request));
			// 投资方式和投资渠道，需要在WEB层设置
			invest.setInvestType(InvestEnum.INVEST_TYPE_WEB.getValue());

			String userAgent = request.getHeader("User-Agent").toLowerCase();// 判断访问来源
																				// 是否是微信浏览器
			if ("android".equals(userAgent) || "iphone".equals(userAgent)) {// app端
				invest.setInvestChannel(InvestEnum.INVEST_CHANNEL_APP.getValue());
			} else {// 微信端
				invest.setInvestChannel(InvestEnum.INVEST_CHANNEL_WECHAT.getValue());
			}
			if (user == null) {
				throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_NOT_LOGIN), BussinessException.TYPE_CLOSE);
			} else {
				invest.setUserId(user.getUuid());
			}
			// 是否校验过定向密码
			if (request.getSession().getAttribute(
					String.format(ProjectConstant.PROJECT_PASS_RIGHT_PROJECT_ID, invest.getProjectId())) != null) {
				invest.setPwdHasVerify(true);
			}
			if (TokenUtils.validToken(request, TokenConstants.TOKEN_INVEST)) {
				TokenUtils.clearToken(request, TokenConstants.TOKEN_INVEST);
			} else {
				throw new BussinessException(ResourceUtils.get(ResourceConstant.REPEAT_SUBMIT_ERROR),
						BussinessException.TYPE_CLOSE);
			}
			JxBidApplyModel jxmodel = (JxBidApplyModel) projectInvestService.doInvest(invest);
            model.addAttribute("model", jxmodel);
            model.addAttribute("sign", jxmodel.getSign());
			return ConfigUtils.getTppName() + "/invest";
		} catch (Exception e) {
			model.addAttribute("r_msg", e.getMessage());
			return ConfigUtils.getTppName() + "/retresult";
		}
	}
	/**移动端
	 * 计算投资预计收益
	 * @param ProjectInvestModel 投资信息：投资项目ID、投资金额
	 * @return 预计收益
	 */
	@RequestMapping(value = "/app/member/invest/interest",method=RequestMethod.POST)
	@ResponseBody
	public Object interest(ProjectInvestModel invest,HttpServletRequest request) {
		Map<String, Object> data = new HashMap<String, Object>();
		Object obj=null;
		try{
			User user = getAppSessionUser(request);
			data.put("interest", projectInvestService.calcInterest(invest));
			if (user != null) {
				invest.setUserId(user.getUuid());
				data.put("userInvestAmount", projectInvestService.countUserInvestProject(invest));
			}
			obj=createSuccessAppResponse(data);
	} catch (Exception e) {
		obj=dealException(e);
	}
	return obj;
}

}
