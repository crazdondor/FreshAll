package com.freshall.freshall.Model;

import java.util.Date;

public class Item {
    String itemName;
    Date harvestDate;
    Date expirationDate;
    Integer quantityInStock;
    Integer minimumQuantity;
    Integer pricePerQuantity;
    String units;

    public Item(String itemName, Date harvestDate, Date expirationDate, Integer quantityInStock,
                Integer minimumQuantity, Integer pricePerQuantity, String units) {
        this.itemName = itemName;
        this.harvestDate = harvestDate;
        this.expirationDate = expirationDate;
        this.quantityInStock = quantityInStock;
        this.minimumQuantity = minimumQuantity;
        this.pricePerQuantity = pricePerQuantity;
        this.units = units;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Date getHarvestDate() {
        return harvestDate;
    }

    public void setHarvestDate(Date harvestDate) {
        this.harvestDate = harvestDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Integer getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public Integer getMinimumQuantity() {
        return minimumQuantity;
    }

    public void setMinimumQuantity(Integer minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }

    public Integer getPricePerQuantity() {
        return pricePerQuantity;
    }

    public void setPricePerQuantity(Integer pricePerQuantity) {
        this.pricePerQuantity = pricePerQuantity;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }
}
