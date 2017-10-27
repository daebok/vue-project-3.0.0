package com.rd.ifaes.core.tpp.model.cbhb;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.resource.CbhbResource;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.tpp.exception.CbhbException;

/**
 * 
 *  3.3	建标（后台方式）
 * @version 3.0
 * @author ZhangBiao
 * @date 2017年2月21日
 */
@SuppressWarnings("serial")
public class CbhbCreateBidModel extends CbhbBaseModel{
	
	/**
	 * LOGGER
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CbhbCreateBidModel.class);
	/**
	 * 标的ID	String(10)
	 */
	private String borrowId;
	
	/**
	 * 标的属性	String(1)
	 */
	private String borrowTyp;
	
	/**
	 * 标的金额	Number(16)
	 */
	private String borrowerAmt;
	
	/**
	 * 标利息	Number(16)
	 */
	private String borrowerInterestAmt;
	
	/**
	 * 借款人账户存管平台客户号	String(32)
	 */
	private String borrCustId;
	
	/**
	 * 对公账户户名	String(128)
	 */
	private String accountName;
	
	/**
	 * 担保人账号	String(32)
	 */
	private String guaranteeNo;
	
	/**
	 * 募集开始日	String(8)
	 */
	private String borrowerStartDate;
	
	/**
	 * 募集到期日	String(8)
	 */
	private String borrowerEndDate;
	
	/**
	 * 标到期日	String(8)
	 */
	private String borrowerRepayDate;
	
	/**
	 * 放款方式	String(1)
	 */
	private String releaseType;
	
	/**
	 * 投资期限类型	
	 */
	private String investDateType;
	
	/**
	 * 投资期限	
	 */
	private String investPeriod;
	
	/**
	 * 标的详细信息	String(1024)
	 */
	private String borrowerDetails;

	/**
	 * 账户存管平台流水	String(32)
	 */
	private String transId;
	
	/**
	 * 请求参数
	 */
	private String[] requestParamNames = new String[] {"char_set","partner_id","version_no","biz_type","sign_type",
			"MerBillNo","BorrowId","BorrowTyp","BorrowerAmt","BorrowerInterestAmt","BorrCustId","AccountName",
			"GuaranteeNo","BorrowerStartDate","BorrowerEndDate","BorrowerRepayDate","ReleaseType","InvestDateType",
			"InvestPeriod","BorrowerDetails","MerPriv","mac"};

	/**
	 * 响应参数
	 */
	private String[] responseParamNames = new String[] {"partner_id","version_no","biz_type","sign_type","MerBillNo","RespCode","RespDesc","TransId","BorrowId","BorrowerAmt","MerPriv","mac"};
	
	public CbhbCreateBidModel(){
		super();
		this.setBizType(CbhbConstant.BIZ_TYPE_CREATE_BID);
	}
	
	/**
	 * 请求参数校验
	 */
	@Override
	public void requestColumnValid() {
		super.requestColumnValid();
		LOGGER.info("cbhbCreateBid valid...");
		if(StringUtils.isBlank(this.getBorrowId())){
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_PORJECTNO_EMPTY),BussinessException.TYPE_JSON);
		}
		if(StringUtils.isBlank(this.getBorrowTyp())){
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_BORROWTYP_EMPTY),BussinessException.TYPE_JSON);
		}
		if(StringUtils.isBlank(this.getBorrowerInterestAmt())){
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_BORROWERINTERESTAMT_EMPTY),BussinessException.TYPE_JSON);
		}
		if(StringUtils.isBlank(this.getBorrCustId())){
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_BORRCUSTID_EMPTY),BussinessException.TYPE_JSON);
		}
		if(StringUtils.isBlank(this.getGuaranteeNo())){
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_GUARANTEENO_EMPTY),BussinessException.TYPE_JSON);
		}
		if(StringUtils.isBlank(this.getBorrowerStartDate())){
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_BORROWSTARTDATE_EMPTY),BussinessException.TYPE_JSON);
		}
		if(StringUtils.isBlank(this.getBorrowerEndDate())){
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_BORROWENDDATE_EMPTY),BussinessException.TYPE_JSON);
		}
		if(StringUtils.isBlank(this.getBorrowerRepayDate())){
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_BORROWREPAYDATE_EMPTY),BussinessException.TYPE_JSON);
		}
		if(StringUtils.isBlank(this.getReleaseType())){
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_RELEASETYPE_EMPTY),BussinessException.TYPE_JSON);
		}
	}
	
	/**
	 * 通用 回调信息处理
	 * 有子类  每个子类需重写此类
	 * @author QianPengZhan
	 * @date 2017年2月24日
	 */
	public void response(Map<String, String> map) {
		super.response(map);
		this.setBorrowId(StringUtils.isNull(map.get("BorrowId")));
		this.setTransId(StringUtils.isNull(map.get("TransId")));
		this.setBorrowerAmt(StringUtils.isNull(map.get("BorrowerAmt")));
		this.setMerPriv(StringUtils.isNull(map.get("MerPriv")));
	}

	public String getBorrowId() {
		return borrowId;
	}

	public void setBorrowId(String borrowId) {
		this.borrowId = borrowId;
	}

	public String getBorrowTyp() {
		return borrowTyp;
	}

	public void setBorrowTyp(String borrowTyp) {
		this.borrowTyp = borrowTyp;
	}

	public String getBorrowerAmt() {
		return borrowerAmt;
	}

	public void setBorrowerAmt(String borrowerAmt) {
		this.borrowerAmt = borrowerAmt;
	}

	public String getBorrowerInterestAmt() {
		return borrowerInterestAmt;
	}

	public void setBorrowerInterestAmt(String borrowerInterestAmt) {
		this.borrowerInterestAmt = borrowerInterestAmt;
	}

	public String getBorrCustId() {
		return borrCustId;
	}

	public void setBorrCustId(String borrCustId) {
		this.borrCustId = borrCustId;
	}

	public String getGuaranteeNo() {
		return guaranteeNo;
	}

	public void setGuaranteeNo(String guaranteeNo) {
		this.guaranteeNo = guaranteeNo;
	}

	public String getBorrowerStartDate() {
		return borrowerStartDate;
	}

	public void setBorrowerStartDate(String borrowerStartDate) {
		this.borrowerStartDate = borrowerStartDate;
	}

	public String getBorrowerEndDate() {
		return borrowerEndDate;
	}

	public void setBorrowerEndDate(String borrowerEndDate) {
		this.borrowerEndDate = borrowerEndDate;
	}

	public String getBorrowerRepayDate() {
		return borrowerRepayDate;
	}

	public void setBorrowerRepayDate(String borrowerRepayDate) {
		this.borrowerRepayDate = borrowerRepayDate;
	}

	public String getReleaseType() {
		return releaseType;
	}

	public void setReleaseType(String releaseType) {
		this.releaseType = releaseType;
	}

	public String getInvestDateType() {
		return investDateType;
	}

	public void setInvestDateType(String investDateType) {
		this.investDateType = investDateType;
	}

	public String getInvestPeriod() {
		return investPeriod;
	}

	public void setInvestPeriod(String investPeriod) {
		this.investPeriod = investPeriod;
	}

	public String getBorrowerDetails() {
		return borrowerDetails;
	}

	public void setBorrowerDetails(String borrowerDetails) {
		this.borrowerDetails = borrowerDetails;
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

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

}
