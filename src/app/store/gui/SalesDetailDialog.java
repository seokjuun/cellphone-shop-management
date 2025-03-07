package app.store.gui;

import app.store.dao.DiscountHistoryDao;
import app.store.dao.SalesDao;
import app.store.dto.Sales;
import app.store.dto.DiscountHistory;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SalesDetailDialog extends JDialog {
    private SalesDao salesDao = new SalesDao();
    private DiscountHistoryDao discountHistoryDao = new DiscountHistoryDao();

    public SalesDetailDialog(JFrame parent, int saleId) {
        super(parent, "판매 상세 정보", true);
        setSize(400, 400);
        setLayout(new GridLayout(10, 1));

        // 1️⃣ 선택한 판매 내역 가져오기
        Sales sale = salesDao.getSaleById(saleId);
        if (sale == null) {
            JOptionPane.showMessageDialog(this, "판매 정보를 찾을 수 없습니다.");
            dispose();
            return;
        }

        // 2️⃣ 할인 내역 가져오기
        List<DiscountHistory> discountHistories = discountHistoryDao.getDiscountsBySaleId(saleId);
        StringBuilder discountText = new StringBuilder();
        for (DiscountHistory dh : discountHistories) {
            discountText.append("할인 ID: ").append(dh.getDiscountId()).append("\n");
        }

        // 3️⃣ 상세 정보 라벨 추가
        add(new JLabel("판매 ID: " + sale.getSaleId()));
        add(new JLabel("판매 날짜: " + sale.getSaleDate()));
        add(new JLabel("총 가격: " + sale.getTotalPrice()));
        add(new JLabel("고객 ID: " + sale.getCustomerId()));
        add(new JLabel("휴대폰 ID: " + sale.getPhoneId()));
        add(new JLabel("요금제 ID: " + sale.getPlanId()));
        add(new JLabel("직원 ID: " + sale.getEmployeeId()));
        add(new JLabel("개통 유형: " + sale.getActivationType()));
        add(new JLabel("유심 옵션: " + sale.getSimOption()));
        add(new JLabel("할부 개월: " + sale.getInstallmentMonths()));
        add(new JLabel("적용된 할인: " + (discountText.length() > 0 ? discountText.toString() : "없음")));

        // 4️⃣ 닫기 버튼 추가
        JButton closeButton = new JButton("닫기");
        closeButton.addActionListener(e -> dispose());
        add(closeButton);

        setLocationRelativeTo(parent);
        setVisible(true);
    }
}