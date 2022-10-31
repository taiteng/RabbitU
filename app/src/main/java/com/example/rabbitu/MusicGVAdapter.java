package com.example.rabbitu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MusicGVAdapter extends ArrayAdapter<Music> {

    //Initialization of variable for equipped music ID
    private String equippedMusicID;

    //Constructor
    public MusicGVAdapter(@NonNull Context context, ArrayList<Music> musicArrayList, String equippedMusicID){
        super(context, 0, musicArrayList);
        this.equippedMusicID = equippedMusicID;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

        //Display the card view
        View listitemView = convertView;
        if (listitemView == null){
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.settings_music_card, parent, false);
        }

        //Retrieve music from arraylist
        Music music = getItem(position);

        //Initialization of elements in the card view
        TextView musicName = listitemView.findViewById(R.id.musicName);
        RelativeLayout musicCard = listitemView.findViewById(R.id.musicCard);

        //Display "(Equipped)" if the music is equipped
        if (music.getMusicID().equals(equippedMusicID)){

            //Display the music name
            musicName.setText(music.getMusicName() + "(EQUIPPED)");

            //Set the card background colour
            musicCard.setBackgroundResource(R.color.purple_200);
        }

        else{
            //Display the music name
            musicName.setText(music.getMusicName());

            //Set the card background colour
            musicCard.setBackgroundResource(R.color.purple_700);
        }

        return listitemView;
    }
}
