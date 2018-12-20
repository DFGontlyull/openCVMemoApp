package com.example.jeon.opencvmemoapp;

import java.io.Serializable;

public class ImageDTO implements Serializable {
    public ImageDTO() {
    }

    private String imageUrl;
    private String title;
    private String Contents;
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    int position;


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return Contents;
    }

    public void setContents(String contents) {
        Contents = contents;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    public ImageDTO(String imageUrl, String title, String Contents, String uid) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.Contents= Contents;
        this.uid = uid;

    }
}
