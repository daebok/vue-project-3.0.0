package com.rd.ifaes.core.user.model;

import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.core.util.ValidateUtils;
import com.rd.ifaes.core.user.domain.UserOpinion;


/**
 * entity:UserOpinion
 * 用户意见反馈记录数据对象
 * @author xxb
 * @version 3.0
 * @date 2016-10-10
 */
public class UserOpinionModel extends UserOpinion{

	private static final long serialVersionUID = -5759491548090948827L;
	
	/**
	 * 图形验证码
	 */
	private String imgValidCode;

	public String getImgValidCode() {
		return imgValidCode;
	}

	public void setImgValidCode(String imgValidCode) {
		this.imgValidCode = imgValidCode;
	}
	
	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public void checkImgValidCode(){
		final String imgValidCode = this.getImgValidCode();
		
		if (StringUtils.isBlank(imgValidCode)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.VALID_CODE_NOTEMPTY),
					BussinessException.TYPE_JSON);
		}
		
		if (!ValidateUtils.checkValidCode(imgValidCode)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.VALID_CODE_ERROR),
					BussinessException.TYPE_JSON);
		}
	}
	
}
