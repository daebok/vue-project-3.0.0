package com.rd.ifaes.web.controller.scoreshop;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.CommonConstants;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.score.domain.ReceivingInfo;
import com.rd.ifaes.core.score.domain.ScoreGoods;
import com.rd.ifaes.core.score.domain.ScoreGoodsCategory;
import com.rd.ifaes.core.score.domain.ScoreGoodsOrder;
import com.rd.ifaes.core.score.model.ScoreGoodsModel;
import com.rd.ifaes.core.score.service.ReceivingInfoService;
import com.rd.ifaes.core.score.service.ScoreGoodsCategoryService;
import com.rd.ifaes.core.score.service.ScoreGoodsOrderService;
import com.rd.ifaes.core.score.service.ScoreGoodsService;
import com.rd.ifaes.core.score.service.ScoreGoodsStoreService;
import com.rd.ifaes.core.score.service.UserScoreService;
import com.rd.ifaes.core.sys.service.ConfigService;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.web.controller.WebController;


/**
 * 积分商城前台Controller
 * @author ywt
 * @date 2017-02-28
 * @version 3.0.2
 */
@Controller
public class ScoreshopController extends WebController{
	
	@Resource
	private transient UserScoreService userScoreService;
	@Resource
	private transient ScoreGoodsService scoreGoodsService;
	@Resource
	private transient ScoreGoodsStoreService scoreGoodsStoreService;
	@Resource
	private transient ScoreGoodsOrderService scoreGoodsOrderService;
	@Resource
	private transient ScoreGoodsCategoryService scoreGoodsCategoryService;
	@Resource
	private transient ReceivingInfoService receivingInfoService;
	@Resource
	private transient UserCacheService userCacheService;
	
	/**
	 * 跳转积分商城首页
	 * @return
	 */
	@RequestMapping(value = "/scoreshop/index")
	public String index(final Model model){
		model.addAttribute("userScore", userScoreService.getUserScore(StringUtils.isNull(getUserId())));
		model.addAttribute("userCache", userCacheService.findByUserId(getUserId()));
		return "/scoreshop/index";
	}
	
	/**
	 * 跳转积分商品详情页
	 * @return
	 */
	@RequestMapping(value = "/scoreshop/goodsDetails")
	public String detail(final String id,final Model model){
		model.addAttribute("userScore", userScoreService.getUserScore(StringUtils.isNull(getUserId())));
		model.addAttribute("scoreGoods", scoreGoodsService.get(id));
		model.addAttribute("exchangedOrders", scoreGoodsOrderService.countUserExchangeOrders(getUserId(), id));
		return "/scoreshop/goodsDetails";
	}
	
	/**
	 * 跳转积分商品详情页(虚拟)
	 * @return
	 */
	@RequestMapping(value = "/scoreshop/virtualOrder")
	public String virtualOrder(final String id,final int num,final Model model){
		model.addAttribute("num", num);
		model.addAttribute("goodsId",id);
		return "/scoreshop/virtualOrder";
	}
	
	/**
	 * 跳转商城帮助页
	 * @return
	 */
	@RequestMapping(value = "/scoreshop/mallHelp")
	public String mallHelp(){
		return "/scoreshop/mallHelp";
	}
	
