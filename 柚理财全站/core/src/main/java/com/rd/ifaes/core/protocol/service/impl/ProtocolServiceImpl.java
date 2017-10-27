package com.rd.ifaes.core.protocol.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.security.shiro.IdGen;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.EncodeUtils;
import com.rd.ifaes.common.util.FreemarkerUtil;
import com.rd.ifaes.common.util.MD5Utils;
import com.rd.ifaes.common.util.MoneyUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.resource.BondResource;
import com.rd.ifaes.common.util.resource.LoanResource;
import com.rd.ifaes.common.util.resource.ProtocolResource;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.bond.domain.Bond;
import com.rd.ifaes.core.bond.domain.BondCollection;
import com.rd.ifaes.core.bond.domain.BondInvest;
import com.rd.ifaes.core.bond.service.BondCollectionService;
import com.rd.ifaes.core.bond.service.BondInvestService;
import com.rd.ifaes.core.bond.service.BondService;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.DictUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.domain.ProjectType;
import com.rd.ifaes.core.project.domain.Realize;
import com.rd.ifaes.core.project.mapper.ProjectInvestMapper;
import com.rd.ifaes.core.project.mapper.ProjectMapper;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.project.service.ProjectTypeService;
import com.rd.ifaes.core.project.service.RealizeService;
import com.rd.ifaes.core.protocol.domain.Protocol;
import com.rd.ifaes.core.protocol.mapper.ProtocolMapper;
import com.rd.ifaes.core.protocol.model.ProtocolModel;
import com.rd.ifaes.core.protocol.service.ProtocolService;
import com.rd.ifaes.core.protocol.util.ProtocolHelper;
import com.rd.ifaes.core.protocol.util.bond.BondProtocolBean;
import com.rd.ifaes.core.protocol.util.invest.AbstractProtocolBean;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.domain.UserCompanyInfo;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserCompanyInfoService;
import com.rd.ifaes.core.user.service.UserService;

/**
 * 协议管理
 *
 */
@Service("protocolService")
public class ProtocolServiceImpl extends BaseServiceImpl<ProtocolMapper, Protocol> implements ProtocolService{

	private static final Logger LOGGER = LoggerFactory.getLogger(ProtocolServiceImpl.class);

	/**
	 * DATE
	 */
	private static final String DATE ="date";
	@Autowired
	private ProjectInvestMapper projectInvestMapper;
	@Autowired
	private transient UserCompanyInfoService userCompanyInfoService;
	@Autowired
	private transient UserService userService;
	@Autowired
	private transient UserCacheService userCacheService;
	@Autowired
	private transient ProjectService projectService;
	@Autowired
	private transient RealizeService realizeService;
	@Autowired
	private ProjectMapper projectMapper;
	 
	/**
	 * ProjectTypeService 业务
	 */
	@Autowired
	private transient  ProjectTypeService projectTypeService;
	/**
	 * 债权投资业务
	 */
	@Autowired
	private transient BondInvestService bondInvestService;
	/**
	 * 债权业务
	 */
	@Autowired
	private transient BondService bondService;
	
	/**
	 * 生成债权协议
	 */
	@Override
	@Transactional(readOnly=false)
	public BondProtocolBean buildBondProtocol(String bondInvestId,
			String userId) {
		LOGGER.info("bondInvestId:{},userId:{}",bondInvestId,userId);
		final BondInvest bondInvest = bondInvestService.get(bondInvestId);
		if(bondInvest == null){
			throw new BussinessException(ResourceUtils.get(BondResource.BOND_INVEST_NOT_EXISTS),BussinessException.TYPE_JSON);
		}
		final Bond bond = bondService.get(bondInvest.getBondId());
		if(bond == null){
			throw new BussinessException(ResourceUtils.get(BondResource.BOND_NOT_EXISTS),BussinessException.TYPE_JSON);
		}
		return this.buildBondProtocol(bondInvestId, bond.getProtocolId(),userId);
	}
	
	/**
	 * 生成债权协议
	 */
	@Override
	@Transactional(readOnly=false)
	public BondProtocolBean buildBondProtocol(String bondInvestId,
			String nid, String userId) {
		LOGGER.info("bondInvestId:{},protocolId:{},userId:{}",bondInvestId,nid,userId);
		final BondInvest bondInvest = bondInvestService.get(bondInvestId);
		
		final Protocol protocol = dao.get(nid);
		if(protocol == null){
			throw new BussinessException(ResourceUtils.get(ProtocolResource.PROTOCL_NOT_EXISTS),BussinessException.TYPE_JSON);
		}
		BondProtocolBean protocolBean = ProtocolHelper.doBondProtocol("");
		
		protocolBean.executer(bondInvest.getBondId(), bondInvestId, protocol, userId);
		return protocolBean;
	}
	
