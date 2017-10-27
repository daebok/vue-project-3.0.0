package com.rd.ifaes.core.project.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rd.ifaes.common.security.shiro.IdGen;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.project.domain.BorrowFollow;
import com.rd.ifaes.core.project.domain.BorrowUpload;
import com.rd.ifaes.core.project.mapper.BorrowFollowMapper;
import com.rd.ifaes.core.project.service.BorrowFollowService;
import com.rd.ifaes.core.project.service.BorrowUploadService;

/**
 * ServiceImpl:BorrowFollowServiceImpl
 * @author wywO
 * @version 3.0
 * @date 2016-8-17
 */
@Service("borrowFollowService") 
public class BorrowFollowServiceImpl  extends BaseServiceImpl<BorrowFollowMapper, BorrowFollow> implements BorrowFollowService{
	
    //@Resource
    //private BorrowFollowMapper borrowFollowMapper;
	@Resource
	private BorrowUploadService borrowUploadService;
	
	@Override
	public void save(BorrowFollow entity) {
		savePics(entity);
		super.save(entity);
	}
	
	private void savePics(BorrowFollow entity){
		if(StringUtils.isNotBlank(entity.getUuid())){
			borrowUploadService.deleteByProjectId(entity.getProjectId(), new String[]{BorrowUpload.FILE_TYPE_BORROWFOLLOW});
		}
		//保存资料信息
		final List<BorrowUpload> uploadList = new ArrayList<>();
		final String[] picPath = entity.getPicPath();
		final List<String> saveFilePathList = new ArrayList<>();
		if(picPath != null && picPath.length > 0){//借款资料			
			int index = 0;
			for (final String pp : picPath) {
				if(saveFilePathList.contains(pp)){
					continue;
				}
				saveFilePathList.add(pp);
				final BorrowUpload upload = new BorrowUpload();
				upload.setUuid(IdGen.uuid());
				upload.setAddTime(DateUtils.getNow());
				upload.setProjectId(entity.getProjectId());
				upload.setFilePath(pp);
				upload.setFileType(BorrowUpload.FILE_TYPE_BORROWFOLLOW);
				upload.setSort(index++);
				uploadList.add(upload);				
			}
		}
		if(!uploadList.isEmpty()){
			borrowUploadService.insertBatch(uploadList);			
		}
	}

}