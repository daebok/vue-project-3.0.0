package com.rd.ifaes.manage.operate;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.util.excel.ExportModel;
import com.rd.ifaes.common.util.excel.ExportUtil;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.score.domain.UserScoreLotteryRecord;
import com.rd.ifaes.core.score.service.UserScoreLotteryRecordService;
import com.rd.ifaes.manage.sys.SystemController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class UserScoreLotteryRecordManageController extends SystemController {
	
	@Resource
    UserScoreLotteryRecordService userScoreLotteryRecordService;
	
	
	@RequestMapping(value = "/operate/score/userScoreLotteryRecordManage")
	@SystemLog(method= SysLogConstant.QUERY,content= SysLogConstant.SCORE_LOTTERY)
	public String userScoreLotteryRecordManage(){
		return "/operate/score/userScoreLotteryRecordManage";
	}
	
	
	@RequestMapping(value = "/operate/score/userScoreLotteryRecordList")
	@ResponseBody
	@SystemLog(method= SysLogConstant.QUERY,content= SysLogConstant.USER_SCORE_LOTTERY_RECORD)
	public Object userScoreLotteryRecordList(final UserScoreLotteryRecord userScoreLotteryRecord) throws Exception {
		return userScoreLotteryRecordService.findPage(userScoreLotteryRecord);
	}
	
	@RequestMapping(value = "/operate/score/exportUserScoreLotteryRecord")
	@RequiresPermissions("oper:scorelottety:record:export")
	@SystemLog(method= SysLogConstant.EXPORT,content= SysLogConstant.USER_SCORE_LOTTERY_RECORD)
	public void exportUserScoreLotteryRecord(final UserScoreLotteryRecord model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ExportUtil.exportExcel(new ExportModel<UserScoreLotteryRecord>() {
			@Override
			public String getCacheKey() {
				return  UserScoreLotteryRecordManageController.this.getUserId();
			}
			@Override
			public UserScoreLotteryRecord getModel() {
				return model;
			}
			@Override
			public int getTotal(UserScoreLotteryRecord entity) {
				return userScoreLotteryRecordService.getCount(entity);
			}
			@Override
			public List<UserScoreLotteryRecord> getPageRecords(UserScoreLotteryRecord entity) {
				return userScoreLotteryRecordService.findPage(entity).getRows();
			}
		}, request, response);
	}
}
