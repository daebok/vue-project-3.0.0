package com.rd.ifaes.manage.operate;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.dict.OperateEnum;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.excel.ExportModel;
import com.rd.ifaes.common.util.excel.ExportUtil;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.score.domain.ScoreType;
import com.rd.ifaes.core.score.domain.UserScore;
import com.rd.ifaes.core.score.domain.UserScoreLog;
import com.rd.ifaes.core.score.service.ScoreTypeService;
import com.rd.ifaes.core.score.service.UserScoreLogService;
import com.rd.ifaes.core.score.service.UserScoreService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.manage.sys.SystemController;
import com.timevale.tgpdfsign.sign.StringUtil;
/**
 * 
 * 积分管理
 * @version 3.0
 * @author wyw
 * @date 2016-8-4
 */
@Controller
public class ScoreManageController extends SystemController {
    /**
     * 用户积分 业务处理
     */
	@Resource
    private transient UserScoreService userScoreService;
	 /**
     * 用户积分日志 业务处理
     */
    @Resource
    private transient UserScoreLogService userScoreLogService;
    @Resource
    private transient ScoreTypeService scoreTypeService;
    @Resource
    private transient UserService userService;
    
  //路径拼接符 "/"
  	private static String separator = File.separator;
	/**
	 * 
	 * 用户积分页面
	 * @author wyw
	 * @date 2016-8-8
	 * @return
	 */
	@RequestMapping(value = "/operate/score/userScoreManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_SCORE)
	public String userScoreManage(){
		return "/operate/score/userScoreManage";
	}
	/**
	 * 
	 * 用户积分数据接口
	 * @author wyw
	 * @date 2016-8-8
	 * @param userScore
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/operate/score/userScoreList", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("oper:score:userScore:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_SCORE)
	public Object userScoreList( final UserScore userScore ,final  HttpServletRequest request){
		 return userScoreService.findPage(userScore);
	}
	/**
	 * 
	 * 用户积分日志页面
	 * @author wyw
	 * @date 2016-8-8
	 * @return
	 */
	@RequestMapping(value = "/operate/score/userScoreLogManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_SCORE_LOG)
	public String userScoreLogManage(){
		return "/operate/score/userScoreLogManage";
	}
	/**
	 * 
	 * 用户积分日志数据
	 * @author wyw
	 * @date 2016-8-8
	 * @param userScoreLog
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/operate/score/userScoreLogList", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("oper:score:scoreList:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_SCORE_LOG)
	public Object userScoreLogList( final UserScoreLog userScoreLog , final HttpServletRequest request){
		userScoreLog.getPage().setSort("create_time");
		userScoreLog.getPage().setOrder("desc");
		return userScoreLogService.findPage(userScoreLog);
	}
	

    /**
	 * 导出用户积分记录
	 * @param model
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/operate/score/exportUserScore")
	@RequiresPermissions("oper:score:scoreList:export")
	@SystemLog(method=SysLogConstant.EXPORT,content=SysLogConstant.USER_SCORE)
	public void exportBorrowRecord(final UserScore model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ExportUtil.exportExcel(new ExportModel<UserScore>() {
			@Override
			public String getCacheKey() {
				return  ScoreManageController.this.getUserId();
			}
			@Override
			public UserScore getModel() {
				return model;
			}
			@Override
			public int getTotal(UserScore entity) {
				return userScoreService.getListCount(entity.getKeywords());
			}
			@Override
			public List<UserScore> getPageRecords(UserScore entity) {
				return userScoreService.findPage(entity).getRows();
			}
		}, request, response);
	}
	
	/**
	 * 导出用户积分日志记录
	 * @param model
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/operate/score/exportScoreLog")
	@RequiresPermissions("oper:score:userScore:export")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_SCORE_LOG)
	public void exportScoreLog(final UserScoreLog model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ExportUtil.exportExcel(new ExportModel<UserScoreLog>() {
			@Override
			public String getCacheKey() {
				return  ScoreManageController.this.getUserId();
			}
			@Override
			public UserScoreLog getModel() {
				return model;
			}
			@Override
			public int getTotal(UserScoreLog entity) {
				return userScoreLogService.getListCount(entity);
			}
			@Override
			public List<UserScoreLog> getPageRecords(UserScoreLog entity) {
				return userScoreLogService.findPage(entity).getRows();
			}
		}, request, response);
	 
	}
	
	
	/*******************手动发放积分***********************/
	@RequestMapping(value = "/loan/score/scoreByHand")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_SCORE)
	public String record(String userId, Model model) {
		LOGGER.info("待处理:{}",userId);
		model.addAttribute("userId", userId) ;
		return "/loan/score/scoreByHand" ;
	}
	
	@RequestMapping(value = "/loan/score/sendScore")
	@RequiresPermissions("oper:score:userScore:score_grant")
	@ResponseBody
	@SystemLog(method=SysLogConstant.GRANT,content=SysLogConstant.USER_SCORE)
	@TokenValid(value = TokenConstants.TOKEN_GRANT_SCORE, dispatch = true)
	public Object  sendScore(String id, String score) {
		if(score.equals("")) {
			return renderResult(false, "发放积分不能为空");
		}
		LOGGER.info("待处理:{}",id);
		LOGGER.info("待处理:{}",score);
		int sendScore = Integer.parseInt(score) ;
		UserScore userScore = userScoreService.getUserScore(id) ;
		LOGGER.info("userScore:{}", userScore);
		int handScore = userScore.getHandScore() ;
		userScore.setHandScore(sendScore+handScore);
		userScore.setNoUseScore(userScore.getNoUseScore()+sendScore);
		userScore.setTotalScore(userScore.getNoUseScore()+userScore.getUseScore());
		LOGGER.info("冻结积分:{}",userScore.getNoUseScore());
		userScoreService.update(userScore);
		return renderResult(true, "发放成功");
	}
	
	/**审核通过发放积分*/
	@RequestMapping(value = "/loan/score/establishScore")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_SCORE)
	public String establishSend(String userId, Model model) {
		LOGGER.info("待处理:{}",userId);
		UserScore userSore = userScoreService.getUserScore(userId) ;
		model.addAttribute("handScore", userSore.getHandScore()) ;
		model.addAttribute("userId", userId) ;
		LOGGER.info("待處理:{}",userSore.getHandScore());
		return "/loan/score/establishScore" ;
	}
	
	
	@RequestMapping(value = "/loan/score/handEstablishScore")
	@RequiresPermissions("oper:score:userScore:score_grant")
	@ResponseBody
	@SystemLog(method=SysLogConstant.GRANT,content=SysLogConstant.USER_SCORE)
	@TokenValid(value = TokenConstants.TOKEN_GRANT_SCORE, dispatch = true)
	public Object doEstablish(String id, String score) {
		LOGGER.info("我的积分{}", score);
		UserScore userScore = userScoreService.getUserScore(id) ;
		int sendScore = Integer.parseInt(score) ;
		if(sendScore == 0) {
			return renderResult(false, "发放积分为0，不予审核");
		}
		userScore.setUseScore(userScore.getUseScore()+sendScore);
		userScore.setNoUseScore(userScore.getNoUseScore()-sendScore);
		userScore.setHandScore(userScore.getHandScore()-sendScore);
		userScoreService.update(userScore);
		/**
		 * 更新积分日志
		 */
		final UserScoreLog userScoreLog = new UserScoreLog();
		userScoreLog.setUserId(id);
		userScoreLog.setTotalScore(userScore.getTotalScore());
		userScoreLog.setUseScore(userScore.getUseScore());
		userScoreLog.setOptValue(sendScore);
		userScoreLog.setOptType(OperateEnum.SCORE_OPT_ADD.getValue());
		userScoreLog.setNoUseScore(userScore.getNoUseScore());
		userScoreLog.setTypeName("手动发放");
		userScoreLog.setTypeCode("score_byhand");
		userScoreLog.setRemark("手动发放"+sendScore+"积分");
		userScoreLog.setCreateTime(DateUtils.getNow());
		userScoreLogService.insert(userScoreLog);
		return renderResult(true, "审核成功");
	}
	
