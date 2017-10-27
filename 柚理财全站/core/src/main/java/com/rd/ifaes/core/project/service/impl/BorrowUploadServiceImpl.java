package com.rd.ifaes.core.project.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rd.ifaes.common.annotation.Cacheable;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.core.base.service.*;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.project.domain.BorrowUpload;
import com.rd.ifaes.core.project.mapper.BorrowUploadMapper;
import com.rd.ifaes.core.project.service.BorrowUploadService;

/**
 * ServiceImpl:BorrowUploadServiceImpl
 * @author FangJun
 * @version 3.0
 * @date 2016-6-20
 */
@Service("borrowUploadService")
public class BorrowUploadServiceImpl extends BaseServiceImpl<BorrowUploadMapper, BorrowUpload> implements BorrowUploadService {

	@Override
	public int deleteByProjectId(String projectId, String[] fileTypes) {
		return dao.deleteByProjectId(projectId,fileTypes);
	}

	@Cacheable(key=CacheConstant.KEY_PREFIX_PROJECT_PIC+"{projectId}", expire=ExpireTime.TWENTY_MIN)
	public List<BorrowUpload> findBorrowPic(String projectId) {
		// 借款资料
		BorrowUpload uploadModel = new BorrowUpload();
		uploadModel.setProjectId(projectId);
		final List<BorrowUpload> uploadList = this.findList(uploadModel);
		for (BorrowUpload upload : uploadList) {
			upload.setFilePath(ConfigUtils.getValue(ConfigConstant.IMAGE_SERVER_URL) +upload.getFilePath());
		}

		return uploadList;
	}

}