	/**
	 * 跳转下单页
	 * @return
	 */
	@RequestMapping(value = "/scoreshop/order")
	public String order(final String id,final int num,final Model model){
		model.addAttribute("num", num);
		model.addAttribute("goodsId",id);
		ScoreGoods goods = scoreGoodsService.get(id);
		if (goods!=null && !Constant.GOODS_STATUS_SALE_ON.equals(goods.getStatus())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GOODS_IS_OVER_STOP_SALE));
		}
		return "/scoreshop/order";
	}
	
	/**
	 * 获取收货地址
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "/scoreshop/getReceivingInfo")
	@ResponseBody
	public Object getReceivingInfo(){
		Map<String, Object> data = renderSuccessResult();
		ReceivingInfo queryModel = new ReceivingInfo();
		queryModel.setUserID(getUserId());
		data.put("list", receivingInfoService.findList(queryModel));
		return data;
	}
	
	/**
	 * 根据ID获取收货地址
	 * @return
	 */
	@RequestMapping(value = "/scoreshop/getReceivingInfoById")
	@ResponseBody
	public Object editReceivingInfoPage(final String id){
		Map<String, Object> data = renderSuccessResult();
		data.put("receivingInfo", receivingInfoService.get(id));
		return data;
	}
	
	/**
	 * 获取订单物品信息
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "/scoreshop/getGoodsInfo")
	@ResponseBody
	public Object getGoodsInfo(final String id){
		return scoreGoodsService.get(id);
	}
	
	/**
	 * 跳转商品列表页
	 * @return
	 */
	@RequestMapping(value = "/scoreshop/goodsList")
	public String goodsList(String id,final Model model){
		model.addAttribute("categoryId", id);
		model.addAttribute("userScore", userScoreService.getUserScore(StringUtils.isNull(getUserId())));
		model.addAttribute("userCache", userCacheService.findByUserId(getUserId()));
		return "/scoreshop/goodsList";
	}
		
	/**
	 * 获取热销商品(三个数据)
	 * @param goodsCategoryId
	 * @return
	 */
	@RequestMapping(value = "/scoreshop/getBestseller")
	@ResponseBody
	public Object getBestseller(final String goodsCategoryId){
		Map<String, Object> data = renderSuccessResult();
		data.put("list", scoreGoodsService.findBestseller(goodsCategoryId,3));
		return data;
	}
	
	/**
	 * 获取最新兑换的商品(三个数据)
	 * @return
	 */
	@RequestMapping(value = "/scoreshop/getNewOrders")
	@ResponseBody
	public Object getNewOrders(){
		Map<String, Object> data = renderSuccessResult();
		data.put("data", scoreGoodsOrderService.getNewOrders());
		return data;
	}
	
	/**
	 * 获取商品列表
	 * @param scoreGoodsModel
	 * @return
	 */
	@RequestMapping(value = "/scoreshop/getGoodsList")
	@ResponseBody
	public Object getGoodsList(final ScoreGoodsModel scoreGoodsModel){
		Map<String, Object> data = renderSuccessResult();
		scoreGoodsModel.putQueryScoreSection();
		scoreGoodsModel.setStatus(Constant.GOODS_STATUS_SALE_ON);
		scoreGoodsModel.getPage().setPageSize(Constant.INT_TWELVE);
		data.put("page", scoreGoodsService.findPage(scoreGoodsModel));
		return data;
	}
	
	/**
	 * 兑换商品
	 * @param scoreGoodsModel
	 * @return
	 */
	@RequestMapping(value = "/scoreshop/exchangeGoods")
	@ResponseBody
	public Object exchangeGoods(ScoreGoodsOrder scoreGoodsOrder,final String receivingInfoId,final String receivingAddress){
		ScoreGoods scoreGoods = scoreGoodsService.get(scoreGoodsOrder.getGoodsId());
		if(scoreGoods==null){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.SCORE_GOODS_IS_NOT_EXIST),BussinessException.TYPE_JSON);
		}
		
		//非虚拟商品
		if(CommonConstants.NO.equals(scoreGoods.getIsVirtual())){
			ReceivingInfo receivingInfo = receivingInfoService.get(receivingInfoId);
			StringBuffer address= new StringBuffer();
			address.append(StringUtils.isNull(receivingInfo.getProvinceStr())).append(StringUtils.isNull(receivingInfo.getCityStr()))
						.append(StringUtils.isNull(receivingInfo.getAreaStr())).append(StringUtils.isNull(receivingInfo.getAddress()));
			if(receivingInfo == null || !address.toString().equals(receivingAddress)){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.RECEIVING_ADDRESS_IS_ERROR),BussinessException.TYPE_JSON);
			}
			scoreGoodsOrder.setReceiveAddress(address.toString());
			scoreGoodsOrder.setReceivePhone(receivingInfo.getMobile());
			scoreGoodsOrder.setReceiveUserName(receivingInfo.getName());
		}

		scoreGoodsOrder.setUserId(getUserId());
		scoreGoodsOrder.setUserName(getUserName());
		scoreGoodsOrderService.exchangeGoods(scoreGoodsOrder);
		final Map<String, Object> rtn = Maps.newHashMap();
		rtn.put(RESULT_NAME, true);
		rtn.put(MSG_NAME, ResourceUtils.get(ResourceConstant.GOODS_EXCHANGE_SUCCESS));
		return rtn;
	}
	
	/**
	 * 增加收货地址
	 * @param receivingInfo
	 * @return
	 */
	@RequestMapping(value="/scoreshop/addReceivingInfo")
	@ResponseBody
	public Object addReceivingInfo(@RequestParam(value = "zone[]")final String[] zone,ReceivingInfo receivingInfo){
		ConfigService configService = (ConfigService) SpringContextHolder.getBean("configService");
		int maxReceivenum = Integer.valueOf(configService.findByCode(ConfigConstant.WEB_MAXRECEIVENUM).getConfigValue());
		if (receivingInfoService.countUserReceivingInfoNum(getUserId())>=maxReceivenum) {
			throw new BussinessException(StringUtils.format(ResourceUtils.get(ResourceConstant.RECEIVE_NUM_MAX), maxReceivenum));
		}
		if(zone!=null && zone.length>0){
			receivingInfo.setProvince(zone[0]);
			receivingInfo.setCity(zone[1]);
			if(zone.length==3){ //类似北京和天津，只有二级
				receivingInfo.setArea(zone[2]);
			}
		}
		receivingInfo.setNewRecord(true);
		receivingInfo.setUserID(getUserId());
		receivingInfoService.save(receivingInfo);
		return renderSuccessResult();
	}
	
	/**
	 * 修改收货地址
	 * @param receivingInfo
	 * @return
	 */
	@RequestMapping(value="/scoreshop/editReceivingInfo")
	@ResponseBody
	public Object editReceivingInfo(@RequestParam(value = "zone[]")final String[] zone,ReceivingInfo receivingInfo,final String id){
		if(zone!=null && zone.length>0){
			receivingInfo.setProvince(zone[0]);
			receivingInfo.setCity(zone[1]);
			if(zone.length==3){ //类似北京和天津，只有二级
				receivingInfo.setArea(zone[2]);
			}
		}
		receivingInfo.setUserID(getUserId());
		receivingInfo.setUuid(id);
		receivingInfoService.update(receivingInfo);
		return renderSuccessResult();
	}
	
	/**
	 * 设置默认收货地址
	 * @param receivingInfo
	 * @return
	 */
	@RequestMapping(value="/scoreshop/configDefaultReceivingInfo")
	@ResponseBody	
	public Object configDefaultReceivingInfo(final String id){
		receivingInfoService.setDefaultReceivingInfo(id,getUserId());;
		return renderSuccessResult();
	}
	
	/**
	 * 删除收货地址
	 * @param receivingInfo
	 * @return
	 */
	@RequestMapping(value="/scoreshop/deleteReceivingInfo")
	@ResponseBody
	public Object deleteReceivingInfo(final String id){
		receivingInfoService.deleteReceivingInfo(id,getUserId());
		return renderSuccessResult();
	}
	
	/**
	 * 获取商品类别
	 * @param receivingInfo
	 * @return
	 */
	@RequestMapping(value="/scoreshop/getScoreGoodsCategoryList")
	@ResponseBody
	public Object getScoreGoodsCategoryList(){
		Map<String, Object> data = renderSuccessResult();
		data.put("list", scoreGoodsCategoryService.findList(new ScoreGoodsCategory()));
		return data;
	}
	
	/**
	 * 获取商品列表查询条件
	 * @param receivingInfo
	 * @return
	 */
	@RequestMapping(value="/scoreshop/queryCondition")
	@ResponseBody
	public Object queryCondition(){
		Map<String, Object> data = renderSuccessResult();
		data.put("condition", scoreGoodsService.queryCondition());
		return data;
	}
}
