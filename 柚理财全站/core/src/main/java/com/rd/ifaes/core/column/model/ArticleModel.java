package com.rd.ifaes.core.column.model;

import org.springframework.beans.BeanUtils;

import com.rd.ifaes.core.column.domain.Article;

/**
 * 
 * 文章Model
 * @version 3.0
 * @author xhf
 * @date 2016年8月4日
 */
public class ArticleModel extends Article {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -1866017482053311442L;

	/**
	 * @param article
	 * @return
	 */
	public static ArticleModel instance(final Article article) {
		final ArticleModel articleModel = new ArticleModel();
		BeanUtils.copyProperties(article, articleModel);
		return articleModel;
	}

	/**
	 * 获取文章原型
	 */
	public Article prototype() {
		final Article article = new Article();
		BeanUtils.copyProperties(this, article);
		return article;
	}

}
