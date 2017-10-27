package com.rd.ifaes.mobile.controller.column;


import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.dict.DeleteFlagEnum;
import com.rd.ifaes.common.dict.InvestEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.NumberUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.resource.ArticleResource;
import com.rd.ifaes.core.column.domain.Article;
import com.rd.ifaes.core.column.domain.Section;
import com.rd.ifaes.core.column.service.ArticleService;
import com.rd.ifaes.core.column.service.SectionService;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.service.UserEarnLogService;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.mobile.common.AppDateUtil;
import com.rd.ifaes.mobile.controller.WebController;
import com.rd.ifaes.mobile.model.PagedData;
import com.rd.ifaes.mobile.model.extra.AppArticleModel;

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
	@Resource
	private UserService userService;
	@Resource
	private ProjectInvestService projectInvestService;
	@Resource
	private UserEarnLogService userEarnLogService;
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
		final int childSize = sectionService.getCount(queryParent);
		if (childSize <= Constant.INT_ZERO) {
			queryParent.setParentId(section.getParentId());
		} else {
			queryParent.setParentId(section.getUuid());
		}
		List<Section> sectionList = sectionService.findList(queryParent);
		final Map<String,Object> map = new ConcurrentHashMap<>();
		map.put(CHILDSIZE, childSize);
		map.put(SECTIONLIST, sectionList);
		map.put(QUERY_ARTICLE, queryArticle);
		return map;
	}
	
	/**
	 * 移动端--帮助中心列表
	 */
	@RequestMapping(value = "/app/open/column/helpCenter")
	public Object getBanner(final Model model) {
		try {
			List<Section> sectionlist= sectionService.findChildSection("1770737057ec4074abdfecc1405513aa");
			sectionlist.remove(0);
			model.addAttribute("sectionlist", sectionlist);
			return "/app/column/help";
		} catch (Exception e) {
			model.addAttribute("r_msg", e.getMessage());
			return ConfigUtils.getTppName()+"/retresult";
		}
	}
	/**
	 * 移动端--帮助中心列表微信使用
	 */
	@RequestMapping(value = "/app/open/column/wxHelpCenter")
	@ResponseBody
	public Object wxHelpCenter(final Model model) {
		Object obj=null;
		final Map<String, Object> data = new HashMap<String, Object>();
		try {
			List<Section> sectionlist= sectionService.findChildSection("1770737057ec4074abdfecc1405513aa");
			sectionlist.remove(0);
			data.put("sectionlist", sectionlist);
			obj = createSuccessAppResponse(data);
		} catch (Exception e) {
			obj = dealException(e);
		}
		return obj;
	}
	/**
	 * 
	 * 移动端 -- 帮助中心
	 * @author lgx
	 * @date 2016-02-09
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/app/open/column/helpCenterDetail")
	public String helpCenterDetail(final Model modelm,final Section model){
		try{
		modelm.addAttribute(SECTION_CODE, model.getSectionCode());
		final Section section = validSection(model);
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
			modelm.addAttribute(SECTION_CODE, sectionList.get(0).getSectionCode());
			modelm.addAttribute(SECTION_NAME, sectionList.get(0).getSectionName());
			articleList = articleService.findPage(queryArticle);
		}else{
			modelm.addAttribute(SECTION_CODE, section.getSectionCode());
			modelm.addAttribute(SECTION_NAME, section.getSectionName());	
		}
		String isLeaf = "0";
		if (articleList.getRows().size() == Constant.INT_ONE) {
			isLeaf = "1";
			modelm.addAttribute("article", articleList.getRows().get(0));
		}
		// 返回参数
		modelm.addAttribute(IS_LEAF, isLeaf);
		modelm.addAttribute(SECTIONLIST, sectionList);
		modelm.addAttribute(ARTICLE_LIST, articleList.getRows());		
		return "/app/column/help_detail";
	} catch (Exception e) {
		modelm.addAttribute("r_msg", e.getMessage());
		return ConfigUtils.getTppName()+"/retresult";
	}
	}
	/**
	 * 
	 * 移动端 -- 帮助中心 微信使用
	 * @author lgx
	 * @date 2016-02-09
	 * @return
	 */
	@RequestMapping(value = "/app/open/column/wxHelpCenterDetail")
	@ResponseBody
	public Object wxHelpCenterDetail(final Model modelm,final Section model){
		Object obj=null;
		final Map<String, Object> data = new HashMap<String, Object>();
		try{
			data.put(SECTION_CODE, model.getSectionCode());
		final Section section = validSection(model);
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
			data.put(SECTION_CODE, sectionList.get(0).getSectionCode());
			data.put(SECTION_NAME, sectionList.get(0).getSectionName());
			articleList = articleService.findPage(queryArticle);
		}else{
			data.put(SECTION_CODE, section.getSectionCode());
			data.put(SECTION_NAME, section.getSectionName());	
		}
		String isLeaf = "0";
		if (articleList.getRows().size() == Constant.INT_ONE) {
			isLeaf = "1";
			data.put("article", articleList.getRows().get(0));
		}
		// 返回参数
		data.put(IS_LEAF, isLeaf);
		data.put(SECTIONLIST, sectionList);
		data.put(ARTICLE_LIST, articleList.getRows());		
		obj = createSuccessAppResponse(data);
	} catch (Exception e) {
		obj = dealException(e);
	}
	return obj;
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
		final Map<String, Object> data = new ConcurrentHashMap<>();
		data.put("partners", articleService.findAll());
		data.put("imageServerUrl", ConfigUtils.getValue("image_server_url"));
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
		final int childSize=sectionService.getCount(queryParent);
		if(childSize<=0){
			queryParent.setParentId(section.getParentId());
		}else{
			queryParent.setParentId(section.getUuid());
		}
		List<Section> sectionList = sectionService.findList(queryParent);
		final Map<String, Object> data= new ConcurrentHashMap<>();
		data.put(SECTIONLIST, sectionList);
		return data;
	}

	/**
	 * 移动端--关于我们
	 * @authorn lgx
	 * @date 2017年02月09日
	 * @return
	 */
	@RequestMapping(value = "/app/open/column/aboutUs")
	public Object aboutUs(HttpServletRequest request,final Model model) {
		try{
			model.addAttribute("mobileUrl", ConfigUtils.getValue("mobile_url"));
		return "/app/column/about_us";
		} catch (Exception e) {
			model.addAttribute("r_msg", e.getMessage());
			return ConfigUtils.getTppName()+"/retresult";
		}
	}
	/**
	 * 移动端--平台简介--融都介绍
	 * @authorn lgx
	 * @date 2017年02月09日
	 * @return
	 */
	@RequestMapping(value = "/app/open/column/getArticleListcolumn")
	public Object getArticleListcolumn(final Model model){
		final Map<String, Object> data = new HashMap<String, Object>();
		try{
			String sectionCode="platformInfo";
				//根据code获取Section对象
		final Section section = sectionService.getByCode(sectionCode);
		if(section==null){
			 throw new BussinessException(ResourceUtils.get(ArticleResource.ARTICLE_NOT_EXISTS), BussinessException.TYPE_JSON);
		 }
		Article queryArticle=new Article();
		Page<Article> pages=new Page<Article>(); 
		pages.setPage(1);
		pages.setOrder(Constant.DESC);
		queryArticle.setPage(pages);
		queryArticle.setSectionCode(sectionCode);
		final Page<Article> articleList = articleService.findPage(queryArticle);	
		if(articleList.getRows().size()>0){
		Article article=(Article) articleList.getRows().get(0);
		model.addAttribute("content", article.getContent());
		}else{
			model.addAttribute("content", "");
		}
		return "/app/column/company_introduce";
	} catch (Exception e) {
		model.addAttribute("r_msg", e.getMessage());
		return ConfigUtils.getTppName()+"/retresult";
	}
	}
	
	/**
	 * 移动端--客户服务  微信使用
	 * @authorn lgx
	 * @date 2017年02月09日
	 * @return
	 */
	@RequestMapping(value = "/app/open/column/getArticleListcolumnWx")
	@ResponseBody
	public Object getArticleListcolumnWx (HttpServletRequest request,final Model model) {
		Object obj=null;
		final Map<String, Object> data = new HashMap<String, Object>();
		try {
			String sectionCode="platformInfo";
			//根据code获取Section对象
	final Section section = sectionService.getByCode(sectionCode);
	if(section==null){
		 throw new BussinessException(ResourceUtils.get(ArticleResource.ARTICLE_NOT_EXISTS), BussinessException.TYPE_JSON);
	 }
	Article queryArticle=new Article();
	Page<Article> pages=new Page<Article>(); 
	pages.setPage(1);
	pages.setOrder(Constant.DESC);
	queryArticle.setPage(pages);
	queryArticle.setSectionCode(sectionCode);
	final Page<Article> articleList = articleService.findPage(queryArticle);	
	if(articleList.getRows().size()>0){
	Article article=(Article) articleList.getRows().get(0);
	data.put("content", article.getContent());
	}else{
		data.put("content", "");
	}
	obj = createSuccessAppResponse(data);
		} catch (Exception e) {
			obj = dealException(e);
		}
		return obj;
	}
	
	
	/**
	 * 移动端--安全保障
	 * @authorn lgx
	 * @date 2017年02月09日
	 * @return
	 */
	@RequestMapping(value = "/app/open/column/insurance")
	public Object insurance(HttpServletRequest request,final Model model) {
		try{
			model.addAttribute("mobileUrl", ConfigUtils.getValue("mobile_url"));
		return "/app/column/security";
		} catch (Exception e) {
			model.addAttribute("r_msg", e.getMessage());
			return ConfigUtils.getTppName()+"/retresult";
		}
	}
	
	/**
	 * 移动端--客户服务
	 * @authorn lgx
	 * @date 2017年02月09日
	 * @return
	 */
	@RequestMapping(value = "/app/open/column/customerService")
	public Object customerService (HttpServletRequest request,final Model model) {
		try{
			model.addAttribute("mobileUrl", ConfigUtils.getValue("mobile_url"));
			model.addAttribute("customerHotline", ConfigUtils.getValue("customer_hotline"));//客户服务热线
			model.addAttribute("webIndexSinaMicroblog", ConfigUtils.getValue("web_index_sina_microblog"));//平台新浪微博
			model.addAttribute("webIndexQq", ConfigUtils.getValue("web_index_qq"));//qq
			model.addAttribute("webIndexWechat", ConfigUtils.getValue("web_index_wechat"));//微信公众号
		return "/app/column/customer_service";
		} catch (Exception e) {
			model.addAttribute("r_msg", e.getMessage());
			return ConfigUtils.getTppName()+"/retresult";
		}
	}
	
	/**
	 * 移动端--客户服务  微信使用
	 * @authorn lgx
	 * @date 2017年02月09日
	 * @return
	 */
	@RequestMapping(value = "/app/open/column/customerServiceWx")
	@ResponseBody
	public Object customerServiceWx (HttpServletRequest request,final Model model) {
		Object obj=null;
		final Map<String, Object> data = new HashMap<String, Object>();
		try {
			data.put("mobileUrl", ConfigUtils.getValue("mobile_url"));
			data.put("customerHotline", ConfigUtils.getValue("customer_hotline"));//客户服务热线
			data.put("webIndexSinaMicroblog", ConfigUtils.getValue("web_index_sina_microblog"));//平台新浪微博
			data.put("webIndexQq", ConfigUtils.getValue("web_index_qq"));//qq
			data.put("webIndexWechat", ConfigUtils.getValue("web_index_wechat"));//微信公众号
			obj = createSuccessAppResponse(data);
		} catch (Exception e) {
			obj = dealException(e);
		}
		return obj;
	}
	
	
	/**
	 * 
	 * 获取文章列表
	 * @author lgx
	 * @date 2016-11-09
	 * @param sectionCode
	 * @return
	 */
	@RequestMapping(value = "/app/open/column/getArticleList")
	@ResponseBody
	public Object getArticleList(final Article queryArticle,final String sectionCode){
		Object obj=null;
		PagedData<AppArticleModel> articleLists=new PagedData<AppArticleModel>();
		try{
				//根据code获取Section对象
		final Section section = sectionService.getByCode(sectionCode);
		if(section==null){
			 throw new BussinessException(ResourceUtils.get(ArticleResource.ARTICLE_NOT_EXISTS), BussinessException.TYPE_JSON);
		 }
		queryArticle.setSectionCode(sectionCode);
		int page=queryArticle.getPage().getPage();
		final Page<Article> articleList = articleService.findPage(queryArticle);	
		articleList.setPage(page);
		 List<Article>  articleItemList=articleList.getRows();
		 fillPageData(articleLists, articleList);
		for(Article article:articleItemList){
			AppArticleModel model=new AppArticleModel();
			model.setClicks(article.getClicks());// 点击量
			model.setUuid(article.getUuid());// uuid
			model.setCreateTime(article.getCreateTime());//添加时间
			model.setPicPath(article.getPicPath());//图片路径
			model.setRemark(article.getRemark());// 简介
			model.setTitle(article.getTitle());//标题
			articleLists.getList().add(model);
		}
		obj=createSuccessAppResponse(articleLists);
			} catch (Exception e) {
				obj=dealException(e);
			}
		return obj;
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
		Page<Article> articleList = articleService.findPage(queryArticle);	
		//如果本栏目文章为空 取第一栏目下的内容

		if(articleList.getRows().size()<=0&&childSize>0){
			queryArticle.setSectionCode(sectionList.get(0).getSectionCode());
			articleList = articleService.findPage(queryArticle);	
		}
		final Map<String, Object> data = new ConcurrentHashMap<>();
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
	@RequestMapping(value = "/app/open/column/articleList")
	@ResponseBody
	public Object articleList(final Article model){
		Map<String, Object> data = new HashMap<String, Object>();
		try{
		//根据uuid获取文章
		final Article articleInfo = articleService.get(model.getUuid());
		//更新点击次数
		articleService.updateClicks(model.getUuid());
		if(articleInfo==null){
			 throw new BussinessException("文章不存在", BussinessException.TYPE_JSON);
		 }
		data.put("clicks", articleInfo.getClicks());// 点击量
		data.put("uuid", articleInfo.getUuid());//uuid
		data.put("content", articleInfo.getContent());//内容
		data.put("picPath", articleInfo.getPicPath());//图片路径
		data.put("remark", articleInfo.getRemark());// 简介
		data.put("title", articleInfo.getTitle());//标题
		data.put("createTime", articleInfo.getCreateTime());//添加时间
			} catch (Exception e) {
				dealException(e);
			}
			return createSuccessAppResponse(data);
	}
	
	/**
	 * 移动端--平台数据
	 * @authorn lgx
	 * @date 2017年02月13日
	 * @return
	 */
	@RequestMapping(value = "/app/open/column/siteData")
	public Object siteData(HttpServletRequest request,final Model model) {
		try{
			model.addAttribute("mobileUrl", ConfigUtils.getValue("mobile_url"));
			//投资总额
			ProjectInvest projectInvest = new ProjectInvest();
			projectInvest.setStatus(InvestEnum.STATUS_SUCCESS.getValue());	
			BigDecimal investTotal=projectInvestService.sumInvest(projectInvest);
			model.addAttribute("investTotal", NumberUtils.isDefault(investTotal)?0:investTotal.doubleValue()/10000);
			User queryUser = new User();
			//总的注册量
			queryUser.setIsVouch(User.USER_NATURE_NOT_VOUCH);
			int registerTotal=userService.getCount(queryUser)-1;//排除admin用户
			model.addAttribute("registerTotal", registerTotal<0?0:registerTotal);
			//累计收益
			BigDecimal totalEarnAmount = userEarnLogService.allEarnAmount(null);
			model.addAttribute("totalEarnAmount", NumberUtils.isDefault(totalEarnAmount)?0:totalEarnAmount.doubleValue()/10000);
			Date date= new Date(System.currentTimeMillis());
		    model.addAttribute("systemTime", AppDateUtil.getYYYYMMDDHHMMSSdate(date));
		return "/app/column/siteData";
		} catch (Exception e) {
			model.addAttribute("r_msg", e.getMessage());
			return ConfigUtils.getTppName()+"/retresult";
		}
	}
	/**
	 * 微信移动端--平台数据
	 * @authorn lgx
	 * @date 2017年02月13日
	 * @return
	 */
	@RequestMapping(value = "/app/open/column/wxSiteData")
	@ResponseBody
	public Object wxSiteData(HttpServletRequest request,final Model model) {
		Object obj=null;
		Map<String, Object> data = new HashMap<String, Object>();
		try{
			data.put("mobileUrl", ConfigUtils.getValue("mobile_url"));
			//投资总额
			ProjectInvest projectInvest = new ProjectInvest();
			projectInvest.setStatus(InvestEnum.STATUS_SUCCESS.getValue());	
			BigDecimal investTotal=projectInvestService.sumInvest(projectInvest);
			data.put("investTotal", NumberUtils.isDefault(investTotal)?0:investTotal.doubleValue()/10000);
			User queryUser = new User();
			//总的注册量
			queryUser.setIsVouch(User.USER_NATURE_NOT_VOUCH);
			int registerTotal=userService.getCount(queryUser)-1;//排除admin用户
			data.put("registerTotal", registerTotal<0?0:registerTotal);
			//累计收益
			BigDecimal totalEarnAmount = userEarnLogService.allEarnAmount(null);
			data.put("totalEarnAmount", NumberUtils.isDefault(totalEarnAmount)?0:totalEarnAmount.doubleValue()/10000);
			Date date= new Date(System.currentTimeMillis());
		    data.put("systemTime", AppDateUtil.getYYYYMMDDHHMMSSdate(date));
			obj=createSuccessAppResponse(data);
		} catch (Exception e) {
			obj=dealException(e);
		}
	return obj;
	}
}