	@RequestMapping(value = "/operate/score/grantUserScore")
	public String grantUserScore() {
		return "/operate/score/grantUserScore" ;
	}
	
	/**
	 * 批量发放用户积分
	 * @param userIds
	 * @param score
	 * @param remark
	 * @return
	 */
	@RequiresPermissions("oper:score:userScore:score_grant")
	@RequestMapping("/operate/score/doGrantScore")
	@ResponseBody
	@SystemLog(method=SysLogConstant.GRANT,content=SysLogConstant.USER_SCORE)
	@TokenValid(value = TokenConstants.TOKEN_GRANT_SCORE, dispatch = true)
	public Object doGrantUserScore(final String[] userIds, final String[] scores, final String remark) {
		LOGGER.info("userIds:{},scores:{}", userIds.length, scores.length);
		if(remark == null || StringUtils.isBlank(remark)) {
			for(int i=0; i<userIds.length; i++) {
				User user = userService.get(userIds[i]) ;
				int optValue = Integer.parseInt(scores[i]) ;
				userScoreService.grantScore(user, "手动发放", "score_byHand", optValue, remark) ;
			}
		}else {
			for(int i=0; i<userIds.length; i++) {
				User user = userService.get(userIds[i]) ;
				int optValue = Integer.parseInt(scores[0]) ;
				userScoreService.grantScore(user, "手动发放", "score_byHand", optValue, remark) ;
			}
		}
		return renderResult(true,"发放成功");
	}
	
