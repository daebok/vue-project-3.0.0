package com.rd.ifaes.manage.loan;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.dict.OperateEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.excel.ExportModel;
import com.rd.ifaes.common.util.excel.ExportUtil;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.operate.service.RateCouponRuleService;
import com.rd.ifaes.core.operate.service.RedenvelopeRuleService;
import com.rd.ifaes.core.operate.service.UserVipLevelService;
import com.rd.ifaes.core.project.domain.Product;
import com.rd.ifaes.core.project.domain.ProjectType;
import com.rd.ifaes.core.project.service.ProductService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.project.service.ProjectTypeService;
import com.rd.ifaes.core.risk.service.LevelConfigService;
import com.rd.ifaes.manage.sys.SystemController;

@Controller
public class ProductManageController extends SystemController {
	@Resource
	private ProductService productService;
	@Resource
	private ProjectTypeService projectTypeService;
	@Resource
	private LevelConfigService levelConfigService;
	@Resource
	private UserVipLevelService userVipLevelService;
	@Resource
	private  ProjectService projectService;
    @Resource
    private RedenvelopeRuleService redenvelopeRuleService;
    @Resource
    private RateCouponRuleService rateCouponRuleService;

	/**
	 * 产品新增
	 * 
	 * @return
	 */
	@RequiresPermissions("project:product:add:add")
	@RequestMapping(value = "/product/manage/productAddPage")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.PRODUCT)
	public String productAddPage(Model model) {
		model.addAttribute("riskLevel", levelConfigService.findList(null));
		//默认利息管理费
		model.addAttribute("interestManageRate", ConfigUtils.getValue(ConfigConstant.INTEREST_MANAGE_RATE));
		model.addAttribute("borrowMinAccount", ConfigUtils.getDouble(ConfigConstant.BORROW_LOWEST_ACCOUNT));
		model.addAttribute("borrowMaxAccount", ConfigUtils.getDouble(ConfigConstant.BORROW_MOST_ACCOUNT));
		model.addAttribute("borrowMinApr", ConfigUtils.getDouble(ConfigConstant.BORROW_LOWEST_APR));
		model.addAttribute("borrowMaxApr", ConfigUtils.getDouble(ConfigConstant.BORROW_MOST_APR));
		//默认逾期罚息利率
		model.addAttribute("overdueFeeRate",ConfigUtils.getBigDecimal(ConfigConstant.OVERDUE_FEE));
		model.addAttribute("hourList", StringUtils.getHourList());
		return "/product/manage/productAddPage";
	}

	/**
	 *  新增产品
	 * @return
	 */
	@RequiresPermissions("project:product:add:add")
	@RequestMapping(value = "/product/manage/productAdd", method = RequestMethod.POST)
	@ResponseBody
	@TokenValid(value = TokenConstants.TOKEN_PRODUCT_ADD, dispatch = true)
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.PRODUCT)
	public Map<String, Object> add(Product product) {
		productService.insert(product);
		return renderSuccessResult();
	}

	/**
	 * 产品维护（ 显示所有新增产品，包括待审状态和审核被拒状态的产品，可以对审核被拒状态的产品进行修改。）
	 * 
	 * @return
	 */
	@RequestMapping(value = "/product/manage/newProductManage")
	@SystemLog(method=SysLogConstant.VIEW,content=SysLogConstant.PRODUCT)
	public String newProductManage( final Model model, final String status) {
		 model.addAttribute("status",status);
		return "/product/manage/newProductManage";
	}
	
	/**
	 * 查询所有新增产品
	 * 
	 * @return
	 */
	@RequestMapping(value = "/product/manage/newProductList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.PRODUCT)
	public Object newProductList(Product product) {
		if (StringUtils.isBlank(product.getStatus())) {
			product.setStatusSet(new String[] { LoanEnum.STATUS_NEW.getValue(), LoanEnum.STATUS_WAIT_PUBLISH.getValue(),
					LoanEnum.STATUS_PUBLISH_FAIL.getValue() });
		}
		return productService.findPage(product);
	}

	/**
	 * 更新借款页
	 * 
	 * @return
	 */
	@RequiresPermissions("project:product:productMan:edit")
	@RequestMapping(value = "/product/manage/productEditPage")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.PRODUCT)
	public String editPage(Model model, String id) {
		if (StringUtils.isBlank(id)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_NO_RECORD));
		}
		Product product = productService.get(id);
		if(product.getFixedRepayDay().intValue() == 0){
			product.setFixedRepayDay(null);			
		}
		model.addAttribute("product",  product);
		model.addAttribute("riskLevel", levelConfigService.findList(null));
		model.addAttribute("borrowMinAccount", ConfigUtils.getDouble(ConfigConstant.BORROW_LOWEST_ACCOUNT));
		model.addAttribute("borrowMaxAccount", ConfigUtils.getDouble(ConfigConstant.BORROW_MOST_ACCOUNT));
		model.addAttribute("hourList", StringUtils.getHourList());
		return "/product/manage/productEditPage";
	}

	/**
	 * 更新借款页
	 * 
	 * @return
	 */
	@RequiresPermissions("project:product:productMan:edit")
	@RequestMapping(value = "/product/manage/productEdit", method = RequestMethod.POST)
	@ResponseBody
	@TokenValid(value = TokenConstants.TOKEN_PRODUCT_EDIT, dispatch = true)
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.PRODUCT)
	public Object edit(Product product) {
		productService.update(product);
		return renderSuccessResult();
	}

	/**
	 * 审核被拒状态的产品进行删除操作
	 * 
	 * @param ids 审核被拒状态的产品UUID 数组
	 * @return
	 */
	@RequiresPermissions("project:product:productMan:cancel")
	@RequestMapping(value = "/product/manage/productDel")
	@ResponseBody
	@SystemLog(method=SysLogConstant.DEL,content=SysLogConstant.PRODUCT)
	public Map<String, Object> del(String[] ids) {
		if (ArrayUtils.isEmpty(ids)) {
			return renderResult(false,  ResourceUtils.get(ResourceConstant.GLOBAL_DEL_NO_RECORD));
		}
		productService.deleteBatch(ids);
		return renderSuccessResult();
	}

	/**
	 * 发布审核页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/product/manage/productPublishManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.PRODUCT_AUDIT)
	public String productPublishManage() {
		return "/product/manage/productPublishManage";
	}

	/**
	 * 查询所有发布待审产品
	 * 
	 * @return
	 */
	@RequestMapping(value = "/product/manage/productPublishList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.PRODUCT_AUDIT)
	public Object productPublishList(Product product) {
		if (StringUtils.isBlank(product.getStatus())) {
			product.setStatus(LoanEnum.STATUS_WAIT_PUBLISH.getValue());
		}
		return productService.findPage(product);
	}

	/**
	 * 发布审核页面
	 * 
	 * @return
	 */
	@RequiresPermissions("project:product:publish:auditing")
	@RequestMapping(value = "/product/manage/publishVerifyPage")
	@SystemLog(method=SysLogConstant.AUDIT,content=SysLogConstant.PRODUCT_AUDIT)
	public String publishVerifyPage(Model model, String id) {
		if (StringUtils.isBlank(id)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_NO_RECORD));
		}
		Product product = productService.get(id);
		model.addAttribute("product", product);
		return "/product/manage/publishVerifyPage";
	}

	/**
	 * 发布审核
	 */
	@RequiresPermissions("project:product:publish:auditing")
	@RequestMapping(value = "/product/manage/publishVerify", method = RequestMethod.POST)
	@ResponseBody
	@TokenValid(value = TokenConstants.TOKEN_PRODUCT_PUBLISH_VERIFY, dispatch = true)
	@SystemLog(method=SysLogConstant.AUDIT,content=SysLogConstant.PRODUCT_AUDIT)
	public Map<String, Object> publishVerify(Product product) {
		product.setOperatorName(getCurrUserName());
		productService.publishVerify(product);
		return renderSuccessResult();
	}

	/**
	 * 上架管理页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/product/manage/productSaleManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.PRODUCT_AUDIT)
	public String productSaleManage(Model model) {
		model.addAttribute("nowTime",System.currentTimeMillis());
		return "/product/manage/productSaleManage";
	}

	/**
	 * 上架管理页面-产品查询
	 * 
	 * @return
	 */
	@RequestMapping(value = "/product/manage/productSaleList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.PRODUCT_AUDIT)
	public Object productSaleList(Product product) {
		if (StringUtils.isBlank(product.getStatus())) {
			product.setStatusSet(new String[] { LoanEnum.STATUS_PUBLISH_SUCCESS.getValue(), LoanEnum.STATUS_RAISING.getValue() });
		}
		product.setShowing(CommonEnum.YES.getValue());
		Page<Product> page = product.getPage();
		if(page == null){
			page = new Page<Product>();
		}
		page.setSort("publish_verify_time");
		page.setOrder(Constant.DESC);
		product.setPage(page);
		return productService.findPage(product);
	}

	/**
	 * 上架管理-上架、编辑页面
	 */
	@RequiresPermissions("project:product:sale:sale")
	@RequestMapping(value = "/product/manage/productSaleEditPage")
	@SystemLog(method=SysLogConstant.SALE,content=SysLogConstant.PRODUCT_AUDIT)
	public String productSaleEditPage(Model model, String id) {
		if (StringUtils.isBlank(id)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_NO_RECORD));
		}
		Product product = productService.get(id);
		// 未上架管理的产品记录
		final  Date now=new Date();
		if (product.getShowTime() == null) {
			// 上架时间和开售时间默认都为系统当前时间
			product.setShowTime(now);
			//product.setSaleTime(now);
			product.setRedEnvelopeRate(ConfigUtils.getBigDecimal(ConfigConstant.INVEST_REDENVELOPE_RATE));
		}
		if(StringUtils.isNotBlank(product.getProjectTypeId())){
			ProjectType projectType = projectTypeService.get(product.getProjectTypeId());
			if(projectType!=null){
			    model.addAttribute("projectTypeName", projectType.getTypeName());
			}
		}
		model.addAttribute("tppName", ConfigUtils.getTppName());
		model.addAttribute("nowTime", now);
		model.addAttribute("product", product);
		model.addAttribute("vipLevelList", userVipLevelService.getLevelByStatus(Constant.FLAG_NO));
		//红包、加息券发放规则
		model.addAttribute("redenvelopeRule", redenvelopeRuleService.findByActivityCode(OperateEnum.ACTIVITYPLAN_TENDER_SUC.getValue()));
		model.addAttribute("rateCouponRule", rateCouponRuleService.findByActivityCode(OperateEnum.ACTIVITYPLAN_TENDER_SUC.getValue()));
		return "/product/manage/productSaleEditPage";
	}

	/**
	 * 上架、编辑管理
	 */
	@RequiresPermissions("project:product:sale:sale")
	@RequestMapping(value = "/product/manage/productSaleEdit", method = RequestMethod.POST)
	@ResponseBody
	@TokenValid(value = TokenConstants.TOKEN_PRODUCT_SALE, dispatch = true)
	@SystemLog(method=SysLogConstant.SALE,content=SysLogConstant.PRODUCT_AUDIT)
	public Map<String, Object> productSaleEdit(Product product) {
		product.setOperatorName(getCurrUserName());		
		productService.productSaleEdit(product);
		return renderSuccessResult();
	}

	/**
	 * 下架管理页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/product/manage/productStopManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.PRODUCT_STOP)
	public String productStopManage() {
		return "/product/manage/productStopManage";
	}

	/**
	 * 下架管理页面-产品查询
	 * 
	 * @return
	 */
	@RequestMapping(value = "/product/manage/productStopList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.PRODUCT_STOP)
	public Object productStopList(Product product) {
		if (StringUtils.isBlank(product.getStatus())) {
			product.setStatus(LoanEnum.STATUS_RAISING.getValue());
		}
		product.getPage().setSort("show_time");
		return productService.findPage(product);
	}

	/**
	 * 下架页面
	 */
	@RequiresPermissions("project:product:stop:stop")
	@RequestMapping(value = "/product/manage/productStopPage")
	@SystemLog(method=SysLogConstant.STOP,content=SysLogConstant.PRODUCT_STOP)
	public String productStopPage(Model model, String id) {
		if (StringUtils.isBlank(id)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_NO_RECORD));
		}
		Product product = productService.get(id);
		ProjectType projectType = projectTypeService.get(product.getProjectTypeId());
		model.addAttribute("product", product);
		model.addAttribute("projectType", projectType);
		return "/product/manage/productStopPage";
	}

	/**
	 * 下架处理
	 */
	@RequiresPermissions("project:product:stop:stop")
	@RequestMapping(value = "/product/manage/productStop", method = RequestMethod.POST)
	@ResponseBody
	@TokenValid(value = TokenConstants.TOKEN_PRODUCT_STOP, dispatch = true)
	@SystemLog(method=SysLogConstant.STOP,content=SysLogConstant.PRODUCT_STOP)
	public Map<String, Object> productStop(Product product) {
		product.setOperatorName(getCurrUserName());
		productService.productStop(product);
		return renderSuccessResult();
	}

	/**
	 * 成立审核页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/product/manage/productEstablishVerifyManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.PRODUCT_ESTABLISH)
	public String productEstablishVerifyManage() {
		return "/product/manage/productEstablishVerifyManage";
	}

	/**
	 * 成立审核管理页面-产品查询
	 * 
	 * @return
	 */
	@RequestMapping(value = "/product/manage/productEstablishVerifyList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.PRODUCT_ESTABLISH)
	public Object productEstablishVerifyList(Product product) {
		if (StringUtils.isBlank(product.getStatus())) {
			product.setStatus(LoanEnum.STATUS_RAISE_FINISHED.getValue());
		}
		product.getPage().setSort("raise_end_time");
		return productService.findPage(product);
	}

	/**
	 * 成立审核页面
	 */
	@RequiresPermissions("project:product:ev:auditing")
	@RequestMapping(value = "/product/manage/productEstablishVerifyPage")
	@SystemLog(method=SysLogConstant.AUDIT,content=SysLogConstant.PRODUCT_ESTABLISH)
	public String productEstablishVerifyPage(Model model, String id) {
		if (StringUtils.isBlank(id)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_NO_RECORD));
		}
		Product product = productService.get(id);
		if(product==null){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_NO_RECORD));
		}
		if (!LoanEnum.STATUS_RAISE_FINISHED.getValue().equals(product.getStatus())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_STATUS_ERROR));
		}
		ProjectType projectType = projectTypeService.get(product.getProjectTypeId());
		model.addAttribute("product", product);
		model.addAttribute("projectType", projectType);
		return "/product/manage/productEstablishVerifyPage";
	}

	/**
	 * 成立审核处理
	 */
	@RequiresPermissions("project:product:ev:auditing")
	@RequestMapping(value = "/product/manage/productEstablishVerify", method = RequestMethod.POST)
	@ResponseBody
	@TokenValid(value = TokenConstants.TOKEN_PRODUCT_ESTABLISH_VERIFY, clear = true)
	@SystemLog(method=SysLogConstant.AUDIT,content=SysLogConstant.PRODUCT_ESTABLISH)
	public Map<String, Object> productEstablishVerify(Product product) {
		product.setOperatorName(getCurrUserName());
		productService.productEstablishVerify(product);
		return renderSuccessResult();
	}

	/**
	 * 产品列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/product/manage/productQuery")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.PRODUCT_LIST)
	public String productQuery(Model model) {
		model.addAttribute("nowTime",System.currentTimeMillis());
		model.addAttribute("webUrl", ConfigUtils.getValue(ConfigConstant.WEB_URL));
		return "/product/manage/productQuery";
	}

	/**
	 * 产品列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/product/manage/productQueryList", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.PRODUCT_LIST)
	public Object productQueryList(Product product) {
		return productService.findManagePage(product);
	}
	
	/**
	 * 产品明细
	 */
	@RequestMapping(value = "/product/manage/productView")
	@SystemLog(method=SysLogConstant.VIEW,content=SysLogConstant.PRODUCT)
	public String productView(Model model,  String id) {
		if (StringUtils.isBlank(id)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_NO_RECORD));
		}
		Product product = productService.get(id);
		ProjectType projectType = projectTypeService.get(product.getProjectTypeId());
		model.addAttribute("product", product);
		model.addAttribute("projectType", projectType);
		return "/product/manage/productView";
	}
	
	/**
	 * 导出产品列表
	 * @param model
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/product/manage/exportProductList")
	@SystemLog(method=SysLogConstant.EXPORT,content=SysLogConstant.BORROW_RECORD)
	public void exportBorrowRecord(final Product model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ExportUtil.exportExcel(new ExportModel<Product>() {
			@Override
			public String getCacheKey() {
				return  ProductManageController.this.getUserId();
			}
			@Override
			public Product getModel() {
				return model;
			}
			@Override
			public int getTotal(Product entity) {
				return productService.getCount(entity);
			}
			@Override
			public List<Product> getPageRecords(Product entity) {
				return productService.findPage(entity).getRows();
			}
		}, request, response);
	}
}
