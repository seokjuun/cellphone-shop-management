package app.store.dao;

import app.store.dto.DiscountHistory;
import java.util.List;

public class DiscountHistoryDaoTest {
    public static void main(String[] args) {
        DiscountHistoryDao dao = new DiscountHistoryDao();

        // 할인 내역 추가 테스트
//        DiscountHistory newDiscountHistory = new DiscountHistory(0, 4, 1); // sale_id=1, discount_id=1
//        int insertResult = dao.insertDiscountHistory(newDiscountHistory);
//        System.out.println(insertResult > 0 ? "할인 내역 등록 성공" : " 할인 내역 등록 실패");

        // 특정 판매의 할인 내역 조회
        List<DiscountHistory> discountHistories = dao.getDiscountsBySaleId(4);
        System.out.println("특정 판매의 할인 내역:");
        for (DiscountHistory history : discountHistories) {
            System.out.println(history);
        }

        // 할인 내역 삭제 테스트 (판매 취소 시 적용)
        int deleteResult = dao.deleteDiscountHistory(4);
        System.out.println(deleteResult > 0 ? "할인 내역 삭제 성공" : "할인 내역 삭제 실패");
    }
}