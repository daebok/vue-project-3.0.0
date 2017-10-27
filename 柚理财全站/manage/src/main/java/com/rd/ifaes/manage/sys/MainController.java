package com.rd.ifaes.manage.sys;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.annotation.Session;
import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.dict.InvestEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.security.shiro.FormAuthenticationFilter;
import com.rd.ifaes.common.util.CommonConstants;
import com.rd.ifaes.common.util.CookieUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.NumberUtils;
import com.rd.ifaes.common.util.PrincipalUtils;
import com.rd.ifaes.common.util.PropertiesUtils;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TreeNode;
import com.rd.ifaes.common.util.validcode.ValidCodeMaker;
import com.rd.ifaes.core.account.model.CashModel;
import com.rd.ifaes.core.account.service.CashService;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.core.util.ValidateUtils;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.service.BorrowService;
import com.rd.ifaes.core.project.service.ProductService;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.project.service.ProjectRepaymentService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.sys.domain.Menu;
import com.rd.ifaes.core.sys.domain.Operator;
import com.rd.ifaes.core.sys.service.MenuService;
import com.rd.ifaes.core.sys.service.OperatorService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserQualificationApply;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserEarnLogService;
import com.rd.ifaes.core.user.service.UserQualificationApplyService;
import com.rd.ifaes.core.user.service.UserService;

@Controller
public class MainController extends SystemController {
	@Resource
	private UserService userService;
	@Resource
	private ProjectInvestService projectInvestService;
	@Resource
	private UserEarnLogService userEarnLogService;
	@Resource
	private ProjectService projectService;
	@Resource
	private ProductService productService;
	@Resource
	private BorrowService borrowService;
	@Resource
	private CashService cashService;
	@Resource
	private UserQualificationApplyService applyService;
	@Resource
	private MenuService menuService;
	@Resource
	private UserCacheService userCacheService;
	@Resource
	private OperatorService operatorService;
	@Resource
	private AccountService accountService;
	@Resource
	private ProjectRepaymentService projectRepaymentService;
	
	@RequestMapping("/index")
	@SystemLog(method=SysLogConstant.VIEW,content=SysLogConstant.INDEX)
	public String index(final Model model) {
		model.addAttribute("productName", PropertiesUtils.getValue("productName"));
		model.addAttribute("version", PropertiesUtils.getValue("version"));		
		handleWebName(model);
		return  "/index";
	}
	
	/**
	 * 处理网站标题
	 * @author QianPengZhan
	 * @date 2016年10月31日
	 */
	private void handleWebName(final Model model) {
		StringBuilder webBackName = new StringBuilder();
		webBackName.append("后台管理-").append(ConfigUtils.getValue(ConfigConstant.WEB_NAME));
		model.addAttribute("webBackName", webBackName.toString());//网站标题
	}
	
	@RequestMapping(value="/admin/login",method = RequestMethod.GET)
	@SystemLog(method=SysLogConstant.LOGIN,content=SysLogConstant.USER_LOGIN)
	public String loginPage(final Model model) {
		handleWebName(model);
		Object operator = PrincipalUtils.getPrincipal();
		if(operator != null){
			 return "redirect:/main.html";
		}	
		return  "/login";
	}
	
	@RequestMapping(value="/admin/login" , method = RequestMethod.POST)
	@SystemLog(method=SysLogConstant.LOGIN,content=SysLogConstant.USER_LOGIN)
	public String loginFail(HttpServletRequest request, HttpServletResponse response,Model model){ 
		Object operator = PrincipalUtils.getPrincipal();
		// 如果已经登录，则跳转到管理首页
		if(operator != null){
			 return "redirect:/main.html";
		}   
		
		String username = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
		boolean rememberMe = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM);
		//String exception = (String)request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		/*String message = (String)request.getAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM);
		
