package app.store.dto;

import java.math.BigDecimal;
import java.sql.Date;

public class Phone {
    private int phoneId;
    private String modelName;
    private String brand;
    private BigDecimal factoryPrice;
    private Date releaseDate;
    private int stock;

    public Phone() {}

    public Phone(int phoneId, String modelName, String brand, BigDecimal factoryPrice, Date releaseDate, int stock) {
        this.phoneId = phoneId;
        this.modelName = modelName;
        this.brand = brand;
        this.factoryPrice = factoryPrice;
        this.releaseDate = releaseDate;
        this.stock = stock;
    }

    public int getPhoneId() { return phoneId; }
    public void setPhoneId(int phoneId) { this.phoneId = phoneId; }
    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public BigDecimal getFactoryPrice() { return factoryPrice; }
    public void setFactoryPrice(BigDecimal factoryPrice) { this.factoryPrice = factoryPrice; }
    public Date getReleaseDate() { return releaseDate; }
    public void setReleaseDate(Date releaseDate) { this.releaseDate = releaseDate; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    @Override
    public String toString() {
        return "Phone{" +
                "phoneId=" + phoneId +
                ", modelName='" + modelName + '\'' +
                ", brand='" + brand + '\'' +
                ", factoryPrice=" + factoryPrice +
                ", releaseDate=" + releaseDate +
                ", stock=" + stock +
                '}';
    }
}