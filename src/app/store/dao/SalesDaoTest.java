package app.store.dao;

import app.store.dto.Sales;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class SalesDaoTest {
    public static void main(String[] args) {
        SalesDao dao = new SalesDao();

        // 판매 등록 테스트 (새로운 판매 추가)
        Sales newSale = new Sales(
                0, // sale_id (AUTO_INCREMENT)
                new Timestamp(System.currentTimeMillis()), // 현재 시간
                new BigDecimal("1000000"), // 총 가격 (예시)
                1, // customer_id (존재하는 고객 ID 사용)
                1, // phone_id (존재하는 휴대폰 ID 사용)
                1, // plan_id (존재하는 요금제 ID 사용)
                1, // employee_id (존재하는 직원 ID 사용)
                "기기변경", // 개통 유형
                "신규유심", // 유심 옵션
                24 // 할부 개월 수
        );

        int insertResult = dao.insertSale(newSale);
        System.out.println(insertResult > 0 ? " 판매 등록 성공" : " 판매 등록 실패");

        // 전체 판매 내역 조회 테스트
        List<Sales> salesList = dao.listSales();
        System.out.println("현재 판매 내역:");
        for (Sales sale : salesList) {
            System.out.println(sale);
        }

        // 특정 판매 조회 테스트
        int saleIdToCheck = newSale.getSaleId(); // 방금 추가한 판매 ID
        Sales fetchedSale = dao.getSaleById(saleIdToCheck);
        System.out.println(fetchedSale != null ? "특정 판매 조회 성공: " + fetchedSale : "특정 판매 조회 실패");

        // 판매 수정 테스트 (할부 개월 수 변경)
        newSale.setInstallmentMonths(36);
        int updateResult = dao.updateSale(newSale);
        System.out.println(updateResult > 0 ? "판매 수정 성공" : "판매 수정 실패");

        // 판매 삭제 테스트
//        int deleteResult = dao.deleteSale(saleIdToCheck);
//        System.out.println(deleteResult > 0 ? "판매 삭제 성공" : "판매 삭제 실패");
    }
}