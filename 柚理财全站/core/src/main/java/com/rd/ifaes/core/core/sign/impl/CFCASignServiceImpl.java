package com.rd.ifaes.core.core.sign.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cfca.sadk.algorithm.common.PKIException;
import cfca.trustsign.common.vo.cs.ContractVO;
import cfca.trustsign.common.vo.cs.CreateContractVO;
import cfca.trustsign.common.vo.cs.EnterpriseTransactorVO;
import cfca.trustsign.common.vo.cs.EnterpriseVO;
import cfca.trustsign.common.vo.cs.HeadVO;
import cfca.trustsign.common.vo.cs.PersonVO;
import cfca.trustsign.common.vo.cs.ProxySignVO;
import cfca.trustsign.common.vo.cs.SignInfoVO;
import cfca.trustsign.common.vo.request.tx3.Tx3001ReqVO;
import cfca.trustsign.common.vo.request.tx3.Tx3002ReqVO;
import cfca.trustsign.common.vo.request.tx3.Tx3101ReqVO;
import cfca.trustsign.common.vo.request.tx3.Tx3102ReqVO;
import cfca.trustsign.common.vo.request.tx3.Tx3201ReqVO;
import cfca.trustsign.common.vo.request.tx3.Tx3202ReqVO;
import cfca.trustsign.common.vo.request.tx3.Tx3210ReqVO;
import cfca.trustsign.common.vo.request.tx3.Tx3211ReqVO;
import cfca.trustsign.common.vo.response.tx3.Tx3001ResVO;
import cfca.trustsign.common.vo.response.tx3.Tx3002ResVO;
import cfca.trustsign.common.vo.response.tx3.Tx3201ResVO;

import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.JsonMapper;
import com.rd.ifaes.core.core.sign.SignPortal;
import com.rd.ifaes.core.core.sign.cfca.connector.HttpConnector;
import com.rd.ifaes.core.core.sign.cfca.util.SecurityUtil;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.user.domain.UserCache;

/**
 * 
 * @ClassName: CFCASignServiceImpl 
 * @Description: CFCA实现
 * @author jxx 
 * @date 2016年7月25日 下午2:52:05 
 *
 */
public class CFCASignServiceImpl implements SignPortal {
	private static final Logger LOGGER = LoggerFactory.getLogger(CFCASignServiceImpl.class);

	private final String platId = ConfigUtils.getValue("sign_id");//38814918D6336888E05311016B0A0B19
	
	private static final String TXCODE_REGISTER_PERSON = "3001";//个人开户3001
	
	private static final String TXCODE_REGISTER_ENTERPRISE = "3002";//企业开户3002
	
	private static final String TXCODE_SEND_CODE = "3101";//发送受托签署电子合同验证码3101
	
	private static final String TXCODE_CHECK_TOKEN = "3102";//确认受托签署电子合同验证码3102
	
	private static final String TXCODE_CREATE_CONTRACT = "3201";//创建合同3201
	
	private static final String TXCODE_BATCHCRATE_CONTRACT = "3202";//批量合同创建3202
	
	private static final String TXCODE_QUERY_CONTRACT = "3210";//合同查询3210
	
	private static final String TXCODE_BATCHQUERY_CONTRACT = "3211";//批量创建合同结果查询3211
	
	private static final String CARD_TYPE_PERSON = "0";//居民身份证
	private static final String CARD_TYPE_ENTERPRISE = "7";//组织机构代码证
	
	@Override
	public void prepare() {
		LOGGER.debug("不需要prepare");
	}

	/**
	 * 开户
	 */
	@Override
	public String register(Map<String, Object> map) {
		String type = (String)map.get("type");//平台用户类型:个人，企业
		String resultStr;
		if(UserCache.USER_NATURE_PERSON.equals(type)){
			LOGGER.debug("******CFCA个人用户开户****");
			resultStr = addPerson(map);
		}else{
			LOGGER.debug("******CFCA企业用户开户****");
			resultStr = addOrganize(map);
		}
		
		return resultStr;
	}

