package com.rd.ifaes.core.tpp.model.cbhb.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.core.tpp.model.cbhb.CbhbBaseModel;

/**
 * 
 * 2.1	存量用户注册(ftp文件&后台方式)
 * @version 3.0
 * @author ZhangBiao
 * @date 2017年2月21日
 */
public class FileExistUserRegisterDetailModel extends CbhbBaseModel{
	
	private static final long serialVersionUID = -3639191443537894840L;
	/**
	 * LOGGER
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(FileExistUserRegisterDetailModel.class);
	
	/**
	 * 证件类型	String(2)
	 */
	private String identType;
	
	/**
	 * 证件号码	String(50)
	 */
	private String identNo;
	
	/**
	 * 姓名	String(50)
	 */
	private String usrName;
	
	/**
	 * 手机号	String(11)
	 */
	private String mobileNo;
	
	/**
	 * 开户银行代号	String(8)
	 */
	private String openBankId;
	
	/**
	 * 开户银行账号	String(40)
	 */
	private String openAcctId;
	
	/**
	 * 请求参数
	 */
	private String[] requestParamNames = new String[] {"IdentType","IdentNo","UsrName","MobileNo","OpenBankId",
		"OpenAcctId"};

	/**
	 * 构造器
	 */
	public FileExistUserRegisterDetailModel(){
		super();
	}
	
	/**
	 * 请求参数校验
	 */
	@Override
	public void requestColumnValid() {
		super.requestColumnValid();
		LOGGER.info("existUserRegister valid...");
	}
	
	public String getIdentType() {
		return identType;
	}

	public void setIdentType(String identType) {
		this.identType = identType;
	}

	public String getIdentNo() {
		return identNo;
	}

	public void setIdentNo(String identNo) {
		this.identNo = identNo;
	}

	public String getUsrName() {
		return usrName;
	}

	public void setUsrName(String usrName) {
		this.usrName = usrName;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getOpenBankId() {
		return openBankId;
	}

	public void setOpenBankId(String openBankId) {
		this.openBankId = openBankId;
	}

	public String getOpenAcctId() {
		return openAcctId;
	}

	public void setOpenAcctId(String openAcctId) {
		this.openAcctId = openAcctId;
	}

	public String[] getRequestParamNames() {
		return requestParamNames;
	}

	public void setRequestParamNames(String[] requestParamNames) {
		this.requestParamNames = requestParamNames;
	}

}
