package app.store.gui;

import app.store.dao.EmployeeDao;
import app.store.dto.Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EmployeeManagementForm extends JFrame {
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private EmployeeDao employeeDao = new EmployeeDao();

    private JTextField searchField;
    private JButton searchButton, addButton, updateButton, deleteButton, viewSalesButton;

    public EmployeeManagementForm() {
        setTitle("직원 관리");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1️⃣ 테이블 설정
        String[] columnNames = {"직원 ID", "이름", "직급"};
        tableModel = new DefaultTableModel(columnNames, 0);
        employeeTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(employeeTable);

        // 2️⃣ 검색 패널
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("직원 검색 (ID, 이름):"));
        searchField = new JTextField(15);
        searchButton = new JButton("검색");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // 3️⃣ 버튼 패널 (등록, 수정, 삭제)
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("새 직원 추가");
        updateButton = new JButton("직원 정보 수정");
        deleteButton = new JButton("직원 삭제");
        viewSalesButton = new JButton("판매 실적 보기");
        buttonPanel.add(viewSalesButton);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        // 4️⃣ UI 배치
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // 5️⃣ 버튼 이벤트 리스너 추가
        searchButton.addActionListener(e -> searchEmployee());
        addButton.addActionListener(e -> new EmployeeDetailDialog(this, null));
        updateButton.addActionListener(e -> updateEmployee());
        deleteButton.addActionListener(e -> deleteEmployee());
        viewSalesButton.addActionListener(e -> viewEmployeeSales());

        // 6️⃣ 초기 데이터 로드
        loadEmployeeData();

        setVisible(true);
    }

    // 전체 직원 목록 로드
    private void loadEmployeeData() {
        tableModel.setRowCount(0); // 기존 데이터 삭제
        List<Employee> employeeList = employeeDao.listEmployees();

        for (Employee employee : employeeList) {
            tableModel.addRow(new Object[]{
                    employee.getEmployeeId(),
                    employee.getName(),
                    employee.getPosition()
            });
        }
    }

    // 직원 검색
    private void searchEmployee() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadEmployeeData();
            return;
        }

        tableModel.setRowCount(0);
        List<Employee> employeeList = employeeDao.searchEmployees(keyword);

        for (Employee employee : employeeList) {
            tableModel.addRow(new Object[]{
                    employee.getEmployeeId(),
                    employee.getName(),
                    employee.getPosition()
            });
        }
    }

    // 직원 정보 수정
    private void updateEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "수정할 직원을 선택하세요.");
            return;
        }

        int employeeId = (int) tableModel.getValueAt(selectedRow, 0);
        new EmployeeDetailDialog(this, employeeDao.getEmployeeById(employeeId));
    }

    // 직원 삭제
    private void deleteEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "삭제할 직원을 선택하세요.");
            return;
        }

        int employeeId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "정말로 직원을 삭제하시겠습니까?", "직원 삭제 확인", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int result = employeeDao.deleteEmployee(employeeId);
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "✅ 직원 삭제 성공!");
                loadEmployeeData();
            } else {
                JOptionPane.showMessageDialog(this, "❌ 직원 삭제 실패!");
            }
        }
    }

    // 선택한 직원의 판매 실적 보기
    private void viewEmployeeSales() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "판매 실적을 볼 직원을 선택하세요.");
            return;
        }

        int employeeId = (int) tableModel.getValueAt(selectedRow, 0); // 선택한 행의 직원 ID
        new EmployeeSalesHistoryDialog(this, employeeId);
    }

    public static void main(String[] args) {
        new EmployeeManagementForm();
    }
}