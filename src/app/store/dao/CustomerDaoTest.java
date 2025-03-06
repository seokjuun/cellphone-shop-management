package app.store.dao;

import app.store.dto.Customer;

import java.sql.Date;
import java.util.List;

public class CustomerDaoTest {
    public static void main(String[] args) {
        CustomerDao dao = new CustomerDao();

        // 고객 추가 테스트
//        Customer newCustomer = new Customer(0, "홍길동", "010-1234-5678", "VIP", "서울시 강남구", Date.valueOf("1990-05-20"));
//        dao.insertCustomer(newCustomer);
//        newCustomer = new Customer(1, "홍석준", "010-1234-5679", "일반 고객", "인천시 서구", Date.valueOf("1999-10-20"));
//        dao.insertCustomer(newCustomer);

        // 전체 고객 목록 조회
        List<Customer> customers = dao.listCustomers();
        for (Customer c : customers) {
            System.out.println(c);
        }
    }
}