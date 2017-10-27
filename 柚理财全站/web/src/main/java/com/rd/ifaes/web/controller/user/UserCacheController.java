package com.rd.ifaes.web.controller.user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.web.controller.WebController;

/**
 * 用户附属信息
 * 
 * @author xhf
 * @version 3.0
 * @since 2016年7月20日
 */
@Controller
public class UserCacheController extends WebController{
	
	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(UserCacheController.class);
	
	/**
	 * 用户附属信息类
	 */
	@Resource
	private transient UserCacheService userCacheService;
	
	/**
	 * 头像修改页面
	 * 
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/member/info/face")
	public String face(final String avaPic,final String error,final Model model,final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		//判断是否是担保用户
		final UserCache userCache = (UserCache) SessionUtils.getSessionAttr(Constant.SESSION_USER_CACHE);
		 model.addAttribute("isVouch", userCache.getUserNature());
		//参数判断
		if(StringUtils.isBlank(avaPic) && StringUtils.isBlank(error) ){
			 model.addAttribute("imageServerUrl", ConfigUtils.getValue(ConfigConstant.IMAGE_SERVER_URL));
			//成功修改跳转地址
			String resourceUrl = getResourceUrl(request);
			if(StringUtils.isBlank(resourceUrl)){
				resourceUrl = "/member/account/main.html";
			}
			 model.addAttribute("returnUrl", resourceUrl);
			return "/member/info/face";
		}
		
		String resultStr = "";
		if (StringUtils.isNotBlank(error)) {
			resultStr = "error=无效的请求，上传失败！";
		} else {
			userCacheService.updateAvaPic(getSessionUser(),avaPic);
			userCache.setAvatarPhoto(avaPic);
			SessionUtils.setSessionAttr(Constant.SESSION_USER_CACHE, userCache);
			resultStr = "success=true";
		}
		
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.println(resultStr);
		} catch (IOException e) {
			LOGGER.error(e.getLocalizedMessage());
			throw e;
		}finally{
			if(out != null){
				out.flush();
				out.close();
			}
		}
		return null;
	}

}
