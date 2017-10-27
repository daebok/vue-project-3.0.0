package com.rd.ifaes.common.orm.dialect.db;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectGroupByClause;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;
import com.alibaba.druid.util.JdbcUtils;

/**
 * Mysql方言的实现
 *
 * @author poplar.yfyang
 * @version 1.0 2010-10-10 下午12:31
 * @since JDK 1.5
 */
public class MySQLDialect extends AbstractDialect {

	private static final Logger LOGGER = LoggerFactory.getLogger(MySQLDialect.class);

    @Override
    public String getLimitString(String sql, int offset, int limit) {
        return getLimitString(sql, offset, Integer.toString(offset),
                Integer.toString(limit));
    }

    public boolean supportsLimit() {
        return true;
    }

    /**
     * 将sql变成分页sql语句,提供将offset及limit使用占位符号(placeholder)替换.
     * <pre>
     * 如mysql
     * dialect.getLimitString("select * from user", 12, ":offset",0,":limit") 将返回
     * select * from user limit :offset,:limit
     * </pre>
     *
     * @param sql               实际SQL语句
     * @param offset            分页开始纪录条数
     * @param offsetPlaceholder 分页开始纪录条数－占位符号
     * @param limitPlaceholder  分页纪录条数占位符号
     * @return 包含占位符的分页sql
     */
    public String getLimitString(String sql, int offset, String offsetPlaceholder, String limitPlaceholder) {
        StringBuilder stringBuilder = new StringBuilder(sql);
        stringBuilder.append(" limit ");
        if (offset > 0) {
            stringBuilder.append(offsetPlaceholder).append(",").append(limitPlaceholder);
        } else {
            stringBuilder.append(limitPlaceholder);
        }
        return stringBuilder.toString();
    }
    
    @Override
    public String getCountString(String sql) {

		// 校验是否支持该sql
    	supportedSql(sql);
		StringBuilder sqlBuffer;
		try {
			MySqlStatementParser parser = new MySqlStatementParser(sql);
			List<SQLStatement> statementList = parser.parseStatementList();

			sqlBuffer = new StringBuilder("select count(0) tmp_count ");
			final StringBuilder from = new StringBuilder();
			final StringBuilder where = new StringBuilder();

			SQLASTOutputVisitor fromVisitor = SQLUtils.createFormatOutputVisitor(from, statementList, JdbcUtils.MYSQL);
			SQLASTOutputVisitor whereVisitor = SQLUtils.createFormatOutputVisitor(where, statementList,
					JdbcUtils.MYSQL);

			if (CollectionUtils.isNotEmpty(statementList) && statementList.size() == 1) {
				SQLStatement stmt = statementList.get(0);
				if (stmt instanceof SQLSelectStatement) {
					SQLSelectStatement sstmt = (SQLSelectStatement) stmt;
					SQLSelect sqlselect = sstmt.getSelect();
					SQLSelectQueryBlock query = (SQLSelectQueryBlock) sqlselect.getQuery();
					query.getFrom().accept(fromVisitor);
					sqlBuffer.append(" \n from ").append(from.toString());
					SQLExpr whereExpr = query.getWhere();
					if (whereExpr != null) {
						whereExpr.accept(whereVisitor);
						sqlBuffer.append(" \n where ").append(where);
					}
					SQLSelectGroupByClause gb = query.getGroupBy();
					if (gb != null) {
						return super.getCountString(sql);
					}
				}
			} else {
				throw new IllegalArgumentException(sql);
			}
			return sqlBuffer.toString();
		} catch (Exception e) {
			LOGGER.warn(e.getMessage());
		}	
    	return super.getCountString(sql);
    }
    

}
