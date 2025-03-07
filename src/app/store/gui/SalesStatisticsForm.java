package app.store.gui;

import app.store.dao.SalesDao;
import java.math.BigDecimal;
import java.util.Map;
import javax.swing.*;
import java.awt.*;

public class SalesStatisticsForm extends JFrame {
    private JComboBox<String> yearComboBox;
    private JComboBox<String> monthComboBox;
    private JButton generateButton;
    private JTextArea statisticsTextArea;
    private JLabel totalSalesLabel, totalAmountLabel;
    private SalesDao salesDao = new SalesDao();

    public SalesStatisticsForm() {
        setTitle("매출 통계 조회");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1️⃣ 상단 필터 패널
        JPanel filterPanel = new JPanel();
        filterPanel.add(new JLabel("연도:"));
        yearComboBox = new JComboBox<>(new String[]{"2023", "2024", "2025"});
        filterPanel.add(yearComboBox);

        filterPanel.add(new JLabel("월:"));
        monthComboBox = new JComboBox<>(new String[]{"전체", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"});
        filterPanel.add(monthComboBox);

        generateButton = new JButton("통계 조회");
        filterPanel.add(generateButton);

        // 2️⃣ 매출 요약 패널
        JPanel summaryPanel = new JPanel();
        totalSalesLabel = new JLabel("총 판매 횟수: 0");
        totalAmountLabel = new JLabel("총 매출 금액: 0원");
        summaryPanel.add(totalSalesLabel);
        summaryPanel.add(totalAmountLabel);

        // 3️⃣ 텍스트 출력 패널
        statisticsTextArea = new JTextArea();
        statisticsTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(statisticsTextArea);

        // 4️⃣ UI 배치
        add(filterPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(summaryPanel, BorderLayout.SOUTH);

        // 5️⃣ 버튼 이벤트 추가
        generateButton.addActionListener(e -> generateStatistics());

        setVisible(true);
    }

    // 매출 통계 생성
    private void generateStatistics() {
        String selectedYear = (String) yearComboBox.getSelectedItem();
        String selectedMonth = (String) monthComboBox.getSelectedItem();
        boolean isYearly = selectedMonth.equals("전체");

        // 1️⃣ DB에서 매출 데이터 조회
        Map<String, BigDecimal> salesData;
        if (isYearly) {
            salesData = salesDao.getYearlySales(selectedYear);
        } else {
            salesData = salesDao.getMonthlySales(selectedYear, selectedMonth);
        }

        // 2️⃣ 데이터 가공 및 출력
        StringBuilder statsText = new StringBuilder();
        BigDecimal totalAmount = BigDecimal.ZERO;
        int totalSales = 0;

        for (Map.Entry<String, BigDecimal> entry : salesData.entrySet()) {
            statsText.append(entry.getKey()).append(": ").append(entry.getValue()).append("원\n");
            totalAmount = totalAmount.add(entry.getValue());
            totalSales++;
        }

        totalSalesLabel.setText("총 판매 횟수: " + totalSales);
        totalAmountLabel.setText("총 매출 금액: " + totalAmount + "원");

        statisticsTextArea.setText(statsText.toString());
    }

    public static void main(String[] args) {
        new SalesStatisticsForm();
    }
}