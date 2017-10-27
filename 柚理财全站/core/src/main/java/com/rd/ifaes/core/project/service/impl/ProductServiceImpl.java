package com.rd.ifaes.core.project.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rd.ifaes.common.annotation.CacheEvict;
import com.rd.ifaes.common.annotation.Cacheable;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.NumberUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TokenUtils;
import com.rd.ifaes.common.validator.BeanValidators;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.cache.ProjectCache;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ProjectConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.DictUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.project.domain.Product;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectType;
import com.rd.ifaes.core.project.domain.ProjectVerifyLog;
import com.rd.ifaes.core.project.mapper.ProductMapper;
import com.rd.ifaes.core.project.service.ProductService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.project.service.ProjectTypeService;
import com.rd.ifaes.core.project.service.ProjectVerifyLogService;
import com.rd.ifaes.core.risk.service.LevelConfigService;
import com.rd.ifaes.core.tpp.model.ufx.UfxProjectUpdateModel;
import com.rd.ifaes.core.tpp.service.tpp.UfxProjectService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.service.UserInvestAutoLogService;
import com.rd.ifaes.core.user.service.UserService;

/**
 * ServiceImpl:ProductServiceImpl
 * 
 * @author FangJun
 * @version 3.0
 * @date 2016-6-20
 */
