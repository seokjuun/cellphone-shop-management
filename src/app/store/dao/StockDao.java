package app.store.dao;

import app.store.common.DBManager;
import java.sql.*;

public class StockDao {

    // 🔹 특정 제품의 재고 확인
    public int getStock(int phoneId) {
        int stock = 0;
        String sql = "SELECT stock FROM Phone WHERE phone_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, phoneId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                stock = rs.getInt("stock");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(rs, pstmt, conn);
        }
        return stock;
    }

    // 🔹 재고 업데이트 (입고 또는 출고)
    public int updateStock(int phoneId, int quantity) {
        int result = -1;
        String sql = "UPDATE Phone SET stock = stock + ? WHERE phone_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, phoneId);

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(pstmt, conn);
        }
        return result;
    }
}