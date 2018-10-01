package com.freshall.freshall.Model;

public class User {

    // fields
    String firstName;
    String lastName;
    String email;
    String phone;

    // constructors
    public User() {
        this.firstName = "First";
        this.lastName = "Last";
        this.email = "firstlast";
        this.phone = "5551231234";
    }

    public User(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    // getters and setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

}
