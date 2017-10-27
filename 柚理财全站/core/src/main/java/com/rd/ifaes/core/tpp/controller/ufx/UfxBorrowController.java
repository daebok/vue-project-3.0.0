package com.rd.ifaes.core.tpp.controller.ufx;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.tpp.model.ufx.UfxProjectApplyModel;

@Controller
public class UfxBorrowController extends BaseController {

	
	/**
	 * 发标异步回调处理
	 * @return
	 */
	@RequestMapping("/ufx/projectApply/notify")
	@ResponseBody
	public Object projectApplyNotify(UfxProjectApplyModel model,final HttpServletRequest request){
		LOGGER.debug("发标进入异步回调，回调参数：{}" , getRequestParams(request));
		boolean result = false;
		String msg = MSG_SUCCESS;
		if (model.validSign(model)) {
			if (model.checkReturn()) {
				result = true;
//				String merPrivJson = new BASE64Decoder().decodeStr(model.getMerPriv());
//				Map<String,Object> map  = (Map<String, Object>) JsonMapper.fromJsonString(merPrivJson, Map.class);
//				String uuid = StringUtils.isNull(map.get("uuid"));
//				String projectId = model.getProjectId();
//				borrowService.updateUfxNo(projectId, uuid);
			}else{
				msg = "第三方登记标信息失败，失败原因："+model.getRespDesc();
				LOGGER.error(msg);
			}
		}else{
			msg = "项目登记验签失败！";
			LOGGER.error(msg);
		}
		return renderResult(result, msg);
	}
	
}
