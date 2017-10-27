package com.rd.ifaes.core.project.service;

import java.util.List;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.project.domain.BorrowUpload;

/**
 * Service Interface:BorrowUploadService
 * @author FangJun
 * @version 3.0
 * @date 2016-6-20
 */
public interface BorrowUploadService extends BaseService<BorrowUpload>{
	
	/**
	 * 删除借款资料
	 * @param projectId 
	 * @return
	 */
	int deleteByProjectId(String projectId,String [] fileTypes);
    /**
     * 查询借款项目资料
     * @author  FangJun
     * @date 2016年9月13日
     * @param projectId  项目UUID
     * @return 项目资料列表
     */
	List<BorrowUpload> findBorrowPic(String projectId);
}