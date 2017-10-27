package com.rd.ifaes.core.project.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;

import com.rd.ifaes.common.annotation.DictType;
import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.HtmlUtils;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ProjectConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.DictUtils;
import com.rd.ifaes.core.core.util.HclientFileUtil;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.core.util.ValidateUtils;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.service.UserCacheService;

/**
 * entity:Borrow
 * 
 * @author FangJun
 * @version 3.0
 * @date 2016-6-22
 */
public class Borrow extends ProjectEntity<Borrow> {

	private static final long serialVersionUID = 1L;
	private String projectId; /* project ID */

	@NotEmpty(message = "{"+ResourceConstant.BORROW_CONTENT_NOT_EMPTY+"}")
	private String content; /* 借款详情 */
	@NotEmpty(message = "{"+ResourceConstant.BORROW_BORROW_USE_NOT_EMPTY+"}")
	private String borrowUse; /* 借款用途 */
	private Integer period; /* 期数 */
	private BigDecimal repaymentAccount; /* 应还本金 */
	private BigDecimal repaymentYesAccount; /* 实还本金 */
	private BigDecimal repaymentYesInterest; /* 实还利息 */
	private String bondUseful; /* 是否可以债权转让 1 可以，0不可以 */
	private Integer bondMaxTurn; /* 债权转让最大次数 */
	@DictType(type="borrowNature")
	private String borrowNature; /* 借款性质: 1 个人借款 2 企业借款 */
	private String hasPawn; /* 是否有抵押( 1有抵押，0 无抵押） */


	// 其他自定义属性
	private String userName; // 用户名
	private String realName; // 认证名称
	private String borrower; // 借款方
	private String mobile;
	private BigDecimal repaymentInterest; // 待还利息总额
	private String validCode; // 图形验证码
	private String projectTypeName;//项目分类名称
	
	String[] picPath;//借款资料
	String[] mtgPath;//抵押物资料
	
	String[] statusSet;//状态集合
	
	String listTag;// 列表查询标识
	
	//投资笔数
	private Integer investCount;
	private String showing;//商品已上架标识（后台上架管理查询使用）
	
	private Date reviewTime ;//成立时间
	
	private BigDecimal costManage ;
	

	
	public static final String LIST_TAG_STOP = "stop";
	public static final String LIST_TAG_ESTABLISH_VERIFY = "establishVerify";
	
