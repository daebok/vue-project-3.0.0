package com.rd.ifaes.common.orm.dialect.db;

import com.rd.ifaes.common.orm.dialect.Dialect;
/**
 * 数据库方言抽象类
 * @author lh
 * @since 2017-04-11
 * @version 3.0
 *
 */
public class AbstractDialect implements Dialect {

	@Override
	public boolean supportsLimit() {
		return false;
	}

	@Override
	public String getLimitString(String sql, int offset, int limit) {
		return null;
	}

	
	public void supportedSql(String sql) {
        if (sql.trim().toUpperCase().endsWith("FOR UPDATE")) {
            throw new RuntimeException("分页插件不支持包含for update的sql");
        }
    }
	@Override
	public String getCountString(String sql) {
		return "select count(1) from (" + sql + ") tmp_count";
	}

}