	@Override
	public String createSeal(Map<String, Object> map) {
		LOGGER.debug("不需要createSeal，开户成功后就在安心签中生成");
		return null;
	}

	@Override
	public String drawSeal(Map<String, Object> map) {
		LOGGER.debug("不需要drawSeal，开户成功后就在安心签中生成");
		return null;
	}

	@Override
	public void execute(Map<String, Object> map) {
		String contractNo = uploadContract(map);//上传合同
		map.put("contractNo", contractNo);
		downloadContract(map);
	}
	
	HttpConnector init(){
	    HttpConnector httpConnector = new HttpConnector();
	    httpConnector.init();
	    return httpConnector;
	}
	
	HeadVO getHead(){
		HeadVO headVO = new HeadVO();
		headVO.setTxTime(DateUtils.getDateTime(DateUtils.DATEFORMAT_STR_011));
		return headVO;
	}
	
	String getSignature(String req){
        LOGGER.info("请求参数：{}",req);
		String res = null;
		try {
			res = SecurityUtil.p7SignMessageDetach(HttpConnector.JKS_PATH, HttpConnector.JKS_PWD, HttpConnector.ALIAS, req);
		} catch (PKIException e) {
			LOGGER.warn("加密错误：{},{}",e.getErrCode(),e.getErrDesc(), e);
		}
		LOGGER.info("加密后的参数：{}",res);
		return res;
	}
	
	/**
	 * 
	 * 个人开户3001
	 * @author jxx
	 * @date 2016年7月28日
	 * @param map
	 * @return
	 */
	private String addPerson(Map<String,Object> map){
		HttpConnector httpConnector = init();
		String name = (String)map.get("name");
        String idNo = (String)map.get("idno");
        String mobile = (String)map.get("mobile");
        
        Tx3001ReqVO tx3001ReqVO = new Tx3001ReqVO();
        PersonVO person = new PersonVO();
        person.setPersonName(name);
        person.setIdentTypeCode(CARD_TYPE_PERSON);
        person.setIdentNo(idNo);
        person.setMobilePhone(mobile);
        person.setAuthenticationMode("公安部");

        tx3001ReqVO.setHead(getHead());
        tx3001ReqVO.setPerson(person);

        String req = JsonMapper.toJsonString(tx3001ReqVO);
        
        String res = httpConnector.post("platId/" + platId + "/txCode/" + TXCODE_REGISTER_PERSON + "/transaction", req, getSignature(req));
        LOGGER.info("返回参数：{}",res);
        
        Tx3001ResVO tx3001ResVO = JsonMapper.fromJsonString(res,Tx3001ResVO.class);
        person = tx3001ResVO.getPerson();
        return person.getUserId();
        
    }
	
	/**
	 * 
	 * 企业开户3002
	 * @author jxx
	 * @date 2016年7月28日
	 * @param map
	 * @return
	 */
    private String addOrganize(Map<String,Object> map) {
    	HttpConnector httpConnector = init();
        String name = (String)map.get("name");
        String mobile = (String)map.get("mobile");
        String organCode = (String)map.get("organCode");
        String legalName = (String)map.get("legalName");
        String legalIdNo = (String)map.get("legalIdNo");
        
        
        Tx3002ReqVO tx3002ReqVO = new Tx3002ReqVO();

        EnterpriseVO enterprise = new EnterpriseVO();
        enterprise.setEnterpriseName(name);
        enterprise.setIdentTypeCode(CARD_TYPE_ENTERPRISE);//7组织机构代码证8企业营业执照
        enterprise.setIdentNo(organCode);
        enterprise.setMobilePhone(mobile);
        enterprise.setAuthenticationMode("公安部");

        EnterpriseTransactorVO enterpriseTransactor = new EnterpriseTransactorVO();
        enterpriseTransactor.setTransactorName(legalName);
        enterpriseTransactor.setIdentTypeCode(CARD_TYPE_PERSON);//0居民身份证
        enterpriseTransactor.setIdentNo(legalIdNo);
        enterpriseTransactor.setAddress("beijing");

        tx3002ReqVO.setHead(getHead());
        tx3002ReqVO.setEnterprise(enterprise);
        tx3002ReqVO.setEnterpriseTransactor(enterpriseTransactor);

        String req = JsonMapper.toJsonString(tx3002ReqVO);

        String res = httpConnector.post("platId/" + platId + "/txCode/" + TXCODE_REGISTER_ENTERPRISE + "/transaction", req, getSignature(req));
        LOGGER.info("返回参数：{}",res);
        
        Tx3002ResVO tx3002ResVO =  JsonMapper.fromJsonString(res, Tx3002ResVO.class);
        enterprise = tx3002ResVO.getEnterprise();
        return enterprise.getUserId();
    }
    
