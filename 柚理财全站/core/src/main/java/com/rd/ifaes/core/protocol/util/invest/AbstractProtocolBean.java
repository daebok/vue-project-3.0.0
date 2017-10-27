package com.rd.ifaes.core.protocol.util.invest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.security.shiro.IdGen;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.FreemarkerUtil;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.sign.constant.SignConstant;
import com.rd.ifaes.core.core.sign.factory.SignFactory;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.DictUtils;
import com.rd.ifaes.core.core.util.FileTypeUtil;
import com.rd.ifaes.core.core.util.PdfHelper;
import com.rd.ifaes.core.project.domain.Product;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.domain.Realize;
import com.rd.ifaes.core.project.service.ProjectCollectionService;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.project.service.RealizeService;
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
 * 
 *  协议抽象类
 *  根据自己的业务定义自己的字段
 * @version 3.0
 * @author jxx
 * @date 2016年7月27日
 */
public abstract class AbstractProtocolBean implements ProtocolBean {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractProtocolBean.class);

	protected UserService userService;
	protected UserCacheService userCacheService;
	protected ProjectService projectService;
	protected ProjectInvestService projectInvestService;
	protected ProjectCollectionService projectCollectionService;
	protected ProtocolLogService protocolLogService;
	protected UserCompanyInfoService userCompanyInfoService;
	protected RealizeService realizeService;
	// 生成pdf路径名
	protected String inPdfName;
	//未盖章之前的pdf路径
	protected String tempStr;
	// 下载pdf名
	protected String downloadFileName;
	// 标id
	protected String productId;
	//项目id
	protected String projectId;
	//项目
	protected Project project;
	
	// 投标标id
	protected String investId;
	
	protected ProjectInvest invest;
	// 投标用户
	protected User investUser;
	protected UserCache investUserCache;
	protected UserCompanyInfo investUserCompanyInfo;
	//借款人
	protected User borrowUser;
	protected UserCache borrowUserCache;
	protected UserCompanyInfo borrowUserCompanyInfo;
	// 协议模板
	protected Protocol protocol;
	// 操纵用户Id
	protected String userId;
	
	//投资服务协议编号
	protected String  investServiceNo;
	
	protected String  bondServiceNo;
	
	//生成pdf的文件
	protected String contextPath;
	
	//下载时间
	protected String downloadDate;
	
	protected Product product;
	
	protected Realize realize;
	
	protected ProjectInvest projectInvest;
	
	protected String investAmount;
	//回款类型
	protected String incomeTypeName;
	//回款金额
	protected String incomeAccount;
	//成立审核时间
	protected String establishVerifyTime;
	//债权剩余时间
	protected int leaveDay;
	
	protected PdfHelper pdf;
	// 引入数据map
	protected Map<String, Object> data = new HashMap<>();

	protected String uploadPath;
	
	/**
	 * 业务核心处理方法
	 */
	@Override
	public void executer(String projectId, Protocol protocol, String userId) {
		this.executer(projectId, "", protocol, userId);
	}

	/**
	 * 业务核心处理方法
	 */
	public void executer(String projectId, String investId, Protocol protocol, String userId) {
		// 初始化基础参数
		LOGGER.info("初始化ProtocolBean productId:{},investId:{}", productId ,investId);
		this.userService = (UserService) SpringContextHolder.getBean("userService");
		this.userCacheService = (UserCacheService) SpringContextHolder.getBean("userCacheService");
		this.projectService = (ProjectService) SpringContextHolder.getBean("projectService");
		this.projectInvestService = (ProjectInvestService) SpringContextHolder.getBean("projectInvestService");
		this.protocolLogService = (ProtocolLogService) SpringContextHolder.getBean("protocolLogService");
		this.userCompanyInfoService = (UserCompanyInfoService)SpringContextHolder.getBean("userCompanyInfoService");
		this.realizeService = (RealizeService)SpringContextHolder.getBean("realizeService");
		this.contextPath = AbstractProtocolBean.class.getClassLoader().getResource("/").getPath();
		LOGGER.info("路径：{}",contextPath);
		this.downloadDate = DateUtils.formatDate(DateUtils.getNow(),DateUtils.DATEFORMAT_STR_012);
		this.tempStr = contextPath + "/"  + investId + "_" + IdGen.uuid() + ".pdf";
		this.inPdfName = contextPath + "/"  + investId + "_" + System.currentTimeMillis() + ".pdf";
		File pdfFile = new File(contextPath);
		try {
			if (!pdfFile.exists()) {
				pdfFile.mkdir();
			}
		} catch (Exception e) {
			LOGGER.error("生成pdf出错", e);
		}
		
		this.pdf = PdfHelper.instance(tempStr);
		this.projectId = projectId;
		this.project = projectService.get(projectId); 
		this.establishVerifyTime = DateUtils.dateStr6(this.project.getReviewTime()==null?DateUtils.getNow():this.project.getReviewTime());
		this.investId = investId;
		this.projectInvest = projectInvestService.get(investId);
		this.downloadFileName = "[" +this.project.getProjectName() + "]"+ "投资协议" + ".pdf";
		this.userId = userId;
		this.investUser = userService.get(projectInvest.getUserId());
		this.investUserCache = userCacheService.findByUserId(this.investUser.getUuid());
		this.investUserCompanyInfo = userCompanyInfoService.findByUserId(this.investUser.getUuid());
		this.borrowUser = userService.get(project.getUserId());
		this.borrowUserCache = userCacheService.findByUserId(this.borrowUser.getUuid());
		this.borrowUserCompanyInfo = userCompanyInfoService.findByUserId(this.borrowUser.getUuid());
		this.protocol = protocol;
		if(LoanEnum.REALIZE_FLAG_REALIZE.eq(project.getRealizeFlag())){
			this.realize = realizeService.findRealize(projectId);
		}
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
		
	/**
	 * 填充数据
	 */
	@Override
	public void initData() {
		//需要加的数据investUserMobile  担保机构 vouchInstitution  projectUserMobile
		data.put("date", this.establishVerifyTime);
		data.put("investUserUserName", this.investUser.getUserName());
		data.put("investUserRealName", this.investUser.getRealName());
		data.put("investUserMobile", StringUtils.isPhone(investUser.getMobile())?StringUtils.hideStr(investUser.getMobile(), 3, 7):investUser.getMobile());
		data.put("projectUserMobile", StringUtils.isPhone(borrowUser.getMobile())?StringUtils.hideStr(borrowUser.getMobile(), 3, 7):borrowUser.getMobile());
		String investUserCardInfo = StringUtils.EMPTY;
		if(UserCache.USER_NATURE_PERSON.equals(this.investUserCache.getUserNature()) && investUserCache != null){
			investUserCardInfo = StringUtils.hideStr(this.investUserCache.getIdNo(), 6, this.investUserCache.getIdNo().length()-4);
		}else if(UserCache.USER_NATURE_COMPANY.equals(this.investUserCache.getUserNature()) && investUserCompanyInfo != null){
			if(Constant.FLAG_YES.equals(investUserCompanyInfo.getThreeCertificate())){// 三证合一
				investUserCardInfo = StringUtils.hideLastChar(this.investUserCompanyInfo.getCreditCode(), 4);
			}else{
				investUserCardInfo = StringUtils.hideLastChar(this.investUserCompanyInfo.getOrgCode(), 4);
			}
		}
		data.put("investUserCardInfo", StringUtils.isBlank(investUserCardInfo)?StringUtils.EMPTY:investUserCardInfo);
		data.put("projectUserRealName", this.borrowUser.getRealName());
		String projectUserCardInfo = StringUtils.EMPTY;
		if(UserCache.USER_NATURE_PERSON.equals(this.borrowUserCache.getUserNature()) && borrowUserCache != null){
			projectUserCardInfo = this.borrowUserCache.getIdNo();
		}else if(UserCache.USER_NATURE_COMPANY.equals(this.borrowUserCache.getUserNature()) && borrowUserCompanyInfo != null){
			if(Constant.FLAG_YES.equals(borrowUserCompanyInfo.getThreeCertificate())){// 三证合一
				projectUserCardInfo = this.borrowUserCompanyInfo.getCreditCode();
			}else{
				projectUserCardInfo = this.borrowUserCompanyInfo.getOrgCode();
			}
		}
		data.put("projectUserCardInfo", projectUserCardInfo);
		data.put("webCompany", ConfigUtils.getValue("web_company"));
		data.put("webName", ConfigUtils.getValue("web_url"));
		data.put("investMoney", BigDecimalUtils.div(this.projectInvest.getAmount(), BigDecimal.ONE).toPlainString());
		data.put("investTime", DateUtils.dateStr4(this.projectInvest.getCreateTime()));
		data.put("collectionMoney", this.projectInvest.getPayment());
		data.put("projectMoney", this.project.getAccount());
		data.put("protocolTypeName", this.protocol.getName());
		data.put("apr", project.getApr() + "%");
		data.put("timeLimit", project.getTimeLimitStr());
		data.put("interestDate", DateUtils.dateStr6(this.projectInvest.getInterestDate()));//起息日
		data.put("endDate", DateUtils.dateStr6(project.getLastRepayTime()));//结束日
		String vouchInstitution = "";//担保机构
		if(CommonEnum.YES.eq(project.getIsVouch())){
			UserCompanyInfo userCompanyInfo = userCompanyInfoService.findByUserId(project.getVouchId());
			if(userCompanyInfo != null){
				vouchInstitution = userCompanyInfo.getCompanyName();
			}
		}
		data.put("vouchInstitution", vouchInstitution);
		if(LoanEnum.REALIZE_FLAG_REALIZE.eq(project.getRealizeFlag())){
			data.put("repayStyle", DictUtils.getItemName("repayStyle", project.getRepayStyle()));
			data.put("interestDate", DateUtils.dateStr6(this.projectInvest.getInterestDate()));
			data.put("endDate", DateUtils.dateStr6(project.getLastRepayTime()));
			Project oldProject = projectService.get(realize.getOldProjectId());
			data.put("oldProjectName", oldProject.getProjectName());
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
		setSignAccount(this.investUser,this.investUserCache,this.investUserCompanyInfo);
		setSignAccount(this.borrowUser,this.borrowUserCache,this.borrowUserCompanyInfo);
		//借款人签名
		Map<String,Object> borrowMap = new HashMap<String,Object>();
		borrowMap.put("type", SignConstant.TYPE_SIGN_PUSER);
		borrowMap.put("accountId", borrowUserCache.getSignAccount());
		borrowMap.put("sealData", borrowUserCache.getSignSealData());
		LOGGER.info("borrow签名参数--accountId：{}--sealData：{}---",borrowUserCache.getSignAccount(),borrowUserCache.getSignSealData());
		borrowMap.put("srcPdfFile", this.inPdfName);
		borrowMap.put("dstPdfFile", this.inPdfName);
		PosBean borrowSignPos = new PosBean();
		borrowSignPos.setPosX(430);
		borrowSignPos.setPosY(250);
		borrowSignPos.setPosPage("4");//签署页码，若为多页签章，支持页码格式“1-3,5,8“，若为坐标定位时，不可空
		borrowMap.put("signPos", borrowSignPos);
		borrowMap.put("signType", SignType.Single);/** * Single单页签章Multi多页签章Edges签骑缝章Key关键字签章*/
		borrowMap.put("fileName", this.downloadFileName);
		borrowMap.put("projectCode", project.getUuid());

		SignFactory borrowFactory = new SignFactory();
		borrowFactory.execute(borrowMap);
		
		
		//投资人签名
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("type", SignConstant.TYPE_SIGN_PUSER);
		map.put("accountId", investUserCache.getSignAccount());
		map.put("sealData", investUserCache.getSignSealData());
		LOGGER.info("invest签名参数--accountId：{}--sealData：{}---",investUserCache.getSignAccount(),investUserCache.getSignSealData());
		map.put("srcPdfFile", this.inPdfName);
		map.put("dstPdfFile", this.inPdfName);
		PosBean signPos = new PosBean();
		signPos.setPosX(125);
		signPos.setPosY(250);
		signPos.setPosPage("4");//签署页码，若为多页签章，支持页码格式“1-3,5,8“，若为坐标定位时，不可空
		map.put("signPos", signPos);
		map.put("signType", SignType.Single);/** * Single单页签章Multi多页签章Edges签骑缝章Key关键字签章*/
		map.put("fileName", this.downloadFileName);
		
		map.put("projectNo", project.getUuid());
		
		SignFactory factory = new SignFactory();
		factory.execute(map);
		
	}
	
	/**
	 * 
	 * 复制合同
	 * @author jxx
	 * @date 2016年7月27日
	 */
	public void copyPdf() {
		File pdfFile = new File(inPdfName);
		final String imageServerUrl =  ConfigUtils.getValue("image_server_url"); 
		String fileType = FileTypeUtil.getFileByFile(pdfFile);
		StringBuilder targetURL = new StringBuilder(imageServerUrl);
		targetURL.append("/upload/httpSavePdf.html?nid=projectContract&prefix=").append(fileType)
			.append("&projectId=").append(projectId).append("&investId=");
		String ownerUserId = null;
		if(userId.equals(investUser.getUuid())){
			//投资人
			ownerUserId = investId;
			targetURL.append(ownerUserId);
		}else if(userId.equals(borrowUser.getUuid())){
			//借款人
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
			LOGGER.info("上传文件模块为：projectContract,返回文件路径:{}",retStr);
			uploadPath = retStr;
			
			
			//保存协议生成记录
			LOGGER.info("生成协议记录");
			ProtocolLog protocolLog = new ProtocolLog();
			protocolLog.setRemark("成功");
			protocolLog.setProtocolName(downloadFileName);
			protocolLog.setProtocolType(protocol.getProtocolType());
			protocolLog.setProtocolId(protocol.getUuid());
			protocolLog.setFilePath(projectId + "-" + ownerUserId);
			protocolLog.setBusinessId(investId);
			protocolLog.setCreateTime(DateUtils.getNow());
			protocolLogService.insert(protocolLog);
			
			if(userId.equals(borrowUser.getUuid())){//如果是借款人，则生成zip记录
				protocolLog = new ProtocolLog();
				protocolLog.setRemark("成功");
				protocolLog.setProtocolName("[" + this.project.getProjectName() + "]全部协议.zip");
				protocolLog.setProtocolType(protocol.getProtocolType());
				protocolLog.setProtocolId(protocol.getUuid());
				protocolLog.setFilePath(projectId + "-" + projectId);
				protocolLog.setBusinessId(investId);
				protocolLog.setCreateTime(DateUtils.getNow());
				protocolLogService.insert(protocolLog);
			}
			
		} catch (Exception ex) {
			LOGGER.info("copyPdf error", ex);
			deletePdf();
		} finally {
			filePost.releaseConnection();
		}
	}
	
	/**
	 * 
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
	public void deletePdf(final String pdfPath){
		File pdfFile = new File(pdfPath);
		if(pdfFile.exists()){
			boolean result = pdfFile.delete();
			LOGGER.info("删除结果：{}", result);
		}else{
			LOGGER.info("已删除或不存在");
		}
	}

	@Override
	public void validDownload() {
		// 判断是否有模板
		if (this.protocol == null || StringUtils.isBlank(this.protocol.getContent())) {
			LOGGER.info("读取协议模板出错！");
		}
		
		// 判断用户是否存在
		if (userId == null) {
			LOGGER.info("用户信息异常！");
		}
		if (investUser == null) {
			LOGGER.info("用户信息异常！");
		}
	}

	
	/**
	 * 生成合同
	 */
	@Override
	public void createPdf() {
		boolean checkFile = false;
		File pdfFile = new File(tempStr);
		try {
			if (!pdfFile.exists()) {
				pdfFile.mkdir();
			}
			String out = FreemarkerUtil.renderTemplate(protocol.getContent(), this.data);
			ProtocolHelper.templateHtml(out, pdf);
			checkFile = true;
		}catch(IOException | DocumentException  e2){
			LOGGER.error("生成pdf出错,{}", e2.getMessage() , e2);
			deletePdf();
		}
		if (!checkFile) {
//			throw new ProductException("pdf生成的路径不存在...");
		}
		pdf.exportPdf();
		
		PdfReader reader = null;
		
        PdfStamper stamp = null;
        FileOutputStream out = null;
        FileInputStream is = null;

		try {
			is = new FileInputStream(tempStr);
			LOGGER.debug("is读取的文件流：{}", is.available());
			reader = new PdfReader(is);
	  
		    out = new FileOutputStream(inPdfName);
		    stamp = new PdfStamper(reader, out);  
		    String jyylc = SpringContextHolder.class.getClassLoader().getResource("/").getPath() + "protocolpdf/jyylc.png";
			LOGGER.debug("水印图片路径------" + jyylc);

			int total = reader.getNumberOfPages();
			PdfContentByte under = stamp.getUnderContent(total);  
		    LOGGER.debug("图片路径vouchPic.substring(1)------" + jyylc.substring(1));
		    Image image = Image.getInstance(jyylc);
		    image.setAbsolutePosition(40,450);    
		    under.addImage(image);  // 插入水印     
		    stamp.close();//将数据写入文件
		    
		    LOGGER.info("pdf生成成功：{}", pdfFile.getName());
		} catch (IOException e) {
			LOGGER.error("IOException错误------" + e.getLocalizedMessage());
			e.printStackTrace();
		} catch (DocumentException e) {
			LOGGER.error("DocumentException错误------" + e.getLocalizedMessage());
			e.printStackTrace();
		}finally{
			//删除tempStr
			if(pdfFile.exists()) {
				LOGGER.info("tempPdfFile的大小------" + pdfFile.length());
				pdfFile.delete();  
		    } 
			//关闭流
			try {
				if(is != null){
					is.close();
				}
				if(reader != null){
					reader.close();
				}
				if(out != null){
					out.close();
				}
			} catch (Exception e2) {
			}
		}
	}

	public String getInPdfName() {
		return inPdfName;
	}

	public void setInPdfName(String inPdfName) {
		this.inPdfName = inPdfName;
	}

	public String getDownloadFileName() {
		return downloadFileName;
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	/**
	 * 获取属性productId的值
	 * @return productId属性值
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * 设置属性productId的值
	 * @param  productId属性值
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * 获取属性projectId的值
	 * @return projectId属性值
	 */
	public String getProjectId() {
		return projectId;
	}

	/**
	 * 设置属性projectId的值
	 * @param  projectId属性值
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
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
	 * 获取属性investId的值
	 * @return investId属性值
	 */
	public String getInvestId() {
		return investId;
	}

	/**
	 * 设置属性investId的值
	 * @param  investId属性值
	 */
	public void setInvestId(String investId) {
		this.investId = investId;
	}

	/**
	 * 获取属性invest的值
	 * @return invest属性值
	 */
	public ProjectInvest getInvest() {
		return invest;
	}

	/**
	 * 设置属性invest的值
	 * @param  invest属性值
	 */
	public void setInvest(ProjectInvest invest) {
		this.invest = invest;
	}

	/**
	 * 获取属性investUser的值
	 * @return investUser属性值
	 */
	public User getInvestUser() {
		return investUser;
	}

	/**
	 * 设置属性investUser的值
	 * @param  investUser属性值
	 */
	public void setInvestUser(User investUser) {
		this.investUser = investUser;
	}

	/**
	 * 获取属性investUserCache的值
	 * @return investUserCache属性值
	 */
	public UserCache getInvestUserCache() {
		return investUserCache;
	}

	/**
	 * 设置属性investUserCache的值
	 * @param  investUserCache属性值
	 */
	public void setInvestUserCache(UserCache investUserCache) {
		this.investUserCache = investUserCache;
	}

	/**
	 * 获取属性investUserCompanyInfo的值
	 * @return investUserCompanyInfo属性值
	 */
	public UserCompanyInfo getInvestUserCompanyInfo() {
		return investUserCompanyInfo;
	}

	/**
	 * 设置属性investUserCompanyInfo的值
	 * @param  investUserCompanyInfo属性值
	 */
	public void setInvestUserCompanyInfo(UserCompanyInfo investUserCompanyInfo) {
		this.investUserCompanyInfo = investUserCompanyInfo;
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
	 * 获取属性borrowUserCompanyInfo的值
	 * @return borrowUserCompanyInfo属性值
	 */
	public UserCompanyInfo getBorrowUserCompanyInfo() {
		return borrowUserCompanyInfo;
	}

	/**
	 * 设置属性borrowUserCompanyInfo的值
	 * @param  borrowUserCompanyInfo属性值
	 */
	public void setBorrowUserCompanyInfo(UserCompanyInfo borrowUserCompanyInfo) {
		this.borrowUserCompanyInfo = borrowUserCompanyInfo;
	}

	/**
	 * 获取属性protocol的值
	 * @return protocol属性值
	 */
	public Protocol getProtocol() {
		return protocol;
	}

	/**
	 * 设置属性protocol的值
	 * @param  protocol属性值
	 */
	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
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
	 * 获取属性investServiceNo的值
	 * @return investServiceNo属性值
	 */
	public String getInvestServiceNo() {
		return investServiceNo;
	}

	/**
	 * 设置属性investServiceNo的值
	 * @param  investServiceNo属性值
	 */
	public void setInvestServiceNo(String investServiceNo) {
		this.investServiceNo = investServiceNo;
	}

	/**
	 * 获取属性bondServiceNo的值
	 * @return bondServiceNo属性值
	 */
	public String getBondServiceNo() {
		return bondServiceNo;
	}

	/**
	 * 设置属性bondServiceNo的值
	 * @param  bondServiceNo属性值
	 */
	public void setBondServiceNo(String bondServiceNo) {
		this.bondServiceNo = bondServiceNo;
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
	 * 获取属性product的值
	 * @return product属性值
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * 设置属性product的值
	 * @param  product属性值
	 */
	public void setProduct(Product product) {
		this.product = product;
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
	 * 获取属性investAmount的值
	 * @return investAmount属性值
	 */
	public String getInvestAmount() {
		return investAmount;
	}

	/**
	 * 设置属性investAmount的值
	 * @param  investAmount属性值
	 */
	public void setInvestAmount(String investAmount) {
		this.investAmount = investAmount;
	}

	/**
	 * 获取属性incomeTypeName的值
	 * @return incomeTypeName属性值
	 */
	public String getIncomeTypeName() {
		return incomeTypeName;
	}

	/**
	 * 设置属性incomeTypeName的值
	 * @param  incomeTypeName属性值
	 */
	public void setIncomeTypeName(String incomeTypeName) {
		this.incomeTypeName = incomeTypeName;
	}

	/**
	 * 获取属性incomeAccount的值
	 * @return incomeAccount属性值
	 */
	public String getIncomeAccount() {
		return incomeAccount;
	}

	/**
	 * 设置属性incomeAccount的值
	 * @param  incomeAccount属性值
	 */
	public void setIncomeAccount(String incomeAccount) {
		this.incomeAccount = incomeAccount;
	}

	/**
	 * 获取属性leaveDay的值
	 * @return leaveDay属性值
	 */
	public int getLeaveDay() {
		return leaveDay;
	}

	/**
	 * 设置属性leaveDay的值
	 * @param  leaveDay属性值
	 */
	public void setLeaveDay(int leaveDay) {
		this.leaveDay = leaveDay;
	}

	/**
	 * 获取属性pdf的值
	 * @return pdf属性值
	 */
	public PdfHelper getPdf() {
		return pdf;
	}

	/**
	 * 设置属性pdf的值
	 * @param  pdf属性值
	 */
	public void setPdf(PdfHelper pdf) {
		this.pdf = pdf;
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
	 * 获取属性establishVerifyTime的值
	 * @return establishVerifyTime属性值
	 */
	public String getEstablishVerifyTime() {
		return establishVerifyTime;
	}

	/**
	 * 设置属性establishVerifyTime的值
	 * @param  establishVerifyTime属性值
	 */
	public void setEstablishVerifyTime(String establishVerifyTime) {
		this.establishVerifyTime = establishVerifyTime;
	}

	/**
	 * 获取属性realize的值
	 * @return realize属性值
	 */
	public Realize getRealize() {
		return realize;
	}

	/**
	 * 设置属性realize的值
	 * @param  realize属性值
	 */
	public void setRealize(Realize realize) {
		this.realize = realize;
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
