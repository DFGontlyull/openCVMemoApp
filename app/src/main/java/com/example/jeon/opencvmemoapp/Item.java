package com.example.jeon.opencvmemoapp;

public class Item {
    int ImagePath;
    String title;
    String date;

    int getImagePath() {
        return this.ImagePath;
    }
    String getTitle() {
        return this.title;
    }
    String getDate() {
        return this.date;
    }



    Item(int image, String title, String date) {
        this.ImagePath = image;
        this.title = title;
        this.date = date;
    }
}