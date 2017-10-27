package com.rd.ifaes.core.project.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.rd.ifaes.common.annotation.CacheEvict;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.dict.WorkflowEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.security.shiro.IdGen;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.PrincipalUtils;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TokenUtils;
import com.rd.ifaes.common.util.resource.BondResource;
import com.rd.ifaes.common.validator.BeanValidators;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.cache.ProjectCache;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ProjectConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.constant.URLConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.DictUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.credit.service.UserCreditLineService;
import com.rd.ifaes.core.project.domain.Borrow;
import com.rd.ifaes.core.project.domain.BorrowUpload;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectType;
import com.rd.ifaes.core.project.domain.ProjectVerifyLog;
import com.rd.ifaes.core.project.mapper.BorrowMapper;
import com.rd.ifaes.core.project.service.BorrowFollowService;
import com.rd.ifaes.core.project.service.BorrowService;
import com.rd.ifaes.core.project.service.BorrowUploadService;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.project.service.ProjectTypeService;
import com.rd.ifaes.core.project.service.ProjectVerifyLogService;
import com.rd.ifaes.core.project.worker.ProjectWorker;
import com.rd.ifaes.core.risk.service.LevelConfigService;
import com.rd.ifaes.core.sys.domain.DictData;
import com.rd.ifaes.core.sys.service.DictDataService;
import com.rd.ifaes.core.tpp.service.tpp.UfxProjectService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.domain.UserIdentify;
import com.rd.ifaes.core.user.model.UserFreezeModel;
import com.rd.ifaes.core.user.model.UserIdentifyModel;
import com.rd.ifaes.core.user.service.UserAuthSignLogService;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserCompanyInfoService;
import com.rd.ifaes.core.user.service.UserFreezeService;
import com.rd.ifaes.core.user.service.UserIdentifyService;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.core.workflow.model.WorkflowBaseModel;
import com.rd.ifaes.core.workflow.service.WorkflowService;

/**
 * ServiceImpl:BorrowServiceImpl
 * @author FangJun
 * @version 3.0
 * @date 2016-6-20
 */
@Service("borrowService") 
public class BorrowServiceImpl  extends BaseServiceImpl<BorrowMapper, Borrow> implements BorrowService{
	private static final Logger  LOGGER=LoggerFactory.getLogger(BorrowServiceImpl.class);
	@Resource
	private ProjectTypeService projectTypeService;
	@Resource
	private ProjectService projectService;
	@Resource
	private UserService userService;
	@Resource
	private UserCacheService userCacheService;
	@Resource
	private BorrowUploadService borrowUploadService;
	@Resource
	private UfxProjectService ufxProjectService;
	@Resource
	private ProjectInvestService projectInvestService;
	@Resource
	private WorkflowService workflowService;
	@Resource
	private DictDataService dictDataService;
	@Resource
	private UserCompanyInfoService userCompanyInfoService;
	@Resource
	private BorrowFollowService borrowFollowService;
	@Resource
	private ProjectVerifyLogService projectVerifyLogService;
	@Resource
	private LevelConfigService levelConfigService;
	@Resource
	private UserAuthSignLogService userAuthSignLogService;
	@Resource
	private UserIdentifyService userIdentifyService;
	@Resource
	private UserCreditLineService userCreditLineService;
	
	@Override
	public Borrow get(String id) {
		Borrow borrow = super.get(id);
		User user = userService.get(borrow.getUserId());
		borrow.setUserName(user.getUserName());
		borrow.setRealName(user.getRealName());
		borrow.setMobile(user.getMobile());
		//风险等级名称
		if(StringUtils.isNotBlank(borrow.getRiskLevel())){
			borrow.setRiskLevelName(levelConfigService.findByRiskLevel(borrow.getRiskLevel()).getRiskLevelName());			
		}
		return borrow;
	}