	/**
	 * 生成协议
	 */
	@Override
	public AbstractProtocolBean buildProtocol(final String investId,final String userId) {
		LOGGER.info("investId:{},userId:{}",investId,userId);
		final ProjectInvest invest = projectInvestMapper.get(investId);
		if(invest == null){
			throw new BussinessException(ResourceUtils.get(LoanResource.INVEST_NOT_FOUND),BussinessException.TYPE_JSON);
		}
		final Project project = projectMapper.get(invest.getProjectId());
		if(project == null){
			throw new BussinessException(ResourceUtils.get(LoanResource.PROJECT_NOT_EXISTS),BussinessException.TYPE_JSON);
		}
		return buildProtocol(investId,project.getProtocolId(),userId);
	}

	/**
	 * 生成协议
	 */
	@Override
	public AbstractProtocolBean buildProtocol(final String investId,final  String protocolId,final String userId) {
		LOGGER.info("investId:{},protocolId:{},userId:{}",investId,protocolId,userId);
		final ProjectInvest invest = projectInvestMapper.get(investId);
		final Protocol protocol = dao.get(protocolId);
		if(protocol == null){
			throw new BussinessException(ResourceUtils.get(ProtocolResource.PROTOCL_NOT_EXISTS),BussinessException.TYPE_JSON);
		}
		AbstractProtocolBean protocolBean = ProtocolHelper.doProtocol("");
		
		protocolBean.executer(invest.getProjectId(), investId, protocol, userId);
		return protocolBean;
	}

	/**
	 * 根据协议类型获取协议模板(只含UUID和name)
	 */
	@Override
	public List<Protocol> getProtocolListByType(String type) {
		LOGGER.info("getProtocolListByType,type:{}", type);
		
		final Protocol model = new Protocol();
		model.setProtocolType(type);
		model.setStatus(Constant.FLAG_YES);
		return dao.getProtocolListByType(model);
	}

	@Override
	public String saveProtocol(Protocol protocol) {
		ProtocolModel model = ProtocolModel.instance(protocol);
		String validResult = model.valid();
		if(StringUtils.isNotBlank(validResult)){
			return validResult;
		}
		try{
			protocol.setContent(StringEscapeUtils.unescapeHtml4(protocol.getContent()));
			if(Constant.PROTOCOL_TYPE_BOND_INVEST.equals(protocol.getProtocolType())
					|| Constant.PROTOCOL_TYPE_REALIZE_INVEST.equals(protocol.getProtocolType()) 
					|| Constant.PROTOCOL_TYPE_INVEST.equals(protocol.getProtocolType())){
				handleBondAndRealizeProtocol(protocol);
			}
			insert(protocol);
		}catch(BussinessException e){
			LOGGER.error("保存协议模板错误,原因:{}", e.getMessage());
			return e.getMessage();
		}
		return null;
	}

	@Override
	public String editProtocol(Protocol protocol) {
		ProtocolModel model = ProtocolModel.instance(protocol);
		String validResult = model.valid();
		if(StringUtils.isNotBlank(validResult)){
			return validResult;
		}
		try{
			//添加新的协议
			final String oldProtocolId = protocol.getUuid();
			LOGGER.info("处理之前,{};",protocol.getContent());
			protocol.setContent(StringEscapeUtils.unescapeHtml4(protocol.getContent()));
			LOGGER.info("处理之后,{};",protocol.getContent());
			protocol.setCreateTime(DateUtils.getNow());
			protocol.setStatus(ProtocolModel.STATUS_VALID);
			protocol.setUuid(IdGen.uuid());//设定新的协议的ID
			if(Constant.PROTOCOL_TYPE_INVEST.equals(protocol.getProtocolType())){
				//普通投资协议  需要和类别关联  自动更新原来用的协议的类别
				handleOldProtocol(oldProtocolId, protocol);
			}else if(Constant.PROTOCOL_TYPE_REGISTER.equals(protocol.getProtocolType())){
				//注册协议
				handleProtocol(oldProtocolId);
			}else{
				handleBondAndRealizeProtocol(protocol);
			}
			dao.insert(protocol);
		}catch(BussinessException e){
			LOGGER.error("编辑协议模板错误,原因:{}", e.getMessage() ,e);
			return e.getMessage();
		}
		return null;
	}
	
	/**
	 * 普通投资协议  需要和类别关联  自动更新原来用的协议的类别
	 * @author QianPengZhan
	 * @date 2016年11月5日
	 * @param oldProtocolId
	 * @param protocol
	 */
	private void handleOldProtocol(final String oldProtocolId,final Protocol protocol){
		//1、更新旧的协议变为失效
		handleProtocol(oldProtocolId);
		//2、旧协议关联的类别的协议ID全部更新为新的ID
		final ProjectType projectTypeModel = new ProjectType();
		projectTypeModel.setProtocolId(oldProtocolId);
		projectTypeModel.setDeleteFlag(CommonEnum.NO.getValue());
		final List<ProjectType> projectTypeList = projectTypeService.findList(projectTypeModel); 
		if(CollectionUtils.isNotEmpty(projectTypeList)){
			for(final ProjectType projectType:projectTypeList){
				projectType.setProtocolId(protocol.getUuid());
			}
			projectTypeService.updateBatch(projectTypeList);//批量处理类别的协议ID
		}
	}
	
