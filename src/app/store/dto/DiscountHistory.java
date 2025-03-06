package app.store.dto;

public class DiscountHistory {
    private int discountHistoryId;
    private int saleId;
    private int discountId;

    public DiscountHistory() {}

    public DiscountHistory(int discountHistoryId, int saleId, int discountId) {
        this.discountHistoryId = discountHistoryId;
        this.saleId = saleId;
        this.discountId = discountId;
    }

    public int getDiscountHistoryId() { return discountHistoryId; }
    public void setDiscountHistoryId(int discountHistoryId) { this.discountHistoryId = discountHistoryId; }

    public int getSaleId() { return saleId; }
    public void setSaleId(int saleId) { this.saleId = saleId; }

    public int getDiscountId() { return discountId; }
    public void setDiscountId(int discountId) { this.discountId = discountId; }

    @Override
    public String toString() {
        return "DiscountHistory{" +
                "discountHistoryId=" + discountHistoryId +
                ", saleId=" + saleId +
                ", discountId=" + discountId +
                '}';
    }
}