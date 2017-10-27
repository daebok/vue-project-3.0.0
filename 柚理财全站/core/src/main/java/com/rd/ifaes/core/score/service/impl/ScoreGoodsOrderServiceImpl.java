package com.rd.ifaes.core.score.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.rd.ifaes.common.annotation.Cacheable;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.dict.OperateEnum;
import com.rd.ifaes.common.dict.ScoreEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.NumberUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.MessageConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.operate.domain.UserActivityAwardLog;
import com.rd.ifaes.core.operate.domain.UserRateCoupon;
import com.rd.ifaes.core.operate.domain.UserRedenvelope;
import com.rd.ifaes.core.operate.service.RateCouponRuleService;
import com.rd.ifaes.core.operate.service.RedenvelopeRuleService;
import com.rd.ifaes.core.operate.service.UserActivityAwardLogService;
import com.rd.ifaes.core.operate.service.UserRateCouponService;
import com.rd.ifaes.core.operate.service.UserRedenvelopeService;
import com.rd.ifaes.core.score.domain.ScoreGoods;
import com.rd.ifaes.core.score.domain.ScoreGoodsOrder;
import com.rd.ifaes.core.score.domain.ScoreGoodsStore;
import com.rd.ifaes.core.score.domain.UserScore;
import com.rd.ifaes.core.score.domain.UserScoreLog;
import com.rd.ifaes.core.score.mapper.ScoreGoodsOrderMapper;
import com.rd.ifaes.core.score.service.ScoreGoodsOrderService;
import com.rd.ifaes.core.score.service.ScoreGoodsService;
import com.rd.ifaes.core.score.service.ScoreGoodsStoreService;
import com.rd.ifaes.core.score.service.UserScoreLogService;
import com.rd.ifaes.core.score.service.UserScoreService;
import com.rd.ifaes.core.sys.service.MessageService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.service.UserService;

/**
 * ServiceImpl:商品订单管理service
 * 
 * @author ywt
 * @version 3.0
 * @date 2017-03-03
 */