	/**
	 * 处理债权和变现协议  作无效处理
	 * @author QianPengZhan
	 * @date 2016年11月5日
	 * @param protocol
	 */
	private void handleBondAndRealizeProtocol(final Protocol protocol){
		//债权和变现协议  保持有效性唯一  其他的同类型的协议都做无效处理
		final Protocol proModel = new Protocol();
		proModel.setProtocolType(protocol.getProtocolType());
		final List<Protocol> protocolList = dao.findList(proModel);
		if(CollectionUtils.isNotEmpty(protocolList)){
			for(final Protocol temp:protocolList){
				temp.setStatus(ProtocolModel.STATUS_INVALID);
			}
			dao.updateBatch(protocolList);
		}
	}
	
	/**
	 * 失效处理
	 * @author QianPengZhan
	 * @date 2016年11月2日
	 * @param oldProtocolId
	 */
	private  void  handleProtocol(final String oldProtocolId){
		final Protocol oldProtocol = new Protocol();
		oldProtocol.setUuid(oldProtocolId);
		oldProtocol.setStatus(ProtocolModel.STATUS_INVALID);
		dao.update(oldProtocol);
	}
	
	@Override
	public String updateStatus(final String uuid) {
		Protocol protocol = get(uuid);
		if(protocol == null){
			return "修改失败";
		}
		//查询同类型的有效的协议的个数
		final Protocol model = new Protocol();
		model.setProtocolType(protocol.getProtocolType());
		model.setStatus(ProtocolModel.STATUS_VALID);
		final int count = dao.getCount(model);
		if(ProtocolModel.STATUS_VALID.equals(protocol.getStatus())){//原来是有效的  做失效处理 
			if(count <= Constant.INT_ONE){
				throw new  BussinessException(ResourceUtils.get(ProtocolResource.PROTOCOL_STATUS_VALID_REMAIN_ONE),BussinessException.TYPE_JSON);
			}
			protocol.setStatus(ProtocolModel.STATUS_INVALID);
		}else if(ProtocolModel.STATUS_INVALID.equals(protocol.getStatus())){//原来是失效的  做有效处理
			protocol.setStatus(ProtocolModel.STATUS_VALID);
			if(Constant.PROTOCOL_TYPE_BOND_INVEST.equals(protocol.getProtocolType()) 
					|| Constant.PROTOCOL_TYPE_REALIZE_INVEST.equals(protocol.getProtocolType()) ){
				if(count == Constant.INT_ONE){
					throw new  BussinessException(ResourceUtils.get(ProtocolResource.PROTOCOL_STATUS_VALID_ONLY_ONE),BussinessException.TYPE_JSON);
				}
			}
		}else{
			return "修改失败";
		}
		try{
			protocol.setContent(StringEscapeUtils.unescapeHtml4(protocol.getContent()));
			update(protocol);
		}catch(BussinessException e){
			LOGGER.error("启用禁用协议模板错误,原因:{}", e.getMessage(), e);
			return e.getMessage();
		}
		return null;
	}
	
	/**
	 * 普通投资协议下载
	 */
	@Override
	public String downloadProtocol(final String projectId, final String investId, final String protocolName,final String url,final String webUrl) {
		StringBuilder sb = new StringBuilder();
		String imageHost = ConfigUtils.getValue(ConfigConstant.IMAGE_SERVER_URL); 
		StringBuilder urlSb = new StringBuilder(imageHost);
		try {
			sb.append(Constant.TOKEN_CODE).append(projectId).append(investId).append(Constant.TOKEN_CODE);
			String token = MD5Utils.encode(sb.toString());
			urlSb.append(url).append("?projectId=").append(projectId).append("&investId=")
					.append(investId).append("&token=").append(token).append("&fileName=").append(EncodeUtils.urlEncode(protocolName)).
					append("&webUrl=").append(webUrl);


		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return null;
		}
		return urlSb.toString();
	}
	
