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

    // 🔹 특정 고객이 받은 할인 내역 조회
    public List<String> getCustomerDiscounts(int customerId) {
        List<String> discountList = new ArrayList<>();
        String sql = "SELECT d.discount_id, d.discount_amount, d.activation_type, d.start_date, d.end_date, p.model_name " +
                "FROM Discount_History dh " +
                "JOIN Discount_Policy d ON dh.discount_id = d.discount_id " +
                "JOIN Sales s ON dh.sale_id = s.sale_id " +
                "JOIN Phone p ON s.phone_id = p.phone_id " +
                "WHERE s.customer_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, customerId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                discountList.add("할인 ID: " + rs.getInt("discount_id") +
                        " | 할인 금액: " + rs.getBigDecimal("discount_amount") +
                        "원 | 개통 유형: " + rs.getString("activation_type") +
                        " | 적용 기간: " + rs.getDate("start_date") + " ~ " + rs.getDate("end_date") +
                        " | 휴대폰 모델: " + rs.getString("model_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(rs, pstmt, conn);
        }
        return discountList;
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