package com.example.rabbitu;

import java.io.File;
import java.util.UUID;

public class Book {
    private UUID mID;
    private String mTitle;
    private String file;
    private int image;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public Book(String title, String file,int image){
        mID=UUID.randomUUID();
        mTitle=title;
        this.file=file;
        this.image=image;
    }

    public UUID getID() {
        return mID;
    }

    public String getTitle() {
        return mTitle;
    }


    public String getFile() {
        return file;
    }

}
