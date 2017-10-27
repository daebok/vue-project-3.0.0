package com.rd.ifaes.web.controller.column;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.dict.DeleteFlagEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.resource.ArticleResource;
import com.rd.ifaes.core.column.domain.Article;
import com.rd.ifaes.core.column.domain.Section;
import com.rd.ifaes.core.column.service.ArticleService;
import com.rd.ifaes.core.column.service.SectionService;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.web.controller.WebController;

/**
 * 
 *  文章 栏目 
 * @version 3.0
 * @author wyw
 * @date 2016-8-9
 */
@Controller
public class ArticleController extends WebController{
	/**
	 * ARTICLE_LIST
	 */
	private static final String ARTICLE_LIST = "articleList";
	/**
	 * IS_LEAF
	 */
	private static final String IS_LEAF = "isLeaf";
	/**
	 * SECTION_NAME
	 */
	private static final String SECTION_NAME = "sectionName";
	/**
	 * SECTION_CODE
	 */
	private static final String SECTION_CODE = "sectionCode";
	/**
	 * QUERY_ARTICLE
	 */
	private static final String  QUERY_ARTICLE= "queryArticle";
	/**
	 * SECTIONLIST
	 */
	private static final String    SECTIONLIST =  "sectionList";
	/**
	 * CHILDSIZE
	 */
	private static final String  CHILDSIZE  =  "childSize";
	
	/**
	 * 栏目业务处理
	 */
	@Resource
	private transient  SectionService   sectionService;
	
	/**
	 * 文章业务处理
	 */
	@Resource
	private transient  ArticleService   articleService;
	
	/**
	 * 校验section
	 * @author QianPengZhan
	 * @date 2016年9月13日
	 * @param model
	 */
	private Section validSection(final Section model){
		// 根据code获取Section对象
		final Section section = sectionService.getByCode(model.getSectionCode());
		if (section == null) {
			throw new BussinessException(ResourceUtils.get(ArticleResource.ARTICLE_NOT_EXISTS), BussinessException.TYPE_JSON);
		}
		return section;
	}
	
	
	private  Map<String,Object> getInfo(final Section section){
		// 栏目是否有子栏目 有的话获取子栏目 没有的话 取同级栏目
		final Section queryParent = new Section();
		final Article queryArticle = new Article();
		queryParent.setParentId(section.getUuid());
		queryParent.setDeleteFlag(DeleteFlagEnum.NO.getValue());
		final int childSize = sectionService.getCount(queryParent);
		if (childSize <= Constant.INT_ZERO) {
			queryParent.setParentId(section.getParentId());
		} else {
			queryParent.setParentId(section.getUuid());
		}
		queryParent.setDeleteFlag(DeleteFlagEnum.NO.getValue());
		List<Section> sectionList = sectionService.findList(queryParent);
		final Map<String, Object> map =   this.renderSuccessResult();
		map.put(CHILDSIZE, childSize);
		map.put(SECTIONLIST, sectionList);
		map.put(QUERY_ARTICLE, queryArticle);
		return map;
	}
	