	/**
	 * 债权协议下载  
	 */
	@Override
	public String downloadBondProtocol(final String bondId,final String bondInvestId,final String protocolName,final String url,final String webUrl) {
		StringBuilder sb = new StringBuilder();
		String imageHost = ConfigUtils.getValue(ConfigConstant.IMAGE_SERVER_URL);
		StringBuilder urlSb = new StringBuilder(imageHost);
		try {
			sb.append(Constant.TOKEN_CODE).append(bondId).append(bondInvestId).append(Constant.TOKEN_CODE);
			String token = MD5Utils.encode(sb.toString());
			urlSb.append(url).append("?bondId=").append(bondId).append("&bondInvestId=").append(bondInvestId)
			.append("&token=").append(token).append("&fileName=").append(EncodeUtils.urlEncode(protocolName)).
			append("&webUrl=").append(webUrl);

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return null;
		}
		return urlSb.toString();
	}
	
	
    /**
     * 获得协议模板
     */
	@Override
	public String getProtocolContent(final String uuid, final Map<String, Object> param) {
		Protocol protocol = dao.get(uuid);
		if(protocol == null){
			throw new BussinessException(ResourceUtils.get(ProtocolResource.PROTOCL_NOT_EXISTS),BussinessException.TYPE_JSON);
		}
		String protocolContent="";
		if (StringUtils.isNotBlank(protocol.getContent())) {
			protocolContent = FreemarkerUtil.renderTemplate(protocol.getContent(), param);
		}
		return protocolContent;
	}

	@Override
	@Transactional(readOnly=false)
	public Map<String, Object> getInvestProtocol(final User user, final String projectId,final String protocolId) {
		final Map<String,Object> data = Maps.newHashMap();
		final Map<String, Object> paramMap = Maps.newHashMap();
		final Protocol protocol = dao.get(protocolId);
		if(protocol == null){
			throw new BussinessException(ResourceUtils.get(ProtocolResource.PROTOCL_NOT_EXISTS),BussinessException.TYPE_JSON);
		}
		initParamMap(paramMap,user,projectId,protocol);
		String content = this.getProtocolContent(protocolId, paramMap);
		data.put("protocolType", DictUtils.getData("protocolType", protocol.getProtocolType()).getItemName());
		data.put("result", true);
		data.put("content", content);
		return data;
	}
	
	/**
	 * 初始化协议参数
	 * @param data
	 */
	private void initParamMap(final Map<String, Object> paramMap,final User user,final String projectId,final Protocol protocol){
		if(user == null){
			handleEmptyInvestParam(paramMap);
		}else{
			final Project project = handleProjectInfo(projectId, paramMap);
			if(project != null){
				User borrowUser = userService.get(project.getUserId());
				handleUserInfo(user, paramMap,borrowUser);
			}	
			paramMap.put("collectionMoney", BigDecimal.ZERO);
			paramMap.put("investTime", DateUtils.dateStr6(DateUtils.getNow()));
		}
		paramMap.put("date", DateUtils.dateStr6(new Date()));
		paramMap.put("webCompany", ConfigUtils.getValue(ConfigConstant.WEB_COMPANY));
		paramMap.put("webName", ConfigUtils.getValue(ConfigConstant.WEB_NAME));
		paramMap.put("protocolTypeName", protocol.getName());
		paramMap.put("investUserMobile", StringUtils.EMPTY);
		paramMap.put("investMoney", StringUtils.EMPTY);
		paramMap.put("apr", StringUtils.EMPTY);
		paramMap.put("interestDate", StringUtils.EMPTY);
		paramMap.put("endDate", StringUtils.EMPTY);
		paramMap.put("vouchInstitution", StringUtils.EMPTY);
		paramMap.put("projectUserMobile", StringUtils.EMPTY);
	}
	
	/**
	 * 变现协议预览
	 * @author fxl
	 * @date 2016年12月20日
	 * @param user
	 * @param investId
	 * @return
	 */
	@Override
	@Transactional(readOnly=false)
	public Map<String, Object> getRealizeProtocol(final User user) {
		final Map<String,Object> data = Maps.newHashMap();
		final Map<String, Object> paramMap = Maps.newHashMap();
		List<Protocol> protocollist = getProtocolListByType(Constant.PROTOCOL_TYPE_REALIZE_INVEST);
		if(CollectionUtils.isEmpty(protocollist)){
			throw new BussinessException("协议未获取到");
		}
		final Protocol protocol = protocollist.get(0);
		if(protocol == null){
			throw new BussinessException(ResourceUtils.get(ProtocolResource.PROTOCL_NOT_EXISTS),BussinessException.TYPE_JSON);
		}
		initRealizeParamMap(paramMap,user,protocol);
		String content = this.getProtocolContent(protocol.getUuid(), paramMap);
		data.put("protocolType", DictUtils.getData("protocolType", protocol.getProtocolType()).getItemName());
		data.put("result", true);
		data.put("content", content);
		return data;
	}
	
