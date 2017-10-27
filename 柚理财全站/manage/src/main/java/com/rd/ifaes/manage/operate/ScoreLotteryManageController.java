package com.rd.ifaes.manage.operate;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.score.domain.ScoreLottery;
import com.rd.ifaes.core.score.service.ScoreLotteryService;
import com.rd.ifaes.manage.sys.SystemController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Controller
public class ScoreLotteryManageController extends SystemController {

	@Resource
	private ScoreLotteryService scoreLotteryService;
	
	
	/**
	 * 
	 * 抽奖页面管理
	 * @author jxx
	 * @date 2017年6月14日
	 * @return
	 */
	@RequestMapping(value = "/operate/score/scoreLotteryManage")
	@SystemLog(method= SysLogConstant.QUERY,content= SysLogConstant.SCORE_LOTTERY)
	public String scoreLotteryManage(){
		return "/operate/score/scoreLotteryManage";
	}
	
	
	@RequestMapping(value = "/operate/score/scoreLotteryList")
	@ResponseBody
	@SystemLog(method= SysLogConstant.QUERY,content= SysLogConstant.SCORE_LOTTERY)
	public Object scoreLotteryList(final ScoreLottery scoreLottery) throws Exception {
		scoreLottery.getPage().setOrder("desc");
		return scoreLotteryService.findPage(scoreLottery);
	}
	
	
	@RequestMapping(value = "/operate/score/scoreLotteryEdit")
	@SystemLog(method= SysLogConstant.EDIT,content= SysLogConstant.SCORE_LOTTERY)
	public String scoreLotteryEdit(String id , final Model model) throws Exception {
		ScoreLottery scoreLottery = scoreLotteryService.get(StringUtils.isNull(id));
		model.addAttribute("scoreLottery", scoreLottery);
		return "/operate/score/scoreLotteryEdit";
	}
	
	
	/**
	 * 修改奖项
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/operate/score/scoreLotteryDoEdit")
	@SystemLog(method= SysLogConstant.EDIT,content= SysLogConstant.SCORE_LOTTERY)
	@ResponseBody
	public Object scoreLotteryDoEdit(final ScoreLottery scoreLotteryModel) throws Exception {
		ScoreLottery scoreLottery = scoreLotteryService.get(scoreLotteryModel.getUuid());
		if (scoreLottery == null || scoreLottery.getStatus() == Constant.ENABLE || scoreLotteryModel.getRate().compareTo(new BigDecimal(100)) > 0) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.SCORE_LOTTORY_STATUS_ERROR));
		}
		scoreLotteryService.update(scoreLotteryModel);
		return renderSuccessResult();
	}
	
}
