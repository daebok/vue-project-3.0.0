package com.rd.ifaes.core.tpp.model.ufx;

/**
 * 用户登录
 * @author xhf
 * @version 3.0
 * @date 2016年6月20日 下午3:22:44 
 */
@SuppressWarnings("serial")
public class UfxLoginModel extends UfxBaseModel {
	
	/**
	 * 展示页面类型
	 */
	private String pageType;
	
	/**
	 * 请求参数数组
	 */
	private String[] requestParamNames = new String[] { "service", "orderNo","ufxCustomerId", "businessWay",
			"userCustId","pageType","signInfo"};

	/**
	 * 空构造函数
	 */
	public UfxLoginModel(){
		super();
		this.setService("login");
	}
	
	/**  
	 * 获取pageType  
	 * @return pageType  
	 */
	public String getPageType() {
		return pageType;
	}

	/**  
	 * 设置pageType  
	 * @param pageType  
	 */
	public void setPageType(final String pageType) {
		this.pageType = pageType;
	}

	/**  
	 * 获取requestParamNames  
	 * @return requestParamNames  
	 */
	public String[] getRequestParamNames() {
		return requestParamNames;
	}

	/**  
	 * 设置requestParamNames  
	 * @param requestParamNames  
	 */
	public void setRequestParamNames(final String[] requestParamNames) {
		this.requestParamNames = requestParamNames;
	}
	
	

}
