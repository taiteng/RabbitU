package com.example.rabbitu;

import java.util.ArrayList;
import java.util.List;

public class User {

    public String fullName, phoneNumber, email, equippedMusicID, equippedMusicAudio;
    public int coins;
    public boolean isMuteMusic;
    List<String> bookList;

    public User() {

    }

    public User(String fullName, String phoneNumber, String email, String equippedMusicID, String equippedMusicAudio, int coins, boolean isMuteMusic, List<String> bookList){
            this.fullName = fullName ;
            this.phoneNumber = phoneNumber ;
            this.email = email ;
            this.equippedMusicID = equippedMusicID;
            this.equippedMusicAudio = equippedMusicAudio;
            this.coins = coins;
            this.isMuteMusic = isMuteMusic;
            this.bookList= bookList;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEquippedMusicID() {
        return equippedMusicID;
    }

    public void setEquippedMusicID(String equippedMusicID) {
        this.equippedMusicID = equippedMusicID;
    }

    public String getEquippedMusicAudio() {
        return equippedMusicAudio;
    }

    public void setEquippedMusicAudio(String equippedMusicAudio) {
        this.equippedMusicAudio = equippedMusicAudio;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public boolean isMuteMusic() {
        return isMuteMusic;
    }

    public void setMuteMusic(boolean muteMusic) {
        isMuteMusic = muteMusic;
    }

    public List<String> getBookList() {
        return bookList;
    }

    public void setBookList(List<String> bookList) {
        this.bookList = bookList;
    }
}
