package com.example.rabbitu;

public class Music {

    private String musicID;
    private String musicName;
    private String musicAudio;

    //Empty constructor
    public Music(){

    }

    //Overloaded constructor
    public Music(String musicID, String musicName, String musicAudio){
        this.musicID = musicID;
        this.musicName = musicName;
        this.musicAudio = musicAudio;
    }

    //Getters
    public String getMusicID(){return musicID;}
    public String getMusicName(){return musicName;}
    public String getMusicAudio(){return musicAudio;}

    //Setters
    public void setMusicID(String musicID){this.musicID = musicID;}
    public void setMusicName(String musicName){this.musicName = musicName;}
    public void setMusicAudio(String musicAudio){this.musicAudio = musicAudio;}
}
