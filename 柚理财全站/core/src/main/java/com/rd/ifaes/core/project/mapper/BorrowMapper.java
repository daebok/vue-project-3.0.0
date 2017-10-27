package com.rd.ifaes.core.project.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.project.domain.Borrow;

/**
 * Dao Interface:BorrowMapper
 * @author FangJun
 * @version 3.0
 * @date 2016-6-20
 */
public interface BorrowMapper extends BaseMapper<Borrow> {

	/**
	 * 根据产品id获取是否可转让
	 * @author ZhangBiao
	 * @date 2016年10月19日
	 * @param projectId
	 * @return
	 */
	String getBondUsefulByProjectId(String projectId);
	/**
	 * 借款记录
	 * @param model 查询条件
	 * @return 符合查询条件的借款记录
	 */
	List<Borrow> 	findRecord(Borrow model);
	
	/**
	 * 根据ids获取列表
	 * @param borrowIds
	 * @return
	 */
	List<Borrow> findBorrowByIds(@Param("borrowIds") String[] borrowIds);
	
	/**
	 * 根据日期查询当日复审借款
	 * @author fxl
	 * @date 2017年2月22日
	 * @param date
	 * @return
	 */
	List<Borrow> findBorrowListByDate(@Param("startDate")Date startDate,@Param("endDate")Date endDate);
	
	/**
	 * 根据projectId查询borrow
	 * @param projectId
	 * @return
	 */
	Borrow getByProjectId(String projectId);
	
	/**
	 * 查询具有合同协议的记录
	 * @return
	 */
	List<Borrow> findByContract(Borrow model);
}