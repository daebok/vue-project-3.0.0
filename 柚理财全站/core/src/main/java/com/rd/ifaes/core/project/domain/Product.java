package com.rd.ifaes.core.project.domain;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringEscapeUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;

import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.HtmlUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.ProjectConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;

/**
 * entity:Product
 * 
 * @author FangJun
 * @version 3.0
 * @date 2016-6-22
 */
public class Product extends ProjectEntity<Product> {

	private static final long serialVersionUID = 1L;
	private String projectId; /* 借款基本信息ID */
	@NotEmpty(message = "{"+ResourceConstant.PRODUCT_CONTENT_NOT_EMPTY+"}")
	private String content; /* 借款标信息内容 */
	private Integer lockTimeLimit; /* 锁定期 */
	private Integer saleTimeLimit; /* 开放期 */
	private String realizeUseful; /* 是否可变现: 1 可变现 0 不可变现，默认 0 */
	private String buybackVerify; /* 赎回是否需要审核( 1 需要， 0 不需要,默认1 ) */
	private BigDecimal realizeLowestMoney; /* 最低变现金额 */
	private BigDecimal overdueRate; /*变现逾期罚息率 */
	private String autoRepay; /* 是否可自动还款: 1 自动还款 0 自动还款，默认 0 */

	// 其他自定义属性
     private String userName; // 用户名
     
     private String remark; //备注
     
     private String[] statusSet;//状态集合
     
     private String showing;//商品已上架标识（后台上架管理查询使用）
     
	// Constructor
	public Product() {
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
			this.content =StringEscapeUtils.unescapeHtml4(content);
		}
	}

	public Integer getLockTimeLimit() {
		return lockTimeLimit;
	}

	public void setLockTimeLimit(Integer lockTimeLimit) {
		this.lockTimeLimit = lockTimeLimit;
	}

	public Integer getSaleTimeLimit() {
		return saleTimeLimit;
	}

	public void setSaleTimeLimit(Integer saleTimeLimit) {
		this.saleTimeLimit = saleTimeLimit;
	}

	public String getRealizeUseful() {
		return realizeUseful;
	}

	public void setRealizeUseful(String realizeUseful) {
		this.realizeUseful = realizeUseful;
	}

	public String getBuybackVerify() {
		return buybackVerify;
	}

	public void setBuybackVerify(String buybackVerify) {
		this.buybackVerify = buybackVerify;
	}

	public BigDecimal getRealizeLowestMoney() {
		return realizeLowestMoney;
	}

	public void setRealizeLowestMoney(BigDecimal realizeLowestMoney) {
		this.realizeLowestMoney = realizeLowestMoney;
	}

	public BigDecimal getOverdueRate() {
		return overdueRate;
	}

	public void setOverdueRate(BigDecimal overdueRate) {
		this.overdueRate = overdueRate;
	}

	public String getAutoRepay() {
		return autoRepay;
	}

	public void setAutoRepay(String autoRepay) {
		this.autoRepay = autoRepay;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String[] getStatusSet() {
		return statusSet;
	}

	public void setStatusSet(String[] statusSet) {
		this.statusSet = statusSet;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Project prototype() {
		Project project = new Project();
		BeanUtils.copyProperties(this, project);
		return project;
	}

	public static Product instance(Project project) {
		Product product = new Product();
		BeanUtils.copyProperties(project, product);
		return product;
	}
	
	/**
	 * 新增、修改借贷时，借贷的基础校验
	 */
	public void validProduct(){
		if(StringUtils.isBlank(this.projectName)){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PRODUCT_PROJECTNAME_NOTEMPTY));
		}
		int nameMaxLen = ProjectConstant.PROJECT_PROJECTNAME_MAX_LENGTH;
		if(this.projectName.length() > nameMaxLen){
			throw new BussinessException(StringUtils.format(ResourceUtils.get(ResourceConstant.PRODUCT_PROJECTNAME_OUTOFRANGE), nameMaxLen));
		}
		
		int contentMaxLen = ProjectConstant.PROJECT_CONTENT_MAX_LENGTH;
		if(this.content != null && HtmlUtils.filterHtmlTags(this.content).length() > contentMaxLen){
			throw new BussinessException(StringUtils.format(ResourceUtils.get(ResourceConstant.PRODUCT_CONTENT_OUTOFRANGE), contentMaxLen));
		}
		//产品期限不能为空
		if(this.timeLimit == null || this.timeLimit.intValue() == 0){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PRODUCT_TIMELIMIT_NOTEMPTY));
		}
	}

	@Override
	public void preInsert() {
		this.borrowFlag = LoanEnum.BORROW_FLAG_PRODUCT.getValue();
		this.realizeFlag = LoanEnum.REALIZE_FLAG_NORMAL.getValue();
		this.status = LoanEnum.STATUS_WAIT_PUBLISH.getValue();
		this.updateTime = this.updateTime==null?DateUtils.getNow():this.updateTime;
	}

	@Override
	public void preUpdate() {
		this.borrowFlag = LoanEnum.BORROW_FLAG_PRODUCT.getValue();
		this.status = LoanEnum.STATUS_WAIT_PUBLISH.getValue();
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
		return "Product [" + "projectId=" + projectId + ", content=" + content + ", lockTimeLimit="
				+ lockTimeLimit + ", saleTimeLimit=" + saleTimeLimit + ", realizeUseful=" + realizeUseful
				+ ", buybackVerify=" + buybackVerify + ", realizeLowestMoney=" + realizeLowestMoney + ", overdueRate="
				+ overdueRate + ", autoRepay=" + autoRepay + "]";
	}
}
