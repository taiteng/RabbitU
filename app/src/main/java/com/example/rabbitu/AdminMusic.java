package com.example.rabbitu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
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
                Intent intent = new Intent(AdminMusic.this,AdminAddMusic.class);
                startActivity(intent);
            }
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
