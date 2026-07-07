package yefan.util;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DBUtil {

    private static String url;
    private static String username;
    private static String password;

    static {
        try (InputStream in = DBUtil.class.getClassLoader()
                .getResourceAsStream("db.properties")) {

            if (in == null) {
                throw new RuntimeException("找不到 db.properties 配置文件");
            }

            Properties props = new Properties();
            props.load(in);

            String driver = props.getProperty("db.driver", "com.mysql.cj.jdbc.Driver");
            Class.forName(driver);

            url = props.getProperty("db.url");
            username = props.getProperty("db.username");
            password = props.getProperty("db.password");

            System.out.println("[DBUtil] 数据库配置加载成功：" + url);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC 驱动未找到，请检查 pom.xml 依赖", e);
        } catch (Exception e) {
            throw new RuntimeException("数据库配置文件加载失败：" + e.getMessage(), e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public static void close(Connection conn) {
        if (conn != null) {
            try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public static void close(Statement stmt) {
        if (stmt != null) {
            try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public static void close(ResultSet rs) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public static void closeAll(Connection conn, Statement stmt, ResultSet rs) {
        close(rs);
        close(stmt);
        close(conn);
    }
}
