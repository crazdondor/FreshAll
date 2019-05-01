package com.freshall.freshall.Model;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Quin on 4/3/19.
 *
 */

public class ConversationPreview {
    private String lastMessageText;
    private long lastMessageTimestamp;
    private String recipientName;
    private String recipientID;
    private String conversationID;

    public ConversationPreview(String lastMessageText, long lastMessageTime, String recipientName, String recipientID, String conversationID) {
        this.lastMessageText = lastMessageText;
        this.lastMessageTimestamp = lastMessageTime;
        this.recipientName = recipientName;
        this.recipientID = recipientID;
        this.conversationID = conversationID;
    }

    public ConversationPreview() {
        this.lastMessageText = "No messages yet";
        this.lastMessageTimestamp = System.currentTimeMillis();
        this.recipientName = "First Last";
        this.recipientID = "recipientID";
        this.conversationID = "conversationID";
    }


    public String getLastMessageText() {
        return lastMessageText;
    }

    public void setLastMessageText(String lastMessageText) {
        this.lastMessageText = lastMessageText;
    }

    public long getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp(long lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientID() {
        return recipientID;
    }

    public void setRecipientID(String recipientID) {
        this.recipientID = recipientID;
    }

    public String getConversationID() {
        return conversationID;
    }

    public void setConversationID(String conversationID) {
        this.conversationID = conversationID;
    }
}