	/**
	 * 初始化变现协议参数
	 * @param data
	 */
	private void initRealizeParamMap(final Map<String, Object> paramMap,final User user,final Protocol protocol){
		if(user == null){
			handleEmptyInvestParam(paramMap);
		}else{
			String projectUserRealName = StringUtils.hideName(user.getRealName());
			String projectUserCardInfo = StringUtils.EMPTY;
			final UserCache borrowUserCache = userCacheService.findByUserId(user.getUuid());
			UserCompanyInfo borrowUserCompanyInfo = null;
			if(UserCache.USER_NATURE_COMPANY.equals(borrowUserCache.getUserNature())){
				borrowUserCompanyInfo  = userCompanyInfoService.findByUserId(user.getUuid());
				if(Constant.FLAG_YES.equals(borrowUserCompanyInfo.getThreeCertificate())){// 三证合一
					projectUserCardInfo = StringUtils.hideLastChar(borrowUserCompanyInfo.getCreditCode(),4);
				}else{
					projectUserCardInfo = StringUtils.hideLastChar(borrowUserCompanyInfo.getOrgCode(),4);
				}
		    }else{
		    	projectUserCardInfo = StringUtils.hideStr(borrowUserCache.getIdNo(), 6, borrowUserCache.getIdNo().length()-4);
		    }
			paramMap.put("investUserRealName", "XXX投资人");
			paramMap.put("investUserCardInfo", "XXXXXXXXX");
			paramMap.put("projectUserRealName", projectUserRealName);
			paramMap.put("projectUserCardInfo", projectUserCardInfo);
			paramMap.put("collectionMoney", BigDecimal.ZERO);
			paramMap.put("investTime", DateUtils.dateStr6(DateUtils.getNow()));
		}
		paramMap.put("date", DateUtils.dateStr6(new Date()));
		paramMap.put("webCompany", ConfigUtils.getValue(ConfigConstant.WEB_COMPANY));
		paramMap.put("webName", ConfigUtils.getValue(ConfigConstant.WEB_NAME));
		paramMap.put("protocolTypeName", protocol.getName());
	}
	
	/**
	 * 用户信息处理
	 * @author QianPengZhan
	 * @date 2016年12月12日
	 * @param user
	 * @param paramMap
	 */
	private void handleUserInfo(final User user,final Map<String, Object> paramMap,final User borrowUser){
		String investUserRealName = StringUtils.EMPTY;
		String investUserCardInfo = StringUtils.EMPTY;
		if(user != null){
			investUserRealName = StringUtils.hideName(user.getRealName());
		}
	    final UserCache investUserCache = userCacheService.findByUserId(user.getUuid());
	    UserCompanyInfo investUserCompanyInfo = null;
	    if(UserCache.USER_NATURE_PERSON.equals(investUserCache.getUserNature())){
	    	if(StringUtils.isNotBlank(investUserCache.getIdNo())){
	    		investUserCardInfo = StringUtils.hideStr(investUserCache.getIdNo(), 6, investUserCache.getIdNo().length()-4);
	    	}
	    }else{
	    	investUserCompanyInfo  = userCompanyInfoService.findByUserId(user.getUuid());
	    	if(Constant.FLAG_YES.equals(investUserCompanyInfo.getThreeCertificate())){// 三证合一
				investUserCardInfo = StringUtils.hideLastChar(investUserCompanyInfo.getCreditCode(), 4);
			}else{
				investUserCardInfo = StringUtils.hideLastChar(investUserCompanyInfo.getOrgCode(), 4);
			}
	    }
		String projectUserRealName = StringUtils.EMPTY;
		String projectUserCardInfo = StringUtils.EMPTY;
		if(borrowUser != null){
			projectUserRealName = StringUtils.hideName(borrowUser.getRealName());
		}
		final UserCache borrowUserCache = userCacheService.findByUserId(borrowUser.getUuid());
		UserCompanyInfo borrowUserCompanyInfo = null;
		if(UserCache.USER_NATURE_COMPANY.equals(borrowUserCache.getUserNature())){
			borrowUserCompanyInfo  = userCompanyInfoService.findByUserId(borrowUser.getUuid());
			if(Constant.FLAG_YES.equals(borrowUserCompanyInfo.getThreeCertificate())){// 三证合一
				projectUserCardInfo = StringUtils.hideLastChar(borrowUserCompanyInfo.getCreditCode(),4);
			}else{
				projectUserCardInfo = StringUtils.hideLastChar(borrowUserCompanyInfo.getOrgCode(),4);
			}
	    }else{
	    	projectUserCardInfo = StringUtils.hideStr(borrowUserCache.getIdNo(), 6, borrowUserCache.getIdNo().length()-4);
	    }
		paramMap.put("investUserRealName", investUserRealName);
		paramMap.put("investUserCardInfo", investUserCardInfo);
		paramMap.put("projectUserRealName", projectUserRealName);
		paramMap.put("projectUserCardInfo", projectUserCardInfo);
	}
	
