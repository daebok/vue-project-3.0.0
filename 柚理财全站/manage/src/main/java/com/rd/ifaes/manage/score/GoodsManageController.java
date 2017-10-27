package com.rd.ifaes.manage.score;



import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.dict.OperateEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.operate.service.RateCouponRuleService;
import com.rd.ifaes.core.operate.service.RedenvelopeRuleService;
import com.rd.ifaes.core.score.domain.ScoreGoods;
import com.rd.ifaes.core.score.domain.ScoreGoodsPic;
import com.rd.ifaes.core.score.domain.ScoreGoodsStore;
import com.rd.ifaes.core.score.model.ScoreGoodsModel;
import com.rd.ifaes.core.score.service.ScoreGoodsCategoryService;
import com.rd.ifaes.core.score.service.ScoreGoodsPicService;
import com.rd.ifaes.core.score.service.ScoreGoodsService;
import com.rd.ifaes.core.score.service.ScoreGoodsStoreService;
import com.rd.ifaes.manage.sys.SystemController;

/**
 * 商品管理Controller
 * @author ywt
 * @date 2017-2-20
 *
 */
@Controller
public class GoodsManageController  extends SystemController{
	@Resource
	private transient ScoreGoodsService scoreGoodsService;
	@Resource
	private transient ScoreGoodsStoreService scoreGoodsStoreService;
	@Resource
	private transient ScoreGoodsCategoryService scoreGoodsCategoryService;
	@Resource
	private transient RedenvelopeRuleService redenvelopeRuleService;
	@Resource
	private transient RateCouponRuleService rateCouponRuleService;
	@Resource
	private transient ScoreGoodsPicService scoreGoodsPicService;
	
	/**
	 * 商品管理列表页面
	 * @return
	 */
	@RequestMapping(value = "/operate/scoreshop/goodsManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.GOODS)
	public String goodsCategoryManage(final Model model){
		model.addAttribute("category", scoreGoodsCategoryService.findAll());
		return "/operate/scoreshop/goodsManage";
	}
	
	/**
	 * 商品审核列表页面
	 * @return
	 */
	@RequestMapping(value = "/operate/scoreshop/goodsVerifyPage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.GOODS)
	public String goodsVerifyPage(){
		return "/operate/scoreshop/goodsVerifyPage";
	}
	
	/**
	 * 商品上架、下架列表页面
	 * @return
	 */
	@RequestMapping(value = "/operate/scoreshop/goodsSalePage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.GOODS)
	public String goodsSalePage(){
		return "/operate/scoreshop/goodsSalePage";
	}
	
	/**
	 * 跳转查看商品页
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/operate/scoreshop/viewGoodsPage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.GOODS)
	public String viewGoods(final String id,final Model model){
		model.addAttribute("scoreGoods", scoreGoodsService.get(id));
		return "/operate/scoreshop/viewGoodsPage";
	}
	
	/**
	 * 列表数据
	 * @param scoreGoodsCategoryModel
	 * @return
	 */
	@RequestMapping(value = "/operate/scoreshop/goodsList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.GOODS)
	public Page<ScoreGoods> getGoodsList(ScoreGoodsModel scoreGoodsModel){
		return scoreGoodsService.findPage(scoreGoodsModel);
	}
	
	/**
	 * 获取待审核的商品信息
	 * @param scoreGoodsCategoryModel
	 * @return
	 */
	@RequestMapping(value = "/operate/scoreshop/getVerifyGoodsList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.GOODS)
	public Page<ScoreGoods> getVerifyGoodsList(ScoreGoodsModel scoreGoodsModel){
		scoreGoodsModel.setStatus(Constant.GOODS_STATUS_VERIFY_WAIT);
		return scoreGoodsService.findVerifyGoods(scoreGoodsModel);
	}
	
	/**
	 * 获取可上架、下架的商品信息
	 * @param scoreGoodsCategoryModel
	 * @return
	 */
	@RequestMapping(value = "/operate/scoreshop/getSaleGoodsList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.GOODS)
	public Page<ScoreGoods> getSaleGoodsList(ScoreGoodsModel scoreGoodsModel){
		return scoreGoodsService.findSaleGoods(scoreGoodsModel);
	}
	
	/**
	 * 添加商品页面
	 * @return
	 */
	@RequestMapping(value = "/operate/scoreshop/addGoodsPage")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.GOODS)
	public String addGoodsPage(final Model model){
		model.addAttribute("goodsCategory", scoreGoodsCategoryService.findAll());
		model.addAttribute("redenvelopes",redenvelopeRuleService.findByActivityCode(OperateEnum.ACTIVITYPLAN_SCORE_SHOP.getValue()));
		model.addAttribute("rateCoupon",rateCouponRuleService.findByActivityCode(OperateEnum.ACTIVITYPLAN_SCORE_SHOP.getValue()));
		return "/operate/scoreshop/addGoodsPage";
	}
	
	/**
	 * 修改商品页面
	 * @return
	 */
	@RequestMapping(value = "/operate/scoreshop/editGoodsPage")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.GOODS)
	public String editGoodsPage(String id, final Model model){
		ScoreGoods scoreGoods = scoreGoodsService.get(StringUtils.isNull(id));
		ScoreGoodsStore scoreGoodsStore = scoreGoodsStoreService.findStoreByGoodsId(StringUtils.isNull(id));
		if (scoreGoods!=null &&(Constant.GOODS_STATUS_SALE_ON.equals(scoreGoods.getStatus()) || Constant.GOODS_STATUS_VERIFY_SUCCESS.equals(scoreGoods.getStatus()))) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GOODS_UPDATE_ERROR));
		}
		model.addAttribute("goodsCategory", scoreGoodsCategoryService.findAll());
		scoreGoods.setStore(scoreGoodsStore.getStore());
		model.addAttribute("scoreGoods", scoreGoods);
		model.addAttribute("redenvelopes",redenvelopeRuleService.findByActivityCode(OperateEnum.ACTIVITYPLAN_SCORE_SHOP.getValue()));
		model.addAttribute("rateCoupon",rateCouponRuleService.findByActivityCode(OperateEnum.ACTIVITYPLAN_SCORE_SHOP.getValue()));
		ScoreGoodsPic picModel= new ScoreGoodsPic();
		picModel.setGoodsId(scoreGoods.getUuid());
		model.addAttribute("picList", scoreGoodsPicService.findList(picModel));
		return "/operate/scoreshop/editGoodsPage";
	}
	
	/**
	 * 审核商品页面
	 * @return
	 */
	@RequestMapping(value = "/operate/scoreshop/verifyGoods")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.GOODS)
	public String verifyGoods(String id, final Model model){
		ScoreGoods scoreGoods = scoreGoodsService.get(StringUtils.isNull(id));
		model.addAttribute("scoreGoods", scoreGoods);
		return "/operate/scoreshop/verifyGoods";
	}
	
