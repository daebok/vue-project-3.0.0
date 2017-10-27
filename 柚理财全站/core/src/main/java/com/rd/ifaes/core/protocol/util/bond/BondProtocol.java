/**
 * 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.protocol.util.bond;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.itextpdf.text.DocumentException;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.FreemarkerUtil;
import com.rd.ifaes.common.util.MoneyUtils;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.bond.domain.Bond;
import com.rd.ifaes.core.bond.domain.BondCollection;
import com.rd.ifaes.core.bond.domain.BondInvest;
import com.rd.ifaes.core.bond.service.BondCollectionService;
import com.rd.ifaes.core.bond.service.BondInvestService;
import com.rd.ifaes.core.bond.service.BondService;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.sign.constant.SignConstant;
import com.rd.ifaes.core.core.sign.factory.SignFactory;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.DictUtils;
import com.rd.ifaes.core.core.util.FileTypeUtil;
import com.rd.ifaes.core.core.util.PdfHelper;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectCollection;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.service.ProjectCollectionService;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.protocol.domain.Protocol;
import com.rd.ifaes.core.protocol.domain.ProtocolLog;
import com.rd.ifaes.core.protocol.service.ProtocolLogService;
import com.rd.ifaes.core.protocol.util.ProtocolHelper;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.domain.UserCompanyInfo;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserCompanyInfoService;
import com.rd.ifaes.core.user.service.UserService;
import com.timevale.esign.sdk.tech.bean.PosBean;
import com.timevale.esign.sdk.tech.bean.seal.OrganizeTemplateType;
import com.timevale.esign.sdk.tech.bean.seal.PersonTemplateType;
import com.timevale.esign.sdk.tech.bean.seal.SealColor;
import com.timevale.esign.sdk.tech.impl.constants.SignType;

/**
 * 债权协议业务实现类
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年9月29日
 */
public class BondProtocol implements BondProtocolBean{
	
	/**
	 * 日志
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BondProtocol.class);
	/**用户业务*/
	protected UserService userService;
	/**用户信息业务*/
	protected UserCacheService userCacheService;
	/**债权业务*/
	protected BondService bondService;
	/**债权投资业务*/
	protected BondInvestService bondInvestService;
	/**项目业务*/
	protected ProjectService projectService;
	/**项目投资*/
	protected ProjectInvestService projectInvestService;
	/**项目待收业务*/
	protected ProjectCollectionService  projectCollectionService;
	protected BondCollectionService bondCollectionService;
	/** 生成pdf路径名*/
	protected String inPdfName;
	/** 下载pdf名*/
	protected String downloadFileName;
	/**下载时间*/
	protected String downloadDate;	
	/**生成pdf的文件*/
	protected String contextPath;
	/**债权*/
	protected Bond bond;
	/**债权投资记录*/
	protected BondInvest bondInvest;
	protected String bondInvestId;
	protected BondCollection bondSumCapAndIn;//总的待收本金和利息
	/**项目*/
	protected Project project;
	/**项目投资*/
	protected ProjectInvest projectInvest;
	/**受让用户*/
	protected User bondInvestUser;
	/**受让用户信息*/
	protected UserCache bondInvestUserCache;
	/**转让用户*/
	protected User bondUser;
	protected UserCache bondUserCache;
	/**原始标借款人用户*/
	protected User borrowUser;
	protected UserCache borrowUserCache;
	/**起息开始时间*/
	protected String interestStartDate;
	/**起息结束时间*/
	protected String interestEndDate;
	/**协议*/
	protected Protocol bondProtocol;
	/**PDF帮助类*/
	protected PdfHelper pdfHelper;
	/**下载路径*/
	protected String uploadPath;
	/**用户ID*/
	protected String userId;
	/**企业担保用户*/
	protected UserCompanyInfo userCompanyInfo;
	/**转让人企业担保用户*/
	protected UserCompanyInfo bondUserCompanyInfo;
	/**协议业务*/
	protected ProtocolLogService protocolLogService;
	/**企业担保业务*/
	protected UserCompanyInfoService userCompanyInfoService;
	/**还款计划 -- 待收计划*/
	protected List<ProjectCollection> repayList = new ArrayList<ProjectCollection>();
	/** 引入数据map*/
	protected Map<String, Object> data = new HashMap<>();
	@Override
	public void executer(final String bondId,final Protocol bondProtocol,final String userId) {
		this.executer(bondId, StringUtils.EMPTY, bondProtocol, userId);
	}