		if (StringUtils.isBlank(message) || StringUtils.equals(message, "null")){
			message = ResourceUtils.get("user.login.password.error");
		}else{
			LOGGER.warn(message);
		}*/
		//重复设置,导致异常，统一在FormAuthenticationFilter中处理
		//model.addAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM, message);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM, rememberMe);
		return  "/login";
	}
	
	/**
	 * 生成图片验证码
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/validImg")
	public void validimg(final HttpServletResponse response) throws Exception {
		try {
			
			ValidCodeMaker authCode = ValidCodeMaker.getInstance();
			// 取得随机字符串放入session
			SessionUtils.setSessionAttr(Constant.VALID_CODE, authCode.getString());
			// 设置验证码输出格式
			response.setContentType("image/jpeg");
			response.setCharacterEncoding("utf-8");
			OutputStream output = new BufferedOutputStream(response.getOutputStream());
			InputStream in = authCode.getImage();
			int len = Constant.INT_ZERO;
			byte[] buf = new byte[1024];
			while ((len = in.read(buf, 0, 1024)) != -1) {
				output.write(buf, 0, len);
			}
			response.flushBuffer();
			output.flush();
			in.close();
			output.close();
		} catch (IOException e) {
			LOGGER.error("build validCode error.", e);
		}
	}
	
	/**
	 * 异步校验图片验证码
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/validLogonCode")
	@ResponseBody
	public Object valicode(String validCode) throws Exception {
		final Map<String, Object> data = this.renderSuccessResult();
		if (StringUtils.isBlank(validCode) || !ValidateUtils.checkValidCode(validCode)) {// 不通过
			throw new BussinessException(ResourceUtils.get(ResourceConstant.VALID_CODE_ERROR), BussinessException.TYPE_JSON);
		}
		SessionUtils.setSessionAttr(Constant.VALID_CODE, validCode);

		return data;
	}
	/**
	 * 登录成功，进入管理首页
	 */
	@Session
	@RequestMapping(value = "/main")
	@SystemLog(method=SysLogConstant.LOGIN,content=SysLogConstant.USER_LOGIN)
	public String main(HttpServletRequest request, HttpServletResponse response,final Model model)throws Exception {
		
	 	Operator user = (Operator) PrincipalUtils.getPrincipal();
	 	
		// 登录成功后，验证码计算器清零
		// 如果已登录，再次访问主页，则退出原账号。
		if (CommonConstants.TRUE.equals(PropertiesUtils.getValue("notAllowRefreshIndex"))){
			String logined = CookieUtils.getCookie(request, "LOGINED");
			if (StringUtils.isBlank(logined) || "false".equals(logined)){
				CookieUtils.setCookie(response, "LOGINED", "true");
			}else if (StringUtils.equals(logined, "true")){
				SecurityUtils.getSubject().logout();
				return "redirect:admin/login";
			}
		}
		 model.addAttribute("loginUser", user);
		//查询顶层节点
		List<Menu> modelList = menuService.findAuthModel(user.getUuid());
		//过滤权限
		List<TreeNode> treeNodeList = menuService.findAuthMenuList(user.getUuid(), Constant.DEFAULT_INDEX_MENU_ID);
		
		Map<String,Object> menuMap = new HashMap<String,Object>();
		for(Menu menu : modelList){
			List<TreeNode> list = menuService.findAuthMenuList(user.getUuid(), menu.getUuid());
			menuMap.put(menu.getUuid(), list);
		}
		
		ObjectMapper mapper = new ObjectMapper();
		model.addAttribute("defaultSelected", Constant.DEFAULT_INDEX_MENU_ID);
		model.addAttribute("authModel", mapper.writeValueAsString(modelList));
		model.addAttribute("authMenu", mapper.writeValueAsString(treeNodeList));
		model.addAttribute("menuMap", mapper.writeValueAsString(menuMap));
		
		request.getSession(true).setAttribute("image_server_url", ConfigUtils.getValue(ConfigConstant.IMAGE_SERVER_URL));
		return "/main";
	}

	/**
	 * 
	 * 初始化首页统计相关数据
	 * @author wyw
	 * @date 2016-8-10
	 */
	@RequestMapping(value = "/index/initStatistics")
	@ResponseBody
	public Object initStatistics() {
		Map<String,Object> dataMap = Maps.newHashMap();
		
		User queryUser = new User(); 
		queryUser.setIsVouch(User.USER_NATURE_NOT_VOUCH);
		//总的注册量
		int registerTotal=userService.getCount(queryUser)-1;//排除admin用户
		dataMap.put("registerTotal",registerTotal<0?0:registerTotal);
		
		//昨日注册人数
		Date dateYesterDay = DateUtils.rollDay(DateUtils.getNow(), -1);
		queryUser.setStartTime(DateUtils.getDateStart(dateYesterDay));
		queryUser.setEndTime(DateUtils.getDateEnd(dateYesterDay));
		int registerYesterday = userService.getCount(queryUser);
		dataMap.put("registerYesterday", NumberUtils.isDefault(registerYesterday)?0:registerYesterday);
		
		//投资总额
		ProjectInvest projectInvest = new ProjectInvest();
		projectInvest.setStatus(InvestEnum.STATUS_SUCCESS.getValue());	
		BigDecimal investTotal=projectInvestService.sumInvest(projectInvest);
		dataMap.put("investTotal", NumberUtils.isDefault(investTotal)?0:investTotal.doubleValue()/10000);
		 
		//昨日投资总额
		projectInvest.setStartTime(DateUtils.getDateStart(dateYesterDay));
		projectInvest.setEndTime(DateUtils.getDateEnd(dateYesterDay));
		BigDecimal investTotalYesterday=projectInvestService.sumInvest(projectInvest);
		dataMap.put("investTotalYesterday", NumberUtils.isDefault(investTotalYesterday)?0:investTotalYesterday.doubleValue()/10000);//昨日投资总额
		 
		//借款总额
		String[] projectStatus={LoanEnum.STATUS_RAISING.getValue(),
				LoanEnum.STATUS_RAISE_FINISHED.getValue(),
				LoanEnum.STATUS_ESTABLISH.getValue(),
				LoanEnum.STATUS_REPAY_START.getValue(),
				LoanEnum.STATUS_REPAY_OVERDUE.getValue(),
				LoanEnum.STATUS_BAD_DEBT.getValue(),
				LoanEnum.STATUS_REPAYED_SUCCESS.getValue()
				};
		BigDecimal totalProjectAccount =  projectService.sumProjectAccount(projectStatus);	
		dataMap.put("totalProjectAccount", NumberUtils.isDefault(totalProjectAccount)?0:totalProjectAccount.doubleValue()/10000);
		
		//昨日借款总额
		BigDecimal totalProjectAccountYesterday =  projectService.sumProjectAccountByDate(projectStatus,DateUtils.getDateStart(dateYesterDay),DateUtils.getDateEnd(dateYesterDay));	
		dataMap.put("totalProjectAccountYesterday", NumberUtils.isDefault(totalProjectAccountYesterday)?0:totalProjectAccountYesterday.doubleValue()/10000);
		
		//累计收益
		BigDecimal totalEarnAmount = userEarnLogService.allEarnAmount(null);
		dataMap.put("totalEarnAmount", NumberUtils.isDefault(totalEarnAmount)?0:totalEarnAmount.doubleValue()/10000);
		
		//昨日累计收益
		BigDecimal totalEarnAmountYesterday = userEarnLogService.getAllEarnAmountByDate(DateUtils.getDateStart(dateYesterDay));
		dataMap.put("totalEarnAmountYesterday", NumberUtils.isDefault(totalEarnAmountYesterday)?0:totalEarnAmountYesterday.doubleValue()/10000);
		
		//累计可用余额
		BigDecimal totalUseAccount =accountService.getTotalUseMoney() ;
		dataMap.put("totalUseAccount", NumberUtils.isDefault(totalUseAccount)?0:totalUseAccount.doubleValue()/10000) ;
		
		//昨日累计可用余额
		BigDecimal totalUseAccountYesterday = accountService.sumUseByDate(DateUtils.getDateStart(dateYesterDay), DateUtils.getDateEnd(dateYesterDay)) ;
		dataMap.put("totalUseAccountYesterday", NumberUtils.isDefault(totalUseAccountYesterday)?0:totalUseAccountYesterday.doubleValue()/10000) ;
		
		//累计冻结金额
		BigDecimal totalNoUseAccount = accountService.getNoUseTotalMoney() ;
		dataMap.put("totalNoUseAccount", NumberUtils.isDefault(totalNoUseAccount)?0:totalNoUseAccount.doubleValue()/10000) ;
		
		//累计昨日冻结金额
		BigDecimal totalNoUseAccountYesterday = accountService.sumNoUseMoneyByDate(DateUtils.getDateStart(dateYesterDay), DateUtils.getDateEnd(dateYesterDay)) ;
		dataMap.put("totalNoUseAccountYesterday", NumberUtils.isDefault(totalNoUseAccountYesterday)?0:totalNoUseAccountYesterday.doubleValue()/10000) ;
		
		//累计待还总额
		BigDecimal totalCollectionAccount = projectRepaymentService.getTotalCollection() ;
		dataMap.put("totalCollectionAccount", NumberUtils.isDefault(totalCollectionAccount)?0:totalCollectionAccount.doubleValue()/10000) ;
		
		//累计昨日待还总额
		BigDecimal totalCollectionAccountYesterday = projectRepaymentService.sumTotalCollectionByDate(DateUtils.getDateStart(dateYesterDay), DateUtils.getDateEnd(dateYesterDay)) ;
		dataMap.put("totalCollectionAccountYesterday", NumberUtils.isDefault(totalCollectionAccountYesterday)?0:totalCollectionAccountYesterday.doubleValue()/10000) ;
		
		return dataMap;
	}
	
	/**
	 * 
	 * 初始化首页标的信息
	 * @author xhf
	 * @date 2016-8-10
	 */
	@RequestMapping(value = "/index/initProjectInfo")
	@ResponseBody
	public Object initProjectInfo(){
		Map<String,Object> dataMap = Maps.newHashMap();
		//产品信息
		initProduct(dataMap);
		//借贷信息
		initBorrow(dataMap);
		//返回结果
		return dataMap;
	}
	
	/**
	 * 
	 * 初始化首页待审信息
	 * @author xhf
	 * @date 2016-8-10
	 */
	@RequestMapping(value = "/index/initAuditInfo")
	@ResponseBody
	public Object initAuditInfo(){
		Map<String,Object> dataMap = Maps.newHashMap();
		//提现待审 
		CashModel cashVerify = new CashModel();
		cashVerify.setStatus(Constant.CASH_STATUS_AUDIT);//申请中状态
		int cashVerifySize = cashService.getCount(cashVerify);
		dataMap.put("cashVerifyListSize", cashVerifySize);	
		//资质待审
		UserQualificationApply qualificationApply = new UserQualificationApply();
		qualificationApply.setStatus(UserQualificationApply.STATUS_AUDIT);
		int applyQualifySize = applyService.getCount(qualificationApply);
		dataMap.put("applyQualifyListSize", applyQualifySize);
		//返回结果
		return dataMap;
	}
	
	/**
	 * 
	 * 初始化 产品相关数据
	 * @author xhf
	 * @date 2016-8-10
	 */
	private void initProduct(final Map<String,Object> model) {
		String[] projectStatus= new String[] { 
				LoanEnum.STATUS_PUBLISH_FAIL.getValue(),
				LoanEnum.STATUS_WAIT_PUBLISH.getValue(),
				LoanEnum.STATUS_PUBLISH_SUCCESS.getValue(),
				LoanEnum.STATUS_RAISE_FINISHED.getValue()
				};
        List<Project>statusList = projectService.getCountByStatus(LoanEnum.BORROW_FLAG_PRODUCT.getValue(), projectStatus);
		
        int borrowMaintenceNum=0, borrowWaitNum=0, borrowSaleNum=0, borrowFullNum=0;
        for (Project project : statusList) {
			if(LoanEnum.STATUS_RAISE_FINISHED.getValue().equals(project.getStatus())){ //成立待审：成立戴胜
				borrowFullNum = project.getStatusCount();
			}else if(LoanEnum.STATUS_PUBLISH_SUCCESS.getValue().equals(project.getStatus())){ //上架待审：发布成功
				borrowSaleNum = project.getStatusCount();
			}else if(LoanEnum.STATUS_WAIT_PUBLISH.getValue().equals(project.getStatus())){ //发布待审：发布待审
				borrowWaitNum = project.getStatusCount();
			}else{ //待维护：发布审核被拒
				borrowMaintenceNum += project.getStatusCount();
			}
		}
        int borrowTotalNum = borrowMaintenceNum+borrowWaitNum+borrowSaleNum+borrowFullNum;
        model.put("newProductListSize", borrowMaintenceNum);
        model.put("wpProductlistSize", borrowWaitNum);
        model.put("productSaleListSize", borrowSaleNum);
        model.put("productFullListSize", borrowFullNum);	
        model.put("productTotal", borrowTotalNum);	
	}
	
	/**
	 * 
	 * 初始化首页借款相关代办信息
	 * @author xhf
	 * @date 2016-8-10
	 */
	private void initBorrow(final Map<String,Object> model) {
		String[] projectStatus= new String[] { 
				LoanEnum.STATUS_NEW.getValue(),
				LoanEnum.STATUS_PUBLISH_FAIL.getValue(),
				LoanEnum.STATUS_VOUCH_VERIFY_FAIL.getValue(),
				LoanEnum.STATUS_WAIT_PUBLISH.getValue(),
				LoanEnum.STATUS_PUBLISH_SUCCESS.getValue(),
				LoanEnum.STATUS_RAISE_FINISHED.getValue()
				};
        List<Project>statusList = projectService.getCountByStatus(LoanEnum.BORROW_FLAG_BORROW.getValue(), projectStatus);
        
        //待维护、发布待审核、上架待审、成立待审、总数
        int borrowMaintenceNum=0, borrowWaitNum=0, borrowSaleNum=0, borrowFullNum=0;
        for (Project project : statusList) {
			if(LoanEnum.STATUS_RAISE_FINISHED.getValue().equals(project.getStatus())){ //成立待审：成立戴胜
				borrowFullNum = project.getStatusCount();
			}else if(LoanEnum.STATUS_PUBLISH_SUCCESS.getValue().equals(project.getStatus())){ //上架待审：发布成功
				borrowSaleNum = project.getStatusCount();
			}else if(LoanEnum.STATUS_WAIT_PUBLISH.getValue().equals(project.getStatus())){ //发布待审：发布待审
				borrowWaitNum = project.getStatusCount();
			}else{ //待维护：新增维护、发布审核被拒、担保审核被拒
				borrowMaintenceNum += project.getStatusCount();
			}
		}
        int borrowTotalNum = borrowMaintenceNum+borrowWaitNum+borrowSaleNum+borrowFullNum;
        model.put("borrowListSize", borrowMaintenceNum);
        model.put("waitPublishSize", borrowWaitNum);
        model.put("borrowSaleSize", borrowSaleNum);
        model.put("borrowFullListSize", borrowFullNum);
        model.put("borrowTotal", borrowTotalNum);
	}
}
