package com.rd.ifaes.core.protocol.util.invest;



/**
 * 投资人下载协议实现类
 * 
 */
public class InvestProtocol extends AbstractProtocolBean {

	@Override
	public void validDownload() {
		// 先调用父类基础校验
		super.validDownload();
		if (investId == null) {
//			throw new ProductException("你不是投资人不能下载该协议！");
		}
	}

	@Override
	public void prepare() {
	}

}
