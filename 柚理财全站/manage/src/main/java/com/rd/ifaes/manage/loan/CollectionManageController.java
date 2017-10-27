package com.rd.ifaes.manage.loan;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.util.excel.ExportModel;
import com.rd.ifaes.common.util.excel.ExportUtil;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.project.domain.ProjectCollection;
import com.rd.ifaes.core.project.service.ProjectCollectionService;
import com.rd.ifaes.manage.sys.SystemController;
/**
 * 待收管理
 * @author lh
 * @version 3.0
 * @since 2016-7-29
 *
 */
@Controller
public class CollectionManageController extends SystemController {
	/**
	 * 待收排序方式
	 */
	private static final String SORT_NAME = "pc.repay_time,pc.uuid";
	@Autowired
	private ProjectCollectionService projectCollectionService;
	
	/****************************** 待收记录 ******************************/
	
	/**
	 * 弹框待收记录页面
	 * @return
	 */
	@RequestMapping(value = "/loan/collection/collectionList")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.COLLECTION_RECORD)
	public String list(Model model,String projectId){
		model.addAttribute("projectId", projectId);
		return "/loan/collection/collectionList";
	}

	/**
	 * 待收记录页面
	 * @return
	 */
	@RequestMapping(value = "/loan/collection/collectionRecord")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.COLLECTION_RECORD)
	public String record(String borrowFlag,Model model){
		model.addAttribute("borrowFlag", borrowFlag);
		return "/loan/collection/collectionRecord";
	}

	
	/**
	 * 待收列表
	 * @return
	 */
	@RequestMapping(value = "/loan/collection/collectionRecordList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.COLLECTION_RECORD)
	public Object recordList(ProjectCollection model){	
		model.getPage().setSort(SORT_NAME);
		model.getPage().setOrder(Constant.ASC);
		return projectCollectionService.findPage(model);
	}
	
	/**
	 * 导出待收列表
	 * 
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/loan/collection/exportExcel")
	@SystemLog(method=SysLogConstant.EXPORT,content=SysLogConstant.COLLECTION_RECORD)
	public void exportExcel(final ProjectCollection model,HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		model.setBorrowFlag(LoanEnum.BORROW_FLAG_BORROW.getValue());
		
		ExportUtil.exportExcel(new ExportModel<ProjectCollection>() {
			@Override
			public String getCacheKey() {
				return  CollectionManageController.this.getUserId();
			}
			@Override
			public ProjectCollection getModel() {
				return model;
			}
			@Override
			public int getTotal(ProjectCollection entity) {
				return projectCollectionService.getCount(entity);
			}
			@Override
			public List<ProjectCollection> getPageRecords(ProjectCollection entity) {
				return projectCollectionService.findPage(entity).getRows();
			}
		}, request, response);
	}

}
