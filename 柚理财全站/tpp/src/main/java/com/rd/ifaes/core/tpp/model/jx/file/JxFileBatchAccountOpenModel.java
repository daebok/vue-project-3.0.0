package com.rd.ifaes.core.tpp.model.jx.file;

import com.rd.ifaes.common.util.FileUtil;
import com.rd.ifaes.common.util.StringUtils;

public class JxFileBatchAccountOpenModel extends JxFileBaseModel {
	private String idNo;// 证件编号* IDNO A 18 1 18 M 企业开户时填组织机构代码/社会信用号
	private String idType;// 证件类型* IDTYPE A 2 19 20 M
							// 01-身份证18位02-身份证15位20-其它25-企业社会信用代码注：企业开户时组织机构代码上送20，社会信用号25
	private String name;// 中文姓名* NAME A 60 21 80 M 企业开户时上送企业名称
	private String gen;// 性别 GEN A 1 81 81 C 1-男,2-女注：企业开户时送空
	private String moPhone;// 手机号码 MOPHONE A 12 82 93 M 个人账户必填；
	private String accType;// 账户类型 ACCTYPE A 1 94 94 0：个人账户；1：企业账户；
	private String email;// E-mail地址 EMAIL A 40 95 134 C
	private String appId;// 请求方用户ID APPID A 60 135 194 M 发放方保证唯一，（即开户的流水号）
	private String busId;// 营业执照编号 BUSID A 9 195 203 C 可不填
	private String taxId;// 税务登记号 TAXID A 30 204 233 C 六位行政区划代码+九位组织机构代码
	private String adNo;// 渠道推荐码 ADNO A 20 234 253
	private String accTypeT;// 账户类型 ACC-TYPE N 1 254 254 M 0-基金账户1-靠档计息账户2-活期账户
	private String fucomCode;// 基金公司代码 FUCOMCODE A 2 255 256 C
								// ZZ-华安0Z-国寿安保空-银行默认开通的基金公司代码
	private String info;// 请求方保留信息 INFO A 100 257 356
	private String cAccount;// 对公账户号 CACCOUNT A 42 357 398 C
							// 当企业开户时必输入，作为企业电子账户的绑定账号
	private String busId1;// 营业执照编号1 BUSID A 18 399 416 C
	private String reves;// 保留域 REVERS A 17 417 433 C

	private String cardNbr;// 存管平台电子账号 CARDNBR A 19 1 19 M 成功失败标志非F时返回
	private String flag;// 成功失败标志 FLAG A 1 40 40 M
						// F-失败S-成功N-该客户已通过其它渠道开户，此时仍返回存管平台电子账号
	private String errCode;// 失败错误码 ERRCODE N 3 41 43 M 三位的错误码

	private StringBuffer detail;

	/**
	 * 请求参数
	 * 
	 * @param idNo
	 * @param idType
	 * @param name
	 * @param gen
	 * @param moPhone
	 * @param accType
	 * @param email
	 * @param appId
	 * @param busId
	 * @param taxId
	 * @param adNo
	 * @param accTypeT
	 * @param fucomCode
	 * @param info
	 * @param cAccount
	 * @param busId1
	 * @param reves
	 */
	public JxFileBatchAccountOpenModel(String idNo, String idType, String name, String gen, String moPhone, String accType,
			String email, String appId, String busId, String taxId, String adNo, String accTypeT, String fucomCode, String info,
			String cAccount, String busId1, String reves) {
		super();
		this.idNo = checkLength(idNo, 18);
		this.idType = checkLength(idType, 2);
		this.name = checkCharLength(name, 60);
		this.gen = checkLength(gen, 1);
		this.moPhone = checkLength(moPhone, 12);
		this.accType = checkLength(accType, 1);
		this.email = checkLength(email, 40);
		this.appId = checkLength(appId, 60);
		this.busId = checkLength(busId, 9);
		this.taxId = checkLength(taxId, 30);
		this.adNo = checkLength(adNo, 20);
		this.accTypeT = checkLengthByLeft(accTypeT, 1);
		this.fucomCode = checkLength(fucomCode, 2);
		this.info = checkLength(info, 100);
		this.cAccount = checkLength(cAccount, 42);
		this.busId1 = checkLength(busId1, 18);
		this.reves = checkCharLength(reves, 17);

		detail = new StringBuffer();
		detail.append(this.idNo).append(this.idType).append(this.name).append(this.gen).append(this.moPhone).append(this.accType)
				.append(this.email).append(this.appId).append(this.busId).append(this.taxId).append(this.adNo).append(this.accTypeT)
				.append(this.fucomCode).append(this.info).append(this.cAccount).append(this.busId1).append(this.reves);
	}

