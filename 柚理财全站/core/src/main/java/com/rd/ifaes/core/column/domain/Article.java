
package com.rd.ifaes.core.column.domain;

import java.util.Date;

import com.rd.ifaes.common.dict.DeleteFlagEnum;
import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.HtmlUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.resource.ArticleResource;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;

/**
 * entity:Article
 * 
 * @author WengYaWen
 * @version 3.0
 * @date 2016-7-21
 */
public class Article extends BaseEntity<Article> {

	private static final long serialVersionUID = 1L;
	/** 栏目标识 */
	private String sectionCode;
	/** 标题 */
	private String title;
	/** 排序 */
	private Integer sort;
	/** 是否推荐，0：否，1：是 */
	private String isRecommend;
	/** 是否置顶，0：否，1：是 */
	private String isTop;
	/** 内容 */
	private String content;
	/** 简介 */
	private String remark;
	/** 点击量 */
	private Integer clicks;
	/** 添加时间 */
	private Date createTime;
	/** 图片路径 */
	private String picPath;
	/** 链接 */
	private String url;
	/** 删除状态，0：显示，1：删除 */
	private String deleteFlag;
	/**状态，0:待审核，1：审核通过，2发布	 */
	private String status;
	// 其他自定义属性
	/** 栏目名称 */
	private String sectionName; /* 栏目名称 */
	/**  推荐 */
	public final static String RECOMMEND_YES = "1";
	/**  不推荐 */
	public final static String RECOMMEND_NO = "0"; 

	/**  置顶*/
	public final static String TOP_YES = "1";
	/**  不置顶*/
	public final static String TOP_NO = "0";
	/** 待审核*/
	public final static String STATUS_WAIT="0";
	/** 审核通过*/
	public final static String STATUS_PASS="1";
	/** 发布*/
	public final static String STATUS_PUBLISH="2";
	/**  显示张数*/
	private Integer size; 
	/**  下一篇uuid*/
	private String nextUuid;
	/**  下一篇标题名称*/
	private String nextTitle;
	/**  上一篇uuid*/
	private String previousUuid;
	/**  上一篇uuid*/
	private String previousTitle;
	/**  栏目uuid*/
	private String sectionUuid;
	/**  栏目uuid*/
	private String sectionUuids[];
	
	/**   Constructor*/
	public Article() {
		super();
	}
	/**   Constructor*/
	public Article(final String sectionCode, final int size) {
		super();
		this.deleteFlag = Constant.FLAG_NO;
		this.sectionCode = sectionCode;
		this.size = size;
	}
	/**   持久化之前做的操作*/
	@Override
	public void preInsert() {
		super.preInsert();
		this.deleteFlag = DeleteFlagEnum.NO.getValue();
		this.createTime = DateUtils.getNow();
		this.setIsRecommend(RECOMMEND_NO);
		this.setIsTop(TOP_NO);
		this.setClicks(0);
	}
	
