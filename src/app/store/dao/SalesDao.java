package app.store.dao;

import app.store.common.DBManager;
import app.store.dto.Sales;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
        String sql = "DELETE FROM Sales WHERE sale_id = ?";

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