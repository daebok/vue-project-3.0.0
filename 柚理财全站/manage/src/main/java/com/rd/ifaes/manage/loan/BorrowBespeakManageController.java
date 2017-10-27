package com.rd.ifaes.manage.loan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.project.domain.BorrowBespeak;
import com.rd.ifaes.core.project.service.BorrowBespeakService;
import com.rd.ifaes.manage.sys.SystemController;

/**
 * 预约跟进管理
 * 
 * @author lh
 * @version 3.0
 * @since 2016-6-24
 */
@Controller
public class BorrowBespeakManageController extends SystemController {

	@Autowired
	private BorrowBespeakService borrowBespeakService;

	/**
	 * 预约跟进管理
	 * 
	 * @return
	 */
	@RequestMapping(value = "/loan/borrowBespeak/borrowBespeakManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.BORROW_BESPEAK)
	public String manage() {
		return "/loan/borrowBespeak/borrowBespeakManage";
	}

	/**
	 * 预约跟进列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/loan/borrowBespeak/list")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.BORROW_BESPEAK)
	public Object list(final BorrowBespeak model) {
		return borrowBespeakService.findPage(model);
	}

	/**
	 * 预约编辑页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/loan/borrowBespeak/borrowBespeakEditPage")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.BORROW_BESPEAK)
	public String borrowBespeakEditPage(final String id ,final Model model) {
		if(StringUtils.isBlank(id)){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.BORROW_BESPEAK_NOT_CHANGE));
		}
		final BorrowBespeak borrowBespeak = borrowBespeakService.get(id);
		 model.addAttribute("borrowBespeak", borrowBespeak);
		return "/loan/borrowBespeak/borrowBespeakEditPage";
	}
	
	/**
	 * 预约编辑
	 * 
	 * @return
	 */
	@RequestMapping(value = "/loan/borrowBespeak/borrowBespeakEdit")
	@ResponseBody
	@TokenValid(value = TokenConstants.TOKEN_EDIT_BORROW_BESPEAK, dispatch = true)
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.BORROW_BESPEAK)
	public Object borrowBespeakEdit(final BorrowBespeak model) {
		model.setHandleTime(DateUtils.getNow());
		if(StringUtils.isNotBlank(model.getRemark()) && model.getRemark().length() > Constant.INT_TWO_HUNDRED){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.BORROW_BESPEAK_REMARK_LENGTH_ERROR));
		}
		borrowBespeakService.update(model);
		return renderSuccessResult();
	}
	
	/**
	 * 预约查看页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/loan/borrowBespeak/borrowBespeakViewPage")
	@SystemLog(method=SysLogConstant.VIEW,content=SysLogConstant.BORROW_BESPEAK)
	public String borrowBespeakViewPage(final String id ,final Model model) {
		if(StringUtils.isBlank(id)){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.BORROW_BESPEAK_NOT_CHANGE));
		}
		final BorrowBespeak borrowBespeak = borrowBespeakService.get(id);
		 model.addAttribute("borrowBespeak", borrowBespeak);
		return "/loan/borrowBespeak/borrowBespeakViewPage";
	}
}
