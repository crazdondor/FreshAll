package com.freshall.freshall.Model;

public class Friend {
    // fields
    User friend;

    // constructor
    public Friend(User friend) {
        this.friend = friend;
    }

    // getters and setters
    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    // methods
    void deleteFriend(User friend){}
}
