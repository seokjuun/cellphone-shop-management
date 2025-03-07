package app.store.dao;

import app.store.common.DBManager;
import app.store.dto.DiscountPolicy;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscountPolicyDao {

    // 할인 정책 추가 (INSERT)
    public int insertDiscountPolicy(DiscountPolicy discountPolicy) {
        int result = -1;
        String sql = "INSERT INTO Discount_Policy (phone_id, activation_type, discount_amount, start_date, end_date) " +
                "VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, discountPolicy.getPhoneId());
            pstmt.setString(2, discountPolicy.getActivationType());
            pstmt.setBigDecimal(3, discountPolicy.getDiscountAmount());
            pstmt.setDate(4, discountPolicy.getStartDate());
            pstmt.setDate(5, discountPolicy.getEndDate());

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(pstmt, conn);
        }
        return result;
    }

    // 전체 할인 정책 목록 조회 (SELECT ALL)
    public List<DiscountPolicy> listDiscountPolicies() {
        List<DiscountPolicy> policies = new ArrayList<>();
        String sql = "SELECT * FROM Discount_Policy";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                policies.add(mapResultSetToDiscountPolicy(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(rs, pstmt, conn);
        }
        return policies;
    }

    // 특정 할인 정책 조회
    public DiscountPolicy getDiscountPolicyById(int discountId) {
        DiscountPolicy discountPolicy = null;
        String sql = "SELECT * FROM Discount_Policy WHERE discount_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, discountId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                discountPolicy = mapResultSetToDiscountPolicy(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(rs, pstmt, conn);
        }
        return discountPolicy;
    }

    // 특정 휴대폰 & 개통 유형에 따른 할인 조회
    public DiscountPolicy getDiscountByPhoneAndType(int phoneId, String activationType) {
        DiscountPolicy discountPolicy = null;
        String sql = "SELECT * FROM Discount_Policy WHERE phone_id = ? AND activation_type = ? AND start_date <= CURDATE() AND end_date >= CURDATE()";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, phoneId);
            pstmt.setString(2, activationType);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                discountPolicy = mapResultSetToDiscountPolicy(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(rs, pstmt, conn);
        }
        return discountPolicy;
    }

    // 🔹 특정 휴대폰과 개통 유형에 적용 가능한 할인 목록 조회
    public Map<Integer, BigDecimal> getAvailableDiscounts(int phoneId, String activationType) {
        Map<Integer, BigDecimal> discounts = new HashMap<>();
        String sql = "SELECT discount_id, discount_amount FROM Discount_Policy " +
                "WHERE phone_id = ? AND activation_type = ? " +
                "AND start_date <= CURDATE() AND end_date >= CURDATE()";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, phoneId);
            pstmt.setString(2, activationType);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                discounts.put(rs.getInt("discount_id"), rs.getBigDecimal("discount_amount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(rs, pstmt, conn);
        }
        return discounts;
    }

    // ResultSet을 DiscountPolicy 객체로 변환하는 메서드
    private DiscountPolicy mapResultSetToDiscountPolicy(ResultSet rs) throws SQLException {
        return new DiscountPolicy(
                rs.getInt("discount_id"),
                rs.getInt("phone_id"),
                rs.getString("activation_type"),
                rs.getBigDecimal("discount_amount"),
                rs.getDate("start_date"),
                rs.getDate("end_date")
        );
    }
}