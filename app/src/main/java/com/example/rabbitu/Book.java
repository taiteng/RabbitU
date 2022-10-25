package com.example.rabbitu;

import java.io.File;
import java.util.UUID;

public class Book {
    private UUID mID;
    private String mTitle;
    private File file;

    public Book(String title,File file){
        mID=UUID.randomUUID();
        mTitle=title;
        this.file=file;
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
