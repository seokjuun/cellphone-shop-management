package app.store.gui;

import app.store.dao.DiscountHistoryDao;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DiscountHistoryForm extends JFrame {
    private JTextField customerIdField;
    private JButton searchButton;
    private JTextArea discountTextArea;
    private DiscountHistoryDao discountHistoryDao = new DiscountHistoryDao();

    public DiscountHistoryForm() {
        setTitle("할인 적용 이력 조회");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 검색 패널
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("고객 ID:"));
        customerIdField = new JTextField(10);
        searchButton = new JButton("조회");
        searchPanel.add(customerIdField);
        searchPanel.add(searchButton);

        // 결과 출력 패널
        discountTextArea = new JTextArea();
        discountTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(discountTextArea);

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // 버튼 이벤트 추가
        searchButton.addActionListener(e -> searchDiscounts());

        setVisible(true);
    }

    // 할인 이력 조회
    private void searchDiscounts() {
        int customerId;
        try {
            customerId = Integer.parseInt(customerIdField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "올바른 고객 ID를 입력하세요.");
            return;
        }

        List<String> discountList = discountHistoryDao.getCustomerDiscounts(customerId);

        StringBuilder resultText = new StringBuilder();
        if (discountList.isEmpty()) {
            resultText.append("할인 적용 이력이 없습니다.");
        } else {
            for (String discount : discountList) {
                resultText.append(discount).append("\n");
            }
        }

        discountTextArea.setText(resultText.toString());
    }

    public static void main(String[] args) {
        new DiscountHistoryForm();
    }
}