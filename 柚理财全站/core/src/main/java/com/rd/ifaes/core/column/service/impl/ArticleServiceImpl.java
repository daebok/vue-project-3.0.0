package com.rd.ifaes.core.column.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rd.ifaes.common.dict.DeleteFlagEnum;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.column.domain.Article;
import com.rd.ifaes.core.column.domain.Section;
import com.rd.ifaes.core.column.mapper.ArticleMapper;
import com.rd.ifaes.core.column.service.ArticleService;
import com.rd.ifaes.core.column.service.SectionService;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;

/**
 * ServiceImpl:ArticleServiceImpl
 * 
 * @author WengYaWen
 * @version 3.0
 * @date 2016-7-21
 */
@Service("articleService")
public class ArticleServiceImpl extends BaseServiceImpl<ArticleMapper, Article> implements ArticleService {
	/** 栏目业务处理 */
	@Resource
	private transient SectionService   sectionService;
	
	/**
	 * 文章添加
	 */
	@Override
	@Transactional(readOnly=false)
	public void insert(Article entity) {
		entity.preInsert();
		entity.validEntity();
		dao.insert(entity);
	}
	
	/**
	 * 文章修改
	 */
	@Override
	@Transactional(readOnly=false)
	public void update(Article entity) {
		entity.validEntity();
		entity.setPicPath(StringUtils.isNull(entity.getPicPath()));
		dao.update(entity);
	}
	/**
	 * 文章查询
	 */
	@Override
	@Transactional(readOnly=false)
	public Article get(String id) {
		Article article=dao.get(id);
		return article;
	}
	/**
	 * 根据栏目获取文章
	 */
	@Override
	public List<Article> findArticleBySection(final Section section,int pageSize) {
		final Article queryArticle = new Article();
		queryArticle.setDeleteFlag(DeleteFlagEnum.NO.getValue());
		queryArticle.setPage(new Page<Article>());
		if (section.getPicNumber() != null && section.getPicNumber() > 0) {
			queryArticle.setSize(section.getPicNumber());
		}
		if (pageSize>0) {
			queryArticle.getPage().setPageSize(pageSize);
		}
		queryArticle.setSectionCode(section.getSectionCode());
		queryArticle.getPage().setSort("sort");
		queryArticle.setStatus(Article.STATUS_PUBLISH);
		final List<Article> picPathList = dao.findList(queryArticle);
		for (final Article article : picPathList) {
			if(StringUtils.isNotBlank(article.getPicPath())){
			   article.setPicPath(ConfigUtils.getValue(ConfigConstant.IMAGE_SERVER_URL) + article.getPicPath());
			}
		}
		return picPathList;
	}

	/**
	 * 获取文章列表
	 */
	@Override
	public Page<Article> findPage(final Article entity) {
		entity.setDeleteFlag(DeleteFlagEnum.NO.getValue());
		entity.setPage(super.findPage(entity));
		final List<Article> list= entity.getPage().getRows();
		final String imageServerUrl = ConfigUtils.getValue(ConfigConstant.IMAGE_SERVER_URL);
		for(int i=0;i<list.size();i++){
			if(StringUtils.isNotBlank(list.get(i).getPicPath())){
				list.get(i).setPicPath(imageServerUrl+list.get(i).getPicPath());
			}
		}
		return entity.getPage();
	}
	/**
	 * 更新点击次数
	 */
	@Override
	public void updateClicks(final String uuid) {
		dao.updateClicks(uuid);	
	}

	@Override
	public List<Article> findArticleBySectionCode(String sectionCode) {
		return dao.findArticleBySectionCode(sectionCode);
	}

}