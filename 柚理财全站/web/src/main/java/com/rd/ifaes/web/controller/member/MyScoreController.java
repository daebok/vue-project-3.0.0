package com.rd.ifaes.web.controller.member;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.score.domain.ScoreGoodsOrder;
import com.rd.ifaes.core.score.domain.UserScoreLog;
import com.rd.ifaes.core.score.service.ScoreGoodsOrderService;
import com.rd.ifaes.core.score.service.UserScoreLogService;
import com.rd.ifaes.core.score.service.UserScoreService;
import com.rd.ifaes.core.sys.service.ConfigService;
import com.rd.ifaes.web.controller.WebController;

/**
 * 
 *  我的资产-我的积分
 * @version 3.0
 * @author ywt
 * @date 2017年03月06日
 */
@Controller
public class MyScoreController extends WebController {
	
	@Resource
	private transient UserScoreLogService userScoreLogService;
	@Resource
	private transient ScoreGoodsOrderService scoreGoodsOrderService;
	@Resource
	private transient UserScoreService userScoreService;
	@Resource
	private ConfigService configService;
	
	
	/**
	 * 跳转积分记录页面
	 * @return
	 */
	@RequestMapping(value = "/member/myScore/scoreIn")
	public String scoreLogs(final Model model){
		model.addAttribute("waitVerifyNum", scoreGoodsOrderService.countOrdersByStatus(getUserId(),Constant.GOODS_ORDER_STATUS_WAIT));
		model.addAttribute("verifySuccessNum", scoreGoodsOrderService.countOrdersByStatus(getUserId(),Constant.GOODS_ORDER_STATUS_VERIFY_SUCCESS));
		model.addAttribute("userScore", userScoreService.getUserScore(StringUtils.isNull(getUserId())));
		return "/member/myScore/scoreIn";
	}
	
	/**
	 * 跳转我的兑换页面
	 * @return
	 */
	@RequestMapping(value = "/member/myScore/scoreOut")
	public String scoreOut(final Model model){
		model.addAttribute("waitVerifyNum", scoreGoodsOrderService.countOrdersByStatus(getUserId(),Constant.GOODS_ORDER_STATUS_WAIT));
		model.addAttribute("verifySuccessNum", scoreGoodsOrderService.countOrdersByStatus(getUserId(),Constant.GOODS_ORDER_STATUS_VERIFY_SUCCESS));
		model.addAttribute("userScore", userScoreService.getUserScore(StringUtils.isNull(getUserId())));
		return "/member/myScore/scoreOut";
	}
	
	/**
	 * 跳转我的收货页面
	 * @return
	 */
	@RequestMapping(value = "/member/myScore/myReceivingPage")
	public String myReceivingPage(final Model model){
		model.addAttribute(ConfigConstant.WEB_MAXRECEIVENUM, configService.findByCode(ConfigConstant.WEB_MAXRECEIVENUM).getConfigValue());
		return "/member/myScore/myReceivingPage";
	}
	
	/**
	 * 获取积分记录
	 * @param goodsCategoryId
	 * @return
	 */
	@RequestMapping(value = "/member/myScore/getScoreLogs")
	@ResponseBody
	public Object getScoreLogs(final UserScoreLog userScoreLog){
		userScoreLog.setUserId(getUserId());
		userScoreLog.putQueryTimeSection();
		Map<String, Object> data = renderSuccessResult();
		data.put("data", userScoreLogService.findPage(userScoreLog));
		return data;
	}
	
	/**
	 * 获取订单记录
	 * @param goodsCategoryId
	 * @return
	 */
	@RequestMapping(value = "/member/myScore/getScoreOrders")
	@ResponseBody
	public Object getScoreOrders(final ScoreGoodsOrder scoreGoodsOrder){
		scoreGoodsOrder.setUserId(getUserId());
		scoreGoodsOrder.putQueryTimeSection();
		Map<String, Object> data = renderSuccessResult();
		data.put("data", scoreGoodsOrderService.findPage(scoreGoodsOrder));
		return data;
	}
}
