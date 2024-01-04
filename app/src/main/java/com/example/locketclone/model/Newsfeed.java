package com.example.locketclone.model;

import java.util.ArrayList;

public class Newsfeed {

    private String newsfeedId;

    private String userId;

    private ArrayList<String> posts = new ArrayList<>();

    public Newsfeed() {
    }

    public Newsfeed(String newsfeedId, String userId, ArrayList<String> posts) {
        this.newsfeedId = newsfeedId;
        this.userId = userId;
        this.posts = posts;
    }

    public Newsfeed(String userId) {
        this.userId = userId;
    }

    public String getNewsfeedId() {
        return newsfeedId;
    }

    public void setNewsfeedId(String newsfeedId) {
        this.newsfeedId = newsfeedId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<String> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<String> posts) {
        this.posts = posts;
    }
}
