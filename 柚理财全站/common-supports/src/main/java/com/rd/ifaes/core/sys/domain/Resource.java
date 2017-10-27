package com.rd.ifaes.core.sys.domain;

import java.util.Locale;

import org.hibernate.validator.constraints.Length;

import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.resource.MessageResource;
import com.rd.ifaes.core.core.constant.ResourceConstant;

/**
 * entity:Resource
 * 
 * @author lh
 * @version 3.0
 * @date 2016-7-27
 */
public class Resource extends BaseEntity<Resource> {
	
	private static final long serialVersionUID = 1L;
	
	@Length(min=5, max=100, message="{"+ResourceConstant.RESOURCE_RESNAME_LENTHERROR+"}")
	private String	resName;		 /* 名称 */ 
	@Length(min=2, max=100, message="{"+ResourceConstant.RESOURCE_RESTEXT_LENTHERROR+"}")
	private String	resText;		 /* 值 */ 
	private String	resLanguage;	 /* 语言 */ 

	//其他自定义属性
	

	// Constructor
	public Resource() {
	}

	/**
	 * full Constructor
	 */
	public Resource(String uuid, String resName, String resText, String resLanguage) {
		setUuid(uuid);
		this.resName = resName;
		this.resText = resText;
		this.resLanguage = resLanguage;
	}

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	public String getResText() {
		return resText;
	}

	public void setResText(String resText) {
		this.resText = resText;
	}

	public String getResLanguage() {
		return resLanguage;
	}

	public void setResLanguage(String resLanguage) {
		this.resLanguage = resLanguage;
	}
	
	@Override
	public void preInsert() {
		super.preInsert();
		if(StringUtils.isBlank(resLanguage)){
			Locale locale = Locale.getDefault();
			this.resLanguage = locale.getLanguage() + MessageResource.DB_SPLIT_CODE + locale.getCountry();
		}
	}

	@Override
	public String toString() {
		return "Resource [" + "uuid=" + uuid + ", resName=" + resName + ", resText=" + resText + ", resLanguage=" + resLanguage +  "]";
	}
}
