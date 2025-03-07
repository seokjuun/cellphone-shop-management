package app.store.gui;

import app.store.dao.*;
import app.store.dto.Sales;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.Map;
import java.util.List;

public class SaleRegistrationForm extends JFrame {
    private JComboBox<String> customerComboBox, phoneComboBox, employeeComboBox;
    private JComboBox<String> planComboBox, activationTypeComboBox, simOptionComboBox, installmentComboBox, discountComboBox;
    private JButton registerSaleButton, loadDiscountButton;
    private SalesDao salesDao = new SalesDao();
    private DiscountPolicyDao discountPolicyDao = new DiscountPolicyDao();
    private CustomerDao customerDao = new CustomerDao();
    private PhoneDao phoneDao = new PhoneDao();
    private EmployeeDao employeeDao = new EmployeeDao();
    private PlanDao planDao = new PlanDao();

    public SaleRegistrationForm() {
        setTitle("판매 등록");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(9, 2, 5, 5));

        // 1️⃣ 고객 선택
        add(new JLabel("고객 선택:"));
        customerComboBox = new JComboBox<>(getCustomerList());
        add(customerComboBox);

        // 2️⃣ 휴대폰 선택
        add(new JLabel("휴대폰 선택:"));
        phoneComboBox = new JComboBox<>(getPhoneList());
        add(phoneComboBox);

        // 3️⃣ 직원 선택
        add(new JLabel("판매 직원 선택:"));
        employeeComboBox = new JComboBox<>(getEmployeeList());
        add(employeeComboBox);

        // 4️⃣ 요금제 선택 (데이터베이스에서 불러오기)
        add(new JLabel("요금제 선택:"));
        planComboBox = new JComboBox<>(getPlanList());
        add(planComboBox);
        // 5️⃣ 개통 유형
        add(new JLabel("개통 유형:"));
        activationTypeComboBox = new JComboBox<>(new String[]{"기기변경", "신규가입", "번호이동"});
        add(activationTypeComboBox);

        // 6️⃣ 유심 옵션
        add(new JLabel("유심 옵션:"));
        simOptionComboBox = new JComboBox<>(new String[]{"신규유심", "기존유심"});
        add(simOptionComboBox);

        // 7️⃣ 할부 개월 수
        add(new JLabel("할부 개월 수:"));
        installmentComboBox = new JComboBox<>(new String[]{"0", "24", "36"});
        add(installmentComboBox);

        // 8️⃣ 할인 적용
        add(new JLabel("적용할 할인:"));
        discountComboBox = new JComboBox<>();
        add(discountComboBox);

        loadDiscountButton = new JButton("할인 조회");
        add(loadDiscountButton);

        registerSaleButton = new JButton("판매 등록");
        add(registerSaleButton);

        // 이벤트 리스너 추가
        loadDiscountButton.addActionListener(e -> loadDiscountOptions());
        registerSaleButton.addActionListener(e -> registerSale());

        setVisible(true);
    }

    // 고객 리스트 가져오기 (기존 메서드 활용)
    // 고객 리스트 가져오기 (ID 포함)
    private String[] getCustomerList() {
        return customerDao.listCustomers().stream()
                .map(customer -> customer.getCustomerId() + " - " + customer.getName()) // "1 - 홍길동" 형식
                .toArray(String[]::new);
    }

    // 휴대폰 리스트 가져오기 (기존 메서드 활용)
    private String[] getPhoneList() {
        return phoneDao.listPhones().stream()
                .map(phone -> phone.getModelName())
                .toArray(String[]::new);
    }

    // 직원 리스트 가져오기 (기존 메서드 활용)
    private String[] getEmployeeList() {
        return employeeDao.listEmployees().stream()
                .map(employee -> employee.getName())
                .toArray(String[]::new);
    }

    // 선택한 휴대폰에 맞는 할인 목록 조회
    private void loadDiscountOptions() {
        int phoneId = phoneComboBox.getSelectedIndex() + 1;
        String activationType = (String) activationTypeComboBox.getSelectedItem();

        Map<Integer, BigDecimal> availableDiscounts = discountPolicyDao.getAvailableDiscounts(phoneId, activationType);
        discountComboBox.removeAllItems();

        for (Map.Entry<Integer, BigDecimal> entry : availableDiscounts.entrySet()) {
            discountComboBox.addItem("할인 ID: " + entry.getKey() + " | " + entry.getValue() + "원");
        }
    }

    // 요금제 리스트 가져오기 (기존 listPlans() 활용)
    private String[] getPlanList() {
        return planDao.listPlans().stream()
                .map(plan -> plan.getPlanId() + " - " + plan.getPlanName()) // "1 - 기본요금제" 형태로 변환
                .toArray(String[]::new);
    }
    // 판매 등록 실행
    private void registerSale() {
        // customer_id 추출 (앞부분 숫자만 가져오기)
        int customerId = Integer.parseInt(customerComboBox.getSelectedItem().toString().split(" - ")[0]);
        int phoneId = phoneComboBox.getSelectedIndex() + 1;
        // 요금제 ID 추출 (앞부분 숫자만 가져오기)
        int planId = Integer.parseInt(planComboBox.getSelectedItem().toString().split(" - ")[0]);
        int employeeId = employeeComboBox.getSelectedIndex() + 1;
        String activationType = (String) activationTypeComboBox.getSelectedItem();
        String simOption = (String) simOptionComboBox.getSelectedItem();
        int installmentMonths = Integer.parseInt((String) installmentComboBox.getSelectedItem());
        int discountId = Integer.parseInt(discountComboBox.getSelectedItem().toString().split(" ")[2]);

        Sales newSale = new Sales(0, null, BigDecimal.ZERO, customerId, phoneId, planId, employeeId, activationType, simOption, installmentMonths);

        int saleResult = salesDao.insertSaleWithDiscount(newSale, discountId);

        if (saleResult > 0) {  // ✅ 성공하면 1 이상 → true로 평가
            phoneDao.reduceStock(phoneId);
            JOptionPane.showMessageDialog(this, "판매 등록 완료! 재고가 업데이트되었습니다.");
        } else {
            JOptionPane.showMessageDialog(this, "판매 등록 실패! 데이터를 확인해주세요.");
        }
    }
    public static void main(String[] args) {
        new SaleRegistrationForm();
    }
}