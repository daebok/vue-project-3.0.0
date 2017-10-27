package com.rd.ifaes.core.tpp.mapper;


import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.tpp.domain.TppCbhbResponse;

/**
 * Dao Interface:TppCbhbResponseMapper
 * @author qpz
 * @version 3.0
 * @date 2017-3-23
 */
public interface TppCbhbResponseMapper extends BaseMapper<TppCbhbResponse> {
	/**
	 * 根据请求流水号查询响应记录
	 * @author QianPengZhan
	 * @date 2017年3月23日
	 * @param merBillNo
	 * @return
	 */
	TppCbhbResponse getByMerbillNo(@Param("merBillNo") String merBillNo);
}