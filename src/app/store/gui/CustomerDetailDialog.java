package app.store.gui;

import app.store.dao.CustomerDao;
import app.store.dto.Customer;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;

public class CustomerDetailDialog extends JDialog {
    private JTextField nameField, phoneField, addressField, birthField;
    private JComboBox<String> gradeComboBox;
    private JButton saveButton;
    private CustomerDao customerDao = new CustomerDao();

    public CustomerDetailDialog(JFrame parent, Customer customer) {
        super(parent, (customer == null) ? "새 고객 추가" : "고객 정보 수정", true);
        setSize(300, 300);
        setLayout(new GridLayout(6, 2));

        add(new JLabel("이름:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("전화번호:"));
        phoneField = new JTextField();
        add(phoneField);

        add(new JLabel("고객 등급:"));
        gradeComboBox = new JComboBox<>(new String[]{"일반 고객", "VIP"});
        add(gradeComboBox);

        add(new JLabel("주소:"));
        addressField = new JTextField();
        add(addressField);

        add(new JLabel("생년월일 (YYYY-MM-DD):"));
        birthField = new JTextField();
        add(birthField);

        saveButton = new JButton("저장");
        add(saveButton);

        // 기존 고객 정보 로드
        if (customer != null) {
            nameField.setText(customer.getName());
            phoneField.setText(customer.getPhoneNumber());
            gradeComboBox.setSelectedItem(customer.getCustomerGrade());
            addressField.setText(customer.getAddress());
            birthField.setText(customer.getBirth().toString());
        }

        // 저장 버튼 이벤트
        saveButton.addActionListener(e -> saveCustomer(customer));

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void saveCustomer(Customer customer) {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String grade = (String) gradeComboBox.getSelectedItem();
        String address = addressField.getText();
        Date birth = Date.valueOf(birthField.getText());

        if (customer == null) {
            // 신규 고객 추가
            customer = new Customer(0, name, phone, grade, address, birth);
            customerDao.insertCustomer(customer);
        } else {
            // 기존 고객 수정
            customer.setName(name);
            customer.setPhoneNumber(phone);
            customer.setCustomerGrade(grade);
            customer.setAddress(address);
            customer.setBirth(birth);
            customerDao.updateCustomer(customer);
        }

        JOptionPane.showMessageDialog(this, "✅ 저장 완료!");
        dispose();
    }
}