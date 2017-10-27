package com.rd.ifaes.core.tpp.model.cbhb;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.resource.CbhbResource;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.tpp.exception.CbhbException;
import com.rd.ifaes.core.tpp.model.cbhb.data.UsrInfExt;

/**
 * 
 *  4.1	用户信息查询（后台方式）
 * @version 3.0
 * @author ZhangBiao
 * @date 2017年2月21日
 */
public class CbhbQueryUserInfModel extends CbhbBaseModel{
	
	private static final long serialVersionUID = -3639191443537894840L;
	/**
	 * Log
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CbhbQueryTransStatModel.class);
	
	/**
	 * 手机号	String(11)
	 */
	private String mobileNo;
	
	/**
	 * 证件类型	String(2)
	 */
	private String identType;
	
	/**
	 * 证件号码	String(50)
	 */
	private String identNo;
	
	/**
	 * 返回数据	String(256)
	 */
	private String respData;
	
	//附加参数
	private List<UsrInfExt> list ;
	
	/**
	 * 请求参数
	 */
	private String[] requestParamNames = new String[] {"char_set","partner_id","version_no","biz_type","sign_type","PlaCustId","MobileNo","mac","merchantCert"};

	/**
	 * 响应参数
	 */
	private String[] responseParamNames = new String[] {"partner_id","version_no","biz_type","sign_type","RespCode","RespDesc",
			"IdentType","IdentNo","PlaCustId","RespData","mac"};
	
	/**
	 * 构造器
	 */
	public CbhbQueryUserInfModel(){
		super();
		this.setBizType(CbhbConstant.BIZ_TYPE_QUERY_USER_INF);
	}
	
	/**
	 * 请求参数校验
	 */
	@Override
	public void requestColumnValid() {
		super.requestColumnValid();
		LOGGER.info("CbhbQueryUserInfModel valid...");
		if(StringUtils.isBlank(this.getMobileNo())  && StringUtils.isBlank(this.getPlaCustId())){
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_MOBILEORPLACUSTID_EMPTY),BussinessException.TYPE_JSON);
		}
	}
	
	
	/**
	 * 响应
	 */
	@Override
	public void response(Map<String, String> map) {
		super.response(map);
		this.setIdentType(StringUtils.isNull(map.get("IdentType")));
		this.setIdentNo(StringUtils.isNull(map.get("IdentNo")));
		this.setRespData(StringUtils.isNull(map.get("RespData")));
		this.handleRespData(this.getRespData());
	}

	/**
	 * 处理xml数据
	 * @param respData
	 */
	private void  handleRespData(String respData){
		String xmlString = CbhbHelper.base64Decode(respData);
		List<Map<String,Object>> list = CbhbHelper.xmlToMap(xmlString);
		if(CollectionUtils.isNotEmpty(list)){
			List<UsrInfExt> newList = new ArrayList<UsrInfExt>();
			for (int i = 0; i < list.size(); i++) {
				Map<String,Object> map= list.get(i);
				UsrInfExt uif = new UsrInfExt();
				uif.setCapCorg(StringUtils.isNull(map.get("cap_corg")));
				uif.setCapCrdNo(StringUtils.isNull(map.get("cap_crd_no")));
				uif.setMblNo(StringUtils.isNull(map.get("mbl_no")));
				try {
					uif.setUseName(URLDecoder.decode(StringUtils.isNull(map.get("usr_nm")),"GBK"));
				} catch (UnsupportedEncodingException e) {
					LOGGER.error("解码失败");;
				}
				newList.add(uif);
			}
			this.setList(newList);
		}
	}
	
	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getIdentType() {
		return identType;
	}

	public void setIdentType(String identType) {
		this.identType = identType;
	}

	public String getIdentNo() {
		return identNo;
	}

	public void setIdentNo(String identNo) {
		this.identNo = identNo;
	}

	public String getRespData() {
		return respData;
	}

	public void setRespData(String respData) {
		this.respData = respData;
	}

	public String[] getRequestParamNames() {
		return requestParamNames;
	}

	public void setRequestParamNames(String[] requestParamNames) {
		this.requestParamNames = requestParamNames;
	}

	public String[] getResponseParamNames() {
		return responseParamNames;
	}

	public void setResponseParamNames(String[] responseParamNames) {
		this.responseParamNames = responseParamNames;
	}

	/**
	 * 获取属性list的值
	 * @return list属性值
	 */
	public List<UsrInfExt> getList() {
		return list;
	}

	/**
	 * 设置属性list的值
	 * @param  list属性值
	 */
	public void setList(List<UsrInfExt> list) {
		this.list = list;
	}
}
