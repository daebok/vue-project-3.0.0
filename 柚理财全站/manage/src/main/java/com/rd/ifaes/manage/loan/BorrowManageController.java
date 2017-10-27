package com.rd.ifaes.manage.loan;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.dict.OperateEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.PropertiesUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.excel.ExportModel;
import com.rd.ifaes.common.util.excel.ExportUtil;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.DictUtils;
import com.rd.ifaes.core.core.util.HclientFileUtil;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.operate.domain.RateCouponRule;
import com.rd.ifaes.core.operate.domain.RedenvelopeRule;
import com.rd.ifaes.core.operate.service.RateCouponRuleService;
import com.rd.ifaes.core.operate.service.RedenvelopeRuleService;
import com.rd.ifaes.core.operate.service.UserVipLevelService;
import com.rd.ifaes.core.project.domain.Borrow;
import com.rd.ifaes.core.project.domain.BorrowFollow;
import com.rd.ifaes.core.project.domain.BorrowUpload;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.domain.ProjectType;
import com.rd.ifaes.core.project.service.BorrowFollowService;
import com.rd.ifaes.core.project.service.BorrowService;
import com.rd.ifaes.core.project.service.BorrowUploadService;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.project.service.ProjectTypeService;
import com.rd.ifaes.core.project.service.ProjectVerifyLogService;
import com.rd.ifaes.core.protocol.service.ProtocolService;
import com.rd.ifaes.core.risk.domain.LevelConfig;
import com.rd.ifaes.core.risk.service.LevelConfigService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCompanyInfo;
import com.rd.ifaes.core.user.model.UserFreezeModel;
import com.rd.ifaes.core.user.model.UserIdentifyModel;
import com.rd.ifaes.core.user.service.UserAuthSignLogService;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserCompanyInfoService;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.manage.sys.SystemController;

/**
 * 借贷管理
 * 
 * @author lh
 * @version 3.0
 * @since 2016-6-24
 */
@Controller
public class BorrowManageController extends SystemController {
	/**
	 * 货后跟进排序方式
	 */
	private static final String BORROW_FOLLOW_ORDER_SORT = "bb.last_repay_time";
	/**
	 * 下架管理的排序方式
	 */
	private static final String STOP_ORDER_SORT = "bb.show_time";
	@Resource
	private BorrowService borrowService;
	@Resource
	private UserService userService;
	@Resource
	private BorrowUploadService borrowUploadService;
	@Resource
	private ProjectService projectService;
	@Resource
	private UserCacheService userCacheService;
	@Resource
	private ProjectTypeService projectTypeService;
	@Resource
	private UserCompanyInfoService userCompanyInfoService;
	@Resource 
	private BorrowFollowService borrowFollowService;
	@Resource 
	private LevelConfigService levelConfigService;
    @Resource
    private UserAuthSignLogService userAuthSignLogService;
	@Resource
	private UserVipLevelService userVipLevelService;
    @Resource
	private ProjectVerifyLogService projectVerifyLogService;
    @Resource
    private RedenvelopeRuleService redenvelopeRuleService;
    @Resource
    private RateCouponRuleService rateCouponRuleService;
    @Resource
    private ProtocolService protocolService ;
    @Resource
	private transient ProjectInvestService projectInvestService;
    
	/**
	 * 借贷管理
	 * 
	 * @return
	 */
	@RequestMapping(value = "/loan/borrow/borrowManage")
	@SystemLog(method=SysLogConstant.VIEW,content=SysLogConstant.BORROW_LIST)
	public String manage() {
		return "/loan/borrow/borrowManage";
	}
	
