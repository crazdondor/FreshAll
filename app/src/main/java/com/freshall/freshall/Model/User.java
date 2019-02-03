package com.freshall.freshall.Model;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    // fields
    String fullName;
    String email;
    String phone;

    // constructors
    public User() {
        this.fullName = "First Last";
        this.email = "firstlast";
        this.phone = "5551231234";
    }

    public User(String fullName, String email, String phone) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
    }

    // getters and setters
    public String getFullName() {
        return fullName;
    }

    public void setFirstName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    //methods
    void makePost(String postText){}

    void addFriend(User potentialFriend){}

    void removeFriend(User exFriend){}

    void createChat(User chatFriend){}

    @Override
    public String toString() {
        return fullName;
    }

}
