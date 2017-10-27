package com.rd.ifaes.core.tpp.model.cbhb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;

/**
 * 
 *  3.8.1	还款（不含收取担保费）
 * @version 3.0
 * @author ZhangBiao
 * @date 2017年2月21日
 */
public class CbhbFileRepaymentModel extends CbhbBaseModel{
	
	private static final long serialVersionUID = -3639191443537894840L;
	/**
	 * LOGGER
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CbhbFileRepaymentModel.class);
	
	/**
	 * 标的ID	String(10)
	 */
	private String borrowId;
	
	/**
	 * 还款类型	String(1)
	 */
	private String repayTyp;
	
	/**
	 * 分期还款总期数	String(3)
	 */
	private String repayInstTot;
	
	/**
	 * 分期还款当前期数	String(3)
	 */
	private String repayInstCur;
	
	/**
	 * 文件名	String(256)
	 */
	private String fileName;
	
	/**
	 * 请求参数
	 */
	private String[] requestParamNames = new String[] {"char_set","partner_id","version_no","biz_type","sign_type",
			"MerBillNo","BorrowId","RepayTyp","BgRetUrl","repay_inst_tot","repay_inst_cur","FileName","MerPriv","mac"};

	/**
	 * 响应参数
	 */
	private String[] responseParamNames = new String[] {"partner_id","version_no","biz_type","sign_type","MerBillNo",
			"RespCode","RespDesc","mac"};
	/**
	 * 构造器
	 */
	public CbhbFileRepaymentModel(){
		super();
		this.setBizType(CbhbConstant.BIZ_TYPE_FILE_REPAYMENT);
		this.setBgRetUrl(ConfigUtils.getValue("notify_url")+ "/cbhb/fileRepayment/notify.html");
		this.setFileName(this.getFileNameOnByRule());
	}
	
	/**
	 * 请求参数校验
	 */
	@Override
	public void requestColumnValid() {
		super.requestColumnValid();
		LOGGER.info("CbhbFileRepaymentModel valid...");
	}
	
	public String getBorrowId() {
		return borrowId;
	}

	public void setBorrowId(String borrowId) {
		this.borrowId = borrowId;
	}

	public String getRepayTyp() {
		return repayTyp;
	}

	public void setRepayTyp(String repayTyp) {
		this.repayTyp = repayTyp;
	}

	public String getRepayInstTot() {
		return repayInstTot;
	}

	public void setRepayInstTot(String repayInstTot) {
		this.repayInstTot = repayInstTot;
	}

	public String getRepayInstCur() {
		return repayInstCur;
	}

	public void setRepayInstCur(String repayInstCur) {
		this.repayInstCur = repayInstCur;
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
