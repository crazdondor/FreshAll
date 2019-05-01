package com.freshall.freshall.Model;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Quin on 4/3/19.
 *
 *  conversationArrayList []
 *      - conversationIDs: {String}
 *          - lastMessageText: {String}
 *          - lastMessageTime: {long}
 *          - memberIDs []
 *              - userId: {String}
 *          - chatMessages []
 *              - messageIDs: {String}
 *                  - content: {String}
 *                  - createdAt: {long}
 */

public class Conversation {
    private String conversationID;
    private ArrayList<String> memberIDs;
    private ArrayList<ChatMessage> chatMessages;
    private String lastMessageText;
    private long lastMessageTimestamp;

    public Conversation(ArrayList<String> memberIDs, ArrayList<ChatMessage> chatMessages, String lastMessageText, long lastMessageTime) {
        this.lastMessageText = lastMessageText;
        this.lastMessageTimestamp = lastMessageTime;
        this.conversationID = UUID.randomUUID().toString();
        this.memberIDs = memberIDs;
        this.chatMessages = chatMessages;
    }

    public Conversation() {
        this.conversationID = UUID.randomUUID().toString();
        this.memberIDs = new ArrayList<>();
        this.chatMessages = new ArrayList<>();
        this.lastMessageText = "No messages yet";
        this.lastMessageTimestamp = System.currentTimeMillis();
    }

    public String getConversationID() {
        return conversationID;
    }

    public void setConversationID(String conversationID) {
        this.conversationID = conversationID;
    }

    public ArrayList<String> getMemberIDs() {
        return memberIDs;
    }

    public void setMemberIDs(ArrayList<String> memberIDs) {
        this.memberIDs = memberIDs;
    }

    public String getLastMessageText() {
        return lastMessageText;
    }

    public ArrayList<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public void setChatMessages(ArrayList<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    public void setLastMessageText(String lastMessageText) {
        this.lastMessageText = lastMessageText;
    }

    public long getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTime(long lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }
}
