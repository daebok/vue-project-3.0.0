package com.rd.ifaes.core.score.service.impl;

import com.rd.ifaes.common.dict.DictDataEnum;
import com.rd.ifaes.common.dict.OperateEnum;
import com.rd.ifaes.common.dict.ScoreEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.operate.service.RateCouponRuleService;
import com.rd.ifaes.core.operate.service.RedenvelopeRuleService;
import com.rd.ifaes.core.operate.service.UserRateCouponService;
import com.rd.ifaes.core.operate.service.UserRedenvelopeService;
import com.rd.ifaes.core.score.domain.ScoreLottery;
import com.rd.ifaes.core.score.domain.UserScore;
import com.rd.ifaes.core.score.domain.UserScoreLog;
import com.rd.ifaes.core.score.domain.UserScoreLotteryRecord;
import com.rd.ifaes.core.score.mapper.ScoreLotteryMapper;
import com.rd.ifaes.core.score.mapper.UserScoreLotteryRecordMapper;
import com.rd.ifaes.core.score.service.*;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

/**
 * 积分抽奖
 *
 * @author jxx
 * @version 3.0
 * @date 2017年6月12日
 */
@Service("userScoreLotteryRecordService")
public class UserScoreLotteryRecordServiceImpl extends BaseServiceImpl<UserScoreLotteryRecordMapper, UserScoreLotteryRecord>
        implements UserScoreLotteryRecordService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserScoreLotteryRecordServiceImpl.class);
    @Resource
    private transient UserScoreService userScoreService;
    @Resource
    private transient UserScoreLogService userScoreLogService;
    @Resource
    private transient ScoreGoodsService scoreGoodsService;
    @Resource
    private transient ScoreGoodsStoreService scoreGoodsStoreService;
    @Resource
    private transient ScoreLotteryMapper scoreLotteryMapper;
    @Resource
    private transient RedenvelopeRuleService redenvelopeRuleService;
    @Resource
    private transient UserRedenvelopeService userRedenvelopeService;
    @Resource
    private transient RateCouponRuleService rateCouponRuleService;
    @Resource
    private transient UserRateCouponService userRateCouponService;
    @Resource
    private transient UserService userService;

    @Override
    public List<UserScoreLotteryRecord> findNewList(UserScoreLotteryRecord userScoreLotteryRecord) {
        List<UserScoreLotteryRecord> list = dao.findNewList(userScoreLotteryRecord);
        if (list != null && list.size() > 0) {
            for (UserScoreLotteryRecord record : list) {
            	if(StringUtils.isPhone(record.getMobile())){
            		record.setMobile(StringUtils.hideStr(record.getMobile(), 3, 7));
            	}
            }
        }
        return list;
    }

    @Override
    public ScoreLottery lottety(UserScoreLotteryRecord model) {

        UserScore userScore = userScoreService.getUserScore(model.getUserId());

        int scoreLotteryNum = ConfigUtils.getInt(ConfigConstant.SCORE_LOTTERY_NUM);
        // 校验用户可用积分
        if (userScore.getUseScore() < scoreLotteryNum) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.SCORE_NOT_ENOUGH));
        }
        model.setScore(scoreLotteryNum);

        ScoreLottery scoreLottery = new ScoreLottery();
        List<ScoreLottery> list = scoreLotteryMapper.findList(scoreLottery);

        // 随机数最大范围
        int range = Constant.INT_TEN_THOUSAND;
        // 抽中对象
        scoreLottery = null;
        // 抽中结果名称
        Random r = new Random();
        // 中奖率
        BigDecimal rate = list.get(0).getRate();
        // 产生随机数
        int round = r.nextInt(range);

        LOGGER.info("中奖数{}", round);

        User user = userService.get(model.getUserId());

        String remark = "积分抽奖，扣除积分" + scoreLotteryNum;
        int OptValue = scoreLotteryNum;
        String OptType = OperateEnum.SCORE_OPT_SUB.getValue();
        for (int i = 0; i < list.size(); i++) {
            if (round < BigDecimalUtils.mul(rate, BigDecimal.TEN, BigDecimal.TEN).intValue()) {
                scoreLottery = list.get(i);
//				result = scoreLottery.getUuid();

                model.setLotteryValue(scoreLottery.getLotteryName());
                model.setLotteryType(scoreLottery.getLotteryType());
                model.setCreateTime(DateUtils.getNow());
                model.setGoodsId(scoreLottery.getGoodsId());
                model.setLotteryArea(scoreLottery.getLotteryArea());
                if (DictDataEnum.LOTTERY_TYPE_REDENVELOPE.getValue().equals(model.getLotteryType())) {// TODO 1 换成常量 红包
                    String oneRedId = StringUtils.isNull(redenvelopeRuleService.findRedenvelopeRuleIdByUrl("score" + scoreLottery.getLotteryValue()));
                    userRedenvelopeService.gainActivityRedenvelope(user, oneRedId);
                    remark = "积分抽奖，获得" + scoreLottery.getLotteryName();
                } else if (DictDataEnum.LOTTERY_TYPE_RATECOUPON.getValue().equals(model.getLotteryType())) {
                    String rateId = StringUtils.isNull(rateCouponRuleService.findRateCouponRuleIdByUrl("score" + scoreLottery.getLotteryValue()));
                    userRateCouponService.gainActivityRateCoupon(user, rateId);
                    remark = "积分抽奖，获得" + scoreLottery.getLotteryName();
                } else if (DictDataEnum.LOTTERY_TYPE_INTEGRAL.getValue().equals(model.getLotteryType())) {
                    int sc = Integer.parseInt(scoreLottery.getLotteryValue()) - scoreLotteryNum;
                    OptValue = -sc;
                    if (sc > 0) {
                        model.setScore(sc);
                        remark = "积分抽奖，获得积分" + sc;
                        OptType = OperateEnum.SCORE_OPT_ADD.getValue();
                    } else {
                        model.setScore(-sc);
                    }
                } else {

                }
                LOGGER.info("{}抽中了{}类型的奖品{}", user.getMobile(), scoreLottery.getLotteryType(), scoreLottery.getLotteryValue());
                break;
            }
            if (i != list.size() - 1) {
                rate = BigDecimalUtils.add(rate, list.get(i + 1).getRate());
            }
        }

        // 扣除积分
        userScore = userScoreService.updateUserScore(-OptValue, Constant.INT_ZERO,
                scoreLotteryNum, model.getUserId());

        model.setRemark(remark);
        // 积分生成记录
        UserScoreLog log = new UserScoreLog();
        log.setCreateTime(DateUtils.getNow());
        log.setNewRecord(true);
        log.setUserId(model.getUserId());
        log.setTotalScore(userScore.getTotalScore());
        log.setUseScore(userScore.getUseScore());
        log.setNoUseScore(userScore.getNoUseScore());
        log.setOptValue(model.getScore());
        log.setOptType(OptType);
        log.setTypeName(ScoreEnum.SCORE_LOTTERY.getName());
        log.setTypeCode(ScoreEnum.SCORE_LOTTERY.getValue());
        log.setRemark(remark);
        userScoreLogService.save(log);

        if (scoreLottery != null) {
            doSave(model, userScore);
        }
        return scoreLottery;
    }

    private void doSave(UserScoreLotteryRecord model, UserScore userScore) {
//		String goodsId = model.getGoodsId();
//		if(StringUtils.isNotBlank(goodsId)){
//			ScoreGoods goods = scoreGoodsService.get(model.getGoodsId());
//			int lessNum = scoreGoodsStoreService.queryGoodsLessNum(model.getGoodsId());
//			
//			// 校验商品剩余数量
//			if (goods !=null && lessNum < Constant.INT_ONE) {
//				throw new BussinessException(ResourceUtils.get(ResourceConstant.SCORE_GOODS_NUM_NONE));
//			}
//		}

        //抽奖记录保存
        save(model);

    }

}