	/**
	 * 处理项目信息
	 * @author QianPengZhan
	 * @date 2016年12月12日
	 * @param projectId
	 * @param paramMap
	 */
	private Project handleProjectInfo(final String projectId,final Map<String, Object> paramMap){
		final Project project = projectService.get(projectId);
		if(project == null){
			paramMap.put("projectMoney", StringUtils.EMPTY);
			paramMap.put("apr", StringUtils.EMPTY);
			paramMap.put("timeLimit", StringUtils.EMPTY);
			paramMap.put("repayStyle", StringUtils.EMPTY);
			paramMap.put("interestDate", StringUtils.EMPTY);
			paramMap.put("endDate", StringUtils.EMPTY);
			paramMap.put("oldProjectName", StringUtils.EMPTY);
		}else{
			paramMap.put("projectMoney", project.getAccount());
			if(LoanEnum.REALIZE_FLAG_REALIZE.eq(project.getRealizeFlag())){
				Realize realize = realizeService.findRealize(projectId);
				if(realize!=null){
					paramMap.put("apr", project.getApr());
					paramMap.put("timeLimit", project.getTimeLimitStr());
					paramMap.put("repayStyle", DictUtils.getItemName("repayStyle", project.getRepayStyle()));
					paramMap.put("interestDate", StringUtils.EMPTY);
					paramMap.put("endDate", StringUtils.EMPTY);
					Project oldProject = projectService.get(realize.getOldProjectId());
					paramMap.put("oldProjectName", oldProject.getProjectName());
				}
			}
		}
		return project;
	}
	
	@Override
	@Transactional(readOnly=false)
	public Map<String, Object> getBondProtocol(User user, String bondId,
			String protocolId) {
		final Map<String,Object> data = Maps.newHashMap();
		final Map<String, Object> paramMap = Maps.newHashMap();
		final Protocol protocol = dao.get(protocolId);
		if(protocol == null){
			throw new BussinessException(ResourceUtils.get(ProtocolResource.PROTOCL_NOT_EXISTS),BussinessException.TYPE_JSON);
		}
		initParamBondMap(paramMap,user,bondId,protocol);
		String content = this.getProtocolContent(protocolId, paramMap);
		data.put("result", true);
		data.put("content", content);
		data.put("protocolType", DictUtils.getData("protocolType", protocol.getProtocolType()).getItemName());
		return data;
	}
	

	/**
	 * 初始化债权协议参数
	 * @param data
	 */
	private void initParamBondMap(final Map<String, Object> data,final User user,final String bondId,final Protocol protocol){
		final Bond bond = bondService.get(bondId);
		if(StringUtils.isBlank(bondId)){
			handleEmptyBondParam(data);
		}else{
			data.put("bondNo", bond.getBondNo());
			data.put("bondMoneyCh", MoneyUtils.transform(BigDecimal.ZERO));//金额大写
			data.put("bondMoney", BigDecimal.ZERO.toPlainString());
			data.put("amount", BigDecimal.ZERO.toPlainString());//本金
			data.put("payedInterest", BigDecimal.ZERO.toPlainString());//利息
			data.put("receivedAccountCh", MoneyUtils.transform(BigDecimal.ZERO));//实付金额
			data.put("receivedAccount", BigDecimal.ZERO.toPlainString());
			final Project project  = projectService.get(bond.getProjectId());
			data.put("projectNo", project != null?project.getProjectNo():StringUtils.EMPTY);
			data.put("bondInvestTime", DateUtils.dateStr6(DateUtils.getNow()));
			if(user == null){   
				data.put("bondUserName", StringUtils.EMPTY);
				data.put("bondUserCardNo", StringUtils.EMPTY);
				data.put("bondInvestUserName", StringUtils.EMPTY);
				data.put("bondInvestUserCardNo", StringUtils.EMPTY);
				data.put("borrowUserName", StringUtils.EMPTY);
				data.put("borrowUserCardNo", StringUtils.EMPTY);
				data.put("interestStartDate", StringUtils.EMPTY);
				data.put("interestEndDate", StringUtils.EMPTY);
				data.put("borrowApr", StringUtils.EMPTY);
				data.put("repayStyle", StringUtils.EMPTY);
				data.put("bondName", "xxx债权转让");
				data.put("bondApr", StringUtils.EMPTY);
			}else{
				final User borrowUser = handleBondProjectInfo(data, bond);
				handleBondUserInfo(data, user, borrowUser, bond);
			}
			data.put("bondName",bond.getBondName());
			data.put("bondApr", bond.getBondApr());
		}
		data.put(ConfigConstant.WEB_NAME, ConfigUtils.getValue(ConfigConstant.WEB_NAME));
		data.put(ConfigConstant.WEB_COMPANY, ConfigUtils.getValue(ConfigConstant.WEB_COMPANY));
		data.put(ConfigConstant.WEB_URL, ConfigUtils.getValue(ConfigConstant.WEB_URL));
		data.put(DATE, DateUtils.dateStr6(new Date()));
		data.put("remainDay", StringUtils.EMPTY);
		data.put("repayList", null);
		data.put("bondInvestMoney", StringUtils.EMPTY);
		data.put("protocolTypeName", protocol.getName());
	}
	
