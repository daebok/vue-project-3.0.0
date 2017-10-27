package com.rd.ifaes.core.tpp.model.jx.file;

import com.rd.ifaes.common.util.StringUtils;

public class JxFileProjectCreditModel extends JxFileBaseModel {
	private String bank; //银行代号	BANK	N	4	1	4	M	存管银行编号
	private String batch; //批次号	BATCH	N	6	5	10	M	对于文件名称中“XXXXXX”
	private String cardnnbr; //债权持有人电子账号	CARDNNBR	A	19	11	29	M	
	private String fissuer; //产品发行方	FUISSUER	A	4	30	33	M	产品发行方即为平台编号
	private String product; //标的号	PRODUCT	A	6	34	39	M	1、未开通标的扩位使用2、填入已登记成功的标的号；标的号不区分大小写；
	private String seriNo; //申请流水号	SERI-NO	A	40	40	79	M	投标或者债转的申请流水号(COINSTCODE+0000+orderId)，COINSTCODE为给平台分配的固定参数，orderId由P2P生成，使用字母+数字的组合，不区分大小写，且平台全局必须保证唯一；
	private String amount; //当前持有债权金额	AMOUNT	N	13	80	92	M	整数11位，小数2位；指扣除已经转让的金额后当前持有的金额。如债权金额100.5元，即为：0000000010050
	private String buydate; //债权获取日期	BUYDATE	N	8	93	100	C	YYYYMMDD或空；债权获取日期必须小于等于银行当前系统日期。若为空时同起息日
	private String intdate; //起息日	INTDATE	N	8	101	108	M	起息日必须小于等于银行当前系统日期
	private String intype; //付息方式	INTTYPE	N	1	109	109	M	0：到期与本金一起归还；1：每月固定日期支付；2：每月不确定日期支付；
	private String intpayday; //利息每月支付日	INTPAYDAY	N	2	110	111	C	DD或空；（请勿上传00）付息方式为1时必填；若设置日期大于月份最后一天时，则为该月最后一天支付；
	private String enddate; //产品到期日	ENDDATE	N	8	112	119	M	YYYYMMDD；产品到期日必须大于等于银行系统日期
	private String yield; //预期年化收益率	YIELD	N	8	120	127	M	整数3位，小数5位。 如年化利率12.5%，即为：01250000
	private String cirr; //币种	CURR	N	3	128	130	M	156：人民币
	private String bidif; //标的编号-40	BIDIF	A	40	131	170	C	1、后台开通标的扩位后使用40位标的号2、填入已登记成功的标的号；标的号不区分大小写；
	private String rese; //保留域	RESE	A	60	171	230	C	

	private String name; //持卡人姓名	NAME	A	60	93	152	C	开户时姓名信息；
	private String dealdate; //处理日期	DEALDATE	N	8	153	160	M	YYYYMMDD
	private String rspcode; //处理响应码	RSPCODE	A	2	161	162	M	‘00’：成功‘14’：无效账号‘93’:姓名校验错误‘99’：其它错误原因
	private String authcode; //申请授权码	AUTHCODE	A	20	163	182		存管平台系统产生，用于到期还款时的校验；
	

	private StringBuffer detail;
	
