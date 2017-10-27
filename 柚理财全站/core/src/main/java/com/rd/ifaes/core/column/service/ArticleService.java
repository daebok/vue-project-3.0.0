package com.rd.ifaes.core.column.service;

import java.util.List;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.column.domain.Article;
import com.rd.ifaes.core.column.domain.Section;

/**
 * 
 *  文章业务接口
 * @version 3.0
 * @author wyw
 * @date 2016-8-6
 */
public interface ArticleService extends BaseService<Article>{

	/**
	 * 
	 * 根据栏目获取文章列表
	 * @author wyw
	 * @date 2016-8-6
	 * @param sectionCode
	 * @param size
	 * @return
	 */
	List<Article> findArticleBySection(Section  section,int pageSize);
	/**
	 * 
	 * 更新点击次数
	 * @author wyw
	 * @date 2016-9-9
	 */
	void updateClicks(String uuid);
	
	/**
	 * 
	 * 根据栏目获取文章列表
	 * @author ywt
	 * @date 2016-11-21
	 * @param sectionCode
	 * @return
	 */
	List<Article> findArticleBySectionCode(String  sectionCode);
	
	
}