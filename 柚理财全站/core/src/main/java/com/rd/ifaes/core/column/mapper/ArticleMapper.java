package com.rd.ifaes.core.column.mapper;

import java.util.List;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.column.domain.Article;

/**
 * 
 * 文章
 * @version 3.0
 * @author wyw
 * @date 2016-8-6
 */
public interface ArticleMapper extends BaseMapper<Article> {
	
	int updateClicks(String uuid);
	
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