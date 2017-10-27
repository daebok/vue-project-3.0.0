package com.rd.ifaes.core.tpp.model.jx.file;

import com.rd.ifaes.common.util.StringUtils;

public class JxFileBatchDebtRegisterModel extends JxFileBaseModel {
	private String bank; //银行代号	N	4	1	4	M	托管银行编号
	private String batch; //批次号	N	6	5	10	M	对于文件名称中“XXXXXX”
	private String bidnbr; //标的编号	A	40	11	50	M	若后台开通标的扩位后，可使用40位以内的标的号，反之仅能使用6位以内标的号。标的号由P2P生成，使用字母+数字的组合， 不区分大小写，且平台全局必须保证唯一；
	private String bidDesc; //标的描述	A	60	51	110	M	　
	private String cardnbr; //借款人电子账号	A	19	111	129	M	　
	private String amount; //借款金额	N	13,2	130	142	M	两位小数
	private String inttype; //付息方式	N	1	143	143	M	0：到期与本金一起归还；1：每月固定日期支付；2：每月不确定日期支付；
	private String intpayday; //利息每月支付日	N	2	144	145	C	DD付息方式为1时必填；若设置日期大于月份最后一天时，则为该月最后一天支付；
	private String loanTerm; //项目期限	N	4	146	149	M	单位为天
	private String yield; //预期年化收益率	N	8,5	150	157	M	五位小数如年化收益率为10%，需上送1,000,000
	private String cardnbrSu; //担保人电子账号	A	19	158	176	C	　
	private String cardnbrMy; //名义借款人电子账号	A	19	177	195	C	填空
	private String mborrowYn; //多种借款人模式标志	N	1	196	196	C	0-不开启1-开启
	private String cardnbrPe; //收款人电子账户	A	19	197	215	C	填空
	private String entrustYn; // 受托支付标志	N	1	216	216	C	多种借款人模式下使用0-不开启1-开启
	private String reserved; //保留域	A	100	217	316	C	　
	private String trdresv; //第三方平台保留域	A	100	317	416	C	第三方平台保留使用,原样返回

	
	
	private String name; //借款人姓名	NAME	A	60	130	189	C	　
	private String inpdate; //发标日期	INPDATE	N	8	207	214	C	CCYYMMDD标的信息录入时的系统日期
	
	private String rspcode; //处理响应码	RSPCODE	A	2	272	273	C	　

