package com.rd.ifaes.manage.score;



import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.score.domain.ScoreGoods;
import com.rd.ifaes.core.score.domain.ScoreGoodsStore;
import com.rd.ifaes.core.score.model.ScoreGoodsStoreModel;
import com.rd.ifaes.core.score.service.ScoreGoodsService;
import com.rd.ifaes.core.score.service.ScoreGoodsStoreService;
import com.rd.ifaes.manage.sys.SystemController;

/**
 * 商品库存Controller
 * @author ywt
 * @date 2017-2-20
 *
 */
@Controller
public class GoodsStoreManageController  extends SystemController{
	@Resource
	private transient ScoreGoodsStoreService scoreGoodsStoreService;
	@Resource
	private transient ScoreGoodsService scoreGoodsService;
	
	/**
	 * 商品库存管理列表页面
	 * @return
	 */
	@RequestMapping(value = "/operate/scoreshop/goodsStoreManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.GOODS_STORE)
	public String goodsStoreManage(){
		return "/operate/scoreshop/goodsStoreManage";
	}
	
	/**
	 * 列表数据
	 * @param scoreGoodsStoreModel
	 * @return
	 */
	@RequestMapping(value = "/operate/scoreshop/goodsStoreList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.GOODS_STORE)
	public Page<ScoreGoodsStore> getGoodsStoreList(ScoreGoodsStoreModel scoreGoodsStoreModel){
		return scoreGoodsStoreService.findPage(scoreGoodsStoreModel);
	}
	
	/**
	 * 修改商品库存页面
	 * @return
	 */
	@RequestMapping(value = "/operate/scoreshop/editGoodsStorePage")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.GOODS_STORE)
	public String editGoodsStorePage(String id ,final Model model){
		ScoreGoodsStore scoreGoodsStore = scoreGoodsStoreService.get(StringUtils.isNull(id));
		ScoreGoods scoreGoods = scoreGoodsService.get(StringUtils.isNull(scoreGoodsStore.getGoodsId()));
		if (Constant.GOODS_STATUS_SALE_ON.equals(scoreGoods.getStatus())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.SCORE_GOODS_STORE_STATUS_ERROR));
		}
		model.addAttribute("goodsStore", scoreGoodsStore);
		return "/operate/scoreshop/editGoodsStorePage";
	}
	
	/**
	 * 保存商品库存信息
	 * @param scoreGoodsStoreModel
	 * @return
	 */
	@RequestMapping(value = "/operate/scoreshop/updateGoodsStore")
	@ResponseBody
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.GOODS_STORE)
	public Map<String, Object> updateGoodsStore(ScoreGoodsStoreModel scoreGoodsStoreModel){
		ScoreGoods scoreGoods = scoreGoodsService.get(StringUtils.isNull(scoreGoodsStoreModel.getGoodsId()));
		if (!Constant.GOODS_STATUS_VERIFY_SUCCESS.equals(scoreGoods.getStatus())
				&& !Constant.GOODS_STATUS_SALE_STOP.equals(scoreGoods.getStatus())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.SCORE_GOODS_STORE_STATUS_ERROR));
		}
		ScoreGoodsStore scoreGoodsStore = scoreGoodsStoreService.get(scoreGoodsStoreModel.getUuid());
		if(scoreGoodsStore!=null && scoreGoodsStore.getStore() != scoreGoodsStoreModel.getStore()){
			if(scoreGoodsStoreModel.getStore() < (scoreGoodsStore.getFreezeStore()+scoreGoodsStore.getSoldAmount())){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.GOODS_STORE_MUSTGT_FREEZE_AND_SOLD));
			}
			scoreGoodsStoreService.updateGoodStoreTotal(scoreGoodsStoreModel.getGoodsId(),scoreGoodsStoreModel.getStore());
		}
		return renderSuccessResult();
	}
	
}
