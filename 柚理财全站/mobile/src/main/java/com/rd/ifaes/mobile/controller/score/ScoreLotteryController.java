package com.rd.ifaes.mobile.controller.score;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.ip.IPUtil;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.DictUtils;
import com.rd.ifaes.core.score.domain.ScoreLottery;
import com.rd.ifaes.core.score.domain.UserScore;
import com.rd.ifaes.core.score.domain.UserScoreLotteryRecord;
import com.rd.ifaes.core.score.service.ScoreLotteryService;
import com.rd.ifaes.core.score.service.UserScoreLogService;
import com.rd.ifaes.core.score.service.UserScoreLotteryRecordService;
import com.rd.ifaes.core.score.service.UserScoreService;
import com.rd.ifaes.core.sys.domain.DictData;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.mobile.controller.WebController;
import com.rd.ifaes.mobile.model.score.ScoreLotteryItemModel;

/**
 * 积分抽奖
 *
 * @author jxx
 * @version 3.0
 * @date 2017年6月15日
 */
@Controller
public class ScoreLotteryController extends WebController {

    @Resource
    private transient UserScoreLogService userScoreLogService;

    @Resource
    private transient UserScoreService userScoreService;

    @Resource
    private transient UserScoreLotteryRecordService userScoreLotteryRecordService;

    @Resource
    private transient ScoreLotteryService scoreLotteryService;

    
    /**
     * 我的积分及可抽奖次数
     *
     * @return
     * @author jxx
     * @date 2017年6月12日
     */
    @RequestMapping(value = "/app/member/lottery/scoreLotteryInfo")
    @ResponseBody
    public Object scoreLotteryInfo(final HttpServletRequest request) {
        Object obj = null;
        try {
            User user = getAppSessionUser(request);
            UserScore userScore = userScoreService.getUserScore(user.getUuid());
            Map<String, Object> data = new HashMap<String, Object>();
            int scoreLotteryNum = ConfigUtils.getInt(ConfigConstant.SCORE_LOTTERY_NUM);
            int lotteryNum = userScore.getUseScore() / scoreLotteryNum;
            data.put("useScore", userScore.getUseScore());
            data.put("lotteryNum", lotteryNum);
            data.put("scoreLotteryNum", scoreLotteryNum);
            obj = createSuccessAppResponse(data);
        } catch (Exception e) {
            obj = dealException(e);
        }
        return obj;
    }
    
    /**
     * 抽奖记录，默认30条，不用登陆即可查看
     * @return
     * @author jxx
     * @date 2017年6月12日
     */
    @RequestMapping(value = "/app/open/lottery/list")
    @ResponseBody
    public Object list(final HttpServletRequest request, String limit) {
        Object obj = null;
        try {
        	int showRecordCount = 30;//默认显示30条
        	if(StringUtils.isNotBlank(limit)){
        		try {
        			showRecordCount = Integer.parseInt(limit);//默认显示30条
				} catch (Exception e) {
				}
        	}
        	UserScoreLotteryRecord record = new UserScoreLotteryRecord();
        	record.setQueryType(Constant.FLAG_YES);//只查看中奖记录
        	record.setShowCount(showRecordCount);
    		List<DictData> dictDataList = DictUtils.list(DictTypeEnum.LOTTERY_TYPE.getValue());
    		if(dictDataList != null){
    			List<String> lotteryList = new ArrayList<String>();
    			for(DictData dictData:dictDataList){
    				lotteryList.add(dictData.getItemValue());
    			}
    			record.setLotteryTypeList(lotteryList);
    		}
            List<UserScoreLotteryRecord> recordList = userScoreLotteryRecordService.findNewList(record);
            ScoreLotteryItemModel model = null;
            Map<String, Object> data = new HashMap<String, Object>();
            List<ScoreLotteryItemModel> list = new ArrayList<ScoreLotteryItemModel>();
            for (UserScoreLotteryRecord userScoreLotteryRecord : recordList) {
                model = new ScoreLotteryItemModel();
                model.setMobile(userScoreLotteryRecord.getMobile());
                model.setLotteryValue(userScoreLotteryRecord.getLotteryValue());
                model.setLotteryTime(userScoreLotteryRecord.getCreateTime());
                list.add(model);
            }
            List<ScoreLottery> lotteryList = scoreLotteryService.findList(null);
            data.put("lotteryList", lotteryList);
            data.put("recordList", list);//中奖纪录
            String lotteryGameRule = ConfigUtils.getValue(ConfigConstant.SCORE_LOTTERY_GAME_RULE);//游戏规则
    		if(StringUtils.isNotBlank(lotteryGameRule)){
    			lotteryGameRule = lotteryGameRule.replaceAll("\\\\n", "<br/>");  
    		}
    		data.put("lotteryGameRule", lotteryGameRule);//游戏规则
            obj = createSuccessAppResponse(data);
        } catch (Exception e) {
            obj = dealException(e);
        }
        return obj;
    }


    /**
     * 积分抽奖
     *
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/app/member/lottery/doLottery")
    @ResponseBody
    public Object doLottery(final UserScoreLotteryRecord userScoreLotteryRecord, final HttpServletRequest request) {
        Object obj = null;
        try {
            User user = getAppSessionUser(request);
            Map<String, Object> data = new HashMap<String, Object>();
            userScoreLotteryRecord.setCreateIp(IPUtil.getRemortIP(request));

            userScoreLotteryRecord.setUserId(user.getUuid());
            ScoreLottery scoreLottery = userScoreLotteryRecordService.lottety(userScoreLotteryRecord);
            if (scoreLottery != null) {
                data.put("lotteryArea", scoreLottery.getLotteryArea());
                data.put("lotteryName", scoreLottery.getLotteryName());
            } else {
                data.put("lotteryArea", "-1");
            }
            data.put("__sid", SessionUtils.getSession().getId());
            data.put("userId", user.getUuid());
            obj = createSuccessAppResponse(data);
        } catch (Exception e) {
            obj = dealException(e);
        }
        return obj;
    }
}
