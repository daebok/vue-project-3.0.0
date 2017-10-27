/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.tpp.model.cbhb.file;

import java.util.ArrayList;
import java.util.List;

import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbBaseModel;

/**
 * 批量投资撤销  第二行 投资明细的实体类
 * @version 3.0
 * @author QianPengZhan
 * @date 2017年3月13日
 */
@SuppressWarnings("serial")
public class FileBatInvestCancleDetailModel extends CbhbBaseModel{
	
	/**
	 * 序号  8位
	 */
	private String id;
	
	/**
	 * 原账户存管平台流水
	 */
	private String oldTransId;
	
	/**
	 * 账户存管平台客户
	 */
	private String plaCustId;
	
	/**
	 * 原投标金额
	 */
	private String transAmt;
	
	/**
	 * 冻结编号
	 */
	private String freezeId;
	
	/**
	 * 请求参数
	 */
	private String[] requestParamNames = new String[] {"ID","OldTransId","PlaCustId","TransAmt","FreezeId"};
	
	/**
	 * 构造器
	 */
	public FileBatInvestCancleDetailModel(){
		super();
	}
	
	/**
	 * 获取明细内容
	 * @author QianPengZhan
	 * @date 2017年3月14日
	 * @param otherContent
	 * @return
	 */
	public List<FileBatInvestCancleDetailModel> getListByArray(String[] otherContent){
		List<FileBatInvestCancleDetailModel> list = new ArrayList<FileBatInvestCancleDetailModel>();
		for (int i = 0; i < otherContent.length; i++) {
			FileBatInvestCancleDetailModel model = new FileBatInvestCancleDetailModel();
			String[] content = otherContent[i].split(StringUtils.VERTICAL_LINE);
			for (int j = 0; j < content.length; j++) {
				model.setId(StringUtils.isNull(content[0]));
				model.setOldTransId(StringUtils.isNull(content[1]));
				model.setRespCode(StringUtils.isNull(content[2]));
				model.setRespDesc(StringUtils.isNull(content[3]));
				model.setTransAmt(StringUtils.isNull(content[4]));
			}
			list.add(model);
		}
		return list;
	}
	
	/**
	 * 获取属性id的值
	 * @return id属性值
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置属性id的值
	 * @param  id属性值
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取属性oldTransId的值
	 * @return oldTransId属性值
	 */
	public String getOldTransId() {
		return oldTransId;
	}

	/**
	 * 设置属性oldTransId的值
	 * @param  oldTransId属性值
	 */
	public void setOldTransId(String oldTransId) {
		this.oldTransId = oldTransId;
	}

	/**
	 * 获取属性plaCustId的值
	 * @return plaCustId属性值
	 */
	public String getPlaCustId() {
		return plaCustId;
	}

	/**
	 * 设置属性plaCustId的值
	 * @param  plaCustId属性值
	 */
	public void setPlaCustId(String plaCustId) {
		this.plaCustId = plaCustId;
	}

	/**
	 * 获取属性transAmt的值
	 * @return transAmt属性值
	 */
	public String getTransAmt() {
		return transAmt;
	}

	/**
	 * 设置属性transAmt的值
	 * @param  transAmt属性值
	 */
	public void setTransAmt(String transAmt) {
		this.transAmt = transAmt;
	}

	/**
	 * 获取属性freezeId的值
	 * @return freezeId属性值
	 */
	public String getFreezeId() {
		return freezeId;
	}

	/**
	 * 设置属性freezeId的值
	 * @param  freezeId属性值
	 */
	public void setFreezeId(String freezeId) {
		this.freezeId = freezeId;
	}

	/**
	 * 获取属性requestParamNames的值
	 * @return requestParamNames属性值
	 */
	public String[] getRequestParamNames() {
		return requestParamNames;
	}

	/**
	 * 设置属性requestParamNames的值
	 * @param  requestParamNames属性值
	 */
	public void setRequestParamNames(String[] requestParamNames) {
		this.requestParamNames = requestParamNames;
	}
	
	
	
}
