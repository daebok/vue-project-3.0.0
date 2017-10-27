package com.rd.ifaes.core.risk.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.risk.domain.Papers;
import com.rd.ifaes.core.base.mapper.BaseMapper;

/**
 * Dao Interface:PapersMapper
 * @author QianPengZhan
 * @version 3.0
 * @date 2016-7-12
 */
public interface PapersMapper extends BaseMapper<Papers> {
	
	/**
	 * 找到已经发布的未删除的试卷
	 * @author QianPengZhan
	 * @date 2016年8月5日
	 * @param status   deleteFlag
	 * @return
	 */
	Papers getPapersByStatusAndDeleteFlag(@Param("status") String status,@Param("deleteFlag") String deleteFlag);
	
	/**
	 * 后台默认排序分页查询试卷
	 * @author QianPengZhan
	 * @date 2016年9月21日
	 * @param entity
	 * @return
	 */
	List<Papers> findPageList(final Papers entity);
}