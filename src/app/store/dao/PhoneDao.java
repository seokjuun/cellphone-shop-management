package app.store.dao;

import app.store.common.DBManager;
import app.store.dto.Phone;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhoneDao {

    // 휴대폰 추가 (INSERT)
    public int insertPhone(Phone phone) {
        int result = -1;
        String sql = "INSERT INTO Phone (model_name, brand, factory_price, release_date, stock) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, phone.getModelName());
            pstmt.setString(2, phone.getBrand());
            pstmt.setBigDecimal(3, phone.getFactoryPrice());
            pstmt.setDate(4, phone.getReleaseDate());
            pstmt.setInt(5, phone.getStock());

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(pstmt, conn);
        }
        return result;
    }

    // 휴대폰 정보 수정 (UPDATE)
    public int updatePhone(Phone phone) {
        int result = -1;
        String sql = "UPDATE Phone SET model_name = ?, brand = ?, factory_price = ?, release_date = ?, stock = ? WHERE phone_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, phone.getModelName());
            pstmt.setString(2, phone.getBrand());
            pstmt.setBigDecimal(3, phone.getFactoryPrice());
            pstmt.setDate(4, phone.getReleaseDate());
            pstmt.setInt(5, phone.getStock());
            pstmt.setInt(6, phone.getPhoneId());

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(pstmt, conn);
        }
        return result;
    }

    // 휴대폰 삭제 (DELETE)
    public int deletePhone(int phoneId) {
        int result = -1;
        String sql = "DELETE FROM Phone WHERE phone_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, phoneId);

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(pstmt, conn);
        }
        return result;
    }

    // 전체 휴대폰 목록 조회 (SELECT ALL)
    public List<Phone> listPhones() {
        List<Phone> phones = new ArrayList<>();
        String sql = "SELECT * FROM Phone";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                phones.add(mapResultSetToPhone(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(rs, pstmt, conn);
        }
        return phones;
    }

    // 휴대폰 검색 (모델명 또는 브랜드)
    public List<Phone> searchPhones(String keyword) {
        List<Phone> phones = new ArrayList<>();
        String sql = "SELECT * FROM Phone WHERE model_name LIKE ? OR brand LIKE ?";

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
                phones.add(mapResultSetToPhone(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(rs, pstmt, conn);
        }
        return phones;
    }

    // 특정 휴대폰 상세 조회
    public Phone getPhoneById(int phoneId) {
        Phone phone = null;
        String sql = "SELECT * FROM Phone WHERE phone_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, phoneId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                phone = mapResultSetToPhone(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(rs, pstmt, conn);
        }
        return phone;
    }

    // 휴대폰 재고 업데이트 (입고 또는 판매 후 변경)
    public int updateStock(int phoneId, int newStock) {
        int result = -1;
        String sql = "UPDATE Phone SET stock = ? WHERE phone_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, newStock);
            pstmt.setInt(2, phoneId);

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(pstmt, conn);
        }
        return result;
    }

    // 특정 휴대폰의 재고 감소 (판매 시)
    public int reduceStock(int phoneId) {
        int result = -1;
        String sql = "UPDATE Phone SET stock = stock - 1 WHERE phone_id = ? AND stock > 0";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, phoneId);

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(pstmt, conn);
        }
        return result;
    }

    // ResultSet을 Phone 객체로 변환하는 메서드
    private Phone mapResultSetToPhone(ResultSet rs) throws SQLException {
        return new Phone(
                rs.getInt("phone_id"),
                rs.getString("model_name"),
                rs.getString("brand"),
                rs.getBigDecimal("factory_price"),
                rs.getDate("release_date"),
                rs.getInt("stock")
        );
    }
}