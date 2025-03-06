package app.store.dto;

import java.math.BigDecimal;

public class Plan {
    private int planId;
    private String planName;
    private BigDecimal monthlyFee;
    private String dataLimit;
    private String callLimit;
    private String textLimit;

    public Plan() {}

    public Plan(int planId, String planName, BigDecimal monthlyFee, String dataLimit, String callLimit, String textLimit) {
        this.planId = planId;
        this.planName = planName;
        this.monthlyFee = monthlyFee;
        this.dataLimit = dataLimit;
        this.callLimit = callLimit;
        this.textLimit = textLimit;
    }

    public int getPlanId() { return planId; }
    public void setPlanId(int planId) { this.planId = planId; }

    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }

    public BigDecimal getMonthlyFee() { return monthlyFee; }
    public void setMonthlyFee(BigDecimal monthlyFee) { this.monthlyFee = monthlyFee; }

    public String getDataLimit() { return dataLimit; }
    public void setDataLimit(String dataLimit) { this.dataLimit = dataLimit; }

    public String getCallLimit() { return callLimit; }
    public void setCallLimit(String callLimit) { this.callLimit = callLimit; }

    public String getTextLimit() { return textLimit; }
    public void setTextLimit(String textLimit) { this.textLimit = textLimit; }

    @Override
    public String toString() {
        return "Plan{" +
                "planId=" + planId +
                ", planName='" + planName + '\'' +
                ", monthlyFee=" + monthlyFee +
                ", dataLimit='" + dataLimit + '\'' +
                ", callLimit='" + callLimit + '\'' +
                ", textLimit='" + textLimit + '\'' +
                '}';
    }
}