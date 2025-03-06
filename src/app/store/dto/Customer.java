package app.store.dto;
import java.sql.Date;

public class Customer {
    private int customerId;
    private String name;
    private String phoneNumber;
    private String customerGrade;
    private String address;
    private Date birth;

    public Customer() {}

    public Customer(int customerId, String name, String phoneNumber, String customerGrade, String address, Date birth) {
        this.customerId = customerId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.customerGrade = customerGrade;
        this.address = address;
        this.birth = birth;
    }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getCustomerGrade() { return customerGrade; }
    public void setCustomerGrade(String customerGrade) { this.customerGrade = customerGrade; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Date getBirth() { return birth; }
    public void setBirth(Date birth) { this.birth = birth; }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", customerGrade='" + customerGrade + '\'' +
                ", address='" + address + '\'' +
                ", birth=" + birth +
                '}';
    }
}