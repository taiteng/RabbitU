package com.example.rabbitu;

import android.net.Uri;

public class PdfClass {
    public String name,url,imageUrl,author, coin;

    public PdfClass(){}

    public PdfClass(String name, String url, String imageUrl, String author, String coin) {
        this.name = name;
        this.url = url;
        this.imageUrl = imageUrl;
        this.author = author;
        this.coin = coin;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
