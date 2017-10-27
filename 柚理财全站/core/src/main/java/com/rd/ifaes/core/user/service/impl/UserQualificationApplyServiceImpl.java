package com.rd.ifaes.core.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.TokenUtils;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.sys.domain.DictData;
import com.rd.ifaes.core.sys.service.DictDataService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.domain.UserQualificationApply;
import com.rd.ifaes.core.user.domain.UserQualificationUpload;
import com.rd.ifaes.core.user.mapper.UserQualificationApplyMapper;
import com.rd.ifaes.core.user.model.UserQualificationApplyModel;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserQualificationApplyService;
import com.rd.ifaes.core.user.service.UserQualificationUploadService;

/**
 * ServiceImpl:UserQualificationApplyServiceImpl
 * @author FangJun
 * @version 3.0
 * @date 2016-7-21
 */
@Service("userQualificationApplyService") 
public class UserQualificationApplyServiceImpl  extends BaseServiceImpl<UserQualificationApplyMapper, UserQualificationApply> implements UserQualificationApplyService{
	
	/**
	 * 字典处理类
	 */
	@Resource
	private transient DictDataService dictDataService;
	/**
	 * 资质文件上传处理类
	 */
	@Resource
	private transient UserQualificationUploadService uploadService;
	/**
	 * 资质文件提交审核类
	 */
	@Resource
	private transient UserQualificationApplyMapper applyMapper;
	/**
	 * 用户附属信息处理类
	 */
	@Resource
	private transient UserCacheService userCacheService;
	
	/**
	 * 根据上传资质类型查询最新上传文件
	 */
	@Override
	public UserQualificationApply findNewestApply(final String userId, final String qualificationType){
		return dao.findNewestApply(new UserQualificationApply(userId, qualificationType));
	}
	
	/**
	 * 获得证明资料列表
	 */
	@Override
	public List<UserQualificationApplyModel> getApplyFile(final String qualificationType,final User user){
		final UserCache userCache = userCacheService.findByUserId(user.getUuid());

		//证件类型
		String certificateType = DictTypeEnum.COMPANY_QUALIFICATION_TYPE.getValue();
		if(UserCache.USER_NATURE_PERSON.equals(userCache.getUserNature())){
			certificateType = DictTypeEnum.QUALIFICATION_TYPE.getValue();
		}
		
		//查询资料类型
		final List<DictData> dictList = new ArrayList<DictData>();
		if(StringUtils.isNotBlank(qualificationType)){
			dictList.add(dictDataService.get(certificateType, qualificationType));
		}else{
			dictList.addAll(dictDataService.findListByDictType(certificateType));
		}
		
		//文件列表
		final List<UserQualificationApplyModel> list = new ArrayList<UserQualificationApplyModel>();
		for (final DictData dictData : dictList) {
			//证明材料类型
			final UserQualificationApplyModel model = new UserQualificationApplyModel();
			model.setName(dictData.getItemName());
			model.setQualificationType(dictData.getItemValue());
			//审核信息
			final UserQualificationApply userQualificationApply = findNewestApply(user.getUuid(), dictData.getItemValue());
			if(userQualificationApply!=null){
				model.setStatus(userQualificationApply.getStatus());
				model.setVerifyTime(userQualificationApply.getVerifyTime());
				model.setVerifyRemark(userQualificationApply.getVerifyRemark());
				model.setFileList(uploadService.findByQualificationApply(userQualificationApply.getUuid(),Constant.FLAG_NO));
			}else{
				model.setStatus(UserQualificationApplyModel.STATUS_WAIT_UPLOAD);
				model.setFileList(null);
			}
			
			list.add(model);
		}
		return list;
	}
	