	@Override
	public void executer(String bondId, String bondInvestId,
			Protocol bondProtocol, String userId) {
		//1、初始化基础参数
		LOGGER.info("初始化BondProtocolBean bondId:{},bondInvestId:{}", bondId ,bondInvestId);
		this.projectCollectionService = (ProjectCollectionService) SpringContextHolder.getBean("projectCollectionService");
		this.projectInvestService = (ProjectInvestService) SpringContextHolder.getBean("projectInvestService");
		this.projectService =  (ProjectService) SpringContextHolder.getBean("projectService");
		this.userService = (UserService) SpringContextHolder.getBean("userService");
		this.userCacheService = (UserCacheService) SpringContextHolder.getBean("userCacheService");
		this.bondService = (BondService) SpringContextHolder.getBean("bondService");
		this.bondInvestService = (BondInvestService) SpringContextHolder.getBean("bondInvestService");
		this.userCacheService = (UserCacheService) SpringContextHolder.getBean("userCacheService");
		this.bondCollectionService = (BondCollectionService) SpringContextHolder.getBean("bondCollectionService");
		this.protocolLogService = (ProtocolLogService)SpringContextHolder.getBean("protocolLogService");
		this.userCompanyInfoService = (UserCompanyInfoService)SpringContextHolder.getBean("userCompanyInfoService");
		
		this.contextPath = BondProtocol.class.getClassLoader().getResource("/").getPath();
		this.downloadDate = DateUtils.formatDate(DateUtils.getNow(),DateUtils.DATEFORMAT_STR_012);
		this.userId= userId;
		//2、生成PDF文件
		this.inPdfName = contextPath + "/"  + bondInvestId + "_" + System.currentTimeMillis() + ".pdf";
		File pdfFile = new File(contextPath);
		try {
			if (!pdfFile.exists()) {
				pdfFile.mkdir();
			}
		} catch (Exception e) {
			LOGGER.error("生成pdf出错", e);
		}
		this.pdfHelper = PdfHelper.instance(inPdfName);
		final Bond bond = bondService.get(bondId);
		final BondInvest bondInvest = bondInvestService.get(bondInvestId);
		final BondCollection bondSumCapAndIn = bondCollectionService.getSumCapAndInByInvestId(bondInvestId);
		final Project project  = projectService.get(bond.getProjectId());
		final ProjectInvest projectInvest = projectInvestService.getInvestByOrderNoAndBondFlag(bondInvest.getInvestOrderNo(), CommonEnum.NO.getValue());
		final User bondInvestUser = userService.get(bondInvest.getUserId());
		final UserCache bondInvestUserCache = userCacheService.findByUserId(bondInvestUser.getUuid());
		final User bondUser = userService.get(bond.getUserId());
		final UserCache bondUserCache = userCacheService.findByUserId(bondUser.getUuid());
		final User borrowUser = userService.get(project.getUserId());
		final UserCache borrowUserCache = userCacheService.findByUserId(borrowUser.getUuid());
		final ProjectCollection pcModel = new ProjectCollection();
		pcModel.setProjectId(project.getUuid());
		pcModel.setUserId(bondInvestUser.getUuid());
		if(projectInvest != null){
			pcModel.setInvestId(projectInvest.getUuid());
		}
		final List<ProjectCollection> list = projectCollectionService.findPcByPIdAndUserId(pcModel);
		if(CollectionUtils.isNotEmpty(list)){
			for (final ProjectCollection pc :list) {
				pc.setStartTime(DateUtils.formatDate(pc.getRepayTime(),DateUtils.DATEFORMAT_STR_022));//为了将还款时间变为字符串，临时改为startTime
			}
		}
		final UserCompanyInfo userCompanyInfo = userCompanyInfoService.findByUserId(bondInvestUser.getUuid());
		final UserCompanyInfo bondUserCompanyInfo = userCompanyInfoService.findByUserId(bondUser.getUuid());
		this.bond = bond;
		this.bondInvest = bondInvest;
		this.project = project;
		this.projectInvest = projectInvest;
		this.bondInvestUser = bondInvestUser;
		this.bondInvestUserCache = bondInvestUserCache;
		this.borrowUser = borrowUser;
		this.borrowUserCache = borrowUserCache;
		this.bondUser = bondUser;
		this.bondUserCache = bondUserCache;
		this.bondProtocol = bondProtocol;
		this.userCompanyInfo = userCompanyInfo; 
		this.bondUserCompanyInfo = bondUserCompanyInfo;
		this.bondSumCapAndIn = bondSumCapAndIn;
		Date interestTime = DateUtils.getNow();
		if(this.project.getReviewTime() != null && this.project.getInterestStyle() != null){
			interestTime = this.project.getReviewTime(); 
			if(LoanEnum.INTEREST_STYLE_EV.eq(this.project.getInterestStyle())){
				interestTime = this.project.getReviewTime();
			}else if(LoanEnum.INTEREST_STYLE_TN.eq(this.project.getInterestStyle())){//T+N
				interestTime = DateUtils.rollDay(this.project.getReviewTime(), this.project.getInterestStartDays());
			}
		}
		this.interestStartDate = DateUtils.formatDate(interestTime,DateUtils.DATEFORMAT_STR_022);
		if(CollectionUtils.isNotEmpty(list)){
			this.repayList = list;
			this.interestEndDate = DateUtils.formatDate(list.get(list.size()-1).getRepayTime(),DateUtils.DATEFORMAT_STR_022);
		}else{
			this.repayList = null;
			this.interestEndDate = DateUtils.formatDate(DateUtils.getNow(),DateUtils.DATEFORMAT_STR_022);
		}
		this.downloadFileName = "[" +this.bond.getBondName() + "]"+ "债权转让协议" + ".pdf";
		
		// 业务预处理
		prepare();
		// 下载协议校验
		validDownload();
		// 初始化参数
		initData();
		// 创建pdf逻辑
		createPdf();
		//签名
		signPdf();
		//复制到static服务器上
		copyPdf();
		//删除本地的文件
		deletePdf();
		
	}

