/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.common.resource;

/**
 * 渤海银行 资源类
 * @version 3.0
 * @author QianPengZhan
 * @date 2017年2月27日
 */
public class CbhbResource {
	
	/**
	 * http请求错误
	 */
	public static final String CBHB_RESOURCE_SUBMIT_ERROR= "cbhb.resource.error.submit";
	/**
	 * 修改银行卡返回码为{0},原因：{1}
	 */
	public static final String CBHB_RESOURCE_ERROR_BINDCARD ="cbhb.resource.error.bindCard";
	/**
	 * 渤海银行请求商户流水号不能为空	
	 */
	public static final String CBHB_RESOURCE_ERROR_REQUEST_MERBILLNO_EMPTY= "cbhb.resource.error.request.merBillNo.empty";
	/**
	 * 修改手机号返回码为：{0},原因:{1}
	 */
	public static final String CBHB_RESOURCE_ERROR_BINDMOBILE ="cbhb.resource.error.bindMobile";
	/**
	 * 私钥初始化错误	
	 */
	public static final String  CBHB_RESOURCE_ERROR_PRIVATE_KEY_INIT = "cbhb.resource.error.privateKey.init";
	/**
	 * 请求参数签名异常:{0}
	 */
	public static final String  CBHB_RESOURCE_ERROR_REQUEST_PARAMS_SIGN = "cbhb.resource.error.request.params.sign";
	/**
	 * 响应回调参数验签异常:{0}
	 */
	public static final String  CBHB_RESOURCE_ERROR_RESPONSE_PARAMS_SIGN = "cbhb.resource.error.response.params.sign";
	/**
	 * 渤海银行请求字符编码不能为空	
	 */
	public static final String  CBHB_RESOURCE_ERROR_REQUEST_CHARSET_EMPTY = "cbhb.resource.error.request.charset.empty";
	/**
	 * 渤海银行请求商户号不能为空	
	 */
	public static final String  CBHB_RESOURCE_ERROR_REQUEST_PARTNERID_EMPTY = "cbhb.resource.error.request.partnerId.empty";
	/**
	 *渤海银行请求提交地址不能为空		
	 */
	public static final String  CBHB_RESOURCE_ERROR_REQUEST_SUBMITURL_EMPTY = "cbhb.resource.error.request.submitUrl.empty";
	/**
	 *渤海银行请求签名类型不能为空	
	 */
	public static final String  CBHB_RESOURCE_ERROR_REQUEST_SIGNTYPE_EMPTY = "cbhb.resource.error.request.signType.empty";
	/**
	 *渤海银行请求版本号不能为空	
	 */
	public static final String  CBHB_RESOURCE_ERROR_REQUEST_VERSIONNO_EMPTY = "cbhb.resource.error.request.versionNo.empty";
	/**
	 *渤海银行请求批次号不能为空	
	 */
	public static final String  CBHB_RESOURCE_ERROR_REQUEST_BATCHNO_EMPTY = "cbhb.resource.error.request.batchNo.empty";
	/**
	 *渤海银行请求存管账户号不能为空	
	 */
	public static final String  CBHB_RESOURCE_ERROR_REQUEST_PLACUSTID_EMPTY = "cbhb.resource.error.request.placustId.empty";
	/**
	 * 渤海银行请求手机号不能为空	
	 */
	public static final String  CBHB_RESOURCE_ERROR_REQUEST_MOBILENO_EMPTY = "cbhb.resource.error.request.mobileNo.empty";
	/**
	 * 渤海银行请求后台通知url不能为空		
	 */
	public static final String  CBHB_RESOURCE_ERROR_REQUEST_PAGERETURNURL_EMPTY = "cbhb.resource.error.request.pageReturnUrl.empty";
	/**
	 * 渤海银行请求后台通知url不能为空		
	 */
	public static final String  CBHB_RESOURCE_ERROR_REQUEST_NOTIFYURL_EMPTY = "cbhb.resource.error.request.notifyUrl.empty";
	/**
	 * 渤海银行请求标的项目编号不能为空	
	 */
	public static final String  CBHB_RESOURCE_ERROR_REQUEST_PORJECTNO_EMPTY = "cbhb.resource.error.request.projectNo.empty";
	/**
	 * 渤海银行请求标的属性不能为空	
	 */
	public static final String  CBHB_RESOURCE_ERROR_REQUEST_BORROWTYP_EMPTY = "cbhb.resource.error.request.borrowTyp.empty";
	/**
	 * 渤海银行请求标的年化率不能为空
	 */
	public static final String  CBHB_RESOURCE_ERROR_REQUEST_BORROWERINTERESTAMT_EMPTY = "cbhb.resource.error.request.borrowerInterestAmt.empty";
	/**
	 * 渤海银行请求借款人tppCustId不能为空
	 */
	public static final String  CBHB_RESOURCE_ERROR_REQUEST_BORRCUSTID_EMPTY = "cbhb.resource.error.request.borrCustId.empty";
	/**
	 * 渤海银行请求担保人账号不能为空
	 */
	public static final String  CBHB_RESOURCE_ERROR_REQUEST_GUARANTEENO_EMPTY = "cbhb.resource.error.request.guaranteeNo.empty";
	/**
	 * 渤海银行请求募集开始日不能为空
	 */
	public static final String  CBHB_RESOURCE_ERROR_REQUEST_BORROWSTARTDATE_EMPTY = "cbhb.resource.error.request.borrowStartDate.empty";
	/**
	 * 渤海银行请求募集到期日不能为空
	 */
	public static final String  CBHB_RESOURCE_ERROR_REQUEST_BORROWENDDATE_EMPTY = "cbhb.resource.error.request.borrowEndDate.empty";
	/**
	 * 渤海银行请求标到期日不能为空
	 */
	public static final String  CBHB_RESOURCE_ERROR_REQUEST_BORROWREPAYDATE_EMPTY = "cbhb.resource.error.request.borrowRepayDate.empty";
	/**
	 * 渤海银行请求放款方式不能为空
	 */
	public static final String  CBHB_RESOURCE_ERROR_REQUEST_RELEASETYPE_EMPTY = "cbhb.resource.error.request.releaseType.empty";
	/**
	 * 渤海银行请求交易金额不能为空
	 */
	public static final String  CBHB_RESOURCE_ERROR_REQUEST_TRANSAMT_EMPTY = "cbhb.resource.error.request.transAmt.empty";
	/**
	 * 渤海银行请求商户手续费收入不能为空
	 */
	public static final String  CBHB_RESOURCE_ERROR_REQUEST_FEETYPE_EMPTY = "cbhb.resource.error.request.feeType.empty";
	/**
	 * 渤海银行请求商户手续费收入不能为空
	 */
	public static final String  CBHB_RESOURCE_ERROR_REQUEST_MERFEEAMT_EMPTY = "cbhb.resource.error.request.merFeeAmt.empty";
	/**
	 * 渤海银行请求标的ID不能为空
	 */
	public static final String  CBHB_RESOURCE_ERROR_REQUEST_BORROWID_EMPTY = "cbhb.resource.error.request.borrowId.empty";
	/**
	 * 渤海银行请求原账户存管平台流水不能为空
	 */
	public static final String  CBHB_RESOURCE_ERROR_REQUEST_OLDTRANSID_EMPTY = "cbhb.resource.error.request.oldTransId.empty";
	/**
	 * 渤海银行请求短信验证码不能为空
	 */
	public static final String  CBHB_RESOURCE_ERROR_REQUEST_SMSCODE_EMPTY = "cbhb.resource.error.request.smsCode.empty";
	/**
	 * 渤海银行请求原投标金额不能为空
	 */
	public static final String  CBHB_RESOURCE_ERROR_REQUEST_OLDTENDERMONEY_EMPTY = "cbhb.resource.error.request.oldTenderMoney.empty";
	/**
	 * 渤海银行请求冻结编号不能为空
	 */
	public static final String  CBHB_RESOURCE_ERROR_REQUEST_FREEZEID_EMPTY = "cbhb.resource.error.request.freezeId.empty";
	/**
	 * 手机号和账户存管平台客户号不可同时为空
	 */
	public static final String  CBHB_RESOURCE_ERROR_REQUEST_MOBILEORPLACUSTID_EMPTY = "cbhb.resource.error.request.mobileOrPlaCustId.empty";
	/**
	 * 渤海银行请求开户类型不能为空
	 */
	public static final String  CBHB_RESOURCE_ERROR_REQUEST_OPENTYPE_EMPTY = "cbhb.resource.error.request.openType.empty";
	/**
	 * 渤海银行请求证件类型不能为空
	 */
	public static final String  CBHB_RESOURCE_ERROR_REQUEST_IDENTITYPE_EMPTY = "cbhb.resource.error.request.identiType.empty";
	/**
	 * 渤海银行请求证件号码不能为空
	 */
	public static final String  CBHB_RESOURCE_ERROR_REQUEST_IDENTNO_EMPTY = "cbhb.resource.error.request.identNo.empty";
	/**
	 * 渤海银行请求姓名不能为空
	 */
	public static final String  CBHB_RESOURCE_ERROR_REQUEST_USRNAME_EMPTY = "cbhb.resource.error.request.usrName.empty";
	/**
	 * 渤海银行请求开户银行代号不能为空
	 */
	public static final String  CBHB_RESOURCE_ERROR_REQUEST_OPENBANKID_EMPTY = "cbhb.resource.error.request.openBankId.empty";
	/**
	 * 渤海银行请求开户银行账号不能为空
	 */
	public static final String  CBHB_RESOURCE_ERROR_REQUEST_OPENACCTID_EMPTY = "cbhb.resource.error.request.openAcctId.empty";
	
	
	
	
	
	/**
	 * 	UrlDecode解码失败
	 */
	public static final String  CBHB_RESOURCE_ERROR_URLDECODE = "cbhb.resource.error.urldecode";
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
