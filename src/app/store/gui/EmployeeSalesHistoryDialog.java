package app.store.gui;

import app.store.dao.SalesDao;
import app.store.dto.Sales;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class EmployeeSalesHistoryDialog extends JDialog {
    private JTable salesTable;
    private DefaultTableModel tableModel;
    private SalesDao salesDao = new SalesDao();
    private JLabel totalSalesLabel, totalAmountLabel;

    public EmployeeSalesHistoryDialog(JFrame parent, int employeeId) {
        super(parent, "직원 판매 실적 조회", true);
        setSize(800, 400);
        setLayout(new BorderLayout());

        // 1️⃣ 테이블 설정
        String[] columnNames = {"판매 ID", "휴대폰 ID", "개통 유형", "판매 날짜", "총 가격"};
        tableModel = new DefaultTableModel(columnNames, 0);
        salesTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(salesTable);

        // 2️⃣ 실적 정보 패널
        JPanel summaryPanel = new JPanel();
        totalSalesLabel = new JLabel("총 판매 횟수: 0");
        totalAmountLabel = new JLabel("총 판매 금액: 0원");
        summaryPanel.add(totalSalesLabel);
        summaryPanel.add(totalAmountLabel);

        // 3️⃣ UI 배치
        add(scrollPane, BorderLayout.CENTER);
        add(summaryPanel, BorderLayout.SOUTH);

        // 4️⃣ 데이터 로드
        loadEmployeeSalesHistory(employeeId);

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    // 직원별 판매 실적 조회
    private void loadEmployeeSalesHistory(int employeeId) {
        tableModel.setRowCount(0); // 기존 데이터 삭제
        List<Sales> salesList = salesDao.listSalesByEmployee(employeeId);

        BigDecimal totalAmount = BigDecimal.ZERO;
        int totalSales = 0;

        for (Sales sale : salesList) {
            tableModel.addRow(new Object[]{
                    sale.getSaleId(),
                    sale.getPhoneId(),
                    sale.getActivationType(),
                    sale.getSaleDate(),
                    sale.getTotalPrice()
            });
            totalAmount = totalAmount.add(sale.getTotalPrice());
            totalSales++;
        }

        totalSalesLabel.setText("총 판매 횟수: " + totalSales);
        totalAmountLabel.setText("총 판매 금액: " + totalAmount + "원");
    }
}