	/**
	 * 处理债权协议中的用户信息
	 * @author QianPengZhan
	 * @date 2016年12月12日
	 * @param data
	 * @param user
	 * @param borrowUser
	 * @param bond
	 */
	private void handleBondUserInfo(final Map<String, Object> data,final User user,final User borrowUser,final Bond bond){
		String  bondUserName= StringUtils.EMPTY;
		String  bondUserCardNo= StringUtils.EMPTY;
		String  bondInvestUserName= StringUtils.EMPTY;
		String  bondInvestUserCardNo= StringUtils.EMPTY;
		String  borrowUserCardNo= StringUtils.EMPTY;
		final User bondUser = userService.get(bond.getUserId());
		if(bondUser != null){
			bondUserName = StringUtils.hideName(bondUser.getRealName());
			final UserCache bondUserCache = userCacheService.findByUserId(bondUser.getUuid());
			if(bondUserCache != null){
				UserCompanyInfo bondUserCompanyInfo =  null;
				if(UserCache.USER_NATURE_PERSON.equals(bondUserCache.getUserNature())){
					bondUserCardNo = StringUtils.hideStr(bondUserCache.getIdNo(), 6, bondUserCache.getIdNo().length()-4);
				}else{
					bondUserCompanyInfo = userCompanyInfoService.findByUserId(bondUser.getUuid());
					bondUserCardNo = StringUtils.hideLastChar(bondUserCompanyInfo.getOrgCode(), 4);
				}
			}
		}
		
		if(user != null){
			bondInvestUserName = StringUtils.hideName(user.getRealName());
			final UserCache bondInvestUserCache = userCacheService.findByUserId(user.getUuid());
			if(bondInvestUserCache != null){
				UserCompanyInfo bondInvestUserCompanyInfo =  null;
				if(UserCache.USER_NATURE_PERSON.equals(bondInvestUserCache.getUserNature())){
					bondInvestUserCardNo = StringUtils.hideStr(bondInvestUserCache.getIdNo(), 6, bondInvestUserCache.getIdNo().length()-4);
				}else{
					bondInvestUserCompanyInfo = userCompanyInfoService.findByUserId(user.getUuid());
					bondInvestUserCardNo = StringUtils.hideLastChar(bondInvestUserCompanyInfo.getOrgCode(), 4);
				}
			}
			
		}
		
		if(borrowUser != null){
			final UserCache borrowUserCache = userCacheService.findByUserId(borrowUser.getUuid());
			if(borrowUserCache != null){
				UserCompanyInfo borrowUserCompanyInfo =  null;
				if(UserCache.USER_NATURE_PERSON.equals(borrowUserCache.getUserNature())){
					borrowUserCardNo = StringUtils.hideStr(borrowUserCache.getIdNo(), 6, borrowUserCache.getIdNo().length()-4);
				}else{
					borrowUserCompanyInfo = userCompanyInfoService.findByUserId(borrowUser.getUuid());
					borrowUserCardNo = StringUtils.hideLastChar(borrowUserCompanyInfo.getOrgCode(), 4);
				}
			}
		}
		data.put("bondUserName",bondUserName);
		data.put("bondUserCardNo",  bondUserCardNo);
		data.put("bondInvestUserName",bondInvestUserName);
		data.put("bondInvestUserCardNo",bondInvestUserCardNo);
		data.put("borrowUserCardNo", borrowUserCardNo);
		
	}
	
	
	/**
	 * 处理债权协议中的项目信息
	 * @author QianPengZhan
	 * @date 2016年12月12日
	 * @param data
	 * @param bond
	 * @param borrowUser
	 */
	private User handleBondProjectInfo(final Map<String, Object> data,final Bond bond){
		User borrowUser= null;
		final Project project = projectService.get(bond.getProjectId());
		if(project == null){
			data.put("borrowInitMoney", StringUtils.EMPTY);
			data.put("interestStartDate",StringUtils.EMPTY);
			data.put("interestEndDate",StringUtils.EMPTY);
			data.put("borrowApr", StringUtils.EMPTY);
			data.put("repayStyle", StringUtils.EMPTY);
		}else{
			borrowUser = userService.get(project.getUserId());
			data.put("borrowInitMoney", project.getAccount());
			Date interestTime = DateUtils.getNow();
			if(project.getReviewTime() != null && project.getInterestStyle() != null){
				interestTime = project.getReviewTime(); 
				if(LoanEnum.INTEREST_STYLE_EV.eq(project.getInterestStyle())){
					interestTime =project.getReviewTime();
				}else if(LoanEnum.INTEREST_STYLE_TN.eq(project.getInterestStyle())){//T+N
					interestTime = DateUtils.rollDay(project.getReviewTime(),project.getInterestStartDays());
				}
			}
			data.put("interestStartDate", DateUtils.dateStr6(interestTime));
			data.put("interestEndDate", DateUtils.dateStr6(project.getLastRepayTime()) );
			data.put("borrowApr", project.getApr());
			data.put("repayStyle", DictUtils.getData("repayStyle",project.getRepayStyle()).getItemName());
		}
		return borrowUser;
	}
	
