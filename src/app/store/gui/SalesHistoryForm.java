package app.store.gui;

import app.store.dao.SalesDao;
import app.store.dto.Sales;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.List;

public class SalesHistoryForm extends JFrame {
    private JTable salesTable;
    private DefaultTableModel tableModel;
    private SalesDao salesDao = new SalesDao();

    // 필터 입력 필드
    private JTextField customerIdField;
    private JTextField employeeIdField;
    private JTextField startDateField;
    private JTextField endDateField;
    private JButton searchButton;
    private JButton resetButton;
    private JButton deleteButton; // 삭제 버튼 추가
    private JButton detailButton; // 상세 보기 버튼 추가


    public SalesHistoryForm() {
        setTitle("판매 내역 조회");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1️⃣ 테이블 설정
        String[] columnNames = {"판매 ID", "날짜", "총 가격", "고객 ID", "휴대폰 ID", "요금제 ID", "직원 ID", "개통 유형", "유심 옵션", "할부 개월"};
        tableModel = new DefaultTableModel(columnNames, 0);
        salesTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(salesTable);

        // 2️⃣ 필터 패널 추가
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new GridLayout(2, 4));

        filterPanel.add(new JLabel("고객 ID:"));
        customerIdField = new JTextField();
        filterPanel.add(customerIdField);

        filterPanel.add(new JLabel("직원 ID:"));
        employeeIdField = new JTextField();
        filterPanel.add(employeeIdField);

        filterPanel.add(new JLabel("시작 날짜 (YYYY-MM-DD):"));
        startDateField = new JTextField();
        filterPanel.add(startDateField);

        filterPanel.add(new JLabel("종료 날짜 (YYYY-MM-DD):"));
        endDateField = new JTextField();
        filterPanel.add(endDateField);

        searchButton = new JButton("검색");
        resetButton = new JButton("초기화");
        deleteButton = new JButton("선택 판매 취소"); // 삭제 버튼 추가
        detailButton = new JButton("판매 상세 보기"); // 상세 보기 버튼 추가

        filterPanel.add(searchButton);
        filterPanel.add(resetButton);

        // 3️⃣ 삭제 버튼 패널 추가
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(detailButton);
        buttonPanel.add(deleteButton);

        // 3️⃣ 버튼 이벤트 리스너 추가
        searchButton.addActionListener(e -> searchSales());
        resetButton.addActionListener(e -> resetFilters());
        deleteButton.addActionListener(e -> deleteSale()); // 삭제 기능 추가
        detailButton.addActionListener(e -> showSaleDetails()); // 상세 보기 버튼 이벤트


        // 4️⃣ UI 배치
        add(filterPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // 5️⃣ 초기 데이터 로드
        loadSalesData();

        setVisible(true);
    }

    // 전체 판매 내역을 테이블에 로드하는 메서드
    private void loadSalesData() {
        tableModel.setRowCount(0); // 기존 데이터 삭제
        List<Sales> salesList = salesDao.listSales();

        for (Sales sale : salesList) {
            tableModel.addRow(new Object[]{
                    sale.getSaleId(),
                    sale.getSaleDate(),
                    sale.getTotalPrice(),
                    sale.getCustomerId(),
                    sale.getPhoneId(),
                    sale.getPlanId(),
                    sale.getEmployeeId(),
                    sale.getActivationType(),
                    sale.getSimOption(),
                    sale.getInstallmentMonths()
            });
        }
    }

    // 필터 적용하여 판매 내역 검색
    private void searchSales() {
        Integer customerId = customerIdField.getText().isEmpty() ? null : Integer.parseInt(customerIdField.getText());
        Integer employeeId = employeeIdField.getText().isEmpty() ? null : Integer.parseInt(employeeIdField.getText());
        Date startDate = startDateField.getText().isEmpty() ? null : Date.valueOf(startDateField.getText());
        Date endDate = endDateField.getText().isEmpty() ? null : Date.valueOf(endDateField.getText());

        tableModel.setRowCount(0);
        List<Sales> salesList = salesDao.searchSales(customerId, employeeId, startDate, endDate);

        for (Sales sale : salesList) {
            tableModel.addRow(new Object[]{
                    sale.getSaleId(),
                    sale.getSaleDate(),
                    sale.getTotalPrice(),
                    sale.getCustomerId(),
                    sale.getPhoneId(),
                    sale.getPlanId(),
                    sale.getEmployeeId(),
                    sale.getActivationType(),
                    sale.getSimOption(),
                    sale.getInstallmentMonths()
            });
        }
    }
    // 선택한 판매 내역 삭제
    private void deleteSale() {
        int selectedRow = salesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "삭제할 판매 내역을 선택하세요.");
            return;
        }

        int saleId = (int) tableModel.getValueAt(selectedRow, 0); // 선택한 행의 판매 ID

        int confirm = JOptionPane.showConfirmDialog(this, "정말로 판매를 취소하시겠습니까?", "판매 취소 확인", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int result = salesDao.deleteSale(saleId);
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "✅ 판매 취소 성공!");
                loadSalesData(); // 테이블 갱신
            } else {
                JOptionPane.showMessageDialog(this, "❌ 판매 취소 실패!");
            }
        }
    }

    private void showSaleDetails() {
        int selectedRow = salesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "상세 정보를 볼 판매 내역을 선택하세요.");
            return;
        }

        int saleId = (int) tableModel.getValueAt(selectedRow, 0); // 선택한 행의 판매 ID
        new SalesDetailDialog(this, saleId); // 상세 정보 창 열기
    }

    // 필터 초기화
    private void resetFilters() {
        customerIdField.setText("");
        employeeIdField.setText("");
        startDateField.setText("");
        endDateField.setText("");
        loadSalesData();
    }

    public static void main(String[] args) {
        new SalesHistoryForm();
    }
}