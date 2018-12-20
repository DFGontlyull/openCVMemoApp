package com.example.jeon.opencvmemoapp;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Item implements Serializable {
    String ImagePath;
    String title;
    String date;
    String content;

    public String getContent() {
        return this.content;
    }

    String getImagePath() {
        return this.ImagePath;
    }
    String getTitle() {
        return this.title;
    }
    String getDate() {
        return this.date;
    }



    Item(String image, String title, String content) {
        this.ImagePath = image;
        this.title = title;
        this.content = content;
    }
}