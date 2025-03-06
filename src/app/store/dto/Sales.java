package app.store.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Sales {
    private int saleId;
    private Timestamp saleDate;
    private BigDecimal totalPrice;
    private int customerId;
    private int phoneId;
    private int planId;
    private int employeeId;
    private String activationType;
    private String simOption;
    private int installmentMonths;

    public Sales() {}

    public Sales(int saleId, Timestamp saleDate, BigDecimal totalPrice, int customerId, int phoneId, int planId, int employeeId, String activationType, String simOption, int installmentMonths) {
        this.saleId = saleId;
        this.saleDate = saleDate;
        this.totalPrice = totalPrice;
        this.customerId = customerId;
        this.phoneId = phoneId;
        this.planId = planId;
        this.employeeId = employeeId;
        this.activationType = activationType;
        this.simOption = simOption;
        this.installmentMonths = installmentMonths;
    }

    public int getSaleId() { return saleId; }
    public void setSaleId(int saleId) { this.saleId = saleId; }

    public Timestamp getSaleDate() { return saleDate; }
    public void setSaleDate(Timestamp saleDate) { this.saleDate = saleDate; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getPhoneId() { return phoneId; }
    public void setPhoneId(int phoneId) { this.phoneId = phoneId; }

    public int getPlanId() { return planId; }
    public void setPlanId(int planId) { this.planId = planId; }

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public String getActivationType() { return activationType; }
    public void setActivationType(String activationType) { this.activationType = activationType; }

    public String getSimOption() { return simOption; }
    public void setSimOption(String simOption) { this.simOption = simOption; }

    public int getInstallmentMonths() { return installmentMonths; }
    public void setInstallmentMonths(int installmentMonths) { this.installmentMonths = installmentMonths; }

    @Override
    public String toString() {
        return "Sales{" +
                "saleId=" + saleId +
                ", saleDate=" + saleDate +
                ", totalPrice=" + totalPrice +
                ", customerId=" + customerId +
                ", phoneId=" + phoneId +
                ", planId=" + planId +
                ", employeeId=" + employeeId +
                ", activationType='" + activationType + '\'' +
                ", simOption='" + simOption + '\'' +
                ", installmentMonths=" + installmentMonths +
                '}';
    }
}