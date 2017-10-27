package com.rd.ifaes.web.controller.member;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.core.sys.domain.Letter;
import com.rd.ifaes.core.sys.service.LetterService;
import com.rd.ifaes.web.controller.WebController;


/**
 * 站内信
 * @version 3.0
 * @author fxl
 * @date 2016年10月14日
 */
@Controller
public class LetterController extends WebController {
	
	@Resource
	private LetterService letterService;
	
	/**
	 * 站内信列表(页面)
	 * @author fxl
	 * @date 2016年10月14日
	 * @return
	 */
	@RequestMapping(value = "/member/letter/letter")
	public String letter(){
		return "/member/letter/letter";
	}
	
	
	/**
	 * 站内信列表(接口)
	 * @author fxl
	 * @date 2016年10月14日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/member/letter/list")
	@ResponseBody
	public Object list(final Letter model){
		final Map<String, Object> data = renderSuccessResult();
		model.setReceiveUser(getSessionUser().getUuid());
		data.put("data", letterService.findLetter(model));
		return data;
	}

	/**
	 * 批量操作
	 * @author fxl
	 * @date 2016年10月14日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/member/letter/batchSet")
	@ResponseBody
	public Object batchSet(final Letter model){
		model.setReceiveUser(getSessionUser().getUuid());
		return letterService.set(model);
		
	}
}
