package com.rd.ifaes.manage.score;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.score.domain.ScoreGoodsOrder;
import com.rd.ifaes.core.score.service.ScoreGoodsOrderService;
import com.rd.ifaes.manage.sys.SystemController;

/**
 * 积分商品订单管理 controller
 * @author ywt
 * @date 2017-03-07
 * @version v3.0
 */
@Controller
public class ScoreGoodsOrderManageController extends SystemController{
	
	@Resource
	private transient ScoreGoodsOrderService scoreGoodsOrderService;
	
	
	/**
	 * 跳转订单管理页面
	 * @return
	 */
	@RequestMapping("/operate/scoreshop/orderManage")
	public String orderManage(){
		return "/operate/scoreshop/orderManage";
	}
	
	/**
	 * 跳转订单审核页面
	 * @return
	 */
	@RequestMapping("/operate/scoreshop/verifyOrder")
	public String orderManage(String id,final Model model){
		model.addAttribute("order", scoreGoodsOrderService.get(StringUtils.isNull(id)));
		return "/operate/scoreshop/verifyOrder";
	}
	
	/**
	 * 跳转订单跟踪页面
	 * @return
	 */
	@RequestMapping("/operate/scoreshop/orderTracking")
	public String orderTracking(String id,final Model model){
		model.addAttribute("order", scoreGoodsOrderService.get(StringUtils.isNull(id)));
		return "/operate/scoreshop/orderTracking";
	}
	
	/**
	 * 跳转录入收货信息页面
	 * @return
	 */
	@RequestMapping("/operate/scoreshop/addDeliverInfoPage")
	public String addDeliverPage(String id,final Model model){
		model.addAttribute("order", scoreGoodsOrderService.get(StringUtils.isNull(id)));
		return "/operate/scoreshop/addDeliverInfoPage";
	}
	
	/**
	 * 获取待审核订单
	 * @return
	 */
	@RequestMapping("/operate/scoreshop/getVerifyOrderList")
	@ResponseBody
	public Page<ScoreGoodsOrder> getVerifyOrderList(ScoreGoodsOrder order){
		order.setStatus(Constant.GOODS_ORDER_STATUS_WAIT);
		return scoreGoodsOrderService.findPage(order);
	}
	
	/**
	 * 获取订单
	 * @return
	 */
	@RequestMapping("/operate/scoreshop/getOrderList")
	@ResponseBody
	public Page<ScoreGoodsOrder> getOrderList(ScoreGoodsOrder order){
		return scoreGoodsOrderService.findPage(order);
	}
	
	/**
	 * 获取除待审核外的所有订单
	 * @return
	 */
	@RequestMapping("/operate/scoreshop/findListExceptWaitVerify")
	@ResponseBody
	public Page<ScoreGoodsOrder> findListExceptWaitVerify(ScoreGoodsOrder order){
		List<ScoreGoodsOrder> list = scoreGoodsOrderService.findListExceptWaitVerify(order.getKeywords());
		Page<ScoreGoodsOrder> page= order.getPage();
		if(page==null){
			page=new Page<ScoreGoodsOrder>();
		}
		page.setRows(list);
		return page;
	}
	
	/**
	 * 修改订单
	 * @return
	 */
	@RequestMapping("/operate/scoreshop/updateOrder")
	@ResponseBody
	public Map<String, Object> updateOrder(ScoreGoodsOrder order){
		if (Constant.GOODS_ORDER_STATUS_VERIFY_SUCCESS.equals(order.getStatus())
				|| Constant.GOODS_ORDER_STATUS_VERIFY_FAILD.equals(order.getStatus())) {
			order.setVerifyTime(DateUtils.getNow());
			order.setVerifyUser(getUser().getRealName());
			order.setVerifyUserId(getUserId());
		}
		scoreGoodsOrderService.update(order);
		return renderSuccessResult();
	}
	
	/**
	 * 录入发货信息
	 * @return
	 */
	@RequestMapping("/operate/scoreshop/enteringDeliverInfo")
	@ResponseBody
	public Map<String, Object> enteringDeliverInfo(ScoreGoodsOrder order){
		scoreGoodsOrderService.enteringDeliverInfo(order);
		return renderSuccessResult();
	}
	
	/**
	 * 确认收货
	 * @return
	 */
	@RequestMapping("/operate/scoreshop/confirmReceive")
	@ResponseBody
	public Map<String, Object> confirmReceive(final String id){
		ScoreGoodsOrder order = new ScoreGoodsOrder();
		order.setUuid(id);
		order.setStatus(Constant.GOODS_ORDER_STATUS_RECEIVE);
		scoreGoodsOrderService.update(order);
		return renderSuccessResult();
	}
}
