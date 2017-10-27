package com.rd.ifaes.core.account.mapper;

import com.rd.ifaes.core.account.domain.CashFeeMarkLog;
import com.rd.ifaes.core.base.mapper.BaseMapper;

public interface CashFeeMarkLogMapper extends BaseMapper<CashFeeMarkLog> {
	
	CashFeeMarkLog findOne(CashFeeMarkLog cashFeeMarkLog);

}
