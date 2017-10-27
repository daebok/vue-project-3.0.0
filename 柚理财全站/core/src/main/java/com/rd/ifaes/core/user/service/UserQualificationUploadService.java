package com.rd.ifaes.core.user.service;

import java.util.List;

import com.rd.ifaes.core.user.domain.UserQualificationUpload;
import com.rd.ifaes.core.base.service.BaseService;

/**
 * Service Interface:UserQualificationUploadService
 * @author FangJun
 * @version 3.0
 * @date 2016-7-21
 */
public interface UserQualificationUploadService extends BaseService<UserQualificationUpload>{

	/**
	 * 
	 * 根据上传文件ID获得文件列表
	 * @author xhf
	 * @date 2016年7月26日
	 * @param userId
	 * @param qualificationType
	 * @return
	 */
	List<UserQualificationUpload> findByQualificationApply(String applyId);
	
	/**
	 * 
	 * 根据qualificationApplyId和删除标识查询资质文件列表
	 * @author xhf
	 * @date 2016年9月21日
	 * @param qualificationApplyId
	 * @param deleteFlag
	 * @return
	 */
	List<UserQualificationUpload> findByQualificationApply(final String qualificationApplyId, final String deleteFlag);
	
	/**
	 * 
	 * 删除证明资料
	 * @author xhf
	 * @date 2016年9月21日
	 * @param user
	 * @param filePath
	 */
	void deletePic(String uploadId, String filePath);
}