@Service("productService")
public class ProductServiceImpl extends BaseServiceImpl<ProductMapper, Product> implements ProductService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
	@Resource
	private ProjectTypeService projectTypeService;
	@Resource
	private ProjectService projectService;
	@Resource
	private UserService userService;
	@Resource
	private UserInvestAutoLogService userInvestAutoLogService;
	@Resource
	private UfxProjectService ufxProjectService;
	@Resource
	private ProjectVerifyLogService projectVerifyLogService;
	@Resource
	private LevelConfigService levelConfigService;

	@Override
	@Transactional(readOnly = true)
	public Product get(String id) {
		Product product = super.get(id);
		User user = userService.get(product.getUserId());
		if (user != null) {
			product.setUserName(user.getUserName());
		}
		//风险等级名称
		if(StringUtils.isNotBlank(product.getRiskLevel())){
			product.setRiskLevelName(levelConfigService.findByRiskLevel(product.getRiskLevel()).getRiskLevelName());			
		}
		return product;
	}


	@Override
	@Transactional(readOnly = false)
	public void insert(Product entity) {
		// 校验数据有效性
		BeanValidators.validate(entity);
		entity.preInsert();
		entity.validBase();
		entity.validProduct();

		// 保存借款基本信息
		Project base = entity.prototype();
		base.preInsert();
		base.setProjectNo(projectService.nextProjectNo(DateUtils.getDate()));
		base.setOverdueFeeRate(BigDecimalUtils.round(entity.getOverdueFeeRate().divide(BigDecimal.valueOf(Constant.DOUBLE_ONE_HUNDRED)),Constant.INT_FOUR));
		TokenUtils.validShiroToken(TokenConstants.TOKEN_PRODUCT_ADD);
		
		projectService.insert(base);
		entity.setProjectId(base.getUuid());
		dao.insert(entity);
	}

	@Override
	@Transactional(readOnly = false)
	public void update(Product entity) {
		// 校验数据有效性
		BeanValidators.validate(entity);
		entity.preUpdate();
		entity.validBase();
		entity.validProduct();
		
		Project base = entity.prototype();
		base.preUpdate();
		base.setOverdueFeeRate(BigDecimalUtils.round(entity.getOverdueFeeRate().divide(BigDecimal.valueOf(Constant.DOUBLE_ONE_HUNDRED)),Constant.INT_FOUR));
		TokenUtils.validShiroToken(TokenConstants.TOKEN_PRODUCT_EDIT);
		
		projectService.update(base);
		dao.update(entity);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteBatch(String[] ids) {
		// 只有发布审核失败的才可以删除
		projectService.deleteBatch(ids);
		dao.deleteBatch(ids);
	}

	@Override
	@Transactional(readOnly = false)
	public void save(Product entity) {
		if (StringUtils.isBlank(entity.getProjectId())) {
			insert(entity);
		} else {
			update(entity);
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void publishVerify(Product model) {
		Product product = this.get(model.getUuid());
		if (product == null) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_RECORD_NO_EXISTS));
		}
		if (!LoanEnum.STATUS_WAIT_PUBLISH.eq(product.getStatus())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_STATUS_ERROR));
		}
		if(StringUtils.isBlank(model.getRemark())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_VERIFY_REMARK_NOT_EMPTY));
		}
		if (CommonEnum.YES.getValue().equals(model.getStatus())) {
			product.setStatus(LoanEnum.STATUS_PUBLISH_SUCCESS.getValue());
		} else {
			product.setStatus(LoanEnum.STATUS_PUBLISH_FAIL.getValue());
		}
		
		TokenUtils.validShiroToken(TokenConstants.TOKEN_PRODUCT_PUBLISH_VERIFY);
		
		Project base = product.prototype();
		base.preUpdate();
		base.setPublishVerifyTime(DateUtils.getNow());
		projectService.update(base);
		dao.update(product);

		// 添加审核记录
		projectVerifyLogService.save(model.getProjectId(), model.getOperatorName(), ProjectConstant.PROCESS_NODE_VERIFY,
				DictUtils.getItemName(ProjectConstant.DICT_TYPE_PROJECTSTATUS, product.getStatus()),
				model.getRemark());
	}

	@Override
	@Transactional(readOnly = false)
	@CacheEvict(keys = CacheConstant.KEY_PREFIX_PROJECT_LIST,interval=ExpireTime.ONE_SEC)
	public void productSaleEdit(Product model) {
		long validateStart = System.currentTimeMillis();
		Product product = this.get(model.getUuid());
		if (product == null) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_RECORD_NO_EXISTS));
		}
		if (!LoanEnum.STATUS_PUBLISH_SUCCESS.eq(product.getStatus()) && !LoanEnum.STATUS_RAISING.eq(product.getStatus())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_STATUS_ERROR));
		}
		//定向销售规则校验
		model.validBeforeSale();
		//即息理财-校验
		if(!ConfigUtils.supportInterestFinancing() && product.isInterestFinancing()){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_NOSUPPORT_INTEREST_FINANCING));
		}
		
		// 可变现校验
		if(LoanEnum.REALIZE_USEFUL.eq(model.getRealizeUseful())){
//			TODO 1.利率为固定利率；(暂缺)
//			2.还款方式为一次性还款、每月还息到期还本、每季还息到期；
//			3.设置了产品期限；
			if(!(LoanEnum.STYLE_ONETIME_REPAYMENT.eq(product.getRepayStyle()) 
				|| LoanEnum.STYLE_MONTHLY_INTEREST.eq(product.getRepayStyle()) 
				|| LoanEnum.STYLE_QUARTER_INTEREST.eq(product.getRepayStyle()))){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.REALIZE_NOWREPAYMENTSTYLE_NOT));
			}
			if(product.getTimeLimit() <= 0){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.REALIZE_NOTLIMITTIME_NOT));
			}
			if(!NumberUtils.isDefault(product.getFixedRepayDay())){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.REALIZE_FIXEDREPAYMENTTIME_NOT));
			}
		}
		//类别检验
		final ProjectType projectType = projectTypeService.get(model.getProjectTypeId());
		if (projectType == null) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_PROJECT_TYPE_ID_NOT_EXISTS));
		}
		model.setProtocolId(projectType.getProtocolId());//上架的时候添加protocolId
		Project base = model.prototype();
		if (LoanEnum.STATUS_PUBLISH_SUCCESS.eq(product.getStatus())) {
			// 判断是否开启自动投资
			if (product.checkOpenAuto(base)) {
				LOGGER.info("产品允许自动投资，产品uuid："+product.getUuid());
				CacheUtils.set(base.getUuid()+CacheConstant.KEY_AUTOINVEST_AUTOSTATUS, Constant.FLAG_INT_YES, ExpireTime.ONE_MIN);
			}
			// 未上架 记录，修改状态
			base.setStatus(LoanEnum.STATUS_RAISING.getValue());
		}

		TokenUtils.validShiroToken(TokenConstants.TOKEN_PRODUCT_SALE);
		long validateEnd = System.currentTimeMillis();
		LOGGER.info("validate time:{} ms",(validateEnd-validateStart));		
		
		model.setContent(null);// 不更新产品详情
		base.setUpdateTime(DateUtils.getNow());
		if(model.getSaleTime().after(DateUtils.getNow())){
			base.setStage(LoanEnum.STAGE_SHOW.getInt());			
		}else{
			base.setStage(LoanEnum.STAGE_SALE.getInt());	
		}
		projectService.update(base);
		
		// 上架时，缓存项目可投金额
		ProjectCache.setRemainAccount(product.getUuid(), product.getAccount().doubleValue());
		// end
		
		//删除project 缓存
		 CacheUtils.del(CacheConstant.KEY_PROJECT_UUID_PREFIX.concat(model.getUuid()));
		 dao.update(model);
		 long persistentEnd = System.currentTimeMillis();
		 LOGGER.info("persistent time:{} ms",(persistentEnd - validateEnd));
		
		// 发布项目
		if(LoanEnum.STATUS_PUBLISH_SUCCESS.eq(product.getStatus())){
			LOGGER.info("进入三方的发布项目.....");
			ufxProjectService.tppProjectApply(model.getUuid());
		}
		long ufxPublishEnd = System.currentTimeMillis();
		LOGGER.info("ufxPublish time:{} ms",(ufxPublishEnd - persistentEnd));
		LOGGER.info("上架操作完成");
		// 添加审核记录
		projectVerifyLogService.save(model.getProjectId(), model.getOperatorName(), ProjectConstant.PROCESS_NODE_SALE,
				DictUtils.getItemName(ProjectConstant.DICT_TYPE_PROJECTSTATUS, base.getStatus()),
				model.getVerifyRemark());
		long saleEnd = System.currentTimeMillis();
		LOGGER.info("product sale time:{} ms",(saleEnd - validateStart));
	}

	@Override
	@CacheEvict(keys = {CacheConstant.KEY_PREFIX_PROJECT_LIST,
			CacheConstant.KEY_PREFIX_PROJECT_DETAIL+ "{model.projectId}",
			CacheConstant.KEY_PROJECT_UUID_PREFIX + "{model.projectId}"},interval=ExpireTime.ONE_SEC)
	public void productStop(Product model) {
		Product product = this.get(model.getUuid());
		if (product == null) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_RECORD_NO_EXISTS));
		}
		if (!LoanEnum.STATUS_RAISING.eq(product.getStatus())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_STATUS_ERROR));
		}
		Project base = model.prototype();
		Date now = DateUtils.getNow();
		base.setUpdateTime(now);
		base.setRaiseEndTime(now);
		base.setStage(LoanEnum.STAGE_REPAY.getInt());
		projectService.update(base);
		
		String status = LoanEnum.STATUS_RAISE_FINISHED.getValue();
		if (Constant.INT_ZERO== product.getRaiseTimeLimit() && BigDecimalUtils.validAmount(product.getAccountYes())) {
			// 没有募集期的产品(活期产品)进行下架操作，则该产品成立(进入还款中)
			projectService.updateStatus(base.getUuid(), LoanEnum.STATUS_REPAY_START.getValue(),LoanEnum.STATUS_RAISING.getValue());
		} else if (LoanEnum.STATUS_RAISING.eq(product.getStatus())) {
			if (product.getSaleTime().after(now) || !BigDecimalUtils.validAmount(product.getAccountYes())) {
				// 对已上架的（未开售 或已投金额为零） 的产品进行下架操作 ，则默认该产品不成立，不需要成立审核。
				projectService.updateStatus(base.getUuid(), LoanEnum.STATUS_NOT_ESTABLISH.getValue(),LoanEnum.STATUS_RAISING.getValue());
				ufxProjectService.tppProjectUpdate(model.getUuid(), UfxProjectUpdateModel.PROJECT_STATE_OVERFLOW);
				status =  LoanEnum.STATUS_NOT_ESTABLISH.getValue();
			} else {
				// 对募集期未结束的产品进行下架操作，则该产品暂停销售，进入成立待审
		   	projectService.updateStatus(base.getUuid(), LoanEnum.STATUS_RAISE_FINISHED.getValue(),LoanEnum.STATUS_RAISING.getValue());
			}
		}
		
		// 添加审核记录
		projectVerifyLogService.save(model.getProjectId(), model.getOperatorName(),
				ProjectConstant.PROCESS_NODE_STOP,
				DictUtils.getItemName(ProjectConstant.DICT_TYPE_PROJECTSTATUS, status), model.getVerifyRemark());
		TokenUtils.validShiroToken(TokenConstants.TOKEN_PRODUCT_STOP);
		projectService.removeInvestBespeak(product.getProjectId());
		 
	}

	/**
	 * 产品成立审核
	 * 
	 * @param product 产品
	 * @return
	 */
	@Override
	@CacheEvict(keys = {CacheConstant.KEY_PREFIX_PROJECT_LIST,
	        CacheConstant.KEY_PREFIX_PROJECT_DETAIL+ "{model.projectId}",
			CacheConstant.KEY_PROJECT_UUID_PREFIX + "{model.projectId}"},interval=ExpireTime.ONE_SEC)
	public void productEstablishVerify(Product model) {
		String status = LoanEnum.STATUS_REPAY_START.getValue();
		if (CommonEnum.YES.eq(model.getStatus())) {
			LOGGER.info("项目({})审核成立,BEGIN............", model.getUuid());
			projectService.verifySuccess(model.getUuid());
		} else {
			LOGGER.info("项目({})审核不成立,将进行退款处理............", model.getUuid());
			projectService.cancel(model.getUuid());
			ufxProjectService.tppProjectUpdate(model.getUuid(), UfxProjectUpdateModel.PROJECT_STATE_OVERFLOW);
			status = LoanEnum.STATUS_NOT_ESTABLISH.getValue();
		}
		LOGGER.info("项目({})审核处理完毕............", model.getUuid());
		// 添加审核记录
		projectVerifyLogService.save(model.getProjectId(), model.getOperatorName(),
				ProjectConstant.PROCESS_NODE_ESTABLISHVERIFY,
				DictUtils.getItemName(ProjectConstant.DICT_TYPE_PROJECTSTATUS, status), model.getVerifyRemark());
				
	}

	@Override
	@Cacheable(key = CacheConstant.KEY_PREFIX_ORGPROJECT_CONTENT , expire = ExpireTime.FIVE_MIN)
	public String getOrgProduct(String projectId) {
		Product	product = super.get(projectId);
		return product.getContent();
	}


	@Override
	public Object findManagePage(Product product) {
		Page<Product> page = product.getPage();
		if(page==null){
			page=new Page<Product>();
		}
		List<Product> list =  dao.productQuery(product);
		if(CollectionUtils.isNotEmpty(list)){
			fillVerifyRemark(list);
		}
		page.setRows(list);
		return page;
	}
	
	private void fillVerifyRemark(List<Product> rows){
		int i = 0;
		String[] projectIds = new String[rows.size()];
		for(Product borrow : rows){
			projectIds[i] = borrow.getProjectId();
			i++;
		}		
		List<ProjectVerifyLog> logs = projectVerifyLogService.findRemarkByProjectIds(projectIds);
		for(Product borrow : rows){
			for (ProjectVerifyLog log : logs) {
				if(borrow.getProjectId().equals(log.getProjectId())){
					borrow.setVerifyRemark(log.getRemark());
				}
			}
		}			
	}

	@Override
	public String getRealizeUsefulByProjectId(String projectId) {
		return dao.getRealizeUsefulByProjectId(projectId);
	}
}