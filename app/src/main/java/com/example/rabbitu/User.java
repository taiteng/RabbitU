package com.example.rabbitu;

public class User {

    public String fullName, phoneNumber, email, equippedMusicID, equippedMusicAudio;
    public int coins;
    public boolean isMuteMusic;

    public User() {

    }

    public User(String fullName, String phoneNumber, String email, String equippedMusicID, String equippedMusicAudio, int coins, boolean isMuteMusic){
            this.fullName = fullName ;
            this.phoneNumber = phoneNumber ;
            this.email = email ;
            this.equippedMusicID = equippedMusicID;
            this.equippedMusicAudio = equippedMusicAudio;
            this.coins = coins;
            this.isMuteMusic = isMuteMusic;
    }


}