	/**
	 * 新增资质申请
	 */
	@Override
	public void addFile(final String qualificationType, final String[] picPaths, final String addIp){
		final User user = SessionUtils.getSessionUser();
		if(StringUtils.isBlank(qualificationType)){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.QUALIFICATION_TYPE_EMPTY), BussinessException.TYPE_JSON);
		}
		if (picPaths == null) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.QUALIFICATION_LEAST_ONE), BussinessException.TYPE_JSON);
		}
		
		if (picPaths.length > Constant.USER_CENRIFICATION_MAX) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.QUALIFICATION_REACH_MAX, Constant.USER_CENRIFICATION_MAX), BussinessException.TYPE_JSON);
		}
		
		//校验审核资料状态
		final UserQualificationApply dbApply = findNewestApply(user.getUuid(), qualificationType);
		if(dbApply!=null && UserQualificationApplyModel.STATUS_AUDIT.equals(dbApply.getStatus())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.QUALIFICATION_IS_AUDITING), BussinessException.TYPE_JSON);
		}
		
		//资质申请文件
		final UserQualificationApply apply = new UserQualificationApply();
		apply.setAddIp(addIp);
		apply.setCreateTime(DateUtils.getNow());
		apply.setUserId(user.getUuid());
		apply.setQualificationType(qualificationType);
		apply.setStatus(UserQualificationApplyModel.STATUS_AUDIT);
		save(apply);
		
		//资质文件列表
		final List<UserQualificationUpload> list = new ArrayList<UserQualificationUpload>();
		for (final String picPath : picPaths) {
			final UserQualificationUpload upload = new UserQualificationUpload();
			upload.setUserId(user.getUuid());
			upload.setQualificationApplyId(apply.getUuid());
			upload.setFilePath(picPath);
			upload.setCreateTime(apply.getCreateTime());
			upload.setDeleteFlag(Constant.FLAG_NO);
			list.add(upload);
		}
		uploadService.insertBatch(list);
	}

	/**
	 * 判断是否需要上传资质证明
	 */
	@Override
	public boolean needAddFiles(final User user) {
		boolean result = false;
		// 获取数据字典中所有资质类型
		final List<DictData> list = dictDataService.findListByDictType(DictTypeEnum.QUALIFICATION_TYPE.getValue());
		// 查询是否所有资质类型已上传
		for(final DictData data : list){
			final int n = applyMapper.countByType(user.getUuid(), data.getItemValue(),UserQualificationApply.STATUS_FAIL);
			if(n <= 0){
				result = true;
			}
		}
		return result;
	}
	
	/**
	 * 获得资质文件列表
	 */
	@Override
	public Page<UserQualificationApply> getLogList(final UserQualificationApply model){
		final Page<UserQualificationApply> page = model.getPage();
		page.setRows(dao.getLogList(model));
		return page;
	}	
	
	/**
	 * 审核
	 */
	@Override
	public void doAudit(final UserQualificationApply model){
		if(StringUtils.isBlank(model.getUuid())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.NEED_SELECT_ONE_RECORD), BussinessException.TYPE_JSON);
		}
		final UserQualificationApply dbApply = dao.get(model.getUuid());
		dbApply.setStatus(model.getStatus());
		dbApply.setVerifyRemark(model.getVerifyRemark());
		dbApply.setVerifyTime(DateUtils.getNow());
		dbApply.setVerifyUser(model.getVerifyUser());
		update(dbApply);
	}

	@Override
	@Transactional
	public void addFiles(String[] qualificationTypes, String addip,
			HttpServletRequest request) {
		if(qualificationTypes == null || qualificationTypes.length <= 0){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.QUALIFICATION_PIC_PATHS_EMPTY));
		}
		// 获取所有资质类型、文件
		for(final String qualificationType : qualificationTypes){
			if(StringUtils.isNotBlank(qualificationType)){
				final String[] picPath = request.getParameterValues(qualificationType+"[]");
				addFile(qualificationType, picPath, addip);
			}
		}
		TokenUtils.validShiroToken(TokenConstants.TOKEN_WEB_BORROW_QUALIFICATION_ADD);
	}


}