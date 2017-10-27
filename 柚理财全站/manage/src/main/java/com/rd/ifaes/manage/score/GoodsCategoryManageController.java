package com.rd.ifaes.manage.score;



import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.score.domain.ScoreGoodsCategory;
import com.rd.ifaes.core.score.model.ScoreGoodsCategoryModel;
import com.rd.ifaes.core.score.service.ScoreGoodsCategoryService;
import com.rd.ifaes.manage.sys.SystemController;

/**
 * 商品分类Controller
 * @author ywt
 * @date 2017-2-20
 *
 */
@Controller
public class GoodsCategoryManageController  extends SystemController{
	
	@Resource
	private transient ScoreGoodsCategoryService scoreGoodsCategoryService;
	
	/**
	 * 商品类别管理列表页面
	 * @return
	 */
	@RequestMapping(value = "/operate/scoreshop/goodsCategoryManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.GOODS_CATEGORY)
	public String goodsCategoryManage(){
		return "/operate/scoreshop/goodsCategoryManage";
	}
	
	/**
	 * 列表数据
	 * @param scoreGoodsCategoryModel
	 * @return
	 */
	@RequestMapping(value = "/operate/scoreshop/goodsCategoryList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.GOODS_CATEGORY)
	public Page<ScoreGoodsCategory> getGoodsCategoryList(ScoreGoodsCategoryModel scoreGoodsCategoryModel){
		return scoreGoodsCategoryService.findPage(scoreGoodsCategoryModel);
	}
	
	/**
	 * 添加商品类别页面
	 * @return
	 */
	@RequestMapping(value = "/operate/scoreshop/addGoodsCategoryPage")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.GOODS_CATEGORY)
	public String addGoodsCategoryPage(){
		return "/operate/scoreshop/addGoodsCategoryPage";
	}
	
	/**
	 * 修改商品类别页面
	 * @return
	 */
	@RequestMapping(value = "/operate/scoreshop/editGoodsCategoryPage.html")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.GOODS_CATEGORY)
	public String editGoodsCategoryPage(String id,final Model model){
		ScoreGoodsCategory goodsCategory = scoreGoodsCategoryService.get(StringUtils.isNull(id));
		model.addAttribute("goodsCategory", goodsCategory);
		return "/operate/scoreshop/editGoodsCategoryPage";
	}
	
	/**
	 * 增加商品分类
	 * @param scoreGoodsCategoryModel
	 * @return
	 */
	@RequestMapping(value = "/operate/scoreshop/addGoodsCategory")
	@ResponseBody
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.GOODS_CATEGORY)
	public Map<String, Object> addGoodsCategory(ScoreGoodsCategoryModel scoreGoodsCategoryModel){
		
		//todo  校验
		
		
		scoreGoodsCategoryModel.setOperator(getUserId());
		scoreGoodsCategoryService.saveOrUpdate(scoreGoodsCategoryModel);
		return renderSuccessResult();
	}
	
	/**
	 * 删除商品分类
	 * @param scoreGoodsCategoryModel
	 * @return
	 */
	@RequestMapping(value = "/operate/scoreshop/deleteGoodsCategory")
	@ResponseBody
	@SystemLog(method=SysLogConstant.DEL,content=SysLogConstant.GOODS_CATEGORY)
	public Map<String, Object> deleteGoodsCategory(String ids){
		scoreGoodsCategoryService.deleteLogic(ids);
		return renderSuccessResult();
	}
}
