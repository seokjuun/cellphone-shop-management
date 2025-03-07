//package app.store.test;
//
//import app.store.dao.DiscountPolicyDao;
//import app.store.dao.PhoneDao;
//import app.store.dao.SalesDao;
//import app.store.dto.Sales;
//import app.store.dto.DiscountPolicy;
//import app.store.dto.Phone;
//
//import java.math.BigDecimal;
//import java.sql.Timestamp;
//import java.util.List;
//
//public class SalesDaoTest {
//    public static void main(String[] args) {
//        SalesDao salesDao = new SalesDao();
//        DiscountPolicyDao discountPolicyDao = new DiscountPolicyDao();
//        PhoneDao phoneDao = new PhoneDao();
//
//        // 1️⃣ 사용자가 선택한 구매 조건
//        int phoneId = 1; // 갤럭시 25 (예시)
//        int customerId = 1;
//        int planId = 1;
//        int employeeId = 1;
//        String activationType = "기기변경";  // 개통 유형
//        String simOption = "신규유심";      // 유심 옵션
//        int installmentMonths = 24;        // 할부 기간
//
//        // 2️⃣ 선택한 휴대폰의 출고가 가져오기
//        Phone selectedPhone = phoneDao.getPhoneById(phoneId);
//        if (selectedPhone == null) {
//            System.out.println("❌ 선택한 휴대폰이 존재하지 않습니다.");
//            return;
//        }
//        BigDecimal factoryPrice = selectedPhone.getFactoryPrice();
//
//        // 3️⃣ 해당 조건에 적용 가능한 할인 정책 조회
//        DiscountPolicy discount = discountPolicyDao.getDiscountByPhoneAndType(phoneId, activationType);
//        BigDecimal discountAmount = (discount != null) ? discount.getDiscountAmount() : BigDecimal.ZERO;
//
//        // 4️⃣ 최종 가격 계산
//        BigDecimal totalPrice = factoryPrice.subtract(discountAmount);
//        if (totalPrice.compareTo(BigDecimal.ZERO) < 0) {
//            totalPrice = BigDecimal.ZERO;
//        }
//
//        // 5️⃣ 판매 데이터 생성 및 저장
//        Sales newSale = new Sales(
//                0, // sale_id (AUTO_INCREMENT)
//                new Timestamp(System.currentTimeMillis()), // 현재 시간
//                totalPrice, // 최종 가격
//                customerId, phoneId, planId, employeeId,
//                activationType, simOption, installmentMonths
//        );
//
//        int insertResult = salesDao.insertSale(newSale);
//        System.out.println(insertResult > 0 ? "✅ 판매 등록 성공" : "❌ 판매 등록 실패");
//
//        // 6️⃣ 판매 내역 조회
//        List<Sales> salesList = salesDao.listSales();
//        System.out.println("📌 현재 판매 내역:");
//        for (Sales sale : salesList) {
//            System.out.println(sale);
//        }
//    }
//}