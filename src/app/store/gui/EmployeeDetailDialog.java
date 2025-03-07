package app.store.gui;

import app.store.dao.EmployeeDao;
import app.store.dto.Employee;

import javax.swing.*;
import java.awt.*;

public class EmployeeDetailDialog extends JDialog {
    private JTextField nameField;
    private JComboBox<String> positionComboBox;
    private JButton saveButton;
    private EmployeeDao employeeDao = new EmployeeDao();

    public EmployeeDetailDialog(JFrame parent, Employee employee) {
        super(parent, (employee == null) ? "새 직원 추가" : "직원 정보 수정", true);
        setSize(300, 200);
        setLayout(new GridLayout(3, 2));

        add(new JLabel("이름:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("직급:"));
        positionComboBox = new JComboBox<>(new String[]{"매니저", "직원"});
        add(positionComboBox);

        saveButton = new JButton("저장");
        add(saveButton);

        // 기존 직원 정보 로드
        if (employee != null) {
            nameField.setText(employee.getName());
            positionComboBox.setSelectedItem(employee.getPosition());
        }

        // 저장 버튼 이벤트
        saveButton.addActionListener(e -> saveEmployee(employee));

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void saveEmployee(Employee employee) {
        String name = nameField.getText();
        String position = (String) positionComboBox.getSelectedItem();

        if (employee == null) {
            // 신규 직원 추가
            employee = new Employee(0, name, position);
            employeeDao.insertEmployee(employee);
        } else {
            // 기존 직원 수정
            employee.setName(name);
            employee.setPosition(position);
            employeeDao.updateEmployee(employee);
        }

        JOptionPane.showMessageDialog(this, "✅ 저장 완료!");
        dispose();
    }
}