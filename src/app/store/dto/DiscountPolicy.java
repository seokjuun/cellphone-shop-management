package app.store.dto;

import java.math.BigDecimal;
import java.sql.Date;

public class DiscountPolicy {
    private int discountId;
    private int phoneId;
    private String activationType;
    private BigDecimal discountAmount;
    private Date startDate;
    private Date endDate;

    public DiscountPolicy() {}

    public DiscountPolicy(int discountId, int phoneId, String activationType, BigDecimal discountAmount, Date startDate, Date endDate) {
        this.discountId = discountId;
        this.phoneId = phoneId;
        this.activationType = activationType;
        this.discountAmount = discountAmount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getDiscountId() { return discountId; }
    public void setDiscountId(int discountId) { this.discountId = discountId; }

    public int getPhoneId() { return phoneId; }
    public void setPhoneId(int phoneId) { this.phoneId = phoneId; }

    public String getActivationType() { return activationType; }
    public void setActivationType(String activationType) { this.activationType = activationType; }

    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    @Override
    public String toString() {
        return "DiscountPolicy{" +
                "discountId=" + discountId +
                ", phoneId=" + phoneId +
                ", activationType='" + activationType + '\'' +
                ", discountAmount=" + discountAmount +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}