@Service("scoreGoodsOrderService")
public class ScoreGoodsOrderServiceImpl extends BaseServiceImpl<ScoreGoodsOrderMapper, ScoreGoodsOrder>
		implements ScoreGoodsOrderService {
	@Resource
	private transient ScoreGoodsService scoreGoodsService;
	@Resource
	private transient ScoreGoodsStoreService scoreGoodsStoreService;
	@Resource
	private transient UserScoreService userScoreService;
	@Resource
	private transient UserScoreLogService userScoreLogService;
	@Resource
	private transient RedenvelopeRuleService redenvelopeRuleService;
	@Resource
	private transient RateCouponRuleService rateCouponRuleService;
	@Resource
	private transient UserService userService;
	/** 用户奖励日志处理 */
	@Resource
	private transient UserActivityAwardLogService userActivityAwardLogService;
	@Resource
	private transient UserRedenvelopeService userRedenvelopeService;
	@Resource
	private transient UserRateCouponService userRateCouponService;
	@Resource
	private transient MessageService messageService;

	@Override
	@Cacheable(key=CacheConstant.KEY_NEW_ORDER,expire = ExpireTime.ONE_MIN)
	public List<ScoreGoodsOrder> getNewOrders() {
		return dao.getNewOrders();
	}
	
	@Transactional(readOnly = false)
	@Override
	public void exchangeGoods(ScoreGoodsOrder scoreGoodsOrder) {
		UserScore score = userScoreService.getUserScore(scoreGoodsOrder.getUserId());
		int lessNum = scoreGoodsStoreService.queryGoodsLessNum(scoreGoodsOrder.getGoodsId());
		validExchange(scoreGoodsOrder, lessNum, score);
		ScoreGoodsStore scoreGoodsStore = new ScoreGoodsStore(scoreGoodsOrder.getGoodsId(), scoreGoodsOrder.getNum(),
				Constant.INT_ZERO);
		if (scoreGoodsStoreService.updateGoodsStore(scoreGoodsStore) != 1) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.UPDATE_GOODS_STORE_ERROR));
		}
		// 保存订单 冻结库存
		scoreGoodsOrder.setNewRecord(true);
		scoreGoodsOrder.setCreateTime(DateUtils.getNow());
		scoreGoodsOrder.setStatus(Constant.STRING_ZERO); // 订单创建
		save(scoreGoodsOrder);
		// 冻结积分
		userScoreService.updateUserScore(-scoreGoodsOrder.getScore(), scoreGoodsOrder.getScore(),
				Constant.INT_ZERO, scoreGoodsOrder.getUserId());
		// 生成记录
		UserScoreLog log = new UserScoreLog();
		log.setCreateTime(DateUtils.getNow());
		log.setNewRecord(true);
		log.setUserId(scoreGoodsOrder.getUserId());
		log.setTotalScore(score.getTotalScore());
		log.setUseScore(score.getTotalScore() - scoreGoodsOrder.getScore());
		log.setNoUseScore(scoreGoodsOrder.getScore());
		log.setOptValue(scoreGoodsOrder.getScore());
		log.setOptType(OperateEnum.SCORE_OPT_SUB.getValue());
		log.setTypeName(ScoreEnum.SCORE_EXCHANGE.getName());
		log.setTypeCode(ScoreEnum.SCORE_EXCHANGE.getValue());
		log.setRemark(StringUtils.format(ResourceUtils.get(ResourceConstant.EXCHANGE_GOODS_APPLY_REMARK),scoreGoodsOrder.getGoodsName(),scoreGoodsOrder.getNum(),scoreGoodsOrder.getScore()));
		userScoreLogService.save(log);
	}

	@Override
	@Transactional(readOnly=false)
	public void update(ScoreGoodsOrder order) {
		ScoreGoods goods = scoreGoodsService.get(order.getGoodsId());
		UserScore score = userScoreService.getUserScore(order.getUserId());
		User user = userService.get(order.getUserId());
		if (Constant.GOODS_ORDER_STATUS_VERIFY_SUCCESS.equals(order.getStatus())) {
			//跟新库存 审核成功 解冻冻结库存 增加售出数量
			ScoreGoodsStore scoreGoodsStore = new ScoreGoodsStore(order.getGoodsId(), -order.getNum(),order.getNum());
			scoreGoodsStoreService.updateGoodsStore(scoreGoodsStore);
			//跟新积分  审核成功 解冻冻结积分 减少可用积分 增加已用积分
			userScoreService.updateUserScore(Constant.INT_ZERO, -order.getScore(), order.getScore(), order.getUserId());
			// 生成记录
			UserScoreLog log = new UserScoreLog();
			log.setCreateTime(DateUtils.getNow());
			log.setNewRecord(true);
			log.setUserId(order.getUserId());
			log.setTotalScore(score.getTotalScore() - order.getScore());
			log.setUseScore(score.getTotalScore() - order.getScore());
			log.setNoUseScore(score.getNoUseScore() - order.getScore());
			log.setOptValue(order.getScore());
			log.setOptType(OperateEnum.SCORE_OPT_SUB.getValue());
			log.setTypeName(ScoreEnum.SCORE_EXCHANGE_VERIFY_SUCCESS.getName());
			log.setTypeCode(ScoreEnum.SCORE_EXCHANGE_VERIFY_SUCCESS.getValue());
			log.setRemark(StringUtils.format(ResourceUtils.get(ResourceConstant.EXCHANGE_GOODS_REMARK),order.getGoodsName(),order.getNum(),order.getScore()));
			userScoreLogService.save(log);
			//虚拟物品直接发放
			if (Constant.FLAG_YES.equals(goods.getIsVirtual())) { //如果是虚拟物品
				order.setStatus(Constant.GOODS_ORDER_STATUS_RECEIVE); 
				List<UserActivityAwardLog> logs = new ArrayList<>();
				if (Constant.FLAG_YES.equals(goods.getVirtualType())) { //如果是加息卷
					List<UserRateCoupon> urvs = new ArrayList<>();
					for(int i=0;i<order.getNum();i++){
						rateCouponRuleService.grantRateCoupon(user, rateCouponRuleService.get(goods.getRuleId()), null, null, urvs, logs);
					}
					
					if(!CollectionUtils.isEmpty(urvs)){
						userRateCouponService.insertBatch(urvs);
					}
				}else if (Constant.FLAG_NO.equals(goods.getVirtualType())) { //如果是红包
					List<UserRedenvelope> urvs = new ArrayList<>();
					for(int i=0;i<order.getNum();i++){
						redenvelopeRuleService.grantRedenvelope(user, redenvelopeRuleService.get(goods.getRuleId()), null, null, urvs, logs);
					}
					if(!CollectionUtils.isEmpty(urvs)){
						userRedenvelopeService.insertBatch(urvs);
					}
				}
				if(!CollectionUtils.isEmpty(logs)){
					userActivityAwardLogService.insertBatch(logs);				
				}
			}
		}else if (Constant.GOODS_ORDER_STATUS_VERIFY_FAILD.equals(order.getStatus())) {
			//跟新库存  审核失败解冻冻结库存
			ScoreGoodsStore scoreGoodsStore = new ScoreGoodsStore(order.getGoodsId(), -order.getNum(),Constant.INT_ZERO);
			scoreGoodsStoreService.updateGoodsStore(scoreGoodsStore);
			//跟新积分 审核失败解冻冻结积分
			userScoreService.updateUserScore(order.getScore(), -order.getScore(), Constant.INT_ZERO, order.getUserId());
			// 生成记录
			UserScoreLog log = new UserScoreLog();
			log.setCreateTime(DateUtils.getNow());
			log.setNewRecord(true);
			log.setUserId(order.getUserId());
			log.setTotalScore(score.getTotalScore() + order.getScore());
			log.setUseScore(score.getTotalScore() + order.getScore());
			log.setNoUseScore(score.getNoUseScore() - order.getScore());
			log.setOptValue(order.getScore());
			log.setOptType(OperateEnum.SCORE_OPT_ADD.getValue());
			log.setTypeName(ScoreEnum.SCORE_EXCHANGE_VERIFY_FAILD.getName());
			log.setTypeCode(ScoreEnum.SCORE_EXCHANGE_VERIFY_FAILD.getValue());
			log.setRemark(StringUtils.format(ResourceUtils.get(ResourceConstant.EXCHANGE_GOODS_FAILD_REMARK),order.getGoodsName(),order.getNum(),order.getScore()));
			userScoreLogService.save(log);
		}
		super.update(order);
	}

	/**
	 * 兑换校验
	 * 
	 * @param scoreGoodsOrder
	 * @param lessNum
	 * @param score
	 */
	public void validExchange(ScoreGoodsOrder scoreGoodsOrder, int lessNum, UserScore score) {
		ScoreGoods goods = scoreGoodsService.get(scoreGoodsOrder.getGoodsId());
		if (!Constant.GOODS_STATUS_SALE_ON.equals(goods.getStatus())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GOODS_IS_OVER_STOP_SALE));
		}
		// 校验商品剩余数量
		if (lessNum < scoreGoodsOrder.getNum()) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.SCORE_GOODS_NUM_NONE));
		}
		// 兑换所需积分
		int needScore = BigDecimalUtils.mul(NumberUtils.getBigDecimal(scoreGoodsOrder.getNum()),NumberUtils.getBigDecimal(goods.getScore())).intValue();
		scoreGoodsOrder.setScore(needScore);
		if(needScore < 0){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.SCORE_CANNOT_LT_ZERO));
		}
		// 校验用户可用积分
		if (score.getUseScore() < needScore) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.SCORE_NOT_ENOUGH));
		}
		// 校验是否超过每个人的最大兑换数量
		if (goods.getExchangeLimit()!=Constant.INT_ZERO &&   //兑换数量有限制并且已兑换的+本次要兑换的数量是否大于限制值
				(countUserExchangeOrders(scoreGoodsOrder.getUserId(),scoreGoodsOrder.getGoodsId())+scoreGoodsOrder.getNum()) >goods.getExchangeLimit()) {
			throw new BussinessException(StringUtils.format(ResourceUtils.get(ResourceConstant.EXCHANGE_GOODS_LIMIT_ERROR), scoreGoodsOrder.getGoodsName(),goods.getExchangeLimit()));
		}
		if(scoreGoodsOrder.getReceiveRemark() != null && scoreGoodsOrder.getReceiveRemark().length() > Constant.ENTITY_LENGTH_FOUR){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.SCORE_RECEIVE_REMARK_TOO_LONG));
		}
		
	}
	
	@Cacheable(key=CacheConstant.KEY_PREFIX_USER_ORDER_STATUS_NUM+"{userId}"+":"+"{status}",expire=ExpireTime.HALF_A_MIN)
	@Override
	public int countOrdersByStatus(String userId,String status) {
		Integer result = dao.countOrdersByStatus(userId,status);
		return result==null?0:result.intValue();
	}

	@Override
	public List<ScoreGoodsOrder> findListExceptWaitVerify(String keywords) {
		return dao.findListExceptWaitVerify(keywords);
	}

	@Override
	public int countUserExchangeOrders(String userId, String goodsId) {
		Integer result = dao.countUserExchangeOrders(userId, goodsId);
		return result==null?0:result.intValue();
	}

	@Override
	public void enteringDeliverInfo(ScoreGoodsOrder order) {
		//发送站内信
		Map<String, Object> sendData = new HashMap<String, Object>();
		ScoreGoodsOrder temp = get(order.getUuid());
		User user = userService.get(temp.getUserId());
		sendData.put("user", user);
		sendData.put("userName",user.getUserName());
		sendData.put("time",temp.getCreateTime());
		sendData.put("goodsName",temp.getGoodsName());
		sendData.put("goodsNum",temp.getNum());
		sendData.put("expressName",order.getExpressName());
		sendData.put("expressNo",order.getExpressNo());
		sendData.put("isVirtual",scoreGoodsService.get(temp.getGoodsId()).getIsVirtual());
		messageService.sendMessage(MessageConstant.DELIVER_SUCC, sendData);
		order.setDeliverTime(DateUtils.getNow());
		super.update(order);
	}

}
