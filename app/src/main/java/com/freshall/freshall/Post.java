package com.freshall.freshall;

/**
 * Created by angelarae on 10/28/18.
 */

public class Post {
    String title;
    String description;

    //DVC
    Post() {
        this.title = "title";
        this.description = "description";
    }

    //EVC
    Post(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
