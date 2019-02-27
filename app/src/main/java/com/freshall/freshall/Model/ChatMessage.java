package com.freshall.freshall.Model;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Created by Quin on 10/28/18.
 */

public class ChatMessage {

    private int profilePicture;
    private String author;
    private String content;
    private Date dateTime;

    public ChatMessage() {
        author = "BLANK AUTHOR";
        content = "BLANK CONTENT";
    }

    public ChatMessage(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(int profilePicture) {
        this.profilePicture = profilePicture;
    }

    @Override
    public String toString() {
        return "From: " + author + "\n" + content;
    }
}
