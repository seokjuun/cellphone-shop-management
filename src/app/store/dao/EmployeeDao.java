package app.store.dao;

import app.store.common.DBManager;
import app.store.dto.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDao {

    // 직원 추가 (INSERT)
    public int insertEmployee(Employee employee) {
        int result = -1;
        String sql = "INSERT INTO Employee (name, position) VALUES (?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getPosition());

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(pstmt, conn);
        }
        return result;
    }

    // 직원 목록 조회 (SELECT ALL)
    public List<Employee> listEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employee";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(rs, pstmt, conn);
        }
        return employees;
    }

    // 특정 직원 조회
    public Employee getEmployeeById(int employeeId) {
        Employee employee = null;
        String sql = "SELECT * FROM Employee WHERE employee_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                employee = mapResultSetToEmployee(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(rs, pstmt, conn);
        }
        return employee;
    }

    // 🔹 직원 검색 (ID 또는 이름)
    public List<Employee> searchEmployees(String keyword) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employee WHERE employee_id LIKE ? OR name LIKE ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(rs, pstmt, conn);
        }
        return employees;
    }

    // 🔹 직원 정보 수정
    public int updateEmployee(Employee employee) {
        int result = -1;
        String sql = "UPDATE Employee SET name = ?, position = ? WHERE employee_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getPosition());
            pstmt.setInt(3, employee.getEmployeeId());

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(pstmt, conn);
        }
        return result;
    }

    // 🔹 직원 삭제
    public int deleteEmployee(int employeeId) {
        int result = -1;
        String sql = "DELETE FROM Employee WHERE employee_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, employeeId);

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(pstmt, conn);
        }
        return result;
    }

    // ResultSet을 Employee 객체로 변환하는 메서드
    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        return new Employee(
                rs.getInt("employee_id"),
                rs.getString("name"),
                rs.getString("position")
        );
    }
}