	/**
	 * 上架商品页面
	 * @return
	 */
	@RequestMapping(value = "/operate/scoreshop/saleGoods")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.GOODS)
	public String saleGoods(String id, final Model model){
		ScoreGoods scoreGoods = scoreGoodsService.get(StringUtils.isNull(id));
		model.addAttribute("scoreGoods", scoreGoods);
		return "/operate/scoreshop/saleGoods";
	}
	
	/**
	 * 下架商品页面
	 * @return
	 */
	@RequestMapping(value = "/operate/scoreshop/stopGoods")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.GOODS)
	public String stopGoods(String id, final Model model){
		ScoreGoods scoreGoods = scoreGoodsService.get(StringUtils.isNull(id));
		model.addAttribute("scoreGoods", scoreGoods);
		return "/operate/scoreshop/stopGoods";
	}
	
	/**
	 * 增加商品
	 * @param scoreGoodsModel
	 * @return
	 */
	@RequestMapping(value = "/operate/scoreshop/addGoods")
	@ResponseBody
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.GOODS)
	public Map<String, Object> addGoods(ScoreGoodsModel scoreGoodsModel){
		if (StringUtils.isBlank(scoreGoodsModel.getUuid())) {
			String uuid=UUID.randomUUID().toString().replaceAll("-", "");
			scoreGoodsModel.setCreateOperatorId(getUserId());
			scoreGoodsModel.setUuid(uuid);
			scoreGoodsModel.setNewRecord(true);
		}
		else{
			scoreGoodsModel.setUpdateOperatorId(getUserId());
		}
		scoreGoodsService.saveOrUpdate(scoreGoodsModel);
		return renderSuccessResult();
	}
	
	/**
	 * 修改商品
	 * @param scoreGoodsModel
	 * @return
	 */
	@RequestMapping(value = "/operate/scoreshop/editGoods")
	@ResponseBody
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.GOODS)
	public Map<String, Object> editGoods(ScoreGoodsModel scoreGoodsModel){
		//todo  校验
		scoreGoodsService.saveOrUpdate(scoreGoodsModel);
		return renderSuccessResult();
	}
	
	
	/**
	 * 更新商品
	 * @param scoreGoodsModel
	 * @return
	 */
	@RequestMapping(value = "/operate/scoreshop/updateGoods")
	@ResponseBody
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.GOODS)
	public Map<String, Object> updateGoods(ScoreGoodsModel scoreGoodsModel){
		//todo  校验
		scoreGoodsService.updateGoods(scoreGoodsModel);
		return renderSuccessResult();
	}
	
	/**
	 * 审核商品
	 * @param scoreGoodsModel
	 * @return
	 */
	@RequestMapping(value = "/operate/scoreshop/doVerifyGoods")
	@ResponseBody
	@SystemLog(method=SysLogConstant.AUDIT,content=SysLogConstant.GOODS)
	public Map<String, Object> doVerifyGoods(ScoreGoodsModel scoreGoodsModel){
		scoreGoodsModel.setVerifyTime(DateUtils.getNow());
		scoreGoodsModel.setVerifyOperatorId(getUser().getUuid());
		scoreGoodsService.updateGoods(scoreGoodsModel);
		return renderSuccessResult();
	}
	
}