	@Override
	public String getView(Protocol protocol) {
		if(protocol == null){
			throw new BussinessException(ResourceUtils.get(ProtocolResource.PROTOCL_NOT_EXISTS),BussinessException.TYPE_JSON);
		}
		final Map<String, Object> paramMap = Maps.newHashMap();
		paramMap.put(ConfigConstant.WEB_NAME, ConfigUtils.getValue(ConfigConstant.WEB_NAME));
		paramMap.put(ConfigConstant.WEB_COMPANY, ConfigUtils.getValue(ConfigConstant.WEB_COMPANY));
		paramMap.put(ConfigConstant.WEB_URL, ConfigUtils.getValue(ConfigConstant.WEB_URL));
		paramMap.put(DATE, DateUtils.dateStr6(new Date()));
		if(Constant.PROTOCOL_TYPE_BOND_INVEST.equals(protocol.getProtocolType())){
			paramMap.put("remainDay", StringUtils.EMPTY);
			paramMap.put("repayList", null);
			paramMap.put("bondInvestMoney", StringUtils.EMPTY);
			paramMap.put("protocolTypeName", protocol.getName());
			handleEmptyBondParam(paramMap);
		}else if(Constant.PROTOCOL_TYPE_INVEST.equals(protocol.getProtocolType())){
			handleEmptyInvestParam(paramMap);
		}
		return this.getProtocolContent(protocol.getUuid(), paramMap);
	}
	
	/**
	 * 
	 * @author QianPengZhan
	 * @param paramMap
	 */
	private void handleEmptyInvestParam(final Map<String, Object> paramMap){
		paramMap.put("investUserUserName", StringUtils.EMPTY);
		paramMap.put("investUserRealName", StringUtils.EMPTY);
		paramMap.put("investUserCardInfo", StringUtils.EMPTY);
		paramMap.put("investUserMobile", StringUtils.EMPTY);
		paramMap.put("webCompany", StringUtils.EMPTY);
		paramMap.put("projectUserRealName",StringUtils.EMPTY);
		paramMap.put("projectUserCardInfo",StringUtils.EMPTY);
		paramMap.put("projectMoney", StringUtils.EMPTY);
		paramMap.put("investMoney", StringUtils.EMPTY);
		paramMap.put("collectionMoney", StringUtils.EMPTY);
		paramMap.put("investTime", StringUtils.EMPTY);
		paramMap.put("apr", StringUtils.EMPTY);
		paramMap.put("interestDate", StringUtils.EMPTY);
		paramMap.put("endDate", StringUtils.EMPTY);
		paramMap.put("vouchInstitution", StringUtils.EMPTY);
		paramMap.put("projectUserMobile", StringUtils.EMPTY);
		paramMap.put("protocolTypeName", StringUtils.EMPTY);
	}
	
	
	/**
	 * 处理空协议
	 * @author QianPengZhan
	 * @param paramMap
	 */
	private void handleEmptyBondParam(final Map<String, Object> paramMap){
		paramMap.put("bondUserName", StringUtils.EMPTY);
		paramMap.put("bondUserCardNo", StringUtils.EMPTY);
		paramMap.put("bondInvestUserName", StringUtils.EMPTY);
		paramMap.put("bondInvestUserCardNo", StringUtils.EMPTY);
		paramMap.put("borrowUserName", StringUtils.EMPTY);
		paramMap.put("borrowUserCardNo", StringUtils.EMPTY);
		paramMap.put("interestStartDate", StringUtils.EMPTY);
		paramMap.put("interestEndDate", StringUtils.EMPTY);
		paramMap.put("borrowApr", StringUtils.EMPTY);
		paramMap.put("repayStyle", StringUtils.EMPTY);
		paramMap.put("bondName", "xxx债权转让");
		paramMap.put("bondApr", StringUtils.EMPTY);
		paramMap.put("bondNo", StringUtils.EMPTY);
		paramMap.put("bondMoneyCh", StringUtils.EMPTY);
		paramMap.put("bondMoney", StringUtils.EMPTY);
		paramMap.put("amount", StringUtils.EMPTY);
		paramMap.put("payedInterest", StringUtils.EMPTY);
		paramMap.put("receivedAccountCh", StringUtils.EMPTY);
		paramMap.put("receivedAccount", StringUtils.EMPTY);
		paramMap.put("projectNo", StringUtils.EMPTY);
		paramMap.put("bondInvestTime", StringUtils.EMPTY);
	}
}