	/**
	 * 列表页面
	 */
	@RequestMapping(value = "/loan/borrow/borrowList")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.BORROW_LIST)
	public String borrowList() {
		return "/loan/borrow/borrowList";
	}

	/**
	 * 借贷列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/loan/borrow/list")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.BORROW_LIST)
	public Object list(Borrow model) {
		if (StringUtils.isBlank(model.getStatus())) {
			model.setStatusSet(new String[] { LoanEnum.STATUS_NEW.getValue(), 
					LoanEnum.STATUS_WAIT_VOUCH_VERIFY.getValue(),
					LoanEnum.STATUS_VOUCH_VERIFY_FAIL.getValue(),
					LoanEnum.STATUS_WAIT_PUBLISH.getValue(),
					LoanEnum.STATUS_PUBLISH_FAIL.getValue()
					});
		}
		return borrowService.findBasePage(model);
	}
	
	/**
	 * 新增借款页
	 * 
	 * @return
	 */
	@RequiresPermissions("project:borrow:add:add")
	@RequestMapping(value = "/loan/borrow/borrowAddPage")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.BORROW)
	public String addPage(Model model) {

		 model.addAttribute("borrowManageRate", ConfigUtils.getValue(ConfigConstant.BORROW_MANAGE_RATE));
		 model.addAttribute("interestManageRate", ConfigUtils.getValue(ConfigConstant.INTEREST_MANAGE_RATE));
		 model.addAttribute("lowestInvestAccount", ConfigUtils.getValue(ConfigConstant.BORROW_LOWEST_INVEST_ACCOUNT));
		 model.addAttribute("vouchList", findVouchList());
		List<LevelConfig> list = levelConfigService.findByOrder();
		 model.addAttribute("levelList",list);
		 model.addAttribute("borrowMinAccount", ConfigUtils.getDouble(ConfigConstant.BORROW_LOWEST_ACCOUNT));
		 model.addAttribute("borrowMaxAccount", ConfigUtils.getDouble(ConfigConstant.BORROW_MOST_ACCOUNT));
		 model.addAttribute("borrowMinApr", ConfigUtils.getDouble(ConfigConstant.BORROW_LOWEST_APR));
		 model.addAttribute("borrowMaxApr", ConfigUtils.getDouble(ConfigConstant.BORROW_MOST_APR));
		//默认逾期罚息利率
		 model.addAttribute("overdueFeeRate",BigDecimalUtils.valueOf(ConfigUtils.getValue(ConfigConstant.OVERDUE_FEE)).
				multiply(BigDecimal.valueOf(Constant.DOUBLE_ONE_HUNDRED)));
		 model.addAttribute("hourList", StringUtils.getHourList());
		return "/loan/borrow/borrowAddPage";
	}

	/**
	 * 新增借款
	 * 
	 * @return
	 */
	@RequiresPermissions("project:borrow:add:add")
	@RequestMapping(value = "/loan/borrow/borrowAdd", method = RequestMethod.POST)
	@ResponseBody
	@TokenValid(value = TokenConstants.TOKEN_BORROW_ADD, dispatch = true)
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.BORROW)
	public Object add(Borrow borrow) {
		borrow.setOperatorName(getCurrUserName());		
		borrowService.insert(borrow);
		return renderSuccessResult();
	}

	/**
	 * 更新借款页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/loan/borrow/borrowEditPage")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.BORROW)
	public String editPage(@RequestParam(value = "id", required = true) String id, final Model model) {
		Borrow borrow = borrowService.get(id);
		if (borrow != null && !(LoanEnum.STATUS_NEW.getValue().equals(borrow.getStatus())
				|| LoanEnum.STATUS_PUBLISH_FAIL.getValue().equals(borrow.getStatus())
				|| LoanEnum.STATUS_VOUCH_VERIFY_FAIL.getValue().equals(borrow.getStatus()))) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_STATUS_ERROR));
		}
		model.addAttribute("vouchList", findVouchList());
		List<LevelConfig> list = levelConfigService.findByOrder();
		model.addAttribute("levelList", list);
		if (borrow != null && borrow.getFixedRepayDay() == 0) {
			borrow.setFixedRepayDay(null);
		}
		// 借款标信息
		model.addAttribute("borrow", borrow);
		model.addAttribute("borrowMinAccount", ConfigUtils.getDouble(ConfigConstant.BORROW_LOWEST_ACCOUNT));
		model.addAttribute("borrowMaxAccount", ConfigUtils.getDouble(ConfigConstant.BORROW_MOST_ACCOUNT));
		model.addAttribute("hourList", StringUtils.getHourList());
		// 借款资料
		loadBorrowUploadInfo(id, model);
		return "/loan/borrow/borrowEditPage";
	}
	
	/**
	 * 更新借款
	 * 
	 * @return
	 */
	@RequestMapping(value = "/loan/borrow/borrowEdit", method = RequestMethod.POST)
	@ResponseBody
	@TokenValid(value = TokenConstants.TOKEN_BORROW_EDIT, dispatch = true)
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.BORROW)
	public Object edit(Borrow borrow) {
		borrow.setOperatorName(getCurrUserName());		
		borrowService.update(borrow);
		return renderSuccessResult();
	}

	/**
	 * 选择借款人
	 * 
	 * @return
	 */
	@RequestMapping(value = "/loan/borrow/borrowerPage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.BORROWER)
	public String borrowerPage() {
		return "/loan/borrow/borrowerPage";
	}

	/**
	 * 借款人列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/loan/borrow/borrowerList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.BORROWER)
	public Object borrowerList(User model) {
		model.setIsVouch(User.USER_NATURE_NOT_VOUCH);
		model.setRealNameStatus(Constant.FLAG_YES);
		model.setStatus(User.USER_STATUS_NORMAL);
		model.setExceptFreeze(UserFreezeModel.STATUS_USER_FREEZE_LOAN);
		if(userAuthSignLogService.checkAuthSign()){
			model.setAuthSignStatus(UserIdentifyModel.STATUS_AUTH_SIGN_SUCC);
		}
		return userService.findBorrowerPage(model);
	}

	/**
	 * 查询担保机构
	 * 
	 * @return
	 */
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.VOUCH_LIST)
	private List<User> findVouchList() {
		User model = new User();
		model.setIsVouch(User.USER_NATURE_VOUCH);
		model.setRealNameStatus(Constant.FLAG_YES);
		return userService.findBorrowerList(model);
	}

	/**
	 * 自动补全借款人信息
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/loan/borrow/autoCompleteBorrower")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.BORROWER)
	public Object autoCompleteBorrower(User model) {
		model.setIsVouch(User.USER_NATURE_NOT_VOUCH);
		model.setRealNameStatus(Constant.FLAG_YES);
		model.setStatus(User.USER_STATUS_NORMAL);
		model.setExceptFreeze(UserFreezeModel.STATUS_USER_FREEZE_LOAN);
		return userService.autoCompleteBorrower(model);
	}

	/**
	 * 删除借款资料（仅删除文件）
	 * 
	 * @param picPath
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/loan/borrow/deleteBorrowPic", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.DEL,content=SysLogConstant.BORROW_PIC)
	public Object deleteBorrowPic(String picPath, String id) {
		//ID不为空，表示已经保存修改信息的借款
		if (StringUtils.isBlank(id) || id.length() < 32) {
			String url = PropertiesUtils.getValue("deleteUrl");
			HclientFileUtil.deleteImg(url, picPath);
		}
		return renderSuccessResult();
	}

	/**
	 * 借款作废
	 */
	@RequestMapping(value = "/loan/borrow/borrowDel", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.DEL,content=SysLogConstant.BORROW)
	public Map<String, Object> del(String[] ids) {
		if (ids == null || ids.length == 0) {
			return renderResult(false, ResourceUtils.get(ResourceConstant.GLOBAL_NO_RECORD));
		}
		projectService.deleteBatch(ids);
		return renderSuccessResult();
	}

	/**
	 * 借款信息查看
	 */
	@RequestMapping(value = "/loan/borrow/borrowView")
	@SystemLog(method=SysLogConstant.VIEW,content=SysLogConstant.BORROW)
	public String borrowView(String id,final Model model) {
		if (StringUtils.isBlank(id)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_NO_RECORD));
		}
		Borrow borrow = borrowService.get(id);

		if (borrow != null && StringUtils.isNotBlank(borrow.getVouchId())) {
			UserCompanyInfo userCompanyInfo = userCompanyInfoService.findByUserId(borrow.getVouchId());
			 model.addAttribute("vouchUser", userCompanyInfo.getCompanyName());
		}
		borrow.setProjectType(DictUtils.getItemName(DictTypeEnum.PROJECT_TYPE_MANAGE.getValue(), borrow.getProjectType()));
		// 借款标信息
		 model.addAttribute("borrow", borrow);

		// 借款资料
		loadBorrowUploadInfo(id,model);