	public JxFileProjectCreditModel(String bank, String batch, String cardnnbr, String fissuer, String product, String seriNo,
			String amount, String buydate, String intdate, String intype, String intpayday, String enddate, String yield, String cirr,
			String bidif, String rese) {
		super();
		this.bank = checkLengthByLeft(bank, 4);
		this.batch = checkLengthByLeft(batch, 6);
		this.cardnnbr = checkLength(cardnnbr, 19);
		this.fissuer = checkLength(fissuer, 4);
		this.product = checkLength(product, 6);
		this.seriNo = checkLength(seriNo, 40);
		this.amount = checkLengthByLeft(amount, 13);
		this.buydate = checkLengthByLeft(buydate, 8);
		this.intdate = checkLengthByLeft(intdate, 8);
		this.intype = checkLengthByLeft(intype, 1);
		this.intpayday = checkLengthByLeft(intpayday, 2);
		this.enddate = checkLengthByLeft(enddate, 8);
		this.yield = checkLengthByLeft(yield, 8);
		this.cirr = checkLengthByLeft(cirr, 3);
		this.bidif = checkLength(bidif, 40);
		this.rese = checkCharLength(rese, 60);
		
		detail = new StringBuffer();
		detail.append(this.bank).append(this.batch).append(this.cardnnbr).append(this.fissuer)
			.append(this.product).append(this.seriNo).append(this.amount).append(this.buydate)
			.append(this.intdate).append(this.intype).append(this.intpayday).append(this.enddate)
			.append(this.yield).append(this.cirr).append(this.bidif).append(this.rese);
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
	public JxFileProjectCreditModel instance(String resultStr) {
		this.setBank(StringUtils.isNull(resultStr.substring(0, 4)));
		this.setBatch(StringUtils.isNull(resultStr.substring(4, 10)));
		this.setCardnnbr(StringUtils.isNull(resultStr.substring(10, 29)));
		this.setFissuer(StringUtils.isNull(resultStr.substring(29, 33)));
		this.setProduct(StringUtils.isNull(resultStr.substring(33, 39)));
		this.setSeriNo(StringUtils.isNull(resultStr.substring(39, 79)));
		this.setAmount(StringUtils.isNull(resultStr.substring(79, 92)));
		this.setName(StringUtils.isNull(resultStr.substring(92, 152)));
		this.setRspcode(StringUtils.isNull(resultStr.substring(152, 160)));
		this.setRspcode(StringUtils.isNull(resultStr.substring(160, 162)));
		this.setAuthcode(StringUtils.isNull(resultStr.substring(162, 182)));
		this.setBidif(StringUtils.isNull(resultStr.substring(182, 222)));
		this.setRspcode(StringUtils.isNull(resultStr.substring(222, 282)));
		return this;
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

	public String getCardnnbr() {
		return cardnnbr;
	}

	public void setCardnnbr(String cardnnbr) {
		this.cardnnbr = cardnnbr;
	}

	public String getFissuer() {
		return fissuer;
	}

	public void setFissuer(String fissuer) {
		this.fissuer = fissuer;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getSeriNo() {
		return seriNo;
	}

	public void setSeriNo(String seriNo) {
		this.seriNo = seriNo;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBuydate() {
		return buydate;
	}

	public void setBuydate(String buydate) {
		this.buydate = buydate;
	}

	public String getIntdate() {
		return intdate;
	}

	public void setIntdate(String intdate) {
		this.intdate = intdate;
	}

	public String getIntype() {
		return intype;
	}

	public void setIntype(String intype) {
		this.intype = intype;
	}

	public String getIntpayday() {
		return intpayday;
	}

	public void setIntpayday(String intpayday) {
		this.intpayday = intpayday;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getYield() {
		return yield;
	}

	public void setYield(String yield) {
		this.yield = yield;
	}

	public String getCirr() {
		return cirr;
	}

	public void setCirr(String cirr) {
		this.cirr = cirr;
	}

	public String getBidif() {
		return bidif;
	}

	public void setBidif(String bidif) {
		this.bidif = bidif;
	}

	public String getRese() {
		return rese;
	}

	public void setRese(String rese) {
		this.rese = rese;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDealdate() {
		return dealdate;
	}

	public void setDealdate(String dealdate) {
		this.dealdate = dealdate;
	}

	public String getRspcode() {
		return rspcode;
	}

	public void setRspcode(String rspcode) {
		this.rspcode = rspcode;
	}

	public String getAuthcode() {
		return authcode;
	}

	public void setAuthcode(String authcode) {
		this.authcode = authcode;
	}

	public StringBuffer getDetail() {
		return detail;
	}

	public void setDetail(StringBuffer detail) {
		this.detail = detail;
	}

	
}