	/**
	 * 
	 * 帮助中心
	 * @author wyw
	 * @date 2016-8-9
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/column/helpCenter")
	public String helpCenter(final Section sect,final Model model) {
		 model.addAttribute(SECTION_CODE, sect.getSectionCode());
		final Section section = validSection(sect);
		Map<String,Object> infoMap = getInfo(section);
		final Article queryArticle = (Article)infoMap.get(QUERY_ARTICLE);
		final int childSize = (int)infoMap.get(CHILDSIZE);
		final List<Section> sectionList = (List<Section>)infoMap.get(SECTIONLIST);
		// 获取该栏目下文章
		queryArticle.setSectionCode(section.getSectionCode());
		queryArticle.setDeleteFlag(DeleteFlagEnum.NO.getValue());
		Page<Article> articleList = articleService.findPage(queryArticle);
		// 如果本栏目文章为空 取第一栏目下的内容

		if (articleList.getRows().size() <= 0 && childSize > 0) {
			queryArticle.setSectionCode(sectionList.get(0).getSectionCode());
			 model.addAttribute(SECTION_CODE, sectionList.get(0).getSectionCode());
			 model.addAttribute(SECTION_NAME, sectionList.get(0).getSectionName());
			articleList = articleService.findPage(queryArticle);
		}else{
			 model.addAttribute(SECTION_CODE, section.getSectionCode());
			 model.addAttribute(SECTION_NAME, section.getSectionName());	
		}
		String isLeaf = "0";
		if (articleList.getRows().size() == Constant.INT_ONE) {
			isLeaf = "1";
			 model.addAttribute("article", articleList.getRows().get(0));
		}
		// 返回参数
		 model.addAttribute(IS_LEAF, isLeaf);
		 model.addAttribute(SECTIONLIST, sectionList);
		 model.addAttribute(ARTICLE_LIST, articleList.getRows());		
		return "/column/helpCenter";
	}
	/**
	 * 
	 * 合作伙伴
	 * @author wyw
	 * @date 2016-8-9
	 * @return
	 */
	@RequestMapping(value = "/column/partner")
	public String partner(){
		return "/column/partner";
	}
	/**
	 * 
	 * 合作伙伴数据
	 * @author wyw
	 * @date 2016-8-9
	 * @param sectionCode
	 * @return
	 */
	@RequestMapping(value = "/column/getPartner")
	@ResponseBody
	public Object getPartner(final String sectionCode){
		//根据code获取Section对象
		final Section section = sectionService.getByCode(sectionCode);
		if(section==null){
			 throw new BussinessException(ResourceUtils.get(ArticleResource.ARTICLE_NOT_EXISTS), BussinessException.TYPE_JSON);
		 }
		final Article queryArticle = new Article();
		queryArticle.setSectionCode(sectionCode);
		final Map<String, Object> data =   this.renderSuccessResult();
		data.put("partners", articleService.findAll());
		data.put("imageServerUrl", ConfigUtils.getValue(ConfigConstant.IMAGE_SERVER_URL));
		return data;
	}
	/**
	 * 
	 * 栏目
	 * @author wyw
	 * @date 2016-8-9
	 * @return
	 */
	@RequestMapping(value = "/column/sectionDetail")
	public String sectionDetail(final String sectionCode,final String uuid,final Model model) {
		 model.addAttribute(SECTION_CODE,StringUtils.isBlank(sectionCode) ? "aboutUs" : sectionCode);
		 model.addAttribute("uuid", uuid);
		return "/column/sectionDetail";
	}
	/**
	 * 
	 * 获取子栏目数据接口
	 * @author wyw
	 * @date 2016-8-9
	 * @param sectionCode
	 * @return
	 */
	@RequestMapping(value = "/column/getChildSection")
	@ResponseBody
	public Object getChildSection(final String sectionCode){
		//根据code获取Section对象
		final Section section = sectionService.getByCode(sectionCode);
		if(ObjectUtils.isEmpty(section)){
			 throw new BussinessException(ResourceUtils.get(ArticleResource.ARTICLE_NOT_EXISTS), BussinessException.TYPE_JSON);
		 }
		final Section queryParent = new Section();
		queryParent.setParentId(section.getUuid());
		queryParent.setDeleteFlag(DeleteFlagEnum.NO.getValue());
		final int childSize=sectionService.getCount(queryParent);
		if(childSize<=0){
			queryParent.setParentId(section.getParentId());
		}else{
			queryParent.setParentId(section.getUuid());
		}
		List<Section> sectionList = sectionService.findList(queryParent);
		
		////发布后删除
		List<Section> sectionList2=new ArrayList<>();
		for(Section section2:sectionList){
			if (!"partner".equals(section2.getSectionCode()) && !"dataReport".equals(section2.getSectionCode())) {
				sectionList2.add(section2);
			}
		}
		final Map<String, Object> data =   this.renderSuccessResult();
		data.put(SECTIONLIST, sectionList2);
		return data;
	}
	
