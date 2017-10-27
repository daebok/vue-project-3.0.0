package com.rd.ifaes.core.project.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.core.sys.service.AreaService;

/**
 * entity:BorrowBespeak
 * 
 * @author zb
 * @version 3.0
 * @date 2016-8-16
 */
public class BorrowBespeak extends BaseEntity<BorrowBespeak> {
	
	private static final long serialVersionUID = 1L;
	
	private String	contactName;		 /* 联系人 */ 
	private String	mobile;		 /* 联系电话 */ 
	private String	sex;		 /* 性别:M 男性，F女性 */ 
	private String	province;		 /* 省 */ 
	private String	city;		 /* 市 */ 
	private String	area;		 /* 区 */ 
	private BigDecimal	money;		 /* 借款金额 */ 
	private Integer	limitTime;		 /* 借款期限(天) */ 
	private String	status;		 /* 状态 0未处理 1 已回访 2不回访 */ 
	private Date	createTime;		 /* 添加时间 */ 
	private String	addIp;		 /* 添加IP */ 
	private Date	handleTime;		 /* 处理时间 */ 
	private String	remark;		 /* 备注 */ 
	private String address;		/*根据IP查出的省市存放在这个属性中*/

	//其他自定义属性
	

	// Constructor
	public BorrowBespeak() {
	}

	/**
	 * full Constructor
	 */
	public BorrowBespeak(String uuid, String contactName, String mobile, String sex, String province, String city, BigDecimal money, Integer limitTime, String status, Date createTime, String addIp, Date handleTime, String remark) {
		setUuid(uuid);
		this.contactName = contactName;
		this.mobile = mobile;
		this.sex = sex;
		this.province = province;
		this.city = city;
		this.money = money;
		this.limitTime = limitTime;
		this.status = status;
		this.createTime = createTime;
		this.addIp = addIp;
		this.handleTime = handleTime;
		this.remark = remark;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getProvince() {
		return province;
	}
	
	public String getProvinceStr() {
		String provinceStr = "";
		if(StringUtils.isNotBlank(getProvince())){
			AreaService areaService = SpringContextHolder.getBean("areaService");
			provinceStr = areaService.getNameByCode(getProvince());
		}
		return provinceStr;
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
	
	public String getCityStr() {
		String cityStr = "";
		if(StringUtils.isNotBlank(getCity())){
			AreaService areaService = SpringContextHolder.getBean("areaService");
			cityStr = areaService.getNameByCode(getCity());
		}
		return cityStr;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public Integer getLimitTime() {
		return limitTime;
	}

	public void setLimitTime(Integer limitTime) {
		this.limitTime = limitTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getAddIp() {
		return addIp;
	}

	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}

	public Date getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
	
	public String getAreaStr() {
		String areaStr = "";
		if(StringUtils.isNotBlank(getArea())){
			AreaService areaService = SpringContextHolder.getBean("areaService");
			areaStr = areaService.getNameByCode(getArea());
		}
		return areaStr;
	}

	@Override
	public String toString() {
		return "BorrowBespeak [" + "uuid=" + uuid + ", contactName=" + contactName + ", mobile=" + mobile + ", sex=" + sex + ", province=" + province + ", city=" + city + ", money=" + money + ", limitTime=" + limitTime + ", status=" + status + ", createTime=" + createTime + ", addIp=" + addIp + ", handleTime=" + handleTime + ", remark=" + remark +  "]";
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
