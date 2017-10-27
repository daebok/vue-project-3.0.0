package com.rd.ifaes.core.tpp.model.cbhb;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.util.StringUtils;

/**
 * 
 *  4.2	用户余额查询（后台方式）
 * @version 3.0
 * @author ZhangBiao
 * @date 2017年2月21日
 */
public class CbhbQueryBalanceModel extends CbhbBaseModel{
	/**
	 * LOGGER
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CbhbQueryBalanceModel.class);
	
	private static final long serialVersionUID = -3639191443537894840L;

	/**
	 * 账户存管平台客户号	String(16)
	 */
	private String plaCustId;
	
	/**
	 * 可用余额	String(16)
	 */
	private String avlBal;
	
	/**
	 * 账户余额	String(16)
	 */
	private String acctBal;
	
	/**
	 * 冻结余额	String(16)
	 */
	private String frzBal;
	
	/**
	 * 请求参数   后台方式提交 必须包含 mac 和 merchantCert
	 */
	private String[] requestParamNames = new String[] {"char_set","partner_id","version_no","biz_type","sign_type","PlaCustId","mac","merchantCert"};

	/**
	 * 响应参数  后台方式提交必须包含mac
	 */
	private String[] responseParamNames = new String[] {"partner_id","version_no","biz_type","sign_type","RespCode","RespDesc","AvlBal","AcctBal","FrzBal","mac"};
	
	/**
	 * 构造器
	 */
	public CbhbQueryBalanceModel(){
		super();
		this.setBizType(CbhbConstant.BIZ_TYPE_QUERY_BALANCE);
	}
	
	/**
	 * 回调响应
	 */
	@Override
	public void response(Map<String, String> map) {
		super.response(map);
		LOGGER.info("queryBalance....");
		this.setAvlBal(StringUtils.isNull(map.get("AvlBal")));
		this.setFrzBal(StringUtils.isNull(map.get("FrzBal")));
		this.setAcctBal(StringUtils.isNull(map.get("AcctBal")));
	}
	
	/**
	 * 请求参数校验
	 */
	@Override
	public void requestColumnValid() {
		super.requestColumnValid();
		LOGGER.info("CbhbQueryBalanceModel valid...");
	}

	public String getPlaCustId() {
		return plaCustId;
	}

	public void setPlaCustId(String plaCustId) {
		this.plaCustId = plaCustId;
	}

	public String getAvlBal() {
		return avlBal;
	}

	public void setAvlBal(String avlBal) {
		this.avlBal = avlBal;
	}

	public String getAcctBal() {
		return acctBal;
	}

	public void setAcctBal(String acctBal) {
		this.acctBal = acctBal;
	}

	public String getFrzBal() {
		return frzBal;
	}

	public void setFrzBal(String frzBal) {
		this.frzBal = frzBal;
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
