package com.rd.ifaes.web.controller.scoreshop;

import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.util.SessionUtils;
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
import com.rd.ifaes.web.controller.WebController;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 积分抽奖
 * @version 3.0
 * @author jxx
 * @date 2017年6月12日
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
	 * 
	 * 积分抽奖页面
	 * @author jxx
	 * @date 2017年6月12日
	 * @return
	 */
	@RequestMapping(value = "/score/lotteryDraw")
	public String lotteryDraw(){
		return "/scoreshop/lotteryDraw";
	}
	
	/**
	 * 
	 * 抽奖记录，默认30条
	 * @author jxx
	 * @date 2017年6月12日
	 * @return
	 */
	@RequestMapping(value = "/score/lottery/list")
	@ResponseBody
	public Object list(){
		Map<String, Object> data = renderSuccessResult();
		UserScoreLotteryRecord userScoreLotteryRecord = new UserScoreLotteryRecord();
		userScoreLotteryRecord.setQueryType(Constant.FLAG_YES);//只查看中奖记录
		userScoreLotteryRecord.setShowCount(30);//显示30条
		List<DictData> list = DictUtils.list(DictTypeEnum.LOTTERY_TYPE.getValue());
		if(list != null){
			List<String> lotteryList = new ArrayList<String>();
			for(DictData dictData:list){
				lotteryList.add(dictData.getItemValue());
			}
			userScoreLotteryRecord.setLotteryTypeList(lotteryList);
		}
		data.put("recordlist", userScoreLotteryRecordService.findNewList(userScoreLotteryRecord));
		data.put("lotterylist", scoreLotteryService.findList(null));
		String lotteryGameRule = ConfigUtils.getValue(ConfigConstant.SCORE_LOTTERY_GAME_RULE);//游戏规则
		if(StringUtils.isNotBlank(lotteryGameRule)){
			lotteryGameRule = lotteryGameRule.replaceAll("\\\\n", "<br/>");  
		}
		data.put("lotteryGameRule", lotteryGameRule);//游戏规则
		return data;
	}
	
	/**
	 * 
	 * 我的积分及可抽奖次数
	 * @author jxx
	 * @date 2017年6月12日
	 * @return
	 */
	@RequestMapping(value = "/score/lottery/scoreLotteryInfo")
	@ResponseBody
	public Object scoreLotteryInfo() {
		User user = SessionUtils.getSessionUser();
		Map<String, Object> data = renderSuccessResult();
		UserScore userScore = userScoreService.getUserScore(user.getUuid());
		int scoreLotteryNum = ConfigUtils.getInt(ConfigConstant.SCORE_LOTTERY_NUM);
		int lotteryNum =  userScore.getUseScore()/scoreLotteryNum;
		data.put("useScore", userScore.getUseScore());
		data.put("lotteryNum",lotteryNum);
		data.put("scoreLotteryNum",scoreLotteryNum);
		return data;
	}
	
	/**
	 * 积分抽奖
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/score/lottery/doLottery")
	@ResponseBody
	public Object doLottery(final UserScoreLotteryRecord userScoreLotteryRecord, final HttpServletRequest request) {
		User user = SessionUtils.getSessionUser();
		Map<String, Object> data = renderSuccessResult();

		userScoreLotteryRecord.setCreateIp(IPUtil.getRemortIP(request));
		userScoreLotteryRecord.setUserId(user.getUuid());
		ScoreLottery scoreLottery = userScoreLotteryRecordService.lottety(userScoreLotteryRecord);
		if(scoreLottery != null){
			data.put("lotteryArea", scoreLottery.getLotteryArea());
		}else{
			data.put("lotteryArea", "-1");
		}
		return data;
	}
}
