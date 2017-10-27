package com.rd.ifaes.core.user.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.domain.UserCompanyInfo;
import com.rd.ifaes.core.user.service.UserCompanyInfoService;

/**
 * 个人资料-基本信息
 *
 * @author xhf
 * @version 3.0
 * @since 2016年6月6日
 */
public class UserCacheModel extends UserCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserCacheModel.class);

    private static final long serialVersionUID = 1L;

    /****************其它属性 start*****************/
    /**
     * 姓名
     **/
    private String realName;

    /**
     * 同步跳转页面
     **/
    private String returnUrl;

    /**
     * 三证合一用户
     **/
    private String threeCertificate;

    /**
     * 统一社会信用代码
     */
    private String creditCode;

    /**
     * 公司名称
     **/
    private String companyName;

    /**
     * 营业执照号
     **/
    private String bussinessCode;

    /**
     * 组织机构代码
     **/
    private String orgCode;

    /**
     * 法定代表
     */
    private String legalDelegate;

    /**
     * 法定代表证件号码
     */
    private String certNo;

    /****************其它属性 end*****************/
    /**
     * 江西银行需要
     */
    private String smsCode;
    /**
     * 江西银行需要
     */
    private String ip;
    /**
     * 江西银行需要
     */
    private String bankNo;

    /**
     * 账户用途
     * 00000-普通账户
     * 10000-红包账户（只能有一个）
     * 01000-手续费账户（只能有一个）
     * 00100-担保账户
     */
    private String acctUse;

    /**
     * 获取
     *
     * @return realName
     */
    public String getRealName() {
        return realName;
    }

    /**
     * 设置
     *
     * @param realName
     */
    public void setRealName(final String realName) {
        this.realName = realName;
    }

    /**
     * 获取同步跳转页面
     *
     * @return returnUrl
     */
    public String getReturnUrl() {
        return returnUrl;
    }

    /**
     * 设置同步跳转页面
     *
     * @param returnUrl
     */
    public void setReturnUrl(final String returnUrl) {
        this.returnUrl = returnUrl;
    }

    /**
     * 获取公司名称
     *
     * @return companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * 设置公司名称
     *
     * @param companyName
     */
    public void setCompanyName(final String companyName) {
        this.companyName = companyName;
    }

    /**
     * 获取营业执照号
     *
     * @return bussinessCode
     */
    public String getBussinessCode() {
        return bussinessCode;
    }

    /**
     * 设置营业执照号
     *
     * @param bussinessCode
     */
    public void setBussinessCode(final String bussinessCode) {
        this.bussinessCode = bussinessCode;
    }

    /**
     * 获取组织机构代码
     *
     * @return orgCode
     */
    public String getOrgCode() {
        return orgCode;
    }

    /**
     * 设置组织机构代码
     *
     * @param orgCode
     */
    public void setOrgCode(final String orgCode) {
        this.orgCode = orgCode;
    }

    /**
     * 获取法定代表
     *
     * @return legalDelegate
     */
    public String getLegalDelegate() {
        return legalDelegate;
    }

    /**
     * 设置法定代表
     *
     * @param legalDelegate
     */
    public void setLegalDelegate(String legalDelegate) {
        this.legalDelegate = legalDelegate;
    }

    /**
     * 获取法人证件号码
     *
     * @return certNo
     */
    public String getCertNo() {
        return certNo;
    }

    /**
     * 设置法人证件号码
     *
     * @param certNo
     */
    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    /**
     * 获取属性threeCertificate的值
     *
     * @return threeCertificate属性值
     */
    public String getThreeCertificate() {
        return threeCertificate;
    }

    /**
     * 设置属性threeCertificate的值
     *
     * @param threeCertificate属性值
     */
    public void setThreeCertificate(String threeCertificate) {
        this.threeCertificate = threeCertificate;
    }

    /**
     * 获取属性creditCode的值
     *
     * @return creditCode属性值
     */
    public String getCreditCode() {
        return creditCode;
    }

    /**
     * 设置属性creditCode的值
     *
     * @param creditCode属性值
     */
    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }

    /**
     * 校验企业开户
     *
     * @author xhf
     * @date 2016年8月31日
     */
    public void checkCompanyRegister() {
        //企业名称
        if (!StringUtils.isCompanyName(getCompanyName())) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.COMPANY_NAME_ERROR), BussinessException.TYPE_CLOSE);
        }
        if (Constant.THREE_CERTIFICATE_YES.equals(getThreeCertificate())) {//三证合一
            //统一社会信用代码
            if (!StringUtils.isIntAndChar(getCreditCode(), 18)) {
                throw new BussinessException(ResourceUtils.get(ResourceConstant.CREDIT_CODE_ERROR), BussinessException.TYPE_CLOSE);
            }
        } else {//非三证合一
            //营业执照号
            if (!StringUtils.isIntAndChar(getBussinessCode(), 15)) {
                throw new BussinessException(ResourceUtils.get(ResourceConstant.BUSSINESS_CODE_ERROR), BussinessException.TYPE_CLOSE);
            }
            //组织机构代码
            if (!StringUtils.isOrgCode(getOrgCode())) {
                throw new BussinessException(ResourceUtils.get(ResourceConstant.ORG_CODE_ERROR), BussinessException.TYPE_CLOSE);
            }
        }
        //企业法人
        if (!StringUtils.isChinese(this.getLegalDelegate())) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_REALNAME_ERROR), BussinessException.TYPE_CLOSE);
        }
        //法人身份证号
        if (!StringUtils.isCard(getCertNo())) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_IDNO_ERROR), BussinessException.TYPE_CLOSE);
        }
    }

    /**
     * 校验用户开户
     *
     * @author xhf
     * @date 2016年8月31日
     */
    public void checkUserRegister() {
        LOGGER.info("[model:实名的信息,姓名：{},身份证号码：{}]", this.getRealName(), this.getIdNo());
        if (!StringUtils.isChinese(this.getRealName())) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_REALNAME_ERROR), BussinessException.TYPE_CLOSE);
        }
        if (!StringUtils.isCard(super.getIdNo())) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_IDNO_ERROR), BussinessException.TYPE_CLOSE);
        }
    }

    /**
     * 校验开户状态
     */
    public void checkAuditStatus(String userId) {
        UserCompanyInfoService companyService = (UserCompanyInfoService) SpringContextHolder.getBean("userCompanyInfoService");

        final UserCompanyInfo userCompanyInfo = companyService.findByUserId(userId);
        if (UserCompanyInfo.AUDIT_STATUS_PROCESS.equals(userCompanyInfo.getAuditStatus())) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.COMPANY_TPP_REG_AUDITING),
                    BussinessException.TYPE_CLOSE);
        } else if (UserCompanyInfo.AUDIT_STATUS_SUCCESS.equals(userCompanyInfo.getAuditStatus())) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.COMPANY_TPP_REG_SUCCESS),
                    BussinessException.TYPE_CLOSE);
        }
    }

    /**
     * 校验企业注册代码参数是否重复
     */
    public void checkRegCode(String userId) {
        UserCompanyInfoService companyService = (UserCompanyInfoService) SpringContextHolder.getBean("userCompanyInfoService");
        UserCompanyInfo codeModel = new UserCompanyInfo();
        codeModel.setUserId(userId);

        if (Constant.THREE_CERTIFICATE_YES.equals(getThreeCertificate())) {//三证合一
            codeModel.setCreditCode(getCreditCode());
            int count = companyService.countRegisterCode(codeModel);
            if (count > 0) {
                throw new BussinessException(ResourceUtils.get(ResourceConstant.CREDIT_CODE_IS_USED), BussinessException.TYPE_CLOSE);
            }
        } else {
            //bussinessCode
            codeModel.setBussinessCode(getBussinessCode());
            int bussCodecount = companyService.countRegisterCode(codeModel);
            if (bussCodecount > 0) {
                throw new BussinessException(ResourceUtils.get(ResourceConstant.BUSSINESS_CODE_IS_USED), BussinessException.TYPE_CLOSE);
            }
            //orgCode
            codeModel.setBussinessCode(null);
            codeModel.setOrgCode(getOrgCode());
            int orgCodecount = companyService.countRegisterCode(codeModel);
            if (orgCodecount > 0) {
                throw new BussinessException(ResourceUtils.get(ResourceConstant.ORG_CODE_IS_USED), BussinessException.TYPE_CLOSE);
            }
        }


    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getAcctUse() {
        return acctUse;
    }

    public void setAcctUse(String acctUse) {
        this.acctUse = acctUse;
    }
}
