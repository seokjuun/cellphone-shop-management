package app.store.gui;

import app.store.dao.SalesDao;
import app.store.dto.Sales;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustomerPurchaseHistoryDialog extends JDialog {
    private JTable salesTable;
    private DefaultTableModel tableModel;
    private SalesDao salesDao = new SalesDao();

    public CustomerPurchaseHistoryDialog(JFrame parent, int customerId) {
        super(parent, "고객 구매 이력 조회", true);
        setSize(800, 400);
        setLayout(new BorderLayout());

        // 1️⃣ 테이블 설정
        String[] columnNames = {"판매 ID", "휴대폰 ID", "개통 유형", "구매 날짜", "총 가격"};
        tableModel = new DefaultTableModel(columnNames, 0);
        salesTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(salesTable);

        // 2️⃣ UI 배치
        add(scrollPane, BorderLayout.CENTER);

        // 3️⃣ 데이터 로드
        loadPurchaseHistory(customerId);

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    // 특정 고객의 구매 이력 조회
    private void loadPurchaseHistory(int customerId) {
        tableModel.setRowCount(0); // 기존 데이터 삭제
        List<Sales> salesList = salesDao.listSalesByCustomer(customerId);

        for (Sales sale : salesList) {
            tableModel.addRow(new Object[]{
                    sale.getSaleId(),
                    sale.getPhoneId(),
                    sale.getActivationType(),
                    sale.getSaleDate(),
                    sale.getTotalPrice()
            });
        }
    }
}