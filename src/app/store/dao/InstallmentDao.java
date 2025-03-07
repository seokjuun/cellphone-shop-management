package app.store.dao;

import app.store.common.DBManager;
import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class InstallmentDao {

    // 🔹 특정 고객의 남은 할부 계약 조회
    public Map<Integer, BigDecimal> getRemainingInstallments(int customerId) {
        Map<Integer, BigDecimal> installmentData = new HashMap<>();
        String sql = "SELECT sale_id, (total_price / installment_months) * (installment_months - TIMESTAMPDIFF(MONTH, sale_date, NOW())) AS remaining_amount " +
                "FROM Sales WHERE customer_id = ? AND installment_months > 0";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, customerId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                installmentData.put(rs.getInt("sale_id"), rs.getBigDecimal("remaining_amount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(rs, pstmt, conn);
        }
        return installmentData;
    }
}