package com.freshall.freshall.Model;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {


    private String fullName;
    private String userID;
    private String email;
    private String phone;
    private ArrayList<Conversation> conversationArrayList;


    // constructors
    public User() {
        this.fullName = "First Last";
        this.userID = "userID";
        this.email = "firstlast@email.com";
        this.phone = "5551231234";
    }

    public User(String fullName, String userID, String email, String phone) {
        this.fullName = fullName;
        this.userID = userID;
        this.email = email;
        this.phone = phone;
    }


    public String getUserID() { return userID; }

    public void setUserID(String userID) { this.userID = userID; }

    public String getFullName() { return fullName; }

    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }



    @Override
    public String toString() { return "Full Name: " + fullName + " \n UserID: " + userID; }
}