package com.rd.ifaes.mobile.controller.user;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.ip.IPUtil;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.service.UserQualificationApplyService;
import com.rd.ifaes.core.user.service.UserQualificationUploadService;
import com.rd.ifaes.mobile.controller.WebController;

/**
 * 借款资质
 * 
 * @author xhf
 * @version 2.0
 * @since 2016年7月20日
 */
@Controller
public class UserCertificationController extends WebController{

    /**
     * 借款资质申请类
     */
	@Resource
	private transient UserQualificationApplyService applyService;
	/**
	 * 借款资质上传类
	 */
	@Resource
	private transient UserQualificationUploadService uploadService;
	
	/**
	 * 
	 * 获得资质证明信息
	 * @author xhf
	 * @date 2016年7月26日
	 * @return
	 */
	@RequestMapping(value = "/member/usercertification/getCertificateInfo")
	@ResponseBody
	public Object getCertificateInfo(final String qualificationType){
		final Map<String, Object> data = Maps.newHashMap();
		final User user = SessionUtils.getSessionUser();
		data.put("result", true);
		data.put("qualificationList", applyService.getApplyFile(qualificationType,user));
		return data;
	}
	
	/**
	 * 删除证明材料
	 */
	@RequestMapping(value = "/member/usercertification/deletePic")
	@ResponseBody
	public Object deletePic(final String uploadId, final String picPath){
		//已保存的图片不能被删除
		uploadService.deletePic(uploadId, picPath);
		return renderResult(true, "操作成功");
	}
	
	/**
	 * 新增证明材料
	 */
	@RequestMapping(value = "/member/usercertification/addFile")
	@ResponseBody
	public Object addFile(final String qualificationType, 
			@RequestParam(value = "picPath[]") final String[] picPath,final HttpServletRequest request){
		applyService.addFile(qualificationType, picPath, IPUtil.getRemortIP(request));
		return renderSuccessResult();
	}
}
