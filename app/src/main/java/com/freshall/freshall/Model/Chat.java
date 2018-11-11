package com.freshall.freshall.Model;

import java.util.ArrayList;

public class Chat {
    // fields
    User user1;
    User user2;
    ArrayList<Message> messages = new ArrayList<Message>();

    // constructor
    public Chat(User user1, User user2, ArrayList<Message> messages) {
        this.user1 = user1;
        this.user2 = user2;
        this.messages = messages;
    }

    // getters and setters
    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    void sendMessage(Message message){}

    void deleteChat(){}
}
