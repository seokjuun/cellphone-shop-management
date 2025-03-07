package app.store.gui;

import javax.swing.*;

import java.awt.*;

public class MainMenuForm extends JFrame {

    public MainMenuForm() {
        setTitle("휴대폰 판매 관리 시스템");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2, 10, 10)); // 4행 2열의 버튼 배치

        // 버튼 추가
        JButton salesManagementButton = new JButton("판매 등록");
        JButton salesHistoryButton = new JButton("판매 내역");
        JButton customerManagementButton = new JButton("고객 관리");
        JButton employeeManagementButton = new JButton("직원 관리");
        JButton stockManagementButton = new JButton("재고 관리");
        JButton installmentManagementButton = new JButton("할부 계약 관리");
        JButton discountHistoryButton = new JButton("할인 내역 조회");
        JButton salesStatisticsButton = new JButton("매출 통계");

        // 버튼 클릭 이벤트
        salesManagementButton.addActionListener(e -> new SaleRegistrationForm());
        salesHistoryButton.addActionListener(e -> new SalesHistoryForm()); // 새롭게 추가될 판매 내역 GUI
        customerManagementButton.addActionListener(e -> new CustomerManagementForm());
        employeeManagementButton.addActionListener(e -> new EmployeeManagementForm());
        stockManagementButton.addActionListener(e -> new StockManagementForm());
        installmentManagementButton.addActionListener(e -> new InstallmentManagementForm());
        discountHistoryButton.addActionListener(e -> new DiscountHistoryForm());
        salesStatisticsButton.addActionListener(e -> new SalesStatisticsForm());

        // 버튼 추가 (배치)
        add(salesManagementButton);
        add(salesHistoryButton);
        add(customerManagementButton);
        add(employeeManagementButton);
        add(stockManagementButton);
        add(installmentManagementButton);
        add(discountHistoryButton);
        add(salesStatisticsButton);

        setVisible(true);
    }

    public static void main(String[] args) {
        new MainMenuForm();
    }
}