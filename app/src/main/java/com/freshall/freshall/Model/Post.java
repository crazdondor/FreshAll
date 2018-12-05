package com.freshall.freshall.Model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by angelarae on 10/28/18.
 */

public class Post implements Serializable {
    private String title;
    private String description;
    private String seller;
    private String location;
    private String quantity;
    private String quantityType;
    private String pricePerQuantity;
    private Date harvestDate;
    private Date expirationDate;
    private int postPhoto;

    //DVC
    public Post() {
        this.title = "title";
        this.description = "description";
        this.location = "city, province, country";
        this.quantity = "0";
        this.quantityType = "each";
        this.pricePerQuantity = "negotiable";
        this.harvestDate = new Date(2000, 1, 1);
        this.expirationDate = new Date(2020, 1, 1);
    }

    //EVC
    public Post(String title, String description, String location, String quantity, String quantityType, String pricePerQuantity, Date harvestDate, Date expirationDate, int postPhoto) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.quantity = quantity;
        this.quantityType = quantityType;
        this.postPhoto = postPhoto;
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
    
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getQuantity() {
        return quantity;
    }


    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(String quantityType) {
        this.quantityType = quantityType;
    }

    public String getPricePerQuantity() {
        return pricePerQuantity;
    }

    public void setPricePerQuantity(String pricePerQuantity) {
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

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getSeller() {
        return seller;
    }

    public int getPostPhoto() {
        return postPhoto;
    }

    public void setPostPhoto(int postPhoto) {
        this.postPhoto = postPhoto;
    }
}