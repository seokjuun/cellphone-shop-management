package app.store.dao;

import app.store.dto.DiscountPolicy;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class DiscountPolicyDaoTest {
    public static void main(String[] args) {
        DiscountPolicyDao dao = new DiscountPolicyDao();

        // 할인 정책 추가 테스트
//        DiscountPolicy newDiscount = new DiscountPolicy(0, 1, "기기변경", new BigDecimal("200000"), Date.valueOf("2024-03-01"), Date.valueOf("2024-03-31"));
//        int insertResult = dao.insertDiscountPolicy(newDiscount);
//        System.out.println(insertResult > 0 ? "할인 정책 등록 성공" : "할인 정책 등록 실패");

        // 전체 할인 정책 목록 조회
        List<DiscountPolicy> policies = dao.listDiscountPolicies();
        System.out.println("현재 할인 정책 목록:");
        for (DiscountPolicy policy : policies) {
            System.out.println(policy);
        }

        // 특정 휴대폰 & 개통 유형 할인 조회
        DiscountPolicy foundPolicy = dao.getDiscountByPhoneAndType(1, "기기변경");
        System.out.println(foundPolicy != null ? "특정 할인 조회 성공: " + foundPolicy : "특정 할인 조회 실패");
    }
}