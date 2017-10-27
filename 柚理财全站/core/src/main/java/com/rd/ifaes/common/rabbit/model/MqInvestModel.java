package com.rd.ifaes.common.rabbit.model;

import com.rd.ifaes.core.bond.model.BondInvestOtherModel;
import com.rd.ifaes.core.protocol.util.bond.BondProtocol;
import com.rd.ifaes.core.protocol.util.invest.InvestProtocol;
import com.rd.ifaes.core.tpp.model.jx.JxBidApplyModel;
import com.rd.ifaes.core.tpp.model.jx.JxCreditInvestModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxCreditTransferModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxInvestFailModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxInvestModel;

/**
 * 投资
 * @version 3.0
 * @author lh
 * @date 2016年8月5日
 */
public class MqInvestModel extends MqBaseModel {
	
	/**
	 * 投资
	 */
	private UfxInvestModel investModel;
	/**
	 * 自动投资
	 */
	private MqAutoInvestModel autoInvestModel;
	/**
	 * 投资失败
	 */
	private UfxInvestFailModel investFailModel;
	/**
	 * 债权转让
	 */
	private UfxCreditTransferModel creditTransferModel;
	
	/**
	 * 借款协议
	 */
	private InvestProtocol investProtocol;
	/**
	 * 债权协议
	 */
	private BondProtocol bondProtocol;
	/**
	 * 债权异步业务类
	 */
	private BondInvestOtherModel bondInvestOtherModel;

	private JxBidApplyModel jxBidApplyModel;
	private JxCreditInvestModel jxCreditInvestModel;

	private String projectId;

	/**
	 * 无参构造方法
	 */
	public MqInvestModel() {
		super();
	}
	
	/**
	 * 
	 * @param operate
	 */
	public MqInvestModel(String operate) {
		this.operate = operate;
	}

	public MqAutoInvestModel getAutoInvestModel() {
		return autoInvestModel;
	}

	public void setAutoInvestModel(MqAutoInvestModel autoInvestModel) {
		this.autoInvestModel = autoInvestModel;
	}

	public UfxInvestModel getInvestModel() {
		return investModel;
	}

	public void setInvestModel(UfxInvestModel investModel) {
		this.investModel = investModel;
	}

	public UfxInvestFailModel getInvestFailModel() {
		return investFailModel;
	}

	public void setInvestFailModel(UfxInvestFailModel investFailModel) {
		this.investFailModel = investFailModel;
	}

	public UfxCreditTransferModel getCreditTransferModel() {
		return creditTransferModel;
	}

	public void setCreditTransferModel(UfxCreditTransferModel creditTransferModel) {
		this.creditTransferModel = creditTransferModel;
	}
	
	/**
	 * 获取属性bondProtocol的值
	 * @return bondProtocol属性值
	 */
	public BondProtocol getBondProtocol() {
		return bondProtocol;
	}

	/**
	 * 设置属性bondProtocol的值
	 * @param  bondProtocol属性值
	 */
	public void setBondProtocol(BondProtocol bondProtocol) {
		this.bondProtocol = bondProtocol;
	}
	
	
	
	/**
	 * 获取属性investProtocol的值
	 * @return investProtocol属性值
	 */
	public InvestProtocol getInvestProtocol() {
		return investProtocol;
	}

	/**
	 * 设置属性investProtocol的值
	 * @param  investProtocol属性值
	 */
	public void setInvestProtocol(InvestProtocol investProtocol) {
		this.investProtocol = investProtocol;
	}

	/**
	 * 获取属性bondInvestOtherModel的值
	 * @return bondInvestOtherModel属性值
	 */
	public BondInvestOtherModel getBondInvestOtherModel() {
		return bondInvestOtherModel;
	}

	/**
	 * 设置属性bondInvestOtherModel的值
	 * @param  bondInvestOtherModel属性值
	 */
	public void setBondInvestOtherModel(BondInvestOtherModel bondInvestOtherModel) {
		this.bondInvestOtherModel = bondInvestOtherModel;
	}

//	@Override
//	public String toString() {
//		return "MqInvestModel [operate="+operate+", investModel=" + investModel + ", autoInvestModel=" + autoInvestModel
//				+ ", investFailModel=" + investFailModel + ", creditTransferModel=" + creditTransferModel +
//				",bondProtocol="+bondProtocol+", bondInvestOtherModel=" + bondInvestOtherModel +"]";
//	}

	public JxBidApplyModel getJxBidApplyModel() {
		return jxBidApplyModel;
	}

	public void setJxBidApplyModel(JxBidApplyModel jxBidApplyModel) {
		this.jxBidApplyModel = jxBidApplyModel;
	}

	public JxCreditInvestModel getJxCreditInvestModel() {
		return jxCreditInvestModel;
	}

	public void setJxCreditInvestModel(JxCreditInvestModel jxCreditInvestModel) {
		this.jxCreditInvestModel = jxCreditInvestModel;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
}