	/**
	 * 积分发放页面 导入excel
	 * 
	 * @author wzy
	 * @date 2017-10-26
	 * @param file excel
	 * @return
	 */
	@RequestMapping(value = "/operate/score/userExcel")
	@ResponseBody
	@SystemLog(method=SysLogConstant.GRANT_RATE_IMPORT_DATA,content=SysLogConstant.USER_LIST)
	public String importUserExcel(@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request) {
		
		String path = request.getSession().getServletContext().getRealPath("/");
		path = path + separator + "data" + separator + "userRedEnvelope" + separator + DateUtils.dateStr7(new Date());
        String fileName = file.getOriginalFilename()+DateUtils.dateStr3(new Date());  
        File targetFile = new File(path, fileName);
        if(!targetFile.exists()){  
            targetFile.mkdirs();  
        }  
        try {
			file.transferTo(targetFile);
		} catch (IllegalStateException|IOException e) {
			LOGGER.error(e.getMessage(), e);
		} 
        return reBuildJson(userService.checkExcelUserScore(targetFile));
	}
	
	@RequestMapping("/operate/score/scoreTypeManage")
    public String scoreTypeManage() {
        return "/operate/score/scoreTypeManage";
    }

    @RequestMapping(value = "/operate/score/scoreTypeList", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions("oper:score:scoreType:query")
    public Object scoreTypeList(final ScoreType scoreType) {
        scoreType.setStatus("1");
        return scoreTypeService.findPage(scoreType);
    }

    @RequestMapping("/operate/score/scoreTypeEditPage")
    public String scoreTypeEditPage(String id, final Model model) {
        model.addAttribute("model", scoreTypeService.get(id));
        return "/operate/score/scoreTypeEditPage";
    }

    @RequestMapping(value = "/operate/score/scoreTypeEdit", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions("oper:score:scoreType:edit")
    public Object scoreTypeEdit(final ScoreType scoreType) {
        scoreTypeService.update(scoreType);
        return renderSuccessResult();
    }
	
}
