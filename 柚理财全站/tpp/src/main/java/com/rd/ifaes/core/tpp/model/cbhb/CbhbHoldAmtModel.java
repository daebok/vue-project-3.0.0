package com.rd.ifaes.core.tpp.model.cbhb;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.util.StringUtils;

/**
 * 
 * 3.9	资金转出冻结（后台方式）
 * @version 3.0
 * @author ZhangBiao
 * @date 2017年2月21日
 */
@Deprecated
public class CbhbHoldAmtModel extends CbhbBaseModel{
	
	private static final long serialVersionUID = -3639191443537894840L;
	/**
	 * LOGGER
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CbhbHoldAmtModel.class);
	/**
	 * 资金转出人账户存管平台客户号	String(16)
	 */
	private String customerId;
	
	/**
	 * 冻结金额	Number(16)
	 */
	private String holdAmt;

	/**
	 * 冻结编号	String(32)
	 */
	private String freezeId;
	
	/**
	 * 请求参数
	 */
	private String[] requestParamNames = new String[] {"char_set","partner_id","version_no","biz_type","sign_type","MerBillNo","CustomerId","HoldAmt","MerPriv","mac"};

	/**
	 * 响应参数
	 */
	private String[] responseParamNames = new String[] {"partner_id","version_no","biz_type","MerBillNo","HoldAmt","FreezeId","RespCode","RespDesc","mac"};
	
	/**
	 * 构造器
	 */
	public CbhbHoldAmtModel(){
		super();
		this.setBizType(CbhbConstant.BIZ_TYPE_HOLD_AMT);
	}
	
	
	/**
	 * 请求参数校验
	 */
	@Override
	public void requestColumnValid() {
		super.requestColumnValid();
		LOGGER.info("CbhbHoldAmtModel valid...");
	}
	
	public void response(Map<String, String> map) {
		super.response(map);
		this.setHoldAmt(StringUtils.isNull(map.get("HoldAmt")));
		this.setFreezeId(StringUtils.isNull(map.get("FreezeId")));
	}
	
	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getHoldAmt() {
		return holdAmt;
	}

	public void setHoldAmt(String holdAmt) {
		this.holdAmt = holdAmt;
	}

	public String getFreezeId() {
		return freezeId;
	}

	public void setFreezeId(String freezeId) {
		this.freezeId = freezeId;
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
