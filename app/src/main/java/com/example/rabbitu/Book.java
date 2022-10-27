package com.example.rabbitu;

import java.io.File;
import java.util.UUID;

public class Book {
    private UUID mID;
    private String mTitle;
    private File file;
    private int image;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public Book(String title, File file,int image){
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


    public File getFile() {
        return file;
    }

}
