package com.freshall.freshall.Model;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by Quin on 4/3/19.
 *
 *  conversationArrayList []
 *      - conversationIDs: {String}
 *          - lastMessageText: {String}
 *          - lastMessageTime: {Timestamp}
 *          - memberIDs []
 *              - userId: {String}
 *          - chatMessages []
 *              - messageIDs: {String}
 *                  - content: {String}
 *                  - createdAt: {Timestamp}
 *                  - senderIDs: {String}
 */

public class Conversation {
    private String conversationID;
    private ArrayList<String> memberIDs;
    private ArrayList<ChatMessage> chatMessages;
    private String lastMessageText;
    private Timestamp lastMessageTimestamp;

    public Conversation(String conversationID, ArrayList<String> memberIDs, ArrayList<ChatMessage> chatMessages, String lastMessageText, Timestamp lastMessageTime) {
        this.conversationID = conversationID;
        this.memberIDs = memberIDs;
        this.chatMessages = chatMessages;
        this.lastMessageText = lastMessageText;
        this.lastMessageTimestamp = lastMessageTime;
    }

    public Conversation() {
        this.conversationID = "conversationID";
        this.memberIDs = new ArrayList<>();
        this.chatMessages = new ArrayList<>();
        this.lastMessageText = "lastMessageText";
        this.lastMessageTimestamp = new Timestamp(System.currentTimeMillis());
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

    public ArrayList<ChatMessage> getMessages() {
        return chatMessages;
    }

    public void setMessages(ArrayList<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    public void setLastMessageText(String lastMessageText) {
        this.lastMessageText = lastMessageText;
    }

    public Timestamp getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTime(Timestamp lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }
}