	// Constructor
	public Borrow() {
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		if(content!=null){
			this.content = HtmlUtils.htmlUnescape(content);
		}
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getBorrowUse() {
		return borrowUse;
	}

	public void setBorrowUse(String borrowUse) {
		this.borrowUse = borrowUse;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public String getBondUseful() {
		return bondUseful;
	}

	public void setBondUseful(String bondUseful) {
		this.bondUseful = bondUseful;
	}

	public Integer getBondMaxTurn() {
		return bondMaxTurn;
	}

	public void setBondMaxTurn(Integer bondMaxTurn) {
		this.bondMaxTurn = bondMaxTurn;
	}

	public String getBorrowNature() {
		return borrowNature;
	}

	public void setBorrowNature(String borrowNature) {
		this.borrowNature = borrowNature;
	}

	public String getHasPawn() {
		return hasPawn;
	}

	public void setHasPawn(String hasPawn) {
		this.hasPawn = hasPawn;
	}

	public BigDecimal getRepaymentAccount() {
		return repaymentAccount;
	}

	public void setRepaymentAccount(BigDecimal repaymentAccount) {
		this.repaymentAccount = repaymentAccount;
	}

	public BigDecimal getRepaymentYesAccount() {
		return repaymentYesAccount;
	}

	public void setRepaymentYesAccount(BigDecimal repaymentYesAccount) {
		this.repaymentYesAccount = repaymentYesAccount;
	}

	public BigDecimal getRepaymentYesInterest() {
		return repaymentYesInterest;
	}

	public void setRepaymentYesInterest(BigDecimal repaymentYesInterest) {
		this.repaymentYesInterest = repaymentYesInterest;
	}

	public Project prototype() {
		Project project = new Project();		
		BeanUtils.copyProperties(this, project);
		return project;
	}

	public static Borrow instance(Project project) {
		Borrow borrow = new Borrow();
		BeanUtils.copyProperties(project, borrow);
		return borrow;
	}
	
	/**
	 * 新增、修改借贷时，借贷的基础校验
	 */
	public void validBorrow(){
		if(StringUtils.isBlank(this.projectName)){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.BORROW_PROJECTNAME_NOTEMPTY));
		}
		int nameMaxLen = ProjectConstant.PROJECT_PROJECTNAME_MAX_LENGTH;
		if(this.projectName.length() > nameMaxLen){
			throw new BussinessException(StringUtils.format(ResourceUtils.get(ResourceConstant.BORROW_PROJECTNAME_OUTOFRANGE), nameMaxLen));
		}
		
		if(StringUtils.isBlank(this.content)){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.BORROW_CONTENT_NOT_EMPTY));
		}
		int contentMaxLen = ProjectConstant.PROJECT_CONTENT_MAX_LENGTH;
		if(HtmlUtils.filterHtmlTags(this.content).length() > contentMaxLen){
			throw new BussinessException(StringUtils.format(ResourceUtils.get(ResourceConstant.BORROW_CONTENT_OUTOFRANGE), contentMaxLen));
		}
		//借款期限不能为空
		if(this.timeLimit == null || this.timeLimit.intValue() == 0){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.BORROW_TIME_LIMIT_NOT_EMPTY));
		}
		
		//借款借款期限类型只能是天标月标
		if(!this.timeType.equals(LoanEnum.TIME_TYPE_DAY.getValue()) && !this.timeType.equals(LoanEnum.TIME_TYPE_MONTH.getValue())
		){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.BORROW_TIME_LIMIT_TYPE_ONLY_DAY_OR_MONTH));
		}
		
		// 借款用途是否存在
		if(StringUtils.isBlank(DictUtils.getItemName(DictTypeEnum.BORROW_USE.getValue(), String.valueOf(getBorrowUse())))){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.BORROW_BORROW_USE_ERROR));
		}
		// 还款方式是否存在
		if(StringUtils.isBlank(DictUtils.getItemName(DictTypeEnum.REPAY_STYLE.getValue(), String.valueOf(getRepayStyle())))){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.BORROW_REPAY_STYLE_ERROR));
		}
		UserCacheService userCacheService = SpringContextHolder.getBean("userCacheService");
		// 担保用户
		if(Constant.FLAG_YES.equals(getIsVouch())){
			UserCache userCache = userCacheService.findByUserId(getVouchId());
			if(userCache == null || !UserCache.USER_NATURE_VOUCH.equals(userCache.getUserNature())){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.BORROW_VOUCH_ERROR));
			}
		}else if(CbhbConstant.CBHB_TPP_NAME.equals(ConfigUtils.getTppName())){
			throw new BussinessException("请选择担保机构");
		}
		//用户类型校验
		UserCache userCache = userCacheService.findByUserId(userId);
		if(userCache == null || StringUtils.isBlank(userCache.getUserNature())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PRODUCT_BORROWER_USERNATURE_NOTCLEAR));
		}
		/**
		 * 借款资料判断
		 */
		final List<String> saveFilePathList = new ArrayList<>();
		final String imgServerUrl = ConfigUtils.getValue(ConfigConstant.IMAGE_SERVER_URL);
		if(picPath != null && picPath.length > 0){//借款资料			
			for(String path : picPath) {
				if(StringUtils.isBlank(path) || saveFilePathList.contains(path)){
					continue;
				}
				//图片不存在
				if (!HclientFileUtil.fileExists(imgServerUrl + path)) {
					continue;
				}
				saveFilePathList.add(path);
			}
		}		
		//借款资料不能为空
		if(saveFilePathList.isEmpty()){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.BORROW_BORROWDATA_NOT_EMPTY), BussinessException.TYPE_CLOSE);
		}
		this.borrowNature = userCache.getUserNature();
		
	}

	@Override
	public void preInsert() {
		this.borrowFlag = LoanEnum.BORROW_FLAG_BORROW.getValue();
		this.realizeFlag = LoanEnum.REALIZE_FLAG_NORMAL.getValue();
		this.saleStyle = LoanEnum.SALE_STYLE_MONEY.getValue();
		//不担保则将担保机构置空
		if(isVouch == null || CommonEnum.NO.eq(isVouch)){
			this.vouchId = null;
		}
		if(StringUtils.isNotBlank(this.vouchId)){
			this.vouchStatus = CommonEnum.NO.getValue();
		}
		if(StringUtils.isBlank(this.status)){
			if(StringUtils.isNotBlank(this.vouchId)){
				this.status = LoanEnum.STATUS_WAIT_VOUCH_VERIFY.getValue();
			}else{
				this.status = LoanEnum.STATUS_WAIT_PUBLISH.getValue();
			}
		}
		this.updateTime = DateUtils.getNow();
	}

	@Override
	public void preUpdate() {
		this.borrowFlag = LoanEnum.BORROW_FLAG_BORROW.getValue();
		//不担保则将担保机构置空
		if(isVouch == null || CommonEnum.NO.eq(isVouch)){
			this.vouchId = null;
		}
		if(StringUtils.isNotBlank(this.vouchId)){
			this.vouchStatus = CommonEnum.NO.getValue();
		}
		if(StringUtils.isBlank(this.status)){
			if(StringUtils.isNotBlank(this.vouchId)){
				this.status = LoanEnum.STATUS_WAIT_VOUCH_VERIFY.getValue();
			}else{
				this.status = LoanEnum.STATUS_WAIT_PUBLISH.getValue();
			}
		}
		this.updateTime = DateUtils.getNow();
	 }

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getBorrower() {
		return borrower;
	}

	public void setBorrower(String borrower) {
		this.borrower = borrower;
	}
	
	public String[] getPicPath() {
		return picPath;
	}

	public void setPicPath(String[] picPath) {
		this.picPath = picPath;
	}

	public String[] getMtgPath() {
		return mtgPath;
	}

	public void setMtgPath(String[] mtgPath) {
		this.mtgPath = mtgPath;
	}

	public String[] getStatusSet() {
		return statusSet;
	}

	public void setStatusSet(String[] statusSet) {
		this.statusSet = statusSet;
	}
	
	public String getListTag() {
		return listTag;
	}

	public void setListTag(String listTag) {
		this.listTag = listTag;
	}

	public Integer getInvestCount() {
		return investCount;
	}

	public void setInvestCount(Integer investCount) {
		this.investCount = investCount;
	}

	public BigDecimal getRepaymentInterest() {
		return repaymentInterest;
	}

	public void setRepaymentInterest(BigDecimal repaymentInterest) {
		this.repaymentInterest = repaymentInterest;
	}
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * 获取属性showing的值
	 * @return showing属性值
	 */
	public String getShowing() {
		return showing;
	}

	/**
	 * 设置属性showing的值
	 * @param  showing属性值
	 */
	public void setShowing(String showing) {
		this.showing = showing;
	}
	
	@Override
	public String toString() {
		return "Borrow [" + "projectId=" + projectId + ", content=" + content + ", borrowUse=" + borrowUse
				+ ", period=" + period + ", repaymentAccount=" + repaymentAccount
				+ ", repaymentYesAccount=" + repaymentYesAccount + ", repaymentYesInterest=" + repaymentYesInterest
				+ ", bondUseful=" + bondUseful + ", bondMaxTurn=" + bondMaxTurn + ", borrowNature=" + borrowNature
				  + "]";
	}

	public String getValidCode() {
		return validCode;
	}

	public void setValidCode(String validCode) {
		this.validCode = validCode;
	}

	/**
	 * 确认借款验证码校验
	 * 
	 * @author ZhangBiao
	 * @date 2016年9月27日
	 */
	public void validRegRule() {
		String vCode = StringUtils.isNull(getValidCode());
		if (vCode.isEmpty()) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.VALID_CODE_NOTEMPTY), BussinessException.TYPE_JSON);
		}
		if (!ValidateUtils.checkValidCode(vCode)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.VALID_CODE_ERROR), BussinessException.TYPE_JSON);
		}
	}

	/**
	 * 后台审核借款校验
	 * 
	 * @author ZhangBiao
	 * @date 2016年10月13日
	 */
	public void checkVerify() {
		// 备注非空
		if(StringUtils.isBlank(getVerifyRemark())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_VERIFY_REMARK_NOT_EMPTY));
		}
		// 备注长度
		if(getVerifyRemark().length() > Constant.INT_ONE_HUNDRED_TWENY_EIGHT){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_VERIFY_REMARK_LENGTH_ERROR));
		}
	}

	public String getProjectTypeName() {
		return projectTypeName;
	}

	public void setProjectTypeName(String projectTypeName) {
		this.projectTypeName = projectTypeName;
	}

	public Date getReviewTime() {
		return reviewTime;
	}

	public void setReviewTime(Date reviewTime) {
		this.reviewTime = reviewTime;
	}

	public BigDecimal getCostManage() {
		return costManage;
	}

	public void setCostManage(BigDecimal costManage) {
		this.costManage = costManage;
	}
	
	
}
