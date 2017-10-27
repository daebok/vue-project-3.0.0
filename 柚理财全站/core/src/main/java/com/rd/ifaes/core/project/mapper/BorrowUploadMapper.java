package com.rd.ifaes.core.project.mapper;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.project.domain.BorrowUpload;

/**
 * Dao Interface:BorrowUploadMapper
 * @author FangJun
 * @version 3.0
 * @date 2016-6-20
 */
public interface BorrowUploadMapper extends BaseMapper<BorrowUpload> {
	
	/**
	 * 删除借款资料
	 * @param projectId 
	 * @return
	 */
	int deleteByProjectId(@Param("projectId") String projectId, @Param("fileTypes") String[] fileTypes);

}