//		Project base = projectService.get(id);
//		final MqAutoInvestModel autoInvestModel = new MqAutoInvestModel();
//		autoInvestModel.setProject(base);
//		if (ConfigUtils.isOpenMq()) {
//			RabbitUtils.startAutoInvest(autoInvestModel);
//		} else {
////			userInvestAutoLogService.startAutoInvest(autoInvestModel);
//		}
		
		return "/loan/borrow/borrowView";
	}

	/****************************** 借款审核 ******************************/
	/**
	 * 审核管理页面
	 */
	@RequestMapping(value = "/loan/borrow/borrowVerifyManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.BORROW_AUDIT)
	public String verifyManage() {
		return "/loan/borrow/borrowVerifyManage";
	}

	/**
	 * 审核列表数据
	 */
	@RequestMapping(value = "/loan/borrow/borrowVerifyList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.BORROW_AUDIT)
	public Object verifyList(Borrow model) {
		if (StringUtils.isBlank(model.getStatus())) {
			model.setStatusSet(new String[] { LoanEnum.STATUS_WAIT_PUBLISH.getValue() });
		}
		return borrowService.findBasePage(model);
	}

	/**
	 * 借款审核页面
	 */
	@RequiresPermissions("project:borrow:verify:auditing")
	@RequestMapping(value = "/loan/borrow/borrowVerifyPage")
	@SystemLog(method=SysLogConstant.VIEW,content=SysLogConstant.BORROW_AUDIT)
	public String verifyPage(String id,final Model model) {
		if (StringUtils.isBlank(id)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_NO_RECORD));
		}
		Borrow borrow = borrowService.get(id);
		if (borrow != null && !LoanEnum.STATUS_WAIT_PUBLISH.getValue().equals(borrow.getStatus())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_STATUS_ERROR));
		}
		if (borrow != null && StringUtils.isNotBlank(borrow.getVouchId())) {
			UserCompanyInfo userCompanyInfo = userCompanyInfoService.findByUserId(borrow.getVouchId());
			if(userCompanyInfo!=null){
				 model.addAttribute("vouchUser", userCompanyInfo.getCompanyName());
			}
		}
		borrow.setProjectType(DictUtils.getItemName(DictTypeEnum.PROJECT_TYPE_MANAGE.getValue(), borrow.getProjectType()));
		// 借款标信息
		 model.addAttribute("borrow", borrow);

		// 借款资料
		loadBorrowUploadInfo(id,model);

		return "/loan/borrow/borrowVerifyPage";
	}

	/**
	 * 借款审核
	 */
	@RequiresPermissions("project:borrow:verify:auditing")
	@RequestMapping(value = "/loan/borrow/borrowVerify", method = RequestMethod.POST)
	@ResponseBody
	@TokenValid(value = TokenConstants.TOKEN_BORROW_VERIFY, dispatch = true)
	@SystemLog(method=SysLogConstant.AUDIT,content=SysLogConstant.BORROW_AUDIT)
	public Map<String, Object> verify(Borrow borrow) {
		borrow.setOperatorName(getCurrUserName());		
		borrowService.verify(borrow);
		return renderSuccessResult();
	}

	/****************************** 上架管理 ******************************/

	/**
	 * 上架管理页面
	 */
	@RequestMapping(value = "/loan/borrow/borrowSaleManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.BORROW_SALE)
	public String saleManage(Model model) {
		model.addAttribute("nowTime",System.currentTimeMillis());
		return "/loan/borrow/borrowSaleManage";
	}

	/**
	 * 上架列表数据
	 */
	@RequestMapping(value = "/loan/borrow/borrowSaleList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.BORROW_SALE)
	public Object saleList(Borrow model) {
		if (StringUtils.isBlank(model.getStatus())) {
			model.setStatusSet(new String[] { LoanEnum.STATUS_PUBLISH_SUCCESS.getValue(), LoanEnum.STATUS_RAISING.getValue() });
		}
		model.setShowing(CommonEnum.YES.getValue());
		return borrowService.findBasePage(model);
	}

	/**
	 * 上架页面
	 */
	@RequiresPermissions("project:borrow:sale:sale")
	@RequestMapping(value = "/loan/borrow/borrowSalePage")
	@SystemLog(method=SysLogConstant.SALE,content=SysLogConstant.BORROW_SALE)
	public String salePage(String id, Model model) {
		if (StringUtils.isBlank(id)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_NO_RECORD));
		}
		Borrow borrow = borrowService.get(id);
		if(borrow==null){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_NO_RECORD));
		}
		if (!LoanEnum.STATUS_PUBLISH_SUCCESS.eq(borrow.getStatus()) && !LoanEnum.STATUS_RAISING.eq(borrow.getStatus())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_STATUS_ERROR));
		}
		if (StringUtils.isNotBlank(borrow.getVouchId())) {
			UserCompanyInfo userCompanyInfo = userCompanyInfoService.findByUserId(borrow.getVouchId());
			if(userCompanyInfo!=null){
				model.addAttribute("vouchUser", userCompanyInfo.getCompanyName());
			}
		}
		if (borrow.getShowTime() == null) {
			borrow.setRedEnvelopeRate(ConfigUtils.getBigDecimal(ConfigConstant.INVEST_REDENVELOPE_RATE));
		}
		Date now = DateUtils.getNow();
		if(borrow.getShowTime() == null){
			borrow.setShowTime(now);
		}
		// 借款标信息
		model.addAttribute("borrow", borrow);
		model.addAttribute("nowTime", now);
		// 借款资料
		loadBorrowUploadInfo(id,model);
		if(StringUtils.isNotBlank(borrow.getProjectTypeId())){
			ProjectType projectType = projectTypeService.get(borrow.getProjectTypeId());
			if(projectType!=null){
			    model.addAttribute("projectTypeName", projectType.getTypeName());
			}
		}
		model.addAttribute("vipLevelList", userVipLevelService.getLevelByStatus(Constant.FLAG_NO));
 		model.addAttribute("userAccountTypeList", DictUtils.list(DictTypeEnum.USER_ACCOUNT_TYPE.getValue()));
		//红包、加息券发放规则
	   model.addAttribute("redenvelopeRule", redenvelopeRuleService.findByActivityCode(OperateEnum.ACTIVITYPLAN_TENDER_SUC.getValue()));
	   model.addAttribute("rateCouponRule", rateCouponRuleService.findByActivityCode(OperateEnum.ACTIVITYPLAN_TENDER_SUC.getValue()));
				
		return "/loan/borrow/borrowSalePage";
	}
	
	@RequestMapping(value = "/loan/borrow/selectRed", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.SALE,content=SysLogConstant.BORROW_SALE)
	public Object selectRed(final String projectTypeId) {
		List<RedenvelopeRule> retList = new ArrayList<>();
		List<RedenvelopeRule> list = redenvelopeRuleService.findByActivityCode(OperateEnum.ACTIVITYPLAN_TENDER_SUC.getValue());
		for (RedenvelopeRule rule : list) {
			if (projectTypeId != null && rule.getGrantProjectType().indexOf(projectTypeId) != -1) {
				retList.add(rule);
			}
		}
		return retList;
	}
	
	@RequestMapping(value = "/loan/borrow/selectRate", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.SALE,content=SysLogConstant.BORROW_SALE)
	public Object selectRate(final String projectTypeId) {
		List<RateCouponRule> retList = new ArrayList<>();
		List<RateCouponRule> list = rateCouponRuleService.findByActivityCode(OperateEnum.ACTIVITYPLAN_TENDER_SUC.getValue());
		for (RateCouponRule rule : list) {
			if (projectTypeId != null && rule.getGrantProjectType().indexOf(projectTypeId) != -1) {
				retList.add(rule);
			}
		}
		return retList;
	}

	/**
	 * 上架
	 */
	@RequiresPermissions("project:borrow:sale:sale")
	@RequestMapping(value = "/loan/borrow/borrowSale", method = RequestMethod.POST)
	@ResponseBody
	@TokenValid(value = TokenConstants.TOKEN_BORROW_SALE, dispatch = true)
	@SystemLog(method=SysLogConstant.SALE,content=SysLogConstant.BORROW_SALE)
	public Map<String, Object> sale(Borrow borrow) {
		borrow.setOperatorName(getCurrUserName());		
		borrowService.sale(borrow);
		return renderSuccessResult();
	}

	/****************************** 下架管理 ******************************/

	/**
	 * 下架管理页面
	 */
	@RequestMapping(value = "/loan/borrow/borrowStopManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.BORROW_STOP)
	public String stopManage() {
		return "/loan/borrow/borrowStopManage";
	}

	/**
	 * 下架列表数据
	 */
	@RequestMapping(value = "/loan/borrow/borrowStopList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.BORROW_STOP)
	public Object stopList(Borrow model) {
		if (StringUtils.isBlank(model.getStatus())) {
			model.setStatusSet(new String[] { LoanEnum.STATUS_RAISING.getValue() });
		}
		model.getPage().setSort(STOP_ORDER_SORT);//根据上架时间倒序排列--根据borrowMapper.xml中的SQL 来定
		model.getPage().setOrder(Constant.DESC);
		model.setListTag(Borrow.LIST_TAG_STOP);
		return borrowService.findBasePage(model);
	}

	/**
	 * 下架页面
	 */
	@RequiresPermissions("project:borrow:stop:stop")
	@RequestMapping(value = "/loan/borrow/borrowStopPage")
	@SystemLog(method=SysLogConstant.STOP,content=SysLogConstant.BORROW_STOP)
	public String stopPage(String id,final Model model) {
		if (StringUtils.isBlank(id)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_NO_RECORD));
		}
		Borrow borrow = borrowService.get(id);
		if(borrow==null){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_NO_RECORD));
		}
		if (!LoanEnum.STATUS_RAISING.getValue().equals(borrow.getStatus())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_STATUS_ERROR));
		}
		if (StringUtils.isNotBlank(borrow.getVouchId())) {
			UserCompanyInfo userCompanyInfo = userCompanyInfoService.findByUserId(borrow.getVouchId());
			 model.addAttribute("vouchUser", userCompanyInfo.getCompanyName());
		}
		if(LoanEnum.SPECIFIC_SALE_USER.getValue().equals(borrow.getSpecificSale())){
			borrow.setSpecificSaleRule(DictUtils.getItemName(DictTypeEnum.USER_ACCOUNT_TYPE.getValue(), borrow.getSpecificSaleRule()));
		}
		// 借款标信息
		 model.addAttribute("borrow", borrow);

		// 借款资料
		loadBorrowUploadInfo(id,model);
		
		 model.addAttribute("now", DateUtils.getDateTime());
		 model.addAttribute("projectType", projectTypeService.get(borrow.getProjectTypeId()));

		return "/loan/borrow/borrowStopPage";
	}

	/**
	 * 下架
	 */
	@RequiresPermissions("project:borrow:stop:stop")
	@RequestMapping(value = "/loan/borrow/borrowStop", method = RequestMethod.POST)
	@ResponseBody
	@TokenValid(value = TokenConstants.TOKEN_BORROW_STOP, dispatch = true)
	@SystemLog(method=SysLogConstant.STOP,content=SysLogConstant.BORROW_STOP)
	public Map<String, Object> stop(Borrow borrow) {
		borrow.setOperatorName(getCurrUserName());		
		borrowService.stop(borrow);
		return renderSuccessResult();
	}

	/****************************** 成立审核 ******************************/

	/**
	 * 成立审核页面
	 */
	@RequestMapping(value = "/loan/borrow/borrowEstablishVerifyManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.BORROW_ESTABLISH)
	public String establishVerifyManage() {
		return "/loan/borrow/borrowEstablishVerifyManage";
	}

	/**
	 * 成立审核列表数据
	 */
	@RequestMapping(value = "/loan/borrow/borrowEstablishVerifyList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.BORROW_ESTABLISH)
	public Object establishVerifyList(Borrow model) {
		if (StringUtils.isBlank(model.getStatus())) {
			model.setStatusSet(new String[] { LoanEnum.STATUS_RAISE_FINISHED.getValue() });
		}
		Page<Borrow> page = borrowService.findBasePage(model);
		return page;
	}

	/**
	 * 成立审核页面
	 */
	@RequiresPermissions("project:borrow:ev:auditing")
	@RequestMapping(value = "/loan/borrow/borrowEstablishVerifyPage")
	@SystemLog(method=SysLogConstant.ESTABLISH,content=SysLogConstant.BORROW_ESTABLISH)
	public String establishVerifyPage(String id,final Model model) {
		if (StringUtils.isBlank(id)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_NO_RECORD));
		}
		Borrow borrow = borrowService.get(id);
		if(borrow==null){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_NO_RECORD));
		}
		if (!LoanEnum.STATUS_RAISE_FINISHED.getValue().equals(borrow.getStatus())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_STATUS_ERROR));
		}
		if (StringUtils.isNotBlank(borrow.getVouchId())) {
			UserCompanyInfo userCompanyInfo = userCompanyInfoService.findByUserId(borrow.getVouchId());
			 model.addAttribute("vouchUser", userCompanyInfo.getCompanyName());
		}
		if(LoanEnum.SPECIFIC_SALE_USER.getValue().equals(borrow.getSpecificSale())){
			borrow.setSpecificSaleRule(DictUtils.getItemName(DictTypeEnum.USER_ACCOUNT_TYPE.getValue(), borrow.getSpecificSaleRule()));
		}
		// 借款标信息
		 model.addAttribute("borrow", borrow);

		// 借款资料
		loadBorrowUploadInfo(id,model);
		 model.addAttribute("now", DateUtils.getDateTime());

		 model.addAttribute("projectType", projectTypeService.get(borrow.getProjectTypeId()));

		return "/loan/borrow/borrowEstablishVerifyPage";
	}

	/**
	 * 成立审核
	 */
	@RequiresPermissions("project:borrow:ev:auditing")
	@RequestMapping(value = "/loan/borrow/borrowEstablishVerify", method = RequestMethod.POST)
	@ResponseBody
	@TokenValid(value = TokenConstants.TOKEN_BORROW_ESTABLISH_VERIFY, clear = true)
	@SystemLog(method=SysLogConstant.ESTABLISH,content=SysLogConstant.BORROW_ESTABLISH)
	public Map<String, Object> establishVerify(Borrow borrow) {
		borrow.setOperatorName(getCurrUserName());		
		borrowService.establishVerify(borrow);
		return renderSuccessResult();
	}

	/****************************** 借款记录 ******************************/

	/**
	 * 借款记录
	 * 
	 * @return
	 */
	@RequestMapping(value = "/loan/borrow/borrowRecord")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.BORROW_RECORD)
	public String record(Model model) {
		model.addAttribute("webUrl", ConfigUtils.getValue(ConfigConstant.WEB_URL));
		return "/loan/borrow/borrowRecord";
	}

	/**
	 * 借款记录列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/loan/borrow/borrowRecordList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.BORROW_RECORD)
	public Object recordList(Borrow model) {
		return borrowService.findRecordPage(model);
	}
	
	/**
	 * 合同记录
	 * 
	 * @return
	 */
	@RequestMapping(value = "/contract/download/downContract")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.BORROW_RECORD)
	public String contract(Model model) {
		model.addAttribute("webUrl", ConfigUtils.getValue(ConfigConstant.WEB_URL));
		return "/contract/download/downContract";
	}

	/**
	 * 合同记录列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/contract/download/downContractList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.BORROW_RECORD)
	public Object contractList(Borrow model) {
		return borrowService.findRecordPage(model, true);
	}
	
	
	
	/**
	 * 导出借款记录
	 * @param model
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/loan/borrow/exportBorrowRecord")
	@SystemLog(method=SysLogConstant.EXPORT,content=SysLogConstant.BORROW_RECORD)
	public void exportBorrowRecord(final Borrow model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ExportUtil.exportExcel(new ExportModel<Borrow>() {
			@Override
			public String getCacheKey() {
				return  BorrowManageController.this.getUserId();
			}
			@Override
			public Borrow getModel() {
				return model;
			}
			@Override
			public int getTotal(Borrow entity) {
				return borrowService.getCount(entity);
			}
			@Override
			public List<Borrow> getPageRecords(Borrow entity) {
				return borrowService.findBasePage(entity).getRows();
			}
		}, request, response);
	}
	
	/**
	 * 
	 * 贷后跟踪
	 * @author wyw
	 * @date 2016-8-17
	 * @return
	 */
	@RequestMapping(value = "/loan/borrow/borrowFollow")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.BORROW_FOLLOW)
	public String borrowFollow() {
		return "/loan/borrow/borrowFollow";
	}
	
	/**
	 * 贷后跟踪列表数据
	 * @author wyw
	 * @date 2016-8-17
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/loan/borrow/borrowFollowList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.BORROW_FOLLOW)
	public Object borrowFollowList(Borrow model) {
		model.getPage().setSort(BORROW_FOLLOW_ORDER_SORT);
		model.getPage().setOrder(Constant.ASC);
		return borrowService.findBorrowFollowPage(model);
	}
	/**
	 * 
	 * 贷后跟踪编辑页面
	 * @author wyw
	 * @date 2016-8-18
	 * @param id
	 * @param request
	 * @return
	 */
	@RequiresPermissions("project:borrow:follow:edit")
	@RequestMapping(value = "/loan/borrow/borrowFollowEdit")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.BORROW_FOLLOW)
	public String borrowFollowEdit(String id,final Model model) {
		if (StringUtils.isBlank(id)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_NO_RECORD));
		}
		Borrow borrow = borrowService.get(id);

		if (borrow != null && StringUtils.isNotBlank(borrow.getVouchId())) {
			UserCompanyInfo userCompanyInfo = userCompanyInfoService.findByUserId(borrow.getVouchId());
			 model.addAttribute("vouchUser", userCompanyInfo.getCompanyName());
		}
		// 借款标信息
		 model.addAttribute("borrow", borrow);
		// 根据borrow获取贷后跟进信息
		BorrowFollow borrowFollow = new BorrowFollow();
		borrowFollow.setProjectId(id);
		List<BorrowFollow> followList=borrowFollowService.findList(borrowFollow);
		/*borrowFollowService.f*/
		if(!CollectionUtils.isEmpty(followList)){
			borrowFollow = followList.get(0);
		}
		 model.addAttribute("borrowFollow", borrowFollow);
		
		BorrowUpload uploadModel = new BorrowUpload();
		uploadModel.setProjectId(id);
		uploadModel.setFileType(BorrowUpload.FILE_TYPE_BORROWFOLLOW);
		 model.addAttribute("picList", borrowUploadService.findList(uploadModel));
		
		return "/loan/borrow/borrowFollowEdit";
	}
	/**
	 * 
	 * 贷后管理编辑操作
	 * @author wyw
	 * @date 2016-8-18
	 * @param borrowFollow
	 * @return
	 */
	@RequiresPermissions("project:borrow:follow:edit")
	@RequestMapping(value = "/loan/borrow/doBorrowFollowEdit", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.BORROW_FOLLOW)
	public Map<String, Object> doBorrowFollowEdit(BorrowFollow borrowFollow) {
		borrowFollowService.save(borrowFollow);
		return renderSuccessResult();
	}
	/**
	 * 
	 * 获取贷后跟踪的信息
	 * @author wyw
	 * @date 2016-8-18
	 * @param projectId
	 * @return
	 */
	@RequestMapping(value = "/loan/borrow/getBorrowFollow", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.VIEW,content=SysLogConstant.BORROW_FOLLOW)
	public Object getBorrowFollow(String projectId) {
		// 根据borrow获取贷后跟进信息
		if (StringUtils.isBlank(projectId)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_NO_RECORD));
		}
		BorrowFollow borrowFollow = new BorrowFollow();
		borrowFollow.setProjectId(projectId);
	    List<BorrowFollow> followList=borrowFollowService.findList(borrowFollow);
	    if(!CollectionUtils.isEmpty(followList)){
					borrowFollow = followList.get(0);
		}
		return borrowFollow;
	}
	/**
	 * 加载借款资料
	 * @author wyw
	 * @date 2016-9-11
	 */
	private void loadBorrowUploadInfo(String projectId ,final Model model) {
		BorrowUpload uploadModel = new BorrowUpload();
		uploadModel.setProjectId(projectId);
		uploadModel.setFileType(BorrowUpload.FILE_TYPE_BORROW);
		 model.addAttribute("picList", borrowUploadService.findList(uploadModel));

		uploadModel.setFileType(BorrowUpload.FILE_TYPE_MORTGAGE);
		 model.addAttribute("mtgList", borrowUploadService.findList(uploadModel));	
	}
	
	/**
	 * 审核记录
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/loan/borrow/verifyLogList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.VERIFY_LOG)
	public Object verifyLogList(String id){
		return projectVerifyLogService.findByProjectId(id);
	}
	
	/****************************** 借款协议下载  start ******************************/
	@RequestMapping("/loan/borrow/downloadProjectProtocol")
	@RequiresPermissions("project:borrow:borrowList:downLoad")
	@SystemLog(method=SysLogConstant.DOWNLOAD,content=SysLogConstant.PROTOCOL)
	public String borrowAllProtocol(String projectId) throws Exception {
		Project project = projectService.get(projectId);
		String fileName = project.getProjectName() + ".zip";
		String url = "/upload/downloadContract.html";
		//请求地址需改为manamge_url
		String webUrl = ConfigUtils.getValue(ConfigConstant.MANAGE_URL);
		String resultStr = protocolService.downloadProtocol(projectId, projectId, fileName, url, webUrl);
		if(StringUtils.isNotBlank(resultStr)){
			return "redirect:"+resultStr;
		}else{
			LOGGER.error("下载出错,地址：{}",resultStr); 
			throw new BussinessException("下载出错");
		}
	}
	
	/**
	 * 注意：从WEB复制过来的代码 不要在这里改
	 */
	@RequestMapping("/member/myInvest/buildProtocolAndDownload")
	@ResponseBody
	public Object buildProtocolAndDownload(final String projectId,String projectInvestId,
			final String type,final HttpServletResponse response) throws IOException{
		LOGGER.info("进入生成普通投资协议接口...start");
		if(StringUtils.isBlank(projectInvestId)){
			projectInvestId = projectId;
		}
		projectInvestService.handleAllProtocol(projectId,projectInvestId);
		if("zip".equals(type)){
			downloadProjectProtocol(projectId,response);
		}else{
			downloadInvestProtocol(projectInvestId,response);
		}
		return renderResult(true, "");
	}
	
	/**
	 * 从WEB复制过来的代码,
	 */
	public String downloadProjectProtocol(final String projectId,final HttpServletResponse response) throws IOException{
		LOGGER.info("进入借款协议下载  .zip接口...start");
		Project project = projectService.get(projectId);
		String fileName = project.getProjectName() +"_"+project.getProjectNo()+ ".zip";
		String url = "/upload/downloadContract.html";
		//请求地址需改为manamge_url
		String webUrl =ConfigUtils.getValue(ConfigConstant.MANAGE_URL);
		String resultStr = protocolService.downloadProtocol(projectId, projectId, fileName, url, webUrl);
		if(StringUtils.isNotBlank(resultStr)){
			response.sendRedirect(resultStr);
			LOGGER.info("进入借款协议下载  .zip接口...end");
		}else{
			LOGGER.error("下载出错,地址：{}",resultStr);
		}
		return null;
	}
	/**
	 * 从WEB复制过来的代码
	 */
	public String downloadInvestProtocol(final String investId,final HttpServletResponse response) throws IOException{
		LOGGER.info("进入借款协议下载  .pdf接口...start");
		ProjectInvest projectInvest = projectInvestService.get(investId);
		String projectId = projectInvest.getProjectId();
		Project project = projectService.get(projectId);
		
		String fileName = project.getProjectName() +"_"+project.getProjectNo()+ ".pdf";
		String url = "/upload/downloadContract.html";
		//请求地址需改为manamge_url
		String webUrl =ConfigUtils.getValue(ConfigConstant.MANAGE_URL);
		String resultStr = protocolService.downloadProtocol(projectId, investId, fileName, url, webUrl);
		if(StringUtils.isNotBlank(resultStr)){
			response.sendRedirect(resultStr);
			LOGGER.info("进入借款协议下载  .pdf接口...end");
		}else{
			LOGGER.error("下载出错,地址：{}",resultStr);
		}
		return null;
	}
	/****************************** 借款协议下载  end ******************************/
}
