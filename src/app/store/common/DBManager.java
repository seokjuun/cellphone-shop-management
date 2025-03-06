package app.store.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBManager {

    static final String URL = "jdbc:mysql://localhost:3306/uplus_store";
    static final String USER = "root";
    static final String PASSWORD = "2gkrsus2qks";

    // 데이터베이스 연결 가져오기
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        }  catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    // 리소스 해제 (Connection, PreparedStatement, ResultSet)
    public static void releaseConnection(ResultSet rs, PreparedStatement pstmt, Connection conn) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (pstmt != null) pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void releaseConnection(PreparedStatement pstmt, Connection conn) {
        releaseConnection(null, pstmt, conn);
    }
}