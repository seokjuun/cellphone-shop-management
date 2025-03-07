package app.store.dao;

import app.store.common.DBManager;
import app.store.dto.Sales;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalesDao {

    // 판매 등록 (INSERT)
    public int insertSale(Sales sale) {
        int result = -1;
        String sql = "INSERT INTO Sales (sale_date, total_price, customer_id, phone_id, plan_id, employee_id, activation_type, sim_option, installment_months) " +
                "VALUES (CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setBigDecimal(1, sale.getTotalPrice());
            pstmt.setInt(2, sale.getCustomerId());
            pstmt.setInt(3, sale.getPhoneId());
            pstmt.setInt(4, sale.getPlanId());
            pstmt.setInt(5, sale.getEmployeeId());
            pstmt.setString(6, sale.getActivationType());
            pstmt.setString(7, sale.getSimOption());
            pstmt.setInt(8, sale.getInstallmentMonths());

            result = pstmt.executeUpdate();

            // 생성된 sale_id 가져오기
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                sale.setSaleId(generatedKeys.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(pstmt, conn);
        }
        return result;
    }

    // 판매 정보 수정 (UPDATE)
    public int updateSale(Sales sale) {
        int result = -1;
        String sql = "UPDATE Sales SET total_price = ?, customer_id = ?, phone_id = ?, plan_id = ?, employee_id = ?, " +
                "activation_type = ?, sim_option = ?, installment_months = ? WHERE sale_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setBigDecimal(1, sale.getTotalPrice());
            pstmt.setInt(2, sale.getCustomerId());
            pstmt.setInt(3, sale.getPhoneId());
            pstmt.setInt(4, sale.getPlanId());
            pstmt.setInt(5, sale.getEmployeeId());
            pstmt.setString(6, sale.getActivationType());
            pstmt.setString(7, sale.getSimOption());
            pstmt.setInt(8, sale.getInstallmentMonths());
            pstmt.setInt(9, sale.getSaleId());

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(pstmt, conn);
        }
        return result;
    }

    // 판매 취소 (DELETE)
    public int deleteSale(int saleId) {
        int result = -1;
        String sqlDeleteDiscount = "DELETE FROM Discount_History WHERE sale_id = ?";
        String sqlGetPhoneId = "SELECT phone_id FROM Sales WHERE sale_id = ?";
        String sqlUpdateStock = "UPDATE Phone SET stock = stock + 1 WHERE phone_id = ?";
        String sqlDeleteSale = "DELETE FROM Sales WHERE sale_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBManager.getConnection();
            conn.setAutoCommit(false); // 트랜잭션 시작

            // 1️⃣ 할인 내역 삭제
            pstmt = conn.prepareStatement(sqlDeleteDiscount);
            pstmt.setInt(1, saleId);
            pstmt.executeUpdate();
            pstmt.close();

            // 2️⃣ 판매된 휴대폰 ID 조회
            int phoneId = -1;
            pstmt = conn.prepareStatement(sqlGetPhoneId);
            pstmt.setInt(1, saleId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                phoneId = rs.getInt("phone_id");
            }
            rs.close();
            pstmt.close();

            // 3️⃣ 재고 복구
            if (phoneId != -1) {
                pstmt = conn.prepareStatement(sqlUpdateStock);
                pstmt.setInt(1, phoneId);
                pstmt.executeUpdate();
                pstmt.close();
            }

            // 4️⃣ 판매 내역 삭제
            pstmt = conn.prepareStatement(sqlDeleteSale);
            pstmt.setInt(1, saleId);
            result = pstmt.executeUpdate();

            conn.commit(); // 트랜잭션 완료
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(rs, pstmt, conn);
        }
        return result;
    }

    // 전체 판매 내역 조회 (SELECT ALL)
    public List<Sales> listSales() {
        List<Sales> salesList = new ArrayList<>();
        String sql = "SELECT * FROM Sales";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                salesList.add(mapResultSetToSales(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(rs, pstmt, conn);
        }
        return salesList;
    }

    // 고객별 판매 내역 조회
    public List<Sales> listSalesByCustomer(int customerId) {
        List<Sales> salesList = new ArrayList<>();
        String sql = "SELECT * FROM Sales WHERE customer_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, customerId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                salesList.add(mapResultSetToSales(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(rs, pstmt, conn);
        }
        return salesList;
    }

    // 직원별 판매 내역 조회
    public List<Sales> listSalesByEmployee(int employeeId) {
        List<Sales> salesList = new ArrayList<>();
        String sql = "SELECT * FROM Sales WHERE employee_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                salesList.add(mapResultSetToSales(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(rs, pstmt, conn);
        }
        return salesList;
    }

    // 특정 판매 상세 조회
    public Sales getSaleById(int saleId) {
        Sales sale = null;
        String sql = "SELECT * FROM Sales WHERE sale_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, saleId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                sale = mapResultSetToSales(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(rs, pstmt, conn);
        }
        return sale;
    }

    public List<Sales> searchSales(Integer customerId, Integer employeeId, Date startDate, Date endDate) {
        List<Sales> salesList = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Sales WHERE 1=1");

        if (customerId != null) sql.append(" AND customer_id = ?");
        if (employeeId != null) sql.append(" AND employee_id = ?");
        if (startDate != null) sql.append(" AND sale_date >= ?");
        if (endDate != null) sql.append(" AND sale_date <= ?");

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql.toString());

            int index = 1;
            if (customerId != null) pstmt.setInt(index++, customerId);
            if (employeeId != null) pstmt.setInt(index++, employeeId);
            if (startDate != null) pstmt.setDate(index++, startDate);
            if (endDate != null) pstmt.setDate(index++, endDate);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                salesList.add(mapResultSetToSales(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(rs, pstmt, conn);
        }
        return salesList;
    }

    // 연간 매출 데이터 조회
    public Map<String, BigDecimal> getYearlySales(String year) {
        Map<String, BigDecimal> salesData = new HashMap<>();
        String sql = "SELECT MONTH(sale_date) AS month, SUM(total_price) AS total FROM Sales WHERE YEAR(sale_date) = ? GROUP BY month";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, year);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                salesData.put(rs.getString("month") + "월", rs.getBigDecimal("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(rs, pstmt, conn);
        }
        return salesData;
    }

    // 특정 월의 매출 데이터 조회
    public Map<String, BigDecimal> getMonthlySales(String year, String month) {
        Map<String, BigDecimal> salesData = new HashMap<>();
        String sql = "SELECT DAY(sale_date) AS day, SUM(total_price) AS total FROM Sales WHERE YEAR(sale_date) = ? AND MONTH(sale_date) = ? GROUP BY day";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, year);
            pstmt.setString(2, month);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                salesData.put(rs.getString("day") + "일", rs.getBigDecimal("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(rs, pstmt, conn);
        }
        return salesData;
    }

    public int insertSaleWithDiscount(Sales sale, int discountId) {
        int result = -1;
        String insertSaleSql = "INSERT INTO Sales (sale_date, total_price, customer_id, phone_id, plan_id, employee_id, activation_type, sim_option, installment_months) " +
                "VALUES (CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?, ?, ?)";

        String insertDiscountHistorySql = "INSERT INTO Discount_History (sale_id, discount_id) VALUES (?, ?)";

        Connection conn = null;
        PreparedStatement saleStmt = null;
        PreparedStatement discountStmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = DBManager.getConnection();
            conn.setAutoCommit(false); // 트랜잭션 시작

            // 1️⃣ 판매 내역 등록
            saleStmt = conn.prepareStatement(insertSaleSql, Statement.RETURN_GENERATED_KEYS);
            saleStmt.setBigDecimal(1, sale.getTotalPrice());
            saleStmt.setInt(2, sale.getCustomerId());
            saleStmt.setInt(3, sale.getPhoneId());
            saleStmt.setInt(4, sale.getPlanId());
            saleStmt.setInt(5, sale.getEmployeeId());
            saleStmt.setString(6, sale.getActivationType());
            saleStmt.setString(7, sale.getSimOption());
            saleStmt.setInt(8, sale.getInstallmentMonths());

            int saleResult = saleStmt.executeUpdate();

            if (saleResult > 0) {
                // 생성된 sale_id 가져오기
                generatedKeys = saleStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int saleId = generatedKeys.getInt(1);
                    sale.setSaleId(saleId);

                    // 2️⃣ 할인 내역 저장 (할인 ID가 0보다 클 경우만 실행)
                    if (discountId > 0) {
                        discountStmt = conn.prepareStatement(insertDiscountHistorySql);
                        discountStmt.setInt(1, saleId);
                        discountStmt.setInt(2, discountId);
                        discountStmt.executeUpdate();
                    }
                }
            }
            conn.commit(); // 트랜잭션 완료
            result = saleResult;
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(generatedKeys, saleStmt, conn);
            if (discountStmt != null) {
                try {
                    discountStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    // ResultSet을 Sales 객체로 변환하는 메서드
    private Sales mapResultSetToSales(ResultSet rs) throws SQLException {
        return new Sales(
                rs.getInt("sale_id"),
                rs.getTimestamp("sale_date"),
                rs.getBigDecimal("total_price"),
                rs.getInt("customer_id"),
                rs.getInt("phone_id"),
                rs.getInt("plan_id"),
                rs.getInt("employee_id"),
                rs.getString("activation_type"),
                rs.getString("sim_option"),
                rs.getInt("installment_months")
        );
    }
}