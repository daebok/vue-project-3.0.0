package com.rd.ifaes.core.tpp.model.jx.file;

import com.rd.ifaes.common.util.DateUtils;

public class JxFileTradeDetailAllModel extends JxFileBaseModel {

	public JxFileTradeDetailAllModel() {
		this(DateUtils.dateStr7(DateUtils.getNow()));
	}
	
	public JxFileTradeDetailAllModel(String date) {
		super();
        this.setTxDate(date);
		this.setFileName(new StringBuffer(BANKNUM).append("-ALEVE").append(PRODUCT).append("-").append(date).toString());
		mapReqData.put("fileName", this.getFileName());
	}

}
