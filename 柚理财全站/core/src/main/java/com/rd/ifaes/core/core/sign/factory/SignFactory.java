package com.rd.ifaes.core.core.sign.factory;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.sign.SignPortal;
import com.rd.ifaes.core.core.sign.constant.SignConstant;
import com.rd.ifaes.core.core.sign.impl.CFCASignServiceImpl;
import com.rd.ifaes.core.core.sign.impl.ESignServiceImpl;
import com.rd.ifaes.core.core.util.ConfigUtils;

/**
 * 
 * @ClassName: SignFactory 
 * @Description: 电子签名工厂类
 * @author jxx 
 * @date 2016年7月15日 下午5:44:25 
 *
 */
public class SignFactory implements BaseFactory{
	private static final Logger LOGGER = LoggerFactory.getLogger(SignFactory.class);
	
	/**
	 * 初始化
	 */
	@Override
	public void prepare() {
 		String type = getConfig();
		if(StringUtils.isBlank(type)){
			LOGGER.error("第三方协议不存在或者未开启！");
		}else if(SignConstant.E_SIGN.equals(type)){
			SignPortal eSignService = new ESignServiceImpl();
			eSignService.prepare();
			LOGGER.info("{}协议已启动！", type);
		}else if(SignConstant.CFCA_SIGN.equals(type)){
			LOGGER.info("prepare,CFCA没有对应的功能！");
		}else{
			LOGGER.error("第三方协议不存在或者未开启！{}",type);
		}
	}
	
	/**
	 * 注册
	 */
	@Override
	public String register(Map<String, Object> map) {
		String type = getConfig();
		if(StringUtils.isBlank(type)){
			LOGGER.error("第三方协议不存在或者未开启！");
		}else if(SignConstant.E_SIGN.equals(type)){
			SignPortal eSignService = new ESignServiceImpl();
			return eSignService.register(map);
		}else if(SignConstant.CFCA_SIGN.equals(type)){
			SignPortal eSignService = new CFCASignServiceImpl();
			return eSignService.register(map);
		}else{
			LOGGER.error("第三方协议不存在或者未开启！{}",type);
		}
		return null;
	}

	/**
	 * 签署
	 */
	@Override
	public void execute(Map<String, Object> map) {
		String type = getConfig();
		if(StringUtils.isBlank(type)){
			LOGGER.error("第三方协议不存在或者未开启！");
		}else if(SignConstant.E_SIGN.equals(type)){
			SignPortal eSignService = new ESignServiceImpl();
			eSignService.execute(map);
		}else if(SignConstant.CFCA_SIGN.equals(type)){
			SignPortal eSignService = new CFCASignServiceImpl();
			eSignService.execute(map);
		}else{
			LOGGER.error("第三方协议不存在或者未开启！{}",type);
		}
	}
	
	/**
	 * 获取通道配置
	 * @return
	 */
	private String getConfig(){
		String signType = ConfigUtils.getValue("sign_type");
		if(StringUtils.isBlank(signType)){
			LOGGER.error("第三方协议不存在或者未开启！");
			return null;
		}
		return signType;
	}

	/**
	 * 根据模板获取签章
	 */
	@Override
	public String createSeal(final Map<String, Object> map) {
		String type = getConfig();
		String sealData = null;
		if(StringUtils.isBlank(type)){
			LOGGER.error("第三方协议不存在或者未开启！");
		}else if(SignConstant.E_SIGN.equals(type)){
			SignPortal eSignService = new ESignServiceImpl();
			sealData = eSignService.createSeal(map);
		}else if(SignConstant.CFCA_SIGN.equals(type)){
			LOGGER.info("createSeal,CFCA没有对应的功能");
		}else{
			LOGGER.error("第三方协议不存在或者未开启！{}",type);
		}
		return sealData;
	}
	
	/**
	 * 根据图片获取签章
	 */
	@Override
	public String drawSeal(final Map<String, Object> map) {
		String type = getConfig();
		String sealData = null;
		if(StringUtils.isBlank(type)){
			LOGGER.error("第三方协议不存在或者未开启！");
		}else if(SignConstant.E_SIGN.equals(type)){
			SignPortal eSignService = new ESignServiceImpl();
			sealData = eSignService.drawSeal(map);
		}else if(SignConstant.CFCA_SIGN.equals(type)){
//			SignPortal eSignService = new CFCASignServiceImpl();
//			return eSignService.drawSeal(map);
			LOGGER.info("drawSeal,CFCA没有对应的功能");
		}else{
			LOGGER.error("第三方协议不存在或者未开启！{}",type);
		}
		return sealData;
	}

	@Override
	public String tokenSendCode(Map<String,Object> map) {
		String type = getConfig();
		String resultStr = null;
		if(StringUtils.isBlank(type)){
			LOGGER.error("第三方协议不存在或者未开启！");
		}else if(SignConstant.E_SIGN.equals(type)){
			LOGGER.info("tokenSendCode,e签宝没有对应的功能");
		}else if(SignConstant.CFCA_SIGN.equals(type)){
			SignPortal eSignService = new CFCASignServiceImpl();
			return eSignService.tokenSendCode(map);
		}else{
			LOGGER.error("第三方协议不存在或者未开启！{}",type);
		}
		return resultStr;
	}

	@Override
	public String tokenCheckCode(Map<String,Object> map) {
		String type = getConfig();
		String resultStr = null;
		if(StringUtils.isBlank(type)){
			LOGGER.error("第三方协议不存在或者未开启！");
		}else if(SignConstant.E_SIGN.equals(type)){
			LOGGER.info("tokenCheckCode,e签宝没有对应的功能");
		}else if(SignConstant.CFCA_SIGN.equals(type)){
			SignPortal eSignService = new CFCASignServiceImpl();
			return eSignService.tokenCheckCode(map);
		}else{
			LOGGER.error("第三方协议不存在或者未开启！{}",type);
		}
		return resultStr;
	}
}