	private StringBuffer detail;
	
	
	public JxFileBatchDebtRegisterModel(String bank, String batch, String bidnbr, String bidDesc, String cardnbr, String amount,
			String inttype, String intpayday, String loanTerm, String yield, String cardnbrSu, String cardnbrMy,
			String mborrowYn, String cardnbrPe, String entrustYn, String reserved, String trdresv) {
		super();
		this.bank = checkLengthByLeft(bank, 4);
		this.batch = checkLengthByLeft(batch, 6);
		this.bidnbr = checkLength(bidnbr, 40);
		this.bidDesc = checkCharLength(bidDesc, 60);
		this.cardnbr = checkLength(cardnbr, 19);
		this.amount = checkLengthByLeft(amount, 13);
		this.inttype = checkLengthByLeft(inttype, 1);
		this.intpayday = checkLengthByLeft(intpayday, 2);
		this.loanTerm = checkLengthByLeft(loanTerm, 4);
		this.yield = checkLengthByLeft(yield, 8);
		this.cardnbrSu = checkLength(cardnbrSu, 19);
		this.cardnbrMy = checkLength(cardnbrMy, 19);
		this.mborrowYn = checkLengthByLeft(mborrowYn, 1);
		this.cardnbrPe = checkLength(cardnbrPe, 19);
		this.entrustYn = checkLengthByLeft(entrustYn, 1);
		this.reserved = checkCharLength(reserved, 100);
		this.trdresv = checkCharLength(trdresv, 100);
		
		detail = new StringBuffer();
		detail.append(this.bank).append(this.batch).append(this.bidnbr).append(this.bidDesc)
			.append(this.cardnbr).append(this.amount).append(this.inttype).append(this.intpayday)
			.append(this.loanTerm).append(this.yield).append(this.cardnbrSu).append(this.cardnbrMy)
			.append(this.mborrowYn).append(this.cardnbrPe).append(this.entrustYn).append(this.reserved)
			.append(this.trdresv);
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
	public JxFileBatchDebtRegisterModel instance(String resultStr) {
		this.setBank(StringUtils.isNull(resultStr.substring(0, 4)));
		this.setBatch(StringUtils.isNull(resultStr.substring(4, 10)));
		this.setBidnbr(StringUtils.isNull(resultStr.substring(10, 50)));
		this.setBidDesc(StringUtils.isNull(resultStr.substring(50, 110)));
		this.setCardnbr(StringUtils.isNull(resultStr.substring(110, 129)));// 要3位，不然又问题
		this.setName(StringUtils.isNull(resultStr.substring(129, 189)));
		this.setAmount(StringUtils.isNull(resultStr.substring(189, 202)));
		this.setLoanTerm(StringUtils.isNull(resultStr.substring(202, 206)));
		this.setInpdate(StringUtils.isNull(resultStr.substring(206, 214)));
		this.setCardnbrSu(StringUtils.isNull(resultStr.substring(214, 233)));
		this.setCardnbrMy(StringUtils.isNull(resultStr.substring(233, 252)));
		this.setCardnbrPe(StringUtils.isNull(resultStr.substring(252, 271)));
		this.setRspcode(StringUtils.isNull(resultStr.substring(271, 273)));
		this.setReserved(StringUtils.isNull(resultStr.substring(273, 373)));
		this.setTrdresv(StringUtils.isNull(resultStr.substring(373, 473)));
		return this;
	}
	public String getBidnbr() {
		return bidnbr;
	}

	public void setBidnbr(String bidnbr) {
		this.bidnbr = bidnbr;
	}

	public String getCardnbr() {
		return cardnbr;
	}

	public void setCardnbr(String cardnbr) {
		this.cardnbr = cardnbr;
	}

	public String getCardnbrSu() {
		return cardnbrSu;
	}

	public void setCardnbrSu(String cardnbrSu) {
		this.cardnbrSu = cardnbrSu;
	}

	public String getCardnbrMy() {
		return cardnbrMy;
	}

	public void setCardnbrMy(String cardnbrMy) {
		this.cardnbrMy = cardnbrMy;
	}

	public String getCardnbrPe() {
		return cardnbrPe;
	}

	public void setCardnbrPe(String cardnbrPe) {
		this.cardnbrPe = cardnbrPe;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public String getBidDesc() {
		return bidDesc;
	}

	public void setBidDesc(String bidDesc) {
		this.bidDesc = bidDesc;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getInttype() {
		return inttype;
	}

	public void setInttype(String inttype) {
		this.inttype = inttype;
	}

	public String getIntpayday() {
		return intpayday;
	}

	public void setIntpayday(String intpayday) {
		this.intpayday = intpayday;
	}

	public String getLoanTerm() {
		return loanTerm;
	}

	public void setLoanTerm(String loanTerm) {
		this.loanTerm = loanTerm;
	}

	public String getYield() {
		return yield;
	}

	public void setYield(String yield) {
		this.yield = yield;
	}

	public String getMborrowYn() {
		return mborrowYn;
	}

	public void setMborrowYn(String mborrowYn) {
		this.mborrowYn = mborrowYn;
	}

	public String getEntrustYn() {
		return entrustYn;
	}

	public void setEntrustYn(String entrustYn) {
		this.entrustYn = entrustYn;
	}

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

	public String getTrdresv() {
		return trdresv;
	}

	public void setTrdresv(String trdresv) {
		this.trdresv = trdresv;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInpdate() {
		return inpdate;
	}

	public void setInpdate(String inpdate) {
		this.inpdate = inpdate;
	}

	public String getRspcode() {
		return rspcode;
	}

	public void setRspcode(String rspcode) {
		this.rspcode = rspcode;
	}

	public StringBuffer getDetail() {
		return detail;
	}

	public void setDetail(StringBuffer detail) {
		this.detail = detail;
	}

	
	/**
	 * 应答码	描述
01	银行号不允许为空
02	批次号不允许为空
03	标的编号不允许为空
04	标的描述不允许为空
05	借款人电子账号不允许为空
06	借款金额不允许为空
07	借款金额不允许为非数字型
09	项目期限不能为空
10	预期年化收益率不允许为空
11	银行号与文件名不一致
12	文件名批次号与文件体不符
13	借款人账户不存在
14	借款人账户所属银行错误
15~17	借款人账户状态不正常
19	未找到借款人卡号对应手机号记录
20	第三方平台验证不通过
21	未找到借款人账户客户信息
22	标的信息已存在，不允许重复录入
23	付息方式不合法
24	利息每月支付日必送
25	黑名单客户无法录入标的信息
26	担保人卡片不存在
27	担保人卡片不属于该发卡行
28	担保人卡片跟借款人卡片非同产品
29	未找到此担保人卡号对应的账户记录
30	担保人账户已销户
31	未找到担保人的客户信息
32	名义借款人卡片不存在
33	名义借款人卡片不属于该发卡行
34	名义借款人卡片跟借款人卡片非同产品
35	未找到名义借款人卡号对应的账户记录
36	名义借款人账户已销户
37	未找到名义借款人的客户记录
38	未找到名义借款人卡片对应记录
39	平台账户不能进行标的录入
41	收款人卡片不存在
45	受托支付标志非法
46	多种借款人模式下收款人电子账号必送
47	未找到此担保人卡号对应的账户记录
48	多种借款人标识非法
49	未找到担保人卡片对应记录
50	收款人卡片不属于该发卡行
51	收款人卡片跟借款人卡片非同产品
52	未找到收款人卡号对应的账户记录
53	收款人账户已销户
54	未找到收款人的客户记录
55	未找到收款人卡片对应记录

	 */
	
	
}
