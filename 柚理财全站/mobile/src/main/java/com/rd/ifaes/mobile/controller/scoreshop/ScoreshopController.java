package com.rd.ifaes.mobile.controller.scoreshop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.CommonConstants;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
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
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.mobile.controller.WebController;
import com.rd.ifaes.mobile.model.PagedData;

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
	@Resource
	private transient ConfigService configService;

	/**
	 * 获取收货地址
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "/app/member/scoreshop/getReceivingInfo")
	@ResponseBody
	public Object getReceivingInfo(HttpServletRequest request){
		Map<String, Object> data = new HashMap<String, Object>();
		Object obj=null;
		try {
			ReceivingInfo queryModel = new ReceivingInfo();
			User user=getAppSessionUser(request);
			queryModel.setUserID(user.getUuid());
			List<ReceivingInfo> list = receivingInfoService.findList(queryModel);
			data.put("list", list);
			data.put("maxCount", ConfigUtils.getInt(ConfigConstant.WEB_MAXRECEIVENUM));
			data.put("useCount", list!=null?list.size():0);
			obj=createSuccessAppResponse(data);
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
	}
	
	/**
	 * 根据ID获取收货地址
	 * @return
	 */
	@RequestMapping(value = "/app/member/scoreshop/getReceivingInfoById")
	@ResponseBody
	public Object editReceivingInfoPage(final String id){
		Map<String, Object> data = new HashMap<String, Object>();
		Object obj=null;
		try {
			data.put("receivingInfo", receivingInfoService.get(id));
			obj=createSuccessAppResponse(data);
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
	}
	
	/**
	 * 获取订单物品信息
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "/app/member/scoreshop/getGoodsInfo")
	@ResponseBody
	public Object getGoodsInfo(final String id,HttpServletRequest request){
		Map<String, Object> data = new HashMap<String, Object>();
		Object obj=null;
		try {
			User user=getAppSessionUser(request);
			data.put("scoreGoods",scoreGoodsService.get(id));
			data.put("exchangedOrders", scoreGoodsOrderService.countUserExchangeOrders(user.getUuid(), id));
			obj=createSuccessAppResponse(data);
		}catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
	}
		
	/**
	 * 获取热销商品(四个数据)
	 * @param goodsCategoryId
	 * @return
	 */
	@RequestMapping(value = "/app/open/scoreshop/getBestseller")
	@ResponseBody
	public Object getBestseller(final String goodsCategoryId){
		Object obj=null;
		try{
			List<ScoreGoods> list = scoreGoodsService.findBestseller(goodsCategoryId,4);
			obj=createSuccessAppResponse(list);
		}catch(Exception e){
			obj=dealException(e);
		}
		return obj;
	}
	
	/**
	 * 获取最新兑换的商品(三个数据)
	 * @return
	 */
	@RequestMapping(value = "/app/open/scoreshop/getNewOrders")
	@ResponseBody
	public Object getNewOrders(){
		Object obj=null;
		try{
			List<ScoreGoodsOrder> list = scoreGoodsOrderService.getNewOrders();
			obj=createSuccessAppResponse(list);
		}catch(Exception e){
			obj=dealException(e);
		}
		return obj;
	}
	
	/**
	 * 获取商品列表
	 * @param scoreGoodsModel
	 * @return
	 */
	@RequestMapping(value = "/app/open/scoreshop/getGoodsList")
	@ResponseBody
	public Object getGoodsList(final ScoreGoodsModel scoreGoodsModel){
		Object obj=null;
		try{
			scoreGoodsModel.putQueryScoreSection();
			scoreGoodsModel.setStatus(Constant.GOODS_STATUS_SALE_ON);
			scoreGoodsModel.getPage().setPageSize(Constant.INT_TWELVE);
			Page<ScoreGoods> pages = scoreGoodsService.findPage(scoreGoodsModel);
			int page=scoreGoodsModel.getPage().getPage();
			List<ScoreGoods> scoreGoodsLogList = pages.getRows();
			PagedData<ScoreGoods> pirlist = new PagedData<ScoreGoods>();
			if(scoreGoodsLogList!=null){
				pages.setPageSize(pages.getRows().size());
				pages.setPage(page);
				fillPageDatas(pirlist, pages);
				for (ScoreGoods scoreGoods : scoreGoodsLogList) {
					pirlist.getList().add(scoreGoods);
				}
			}
			obj=createSuccessAppResponse(pirlist);
		}catch(Exception e){
			obj=dealException(e);
		}
		return obj;
	}
	
	/**
	 * 兑换商品
	 * @param scoreGoodsModel
	 * @return
	 */
	@RequestMapping(value = "/app/member/scoreshop/exchangeGoods")
	@ResponseBody
	public Object exchangeGoods(ScoreGoodsOrder scoreGoodsOrder,final String receivingInfoId,final String receivingAddress,HttpServletRequest request){
		Object obj=null;
		try{	
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
			User user=getAppSessionUser(request);
			scoreGoodsOrder.setUserId(user.getUuid());
			scoreGoodsOrder.setUserName(user.getUserName());
			scoreGoodsOrder.setGoodsName(scoreGoods.getGoodsName());
			scoreGoodsOrderService.exchangeGoods(scoreGoodsOrder);
			obj=createSuccessAppResponse(ResourceUtils.get(ResourceConstant.GOODS_EXCHANGE_SUCCESS));
		}catch(Exception e){
			obj=dealException(e);
		}
		return obj;
	}
	
	/**
	 * 增加收货地址
	 * @param receivingInfo
	 * @return
	 */
	@RequestMapping(value="/app/member/scoreshop/addReceivingInfo")
	@ResponseBody
	public Object addReceivingInfo(@RequestParam(value = "zone[]")final String[] zone,ReceivingInfo receivingInfo,HttpServletRequest request){
		Object obj=null;
		try{
			User user=getAppSessionUser(request);
			int maxReceivenum = ConfigUtils.getInt(ConfigConstant.WEB_MAXRECEIVENUM);
			if (receivingInfoService.countUserReceivingInfoNum(user.getUuid())>=maxReceivenum) {
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
			receivingInfo.setUserID(user.getUuid());
			receivingInfoService.save(receivingInfo);
			obj=createSuccessAppResponse(MSG_SUCCESS);
		}catch(Exception e){
			obj=dealException(e);
		}
		return obj;
	}
	
	/**
	 * 修改收货地址
	 * @param receivingInfo
	 * @return
	 */
	@RequestMapping(value="/app/member/scoreshop/editReceivingInfo")
	@ResponseBody
	public Object editReceivingInfo(@RequestParam(value = "zone[]")final String[] zone,ReceivingInfo receivingInfo,final String id,HttpServletRequest request){
		Object obj=null;
		try{
			User user=getAppSessionUser(request);
			if(zone!=null && zone.length>0){
				receivingInfo.setProvince(zone[0]);
				receivingInfo.setCity(zone[1]);
				if(zone.length==3){ //类似北京和天津，只有二级
					receivingInfo.setArea(zone[2]);
				}
			}
			receivingInfo.setUserID(user.getUuid());
			receivingInfo.setUuid(id);
			receivingInfoService.update(receivingInfo);
			obj=createSuccessAppResponse(MSG_SUCCESS);
		}catch(Exception e){
			obj=dealException(e);
		}
		return obj;
	}
	
	/**
	 * 设置默认收货地址
	 * @param receivingInfo
	 * @return
	 */
	@RequestMapping(value="/app/member/scoreshop/configDefaultReceivingInfo")
	@ResponseBody	
	public Object configDefaultReceivingInfo(final String id,HttpServletRequest request){
		Object obj=null;
		try{
			User user=getAppSessionUser(request);
			receivingInfoService.setDefaultReceivingInfo(id,user.getUuid());
			obj=createSuccessAppResponse(MSG_SUCCESS);
		}catch(Exception e){
			obj=dealException(e);
		}
		return obj;
	}
	
	/**
	 * 删除收货地址
	 * @param receivingInfo
	 * @return
	 */
	@RequestMapping(value="/app/member/scoreshop/deleteReceivingInfo")
	@ResponseBody
	public Object deleteReceivingInfo(final String id,HttpServletRequest request){
		Object obj=null;
		try{
			User user=getAppSessionUser(request);
			receivingInfoService.deleteReceivingInfo(id,user.getUuid());
			obj=createSuccessAppResponse(MSG_SUCCESS);
		}catch(Exception e){
			obj=dealException(e);
		}
		return obj;
	}
	
	/**
	 * 获取商品类别
	 * @param receivingInfo
	 * @return
	 */
	@RequestMapping(value="/app/open/scoreshop/getScoreGoodsCategoryList")
	@ResponseBody
	public Object getScoreGoodsCategoryList(){
		Object obj;
		try{
			List<ScoreGoodsCategory> list =  scoreGoodsCategoryService.findList(new ScoreGoodsCategory());
			obj=createSuccessAppResponse(list);
		}catch(Exception e){
			obj=dealException(e);
		}
		return obj;
	}
	
	/**
	 * 获取用户积分
	 * @param receivingInfo
	 * @return
	 */
	@RequestMapping(value="/app/member/scoreshop/getUserScore")
	@ResponseBody
	public Object getUserScore(HttpServletRequest request){
		Map<String, Object> data = new HashMap<String, Object>();
		Object obj=null;
		try {
			User user=getAppSessionUser(request);
			data.put("userScore",userScoreService.getUserScore(StringUtils.isNull(user.getUuid())));
			obj=createSuccessAppResponse(data);
		}catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
	}
	
	
	
}
