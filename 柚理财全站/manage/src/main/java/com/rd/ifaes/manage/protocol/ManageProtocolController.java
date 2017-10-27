package com.rd.ifaes.manage.protocol;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.protocol.domain.Protocol;
import com.rd.ifaes.core.protocol.domain.ProtocolLog;
import com.rd.ifaes.core.protocol.service.ProtocolLogService;
import com.rd.ifaes.core.protocol.service.ProtocolService;
import com.rd.ifaes.manage.sys.SystemController;

/**
 * 
 * 协议管理
 * @version 3.0
 * @author jxx
 * @date 2016年7月27日
 */
@Controller
public class ManageProtocolController extends SystemController {

	@Resource
	private ProtocolService protocolService;
	@Resource
	private ProtocolLogService protocolLogService;
	@Resource
	private ProjectService projectService;
	
	/**
	 * 
	 * 协议模板列表页
	 * @author jxx
	 * @date 2016年7月28日
	 * @return
	 */
	@RequestMapping(value = "/protocol/protocol/protocolList")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.PROTOCOL)
	public String protocolList(){
		return "/protocol/protocol/protocolList";
	}

	/**
	 * 
	 * 列表数据
	 * @author jxx
	 * @date 2016年7月28日
	 * @param protocol
	 * @return
	 */
	@RequestMapping(value = "/protocol/protocol/list")
	@ResponseBody
	@RequiresPermissions("project:protocol:tempalte:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.PROTOCOL)
	public Object list(Protocol protocol){
		return protocolService.findPage(protocol);
	}
	
	/**
	 * 
	 * 添加协议
	 * @author jxx
	 * @date 2016年7月28日
	 * @return
	 */
	@RequestMapping(value = "/protocol/protocol/protocolAdd")
	@RequiresPermissions("project:protocol:tempalte:add")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.PROTOCOL)
	public String protocolAdd(){
		return "/protocol/protocol/protocolAdd";
	}
	
	/**
	 * 
	 * 添加
	 * @author jxx
	 * @date 2016年7月27日
	 * @param protocol
	 * @return
	 */
	@RequestMapping(value = "/protocol/protocol/add")
	@ResponseBody
	@RequiresPermissions("project:protocol:tempalte:add")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.PROTOCOL)
	public Object add(Protocol protocol){
		String resultStr = protocolService.saveProtocol(protocol);
		if(StringUtils.isNotBlank(resultStr)){
			return renderResult(false, resultStr);
		}
		return renderResult(true, "新增成功");
	}
	
	/**
	 * 
	 * 编辑协议，包括内容，名称
	 * @author jxx
	 * @date 2016年7月27日
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/protocol/protocol/protocolEdit")
	@RequiresPermissions("project:protocol:tempalte:edit")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.PROTOCOL)
	public String protocolEdit(String id,final Model model) {
		Protocol protocol = protocolService.get(id);
		 model.addAttribute("protocol", protocol);
		return "/protocol/protocol/protocolEdit";
	}
	
	/**
	 * 
	 * 编辑
	 * @author jxx
	 * @date 2016年7月27日
	 * @param protocol
	 * @return
	 */
	@RequestMapping(value = "/protocol/protocol/edit")
	@ResponseBody
	@RequiresPermissions("project:protocol:tempalte:edit")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.PROTOCOL)
	public Object edit(Protocol protocol){
		String resultStr = protocolService.editProtocol(protocol);
		if(StringUtils.isNotBlank(resultStr)){
			return renderResult(false, resultStr);
		}
		return renderResult(true, "操作成功");
	}
	
	/**
	 * 
	 * 启用禁用协议
	 * @author jxx
	 * @date 2016年7月27日
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/protocol/protocol/updateStatus")
	@ResponseBody
	@RequiresPermissions("project:protocol:tempalte:cancel")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.PROTOCOL)
	public Object updateStatus(String ids){
		String resultStr = protocolService.updateStatus(ids);
		if(StringUtils.isNotBlank(resultStr)){
			return renderResult(false, resultStr);
		}
		return renderResult(true, "操作成功");
	}
	
	/**
	 * 
	 * 列表页面
	 * @author jxx
	 * @date 2016年7月27日
	 * @return
	 */
	@RequestMapping(value = "/protocol/protocolLog/protocolLogList")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.PROTOCOL)
	public String protocolLogList(){
		return "/protocol/protocolLog/protocolLogList";
	}

	/**
	 * 
	 * 列表数据
	 * @author jxx
	 * @date 2016年7月27日
	 * @param protocol
	 * @return
	 */
	@RequestMapping(value = "/protocol/protocolLog/logList")
	@ResponseBody
	@RequiresPermissions("project:protocol:list:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.PROTOCOL)
	public Object loglist(ProtocolLog protocolLog){
		return protocolLogService.findManagePage(protocolLog);
	}
	
	/**
	 * 
	 * 投资人协议下载
	 * @author jxx
	 * @date 2016年7月28日
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/protocol/protocolLog/downloadProtocol")
	@RequiresPermissions("project:protocol:list:download")
	@SystemLog(method=SysLogConstant.DOWNLOAD,content=SysLogConstant.PROTOCOL)
	public String downloadProtocol(String uuid) throws Exception {
		ProtocolLog protocolLog = protocolLogService.get(uuid);
		if(protocolLog == null || StringUtils.isBlank(protocolLog.getFilePath()) || StringUtils.isBlank(protocolLog.getProtocolName())){
			throw new BussinessException();
		}
		String[] strs = protocolLog.getFilePath().split("-");
		if(strs.length !=2){
			throw new BussinessException();
		}
		String url = "/upload/downloadContract.html";
		String webUrl =ConfigUtils.getValue(ConfigConstant.WEB_URL);
		String resultStr = protocolService.downloadProtocol(strs[0], strs[1], protocolLog.getProtocolName(),url,webUrl);
		if(StringUtils.isBlank(resultStr)){
			throw new BussinessException("下载出错");
		}
		return  "redirect:"+resultStr;
	}
	
    /**
     *  打开协议选择窗口
     * @author  FangJun
     * @date 2016年8月10日
     * @return
     */
	@RequestMapping(value = "/protocol/protocol/protocolPage")
	@SystemLog(method=SysLogConstant.VIEW,content=SysLogConstant.PROTOCOL)
	public String protocolPage(){
		return "/protocol/protocol/protocolPage";
	}
	
	/**
	 * 
	 * 查看
	 * @author jxx
	 * @date 2016年7月27日
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/protocol/protocol/protocolView")
	@SystemLog(method=SysLogConstant.VIEW,content=SysLogConstant.PROTOCOL)
	public String protocolView(final String id,final Model model) {
		Protocol protocol = protocolService.get(id);
		model.addAttribute("protocol", protocol);
		model.addAttribute("content",protocolService.getView(protocol));
		return "/protocol/protocol/protocolView";
	}
	
	/**
	 * 根据借款项目id获取协议
	 */
	@RequestMapping(value = "/protocol/protocol/getProtocolView")
	@SystemLog(method=SysLogConstant.VIEW,content=SysLogConstant.PROTOCOL)
	public String getProtocolView(final String projectId, final Model model) {
		LOGGER.info("待處理:{}",projectId);
		Project project = projectService.get(projectId) ;
		String protocolId = project.getProtocolId() ;
		Protocol protocol = protocolService.get(protocolId);
		model.addAttribute("protocol", protocol);
		model.addAttribute("content",protocolService.getView(protocol));
		return "/protocol/protocol/protocolView";
	}
 
}
