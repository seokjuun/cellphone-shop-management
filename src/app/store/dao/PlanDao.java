package app.store.dao;

import app.store.common.DBManager;
import app.store.dto.Plan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlanDao {

    // 요금제 추가 (INSERT)
    public int insertPlan(Plan plan) {
        int result = -1;
        String sql = "INSERT INTO plan (plan_name, monthly_fee, data_limit, call_limit, text_limit) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, plan.getPlanName());
            pstmt.setBigDecimal(2, plan.getMonthlyFee());
            pstmt.setString(3, plan.getDataLimit());
            pstmt.setString(4, plan.getCallLimit());
            pstmt.setString(5, plan.getTextLimit());

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(pstmt, conn);
        }
        return result;
    }

    // 전체 요금제 목록 조회 (SELECT ALL)
    public List<Plan> listPlans() {
        List<Plan> plans = new ArrayList<>();
        String sql = "SELECT * FROM plan";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                plans.add(mapResultSetToPlan(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(rs, pstmt, conn);
        }
        return plans;
    }

    // 특정 요금제 조회
    public Plan getPlanById(int planId) {
        Plan plan = null;
        String sql = "SELECT * FROM plan WHERE plan_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, planId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                plan = mapResultSetToPlan(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(rs, pstmt, conn);
        }
        return plan;
    }

    // ResultSet을 Plan 객체로 변환하는 메서드
    private Plan mapResultSetToPlan(ResultSet rs) throws SQLException {
        return new Plan(
                rs.getInt("plan_id"),
                rs.getString("plan_name"),
                rs.getBigDecimal("monthly_fee"),
                rs.getString("data_limit"),
                rs.getString("call_limit"),
                rs.getString("text_limit")
        );
    }
}