package com.freshall.freshall.Model;

import java.util.ArrayList;

public class Chat {
    // fields
    User myPhone;
    User theirPhone;
    ArrayList<ChatMessage> messages = new ArrayList<ChatMessage>();

    // constructor
    public Chat(User myPhone, User theirPhone, ArrayList<ChatMessage> messages) {
        this.myPhone = myPhone;
        this.theirPhone = theirPhone;
        this.messages = messages;
    }

    // getters and setters
    public User getMyPhone() {
        return myPhone;
    }

    public void setMyPhone(User myPhone) {
        this.myPhone = myPhone;
    }

    public User getTheirPhone() {
        return theirPhone;
    }

    public void setTheirPhone(User theirPhone) {
        this.theirPhone = theirPhone;
    }

    public ArrayList<ChatMessage> getMessages() {
        return messages;
    }

}
