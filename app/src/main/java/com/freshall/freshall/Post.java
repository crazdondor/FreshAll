package com.freshall.freshall;

/**
 * Created by angelarae on 10/28/18.
 */

public class Post {
    String title;
    String description;
    String sellerName;

    //DVC
    Post() {
        this.title = "title";
        this.description = "description";
        this.sellerName = "seller_name";
    }

    //EVC
    Post(String title, String description, String sellerName) {
        this.title = title;
        this.description = description;
        this.sellerName = sellerName;
    }

    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getSellerName() {return sellerName;}
}