    /**
     * 
     * 发送受托签署电子合同验证码3101
     * 借款人在确认签署时发送
     * 投资人在确认签署时发送
     * 针对每个项目
     * @author jxx
     * @date 2016年7月28日
     * @param map
     * @return
     */
    @Override
    public String tokenSendCode(Map<String,Object> map) {
    	HttpConnector httpConnector = init();

        String userId = (String)map.get("accountId");
        String projectCode = (String)map.get("projectNo");
        
        Tx3101ReqVO tx3101ReqVO = new Tx3101ReqVO();

        ProxySignVO proxySignVO = new ProxySignVO();
        proxySignVO.setUserId(userId);
        proxySignVO.setProjectCode(projectCode);

        tx3101ReqVO.setHead(getHead());
        tx3101ReqVO.setProxySign(proxySignVO);

        String req = JsonMapper.toJsonString(tx3101ReqVO);

        String res = httpConnector.post("platId/" + platId + "/txCode/" + TXCODE_SEND_CODE + "/transaction", req, getSignature(req));
        LOGGER.info("返回参数：{}",res);
        
        return res;
    }
    
    /**
     * 
     * 确认受托签署电子合同验证码3102
     * 借款人，投资人根据收到的短信在页面上确认调用
     * @author jxx
     * @date 2016年7月28日
     * @param map
     * @return
     */
    @Override
    public String tokenCheckCode(Map<String,Object> map) {
    	HttpConnector httpConnector = init();
        
        Tx3102ReqVO tx3102ReqVO = new Tx3102ReqVO();
        String userId = (String)map.get("accountId");
        String projectCode = (String)map.get("projectNo");
        String checkCode = (String)map.get("checkCode");
        
        ProxySignVO proxySignVO = new ProxySignVO();
        proxySignVO.setUserId(userId);
        proxySignVO.setProjectCode(projectCode);
        proxySignVO.setCheckCode(checkCode);

        tx3102ReqVO.setHead(getHead());
        tx3102ReqVO.setProxySign(proxySignVO);

        String req = JsonMapper.toJsonString(tx3102ReqVO);

        String res = httpConnector.post("platId/" + platId + "/txCode/" + TXCODE_CHECK_TOKEN + "/transaction", req, getSignature(req));
        LOGGER.info("返回参数：{}",res);
        
        return res;
    }
    
