package com.freshall.freshall.Model;

import java.util.ArrayList;

public class Feed {
    // fields
    ArrayList<Post> posts = new ArrayList<Post>();
    ArrayList<Friend> friendArrayList = new ArrayList<Friend>();

    public Feed(ArrayList<Friend> friendArrayList) {
        this.posts = getPosts(friendArrayList);
        this.friendArrayList = friendArrayList;
    }

    // getters and setters
    public ArrayList<Post> getPosts(ArrayList<Friend> friendArrayList) {
        // // get recent posts from friends
        // for friend in friendList
        // posts.append recent posts
        // sort by date
        return posts;
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
    }

    public ArrayList<Friend> getFriendArrayList() {
        return friendArrayList;
    }

    public void setFriendArrayList(ArrayList<Friend> friendArrayList) {
        this.friendArrayList = friendArrayList;
    }

    // methods
    void diplayPosts(ArrayList<Post> posts){}


}
