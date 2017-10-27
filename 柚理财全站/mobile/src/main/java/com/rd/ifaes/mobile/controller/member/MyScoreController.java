package com.rd.ifaes.mobile.controller.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.DictUtils;
import com.rd.ifaes.core.score.domain.ScoreGoodsOrder;
import com.rd.ifaes.core.score.domain.UserScore;
import com.rd.ifaes.core.score.domain.UserScoreLog;
import com.rd.ifaes.core.score.domain.UserScoreLotteryRecord;
import com.rd.ifaes.core.score.service.ScoreGoodsOrderService;
import com.rd.ifaes.core.score.service.UserScoreLogService;
import com.rd.ifaes.core.score.service.UserScoreLotteryRecordService;
import com.rd.ifaes.core.score.service.UserScoreService;
import com.rd.ifaes.core.sys.domain.DictData;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.mobile.controller.WebController;
import com.rd.ifaes.mobile.model.PagedData;
import com.rd.ifaes.mobile.model.score.ScoreGoodsOrderItemModel;
import com.rd.ifaes.mobile.model.score.ScoreLogItemModel;
import com.rd.ifaes.mobile.model.score.ScoreLotteryItemModel;

/**
 * 
 * 我的资产-我的积分
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
	private transient UserScoreLotteryRecordService userScoreLotteryRecordService;
	
	
	/**
	 * 获取积分记录
	 * @param goodsCategoryId
	 * @return
	 */
	@RequestMapping(value = "/app/member/myScore/getScoreLogs")
	@ResponseBody
	public Object getScoreLogs(final UserScoreLog userScoreLog,HttpServletRequest request){
		Object obj;
		try{
			User user=getAppSessionUser(request);
			userScoreLog.setUserId(user.getUuid());
			userScoreLog.putQueryTimeSection();
			Page<UserScoreLog> pages= userScoreLogService.findPage(userScoreLog);
			int page=userScoreLog.getPage().getPage();
			List<UserScoreLog> userScoreLogList = pages.getRows();
			PagedData<UserScoreLog> pirlist = new PagedData<UserScoreLog>();
			if(userScoreLogList!=null){
				pages.setPageSize(pages.getRows().size());
				pages.setPage(page);
				fillPageDatas(pirlist, pages);
				for (UserScoreLog scoreLog : userScoreLogList) {
					pirlist.getList().add(scoreLog);
				}
			}
			obj=createSuccessAppResponse(pirlist);
		}catch(Exception e){
			obj=dealException(e);
		}
		return obj;
	}
	
	/**
	 * 获取订单记录
	 * @param goodsCategoryId
	 * @return
	 */
	@RequestMapping(value = "/app/member/myScore/getScoreOrders")
	@ResponseBody
	public Object getMyScoreOrders(final ScoreGoodsOrder scoreGoodsOrder, HttpServletRequest request){
		Object obj;
		try{
			User user=getAppSessionUser(request);
			scoreGoodsOrder.setUserId(user.getUuid());
			scoreGoodsOrder.putQueryTimeSection();
			Page<ScoreGoodsOrder> pages= scoreGoodsOrderService.findPage(scoreGoodsOrder);
			int page=scoreGoodsOrder.getPage().getPage();
			List<ScoreGoodsOrder> ScoreGoodsOrderList = pages.getRows();
			PagedData<ScoreGoodsOrder> pirlist = new PagedData<ScoreGoodsOrder>();
			if(ScoreGoodsOrderList!=null){
				pages.setPageSize(pages.getRows().size());
				pages.setPage(page);
				fillPageDatas(pirlist, pages);
				for (ScoreGoodsOrder goodsOrder : ScoreGoodsOrderList) {
					pirlist.getList().add(goodsOrder);
				}
			}
			obj=createSuccessAppResponse(pirlist);
		}catch(Exception e){
			obj=dealException(e);
		}
		return obj;
	}
	
	/**
	 * 获取审核信息
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "/app/member/myScore/getVerifyNum")
	@ResponseBody
	public Object getVerifyNum(HttpServletRequest request){
		Map<String, Object> data = new HashMap<String, Object>();
		Object obj=null;
		try {
			User user=getAppSessionUser(request);
			data.put("waitVerifyNum", scoreGoodsOrderService.countOrdersByStatus(user.getUuid(),Constant.GOODS_ORDER_STATUS_WAIT));
			data.put("verifySuccessNum", scoreGoodsOrderService.countOrdersByStatus(user.getUuid(),Constant.GOODS_ORDER_STATUS_VERIFY_SUCCESS));
			obj=createSuccessAppResponse(data);
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
	}
	
	/**
	 * 
	 * 我的积分
	 * @author jxx
	 * @date 2017年6月12日
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/app/member/score/myScore")
	@ResponseBody
	public Object myScore(HttpServletRequest request) {
		Object obj = null;
		try {
			User user = getAppSessionUser(request);
			Map<String, Object> map = new HashMap<String, Object>();
			UserScore userScore = userScoreService.getUserScore(StringUtils.isNull(user.getUuid()));
			map.put("totalScore", userScore.getTotalScore());
			map.put("useScore", userScore.getUseScore());
			obj = createSuccessAppResponse(map);
		} catch (Exception e) {
			obj = dealException(e);
		}
		return obj;
	}
	
	/**
	 * 
	 * 积分记录
	 * @author jxx
	 * @date 2017年6月12日
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/app/member/score/getScoreLogList")
	@ResponseBody
	public Object getScoreLogList(final UserScoreLog userScoreLog, HttpServletRequest request) {
		Object obj = null;
		try {
			User user = getAppSessionUser(request);
			userScoreLog.setUserId(user.getUuid());
			
			PagedData<ScoreLogItemModel> list = new PagedData<ScoreLogItemModel>();
			
			userScoreLog.setOptType("1");
			Page<UserScoreLog> page =  userScoreLogService.findPage(userScoreLog);
			
			int pages = userScoreLog.getPage().getPage();
			List<UserScoreLog> scoreLogList = page.getRows();
			page.setPage(pages);
			fillPageData(list, page);
			for (UserScoreLog scoreLog : scoreLogList) {
				ScoreLogItemModel model = new ScoreLogItemModel();
				model.setConvertTime(scoreLog.getCreateTime());
				model.setOptType(scoreLog.getOptType());
				model.setOptValue(scoreLog.getOptValue());
				model.setTypeName(scoreLog.getTypeName());
				list.getList().add(model);
			}
			
			obj = createSuccessAppResponse(list);
		} catch (Exception e) {
			obj = dealException(e);
		}
		return obj;
	}
	
	
	/**
	 * 获取订单记录
	 * @param goodsCategoryId
	 * @return
	 */
	@RequestMapping(value = "/app/member/score/getScoreOrders")
	@ResponseBody
	public Object getScoreOrders(final ScoreGoodsOrder scoreGoodsOrder, HttpServletRequest request){
		Object obj = null;
		try {
			User user = getAppSessionUser(request);
			scoreGoodsOrder.setUserId(user.getUuid());
			PagedData<ScoreGoodsOrderItemModel> list = new PagedData<ScoreGoodsOrderItemModel>();
			Page<ScoreGoodsOrder> page =  scoreGoodsOrderService.findPage(scoreGoodsOrder);
			int pages = scoreGoodsOrder.getPage().getPage();
			List<ScoreGoodsOrder> scoreLogList = page.getRows();
			page.setPage(pages);
			fillPageData(list, page);
			for (ScoreGoodsOrder goodsOrder : scoreLogList) {
				ScoreGoodsOrderItemModel model = new ScoreGoodsOrderItemModel();
				model.setConvertTime(goodsOrder.getCreateTime());
				model.setConvertGoods(goodsOrder.getGoodsName());
				model.setScore(goodsOrder.getScore());
				list.getList().add(model);
			}
			obj = createSuccessAppResponse(list);
		} catch (Exception e) {
			obj = dealException(e);
		}
		return obj;
	}
	
	
	/**
	 * 获取抽奖记录
	 * @param goodsCategoryId
	 * @return
	 */
	@RequestMapping(value = "/app/member/score/getScoreLottery")
	@ResponseBody
	public Object getScoreLottery(final UserScoreLotteryRecord userScoreLotteryRecord, HttpServletRequest request){
		Object obj = null;
		try {
			User user = getAppSessionUser(request);
			userScoreLotteryRecord.setUserId(user.getUuid());
			PagedData<ScoreLotteryItemModel> list = new PagedData<ScoreLotteryItemModel>();
			//排除谢谢参与的抽奖记录（积分类型，积分奖励=一次抽奖消耗的积分）
			userScoreLotteryRecord.setQueryType(Constant.FLAG_YES);//只查看中奖记录
			List<DictData> dictDataList = DictUtils.list(DictTypeEnum.LOTTERY_TYPE.getValue());
			if(dictDataList != null){
				List<String> lotteryList = new ArrayList<String>();
				for(DictData dictData:dictDataList){
					lotteryList.add(dictData.getItemValue());
				}
				userScoreLotteryRecord.setLotteryTypeList(lotteryList);
			}
			Page<UserScoreLotteryRecord> page =  userScoreLotteryRecordService.findPage(userScoreLotteryRecord);
			int pages = userScoreLotteryRecord.getPage().getPage();
			List<UserScoreLotteryRecord> scoreLotteryList = page.getRows();
			page.setPage(pages);
			fillPageData(list, page);
			for (UserScoreLotteryRecord scoreLotteryRecord : scoreLotteryList) {
				ScoreLotteryItemModel model = new ScoreLotteryItemModel();
				model.setLotteryValue(scoreLotteryRecord.getLotteryValue());
				model.setLotteryTime(scoreLotteryRecord.getCreateTime());
				list.getList().add(model);
			}
			obj = createSuccessAppResponse(list);
		} catch (Exception e) {
			obj = dealException(e);
		}
		return obj;
	}
}