	public JxFileBatchAccountOpenModel() {
		super();
	}

	/**
	 * 
	 * 获取返回值
	 * 
	 * @author jxx
	 * @date 2017年8月31日
	 * @param resultStr
	 * @return
	 */
	public JxFileBatchAccountOpenModel instance(String resultStr) {
		this.setCardNbr(StringUtils.isNull(resultStr.substring(0, 19)));
		this.setIdNo(StringUtils.isNull(resultStr.substring(19, 37)));
		this.setIdType(StringUtils.isNull(resultStr.substring(37, 39)));
		this.setFlag(StringUtils.isNull(resultStr.substring(39, 40)));
		this.setErrCode(StringUtils.isNull(resultStr.substring(40, 43)));// 要3位，不然又问题
		this.setName(StringUtils.isNull(resultStr.substring(43, 103)));
		this.setAccType(StringUtils.isNull(resultStr.substring(103, 104)));
		this.setAppId(StringUtils.isNull(resultStr.substring(104, 164)));
		this.setMoPhone(StringUtils.isNull(resultStr.substring(164, 176)));
		this.setInfo(StringUtils.isNull(resultStr.substring(176, 276)));
		this.setReves(StringUtils.isNull(resultStr.substring(276, 364)));
		return this;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGen() {
		return gen;
	}

	public void setGen(String gen) {
		this.gen = gen;
	}

	public String getMoPhone() {
		return moPhone;
	}

	public void setMoPhone(String moPhone) {
		this.moPhone = moPhone;
	}

	public String getAccType() {
		return accType;
	}

	public void setAccType(String accType) {
		this.accType = accType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getBusId() {
		return busId;
	}

	public void setBusId(String busId) {
		this.busId = busId;
	}

	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public String getAdNo() {
		return adNo;
	}

	public void setAdNo(String adNo) {
		this.adNo = adNo;
	}

	public String getAccTypeT() {
		return accTypeT;
	}

	public void setAccTypeT(String accTypeT) {
		this.accTypeT = accTypeT;
	}

	public String getFucomCode() {
		return fucomCode;
	}

	public void setFucomCode(String fucomCode) {
		this.fucomCode = fucomCode;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getcAccount() {
		return cAccount;
	}

	public void setcAccount(String cAccount) {
		this.cAccount = cAccount;
	}

	public String getBusId1() {
		return busId1;
	}

	public void setBusId1(String busId1) {
		this.busId1 = busId1;
	}

	public String getReves() {
		return reves;
	}

	public void setReves(String reves) {
		this.reves = reves;
	}

	public String getCardNbr() {
		return cardNbr;
	}

	public void setCardNbr(String cardNbr) {
		this.cardNbr = cardNbr;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public StringBuffer getDetail() {
		return detail;
	}

	public void setDetail(StringBuffer detail) {
		this.detail = detail;
	}

	public static void main(String[] args) {
		StringBuffer sb = new StringBuffer();
		JxFileBatchAccountOpenModel model = 
				new JxFileBatchAccountOpenModel("913715021564897531", "25", "小胖食品有限公司", "",
				"13735885111", "1", "hello@163.com", "2017083116381100001", "", "", "", "2", "", "", "130686867038", "", "");
		sb = model.getDetail();
		sb.append(StringUtils.LF);
		model = new JxFileBatchAccountOpenModel("72539548", "20", "小瘦办公有限公司", "", "13735885112", "1", "world@163.com",
				"2017083116381100002", "", "310229312565852", "", "2", "", "", "03803900040205529", "510106000167012", "");
		sb.append(model.getDetail());
		sb.append(StringUtils.LF);
		model =  new JxFileBatchAccountOpenModel("91310230324346884P", "25", "上海金宠投资管理中心", "",
				"13735885113", "1", "1111111@163.com", "2017083116381100003", "", "", "", "2", "", "", "31001507000059730261", "", "");
		sb.append(model.getDetail());
		sb.append(StringUtils.LF);
		LOGGER.info("文件内容：{}", sb.toString());
		
		String path = "D:/3005-APPZX0139-000002-20170831";
		try {
			FileUtil.writeTxtFile(sb.toString(), path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
