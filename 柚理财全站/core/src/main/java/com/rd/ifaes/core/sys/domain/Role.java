package com.rd.ifaes.core.sys.domain;

import java.util.Date;

import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;

/**
 * entity:Role
 * 
 * @author lh
 * @version 3.0
 * @date 2016-5-12
 */
public class Role extends BaseEntity<Role> {
	
	private static final long serialVersionUID = 1L;
	
	private String	code;		 /* 角色标识，如admin/manager等 */ 
	private String	roleName;		 /* 角色名 */ 
	private Date	createTime;		 /* 添加时间 */ 
	private String	deleteFlag;		 /* 0 未删除，1 已删除 */ 
	private String	remark;		 /* 备注 */
	
	/************model start*****************/
	private String createUserName;
	/************model end*****************/
	/************标识常量 *****************/
	public static final String ONLINE_SERVER="onlineServer"; //在线客服
	
	// Constructor
	public Role() {
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public void preInsert() {
		super.preInsert();
		this.createTime = new Date();
		this.deleteFlag = StringUtils.isBlank(this.deleteFlag)?CommonEnum.NO.getValue():this.deleteFlag;
	}
	
	public void checkRole(){
		if(StringUtils.isBlank(this.getRoleName()) || this.getRoleName().length() > Constant.ENTITY_LENGTH_ZERO){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.ROLE_NAME_ERROR,  Constant.ENTITY_LENGTH_ZERO));
		}
	}
	
	@Override
	public String toString() {
		return "Role [uuid = " + uuid + ",code=" + code + ", roleName=" + roleName + ", createTime=" + createTime + ", deleteFlag="
				+ deleteFlag + ", remark=" + remark + ", createUserName=" + createUserName + "]";
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	
}