	@Override
	public void validDownload() {
		// 判断是否有模板
		if (this.bondProtocol == null || StringUtils.isBlank(this.bondProtocol.getContent())) {
			LOGGER.info("读取协议模板出错！");
		}
		// 判断受让用户是否存在
		if (this.bondInvestUser == null) {
			LOGGER.info("受让用户信息异常！");
		}
		// 判断借款人用户是否存在
		if (this.borrowUser == null) {
			LOGGER.info("借款人用户信息异常！");
		}
		// 判断转让用户是否存在
		if (this.bondUser == null) {
			LOGGER.info("转让用户信息异常！");
		}
	}

	@Override
	public void prepare() {
		
	}

	@Override
	public void initData() {
		data.put("bondUserName", StringUtils.hideName(this.bondUser.getRealName()));
		data.put("bondUserCardNo", UserCache.USER_NATURE_PERSON.equals(this.bondUserCache.getUserNature())?StringUtils.hideStr(this.bondUserCache.getIdNo(),6,this.bondUserCache.getIdNo().length()-4):StringUtils.hideLastChar(this.bondUserCompanyInfo.getOrgCode(),4));
		data.put("bondInvestUserName", StringUtils.hideName(this.bondInvestUser.getRealName()));
		data.put("bondInvestUserCardNo",UserCache.USER_NATURE_PERSON.equals(this.bondInvestUserCache.getUserNature())?StringUtils.hideStr(this.bondInvestUserCache.getIdNo(),6,this.bondInvestUserCache.getIdNo().length()-4):StringUtils.hideLastChar(this.userCompanyInfo.getOrgCode(),4));
		data.put("web_company", ConfigUtils.getValue("web_company"));
		data.put("web_url", ConfigUtils.getValue("web_url"));
		data.put("bondName", this.bond.getBondName());
		//添加字段债权编号 bondNo,债权金额大写
		data.put("borrowUserName", this.borrowUser.getRealName());
		data.put("borrowUserCardNo", this.borrowUserCache.getIdNo());
		data.put("borrowInitMoney", this.project.getAccount());
		data.put("interestStartDate", this.interestStartDate);
		data.put("interestEndDate", this.interestEndDate);
		data.put("remainDay", this.bond.getRemainDays());
		data.put("bondApr", this.bond.getBondApr());
		data.put("borrowApr", this.project.getApr());
		data.put("repayStyle", DictUtils.getData("repayStyle", this.project.getRepayStyle()).getItemName());
		data.put("repayList", this.repayList);
		data.put("date", DateUtils.dateStr6(this.bondInvest.getCreateTime()));
		data.put("bondInvestMoney", bondInvest.getAmount());
		data.put("protocolTypeName", this.bondProtocol.getName());
		
		//协议修改新增的填充的数据 2017-09-27
		data.put("bondNo", this.bond.getBondNo());//债权编号
		BigDecimal bondMoney = BigDecimal.ZERO;//本金利息之和
		BigDecimal sumCapital = BigDecimal.ZERO;//本金
		BigDecimal sumInterest = BigDecimal.ZERO;//利息
		try {
			sumCapital = BigDecimalUtils.div(bondSumCapAndIn.getCapital(), BigDecimal.ONE);
			sumInterest = BigDecimalUtils.div(bondSumCapAndIn.getInterest(), BigDecimal.ONE);
			bondMoney = BigDecimalUtils.add(sumCapital, sumInterest);
		} catch (Exception e) {
			LOGGER.error("债权本金和利息异常,{}", e.getLocalizedMessage());
		}
		data.put("bondMoneyCh", MoneyUtils.transform(bondMoney));//金额大写
		data.put("bondMoney", bondMoney.toPlainString());
		data.put("amount", sumCapital.toPlainString());//本金
		data.put("payedInterest", sumInterest.toPlainString());//利息
		data.put("receivedAccountCh", MoneyUtils.transform(bondInvest.getReceivedAccount()));//实付金额
		data.put("receivedAccount", BigDecimalUtils.div(bondInvest.getReceivedAccount(), BigDecimal.ONE).toPlainString());
		data.put("projectNo", project.getProjectNo());
		data.put("bondInvestTime", DateUtils.dateStr6(bondInvest.getCreateTime()));
	}
	
