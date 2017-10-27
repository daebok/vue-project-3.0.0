package com.rd.ifaes.core.sys.domain;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;

/**
 * entity:User
 * 
 * @author lh
 * @version 3.0
 * @date 2016-5-30
 */
public class Operator extends BaseEntity<Operator> {
	
	private static final long serialVersionUID = 1L;
	
	public static final String DEFAULT_USER_STATUS = "0";//正常
	public static final String USER_STATUS_LOCKED = "-1";//锁定	
	public static final String ROLE_EQ = "1";	
	public static final String ROLE_NOT_EQ = "0";
	/**
	 * 未重置密码
	 */
	public static final String PWD_FLAG_NO="0";
	/**
	 * 已重置密码
	 */
	public static final String PWD_FLAG_YES="1";
	@Length(min = 6, max = 20, message =  "{"+ResourceConstant.SYS_USER_LOGINNAME_LENGTHERROR+"}")
	private String	loginName;		 /* 用户登录名 */ 
	private String	pwd;		 /* 登陆密码 */ 
	@Length(min = 2, max = 10, message = "{"+ResourceConstant.SYS_USER_REALNAME_LENGTHERROR+"}")
	private String	realName;		 /* 姓名 */ 
	private String	empNo;		 /* 工号 */ 
	private String	orgId;		 /* 所属部门 */ 
	private String	telephone;		 /* 电话 */ 
	@javax.validation.constraints.Pattern(regexp = "^((13[0-9])|(15[0-9])|(18[0-9])|145|147|170|171|173|175|176|177|178)\\d{8}$", message="{user.mobile.error}")
	private String	mobile;		 /* 手机 */ 
	@Email(message = "{"+ResourceConstant.EMAIL_FORMAT_ERROR+"}")	
	private String	email;		 /* 邮箱 */ 
	private String	qq;		 /* QQ号码 */ 
	private Date	createTime;		 /* 添加时间 */ 
	private String	status;		 /* 用户状态:(-1 锁定,0 正常) */ 
	private String	deleteFlag;		 /* 0 未删除，1 已删除 */ 
	private String	remark;		 /* 备注 */ 
	private String  pwdFlag;    /*重置密码状态：0 未重置密码,1 已重置密码 */
	private Date lockTime;/*锁定时间*/
	
	//其他自定义属性
	private String orgName;	//组织机构名称
	private String roleId;//角色Id
	private String roleEq;//角色匹配：1 匹配角色；0 不在角色
	private String oldPwd;
	private String pwdSecond;
	private String [] roleIds;//角色ids
	private int isFirstLogin;/*是否是首次登录*/
	// Constructor
	public Operator() {
	}

	/**
	 * full Constructor
	 */
	public Operator(String uuid,String loginName, String pwd, String orgId, String telephone, String mobile, String email, String qq,
			String realName, String empNo, Date createTime, String status, String deleteFlag, String remark) {
		super(uuid);
		this.loginName = loginName;
		this.pwd = pwd;
		this.orgId = orgId;
		this.telephone = telephone;
		this.mobile = mobile;
		this.email = email;
		this.qq = qq;
		this.realName = realName;
		this.empNo = empNo;
		this.createTime = createTime;
		this.status = status;
		this.deleteFlag = deleteFlag;
		this.remark = remark;
	}


	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getEmpNo() {
		return empNo;
	}

	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
		this.status = StringUtils.isBlank(this.status)?DEFAULT_USER_STATUS:this.status;
	}

	@Override
	public String toString() {
		return "User ["+ "uuid=" + getUuid() + ", loginName=" + loginName + ", pwd=" + pwd + ", orgId=" + orgId + ", telephone=" + telephone
				+ ", mobile=" + mobile + ", email=" + email + ", qq=" + qq + ", realName=" + realName + ", empNo="
				+ empNo + ", createTime=" + createTime + ", status=" + status + ", deleteFlag=" + deleteFlag
				+ ", remark=" + remark + "]";
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleEq() {
		return roleEq;
	}

	public void setRoleEq(String roleEq) {
		this.roleEq = roleEq;
	}

	public String[] getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String[] roleIds) {
		this.roleIds = roleIds;
	}

	public String getOldPwd() {
		return oldPwd;
	}

	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}

	public String getPwdSecond() {
		return pwdSecond;
	}

	public void setPwdSecond(String pwdSecond) {
		this.pwdSecond = pwdSecond;
	}
	
	/**
	 * 校验密码
	 * 
	 * @param pwd
	 * @return
	 */
	public static boolean isPwd(String pwd) {
		pwd = StringUtils.isNull(pwd);
		Pattern regex = Pattern.compile("(?![^a-zA-Z]+$)(?!\\D+$)^(?!.*\\s).{8,16}");
		Matcher matcher = regex.matcher(pwd);
		boolean isMatched = matcher.matches();
		return isMatched;
	}

	/**
	 * 修改密码校验
	 * 
	 * @author ZhangBiao
	 * @date 2016年9月27日
	 */
	public void checkChangePwd() {
		// 两次密码输入不一致
		if(!getPwd().equals(getPwdSecond())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.OPERATOR_PWD_AGAIN_ERROR));
		}
		// 密码格式错误
		if(!isPwd(getPwd())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.OPERATOR_PWD_ERROR));
		}
	}
	/**
	 * 获得重置密码的状态
	 * @return
	 */
	public String getPwdFlag() {
		return pwdFlag;
	}
	/**
	 * 设置重置密码的状态
	 * @param pwdFlag
	 */
	public void setPwdFlag(String pwdFlag) {
		this.pwdFlag = pwdFlag;
	}
	/**
	 * 获得锁定时间
	 * @return
	 */
	public Date getLockTime() {
		return lockTime;
	}
	/**
	 * 设置锁定时间
	 * @param lockTime
	 */
	public void setLockTime(Date lockTime) {
		this.lockTime = lockTime;
	}
	/**
	 * 获得是否是首次登录状态
	 * @return
	 */
	public int getIsFirstLogin() {
		return isFirstLogin;
	}
	/**
	 * 设置首次登录状态
	 * @param isFirstLogin
	 */
	public void setIsFirstLogin(int isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}

	
}
