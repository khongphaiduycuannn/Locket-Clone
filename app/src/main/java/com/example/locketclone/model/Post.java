package com.example.locketclone.model;

import java.util.Date;

public class Post {

    private String content;

    private Date createdAt;

    private String image;

    private String postId;

    private String userId;

    public Post() {
    }

    public Post(String content, Date createdAt, String image, String userId) {
        this.content = content;
        this.createdAt = createdAt;
        this.image = image;
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
