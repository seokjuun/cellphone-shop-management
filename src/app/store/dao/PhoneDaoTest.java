package app.store.dao;

import app.store.dto.Phone;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class PhoneDaoTest {
    public static void main(String[] args) {
        PhoneDao dao = new PhoneDao();

        // 테스트용 휴대폰 추가
        Phone newPhone = new Phone(0, "갤럭시 S30 256GB", "삼성", new BigDecimal(1200000), Date.valueOf("2024-02-01"), 10);
        dao.insertPhone(newPhone);

        // 휴대폰 목록 조회
        List<Phone> phones = dao.listPhones();
        for (Phone p : phones) {
            System.out.println(p);
        }
    }
}