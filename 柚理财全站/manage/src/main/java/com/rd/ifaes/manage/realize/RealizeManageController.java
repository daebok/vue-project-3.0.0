package com.rd.ifaes.manage.realize;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.project.domain.Realize;
import com.rd.ifaes.core.project.service.RealizeService;
import com.rd.ifaes.manage.sys.SystemController;

/**
 * 变现记录
 * @version 3.0
 * @author fxl
 * @date 2016年8月1日
 */
@Controller
public class RealizeManageController extends SystemController {
	
	@Resource
	private transient RealizeService realizeService;

	/**
	 * 变现记录
	 * @author fxl
	 * @date 2016年8月1日
	 * @return
	 */
	@RequestMapping(value = "/realize/manage/realizeManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.REALIZE)
	public String manage(Model model){
		model.addAttribute("webUrl", ConfigUtils.getValue(ConfigConstant.WEB_URL));
		return "/realize/manage/realizeManage";
	}
	
	/**
	 * 变现列表
	 * @author fxl
	 * @date 2016年8月1日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/realize/manage/list")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.REALIZE)
	public Object list(final Realize model){		
		return realizeService.findPageModel(model);
	}
	
	/**
	 * 变现投资列表
	 * @author fxl
	 * @date 2016年8月10日
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/realize/manage/realizeTenderPage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.INVEST_RECORD)
	public String tenderPage(final String id,final Model model) {
		 model.addAttribute("projectId", id);
		return "/realize/manage/realizeTenderPage";
	}
	
}
