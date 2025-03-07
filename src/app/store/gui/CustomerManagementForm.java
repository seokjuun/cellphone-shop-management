package app.store.gui;

import app.store.dao.CustomerDao;
import app.store.dto.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CustomerManagementForm extends JFrame {
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private CustomerDao customerDao = new CustomerDao();

    private JTextField searchField;
    private JButton searchButton, addButton, updateButton, deleteButton;
    private JButton viewHistoryButton; // 구매 이력 버튼 추가
    public CustomerManagementForm() {
        setTitle("고객 관리");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1️⃣ 테이블 설정
        String[] columnNames = {"고객 ID", "이름", "전화번호", "고객 등급", "주소", "생년월일"};
        tableModel = new DefaultTableModel(columnNames, 0);
        customerTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(customerTable);

        // 2️⃣ 검색 패널
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("고객 검색 (ID, 이름, 전화번호):"));
        searchField = new JTextField(15);
        searchButton = new JButton("검색");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // 3️⃣ 버튼 패널 (등록, 수정, 삭제)
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("새 고객 추가");
        updateButton = new JButton("고객 정보 수정");
        deleteButton = new JButton("고객 삭제");
        viewHistoryButton = new JButton("구매 이력 보기");
        buttonPanel.add(viewHistoryButton);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        // 4️⃣ UI 배치
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // 5️⃣ 버튼 이벤트 리스너 추가
        searchButton.addActionListener(e -> searchCustomer());
        addButton.addActionListener(e -> new CustomerDetailDialog(this, null));
        updateButton.addActionListener(e -> updateCustomer());
        deleteButton.addActionListener(e -> deleteCustomer());
        viewHistoryButton.addActionListener(e -> viewCustomerHistory());

        // 6️⃣ 초기 데이터 로드
        loadCustomerData();

        setVisible(true);
    }

    // 전체 고객 목록 로드
    private void loadCustomerData() {
        tableModel.setRowCount(0); // 기존 데이터 삭제
        List<Customer> customerList = customerDao.listCustomers();

        for (Customer customer : customerList) {
            tableModel.addRow(new Object[]{
                    customer.getCustomerId(),
                    customer.getName(),
                    customer.getPhoneNumber(),
                    customer.getCustomerGrade(),
                    customer.getAddress(),
                    customer.getBirth()
            });
        }
    }

    // 고객 검색
    private void searchCustomer() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadCustomerData();
            return;
        }

        tableModel.setRowCount(0);
        List<Customer> customerList = customerDao.searchCustomers(keyword);

        for (Customer customer : customerList) {
            tableModel.addRow(new Object[]{
                    customer.getCustomerId(),
                    customer.getName(),
                    customer.getPhoneNumber(),
                    customer.getCustomerGrade(),
                    customer.getAddress(),
                    customer.getBirth()
            });
        }
    }

    // 고객 정보 수정
    private void updateCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "수정할 고객을 선택하세요.");
            return;
        }

        int customerId = (int) tableModel.getValueAt(selectedRow, 0);
        new CustomerDetailDialog(this, customerDao.getCustomerById(customerId));
    }

    // 고객 삭제
    private void deleteCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "삭제할 고객을 선택하세요.");
            return;
        }

        int customerId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "정말로 고객을 삭제하시겠습니까?", "고객 삭제 확인", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int result = customerDao.deleteCustomer(customerId);
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "✅ 고객 삭제 성공!");
                loadCustomerData();
            } else {
                JOptionPane.showMessageDialog(this, "❌ 고객 삭제 실패!");
            }
        }
    }

    // 선택한 고객의 구매 이력 보기
    private void viewCustomerHistory() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "구매 이력을 볼 고객을 선택하세요.");
            return;
        }

        int customerId = (int) tableModel.getValueAt(selectedRow, 0); // 선택한 행의 고객 ID
        new CustomerPurchaseHistoryDialog(this, customerId);
    }

    public static void main(String[] args) {
        new CustomerManagementForm();
    }
}