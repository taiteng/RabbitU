package com.example.rabbitu;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminMusic extends AppCompatActivity {

    final String firebaseURL = "https://rabbitu-ae295-default-rtdb.firebaseio.com/";

    RecyclerView  mRecyclerView;
    AdminMusicAdapter mAdminMusicAdapter;
    ExtendedFloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_music);

        mRecyclerView = findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Music> options =
                new FirebaseRecyclerOptions.Builder<Music>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("MusicStorage"), Music.class)
                        .build();

        mAdminMusicAdapter = new AdminMusicAdapter(options);
        mRecyclerView.setAdapter(mAdminMusicAdapter);

        mFloatingActionButton = findViewById(R.id.add_music);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show dialog
                addMusic();
            }
        });
    }

    /**
     * Shows a dialog box with details of the chronometer
     */
    public void addMusic(){
        //Create the alert dialog box
        AlertDialog dialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AdminMusic.this);
        LayoutInflater inflater = this.getLayoutInflater();
        dialogBuilder.setView(inflater.inflate(R.layout.add_music, null));
        dialog = dialogBuilder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        //Display the alert dialog box
        dialog.show();

        //Initialization of the elements in add_music.xml
        EditText musicName = dialog.findViewById(R.id.editMusicName);
        EditText musicID = dialog.findViewById(R.id.editMusicID);
        EditText musicAudio = dialog.findViewById(R.id.editMusicAudio);
        Button submitButton = dialog.findViewById(R.id.submitButton);
        ImageButton exitButton = dialog.findViewById(R.id.exitIcon);

        //Insert into database
        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String music_name = musicName.getText().toString();
                String music_id = musicID.getText().toString();
                String music_audio = musicAudio.getText().toString();

                if (TextUtils.isEmpty(music_name)){
                    musicName.setError("Name cannot be empty");
                    musicName.requestFocus();
                }
                else if(TextUtils.isEmpty(music_id)){
                    musicID.setError("ID cannot be empty");
                    musicID.requestFocus();
                }
                else if(TextUtils.isEmpty(music_audio)){
                    musicAudio.setError("Audio link cannot be empty");
                    musicAudio.requestFocus();
                }
                else{
                    Music music = new Music(music_id, music_name, music_audio);

                    FirebaseDatabase.getInstance().getReference("MusicStorage")
                            .child(music_id).setValue(music).addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(AdminMusic.this,"Music Added successfully",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(AdminMusic.this,"Fail to add! Try again!",Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }
                    });
                }
            }
        });

        //Closes the alert dialog box
        exitButton.setOnClickListener(v -> {
            dialog.dismiss();
        });
    }

    //Back button
    public void onBackClick(View View){
        Intent intent = new Intent(AdminMusic.this,Admin.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdminMusicAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdminMusicAdapter.stopListening();
    }
}
