package com.freshall.freshall.Model;

import java.util.Date;

public class Post {
    String postText;
    Item postItem;
    User postUser;
    Date postTime;

    public Post(String postText, Item postItem, User postUser, Date postTime) {
        this.postText = postText;
        this.postItem = postItem;
        this.postUser = postUser;
        this.postTime = postTime;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public Item getPostItem() {
        return postItem;
    }

    public void setPostItem(Item postItem) {
        this.postItem = postItem;
    }

    public User getPostUser() {
        return postUser;
    }

    public void setPostUser(User postUser) {
        this.postUser = postUser;
    }

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    //methods
    void deletePost(){}
}
