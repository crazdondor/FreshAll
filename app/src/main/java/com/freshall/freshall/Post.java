package com.freshall.freshall;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by angelarae on 10/28/18.
 */

public class Post implements Serializable {
    String title;
    String description;
//    User seller;
    String sellerName;
    String location;
    Double quantity;
    String quantityType;
    Double pricePerQuantity;
    Date harvestDate;
    Date expirationDate;

    //DVC
    Post() {
        this.title = "title";
        this.description = "description";
        this.sellerName = "seller_name";
        this.location = "city, province, country";
        this.quantity = 0.0;
        this.quantityType = "each";
        this.pricePerQuantity = 0.0;
        this.harvestDate = new Date(2000, 1, 1);
        this.expirationDate = new Date(2020, 1, 1);
    }

    //EVC
    Post(String title, String description, String sellerName, String location, Double quantity, String quantityType, Double pricePerQuantity, Date harvestDate, Date expirationDate) {
        this.title = title;
        this.description = description;
        this.sellerName = sellerName;
        this.location = location;
        this.quantity = quantity;
        this.quantityType = quantityType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getSellerName() {return sellerName;}

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getQuantity() {
        return quantity;
    }


    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(String quantityType) {
        this.quantityType = quantityType;
    }

    public Double getPricePerQuantity() {
        return pricePerQuantity;
    }

    public void setPricePerQuantity(Double pricePerQuantity) {
        this.pricePerQuantity = pricePerQuantity;
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
}
