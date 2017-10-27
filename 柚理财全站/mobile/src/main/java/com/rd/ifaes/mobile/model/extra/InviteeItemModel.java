/**
 * 被邀请人列表条目
 */
package com.rd.ifaes.mobile.model.extra;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yoseflin
 * 被邀请人列表条目
 */
public class InviteeItemModel {
/**
 *  邀请时间
 */ 
private Date	inviteTime;		 
/**
 *  邀请人手机号 
 */
private String inviteUserMobile; 
/**
 *  被邀请人手机号 
 */
private String inviteeUserMobile; 
/**  
 * 被邀请人注册时间 
 */
private Date   inviteeUserTime; 

public Date getInviteTime() {
	return inviteTime;
}
public void setInviteTime(Date inviteTime) {
	this.inviteTime = inviteTime;
}
public String getInviteUserMobile() {
	return inviteUserMobile;
}
public void setInviteUserMobile(String inviteUserMobile) {
	this.inviteUserMobile = inviteUserMobile;
}
public String getInviteeUserMobile() {
	return inviteeUserMobile;
}
public void setInviteeUserMobile(String inviteeUserMobile) {
	this.inviteeUserMobile = inviteeUserMobile;
}
public Date getInviteeUserTime() {
	return inviteeUserTime;
}
public void setInviteeUserTime(Date inviteeUserTime) {
	this.inviteeUserTime = inviteeUserTime;
}
public BigDecimal getAwardRedTotal() {
	return awardRedTotal;
}
public void setAwardRedTotal(BigDecimal awardRedTotal) {
	this.awardRedTotal = awardRedTotal;
}
public BigDecimal getAwardRateTotal() {
	return awardRateTotal;
}
public void setAwardRateTotal(BigDecimal awardRateTotal) {
	this.awardRateTotal = awardRateTotal;
}
/**
 *  红包奖励总额 
 */
private BigDecimal awardRedTotal;
/**  
 * 加息券总个数
 */
private BigDecimal awardRateTotal;

}
