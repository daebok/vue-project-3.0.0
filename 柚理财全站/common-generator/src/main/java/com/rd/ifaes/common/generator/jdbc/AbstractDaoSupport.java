package com.rd.ifaes.common.generator.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.rd.ifaes.common.generator.config.DbConfig;
import com.rd.ifaes.common.generator.config.SetupConfig;
import com.rd.ifaes.common.generator.core.Column;

/**
 * @author lh@erongdu.com
 */
public abstract class AbstractDaoSupport {

    protected static String driverName = ""; 	// Load the JDBC driver
    protected static String url = ""; 		// a JDBC url
    protected static String username = "";
    protected static String password = "";

    // init config
    static {
        DbConfig dbConfig = SetupConfig.getInstance().getDbConfig();
        driverName = dbConfig.getDriverClass();
        url = dbConfig.getUrl();
        username = dbConfig.getUsername();
        password = dbConfig.getPassword();
    }

    public static AbstractDaoSupport getInstance() {
        if (driverName.contains("oracle")) {
            return new OracleDao();
        }
        return new MysqlDao();
    }


    public List<String> queryAllTables(String nativeSql) {
        List<String> list = new ArrayList<String>();
        try {
            checkDriver();
            Connection conn = getConn();
            ResultSet rs = createQuary(conn, nativeSql);
            while (rs.next()) {
                list.add(rs.getString(1));
            }
            rs.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public abstract List<String> queryAllTables();

    public abstract List<Column> queryColumns(String tableName);

    public abstract String typesConvert(String sqlType);


    protected ResultSet createQuary(Connection conn, String sql) throws SQLException {
        return conn.createStatement().executeQuery(sql);
    }

    protected Connection getConn() throws SQLException {
        return (Connection) DriverManager.getConnection(url, username, password);
    }

    protected void checkDriver() {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
