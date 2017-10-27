package com.rd.ifaes.manage.column;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.dict.DeleteFlagEnum;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.manage.sys.SystemController;
import com.rd.ifaes.core.column.domain.Article;
import com.rd.ifaes.core.column.domain.Section;
import com.rd.ifaes.core.column.service.ArticleService;
import com.rd.ifaes.core.column.service.SectionService;
import com.rd.ifaes.core.core.constant.SysLogConstant;

@Controller
public class ArticleManageController  extends SystemController {
	@Resource
	private SectionService   sectionService;
	@Resource
	private ArticleService   articleService;
	/**
	 * 文章管理管理
	 * @return
	 */
	@RequestMapping(value = "/column/article/articleManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.ARTICLE)
	public String articleManage(){	
		return "/column/article/articleManage";
	}
	/**
	 * 添加文章页面
	 * @return
	 */
	@RequestMapping(value = "/column/article/articleAddPage")
	@RequiresPermissions("set:website:article:add")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.ARTICLE)
	public String articleAddPage(final String sectionUuid, final Model model){	
		if(!StringUtils.isBlank(sectionUuid)){
			model.addAttribute("sectionUuid", sectionUuid);
			final Section section= sectionService.get(sectionUuid);
			model.addAttribute("sectionName", sectionUuid);
			if(section!=null){
				  model.addAttribute("sectionCode", section.getSectionCode());
			      model.addAttribute("sectionName",section.getSectionName());
			}
		}
		return "/column/article/articleAddPage";
	}
	/**
	 * 添加文章
	 * @return 
	 */
	@RequestMapping(value = "/column/article/articleAdd", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("set:website:article:add")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.ARTICLE)
	public Map<String, Object> articleAdd(final Article model , final HttpServletRequest request){		
		    //设置栏目属性
			articleService.insert(model);
			return renderSuccessResult();			
	}
	/**
	 * 根据传入所属类别查询文章
	 * @param model 含所属类别
	 * @return 分类列表
	 */
	@RequestMapping(value = "/column/article/articlelist")
	@ResponseBody
	@RequiresPermissions("set:website:article:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.ARTICLE)
	public Object articlelist(final Article model) {
		model.setDeleteFlag(DeleteFlagEnum.NO.getValue());
		List<Section> sectionList = sectionService.findChildSection(model.getSectionUuid());

		int size = sectionList.size();
		String[] sectionUuids = new String[size];
		for (int index = 0; index < size; index++) {
			Section section = sectionList.get(index);
			if (section != null) {
				sectionUuids[index] = section.getSectionCode();
			}
		}
		model.setSectionUuids(sectionUuids);
		return articleService.findPage(model);
	}
	
	/**
	 * 删除文章
	 * @return 
	 */
	@RequestMapping(value = "/column/article/articleDel", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("set:website:article:add")
	@SystemLog(method=SysLogConstant.DEL,content=SysLogConstant.ARTICLE)
	public Map<String, Object> articleDel(final String[] ids){
		if(ids ==null || ids.length==0){
			return renderResult( false, "请勾选需要删除的记录");
		}
	    articleService.deleteBatch(ids);
	    return renderSuccessResult();	
	}
    /**
	 * 页面编辑页面
	 * @return
	 */
	@RequestMapping(value = "/column/article/articleEditPage")
	@RequiresPermissions("set:website:article:add")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.ARTICLE)
	public String articleEditPage(final String id,final Model model){
		if(StringUtils.isNotBlank(id)){
			//菜单详情
			final Article article= articleService.get(id);
			model.addAttribute("article",article);	
			if(article!=null){
				final Section section= sectionService.getByCode(article.getSectionCode());
				if(section!=null){
					model.addAttribute("sectionCode", section.getSectionCode());
					model.addAttribute("sectionUuid", section.getUuid());
					model.addAttribute("sectionName",section.getSectionName());
				}
			}
		}
		return "/column/article/articleEditPage";
	}
	
	/**
	 * 编辑文章
	 * @return 
	 */
	@RequestMapping(value = "/column/article/articleEdit", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("set:website:article:add")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.ARTICLE)
	public Map<String, Object> articleEdit(final Article article , final HttpServletRequest request){
			Article articleEdit=articleService.get(article.getUuid());
			if(Article.STATUS_WAIT.equals(articleEdit.getStatus())) {
				articleEdit.setTitle(article.getTitle());
				articleEdit.setSort(article.getSort());
				articleEdit.setUrl(article.getUrl());
				articleEdit.setRemark(article.getRemark());
				articleEdit.setPicPath(article.getPicPath());
				articleEdit.setContent(article.getContent());
				articleService.update(articleEdit);
				return renderSuccessResult();
			}
				return renderResult(false, MSG_FAIL);
	}
	
	/**
	 * 推荐文章
	 * @return 
	 */
	@RequestMapping(value = "/column/article/articleRecommend", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("set:website:article:edit")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.ARTICLE)
	public Map<String, Object> articleRecommend(String[] ids){
			Article article=articleService.get(ids[0]);
			if(Article.RECOMMEND_NO.equals(article.getIsRecommend())) {
				article.setIsRecommend(Article.RECOMMEND_YES);
			}
			else {
				article.setIsRecommend(Article.RECOMMEND_NO);
			}
			articleService.update(article);
			return renderSuccessResult();		
	}
	/**
	 * 置顶文章
	 * @return 
	 */
	@RequestMapping(value = "/column/article/articleTop", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("set:website:article:edit")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.ARTICLE)
	public Map<String, Object> articleTop(String[] ids){
			Article article=articleService.get(ids[0]);
			if(Article.TOP_NO.equals(article.getIsTop())) {
				article.setIsTop(Article.TOP_YES);
			}
			else {
				article.setIsTop(Article.TOP_NO);
			}
			articleService.update(article);
			return renderSuccessResult();		
	}
	/**
	 * 审核文章
	 * @return
	 */
	@RequestMapping(value = "/column/article/articleReview", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("set:website:article:edit")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.ARTICLE)
	public Map<String, Object> articleReview(String[] ids){
			Article article=articleService.get(ids[0]);
			article.setStatus(Article.STATUS_PASS);
			articleService.update(article);
			return renderSuccessResult();		
	}
	/**
	 * 发布文章文章
	 * @return
	 */
	@RequestMapping(value = "/column/article/articlePublish", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("set:website:article:edit")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.ARTICLE)
	public Map<String, Object> articlePublish(String[] ids){
			Article article=articleService.get(ids[0]);
			article.setStatus(Article.STATUS_PUBLISH);
			articleService.update(article);
			return renderSuccessResult();		
	}
	/**
	 * 撤回文章
	 * @return
	 */
	@RequestMapping(value = "/column/article/articleRevoke", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("set:website:article:edit")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.ARTICLE)
	public Map<String, Object> articleRevoke(String[] ids){
			Article article=articleService.get(ids[0]);
			article.setStatus(Article.STATUS_WAIT);
			articleService.update(article);
			return renderSuccessResult();		
	}
}
