package com.freshall.freshall.Model;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Created by Quin on 3/28/19.
 */

public class ChatMessage {

    private String senderID;
    private String fullName;
    private String content;
    private Timestamp createdAt;

    public ChatMessage() {
        senderID = "senderID";
        fullName = "First Last";
        content = "BLANK CONTENT";
        createdAt = new Timestamp(System.currentTimeMillis());
    }

    public ChatMessage(String senderID, String fullName, String content, Timestamp createdAt) {
        this.senderID = senderID;
        this.fullName = fullName;
        this.content = content;
        this.createdAt = createdAt;
    }

    public String getSenderID() { return senderID; }

    public void setSenderID(String senderID) { this.senderID = senderID; }

    public String getFullName() { return fullName; }

    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public Timestamp getCreatedAt() { return createdAt; }

    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "From ID: " + senderID + "\n" + content + "\n" + createdAt;
    }
}