	/**
	 * 生成合同
	 */
	@Override
	public void createPdf() {
		File pdfFile = new File(inPdfName);
		try {
			if (!pdfFile.exists()) {
				pdfFile.mkdir();
			} 
			String out = FreemarkerUtil.renderTemplate(this.bondProtocol.getContent(), this.data);
			final String protocolStyle = ConfigUtils.getValue("protocolStyle");
			if(CommonEnum.YES.eq(protocolStyle)){
				ProtocolHelper.templateHtml(out, pdfHelper);
			}else{
				ProtocolHelper.fileHtml(inPdfName, out, pdfHelper);
			}
		}catch (IOException | DocumentException e) {
			LOGGER.error("生成pdf出错,{}", e.getMessage(), e);
			deletePdf();
		}
		pdfHelper.exportPdf();
		LOGGER.info("pdf生成成功：{}", pdfFile.getName());
	}
	
	/**
	 * 
	 * 复制合同
	 * @author jxx
	 * @date 2016年7月27日
	 */
	public void copyPdf() {
		File pdfFile = new File(inPdfName);
		final String imageServerUrl =ConfigUtils.getValue(ConfigConstant.IMAGE_SERVER_URL);
		String fileType = FileTypeUtil.getFileByFile(pdfFile);
		StringBuilder targetURL = new StringBuilder(imageServerUrl);
		targetURL.append("/upload/httpSaveBondPdf.html?nid=bondContract&prefix=").append(fileType)
			.append("&bondId=").append(this.bond.getUuid()).append("&bondInvestId=");
		String ownerUserId = null;
		if(userId.equals(this.bondInvestUser.getUuid())){
			//受让人
			ownerUserId = this.bondInvest.getUuid();
			targetURL.append(ownerUserId);
		}else if(userId.equals(bondUser.getUuid())){
			//转让人
			ownerUserId = userId;
			targetURL.append(ownerUserId).append("&bulidZip=true");
		}
		String retStr = "";
		PostMethod filePost = new PostMethod(targetURL.toString());
		filePost.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"utf-8"); 
		try {
			Part[] parts = { new FilePart("upload", pdfFile)};
			filePost.setRequestEntity(new MultipartRequestEntity(parts,
					filePost.getParams()));
			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams()
					.setConnectionTimeout(5000);
			int status = client.executeMethod(filePost);
			InputStream in = filePost.getResponseBodyAsStream();
			if (status == HttpStatus.SC_OK) {
				LOGGER.debug("上传成功");
				// 上传成功
				deletePdf();
			} else {
				// 上传失败
				LOGGER.error("上传失败,{}",status);
			}
			// 处理内容
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					in));
			String tempBf = null;
			StringBuilder html = new StringBuilder();
			try {
				while ((tempBf = reader.readLine()) != null) {
					html.append(tempBf);
				}
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
			retStr = html.toString();
			LOGGER.info("上传文件模块为：contract,返回文件路径:{}",retStr);
			uploadPath = retStr;
			
			
			//保存协议生成记录
			LOGGER.info("生成协议记录");
			ProtocolLog protocolLog = new ProtocolLog();
			protocolLog.setRemark("成功");
			protocolLog.setProtocolName(downloadFileName);
			protocolLog.setProtocolType(this.bondProtocol.getProtocolType());
			protocolLog.setProtocolId(this.bondProtocol.getUuid());
			protocolLog.setFilePath(this.bond.getUuid() + "-" + ownerUserId);
			protocolLog.setBusinessId(this.bondInvest.getUuid());
			protocolLog.setCreateTime(DateUtils.getNow());
			this.protocolLogService.insert(protocolLog);
			
			if(userId.equals(this.bondUser.getUuid())){//如果是转让人，则生成zip记录
				protocolLog = new ProtocolLog();
				protocolLog.setRemark("成功");
				protocolLog.setProtocolName("[" + this.bond.getBondName() + "]全部协议.zip");
				protocolLog.setProtocolType(this.bondProtocol.getProtocolType());
				protocolLog.setProtocolId(this.bondProtocol.getUuid());
				protocolLog.setFilePath(this.bond.getUuid() + "-" + this.bond.getUuid());
				protocolLog.setBusinessId(this.bondInvest.getUuid());
				protocolLog.setCreateTime(DateUtils.getNow());
				this.protocolLogService.insert(protocolLog);
			}
			
		} catch (Exception ex) {
			LOGGER.info("copyPdf error", ex);
			deletePdf();
		} finally {
			filePost.releaseConnection();
		}
	}
	
	/**
	 * 添加签名
	 * @author QianPengZhan
	 * @date 2016年10月19日
	 * @param user
	 * @param userCache
	 * @param userCompanyInfo
	 */
	private void setSignAccount(final User user,final UserCache userCache,final UserCompanyInfo userCompanyInfo){
		if(userCache.getSignAccount() == null ||  userCache.getSignSealData() == null ){
			if(UserCache.USER_NATURE_PERSON.equals(userCache.getUserNature())){
				//协议注册签名
				final Map<String,Object> map = Maps.newHashMap();
				map.put("type", UserCache.USER_NATURE_PERSON);
				map.put("name", user.getRealName());
				map.put("idno", userCache.getIdNo());
				map.put("mobile", user.getMobile());
				SignFactory factory = new SignFactory();
				
				//协议签章默认
				map.put("accountId", userCache== null?null:userCache.getSignAccount());
				map.put("templateType", PersonTemplateType.SQUARE);
				map.put("color", SealColor.RED);
				if (userCache != null) {
					userCache.setSignAccount(factory.register(map));
					userCache.setSignSealData(factory.createSeal(map));
					userCacheService.update(userCache);			
				}
			}else{
				//协议注册签名
				final Map<String, Object> map = Maps.newHashMap();
				map.put("type", UserCache.USER_NATURE_COMPANY);
				map.put("name", userCompanyInfo.getCompanyName());
				map.put("mobile", user.getMobile());
				map.put("organCode", userCompanyInfo.getOrgCode());
				map.put("legalName", userCompanyInfo.getLegalDelegate());
				map.put("legalIdNo", userCompanyInfo.getCertNo());
				//可选
				map.put("regCode",userCompanyInfo.getBussinessCode());
				
				//附属信息
				final UserCache cache = userCacheService.findByUserId(user.getUuid());
				final SignFactory factory = new SignFactory();
				cache.setSignAccount(factory.register(map));
				
				//协议签章默认
				map.put("accountId", cache.getSignAccount());
				map.put("templateType", OrganizeTemplateType.STAR);
				map.put("color", SealColor.RED);
				map.put("hText", Constant.SIGN_ACCOUNT_HTEXT);
				map.put("qText", Constant.SIGN_ACCOUNT_QTEXT);
				cache.setSignSealData(factory.createSeal(map));
				userCacheService.update(cache);
				CacheUtils.del(CacheConstant.KEY_PREFIX_USER_CACHE_USER_ID.concat(user.getUuid()));
			}
			CacheUtils.del(CacheConstant.KEY_PREFIX_USER_CACHE_USER_ID.concat(user.getUuid()));
		}
	}
	
	/**
	 * 
	 * 签署合同
	 * 如果是安心签的话，需要一次性上传多个签署用户，
	 * 如果是e签宝的话，需要重复签署
	 * @author jxx
	 * @date 2016年7月27日
	 */
	public void signPdf() {
		setSignAccount(this.bondInvestUser,this.bondInvestUserCache,this.userCompanyInfo);
		setSignAccount(this.bondUser,this.bondUserCache,this.bondUserCompanyInfo);
		
		final int repaySize = this.repayList == null ? 0:this.repayList.size();
		//转让人签名
		Map<String,Object> borrowMap = new HashMap<String,Object>();
		borrowMap.put("type", SignConstant.TYPE_SIGN_PUSER);
		borrowMap.put("accountId", bondUserCache.getSignAccount());
		borrowMap.put("sealData", bondUserCache.getSignSealData());
		borrowMap.put("srcPdfFile", this.inPdfName);
		borrowMap.put("dstPdfFile", this.inPdfName);
		PosBean borrowSignPos = new PosBean();
		borrowSignPos.setPosX(125);
		borrowSignPos.setPosY(335-repaySize*25);
		borrowSignPos.setPosPage("2");//签署页码，若为多页签章，支持页码格式“1-3,5,8“，若为坐标定位时，不可空
		borrowMap.put("signPos", borrowSignPos);
		borrowMap.put("signType", SignType.Single);/** * Single单页签章Multi多页签章Edges签骑缝章Key关键字签章*/
		borrowMap.put("fileName", this.downloadFileName);
		borrowMap.put("projectCode", project.getUuid());
		SignFactory borrowFactory = new SignFactory();
		borrowFactory.execute(borrowMap);
		
		//投资人签名
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("type", SignConstant.TYPE_SIGN_PUSER);
		map.put("accountId", bondInvestUserCache.getSignAccount());
		map.put("sealData", bondInvestUserCache.getSignSealData());
		map.put("srcPdfFile", this.inPdfName);
		map.put("dstPdfFile", this.inPdfName);
		PosBean signPos = new PosBean();
		signPos.setPosX(430);
		signPos.setPosY(335-repaySize*25);
		signPos.setPosPage("2");//签署页码，若为多页签章，支持页码格式“1-3,5,8“，若为坐标定位时，不可空
		map.put("signPos", signPos);
		map.put("signType", SignType.Single);/** * Single单页签章Multi多页签章Edges签骑缝章Key关键字签章*/
		map.put("fileName", this.downloadFileName);
		
		map.put("projectNo", this.bond.getBondNo());
		
		SignFactory factory = new SignFactory();
		factory.execute(map);
		
	}
	
	
	/**
	 * 删除协议
	 * @author QianPengZhan
	 * @date 2016年9月29日
	 */
	public void deletePdf(){
		File pdfFile = new File(inPdfName);
		if(pdfFile.exists()){
			boolean result = pdfFile.delete();
			LOGGER.info("删除结果：{}", result);
		}else{
			LOGGER.info("已删除或不存在");
		}
	}
	
	
	
	/**
	 * 获取属性bondService的值
	 * @return bondService属性值
	 */
	public BondService getBondService() {
		return bondService;
	}

	/**
	 * 设置属性bondService的值
	 * @param  bondService属性值
	 */
	public void setBondService(BondService bondService) {
		this.bondService = bondService;
	}

	/**
	 * 获取属性projectService的值
	 * @return projectService属性值
	 */
	public ProjectService getProjectService() {
		return projectService;
	}

	/**
	 * 设置属性projectService的值
	 * @param  projectService属性值
	 */
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	/**
	 * 获取属性downloadDate的值
	 * @return downloadDate属性值
	 */
	public String getDownloadDate() {
		return downloadDate;
	}

	/**
	 * 设置属性downloadDate的值
	 * @param  downloadDate属性值
	 */
	public void setDownloadDate(String downloadDate) {
		this.downloadDate = downloadDate;
	}

	/**
	 * 获取属性contextPath的值
	 * @return contextPath属性值
	 */
	public String getContextPath() {
		return contextPath;
	}

	/**
	 * 设置属性contextPath的值
	 * @param  contextPath属性值
	 */
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	/**
	 * 获取属性bond的值
	 * @return bond属性值
	 */
	public Bond getBond() {
		return bond;
	}

	/**
	 * 设置属性bond的值
	 * @param  bond属性值
	 */
	public void setBond(Bond bond) {
		this.bond = bond;
	}

	/**
	 * 获取属性bondInvest的值
	 * @return bondInvest属性值
	 */
	public BondInvest getBondInvest() {
		return bondInvest;
	}

	/**
	 * 设置属性bondInvest的值
	 * @param  bondInvest属性值
	 */
	public void setBondInvest(BondInvest bondInvest) {
		this.bondInvest = bondInvest;
	}

	/**
	 * 获取属性project的值
	 * @return project属性值
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * 设置属性project的值
	 * @param  project属性值
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * 获取属性projectInvest的值
	 * @return projectInvest属性值
	 */
	public ProjectInvest getProjectInvest() {
		return projectInvest;
	}

	/**
	 * 设置属性projectInvest的值
	 * @param  projectInvest属性值
	 */
	public void setProjectInvest(ProjectInvest projectInvest) {
		this.projectInvest = projectInvest;
	}

	/**
	 * 获取属性bondInvestUser的值
	 * @return bondInvestUser属性值
	 */
	public User getBondInvestUser() {
		return bondInvestUser;
	}

	/**
	 * 设置属性bondInvestUser的值
	 * @param  bondInvestUser属性值
	 */
	public void setBondInvestUser(User bondInvestUser) {
		this.bondInvestUser = bondInvestUser;
	}

	/**
	 * 获取属性bondInvestUserCache的值
	 * @return bondInvestUserCache属性值
	 */
	public UserCache getBondInvestUserCache() {
		return bondInvestUserCache;
	}

	/**
	 * 设置属性bondInvestUserCache的值
	 * @param  bondInvestUserCache属性值
	 */
	public void setBondInvestUserCache(UserCache bondInvestUserCache) {
		this.bondInvestUserCache = bondInvestUserCache;
	}

	/**
	 * 获取属性bondUser的值
	 * @return bondUser属性值
	 */
	public User getBondUser() {
		return bondUser;
	}

	/**
	 * 设置属性bondUser的值
	 * @param  bondUser属性值
	 */
	public void setBondUser(User bondUser) {
		this.bondUser = bondUser;
	}

	/**
	 * 获取属性bondUserCache的值
	 * @return bondUserCache属性值
	 */
	public UserCache getBondUserCache() {
		return bondUserCache;
	}

	/**
	 * 设置属性bondUserCache的值
	 * @param  bondUserCache属性值
	 */
	public void setBondUserCache(UserCache bondUserCache) {
		this.bondUserCache = bondUserCache;
	}

	/**
	 * 获取属性borrowUser的值
	 * @return borrowUser属性值
	 */
	public User getBorrowUser() {
		return borrowUser;
	}

	/**
	 * 设置属性borrowUser的值
	 * @param  borrowUser属性值
	 */
	public void setBorrowUser(User borrowUser) {
		this.borrowUser = borrowUser;
	}

	/**
	 * 获取属性borrowUserCache的值
	 * @return borrowUserCache属性值
	 */
	public UserCache getBorrowUserCache() {
		return borrowUserCache;
	}

	/**
	 * 设置属性borrowUserCache的值
	 * @param  borrowUserCache属性值
	 */
	public void setBorrowUserCache(UserCache borrowUserCache) {
		this.borrowUserCache = borrowUserCache;
	}

	/**
	 * 获取属性interestStartDate的值
	 * @return interestStartDate属性值
	 */
	public String getInterestStartDate() {
		return interestStartDate;
	}

	/**
	 * 设置属性interestStartDate的值
	 * @param  interestStartDate属性值
	 */
	public void setInterestStartDate(String interestStartDate) {
		this.interestStartDate = interestStartDate;
	}

	/**
	 * 获取属性interestEndDate的值
	 * @return interestEndDate属性值
	 */
	public String getInterestEndDate() {
		return interestEndDate;
	}

	/**
	 * 设置属性interestEndDate的值
	 * @param  interestEndDate属性值
	 */
	public void setInterestEndDate(String interestEndDate) {
		this.interestEndDate = interestEndDate;
	}

	/**
	 * 获取属性bondProtocol的值
	 * @return bondProtocol属性值
	 */
	public Protocol getBondProtocol() {
		return bondProtocol;
	}

	/**
	 * 设置属性bondProtocol的值
	 * @param  bondProtocol属性值
	 */
	public void setBondProtocol(Protocol bondProtocol) {
		this.bondProtocol = bondProtocol;
	}

	/**
	 * 获取属性pdfHelper的值
	 * @return pdfHelper属性值
	 */
	public PdfHelper getPdfHelper() {
		return pdfHelper;
	}

	/**
	 * 设置属性pdfHelper的值
	 * @param  pdfHelper属性值
	 */
	public void setPdfHelper(PdfHelper pdfHelper) {
		this.pdfHelper = pdfHelper;
	}

	/**
	 * 获取属性userId的值
	 * @return userId属性值
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置属性userId的值
	 * @param  userId属性值
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 获取属性repayList的值
	 * @return repayList属性值
	 */
	public List<ProjectCollection> getRepayList() {
		return repayList;
	}

	/**
	 * 设置属性repayList的值
	 * @param  repayList属性值
	 */
	public void setRepayList(List<ProjectCollection> repayList) {
		this.repayList = repayList;
	}

	/**
	 * 获取属性data的值
	 * @return data属性值
	 */
	public Map<String, Object> getData() {
		return data;
	}

	/**
	 * 设置属性data的值
	 * @param  data属性值
	 */
	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	/**
	 * 获取属性inPdfName的值
	 * @return inPdfName属性值
	 */
	public String getInPdfName() {
		return inPdfName;
	}

	/**
	 * 设置属性inPdfName的值
	 * @param  inPdfName属性值
	 */
	public void setInPdfName(String inPdfName) {
		this.inPdfName = inPdfName;
	}

	/**
	 * 获取属性downloadFileName的值
	 * @return downloadFileName属性值
	 */
	public String getDownloadFileName() {
		return downloadFileName;
	}

	/**
	 * 设置属性downloadFileName的值
	 * @param  downloadFileName属性值
	 */
	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	/**
	 * 获取属性uploadPath的值
	 * @return uploadPath属性值
	 */
	public String getUploadPath() {
		return uploadPath;
	}

	/**
	 * 设置属性uploadPath的值
	 * @param  uploadPath属性值
	 */
	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	/**
	 * 获取属性userCompanyInfo的值
	 * @return userCompanyInfo属性值
	 */
	public UserCompanyInfo getUserCompanyInfo() {
		return userCompanyInfo;
	}

	/**
	 * 设置属性userCompanyInfo的值
	 * @param  userCompanyInfo属性值
	 */
	public void setUserCompanyInfo(UserCompanyInfo userCompanyInfo) {
		this.userCompanyInfo = userCompanyInfo;
	}

	/**
	 * 获取属性bondInvestId的值
	 * @return bondInvestId属性值
	 */
	public String getBondInvestId() {
		return bondInvestId;
	}

	/**
	 * 设置属性bondInvestId的值
	 * @param  bondInvestId属性值
	 */
	public void setBondInvestId(String bondInvestId) {
		this.bondInvestId = bondInvestId;
	}

	/**
	 * 获取属性bondUserCompanyInfo的值
	 * @return bondUserCompanyInfo属性值
	 */
	public UserCompanyInfo getBondUserCompanyInfo() {
		return bondUserCompanyInfo;
	}

	/**
	 * 设置属性bondUserCompanyInfo的值
	 * @param  bondUserCompanyInfo属性值
	 */
	public void setBondUserCompanyInfo(UserCompanyInfo bondUserCompanyInfo) {
		this.bondUserCompanyInfo = bondUserCompanyInfo;
	}
	
	
}


class MyFilter implements FilenameFilter{
	private String type;
	public MyFilter(String type){
		this.type = type;
	}
	public boolean accept(File dir,String name){
		return name.endsWith(type);
	}
}
