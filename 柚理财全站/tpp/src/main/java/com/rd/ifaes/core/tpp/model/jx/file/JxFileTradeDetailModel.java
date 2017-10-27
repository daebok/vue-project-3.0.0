package com.rd.ifaes.core.tpp.model.jx.file;

import com.rd.ifaes.common.util.DateUtils;

public class JxFileTradeDetailModel extends JxFileBaseModel {
	
	public JxFileTradeDetailModel() {
		this(DateUtils.dateStr7(DateUtils.getNow()));
	}
	
	public JxFileTradeDetailModel(String date) {
		super();
        this.setTxDate(date);
		this.setFileName(new StringBuffer(BANKNUM).append("-EVE").append(PRODUCT).append("-").append(date).toString());
		mapReqData.put("fileName", this.getFileName());
	}
}
