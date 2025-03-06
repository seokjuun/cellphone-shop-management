package app.store.dao;

import app.store.dto.Plan;
import java.math.BigDecimal;
import java.util.List;

public class PlanDaoTest {
    public static void main(String[] args) {
        PlanDao dao = new PlanDao();

        // 요금제 추가 테스트
        Plan newPlan = new Plan(1, "4G 프리미엄", new BigDecimal("39000"), "무제한", "무제한", "무제한");
        int insertResult = dao.insertPlan(newPlan);
        System.out.println(insertResult > 0 ? "요금제 등록 성공" : "요금제 등록 실패");

        // 전체 요금제 목록 조회
        List<Plan> plans = dao.listPlans();
        System.out.println("현재 요금제 목록:");
        for (Plan plan : plans) {
            System.out.println(plan);
        }
    }
}