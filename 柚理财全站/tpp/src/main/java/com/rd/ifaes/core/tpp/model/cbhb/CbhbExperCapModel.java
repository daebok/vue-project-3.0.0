package com.rd.ifaes.core.tpp.model.cbhb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;

/**
 * 
 * 3.12.1	红包申请通知（后台方式）
 * @version 3.0
 * @author ZhangBiao
 * @date 2017年2月21日
 */
public class CbhbExperCapModel extends CbhbBaseModel{
	
	private static final long serialVersionUID = -3639191443537894840L;
	/**
	 * LOGGER
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CbhbExperCapModel.class);
	/**
	 * 文件名	String(256)
	 */
	private String fileName;
	
	/**
	 * 请求参数
	 */
	private String[] requestParamNames = new String[] {"char_set","partner_id","version_no","biz_type","sign_type",
			"MerBillNo","BgRetUrl","FileName","MerPriv","mac"};

	/**
	 * 响应参数
	 */
	private String[] responseParamNames = new String[] {"partner_id","version_no","biz_type","sign_type","MerBillNo",
			"RespCode","RespDesc","mac"};

	/**
	 * 构造器
	 */
	public CbhbExperCapModel(){
		super();
		this.setBizType(CbhbConstant.BIZ_TYPE_EXPER_CAP);
		this.setBgRetUrl(ConfigUtils.getValue("notify_url")+ "/cbhb/experCap/notify.html");
		this.setFileName(this.getFileNameOnByRule());
	}
	
	/**
	 * 请求参数校验
	 */
	@Override
	public void requestColumnValid() {
		super.requestColumnValid();
		LOGGER.info("CbhbExperCapModel valid...");
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String[] getRequestParamNames() {
		return requestParamNames;
	}

	public void setRequestParamNames(String[] requestParamNames) {
		this.requestParamNames = requestParamNames;
	}

	public String[] getResponseParamNames() {
		return responseParamNames;
	}

	public void setResponseParamNames(String[] responseParamNames) {
		this.responseParamNames = responseParamNames;
	}

}
