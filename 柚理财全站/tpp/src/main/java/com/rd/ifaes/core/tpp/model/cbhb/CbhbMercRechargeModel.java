package com.rd.ifaes.core.tpp.model.cbhb;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.util.StringUtils;

/**
 * 
 *  3.13	商户账户充值（后台方式）
 * @version 3.0
 * @author ZhangBiao
 * @date 2017年2月21日
 */
public class CbhbMercRechargeModel extends CbhbBaseModel{
	
	private static final long serialVersionUID = -3639191443537894840L;
	/**
	 * LOGGER
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CbhbMercRechargeModel.class);
	/**
	 * 交易金额	Number(16)
	 */
	private String transAmt;
	
	/**
	 * 充值账户	String(3)
	 */
	private String merAccTyp;
	
	/**
	 * 账户存管平台流水号	String(32)
	 */
	private String transId;

	/**
	 * 请求参数
	 */
	private String[] requestParamNames = new String[] {"char_set","partner_id","version_no","biz_type","sign_type",
			"MerBillNo","TransAmt","MerAccTyp","MerPriv","mac"};

	/**
	 * 响应参数
	 */
	private String[] responseParamNames = new String[] {"partner_id","version_no","biz_type","sign_type","MerBillNo","TransId",
			"RespCode","RespDesc","mac"};
	
	/**
	 * 构造器
	 */
	public CbhbMercRechargeModel(){
		super();
		this.setBizType(CbhbConstant.BIZ_TYPE_MERC_RECHARGE);
	}
	
	/**
	 * 请求参数校验
	 */
	@Override
	public void requestColumnValid() {
		super.requestColumnValid();
		LOGGER.info("CbhbMercRechargeModel valid...");
	}
	
	public void response(Map<String, String> map) {
		super.response(map);
		this.setTransId(StringUtils.isNull(map.get("TransId")));
	}
	
	public String getTransAmt() {
		return transAmt;
	}

	public void setTransAmt(String transAmt) {
		this.transAmt = transAmt;
	}

	public String getMerAccTyp() {
		return merAccTyp;
	}

	public void setMerAccTyp(String merAccTyp) {
		this.merAccTyp = merAccTyp;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
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
