package app.store.gui;

import app.store.dao.StockDao;
import javax.swing.*;
import java.awt.*;

public class StockManagementForm extends JFrame {
    private JTextField phoneIdField, quantityField;
    private JButton checkStockButton, updateStockButton;
    private JLabel stockLabel;
    private StockDao stockDao = new StockDao();

    public StockManagementForm() {
        setTitle("재고 관리");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(0,4, 5,5));

        add(new JLabel("휴대폰 ID:"));
        phoneIdField = new JTextField();
        add(phoneIdField);

        checkStockButton = new JButton("재고 확인");
        add(checkStockButton);
        stockLabel = new JLabel("재고: -");
        add(stockLabel);

        add(new JLabel("변경 수량 (+추가, -출고):"));
        quantityField = new JTextField();
        add(quantityField);

        updateStockButton = new JButton("재고 업데이트");
        add(updateStockButton);

        checkStockButton.addActionListener(e -> checkStock());
        updateStockButton.addActionListener(e -> updateStock());

        setVisible(true);
    }

    private void checkStock() {
        int phoneId = Integer.parseInt(phoneIdField.getText().trim());
        int stock = stockDao.getStock(phoneId);
        stockLabel.setText("재고: " + stock);
    }

    private void updateStock() {
        int phoneId = Integer.parseInt(phoneIdField.getText().trim());
        int quantity = Integer.parseInt(quantityField.getText().trim());
        stockDao.updateStock(phoneId, quantity);
        checkStock();
    }

    public static void main(String[] args) {
        new StockManagementForm();
    }
}