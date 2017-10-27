package com.rd.ifaes.core.core.sign.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.sign.SignPortal;
import com.rd.ifaes.core.core.sign.constant.SignConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.user.domain.UserCache;
import com.timevale.esign.sdk.tech.bean.OrganizeBean;
import com.timevale.esign.sdk.tech.bean.PersonBean;
import com.timevale.esign.sdk.tech.bean.PosBean;
import com.timevale.esign.sdk.tech.bean.result.AddAccountResult;
import com.timevale.esign.sdk.tech.bean.result.AddSealResult;
import com.timevale.esign.sdk.tech.bean.result.LoginResult;
import com.timevale.esign.sdk.tech.bean.result.Result;
import com.timevale.esign.sdk.tech.bean.seal.OrganizeTemplateType;
import com.timevale.esign.sdk.tech.bean.seal.PersonTemplateType;
import com.timevale.esign.sdk.tech.bean.seal.SealColor;
import com.timevale.esign.sdk.tech.impl.constants.OrganRegType;
import com.timevale.esign.sdk.tech.impl.constants.SignType;
import com.timevale.esign.sdk.tech.service.AccountService;
import com.timevale.esign.sdk.tech.service.EsignsdkService;
import com.timevale.esign.sdk.tech.service.SealService;
import com.timevale.esign.sdk.tech.service.SignService;
import com.timevale.esign.sdk.tech.service.factory.AccountServiceFactory;
import com.timevale.esign.sdk.tech.service.factory.EsignsdkServiceFactory;
import com.timevale.esign.sdk.tech.service.factory.SealServiceFactory;
import com.timevale.esign.sdk.tech.service.factory.SignServiceFactory;

/**
 * 
 * @ClassName: ESignServiceImpl 
 * @Description: E签宝实现
 * @author jxx 
 * @date 2016年7月25日 下午2:51:57 
 *
 */