    /**
     * 
     * 创建合同3201
     * 企业在安心签网站上先上传后好pdf模板，个人理解为pdf是带标签的，map中的字段是对应标签名
     * @author jxx
     * @date 2016年7月28日
     * @param map
     * @return
     */
    public String createContract(Map<String,Object> map){
    	HttpConnector httpConnector = init();

         Tx3201ReqVO tx3201ReqVO = new Tx3201ReqVO();

         String accountId = (String)map.get("accountId");
         String projectCode = (String)map.get("projectNo");
         String srcPdfFile = (String)map.get("srcPdfFile");
         String fileName = (String)map.get("fileName");

         Map<String, String> fieldMap = new HashMap<String, String>();
         CreateContractVO createContract = new CreateContractVO();

         createContract.setIsSign(1);
         createContract.setTemplateId("买卖合同");//在安心签中创建返回的
         fieldMap.put("Text1", accountId);
         fieldMap.put("Text2", srcPdfFile);
         fieldMap.put("Text3", fileName);

         createContract.setInvestmentInfo(fieldMap);

         SignInfoVO[] signInfos = new SignInfoVO[1];
         SignInfoVO signInfoVO0 = new SignInfoVO();
         signInfoVO0.setUserId(accountId);
         signInfoVO0.setIsProxySign(1);
         signInfoVO0.setLocation("成都");
         signInfoVO0.setProjectCode(projectCode);
         signInfoVO0.setSignLocation("Signature4");
         signInfoVO0.setAuthorizationTime(DateUtils.getDateTime(DateUtils.DATEFORMAT_STR_011));
         signInfos[0] = signInfoVO0;

         createContract.setSignInfos(signInfos);

         tx3201ReqVO.setHead(getHead());
         tx3201ReqVO.setCreateContract(createContract);

         String req = JsonMapper.toJsonString(tx3201ReqVO);

         
         String res = httpConnector.post("platId/" + platId + "/txCode/" + TXCODE_CREATE_CONTRACT + "/transaction", req, getSignature(req));
         LOGGER.info("返回参数：{}",res);
         
         Tx3201ResVO tx3201ResVO = JsonMapper.fromJsonString(res,Tx3201ResVO.class);
         ContractVO contractVO = tx3201ResVO.getContract();
         return contractVO.getContractNo();
    }

    /**
     * 
     * 批量合同创建3202
     * 多个合同一起模板创建
     * 未验证
     * @author jxx
     * @date 2016年7月28日
     * @param map
     * @return
     */
    public String batchCreateContract(Map<String,Object> map){
    	HttpConnector httpConnector = init();

        Tx3202ReqVO tx3202ReqVO = new Tx3202ReqVO();

        List<CreateContractVO> createContractlist = new ArrayList<>();

        CreateContractVO createContract = new CreateContractVO();
        createContract.setTemplateId("1");

        Map<String, String> fieldMap = new HashMap<>();
        fieldMap.put("text1", "2016");
        fieldMap.put("text2", "3");
        fieldMap.put("text3", "16");
        fieldMap.put("text4", "孙一");
        fieldMap.put("text5", "222321199112050001");
        fieldMap.put("text6", "成都");
        fieldMap.put("text7", "壹万");
        fieldMap.put("text8", "壹万元整");
        fieldMap.put("text9", "10000");
        fieldMap.put("text10", "壹万元整");
        fieldMap.put("text11", "10000");
        fieldMap.put("text12", "壹万元整");
        fieldMap.put("text13", "10000");
        fieldMap.put("text14", "叁万元整");
        fieldMap.put("text15", "30000");
        fieldMap.put("text16", "10000");
        fieldMap.put("text17", "10000");
        fieldMap.put("text18", "10000");
        fieldMap.put("text19", "孙一");
        fieldMap.put("text20", "成都支行");
        fieldMap.put("text21", "001");
        createContract.setInvestmentInfo(fieldMap);

        SignInfoVO[] signInfos = new SignInfoVO[1];
        SignInfoVO signInfoVO0 = new SignInfoVO();
        signInfoVO0.setUserId("6327987D3B854A3A9F4A198426084F02");
        signInfoVO0.setIsProxySign(1);
        signInfoVO0.setLocation("成都");
        signInfoVO0.setProjectCode("002");
        signInfoVO0.setSignLocation("Signature1");
        signInfoVO0.setAuthorizationTime("20160214171200");
        signInfos[0] = signInfoVO0;
        createContract.setSignInfos(signInfos);

        CreateContractVO createContract2 = new CreateContractVO();
        createContract2.setTemplateId("1");
        createContract2.setInvestmentInfo(fieldMap);
        createContract2.setSignInfos(signInfos);

        createContractlist.add(createContract);
        createContractlist.add(createContract2);

        tx3202ReqVO.setHead(getHead());
        tx3202ReqVO.setBatchNo("B112");
        tx3202ReqVO.setCreateContracts(createContractlist.toArray(new CreateContractVO[0]));

        String req = JsonMapper.toJsonString(tx3202ReqVO);
        
        String res = httpConnector.post("platId/" + platId + "/txCode/" + TXCODE_BATCHCRATE_CONTRACT + "/transaction", req, getSignature(req));
        LOGGER.info("返回参数：{}",res);
        
        return res;
   }
    
