package com.example.jeon.opencvmemoapp;

public class Item {
    int image;
    String title;
    String date;

    int getImage() {
        return this.image;
    }
    String getTitle() {
        return this.title;
    }
    String getDate() {
        return this.date;
    }



    Item(int image, String title, String date) {
        this.image = image;
        this.title = title;
        this.date = date;
    }
}