	@Override
	@Transactional
	public void insert(Borrow entity) {
		
		BeanValidators.validate(entity);
		entity.preInsert();
		entity.validBase();
		entity.validBorrow();
		
		//保存project信息
		Project project = entity.prototype();
		project.setCostManage(entity.getCostManage());
		project.preInsert();		
		project.setProjectNo(projectService.nextProjectNo(DateUtils.getDate()));		
		project.setOverdueFeeRate(BigDecimalUtils.round(entity.getOverdueFeeRate().divide(BigDecimal.valueOf(Constant.DOUBLE_ONE_HUNDRED)),Constant.INT_FOUR));
		
		//信用检查
		if(LoanEnum.PROJECT_TYPE_CREDIT.getValue().equals(entity.getProjectType())){
			//扣除信用额度
			if(!userCreditLineService.subCreditByAccount(entity.getUserId(), entity.getAccount(), 
					project.getProjectName() + "-" + project.getProjectNo())){
				//扣除信用积分失败，信用额度不足
				throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_CREDIT_INSUFFICIENT, userService.get(entity.getUserId()).getUserName(), entity.getAccount().toString()));
			}
		}
		
		TokenUtils.validShiroToken(TokenConstants.TOKEN_BORROW_ADD);
		
		projectService.insert(project);
		
		String projectId = project.getUuid();
		entity.setProjectId(projectId);
		//保存借款资料
		saveBorrowUpload(entity, false);
		dao.insert(entity);
		
	}
	
	
	@Override
	@Transactional
	public void update(Borrow entity) {
		
		int notEditableCount = projectService.getNotEditableCount(new String[]{entity.getUuid()});
		if(notEditableCount > 0){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_STATUS_ERROR));
		}
		BeanValidators.validate(entity);
		entity.preUpdate();
		entity.validBase();
		entity.validBorrow();
		
		//判断项目状态
		Project pmodel = projectService.get(entity.getUuid());
 
		Project project = entity.prototype();
		project.preUpdate();	
		project.setOverdueFeeRate(BigDecimalUtils.round(entity.getOverdueFeeRate().divide(BigDecimal.valueOf(Constant.DOUBLE_ONE_HUNDRED)),Constant.INT_FOUR));
		
		//信用检查
		if(LoanEnum.PROJECT_TYPE_CREDIT.getValue().equals(entity.getProjectType())){
			//扣除信用额度
			if(!userCreditLineService.subCreditByAccount(entity.getUserId(), entity.getAccount(), 
					project.getProjectName() + "-" + pmodel.getProjectNo())){
				//扣除信用积分失败，信用额度不足
				throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_CREDIT_INSUFFICIENT, userService.get(entity.getUserId()).getUserName(), entity.getAccount().toString()));
			}
		}
		
		TokenUtils.validShiroToken(TokenConstants.TOKEN_BORROW_EDIT);
		
		projectService.update(project);
		
		//如果有担保公司并且担保公司审核拒绝-初始化担保审核状态
		if(Constant.FLAG_YES.equals(pmodel.getIsVouch()) && LoanEnum.VOUCH_STATUS_WAIT_FAIL.eq(pmodel.getVouchStatus())){
			project.setVouchStatus(LoanEnum.VOUCH_STATUS_WAIT_VERIFY.getValue());
			projectService.updateVouchStatus(project);
		}
		
		//保存借款资料
		saveBorrowUpload(entity, true);
		
		dao.update(entity);
	}
	
	@Override
	@Transactional
	public void verify(Borrow model) {
		Borrow borrow = dao.get(model.getProjectId());
		if(borrow == null){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_NO_RECORD));
		}
		if(!LoanEnum.STATUS_WAIT_PUBLISH.eq(borrow.getStatus())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_STATUS_ERROR));
		}
		model.checkVerify();
		//执行工作流
		//executeProcess(borrow);	
		Project base = model.prototype();
		base.preUpdate();
		
		TokenUtils.validShiroToken(TokenConstants.TOKEN_BORROW_VERIFY);
		
		base.setPublishVerifyTime(DateUtils.getNow());
		projectService.update(base);
		if(LoanEnum.STATUS_PUBLISH_FAIL.getValue().equals(model.getStatus())){
			//发布审核被拒
			Project project = projectService.get(model.getProjectId());
			if(LoanEnum.PROJECT_TYPE_CREDIT.getValue().equals(project.getProjectType())){
				//信用标，返还用户的信用额度
				userCreditLineService.addCreditByAccount(project.getUserId(), project.getAccount(), project.getProjectName() + "-" + project.getProjectNo());
			}
		}
		// 添加审核记录
		projectVerifyLogService.save(model.getProjectId(), model.getOperatorName(),
				ProjectConstant.PROCESS_NODE_VERIFY,
				DictUtils.getItemName(ProjectConstant.DICT_TYPE_PROJECTSTATUS, model.getStatus()),
				model.getVerifyRemark());
	}
	
	@Override
	@Transactional
	@CacheEvict(keys = {CacheConstant.KEY_PREFIX_PROJECT_LIST, CacheConstant.KEY_PROJECT_UUID_PREFIX + "{model.projectId}"})
	public void sale(Borrow model) {
		long validateStart = System.currentTimeMillis();
		LOGGER.info("是否允许转让：{}",model.getBondUseful());
		final Project proj = projectService.getProjectByUuid(model.getProjectId());
		//上架前校验
		model.validBeforeSale();
		// 借贷校验
		validForBorrow(proj,model);
		Project project = model.prototype();
		// 判断是否开启自动投资
		if(proj.checkOpenAuto(project)){
			LOGGER.info("产品允许自动投资，产品uuid："+project.getUuid());
			CacheUtils.set(project.getUuid()+CacheConstant.KEY_AUTOINVEST_AUTOSTATUS, Constant.FLAG_INT_YES, ExpireTime.ONE_MIN);
		}
		// 修改状态
		project.setStatus(LoanEnum.STATUS_RAISING.getValue());
		
		if(model.getSaleTime().after(DateUtils.getNow())){
			project.setStage(LoanEnum.STAGE_SHOW.getInt());			
		}else{
			project.setStage(LoanEnum.STAGE_SALE.getInt());	
		}
		TokenUtils.validShiroToken(TokenConstants.TOKEN_BORROW_SALE);
		long validateEnd = System.currentTimeMillis();
		LOGGER.info("validate time:{} ms",(validateEnd-validateStart));		
		
		projectService.update(project);
		model.setContent(null);
		LOGGER.info("是否允许转让：{}",model.getBondUseful());
		dao.update(model);
		long persistentEnd = System.currentTimeMillis();
		LOGGER.info("persistent time:{} ms",(persistentEnd - validateEnd));
		 
		//上架后project放入缓存
		ProjectCache.setRemainAccount(proj.getUuid(), proj.getAccount().doubleValue());
		
		if(LoanEnum.STATUS_PUBLISH_SUCCESS.eq(proj.getStatus())){//发布前判断状态，防止重复发布项目
			//发布项目
			ufxProjectService.tppProjectApply(project.getUuid());
		}
		long ufxPublishEnd = System.currentTimeMillis();
		LOGGER.info("ufxPublish time:{} ms",(ufxPublishEnd - persistentEnd));
		LOGGER.info("上架操作完成");
		// 添加审核记录
		projectVerifyLogService.save(model.getProjectId(), model.getOperatorName(),
				ProjectConstant.PROCESS_NODE_SALE,
				DictUtils.getItemName(ProjectConstant.DICT_TYPE_PROJECTSTATUS, project.getStatus()),
				model.getVerifyRemark());
		long saleEnd = System.currentTimeMillis();
		LOGGER.info("borrow sale total time:{} ms",(saleEnd - validateStart));
	}
	
	/**
	 * 借贷校验方法
	 * @author fxl
	 * @date 2016年9月23日
	 * @param proj
	 * @param model
	 */
	public void validForBorrow(Project proj,Borrow model){
		if(proj == null){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_NO_RECORD));
		}
		if(!LoanEnum.STATUS_PUBLISH_SUCCESS.eq(proj.getStatus()) && !LoanEnum.STATUS_RAISING.eq(proj.getStatus())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_STATUS_ERROR));
		}
		// 类别校验
		if (StringUtils.isBlank(model.getProjectTypeId())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_PROJECT_TYPE_ID_NOT_EMPTY));
		}
		final ProjectType projectType = projectTypeService.get(model.getProjectTypeId());
		if (projectType == null) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_PROJECTTYPE_NOEXISTS));
		}
		model.setProtocolId(projectType.getProtocolId());//上架的时候添加protocolId
	}
	
	
	@Override
	@Transactional
	@CacheEvict(keys = {CacheConstant.KEY_PREFIX_PROJECT_LIST,	
			CacheConstant.KEY_PREFIX_PROJECT_DETAIL+ "{model.projectId}",
			CacheConstant.KEY_PROJECT_UUID_PREFIX + "{model.projectId}"},interval=ExpireTime.ONE_SEC)
	public void stop(Borrow model) {		
		Project pro = projectService.get(model.getUuid());
		if(pro == null){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_NO_RECORD));
		}
		if (model.getVerifyRemark().length()>Constant.ENTITY_LENGTH_THREE) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.BORROW_STOP_REMARK_OVER_LENGTH));
		}
		Project project = model.prototype();
		if(pro.getAccountYes() == null || pro.getAccountYes().compareTo(BigDecimal.ZERO) == 0){//尚未投资，直接撤销项目
//		    ufxProjectService.tppProjectUpdate(model.getUuid(), UfxProjectUpdateModel.PROJECT_STATE_OVERFLOW);		
			project.setStatus(LoanEnum.STATUS_NOT_ESTABLISH.getValue());
			if(LoanEnum.PROJECT_TYPE_CREDIT.getValue().equals(pro.getProjectType())){
				//信用标流标，返还用户的信用额度
				userCreditLineService.addCreditByAccount(pro.getUserId(), pro.getAccount(), pro.getProjectName() + "-" + pro.getProjectNo());
			}
		}else{			
			project.setStatus(LoanEnum.STATUS_RAISE_FINISHED.getValue());
		}	
		project.setStopTime(DateUtils.getNow());
		project.setStage(LoanEnum.STAGE_REPAY.getInt());
		projectService.update(project);
		// 添加审核记录
		projectVerifyLogService.save(model.getProjectId(), model.getOperatorName(),
				ProjectConstant.PROCESS_NODE_STOP,
				DictUtils.getItemName(ProjectConstant.DICT_TYPE_PROJECTSTATUS, project.getStatus()),
				model.getVerifyRemark());
		TokenUtils.validShiroToken(TokenConstants.TOKEN_BORROW_STOP);
		projectService.removeInvestBespeak(pro.getUuid());
	}
	
	@Override
	@CacheEvict(keys = {CacheConstant.KEY_PREFIX_PROJECT_LIST,	
			CacheConstant.KEY_PREFIX_PROJECT_DETAIL+ "{model.projectId}",
			CacheConstant.KEY_PROJECT_UUID_PREFIX + "{model.projectId}"},interval=ExpireTime.ONE_SEC)
	public void establishVerify(Borrow model) {
		if (LoanEnum.STATUS_NOT_ESTABLISH.getValue().equals(model.getStatus())) {// 成立审核不通过
		     LOGGER.info("项目({})审核不成立,将进行退款处理............",model.getUuid());
			projectService.cancel(model.getUuid());
		} else {
			 LOGGER.info("项目({})审核成立,BEGIN............",model.getUuid());
			projectService.verifySuccess(model.getUuid());
		}
		LOGGER.info("项目({})审核处理完毕............",model.getUuid());
		// 添加审核记录
		projectVerifyLogService.save(model.getProjectId(), model.getOperatorName(),
				ProjectConstant.PROCESS_NODE_ESTABLISHVERIFY,
				DictUtils.getItemName(ProjectConstant.DICT_TYPE_PROJECTSTATUS, model.getStatus()),
				model.getVerifyRemark());
			
	}
	
	@Override
	public Page<Borrow> findRecordPage(Borrow entity) {
		Page<Borrow> page = findRecordPage(entity, false);
		return page;
	}
	
	/**
	 * 查询合同管理记录
	 */
	@Override
	public Page<Borrow> findRecordPage(Borrow entity, boolean flag) {
		Page<Borrow> page = entity.getPage();
		List<Borrow> rows = null ;
		/*
		 * 是否是合同查询
		 */
		if(flag) {
			rows = dao.findByContract(entity) ;
		}else {
			rows = dao.findRecord(entity);
		}
		boolean userResult = false;
		boolean borrowResult = false;
		if(StringUtils.isBlank(entity.getKeywords()) && StringUtils.isBlank(entity.getBorrower())){
			userResult = true;
		}
		if(StringUtils.isBlank(entity.getBorrowNature())){
			borrowResult = true;
		}
		if(rows != null && rows.size() > 0 && (userResult || borrowResult)){
			fillUser(rows,userResult,borrowResult);
		}
		if(CollectionUtils.isNotEmpty(rows)){
			fillVerifyRemark(rows);
		}
		page.setRows(rows);
		return page;
	}
	
	
	@Override
	public Page<Borrow> findBorrowFollowPage(Borrow entity) {
		//查询状态为 还款中的借款 
		// 还款中 8  逾期中87   坏账 88  部分还款90 
		String[] statusSet ={LoanEnum.STATUS_REPAY_START.getValue(),
							 LoanEnum.STATUS_REPAY_OVERDUE.getValue(),
							 LoanEnum.STATUS_BAD_DEBT.getValue() };
		entity.setStatusSet(statusSet);
		Page<Borrow> page = entity.getPage();
		List<Borrow> list = dao.findList(entity);
		boolean userResult = false;
		boolean borrowResult = false;
		if(StringUtils.isBlank(entity.getKeywords()) && StringUtils.isBlank(entity.getBorrower())){
			userResult = true;
		}
		if(StringUtils.isBlank(entity.getBorrowNature())){
			borrowResult = true;
		}
		if(list != null && list.size() > 0 && (userResult || borrowResult)){
			fillUser(list,userResult,borrowResult);
		}
		page.setRows(list);
		return page;
	}
	@Override
	public Borrow addBorrow(final Borrow entity, final User user) {
		entity.setUserId(user.getUuid());
		// 设置最低投资金额
		BigDecimal borrowLowestInvestAccount = ConfigUtils.getBigDecimal(ConfigConstant.BORROW_LOWEST_INVEST_ACCOUNT);
		if(borrowLowestInvestAccount.doubleValue() > Constant.INT_ZERO){
			entity.setLowestAccount(borrowLowestInvestAccount);
		}else{
			entity.setLowestAccount(BigDecimal.ZERO);
		}
		entity.webValid();
		entity.preInsert();
		entity.validBorrow();
		// 初始化数据
		entity.setInterestManageRate(ConfigUtils.getBigDecimal(ConfigConstant.INTEREST_MANAGE_RATE));
		entity.setInterestStyle(LoanEnum.INTEREST_STYLE_EV.getValue());
		entity.setInterestStartDays(Constant.INT_ZERO);
		entity.setMostAccount(BigDecimal.ZERO);
		entity.setStatus(LoanEnum.STATUS_NEW.getValue());
		entity.setOverdueFeeRate(ConfigUtils.getBigDecimal(ConfigConstant.OVERDUE_FEE));
		entity.setBorrowManageRate(ConfigUtils.getBigDecimal(ConfigConstant.BORROW_MANAGE_RATE));
		entity.setCostManage(BigDecimalUtils.div(BigDecimalUtils.mul(entity.getAccount(), entity.getBorrowManageRate()), new BigDecimal("100")));
		// 获取预计利息总额
		final ProjectWorker work = new ProjectWorker(entity.prototype());
		entity.setRepaymentInterest(work.getRepaymentInterest(null));
		
		TokenUtils.validShiroToken(TokenConstants.TOKEN_WEB_BORROW_ADD);
		return entity;
	}
	
	@Override
	public void doAddBorrow(final Borrow entity) {
		//保存project信息
		final Project project = entity.prototype();
		project.preInsert();		
		project.setProjectNo(projectService.nextProjectNo(DateUtils.getDate()));
		TokenUtils.validShiroToken(TokenConstants.TOKEN_WEB_BORROW_DO_ADD);
		projectService.insert(project);
		
		final String projectId = project.getUuid();
		entity.setProjectId(projectId);
		//保存借款资料
		saveBorrowUpload(entity, false);
		
		final int n = dao.insert(entity);
		if(n <= 0){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_MSG_SUCCESS), BussinessException.TYPE_CLOSE);
		}
		SessionUtils.removeAttribute("webBorrow");
	}
	
	/*************************************** private method ***************************************/
	/**
	 * 保存借款资料
	 * @param entity
	 * @param update
	 */
	private void saveBorrowUpload(final Borrow entity,final boolean update){
		final String projectId = entity.getProjectId();		
		if(update){
			entity.setHasPawn(Constant.FLAG_NO);
			//删除表中原有记录
			borrowUploadService.deleteByProjectId(projectId, new String[]{BorrowUpload.FILE_TYPE_BORROW,BorrowUpload.FILE_TYPE_MORTGAGE});
		}
		
		final List<String> saveFilePathList = new ArrayList<>();
		//保存资料信息
		final List<BorrowUpload> uploadList = new ArrayList<>();
		//借款资料
		final String[] picPath = entity.getPicPath();
		if(picPath != null && picPath.length > 0){//借款资料			
			int index = 0;
			for (final String pp : picPath) {
				if(saveFilePathList.contains(pp)){
					continue;
				}
				saveFilePathList.add(pp);
				final BorrowUpload upload = new BorrowUpload();
				upload.setUuid(IdGen.uuid());
				upload.setAddTime(DateUtils.getNow());
				upload.setProjectId(projectId);
				upload.setFilePath(pp);
				upload.setFileType(BorrowUpload.FILE_TYPE_BORROW);
				upload.setSort(index++);
				uploadList.add(upload);				
			}
		}
		
		//抵押物资料
		final String[] mtgPath = entity.getMtgPath();
		if(mtgPath != null && mtgPath.length > 0){
			int index = 0;
			for (final String mp : mtgPath) {
				if(saveFilePathList.contains(mp)){
					continue;
				}
				saveFilePathList.add(mp);
				final BorrowUpload upload = new BorrowUpload();
				upload.setUuid(IdGen.uuid());
				upload.setAddTime(DateUtils.getNow());
				upload.setProjectId(projectId);
				upload.setFilePath(mp);
				upload.setFileType(BorrowUpload.FILE_TYPE_MORTGAGE);
				upload.setSort(index++);
				uploadList.add(upload);	
				entity.setHasPawn(Constant.FLAG_YES);
			}
		}		
		
		if(!uploadList.isEmpty()){
			borrowUploadService.insertBatch(uploadList);			
		}
	}	
	
	/**
	 * 执行当前任务
	 * @author lh
	 * @date 2016年8月3日
	 * @param borrow	
	 */
	
	public void executeProcess(Borrow borrow){
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(WorkflowEnum.PARAMS_NAME_AUDIT_OPINION.getValue(), borrow.getVerifyRemark());
		
		WorkflowBaseModel workflowBaseModel = new WorkflowBaseModel();
		workflowBaseModel.setOperator(PrincipalUtils.getCurrUserId());
//		workflowBaseModel.setTaskId(taskId);
		workflowBaseModel.setActionName(WorkflowBaseModel.ActionName.COMMIT.name());
//		workflowBaseModel.setProcessInstanceId(processInstanceId);
		
		//Task task = workflowService.executeProcess(borrow.getProjectName(), parameters, workflowBaseModel);
		
	}

	@Override
	public Map<String, Object> getBorrowDetail(final Borrow borrow) {
		final Map<String, Object> data = new HashMap<String, Object>();
		// 获取借款信息
		if(borrow != null){
			data.put("borrow", borrow);
		}
		// 借款方式
		final List<DictData> repayStyleList = dictDataService.findListByDictType(DictTypeEnum.REPAY_STYLE.getValue());
		data.put("repayStyleList", repayStyleList);
		// 借款方式
		final List<DictData> borrowUseList = dictDataService.findListByDictType(DictTypeEnum.BORROW_USE.getValue());
		data.put("borrowUseList", borrowUseList);
		// 担保机构
		User model = new User();
		model.setIsVouch(User.USER_NATURE_VOUCH);
		model.setRealNameStatus(Constant.FLAG_YES);
		final List<User> userCompanyList = userService.findBorrowerList(model);
		data.put("userCompanyList", userCompanyList);
		data.put("borrowMinAccount", ConfigUtils.getDouble(ConfigConstant.BORROW_LOWEST_ACCOUNT));
		data.put("borrowMaxAccount", ConfigUtils.getDouble(ConfigConstant.BORROW_MOST_ACCOUNT));
		// 年化利率区间
		double borrowMinApr = ConfigUtils.getDouble(ConfigConstant.BORROW_LOWEST_APR);
		double borrowMaxApr = ConfigUtils.getDouble(ConfigConstant.BORROW_MOST_APR);
		data.put("borrowMinApr", borrowMinApr);
		data.put("borrowMaxApr", borrowMaxApr);
		return data;
	}

	@Override
	public Borrow getBorrow(final String projectId,final String backUrl) {
		final Borrow borrow = dao.get(projectId);
		if(borrow == null){
			throw new BussinessException(ResourceUtils.get(BondResource.PROJECT_NOT_EXISTS), backUrl ,BussinessException.TYPE_JSON);
		}
		return borrow;
	}

	@Override
	public String getBondUsefulByProjectId(String projectId) {
		return dao.getBondUsefulByProjectId(projectId);
	}

	@Override
	public Map<String, Object> checkUserBorrow(User user) {
		Map<String, Object> data = Maps.newHashMap();
		data.put("result", false);
		// 判断是否实名和授权
		final UserIdentify userIdentify = userIdentifyService.findByUserId(user.getUuid());
		if (!UserIdentifyModel.STATUS_REALNAME_SUCC.equals(userIdentify.getRealNameStatus()) || StringUtils.isBlank(user.getTppUserCustId())) {
			data.put("msg", ResourceUtils.get(ResourceConstant.USER_REAL_NAME_STATUS));
			data.put("url", URLConstant.SECURITY_REALNAMEIDENTIFY);
			data.put("buttonName", ResourceUtils.get(ResourceConstant.USER_REAL_NAME_GO_OPEN));
			return data;
		}
		if (userAuthSignLogService.checkAuthSign() && !UserIdentifyModel.STATUS_AUTH_SIGN_SUCC.equals(userIdentify.getAuthSignStatus())) {
			data.put("msg", ResourceUtils.get(ResourceConstant.USER_AUTH_SIGN_STATUS_ERROR));
			data.put("url", URLConstant.SECURITY_AUTHSIGN);
			data.put("buttonName", ResourceUtils.get(ResourceConstant.AUTO_INVEST_RULE_LOG_OPEN_AUTH_SIGN));
			return data;
		}
		// 判断用户类型
		final UserCache userCache = userCacheService.findByUserId(user.getUuid());
		if(!UserCache.USER_NATURE_PERSON.equals(userCache.getUserNature()) && !UserCache.USER_NATURE_COMPANY.equals(userCache.getUserNature())){
			data.put("msg", ResourceUtils.get(ResourceConstant.USER_NATURE_ERROR));
			data.put("url", URLConstant.BORROW_BESPEAK);
			data.put("buttonName", ResourceUtils.get(ResourceConstant.COMMON_GO_BACK));
			return data;
		}
		//用户借款冻结校验
		UserFreezeService userFreezeService = (UserFreezeService)SpringContextHolder.getBean("userFreezeService");
		if(userFreezeService.isFreezed(user.getUuid(), UserFreezeModel.STATUS_USER_FREEZE_LOAN)){
			data.put("msg", ResourceUtils.get(ResourceConstant.PRODUCT_BORROWER_LOAN_FREEZE));
			data.put("url", URLConstant.BORROW_BESPEAK);
			data.put("buttonName", ResourceUtils.get(ResourceConstant.COMMON_GO_BACK));
			return data;
		}
		data.put("result", true);
		return data;
	}
	
	public Page<Borrow> findBasePage(Borrow entity) {
		Page<Borrow> pageList = entity.getPage();
		List<Borrow> rows = dao.findList(entity);
		for (Borrow borrow : rows) {
			LOGGER.info("待处理：{}", borrow.getCostManage());
		}
		boolean userResult = false;
		boolean borrowResult = false;
		if(StringUtils.isBlank(entity.getKeywords()) && StringUtils.isBlank(entity.getBorrower())){
			userResult = true;
		}
		if(StringUtils.isBlank(entity.getBorrowNature())){
			borrowResult = true;
		}
		if(rows != null && rows.size() > 0 && (userResult || borrowResult)){
			fillUser(rows,userResult,borrowResult);
		}
		pageList.setRows(rows);
		return pageList;
	}
	
	private void fillVerifyRemark(List<Borrow> rows){
		int i = 0;
		String[] borrowIds = new String[rows.size()];
		for(Borrow borrow : rows){
			borrowIds[i] = borrow.getProjectId();
			i++;
		}		
		List<ProjectVerifyLog> logs = projectVerifyLogService.findRemarkByProjectIds(borrowIds);
		for(Borrow borrow : rows){
			for (ProjectVerifyLog log : logs) {
				if(borrow.getProjectId() != null && borrow.getProjectId().equals(log.getProjectId())){
					borrow.setVerifyRemark(log.getRemark());
				}
			}
		}			
	}
	
	/**
	 * 封装关联表属性
	 * @param rows
	 * @param borrowResult 
	 * @param userResult 
	 */
	private void fillUser(List<Borrow> rows, boolean userResult, boolean borrowResult){
		// 获取用户id数组
		String[] userIds = new String[rows.size()];
		String[] borrowIds = new String[rows.size()];
		int i = 0;
		for(Borrow borrow : rows){
			userIds[i] = borrow.getUserId();
			borrowIds[i] = borrow.getUuid();
			i ++;
		}
		if(userResult){
			// 获取用户列表封装成map方便获取
			List<User> userList = userService.findUserByIds(userIds);
			Map<String,User> map = new HashMap<String,User>();
			for(User user : userList){
				map.put(user.getUuid(), user);
			}
			// 用户属性封装入对象
			for(Borrow borrow : rows){
				User user = map.get(borrow.getUserId());
				if(user != null){
					borrow.setUserName(user.getUserName());
					borrow.setBorrower(user.getRealName());
				}
			}
		}
		
		if(borrowResult){
			// 获取借贷列表封装成map方便获取
			List<Borrow> borrowList = dao.findBorrowByIds(borrowIds);
			Map<String,Borrow> map = new HashMap<String,Borrow>();
			for(Borrow borrow : borrowList){
				map.put(borrow.getProjectId(), borrow);
			}
			// 借贷属性封装入对象
			for(Borrow borrow : rows){
				Borrow bor = map.get(borrow.getUuid());
				if(bor != null){
					borrow.setProjectId(bor.getProjectId());
					borrow.setBorrowNature(bor.getBorrowNature());
					borrow.setBorrowUse(bor.getBorrowUse());
					borrow.setRepaymentAccount(bor.getRepaymentAccount());
					borrow.setRepaymentYesAccount(bor.getRepaymentYesAccount());
					borrow.setRepaymentYesInterest(bor.getRepaymentYesInterest());
				}
			}
		}
	}
	
	@Override
	public List<Borrow> findBorrowListByDate(Date date){
		Date endDate = DateUtils.getDayEnd(date);
		return dao.findBorrowListByDate(date,endDate);
	}
	
	/**
	 * 根据projectId查询borrow
	 * @param projectId
	 * @return
	 */
	@Override
	public Borrow getByProjectId(String projectId){
		return dao.getByProjectId(projectId);
	}

}