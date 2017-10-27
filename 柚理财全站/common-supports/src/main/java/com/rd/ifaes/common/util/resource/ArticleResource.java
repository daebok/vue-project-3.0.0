/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.common.util.resource;

/**
 * 债权模块资源类
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年9月11日
 */
public class ArticleResource {
	private ArticleResource() {
	}
	/**
	 *栏目不存在
	 */
	public static final String ARTICLE_NOT_EXISTS = "article.message.objectNotExists";
	
	/**
	 *栏目标识不可为空
	 */
	public static final String ARTICLE_CODE_BLANK = "article.code.isNotBlank";
	/**
	 * 文章标题不能为空，并且不能超过{0}个字符！
	 */
	public static final String ARTICLE_TITLE_VALID ="article.title.valid";
	/**
	 * 文章简介不能为空，并且不能超过{0}个字符！
	 */
	public static final String ARTICLE_REMARK_VALID ="article.remark.valid";
	/**
	 * 文章链接不能为空，并且不能超过{0}个字符！
	 */
	public static final String ARTICLE_URL_VALID ="article.url.valid";
	
	/**
	 * 文章的url链接必须以http://或者https://开头
	 */
	public static final String ARTICLE_URL_PREFIX_ERROR ="article.url.prefix.error";

	/**
	 * 文章详情不能超过?个字符
	 */
	public static final String ARTICLE_CONTENT_OUTOFRANGE = "article.content.outofRange";
	
}