    /**
     * 
     * 上传合同签署3203
     * @author jxx
     * @date 2016年7月28日
     * @param map
     * @return
     */
    private String uploadContract(Map<String,Object> map){/*
    	HttpConnector httpConnector = init();
        Tx3203ReqVO tx3203ReqVO = new Tx3203ReqVO();
        String accountId = (String)map.get("accountId");
        String projectCode = (String)map.get("projectNo");
        String srcPdfFile = (String)map.get("srcPdfFile");
        String fileName = (String)map.get("fileName");

        UploadContractVO uploadContract = new UploadContractVO();

        UploadSignInfoVO[] signInfos = new UploadSignInfoVO[1];
        UploadSignInfoVO signInfoVO0 = new UploadSignInfoVO();
        signInfoVO0.setUserId(accountId);
        signInfoVO0.setIsProxySign(1);
        signInfoVO0.setLocation("成都");

        SignLocationVO[] signLocations0 = new SignLocationVO[1];
        SignLocationVO signLocation0 = new SignLocationVO();
        signLocation0.setSignOnPage("1");
        signLocation0.setSignLocationLBX("85");
        signLocation0.setSignLocationLBY("550");
        signLocation0.setSignLocationRUX("140");
        signLocation0.setSignLocationRUY("575");
        signLocations0[0] = signLocation0;
        signInfoVO0.setSignLocations(signLocations0);
        signInfoVO0.setProjectCode(projectCode);
        signInfoVO0.setAuthorizationTime(DateUtils.getDateTime(DateUtils.DATEFORMAT_STR_011));
        signInfos[0] = signInfoVO0;

//        UploadSignInfoVO signInfoVO1 = new UploadSignInfoVO();
//        signInfoVO1.setUserId("2ED850C06CFB3AADE050007F010003F0");
//        signInfoVO1.setIsProxySign(1);
//        signInfoVO1.setLocation("成都");
//        SignLocationVO[] signLocations1 = new SignLocationVO[1];
//        SignLocationVO signLocation1 = new SignLocationVO();
//        signLocation1.setSignOnPage("2");
//        signLocation1.setSignLocationLBX("151");
//        signLocation1.setSignLocationLBY("476");
//        signLocation1.setSignLocationRUX("240");
//        signLocation1.setSignLocationRUY("500");
//        signLocations1[0] = signLocation1;
//        signInfoVO1.setSignLocations(signLocations1);
//        signInfoVO1.setProjectCode("541840");
//        signInfoVO1.setAuthorizationTime("20160214171200");
//        signInfos[1] = signInfoVO1;

//        UploadSignInfoVO signInfoVO2 = new UploadSignInfoVO();
//        signInfoVO2.setUserId("2E267C3C9EC088F0E050007F0100780B");
//        signInfoVO2.setIsProxySign(1);
//        signInfoVO2.setLocation("成都");
//        SignLocationVO[] signLocations2 = new SignLocationVO[1];
//        SignLocationVO signLocation2 = new SignLocationVO();
//        signLocation2.setSignOnPage("2");
//        signLocation2.setSignLocationLBX("230");
//        signLocation2.setSignLocationLBY("105");
//        signLocation2.setSignLocationRUX("330");
//        signLocation2.setSignLocationRUY("130");
//        signLocations2[0] = signLocation2;
//        signInfoVO2.setSignLocations(signLocations2);
//        signInfoVO2.setProjectCode("002");
//        signInfoVO2.setAuthorizationTime("20160214171200");
//        signInfos[2] = signInfoVO2;

        uploadContract.setSignInfos(signInfos);
        uploadContract.setContractTypeCode(BUY_SELL_CONTRACT);
        uploadContract.setContractName(fileName);

        uploadContract.setIsSign(1);
        SignLocationVO[] signLocationsPlat = new SignLocationVO[2];
        SignLocationVO signLocationPlat = new SignLocationVO();
        signLocationPlat.setSignOnPage("1");
        signLocationPlat.setSignLocationLBX("240");
        signLocationPlat.setSignLocationLBY("430");
        signLocationPlat.setSignLocationRUX("340");
        signLocationPlat.setSignLocationRUY("530");
        signLocationsPlat[0] = signLocationPlat;

        SignLocationVO signLocationPlat2 = new SignLocationVO();
        signLocationPlat2.setSignOnPage("2");
        signLocationPlat2.setSignLocationLBX("240");
        signLocationPlat2.setSignLocationLBY("430");
        signLocationPlat2.setSignLocationRUX("340");
        signLocationPlat2.setSignLocationRUY("530");
        signLocationsPlat[1] = signLocationPlat2;
        uploadContract.setSignLocations(signLocationsPlat);

        tx3203ReqVO.setHead(getHead());
        tx3203ReqVO.setUploadContract(uploadContract);

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = JsonMapper.toJsonString(tx3203ReqVO);
        
        File file = new File(srcPdfFile);

        String res = httpConnector.post("platId/" + platId + "/txCode/" + TXCODE_UPLOAD_CONTRACT + "/transaction", req, getSignature(req), file);
        LOGGER.info("返回参数：{}",res);
    	

        Tx3203ResVO tx3203ResVO = jsonObjectMapper.readValue(res,Tx3203ResVO.class);
        ContractVO contract = tx3203ResVO.getContract();
        
    	return contract.getContractNo();
    */
    	return null;
    }
    