	/**
	 * 参数校验
	 * @author QianPengZhan
	 * @date 2016年10月22日
	 */
	public void validEntity(){
		if(this.title.length() > Constant.ENTITY_LENGTH_ONE){
			throw new BussinessException(StringUtils.format(ResourceUtils.get(ArticleResource.ARTICLE_TITLE_VALID), Constant.ENTITY_LENGTH_ONE));
		}
		if(this.remark.length() > Constant.ENTITY_LENGTH_FIVE){
			throw new BussinessException(StringUtils.format(ResourceUtils.get(ArticleResource.ARTICLE_REMARK_VALID), Constant.ENTITY_LENGTH_FIVE));
		}
		if(this.url.length() > Constant.ENTITY_LENGTH_FOUR){
			throw new BussinessException(StringUtils.format(ResourceUtils.get(ArticleResource.ARTICLE_URL_VALID), Constant.ENTITY_LENGTH_FOUR));
		}
		if(StringUtils.isNotBlank(this.url) && !(this.url.startsWith("http://") || this.url.startsWith("https://")) ){
			throw new BussinessException(StringUtils.format(ResourceUtils.get(ArticleResource.ARTICLE_URL_PREFIX_ERROR), Constant.ENTITY_LENGTH_FOUR));
		}
		if (StringUtils.isBlank(this.content)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.ARTICLE_CONTENT_NOT_NULL));
		}
		int contentMaxLen = Constant.ATRCILE_CONTENT_MAX_LENGTH;
		if(HtmlUtils.filterHtmlTags(this.content).length() > contentMaxLen){
			throw new BussinessException(StringUtils.format(ResourceUtils.get(ArticleResource.ARTICLE_CONTENT_OUTOFRANGE), contentMaxLen));
		}
	}
	/**
	 * full Constructor
	 */
	public Article(final String uuid, final String sectionCode, final String title,final Integer sort,final  String isRecommend, 
			final String isTop,  final String  content,
			final String remark,final  Integer clicks, final Date createTime,final String picPath, final String url, final String deleteFlag) {
		super();
		setUuid(uuid);
		this.sectionCode = sectionCode;
		this.title = title;
		this.sort = sort;
		this.isRecommend = isRecommend;
		this.isTop = isTop;
		this.content = content;
		this.remark = remark;
		this.clicks = clicks;
		this.createTime = createTime;
		this.picPath = picPath;
		this.url = url;
		this.deleteFlag = deleteFlag;
	}
	/** 获取栏目标示*/
	public String getSectionCode() {
		return sectionCode;
	}
	/** 设置栏目标示*/
	public void setSectionCode(final String sectionCode) {
		this.sectionCode = sectionCode;
	}
	/** 获取文章标题*/
	public String getTitle() {
		return title;
	}
	/** 设置文章标题*/
	public void setTitle(final String title) {
		this.title = title;
	}
	/** 获取文章排序*/
	public Integer getSort() {
		return sort;
	}
	/** 设置文章排序*/
	public void setSort(final Integer sort) {
		this.sort = sort;
	}
	/** 获取文章是否推荐*/
	public String getIsRecommend() {
		return isRecommend;
	}
	/** 设置文章是否推荐*/
	public void setIsRecommend(final String isRecommend) {
		this.isRecommend = isRecommend;
	}
	/** 获取文章是否置顶*/
	public String getIsTop() {
		return isTop;
	}
	/** 设置文章是否置顶*/
	public void setIsTop(final String isTop) {
		this.isTop = isTop;
	}
	/** 获取文章内容*/
	public String getContent() {
		return content;
	}
	/** 设置文章内容*/
	public void setContent(final String content) {
		if (content != null) {
			this.content = HtmlUtils.htmlUnescape(content);
		}
	}
	/** 获取文章备注*/
	public String getRemark() {
		return remark;
	}
	/** 设置文章备注*/
	public void setRemark(final String remark) {
		this.remark = remark;
	}
	/** 获取文章点击次数*/
	public Integer getClicks() {
		return clicks;
	}
	/** 设置文章点击次数*/
	public void setClicks(final Integer clicks) {
		this.clicks = clicks;
	}
	/** 获取文章创建时间*/
	public Date getCreateTime() {
		return createTime;
	}
	/** 设置文章创建时间*/
	public void setCreateTime(final Date createTime) {
		this.createTime = createTime;
	}
	/** 获取文章图片地址*/
	public String getPicPath() {
		return picPath;
	}
	/** 设置文章图片地址*/
	public void setPicPath(final String picPath) {
		this.picPath = picPath;
	}
	/** 获取文章链接*/
	public String getUrl() {
		return url;
	}
	/** 设置文章链接*/
	public void setUrl(final String url) {
		this.url = url;
	}
	/** 获取删除标示*/
	public String getDeleteFlag() {
		return deleteFlag;
	}
	/** 设置删除标示*/
	public void setDeleteFlag(final String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	/** 获取显示张数*/
	public Integer getSize() {
		return size;
	}
	/** 设置显示张数*/
	public void setSize(final Integer size) {
		this.size = size;
	}
	/** 获取栏目名称*/
	public String getSectionName() {
		return sectionName;
	}
	/** 设置栏目名称*/
	public void setSectionName(final String sectionName) {
		this.sectionName = sectionName;
	}
	 
	/** 获取下一篇UUID*/
	public String getNextUuid() {
		return nextUuid;
	}
	/** 设置下一篇UUID*/
	public void setNextUuid(String nextUuid) {
		this.nextUuid = nextUuid;
	}
	/** 获取下一篇名称*/
	public String getNextTitle() {
		return nextTitle;
	}
	/** 设置下一篇名称*/
	public void setNextTitle(String nextTitle) {
		this.nextTitle = nextTitle;
	}
	/** 获取上一篇uuid*/
	public String getPreviousUuid() {
		return previousUuid;
	}
	/** 设置上一篇uuid*/
	public void setPreviousUuid(String previousUuid) {
		this.previousUuid = previousUuid;
	}
	/** 获取上一篇名称*/
	public String getPreviousTitle() {
		return previousTitle;
	}
	/** 设置上一篇名称*/
	public void setPreviousTitle(String previousTitle) {
		this.previousTitle = previousTitle;
	}
	/** 获取栏目id*/
	public String[] getSectionUuids() {
		return sectionUuids;
	}
	/** 设置栏目id*/
	public void setSectionUuids(String[] sectionUuids) {
		this.sectionUuids = sectionUuids;
	}
	public String getSectionUuid() {
		return sectionUuid;
	}
	public void setSectionUuid(String sectionUuid) {
		this.sectionUuid = sectionUuid;
	}
	/** toString*/
	@Override
	public String toString() {
		return "Article [" + "uuid=" + uuid + ", sectionCode=" + sectionCode + ", title=" + title + ", sort=" + sort
				+ ", isRecommend=" + isRecommend + ", isTop=" + isTop + ", content=" + content + ", remark=" + remark
				+ ", clicks=" + clicks + ", createTime=" + createTime + ", picPath=" + picPath + ", url=" + url + ", deleteFlag="
				+ deleteFlag + "]";
	}
	/**
	 * 获得文章状态
	 * @return
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * 设置文章状态
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

}
