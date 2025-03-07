package app.store.gui;

import app.store.dao.InstallmentDao;
import java.math.BigDecimal;
import java.util.Map;
import javax.swing.*;
import java.awt.*;

public class InstallmentManagementForm extends JFrame {
    private JTextField customerIdField;
    private JButton searchButton;
    private JTextArea installmentTextArea;
    private InstallmentDao installmentDao = new InstallmentDao();

    public InstallmentManagementForm() {
        setTitle("할부 계약 정보 관리");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 상단 검색 패널
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("고객 ID:"));
        customerIdField = new JTextField(10);
        searchButton = new JButton("조회");
        searchPanel.add(customerIdField);
        searchPanel.add(searchButton);

        // 결과 출력 패널
        installmentTextArea = new JTextArea();
        installmentTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(installmentTextArea);

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // 버튼 이벤트 추가
        searchButton.addActionListener(e -> searchInstallments());

        setVisible(true);
    }

    // 할부 계약 조회
    private void searchInstallments() {
        int customerId;
        try {
            customerId = Integer.parseInt(customerIdField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "올바른 고객 ID를 입력하세요.");
            return;
        }

        Map<Integer, BigDecimal> installmentData = installmentDao.getRemainingInstallments(customerId);

        StringBuilder resultText = new StringBuilder();
        if (installmentData.isEmpty()) {
            resultText.append("남은 할부 계약이 없습니다.");
        } else {
            for (Map.Entry<Integer, BigDecimal> entry : installmentData.entrySet()) {
                resultText.append("판매 ID: ").append(entry.getKey())
                        .append(" | 남은 할부 금액: ").append(entry.getValue()).append("원\n");
            }
        }

        installmentTextArea.setText(resultText.toString());
    }

    public static void main(String[] args) {
        new InstallmentManagementForm();
    }
}