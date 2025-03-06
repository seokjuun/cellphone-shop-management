package app.store.dao;

import app.store.common.DBManager;
import app.store.dto.DiscountHistory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiscountHistoryDao {

    // 할인 적용 내역 추가 (INSERT)
    public int insertDiscountHistory(DiscountHistory discountHistory) {
        int result = -1;
        String sql = "INSERT INTO Discount_History (sale_id, discount_id) VALUES (?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, discountHistory.getSaleId());
            pstmt.setInt(2, discountHistory.getDiscountId());

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(pstmt, conn);
        }
        return result;
    }

    // 특정 판매의 할인 내역 조회 (SELECT)
    public List<DiscountHistory> getDiscountsBySaleId(int saleId) {
        List<DiscountHistory> discounts = new ArrayList<>();
        String sql = "SELECT * FROM Discount_History WHERE sale_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, saleId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                discounts.add(mapResultSetToDiscountHistory(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(rs, pstmt, conn);
        }
        return discounts;
    }

    // 할인 내역 삭제 (판매 취소 시 적용)
    public int deleteDiscountHistory(int saleId) {
        int result = -1;
        String sql = "DELETE FROM Discount_History WHERE sale_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, saleId);

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(pstmt, conn);
        }
        return result;
    }

    // ResultSet을 DiscountHistory 객체로 변환하는 메서드
    private DiscountHistory mapResultSetToDiscountHistory(ResultSet rs) throws SQLException {
        return new DiscountHistory(
                rs.getInt("discount_history_id"),
                rs.getInt("sale_id"),
                rs.getInt("discount_id")
        );
    }
}