    /**
     * 
     * 合同查询3210
     * @author jxx
     * @date 2016年7月28日
     * @param map
     * @return
     */
    public String queryContract(Map<String,Object> map){
    	HttpConnector httpConnector = init();

        String contractNo = (String)map.get("contractNo");
    	
    	Tx3210ReqVO tx3210ReqVO = new Tx3210ReqVO();
	
	    ContractVO contract = new ContractVO();
	    contract.setContractNo(contractNo);
	
	    tx3210ReqVO.setHead(getHead());
	    tx3210ReqVO.setContract(contract);
	
	    String req = JsonMapper.toJsonString(tx3210ReqVO);
	    
	    String res = httpConnector.post("platId/" + platId + "/txCode/" + TXCODE_QUERY_CONTRACT + "/transaction", req, getSignature(req));
        LOGGER.info("返回参数：{}",res);
        return res;
   }
    
    /**
     * 
     * 批量创建合同结果查询3211
     * @author jxx
     * @date 2016年7月28日
     * @param map
     * @return
     */
    public String batchQueryContract(Map<String,Object> map){
    	HttpConnector httpConnector = init();

        String batchNo = (String)map.get("batchNo");
    	
        Tx3211ReqVO tx3211ReqVO = new Tx3211ReqVO();

        tx3211ReqVO.setHead(getHead());
        tx3211ReqVO.setBatchNo(batchNo);

        String req = JsonMapper.toJsonString(tx3211ReqVO);
        
	    String res = httpConnector.post("platId/" + platId + "/txCode/" + TXCODE_BATCHQUERY_CONTRACT + "/transaction", req, getSignature(req));
        LOGGER.info("返回参数：{}",res);
        	    
        return res;
   }
    
    /**
     * 
     * 合同下载接口
     * @author jxx
     * @date 2016年7月28日
     * @param map
     * @return
     */
    public String downloadContract(Map<String,Object> map){
    	HttpConnector httpConnector = init();
    	
        String contractNo = (String)map.get("contractNo");
        String dstPdfFile = (String)map.get("dstPdfFile");

        byte[] fileBtye = httpConnector.getFile("platId/" + platId + "/contractNo/" + contractNo + "/downloading");

        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            file = new File(dstPdfFile);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(fileBtye);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage(),e);
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    LOGGER.warn(e.getMessage(),e);
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    LOGGER.warn(e.getMessage(),e);
                }
            }
        }
        String result = file == null? null:file.getAbsolutePath();
        LOGGER.info("返回参数：{}",result);
        return result;
   }
}
