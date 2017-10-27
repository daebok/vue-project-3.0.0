package com.rd.ifaes.core.user.service.impl;

import java.util.List;

import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.PropertiesUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.user.service.UserQualificationUploadService;
import com.rd.ifaes.core.user.mapper.UserQualificationUploadMapper;
import com.rd.ifaes.core.user.domain.UserQualificationUpload;
import com.rd.ifaes.core.base.service.*;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.HclientFileUtil;
import com.rd.ifaes.core.core.util.ResourceUtils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 资质文件上传处理类
 * @author FangJun
 * @version 3.0
 * @date 2016-7-21
 */
@Service("userQualificationUploadService") 
public class UserQualificationUploadServiceImpl  extends BaseServiceImpl<UserQualificationUploadMapper, UserQualificationUpload> implements UserQualificationUploadService{
		
	/**
	 * 根据qualificationApplyId查询资质文件列表
	 */
	@Override
	public List<UserQualificationUpload> findByQualificationApply(final String qualificationApplyId){
		return dao.findList(new UserQualificationUpload(qualificationApplyId));
	}
	
	/**
	 * 根据qualificationApplyId和删除标识查询资质文件列表
	 */
	@Override
	public List<UserQualificationUpload> findByQualificationApply(final String qualificationApplyId, final String deleteFlag){
		UserQualificationUpload model = new UserQualificationUpload();
		model.setQualificationApplyId(qualificationApplyId);
		model.setDeleteFlag(deleteFlag);
		return dao.findList(model);
	}	
	
	/**
	 * 
	 * 删除证明资料
	 * @author xhf
	 * @date 2016年9月21日
	 * @param user
	 * @param filePath
	 */
	@Transactional(readOnly=false)
	@Override
	public void deletePic(final String uploadId, final String filePath){
		//校验
		if(StringUtils.isBlank(uploadId) && StringUtils.isBlank(filePath)){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PIC_PATH_EMPTY),BussinessException.TYPE_JSON);
		}
	   //数据库存在则进行逻辑删除，否则进行物理删除
       if(StringUtils.isNotBlank(uploadId)){
        	dao.deleteLogic(uploadId);
        }else{
        	HclientFileUtil.deleteImg(PropertiesUtils.getValue("deleteUrl"), filePath);
        }
	}
	
}