	/**
	 * 
	 * 获取文章列表
	 * @author wyw
	 * @date 2016-8-23
	 * @param sectionCode
	 * @return
	 */
	@RequestMapping(value = "/column/getArticleList")
	@ResponseBody
	public Object getArticleList(final Article queryArticle,final String sectionCode){
				//根据code获取Section对象
		final Section section = sectionService.getByCode(sectionCode);
		if(section==null){
			 throw new BussinessException(ResourceUtils.get(ArticleResource.ARTICLE_NOT_EXISTS), BussinessException.TYPE_JSON);
		 }
		queryArticle.setSectionCode(sectionCode);
		queryArticle.setStatus(Article.STATUS_PUBLISH);
		Page<Article> page=queryArticle.getPage();
		if (page==null) {
			page=new Page<Article>();
		}
		page.setOrder(Constant.DESC);
		queryArticle.setPage(page);
		final Page<Article> articleList = articleService.findPage(queryArticle);	
		String isLeaf = "0";
		final Map<String, Object> data =   this.renderSuccessResult();
		/*if(articleList.getRows().size()== Constant.INT_ONE){
			isLeaf = "1";
			articleList.getRows().get(0).setSectionName(section.getSectionName());
			data.put("articleInfo", articleList.getRows().get(0));
		}*/
		//返回参数
		data.put(IS_LEAF, isLeaf);
		if(articleList != null){
			if(articleList.getRows() != null){
				for(int i =0;i<articleList.getRows().size();i++){
					Article article = articleList.getRows().get(i);
					article.setSectionName(section.getSectionName());
					data.put(SECTION_NAME, section.getSectionName());
				}
			}
		}
		data.put(ARTICLE_LIST, articleList);
		return data;
	}
	/**
	 * 
	 * 栏目数据接口
	 * @author wyw
	 * @date 2016-8-9
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/column/sectionList")
	@ResponseBody
	public Object sectionList(final Section model){
		final Section section = validSection(model);
		Map<String,Object> infoMap = getInfo(section);
		final Article queryArticle = (Article)infoMap.get(QUERY_ARTICLE);
		final int childSize = (int)infoMap.get(CHILDSIZE);
		@SuppressWarnings("unchecked")
		final List<Section> sectionList = (List<Section>)infoMap.get(SECTIONLIST);
		//获取该栏目下文章
		queryArticle.setSectionCode(section.getSectionCode());
		queryArticle.setStatus(Article.STATUS_PUBLISH);
		Page<Article> articleList = articleService.findPage(queryArticle);	
		//如果本栏目文章为空 取第一栏目下的内容

		if(articleList.getRows().size()<=0&&childSize>0){
			queryArticle.setSectionCode(sectionList.get(0).getSectionCode());
			articleList = articleService.findPage(queryArticle);	
		}
		final Map<String, Object> data =   this.renderSuccessResult();
		String isLeaf = String.valueOf(Constant.INT_ZERO);;
		if(articleList.getRows().size()== Constant.INT_ONE){
			isLeaf = String.valueOf(Constant.INT_ONE);
			data.put("articleinfo", articleList.getRows().get(0));
		}
		//返回参数
		data.put(IS_LEAF, isLeaf);
		data.put(SECTIONLIST, sectionList);
		data.put(ARTICLE_LIST, articleList);
		return data;
	}
	/**
	 * 
	 * 文章详情页
	 * @author wyw
	 * @date 2016-8-9
	 * @return
	 */
	@RequestMapping(value = "/column/articleDetail")
	public String articleDetail(final String uuid,final String id,final Model model) {
		String sectionCode = StringUtils.EMPTY;
		final Article article = articleService.get(StringUtils.isBlank(uuid)?id: uuid );
		if( article!=null){
			sectionCode = article.getSectionCode();
		}
		 model.addAttribute(SECTION_CODE, sectionCode);
		 model.addAttribute("uuid", uuid);
		return "/column/articleDetail";
	}
	/**
	 * 
	 * 文章详情页数据
	 * @author wyw
	 * @date 2016-8-9
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/column/articleList")
	@ResponseBody
	public Object articleList(final Article model){
		//根据uuid获取文章
		final Article articleInfo = articleService.get(model.getUuid());
		Article queryArticle=new Article();
		queryArticle.setSectionCode(model.getSectionCode());
		queryArticle.setDeleteFlag(DeleteFlagEnum.NO.getValue());
		List<Article> articleList = articleService.findList(queryArticle);
		Article next = null;
		Article previous = null;
		for(int i=0;i<articleList.size();i++){
			if (articleInfo.getUuid().equals(articleList.get(i).getUuid())) {
				if (i>0) {
					previous=articleList.get(i-1);
				}
				if (i<articleList.size()-1) {
					next=articleList.get(i+1);
				}
				break;
			}
		}
		//更新点击次数 
		articleService.updateClicks(model.getUuid());
		if(articleInfo==null){
			 throw new BussinessException("文章不存在", BussinessException.TYPE_JSON);
		 }
		//根据nid获取Section对象
		final Section section = sectionService.getByCode(articleInfo.getSectionCode());
		final Section queryParent = new Section();
		queryParent.setParentId(section.getParentId());
		List<Section> sectionList =  sectionService.findList(queryParent);

		final Map<String, Object> data =   this.renderSuccessResult();
		if(next!=null){
			articleInfo.setNextUuid(next.getUuid());
			articleInfo.setNextTitle(next.getTitle());
		}
		if(previous!=null){
			articleInfo.setPreviousUuid(previous.getUuid());
			articleInfo.setPreviousTitle(previous.getTitle());
		}
		data.put(SECTIONLIST, sectionList);
		data.put("articleInfo", articleInfo);
		return data;
	}
	
	
	@RequestMapping(value = "/column/dataReportDetail")
	public String dataReportDetail(final Model model) {
		return "/column/dataReportDetail";
	}
	
	@RequestMapping(value = "/column/dataReport")
	public String dataReport(final Model model) {
		return "/column/dataReport";
	}
	
	@RequestMapping(value = "/column/aboutUs")
	public String aboutUs(final Model model) {
		return "/column/aboutUs";
	}
}
