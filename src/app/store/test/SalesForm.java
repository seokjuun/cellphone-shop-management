//package app.store.gui;
//
//import app.store.dao.*;
//import app.store.dto.*;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.math.BigDecimal;
//import java.sql.Timestamp;
//import java.util.List;
//
//public class SalesForm extends JFrame {
//    private JComboBox<String> phoneComboBox;
//    private JComboBox<String> activationTypeComboBox;
//    private JComboBox<String> simOptionComboBox;
//    private JComboBox<Integer> installmentComboBox;
//    private JLabel priceLabel;
//    private JButton calculatePriceButton;
//    private JButton submitButton;
//    private JButton viewSalesButton; // 판매 내역 보기 버튼
//
//    private SalesDao salesDao = new SalesDao();
//    private PhoneDao phoneDao = new PhoneDao();
//    private DiscountPolicyDao discountPolicyDao = new DiscountPolicyDao();
//
//    public SalesForm() {
//        setTitle("휴대폰 판매 등록");
//        setSize(400, 400);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLayout(new GridLayout(7, 2));
//
//        // 1️⃣ 휴대폰 목록 불러오기
//        List<Phone> phoneList = phoneDao.listPhones();
//        phoneComboBox = new JComboBox<>();
//        for (Phone phone : phoneList) {
//            phoneComboBox.addItem(phone.getPhoneId() + " - " + phone.getModelName());
//        }
//
//        // 2️⃣ 개통 유형, 유심 옵션, 할부 옵션 설정
//        activationTypeComboBox = new JComboBox<>(new String[]{"기기변경", "신규가입", "번호이동"});
//        simOptionComboBox = new JComboBox<>(new String[]{"신규유심", "기존유심"});
//        installmentComboBox = new JComboBox<>(new Integer[]{0, 24, 36});
//
//        priceLabel = new JLabel("출고가: 0원");
//
//        calculatePriceButton = new JButton("할인 적용 후 가격 계산");
//        submitButton = new JButton("판매 등록");
//        viewSalesButton = new JButton("판매 내역 보기");
//        // 3️⃣ UI 배치
//        add(new JLabel("휴대폰 선택:"));
//        add(phoneComboBox);
//        add(new JLabel("개통 유형:"));
//        add(activationTypeComboBox);
//        add(new JLabel("유심 옵션:"));
//        add(simOptionComboBox);
//        add(new JLabel("할부 개월:"));
//        add(installmentComboBox);
//        add(priceLabel);
//        add(calculatePriceButton);
//        add(new JLabel());
//        add(submitButton);
//        add(viewSalesButton);
//
//        viewSalesButton.addActionListener(e -> new SalesHistoryForm());
//
//        // 4️⃣ 가격 계산 버튼 이벤트
//        calculatePriceButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                calculatePrice();
//            }
//        });
//
//        // 5️⃣ 판매 등록 버튼 이벤트
//        submitButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                registerSale();
//            }
//        });
//
//        setVisible(true);
//    }
//
//    // 가격 계산 로직
//    private void calculatePrice() {
//        String selectedPhoneStr = (String) phoneComboBox.getSelectedItem();
//        if (selectedPhoneStr == null) return;
//
//        int phoneId = Integer.parseInt(selectedPhoneStr.split(" - ")[0]);
//        String activationType = (String) activationTypeComboBox.getSelectedItem();
//
//        // 출고가 가져오기
//        Phone phone = phoneDao.getPhoneById(phoneId);
//        if (phone == null) {
//            JOptionPane.showMessageDialog(this, "휴대폰 정보를 찾을 수 없습니다.");
//            return;
//        }
//
//        BigDecimal factoryPrice = phone.getFactoryPrice();
//
//        // 할인 정보 가져오기
//        DiscountPolicy discount = discountPolicyDao.getDiscountByPhoneAndType(phoneId, activationType);
//        BigDecimal discountAmount = (discount != null) ? discount.getDiscountAmount() : BigDecimal.ZERO;
//
//        // 최종 가격 계산
//        BigDecimal finalPrice = factoryPrice.subtract(discountAmount);
//        if (finalPrice.compareTo(BigDecimal.ZERO) < 0) {
//            finalPrice = BigDecimal.ZERO;
//        }
//
//        priceLabel.setText("출고가: " + finalPrice + "원");
//    }
//
//    // 판매 등록
//    private void registerSale() {
//        String selectedPhoneStr = (String) phoneComboBox.getSelectedItem();
//        if (selectedPhoneStr == null) return;
//
//        int phoneId = Integer.parseInt(selectedPhoneStr.split(" - ")[0]);
//        String activationType = (String) activationTypeComboBox.getSelectedItem();
//        String simOption = (String) simOptionComboBox.getSelectedItem();
//        int installmentMonths = (Integer) installmentComboBox.getSelectedItem();
//
//        // 고객 정보와 직원 정보 (테스트용으로 기본값 사용)
//        int customerId = 1;
//        int planId = 1;
//        int employeeId = 1;
//
//        // 최종 가격 계산
//        Phone phone = phoneDao.getPhoneById(phoneId);
//        DiscountPolicy discount = discountPolicyDao.getDiscountByPhoneAndType(phoneId, activationType);
//        BigDecimal discountAmount = (discount != null) ? discount.getDiscountAmount() : BigDecimal.ZERO;
//        BigDecimal finalPrice = phone.getFactoryPrice().subtract(discountAmount);
//        if (finalPrice.compareTo(BigDecimal.ZERO) < 0) {
//            finalPrice = BigDecimal.ZERO;
//        }
//
//        // 판매 등록
//        Sales newSale = new Sales(
//                0, new Timestamp(System.currentTimeMillis()), finalPrice,
//                customerId, phoneId, planId, employeeId, activationType, simOption, installmentMonths
//        );
//
//        int result = salesDao.insertSale(newSale);
//        if (result > 0) {
//            JOptionPane.showMessageDialog(this, "✅ 판매 등록 성공!");
//        } else {
//            JOptionPane.showMessageDialog(this, "❌ 판매 등록 실패!");
//        }
//    }
//
//    public static void main(String[] args) {
//        new SalesForm();
//    }
//}