package com.rd.ifaes.core.score.domain;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.core.sys.service.AreaService;

/**
 * entity:ScoreType 收货信息表
 * 
 * @author ywt
 * @version 3.0
 * @date 2017-2-17
 */
public class ReceivingInfo extends BaseEntity<ReceivingInfo>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 404364382273526458L;
	
	/**用户id*/
	private String userID;
	/**收货详细地址*/
	private String address;
	/**省*/
	private String province;
	/**市*/
	private String city;
	/**用户区*/
	private String area;
	/**是否默认收货地址，1-是 0-不是*/
	private String isDefult;
	/**手机号码*/
	private String mobile;
	/**收货人姓名*/
	private String name;
	/**邮政编码*/
	private String postalCode;
	/**创建日期*/
	private Date createTime;
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getIsDefult() {
		return isDefult;
	}
	public void setIsDefult(String isDefult) {
		this.isDefult = isDefult;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreate_time(Date createTime) {
		this.createTime = createTime;
	}
	
	/**
	 * 省显示值
	 * @return
	 */
	public String getProvinceStr() {
		String provinceStr = "";
		if(StringUtils.isNotBlank(getProvince())){
			final AreaService areaService = SpringContextHolder.getBean("areaService");
			provinceStr = areaService.getNameByCode(getProvince());
		}
		return provinceStr;
	}
	
	/**
	 * 市显示值
	 * @return
	 */
	public String getCityStr() {
		String cityStr = "";
		if(StringUtils.isNotBlank(getProvince())){
			final AreaService areaService = SpringContextHolder.getBean("areaService");
			cityStr = areaService.getNameByCode(getCity());
		}
		return cityStr;
	}

	/**
	 * 区显示值
	 * @return
	 */
	public String getAreaStr() {
		String areaStr = "";
		if(StringUtils.isNotBlank(getProvince())){
			final AreaService areaService = SpringContextHolder.getBean("areaService");
			areaStr = areaService.getNameByCode(getArea());
		}
		return areaStr;
	}

}
