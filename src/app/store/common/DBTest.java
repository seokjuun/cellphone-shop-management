package app.store.common;

import java.sql.Connection;

public class DBTest {
    public static void main(String[] args) {
        // DB 연결 테스트
        Connection conn = DBManager.getConnection();
        if (conn != null) {
            System.out.println("DB 연결 성공!");
        } else {
            System.out.println("DB 연결 실패!");
        }
        DBManager.releaseConnection(null, conn);
    }
}