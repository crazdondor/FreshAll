package com.freshall.freshall.Model;

import java.util.Date;

public class Message {
    // fields
    User sender;
    User recipient;
    String messageText;
    Date sentTime;

    // constructor
    public Message(User sender, User recipient, String messageText, Date sentTime) {
        this.sender = sender;
        this.recipient = recipient;
        this.messageText = messageText;
        this.sentTime = sentTime;
    }

    // getters and setters
    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Date getSentTime() {
        return sentTime;
    }

    public void setSentTime(Date sentTime) {
        this.sentTime = sentTime;
    }

    // methods
    void deleteChat(){}
}
