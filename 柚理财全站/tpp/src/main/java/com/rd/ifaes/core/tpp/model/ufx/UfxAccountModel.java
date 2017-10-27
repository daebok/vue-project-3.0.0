package com.rd.ifaes.core.tpp.model.ufx;

/**
 * 用户第三方资金model
 * @author xhf
 * @version 3.0
 * @date 2016年7月28日 下午2:58:44
 */
public class UfxAccountModel {
	
	/**
	 * 账户总额
	 */
	private String totalMoney;
	
	/**
	 * 账户可用余额
	 */
	private String userMoney;
	
	/**
	 * 账户冻结金额
	 */
	private String noMoney;
	
    /**
     * 构造函数
     */
	public UfxAccountModel() {
		super();
	}

    /**
     * 构造函数
     */
	public UfxAccountModel(final String totalMoney, final String userMoney, final String noMoney) {
		super();
		this.totalMoney = totalMoney;
		this.userMoney = userMoney;
		this.noMoney = noMoney;
	}

    /**
     * 获得账户总额
     */
	public String getTotalMoney() {
		return totalMoney;
	}

    /**
     * 设置账户总额
     */
	public void setTotalMoney(final String totalMoney) {
		this.totalMoney = totalMoney;
	}

    /**
     * 获得可用余额
     */
	public String getUserMoney() {
		return userMoney;
	}

    /**
     * 设置可用余额
     */
	public void setUserMoney(final String userMoney) {
		this.userMoney = userMoney;
	}

    /**
     * 获得冻结金额
     */
	public String getNoMoney() {
		return noMoney;
	}

    /**
     * 设置冻结金额
     */
	public void setNoMoney(final String noMoney) {
		this.noMoney = noMoney;
	}

	
}
