package com.rd.ifaes.core.user.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserQualificationApply;
import com.rd.ifaes.core.user.model.UserQualificationApplyModel;

/**
 * 资质文件申请提交审核处理类接口
 * @author FangJun
 * @version 3.0
 * @date 2016-7-21
 */
public interface UserQualificationApplyService extends BaseService<UserQualificationApply>{

	/**
	 * 
	 * 根据上传资质类型查询最新上传文件
	 * @author xhf
	 * @date 2016年8月3日
	 * @param userId
	 * @param qualificationType
	 * @return
	 */
	UserQualificationApply findNewestApply(String userId, String qualificationType);
	
	/**
	 * 
	 * 获得证明资料列表
	 * @author xhf
	 * @date 2016年7月28日
	 * @param qualificationType
	 * @return
	 */
	List<UserQualificationApplyModel> getApplyFile(String qualificationType,User user);
	
	/**
	 * 
	 * 新增企业资质审核文件
	 * @author xhf
	 * @date 2016年7月26日
	 * @param userQualificationApply
	 * @param list
	 */
	void addFile(String qualificationType, String[] picPaths,String addIp);

	/**
	 * 
	 * 判断是否需要上传资质证明
	 * @author ZhangBiao
	 * @date 2016年8月2日
	 * @return
	 */
	boolean needAddFiles(User user);
	
	/**
	 * 
	 * 获取记录列表
	 * @author xhf
	 * @date 2016年8月3日
	 * @param model
	 * @return
	 */
	Page<UserQualificationApply> getLogList(UserQualificationApply model);
	
	/**
	 * 
	 * 资质文件审核
	 * @author xhf
	 * @date 2016年8月4日
	 * @param model
	 * @return
	 */
	void doAudit(UserQualificationApply model);

	/**
	 * 多个资质证明上传
	 * 
	 * @author ZhangBiao
	 * @date 2016年9月23日
	 * @param qualificationTypes
	 * @param addip 
	 * @param request 
	 */
	void addFiles(String[] qualificationTypes, String addip, HttpServletRequest request);
}