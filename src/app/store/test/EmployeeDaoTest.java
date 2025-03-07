package app.store.test;

import app.store.dao.EmployeeDao;
import app.store.dto.Employee;
import java.util.List;

public class EmployeeDaoTest {
    public static void main(String[] args) {
        EmployeeDao dao = new EmployeeDao();

        // 직원 추가 테스트
        Employee newEmployee = new Employee(0, "권지용", "직원");
        int insertResult = dao.insertEmployee(newEmployee);
        System.out.println(insertResult > 0 ? "직원 등록 성공" : "직원 등록 실패");

        // 전체 직원 목록 조회
        List<Employee> employees = dao.listEmployees();
        System.out.println("현재 직원 목록:");
        for (Employee employee : employees) {
            System.out.println(employee);
        }
    }
}