public class ESignServiceImpl implements SignPortal{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ESignServiceImpl.class);
	
	protected EsignsdkService esignService = EsignsdkServiceFactory.instance();
	
	protected AccountService accountService = AccountServiceFactory.instance() ;
	
	protected SignService signService = SignServiceFactory.instance();
	
	protected SealService sealService = SealServiceFactory.instance();
	
	public ESignServiceImpl(){
		//prepare();
	}
	
	/**
	 * 初始化设置
	 */
	@Override
	public void prepare() {
		LOGGER.info("E签宝初始化start");
		String esignId = ConfigUtils.getValue("sign_Id");//1111563517
		String esignSecret = ConfigUtils.getValue("sign_Secret");//95439b0863c241c63a861b87d1e647b7
		esignService.init(esignId, esignSecret);
	}
	
	/**
	 * 注册
	 */
	@Override
	public String register(Map<String,Object> map) {
		String type = (String)map.get("type");//平台用户类型:个人，企业
		AddAccountResult result = null;
		if(UserCache.USER_NATURE_PERSON.equals(type)){
			LOGGER.debug("******E签宝个人用户开户****");
			result = addPerson(map);
		}else{
			LOGGER.debug("******E签宝企业用户开户****");
			result = addOrganize(map);
		}
		if(result.getErrCode()!=0){
			LOGGER.info(JSON.toJSONString(map));
        	LOGGER.error("创建账户错误，code:{},msg:{},accountId:{}" ,result.getErrCode() ,result.getMsg(),result.getAccountId());
			return "";
		}
		return result.getAccountId();
	}

	/**
	 * 获取签章
	 */
	@Override
	public String createSeal(Map<String, Object> map) {
    	String userNature = map.get("type").toString();

        AddSealResult result = null;
    	if (UserCache.USER_NATURE_PERSON.equals(userNature)) {
			LOGGER.debug("******E签宝个人印章****");
    		result = createSealPersonal(map);
        } else {
			LOGGER.debug("******E签宝企业印章****");
        	result = createSealOrganize(map);
        }
    	if (0 != result.getErrCode()) {
        	LOGGER.info(JSON.toJSONString(map));
        	LOGGER.error("创建所属印章数据错误，code:{},msg:{}" ,result.getErrCode(),result.getMsg(),result.getSealData());
            return "";
        }
    	return result.getSealData();
	}
	
	/**
	 * 画图获取签章
	 */
	@Override
	public String drawSeal(Map<String, Object> map) {
		LOGGER.debug("******E签宝手绘印章生成服务****");
		AddSealResult result = addDrawEsign(map);
    	if (0 != result.getErrCode()) {
        	LOGGER.info(JSON.toJSONString(map));
        	LOGGER.error("创建所属印章数据错误，code:{},msg:{}" ,result.getErrCode(),result.getMsg(),result.getSealData());
            return "";
        }
    	return result.getSealData();
	}

	/**
	 * 签署
	 */
	@Override
	public void execute(Map<String,Object> map) {
		int type = Integer.valueOf(map.get("type").toString());//0平台自身/1平台用户
		Result result = null;
		if(SignConstant.TYPE_SIGN_PUSER == type){
			LOGGER.debug("******E签宝平台用户信息摘要****");
			result = userPDF(map);
		}else{
			LOGGER.debug("******E签宝平台自身信息摘要****");
			result = platformPDF(map);
		}
		if (0 != result.getErrCode()) {
        	LOGGER.info(JSON.toJSONString(map));
        	LOGGER.error("摘要签署错误，code:{},msg:{}" ,result.getErrCode() ,result.getMsg());
         }else{
 			LOGGER.debug("******E签宝信息摘成功****");
         }
		
	}
	
	/**
	 * 登录
	 */
	 private String getDevId(){
		 LoginResult result = esignService.login();
		 LOGGER.info("E签宝登录结果:{}",result.getAccountId());
		 return result.getAccountId();//平台在E签宝标识
	}
	
	/**********************************注册start**************************/
	/**
	 * 添加个人用户
	 * @param map
	 * @param devId
	 * @return
	 */
	private AddAccountResult addPerson(Map<String,Object> map) {
		String devId = getDevId();//平台标识
		String name = (String)map.get("name");
        String idNo = (String)map.get("idno");
        String mobile = (String)map.get("mobile");
        PersonBean psn = new PersonBean();
        psn.setName(name).setIdNo(idNo).setMobile(mobile);
        return accountService.addAccount(devId, psn);
        
    }
	
	/**
	 * 添加企业用户
	 * @param map
	 * @param devId
	 */
    private AddAccountResult addOrganize(Map<String,Object> map) {
		String devId = getDevId();//平台标识
        String name = (String)map.get("name");
        String mobile = (String)map.get("mobile");
        OrganRegType regType = (OrganRegType)map.get("regType");
        String organCode = (String)map.get("organCode");
        String legalName = (String)map.get("legalName");
        String legalIdNo = (String)map.get("legalIdNo");
        
        //可选
        String regCode = (String)map.get("regCode");
        OrganizeBean org = new OrganizeBean();
        
        org.setUserType(Integer.parseInt(UserCache.USER_NATURE_COMPANY)).setName(name).setMobile(mobile).setOrganCode(organCode)
        .setRegCode(regCode).setLegalName(legalName).setLegalIdNo(legalIdNo).setRegType(regType);
        
        return accountService.addAccount(devId, org);
    }
    /**********************************注册end****************************/
    
    /**********************************摘要签署start****************************/
    private Result platformPDF(Map<String,Object> map){
    	String devId = getDevId();//平台标识
    	String srcPdfFile = map.get("srcPdfFile").toString();
    	String dstPdfFile = map.get("dstPdfFile").toString(); 
    	PosBean signPos = (PosBean)map.get("signPos");
		int sealId = Integer.valueOf(map.get("sealId").toString());
		SignType signType = (SignType)map.get("signType"); 
		String fileName = map.get("fileName").toString();
    	return signService.localSignPDF(devId, srcPdfFile, dstPdfFile, signPos,sealId, signType, fileName);
    }
    
    private Result userPDF(Map<String,Object> map){
    	String devId = getDevId();//平台标识
    	String accountId = StringUtils.isNull(map.get("accountId"));
    	String sealData = StringUtils.isNull(map.get("sealData"));
    	String srcPdfFile = StringUtils.isNull(map.get("srcPdfFile"));
    	String dstPdfFile = StringUtils.isNull(map.get("dstPdfFile"));
    	PosBean signPos = (PosBean)map.get("signPos");
    	SignType signType = (SignType)map.get("signType");
    	String fileName = StringUtils.isNull(map.get("fileName"));
    	return signService.localSignPDF(devId, accountId, sealData, srcPdfFile,dstPdfFile, signPos, signType, fileName);
    }
    
    /**********************************摘要签署end******************************/

    

    /**********************************获取签章start******************************/
    /**
     * 
     * @Title: createSealPersonal 
     * @Description: 创建所属平台个人用户印章数据
     * @param @param map
     * @param @return 
     * @return String
     * @throws
     */
    private AddSealResult createSealPersonal(Map<String, Object> map) {
    	String devId = getDevId();
        String accountId = (String)map.get("accountId");
        PersonTemplateType templateType = (PersonTemplateType)map.get("templateType");
        if(templateType == null){
        	templateType = PersonTemplateType.SQUARE;
        }
        
        SealColor color = (SealColor)map.get("color");
        if(color == null){
        	color = SealColor.RED;
        }

        return sealService.addTemplateSeal(devId, accountId, templateType,color);
    }
    
   
    
    /**
     * 
     * @Title: createSealOrganize 
     * @Description: 创建所属平台企业用户印章数据
     * @param @param map
     * @param @return 
     * @return String
     * @throws
     */
    private AddSealResult createSealOrganize(Map<String, Object> map) {
    	//(STAR--标准公章) (OVAL--椭圆形印章)
    	String devId = getDevId();
        String accountId = (String)map.get("accountId");
        OrganizeTemplateType templateType = (OrganizeTemplateType)map.get("templateType");
        if(templateType == null){
        	templateType = OrganizeTemplateType.STAR;
        }
        SealColor color = (SealColor)map.get("color");
        if(color == null){
        	color = SealColor.RED;
        }

        String hText = (String)map.get("hText");
        if(hText == null){
        	hText = "123";
        }
        String qText = (String)map.get("qText");
        if(qText == null){
        	qText = "456";
        }

        return sealService.addTemplateSeal(devId, accountId, templateType,color, hText, qText);
    }
    
    /**
     * 
     * @Title: addDrawEsign 
     * @Description: 手绘印章生成服务
     * @param @param map
     * @param @return 
     * @return AddSealResult
     * @throws
     */
    private AddSealResult addDrawEsign(Map<String, Object> map) {
		LOGGER.info("E签宝====手绘印章创建====== ");
		// 获取账号
		String devId = getDevId();
        String accountId = (String)map.get("accountId");
        String imgB64 = (String)map.get("imgB64");
		SealService sealService = SealServiceFactory.instance();
		SealColor color = (SealColor)map.get("color");
        if(color == null){
        	color = SealColor.RED;
        }
		AddSealResult result = sealService.addFileSeal(devId, accountId,
				imgB64, color);
		LOGGER.info("创建E签宝手绘印章：{}" , result.getMsg());
		return result;
	}
    /**********************************获取签章end******************************/

	@Override
	public String tokenSendCode(Map<String, Object> map) {
		return null;
	}

	@Override
	public String tokenCheckCode(Map<String, Object> map) {
		return null;
	}
}
