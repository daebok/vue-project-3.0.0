package com.rd.ifaes.mobile.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.validcode.ValidCodeMaker;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.ValidateUtils;
import com.rd.ifaes.mobile.common.AppException;
import com.rd.ifaes.mobile.common.AppResultCode;
/**
 * 工具类Action: 图形验证码 
 * @version 3.0
 * @author FangJun
 * @date 2016年10月28日
 */
@Controller
public class ToolController extends WebController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ToolController.class);

	
	/**
	 * 生成图片验证码
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/app/open/validimg")
	@ResponseBody
	public void validimg(final HttpServletResponse response,Model model) throws Exception {
		try {
			ValidCodeMaker authCode = ValidCodeMaker.getInstance();
			// 取得随机字符串放入session
			SessionUtils.setSessionAttr(Constant.VALID_CODE, authCode.getString());
			// 设置验证码输出格式
			response.setContentType("image/jpeg");
			response.setCharacterEncoding("utf-8");
			OutputStream output = new BufferedOutputStream(response.getOutputStream());
			InputStream in = authCode.getImage();
			int len = Constant.INT_ZERO;
			byte[] buf = new byte[1024];
			while ((len = in.read(buf, 0, 1024)) != -1) {
				output.write(buf, 0, len);
			}
			response.flushBuffer();
			model.addAttribute("__sid", (String) SessionUtils.getSession().getId());
			output.flush();
			in.close();
			output.close();
		} catch (IOException e) {
			LOGGER.error("build validCode error.", e);
		}
	}

	/**
	 * 异步校验图片验证码
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/app/open/valicode")
	@ResponseBody
	public Object valicode(String validCode) throws Exception {
		Object obj=null;
		try{
		if (StringUtils.isBlank(validCode) || !ValidateUtils.checkValidCode(validCode)) {// 不通过
			throw new AppException(AppResultCode.ERROR, "验证码不正确");
		}else{
		SessionUtils.setSessionAttr(Constant.VALID_CODE, validCode);
		obj=createSuccessAppResponse("校验成功！");
		}
	  
		} catch (Exception e) {
			obj=dealException(e);
		}
       return obj;
       }
       
       }
